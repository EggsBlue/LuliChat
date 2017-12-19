package com.dd.socket;

import com.dd.listener.ShiroSessionListener;
import com.dd.utils.SocketMsgUtils;
import com.dd.context.SessionContext;
import com.dd.dao.UserDao;
import com.dd.entity.Flock;
import com.dd.entity.SocketMsg;
import com.dd.entity.User;
import com.dd.socket.handler.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.mapl.Mapl;
import org.nutz.mvc.Mvcs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.core.Aio;
import org.tio.core.ChannelContext;
import org.tio.core.ChannelContextFilter;
import org.tio.http.common.HttpRequest;
import org.tio.http.common.HttpResponse;
import org.tio.utils.lock.SetWithLock;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.server.handler.IWsMsgHandler;


import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * 处理socket请求
 *@Author: 蛋蛋 [TopCoderMyDream@gmail.com,https://github.com/TopCoderMyDream]
 *@Time:2017/12/12 13:57
 */
@IocBean(name = "accepter")
public class Accepter implements IWsMsgHandler {
	private static Logger log = LoggerFactory.getLogger(Accepter.class);

	@Inject(value="userDao")
	private UserDao userDao;

	@Inject
	private ReqMessageHandler reqMessageHandler;
	@Inject
	private DelFriendMessageHandler delFriendMessageHandler;
	@Inject
	private JoinGroupMessageHandler joinGroupMessageHandler;
	@Inject
	private OutGroupMessageHandler outGroupMessageHandler;
	@Inject
	private P2PMessageHandler p2PMessageHandler;
	@Inject
	private GroupMessageHandler groupMessageHandler;
	@Inject
	private LoginMessageHandler loginMessageHandler;
	@Inject
	private OldMessageHandler oldMessageHandler;
	private NutMap handlers = new NutMap();
	/**
	 * @param args
	 * @author tanyaowu
	 */
	public static void main(String[] args) {

	}

	/**
	 *
	 * @author tanyaowu
	 */
	public Accepter() {
		handlers.addv(String.valueOf(Type.REQFRIEND),Mvcs.getIoc().get(ReqMessageHandler.class,"reqMessageHandler"));
		handlers.addv(String.valueOf(Type.DELFRIEND),Mvcs.getIoc().get(DelFriendMessageHandler.class,"delFriendMessageHandler"));
		handlers.addv(String.valueOf(Type.JOIN_GROUP_REQ),Mvcs.getIoc().get(JoinGroupMessageHandler.class,"joinGroupMessageHandler"));
		handlers.addv(String.valueOf(Type.OUT_GROUP_REQ),Mvcs.getIoc().get(OutGroupMessageHandler.class,"outGroupMessageHandler"));
		handlers.addv(String.valueOf(Type.P2P_REQ),Mvcs.getIoc().get(P2PMessageHandler.class,"p2PMessageHandler"));
		handlers.addv(String.valueOf(Type.GROUP_MSG_REQ),Mvcs.getIoc().get(GroupMessageHandler.class,"groupMessageHandler"));
		handlers.addv(String.valueOf(Type.LOGIN_REQ_MSG),Mvcs.getIoc().get(LoginMessageHandler.class,"loginMessageHandler"));
		handlers.addv(String.valueOf(Type.OLD_MESSAGE_REQ_MSG),Mvcs.getIoc().get(OldMessageHandler.class,"oldMessageHandler"));
	}

	/**
	 * 握手
	 * @param request
	 * @param httpResponse
	 * @param channelContext
	 * @return
	 * @throws Exception
	 */
	@Override
	public HttpResponse handshake(HttpRequest request, HttpResponse httpResponse, ChannelContext channelContext) throws Exception {
		log.debug("握手信息");
		Map<String, Object[]> params = request.getParams();
		for (String s : params.keySet()) {
			System.out.println(s+"--"+params.get(s).length+"---"+params.get(s)[0]);
		}
		//验证合法性
		String sessionId = request.getParam("sessionId");
		String id = request.getParam("id");
		if(Strings.isBlank(sessionId) || Strings.isBlank(id)){
			log.debug("用户未登录!");
			return null;
		}
		User user = userDao.findbyid(Integer.valueOf(id));
		if(user == null){
			log.debug("用户不存在!");
			return null;
		}

		Session session = ShiroSessionListener.get(sessionId);
		if(session == null){
			log.debug("用户未登录!");
			return null;
		}

		//绑定用户
		channelContext.setAttribute("userName",user.getUsername());
		Aio.bindUser(channelContext,id);
		//绑定群组
		bindGroup(Integer.valueOf(id),channelContext);

		//上线
		userDao.online(Integer.valueOf(id));
		//上线通知
		onLineMsg(user.getUsername(),channelContext);
		return httpResponse;
	}

	/**
	 * 绑定群组
	 * @param id
	 * @param context
	 */
	public void bindGroup(int id,ChannelContext context){
		List<Flock> list = userDao.getFlocks(id);
		if(list!=null && list.size()>0){
			for(Flock f : list){
				Aio.bindGroup(context,f.getId().toString());
			}
		}
	}

	/**
	 * 上线通知
	 */
	public void onLineMsg(String name,ChannelContext context){
		//直接调用 sendToAll方法会导致错误,不知何种原因
//		Aio.sendToAll(context.getGroupContext(), SocketMsgUtils.madeWsResponse(Type.SUCCESS_MESSAGE_RESP,Json.toJson(NutMap.NEW().setv("msg",name+"上线了!"))));
		SetWithLock<ChannelContext> allConnectedsChannelContexts = Aio.getAllConnectedsChannelContexts(context.getGroupContext());
		Aio.sendToSet(context.getGroupContext(), allConnectedsChannelContexts, SocketMsgUtils.madeWsResponse(Type.SUCCESS_MESSAGE_RESP, Json.toJson(NutMap.NEW().setv("msg", name + "上线了!"))), new ChannelContextFilter() {
			@Override
			public boolean filter(ChannelContext channelContext) {
				if(context == channelContext){
					return false;
				}else{
					return true;
				}
			}
		});

//		for (ChannelContext con : allConnectedsChannelContexts.getObj()){
//			Aio.sendToSet(context.getGroupContext(), SocketMsgUtils.madeWsResponse(Type.SUCCESS_MESSAGE_RESP,Json.toJson(NutMap.NEW().setv("msg",name+"上线了!"))));
//		}
	}

	public void offLineMsg(String name,ChannelContext context){
		SetWithLock<ChannelContext> allConnectedsChannelContexts = Aio.getAllConnectedsChannelContexts(context.getGroupContext());
		Aio.sendToSet(context.getGroupContext(), allConnectedsChannelContexts, SocketMsgUtils.madeWsResponse(Type.FAIL_MESSAGE_RESP, Json.toJson(NutMap.NEW().setv("msg", name + "下线了!"))), new ChannelContextFilter() {
			@Override
			public boolean filter(ChannelContext channelContext) {
				if(context == channelContext){
					return false;
				}else{
					return true;
				}
			}
		});
	}

	@Override
	public Object onBytes(WsRequest wsRequest, byte[] bytes, ChannelContext channelContext) throws Exception {
		String ss = new String(bytes, "utf-8");
		log.info("收到byte消息:{},{}", bytes, ss);
		SocketMsg socketMsg = Json.fromJson(SocketMsg.class, ss);
		MsgHandlerInterface msgHandler = (MsgHandlerInterface) handlers.get(socketMsg.getAction());
		if(msgHandler!=null){
			msgHandler.handler(socketMsg.getBody().toString(),channelContext);
		}else{
			return null;
		}

		//		byte[] bs1 = "收到byte消息".getBytes("utf-8");
//		ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
//		buffer.put(bytes);

		return null;
	}

	@Override
	public Object onClose(WsRequest wsRequest, byte[] bytes, ChannelContext channelContext) throws Exception {
		Aio.remove(channelContext, "receive close flag");
		//下线
		String userid = channelContext.getUserid();
		if(!Strings.isBlank(userid)){
			userDao.hide(Integer.valueOf(userid));
		}
		//下线通知
		offLineMsg(String.valueOf(channelContext.getAttribute("userName")),channelContext);
		return null;
	}

	@Override
	public Object onText(WsRequest wsRequest, String text, ChannelContext channelContext) throws Exception {
		String action =(String) Mapl.cell(Json.fromJson(text), "action");
		if(!Strings.isBlank(action) && !"".equalsIgnoreCase(action)){
			MsgHandlerInterface msgHandler = (MsgHandlerInterface) handlers.get(action);
			if(msgHandler != null){
				msgHandler.handler(text,channelContext);
			}else{
				log.debug("非法请求...");
			}
		}else{
			log.debug("非法请求...");
		}
		return null;
	}
}
