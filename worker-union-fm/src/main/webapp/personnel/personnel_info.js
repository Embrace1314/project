var ungrantedRoles = new Array();
var grantedRoles = new Array();

function Role(id, name) // 声明对象
{
	this.id = id;
	this.name = name;
}

/**
 * 页面启动加载
 */
$(function() {
	$("#txtUserName").html($.cookie(COOKIE_STAFF_NAME_KEY));
	initdatepicker_cn();
	$("#txtEntryDate").datepicker({
		dateFormat : "yy-mm-dd"
	});
	showStaffDetail();
});

/**
 * 显示员工信息详情
 */
function showStaffDetail() {
	ajaxGetStaffDetailById(getUrlParam("staff.id"), function(data) {
		$("#txtName").val(data.staffDto.name);
		$("#txtPhone").val(data.staffDto.phone);
		$("#txtAddress").val(data.staffDto.address);
		$("#txtEntryDate").val(data.staffDto.entryDate.substring(0, 10));
		$("#txtCareer").val(data.staffDto.career);
		$("#txtMemo").val(data.staffDto.memo);
		$("#txtJobNo").html(data.staffDto.jobNo);
		$("#txtIdCardNo").val(data.staffDto.idCardNo);
		$("#txtAge").html(data.staffDto.age);
		$("#txtSex").html(SEX[data.staffDto.sex]);
		$("#txtNativePlace").html(data.staffDto.nativePlace);
		$("#txtRoleName").html(data.staffDto.roleName);
		$("#txtProjectName").html(data.staffDto.projectName);
		showStaffCertificates(data.certificates);
	}, function(data) {
	}, true, waiting, run, true);
}

/**
 * 显示员工的资质列表
 * @param certificates
 */
function showStaffCertificates(certificates){
	var content ="";
	for(var i=0; i<certificates.length; i++){
		content+=certificates[i].num+"  "+certificates[i].certificateTypeName+"\n";
	}
	$("#txtCertificates").val(content);
}

/**
 * 去配置角色
 */
function btnToSetRole() {
	$("#dialogSetRole").show();
	showAllRoles();
}

/**
 * 隐藏配置岗位窗口
 * 
 * @returns
 */
function btnToHideDialogSetRole() {
	$("#dialogSetRole").hide();
}

/**
 * 初始化角色数组
 * 
 * @param localRoles
 * @param dataRoles
 */
function initRoleArray(localRoles, dataRoles) {
	for (var i = 0; i < dataRoles.length; i++) {
		localRoles.push(new Role(dataRoles[i].id, dataRoles[i].name));
	}
}

/**
 * 显示所有角色信息
 */
function showAllRoles() {
	ungrantedRoles.splice(0, ungrantedRoles.length);
	grantedRoles.splice(0, grantedRoles.length);
	ajaxListRoleByCompany(function(data) {
		initRoleArray(ungrantedRoles, data.roles);
		ajaxListRoleByStaff(getUrlParam("staff.id"), function(data) {
			for (var i = 0; i < data.roles.length; i++) {
				removeRolesItemById(ungrantedRoles, grantedRoles,
						data.roles[i].id);
			}
			refreshUngrantedRoles(ungrantedRoles);
			refreshGrantedRoles(grantedRoles);
		}, function(data) {
		}, true, waiting, run, true);
	}, function(data) {
	}, true, waiting, run, true);
}

function btnGrant() {
	var roleId = getSelectedTableTrValue("#ungrantedRoles");
	if (!isEmpty(roleId)) {
		removeRolesItemById(ungrantedRoles, grantedRoles, roleId);
		refreshUngrantedRoles(ungrantedRoles);
		refreshGrantedRoles(grantedRoles);
	}
}
function btnUngrant() {
	var roleId = getSelectedTableTrValue("#grantedRoles");
	if (!isEmpty(roleId)) {
		removeRolesItemById(grantedRoles, ungrantedRoles, roleId);
		refreshUngrantedRoles(ungrantedRoles);
		refreshGrantedRoles(grantedRoles);
	}
}
/**
 * 根据ID删除角色信息
 * 
 * @param roleId
 */
function removeRolesItemById(localRolesFrom, localRolesTo, roleId) {
	for (var i = 0; i < localRolesFrom.length; i++) {
		if (localRolesFrom[i].id == roleId) {
			localRolesTo.push(new Role(localRolesFrom[i].id,
					localRolesFrom[i].name));
			localRolesFrom.splice(i, 1);
			return;
		}
	}
}

/**
 * 显示未授权角色列表
 * 
 * @param roles
 */
function refreshUngrantedRoles(roles) {
	removeTableTr("#ungrantedRoles");
	for (var i = 0; i < roles.length; i++) {
		$("#ungrantedRoles").append(
				'<tr class="list_mid_row"><td hidden="true">' + roles[i].id
						+ '</td><td>' + roles[i].name + '</td></tr>');
		setRowOnClickSelected("#ungrantedRoles", i, true);
	}
}

/**
 * 显示已授权角色列表
 * 
 * @param roles
 */
function refreshGrantedRoles(roles) {
	removeTableTr("#grantedRoles");
	for (var i = 0; i < roles.length; i++) {
		$("#grantedRoles").append(
				'<tr class="list_mid_row"><td hidden="true">' + roles[i].id
						+ '</td><td>' + roles[i].name + '</td></tr>');
		setRowOnClickSelected("#grantedRoles", i, true);
	}
}

/**
 * 保存权限
 */
function btnSavePermission(){
	ajaxSaveStaffRole(getUrlParam("staff.id"), grantedRoles, function(data){
		alert("岗位配置成功！");
		reload();
	}, function(data){}, true, waiting, run, true);
}

/**
 * 修改员工信息
 */
function btnSave() {
	var name = $("#txtName").val();
	var phone = $("#txtPhone").val();
	var address = $("#txtAddress").val();
	var entryDate = $("#txtEntryDate").val();
	var career = $("#txtCareer").val();
	var memo = $("#txtMemo").val();
	var idCardNo = $("#txtIdCardNo").val();

	if(!verifyStringNotEmpty(name, "姓名", 32,true)){
		return;
	}
	if(!verifyStringNotEmpty(idCardNo, "身份证号码", 32,true)){
		return;
	}
	if(!verifyString(phone,"联系方式",32,true)){
		return;
	}
	if(!verifyString(address,"家庭住址",512,true)){
		return;
	}
	if(!verifyString(career,"职业经历",10240,true)){
		return;
	}
	if(!verifyString(memo,"备注",1024,true)){
		return;
	}
	if (!verifyDate(entryDate,"入职时间",true)) {
		return;
	}
	var staff = {
		id : getUrlParam("staff.id"),
		name : name,
		phone : phone,
		address : address,
		entryDate : entryDate,
		career : career,
		memo : memo,
		idCardNo : idCardNo
	};
	// 修改员工信息
	ajaxModifyStaffById(staff, function(data) {
		alert("修改成功！");
		gotoBack();
	}, function(data) {
	}, true, waiting, run, true);
}