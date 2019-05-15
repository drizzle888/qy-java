<!DOCTYPE HTML>
<html>
	<head>
		<meta charset="utf-8">
		<meta name="renderer" content="webkit|ie-comp|ie-stand">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
		<meta http-equiv="Cache-Control" content="no-siteapp" />
		<LINK rel="Bookmark" href="/favicon.ico">
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

		<title>添加用户 - H-ui.admin v2.3</title>
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
			<form method="post" class="form form-horizontal" id="form-account-edit" action="${base}/account/edit.do">
				<div class="Huialert Huialert-success"><i class="icon-remove"></i>个人信息详情</div>
				<div class="row cl">
					<label class="form-label col-xs-4 col-sm-3">成员ID：</label>
					<div class="formControls col-xs-8 col-sm-9">
						<input type="text" class="input-text" value="${account.memberId}" id="memberId" name="memberId" readonly style="background:#eaeae3;">
					</div>
				</div>
				<div class="row cl">
					<label class="form-label col-xs-4 col-sm-3">成员名称：</label>
					<div class="formControls col-xs-8 col-sm-9">
						<input type="text" class="input-text" value="${account.name}" id="name" name="name" readonly style="background:#eaeae3;">
					</div>
				</div>
				<div class="row cl">
				<label class="form-label col-xs-4 col-sm-3">性别：</label>
					<div class="formControls col-xs-8 col-sm-9">
						<!--<input type="text" class="input-text" value="${game.isHot}" id="isHot" name="isHot" >-->
						<span class="select-box" style="width:150px;">
							<select class="select" id="sex" name="sex" disabled style="background:#eaeae3;">
								<option value="0">女</option>
								<option value="1">男</option>
							</select>
						</span>
					</div>
				</div>
				<div class="row cl">
					<label class="form-label col-xs-4 col-sm-3">头像：</label>
					<div class="formControls col-9 col-xs-offset-3">
						<input type="file" id="file" name="file" onchange="uploadFile(this, 1)" disabled />
						<div style="width: 100px;height:100px;border:1px solid gray;">
							<div id="preview">
								<img id="imgAvatar" name="imgAvatar" src="${account.icon}" />
							</div>
							<input type="text" value="${account.icon}" id="icon" name="icon" style="opacity:0;width:0!important;height:0!important" />
						</div>
					</div>
				</div>
				<div class="row cl">
						<label class="form-label col-xs-4 col-sm-3">注册时间：</label>
							<div class="formControls col-xs-8 col-sm-9">
							<input type="text" class="input-text" value="${account.createTime}" id="createTime" name="createTime" readonly style="background:#eaeae3;">
					</div>
				</div>
				<div class="row cl">
						<label class="form-label col-xs-4 col-sm-3">登陆时间：</label>
							<div class="formControls col-xs-8 col-sm-9">
							<input type="text" class="input-text" value="${account.loginTime}" id="loginTime" name="loginTime" readonly style="background:#eaeae3;">
					</div>
				</div>
				<div class="row cl">
						<label class="form-label col-xs-4 col-sm-3">离开时间：</label>
							<div class="formControls col-xs-8 col-sm-9">
							<input type="text" class="input-text" value="${account.logoutTime}" id="logoutTime" name="logoutTime" readonly style="background:#eaeae3;">
					</div>
				</div>
				<div class="row cl">
				<label class="form-label col-xs-4 col-sm-3">设备类型：</label>
					<div class="formControls col-xs-8 col-sm-9">
						<!--<input type="text" class="input-text" value="${game.isHot}" id="isHot" name="isHot" >-->
						<span class="select-box" style="width:150px;">
							<select class="select" id="device" name="device" disabled style="background:#eaeae3;">
								<option value="0">安卓</option>
								<option value="1">苹果</option>
							</select>
						</span>
					</div>
				</div>		
						
				<div class="row cl">
					<label class="form-label col-xs-4 col-sm-3">总的登录数量：</label>
					<div class="formControls col-xs-8 col-sm-9">
						<input type="text" class="input-text" value="${account.loginCount}" id="loginCount" name="loginCount" readonly style="background:#eaeae3;">
					</div>
				</div>
				
				<div class="row cl">
					<label class="form-label col-xs-4 col-sm-3">总局数：</label>
					<div class="formControls col-xs-8 col-sm-9">
						<input type="text" class="input-text" value="${account.roundCount}" id="roundCount" name="roundCount" readonly style="background:#eaeae3;" >
					</div>
				</div>
				
				<div class="row cl">
					<label class="form-label col-xs-4 col-sm-3">金币：</label>
					<div class="formControls col-xs-8 col-sm-9">
						<input type="text" class="input-text" value="${account.gold}" id="gold" name="gold" readonly style="background:#eaeae3;">
					</div>
				</div>


				<div class="row cl">
					<label class="form-label col-xs-4 col-sm-3">幸运值：</label>
					<div class="formControls col-xs-8 col-sm-9">
						<input type="text" class="input-text" value="${account.lucky}" id="lucky" name="lucky" readonly style="background:#eaeae3;">
					</div>
				</div>
				
				<div class="row cl">
					<label class="form-label col-xs-4 col-sm-3">会场编号：</label>
					<div class="formControls col-xs-8 col-sm-9">
						<input type="text" class="input-text" value="${account.siteNo}" id="siteNo" name="siteNo" readonly style="background:#eaeae3;">
					</div>
				</div>
				
				<div class="row cl">
						<label class="form-label col-xs-4 col-sm-3">会场名称：</label>
							<div class="formControls col-xs-8 col-sm-9">
							<input type="text" class="input-text" value="${account.siteName}" id="siteName" name="siteName" readonly style="background:#eaeae3;">
					</div>
				</div>
				<div class="row cl">
				<label class="form-label col-xs-4 col-sm-3">是否是机器人：</label>
					<div class="formControls col-xs-8 col-sm-9">
						<!--<input type="text" class="input-text" value="${game.isHot}" id="isHot" name="isHot" >-->
						<span class="select-box" style="width:150px;">
							<select class="select" id="isRobot" name="isRobot" disabled style="background:#eaeae3;">
								<option value="0">否</option>
								<option value="1">是</option>
							</select>
						</span>
					</div>
				</div>
				
				<br />
				<div class="line"></div>
				<div class="row cl">
					<div class="col-xs-6 col-sm-6 col-xs-offset-6 col-sm-offset-6">
						<input onClick="layer_close();" class="btn btn-primary  radius" type="button" value="&nbsp;&nbsp;关闭&nbsp;&nbsp;">
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
				$('#sex').val(${account.sex});
				$('#device').val(${account.device});
				$('#isRobot').val(${account.isRobot});
				var createTime = getNowFormatDate(${account.createTime});
				$('#createTime').val(createTime);
				var loginTime = getNowFormatDate(${account.loginTime});
				$('#loginTime').val(loginTime);
				var logoutTime = getNowFormatDate(${account.logoutTime});
				$('#logoutTime').val(logoutTime);
				//$("input:not(:button,:hidden)").prop("disabled", "disabled");
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
		<!--/请在上方写此页面业务相关的脚本-->
	</body>

</html>