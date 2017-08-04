/**
 * 页面启动加载
 */
$(function() {
	$("#txtUserName").html($.cookie(COOKIE_STAFF_NAME_KEY));
	initdatepicker_cn();
	$("#txtValidDate").datepicker({
		dateFormat : "yy-mm-dd"
	});
	showListSubcontractorGrade();
});

/**
 * 显示分包商级别下拉框
 */
function showListSubcontractorGrade() {
	ajaxListSubcontractorGrade(function(data) {
		$("#selSubcontractorGrade").append("<option value=''>请选择分包商级别</option>");
		for (var i = 0; i < data.subcontractorGrades.length; i++) {
			$("#selSubcontractorGrade").append(
					"<option value='" + data.subcontractorGrades[i].id + "'>"
							+ data.subcontractorGrades[i].name + "</option>");
		}
		// 初始化分包商资质
		showListSubcontractorQualification();
	}, function(data) {
	}, true, waiting, run, true);
}

/**
 * 显示分包商资质复选框
 */
function showListSubcontractorQualification() {
	ajaxListSubcontractorQualification(function(data) {
		for (var i = 0; i < data.subcontractorQualifications.length; i++) {
			$("#cboxSubcontractorQualification").append(
					'<a class="txt_qualification_checkbox"><input type="checkbox" class="div_checkbox" name="subcontractorQualificationId" value="' + data.subcontractorQualifications[i].id +'" />'
							+ data.subcontractorQualifications[i].name + "</a>");
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
 * 创建分包商，并进行下一步
 */
function btnNext(){
	var subcontractorQualificationIds =[]; 
	$("input[name='subcontractorQualificationId']:checked").each(function(){ 
		subcontractorQualificationIds.push($(this).val()); 
		}); 
	var validDate = $("#txtValidDate").val();
	var name = $("#txtSubcontractorName").val();
	var num = $("#txtSubcontractorNum").val();
	var subcontractorGradeId = $("#selSubcontractorGrade").val();
	var contactPerson = $("#txtContactPerson").val();
	var contactPhone = $("#txtContactPhone").val();
	
	if(!verifyDate(validDate, "合作终止时间", true)){
		return;
	}
	if (!verifyStringNotEmpty(name, "分包商名称", 512, true)){
		return;
	}
	if (!verifyStringNotEmpty(num, "分包商编号", 32, true)){
		return;
	}
	if (!verifyNotEmpty(subcontractorGradeId, "分包商级别",true)){
		return;
	}
	if (!verifyString(contactPerson, "联系人", 32, true)) {
		return;
	}
	if (!verifyString(contactPhone, "联系方式", 32, true)) {
		return;
	}

	var subcontractor = {
			validDate:validDate,
			name:name,
			num:num,
			subcontractorGradeId:subcontractorGradeId,
			contactPerson:contactPerson,
			contactPhone:contactPhone
	}
	// 新增分包商信息
	ajaxAddSubcontractor(subcontractor, subcontractorQualificationIds, function(data) {
		gotoLocalHtml("/subcontractor/subcontractor_add_next.html?subcontractor.id="+data.id);
	}, function(data) {
	}, true, waiting, run, true);
}

