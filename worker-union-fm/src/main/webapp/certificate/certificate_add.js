/**
 * 页面启动加载
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
 * 显示证书资质系列下拉框
 */
function showListCertificateSerie() {
	ajaxListCertificateSerie(function(data) {
		$("#selCertificateSerie").append("<option value=''>请选择证书名称</option>");
		for (var i = 0; i < data.certificateSeries.length; i++) {
			$("#selCertificateSerie").append(
					"<option value='" + data.certificateSeries[i].id + "'>"
							+ data.certificateSeries[i].name + "</option>");
		}
	}, function(data) {
	}, true, waiting, run, true);
}

/**
 * 显示证书资质类型下拉框
 */
function showListCertificateType() {
	// 清空原有显示
	$("#selCertificateType").empty();
	// 获取证书系列ID
	var certificateSerieId = $("#selCertificateSerie").val();
	// 显示证书类型列表
	if (!isEmpty(certificateSerieId)){
		ajaxListCertificateTypeBySerieId(certificateSerieId, function(data) {
			$("#selCertificateType").append("<option value=''>请选择证书级别</option>");
			for (var i = 0; i < data.certificateTypes.length; i++) {
				$("#selCertificateType").append(
						"<option value='" + data.certificateTypes[i].id + "'>"
								+ data.certificateTypes[i].name + "</option>");
			}
		}, function(data) {
		}, true, waiting, run, true);
	}
}
/**
 * 新增证书信息，并进行下一步
 */
function btnNext() {
	var staffJobNo = $("#txtStaffJobNo").val();
	var staffIdCardNo = $("#txtStaffIdCardNo").val();
	var num = $("#txtNum").val();
	var validDate = $("#txtValidDate").val();
	var certificateTypeId = $("#selCertificateType").val();
	if (!verifyNotEmpty(staffJobNo, "工号",true)){
		return;
	}
	if (!verifyStringNotEmpty(staffIdCardNo, "身份证编号",32,true)){
		return;
	}
	if (!verifyStringNotEmpty(num, "证书编号", 32, true)) {
		return;
	}
	if (!verifyDate(validDate, "资质证书有效期", true)) {
		return;
	}
	if (!verifyNotEmpty(certificateTypeId, "证书级别",true)){
		return;
	}
	var staff = {
		jobNo : staffJobNo,
		idCardNo : staffIdCardNo
	};
	var certificate = {
		certificateTypeId : certificateTypeId,
		num : num,
		validDate : validDate
	};
	// 新增证书信息
	ajaxAddCertificate(staff, certificate, function(data) {
		gotoLocalHtml("/certificate/certificate_add_next.html?certificate.id="+data.id);
	}, function(data) {
	}, true, waiting, run, true);
}
