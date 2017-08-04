/**
 * 页面启动调用
 */
$(function() {
	checkAuthorization();
});

/**
 * 获取授权信息
 */
function checkAuthorization() {
	$("#txtUserName").html($.cookie(COOKIE_STAFF_NAME_KEY));
	// 发起AJAX请求
	ajaxCheckAuthorization(function(data) {
		// 项目管理
		if (data.project) {
			$("#btnProjectManage").attr("class", "btn_manage");
			$("#btnProjectManage").attr("disabled", false);
		} else {
			$("#btnProjectManage").attr("class", "btn_manage_disabled");
			$("#btnProjectManage").attr("disabled", "disabled");
		}
		// 证书管理
		if (data.certificate) {
			$("#btnCertificateManage").attr("class", "btn_manage");
			$("#btnCertificateManage").attr("disabled", false);
		} else {
			$("#btnCertificateManage").attr("class", "btn_manage_disabled");
			$("#btnCertificateManage").attr("disabled", "disabled");
		}
		// 人员管理
		if (data.personnel) {
			$("#btnPersonnelManage").attr("class", "btn_manage");
			$("#btnPersonnelManage").attr("disabled", false);
		} else {
			$("#btnPersonnelManage").attr("class", "btn_manage_disabled");
			$("#btnPersonnelManage").attr("disabled", "disabled");
		}
		// 合同管理
		if (data.contract) {
			$("#btnContractManage").attr("class", "btn_manage");
			$("#btnContractManage").attr("disabled", false);
		} else {
			$("#btnContractManage").attr("class", "btn_manage_disabled");
			$("#btnContractManage").attr("disabled", "disabled");
		}
		// 供应商管理
		if (data.supplier) {
			$("#btnSupplierManage").attr("class", "btn_manage");
			$("#btnSupplierManage").attr("disabled", false);
		} else {
			$("#btnSupplierManage").attr("class", "btn_manage_disabled");
			$("#btnSupplierManage").attr("disabled", "disabled");
		}
		// 分包商管理
		if (data.subcontractor) {
			$("#btnSubcontractorManage").attr("class", "btn_manage");
			$("#btnSubcontractorManage").attr("disabled", false);
		} else {
			$("#btnSubcontractorManage").attr("class", "btn_manage_disabled");
			$("#btnSubcontractorManage").attr("disabled", "disabled");
		}
		// 材料管理
		if (data.material) {
			$("#btnMaterialManage").attr("class", "btn_manage");
			$("#btnMaterialManage").attr("disabled", false);
		} else {
			$("#btnMaterialManage").attr("class", "btn_manage_disabled");
			$("#btnMaterialManage").attr("disabled", "disabled");
		}
		// 仓库管理
		if (data.storage) {
			$("#btnStorageManage").attr("class", "btn_manage");
			$("#btnStorageManage").attr("disabled", false);
		} else {
			$("#btnStorageManage").attr("class", "btn_manage_disabled");
			$("#btnStorageManage").attr("disabled", "disabled");
		}
	}, function(data) {
	}, true, waiting, run, true);

	// 获取待办事项
	ajaxListSchedule(function(data) {
		showSchedule(data.attachments, data.safetyCheckItems, data.qualityCheckItems);
	}, function(data) {
	}, true, waiting, run, true);
}

/**
 * 显示待办事项
 */
function showSchedule(attachments, safetyCheckItems, qualityCheckItems) {
	removeTableTr("#tableSchedules");
	var trLen = 1;
	for (var i = 0; i < attachments.length; i++) {
		$("#tableSchedules").append(
			'<tr class="list_mid_row"><td hidden="true">'
			+ attachments[i].id + '</td><td>' + trLen + '</td><td>'
			+ attachments[i].projectNum + '</td><td>'
			+ attachments[i].projectName + '</td><td><a href="'+SCHEDULE_URL[attachments[i].type]+"?project.id="+attachments[i].projectId+'">'
			+ SCHEDULE_TYPE[attachments[i].type] + '</a></td><td>'
			+ attachments[i].staffName
			+ '</td><td class="list_last_col_row">'
			+ attachments[i].cstCreate.substring(0, 10) + '</td>');
		trLen++;
	}
	for(var i=0; i<safetyCheckItems.length; i++){
		$("#tableSchedules").append(
			'<tr class="list_mid_row"><td hidden="true">'
			+ safetyCheckItems[i].id + '</td><td>' + trLen + '</td><td>'
			+ safetyCheckItems[i].projectNum + '</td><td>'
			+ safetyCheckItems[i].projectName + '</td><td><a href="'+SCHEDULE_URL["SAFETY_CHECK_ITEM"]+"?project.id="+safetyCheckItems[i].projectId+"&safetyCheckItem.id="+safetyCheckItems[i].id+'">'
			+ SCHEDULE_TYPE["SAFETY_CHECK_ITEM"] + '</a></td><td>'
			+ safetyCheckItems[i].staffName
			+ '</td><td class="list_last_col_row">'
			+ safetyCheckItems[i].cstCreate.substring(0, 10) + '</td>');
		trLen++;
	}
	for(var i=0; i<qualityCheckItems.length; i++){
		$("#tableSchedules").append(
			'<tr class="list_mid_row"><td hidden="true">'
			+ qualityCheckItems[i].id + '</td><td>' + trLen + '</td><td>'
			+ qualityCheckItems[i].projectNum + '</td><td>'
			+ qualityCheckItems[i].projectName + '</td><td><a href="'+SCHEDULE_URL["QUALITY_CHECK_ITEM"]+"?project.id="+qualityCheckItems[i].projectId+"&qualityCheckItem.id="+qualityCheckItems[i].id+'">'
			+ SCHEDULE_TYPE["QUALITY_CHECK_ITEM"] + '</a></td><td>'
			+ qualityCheckItems[i].staffName
			+ '</td><td class="list_last_col_row">'
			+ qualityCheckItems[i].cstCreate.substring(0, 10) + '</td>');
		trLen++;
	}

	$("#txtScheduleCount").html(trLen-1);
}

/**
 * 修改密码
 * 显示修改密码弹窗
 */
function btnToModifyPasswordDialog() {
	$("#dialog").show();
}

/**
 * 保存密码
 */
function btnSavePassword() {
	var original = $("#txtOriginal").val();
	var newCode = $("#txtNew").val();
	var confirm = $("#txtConfirm").val();

	if (!verifyNotEmpty(original, "原始密码",true)){
		return;
	}
	if (!verifyNotEmpty(newCode, "新密码",true)){
		return;
	}
	if (!verifyNotEmpty(confirm, "确认新密码",true)){
		return;
	}
	if(newCode!=confirm){
		alert("新密码与确认密码输入不一致！");
		return;
	}
	ajaxChangePassword(original, newCode, confirm, function(data){
		closeDialog();
		alert("密码修改成功，请重新登录！");
		// 重新登录
		gotoHtml(HTML_LOGIN);
	}, function(data){}, true, waiting, run, true);
}

/**
 * 关闭弹窗
 */
function closeDialog() {
	$("#txtOriginal").val("");
	$("#txtNew").val("");
	$("#txtConfirm").val("");
	$("#dialog").hide();
}

