$(function() {
	$("#txtUserName").html($.cookie(COOKIE_STAFF_NAME_KEY));
	// 显示转库记录
	showUndeterminedMaterialTransferApplications();
});

function showUndeterminedMaterialTransferApplications() {
	ajaxListUndeterminedMaterialTransferApplication(
			function(data) {
				$("#tableMaterialTransferApplications").find("tr").first()
						.siblings().remove();
				for (var i = 0; i < data.materialTransferApplications.length; i++) {
					var importProjectName = "";
					if (data.materialTransferApplications[i].type == MATERIAL_TRANSFER_APPLICATION_TYPE_TO_HEADQUARTERS) {
						importProjectName = MATERIAL_TRANSFER_APPLICATION_TYPE[MATERIAL_TRANSFER_APPLICATION_TYPE_TO_HEADQUARTERS];
					} else {
						importProjectName = "("
								+ data.materialTransferApplications[i].importProjectNum
								+ ")"
								+ data.materialTransferApplications[i].importProjectName;
					}
					var exportProjectName = "("
							+ data.materialTransferApplications[i].exportProjectNum
							+ ")"
							+ data.materialTransferApplications[i].exportProjectName;
					var id = data.materialTransferApplications[i].id;
					var staffName = data.materialTransferApplications[i].staffName;
					var cstCreate = data.materialTransferApplications[i].cstCreate
							.substring(0, 10);
					$("#tableMaterialTransferApplications")
							.append(
									'<tr><td hidden="true">'
											+ id
											+ '</td><td><a href="/worker-union-fm/storage/transfer_material_info.html?materialTransferApplication.id='+id+'">'
											+ id
											+ '</a></td><td>'
											+ importProjectName
											+ '</td><td>'
											+ exportProjectName
											+ '</td><td>'
											+ staffName
											+ '</td><td>'
											+ cstCreate
											+ '</td><td class="list_last_col_row"><div class="div_audit_btn"><button class="btn_pass" type="button" onclick="btnPass(this)">通过</button><div class="div_line"></div><button class="btn_rebut" type="button" onclick="btnFail(this)">驳回</button></div></td></tr>');
				}

			}, function(data) {

			}, true, waiting, run, true);

}

/**
 * 通过审核按钮响应函数
 * @param obj
 */
function btnPass(obj) {
	var id =$(obj).parent().parent().siblings().first().html();
	if(!isEmpty(id)){
		if(confirm("确定通过该转库审核?")){
			ajaxPassMaterialTransferApplicationById(id, function(data){
				showUndeterminedMaterialTransferApplications();
			}, function(data){}, true, waiting, run, true);
		}
	}
}

/**
 * 驳回审核按钮响应函数
 * @param id
 */
function btnFail(obj) {
	var id =$(obj).parent().parent().siblings().first().html();
	if(!isEmpty(id)){
		if(confirm("确定驳回该转库审核?")){
			ajaxFailMaterialTransferApplicationById(id, function(data){
				showUndeterminedMaterialTransferApplications();
			}, function(data){}, true, waiting, run, true);
		}
	}
}