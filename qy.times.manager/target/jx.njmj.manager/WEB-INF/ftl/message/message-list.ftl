<!DOCTYPE HTML>
<html>
	<head>
		<meta charset="utf-8">
		<meta name="renderer" content="webkit|ie-comp|ie-stand">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
		<meta http-equiv="Cache-Control" content="no-siteapp" />
		<!--[if lt IE 9]>
	<script type="text/javascript" src="${base}/lib/html5.js"></script>
	<script type="text/javascript" src="${base}/lib/respond.min.js"></script>
	<script type="text/javascript" src="${base}/lib/PIE_IE678.js"></script>
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
		<title>好友列表</title>
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
		<input type="hidden" class="input-text" value="${base}" placeholder="" id="base" name="base" readonly/>
		<input type="hidden" class="input-text" value="${contextPath}" placeholder="" id="contextPath" name="contextPath" readonly/>
		<nav class="breadcrumb"> <i class="Hui-iconfont">&#xe67f;</i> 首页
			<span class="c-gray en">&gt;</span> 信息管理
			<span class="c-gray en">&gt;</span> 信息列表
			<a class="btn btn-success radius r mr-20" style="line-height:1.6em;margin-top:3px" href="javascript:location.replace(location.href);" title="刷新"> <i class="Hui-iconfont">&#xe68f;</i>
			</a>
		</nav>
		<div class="pd-20">
			<div class="text-c">
				<input type="text" name="fromId" id="fromId" placeholder=" 发送人ID" style="width:300px" class="input-text" value="${fromId}">
				<input type="text" name="roomId" id="roomId" placeholder=" 房间Id" style="width:300px" class="input-text" value="${roomId}">
				<button name="" id="btn_search" class="btn btn-success" type="submit">
				<i class="Hui-iconfont">&#xe665;</i>
				查询
			</button>
				<button name="" id="btn_add" class="btn btn-success" type="submit" disabled="disabled" style="background:#eaeae3;">
				<i class="Hui-iconfont">&#xe600;</i> 添加
			</button>
			</div>
			<div class="mt-20">
				<table id="messageTable" class="table table-border table-bordered table-bg table-hover table-sort">
					<thead>
						<tr class="text-c">
							<th>序号</th>
							<th>发送人ID</th>
							<th>内容</th>
							<th>类型</th>
							<th>房间Id</th>
							<th>玩法类型</th>
							<th>底分</th>
							<th>圈数</th>
							<th>接收人ID</th>
							<th>房间编号</th>
							<th>创建时间</th>
							<th>操作</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>
		<script type="text/javascript" src="${base}/lib/jquery/1.9.1/jquery.min.js"></script>
		<script type="text/javascript" src="${base}/lib/layer/2.4/layer.js"></script>
		<script type="text/javascript" src="${base}/lib/My97DatePicker/4.8/WdatePicker.js"></script>
		<script type="text/javascript" src="${base}/lib/datatables/1.10.0/jquery.dataTables.min.js"></script>
		<script type="text/javascript" src="${base}/static/h-ui/js/H-ui.js"></script>
		<script type="text/javascript" src="${base}/static/h-ui.admin/js/H-ui.admin.js"></script>
		<script type="text/javascript" src="${base}/static/js/common/dateUtil.js"></script>
		<script type="text/javascript">
			function getNowFormatDate(createTime) {
				//debugger;
				createTime = createTime * 1000;
				var date = new Date(createTime);
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
				if(strMin >= 0 && strMin <= 9) {
					strMin = "0" + strMin;
				}
				if(strSec >= 0 && strSec <= 9) {
					strSec = "0" + strSec;
				}
				var cTime = date.getFullYear() + seperator1 + month + seperator1 + strDate +
					" " + date.getHours() + seperator2 + strMin +
					seperator2 + strSec;
				return cTime;
			}

			var messageDT;
			$(document).ready(function() {
				messageDT = $("#messageTable").DataTable({
					'ajax': {
						'url': $("#contextPath").val() + '/message/list.do',
						'data': function(data) {
							var fromId = $('#fromId').val();
							var roomId = $('#roomId').val();
							data.fromId = fromId;
							data.roomId = roomId;
						},
					},
					'columns': [{
						'data': 'id'
					}, {
						'data': 'fromId'
					}, {
						'data': 'content'
					}, {
						'data': 'type'
					}, {
						'data': 'roomId'
					}, {
						'data': 'playType'
					}, {
						'data': 'lowGold'
					}, {
						'data': 'roundCount'
					}, {
						'data': 'toId'
					}, {
						'data': 'roomNumber'
					}, {
						'data': 'createTime'
					}, {

					}],
					'columnDefs': [{
							'targets': 3,
							'render': function(data, type, row) {
								return formatStatus(data);
							}
						},  {
							'targets': 5,
							'render': function(data, type, row) {
								return formatStatus2(data);
							}
						},  {
							'targets': 10,
							'render': function(data, type, row) {
								return getNowFormatDate(data);
							}
						},  {
							'targets': 11,
							'render': function(data, type, row) {
								var html = '';
								html += '<a title="编辑" href="javascript:;" onclick="edit(\'' + row.id + '\')" class="ml-5" style="text-decoration:none">';
								html += '<i class="Hui-iconfont">&#xe6df;</i>';
								html += '</a>';
								html += '<a title="详情" href="javascript:;" onclick="browse(\'' + row.id + '\')" class="ml-5" style="text-decoration:none">';
								html += '<i class="Hui-iconfont">&#xe665;</i>';
								html += '</a>';
								html += '&nbsp&nbsp<a title="删除" href="javascript:;" onclick="del(this, \'' + row.id + '\')" class="ml-5" style="text-decoration:none">';
								html += '<i class="Hui-iconfont">&#xe609;</i>';
								html += '</a>';
								return html;
							}
						}
					],
					'createdRow': function(row, data, dataIndex) {
						$(row).addClass('text-c');
					}
				});

				$('#btn_search').on('click', function() {
					_search();
				});

				$('#btn_add').on('click', function() {
					_add();
				});
			});

			function formatStatus(data) {
				switch(data) {
					case 1:
						return '<span>系统消息</span>';
						break;
					case 2:
						return '<span>好友消息</span>';
						break;
					default:
						break;
				}
			}
			
			function formatStatus2(data) {
				switch(data) {
					case 1:
						return '<span>玩法一</span>';
						break;
					case 2:
						return '<span>玩法二</span>';
						break;
					default:
						break;
				}
			}

			function _search() {
				messageDT.ajax.reload();
			}

		/*	function _add() {
				var title = "添加";
				var url = $("#base").val() + '/notice/goAdd.do?';
				layer_show(title, url, '', '');
			}*/

			function edit(id) {
				var title = "编辑";
				var url = $("#base").val() + '/message/goEdit.do?id=' + id;
				layer_show(title, url, '', '');
			}

			function browse(id) {
				//layer_show(title, '/redman/redManView.do?id=' + id, '', '');
				var title = "查看";
				var url = $("#base").val() + '/message/goView.do?id=' + id;
				layer_show(title, url, '', '');
			}

			function del(salf, id) {

				layer.confirm('确认要删除吗？', function(index) {
					var url = $("#base").val() + '/message/delete.do?id=' + id;
					$.ajax({
						type: 'POST',
						url: url,
						dataType: 'json',
						success: function(data) {
							if(data.errorCode == 0) {
								$(salf).parents("tr").remove();
								window.location.reload();
								layer.msg('已删除!', {
									icon: 1,
									time: 1000
								});
							} else {
								layer.alert(data.errorInfo);
							}
						},
						error: function(data) {
							console.log(data.errorInfo);
						},
					});
				});

			}
		</script>
	</body>
</html>