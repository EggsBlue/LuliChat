var ioc = {
	dataSource : {
		type : "com.alibaba.druid.pool.DruidDataSource",
		events : {
			create : "init",
			depose : "close"
		},
		fields : {
			driverClassName : {
				java : "$conf.get('db.driver')"
			},
			url : {
				java : "$conf.get('db.url')"
			},
			username : {
				java : "$conf.get('db.username')"
			},
			password : {
				java : "$conf.get('db.password')"
			},
			maxActive : {
				java : "$conf.get('db.maxActive')"
			},
			testWhileIdle : true,
			validationQuery : {
				java : "$conf.get('db.validationQuery')"
			},
			filters : "mergeStat",
			connectionProperties : "druid.stat.slowSqlMillis=2000"
		}
	},
	fileSqlManager : {
		type : "org.nutz.dao.impl.FileSqlManager",
		args : [ "sql" ]
	},
	dao : {
		type : "org.nutz.dao.impl.NutDao",
		args : [ {
			refer : 'dataSource'
		} ],
		fields : {
			sqlManager : {
				refer : 'fileSqlManager'
			}
		}
	}
}