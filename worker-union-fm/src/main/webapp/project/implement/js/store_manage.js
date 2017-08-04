var PAGE_SIZE = 12;
var PAGE_SIZE_ORDER = 9;

/**
 * 页面初始调用
 **/
$(function () {
    $("#txtUserName").html($.cookie(COOKIE_STAFF_NAME_KEY));
    //显示项目名称
    showProject();
    //初始化时间
    initDate();
});

/**
 * 显示项目名称
 **/
function showProject(){
    ajaxGetProjectDetailById(getUrlParam("project.id"), function (data) {
        showProjectTitle(data.project);
    }, function (data) {
    }, true, waiting, run, true);
}

/**
 * 初始化时间
 **/
function initDate() {
    initdatepicker_cn();
    $("#txtOrderDate").datepicker({
        dateFormat : "yy-mm-dd"
    });
}


/**
 * 查询按钮
 * 仓库材料列表
 **/
function btnSearchMaterial() {
    ajaxPageMaterialByFuzzy(getUrlParam("project.id"), $("#txtMaterialNum").val(), $("#txtMaterialName").val(),
        new Page(PAGE_SIZE, 1), function(data) {
            showSearchResult(data);
        }, function(data) {
        }, true, waiting, run, true);
}

/**
 * 显示搜索结果
 * 仓库材料列表
 *
 * @param data
 */
function showSearchResult(data) {
    clearTableTr("#tableMaterial");
    for (var i = 0; i < PAGE_SIZE; i++) {
        if (i < data.page.list.length) {
            setTableRowContent("#tableMaterial", i+1, data.page.list[i].id,
                '<a href="#" onclick="btnToMaterialDetail(this)">' + data.page.list[i].num + '</a>',
                data.page.list[i].name, data.page.list[i].model,
                data.page.list[i].unit, data.page.list[i].amount);
        } else {
            setTableRowContent("#tableMaterial", i+1, "", "", "", "", "", "");
        }
    }
    // 设置 页码
    $("#txtPagination").html(data.page.pagination);
    $("#txtTotalPage").html(data.page.totalPage);
}

/**
 * 上一页
 * 查询仓库材料列表
 */
function btnPrevPage() {
    ajaxPageMaterialByFuzzy(getUrlParam("project.id"), $("#txtMaterialNum").val(), $("#txtMaterialName").val(),
        new Page(PAGE_SIZE, Math.max(parseInt($("#txtPagination").html()) - 1, 1)),
        function(data) {
            showSearchResult(data);
        }, function(data) {
        }, true, waiting, run, true);
}

/**
 * 下一页
 * 查询仓库材料列表
 */
function btnNextPage() {
    ajaxPageMaterialByFuzzy(getUrlParam("project.id"), $("#txtMaterialNum").val(), $("#txtMaterialName").val(),
        new Page(PAGE_SIZE, Math.max(parseInt($("#txtPagination").html()) + 1, 1)),
        function(data) {
            showSearchResult(data);
        }, function(data) {
        }, true, waiting, run, true);
}

/**
 * 点击材料编码
 * 查看材料信息详情
 */
function btnToMaterialDetail(obj) {
    gotoLocalHtml('/project/implement/material_info.html?project.id='
        + getUrlParam('project.id') + '&material.num=' + $(obj).html());
}

/**
 * 查询按钮
 * 仓库材料出入库批次列表
 **/
function btnSearchMaterialOrder() {
    ajaxPageMaterialOrderByFuzzy(getUrlParam("project.id"), $("#txtStaffName").val(),
        $("#txtOrderDate").val(), $("#txtBatchNo").val(), new Page(PAGE_SIZE_ORDER, 1),
        function(data) {
            showSearchResultOrder(data);
        }, function(data) {
        }, true, waiting, run, true);
}

/**
 * 显示搜索结果
 * 仓库材料出入库批次列表
 *
 * @param data
 */
function showSearchResultOrder(data) {
    clearTableTr("#tableMaterialOrder");
    for (var i = 0; i < PAGE_SIZE; i++) {
        if (i < data.page.list.length) {
            setTableRowContent("#tableMaterialOrder", i+1, data.page.list[i].id,
                MATERIAL_ORDER_TYPE[data.page.list[i].materialOrderType],  data.page.list[i].materialOrderBatchNo,
                '<a href="#" onclick="btnToMaterialDetail(this)">' + data.page.list[i].num + '</a>',
                data.page.list[i].model, data.page.list[i].price,
                data.page.list[i].cstCreate, data.page.list[i].amount,
                data.page.list[i].materialOrderStaffName);
        } else {
            setTableRowContent("#tableMaterialOrder", i+1, "", "", "", "", "","","", "", "");
        }
    }
    // 设置 页码
    $("#txtPaginationOrder").html(data.page.pagination);
    $("#txtTotalPageOrder").html(data.page.totalPage);
}

/**
 * 上一页
 * 查询仓库材料出入库批次列表
 */
function btnPrevPageOrder() {
    ajaxPageMaterialOrderByFuzzy(getUrlParam("project.id"), $("#txtStaffName").val(),
        $("#txtOrderDate").val(), $("#txtBatchNo").val(),
        new Page(PAGE_SIZE_ORDER, Math.max(parseInt($("#txtPaginationOrder").html()) - 1, 1)),
        function(data) {
            showSearchResultOrder(data);
        }, function(data) {
        }, true, waiting, run, true);
}

/**
 * 下一页
 * 查询仓库材料出入库批次列表
 */
function btnNextPageOrder() {
    ajaxPageMaterialOrderByFuzzy(getUrlParam("project.id"), $("#txtStaffName").val(),
        $("#txtOrderDate").val(), $("#txtBatchNo").val(),
        new Page(PAGE_SIZE_ORDER, Math.max(parseInt($("#txtPaginationOrder").html()) + 1, 1)),
        function(data) {
            showSearchResultOrder(data);
        }, function(data) {
        }, true, waiting, run, true);
}




