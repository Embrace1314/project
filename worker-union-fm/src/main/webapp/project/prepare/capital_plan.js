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
    //显示资金计划详细
    showCapitalDetail();
}

/**
 * 显示资金计划详细
 */
function showCapitalDetail() {
    ajaxListFundPlanItem(getUrlParam("project.id"), function (data) {
        var moneySum = 0;
        removeTableTr("#tableCapital");
        for (var i = 0; i < data.fundPlanItems.length; i++) {
            moneySum = moneySum + data.fundPlanItems[i].money;
            $("#tableCapital").append(
                '<tr class="list_mid_row"><td hidden="true">'
                + data.fundPlanItems[i].id + '</td><td>'
                + data.fundPlanItems[i].purchaseItemName + '</td><td>'
                + data.fundPlanItems[i].requirement + '</td><td>'
                + data.fundPlanItems[i].money + '</td><td>'
                + data.fundPlanItems[i].expectedPurchaseDate.substring(0,10) + '</td><td>'
                + data.fundPlanItems[i].expectedServiceDate.substring(0,10)
                + '</td><td class="list_last_col">'
                + data.fundPlanItems[i].costAnalysisItemDeepenName + '</td></tr>');
        }
        $("#txtMoneySum").html(moneySum);
    }, function (data) {
    }, true, waiting, run, true);
}


