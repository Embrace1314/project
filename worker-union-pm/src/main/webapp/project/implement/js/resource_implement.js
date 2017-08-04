
/**
 * 页面启动调用
 */
$(function() {
	$("#txtUserName").html($.cookie(COOKIE_STAFF_NAME_KEY));
    // 显示项目名称
    showProject();
});

/**
 * 显示项目名称
 */
function showProject() {
    ajaxGetProjectDetailById(getUrlParam("project.id"), function(data) {
    	showProjectTitle(data.project);
        // 显示资源落实项列表
        showResourceList();
    }, function(data) {
    }, true, waiting, run, true);
}

/**
 * 显示资源落实项列表
 */
function showResourceList() {
    ajaxListResourceImplementItem(getUrlParam("project.id"), function(data) {
        removeTableTr("#tableResource");
        for (var i = 0; i < data.resourceImplementItems.length; i++) {
            var moneyPlan = data.resourceImplementItems[i].hasFundPlanItem?data.resourceImplementItems[i].moneyPlan
                +"("+data.resourceImplementItems[i].moneyRetain+")":"";
            $("#tableResource").append(
                '<tr class="list_mid_row" onclick="btnSelectTableTr(this)"><td hidden="true">'+ data.resourceImplementItems[i].id
                +'</td><td>'+ data.resourceImplementItems[i].purchaseItemName
                +'</td><td>' + data.resourceImplementItems[i].name
                +'</td><td>' + data.resourceImplementItems[i].unit
                +'</td><td>'+ data.resourceImplementItems[i].price
                +'</td><td>'+ data.resourceImplementItems[i].amount
                +'</td><td>' + data.resourceImplementItems[i].subcontractorName
                +'</td><td>'+ moneyPlan
                +'</td><td class="list_last_col">'+ data.resourceImplementItems[i].money +'</td></tr>');

        }
    }, function(data) {
    }, true, waiting, run, true);
}

/**
 * 添加按钮
 */
function btnToAddResource() {
    gotoLocalHtml('/project/implement/resource_implement_add.html?project.id='
        + getUrlParam('project.id'));
}

/**
 * 修改按钮
 */
function btnToModifyResource() {
    var resourceItemId = getSelectedTableTrValue("#tableResource");
    if(!isEmpty(resourceItemId)){
        gotoLocalHtml('/project/implement/resource_implement_modify.html?project.id='
            + getUrlParam('project.id') + '&resourceImplementItem.id=' + resourceItemId);
    }else {
        alert("请先选择一条资源落实信息");
    }
}

/**
 * 删除按钮
 */
function btnDeleteResourceItem() {
    var resourceItemId = getSelectedTableTrValue("#tableResource");
    if (!isEmpty(resourceItemId)){
        if(confirm("确定删除该资源落实项？")){
            ajaxRemoveResourceImplementItemById(getUrlParam("project.id"), resourceItemId, function(data){
                // 刷新资源落实项列表
                showResourceList();
            }, function(data){}, true, waiting, run, true);
        }
    }
}