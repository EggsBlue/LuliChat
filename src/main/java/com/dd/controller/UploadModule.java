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
		System.out.println(file.getMeta().getFileLocalName());
		InputStream in = null;
		OutputStream out = null;
		File f = file.getFile();
//		System.out.println("目录:"+context.getContextPath());	//获取项目名
//		System.out.println("目录2:"+context.getRealPath("/"));
		String tomcatPath =context.getRealPath("/");//获取到tomcat位于系统的绝对磁盘路径,//此为:D:\apache-tomcat-8.0.36\webapps\mychat\
		tomcatPath = tomcatPath.substring(0,tomcatPath.length()-1);//此为:D:\apache-tomcat-8.0.36\webapps\mychat
		tomcatPath = tomcatPath.substring(0,tomcatPath.lastIndexOf("\\"));//此为:D:\apache-tomcat-8.0.36\webapps
		String relpath = tomcatPath+"\\upload\\imgs\\"+file.getMeta().getFileLocalName(); // 此为: D:\\apache-tomcat-8.0.36\\webapps\\upload\\tomat.png
		System.out.println("tomcatPath:"+tomcatPath);

		Files.copy(file.getFile(),new File(relpath));

		String url ="/upload/imgs/"+file.getMeta().getFileLocalName();	//eclipse默认的tomcat目录是在其缓存文件中,你要自己指定到tomcat所在目录
		System.out.println(url);
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
		System.out.println(file.getMeta().getFileLocalName());
		InputStream in = null;
		OutputStream out = null;
		File f = file.getFile();
//		System.out.println("目录:"+context.getContextPath());	//获取项目名
//		System.out.println("目录2:"+context.getRealPath("/"));
		String tomcatPath =context.getRealPath("/");//获取到tomcat位于系统的绝对磁盘路径,//此为:D:\apache-tomcat-8.0.36\webapps\mychat\
		tomcatPath = tomcatPath.substring(0,tomcatPath.length()-1);//此为:D:\apache-tomcat-8.0.36\webapps\mychat
		tomcatPath = tomcatPath.substring(0,tomcatPath.lastIndexOf("\\"));//此为:D:\apache-tomcat-8.0.36\webapps
		String relpath = tomcatPath+"\\upload\\files\\"+file.getMeta().getFileLocalName(); // 此为: D:\\apache-tomcat-8.0.36\\webapps\\upload\\tomat.png
		System.out.println("tomcatPath:"+tomcatPath);

		Files.copy(file.getFile(),new File(relpath));

		String url ="/upload/files/"+file.getMeta().getFileLocalName();	//eclipse默认的tomcat目录是在其缓存文件中,你要自己指定到tomcat所在目录
		System.out.println(url);
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
	public void test(){
		System.out.println("lalala");
	}
	
}
