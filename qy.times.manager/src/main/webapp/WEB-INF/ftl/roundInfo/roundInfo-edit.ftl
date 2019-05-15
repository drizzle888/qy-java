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
			<form method="post" class="form form-horizontal" id="form-roundInfo-edit" action="${base}/roundInfo/edit.do">
				<div class="Huialert Huialert-success"><i class="icon-remove"></i>牌局详情</div>
				<div class="row cl">
					<label class="form-label col-xs-4 col-sm-3">序号：</label>
					<div class="formControls col-xs-8 col-sm-9">
						<input type="text" class="input-text" value="${roundInfo.id}" id="id" name="id" readonly style="background:#eaeae3;">
					</div>
				</div>
				<div class="row cl">
					<label class="form-label col-xs-4 col-sm-3">一号玩家Id：</label>
					<div class="formControls col-xs-8 col-sm-9">
						<input type="text" class="input-text" value="${roundInfo.memberId1}" id="memberId1" name="memberId1" >
					</div>
				</div>
				<div class="row cl">
					<label class="form-label col-xs-4 col-sm-3">二号玩家Id：</label>
					<div class="formControls col-xs-8 col-sm-9">
						<input type="text" class="input-text" value="${roundInfo.memberId2}" id="memberId2" name="memberId2" >
					</div>
				</div>
				<div class="row cl">
					<label class="form-label col-xs-4 col-sm-3">三号玩家Id：</label>
					<div class="formControls col-xs-8 col-sm-9">
						<input type="text" class="input-text" value="${roundInfo.memberId3}" id="memberId3" name="memberId3" >
					</div>
				</div>
				<div class="row cl">
					<label class="form-label col-xs-4 col-sm-3">四号玩家Id：</label>
					<div class="formControls col-xs-8 col-sm-9">
						<input type="text" class="input-text" value="${roundInfo.memberId4}" id="memberId4" name="memberId4" >
					</div>
				</div>
				<div class="row cl">
					<label class="form-label col-xs-4 col-sm-3">庄家Id：</label>
					<div class="formControls col-xs-8 col-sm-9">
						<input type="text" class="input-text" value="${roundInfo.masterId}" id="masterId" name="masterId" >
					</div>
				</div>
				<!--<div class="row cl">
					<label class="form-label col-xs-4 col-sm-3">创建时间：</label>
					<div class="formControls col-xs-8 col-sm-9">
						<input type="text" class="input-text" value="${room.createTime}" id="createTime" name="createTime" readonly style="background:#eaeae3;">
					</div>
				</div>-->
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
			$("#form-roundInfo-edit").validate({
				rules: {
					memberId1: {
						required: true,
					},
					memberId2: {
						required: true,
					},
					memberId3: {
						required: true,
					},
					memberId4: {
						required: true,
					},
					masterId: {
						required: true,
					},
				},
				messages: {
					memberId1: "请输入-一号玩家Id ",
					memberId2: "请输入-二号玩家Id ",
					memberId3: "请输入-三号玩家Id ",
					memberId4: "请输入-四号玩家Id ",
					masterId: "请输入-庄家Id "
				},
				success: "valid",
				submitHandler: function(form) {
					$.ajax({
						'cache': false,
						'type': "POST",
						'url': '${base}/roundInfo/edit.do',
						'data': $('#form-roundInfo-edit').serialize(),
						'error': function(request) {
							layer.alert('保存失败!');
						},
						'success': function(data) {
							if(data.errorCode == 0) {
								window.parent.location.reload();
							} else {
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