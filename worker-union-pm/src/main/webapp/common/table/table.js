/**
 * 删除表格的内容
 */
function removeTableTr(tableId) {
	$(tableId + " tr").remove(".list_mid_row,.list_last_row,.list_tr_selected");
}

/**
 * 清空表格的内容
 */
function clearTableTr(tableId) {
	$(tableId + " tr").find("td").html("");
	$(tableId).find(".list_tr_selected").attr("class", "list_mid_row");
}

/**
 * 清空表格的选中状态
 * 
 * @param tableId
 */
function clearTableSelectStatus(obj) {
	$(obj).parents("table").find(".list_tr_selected").attr("class",
			"list_mid_row");
}

/**
 * 选中表格当前行
 * 
 * @param obj
 */
function btnSelectTableTr(obj) {
	clearTableSelectStatus(obj);
	$(obj).attr("class", "list_tr_selected");
}

/**
 * 获取选中的行
 */
function getSelectedTableTr(tableId) {
	return $(tableId).find("tr.list_tr_selected").first();
}

/**
 * 获取选中的行，第一个隐藏td的值
 */
function getSelectedTableTrValue(tableId) {
	return $(tableId).find("tr.list_tr_selected").first().children("td")
			.first().html();
}



/**
 * 设置表格行内容
 * 
 * @returns {String}
 */
function setTableRowContent(tableId, index) {
	for(var i=2; i<arguments.length; i++){
		$(tableId+" tr").eq(index).children("td").eq(i-2).html(formatEmpty(arguments[i]));
	}	
}

/**
 * 设置行点击效果
 * @param tableId
 * @param index
 * @param canSelecte
 */
function setRowOnClickSelected(tableId, index, canSelecte) {
	if (canSelecte) {
		$(tableId+" tr").eq(index)
				.attr("onclick", "btnSelectTableTr(this)");
	} else {
		$(tableId+" tr").eq(index).attr("onclick",
				"clearTableSelectStatus(this)");
	}
}


/**
 * 整理字符，为null为空时返回""
 * 否则返回原变量
 * @param str
 * @returns
 */
function formatEmpty(str){
	if (null == str || undefined == str || 0 == (str+"").trim().length) {
		return "";
	} else {
		return str;
	}
}

/**
 * 整理字符，为null为空时返回""
 * 否则返回原变量的字符形式
 * @param str
 * @returns
 */
function formatEmpty2Str(str){
	return formatEmpty(str)+"";
}