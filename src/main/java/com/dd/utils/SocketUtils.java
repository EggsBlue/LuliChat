package com.dd.utils;

import com.dd.socket.SocketServer;
import org.tio.core.Tio;
import org.tio.core.intf.Packet;

/**
 * Socket常用封装
 *@Author: 蛋蛋 [TopCoderMyDream@gmail.com,https://github.com/TopCoderMyDream]
 *@Time:2017/12/7 13:37
 */
public class SocketUtils {


    public static void sendByUserId(String userId, Packet packet){
        Tio.sendToUser(SocketServer.groupContext,userId,packet);
    }

}
