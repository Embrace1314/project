/**
 * 页面启动加载
 */
$(function() {
	$("#txtUserName").html($.cookie(COOKIE_STAFF_NAME_KEY));
	refreshSafetyCheckItemAttachments();
});

/**
 * 刷新附件显示
 */
function refreshSafetyCheckItemAttachments() {
	ajaxGetSafetyCheckItemDetail(getUrlParam("project.id"),
			getUrlParam("safetyCheckItem.id"), function(data) {
				// 显示附件信息
				showSafetyCheckItemAttachments(data.attachments);
			}, function(data) {
			}, true, waiting, run, true);
}

/**
 * 显示附件列表
 * 
 * @param safetyCheckItemAttachments
 */
function showSafetyCheckItemAttachments(safetyCheckItemAttachments) {
	$("#btnUpload").siblings().remove();
	for (var i = 0; i < safetyCheckItemAttachments.length; i++) {
		$("#btnUpload")
				.before(
						'<div class="img_picture_display"><button class="btn_picture_delete" onclick="btnDeleteAttachment(this)"></button><attachmentId hidden="true">'
								+ safetyCheckItemAttachments[i].id
								+ '</attachmentId><a class="txt_file_name" onclick="btnDownloadAttachment(this)">'
								+ safetyCheckItemAttachments[i].name
								+ '</a></div>');
	}
	if (uploader != null) {
		uploader.destroy();
	}
	uploader = new plupload.Uploader(uploaderParams);
	uploader.init();
}

/**
 * 添加成功
 */
function btnSave() {
	alert("添加成功！");
	gotoBack2();
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
		ajaxAcquireSafetyCheckItemAttachmentDownToken(
				getUrlParam("project.id"), attachmentId, function(data) {
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
			ajaxRemoveSafetyCheckItemAttachmentById(getUrlParam("project.id"), attachmentId,
					function(data) {
						// 刷新附件显示
						refreshSafetyCheckItemAttachments();
					}, function(data) {
					}, true, waiting, run, true);
		}
	}
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
			ajaxAcquireSafetyCheckItemAttachmentUpToken(
					getUrlParam("project.id"),
					getUrlParam("safetyCheckItem.id"), function(data) {
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
					refreshSafetyCheckItemAttachments();
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
