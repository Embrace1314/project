var PAGE_SIZE = 10;

/**
 * 页面启动加载 在HTML文件中添加 <link href="/worker-union-fm/common/css/jquery-ui.min.css"
 * rel="stylesheet" type="text/css"> <script type="text/javascript"
 * src="/worker-union-fm/common/js/jquery-ui.min.js"></script> <script type
 * ="text/javascript" src="/worker-union-fm/common/js/datepicker_cn.js"></script>
 */
/**
 * 页面启动加载
 */
$(function() {
	$("#txtUserName").html($.cookie(COOKIE_STAFF_NAME_KEY));
});



/**
 * 删除合同信息
 */
function btnDeleteContract() {
	if (!isEmpty(getSelectedTableTrValue("#tableContracts"))) {
		if (confirm("确定删除该合同？")) {
			ajaxRemoveContractById(getSelectedTableTrValue("#tableContracts"),
					function(data) {
						refreshSearch();
					}, function(data) {
					}, true, waiting, run, true);
		}
	}
}

/**
 * 修改合同信息
 */
function btnModifyContract() {
	if (!isEmpty(getSelectedTableTrValue("#tableContracts"))) {
		gotoLocalHtml("/contract/contract_info.html?contract.id="
				+ getSelectedTableTrValue("#tableContracts"));
	}
}

/**
 * 查询合同
 */
function btnSearch() {
	var contractDto = {
		name : $("#txtContractName").val(),
		num : $("#txtContractNum").val(),
		projectNum : $("#txtProjectNum").val(),
		projectName : $("#txtProjectName").val(),
		type:$("#txtContractType").val()
	};
	ajaxPageContractByFuzzy(contractDto, new Page(PAGE_SIZE, 1),
			function(data) {
				showSearchResult(data);
			}, function(data) {
			}, true, waiting, run, true);
}

/**
 * 刷新搜索结果
 */
function refreshSearch() {
	var contractDto = {
			name : $("#txtContractName").val(),
			num : $("#txtContractNum").val(),
			projectNum : $("#txtProjectNum").val(),
			projectName : $("#txtProjectName").val(),
			type:$("#txtContractType").val()
		};
	ajaxPageContractByFuzzy(contractDto, new Page(PAGE_SIZE,
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
	clearTableTr("#tableContracts");
	for (var i = 0; i < PAGE_SIZE; i++) {
		if (i < data.page.list.length) {
			setTableRowContent("#tableContracts", i+1, data.page.list[i].id,
					data.page.list[i].num, data.page.list[i].name,
					data.page.list[i].type,
					data.page.list[i].money, "("+data.page.list[i].projectNum+")"+
					data.page.list[i].projectName);
			setRowOnClickSelected("#tableContracts", i+1, true);
		} else {
			setTableRowContent("#tableContracts", i+1, "", "", "", "", "", "");
			setRowOnClickSelected("#tableContracts", i+1, false);
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
	var contractDto = {
			name : $("#txtContractName").val(),
			num : $("#txtContractNum").val(),
			projectNum : $("#txtProjectNum").val(),
			projectName : $("#txtProjectName").val(),
			type:$("#txtContractType").val()
		};
	ajaxPageContractByFuzzy(contractDto, new Page(PAGE_SIZE, Math.max(
			parseInt($("#txtPagination").html()) - 1, 1)), function(data) {
		showSearchResult(data);
	}, function(data) {
	}, true, waiting, run, true);

}

/**
 * 下一页
 */
function btnNextPage() {
	var contractDto = {
			name : $("#txtContractName").val(),
			num : $("#txtContractNum").val(),
			projectNum : $("#txtProjectNum").val(),
			projectName : $("#txtProjectName").val(),
			type:$("#txtContractType").val()
		};
	ajaxPageContractByFuzzy(contractDto, new Page(PAGE_SIZE, parseInt($(
			"#txtPagination").html()) + 1), function(data) {
		showSearchResult(data);
	}, function(data) {
	}, true, waiting, run, true);

}

