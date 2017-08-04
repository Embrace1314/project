/**
 * AJAX-分页获取仓库材料列表
 *
 * @param projectId
 * @param materialNum
 * @param materialName
 * @param page
 * @param waiting
 *            缓冲
 * @param success
 *            处理成功获取业务数据
 * @param run
 *            正常运行
 * @param fail
 * @param isExplicit
 *            是否显式处理
 * @param isLoadingExplicit
 *            缓冲是否显示处理
 */
function ajaxPageMaterialByFuzzy(projectId, materialNum, materialName, page, success, fail, isExplicit, waiting, run,
                         isLoadingExplicit) {
    // 显示缓冲
    if (isLoadingExplicit) {
        waiting();
    }
    $.ajax({
        url : URL_PAGE_MATERIAL_BY_FUZZY,
        type : TYPE_POST,
        data : {
            "project.id" : projectId,
            "material.num" : materialNum,
            "material.name" : materialName,
            "page.pagination" : page.pagination,
            "page.size" : page.size
        },
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
 * AJAX-分页获取仓库材料出入库批次列表
 *
 * @param projectId
 * @param staffName
 * @param orderDate
 * @param batchNo
 * @param page
 * @param waiting
 *            缓冲
 * @param success
 *            处理成功获取业务数据
 * @param run
 *            正常运行
 * @param fail
 * @param isExplicit
 *            是否显式处理
 * @param isLoadingExplicit
 *            缓冲是否显示处理
 */
function ajaxPageMaterialOrderByFuzzy(projectId, staffName, orderDate, batchNo, page, success, fail, isExplicit, waiting, run,
                                 isLoadingExplicit) {
    // 显示缓冲
    if (isLoadingExplicit) {
        waiting();
    }
    $.ajax({
        url : URL_PAGE_MATERIAL_ORDER_BY_FUZZY,
        type : TYPE_POST,
        data : {
            "project.id" : projectId,
            "materialOrder.staffName" : staffName,
            "materialOrder.cstCreate" : orderDate,
            "materialOrder.batchNo" : batchNo,
            "page.pagination" : page.pagination,
            "page.size" : page.size
        },
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
 * AJAX-材料入库
 *
 * @param materialInfo
 * @param waiting
 *            缓冲
 * @param success
 *            处理成功获取业务数据
 * @param run
 *            正常运行
 * @param fail
 * @param isExplicit
 *            是否显式处理
 * @param isLoadingExplicit
 *            缓冲是否显示处理
 */
function ajaxImportMaterial(materialInfo, success, fail, isExplicit, waiting, run,
                                      isLoadingExplicit) {
    // 显示缓冲
    if (isLoadingExplicit) {
        waiting();
    }
    $.ajax({
        url : URL_IMPORT_MATERIAL,
        type : TYPE_POST,
        data : materialInfo,
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
 * AJAX-材料领用
 *
 * @param materialInfo
 * @param waiting
 *            缓冲
 * @param success
 *            处理成功获取业务数据
 * @param run
 *            正常运行
 * @param fail
 * @param isExplicit
 *            是否显式处理
 * @param isLoadingExplicit
 *            缓冲是否显示处理
 */
function ajaxExportMaterial(materialInfo, success, fail, isExplicit, waiting, run,
                            isLoadingExplicit) {
    // 显示缓冲
    if (isLoadingExplicit) {
        waiting();
    }
    $.ajax({
        url : URL_EXPORT_MATERIAL,
        type : TYPE_POST,
        data : materialInfo,
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
 * AJAX-转库申请
 *
 * @param materialInfo
 * @param waiting
 *            缓冲
 * @param success
 *            处理成功获取业务数据
 * @param run
 *            正常运行
 * @param fail
 * @param isExplicit
 *            是否显式处理
 * @param isLoadingExplicit
 *            缓冲是否显示处理
 */
function ajaxApplyTransferMaterial(materialInfo, success, fail, isExplicit, waiting, run,
                            isLoadingExplicit) {
    // 显示缓冲
    if (isLoadingExplicit) {
        waiting();
    }
    $.ajax({
        url : URL_APPLY_TRANSFER_MATERIAL,
        type : TYPE_POST,
        data : materialInfo,
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
 * AJAX-获取企业材料编号对应的材料信息-出库
 *
 * @param projectId
 * @param materialNum
 * @param waiting
 *            缓冲
 * @param success
 *            处理成功获取业务数据
 * @param run
 *            正常运行
 * @param fail
 * @param isExplicit
 *            是否显式处理
 * @param isLoadingExplicit
 *            缓冲是否显示处理
 */
function ajaxGetExportMaterialByNum(projectId, materialNum, success, fail, isExplicit, waiting, run,
                                      isLoadingExplicit) {
    // 显示缓冲
    if (isLoadingExplicit) {
        waiting();
    }
    $.ajax({
        url : URL_GET_EXPORT_MATERIAL_BY_NUM,
        type : TYPE_POST,
        data : {
            "project.id" : projectId,
            "material.num" : materialNum
        },
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
 * AJAX-分页获取指定材料出入库历史记录
 *
 * @param projectId
 * @param materialNum
 * @param page
 * @param waiting
 *            缓冲
 * @param success
 *            处理成功获取业务数据
 * @param run
 *            正常运行
 * @param fail
 * @param isExplicit
 *            是否显式处理
 * @param isLoadingExplicit
 *            缓冲是否显示处理
 */
function ajaxPageMaterialOrderByMaterialNum(projectId, materialNum, page, success, fail, isExplicit, waiting, run,
                                      isLoadingExplicit) {
    // 显示缓冲
    if (isLoadingExplicit) {
        waiting();
    }
    $.ajax({
        url : URL_PAGE_MATERIAL_ORDER_BY_MATERIAL_NUM,
        type : TYPE_POST,
        data : {
            "project.id" : projectId,
            "material.num" : materialNum,
            "page.pagination" : page.pagination,
            "page.size" : page.size
        },
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
