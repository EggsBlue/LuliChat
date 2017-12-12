package com.dd.test;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;
import org.nutz.dao.Cnd;
import org.nutz.dao.sql.OrderBy;
import org.nutz.dao.sql.PItem;

public class CndTest {

    @Test
    public void test1(){

        Cnd aNew = Cnd.NEW();
        OrderBy by = aNew.orderBy("name", "desc");
        PItem item = copy(by);
//        System.out.println(aaa.toString());

    }

    private static PItem copy(PItem where) {
        byte[] outwhere = SerializationUtils.serialize(where);
        PItem pItem = SerializationUtils.deserialize(outwhere);
        return pItem;
    }
}
