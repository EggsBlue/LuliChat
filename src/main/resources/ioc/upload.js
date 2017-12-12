var ioc = {
	tmpFilePool : {
		type : 'org.nutz.filepool.NutFilePool',
		// 临时文件最大个数为 1000 个
		args : [ "${app.root}/WEB-INF/filetmps", 1000 ]
	},
	uploadFileContext : {
		type : 'org.nutz.mvc.upload.UploadingContext',
		singleton : false,
		args : [ {
			refer : 'tmpFilePool'
		} ],
		fields : {
			// 是否忽略空文件, 默认为 false
			ignoreNull : true,
			// 单个文件最大尺寸(大约的值，单位为字节，即 1048576 为 1M)
			maxFileSize : 1048576,
			// 正则表达式匹配可以支持的文件名
			nameFilter : '^(.+[.])(jpeg|jpg|png)$'
		}
	},
	imageUpload : {
		type : 'org.nutz.mvc.upload.UploadAdaptor',
		singleton : false,
		args : [ {
			refer : 'uploadFileContext'
		} ]
	},
	uploadTextFileContext : {
		type : 'org.nutz.mvc.upload.UploadingContext',
		singleton : false,
		args : [ {
			refer : 'tmpFilePool'
		} ],
		fields : {
			// 是否忽略空文件, 默认为 false
			ignoreNull : true,
			// 单个文件最大尺寸(大约的值，单位为字节，即 1048576 为 1M)
			maxFileSize : 1048576,
			// 正则表达式匹配可以支持的文件名
			nameFilter : '^(.+[.])(txt)$'
		}
	},
	txtUpload : {
		type : 'org.nutz.mvc.upload.UploadAdaptor',
		singleton : false,
		args : [ {
			refer : 'uploadTextFileContext'
		} ]
	}
}