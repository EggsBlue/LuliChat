package com.dd.entity;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * Describe:群聊
 * Author:蛋蛋
 * Age:Eighteen
 * Time:2017年4月28日 上午9:56:48
 */
@Table("flock")
public class Flock {
	@Id
	private Integer id;
	@Column
	private int userid;
	@Column("groupname")
	private String groupname;//群名
	@Column("avatar")
	private String avatar;//头像
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getGroupname() {
		return groupname;
	}
	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public static final String ID = "id";
	public static final String GROUPNAME = "groupname";
	public static final String AVATAR = "avatar";
	public static final String USERID = "userid";


}
