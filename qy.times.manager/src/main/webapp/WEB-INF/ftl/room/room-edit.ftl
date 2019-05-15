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
			<form method="post" class="form form-horizontal" id="form-room-edit" action="${base}/room/edit.do">
				<div class="Huialert Huialert-success"><i class="icon-remove"></i>房间详情</div>
				<div class="row cl">
					<label class="form-label col-xs-4 col-sm-3">序号：</label>
					<div class="formControls col-xs-8 col-sm-9">
						<input type="text" class="input-text" value="${room.id}" id="id" name="id" readonly style="background:#eaeae3;">
					</div>
				</div>
				<div class="row cl">
					<label class="form-label col-xs-4 col-sm-3">创建者Id：</label>
					<div class="formControls col-xs-8 col-sm-9">
						<input type="text" class="input-text" value="${room.createMemberId}" id="createMemberId" name="createMemberId" >
					</div>
				</div>
				<div class="row cl">
					<label class="form-label col-xs-4 col-sm-3">局数：</label>
					<div class="formControls col-xs-8 col-sm-9">
						<input type="text" class="input-text" value="${room.roundCount}" id="roundCount" name="roundCount">
					</div>
				</div>
				<div class="row cl">
					<label class="form-label col-xs-4 col-sm-3">房间类型：</label>
					<div class="formControls col-xs-8 col-sm-9">
						<span class="select-box" style="width:150px;">
							<select class="select" id="type" name="type">
								<option value="1">临时房间</option>
								<option value="2">包间</option>
							</select>
						</span>
					</div>
				</div>
				<div class="row cl">
					<label class="form-label col-xs-4 col-sm-3">底分：</label>
					<div class="formControls col-xs-8 col-sm-9">
						<input type="text" class="input-text" value="${room.lowGold}" id="lowGold" name="lowGold">
					</div>
				</div>
				<div class="row cl">
					<label class="form-label col-xs-4 col-sm-3">入场金币限制：</label>
					<div class="formControls col-xs-8 col-sm-9">
						<input type="text" class="input-text" value="${room.limitGold}" id="limitGold" name="limitGold">
					</div>
				</div>
				<div class="row cl">
					<label class="form-label col-xs-4 col-sm-3">金顶：</label>
					<div class="formControls col-xs-8 col-sm-9">
						<input type="text" class="input-text" value="${room.manyGold}" id="manyGold" name="manyGold">
					</div>
				</div>
				<div class="row cl">
					<label class="form-label col-xs-4 col-sm-3">封顶：</label>
					<div class="formControls col-xs-8 col-sm-9">
						<input type="text" class="input-text" value="${room.personGold}" id="personGold" name="personGold">
					</div>
				</div>
				<div class="row cl">
					<label class="form-label col-xs-4 col-sm-3">房间号：</label>
					<div class="formControls col-xs-8 col-sm-9">
						<input type="text" class="input-text" value="${room.number}" id="number" name="number">
					</div>
				</div>
				<!--<div class="row cl">
					<label class="form-label col-xs-4 col-sm-3">玩法类型：</label>
					<div class="formControls col-xs-8 col-sm-9">
						<span class="select-box" style="width:150px;">
							<select class="select" id="playMethod" name="playMethod">
								<option value="1">玩法一</option>
								<option value="2">玩法二</option>
							</select>
						</span>
					</div>
				</div>-->
				<div class="row cl">
					<label class="form-label col-xs-4 col-sm-3">封顶分数：</label>
					<div class="formControls col-xs-8 col-sm-9">
						<input type="text" class="input-text" value="${room.capScore}" id="capScore" name="capScore">
					</div>
				</div>
				<div class="row cl">
					<label class="form-label col-xs-4 col-sm-3">高低分区：</label>
					<div class="formControls col-xs-8 col-sm-9">
						<span class="select-box" style="width:150px;">
							<select class="select" id="grade" name="grade">
								<option value="1">低分区</option>
								<option value="2">高分区</option>
							</select>
						</span>
					</div>
				</div>
				<div class="row cl">
					<label class="form-label col-xs-4 col-sm-3">模板Id：</label>
					<div class="formControls col-xs-8 col-sm-9">
						<input type="text" class="input-text" value="${room.roomTemplateId}" id="roomTemplateId" name="roomTemplateId">
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
				$('#type').val(${room.type});
				//$('#playMethod').val(${room.playMethod});
				$('#grade').val(${room.grade});
			});
			
			$("#form-room-edit").validate({
				rules: {
					createMemberId: {
						required: true,
					},
					roundCount:    {
						digits: true,
					},
					type: {
						required: true,
					},
					lowGold:      {
						digits: true,
					},
					limitGold:    {
						digits: true,
					},
					manyGold: {
						digits: true,
					},
					personGold: {
						digits: true,
					},
					number:    {
						required: true,
					},
					/*playMethod: {
						required: true,
					},*/
					capScore: {
						digits: true,
					},
					grade: {
						required: true,
					},
					roomTemplateId: {
						required: true,
					},
				},
				messages: {
					createMemberId: "请输入-创建者Id ",
					roundCount: "请输入-局数 ",
					type: "请选择-房间类型 ",
					lowGold: "请输入-整数底分 ",
					limitGold: "请输入-整数入场金币限制 ",
					manyGold: "请输入-整数金顶金币数 ",
					personGold: "请输入-整数封顶金币数 ",
					number: "请输入- 房间号",
					//playMethod: "请选择-玩法 ",
					capScore: "请输入-整数封顶分数 ",
					grade: "请选择-高低分区 ",
					roomTemplateId: "请输入模板Id "
				},
				success: "valid",
				submitHandler: function(form) {
					$.ajax({
						'cache': false,
						'type': "POST",
						'url': '${base}/room/edit.do',
						'data': $('#form-room-edit').serialize(),
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