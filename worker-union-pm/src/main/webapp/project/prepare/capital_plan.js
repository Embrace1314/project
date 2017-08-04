/**
 * 页面初始调用
 **/
$(function () {
	$("#txtUserName").html($.cookie(COOKIE_STAFF_NAME_KEY));
    //显示项目名称
    showProject();
    //初始化时间
    initDate();
    //初始化资源项目下拉框
    initCapitalItemSelect();
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
                '<tr class="list_mid_row" onclick="btnSelectTableTr(this)"><td hidden="true">'
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

/**
 * 初始化时间
 */
function initDate() {
    initdatepicker_cn();
    $("#txtPurchaseDateModify").datepicker({
        dateFormat : "yy-mm-dd"
    });
    $("#txtUseDateModify").datepicker({
        dateFormat : "yy-mm-dd"
    });
    $("#txtPurchaseDateAdd").datepicker({
        dateFormat : "yy-mm-dd"
    });
    $("#txtUseDateAdd").datepicker({
        dateFormat : "yy-mm-dd"
    });
}

/**
 * 初始化资源项目下拉框
 */
function initCapitalItemSelect() {
    ajaxListPurchaseItem(function (data) {
       for(var i = 0; i < data.purchaseItems.length; i++){
           $("#txtTypeAdd").append(
               "<option value='" + data.purchaseItems[i].id + "'>" + data.purchaseItems[i].name
               + "</option>");
       }
       //初始化关联成本项下拉框
        initCostItemSelect();
    }, function (data) {
    }, true, waiting, run, true);
}

/**
 * 初始化关联成本项下拉框
 */
function initCostItemSelect() {
    ajaxListCostAnalysisItemDeepen(getUrlParam("project.id"), function (data) {
        for(var i = 0; i < data.costAnalysisItemDeepens.length; i++){
            $("#txtCostTypeModify").append(
                "<option value='" + data.costAnalysisItemDeepens[i].id + "'>" + data.costAnalysisItemDeepens[i].name
                + "</option>");
            $("#txtCostTypeAdd").append(
                "<option value='" + data.costAnalysisItemDeepens[i].id + "'>" + data.costAnalysisItemDeepens[i].name
                + "</option>");
        }
    }, function (data) {
    }, true, waiting, run, true);
}

/**
 * 添加按钮
 * 显示资金计划添加弹窗
 */
function btnToAddDialog() {
    //显示资金计划添加弹窗
    $("#dialogAdd").show();
}

/**
 * 修改按钮
 * 显示资金计划修改弹窗
 */
function btnToModifyDialog() {
    var capitalItemId = getSelectedTableTrValue("#tableCapital");
    if (!isEmpty(capitalItemId)){
        ajaxGetFundPlanItemById(getUrlParam("project.id"), capitalItemId, function(data){
            //显示资金计划修改弹窗
            $("#dialogModify").show();
            // 保存当前选择的资金计划项ID
            $("#txtCapitalItemId").html(capitalItemId);
            $("#txtTypeModify").html(data.fundPlanItem.purchaseItemName);
            $("#txtRequireModify").val(data.fundPlanItem.requirement);
            $("#txtMoneyModify").val(data.fundPlanItem.money);
            $("#txtPurchaseDateModify").val(data.fundPlanItem.expectedPurchaseDate.substring(0,10));
            $("#txtUseDateModify").val(data.fundPlanItem.expectedServiceDate.substring(0,10));
            $("#txtCostTypeModify").val(data.fundPlanItem.costAnalysisItemDeepenId);
        }, function(data){}, true, waiting, run, true);
    }
}

/**
 * 清空弹窗资金计划项编辑框
 */
function clearCapitalItem(){
    $("#txtTypeAdd").val("");
    $("#txtRequireAdd").val("");
    $("#txtMoneyAdd").val("");
    $("#txtPurchaseDateAdd").val("");
    $("#txtUseDateAdd").val("");
    $("#txtCostTypeAdd").val("");
}

/**
 * 关闭弹窗
 */
function closeDialog() {
    //清空弹窗编辑框
    clearCapitalItem();
    $("#dialogAdd").hide();
    $("#dialogModify").hide();
}

/**
 * 添加弹窗
 * 保存
 */
function btnSaveCapitalItem() {
    var projectId = getUrlParam("project.id");
    var type = $("#txtTypeAdd").val();
    var require = $("#txtRequireAdd").val();
    var money = $("#txtMoneyAdd").val();
    var purchaseDate = $("#txtPurchaseDateAdd").val();
    var useDate = $("#txtUseDateAdd").val();
    var costType = $("#txtCostTypeAdd").val();

    if (isEmpty(projectId)){
        alert("无效的项目");
        return;
    }
    if (!verifyNotEmpty(type,"资源项目",true)){
        return;
    }
    if (!verifyStringNotEmpty(require,"需求说明",32,true)){
        return;
    }
    if (!verifyMoney(money,"资金量",true)){
        return;
    }
    if (!verifyDate(purchaseDate,"预计采购时间",true)){
        return;
    }
    if (!verifyDate(useDate,"使用时间",true)){
        return;
    }
    if (!verifyNotEmpty(costType,"关联成本项",true)){
        return;
    }
    var capitalItem = {
        projectId : projectId,
        type : type,
        require : require,
        money : money,
        purchaseDate : purchaseDate,
        useDate : useDate,
        costType : costType
    };
    ajaxAddFundPlanItem(capitalItem, function(data){
        closeDialog();
        // 刷新资金计划信息
        showCapitalDetail();
    }, function(data){}, true, waiting, run, true);
}

/**
 * 修改弹窗
 * 保存
 */
function btnModifyCapitalItem() {
	if(!preventRapidClick()){
		return;
	}
    var projectId = getUrlParam("project.id");
    var id = $("#txtCapitalItemId").html();
    var require = $("#txtRequireModify").val();
    var money = $("#txtMoneyModify").val();
    var purchaseDate = $("#txtPurchaseDateModify").val();
    var useDate = $("#txtUseDateModify").val();
    var costType = $("#txtCostTypeModify").val();

    if (isEmpty(projectId)){
        alert("无效的项目");
        return;
    }
    if (!verifyStringNotEmpty(require,"需求说明",32,true)){
        return;
    }
    if (!verifyMoney(money,"资金量",true)){
        return;
    }
    if (!verifyDate(purchaseDate,"预计采购时间",true)){
        return;
    }
    if (!verifyDate(useDate,"使用时间",true)){
        return;
    }
    if (!verifyNotEmpty(costType,"关联成本项",true)){
        return;
    }
    var capitalItem = {
        projectId : projectId,
        id : id,
        require : require,
        money : money,
        purchaseDate : purchaseDate,
        useDate : useDate,
        costType : costType
    };
    ajaxModifyFundPlanItemById(capitalItem, function(data){
        closeDialog();
        // 刷新资金计划信息
        showCapitalDetail();
    }, function(data){}, true, waiting, run, true);
}

/**
 * 删除按钮
 * 删除选中的资金计划项
 */
function btnDeleteCapitalItem() {
    var capitalItemId = getSelectedTableTrValue("#tableCapital");
    if (!isEmpty(capitalItemId)){
        if(confirm("确定删除该资金计划项？")){
            ajaxRemoveFundPlanItemById(getUrlParam("project.id"), capitalItemId, function(data){
                // 刷新资金计划信息
                showCapitalDetail();
            }, function(data){}, true, waiting, run, true);
        }
    }
}

