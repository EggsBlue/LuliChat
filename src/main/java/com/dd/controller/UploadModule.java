package com.dd.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Files;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.AdaptBy;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.POST;
import org.nutz.mvc.annotation.Param;
import org.nutz.mvc.filter.CrossOriginFilter;
import org.nutz.mvc.upload.TempFile;
import org.nutz.mvc.upload.UploadAdaptor;

/**
 * 文件上传
 *@Author: 蛋蛋i
 *@Time:2017/11/28 13:27
 */
@IocBean
@At("/upload")
@Ok("json")
public class UploadModule {

	/**
	 * 发送图片,上传图片接口
	 * @param file
	 * @param context
	 * @return
	 */
	@At
	@POST
	@Filters({@By(type=CrossOriginFilter.class)})
	@AdaptBy(type = UploadAdaptor.class, args = { "${app.root}/WEB-INF/tmp" })
	public Object image(@Param("file") TempFile file,ServletContext context){
		System.out.println(file.getName());
		System.out.println(file.getSubmittedFileName());
		String relpath = getDir()+"/upload/imgs/"+file.getSubmittedFileName(); // 此为: D:\\apache-tomcat-8.0.36\\webapps\\upload\\tomat.png
		Files.copy(file.getFile(),new File(relpath));

		String url ="/upload/imgs/"+file.getSubmittedFileName();	//eclipse默认的tomcat目录是在其缓存文件中,你要自己指定到tomcat所在目录
		//构建json数据
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("code", "0");
		data.put("msg", "");
		Map<String,String> sourceUrl = new HashMap<String,String>();
		sourceUrl.put("src", url);
		data.put("data", sourceUrl);
		
		return data;
	}

	/**
	 * 发送图片,上传图片接口
	 * @param file
	 * @param context
	 * @return
	 */
	@At
	@POST
	@Filters({@By(type=CrossOriginFilter.class)})
	@AdaptBy(type = UploadAdaptor.class, args = { "${app.root}/WEB-INF/tmp" })
	public Object files(@Param("file") TempFile file,ServletContext context){
		System.out.println(file.getName());
		System.out.println(file.getSubmittedFileName());
		String relpath = getDir()+"/upload/files/"+file.getSubmittedFileName(); // 此为: D:\\apache-tomcat-8.0.36\\webapps\\upload\\tomat.png
		Files.copy(file.getFile(),new File(relpath));
		String url ="/upload/files/"+file.getSubmittedFileName();	//eclipse默认的tomcat目录是在其缓存文件中,你要自己指定到tomcat所在目录
		//构建json数据
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("code", "0");
		data.put("msg", "");
		Map<String,String> sourceUrl = new HashMap<String,String>();
		sourceUrl.put("src", url);
		data.put("data", sourceUrl);

		return data;
	}
	
	
	@At
	public Object test(){
		NutMap nm = new NutMap();
		String contextPath = Mvcs.getServletContext().getContextPath();
		String realPath = Mvcs.getServletContext().getRealPath("/");
		String parent = new File(realPath).getParent();
		nm.setv("contextPath",contextPath);
		nm.setv("realPath",realPath);
		nm.setv("parent",parent);
		return nm;
	}

	Log log = Logs.get();

	public String getDir(){
		String realPath = Mvcs.getServletContext().getRealPath("/");
		String parent = new File(realPath).getParent();
		log.debug("uploadDir:"+parent);
		return parent;
	}
	
}
