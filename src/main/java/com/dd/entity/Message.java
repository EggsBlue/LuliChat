package com.dd.entity;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

import java.util.Date;

/**
 * Describe:消息Entity
 * Author:蛋蛋
 * Age:Eighteen
 * Time:2017年4月25日 下午3:10:24
 */
@Table("message")
public class Message {
	@Id
	private int id;//消息id
	@Column("content")
	private String content;//消息内容
	@Column("uid")
	private int uid;//我的id
	@Column(wrap=true,value="from")
	private Integer from;//发送人,如果此字段为0或者为没有则为系统消息
	@Column("from_group")
	private int from_group;//分组id
	@Column(wrap=true,value="type")
	private int type;//1.请求加好友.  2.已拒绝  3.已同意
	@Column("remark")
	private String remark;//留言
	@Column("href")
	private String href;
	@Column(wrap=true,value="read")
	private int read;//是否已读.1.已读.0.未读
	@Column(wrap=true,value="time")
	private Date time;//消息日期
	private User user;//发送人信息
	
	public final static String ID = "id";
	public final static String CONTENT = "content";
	public final static String UID = "uid";
	public final static String FROM = "`from`";
	public final static String FROM_GROUP = "from_group";
	public final static String TYPE = "`type`";
	public final static String REMARK = "remark";
	public final static String HREF = "href";
	public final static String READ = "`read`";
	public final static String TIME = "time";

	public static final String TABLE_NAME = "message";
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public Integer getFrom() {
		return from;
	}
	public void setFrom(Integer from) {
		this.from = from;
	}
	public int getFrom_group() {
		return from_group;
	}
	public void setFrom_group(int from_group) {
		this.from_group = from_group;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public int getRead() {
		return read;
	}
	public void setRead(int read) {
		this.read = read;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	@Override
	public String toString() {
		return "Message [id=" + id + ", content=" + content + ", uid=" + uid + ", from=" + from + ", from_group="
				+ from_group + ", type=" + type + ", remark=" + remark + ", href=" + href + ", read=" + read + ", time="
				+ time + ", user=" + user + "]";
	}
	
}
