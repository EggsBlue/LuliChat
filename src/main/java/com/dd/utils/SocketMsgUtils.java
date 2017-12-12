package com.dd.utils;

import com.dd.entity.SocketMsg;
import org.nutz.json.Json;
import org.tio.websocket.common.WsResponse;

/**
 * 处理Socket消息工具类
 */
public class SocketMsgUtils {


    /**
     * 快速生成WsResponse
     * @param action
     * @param body
     * @return
     */
    public static WsResponse madeWsResponse(byte action, Object body){
        return WsResponse.fromText(Json.toJson(new SocketMsg(action,body)),"utf-8");
    }

}
