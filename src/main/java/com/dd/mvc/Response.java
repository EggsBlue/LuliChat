package com.dd.mvc;

import org.nutz.lang.util.NutMap;

/**
 * 响应实体
 */
public class Response extends NutMap{

	private static final long serialVersionUID = 1L;
	
	public static final int OK = 1;
	public static final int FAIL = 0;
	public static final String MSG = "msg";
	public static final String CODE = "code";
	public static final String BODY = "body";

	public String getMsg() {
		return getString(MSG);
	}

	public void setMsg(String msg) {
		put(MSG, msg);
	}

	public int getCode() {
		return getInt(CODE);
	}

	public void setCode(int code) {
		add(CODE, code);
	}

	public Object getBody() {
		return get(BODY);
	}

	public void setBody(Object body) {
		put(BODY, body);
	} 
	
	public Response add(String key,Object value){
		put(key, value);
		return this;
	}


	public static Response ok(){
		Response response = new Response();
		response.setCode(1);
		response.setMsg("操作成功!");
		return response;
	}

	public static Response ok(String msg){
		Response response = new Response();
		response.setCode(1);
		response.setMsg(msg);
		return response;
	}

	public static Response fail(){
		Response response = new Response();
		response.setCode(0);
		response.setMsg("操作失败!");
		return response;
	}
	public static Response fail(String msg){
		Response response = new Response();
		response.setCode(0);
		response.setMsg(msg);
		return response;
	}



}
