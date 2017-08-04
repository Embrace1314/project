var MONEY_MAX = 9999999999999;
var MONEY_AMOUNT = 9999999999999;

/**
 * 校验金额格式
 * 
 * @param money
 * @returns
 */
function validMoney(money) {
	var reg = /(^[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/;
	return reg.test(money);
}

/**
 * 校验数值格式
 * 
 * @param number
 * @returns
 */
function valid5Point(number) {
	var reg = /^\d+(\.\d{1,5})?$/;
	return reg.test(number);
}

/**
 * 校验日期
 */
function validDate(date) {
	var reg = /^(\d{4})-(\d{2})-(\d{2})$/;
	return reg.test(date);
}

/**
 * 校验金额
 * 
 * @param money
 * @param varName
 * @isExplicit 是否显示异常
 * @returns {Boolean}
 */
function verifyMoney(money, varName, isExplicit) {
	if (!validMoney(money)) {
		if (isExplicit) {
			alert("无效的金额格式(" + varName + ")，请重新输入！");
		}
		return false;
	}

	if (money > MONEY_MAX) {
		if (isExplicit) {
			alert("输入金额(" + varName + ")超出范围，请重新输入！");
		}
		return false;
	}
	return true;
}

/**
 * 校验金额，且大于0
 * 
 * @param money
 * @param varName
 * @isExplicit 是否显示异常
 * @returns {Boolean}
 */
function verifyMoneyGt0(money, varName, isExplicit) {
	if (!validMoney(money)) {
		if (isExplicit) {
			alert("无效的金额格式(" + varName + ")，请重新输入！");
		}
		return false;
	}

	if (money > MONEY_MAX) {
		if (isExplicit) {
			alert("输入金额(" + varName + ")超出范围，请重新输入！");
		}
		return false;
	}

	if (money <= 0) {
		if (isExplicit) {
			alert("输入金额(" + varName + ")要求大于0，请重新输入！");
		}
		return false;
	}
	return true;
}

/**
 * 校验数量 保留小数点后5位
 * 
 * @param money
 * @param varName
 * @isExplicit 是否显示异常
 * @returns {Boolean}
 */
function verifyAmount(amount, varName, isExplicit) {
	if (!validMoney(amount)) {
		if (isExplicit) {
			alert("无效的数量格式(" + varName + ")，请重新输入！");
		}
		return false;
	}

	if (amount > MONEY_AMOUNT) {
		if (isExplicit) {
			alert("输入数量(" + varName + ")超出范围，请重新输入！");
		}
		return false;
	}
	return true;
}

/**
 * 校验数量 保留小数点后5位
 * 
 * @param money
 * @param varName
 * @isExplicit 是否显示异常
 * @returns {Boolean}
 */
function verifyAmountGt0(amount, varName, isExplicit) {
	if (!validMoney(amount)) {
		if (isExplicit) {
			alert("无效的数量格式(" + varName + ")，请重新输入！");
		}
		return false;
	}

	if (amount > MONEY_AMOUNT) {
		if (isExplicit) {
			alert("输入数量(" + varName + ")超出范围，请重新输入！");
		}
		return false;
	}
	if (amount <= 0) {
		if (isExplicit) {
			alert("输入数量(" + varName + ")要求大于0，请重新输入！");
		}
		return false;
	}
	return true;
}

/**
 * 校验字符非空，且长度不超过maxLen
 * 
 * @param str
 * @param varName
 * @param maxLen
 * @param isExplicit
 * @returns {Boolean}
 */
function verifyStringNotEmpty(str, varName, maxLen, isExplicit) {
	if (isEmpty(str)) {
		if (isExplicit) {
			alert("输入字符串(" + varName + ")不能为空，请重新输入！");
		}
		return false;
	}

	if ((str + "").length > maxLen) {
		if (isExplicit) {
			alert("输入字符串(" + varName + ")长度不能超过" + maxLen + "个字符，请重新输入！");
		}
		return false;
	}
	return true;
}

/**
 * 校验字符，长度不超过maxLen
 * 
 * @param str
 * @param varName
 * @param maxLen
 * @param isExplicit
 * @returns {Boolean}
 */
function verifyString(str, varName, maxLen, isExplicit) {
	if ((str + "").length > maxLen) {
		if (isExplicit) {
			alert("输入字符串(" + varName + ")长度不能超过" + maxLen + "个字符，请重新输入！");
		}
		return false;
	}
	return true;
}

/**
 * 校验输入的日期格式
 * 
 * @param date
 * @param varName
 * @param isExplicit
 * @returns {Boolean}
 */
function verifyDate(date, varName, isExplicit) {
	if (isEmpty(date)) {
		if (isExplicit) {
			alert("输入日期(" + varName + ")不能为空，请重新输入！");
		}
		return false;
	}

	if (!validDate(date)) {
		if (isExplicit) {
			alert("无效的日期格式(" + varName + ")，请重新输入！");
		}
		return false;
	}
	return true;
}

/**
 * 校验输入的非空
 * 
 * @param date
 * @param varName
 * @param isExplicit
 * @returns {Boolean}
 */
function verifyNotEmpty(value, varName, isExplicit) {
	if (isEmpty(value)) {
		if (isExplicit) {
			alert("输入(" + varName + ")不能为空，请重新输入！");
		}
		return false;
	}

	return true;
}

/**
 * 判断字符串是否为空字符串
 * 
 * @param str
 *            字符串
 * @returns {Boolean}
 */
function isEmpty(str) {
	if (null == str || undefined == str || 0 == (str + "").trim().length) {
		return true;
	} else {
		return false;
	}
}

var markTime;

/**
 * 防止重复点击操作
 * @returns {Boolean}
 */
function preventRapidClick(){
	if(isEmpty(markTime)){
		markTime = new Date();
		return true;
	}
	
	if (Math.abs((new Date() - markTime)) > 1000) {
		markTime = new Date();
		return true;
	}
	
	markTime = new Date();
	return false;
	
}