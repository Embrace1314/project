var PAGE_SIZE = 9;

/**
 * 页面初始调用
 **/
$(function () {
    $("#txtUserName").html($.cookie(COOKIE_STAFF_NAME_KEY));
	showMaterialDetial();
});

/**
 * 显示材料信息详情
 **/
function showMaterialDetial(){
    // 分页参数
    var pagination = $("#pagination").html();
    // 清空页码
    $("#pagination").html("");
    $("#totalPage").html("");

    ajaxPageMaterialHeadquartersOrderByMaterialHeadquartersNum(getUrlParam("material.num"), {
        "pagination": pagination,
        "size": PAGE_SIZE
    }, function (data) {
        //显示材料编号数量信息
        $("#txtMaterialNumName").html(data.materialHeadquarters.name + ":" + data.materialHeadquarters.num);
        $("#txtAmountIn").html(data.amountImport);
        $("#txtAmountUse").html(data.amountExport);
        $("#txtSurplus").html(data.amountImport-data.amountExport);

        // 设置 页码
        $("#pagination").html(data.page.pagination);
        $("#totalPage").html(data.page.totalPage);

        clearTableTr("#tableMaterialInfo");
        for (var i = 0; i < PAGE_SIZE; i++) {
            if (i < data.page.list.length) {
                setTableRowContent("#tableMaterialInfo", i+1, MATERIAL_ORDER_TYPE[data.page.list[i].materialHeadquartersOrderType],
                    data.page.list[i].materialHeadquartersOrderBatchNo, data.page.list[i].model,
                    data.page.list[i].unit, data.page.list[i].price,
                    data.page.list[i].cstCreate, data.page.list[i].amount,
                    data.page.list[i].materialHeadquartersOrderStaffName);
            } else {
                setTableRowContent("#tableMaterialInfo", i+1, "", "", "", "","", "", "", "");
            }
        }
    }, function (data) {
    }, true, waiting, run, true);
}

/**
 * 点击上一页
 */
function btnPrevPage() {
    $("#pagination").html(Math.max(parseInt($("#pagination").html()) - 1, 1));
    showMaterialDetial();
}

/**
 * 点击下一页
 */
function btnNextPage() {
    $("#pagination").html(Math.max(parseInt($("#pagination").html()) + 1, 1));
    showMaterialDetial();
}


