var PAGE_SIZE = 12;

/**
 * 页面启动加载 在HTML文件中添加 <link href="/worker-union-fm/common/css/jquery-ui.min.css"
 * rel="stylesheet" type="text/css"> <script type="text/javascript"
 * src="/worker-union-fm/common/js/jquery-ui.min.js"></script> <script type
 * ="text/javascript" src="/worker-union-fm/common/js/datepicker_cn.js"></script>
 */
$(function() {
	$("#txtUserName").html($.cookie(COOKIE_STAFF_NAME_KEY));
	initdatepicker_cn();
	$("#txtStartDate").datepicker({
		dateFormat : "yy-mm-dd"
	});
	$("#txtEndDate").datepicker({
		dateFormat : "yy-mm-dd"
	});
});

/**
 * 删除员工信息
 */
function btnDeleteStaff() {
	if (!isEmpty(getSelectedTableTrValue("#tableStaffs"))) {
		if (confirm("确定删除该员工？")) {
			ajaxRemoveStaffById(getSelectedTableTrValue("#tableStaffs"),
					function(data) {
						refreshSearch();
					}, function(data) {
					}, true, waiting, run, true);
		}
	}
}

/**
 * 修改员工信息
 */
function btnModifyStaff() {
	if (!isEmpty(getSelectedTableTrValue("#tableStaffs"))) {
		gotoLocalHtml("/personnel/personnel_info.html?staff.id="
				+ getSelectedTableTrValue("#tableStaffs"));
	}
}

/**
 * 查询员工
 */
function btnSearch() {
	var staffDto = {
			name : $("#txtName").val(),
			jobNo : $("#txtJobNo").val(),
			idCardNo : $("#txtIdCardNo").val(),
			roleName : $("#txtRoleName").val(),
			projectName : $("#txtProjectName").val(),
			startDate : $("#txtStartDate").val(),
			endDate : $("#txtEndDate").val()
		};
	ajaxPageStaffByFuzzy(staffDto, new Page(PAGE_SIZE, 1),
			function(data) {
				showSearchResult(data);
			}, function(data) {
			}, true, waiting, run, true);
}

/**
 * 刷新搜索结果
 */
function refreshSearch() {
	var staffDto = {
		name : $("#txtName").val(),
		jobNo : $("#txtJobNo").val(),
		idCardNo : $("#txtIdCardNo").val(),
		roleName : $("#txtRoleName").val(),
		projectName : $("#txtProjectName").val(),
		startDate : $("#txtStartDate").val(),
		endDate : $("#txtEndDate").val()
	};
	ajaxPageStaffByFuzzy(staffDto, new Page(PAGE_SIZE, $("#txtPagination")
			.html()), function(data) {
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
	clearTableTr("#tableStaffs");
	for (var i = 0; i < PAGE_SIZE; i++) {
		if (i < data.page.list.length) {
			setTableRowContent("#tableStaffs", i+1, data.page.list[i].id,
					data.page.list[i].jobNo, data.page.list[i].name,
					data.page.list[i].idCardNo, data.page.list[i].phone,
					data.page.list[i].roleName, data.page.list[i].entryDate
							.substring(0, 10), data.page.list[i].projectName);
			setRowOnClickSelected("#tableStaffs", i+1, true);
		} else {
			setTableRowContent("#tableStaffs", i+1, "", "", "", "", "", "", "",
					"");
			setRowOnClickSelected("#tableStaffs", i+1, false);
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
	var staffDto = {
			name : $("#txtName").val(),
			jobNo : $("#txtJobNo").val(),
			idCardNo : $("#txtIdCardNo").val(),
			roleName : $("#txtRoleName").val(),
			projectName : $("#txtProjectName").val(),
			startDate : $("#txtStartDate").val(),
			endDate : $("#txtEndDate").val()
		};
	ajaxPageStaffByFuzzy(staffDto, new Page(PAGE_SIZE, Math.max(
					parseInt($("#txtPagination").html()) - 1, 1)), function(
					data) {
				showSearchResult(data);
			}, function(data) {
			}, true, waiting, run, true);
}

/**
 * 下一页
 */
function btnNextPage() {
	var staffDto = {
			name : $("#txtName").val(),
			jobNo : $("#txtJobNo").val(),
			idCardNo : $("#txtIdCardNo").val(),
			roleName : $("#txtRoleName").val(),
			projectName : $("#txtProjectName").val(),
			startDate : $("#txtStartDate").val(),
			endDate : $("#txtEndDate").val()
		};
	ajaxPageStaffByFuzzy(staffDto, new Page(PAGE_SIZE, parseInt($(
					"#txtPagination").html()) + 1), function(data) {
				showSearchResult(data);
			}, function(data) {
			}, true, waiting, run, true);
}
