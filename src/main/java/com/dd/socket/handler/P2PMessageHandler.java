package com.dd.socket.handler;

import com.dd.utils.SocketMsgUtils;
import com.dd.dao.ChatMessageDao;
import com.dd.dao.UserDao;
import com.dd.entity.msg.ChatMessage;
import com.dd.entity.msg.ReceiveMessage;
import com.dd.entity.msg.SendMessage;
import com.dd.socket.MsgHandlerInterface;
import com.dd.socket.Type;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.util.NutMap;
import org.tio.core.Tio;
import org.tio.core.ChannelContext;
import org.tio.utils.lock.SetWithLock;

import java.util.Map;

/**
 * 处理点对点消息
 *@Author: 蛋蛋 [TopCoderMyDream@gmail.com,https://github.com/TopCoderMyDream]
 *@Time:2017/12/6 10:20
 */
@IocBean
public class P2PMessageHandler implements MsgHandlerInterface {

    @Inject
    private UserDao userDao;
    @Inject("chatMessageDao")
    private ChatMessageDao chatMessageDao;

    public P2PMessageHandler(){}
    @Override
    public Object handler(String text, ChannelContext context) {
        //解析消息内容
        NutMap msg = Json.fromJson(NutMap.class, text);
        ReceiveMessage msgData = Json.fromJson(ReceiveMessage.class,text);
        Map<String, String> to = msgData.getTo();
        Map<String, String> mine = msgData.getMine();
        String id = to.get("id");
        String type = msg.getString("action");

        //t-io支持多点登录,获取的是一个集合,因为此账号可能存在多个连接哦
        SetWithLock<ChannelContext> contexts = Tio.getChannelContextsByUserid(context.getGroupContext(), id);

        System.out.println("send friend message...");
//        WsHandler handler = nutSocket.getHandler(toName);
        if(contexts!=null && contexts.size() > 0){
            //构建转发消息实体
            SendMessage sendMessage = new SendMessage();
            sendMessage.setUsername(mine.get("username"));
            sendMessage.setAvatar(mine.get("avatar"));
            sendMessage.setId(mine.get("id"));
            sendMessage.setType("friend");
            sendMessage.setContent(mine.get("content"));
//                		if(to.get("username").equals(username)){
//                			sendMessage.setMine(true);
//                		}else{
//                			sendMessage.setMine(false);
//                		}
            sendMessage.setFromid(mine.get("id"));
            sendMessage.setTimestamp(System.currentTimeMillis());
            String strJson =Json.toJson(sendMessage);
            System.out.println(strJson);
//                		String strJson = sendMessage.toJson();
            //发送消息,记得转成json
            try {
//                			Session toSession = socket.getSession(toName);
//                			toSession.getBasicRemote().sendText(strJson);
//                nutSocket.sendJson(toName, sendMessage);

                for (ChannelContext con : contexts.getObj()){
                    Tio.send(con, SocketMsgUtils.madeWsResponse(Type.P2P_REQ,strJson));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }finally{
                //存储消息
                ChatMessage cm = new ChatMessage();
                cm.setAvatar(mine.get("avatar"));
                cm.setContent(mine.get("content"));
                cm.setFrom(Integer.valueOf(mine.get("id")));
                cm.setTimestamp(String.valueOf(System.currentTimeMillis()));
                cm.setToid(Integer.valueOf(to.get("id")));
                cm.setType(1);
                cm.setUnreadpoint(0);	//0.已读,1.未读
                cm.setUsername(mine.get("username"));
                chatMessageDao.addChatMsg(cm);
            }
        }else{
            //对方不在线,存储离线消息
            ChatMessage cm = new ChatMessage();
            cm.setAvatar(mine.get("avatar"));
            cm.setContent(mine.get("content"));
            cm.setFrom(Integer.valueOf(mine.get("id")));
            cm.setTimestamp(String.valueOf(System.currentTimeMillis()));
            cm.setToid(Integer.valueOf(to.get("id")));
            cm.setType(1);
            cm.setUnreadpoint(1);//0.已读,1.未读
            cm.setUsername(mine.get("username"));

            chatMessageDao.addChatMsg(cm);
        }

        return null;
    }
}
