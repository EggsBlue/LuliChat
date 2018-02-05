package com.dd;

import com.dd.setup.InitSetup;
import org.nutz.mvc.annotation.*;

@SetupBy(InitSetup.class)
//@SessionBy(ShiroSessionProvider.class)
@Encoding(input = "UTF-8", output = "UTF-8")
//@Views(DDView.class)
public class MainModule {
}