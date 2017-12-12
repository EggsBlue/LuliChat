package com.dd.setup;

import com.dd.socket.SocketServer;
import org.nutz.ioc.impl.PropertiesProxy;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.NutConfig;
import org.nutz.mvc.Setup;
import org.tio.core.Aio;
import org.tio.server.ServerGroupContext;
import org.tio.websocket.server.WsServerStarter;
import org.tio.websocket.server.handler.IWsMsgHandler;

import java.io.IOException;

public class MySetup implements Setup{
    Log log = Logs.get();

    @Override
    public void init(NutConfig nc) {
//        Dao dao = nc.getIoc().get(Dao.class, "dao");
//        System.out.println(dao.count("flock",null));
        SocketServer server = nc.getIoc().get(SocketServer.class, "socketServer");
        server.start();
    }

    @Override
    public void destroy(NutConfig nc) {
    }
}
