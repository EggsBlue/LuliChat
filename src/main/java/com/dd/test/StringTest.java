package com.dd.test;

import com.xiaoleilu.hutool.io.resource.ClassPathResource;
import com.xiaoleilu.hutool.io.resource.Resource;
import org.junit.Test;
import org.nutz.lang.Streams;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class StringTest {
    @Test
    public void test1(){
        String s1 = "1";
        String s2 = "1";
        int s3 = 1;
        System.out.println(s1.equals(s3));
    }


    @Test
    public void test2() throws IOException {
//        StringBuilder sb = new StringBuilder();
//        InputStream in = this.getClass().getResourceAsStream("/ehcache.xml");
        System.out.println(this.getClass().getResource("/"));
        Resource res = new ClassPathResource("/");
        System.out.println(res.getUrl().getFile());
//        File f = new File(res.getUrl().getFile());
//        String[] files = f.list();
//        for (String file : files) {
//            System.out.println(file);
//        }
        ;
//        System.out.println(new String(Streams.readBytes(in),"utf-8"));
    }
}
