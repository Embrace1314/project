
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
        //显示竣工结算信息
        showCompleteInfo();
    }, function(data) {
    }, true, waiting, run, true);
}

/**
 * 显示竣工结算信息
 */
function showCompleteInfo() {
    ajaxListFinalSettlementItem(getUrlParam("project.id"), function(data) {
        removeTableTr("#tableComplete");
        for (var i = 0; i < data.finalSettlementItems.length; i++) {
            var attachment = data.finalSettlementItems[i].hasAttachment?"附件下载":"";
            var j = i+1;
            $("#tableComplete").append(
                '<tr class="list_mid_row"><td hidden="true">'+ data.finalSettlementItems[i].id
                +'</td><td>'+ j
                +'</td><td>' + data.finalSettlementItems[i].subprojectName
                +'</td><td>' + data.finalSettlementItems[i].submitMoney
                +'</td><td>' + data.finalSettlementItems[i].checkedMoney
                +'</td><td>' + data.finalSettlementItems[i].decreasedMoney
                +'</td><td>' + data.finalSettlementItems[i].increasedMoney
                +'</td><td class="list_last_col"><a href="#" onclick="btnDownloadFile(this)">'+ attachment
                +'</a></td></tr>');
        }
        $("#tableComplete").append(
            '<tr class="list_last_row"><td>'+ "合计"
            +'</td><td>'+ ""
            +'</td><td>' + data.sumSubmitMoney
            +'</td><td>' + data.sumCheckedMoney
            +'</td><td class="list_col_color_yellow">' + data.sumDecreasedMoney
            +'</td><td  class="list_col_color_yellow">' + data.sumIncreasedMoney
            +'</td><td class="list_last_col"></td></tr>');
    }, function(data) {
    }, true, waiting, run, true);
}

/**
 *下载
 */
function btnDownloadFile(obj) {
    // 获取授权信息
    var attachmentId = $(obj).parent().parent().children("td").first().html();
    ajaxAcquireFinalSettlementAttachmentDownToken(getUrlParam("project.id"), attachmentId,
        function(data) {
			window.location = data.url;
        }, function() {
        }, true, waiting, run, true);
}