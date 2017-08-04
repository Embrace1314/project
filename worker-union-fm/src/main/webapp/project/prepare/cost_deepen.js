
/**
 * 页面初始调用
 **/
$(function () {
    $("#txtUserName").html($.cookie(COOKIE_STAFF_NAME_KEY));
    //显示项目名称
    showProject();
});

/**
 * 显示项目名称
 **/
function showProject(){
    ajaxGetProjectDetailById(getUrlParam("project.id"), function (data) {
        showProjectTitle(data.project);
    }, function (data) {
    }, true, waiting, run, true);
    //显示成本深化表
    showCostDeepenFile();
}

/**
 * 显示成本深化表
 */
function showCostDeepenFile() {
    ajaxGetCostAnalysisDeepenUniqueAttachment(getUrlParam("project.id"), function (data) {
        if(data.hasAttachment){
            //显示下载按钮
            $("#btnDownloadCost").show();
            $("#btnDownloadCostDisable").hide();
            //显示成本深化表
            $("#txtAttachmentId").val(data.attachment.id);
            $("#txtFileName").html(data.attachment.name);
            $("#txtCreatorName").html(data.attachment.staffName);
            $("#txtCreateTime").html(data.attachment.cstCreate);
        }else {
            //显示下载按钮不可点击状态
            $("#btnDownloadCost").hide();
            $("#btnDownloadCostDisable").show();
            //显示成本深化表
            $("#txtAttachmentId").val("");
            $("#txtFileName").html("");
            $("#txtCreatorName").html("");
            $("#txtCreateTime").html("");
        }
        //显示成本深化详情
        showCostDeepenDetail();
    }, function (data) {
    }, true, waiting, run, true);
}

/**
 * 显示成本深化详情
 **/
function showCostDeepenDetail() {
    ajaxAcquireCostAnalysisDeepenDetail(getUrlParam("project.id"), function(data) {
        $("#txtDirectCostSum").html(data.directCostSum);
        $("#txtCostSum").html(data.costSum);
        $("#txtPretaxSum").html(data.pretaxSum);
        $("#txtProfitSum").html(data.profitSum);
        $("#txtSubcontractCostSum").html(data.subcontractCostSum);
        $("#txtIndirectCostSum").html(data.indirectCostSum);
        showCostDeepenItems(data.costAnalysisItemDeepens);
    }, function() {
    }, true, waiting, run, true);
}

/**
 * 显示成本深化项列表
 */
function showCostDeepenItems(costAnalysisItemDeepens) {
    removeTableTr("#tableCost");
    for (var i = 0; i < costAnalysisItemDeepens.length; i++) {
        if(i == costAnalysisItemDeepens.length){
            $("#tableCost").append(
                '<tr class="list_last_row"><td hidden="true">'
                + costAnalysisItemDeepens[i].id + '</td><td>'
                + COST_ANALYSIS_ITEM_TYPE[costAnalysisItemDeepens[i].type]
                + '</td><td>' + costAnalysisItemDeepens[i].name + '</td><td>'
                + costAnalysisItemDeepens[i].price + '</td><td>'
                + costAnalysisItemDeepens[i].amount + '</td><td>'
                + costAnalysisItemDeepens[i].unit + '</td><td>'
                + costAnalysisItemDeepens[i].cost
                + '</td><td class="list_last_col">'
                + costAnalysisItemDeepens[i].memo + '</td></tr>');
        }else{
            $("#tableCost").append(
                '<tr class="list_mid_row"><td hidden="true">'
                + costAnalysisItemDeepens[i].id + '</td><td>'
                + COST_ANALYSIS_ITEM_TYPE[costAnalysisItemDeepens[i].type]
                + '</td><td>' + costAnalysisItemDeepens[i].name + '</td><td>'
                + costAnalysisItemDeepens[i].price + '</td><td>'
                + costAnalysisItemDeepens[i].amount + '</td><td>'
                + costAnalysisItemDeepens[i].unit + '</td><td>'
                + costAnalysisItemDeepens[i].cost
                + '</td><td class="list_last_col">'
                + costAnalysisItemDeepens[i].memo + '</td></tr>');
        }
    }

}

/**
 *下载成本深化表
 */
function btnDownloadFile() {
    // 获取授权信息
    ajaxAcquireCostDeepenDownToken(getUrlParam("project.id"), $("#txtAttachmentId").val(),
        function(data) {
    		window.location = data.url;
        }, function() {
        }, true, waiting, run, true);
}

