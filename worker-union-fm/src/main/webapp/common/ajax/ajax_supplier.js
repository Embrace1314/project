/**
 * 根据模糊条件查询供应商信息，分页
 * 
 * @param supplierDto
 * @param page
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxPageSupplierByFuzzy(supplierDto, page, success, fail, isExplicit,
		waiting, run, isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$
			.ajax({
				url : URL_PAGE_SUPPLIER_BY_FUZZY,
				type : TYPE_POST,
				data : {
					"supplierDto.name" : supplierDto.name,
					"supplierDto.num" : supplierDto.num,
					"supplierDto.supplierQualificationId" : supplierDto.supplierQualificationId,
					"supplierDto.validDate" : supplierDto.validDate,
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
 * 添加供应商信息
 * 
 * @param supplier
 * @param supplierQualifications
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxAddSupplier(supplier, supplierQualificationIds, success, fail,
		isExplicit, waiting, run, isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	var data = "supplier.validDate=" + supplier.validDate + "&supplier.name="
			+ supplier.name + "&supplier.num=" + supplier.num
			+ "&supplier.supplierGradeId=" + supplier.supplierGradeId
			+ "&supplier.contactPerson=" + supplier.contactPerson
			+ "&supplier.contactPhone=" + supplier.contactPhone;
	for (var i = 0; i < supplierQualificationIds.length; i++) {
		data = data + "&supplierQualifications[" + i + "].id="
				+ supplierQualificationIds[i];
	}
	$.ajax({
		url : URL_ADD_SUPPLIER,
		type : TYPE_POST,
		data : data,
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
 * 删除供应商信息
 * 
 * @param supplierId
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxRemoveSupplierById(supplierId, success, fail, isExplicit, waiting,
		run, isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_REMOVE_SUPPLIER_BY_ID,
		type : TYPE_POST,
		data : {
			"supplier.id" : supplierId
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
 * 查询供应商详细信息
 * 
 * @param supplierId
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxGetSupplierDetailById(supplierId, success, fail, isExplicit,
		waiting, run, isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_GET_SUPPLIER_DETAIL_BY_ID,
		type : TYPE_POST,
		data : {
			"supplier.id" : supplierId
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
 * 修改供应商信息
 * 
 * @param supplier
 * @param supplierQualificationIds
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxModifySupplierById(supplier, supplierQualificationIds, success,
		fail, isExplicit, waiting, run, isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	var data = "supplier.id=" + supplier.id + "&supplier.validDate="
			+ supplier.validDate + "&supplier.name=" + supplier.name
			+ "&supplier.supplierGradeId=" + supplier.supplierGradeId
			+ "&supplier.contactPerson=" + supplier.contactPerson
			+ "&supplier.contactPhone=" + supplier.contactPhone;
	for (var i = 0; i < supplierQualificationIds.length; i++) {
		data = data + "&supplierQualifications[" + i + "].id="
				+ supplierQualificationIds[i];
	}
	$.ajax({
		url : URL_MODIFY_SUPPLIER_BY_ID,
		type : TYPE_POST,
		data : data,
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
 * 删除供应商信息附件文件
 * 
 * @param supplierAttachmentId
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxRemoveSupplierAttachmentById(supplierAttachmentId, success, fail,
		isExplicit, waiting, run, isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_REMOVE_SUPPLIER_ATTACHMENT_BY_ID,
		type : TYPE_POST,
		data : {
			"supplierAttachment.id" : supplierAttachmentId
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
 * 获取企业下的供应商级别列表
 * 
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxListSupplierGrade(success, fail, isExplicit, waiting, run,
		isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_LIST_SUPPLIER_GRADE,
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
 * 获取企业下的供应商资质列表
 * 
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxListSupplierQualification(success, fail, isExplicit, waiting, run,
		isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_LIST_SUPPLIER_QUALIFICATION,
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
