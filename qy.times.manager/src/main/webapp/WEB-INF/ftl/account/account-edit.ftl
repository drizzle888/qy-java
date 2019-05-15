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
						<input type="text" class="input-text" value="${account.name}" id="name" name="name" >
					</div>
				</div>
				<div class="row cl">
				<label class="form-label col-xs-4 col-sm-3">性别：</label>
					<div class="formControls col-xs-8 col-sm-9">
						<span class="select-box" style="width:150px;">
							<select class="select" id="sex" name="sex" >
								<option value="0">女</option>
								<option value="1">男</option>
							</select>
						</span>
					</div>
				</div>
				<div class="row cl">
					<label class="form-label col-xs-4 col-sm-3">头像：</label>
					<div class="formControls col-9 col-xs-offset-3">
						<input type="file" id="file" name="file" onchange="uploadFile(this, 1)"  />
						<div style="width: 100px;height:100px;border:1px solid gray;">
							<div id="preview">
								<img id="imgAvatar" name="imgAvatar" src="${account.icon}" />
							</div>
							<input type="text" value="${account.icon}" id="icon" name="icon" style="opacity:0;width:0!important;height:0!important" />
						</div>
					</div>
				</div>
				<!--<div class="row cl">
						<label class="form-label col-xs-4 col-sm-3">创建时间：</label>
							<div class="formControls col-xs-8 col-sm-9">
							<input type="text" class="input-text" value="${account.createTime}" id="createTime" style="background:#eaeae3;">
					</div>
				</div>
				<div class="row cl">
						<label class="form-label col-xs-4 col-sm-3">登陆时间：</label>
							<div class="formControls col-xs-8 col-sm-9">
							<input type="text" class="input-text" value="${account.loginTime}" id="loginTime" style="background:#eaeae3;">
					</div>
				</div>
				<div class="row cl">
						<label class="form-label col-xs-4 col-sm-3">离开时间：</label>
							<div class="formControls col-xs-8 col-sm-9">
							<input type="text" class="input-text" value="${account.logoutTime}" id="logoutTime" style="background:#eaeae3;">
					</div>
				</div>-->
				<div class="row cl">
				<label class="form-label col-xs-4 col-sm-3">设备类型：</label>
					<div class="formControls col-xs-8 col-sm-9">
						<span class="select-box" style="width:150px;">
							<select class="select" id="device" name="device">
								<option value="0">安卓</option>
								<option value="1">苹果</option>
							</select>
						</span>
					</div>
				</div>		
				<div class="row cl">
					<label class="form-label col-xs-4 col-sm-3">总的登录数量：</label>
					<div class="formControls col-xs-8 col-sm-9">
						<input type="text" class="input-text" value="${account.loginCount}" id="loginCount" name="loginCount" >
					</div>
				</div>
				<div class="row cl">
					<label class="form-label col-xs-4 col-sm-3">总局数：</label>
					<div class="formControls col-xs-8 col-sm-9">
						<input type="text" class="input-text" value="${account.roundCount}" id="roundCount" name="roundCount" >
					</div>
				</div>
				<div class="row cl">
					<label class="form-label col-xs-4 col-sm-3">金币：</label>
					<div class="formControls col-xs-8 col-sm-9">
						<input type="text" class="input-text" value="${account.gold}" id="gold" name="gold">
					</div>
				</div>
				<div class="row cl">
					<label class="form-label col-xs-4 col-sm-3">幸运值：</label>
					<div class="formControls col-xs-8 col-sm-9">
						<input type="text" class="input-text" value="${account.lucky}" id="lucky" name="lucky" >
					</div>
				</div>
				<div class="row cl">
					<label class="form-label col-xs-4 col-sm-3">会场编号：</label>
					<div class="formControls col-xs-8 col-sm-9">
						<input type="text" class="input-text" value="${account.siteNo}" id="siteNo" name="siteNo">
					</div>
				</div>
				<div class="row cl">
					<label class="form-label col-xs-4 col-sm-3">会场名称：</label>
					<div class="formControls col-xs-8 col-sm-9">
						<input type="text" class="input-text" value="${account.siteName}" id="siteName" name="siteName">
					</div>
				</div>
				<div class="row cl">
				<label class="form-label col-xs-4 col-sm-3">是否是机器人：</label>
					<div class="formControls col-xs-8 col-sm-9">
						<span class="select-box" style="width:150px;">
							<select class="select" id="isRobot" name="isRobot">
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
						<input class="btn btn-primary radius" type="submit" value="&nbsp;&nbsp;保存&nbsp;&nbsp;">
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
				/*var gold = parseFloat(${account.gold}).toFixed(2);
				$('#gold').val(gold);*/
			});
			
			 $("#form-account-edit").validate({
			 rules:{
			      name:{
			        required:true,
			      },
			      sex:{
			        required:true,
			      },
			      icon:{
			        required:true,
			      },
			      device:{
			      	required:true,
			      },
			      loginCount:{
			        required:true,
			      },
			      roundCount:{
			      	required:true,
			      },
			      gold:{
			      	digits:true,  //验证整数
			      },
			      lucky:{
			      	required:true,
			      },
			      siteNo:{
			      	required:true,
			      },
			      siteName:{
			      	required:true,
			      },
			      isRobot:{
			      	required:true,
			      }
			     
			    },
			    messages: {
			    	name: "请输入-成员名称",
			    	sex: "请选择-性别 ",
			    	icon: "请选择-头像",
			    	device: "请选择-设备类型",
			    	loginCount: "请输入-登录次数",
			    	roundCount: "请输入-总局数",
			    	gold: "请输入-整数金币数" ,  
			    	lucky: "请输入-幸运值",
			    	siteNo: "请输入-会场编号 ",
			    	siteName: "请输入-会场名称",
			    	isRobot: "请选择-是否是机器人 "
			    },
			   /* errorPlacement: function(error, element) {
					if (element.attr("name") == "logo") {
						error.appendTo(element.parent());
					}
				},*/
			    success:"valid",
			    submitHandler: function(form){
			      $.ajax({
				'cache': false,
			        'type': "POST",
			        'url' : '${base}/account/edit.do',
			        'data': $('#form-account-edit').serialize(),
			        'error': function(request) {
			              layer.alert('保存失败!');
			        },
			        'success': function(data) {
			            if(data.errorCode == 0){
			              window.parent.location.reload();
			            }else{
			              layer.alert(data.errorInfo);
			            }
			        }
			      });
			    }
			  });
			
		function uploadFile(self, type) {  
	   		var avatarUrl = '${account.icon}';
    		var alias = avatarUrl.substring(avatarUrl.lastIndexOf("/") + 1);
			    $.ajaxFileUpload({
			        url : '${base}/file/accoUpload.do',
			        secureuri : false,
			        fileElementId : "file",
			        dataType : 'text',
			        data: {'type': type, "type2": 2, "alias": alias},  
			        success : function(data) {
			        	console.log(data);
			        	$('#imgAvatar').attr('src',data);
			        	$('#icon').val(data);
			        },  
			        error : function(data, status, e) {  
						console.log(e);  
			        }  
			    });  
			    return false;
			}
	    
		</script>
		<!--/请在上方写此页面业务相关的脚本-->
	</body>

</html>