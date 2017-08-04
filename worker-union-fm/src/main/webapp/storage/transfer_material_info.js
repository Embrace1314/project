$(function() {
	$("#txtUserName").html($.cookie(COOKIE_STAFF_NAME_KEY));
	// 显示转库记录
	showMaterialTransferApplicationDetail();
});

function showMaterialTransferApplicationDetail() {
	ajaxGetMaterialTransferApplicationDetailById(
			getUrlParam("materialTransferApplication.id"),
			function(data) {
				var importProjectName = "";
				if (data.materialTransferApplication.type == MATERIAL_TRANSFER_APPLICATION_TYPE_TO_HEADQUARTERS) {
					importProjectName = MATERIAL_TRANSFER_APPLICATION_TYPE[MATERIAL_TRANSFER_APPLICATION_TYPE_TO_HEADQUARTERS];
				} else {
					importProjectName = "("
							+ data.materialTransferApplication.importProjectNum
							+ ")"
							+ data.materialTransferApplication.importProjectName;
				}
				exportProjectName = "("
						+ data.materialTransferApplication.exportProjectNum
						+ ")"
						+ data.materialTransferApplication.exportProjectName;
				var id = data.materialTransferApplication.id;
				var staffName = data.materialTransferApplication.staffName;
				var cstCreate = data.materialTransferApplication.cstCreate
						.substring(0, 10);
				var status = data.materialTransferApplication.status;
				$("#txtImportProjectName").html(importProjectName);
				$("#txtExportProjectName").html(exportProjectName);
				$("#txtStaffName").html(staffName);
				$("#txtCstCreate").html(cstCreate);
				$("#txtStatus").html(MATERIAL_TRANSFER_APPLICATION_STATUS[status]);
				showMaterialTransferApplicationDetails(data.materialTransferApplicationDetails);
			}, function(data) {

			}, true, waiting, run, true);
}

/**
 * 显示材料详情
 * @param materialTransferApplicationDetails
 */
function showMaterialTransferApplicationDetails(materialTransferApplicationDetails){
	$("#tableMaterialTransferApplicationDetails").find("tr").first()
	.siblings().remove();
	for(var i=0; i<materialTransferApplicationDetails.length; i++){
		$("#tableMaterialTransferApplicationDetails")
		.append('<tr><td>'+materialTransferApplicationDetails[i].num+'</td><td>'+materialTransferApplicationDetails[i].name+'</td><td>'+materialTransferApplicationDetails[i].model+'</td><td>'+materialTransferApplicationDetails[i].amount+'</td><td class="list_last_col">'+materialTransferApplicationDetails[i].price+'</td></tr>');
	}
}
