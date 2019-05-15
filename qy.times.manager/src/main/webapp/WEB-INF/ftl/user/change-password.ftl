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

	<title>添加成员 - H-ui.admin v2.3</title>
	<meta name="keywords" content="H-ui.admin v2.3,H-ui网站后台模版,后台模版下载,后台管理系统模版,HTML后台模版下载">
	<meta name="description" content="H-ui.admin v2.3，是一款由国人开发的轻量级扁平化网站后台模板，完全免费开源的网站后台管理系统模版，适合中小型CMS后台系统。"></head>
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
    </style>
<body>
	<input type="hidden" value="${base}" id="base" name="base" />
	<input type="hidden" value="${contextPath}" id="contextPath" name="contextPath" />
	<article class="page-container">
		<form method="post" class="form form-horizontal" id="form-chaneg-password">
			<div class="row cl">
				<label class="form-label col-xs-4 col-sm-3">请输入原密码：</label>
				<div class="formControls col-xs-4 col-sm-3">
					<input type="password" class="input-text" id="oldPassword" name="oldPassword"></div>
			</div>
			<div class="row cl">
				<label class="form-label col-xs-4 col-sm-3">请输入新密码：</label>
				<div class="formControls col-xs-4 col-sm-3">
					<input type="password" class="input-text" id="newPassword" name="newPassword"></div>
			</div>
			
			<div class="row cl">
				<label class="form-label col-xs-4 col-sm-3">请确认新密码：</label>
				<div class="formControls col-xs-4 col-sm-3">
					<input type="password" class="input-text" id="confirmPassword" name="confirmPassword"></div>
			</div>
			
			<br />
			<div class="line"></div>
			<div class="row cl">
				<div class="col-xs-6 col-sm-6 col-xs-offset-4 col-sm-offset-4">
					<input class="btn btn-primary radius" type="submit" value="&nbsp;&nbsp;保存&nbsp;&nbsp;">
					<!--<input class="btn btn-primary  radius" type="submit" value="&nbsp;&nbsp;提交&nbsp;&nbsp;">-->
				</div>
			</div>
		</form>
	</article>

	<!--_footer 作为公共模版分离出去-->
	<script type="text/javascript" src="${base}/lib/jquery/1.9.1/jquery.min.js"></script>
	<script type="text/javascript" src="${base}/lib/layer/2.4/layer.js"></script>
	<script type="text/javascript" src="${base}/lib/icheck/jquery.icheck.min.js"></script>
	<script type="text/javascript" src="${base}/lib/jquery.validation/1.14.0/jquery.validate.js"></script>
	<script type="text/javascript" src="${base}/lib/jquery.validation/1.14.0/validate-methods.js"></script>
	<script type="text/javascript" src="${base}/lib/jquery.validation/1.14.0/messages_zh.js"></script>
	<script type="text/javascript" src="${base}/static/h-ui/js/H-ui.js"></script>
	<script type="text/javascript" src="${base}/static/h-ui.admin/js/H-ui.admin.js"></script>
	<script type="text/javascript" src="${base}/static/ajaxfileupload.js"></script>
	<!--/_footer /作为公共模版分离出去-->

	<!--请在下方写此页面业务相关的脚本-->
<script>
	
	$(document).ready(function() {
		  addItems();
	});
	
	 $("#form-chaneg-password").validate({
	 rules:{
	   	   oldPassword:{
	        required:true,
	      },
	       newPassword:{
	        required:true,
	      },
	      confirmPassword:{
	        required:true,
	      },
	    },
	    messages: {
	    	oldPassword: "请输入-原密码",
	    	newPassword: "请输入-新密码",
	    	confirmPassword: "请输入-确认新密码"
	    },
	    /*errorPlacement: function(error, element) {
			if (element.attr("name") == "logo") {
				error.appendTo(element.parent());
			}
		},*/
	    success:"valid",
	    submitHandler:function(form){
	      $.ajax({
	        'cache': false,
	        'type': "POST",
	        'url' : '${base}/user/changePassword.do',
	        'data': {"oldPassword": $('#oldPassword').val(), "newPassword": $('#newPassword').val(), "confirmPassword":$('#confirmPassword').val()},
	        'error': function(request) {
	              layer.alert('保存失败!');
	        },
	        'success': function(data) {
	            if(data.errorCode == 0){
		            var title = "修改密码";
		            var url = $("#base").val() + '/user/goLogin.do?';
					layer_show(title, url, '', '');
	            }else{
	                layer.alert(data.errorInfo);
	            }
	        }
	      });
	    }
	  });
</script>
	<!--/请在上方写此页面业务相关的脚本-->
</body>
</html>