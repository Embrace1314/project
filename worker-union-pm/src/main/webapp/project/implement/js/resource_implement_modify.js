
/**
 * 页面启动调用
 */
$(function() {
	$("#txtUserName").html($.cookie(COOKIE_STAFF_NAME_KEY));
    // 显示项目名称
    showProject();
    //初始化资金需求下拉框
    initRequireItemSelect();
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
 * 显示资源落实项信息
 */
function showResourceItem() {
    ajaxGetResourceImplementItemById(getUrlParam("project.id"), getUrlParam("resourceImplementItem.id"), function(data){

        $("#txtRequire").val(data.hasFundPlanItem?data.resourceImplementItem.fundPlanItemId:"");
        $("#txtResource").html(data.resourceImplementItem.purchaseItemName);
        $("#txtResourceDetail").val(data.resourceImplementItem.name);
        $("#txtUnit").val(data.resourceImplementItem.unit);
        $("#txtPrice").val(data.resourceImplementItem.price);
        $("#txtAmount").val(data.resourceImplementItem.amount);
        $("#txtSubcontractor").html(data.resourceImplementItem.subcontractorName);
        $("#txtMoney").html(data.resourceImplementItem.money);
        $("#txtContract").html(data.resourceImplementItem.contractName);
        }, function(data){}, true, waiting, run, true);
}

/**
 * 保存按钮
 * 修改资源落实项
 */
function btnModifyResourceitem() {
    var projectId = getUrlParam("project.id");
    var id = getUrlParam("resourceImplementItem.id");
    var require = $("#txtRequire").val();
    var name = $("#txtResourceDetail").val();
    var unit = $("#txtUnit").val();
    var price = $("#txtPrice").val();
    var amount = $("#txtAmount").val();

    if (isEmpty(projectId)){
        alert("无效的项目");
        return;
    }
    if (!verifyStringNotEmpty(name,"资源项目细分",32,true)){
        return;
    }
    if (!verifyStringNotEmpty(unit,"计量单位",32,true)){
        return;
    }
    if (!verifyMoney(price,"单价",true)){
        return;
    }
    if (!verifyAmount(amount,"实际发包数量",true)){
        return;
    }
    if (!verifyMoney(multiply(price,amount),"实际资金额",true)){
        return;
    }
    var resourceItem = {
        projectId : projectId,
        id : id,
        require : require,
        name : name,
        unit : unit,
        price : price,
        amount : amount
    };
    ajaxModifyResourceImplementItemById(resourceItem, function(data){
        alert("修改成功");
        // 跳转到资源落实页面
        gotoLocalHtml('/project/implement/resource_implement.html?project.id='
            + getUrlParam('project.id'));
    }, function(data){}, true, waiting, run, true);
}

