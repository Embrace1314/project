/**
 * 页面启动加载
 */
$(function() {
	$("#txtUserName").html($.cookie(COOKIE_STAFF_NAME_KEY));
	initdatepicker_cn();
	$("#txtEntryDate").datepicker({
		dateFormat : "yy-mm-dd"
	});
});

/**
 * 新增员工信息
 */
function btnSave() {
	var jobNo = $("#txtJobNo").val();
	var name = $("#txtName").val();
	var phone = $("#txtPhone").val();
	var idCardNo = $("#txtIdCardNo").val();
	var address = $("#txtAddress").val();
	var entryDate = $("#txtEntryDate").val();
	var career = $("#txtCareer").val();
	var memo = $("#txtMemo").val();

	if(!verifyStringNotEmpty(name, "姓名", 32,true)){
		return;
	}
	if(!verifyStringNotEmpty(jobNo, "工号", 32,true)){
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
		jobNo : jobNo,
		name : name,
		phone : phone,
		idCardNo : idCardNo,
		address : address,
		entryDate : entryDate,
		career : career,
		memo : memo
	};
	// 新增员工信息
	ajaxAddStaff(staff, function(data) {
		alert("添加成功！");
		gotoBack();
	}, function(data) {
	}, true, waiting, run, true);
}