package com.dd.socket.handler;

import com.dd.socket.MsgHandlerInterface;
import org.nutz.ioc.loader.annotation.IocBean;
import org.tio.core.ChannelContext;

/**
 * 处理加好友请求
 *@Author: 蛋蛋i
 *@Time:2017/11/28 10:22
 */
@IocBean
public class ReqMessageHandler implements MsgHandlerInterface {

    @Override
    public Object handler(String text, ChannelContext context) {
        return null;
    }
}
