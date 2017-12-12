package com.dd.socket;
/**
 * 消息规格说明
 *@Author: 蛋蛋 [TopCoderMyDream@gmail.com,https://github.com/TopCoderMyDream]
 *@Time:2017/12/12 13:57
 */
public interface Type {

    /**
     * 加好友消息请求
     */
    byte REQFRIEND = 1;
    /**
     * 删除好友消息请求
     */
    byte DELFRIEND = 2;
    /**
     * 进入群组消息请求
     */
    byte JOIN_GROUP_REQ = 3;
    /**
     * 退群消息
     */
    byte OUT_GROUP_REQ = 4;
    /**
     * 点对点消息请求
     */
    byte P2P_REQ = 5;
    /**
     * 群聊消息请求
     */
    byte GROUP_MSG_REQ = 6;
       /**
     * 登录请求
     */
    byte LOGIN_REQ_MSG = 7;
    /**
     * 未读消息
     */
    byte OLD_MESSAGE_REQ_MSG = 8;
    /**
     * 失败通知
     */
    byte FAIL_MESSAGE_RESP = 9;

    /**
     * 成功通知
     */
    byte SUCCESS_MESSAGE_RESP = 10;

    /**
     * 加好友成功
     */
    byte REQFRIENDSUCCESS = 11;

}
