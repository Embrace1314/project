var PAGE_SIZE = 10;

/**
 * 页面启动加载 在HTML文件中添加 <link href="/worker-union-fm/common/css/jquery-ui.min.css"
 * rel="stylesheet" type="text/css"> <script type="text/javascript"
 * src="/worker-union-fm/common/js/jquery-ui.min.js"></script> <script type
 * ="text/javascript" src="/worker-union-fm/common/js/datepicker_cn.js"></script>
 */
$(function() {
	$("#txtUserName").html($.cookie(COOKIE_STAFF_NAME_KEY));
});

/**
 * 删除材料类型信息
 */
function btnDeleteMaterialType() {
	if (!isEmpty(getSelectedTableTrValue("#tableMaterialTypes"))) {
		if (confirm("确定删除该材料类型？")) {
			ajaxRemoveMaterialTypeById(
					getSelectedTableTrValue("#tableMaterialTypes"), function(
							data) {
						refreshSearch();
					}, function(data) {
					}, true, waiting, run, true);
		}
	}
}

/**
 * 修改材料类型信息
 */
function btnModifyMaterialType() {
	if (!isEmpty(getSelectedTableTrValue("#tableMaterialTypes"))) {
		gotoLocalHtml("/material/material_type_info.html?materialType.id="
				+ getSelectedTableTrValue("#tableMaterialTypes"));
	}
}

/**
 * 查询材料类型
 */
function btnSearch() {
	var materialType = {
		name : $("#txtName").val(),
		num : $("#txtNum").val()
	};
	ajaxPageMaterialTypeByFuzzy(materialType, new Page(PAGE_SIZE, 1), function(
			data) {
		showSearchResult(data);
	}, function(data) {
	}, true, waiting, run, true);
}

/**
 * 刷新搜索结果
 */
function refreshSearch() {
	var materialType = {
		name : $("#txtName").val(),
		num : $("#txtNum").val()
	};
	ajaxPageMaterialTypeByFuzzy(materialType, new Page(PAGE_SIZE, $(
			"#txtPagination").html()), function(data) {
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
	clearTableTr("#tableMaterialTypes");
	for (var i = 0; i < PAGE_SIZE; i++) {
		if (i < data.page.list.length) {
			setTableRowContent("#tableMaterialTypes", i+1, data.page.list[i].id,
					"<a href='javascript:gotoLocalHtml(\"/material/material_type_info.html?materialType.id="
							+ data.page.list[i].id + "\");'>"
							+ data.page.list[i].num + "</a>",
					data.page.list[i].name, data.page.list[i].model,
					data.page.list[i].unit);
			setRowOnClickSelected("#tableMaterialTypes", i+1, true);
		} else {
			setTableRowContent("#tableMaterialTypes", i+1, "", "", "", "", "");
			setRowOnClickSelected("#tableMaterialTypes", i+1, false);
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
	var materialType = {
		name : $("#txtName").val(),
		num : $("#txtNum").val()
	};
	ajaxPageMaterialTypeByFuzzy(materialType, new Page(PAGE_SIZE, Math.max(
			parseInt($("#txtPagination").html()) - 1, 1)), function(data) {
		showSearchResult(data);
	}, function(data) {
	}, true, waiting, run, true);
}

/**
 * 下一页
 */
function btnNextPage() {
	var materialType = {
		name : $("#txtName").val(),
		num : $("#txtNum").val()
	};
	ajaxPageMaterialTypeByFuzzy(materialType, new Page(PAGE_SIZE, parseInt($(
			"#txtPagination").html()) + 1), function(data) {
		showSearchResult(data);
	}, function(data) {
	}, true, waiting, run, true);
}
