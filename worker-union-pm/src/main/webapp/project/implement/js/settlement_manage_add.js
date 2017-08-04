var JANUARY = 1;
var DECEMBER = 12;

/**
 * 页面启动调用
 */
$(function() {
	$("#txtUserName").html($.cookie(COOKIE_STAFF_NAME_KEY));
    // 显示项目名称
    showProject();
    //初始化年份月份下拉框
    initTimeSelect();
    //初始化资源项目下拉框
    initCapitalItemSelect();
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
 * 初始化年份月份下拉框
 */
function initTimeSelect() {
    //设置年份的选择
    var myDate = new Date();
    var startYear = myDate.getFullYear() - 20;//起始年份
    var endYear = myDate.getFullYear();//结束年份
    var year = document.getElementById('selectYear');
    for (var i = endYear; i >= startYear; i--) {
        year.options.add(new Option(i, i));
    }
    year.options[0].selected = 1;

    //设置月份的选择
    var month = document.getElementById('selectMonth');
    for (var j = JANUARY; j <= DECEMBER; j++) {
        month.options.add(new Option(j, j));
    }
    month.options[0].selected = 1;
}

/**
 * 初始化资源项目下拉框
 */
function initCapitalItemSelect() {
    ajaxListPurchaseItem(function (data) {
        for(var i = 0; i < data.purchaseItems.length; i++){
            $("#txtResource").append(
                "<option value='" + data.purchaseItems[i].id + "'>" + data.purchaseItems[i].name
                + "</option>");
        }
        //初始化资源项目细分下拉框
        initResourceDetail();
    }, function (data) {
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
        //初始化分包商下拉框
        initSubcontractorSelect();
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
 * 初始化分包商下拉框
 */
function initSubcontractorSelect() {
    ajaxListSimpleSubcontractorItem(function (data) {
        for(var i = 0; i < data.subcontractors.length; i++){
            $("#txtSubcontractor").append(
                "<option value='" + data.subcontractors[i].id + "'>" + data.subcontractors[i].name
                + "</option>");
        }
    }, function (data) {
    }, true, waiting, run, true);
}

/**
 * 添加按钮
 * 增加结算管理项
 */
function btnAddSettlementItem() {
	if(!preventRapidClick()){
		return;
	}
    var projectId = getUrlParam("project.id");
    var year = $("#selectYear").val();
    var month = $("#selectMonth").val();
    var resource = $("#txtResource").val();
    var name = $("#txtResourceDetail").val();
    var subcontractor = $("#txtSubcontractor").val();
    var settlement = $("#txtSettlement").val();

    if (isEmpty(projectId)){
        alert("无效的项目");
        return;
    }
    if (!verifyNotEmpty(resource,"资源项目",true)){
        return;
    }
    if (!verifyNotEmpty(name,"资源项目细分",true)){
        return;
    }
    if (!verifyNotEmpty(subcontractor,"分包商",true)){
        return;
    }
    if (!verifyMoney(settlement,"本月结算值",true)){
        return;
    }
    var settlementItem = {
        projectId : projectId,
        year : year,
        month : month,
        resource : resource,
        name : name,
        subcontractor : subcontractor,
        settlement : settlement
    };
    ajaxAddSettlementItem(settlementItem, function(data){
        alert("添加成功");
        // 跳转到结算管理页面
        gotoLocalHtml('/project/implement/settlement_manage.html?project.id='
            + getUrlParam('project.id'));
    }, function(data){}, true, waiting, run, true);
}

