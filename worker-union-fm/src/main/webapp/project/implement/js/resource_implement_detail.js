

/**
 * 页面启动调用
 */
$(function() {
    // 显示项目名称
    showProject();
    //初始化资金需求下拉框
    initRequireItemSelect();
});

/**
 * 初始化资金需求下拉框
 */
function initRequireItemSelect() {
    ajaxListSimpleFundPlanItem(getUrlParam("project.id"), function (data) {
        for(var i = 0; i < data.fundPlanItems.length; i++){
            $("#txtRequire").append(
                "<option value='" + data.fundPlanItems[i].id + "'>" + data.fundPlanItems[i].requirement
                + "</option>");
        }
        //显示资源落实项信息
        showResourceItem();
    }, function (data) {
    }, true, waiting, run, true);
}

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
 * 显示资源落实项信息
 */
function showResourceItem() {
    ajaxGetResourceImplementItemById(getUrlParam("project.id"), getUrlParam("resourceImplementItem.id"), function(data){
        $("#txtRequire").val(data.hasFundPlanItem?data.resourceImplementItem.fundPlanItemId:"");
        $("#txtResource").html(data.resourceImplementItem.purchaseItemName);
        $("#txtResourceDetail").html(data.resourceImplementItem.name);
        $("#txtUnit").html(data.resourceImplementItem.unit);
        $("#txtPrice").html(data.resourceImplementItem.price);
        $("#txtAmount").html(data.resourceImplementItem.amount);
        $("#txtSubcontractor").html(data.resourceImplementItem.subcontractorName);
        $("#txtMoney").html(data.resourceImplementItem.money);
        $("#txtContract").html(data.resourceImplementItem.contractName);
    }, function(data){}, true, waiting, run, true);
}
