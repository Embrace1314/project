/**
 * 页面启动加载
 */
$(function() {
	$("#txtUserName").html($.cookie(COOKIE_STAFF_NAME_KEY));
	initdatepicker_cn();
	$("#txtInspectDate").datepicker({
		dateFormat : "yy-mm-dd"
	});
	$("#txtEvaluateDate").datepicker({
		dateFormat : "yy-mm-dd"
	});
	$("#staffName").html($.cookie(COOKIE_STAFF_NAME_KEY));
});

/**
 * 创建质量记录，并进行下一步
 */
function btnNext() {
	if(!preventRapidClick()){
		return;
	}
	var inspectDate = $("#txtInspectDate").val();
	var inspectRecord = $("#txtInspectRecord").val();
	var rectifyOpinion = $("#txtRectifyOpinion").val();
	var evaluate = $("#txtEvaluate").val();
	var evaluateDate = $("#txtEvaluateDate").val();
	var rectifyStatus = $("input[name='raRectifyStatus']:checked").val();

	if (!verifyDate(inspectDate, "检查日期", true)) {
		return;
	}

	if (!verifyStringNotEmpty(inspectRecord, "检查记录", 1024, true)) {
		return;
	}
	if (!verifyString(rectifyOpinion, "整改意见", 1024, true)) {
		return;
	}
	if (!verifyString(evaluate, "检查评定", 1024, true)) {
		return;
	}
	if (!verifyDate(evaluateDate, "评定日期", true)) {
		return;
	}
	if (!verifyNotEmpty(rectifyStatus, "整改操作", true)) {
		return;
	}

	var qualityCheckItem = {
		inspectDate : inspectDate,
		inspectRecord : inspectRecord,
		rectifyOpinion : rectifyOpinion,
		evaluateDate : evaluateDate,
		evaluate : evaluate,
		rectifyStatus : rectifyStatus
	};
	// 新增质量记录信息
	ajaxAddQualityCheckItem(
			getUrlParam("project.id"),
			qualityCheckItem,
			function(data) {
				gotoLocalHtml("/project/implement/quality_record_add_next.html?project.id="+getUrlParam("project.id")+"&qualityCheckItem.id="
						+ data.id);
			}, function(data) {
			}, true, waiting, run, true);
}

