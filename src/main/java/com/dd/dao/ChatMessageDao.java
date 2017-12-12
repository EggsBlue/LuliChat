package com.dd.dao;

import com.dd.entity.msg.ChatMessage;
import org.nutz.lang.util.NutMap;

import java.util.List;

/**
 * Describe:聊天记录DAO接口
 * Author:蛋蛋
 * Age:Eighteen
 * Time:2017年4月26日 下午4:11:26
 */
public interface ChatMessageDao {
	/**
	 * 插入一条消息
	 * @param cm 消息实体
	 * @return
	 */
	public int addChatMsg(ChatMessage cm);
	/**
	 * 分页查询
	 * @param pageSize 每页显示数量
	 * @param fromId 我的id
	 * @param toId 对方id
	 * @param type 消息类型
	 * @return
	 */
	public NutMap pageMsg(int pageNo, int pageSize, int fromId, int toId, int type);
	/**
	 * 检测我是否有未读消息
	 * @param fromId 我的id
	 * @return 未读消息数量
	 */
	public int isUnread(int fromId);
	/**
	 * 把我的消息都置为已读
	 * @param fromId
	 */
	public void readAll(int fromId);

	/**
	 * 把我的消息都置为已读
	 * @param toid
	 */
	public void readAll2(int toid);

	/**
	 * 指定消息已读
	 */
	public void readAll3(ChatMessage c);

	/**
	 * 获取未读消息
	 * @param fromId 我的id
	 * @return
	 */
	public List<ChatMessage> getUnRead(int fromId, String username);
	
}
