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
	showListSupplierQualification();
});

/**
 * 显示供应商资质下拉框
 */
function showListSupplierQualification() {
	ajaxListSupplierQualification(function(data) {
		$("#selSupplierQualification").append(
				"<option value=''>请选择供应商资质</option>");
		for (var i = 0; i < data.supplierQualifications.length; i++) {
			$("#selSupplierQualification").append(
					"<option value='" + data.supplierQualifications[i].id
							+ "'>" + data.supplierQualifications[i].name
							+ "</option>");
		}
	}, function(data) {
	}, true, waiting, run, true);
}

/**
 * 删除供应商信息
 */
function btnDeleteSupplier() {
	if (!isEmpty(getSelectedTableTrValue("#tableSuppliers"))) {
		if (confirm("确定删除该供应商？")) {
			ajaxRemoveSupplierById(getSelectedTableTrValue("#tableSuppliers"),
					function(data) {
						refreshSearch();
					}, function(data) {
					}, true, waiting, run, true);
		}
	}
}

/**
 * 修改供应商信息
 */
function btnModifySupplier() {
	if (!isEmpty(getSelectedTableTrValue("#tableSuppliers"))) {
		gotoLocalHtml("/supplier/supplier_info.html?supplier.id="
				+ getSelectedTableTrValue("#tableSuppliers"));
	}
}

/**
 * 查询供应商
 */
function btnSearch() {
	var supplierDto = {
		name : $("#txtSupplierName").val(),
		num : $("#txtSupplierNum").val(),
		supplierQualificationId : $("#selSupplierQualification").val(),
		validDate : $("#txtValidDate").val()
	};
	ajaxPageSupplierByFuzzy(supplierDto, new Page(PAGE_SIZE, 1),
			function(data) {
				showSearchResult(data);
			}, function(data) {
			}, true, waiting, run, true);
}

/**
 * 刷新搜索结果
 */
function refreshSearch() {
	var supplierDto = {
			name : $("#txtSupplierName").val(),
			num : $("#txtSupplierNum").val(),
			supplierQualificationId : $("#selSupplierQualification").val(),
			validDate : $("#txtValidDate").val()
		};
	ajaxPageSupplierByFuzzy(supplierDto, new Page(PAGE_SIZE,
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
	clearTableTr("#tableSuppliers");
	for (var i = 0; i < PAGE_SIZE; i++) {
		if (i < data.page.list.length) {
			setTableRowContent("#tableSuppliers", i+1, data.page.list[i].id,
					data.page.list[i].num, data.page.list[i].name,
					data.page.list[i].validDate.substring(0, 10),
					data.page.list[i].supplierQualificationName,
					data.page.list[i].supplierGradeName);
			setRowOnClickSelected("#tableSuppliers", i+1, true);
		} else {
			setTableRowContent("#tableSuppliers", i+1, "", "", "", "", "", "");
			setRowOnClickSelected("#tableSuppliers", i+1, false);
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
	var supplierDto = {
			name : $("#txtSupplierName").val(),
			num : $("#txtSupplierNum").val(),
			supplierQualificationId : $("#selSupplierQualification").val(),
			validDate : $("#txtValidDate").val()
		};
	ajaxPageSupplierByFuzzy(supplierDto, new Page(PAGE_SIZE, Math.max(
			parseInt($("#txtPagination").html()) - 1, 1)), function(data) {
		showSearchResult(data);
	}, function(data) {
	}, true, waiting, run, true);

}

/**
 * 下一页
 */
function btnNextPage() {
	var supplierDto = {
			name : $("#txtSupplierName").val(),
			num : $("#txtSupplierNum").val(),
			supplierQualificationId : $("#selSupplierQualification").val(),
			validDate : $("#txtValidDate").val()
		};
	ajaxPageSupplierByFuzzy(supplierDto, new Page(PAGE_SIZE, parseInt($(
			"#txtPagination").html()) + 1), function(data) {
		showSearchResult(data);
	}, function(data) {
	}, true, waiting, run, true);

}
