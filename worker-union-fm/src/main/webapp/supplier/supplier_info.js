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
	ajaxListSupplierQualification(
			function(data) {
				for (var i = 0; i < data.supplierQualifications.length; i++) {
					$("#cboxSupplierQualification")
							.append(
									'<a class="txt_qualification_checkbox"><input class="div_checkbox" type="checkbox" name="supplierQualificationId" value="'
											+ data.supplierQualifications[i].id
											+ '" />'
											+ data.supplierQualifications[i].name
											+ "</a>");
				}
				showSupplierDetail();
			}, function(data) {
			}, true, waiting, run, true);
}

/**
 * 显示供应商信息详情
 */
function showSupplierDetail() {
	ajaxGetSupplierDetailById(getUrlParam("supplier.id"), function(data) {
		$("#txtValidDate").val(data.supplier.validDate.substring(0, 10));
		$("#txtSupplierName").val(data.supplier.name);
		$("#txtSupplierNum").html(data.supplier.num);
		$("#selSupplierGrade").val(data.supplier.supplierGradeId);
		$("#txtContactPerson").val(data.supplier.contactPerson);
		$("#txtContactPhone").val(data.supplier.contactPhone);

		// 选中已有资质
		checkSupplierQualifications(data.supplierQualifications);

		// 显示附件信息
		showSupplierAttachments(data.supplierAttachments);
	}, function(data) {
	}, true, waiting, run, true);
}

/**
 * 显示附件列表
 * 
 * @param supplierAttachments
 */
function showSupplierAttachments(supplierAttachments) {
	$("#btnUpload").siblings().remove();
	for (var i = 0; i < supplierAttachments.length; i++) {
		$("#btnUpload")
				.before(
						'<div class="img_picture_display"><button class="btn_picture_delete" onclick="btnDeleteAttachment(this)"></button><attachmentId hidden="true">'
								+ supplierAttachments[i].id
								+ '</attachmentId><a class="txt_file_name" onclick="btnDownloadAttachment(this)">'
								+ supplierAttachments[i].name + '</a></div>');
	}
	if (uploader != null) {
		uploader.destroy();
	}
	uploader = new plupload.Uploader(uploaderParams);
	uploader.init();
}

/**
 * 下载附件
 * 
 * @param obj
 */
function btnDownloadAttachment(obj) {
	var attachmentId = $(obj).siblings("attachmentId").first().html();
	if (!isEmpty(attachmentId)) {
		// 获取授权信息
		ajaxAcquireSupplierAttachmentDownToken(attachmentId, function(data) {
    		window.location = data.url;
		}, function(data) {
		}, true, waiting, run, true);
	}
}

/**
 * 删除附件
 * 
 * @param obj
 */
function btnDeleteAttachment(obj) {
	var attachmentId = $(obj).siblings("attachmentId").first().html();
	if (!isEmpty(attachmentId)) {
		if (confirm("确定删除该附件？")) {
			// 删除附件
			ajaxRemoveSupplierAttachmentById(attachmentId, function(data) {
				// 刷新附件显示
				refreshSupplierAttachments();
			}, function(data) {
			}, true, waiting, run, true);
		}
	}
}

/**
 * 刷新附件显示
 */
function refreshSupplierAttachments() {
	ajaxGetSupplierDetailById(getUrlParam("supplier.id"), function(data) {
		// 显示附件信息
		showSupplierAttachments(data.supplierAttachments);
	}, function(data) {
	}, true, waiting, run, true);
}

/**
 * 选中已有供应商资质
 * 
 * @param supplierQualifications
 */
function checkSupplierQualifications(supplierQualifications) {
	// 显示资质
	for (var i = 0; i < supplierQualifications.length; i++) {
		$("input:checkbox[value='" + supplierQualifications[i].id + "']").attr(
				'checked', true);
	}
}

/**
 * 修改供应商信息
 */
function btnSave() {
	var supplierQualificationIds = [];
	$("input[name='supplierQualificationId']:checked").each(function() {
		supplierQualificationIds.push($(this).val());
	});
	var validDate = $("#txtValidDate").val();
	var name = $("#txtSupplierName").val();
	var supplierGradeId = $("#selSupplierGrade").val();
	var contactPerson = $("#txtContactPerson").val();
	var contactPhone = $("#txtContactPhone").val();

	if (!verifyDate(validDate, "合作终止时间", true)) {
		return;
	}
	if (!verifyStringNotEmpty(name, "供应商名称", 512, true)) {
		return;
	}
	if (!verifyNotEmpty(supplierGradeId, "供应商级别", true)) {
		return;
	}
	if (!verifyString(contactPerson, "联系人", 32, true)) {
		return;
	}
	if (!verifyString(contactPhone, "联系方式", 32, true)) {
		return;
	}

	var supplier = {
		id : getUrlParam("supplier.id"),
		validDate : validDate,
		name : name,
		supplierGradeId : supplierGradeId,
		contactPerson : contactPerson,
		contactPhone : contactPhone
	};

	// 修改供应商信息
	ajaxModifySupplierById(supplier, supplierQualificationIds, function(data) {
		alert("修改成功！");
		gotoBack();
	}, function(data) {
	}, true, waiting, run, true);
}

// 上传工具的参数
var uploaderParams = {
	runtimes : 'html5,flash,silverlight,html4',
	browse_button : 'btnUpload',
	multi_selection : false,
	multipart : true,
	url : URL_OSS,
	flash_swf_url : getContextRoot() + '/common/plupload/Moxie.swf',
	silverlight_xap_url : getContextRoot() + '/common/plupload/Moxie.xap',
	init : {

		// 上传绑定事件-添加文件
		FilesAdded : function(uploader, files) {
			// 开始上传
			ajaxAcquireSupplierAttachmentUpToken(getUrlParam("supplier.id"),
					function(data) {
						uploader.bind('BeforeUpload', function(uploader, file) {
							// 进行参数配置
							new_multipart_params = {
								'key' : data.dir + "/" + data.uuid + "_"
										+ file.name,
								'OSSAccessKeyId' : data.OSSAccessKeyId,
								'policy' : data.policy,
								'success_action_status' : "200",
								'Signature' : data.Signature,
								'x-oss-security-token' : data.securityToken,
								'callback' : data.callback,
								'Content-Disposition':"attachment; filename="+file.name
							};
							uploader.settings.url=data.ossUrl;
							uploader.setOption({
								'multipart_params' : new_multipart_params
							});
							waiting();
						});
						uploader.start();
					}, function(data) {
					}, true, waiting, run, true);
		},
		// 上传进度显示
		UploadProgress : function(uploader, file) {
			waiting(file.percent + "%");
		},
		FileUploaded : function(uploader, file, info) {
			run();
			if (info.status == 200) {
				if (RETURNCODE_SUCCESS == JSON.parse(info.response).returnCode) {
					refreshSupplierAttachments();
				} else if (RETURNCODE_FAIL == JSON.parse(info.response).returnCode) {
					alert(JSON.parse(info.response).errorCodeDesc);
				}
			} else if (info.status == 203) {
				alert(info.response);
			} else {
				alert(info.response);
			}
		},
		Error : function(uploader, errObject) {
			run();
			alert(errObject.message);
		}
	}
};

// 上传工具
var uploader = new plupload.Uploader(uploaderParams);
