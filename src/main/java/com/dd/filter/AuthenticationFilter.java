package com.dd.filter;

import com.dd.entity.User;
import com.dd.mvc.Response;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.integration.shiro.NutShiro;
import org.nutz.ioc.Ioc;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.Mvcs;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class AuthenticationFilter  extends FormAuthenticationFilter {

    private Dao dao;
    private Dao dao() {
        if (null == dao) {
            Ioc ioc = Mvcs.getIoc();
            if (null == ioc)
                ioc = Mvcs.ctx.getDefaultIoc();
            dao = ioc.get(Dao.class);
        }
        return dao;
    }

    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest req,
                                     ServletResponse resp) throws Exception {

        HttpServletRequest req1 = (HttpServletRequest) req;
        HttpSession session = null;
        if(req1 != null)
            session = req1.getSession();
        User user = dao().fetch(User.class, Cnd.where(User.USERNAME, "=", subject.getPrincipal()));
        if(user!=null){
            if(session!=null){
                subject.getSession().setAttribute("me", user.getId());
                subject.getSession().setAttribute("username", user.getUsername());
                subject.getSession().setAttribute("sessionId", session.getId());

                //session.setAttribute("me", user.getId());
//                session.setAttribute("username", user.getUsername());
//                session.setAttribute("sessionId",session.getId());
            }
        }
        NutShiro.rendAjaxResp(req, resp, Response.ok("登陆成功"));
        return false;
    }


    Log log = Logs.get();
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        log.debug("=========登录失败============");
        return super.onLoginFailure(token, e, request, response);
    }
}
