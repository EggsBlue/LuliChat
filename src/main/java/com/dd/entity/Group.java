package com.dd.entity;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

import java.util.List;

/**
 * Describe:分组表
 * Author:蛋蛋
 * Age:Eighteen
 * Time:2017年5月5日 下午2:04:26
 */
@Table("`group`")
public class Group {
	@Id
	private Integer id;
	@Column
	private Integer user_id;
	@Column
	private String groupname;
	
	private List<User> list;


	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getUser_id() {
		return user_id;
	}
	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}
	public String getGroupname() {
		return groupname;
	}
	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}
	public List<User> getList() {
		return list;
	}
	public void setList(List<User> list) {
		this.list = list;
	}
	public static final String ID="id";
	public static final String USER_ID="user_id";
	public static final String GEOUPNAME="groupname";
	
}
