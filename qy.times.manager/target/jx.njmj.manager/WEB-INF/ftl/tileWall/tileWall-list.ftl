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
		<title>牌墙列表</title>
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
		<input type="hidden" class="input-text" value="${base}" placeholder="" id="base" name="base"/>
		<input type="hidden" class="input-text" value="${contextPath}" placeholder="" id="contextPath" name="contextPath"/>
		<nav class="breadcrumb"> <i class="Hui-iconfont">&#xe67f;</i> 首页
			<span class="c-gray en">&gt;</span> 牌局管理
			<span class="c-gray en">&gt;</span> 牌墙列表
			<a class="btn btn-success radius r mr-20" style="line-height:1.6em;margin-top:3px" href="javascript:location.replace(location.href);" title="刷新"> <i class="Hui-iconfont">&#xe68f;</i>
			</a>
		</nav>
		<div class="pd-20">
			<div class="text-c">
				<input type="text" name="weight" id="weight" placeholder=" 权重值" style="width:300px" class="input-text" value="${weight}">
				<!--<input type="text" name="id" id="id" placeholder=" 序号" style="width:300px" class="input-text" value="${id}">-->
				<button name="" id="btn_search" class="btn btn-success" type="submit">
				<i class="Hui-iconfont">&#xe665;</i>
				查询
			</button>
				<button name="" id="btn_add" class="btn btn-success" type="submit">
				<i class="Hui-iconfont">&#xe600;</i> 添加
			</button>
			</div>
			<div class="mt-20">
				<table id="tileWallTable" class="table table-border table-bordered table-bg table-hover table-sort">
					<thead>
						<tr class="text-c">
							<th>序号</th>
							<th>牌墙</th>
							<th>权重值</th>
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
			var tileWallDT;
			$(document).ready(function() {
				//debugger;
				tileWallDT = $("#tileWallTable").DataTable({
					'ajax': {
						'url': $("#contextPath").val() + '/tileWall/list.do',
						'data': function(data) {
							var weight = $('#weight').val();
							data.weight = weight;
						},
					},
					'columns': [{
						'data': 'id'
					}, {
						'data': 'tiles'
					},  {
						'data': 'weight'
					}, {

					}],
					'columnDefs': [{
							'targets': 3,
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

			function _search() {
				tileWallDT.ajax.reload();
			}

			function _add() {
				var title = "添加";
				var url = $("#base").val() + '/tileWall/goAdd.do?';
				layer_show(title, url, '', '');
			}

			function _clear() {
				$('#title').val('');
				$('#memberId').val('');
			}

			function edit(id) {
				var title = "编辑";
				var url = $("#base").val() + '/tileWall/goEdit.do?id=' + id;
				layer_show(title, url, '', '');
			}

			function browse(id) {
				//layer_show(title, '/redman/redManView.do?id=' + id, '', '');
				var title = "查看";
				var url = $("#base").val() + '/tileWall/goView.do?id=' + id;
				layer_show(title, url, '', '');
			}

			function del(salf, id) {
				layer.confirm('确认要删除吗？', function(index) {
					var url = $("#base").val() + '/tileWall/delete.do?id=' + id;
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