/**
 * 页面启动加载
 */
$(function() {
	$("#txtUserName").html($.cookie(COOKIE_STAFF_NAME_KEY));
	initdatepicker_cn();
	$("#txtSignDate").datepicker({
		dateFormat : "yy-mm-dd"
	});
	showContractDetail();
});

/**
 * 显示合同信息详情
 */
function showContractDetail() {
	ajaxGetContractDetailById(getUrlParam("contract.id"), function(data) {
		$("#txtName").val(data.contract.name);
		$("#txtNum").html(data.contract.num);
		$("#txtType").val(data.contract.type);
		$("#txtPartA").val(data.contract.partA);
		$("#txtPartB").val(data.contract.partB);
		$("#txtSignDate").val(data.contract.signDate.substring(0, 10));
		$("#txtMoney").val(data.contract.money);
		$("#txtDutyDepartment").val(data.contract.dutyDepartment);
		$("#txtProjectName").html(
				"(" + data.contract.projectNum + ")"
						+ data.contract.projectName);
		$("#txtContentAbstract").val(data.contract.contentAbstract);

		// 显示附件信息
		showContractAttachments(data.contractAttachments);
	}, function(data) {
	}, true, waiting, run, true);
}

/**
 * 显示附件列表
 * 
 * @param contractAttachments
 */
function showContractAttachments(contractAttachments) {
	$("#btnUpload").siblings().remove();
	for (var i = 0; i < contractAttachments.length; i++) {
		$("#btnUpload")
				.before(
						'<div class="img_picture_display"><button class="btn_picture_delete" onclick="btnDeleteAttachment(this)"></button><attachmentId hidden="true">'
								+ contractAttachments[i].id
								+ '</attachmentId><a class="txt_file_name" onclick="btnDownloadAttachment(this)">'
								+ contractAttachments[i].name + '</a></div>');
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
		ajaxAcquireContractAttachmentDownToken(attachmentId, function(data) {
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
			ajaxRemoveContractAttachmentById(attachmentId, function(data) {
				// 刷新附件显示
				refreshContractAttachments();
			}, function(data) {
			}, true, waiting, run, true);
		}
	}
}

/**
 * 刷新附件显示
 */
function refreshContractAttachments() {
	ajaxGetContractDetailById(getUrlParam("contract.id"), function(data) {
		// 显示附件信息
		showContractAttachments(data.contractAttachments);
	}, function(data) {
	}, true, waiting, run, true);
}

/**
 * 修改合同信息
 */
function btnSave() {
	var name = $("#txtName").val();
	var type = $("#txtType").val();
	var partA = $("#txtPartA").val();
	var partB = $("#txtPartB").val();
	var signDate = $("#txtSignDate").val();
	var money = $("#txtMoney").val();
	var dutyDepartment = $("#txtDutyDepartment").val();
	var contentAbstract = $("#txtContentAbstract").val();

	if (!verifyStringNotEmpty(name, "合同名称", 512, true)) {
		return;
	}
	if (!verifyStringNotEmpty(type, "合同类型", 32, true)) {
		return;
	}
	if (!verifyStringNotEmpty(partA, "甲方", 512, true)) {
		return;
	}
	if (!verifyStringNotEmpty(partB, "乙方", 512, true)) {
		return;
	}
	if (!verifyDate(signDate, "签订时间", true)) {
		return;
	}
	if (!verifyMoney(money, "合同金额", true)) {
		return;
	}
	if (!verifyString(dutyDepartment, "责任部门", 32, true)) {
		return;
	}
	if (!verifyString(contentAbstract, "内容摘要", 1024, true)) {
		return;
	}
	
	var contract = {
		id : getUrlParam("contract.id"),
		name : name,
		type : type,
		partA : partA,
		partB : partB,
		signDate : signDate,
		money : money,
		dutyDepartment : dutyDepartment,
		contentAbstract : contentAbstract
	};

	// 修改合同信息
	ajaxModifyContractById(contract, function(data) {
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
			ajaxAcquireContractAttachmentUpToken(getUrlParam("contract.id"),
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
					refreshContractAttachments();
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
