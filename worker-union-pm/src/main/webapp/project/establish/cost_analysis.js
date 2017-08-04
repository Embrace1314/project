/**
 * 页面启动调用
 */
$(function() {
	$("#txtUserName").html($.cookie(COOKIE_STAFF_NAME_KEY));
    // 显示项目信息
    showProject();
});

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
            $("#btnDownloadCost").show();
            $("#btnDownloadCostDisable").hide();
            $("#txtAttachmentId").val(data.attachment.id);
            $("#txtFileName").html(data.attachment.name);
            $("#txtCreatorName").html(data.attachment.staffName);
            $("#txtCreateTime").html(data.attachment.cstCreate);
        }else {
            $("#btnDownloadCost").hide();
            $("#btnDownloadCostDisable").show();
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
    for (var i = 0; i < costAnalysisItems.length-1; i++) {
        $("#tableCost").append(
            '<tr class="list_mid_row"><td hidden="true">'
            + costAnalysisItems[i].id + '</td><td>'
            + COST_ANALYSIS_ITEM_TYPE[costAnalysisItems[i].type]
            + '</td><td>' + costAnalysisItems[i].name + '</td><td>'
            + costAnalysisItems[i].cost
            + '</td><td class="list_last_col">'
            + costAnalysisItems[i].memo + '</td></tr>');
    }
    $("#tableCost")
        .append(
            '<tr class="list_last_row"><td hidden="true">'
            + costAnalysisItems[costAnalysisItems.length-1].id + '</td><td>'
            + COST_ANALYSIS_ITEM_TYPE[costAnalysisItems[costAnalysisItems.length-1].type]
            + '</td><td>' + costAnalysisItems[costAnalysisItems.length-1].name + '</td><td>'
            + costAnalysisItems[costAnalysisItems.length-1].cost
            + '</td><td class="list_last_col">'
            + costAnalysisItems[costAnalysisItems.length-1].memo + '</td></tr>');
}

/**
 * 下载成本分析表
 */
function btnDownloadFile() {
    // 获取授权信息
    ajaxAcquireCostAnalysisDownToken(getUrlParam("project.id"), $("#txtAttachmentId").val(),
        function(data) {
			window.location = data.url;
        }, function() {
        }, true, waiting, run, true);
}