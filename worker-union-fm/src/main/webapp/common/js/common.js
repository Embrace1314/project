/**
 * 获取项目名称，如"/projectName"
 */
function getContextRoot() {
	return window.document.location.pathname.substring(0,
			window.document.location.pathname.substr(1).indexOf('/') + 1);
}

/**
 * 获取URL参数值
 * 
 * @param param
 * @returns
 */
function getUrlParam(param) {
	var reg = new RegExp("(^|&)" + param + "=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	return null == r ? null : unescape(r[2]);
}

/**
 * 刷新当前页面
 */
function reload() {
	location.reload();
}

/**
 * 返回
 */
function gotoBack(){
	window.history.go(-1);
}

/**
 * 返回两步
 */
function gotoBack2(){
	window.history.go(-2);
}

/**
 * 页面跳转
 */
function gotoHtml(url) {
	window.location.href = url;
}

/**
 * 页面本项目跳转
 */
function gotoLocalHtml(url) {
	window.location.href = getContextRoot() + url;
}


/**
 * 打开新页面
 */
function openNewHtml(url) {
	window.open(url);
}

/**
 * 判断字符串长度大于len是，后面加省略号
 */
function retainStringFixLen(str, len){
	return str.length>len?str.slice(0, len-3) + '...':str;
}

/**
 * 判断字符串长度大于自然长度engLen时，后面加省略号
 */
function retainStringFixLenEng(str, engLen) {
	if (getLenEng(str) <= engLen) {
		return str;
	}
	// 进行截断操作
	var result = "";
	var iLengthEng = 0;
	for (var i = 0; i < str.length; i++) {
		var charCode = str.charCodeAt(i);
		if (charCode >= 0 && charCode <= 128) {
			iLengthEng += 1;
		} else {
			iLengthEng += 2;
		}
		if (iLengthEng < engLen - 3) {
			result += str.substr(i,1);
		} else {
			break;
		}
	}
	return result + '...';
}


/**
 * 获取字符自然长度
 * 
 * @param str
 * @returns {Number}
 */
function getLenEng(str) {
	var lengthEng = 0;
	for (var i = 0; i < str.length; i++) {
		var charCode = str.charCodeAt(i);
		if (charCode >= 0 && charCode <= 128) {
			lengthEng += 1;
		} else {
			lengthEng += 2;
		}
	}
	return lengthEng;
};

/**
 * 乘法
 */
function multiply(arg1, arg2){
	var m = 0, s1 = arg1.toString(), s2 = arg2.toString();
	try { m += s1.split(".")[1].length;}
	catch (e) {}
	try {m += s2.split(".")[1].length;}
	catch (e) {}
	return Number(s1.replace(".", "")) * Number(s2.replace(".", "")) / Math.pow(10, m);
}