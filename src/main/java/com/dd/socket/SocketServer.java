package com.dd.socket;

import org.nutz.ioc.Ioc;
import org.nutz.ioc.impl.PropertiesProxy;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.Mvcs;
import org.tio.core.Aio;
import org.tio.server.ServerGroupContext;
import org.tio.websocket.server.WsServerStarter;
import org.tio.websocket.server.handler.IWsMsgHandler;

import java.io.IOException;

/**
 * Socket启动器
 *@Author: 蛋蛋 [TopCoderMyDream@gmail.com,https://github.com/TopCoderMyDream]
 *@Time:2017/12/12 13:57
 */
@IocBean(name = "socketServer")
public class SocketServer {

    private static Ioc ioc = null;
    public static ServerGroupContext groupContext = null;

    public void start(){
        IWsMsgHandler accepter = getIoc().get(IWsMsgHandler.class, "accepter");
        PropertiesProxy conf = getIoc().get(PropertiesProxy.class, "conf");
        int port = 5210;
        port = Integer.valueOf( conf.get("tio.port"));
        try {
            WsServerStarter wsServerStarter = new WsServerStarter(port, accepter);
            groupContext= wsServerStarter.getServerGroupContext();
            wsServerStarter.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Ioc getIoc(){
        if(ioc == null){
            ioc = Mvcs.getIoc();
        }else if(ioc == null){
            ioc = Mvcs.ctx().getDefaultIoc();
        }
        return ioc;
    }


}
