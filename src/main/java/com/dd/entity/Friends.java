package com.dd.entity;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

/**
 * Describe:好友关系表
 * Author:蛋蛋
 * Age:Eighteen
 * Time:2017年5月5日 下午2:04:19
 */
@Table("friends")
public class Friends {
	@Column
	private Integer me;
	@Column
	private Integer friend;
	@Column
	private Integer groupId;
	
	public int getMe() {
		return me;
	}
	public void setMe(int me) {
		this.me = me;
	}
	public int getFriend() {
		return friend;
	}
	public void setFriend(int friend) {
		this.friend = friend;
	}
	
	public Integer getGroupId() {
		return groupId;
	}
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}


	public static final String ME = "me";
	public static final String FRIEND = "friend";
	public static final String GROUPID = "groupId";
	
}
