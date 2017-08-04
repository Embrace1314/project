/**
 * AJAX-分页获取仓库材料列表
 * 
 * @param materialHeadquartersNum
 * @param materialHeadquartersName
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
function ajaxPageMaterialHeadquartersByFuzzy(materialHeadquartersNum,
		materialHeadquartersName, page, success, fail, isExplicit, waiting,
		run, isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_PAGE_MATERIAL_HEADQUARTERS_BY_FUZZY,
		type : TYPE_POST,
		data : {
			"materialHeadquarters.num" : materialHeadquartersNum,
			"materialHeadquarters.name" : materialHeadquartersName,
			"page.pagination" : page.pagination,
			"page.size" : page.size
		},
		dataType : DATATYPE_JSON,
		async : true,
		// 将XHR对象的withCredentials设为true
		xhrFields : {
			withCredentials : true
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
function ajaxPageMaterialHeadquartersOrderByFuzzy(staffName, orderDate,
		batchNo, page, success, fail, isExplicit, waiting, run,
		isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_PAGE_MATERIAL_HEADQUARTERS_ORDER_BY_FUZZY,
		type : TYPE_POST,
		data : {
			"materialHeadquartersOrder.staffName" : staffName,
			"materialHeadquartersOrder.cstCreate" : orderDate,
			"materialHeadquartersOrder.batchNo" : batchNo,
			"page.pagination" : page.pagination,
			"page.size" : page.size
		},
		dataType : DATATYPE_JSON,
		async : true,
		// 将XHR对象的withCredentials设为true
		xhrFields : {
			withCredentials : true
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
 * @param materialHeadquartersNum
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
function ajaxPageMaterialHeadquartersOrderByMaterialHeadquartersNum(
		materialHeadquartersNum, page, success, fail, isExplicit, waiting, run,
		isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$
			.ajax({
				url : URL_PAGE_MATERIAL_HEADQUARTERS_ORDER_BY_MATERIAL_HEADQUARTERS_NUM,
				type : TYPE_POST,
				data : {
					"materialHeadquarters.num" : materialHeadquartersNum,
					"page.pagination" : page.pagination,
					"page.size" : page.size
				},
				dataType : DATATYPE_JSON,
				async : true,
				// 将XHR对象的withCredentials设为true
				xhrFields : {
					withCredentials : true
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
 * @param materialHeadquartersInfo
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
function ajaxImportMaterialHeadquarters(materialHeadquartersInfo, success,
		fail, isExplicit, waiting, run, isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_IMPORT_MATERIAL_HEADQUARTERS,
		type : TYPE_POST,
		data : materialHeadquartersInfo,
		dataType : DATATYPE_JSON,
		async : true,
		// 将XHR对象的withCredentials设为true
		xhrFields : {
			withCredentials : true
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
 * @param materialHeadquartersInfo
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
function ajaxExportMaterialHeadquarters(materialHeadquartersInfo, success,
		fail, isExplicit, waiting, run, isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_EXPORT_MATERIAL_HEADQUARTERS,
		type : TYPE_POST,
		data : materialHeadquartersInfo,
		dataType : DATATYPE_JSON,
		async : true,
		// 将XHR对象的withCredentials设为true
		xhrFields : {
			withCredentials : true
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
 * @param materialHeadquartersNum
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
function ajaxGetExportMaterialHeadquartersByNum(materialHeadquartersNum,
		success, fail, isExplicit, waiting, run, isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_GET_EXPORT_MATERIAL_HEADQUARTERS_BY_NUM,
		type : TYPE_POST,
		data : {
			"materialHeadquarters.num" : materialHeadquartersNum
		},
		dataType : DATATYPE_JSON,
		async : true,
		// 将XHR对象的withCredentials设为true
		xhrFields : {
			withCredentials : true
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
 * 获取未审核转库申请列表
 * 
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 * @returns
 */
function ajaxListUndeterminedMaterialTransferApplication(success, fail,
		isExplicit, waiting, run, isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_LIST_UNDETERMINED_MATERIAL_TRANSFER_APPLICATION,
		type : TYPE_POST,
		dataType : DATATYPE_JSON,
		async : true,
		// 将XHR对象的withCredentials设为true
		xhrFields : {
			withCredentials : true
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
 * 模糊查询转库申请历史记录，分页
 * 
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxPageMaterialTransferApplicationByFuzzy(
		materialTransferApplicationDto, page, success, fail, isExplicit,
		waiting, run, isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$
			.ajax({
				url : URL_PAGE_MATERIAL_TRANSFER_APPLICATION_BY_FUZZY,
				type : TYPE_POST,
				data : {
					"page.pagination" : page.pagination,
					"page.size" : page.size,
					"materialTransferApplicationDto.id" : materialTransferApplicationDto.id,
					"materialTransferApplicationDto.importProjectName" : materialTransferApplicationDto.importProjectName,
					"materialTransferApplicationDto.exportProjectName" : materialTransferApplicationDto.exportProjectName,
					"materialTransferApplicationDto.status" : materialTransferApplicationDto.status,
					"materialTransferApplicationDto.staffName" : materialTransferApplicationDto.staffName,
					"materialTransferApplicationDto.cstCreate" : materialTransferApplicationDto.cstCreate
				},
				dataType : DATATYPE_JSON,
				async : true,
				// 将XHR对象的withCredentials设为true
				xhrFields : {
					withCredentials : true
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
 * 获取转库申请详情
 * 
 * @param materialTransferApplicationId
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxGetMaterialTransferApplicationDetailById(
		materialTransferApplicationId, success, fail, isExplicit, waiting, run,
		isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_GET_MATERIAL_TRANSFER_APPLICATION_DETAIL_BY_ID,
		type : TYPE_POST,
		data : {
			"materialTransferApplication.id" : materialTransferApplicationId
		},
		dataType : DATATYPE_JSON,
		async : true,
		// 将XHR对象的withCredentials设为true
		xhrFields : {
			withCredentials : true
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
 * 通过转库审核
 * 
 * @param materialTransferApplicationId
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxPassMaterialTransferApplicationById(materialTransferApplicationId,
		success, fail, isExplicit, waiting, run, isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_PASS_MATERIAL_TRANSFER_APPLICATION_BY_ID,
		type : TYPE_POST,
		data : {
			"materialTransferApplication.id" : materialTransferApplicationId
		},
		dataType : DATATYPE_JSON,
		async : true,
		// 将XHR对象的withCredentials设为true
		xhrFields : {
			withCredentials : true
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
 * 驳回转库审核
 * 
 * @param materialTransferApplicationId
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxFailMaterialTransferApplicationById(materialTransferApplicationId,
		success, fail, isExplicit, waiting, run, isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_FAIL_MATERIAL_TRANSFER_APPLICATION_BY_ID,
		type : TYPE_POST,
		data : {
			"materialTransferApplication.id" : materialTransferApplicationId
		},
		dataType : DATATYPE_JSON,
		async : true,
		// 将XHR对象的withCredentials设为true
		xhrFields : {
			withCredentials : true
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