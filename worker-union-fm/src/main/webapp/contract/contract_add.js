/**
 * 页面启动加载
 */
$(function() {
	$("#txtUserName").html($.cookie(COOKIE_STAFF_NAME_KEY));
	initdatepicker_cn();
	$("#txtSignDate").datepicker({
		dateFormat : "yy-mm-dd"
	});
	showListUnarchivedProject();
});

/**
 * 显示项目组下拉框
 */
function showListUnarchivedProject() {
	ajaxListUnarchivedProject(function(data) {
		$("#selProject").append("<option value=''>请选择项目组</option>");
		for (var i = 0; i < data.projects.length; i++) {
			$("#selProject").append(
					"<option value='" + data.projects[i].id + "'>" + "("
							+ data.projects[i].num + ")"
							+ data.projects[i].name + "</option>");
		}
	}, function(data) {
	}, true, waiting, run, true);
}

/**
 * 创建合同，并进行下一步
 */
function btnNext() {
	var name = $("#txtName").val();
	var num = $("#txtNum").val();
	var type = $("#txtType").val();
	var partA = $("#txtPartA").val();
	var partB = $("#txtPartB").val();
	var signDate = $("#txtSignDate").val();
	var money = $("#txtMoney").val();
	var dutyDepartment = $("#txtDutyDepartment").val();
	var contentAbstract = $("#txtContentAbstract").val();
	var projectId = $("#selProject").val();
	if (!verifyStringNotEmpty(name, "合同名称", 512, true)) {
		return;
	}
	if (!verifyStringNotEmpty(num, "合同编号", 32, true)) {
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
	if (!verifyNotEmpty(projectId, "项目组", true)) {
		return;
	}
	var contract = {
		num : num,
		name : name,
		type : type,
		partA : partA,
		partB : partB,
		signDate : signDate,
		money : money,
		dutyDepartment : dutyDepartment,
		contentAbstract : contentAbstract,
		projectId : projectId
	}
	// 新增合同信息
	ajaxAddContract(contract,
			function(data) {
				gotoLocalHtml("/contract/contract_add_next.html?contract.id="
						+ data.id);
			}, function(data) {
			}, true, waiting, run, true);
}
