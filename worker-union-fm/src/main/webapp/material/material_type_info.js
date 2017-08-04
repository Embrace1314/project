/**
 * 页面启动加载
 */
$(function() {
	$("#txtUserName").html($.cookie(COOKIE_STAFF_NAME_KEY));
	showMaterialTypeDetail();
});

/**
 * 显示材料类型信息详情
 */
function showMaterialTypeDetail() {
	ajaxGetMaterialTypeDetailById(getUrlParam("materialType.id"),
			function(data) {
				$("#txtName").html(data.materialType.name);
				$("#txtNum").html(data.materialType.num);
				$("#txtModel").html(data.materialType.model);
				$("#txtUnit").html(data.materialType.unit);
				// 显示附件信息
				showMaterialTypeAttachments(data.materialTypeAttachments);
			}, function(data) {
			}, true, waiting, run, true);
}

/**
 * 显示附件列表
 * 
 * @param materialTypeAttachments
 */
function showMaterialTypeAttachments(materialTypeAttachments) {
	$("#btnUpload").siblings().remove();
	for (var i = 0; i < materialTypeAttachments.length; i++) {
		$("#btnUpload")
				.before(
						'<div class="img_picture_display"><button class="btn_picture_delete" onclick="btnDeleteAttachment(this)"></button><attachmentId hidden="true">'
								+ materialTypeAttachments[i].id
								+ '</attachmentId><a class="txt_file_name" onclick="btnDownloadAttachment(this)">'
								+ materialTypeAttachments[i].name
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
		ajaxAcquireMaterialTypeAttachmentDownToken(attachmentId,
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
			ajaxRemoveMaterialTypeAttachmentById(attachmentId, function(data) {
				// 刷新附件显示
				refreshMaterialTypeAttachments();
			}, function(data) {
			}, true, waiting, run, true);
		}
	}
}

/**
 * 刷新附件显示
 */
function refreshMaterialTypeAttachments() {
	ajaxGetMaterialTypeDetailById(getUrlParam("materialType.id"),
			function(data) {
				// 显示附件信息
				showMaterialTypeAttachments(data.materialTypeAttachments);
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
			ajaxAcquireMaterialTypeAttachmentUpToken(
					getUrlParam("materialType.id"), function(data) {
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
					refreshMaterialTypeAttachments();
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
