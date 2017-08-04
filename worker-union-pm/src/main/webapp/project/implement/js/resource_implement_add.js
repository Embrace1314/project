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
        //初始化资源项目下拉框
        initCapitalItemSelect();
    }, function (data) {
    }, true, waiting, run, true);
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
        //初始化分包商下拉框
        initSubcontractorSelect();
    }, function (data) {
    }, true, waiting, run, true);
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
        //初始化合同库下拉框
        initContractSelect();
    }, function (data) {
    }, true, waiting, run, true);
}

/**
 * 初始化合同下拉框
 */
function initContractSelect() {
    ajaxListSimpleContractItemByProjectId(getUrlParam("project.id"), function (data) {
        for(var i = 0; i < data.contracts.length; i++){
            $("#txtContract").append(
                "<option value='" + data.contracts[i].id + "'>" + data.contracts[i].name
                + "</option>");
        }
    }, function (data) {
    }, true, waiting, run, true);
}

/**
 * 添加按钮
 * 增加资源落实项
 */
function btnAddResourceitem() {
	if(!preventRapidClick()){
		return;
	}
    var projectId = getUrlParam("project.id");
    var require = $("#txtRequire").val();
    var resource = $("#txtResource").val();
    var name = $("#txtResourceDetail").val();
    var unit = $("#txtUnit").val();
    var price = $("#txtPrice").val();
    var amount = $("#txtAmount").val();
    var subcontractor = $("#txtSubcontractor").val();
    var contract = $("#txtContract").val();

    if (isEmpty(projectId)){
        alert("无效的项目");
        return;
    }
    if (!verifyNotEmpty(resource,"资源项目",true)){
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
    if (!verifyNotEmpty(subcontractor,"分包商",true)){
        return;
    }
    if (!verifyNotEmpty(contract,"关联合同",true)){
        return;
    }
    if (!verifyMoney(multiply(price,amount),"实际资金额",true)){
        return;
    }
    var resourceItem = {
        projectId : projectId,
        require : require,
        resource : resource,
        name : name,
        unit : unit,
        price : price,
        amount : amount,
        subcontractor : subcontractor,
        contract : contract
    };
    ajaxAddResourceImplementItem(resourceItem, function(data){
        alert("添加成功");
        // 跳转到资源落实页面
        gotoLocalHtml('/project/implement/resource_implement.html?project.id='
            + getUrlParam('project.id'));
    }, function(data){}, true, waiting, run, true);
}

