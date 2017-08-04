var JANUARY = 1;
var DECEMBER = 12;

/**
 * 页面启动调用
 */
$(function() {
	$("#txtUserName").html($.cookie(COOKIE_STAFF_NAME_KEY));
    // 显示项目名称
    showProject();
    //初始化资源项目细分下拉框
    initResourceDetail();
});

/**
 * 显示项目名称
 */
function showProject() {
    ajaxGetProjectDetailById(getUrlParam("project.id"), function(data) {
    	showProjectTitle(data.project);
    }, function(data) {
    }, true, waiting, run, true);
}

/**
 * 初始化资源项目细分下拉框
 */
function initResourceDetail() {
    ajaxListSimpleResourceImplementItem(getUrlParam("project.id"), function (data) {
        for(var i = 0; i < data.resourceImplementItems.length; i++){
            $("#txtResourceDetail").append(
                "<option value='" + data.resourceImplementItems[i].id + "'>" + data.resourceImplementItems[i].name
                + "</option>");
            $("#txtMoney").append(
                "<option value='" + data.resourceImplementItems[i].id + "'>" + data.resourceImplementItems[i].money
                + "</option>");
        }
        //显示结算项信息
        showSettlementItem();
    }, function (data) {
    }, true, waiting, run, true);
}

/**
 * 资源项目细分下拉框修改时
 * 显示相应的资金额
 */
function showMoney(obj) {
    $("#txtMoney").val(obj);
}

/**
 * 显示结算项信息
 */
function showSettlementItem(obj) {
    ajaxGetSettlementItemById(getUrlParam("project.id"), getUrlParam("settlementItem.id"), function(data){
        $("#selectYear").html(data.settlementItem.year);
        $("#selectMonth").html(data.settlementItem.month);
        $("#txtResource").html(data.settlementItem.purchaseItemName);
        $("#txtResourceDetail").val(data.settlementItem.resourceImplementItemId);
        $("#txtSubcontractor").html(data.settlementItem.subcontractorName);
        $("#txtSettlement").val(data.settlementItem.money);
        $("#txtMoney").val(data.settlementItem.resourceImplementItemId);
    }, function(data){}, true, waiting, run, true);
}

/**
 * 保存按钮
 * 修改资源落实项
 */
function btnModifySettlementItem() {
    var projectId = getUrlParam("project.id");
    var id = getUrlParam("settlementItem.id");
    var name = $("#txtResourceDetail").val();
    var settlement = $("#txtSettlement").val();

    if (isEmpty(projectId)){
        alert("无效的项目");
        return;
    }
    if (!verifyNotEmpty(name,"资源项目细分",true)){
        return;
    }
    if (!verifyMoney(settlement,"本月结算值",true)){
        return;
    }
    var settlementItem = {
        projectId : projectId,
        id : id,
        name : name,
        settlement : settlement
    };
    ajaxModifySettlementItemById(settlementItem, function(data){
        alert("修改成功");
        // 跳转到结算管理页面
        gotoLocalHtml('/project/implement/settlement_manage.html?project.id='
            + getUrlParam('project.id'));
    }, function(data){}, true, waiting, run, true);
}
