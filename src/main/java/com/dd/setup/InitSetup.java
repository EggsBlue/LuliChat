package com.dd.setup;

import com.dd.entity.*;
import com.dd.socket.SocketServer;
import org.nutz.dao.Dao;
import org.nutz.dao.util.Daos;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.NutConfig;
import org.nutz.mvc.Setup;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


public class InitSetup implements Setup{
    Log log = Logs.get();

    @Override
    public void init(NutConfig nc) {
        Dao dao = nc.getIoc().get(Dao.class, "dao");
        //初始化表结构
        Daos.createTablesInPackage(dao,"com.dd.entity",false);
        if(dao.count(Flock.class,null) == 0){
            initData(dao);
        }

//        System.out.println(dao.count("flock",null));
        SocketServer server = nc.getIoc().get(SocketServer.class, "socketServer");
        server.start();
    }

    @Override
    public void destroy(NutConfig nc) {

    }

    //初始化数据
    public void initData(Dao dao){
        List<Flock> flocks = new ArrayList<>();
        List<User> users = new ArrayList<>();
        List<Group> groups = new ArrayList<>();
        List<Friends> friends = new ArrayList<>();
        List<FlockRefUser> frs = new ArrayList<>();
        //初始化用户
        User u1 =new User();
        u1.setStatus("online");
        u1.setAvatar("imgs/8.jpg");
        u1.setPwd("123456");
        u1.setUsername("蛋蛋的忧伤");
        u1.setSign("Nutz是最好的Java编程框架!");
        u1.setCreate_time(new Timestamp(System.currentTimeMillis()));
        User u2 =new User();
        u2.setStatus("online");
        u2.setAvatar("imgs/5.jpg");
        u2.setPwd("123456");
        u2.setUsername("wendal");
        u2.setSign("我是nutz主要贡献者!");
        u2.setCreate_time(new Timestamp(System.currentTimeMillis()));

        User u3 =new User();
        u3.setStatus("online");
        u3.setAvatar("imgs/2.jpg");
        u3.setPwd("123456");
        u3.setUsername("姜源");
        u3.setSign("我是蛋蛋的乖儿子.");
        u3.setCreate_time(new Timestamp(System.currentTimeMillis()));
        users.add(u1);
        users.add(u2);
        users.add(u3);
        dao.insert(users);

        //初始化分组
        Group g1 = new Group();
        g1.setGroupname("亲人");
        g1.setUser_id(u1.getId());

        Group g2 = new Group();
        g2.setGroupname("朋友");
        g2.setUser_id(u1.getId());

        Group g3 = new Group();
        g3.setGroupname("亲人");
        g3.setUser_id(u2.getId());

        Group g4 = new Group();
        g4.setGroupname("朋友");
        g4.setUser_id(u2.getId());

        Group g5 = new Group();
        g5.setGroupname("亲人");
        g5.setUser_id(u3.getId());

        Group g6 = new Group();
        g6.setGroupname("朋友");
        g6.setUser_id(u3.getId());
        groups.add(g1);
        groups.add(g2);
        groups.add(g3);
        groups.add(g4);
        groups.add(g5);
        groups.add(g6);
        dao.insert(groups);

        //初始化好友关系
        Friends fe1 = new Friends();
        fe1.setMe(u1.getId());
        fe1.setFriend(u2.getId());
        fe1.setGroupId(g1.getId());

        Friends fe2 = new Friends();
        fe2.setMe(u1.getId());
        fe2.setFriend(u3.getId());
        fe2.setGroupId(g1.getId());

        Friends fe3 = new Friends();
        fe3.setMe(u2.getId());
        fe3.setFriend(u1.getId());
        fe3.setGroupId(g3.getId());

        Friends fe4 = new Friends();
        fe4.setMe(u2.getId());
        fe4.setFriend(u3.getId());
        fe4.setGroupId(g3.getId());

        Friends fe5 = new Friends();
        fe5.setMe(u3.getId());
        fe5.setFriend(u1.getId());
        fe5.setGroupId(g5.getId());

        Friends fe6 = new Friends();
        fe6.setMe(u3.getId());
        fe6.setFriend(u2.getId());
        fe6.setGroupId(g5.getId());

        friends.add(fe1);
        friends.add(fe2);
        friends.add(fe3);
        friends.add(fe4);
        friends.add(fe5);
        friends.add(fe6);
        dao.insert(friends);

        //初始化群组
        Flock f1 = new Flock();
        f1.setUserid(1);
        f1.setGroupname("LuliChat开源项目交流");
        f1.setAvatar("imgs/1.jpg");

        Flock f2 = new Flock();
        f2.setUserid(1);
        f2.setGroupname("Nutz社区群");
        f2.setAvatar("imgs/10.jpg");

        Flock f3 = new Flock();
        f3.setUserid(1);
        f3.setGroupname("Layui社区群");
        f3.setAvatar("imgs/1.jpg");

        Flock f4 = new Flock();
        f4.setUserid(1);
        f4.setGroupname("T-io社区群");
        f4.setAvatar("imgs/2.jpg");

        flocks.add(f1);
        flocks.add(f2);
        flocks.add(f3);
        flocks.add(f4);
        dao.insert(flocks);

        //初始化群成员
        FlockRefUser fr1 = new FlockRefUser();
        fr1.setFid(f1.getId());
        fr1.setUid(u1.getId());

        FlockRefUser fr2 = new FlockRefUser();
        fr2.setFid(f1.getId());
        fr2.setUid(u2.getId());

        FlockRefUser fr3 = new FlockRefUser();
        fr3.setFid(f1.getId());
        fr3.setUid(u3.getId());

        FlockRefUser fr4 = new FlockRefUser();
        fr4.setFid(f2.getId());
        fr4.setUid(u3.getId());

        FlockRefUser fr5 = new FlockRefUser();
        fr5.setFid(f3.getId());
        fr5.setUid(u1.getId());

        FlockRefUser fr6 = new FlockRefUser();
        fr6.setFid(f2.getId());
        fr6.setUid(u1.getId());

        frs.add(fr1);
        frs.add(fr2);
        frs.add(fr3);
        frs.add(fr4);
        frs.add(fr5);
        frs.add(fr6);
        dao.insert(frs);
    }

}
