package com.dd.entity.msg;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * Describe:聊天消息记录
 * Author:蛋蛋
 * Time:2017年4月26日 下午5:01:05
 */
@Table("chatmessage")
public class ChatMessage {
	@Id
	private int id;	//消息类型
	@Column("username")
	private String username; //用户名
	@Column("avatar")
	private String avatar; //头像地址
	@Column(value="timestamp",wrap=true)
	private String timestamp; //时间戳
	@Column("content")
	private String content; //消息内容
	@Column("unreadpoint")
	private Integer unreadpoint;  //是否未读
	@Column("type")
	private Integer type;  //消息类型,1单聊 2.群聊
	@Column("toid")
	private Integer toid;  //对方id
	@Column(value="from",wrap=true)
	private Integer from;
	@Column("unreadnumbers")
	private String unreadnumbers;	//是否是我接收的.  0我发收的,1我接收的


	public Integer getUnreadpoint() {
		return unreadpoint;
	}

	public String getUnreadnumbers() {
		return unreadnumbers;
	}

	public void setUnreadnumbers(String unreadnumbers) {
		this.unreadnumbers = unreadnumbers;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getToid() {
		return toid;
	}
	public void setToid(Integer toid) {
		this.toid = toid;
	}
	public void setUnreadpoint(Integer unreadpoint) {
		this.unreadpoint = unreadpoint;
	}
	public Integer getFrom() {
		return from;
	}
	public void setFrom(Integer from) {
		this.from = from;
	}

	public static final String ID = "id";
	public static final String USERNAME = "username";
	public static final String AVATAR = "avatar";
	public static final String TIMESTAMP = "timestamp";
	public static final String CONTENT = "content";
	public static final String UNREADPOINT = "unreadpoint";
	public static final String TYPE = "type";
	public static final String TOID = "toid";
	public static final String FROM = "from";
	public static final String UNREADNUMBERS = "unreadnumbers";
}
