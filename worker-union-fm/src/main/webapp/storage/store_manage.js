var PAGE_SIZE = 12;
var PAGE_SIZE_ORDER = 9;
/**
 * 页面初始调用
 **/
$(function () {
    $("#txtUserName").html($.cookie(COOKIE_STAFF_NAME_KEY));
    //初始化时间
    initDate();
    //初始化入库人、转库人、领用人
    initStaffName();
});


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
    ajaxPageMaterialHeadquartersByFuzzy($("#txtMaterialNum").val(), $("#txtMaterialName").val(),
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
    ajaxPageMaterialHeadquartersByFuzzy($("#txtMaterialNum").val(), $("#txtMaterialName").val(),
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
    ajaxPageMaterialHeadquartersByFuzzy($("#txtMaterialNum").val(), $("#txtMaterialName").val(),
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
    gotoLocalHtml('/storage/material_info.html?material.num=' + $(obj).html());
}

/**
 * 查询按钮
 * 仓库材料出入库批次列表
 **/
function btnSearchMaterialOrder() {
    ajaxPageMaterialHeadquartersOrderByFuzzy($("#txtStaffName").val(),
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
                MATERIAL_ORDER_TYPE[data.page.list[i].materialHeadquartersOrderType],  data.page.list[i].materialHeadquartersOrderBatchNo,
                '<a href="#" onclick="btnToMaterialDetail(this)">' + data.page.list[i].num + '</a>',
                data.page.list[i].model, data.page.list[i].price,
                data.page.list[i].cstCreate, data.page.list[i].amount,
                data.page.list[i].materialHeadquartersOrderStaffName);
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
    ajaxPageMaterialHeadquartersOrderByFuzzy($("#txtStaffName").val(),
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
    ajaxPageMaterialHeadquartersOrderByFuzzy($("#txtStaffName").val(),
        $("#txtOrderDate").val(), $("#txtBatchNo").val(),
        new Page(PAGE_SIZE_ORDER, Math.max(parseInt($("#txtPaginationOrder").html()) + 1, 1)),
        function(data) {
            showSearchResultOrder(data);
        }, function(data) {
        }, true, waiting, run, true);
}

/**
 * 初始化弹窗入库人、转库人、领用人
 */
function initStaffName() {
    //显示当前用户名称
    /**
    ajaxGetLoginedStaffInfo(function(data) {
        $("#txtNameDialogIn").html(data.name);
        $("#txtNameDialogTransfer").html(data.name);
        $("#txtNameDialogUse").html(data.name);
    }, function(data) {
    }, true, waiting, run, true);
**/
}

//日期 补齐两位数
function padleft0(obj) {
    return obj.toString().replace(/^[0-9]{1}$/, "0" + obj);
}

/**
 * 入库按钮
 * 显示材料入库弹窗
 */
function btnToInDialog() {
    //显示当前时间
    var date = new Date();
    var year = date.getFullYear();
    var month = padleft0(date.getMonth() + 1);
    var day = padleft0(date.getDate());
    var hour = padleft0(date.getHours());
    var minute = padleft0(date.getMinutes());
    var second = padleft0(date.getSeconds());
    var txtDate = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
    $("#txtDateDialogIn").html(txtDate);
    $("#txtNameDialogIn").html($.cookie(COOKIE_STAFF_NAME_KEY));

    $("#dialogIn").show();
}

/**
 * 入库弹窗
 * 查询材料信息按钮
 */
function btnSearchDialogIn() {
    var materialNum = $("#txtMaterialNumDialogIn").val();
    if(!isEmpty(materialNum)){
        ajaxGetImportMaterialTypeByNum(materialNum, function(data) {
            $("#txtMaterialNameDialogIn").html(data.materialType.name);
            $("#txtModelDialogIn").html(data.materialType.model);
            $("#txtUnitDialogIn").html(data.materialType.unit);
        }, function(data) {
        }, true, waiting, run, true);
    }
}

/**
 * 入库弹窗
 * 材料编码输入内容改变时，清空材料名称、规格型号、计量单位显示框
 */
function MaterialChangeIn() {
    $("#txtMaterialNameDialogIn").html("");
    $("#txtModelDialogIn").html("");
    $("#txtUnitDialogIn").html("");
}

/**
 * 入库弹窗
 * 添加材料按钮
 */
function btnAddMaterialDialogIn() {
	if(!preventRapidClick()){
		return;
	}
    var materialNum = $("#txtMaterialNumDialogIn").val();
    var materialName = $("#txtMaterialNameDialogIn").html();
    var model = $("#txtModelDialogIn").html();
    var unit = $("#txtUnitDialogIn").html();
    var price = $("#txtPriceDialogIn").val();
    var amount = $("#txtAmountDialogIn").val();
    //判断是否为空，是则提示；否则添加材料
    if(isEmpty(materialNum)||isEmpty(materialName)||isEmpty(model)||isEmpty(unit)){
        alert("请确认材料编码");
        return;
    }
    if(!verifyMoney(price,"单价",true)){
        return;
    }
    if(!verifyAmount(amount,"入库数量",true)){
        return;
    }
    if(!verifyMoney(multiply(price,amount),"合计金额",true)){
        return;
    }

    //判断表格中是否有此材料编码，有则将入库数量相加，显示；否则直接添加到表格中
    var flag = 0;
    for(var i=0;i<$("#tableMaterialDialogIn").find("tr").length;i++){
        var materialNumTable = $("#tableMaterialDialogIn"+" tr").eq(i).children("td").eq(0).html();
        if(materialNum == materialNumTable){
            var amountTable = $("#tableMaterialDialogIn"+" tr").eq(i).children("td").eq(5).html();
            $("#tableMaterialDialogIn"+" tr").eq(i).children("td").eq(5).html(parseInt(amountTable) + parseInt(amount));
            break;
        }else {
            flag++;
        }
    }
    if(flag == $("#tableMaterialDialogIn").find("tr").length){
        $("#tableMaterialDialogIn").append(
            '<tr class="list_mid_row"><td>' + materialNum
            +'</td><td>' + materialName
            +'</td><td>' + model
            +'</td><td>' + unit
            +'</td><td>' + price
            +'</td><td>' + amount
            +'</td><td class="list_last_col"><button class="btn_delete" type="button" onclick="btnDeleteMaterial(this)">删除</button>'
            +'</td></tr>'
        );
    }
    $("#txtMaterialNumDialogIn").val("");
    $("#txtMaterialNameDialogIn").html("");
    $("#txtModelDialogIn").html("");
    $("#txtUnitDialogIn").html("");
    $("#txtPriceDialogIn").val("");
    $("#txtAmountDialogIn").val("");
}

/**
 * 入库、转库、领用弹窗
 * 删除按钮，删除该行材料信息
 */
function btnDeleteMaterial(obj) {
    $(obj).parent().parent().remove(".list_mid_row");
}

/**
 * 入库弹窗
 * 全部入库按钮
 */
function btnToAddAllMaterial() {
    //批次号不能为空
    var batchNo = $("#txtBatchNoDialogIn").val();
    if(!verifyStringNotEmpty(batchNo,"批次号",32,true)){
        return;
    }
    //整合数据
    var data = "materialHeadquartersOrder.batchNo="+batchNo;
    //获取表格行数
    var rows = document.getElementById("tableMaterialDialogIn").rows.length;
    if(1==rows){
    	alert("请添加材料后操作！");
    	return;
    }
    for(var i=0;i<rows-1;i++){
        var num = $("#tableMaterialDialogIn"+" tr").eq(i+1).children("td").eq(0).html();
        var price = $("#tableMaterialDialogIn"+" tr").eq(i+1).children("td").eq(4).html();
        var amount = $("#tableMaterialDialogIn"+" tr").eq(i+1).children("td").eq(5).html();
        data = data + "&materialHeadquartersOrderDetails[" + i + "].num=" + num
                    + "&materialHeadquartersOrderDetails[" + i + "].price=" + price
                    + "&materialHeadquartersOrderDetails[" + i + "].amount=" + amount;
    }
    //发送AJAX请求
    ajaxImportMaterialHeadquarters(data, function(data) {
        alert("入库成功");
        //关闭弹窗
        closeDialogIn();
    }, function(data) {
    }, true, waiting, run, true);
}

/**
 * 关闭材料入库弹窗
 */
function closeDialogIn() {
    //清空弹窗编辑框
    $("#txtBatchNoDialogIn").val("");
    $("#txtDateDialogIn").html("");
    removeTableTr("#tableMaterialDialogIn");
    $("#txtMaterialNumDialogIn").val("");
    $("#txtMaterialNameDialogIn").html("");
    $("#txtModelDialogIn").html("");
    $("#txtUnitDialogIn").html("");
    $("#txtPriceDialogIn").val("");
    $("#txtAmountDialogIn").val("");

    $("#dialogIn").hide();
}

/**
 * 出库按钮
 * 显示材料出库弹窗
 */
function btnToOutDialog() {
    //显示当前时间
    var date = new Date();
    var year = date.getFullYear();
    var month = padleft0(date.getMonth() + 1);
    var day = padleft0(date.getDate());
    var hour = padleft0(date.getHours());
    var minute = padleft0(date.getMinutes());
    var second = padleft0(date.getSeconds());
    var txtDate = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
    $("#txtDateDialogOut").html(txtDate);
    $("#txtNameDialogOut").html($.cookie(COOKIE_STAFF_NAME_KEY));
    $("#dialogOut").show();
    //初始化入驻项目下拉框
    showSettledProject();
}

/**
 * 初始化入驻项目下拉框
 */
function showSettledProject() {
    ajaxListUnarchivedProject(function(data) {
        $("#txtSettledProjectOut").empty();
        $("#txtSettledProjectOut").append("<option value=''>" + "请选择入驻项目" + "</option>");
        for(var i = 0; i < data.projects.length; i++){
            $("#txtSettledProjectOut").append(
                "<option value='" + data.projects[i].id + "'>" +"("+ data.projects[i].num+")" + data.projects[i].name
                + "</option>");
        }
    }, function(data) {
    }, true, waiting, run, true);
}

/**
 * 出库弹窗
 * 查询材料信息按钮
 */
function btnSearchDialogOut() {
    var materialNum = $("#txtMaterialNumDialogOut").val();
    if(!isEmpty(materialNum)){
        ajaxGetExportMaterialHeadquartersByNum(materialNum, function(data) {
            $("#txtMaterialNameDialogOut").html(data.materialHeadquarters.name);
            $("#txtModelDialogOut").html(data.materialHeadquarters.model);
            $("#txtUnitDialogOut").html(data.materialHeadquarters.unit);
            $("#txtAmountDialogOut").attr('placeholder','当前剩余量为'+data.materialHeadquarters.amount);
        }, function(data) {
        }, true, waiting, run, true);
    }
}

/**
 * 出库弹窗
 * 材料编码输入内容改变时，清空材料名称、规格型号、计量单位显示框
 */
function MaterialChangeOut() {
    $("#txtMaterialNameDialogOut").html("");
    $("#txtModelDialogOut").html("");
    $("#txtUnitDialogOut").html("");
    $("#txtAmountDialogOut").attr('placeholder','');
}

/**
 * 出库弹窗
 * 添加材料按钮
 */
function btnAddMaterialDialogOut() {
	if(!preventRapidClick()){
		return;
	}
    var materialNum = $("#txtMaterialNumDialogOut").val();
    var materialName = $("#txtMaterialNameDialogOut").html();
    var model = $("#txtModelDialogOut").html();
    var unit = $("#txtUnitDialogOut").html();
    var price = $("#txtPriceDialogOut").val();
    var amount = $("#txtAmountDialogOut").val();
    //判断是否为空，是则提示；否则添加材料
    if(isEmpty(materialNum)||isEmpty(materialName)||isEmpty(model)||isEmpty(unit)){
        alert("请确认材料编码");
        return;
    }
    if(!verifyMoney(price,"单价",true)){
        return;
    }
    if(!verifyAmount(amount,"出库数量",true)){
        return;
    }
    if(!verifyMoney(multiply(price,amount),"合计金额",true)){
        return;
    }
    //判断表格中是否有此材料编码，有则将转库数量相加，显示；否则直接添加到表格中
    var flag = 0;
    for(var i=0;i<$("#tableMaterialDialogOut").find("tr").length;i++){
        var materialNumTable = $("#tableMaterialDialogOut"+" tr").eq(i).children("td").eq(0).html();
        if(materialNum == materialNumTable){
            var amountTable = $("#tableMaterialDialogOut"+" tr").eq(i).children("td").eq(5).html();
            $("#tableMaterialDialogOut"+" tr").eq(i).children("td").eq(5).html(parseInt(amountTable) + parseInt(amount));
            break;
        }else {
            flag++;
        }
    }
    if(flag == $("#tableMaterialDialogOut").find("tr").length){
        $("#tableMaterialDialogOut").append(
            '<tr class="list_mid_row"><td>' + materialNum
            +'</td><td>' + materialName
            +'</td><td>' + model
            +'</td><td>' + unit
            +'</td><td>' + price
            +'</td><td>' + amount
            +'</td><td class="list_last_col"><button class="btn_delete" type="button" onclick="btnDeleteMaterial(this)">删除</button>'
            +'</td></tr>'
        );
    }
    $("#txtMaterialNumDialogOut").val("");
    $("#txtMaterialNameDialogOut").html("");
    $("#txtModelDialogOut").html("");
    $("#txtUnitDialogOut").html("");
    $("#txtPriceDialogOut").val("");
    $("#txtAmountDialogOut").val("");
    $("#txtAmountDialogOut").attr('placeholder','');
}

/**
 * 出库弹窗
 * 全部出库按钮
 */
function btnToOutAllMaterial() {
    var targetProjectId = $("#txtSettledProjectOut").val();
    var batchNo = $("#txtBatchNoDialogOut").val();
    if(!verifyNotEmpty(targetProjectId,"入驻项目",true)){
        return;
    }
    if(!verifyStringNotEmpty(batchNo,"批次号",32,true)){
        return;
    }
    //整合数据
    var data = "materialHeadquartersOrder.targetProjectId="+targetProjectId+"&materialHeadquartersOrder.batchNo="+batchNo;
    //获取表格行数
    var rows = document.getElementById("tableMaterialDialogOut").rows.length;
    if(1==rows){
    	alert("请添加材料后操作！");
    	return;
    }
    for(var i=0;i<rows-1;i++){
        var num = $("#tableMaterialDialogOut"+" tr").eq(i+1).children("td").eq(0).html();
        var price = $("#tableMaterialDialogOut"+" tr").eq(i+1).children("td").eq(4).html();
        var amount = $("#tableMaterialDialogOut"+" tr").eq(i+1).children("td").eq(5).html();
        data = data + "&materialHeadquartersOrderDetails[" + i + "].num=" + num
            + "&materialHeadquartersOrderDetails[" + i + "].price=" + price
            + "&materialHeadquartersOrderDetails[" + i + "].amount=" + amount;
    }
    //发送AJAX请求
    ajaxExportMaterialHeadquarters(data, function(data) {
        alert("出库成功");
        //关闭弹窗
        closeDialogOut();
    }, function(data) {
    }, true, waiting, run, true);
}

/**
 * 关闭材料出库弹窗
 */
function closeDialogOut() {
    //清空弹窗编辑框
    $("#txtSettledProjectOut").val("");
    $("#txtBatchNoDialogOut").val("");
    $("#txtDateDialogOut").html("");
    removeTableTr("#tableMaterialDialogOut");
    $("#txtMaterialNumDialogOut").val("");
    $("#txtMaterialNameDialogOut").html("");
    $("#txtModelDialogOut").html("");
    $("#txtUnitDialogOut").html("");
    $("#txtPriceDialogOut").val("");
    $("#txtAmountDialogOut").val("");
    $("#txtAmountDialogOut").attr('placeholder','');

    $("#dialogOut").hide();
}









