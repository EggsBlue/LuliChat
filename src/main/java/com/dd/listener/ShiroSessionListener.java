package com.dd.listener;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;
import org.nutz.lang.util.NutMap;

/**
 * 监听Session生命周期
 *@Author: 蛋蛋 [TopCoderMyDream@gmail.com,https://github.com/TopCoderMyDream]
 *@Time:2017/12/19 9:54
 */
public class ShiroSessionListener implements SessionListener {
    private static NutMap sessions = new NutMap();

    /**
     * 创建
     * @param session
     */
    @Override
    public void onStart(Session session) {
        sessions.setv(session.getId().toString(),session);
    }

    /**
     * 会话停止
     * @param session
     */
    @Override
    public void onStop(Session session) {
        sessions.remove(session.getId().toString());
    }

    /**
     * 会话过期
     * @param session
     */
    @Override
    public void onExpiration(Session session) {
        sessions.remove(session.getId().toString());
    }

    public static Session get(String sid){
        return (Session)sessions.get(sid);
    }


}
