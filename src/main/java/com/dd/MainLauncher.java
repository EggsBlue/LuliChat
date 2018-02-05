package com.dd;

import org.nutz.boot.NbApp;
import org.nutz.ioc.loader.annotation.IocBean;

@IocBean
public class MainLauncher {

	public static void main(String[] args) {
		new NbApp().setMainClass(MainModule.class).setPrintProcDoc(true).run();
	}

}
