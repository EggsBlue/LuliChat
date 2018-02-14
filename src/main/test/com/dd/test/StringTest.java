package com.dd.test;

import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.io.resource.Resource;
import org.junit.Test;

import java.io.IOException;

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
