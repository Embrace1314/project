
/**
 * 获取资源落实项列表
 *
 * @param projectId
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxListResourceImplementItem(projectId, success, fail, isExplicit,
                              waiting, run, isLoadingExplicit) {
    // 显示缓冲
    if (isLoadingExplicit) {
        waiting();
    }
    $.ajax({
        url : URL_LIST_RESOURCE_IMPLEMENT_ITEM,
        data : {
            "project.id" : projectId
        },
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

/**
 * 删除资源落实项
 *
 * @param projectId
 * @param resourceItemId
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxRemoveResourceImplementItemById(projectId, resourceItemId, success, fail, isExplicit,
                                             waiting, run, isLoadingExplicit) {
    // 显示缓冲
    if (isLoadingExplicit) {
        waiting();
    }
    $.ajax({
        url : URL_REMOVE_RESOURCE_IMPLEMENT_ITEM_BY_ID,
        data : {
            "project.id" : projectId,
            "resourceImplementItem.id" : resourceItemId
        },
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

/**
 * 增加资源落实项
 *
 * @param resourceItem
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxAddResourceImplementItem(resourceItem, success, fail, isExplicit,
                             waiting, run, isLoadingExplicit) {
    // 显示缓冲
    if (isLoadingExplicit) {
        waiting();
    }
    $.ajax({
            url : URL_ADD_RESOURCE_IMPLEMENT_ITEM,
        data : {
            "project.id" : resourceItem.projectId,
            "resourceImplementItem.purchaseItemId" : resourceItem.resource,
            "resourceImplementItem.subcontractorId" : resourceItem.subcontractor,
            "resourceImplementItem.contractId" : resourceItem.contract,
            "resourceImplementItem.fundPlanItemId" : resourceItem.require,
            "resourceImplementItem.name" : resourceItem.name,
            "resourceImplementItem.unit" : resourceItem.unit,
            "resourceImplementItem.price" : resourceItem.price,
            "resourceImplementItem.amount" : resourceItem.amount
        },
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

/**
 * 获取资源落实项
 *
 * @param projectId
 * @param resourceItemId
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxGetResourceImplementItemById(projectId, resourceItemId, success, fail, isExplicit,
                                             waiting, run, isLoadingExplicit) {
    // 显示缓冲
    if (isLoadingExplicit) {
        waiting();
    }
    $.ajax({
        url : URL_GET_RESOURCE_IMPLEMENT_ITEM_BY_ID,
        data : {
            "project.id" : projectId,
            "resourceImplementItem.id" : resourceItemId
        },
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

/**
 * 修改资源落实项
 *
 * @param resourceItem
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxModifyResourceImplementItemById(resourceItem, success, fail, isExplicit,
                                      waiting, run, isLoadingExplicit) {
    // 显示缓冲
    if (isLoadingExplicit) {
        waiting();
    }
    $.ajax({
        url : URL_MODIFY_RESOURCE_IMPLEMENT_ITEM_BY_ID,
        data : {
            "project.id" : resourceItem.projectId,
            "resourceImplementItem.id" : resourceItem.id,
            "resourceImplementItem.fundPlanItemId" : resourceItem.require,
            "resourceImplementItem.name" : resourceItem.name,
            "resourceImplementItem.unit" : resourceItem.unit,
            "resourceImplementItem.price" : resourceItem.price,
            "resourceImplementItem.amount" : resourceItem.amount
        },
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

/**
 * 简要获取资源落实项列表
 *
 * @param projectId
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxListSimpleResourceImplementItem(projectId, success, fail, isExplicit,
                                       waiting, run, isLoadingExplicit) {
    // 显示缓冲
    if (isLoadingExplicit) {
        waiting();
    }
    $.ajax({
        url : URL_LIST_SIMPLE_RESOURCE_IMPLEMENT_ITEM,
        data : {
            "project.id" : projectId
        },
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

