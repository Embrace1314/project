var PAGE_SIZE = 12;

/**
 * 页面启动调用
 */
$(function() {
	$("#txtUserName").html($.cookie(COOKIE_STAFF_NAME_KEY));
	// 显示项目名称
	showProject();
});

/**
 * 显示项目名称
 */
function showProject() {
	ajaxGetProjectDetailById(getUrlParam("project.id"), function(data) {
		showProjectTitle(data.project);
		// 显示质量管理信息
		showQualityInfo();
	}, function(data) {
	}, true, waiting, run, true);
}

/**
 * 显示质量管理信息
 */
function showQualityInfo() {
	// 分页参数
	var pagination = $("#pagination").html();
	// 清空页码
	$("#pagination").html("");
	$("#totalPage").html("");

	ajaxPageQualityCheckItem(getUrlParam("project.id"), {
		"pagination" : pagination,
		"size" : PAGE_SIZE
	}, function(data) {
		// 设置 页码
		$("#pagination").html(data.page.pagination);
		$("#totalPage").html(data.page.totalPage);

		clearTableTr("#tableQuality");
		for (var i = 0; i < PAGE_SIZE; i++) {
			if (i < data.page.list.length) {
				var j = i + 1;
				setTableRowContent("#tableQuality", i + 1,
						data.page.list[i].id, j, data.page.list[i].inspectDate
								.substring(0, 10), data.page.list[i].staffName,
						RECTIFY_STATUS[data.page.list[i].rectifyStatus],
						data.page.list[i].evaluate,
						data.page.list[i].evaluateDate.substring(0, 10));
				setRowOnClickSelected("#tableQuality", i + 1, true);
			} else {
				setTableRowContent("#tableQuality", i + 1, "", "", "", "", "",
						"", "", "");
				setRowOnClickSelected("#tableQuality", i + 1, false);
			}
		}
	}, function(data) {
	}, true, waiting, run, true);
}

/**
 * 质量检查记录修改详情
 */
function btnGoToDetail(obj) {
	var attachmentId = $(obj).parent().parent().children("td").first().html();
	gotoLocalHtml('/project/implement/quality_record_modify.html?project.id='
			+ getUrlParam('project.id') + '&qualityCheckItem.id='
			+ attachmentId);
}

/**
 * 点击上一页
 */
function btnPrevPage() {
	$("#pagination").html(Math.max(parseInt($("#pagination").html()) - 1, 1));
	showQualityInfo();
}

/**
 * 点击下一页
 */
function btnNextPage() {
	$("#pagination").html(Math.max(parseInt($("#pagination").html()) + 1, 1));
	showQualityInfo();
}

/**
 * 删除质量检查记录
 */
function btnDeleteQualityCheckItem() {
	if (!isEmpty(getSelectedTableTrValue("#tableQuality"))) {
		if (confirm("确定删除该质量检查记录？")) {
			ajaxRemoveQualityCheckItemById(getUrlParam("project.id"),
					getSelectedTableTrValue("#tableQuality"), function(data) {
						showQualityInfo();
					}, function(data) {
					}, true, waiting, run, true);
		}
	}
}

/**
 * 修改质量检查记录
 */
function btnModifyQualityCheckItem() {
	if (!isEmpty(getSelectedTableTrValue("#tableQuality"))) {
		gotoLocalHtml("/project/implement/quality_record_modify.html?project.id="
				+ getUrlParam("project.id")
				+ "&qualityCheckItem.id="
				+ getSelectedTableTrValue("#tableQuality"));
	}
}
