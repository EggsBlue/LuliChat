package com.dd.entity.msg;

import org.nutz.json.Json;

import java.util.Map;

/**
 * 类描述:接收消息规格
 * 作者: 蛋蛋
 * 时间:2017年4月23日 下午2:49:34
 * 年龄:18
 */
public class ReceiveMessage {
	private Map<String,String> mine;
	private Map<String,String> to;
	
	public Map<String, String> getMine() {
		return mine;
	}
	public void setMine(Map<String, String> mine) {
		this.mine = mine;
	}
	public Map<String, String> getTo() {
		return to;
	}
	public void setTo(Map<String, String> to) {
		this.to = to;
	}
	public String toJson(){
		return Json.toJson(this);
	}
}
