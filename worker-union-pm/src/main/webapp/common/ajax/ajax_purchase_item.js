/**
 * 获取采购条目列表
 *
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxListPurchaseItem(success, fail, isExplicit,
                              waiting, run, isLoadingExplicit) {
    // 显示缓冲
    if (isLoadingExplicit) {
        waiting();
    }
    $.ajax({
        url : URL_LIST_PURCHASE_ITEM,
        data : {},
        type : TYPE_POST,
        dataType : DATATYPE_JSON,
        async : true,
        // 将XHR对象的withCredentials设为true
        xhrFields:{
            withCredentials:true
        },
        success : function(data) {
            // 关闭缓冲
            if (isLoadingExplicit) {
                run();
            }

            // 业务返回数据处理
            if (RETURNCODE_SUCCESS == data.returnCode) {
                success(data);
            } else if (RETURNCODE_FAIL == data.returnCode) {
                failDefault(data, isExplicit);
                fail(data);
            }
        },
        error : function() {
            // 关闭缓冲
            if (isLoadingExplicit) {
                run();
            }
            // 网络故障
            networkFault(isExplicit);
        }
    });
}

