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
		$("#selSubcontractorGrade")
				.append("<option value=''>请选择分包商级别</option>");
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
	ajaxListSubcontractorQualification(
			function(data) {
				for (var i = 0; i < data.subcontractorQualifications.length; i++) {
					$("#cboxSubcontractorQualification")
							.append(
									'<a class="txt_qualification_checkbox"><input class="div_checkbox" type="checkbox" name="subcontractorQualificationId" value="'
											+ data.subcontractorQualifications[i].id
											+ '" />'
											+ data.subcontractorQualifications[i].name
											+ "</a>");
				}
				showSubcontractorDetail();
			}, function(data) {
			}, true, waiting, run, true);
}

/**
 * 显示分包商信息详情
 */
function showSubcontractorDetail() {
	ajaxGetSubcontractorDetailById(getUrlParam("subcontractor.id"), function(
			data) {
		$("#txtValidDate").val(data.subcontractor.validDate.substring(0, 10));
		$("#txtSubcontractorName").val(data.subcontractor.name);
		$("#txtSubcontractorNum").html(data.subcontractor.num);
		$("#selSubcontractorGrade")
				.val(data.subcontractor.subcontractorGradeId);
		$("#txtContactPerson").val(data.subcontractor.contactPerson);
		$("#txtContactPhone").val(data.subcontractor.contactPhone);

		// 选中已有资质
		checkSubcontractorQualifications(data.subcontractorQualifications);

		// 显示附件信息
		showSubcontractorAttachments(data.subcontractorAttachments);
	}, function(data) {
	}, true, waiting, run, true);
}

/**
 * 显示附件列表
 * 
 * @param subcontractorAttachments
 */
function showSubcontractorAttachments(subcontractorAttachments) {
	$("#btnUpload").siblings().remove();
	for (var i = 0; i < subcontractorAttachments.length; i++) {
		$("#btnUpload")
				.before(
						'<div class="img_picture_display"><button class="btn_picture_delete" onclick="btnDeleteAttachment(this)"></button><attachmentId hidden="true">'
								+ subcontractorAttachments[i].id
								+ '</attachmentId><a class="txt_file_name" onclick="btnDownloadAttachment(this)">'
								+ subcontractorAttachments[i].name
								+ '</a></div>');
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
		ajaxAcquireSubcontractorAttachmentDownToken(attachmentId,
				function(data) {
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
			ajaxRemoveSubcontractorAttachmentById(attachmentId, function(data) {
				// 刷新附件显示
				refreshSubcontractorAttachments();
			}, function(data) {
			}, true, waiting, run, true);
		}
	}
}

/**
 * 刷新附件显示
 */
function refreshSubcontractorAttachments() {
	ajaxGetSubcontractorDetailById(getUrlParam("subcontractor.id"), function(
			data) {
		// 显示附件信息
		showSubcontractorAttachments(data.subcontractorAttachments);
	}, function(data) {
	}, true, waiting, run, true);
}

/**
 * 选中已有分包商资质
 * 
 * @param subcontractorQualifications
 */
function checkSubcontractorQualifications(subcontractorQualifications) {
	// 显示资质
	for (var i = 0; i < subcontractorQualifications.length; i++) {
		$("input:checkbox[value='" + subcontractorQualifications[i].id + "']")
				.attr('checked', true);
	}
}

/**
 * 修改分包商信息
 */
function btnSave() {
	var subcontractorQualificationIds = [];
	$("input[name='subcontractorQualificationId']:checked").each(function() {
		subcontractorQualificationIds.push($(this).val());
	});
	var validDate = $("#txtValidDate").val();
	var name = $("#txtSubcontractorName").val();
	var subcontractorGradeId = $("#selSubcontractorGrade").val();
	var contactPerson = $("#txtContactPerson").val();
	var contactPhone = $("#txtContactPhone").val();

	if (!verifyDate(validDate, "合作终止时间", true)) {
		return;
	}
	if (!verifyStringNotEmpty(name, "分包商名称", 512, true)) {
		return;
	}
	if (!verifyNotEmpty(subcontractorGradeId, "分包商级别", true)) {
		return;
	}
	if (!verifyString(contactPerson, "联系人", 32, true)) {
		return;
	}
	if (!verifyString(contactPhone, "联系方式", 32, true)) {
		return;
	}

	var subcontractor = {
		id : getUrlParam("subcontractor.id"),
		validDate : validDate,
		name : name,
		subcontractorGradeId : subcontractorGradeId,
		contactPerson : contactPerson,
		contactPhone : contactPhone
	}

	// 修改分包商信息
	ajaxModifySubcontractorById(subcontractor, subcontractorQualificationIds,
			function(data) {
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
			ajaxAcquireSubcontractorAttachmentUpToken(
					getUrlParam("subcontractor.id"), function(data) {
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
					refreshSubcontractorAttachments();
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
