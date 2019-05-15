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
		<title>金币列表</title>
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
			<span class="c-gray en">&gt;</span> 金币管理
			<span class="c-gray en">&gt;</span> 金币列表
			<a class="btn btn-success radius r mr-20" style="line-height:1.6em;margin-top:3px" href="javascript:location.replace(location.href);" title="刷新"> <i class="Hui-iconfont">&#xe68f;</i>
			</a>
		</nav>
		<div class="pd-20">
			<div class="text-c">
				<input type="text" name="memberId" id="memberId" placeholder=" 成员ID" style="width:300px" class="input-text" value="${memberId}">
				<input type="text" name="roomId" id="roomId" placeholder=" 房间号" style="width:300px" class="input-text" value="${roomId}">
				<button name="" id="btn_search" class="btn btn-success" type="submit">
				<i class="Hui-iconfont">&#xe665;</i>
				查询
			</button>
				<button name="" id="btn_add" class="btn btn-success" type="submit" disabled="disabled" style="background:#eaeae3;">
				<i class="Hui-iconfont">&#xe600;</i> 添加
			</button>
			</div>
			<div class="mt-20">
				<table id="goldTable" class="table table-border table-bordered table-bg table-hover table-sort">
					<thead>
						<tr class="text-c">
							<th>序号</th>
							<th>成员Id</th>
							<th>交易金额</th>
							<th>交易后金额</th>
							<th>创建时间</th>
							<th>备注</th>
							<th>交易类型</th>
							<th>房间号</th>
							<!--<th>操作</th>-->
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

			var goldDT;
			$(document).ready(function() {
				if(goldDT == null) {
					goldDT = $("#goldTable").DataTable({
						'iDisplayLength' : 10,//默认每页数量
						'bLengthChange' : false, 				//改变每页显示数据数量
					    'processing' : true,					//加载数据时显示正在加载信息  
                        'serverSide' : true,					//指定从服务器端获取数据  
                        'sPaginationType': 'full_numbers',      //翻页界面类型  
                        'bFilter': false,                       //不使用过滤功能  
                        'oLanguage': {                          //汉化  
			                'sLengthMenu': '每页显示 _MENU_ 条记录',  
			                'sZeroRecords': '没有检索到数据',  
			                'sInfo': '显示 _START_ 到 _END_ , 共 _TOTAL_ 条',  
			                'sInfoEmtpy': '没有数据',  
			                'sProcessing': '正在加载数据...',  
			                'oPaginate': {  
			                    'sFirst': '首页',  
			                    'sPrevious': '上一页',  
			                    'sNext': '下一页',  
			                    'sLast': '尾页'  
			                }  
			            },
						ajax: function(data, callback, settings) { //ajax配置为function,手动调用异步查询
							var param = getQueryCondition(data);
							$.ajax({
								'url': $("#contextPath").val() + '/goldRecord/list.do',
								'data': param,
								"success": function(data) {
									if(data.errorCode==0) {
						            	callback(data.data); //服务器端返回的对象的returnObject部分是要求的格式    
						            } else {
						              layer.alert(data.errorInfo);
						            }
						        } 
						    });    
						},
						'order': [[ 0, 'desc' ]],
						'columns': [{
							'data': 'id'
						}, {
							'data': 'memberId'
						}, {
							'data': 'amount'
						}, {
							'data': 'after'
						},{
							'data': 'createTime'
						}, {
							'data': 'remark'
						}, {
							'data': 'type'
						}, {
							'data': 'roomId'
						}/*, {
	
						}*/],
						'columnDefs': [{
								'targets': 4,
								'render': function(data, type, row) {
									return getNowFormatDate(data);
								}
							}, {
								'targets': 6,
								'render': function(data, type, row) {
									return formatStatus(data);
								}
							}, /*{
								'targets': 8,
								'render': function(data, type, row) {
									var html = '';
									html += '<a title="编辑" href="javascript:;" onclick="edit(\'' + row.id + '\')" class="ml-5" style="text-decoration:none">';
									html += '<i class="Hui-iconfont">&#xe6df;</i>';
									html += '</a>';
									html += '<a title="详情" href="javascript:;" onclick="browse(\'' + row.id + '\')" class="ml-5" style="text-decoration:none">';
									html += '<i class="Hui-iconfont">&#xe665;</i>';
									html += '</a>';
									html += '&nbsp&nbsp<a title="删除" href="javascript:;" onclick="del(this, \'' + row.id + '\', \'' + row.status + '\')" class="ml-5" style="text-decoration:none">';
									html += '<i class="Hui-iconfont">&#xe609;</i>';
									html += '</a>';
									return html;
								}
							}*/
						],
						'createdRow': function(row, data, dataIndex) {
							$(row).addClass('text-c');
						},
					});
				}	
	
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
						return '<span>转入</span>';
						break;
					case 2:
						return '<span>转出</span>';
						break;
					case 3:
						return '<span>输赢钱</span>';
						break;
					case 4:
						return '<span>后台操作</span>';
						break;
					default:
						break;
				}
			}

			function _search() {
				goldDT.ajax.reload();
			}

		/*	function _add() {
				var title = "添加";
				var url = $("#base").val() + '/notice/goAdd.do?';
				layer_show(title, url, '', '');
			}*/

/*			function _clear() {
				$('#title').val('');
				$('#memberId').val('');
			}*/

			function edit(id) {
				var title = "编辑";
				var url = $("#base").val() + '/goldRecord/goEdit.do?id=' + id;
				layer_show(title, url, '', '');
			}

			function browse(id) {
				//layer_show(title, '/redman/redManView.do?id=' + id, '', '');
				var title = "查看";
				var url = $("#base").val() + '/goldRecord/goView.do?id=' + id;
				layer_show(title, url, '', '');
			}

			function del(salf, id) {

				layer.confirm('确认要删除吗？', function(index) {
					var url = $("#base").val() + '/goldRecord/delete.do?id=' + id;
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
			
			function getQueryCondition(data) {
                var param = {};  
                //组装排序参数  
                if (data != null && data.length > 0 && data.order[0].column != null) {  
                    switch (data.order[0].column) {  
                    case 0:  
                        param.orderColumn = "id";//数据库列名称  
                        break;  
                    case 1:  
                        param.orderColumn = "memberId";//数据库列名称  
                        break;  
                    case 2:  
                        param.orderColumn = "roomId";//数据库列名称 
                        break;   
                    default:  
                        param.orderColumn = "memberId";//数据库列名称  
                        break;  
                    }  
                    //排序方式asc或者desc  
                    param.orderDir = data.order[0].dir;  
                }  
                //组装分页参数  
                param.startIndex = data.start;  
                param.pageSize = data.length;  
                param.draw = data.draw;  
                var memberId = $('#memberId').val();
				var roomId = $('#roomId').val();
				param.memberId = memberId;
				param.roomId = roomId;
                return param;  
            }
		</script>
	</body>

</html>