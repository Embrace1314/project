/**
 * 页面启动调用
 */
$(function() {
	$("#txtUserName").html($.cookie(COOKIE_STAFF_NAME_KEY));
	// 显示项目名称
	showProject();
	initdatepicker_cn();
	$("#txtInspectDate").datepicker({
		dateFormat : "yy-mm-dd"
	});
	$("#txtEvaluateDate").datepicker({
		dateFormat : "yy-mm-dd"
	});
});

/**
 * 显示项目名称
 */
function showProject() {
	ajaxGetProjectDetailById(getUrlParam("project.id"), function(data) {
		showProjectTitle(data.project);
		// 显示质量记录详情
		showQualityDetail();
	}, function(data) {
	}, true, waiting, run, true);
}

/**
 * 显示质量记录详情
 */
function showQualityDetail() {
	ajaxGetQualityCheckItemDetail(getUrlParam("project.id"),
			getUrlParam("qualityCheckItem.id"), function(data) {
				// 显示质量记录信息
				$("#txtInspectDate").val(
						data.qualityCheckItem.inspectDate.substring(0, 10));
				$("#txtInspector").html(data.qualityCheckItem.staffName);
				$("#txtInspectRecord").val(data.qualityCheckItem.inspectRecord);
				$("#txtRectifyOpinion")
						.val(data.qualityCheckItem.rectifyOpinion);
				$("#txtEvaluate").val(data.qualityCheckItem.evaluate);
				$("#txtEvaluateDate").val(
						data.qualityCheckItem.evaluateDate.substring(0, 10));
				$("#txtProjectFeedback").html(
						data.qualityCheckItem.projectFeedback);
				$("[value='" + data.qualityCheckItem.rectifyStatus + "']:radio")
						.attr("checked", "checked");

				// 显示附件
				showQualityCheckItemAttachments(data.attachments);
			}, function(data) {
			}, true, waiting, run, true);
}

/**
 * 保存质量记录
 */
function btnSave() {
	var inspectDate = $("#txtInspectDate").val();
	var inspectRecord = $("#txtInspectRecord").val();
	var rectifyOpinion = $("#txtRectifyOpinion").val();
	var evaluate = $("#txtEvaluate").val();
	var evaluateDate = $("#txtEvaluateDate").val();
	var rectifyStatus = $("input[name='raRectifyStatus']:checked").val();

	if (!verifyDate(inspectDate, "检查日期", true)) {
		return;
	}

	if (!verifyString(inspectRecord, "检查记录", 1024, true)) {
		return;
	}
	if (!verifyString(rectifyOpinion, "整改意见", 1024, true)) {
		return;
	}
	if (!verifyStringNotEmpty(evaluate, "检查评定", 1024, true)) {
		return;
	}
	if (!verifyDate(evaluateDate, "评定日期", true)) {
		return;
	}
	if (!verifyNotEmpty(rectifyStatus, "整改操作", true)) {
		return;
	}

	var qualityCheckItem = {
		id : getUrlParam("qualityCheckItem.id"),
		inspectDate : inspectDate,
		inspectRecord : inspectRecord,
		rectifyOpinion : rectifyOpinion,
		evaluateDate : evaluateDate,
		evaluate : evaluate,
		rectifyStatus : rectifyStatus
	}

	// 新增质量记录信息
	ajaxModifyQualityCheckItemById(
			getUrlParam("project.id"),
			qualityCheckItem,
			function(data) {
				alert("修改成功！");
				gotoLocalHtml("/project/implement/quality_manage.html?project.id="
						+ getUrlParam("project.id"));
			}, function(data) {
			}, true, waiting, run, true);
}

/**
 * 刷新附件显示
 */
function refreshQualityCheckItemAttachments() {
	ajaxGetQualityCheckItemDetail(getUrlParam("project.id"),
			getUrlParam("qualityCheckItem.id"), function(data) {
				// 显示附件信息
				showQualityCheckItemAttachments(data.attachments);
			}, function(data) {
			}, true, waiting, run, true);
}

/**
 * 显示附件列表
 * 
 * @param qualityCheckItemAttachments
 */
function showQualityCheckItemAttachments(qualityCheckItemAttachments) {
	$("#btnUpload").siblings().remove();
	for (var i = 0; i < qualityCheckItemAttachments.length; i++) {
		$("#btnUpload")
				.before(
						'<div class="img_picture_display"><button class="btn_picture_delete" onclick="btnDeleteAttachment(this)"></button><attachmentId hidden="true">'
								+ qualityCheckItemAttachments[i].id
								+ '</attachmentId><a class="txt_file_name" onclick="btnDownloadAttachment(this)">'
								+ qualityCheckItemAttachments[i].name
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
		ajaxAcquireQualityCheckItemAttachmentDownToken(
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
			ajaxRemoveQualityCheckItemAttachmentById(getUrlParam("project.id"),
					attachmentId, function(data) {
						// 刷新附件显示
						refreshQualityCheckItemAttachments();
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
			ajaxAcquireQualityCheckItemAttachmentUpToken(
					getUrlParam("project.id"),
					getUrlParam("qualityCheckItem.id"), function(data) {
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
					refreshQualityCheckItemAttachments();
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

