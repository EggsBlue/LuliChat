package com.dd.socket.handler;

import com.dd.dao.UserDao;
import com.dd.dao.impl.ChatMessageDaoImpl;
import com.dd.entity.SocketMsg;
import com.dd.entity.msg.ChatMessage;
import com.dd.entity.msg.SendMessage;
import com.dd.socket.MsgHandlerInterface;
import com.dd.socket.Type;
import com.dd.utils.SocketMsgUtils;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.util.NutMap;
import org.tio.core.Tio;
import org.tio.core.ChannelContext;
import org.tio.utils.lock.SetWithLock;

import java.util.List;

/**
 * 历史消息处理
 *
 * @Author: 蛋蛋i
 * @Time:2017/12/5 11:27
 */
@IocBean
public class OldMessageHandler implements MsgHandlerInterface {

    @Inject(value = "userDao")
    private UserDao userDao;

    @Inject
    private ChatMessageDaoImpl chatMessageDao;

    @Override
    public Object handler(String text, ChannelContext context) {
        try {
            SocketMsg msg = Json.fromJson(SocketMsg.class, text);
            Object body = msg.getBody();
            System.out.println(body.toString());
            NutMap nutMap = Json.fromJson(NutMap.class, body.toString());
            if (body != null) {
                //查看是否有离线消息
                List<ChatMessage> list = chatMessageDao.getUnRead(nutMap.getInt("me"), nutMap.getString("name"));
                if (list != null && list.size() > 0) {
                    for (ChatMessage c : list) {
                        if (1 == c.getType()) {    //单聊
                            SendMessage sendMessage = new SendMessage();
                            sendMessage.setUsername(c.getUsername());
                            sendMessage.setAvatar(c.getAvatar());
                            sendMessage.setId(c.getFrom().toString());
                            sendMessage.setType("friend");
                            sendMessage.setContent(c.getContent());
//                    		if(to.get("username").equals(username)){
//                    			sendMessage.setMine(true);
//                    		}else{
//                    			sendMessage.setMine(false);
//                    		}
                            sendMessage.setFromid(c.getFrom().toString());
                            sendMessage.setTimestamp(Long.valueOf(c.getTimestamp()));
                            String strJson = Json.toJson(sendMessage);
                            System.out.println("好友消息:" + strJson);

                            Tio.send(context, SocketMsgUtils.madeWsResponse(Type.OLD_MESSAGE_REQ_MSG,strJson));
//                    		session.getAsyncRemote().sendText(strJson);
//                        try {
//                            session.getBasicRemote().sendText(strJson);
//                        } catch (IOException e) {
//                            // TODO Auto-generated catch block
//                            e.printStackTrace();
//                        }
                        } else if (2 == c.getType()) {    //群聊
                            System.out.println("send group message...");

                            //构建转发消息实体
                            SendMessage sendMessage = new SendMessage();
                            sendMessage.setUsername(c.getUsername());
                            sendMessage.setAvatar(c.getAvatar());
                            sendMessage.setId(c.getToid().toString());
                            sendMessage.setType("group");
                            sendMessage.setContent(c.getContent());
                            sendMessage.setFromid(null);
//                    		if(to.get("username").equals(username)){
//                    			sendMessage.setMine(true);
//                    		}else{
//                    			sendMessage.setMine(false);
//                    		}
//                    		sendMessage.setFromid(mine.get("id"));
                            sendMessage.setTimestamp(Long.valueOf(c.getTimestamp()));

                            String strJson = Json.toJson(sendMessage);
//                    		String strJson = sendMessage.toJson();
                            System.out.println(strJson);
                            Tio.send(context, SocketMsgUtils.madeWsResponse(Type.OLD_MESSAGE_REQ_MSG,strJson));
                            sendOnlineCount(context);
//                    		session.getAsyncRemote().sendText(strJson);
//                        try {
//                            session.getBasicRemote().sendText(strJson);
//                        } catch (IOException e) {
//                            // TODO Auto-generated catch block
//                            e.printStackTrace();
//                        }
                        }
                    }
                }else{
                    //没有未读消息
                    this.sendOnlineCount(context);
                }
            }else{
                //没有未读消息
                this.sendOnlineCount(context);
            }
        } catch (Exception e) {
            e.printStackTrace();
//            Tio.send(context, WsResponse.fromText(Json.toJson(new SocketMsg(Type.FAIL_MESSAGE_RESP,"查询未读消息失败,"+e.getMessage())),"utf-8"));
            Tio.send(context, SocketMsgUtils.madeWsResponse(Type.FAIL_MESSAGE_RESP,"查询未读消息失败,"+e.getMessage()));
        }

        return null;
    }

    /**
     * 通知客户端当前在线人数
     * @param context
     */
    public void sendOnlineCount(ChannelContext context){
        SetWithLock<ChannelContext> allConnectedsChannelContexts = context.getGroupContext().connections;
        Tio.send(context, SocketMsgUtils.madeWsResponse(Type.ONLINECOUNT,Json.toJson(new NutMap().setv("count",allConnectedsChannelContexts.getObj().size()))));
    }

}
