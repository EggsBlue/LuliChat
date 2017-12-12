var ioc = {
		conf : {
			type : "org.nutz.ioc.impl.PropertiesProxy",
			fields : {
				paths : [ "config/" ]
			}
		},
		
		siteConf : {
			type : "org.nutz.ioc.impl.PropertiesProxy",
			fields : {
				utf8  : false,
				paths : [ "website.properties" ]
			}
		},
		currentTime : {
			type : "org.nutz.plugins.view.freemarker.directive.CurrentTimeDirective"
		},
		configuration : {
			type : "freemarker.template.Configuration"
		},
		freeMarkerConfigurer : {
			type : "org.nutz.plugins.view.freemarker.FreeMarkerConfigurer",
			events : {
				create : 'init'
			},
			args : [ {
				refer : "configuration"
			}, {
				app : '$servlet'
			}, "/", ".html", {
				refer : "freemarkerDirectiveFactory"
			} ]
		},
		freemarkerDirectiveFactory : {
			type : "org.nutz.plugins.view.freemarker.FreemarkerDirectiveFactory",
			fields : {
				freemarker : 'org/nutz/plugins/view/freemarker/freemarker.properties',
			}
		}
}