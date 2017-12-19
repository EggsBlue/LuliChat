package com.dd;

import com.dd.setup.MySetup;
import org.nutz.integration.shiro.ShiroSessionProvider;
import org.nutz.mvc.annotation.*;
import org.nutz.mvc.ioc.provider.ComboIocProvider;
import org.nutz.plugins.view.freemarker.FreemarkerViewMaker;

@Modules(scanPackage = true)
@SetupBy(MySetup.class)
@SessionBy(ShiroSessionProvider.class)
@Encoding(input = "UTF-8", output = "UTF-8")
@IocBy(type = ComboIocProvider.class,
        args = {"*js", "ioc/", "*anno", "com.dd"})
//@Views(DDView.class)
@Views(FreemarkerViewMaker.class)
@Fail("json")
public class MainModule {
}