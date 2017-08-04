/**
 * 页面启动加载
 */
$(function() {
	$("#txtUserName").html($.cookie(COOKIE_STAFF_NAME_KEY));
	initdatepicker_cn();
	$("#txtValidDate").datepicker({
		dateFormat : "yy-mm-dd"
	});
	showListSupplierGrade();
});

/**
 * 显示供应商级别下拉框
 */
function showListSupplierGrade() {
	ajaxListSupplierGrade(function(data) {
		$("#selSupplierGrade").append("<option value=''>请选择供应商级别</option>");
		for (var i = 0; i < data.supplierGrades.length; i++) {
			$("#selSupplierGrade").append(
					"<option value='" + data.supplierGrades[i].id + "'>"
							+ data.supplierGrades[i].name + "</option>");
		}
		// 初始化供应商资质
		showListSupplierQualification();
	}, function(data) {
	}, true, waiting, run, true);
}

/**
 * 显示供应商资质复选框
 */
function showListSupplierQualification() {
	ajaxListSupplierQualification(function(data) {
		for (var i = 0; i < data.supplierQualifications.length; i++) {
			$("#cboxSupplierQualification").append(
					'<a class="txt_qualification_checkbox"><input class="div_checkbox" type="checkbox" name="supplierQualificationId" value="' + data.supplierQualifications[i].id +'" />'
							+ data.supplierQualifications[i].name + "</a>");
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
 * 创建供应商，并进行下一步
 */
function btnNext(){
	var supplierQualificationIds =[]; 
	$("input[name='supplierQualificationId']:checked").each(function(){ 
		supplierQualificationIds.push($(this).val()); 
		}); 
	var validDate = $("#txtValidDate").val();
	var name = $("#txtSupplierName").val();
	var num = $("#txtSupplierNum").val();
	var supplierGradeId = $("#selSupplierGrade").val();
	var contactPerson = $("#txtContactPerson").val();
	var contactPhone = $("#txtContactPhone").val();
	
	if(!verifyDate(validDate, "合作终止时间", true)){
		return;
	}
	if (!verifyStringNotEmpty(name, "供应商名称", 512, true)){
		return;
	}
	if (!verifyStringNotEmpty(num, "供应商编号", 32, true)){
		return;
	}
	if (!verifyNotEmpty(supplierGradeId, "供应商级别",true)){
		return;
	}
	if (!verifyString(contactPerson, "联系人", 32, true)) {
		return;
	}
	if (!verifyString(contactPhone, "联系方式", 32, true)) {
		return;
	}

	var supplier = {
			validDate:validDate,
			name:name,
			num:num,
			supplierGradeId:supplierGradeId,
			contactPerson:contactPerson,
			contactPhone:contactPhone
	}
	// 新增供应商信息
	ajaxAddSupplier(supplier, supplierQualificationIds, function(data) {
		gotoLocalHtml("/supplier/supplier_add_next.html?supplier.id="+data.id);
	}, function(data) {
	}, true, waiting, run, true);
}
