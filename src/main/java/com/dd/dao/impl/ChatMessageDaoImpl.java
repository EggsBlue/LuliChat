package com.dd.dao.impl;

import com.dd.dao.ChatMessageDao;
import com.dd.entity.msg.ChatMessage;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.pager.Pager;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Describe:历史消息DAO实现
 * Author:蛋蛋
 * Age:Eighteen
 * Time:2017年5月3日 下午2:33:18
 */
@IocBean(name="chatMessageDao")
public class ChatMessageDaoImpl implements ChatMessageDao {

	@Inject
	private Dao dao;
	
	@Override
	public int addChatMsg(ChatMessage cm) {
		ChatMessage i = dao.insert(cm);
		if(null!=i){
			return 1;
		}
		return 0;
	}
	
	@Override
	public NutMap pageMsg(int pageNo, int pageSize, int fromId, int toId, int type) {
		if(pageNo == 0){
			pageNo =1;
		}
		
		if(pageSize == 0){
			pageSize = 20;
		}
		NutMap nm = new NutMap();
		nm.addv("code", 0);
		nm.addv("message", "");
		Pager p = new Pager();
		p.setRecordCount(dao.count(ChatMessage.class, Cnd.where(ChatMessage.FROM,"=",fromId).and(ChatMessage.TOID,"=",toId).or("`from`","=",fromId).and("toid","=",toId).orderBy("timestamp", "desc")));
	
		p.setPageNumber(pageNo);
		p.setPageSize(pageSize);
//		List<ChatMessage> list = dao.query(ChatMessage.class, Cnd.where(ChatMessage.FROM,"=",fromId).and(ChatMessage.TOID,"=",toId).and(ChatMessage.TYPE,"=",type).orderBy("timestamp", "desc"), p);
		List<ChatMessage> list  = dao.query(ChatMessage.class, Cnd.where(ChatMessage.FROM,"=",fromId).and(ChatMessage.TOID,"=",toId).or("`from`","=",toId).and("toid","=",fromId).orderBy("timestamp", "dasc"),p);
		//Collections.reverse(list);
		nm.addv("pager", p);
		nm.addv("data", list);
		return nm;
	}

	@Override
	public int isUnread(int fromId) {
		List<ChatMessage> list = dao.query(ChatMessage.class, Cnd.where(ChatMessage.UNREADPOINT,"=",1).and(ChatMessage.FROM,"=",fromId));
		if(list!=null && list.size()>0){
			return list.size();
		}
		return 0;
	}

	@Override
	public void readAll(int fromId) {
		int i = dao.update(ChatMessage.class, Chain.make("unreadpoint", 0), Cnd.where(ChatMessage.FROM,"=",fromId));
		System.out.println("所有消息置为已读:"+i);	
	}

	/**
	 * 获取我的未读消息
	 * @param fromId 我的id
	 * @param username
	 * @return
	 */
	@Override
	public List<ChatMessage> getUnRead(int fromId,String username) {
//		List<ChatMessage> list = dao.query(ChatMessage.class, Cnd.where(ChatMessage.UNREADPOINT,"=",1).and(ChatMessage.TOID,"=",fromId).orderBy(ChatMessage.TIMESTAMP, "desc"));
		List<ChatMessage> unreadList = new ArrayList<ChatMessage>();
		List<ChatMessage> list = dao.query(ChatMessage.class,Cnd.where(ChatMessage.UNREADPOINT,"=",1));
		if(list!=null && list.size()>0){
			for(ChatMessage c : list){
					if(c.getType() == 1){//1.单聊
						if(c.getToid() == fromId)
							unreadList.add(c);
						this.readAll3(c);//指定消息已读
					}else{	//群聊
//						int groupid = c.getToid();
//						FlockRefUser flock = dao.fetch(FlockRefUser.class, Cnd.where(FlockRefUser.FID,"=",groupid).and(FlockRefUser.UID,"=",fromId));
//						if(flock!=null){
//							unreadList.add(c);
//						}
						
						//判断是否我在未读列表里
						String strs = c.getUnreadnumbers();
						String newstr = "";
						if(!Strings.isBlank(strs)){
							String[] numbers = strs.split(",");
							if(numbers == null || numbers.length == 0){
								this.readAll3(c);//指定消息已读
								continue;
							}
							for(int i=0; i<numbers.length; i++){
								if(numbers[i].equals(String.valueOf(fromId))){
									unreadList.add(c);
									numbers[i] = null;
								}
							}
							for(int i=0; i<numbers.length; i++){
								if(numbers[i]!=null){
									newstr +=numbers[i]+",";
								}
							}

							if(newstr.length()>0){
								newstr = newstr.substring(0, newstr.length()-1);
	            			}else{
								this.readAll3(c);//指定消息已读
							}
							c.setUnreadnumbers(newstr);
							dao.updateIgnoreNull(c);
						}else{
							this.readAll3(c);//指定消息已读
						}
				}
			}
		}
		return unreadList;
	}

	@Override
	public void readAll2(int toid) {
		int i = dao.update(ChatMessage.class, Chain.make("unreadpoint", 0), Cnd.where(ChatMessage.TOID,"=",toid));
		System.out.println("所有消息置为已读:"+i);	
	}

	@Override
	public void readAll3(ChatMessage c) {
		c.setUnreadpoint(0);
		c.setUnreadnumbers("");
		dao.updateIgnoreNull(c);
	}

}
