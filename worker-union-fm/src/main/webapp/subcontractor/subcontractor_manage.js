var PAGE_SIZE = 10;

/**
 * 页面启动加载 在HTML文件中添加 <link href="/worker-union-fm/common/css/jquery-ui.min.css"
 * rel="stylesheet" type="text/css"> <script type="text/javascript"
 * src="/worker-union-fm/common/js/jquery-ui.min.js"></script> <script type
 * ="text/javascript" src="/worker-union-fm/common/js/datepicker_cn.js"></script>
 */
$(function() {
	$("#txtUserName").html($.cookie(COOKIE_STAFF_NAME_KEY));
	initdatepicker_cn();
	$("#txtValidDate").datepicker({
		dateFormat : "yy-mm-dd"
	});
	showListSubcontractorQualification();
});

/**
 * 显示分包商资质下拉框
 */
function showListSubcontractorQualification() {
	ajaxListSubcontractorQualification(function(data) {
		$("#selSubcontractorQualification").append(
				"<option value=''>请选择分包商资质</option>");
		for (var i = 0; i < data.subcontractorQualifications.length; i++) {
			$("#selSubcontractorQualification").append(
					"<option value='" + data.subcontractorQualifications[i].id
							+ "'>" + data.subcontractorQualifications[i].name
							+ "</option>");
		}
	}, function(data) {
	}, true, waiting, run, true);
}

/**
 * 删除分包商信息
 */
function btnDeleteSubcontractor() {
	if (!isEmpty(getSelectedTableTrValue("#tableSubcontractors"))) {
		if (confirm("确定删除该分包商？")) {
			ajaxRemoveSubcontractorById(getSelectedTableTrValue("#tableSubcontractors"),
					function(data) {
						refreshSearch();
					}, function(data) {
					}, true, waiting, run, true);
		}
	}
}

/**
 * 修改分包商信息
 */
function btnModifySubcontractor() {
	if (!isEmpty(getSelectedTableTrValue("#tableSubcontractors"))) {
		gotoLocalHtml("/subcontractor/subcontractor_info.html?subcontractor.id="
				+ getSelectedTableTrValue("#tableSubcontractors"));
	}
}

/**
 * 查询分包商
 */
function btnSearch() {
	var subcontractorDto = {
		name : $("#txtSubcontractorName").val(),
		num : $("#txtSubcontractorNum").val(),
		subcontractorQualificationId : $("#selSubcontractorQualification").val(),
		validDate : $("#txtValidDate").val()
	};
	ajaxPageSubcontractorByFuzzy(subcontractorDto, new Page(PAGE_SIZE, 1),
			function(data) {
				showSearchResult(data);
			}, function(data) {
			}, true, waiting, run, true);
}

/**
 * 刷新搜索结果
 */
function refreshSearch() {
	var subcontractorDto = {
			name : $("#txtSubcontractorName").val(),
			num : $("#txtSubcontractorNum").val(),
			subcontractorQualificationId : $("#selSubcontractorQualification").val(),
			validDate : $("#txtValidDate").val()
		};
	ajaxPageSubcontractorByFuzzy(subcontractorDto, new Page(PAGE_SIZE,
			$("#txtPagination").html()), function(data) {
		showSearchResult(data);
	}, function(data) {
	}, true, waiting, run, true);
}

/**
 * 显示搜索结果
 * 
 * @param data
 */
function showSearchResult(data) {
	clearTableTr("#tableSubcontractors");
	for (var i = 0; i < PAGE_SIZE; i++) {
		if (i < data.page.list.length) {
			setTableRowContent("#tableSubcontractors", i+1, data.page.list[i].id,
					data.page.list[i].num, data.page.list[i].name,
					data.page.list[i].validDate.substring(0, 10),
					data.page.list[i].subcontractorQualificationName,
					data.page.list[i].subcontractorGradeName);
			setRowOnClickSelected("#tableSubcontractors", i+1, true);
		} else {
			setTableRowContent("#tableSubcontractors", i+1, "", "", "", "", "", "");
			setRowOnClickSelected("#tableSubcontractors", i+1, false);
		}
	}

	// 设置 页码
	$("#txtPagination").html(data.page.pagination);
	$("#txtTotalPage").html(data.page.totalPage);
}

/**
 * 上一页
 */
function btnPrevPage() {
	var subcontractorDto = {
			name : $("#txtSubcontractorName").val(),
			num : $("#txtSubcontractorNum").val(),
			subcontractorQualificationId : $("#selSubcontractorQualification").val(),
			validDate : $("#txtValidDate").val(),
		};
	ajaxPageSubcontractorByFuzzy(subcontractorDto, new Page(PAGE_SIZE, Math.max(
			parseInt($("#txtPagination").html()) - 1, 1)), function(data) {
		showSearchResult(data);
	}, function(data) {
	}, true, waiting, run, true);

}

/**
 * 下一页
 */
function btnNextPage() {
	var subcontractorDto = {
			name : $("#txtSubcontractorName").val(),
			num : $("#txtSubcontractorNum").val(),
			subcontractorQualificationId : $("#selSubcontractorQualification").val(),
			validDate : $("#txtValidDate").val(),
		};
	ajaxPageSubcontractorByFuzzy(subcontractorDto, new Page(PAGE_SIZE, parseInt($(
			"#txtPagination").html()) + 1), function(data) {
		showSearchResult(data);
	}, function(data) {
	}, true, waiting, run, true);

}

