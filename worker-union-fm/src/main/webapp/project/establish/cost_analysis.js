/**
 * 页面启动调用
 */
$(function() {
	$("#txtUserName").html($.cookie(COOKIE_STAFF_NAME_KEY));
	// 初始化select的成本分析项类型
	initCostAnalysisItemType();
	// 显示项目信息
	showProject();
});

/**
 * 初始化select的成本分析项类型
 */
function initCostAnalysisItemType() {
	for ( var key in COST_ANALYSIS_ITEM_TYPE) {
		$("#txtType").append(
				"<option value='" + key + "'>" + COST_ANALYSIS_ITEM_TYPE[key]
						+ "</option>");
	}
}

/**
 * 清空成本分析项内容
 */
function clearCostAnalysisItem(){
	$("#txtType").val("");
	$("#txtName").val("");
	$("#txtCost").val("");
	$("#txtMemo").val("");
}

/**
 * To增加成本分析项
 */
function btnToAddCostAnalysisItem() {
	// 清除保存在的当前选择的成本分析项ID
	$("#txtCostAnalysisItemId").html("");
	showDialog();
}

/**
 * 增加成本分析项
 */
function addCostAnalysisItem() {
	var projectId = getUrlParam("project.id");
	var type = $("#txtType").val();
	var name = $("#txtName").val();
	var cost = $("#txtCost").val();
	var memo = $("#txtMemo").val();

	if(!verifyNotEmpty(type, "成本类型", true)){
		return;
	}
	if(!verifyStringNotEmpty(name, "成本项名称", 32, true)){
		return;
	}
	if(!verifyMoneyGt0(cost, "成本费用", true)){
		return;
	}
	if(!verifyString(memo, "备注", 1024, true)){
		return;
	}
	var costAnalysisItem = {
		projectId : projectId,
		type : type,
		cost : cost,
		name : name,
		memo : memo
	};
	ajaxAddCostAnalysisItem(getUrlParam("project.id"), costAnalysisItem, function(data){
		closeDialog();
		// 刷新成本分析信息
		showCostAnalysisDetail();
	}, function(data){}, true, waiting, run, true);
}

/**
 * 修改成本分析项
 */
function modifyCostAnalysisItem() {
	var projectId = getUrlParam("project.id");
	var id = $("#txtCostAnalysisItemId").html();
	var type = $("#txtType").val();
	var name = $("#txtName").val();
	var cost = $("#txtCost").val();
	var memo = $("#txtMemo").val();

	if (isEmpty(projectId)){
		alert("无效的项目");
		return;
	}
	if (isEmpty(id)){
		alert("无效的成本分析项");
		return;
	}
	if(!verifyNotEmpty(type, "成本类型", true)){
		return;
	}
	if(!verifyStringNotEmpty(name, "成本项名称", 32, true)){
		return;
	}
	if(!verifyMoneyGt0(cost, "成本费用", true)){
		return;
	}
	if(!verifyString(memo, "备注", 1024, true)){
		return;
	}
	var costAnalysisItem = {
		projectId : projectId,
		id : id,
		type : type,
		cost : cost,
		name : name,
		memo : memo
	};
	ajaxModifyCostAnalysisItem(getUrlParam("project.id"), costAnalysisItem, function(data){
		closeDialog();
		// 刷新成本分析信息
		showCostAnalysisDetail();
	}, function(data){}, true, waiting, run, true);
}

/**
 * 保存成本分析项
 */
function btnSaveCostAnalysisItem() {
	if(!preventRapidClick()){
		return;
	}
	if (isEmpty($("#txtCostAnalysisItemId").html())) {
		// 为空，则表示按钮，进行添加成本分析项
		addCostAnalysisItem();
	} else {
		// 修改成本分析项
		modifyCostAnalysisItem();
	}
}

/**
 * 显示对话框
 */
function showDialog() {
	$("#dialog").show();
}

/**
 * 隐藏对话框
 */
function closeDialog() {
	clearCostAnalysisItem();
	$("#dialog").hide();
}

/**
 * 修改成本分析项
 */
function btnToModifyCostAnalysisItem() {
	var costAnalysisItemId = getSelectedTableTrValue("#tableCost");
	if (!isEmpty(costAnalysisItemId)){
		ajaxGetCostAnalysisItemById(getUrlParam("project.id"), costAnalysisItemId, function(data){
			showDialog();
			// 保存当前选择的成本分析项ID
			$("#txtCostAnalysisItemId").html(costAnalysisItemId);
			$("#txtType").val(data.costAnalysisItem.type);
			$("#txtName").val(data.costAnalysisItem.name);
			$("#txtCost").val(data.costAnalysisItem.cost);
			$("#txtMemo").val(data.costAnalysisItem.memo);
		}, function(data){}, true, waiting, run, true);
		
	}
}

/**
 * 删除成本分析项
 */
function btnDeleteCostAnalysisItem() {
	var costAnalysisItemId = getSelectedTableTrValue("#tableCost");
	if (!isEmpty(costAnalysisItemId)){
		if(confirm("确定删除该成本分析项？")){
			ajaxRemoveCostAnalysisItem(getUrlParam("project.id"), costAnalysisItemId, function(data){
				// 刷新成本分析信息
				showCostAnalysisDetail();
			}, function(data){}, true, waiting, run, true);
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
			ajaxAcquireCostAnalysisUpToken(getUrlParam("project.id"), function(
					data) {
				uploader.bind('BeforeUpload', function(uploader, file) {
					// 进行参数配置
					new_multipart_params = {
						'key' : data.dir + "/" + data.uuid + "_" + file.name,
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
					alert("上传成功！");
					showAttachment();
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

/**
 * 显示项目信息
 */
function showProject() {
	ajaxGetProjectDetailById(getUrlParam("project.id"), function(data) {
		showProjectTitle(data.project);
		// 显示附件信息
		showAttachment();
	}, function(data) {
	}, true, waiting, run, true);
}

/**
 * 显示成本分析表信息
 */
function showAttachment() {
	ajaxGetCostAnalysisUniqueAttachment(getUrlParam("project.id"), function(
			data) {
		if (data.hasAttachment) {
			showBtnDeleteFile();
			$("#txtAttachmentId").val(data.attachment.id);
			$("#txtFileName").html(data.attachment.name);
			$("#txtCreatorName").html(data.attachment.staffName);
			$("#txtCreateTime").html(data.attachment.cstCreate);
		} else {
			showBtnUpload();
			$("#txtAttachmentId").val("");
			$("#txtFileName").html("");
			$("#txtCreatorName").html("");
			$("#txtCreateTime").html("");
		}
		showCostAnalysisDetail();
	}, function(data) {
	}, true, waiting, run, true);
}

/**
 * 显示成本分析详情
 */
function showCostAnalysisDetail() {
	ajaxAcquireCostAnalysisDetail(getUrlParam("project.id"), function(data) {
		$("#txtDirectCostSum").html(data.directCostSum);
		$("#txtCostSum").html(data.costSum);
		$("#txtPretaxSum").html(data.pretaxSum);
		$("#txtProfitSum").html(data.profitSum);
		$("#txtSubcontractCostSum").html(data.subcontractCostSum);
		$("#txtIndirectCostSum").html(data.indirectCostSum);
		showCostAnalysisItems(data.costAnalysisItems);
	}, function(data) {
	}, true, waiting, run, true);
}

/**
 * 显示成本项列表
 */
function showCostAnalysisItems(costAnalysisItems) {
	removeTableTr("#tableCost");
	for (var i = 0; i < costAnalysisItems.length; i++) {
		$("#tableCost").append(
				'<tr class="list_mid_row" onclick="btnSelectTableTr(this)"><td hidden="true">'
						+ costAnalysisItems[i].id + '</td><td>'
						+ COST_ANALYSIS_ITEM_TYPE[costAnalysisItems[i].type]
						+ '</td><td>' + costAnalysisItems[i].name + '</td><td>'
						+ costAnalysisItems[i].cost
						+ '</td><td class="list_last_col">'
						+ costAnalysisItems[i].memo + '</td></tr>');
	}
	
}


/**
 * 显示上传按钮
 */
function showBtnUpload() {
	$("#btnUpload").show();
	$("#btnDeleteFile").hide();
	if (uploader == null) {
		uploader = new plupload.Uploader(uploaderParams);
	}
	uploader.init();
}

/**
 * 显示删除文件按钮
 */
function showBtnDeleteFile() {
	$("#btnUpload").hide();
	$("#btnDeleteFile").show();
	if (uploader != null) {
		uploader.destroy();
		uploader = null;
	}
}

/**
 * 删除附件
 */
function btnDeleteFile() {
	if (confirm("确定删除该附件？")) {
		ajaxRemoveCostAnalysisUniqueAttachment(getUrlParam("project.id"), $("#txtAttachmentId").val(),
				function(data) {
					// 刷新附件信息
					showAttachment();
				}, function(data) {
				}, true, waiting, run, true);
	}
}

/**
 * 下载附件
 */
function btnDownloadFile() {
	// 获取授权信息
	ajaxAcquireCostAnalysisDownToken(getUrlParam("project.id"), $("#txtAttachmentId").val(),
			function(data) {
				window.location = data.url;
			}, function() {
			}, true, waiting, run, true);
}
