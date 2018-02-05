package com.dd.controller;

import com.dd.mvc.Response;
import org.apache.shiro.web.session.HttpServletSession;
import org.nutz.ioc.impl.PropertiesProxy;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.util.NutMap;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;

import javax.servlet.http.HttpSession;

@IocBean
@At("/test")
public class TestController {

    @Inject
    private PropertiesProxy  conf;


    @At("/test")
    @Ok("json")
    public Object test(){
        return NutMap.NEW().setv("test","test");
    }


    @At("/test2")
    @Ok("fm:test")
    public Object test2(HttpSession servletSession){
        servletSession.setAttribute("name","Wendal");
        return Response.ok().setv("hello","hello");
    }
}
