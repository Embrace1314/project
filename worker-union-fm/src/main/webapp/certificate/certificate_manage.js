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
	showListCertificateSerie();
});

/**
 * 显示证书资质下拉框
 */
function showListCertificateSerie() {
	ajaxListCertificateSerie(function(data) {
		$("#selCertificateSerie").append("<option value=''>请选择证书资质</option>");
		for (var i = 0; i < data.certificateSeries.length; i++) {
			$("#selCertificateSerie").append(
					"<option value='" + data.certificateSeries[i].id + "'>"
							+ data.certificateSeries[i].name + "</option>");
		}
	}, function(data) {
	}, true, waiting, run, true);
}

/**
 * 删除证书信息
 */
function btnDeleteCertificate() {
	if (!isEmpty(getSelectedTableTrValue("#tableCertificates"))) {
		if (confirm("确定删除该证书？")) {
			ajaxRemoveCertificateById(
					getSelectedTableTrValue("#tableCertificates"),
					function(data) {
						refreshSearch();
					}, function(data) {
					}, true, waiting, run, true);
		}
	}
}

/**
 * 修改证书信息
 */
function btnModifyCertificate() {
	if (!isEmpty(getSelectedTableTrValue("#tableCertificates"))) {
		gotoLocalHtml("/certificate/certificate_info.html?certificate.id="
				+ getSelectedTableTrValue("#tableCertificates"));
	}
}

/**
 * 查询证书
 */
function btnSearch() {
	var certificateDto = {
		staffIdCardNo : $("#txtStaffIdCardNo").val(),
		staffName : $("#txtStaffName").val(),
		num : $("#txtNum").val(),
		certificateTypeName : $("#txtCertificateTypeName").val(),
		validDate : $("#txtValidDate").val(),
		certificateSerieId : $("#selCertificateSerie").val()
	};
	ajaxPageCertificateByFuzzy(certificateDto, new Page(PAGE_SIZE, 1),
			function(data) {
				showSearchResult(data);
			}, function(data) {
			}, true, waiting, run, true);
}

/**
 * 刷新搜索结果
 */
function refreshSearch() {
	var certificateDto = {
		staffIdCardNo : $("#txtStaffIdCardNo").val(),
		staffName : $("#txtStaffName").val(),
		num : $("#txtNum").val(),
		certificateTypeName : $("#txtCertificateTypeName").val(),
		validDate : $("#txtValidDate").val(),
		certificateSerieId : $("#selCertificateSerie").val()
	};
	ajaxPageCertificateByFuzzy(certificateDto, new Page(PAGE_SIZE, $(
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
	clearTableTr("#tableCertificates");
	for (var i = 0; i < PAGE_SIZE; i++) {
		if (i < data.page.list.length) {
			setTableRowContent("#tableCertificates", i+1, data.page.list[i].id,
					data.page.list[i].certificateSerieName,
					data.page.list[i].certificateTypeName,
					data.page.list[i].num, data.page.list[i].validDate
							.substring(0, 10), data.page.list[i].staffName,
					data.page.list[i].staffIdCardNo);
			setRowOnClickSelected("#tableCertificates", i+1, true);
		} else {
			setTableRowContent("#tableCertificates", i+1, "", "", "", "", "", "",
					"", "");
			setRowOnClickSelected("#tableCertificates", i+1, false);
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
	var certificateDto = {
		staffIdCardNo : $("#txtStaffIdCardNo").val(),
		staffName : $("#txtStaffName").val(),
		num : $("#txtNum").val(),
		certificateTypeName : $("#txtCertificateTypeName").val(),
		validDate : $("#txtValidDate").val(),
		certificateSerieId : $("#selCertificateSerie").val()
	};
	ajaxPageCertificateByFuzzy(certificateDto, new Page(PAGE_SIZE, Math.max(
			parseInt($("#txtPagination").html()) - 1, 1)), function(data) {
		showSearchResult(data);
	}, function(data) {
	}, true, waiting, run, true);

}

/**
 * 下一页
 */
function btnNextPage() {
	var certificateDto = {
		staffIdCardNo : $("#txtStaffIdCardNo").val(),
		staffName : $("#txtStaffName").val(),
		num : $("#txtNum").val(),
		certificateTypeName : $("#txtCertificateTypeName").val(),
		validDate : $("#txtValidDate").val(),
		certificateSerieId : $("#selCertificateSerie").val()
	};
	ajaxPageCertificateByFuzzy(certificateDto, new Page(PAGE_SIZE, parseInt($(
			"#txtPagination").html()) + 1), function(data) {
		showSearchResult(data);
	}, function(data) {
	}, true, waiting, run, true);

}
