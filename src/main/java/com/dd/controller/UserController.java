package com.dd.controller;

import com.dd.dao.ChatMessageDao;
import com.dd.dao.UserDao;
import com.dd.entity.*;
import com.dd.mvc.Response;
import com.dd.socket.Type;
import com.dd.utils.SocketMsgUtils;
import com.dd.utils.SocketUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.nutz.castor.Castors;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.pager.Pager;
import org.nutz.integration.shiro.SimpleShiroToken;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.Strings;
import org.nutz.lang.random.R;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.*;


/**
 * 主控制器
 *@Author: 蛋蛋 [TopCoderMyDream@gmail.com,https://github.com/TopCoderMyDream]
 *@Time:2017/12/12 13:54
 */
@At("/user")
@IocBean
@Ok("json")
public class UserController  {
    @Inject(value="userDao")
    private UserDao userDao;

    @Inject
    private Dao dao;

    @Inject
    private ChatMessageDao chatMessageDao;

    /**
     * 登陆
     * @param u
     * @param session
     * @return
     */
    @At
    @POST
    public Object login(@Param("..")User u, HttpSession session, HttpServletResponse response,HttpServletRequest request){

        String msg = checkUser(u,false);
        if(msg!=null){
           return Response.fail(msg);
        }
//        session.setAttribute("me", user.getId());
//        session.setAttribute("username", user.getUsername());
//        session.setAttribute("sessionId",session.getId());

        UsernamePasswordToken token = new UsernamePasswordToken(u.getUsername(),u.getPwd());
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);//
        } catch (UnknownAccountException e2){
           return Response.fail("账户不存在!");
        }catch ( IncorrectCredentialsException e1){
            return Response.fail("密码错误!");
        }
        User fetch = dao.fetch(User.class, Cnd.where(User.USERNAME, "=", u.getUsername()));
        if(  SecurityUtils.getSubject().isAuthenticated()){

            subject.getSession().setAttribute("me", fetch.getId());
            subject.getSession().setAttribute("username", fetch.getUsername());
            subject.getSession().setAttribute("sessionId", session.getId());

            return Response.ok("登陆成功!");
        }else{
            return Response.fail("登录失败!");
        }

    }

    /**
     * 注册
     * @param user
     * @param session
     * @return
     */
    @At
    @POST
    public Object registry(@Param("..") User user,HttpSession session,HttpServletResponse response){
        NutMap re = new NutMap();
        String msg = checkUser(user,true);
        if(msg != null){
            re.setv("ok", false).setv("msg", msg);
            return re;
        }

        String avatar = getAvatar();
        user.setAvatar("imgs/"+avatar+".jpg");
        User u = userDao.save(user);
        if(u==null){
            re.setv("ok", false).setv("msg", "注册失败!");
            return re;
        }else{
            UsernamePasswordToken token = new UsernamePasswordToken(u.getUsername(),u.getPwd());
            Subject subject = SecurityUtils.getSubject();
            try {
                subject.login(token);//
            } catch (UnknownAccountException e2){
                return Response.fail("登录失败,请手动登录!");
            }catch ( IncorrectCredentialsException e1){
                return Response.fail("登录失败,请手动登录!");
            }

            if( SecurityUtils.getSubject().isAuthenticated()){
                subject.getSession().setAttribute("me", u.getId());
                subject.getSession().setAttribute("username", u.getUsername());
                subject.getSession().setAttribute("sessionId", session.getId());

                //添加默认分组
                userDao.addGroup(u.getId(), "亲人们");
                int i = userDao.addGroup(u.getId(), "Ji友们");

                //加群
                FlockRefUser fr = new FlockRefUser();//Nutz社区群
                fr.setFid(4);
                fr.setUid(user.getId());
                dao.insert(fr);

                FlockRefUser fr2 = new FlockRefUser();//Layui群
                fr2.setFid(5);
                fr2.setUid(user.getId());
                dao.insert(fr2);

                FlockRefUser fr3 = new FlockRefUser();//T-io群
                fr3.setFid(6);
                fr3.setUid(user.getId());
                dao.insert(fr3);

                //把我加上呀得
                userDao.addFriend(user.getId(), 1, i);
                userDao.addFriend(1,user.getId(), 2);
                re.setv("ok", true).setv("msg", "注册成功!");
            }else{
                re.setv("ok", true).setv("msg", "自动登录失败,请手动登录!");
            }

            return re;
        }
    }

    public String getAvatar(){
        int random = R.random(1, 15);
        return String.valueOf(random);
    }

    /**
     * 查找用户
     * @param name
     * @return
     */
    @At
    public Object seachUser(@Param("name") String name){
        List<User> users = userDao.getByLikeName(name);
        return Json.toJson(users);
    }

    /**
     * 查询用户和群组
     * @param name
     * @return
     */
    @At
    public Object seach(@Param("name") String name){
        List<User> users = userDao.getByLikeName(name);
        List<Flock> flocks = dao.query(Flock.class, Cnd.where(Flock.GROUPNAME, "like", "%" + name + "%"));

        return Response.ok().setv("users",Json.toJson(users)).setv("groups",Json.toJson(flocks));
    }



    /**
     * 初始化数据
     * @param me
     * @return
     */
    @At
    @Ok("raw")
    public String getInitData(@Attr("me") int me){
        String data = userDao.getInitData(me);
//        System.out.println(data);
        return data;
    }


    /**
     * 获取未读消息数量
     * @param me
     * @return
     */
    @At
    public Object unreadMsgCount(@Attr("me") int me){
        List<Message> msgs = userDao.getMessages(me);
        int count = 0;
        for(Message msg : msgs){
            if(msg.getRead() == 0){//0未读
                count++;
            }
        }
        NutMap nm = new NutMap();
        nm.setv("ok", true).setv("count",count);
        return nm;
    }

    /**
     * 获取我的消息
     * @param me
     * @return
     */
    @At
    public Object getMsg(@Attr("me") int me){
        List<Message> msgs = userDao.getMessages(me);
        JsonMsgModel jmm = new JsonMsgModel();
        jmm.setCode(0);
        jmm.setPages(1);
        jmm.setData(Castors.me().castTo(msgs,ArrayList.class));
        return jmm;
    }

    /**
     * 已读我的消息
     * @param me
     */
    @At
    public void markRead(@Attr("me") int me){
        userDao.markRead(me);
    }

    /**
     * 申请添加好友
     * @param me 我的id
     * @param uid 对方id
     * @param from_group  到哪个分组?
     * @return
     */
    @At
    public Object applyFriend(@Attr("me") int me,@Param("uid")int uid,@Param("from_group")int from_group ,@Param("remark")String remark){
        NutMap nm = new NutMap();
        if(!isFirend(me,uid)) {
            int i = userDao.applyFriend(uid, me, from_group, remark);
        if (1 > 0){
            SocketUtils.sendByUserId(String.valueOf(uid), SocketMsgUtils.madeWsResponse(Type.REQFRIEND, "1"));
            nm.setv("ok", 1);
        }else
            nm.setv("ok", 0);
        }else{
            return Response.fail("对方已经是您的好友,不可重复添加哦!");
        }

        return nm;
    }


    /**
     * 检查是否已经是好友
     * @param me
     * @param uid
     * @return
     */
    public boolean isFirend(int me,int uid){
        List<Friends> query = dao.query(Friends.class, Cnd.where(Friends.ME, "=", me).and(Friends.FRIEND, "=", uid));
        if(query == null || query.size() == 0){//不是则返回true
            return false;
        }else{
            return true;
        }
    }

    /**
     * 同意添加
     * @param me 我的id
     * @param uid 对方的id
     * @param group 我要添加到的分组id
     * @param from_group 对方要添加到的分组id
     * @return
     */
    @At
    public Object addFridend(@Attr("me") int me,@Param("uid") int uid,@Param("group") int group,@Param("from_group") int from_group,@Param("msgid")int msgid){
        NutMap nm = new NutMap();

        //将对方添加成我的好友
        int id = userDao.addFriend(me, uid, group);
        //将我添加成对方的好友
        int i = userDao.addFriend(uid, me, from_group);
        System.out.println("加好友成功!");

        //更新消息状态
        userDao.updateMsg(msgid, 3);  //更新状态为已同意

        //给对方发送一个添加成功的消息
        Message msg = new Message();
        msg.setContent("成功添加对方为好友!");
        msg.setUid(uid);
        msg.setFrom(me);
        msg.setFrom_group(from_group);;
        msg.setType(3);
        msg.setRead(1);
        msg.setTime(new Date());
        msg.setRemark("");
        Message i2= dao.insert(msg);

        User fetch = dao.fetch(User.class, me);
        NutMap n = new NutMap();
        n.setv("user",fetch);
        n.setv("group",from_group);
        SocketUtils.sendByUserId(String.valueOf(uid),SocketMsgUtils.madeWsResponse(Type.REQFRIENDSUCCESS,Json.toJson(n)));

        nm.setv("code", 0);
        return nm;
    }

    /**
     * 拒绝添加
     * @param me
     * @param msgid
     * @return
     */
    @At
    public Object declineApply(@Attr("me") int me,@Param("msgid")int msgid,@Param("remark")String remark){
        NutMap nm = new NutMap();
        //更新消息状态
        userDao.updateMsg(msgid, 2);

        //给对方发一条拒绝通知
        Message fetch = dao.fetch(Message.class, msgid);
        Message msg = new Message();
        msg.setType(2);
        msg.setContent("对方拒绝了您的请求消息");
        msg.setUid(fetch.getFrom());
        msg.setFrom(me);
        msg.setFrom_group(fetch.getFrom_group());
        msg.setRemark(remark);
        msg.setHref("");
        msg.setRead(0);
        msg.setTime(new Date());
        dao.insert(msg);

        //socket通知对方
        SocketUtils.sendByUserId(String.valueOf(fetch.getFrom()), SocketMsgUtils.madeWsResponse(Type.REQFRIEND, "1"));

        nm.setv("code", 0);
        return nm;
    }

    /**
     * 上线
     * @param me
     */
    @At
    public void online(@Attr("me") int me){
        userDao.online(me);
    }

    /**
     * 下线
     * @param me
     */
    @At
    public void hide(@Attr("me") int me){
        userDao.hide(me);
    }

    /**
     * 修改签名
     * @param me
     * @param sign
     */
    @At
    public void updateSign(@Attr("me") int me,@Param("sign") String sign){
        userDao.updateSign(me, sign);
    }

    /**
     * 根据id获取用户信息,可用于查看在线状态
     * @param id
     * @return
     */
    @At
    public Object getUser(@Param("id") int id){
        User user = userDao.findbyid(id);
        return user;
    }

    /**
     * 查询群成员
     * @return
     */
    @At
    public Object getMembers(@Param("id") int fid){
        List<User> members = userDao.getMembers(fid);
        InitData id = new InitData();
        id.setCode(0);
        id.setMsg("");

        Map<String,Object> war = new HashMap<String,Object>();
        war.put("list", members);
        id.setData(war);
        return id;
    }

    /**
     * 分页查询聊天记录
     * @param me
     * @param pageNo
     * @param pageSize
     * @param toid
     * @param type
     * @return
     */
    @At
    @Ok("json")
    public Object getOldMsgs(@Attr("me") int me,@Param("pageNo") int pageNo,@Param("pageSize") int pageSize,@Param("toid") int toid,@Param("type") int type){
		/*
		   username: '纸飞机'
	      ,id: 1
	      ,avatar: 'http://tva3.sinaimg.cn/crop.0.0.512.512.180/8693225ajw8f2rt20ptykj20e80e8weu.jpg'
	      ,timestamp: 1480897882000
	      ,content: 'face[抱抱] face[心] 你好啊小美女'
		 */
        NutMap nm = chatMessageDao.pageMsg(pageNo, pageSize, me, toid, type);
        return nm;
    }

    /**
     * Validate Data
     * @param user
     * @param create
     * @return
     */
    protected String checkUser(User user, boolean create) {
        if (user == null) {
            return "空对象";
        }
        if (create) {
            if (Strings.isBlank(user.getUsername()) || Strings.isBlank(user.getPwd()))
                return "用户名/密码不能为空";
        } else {
            if (Strings.isBlank(user.getPwd()))
                return "密码不能为空";
        }
//        dao.update(User.class,org.nutz.dao.Chain.make("name","admin").add("age",15),Cnd.where("id","=",2));
        String passwd = user.getPwd().trim();
        if (6 > passwd.length() || passwd.length() > 12) {
            return "密码长度必须大于6位小于12位!";
        }

        user.setPwd(passwd);
        if (create) {
            int count = dao.count(User.class, Cnd.where("username", "=", user.getUsername()));

            if (count != 0) {
                if("wendal".equalsIgnoreCase(user.getUsername())){
                    return "wendal大叔可直接登录的哦!\n账户:wendal\n密码:wendal\n(๑′ᴗ‵๑)";
                }
                return "用户名已经存在";
            }
        }
        if (user.getUsername() != null)
            user.setUsername(user.getUsername().trim());
        return null;
    }

    /**
     * 分手...
     * @return
     */
    @At
    public Object separate(@Param("id")int id,@Attr("me")int me){
        int i = dao.clear(Friends.class, Cnd.where(Friends.ME, "=", me).and(Friends.FRIEND, "=", id));
        if(i > 0)
            return Response.ok();
        else
            return Response.fail();
    }


    /**
     * 加群
     * @param userId
     * @param groupId
     * @param me
     * @return
     */
    @At
    public Object innerGroup(@Param("userId")int userId,@Param("groupId")int groupId,@Attr("me")int me,@Param("remark")String remark){
        Flock group = dao.fetch(Flock.class, groupId);
        if(group == null){
            return Response.fail("群组不存在!");
        }
        FlockRefUser user = dao.fetch(FlockRefUser.class, Cnd.where(FlockRefUser.UID, "=", userId).and(FlockRefUser.FID, "=", groupId));
        if(user != null){
            return Response.fail("不可重复加群!");
        }

        Message msg = new Message();
        msg.setContent("请求加入"+group.getGroupname()+"群");
        msg.setUid(group.getUserid());//发给群主呀
        msg.setFrom(userId);
        msg.setFrom_group(groupId);
        msg.setType(4);
        msg.setRemark(remark);
        msg.setHref("");
        msg.setRead(0);
        msg.setTime(new Timestamp(System.currentTimeMillis()));

        dao.clear(Message.TABLE_NAME, Cnd.where(Message.FROM, "=", userId).and(Message.UID, "=", group.getUserid()).and(Message.TYPE, "=", "4").and(Message.READ,"=",0));

        dao.insert(msg);

        //Socket通知
        SocketUtils.sendByUserId(String.valueOf(group.getUserid()), SocketMsgUtils.madeWsResponse(Type.REQFRIEND, "1"));
        return Response.ok();
    }


    /***
     * 同意加群请求
     * @param id 请求编号
     * @param me 当前用户
     * @return
     */
    @At
    @POST
    public Object agreeGroup(@Param("msgId")int id,@Attr("me")int me){
        Message fetch = dao.fetch(Message.class, id);
        if(fetch == null){
            return Response.fail("请求消息不存在!");
        }

        Flock group = dao.fetch(Flock.class, fetch.getFrom_group());

        if(group == null){
            return Response.fail("该群已解散或不存在!");
        }

        if(fetch.getUid() != me){
            return Response.fail("您不是群主,操作失败!");
        }

        //加群
        FlockRefUser fr = new FlockRefUser();
        fr.setFid(fetch.getFrom_group());
        fr.setUid(fetch.getFrom());
        dao.clear(FlockRefUser.class,Cnd.where(FlockRefUser.UID,"=",fetch.getFrom()).and(FlockRefUser.FID,"=",fetch.getFrom_group()));
        dao.insert(fr);

        //消息已读
        fetch.setRead(1);
        fetch.setType(3);
        dao.updateIgnoreNull(fetch);

        //给请求人发个加群成功的消息
        Message msg = new Message();
        msg.setContent("您已加入 "+ group.getGroupname() +" 群!");
        msg.setUid(fetch.getFrom());
        msg.setFrom(me);
        msg.setFrom_group(fetch.getFrom_group());;
        msg.setType(3);
        msg.setRead(0);
        msg.setTime(new Date());
        msg.setRemark("");
        Message i2= dao.insert(msg);

        //Socket通知对方
        NutMap nm = new NutMap();
        nm.setv("type","group");
        nm.setv("avatar",group.getAvatar());
        nm.setv("groupname",group.getGroupname());
        nm.setv("id",group.getId());
        SocketUtils.sendByUserId(String.valueOf(fetch.getFrom()), SocketMsgUtils.madeWsResponse(Type.JOIN_GROUP_REQ, Json.toJson(nm)));
        return Response.ok();
    }


    /***
     * 同意加群请求
     * @param id 请求编号
     * @param me 当前用户
     * @return
     */
    @At
    @POST
    public Object refuseGroup(@Param("msgId")int id,@Attr("me")int me,@Param("remark")String remark){
        Message fetch = dao.fetch(Message.class, id);
        if(fetch == null){
            return Response.fail("请求消息不存在!");
        }

        Flock group = dao.fetch(Flock.class, fetch.getFrom_group());
        if(group == null){
            return Response.fail("该群已解散或不存在!");
        }
        if(fetch.getUid() != me){
            return Response.fail("您不是群主,操作失败!");
        }

        //更新消息状态
        userDao.updateMsg(id, 2);

        //给对方发一条拒绝通知
        Message msg = new Message();
        msg.setType(2);
        msg.setContent("拒绝了您的加群请求!");
        msg.setUid(fetch.getFrom());
        msg.setFrom(me);
        msg.setFrom_group(fetch.getFrom_group());
        msg.setRemark(remark);
        msg.setHref("");
        msg.setRead(0);
        msg.setTime(new Date());
        dao.insert(msg);

        //socket通知对方
        SocketUtils.sendByUserId(String.valueOf(fetch.getFrom()), SocketMsgUtils.madeWsResponse(Type.REQFRIEND, "1"));

        return Response.ok();
    }
    Log log = Logs.get();




    /**
     * 随机推荐几个人和群
     * @return
     */
    @At
    @Ok("json:{actived:'username|groupname'}")
    public Object random(){
        List<User> list1 = dao.query(User.class, Cnd.wrap("ORDER BY RAND()"), new Pager(1, 2));
        List<Flock> list2 = dao.query(Flock.class, Cnd.wrap("ORDER BY RAND()"), new Pager(1, 2));
        ArrayList<String> str = new ArrayList<>();
        for (int i = 0; i < list1.size(); i++) {
            str.add(list1.get(i).getUsername());
        }
        for (int i = 0; i < list2.size(); i++) {
            str.add(list2.get(i).getGroupname());
        }

        return str;
    }


//    @At
//    @Ok("fm:/user/hello")
//    public Object hello(){
//        return new NutMap().setv("hello","world");
//    }
}


