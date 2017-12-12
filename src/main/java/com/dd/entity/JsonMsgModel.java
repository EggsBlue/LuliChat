package com.dd.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Describe:消息模型
 * Author:蛋蛋
 * Age:Eighteen
 * Time:2017年4月25日 下午3:43:20
 */
public class JsonMsgModel {
	private int code;
	private int pages;
	private ArrayList<Message> data;
	
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public int getPages() {
		return pages;
	}
	public void setPages(int pages) {
		this.pages = pages;
	}
	public ArrayList<Message> getData() {
		return data;
	}
	public void setData(ArrayList<Message> data) {
		this.data = data;
	}
	
	
	@Override
	public String toString() {
		return "JsonMsgModel [code=" + code + ", pages=" + pages + ", data=" + data + "]";
	}
}
