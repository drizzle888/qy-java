<!DOCTYPE HTML>
<html>
	<head>
		<meta charset="utf-8">
		<meta name="renderer" content="webkit|ie-comp|ie-stand">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
		<meta http-equiv="Cache-Control" content="no-siteapp" />
		<!--[if lt IE 9]>
<script type="text/javascript" src="lib/html5shiv.js"></script>
<script type="text/javascript" src="lib/respond.min.js"></script>
<![endif]-->
		<link rel="stylesheet" type="text/css" href="${base}/static/h-ui/css/H-ui.min.css" />
		<link rel="stylesheet" type="text/css" href="${base}/static/h-ui.admin/css/H-ui.admin.css" />
		<link rel="stylesheet" type="text/css" href="${base}/lib/Hui-iconfont/1.0.8/iconfont.css" />
		<link rel="stylesheet" type="text/css" href="${base}/lib/icheck/icheck.css" />
		<link rel="stylesheet" type="text/css" href="${base}/static/h-ui.admin/skin/default/skin.css" id="skin" />
		<link rel="stylesheet" type="text/css" href="${base}/static/h-ui.admin/css/style.css" />
		<!--[if IE 6]>
<script type="text/javascript" src="lib/DD_belatedPNG_0.0.8a-min.js" ></script>
<script>DD_belatedPNG.fix('*');</script>
<![endif]-->
		<title>每天收益图表</title>
	</head>

	<body>
		<nav class="breadcrumb"><i class="Hui-iconfont">&#xe67f;</i> 首页 <span class="c-gray en">&gt;</span> 统计管理 <span class="c-gray en">&gt;</span> 每天收益图表
			<a class="btn btn-success radius r" style="line-height:1.6em;margin-top:3px" href="javascript:location.replace(location.href);" title="刷新"><i class="Hui-iconfont">&#xe68f;</i></a>
		</nav>
		<div class="page-container">
			<div id="container" style="min-width:700px;height:400px"></div>
		</div>
		<!--_footer 作为公共模版分离出去-->
		<script type="text/javascript" src="${base}/lib/jquery/1.9.1/jquery.min.js"></script>
		<script type="text/javascript" src="${base}/lib/layer/2.4/layer.js"></script>
		<script type="text/javascript" src="${base}/lib/My97DatePicker/4.8/WdatePicker.js"></script>
		<script type="text/javascript" src="${base}/lib/datatables/1.10.0/jquery.dataTables.min.js"></script>
		<script type="text/javascript" src="${base}/static/h-ui/js/H-ui.js"></script>
		<script type="text/javascript" src="${base}/static/h-ui.admin/js/H-ui.admin.js"></script>
		<script type="text/javascript" src="${base}/static/js/common/dateUtil.js"></script>
		<script type="text/javascript" src="${base}/static/common.js"></script>
		<!--请在下方写此页面业务相关的脚本-->
		<script type="text/javascript" src="${base}/lib/hcharts/Highcharts/5.0.6/js/highcharts.js"></script>
		<script type="text/javascript" src="${base}/lib/hcharts/Highcharts/5.0.6/js/modules/exporting.js"></script>
		<script type="text/javascript">
			//定义一个Highcharts的变量，初始值为null
			var oChart = null;
			//定义oChart的布局环境
			//布局环境组成：X轴、Y轴、数据显示、图标标题
			var oOptions = {
				//设置图表关联显示块和图形样式
				chart: {
					renderTo: 'container', //设置显示的页面块
					type: 'area' //设置显示的方式
					//type: 'column'
				},
				//图标标题
				title: {
					text: '每天收益图表'
					//text: null, //设置null则不显示标题
				},
				//x轴
				xAxis: {
					//categories: ['一号', '二号', '三号', '四号', '五号', '六号', '七号', '八号', '九号', '十号', '十一号', '十二号', '十三号', '十四号', '十五号', '十六号', '十七号', '十八号', '十九号', '二十号', '二十一号', '二十二号', '二十三号', '二十四号', '二十五号', '二十六号', '二十七号', '二十八号', '二十九号', '三十号', '三十一号']
					type: 'datetime',
					//maxZoom: 1000 * 60 * 60 * 24,
					title: {
						text: null
					}
				},
				//y轴
				yAxis: {
					title: {
						text: '金币(个)'
					}
				},
				tooltip: {
		            valueSuffix: '个' //折线图中的单位
				},
				plotOptions: {
					area: {
						fillColor: {
							linearGradient: {
								x1: 0,
								y1: 0,
								x2: 0,
								y2: 1
							},
							stops: [
								[0, Highcharts.getOptions().colors[0]],
								[1, Highcharts.Color(Highcharts.getOptions().colors[0]).setOpacity(0).get('rgba')]
							]
						},
						lineWidth: 1,
						marker: {
							enabled: false
						},
						shadow: false,
						states: {
							hover: {
								lineWidth: 1
							}
						},
						threshold: null
					}
				},
				credits: {
					enable: 'false',
					text: '<b style="font-size:12px;">吉祥美游戏统计中心</b>',
					href: ' '
				},
				lang: {　
					printChart: "打印图表",
					downloadJPEG: "下载JPEG 图片",
					downloadPDF: "下载PDF文档",
					downloadPNG: "下载PNG 图片",
					downloadSVG: "下载SVG 矢量图",
					exportButtonTitle: "导出图片"
				},
				//数据列
				series: []
			};
			$(document).ready(function() {
				oChart = new Highcharts.Chart(oOptions);
				//异步动态加载数据列
				LoadSerie_Ajax();
			});
			//异步读取数据并加载到图表
			function LoadSerie_Ajax() {
				oChart.showLoading();
				$.ajax({
					url: '${base}/count/getProfitList.do',
					type: 'POST',
					dataType: 'json',
					contentType: "application/x-www-form-urlencoded; charset=utf-8",
					success: function(rntData) {
						var oSeries = {
							name: "每天金币收益",
							pointInterval: 24 * 60 * 60 * 1000, //间隔一天取一次数据
							pointStart: Date.UTC(2017, 9, 27),
							data: rntData.data
						};
						oChart.addSeries(oSeries);
					}
				});
				oChart.hideLoading();
			}
		</script>
	</body>

</html>