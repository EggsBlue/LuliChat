package com.dd.socket.handler;

import com.dd.dao.ChatMessageDao;
import com.dd.dao.UserDao;
import com.dd.entity.User;
import com.dd.entity.msg.ChatMessage;
import com.dd.entity.msg.ReceiveMessage;
import com.dd.entity.msg.SendMessage;
import com.dd.socket.MsgHandlerInterface;
import com.dd.socket.SocketServer;
import com.dd.socket.Type;
import com.dd.utils.SocketMsgUtils;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.tio.core.Aio;
import org.tio.core.ChannelContext;
import org.tio.core.ChannelContextFilter;
import org.tio.core.maintain.Users;
import org.tio.utils.lock.ObjWithLock;
import org.tio.utils.lock.SetWithLock;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *处理群组消息
 *@Author: 蛋蛋i
 *@Time:2017/11/28 10:22
 */
@IocBean
public class GroupMessageHandler implements MsgHandlerInterface {
    Log log = Logs.get();
    @Inject("chatMessageDao")
    private ChatMessageDao chatMessageDao;

    @Inject
    private UserDao userDao;

    @Override
    public Object handler(String text, ChannelContext context) {
        log.debug(text);

        ReceiveMessage msgData = Json.fromJson(ReceiveMessage.class,text);
        Map<String, String> to = msgData.getTo();
        Map<String, String> mine = msgData.getMine();
        String toName = to.get("name");

        //构建转发消息实体
        SendMessage sendMessage = new SendMessage();
        sendMessage.setUsername(mine.get("username"));
        sendMessage.setAvatar(mine.get("avatar"));
        sendMessage.setId(to.get("id"));
        sendMessage.setType("group");
        sendMessage.setContent(mine.get("content"));
        sendMessage.setFromid(null);
//            		if(to.get("username").equals(username)){
//            			sendMessage.setMine(true);
//            		}else{
//            			sendMessage.setMine(false);
//            		}
//            		sendMessage.setFromid(mine.get("id"));
        sendMessage.setTimestamp(System.currentTimeMillis());
        String strJson =Json.toJson(sendMessage);
//            		String strJson = sendMessage.toJson();
        System.out.println(strJson);

        //发送消息,记得转成json
        try {
            Aio.sendToGroup(SocketServer.groupContext, to.get("id"), SocketMsgUtils.madeWsResponse(Type.GROUP_MSG_REQ, strJson), new ChannelContextFilter() {
                @Override
                public boolean filter(ChannelContext channelContext) {
                    if(channelContext == context){
                        return false;
                    }else{
                        return true;
                    }
                }
            });
//            			Session toSession = socket.getSession(toName);
//            			toSession.getBasicRemote().sendText(strJson);
//            			nutSocket.sendJson(toName, sendMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            //存储消息,包含离线消息哦
            ChatMessage cm = new ChatMessage();
            cm.setAvatar(mine.get("avatar"));
            cm.setContent(mine.get("content"));
            cm.setFrom(Integer.valueOf(mine.get("id")));
            cm.setTimestamp(String.valueOf(System.currentTimeMillis()));
            cm.setToid(Integer.valueOf(to.get("id")));
            cm.setType(2);	//2为群组消息
            cm.setUnreadpoint(1);	//0.已读,1.未读
            cm.setUsername(mine.get("username"));
            String unreadNumbers = "";
//            List<String> list = nutSocket.getOnlines();  //旧时Socket方式获取在线连接
            Users users1 = SocketServer.groupContext.users;
            ObjWithLock<Map<String, SetWithLock<ChannelContext>>> map = users1.getMap();
            Map<String, SetWithLock<ChannelContext>> obj = map.getObj();
//            for (String s : obj.keySet()) {
//                log.debug(s);
//            }
            int groupId = Integer.valueOf(to.get("id"));
            List<User> users = userDao.getMembers(groupId);

            Set<String> keys = obj.keySet();
            for(int i=0; i<users.size(); i++){//过滤在线用户
                if(keys.contains(String.valueOf(users.get(i).getId()))){
                    users.remove(i);
                }
            }

            for(User u : users){//设置未在线的人
                unreadNumbers+=u.getId()+",";
            }
            if(unreadNumbers.length()>0){
                unreadNumbers = unreadNumbers.substring(0, unreadNumbers.length()-1);
            }
            cm.setUnreadnumbers(unreadNumbers);
            System.out.println("unreadNumbers:"+unreadNumbers);
            chatMessageDao.addChatMsg(cm);
        }

        return null;
    }


}
