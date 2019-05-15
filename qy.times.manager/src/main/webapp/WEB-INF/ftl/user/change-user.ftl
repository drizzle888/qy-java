<!DOCTYPE HTML>
<html>
<head>
	<meta charset="utf-8">
	<meta name="renderer" content="webkit|ie-comp|ie-stand">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
	<meta http-equiv="Cache-Control" content="no-siteapp" />
	<LINK rel="Bookmark" href="/favicon.ico" >
	<LINK rel="Shortcut Icon" href="/favicon.ico" />
	<!--[if lt IE 9]>
	<script type="text/javascript" src="lib/html5.js"></script>
	<script type="text/javascript" src="lib/respond.min.js"></script>
	<script type="text/javascript" src="lib/PIE_IE678.js"></script>
	<![endif]-->
	<link rel="stylesheet" type="text/css" href="${base}/static/h-ui/css/H-ui.min.css" />
	<link rel="stylesheet" type="text/css" href="${base}/static/h-ui.admin/css/H-ui.admin.css" />
	<link rel="stylesheet" type="text/css" href="${base}/lib/Hui-iconfont/1.0.8/iconfont.css" />
	<link rel="stylesheet" type="text/css" href="${base}/lib/icheck/icheck.css" />
	<link rel="stylesheet" type="text/css" href="${base}/static/h-ui.admin/skin/default/skin.css" id="skin" />
	<link rel="stylesheet" type="text/css" href="${base}/static/h-ui.admin/css/style.css" />
	<!--[if IE 6]>
	<script type="text/javascript" src="http://lib.h-ui.net/DD_belatedPNG_0.0.8a-min.js" ></script>
	<script>DD_belatedPNG.fix('*');</script>
	<![endif]-->
	<!--/meta 作为公共模版分离出去-->

	<title>编辑游戏 - H-ui.admin v2.3</title>
	<meta name="keywords" content="H-ui.admin v2.3,H-ui网站后台模版,后台模版下载,后台管理系统模版,HTML后台模版下载">
	<meta name="description" content="H-ui.admin v2.3，是一款由国人开发的轻量级扁平化网站后台模板，完全免费开源的网站后台管理系统模版，适合中小型CMS后台系统。">
</head>
	<style>
        #preview {
            width: 100px;
            height: 100px;
            overflow: hidden;
        }
        #preview img {
            width: 100%;
            height: 100%;
        }
        .Huifold .item{ position:relative}
		.Huifold .item h4{margin:0;font-weight:bold;position:relative;border-top: 1px solid #fff;font-size:15px;line-height:22px;padding:7px 10px;background-color:#eee;cursor:pointer;padding-right:30px}
		.Huifold .item h4 b{position:absolute;display: block; cursor:pointer;right:10px;top:7px;width:16px;height:16px; text-align:center; color:#666}
		.Huifold .item .info{display:none;padding:10px}
    </style>
<body>
	<input type="hidden" value="${base}" id="base" name="base" />
	<input type="hidden" value="${contextPath}" id="contextPath" name="contextPath" />
	<article class="page-container">
					<h4>个人设置 :<b></b></h4>
					<div class="info">
						<form class="form form-horizontal" id="form-chaneg-user" action="${base}/user/changeUser.do" >
							<div class="row cl">
								<label class="form-label col-xs-3 col-sm-2">用户Id：</label>
								<div class="formControls col-xs-5 col-sm-4">
									<input type="text" class="input-text" value="${user.id}" id="id" name="id"  style="background:#eaeae3;">
								</div>
							</div>
							<div class="row cl">
								<label class="form-label col-xs-3 col-sm-2">用户名：</label>
								<div class="formControls col-xs-5 col-sm-4">
									<input type="text" class="input-text" value="${user.username}" id="username" name="username" style="background:#eaeae3;">
								</div>
							</div>
							<div class="row cl">
								<label class="form-label col-xs-3 col-sm-2">真实姓名：</label>
								<div class="formControls col-xs-5 col-sm-4">
									<input type="text" class="input-text" value="${user.realName}" id="realName" name="realName">
								</div>
							</div>
							<div class="row cl">
								<label class="form-label col-xs-3 col-sm-2">上次登录时间：</label>
								<div class="formControls col-xs-5 col-sm-4">
									<input type="text" class="input-text" value="${user.lastLoginTime}" id="lastLoginTime" name="lastLoginTime" style="background:#eaeae3;">
								</div>
							</div>
							</br>
							<div class="line"></div>
							<div class="row cl">
								<div class="col-xs-6 col-sm-6 col-xs-offset-6 col-sm-offset-6">
									<button class="btn btn-primary radius" type="submit"><i class="Hui-iconfont">&#xe632;</i> 保存</button>
								</div>
							</div>
						</form>
					</div>
	</article>

	<!--_footer 作为公共模版分离出去-->
	<script type="text/javascript" src="${base}/lib/jquery/1.9.1/jquery.min.js"></script>
	<script type="text/javascript" src="${base}/lib/layer/2.4/layer.js"></script>
	<script type="text/javascript" src="${base}/lib/icheck/jquery.icheck.min.js"></script>
	<script type="text/javascript" src="${base}/lib/jquery.validation/1.14.0/jquery.validate.js"></script>
	<script type="text/javascript" src="${base}/lib/datatables/1.10.0/jquery.dataTables.min.js"></script>
	<script type="text/javascript" src="${base}/lib/jquery.validation/1.14.0/validate-methods.js"></script>
	<script type="text/javascript" src="${base}/lib/jquery.validation/1.14.0/messages_zh.js"></script>
	<script type="text/javascript" src="${base}/static/h-ui/js/H-ui.js"></script>
	<script type="text/javascript" src="${base}/static/h-ui.admin/js/H-ui.admin.js"></script>
	<script type="text/javascript" src="${base}/static/ajaxfileupload.js"></script>
	<!--/_footer /作为公共模版分离出去-->

	<!--请在下方写此页面业务相关的脚本-->
<script>
	$(document).ready(function() {
		$('#id').val(${user.id});
		//$('#username').val(${user.username});
		//$('#realName').val(${user.realName});
		var lastLoginTime = getNowFormatDate(${user.lastLoginTime});
		$('#lastLoginTime').val(lastLoginTime);
	});
	
	 $("#form-chaneg-user").validate({
	 rules:{
	      realName:{
	        required:true,
	      }
	    },
	    messages: {
	    	realName: "请输入-真实姓名"
	    },
	    success:"valid",
	    submitHandler: function(form){
	      $.ajax({
			'cache': false,
	        'type': "POST",
	        'url' : '${base}/user/changeUser.do',
	        'data': $('#form-chaneg-user').serialize(),
	        'error': function(request) {
	              layer.alert('保存失败!');
	        },
	        'success': function(data) {
	            if(data.errorCode == 0){
					layer.msg('保存成功');
					window.location.reload();
	            }else{
					layer.alert(data.errorInfo);
	            }
	        }
	      });
	    }
	  });
	  
	  
	  function getNowFormatDate(lastLoginTime) {
			//debugger;
				lastLoginTime = lastLoginTime * 1000;
				var date = new Date(lastLoginTime);
				var seperator1 = "-";
				var seperator2 = ":";
				var month = date.getMonth() + 1;
				var strDate = date.getDate();
				var strMin = date.getMinutes();
				var strSec = date.getSeconds();
				if(month >= 1 && month <= 9) {
					month = "0" + month;
				}
				if(strDate >= 0 && strDate <= 9) {
					strDate = "0" + strDate;
				}
				if(strMin >= 0 && strMin <= 9){
					strMin = "0" +strMin;
				}
				if(strSec >= 0 && strSec <= 9){
					strSec = "0" +strSec;
				}
				var cTime = date.getFullYear() + seperator1 + month + seperator1 + strDate +
					" " + date.getHours() + seperator2 + strMin +
					seperator2 + strSec;
				return cTime;
			}
	  
</script>
	<!--/控制列表栏显示-->
</body>
</html>