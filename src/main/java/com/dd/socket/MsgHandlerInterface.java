package com.dd.socket;

import org.tio.core.ChannelContext;
/**
 * 处理消息的抽象接口,俺也玩点高级点的
 *@Author: 蛋蛋i
 *@Time:2017/11/28 10:24
 */
public interface MsgHandlerInterface {
    Object handler(String text, ChannelContext context);
}
