package com.dd.view;

import org.nutz.ioc.Ioc;
import org.nutz.mvc.View;
import org.nutz.mvc.view.DefaultViewMaker;

public class DDView extends DefaultViewMaker{
    public static final String VIEW_FAIL = "fail";
    public static final String VIEW_FREEMAKER = "fm";

    public DDView() {
    }

    @Override
    public View make(Ioc ioc, String type, String value) {
        if (VIEW_FREEMAKER.equals(type)) {
            return new FreeMakerView(value);
        }
        return super.make(ioc, type, value);
    }

}
