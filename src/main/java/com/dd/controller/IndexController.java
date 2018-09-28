package com.dd.controller;

import com.dd.mvc.Response;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.util.NutMap;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;

@IocBean
@At("/")
public class IndexController {


    @At
    @Ok("fm:/login")
    public Object login(){
        return NutMap.NEW();
    }

    @At
    @Ok("fm:/chat")
    public Object chat(){
        return NutMap.NEW();
    }

    @At
    @Ok("fm:/find")
    public Object find(){
        return Response.ok();
    }

}
