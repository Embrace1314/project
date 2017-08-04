/**
 * 页面启动加载
 */
$(function() {
	$("#txtUserName").html($.cookie(COOKIE_STAFF_NAME_KEY));
});

/**
 * 创建材料类型，并进行下一步
 */
function btnNext(){
	var name = $("#txtName").val();
	var num = $("#txtNum").val();
	var model = $("#txtModel").val();
	var unit = $("#txtUnit").val();
	
	if (!verifyStringNotEmpty(name, "材料类型名称", 512, true)){
		return;
	}
	if (!verifyStringNotEmpty(num, "材料类型编号", 32, true)){
		return;
	}
	if (!verifyStringNotEmpty(model, "材料类型规格型号", 32, true)){
		return;
	}
	if (!verifyStringNotEmpty(unit, "材料类型计量单位", 32, true)){
		return;
	}

	var materialType = {
			name:name,
			num:num,
			model:model,
			unit:unit
	};
	// 新增材料类型信息
	ajaxAddMaterialType(materialType, function(data) {
		gotoLocalHtml("/material/material_add_next.html?materialType.id="+data.id);
	}, function(data) {
	}, true, waiting, run, true);
}

