package com.dd.socket.handler;

import com.dd.socket.MsgHandlerInterface;
import org.nutz.ioc.loader.annotation.IocBean;
import org.tio.core.ChannelContext;

/**
 * 加好友消息
 * @Author: 蛋蛋i
 * @Time:2017/11/28 10:47
 */
@IocBean
public class DelFriendMessageHandler implements MsgHandlerInterface {
    @Override
    public Object handler(String text, ChannelContext context) {

        return null;
    }
}
