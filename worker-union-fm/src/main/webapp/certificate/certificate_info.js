/**
 * 页面启动加载
 */
$(function() {
	$("#txtUserName").html($.cookie(COOKIE_STAFF_NAME_KEY));
	initdatepicker_cn();
	$("#txtValidDate").datepicker({
		dateFormat : "yy-mm-dd"
	});
	showCertificateDetail();
});

/**
 * 显示证书信息详情
 */
function showCertificateDetail() {
	ajaxGetCertificateDetailById(getUrlParam("certificate.id"), function(data) {
		$("#txtStaffName").html(data.certificate.staffName);
		$("#txtStaffJobNo").html(data.certificate.staffJobNo);
		$("#txtCertificateSerieName").html(data.certificate.certificateSerieName);
		$("#txtCertificateTypeName").html(data.certificate.certificateTypeName);
		$("#txtStaffIdCardNo").html(data.certificate.staffIdCardNo);
		$("#txtNum").val(data.certificate.num);
		$("#txtValidDate").val(data.certificate.validDate.substring(0, 10));

		// 显示附件信息
		showCertificateAttachments(data.certificateAttachments);
	}, function(data) {
	}, true, waiting, run, true);
}

/**
 * 显示附件列表
 *
 * @param certificateAttachments
 */
function showCertificateAttachments(certificateAttachments) {
	$("#btnUpload").siblings().remove();
	for (var i = 0; i < certificateAttachments.length; i++) {
		$("#btnUpload")
			.before(
				'<div class="img_picture_display"><button class="btn_picture_delete" onclick="btnDeleteAttachment(this)"></button><attachmentId hidden="true">'
				+ certificateAttachments[i].id
				+ '</attachmentId><a class="txt_file_name" onclick="btnDownloadAttachment(this)">'
				+ certificateAttachments[i].name
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
		ajaxAcquireCertificateAttachmentDownToken(attachmentId,
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
			ajaxRemoveCertificateAttachmentById(attachmentId, function(data) {
				// 刷新附件显示
				refreshCertificateAttachments();
			}, function(data) {
			}, true, waiting, run, true);
		}
	}
}

/**
 * 刷新附件显示
 */
function refreshCertificateAttachments() {
	ajaxGetCertificateDetailById(getUrlParam("certificate.id"), function(
		data) {
		// 显示附件信息
		showCertificateAttachments(data.certificateAttachments);
	}, function(data) {
	}, true, waiting, run, true);
}


/**
 * 修改证书信息
 */
function btnSave() {
	var num = $("#txtNum").val();
	var validDate = $("#txtValidDate").val();
	if (!verifyStringNotEmpty(num, "证书编号", 32, true)) {
		return;
	}
	if (!verifyDate(validDate, "资质证书有效期", true)) {
		return;
	}
	
	var certificate = {
		id : getUrlParam("certificate.id"),
		num : num,
		validDate : validDate
	};
	// 修改员工信息
	ajaxModifyCertificateById(certificate, function(data) {
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
			ajaxAcquireCertificateAttachmentUpToken(
				getUrlParam("certificate.id"), function(data) {
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
					refreshCertificateAttachments();
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
