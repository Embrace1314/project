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
    //初始化入库人、转库人、领用人
    initStaffName();
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

/**
 * 初始化弹窗入库人、转库人、领用人
 */
function initStaffName() {
    //显示当前用户名称
    ajaxGetLoginedStaffInfo(function(data) {
        $("#txtNameDialogIn").html(data.name);
        $("#txtNameDialogTransfer").html(data.name);
        $("#txtNameDialogUse").html(data.name);
    }, function(data) {
    }, true, waiting, run, true);

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
    if (!verifyMoney(price,"单价",true)){
        return;
    }
    if (!verifyAmount(amount,"入库数量",true)){
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
	if(!preventRapidClick()){
		return;
	}
    //批次号不能为空
    var batchNo = $("#txtBatchNoDialogIn").val();
    if(isEmpty(batchNo)){
        alert("请填写批次号");
        return;
    }
    //整合数据
    var data = "project.id="+getUrlParam("project.id")+"&materialOrder.batchNo="+batchNo;
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
        data = data + "&materialOrderDetails[" + i + "].num=" + num
                    + "&materialOrderDetails[" + i + "].price=" + price
                    + "&materialOrderDetails[" + i + "].amount=" + amount;
    }
    //发送AJAX请求
    ajaxImportMaterial(data, function(data) {
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
 * 转库按钮
 * 显示材料转库弹窗
 */
function btnToTransferDialog() {
    //显示当前时间
    var date = new Date();
    var year = date.getFullYear();
    var month = padleft0(date.getMonth() + 1);
    var day = padleft0(date.getDate());
    var hour = padleft0(date.getHours());
    var minute = padleft0(date.getMinutes());
    var second = padleft0(date.getSeconds());
    var txtDate = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
    $("#txtDateDialogTransfer").html(txtDate);
    $("#dialogTransfer").show();
    //初始化材料接收方下拉框
    showMaterialReceiver();
}

/**
 * 初始化材料接收方下拉框
 */
function showMaterialReceiver() {
    $("#txtReceiverDialogTransfer").empty();
    $("#txtReceiverDialogTransfer").append(
        "<option value=''>总部</option>");
    ajaxListUnarchivedProject(function(data) {
        for(var i = 0; i < data.projects.length; i++){
        	if(data.projects[i].id !=getUrlParam("project.id")){
        		// 不显示当前项目
        		 $("#txtReceiverDialogTransfer").append(
        	                "<option value='" + data.projects[i].id + "'>" + data.projects[i].num + data.projects[i].name
        	                + "</option>");
        	}
        }
    }, function(data) {
    }, true, waiting, run, true);
}

/**
 * 转库弹窗
 * 查询材料信息按钮
 */
function btnSearchDialogTransfer() {
    var materialNum = $("#txtMaterialNumDialogTransfer").val();
    if(!isEmpty(materialNum)){
        ajaxGetExportMaterialByNum(getUrlParam("project.id"), materialNum, function(data) {
            $("#txtMaterialNameDialogTransfer").html(data.material.name);
            $("#txtModelDialogTransfer").html(data.material.model);
            $("#txtUnitDialogTransfer").html(data.material.unit);
            $("#txtAmountDialogTransfer").attr('placeholder','当前剩余量为'+data.material.amount);
        }, function(data) {
        }, true, waiting, run, true);
    }
}

/**
 * 转库弹窗
 * 材料编码输入内容改变时，清空材料名称、规格型号、计量单位显示框
 */
function MaterialChangeTransfer() {
    $("#txtMaterialNameDialogTransfer").html("");
    $("#txtModelDialogTransfer").html("");
    $("#txtUnitDialogTransfer").html("");
    $("#txtAmountDialogTransfer").attr('placeholder','');
}

/**
 * 转库弹窗
 * 添加材料按钮
 */
function btnAddMaterialDialogTransfer() {
	if(!preventRapidClick()){
		return;
	}
    var materialNum = $("#txtMaterialNumDialogTransfer").val();
    var materialName = $("#txtMaterialNameDialogTransfer").html();
    var model = $("#txtModelDialogTransfer").html();
    var unit = $("#txtUnitDialogTransfer").html();
    var price = $("#txtPriceDialogTransfer").val();
    var amount = $("#txtAmountDialogTransfer").val();
    //判断是否为空，是则提示；否则添加材料
    if(isEmpty(materialNum)||isEmpty(materialName)||isEmpty(model)||isEmpty(unit)){
        alert("请确认材料编码");
        return;
    }
    if (!verifyMoney(price,"单价",true)){
        return;
    }
    if (!verifyAmount(amount,"转库数量",true)){
        return;
    }
    if(!verifyMoney(multiply(price,amount),"合计金额",true)){
        return;
    }

    //判断表格中是否有此材料编码，有则将转库数量相加，显示；否则直接添加到表格中
    var flag = 0;
    for(var i=0;i<$("#tableMaterialDialogTransfer").find("tr").length;i++){
        var materialNumTable = $("#tableMaterialDialogTransfer"+" tr").eq(i).children("td").eq(0).html();
        if(materialNum == materialNumTable){
            var amountTable = $("#tableMaterialDialogTransfer"+" tr").eq(i).children("td").eq(5).html();
            $("#tableMaterialDialogTransfer"+" tr").eq(i).children("td").eq(5).html(parseInt(amountTable) + parseInt(amount));
            break;
        }else {
            flag++;
        }
    }
    if(flag == $("#tableMaterialDialogTransfer").find("tr").length){
        $("#tableMaterialDialogTransfer").append(
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
    $("#txtMaterialNumDialogTransfer").val("");
    $("#txtMaterialNameDialogTransfer").html("");
    $("#txtModelDialogTransfer").html("");
    $("#txtUnitDialogTransfer").html("");
    $("#txtPriceDialogTransfer").val("");
    $("#txtAmountDialogTransfer").val("");
    $("#txtAmountDialogTransfer").attr('placeholder','');
}

/**
 * 转库弹窗
 * 全部转库按钮
 */
function btnToTransferAllMaterial() {
	if(!preventRapidClick()){
		return;
	}
    var receiver = $("#txtReceiverDialogTransfer").val();
    var type = "";
    if(receiver==""){
        type = "TO_HEADQUARTERS";
    }
    if(!isEmpty(receiver)){
        type = "TO_PROJECT";
    }
    //整合数据
    var data = "project.id="+getUrlParam("project.id")+"&materialTransferApplication.type="+type
        + "&materialTransferApplication.importProjectId=" + receiver;
    //获取表格行数
    var rows = document.getElementById("tableMaterialDialogTransfer").rows.length;
    if(1==rows){
    	alert("请添加材料后操作！");
    	return;
    }
    for(var i=0;i<rows-1;i++){
        var num = $("#tableMaterialDialogTransfer"+" tr").eq(i+1).children("td").eq(0).html();
        var price = $("#tableMaterialDialogTransfer"+" tr").eq(i+1).children("td").eq(4).html();
        var amount = $("#tableMaterialDialogTransfer"+" tr").eq(i+1).children("td").eq(5).html();
        data = data + "&materialTransferApplicationDetails[" + i + "].num=" + num
            + "&materialTransferApplicationDetails[" + i + "].amount=" + amount
            + "&materialTransferApplicationDetails[" + i + "].price=" + price;
    }
    //发送AJAX请求
    ajaxApplyTransferMaterial(data, function(data) {
        alert("转库成功");
        //关闭弹窗
        closeDialogTransfer();
    }, function(data) {
    }, true, waiting, run, true);
}

/**
 * 关闭材料转库弹窗
 */
function closeDialogTransfer() {
    //清空弹窗编辑框
    $("#txtBatchNoDialogTransfer").val("");
    $("#txtDateDialogTransfer").html("");
    removeTableTr("#tableMaterialDialogTransfer");
    $("#txtMaterialNumDialogTransfer").val("");
    $("#txtMaterialNameDialogTransfer").html("");
    $("#txtModelDialogTransfer").html("");
    $("#txtUnitDialogTransfer").html("");
    $("#txtPriceDialogTransfer").val("");
    $("#txtAmountDialogTransfer").val("");
    $("#txtAmountDialogTransfer").attr('placeholder','');

    $("#dialogTransfer").hide();
}

/**
 * 出库按钮
 * 显示材料领用弹窗
 */
function btnToUseDialog() {
    //显示当前时间
    var date = new Date();
    var year = date.getFullYear();
    var month = padleft0(date.getMonth() + 1);
    var day = padleft0(date.getDate());
    var hour = padleft0(date.getHours());
    var minute = padleft0(date.getMinutes());
    var second = padleft0(date.getSeconds());
    var txtDate = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
    $("#txtDateDialogUse").html(txtDate);

    $("#dialogUse").show();
}

/**
 * 领用弹窗
 * 查询材料信息按钮
 */
function btnSearchDialogUse() {
    var materialNum = $("#txtMaterialNumDialogUse").val();
    if(!isEmpty(materialNum)){
        ajaxGetExportMaterialByNum(getUrlParam("project.id"), materialNum, function(data) {
            $("#txtMaterialNameDialogUse").html(data.material.name);
            $("#txtModelDialogUse").html(data.material.model);
            $("#txtAmountDialogUse").attr('placeholder','当前剩余量为'+data.material.amount);
        }, function(data) {
        }, true, waiting, run, true);
    }
}

/**
 * 领用弹窗
 * 材料编码输入内容改变时，清空材料名称、规格型号显示框
 */
function MaterialChangeUse() {
    $("#txtMaterialNameDialogUse").html("");
    $("#txtModelDialogUse").html("");
    $("#txtAmountDialogUse").attr('placeholder','');
}

/**
 * 领用弹窗
 * 添加材料按钮
 */
function btnAddMaterialDialogUse() {
	if(!preventRapidClick()){
		return;
	}
    var materialNum = $("#txtMaterialNumDialogUse").val();
    var materialName = $("#txtMaterialNameDialogUse").html();
    var model = $("#txtModelDialogUse").html();
    var amount = $("#txtAmountDialogUse").val();
    //判断是否为空，是则提示；否则添加材料
    if(isEmpty(materialNum)||isEmpty(materialName)||isEmpty(model)){
        alert("请确认材料编码");
        return;
    }
    if (!verifyAmount(amount,"领用数量",true)){
        return;
    }

    //判断表格中是否有此材料编码，有则将领用数量相加，显示；否则直接添加到表格中
    var flag = 0;
    for(var i=0;i<$("#tableMaterialDialogUse").find("tr").length;i++){
        var materialNumTable = $("#tableMaterialDialogUse"+" tr").eq(i).children("td").eq(0).html();
        if(materialNum == materialNumTable){
            var amountTable = $("#tableMaterialDialogUse"+" tr").eq(i).children("td").eq(3).html();
            $("#tableMaterialDialogUse"+" tr").eq(i).children("td").eq(3).html(parseInt(amountTable) + parseInt(amount));
            break;
        }else {
            flag++;
        }
    }
    if(flag == $("#tableMaterialDialogUse").find("tr").length){
        $("#tableMaterialDialogUse").append(
            '<tr class="list_mid_row"><td>' + materialNum
            +'</td><td>' + materialName
            +'</td><td>' + model
            +'</td><td>' + amount
            +'</td><td class="list_last_col"><button class="btn_delete" type="button" onclick="btnDeleteMaterial(this)">删除</button>'
            +'</td></tr>'
        );
    }
    $("#txtMaterialNumDialogUse").val("");
    $("#txtMaterialNameDialogUse").html("");
    $("#txtModelDialogUse").html("");
    $("#txtAmountDialogUse").val("");
    $("#txtAmountDialogUse").attr('placeholder','');
}

/**
 * 领用弹窗
 * 全部领用按钮
 */
function btnToUseAllMaterial() {
	if(!preventRapidClick()){
		return;
	}
    //整合数据
    var data = "project.id="+getUrlParam("project.id");
    //获取表格行数
    var rows = document.getElementById("tableMaterialDialogUse").rows.length;
    if(1==rows){
    	alert("请添加材料后操作！");
    	return;
    }
    for(var i=0;i<rows-1;i++){
        var num = $("#tableMaterialDialogUse"+" tr").eq(i+1).children("td").eq(0).html();
        var amount = $("#tableMaterialDialogUse"+" tr").eq(i+1).children("td").eq(3).html();
        data = data + "&materialOrderDetails[" + i + "].num=" + num
                    + "&materialOrderDetails[" + i + "].amount=" + amount;
    }
    //发送AJAX请求
    ajaxExportMaterial(data, function(data) {
        alert("领用成功");
        //关闭弹窗
        closeDialogUse();
    }, function(data) {
    }, true, waiting, run, true);
}

/**
 * 关闭材料领用弹窗
 */
function closeDialogUse() {
    //清空弹窗编辑框
    $("#txtDateDialogTransfer").html("");
    removeTableTr("#tableMaterialDialogUse");
    $("#txtMaterialNumDialogUse").val("");
    $("#txtMaterialNameDialogUse").html("");
    $("#txtModelDialogUse").html("");
    $("#txtAmountDialogUse").val("");
    $("#txtAmountDialogUse").attr('placeholder','');

    $("#dialogUse").hide();
}







