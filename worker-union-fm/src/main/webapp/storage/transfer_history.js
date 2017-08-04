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
	$("#txtCstCreate").datepicker({
		dateFormat : "yy-mm-dd"
	});
	initStatus();
});

function initStatus() {
	$("#selStatus").append("<option value=''>请选择审核状态</option>");
	for ( var key in MATERIAL_TRANSFER_APPLICATION_STATUS) {
		$("#selStatus").append(
				"<option value='" + key + "'>"
						+ MATERIAL_TRANSFER_APPLICATION_STATUS[key]
						+ "</option>");
	}
}

/**
 * 查询历史记录
 */
function btnSearch() {
	var materialTransferApplicationDto = {
		id : $("#txtId").val(),
		importProjectName : $("#txtImportProjectName").val(),
		exportProjectName : $("#txtExportProjectName").val(),
		status : $("#selStatus").val(),
		staffName : $("#txtStaffName").val(),
		cstCreate : $("#txtCstCreate").val(),
	};
	ajaxPageMaterialTransferApplicationByFuzzy(materialTransferApplicationDto,
			new Page(PAGE_SIZE, 1), function(data) {
				showSearchResult(data);
			}, function(data) {
			}, true, waiting, run, true);
}

/**
 * 刷新搜索结果
 */
function refreshSearch() {
	var materialTransferApplicationDto = {
		id : $("#txtId").val(),
		importProjectName : $("#txtImportProjectName").val(),
		exportProjectName : $("#txtExportProjectName").val(),
		status : $("#selStatus").val(),
		staffName : $("#txtStaffName").val(),
		cstCreate : $("#txtCstCreate").val(),
	};
	ajaxPageMaterialTransferApplicationByFuzzy(materialTransferApplicationDto,
			new Page(PAGE_SIZE, $("#txtPagination").html()), function(data) {
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
	clearTableTr("#tableMaterialTransferApplications");
	for (var i = 0; i < PAGE_SIZE; i++) {
		if (i < data.page.list.length) {
			var importProjectName = "";
			var status = MATERIAL_TRANSFER_APPLICATION_STATUS[data.page.list[i].status];
			if (data.page.list[i].type == MATERIAL_TRANSFER_APPLICATION_TYPE_TO_HEADQUARTERS) {
				importProjectName = MATERIAL_TRANSFER_APPLICATION_TYPE[MATERIAL_TRANSFER_APPLICATION_TYPE_TO_HEADQUARTERS];
			} else {
				importProjectName = "(" + data.page.list[i].importProjectNum
						+ ")" + data.page.list[i].importProjectName;
			}
			var exportProjectName = "(" + data.page.list[i].exportProjectNum
					+ ")" + data.page.list[i].exportProjectName;
			var id = data.page.list[i].id;
			var staffName = data.page.list[i].staffName;
			var cstCreate = data.page.list[i].cstCreate.substring(0, 10);
			setTableRowContent("#tableMaterialTransferApplications", i + 1, id,
					'<a href="/worker-union-fm/storage/transfer_material_info.html?materialTransferApplication.id='+id+'">'
					+ id
					+ '</a>', importProjectName, exportProjectName, staffName,
					cstCreate, status);
			setRowOnClickSelected("#tableMaterialTransferApplications", i + 1,
					true);
		} else {
			setTableRowContent("#tableMaterialTransferApplications", i + 1, "",
					"", "", "", "", "", "");
			setRowOnClickSelected("#tableMaterialTransferApplications", i + 1,
					false);
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
	var materialTransferApplicationDto = {
		id : $("#txtId").val(),
		importProjectName : $("#txtImportProjectName").val(),
		exportProjectName : $("#txtExportProjectName").val(),
		status : $("#selStatus").val(),
		staffName : $("#txtStaffName").val(),
		cstCreate : $("#txtCstCreate").val(),
	};
	ajaxPageMaterialTransferApplicationByFuzzy(materialTransferApplicationDto,
			new Page(PAGE_SIZE, Math.max(
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
	var materialTransferApplicationDto = {
		id : $("#txtId").val(),
		importProjectName : $("#txtImportProjectName").val(),
		exportProjectName : $("#txtExportProjectName").val(),
		status : $("#selStatus").val(),
		staffName : $("#txtStaffName").val(),
		cstCreate : $("#txtCstCreate").val(),
	};
	ajaxPageMaterialTransferApplicationByFuzzy(materialTransferApplicationDto,
			new Page(PAGE_SIZE, parseInt($("#txtPagination").html()) + 1),
			function(data) {
				showSearchResult(data);
			}, function(data) {
			}, true, waiting, run, true);
}
