function dateUtil() {

}

dateUtil.baseDate = 946656000;

dateUtil.getDateTime = function(timeStamp) {
	timeStamp += dateUtil.baseDate;
	var date = new Date(parseInt(timeStamp) * 1000);
	var d1 = date.toLocaleDateString();
	if (date.toLocaleDateString() == new Date().toLocaleDateString()) {
		return date.toLocaleTimeString();
	} else {
		return date.toLocaleString();
	}
};

dateUtil.getTimeStamp = function(strDateTime) {
	var timeStamp = new Date(strDateTime).valueOf();
	timeStamp /= 1000;
	timeStamp -= dateUtil.baseDate;
	return timeStamp;
};


/*
 *	格式化日期函数
 *	参数:
 *		{1}  ->  date : 日期对象(Date)
 *		{2}  _>  format: 格式 (yyyy-MM-dd hh:mm:ss)
 *	
 *	wangertiao 2016-5-31
 */
dateUtil.format = function(date, format) {
	var o = {
		"M+": date.getMonth() + 1,
		"d+": date.getDate(),
		"h+": date.getHours(),
		"m+": date.getMinutes(),
		"s+": date.getSeconds(),
		"q+": Math.floor((date.getMonth() + 3) / 3),
		"S": date.getMilliseconds()
	}

	if (/(y+)/.test(format)) {
		format = format.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
	}

	for (var k in o) {
		if (new RegExp("(" + k + ")").test(format)) {
			format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
		}
	}
	return format;
}

dateUtil.test = function() {
	
}