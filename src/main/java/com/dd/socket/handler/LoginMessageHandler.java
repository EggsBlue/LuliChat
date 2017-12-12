package com.dd.socket.handler;

import com.dd.dao.UserDao;
import com.dd.entity.Flock;
import com.dd.entity.User;
import com.dd.mvc.Response;
import com.dd.socket.MsgHandlerInterface;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.tio.core.Aio;
import org.tio.core.ChannelContext;
import org.tio.websocket.common.WsResponse;

import java.util.List;

/**
 *登录请求
 *@Author: 蛋蛋i
 *@Time:2017/11/28 13:32
 */
@IocBean
public class LoginMessageHandler implements MsgHandlerInterface {
    Log log = Logs.get();

    @Inject(value="userDao")
    private UserDao userDao;

    @Override
    public Object handler(String text, ChannelContext context) {
        User user = Json.fromJson(User.class, text);
        if(user == null || user.getId() == 0 || Strings.isBlank(user.getUsername()) || Strings.isBlank(user.getPwd())){
            log.debug("用户数据有误,无法登录!"+user);
            return null;
        }
        User fetch = userDao.getByNamPwd(user);
        if(fetch == null){
            Aio.send(context,WsResponse.fromText(Json.toJson(Response.fail()),"utf-8"));
        }
        //初始化用户
        Aio.bindUser(context,String.valueOf(user.getId()));
        //初始化该用户群组
        List<Flock> flocks = userDao.getFlocks(user.getId());
        for(Flock fl: flocks){
            Aio.bindGroup(context,fl.getId().toString());
        }

        Aio.send(context,WsResponse.fromText(Json.toJson(Response.ok()),"utf-8"));
        //更新状态
//        userDao.online(user.getId());
        return null;
    }
}
