package com.dd.test;

import com.dd.entity.msg.SendMessage;
import org.junit.Test;
import org.nutz.json.Json;

public class JsonTest {

    @Test
    public void test1(){

//        Flock f  = new Flock();
//        f.setAvatar("111");
//        f.setGroupname("2222");
//        String s = Json.toJson(f);




        SendMessage sendMessage = new SendMessage();
        sendMessage.setUsername("");
        sendMessage.setAvatar("");
        sendMessage.setId("");
        sendMessage.setType("friend");
        sendMessage.setContent("");
//                    		if(to.get("username").equals(username)){
//                    			sendMessage.setMine(true);
//                    		}else{
//                    			sendMessage.setMine(false);
//                    		}
        sendMessage.setFromid("");
        sendMessage.setTimestamp(System.currentTimeMillis());
        String strJson = Json.toJson(sendMessage);



        System.out.println(strJson);
    }

}
