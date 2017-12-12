package com.dd.dao;


import com.dd.entity.Flock;
import com.dd.entity.Message;
import com.dd.entity.User;

import java.util.List;

/**
 * Describe:用户DAO接口
 * Author:蛋蛋
 * Age:Eighteen
 * Time:2017年4月26日 下午5:01:50
 */
public interface UserDao {
	//根据id获取用户
	public User getByNamPwd(User user);
	//搜索用户
	public List<User> getByLikeName(String name);
	//添加一个用户
	public User save(User user);
	//初始化主页数据
	public String getInitData(int id);
	//获取好友
	public List<User> getFriends(int id);
	//更新状态
	public void updateStatus(String name);
	//获取我的所有消息
	public List<Message> getMessages(int me);
	//标记消息为已读
	public void markRead(int id);
	//添加好友
	public int addFriend(int me, int to, int groupid);
	//添加分组
	public int addGroup(int me, String groupName);
	//更新消息状态
	public void updateMsg(int id, int type);
	//申请加好友
	public int applyFriend(int uid, int from, int from_group,String remark);
	//上线
	public void online(int id);
	//下线
	public void hide(int id);
	//修改签名
	public void updateSign(int id, String sign);
	//根据id获取用户
	public User findbyid(int id);
	//查询群成员
	public List<User> getMembers(int fid);
	//查询我的分组
	public List<Flock> getFlocks(int id);
	//查看是否为好友关系
	public boolean isFriend(int me, int to);
	//给定搜索字段,搜索群和用户
	public List<Object> findUserAndFlockByName(String name);
}
