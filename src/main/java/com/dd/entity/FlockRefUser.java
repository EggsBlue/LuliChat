package com.dd.entity;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

/**
 * Describe:群聊关系表
 * Author:蛋蛋
 * Age:Eighteen
 * Time:2017年5月5日 下午2:04:10
 */
@Table("flockrefuser")
public class FlockRefUser {
	@Column
	private Integer uid;
	@Column
	private Integer fid;
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	public Integer getFid() {
		return fid;
	}
	public void setFid(Integer fid) {
		this.fid = fid;
	}
	
	public static final String UID = "uid";
	public static final String FID = "fid";
}
