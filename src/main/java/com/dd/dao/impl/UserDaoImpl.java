package com.dd.dao.impl;

import com.dd.dao.UserDao;
import com.dd.entity.*;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.util.cri.SqlExpression;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;

import java.util.*;

/**
 * Describe: 用户DAO类实现
 * Author:蛋蛋
 * Age:Eighteen
 * Time:2017年4月26日 下午5:01:26
 */
@IocBean(name="userDao")
public class UserDaoImpl implements UserDao {
	
	@Inject
	private Dao dao;
	
	/**
	 * 根据用户名密码查找用户
	 */
	public User getByNamPwd(User user){
		User u = null;
		u = dao.fetch(User.class, Cnd.where(User.USERNAME,"=",user.getUsername()).and(User.PWD,"=",user.getPwd()));
		return u;
	}
	
	/**
	 * 初始化主页数据
	 * @param id 我的id
	 * @return 返回json数据
	 */
	public String getInitData(int id){
		InitData data=  new InitData();//总json对象
		Map<String,Object> wrap = new HashMap<String,Object>();//包装
		
		//我的信息
		User user = dao.fetch(User.class, Cnd.where(User.ID,"=",id));
		wrap.put("mine", user);
		
		//我的好友
		SqlExpression exp = Cnd.exp(Group.USER_ID, "in", id);
		
		List<Group> groups = dao.query(Group.class, Cnd.where(exp));//我的分组,这里不能用in~
		for(int i=0; i<groups.size(); i++){
			SqlExpression exp2 = Cnd.exp(Friends.GROUPID,"=",groups.get(i).getId());
			List<Friends> query = dao.query(Friends.class, Cnd.where(exp2).and(Friends.ME,"=",id));
			List<User> temp = new ArrayList<User>();
			for(Friends f : query){
				SqlExpression exp3 = Cnd.exp(User.ID,"=",f.getFriend());
				temp.add(dao.fetch(User.class, Cnd.where(exp3)));
			}
			groups.get(i).setList(temp);
		}
		wrap.put("friend", groups);
		
		//我的分组
//		Map<String,Object> gs=  new HashMap<String,Object>();
//		gs.put("groupname", "我的女神们");
//		gs.put("id", "1");
//		gs.put("avatar", "/mychat/imgs/1.jpg");
//		
//		Map<String,Object> gs2=  new HashMap<String,Object>();
//		gs2.put("groupname", "屌丝大本营");
//		gs2.put("id", "2");
//		gs2.put("avatar", "/mychat/imgs/2.jpg");
//		
//		List<Map<String,Object>> msList = new ArrayList<Map<String,Object>>();
//		msList.add(gs);
//		msList.add(gs2);
		
		List<FlockRefUser> refUsers = dao.query(FlockRefUser.class, Cnd.where(FlockRefUser.UID,"=",id));
		List<Flock> fs = new ArrayList<Flock>();
		for(FlockRefUser u : refUsers){
			Flock fetch = dao.fetch(Flock.class, Cnd.where(Flock.ID,"=",u.getFid()));
			fs.add(fetch);
		}

		wrap.put("group", fs);
		data.setData(wrap);
		data.setMsg("");
		return Json.toJson(data);
	}
	

	public List<User> getFriends(int id){
		List<User> friends = new ArrayList<User>();
		List<Friends> list = dao.query(Friends.class, Cnd.where(Friends.ME,"=",id));
		for(Friends f : list){
			User user = dao.fetch(User.class, Cnd.where(User.ID,"=",f.getFriend()));
			friends.add(user);
		}
		return friends;
	}

	/**
	 * 保存一条数据
	 */
	@Override
	public User save(User user) {
		User user2 = dao.insert(user);
		return user2;
	}

	/**
	 * 根据姓名查询用户
	 */
	@Override
	public List<User> getByLikeName(String name) {
		List<User> list = dao.query(User.class, Cnd.where(User.USERNAME,"like","%"+name+"%"));
		return list;
	}
	
	
	
	/**
	 * 更新在线状态
	 */
	@Override
	public void updateStatus(String name) {
		User user = dao.fetch(User.class, Cnd.where(User.USERNAME,"=",name));
		if("online".equals(user.getStatus())){
			user.setStatus("hide");
		}else{
			user.setStatus("online");
		}
		dao.update(user);
	}

	/**
	 * 获取我的消息
	 */
	@Override
	public List<Message> getMessages(int me) {
		List<Message> list = dao.query(Message.class, Cnd.where(Message.UID,"=",me).orderBy("time", "desc"));
		if(list!=null && list.size()>0){
			for(int i=0; i<list.size(); i++){
				if(null != list.get(i).getFrom()){
					int from_id = list.get(i).getFrom();
					User u = dao.fetch(User.class, Cnd.where(User.ID,"=",from_id));
					list.get(i).setUser(u);
				}
			}
		}
		return list;
	}

	/**
	 * 添加好友
	 */
	@Override
	public int addFriend(int me, int to, int groupid) {
		int count = dao.count(Friends.class, Cnd.where(Friends.ME, "=", me).and(Friends.FRIEND, "=", to));
		if(count > 0){
			return 0;
		}
		Friends fds = new Friends();
		fds.setMe(me);
		fds.setFriend(to);
		fds.setGroupId(groupid);
		Friends insert = dao.insert(fds);
		if(insert!=null){
			return 1;
		}
		return 0;
	}

	/**
	 * 添加分组
	 */
	@Override
	public int addGroup(int me, String groupName) {
		Group g = new Group();
		g.setUser_id(me);
		g.setGroupname(groupName);
		Group group = dao.insert(g);
		if(group==null){
			System.out.println("添加分组成功--id:"+group.getId()+",groupName:"+group.getGroupname());
			return 0;
		}
		return group.getId();
	}

	/**
	 * 标记消息为已读 
	 */
	@Override
	public void markRead(int id) {
//		List<Message> list = dao.query(Message.class, Cnd.where(Message.UID,"=",id));
//		for(){
//			
//		}
		dao.update("Message", Chain.make("`read`", 1), Cnd.where(Message.UID,"=",id));
	}

	/**
	 * 更新消息状态
	 */
	@Override
	public void updateMsg(int id, int type) {
		dao.update("message", Chain.make("type", type), Cnd.where(Message.ID,"=",id));
	}

	/**
	 * 申请加好友
	 */
	@Override
	public int applyFriend(int uid, int from, int from_group,String remark) {
		//delete方法没有Cnd函数,需使用clera ^_^
//		dao.delete(Message.TABLE_NAME,Cnd.where(Message.FROM,"=",from).and(Message.UID,"=",uid).and(Message.TYPE,"=","1"));
		//根据qq特性,重复加好友的请求消息只有一条,默认覆盖旧的,so clear old msg.
		dao.clear(Message.TABLE_NAME, Cnd.where(Message.FROM, "=", from).and(Message.UID, "=", uid).and(Message.TYPE, "=", "1").and(Message.READ,"=",0));
		Message msg = new Message();
		msg.setContent("申请添加你为好友");
		msg.setUid(uid);
		msg.setFrom(from);
		msg.setFrom_group(from_group);;
		msg.setType(1);
		msg.setRead(0);
		msg.setTime(new Date());
		msg.setRemark(remark);
		Message i= dao.insert(msg);
		if(i!=null){
			return 1;
		}
		return 0;
	}

	/**
	 * 上线
	 */
	@Override
	public void online(int id) {
		dao.update("user", Chain.make("status", "online"), Cnd.where(User.ID,"=",id));
	}

	/**
	 * 下线
	 */
	@Override
	public void hide(int id) {
		dao.update("user", Chain.make("status", "hide"), Cnd.where(User.ID,"=",id));
	}

	/**
	 * 修改签名
	 */
	@Override
	public void updateSign(int id, String sign) {
		dao.update("user", Chain.make("sign", sign), Cnd.where(User.ID,"=",id));
	}

	/**
	 * 根据id查找用户
	 */
	@Override
	public User findbyid(int id) {
		User user = dao.fetch(User.class, Cnd.where(User.ID,"=",id));
		return user;
	}

	/**
	 * 查询群成员
	 */
	@Override
	public List<User> getMembers(int fid) {
		List<FlockRefUser> list = dao.query(FlockRefUser.class, Cnd.where(FlockRefUser.FID,"=",fid));
		List<User> users=  new ArrayList<User>();
		for(FlockRefUser u : list){
			User ser = dao.fetch(User.class, Cnd.where(User.ID,"=",u.getUid()));
			users.add(ser);
		}
		return users;
	}

	/**
	 * 查询我的分组
	 */
	@Override
	public List<Flock> getFlocks(int id) {
		List<FlockRefUser> refUser = dao.query(FlockRefUser.class, Cnd.where(FlockRefUser.UID,"=",id));
		List<Flock> flocks = new ArrayList<Flock>();
		for(FlockRefUser f : refUser){
			flocks.add(dao.fetch(Flock.class, Cnd.where(Flock.ID,"=",f.getFid())));
		}
		return flocks;
	}

	/**
	 * 查询是否是好友关系
	 */
	@Override
	public boolean isFriend(int me, int to) {
		Friends f = dao.fetch(Friends.class, Cnd.where(Friends.ME,"=",me).and(Friends.FRIEND,"=",to));
		return f==null?false:true;
	}

	@Override
	public List<Object> findUserAndFlockByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}
}
