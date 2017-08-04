/**
 * 获取简要分包商列表
 *
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxListSimpleSubcontractorItem(success, fail, isExplicit,
                                               waiting, run, isLoadingExplicit) {
    // 显示缓冲
    if (isLoadingExplicit) {
        waiting();
    }
    $.ajax({
        url : URL_LIST_SIMPLE_SUBCONTRACTOR_ITEM,
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

/**
 * 根据模糊条件查询供应商信息，分页
 * 
 * @param subcontractorDto
 * @param page
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxPageSubcontractorByFuzzy(subcontractorDto, page, success, fail, isExplicit,
		waiting, run, isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$
			.ajax({
				url : URL_PAGE_SUBCONTRACTOR_BY_FUZZY,
				type : TYPE_POST,
				data : {
					"subcontractorDto.name" : subcontractorDto.name,
					"subcontractorDto.num" : subcontractorDto.num,
					"subcontractorDto.subcontractorQualificationId" : subcontractorDto.subcontractorQualificationId,
					"subcontractorDto.validDate" : subcontractorDto.validDate,
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
 * @param subcontractor
 * @param subcontractorQualifications
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxAddSubcontractor(subcontractor, subcontractorQualificationIds, success, fail,
		isExplicit, waiting, run, isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	var data = "subcontractor.validDate=" + subcontractor.validDate + "&subcontractor.name="
			+ subcontractor.name + "&subcontractor.num=" + subcontractor.num
			+ "&subcontractor.subcontractorGradeId=" + subcontractor.subcontractorGradeId
			+ "&subcontractor.contactPerson=" + subcontractor.contactPerson
			+ "&subcontractor.contactPhone=" + subcontractor.contactPhone;
	for (var i = 0; i < subcontractorQualificationIds.length; i++) {
		data = data + "&subcontractorQualifications[" + i + "].id="
				+ subcontractorQualificationIds[i];
	}
	$.ajax({
		url : URL_ADD_SUBCONTRACTOR,
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
 * @param subcontractorId
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxRemoveSubcontractorById(subcontractorId, success, fail, isExplicit, waiting,
		run, isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_REMOVE_SUBCONTRACTOR_BY_ID,
		type : TYPE_POST,
		data : {
			"subcontractor.id" : subcontractorId
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
 * @param subcontractorId
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxGetSubcontractorDetailById(subcontractorId, success, fail, isExplicit,
		waiting, run, isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_GET_SUBCONTRACTOR_DETAIL_BY_ID,
		type : TYPE_POST,
		data : {
			"subcontractor.id" : subcontractorId
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
 * @param subcontractor
 * @param subcontractorQualificationIds
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxModifySubcontractorById(subcontractor, subcontractorQualificationIds, success,
		fail, isExplicit, waiting, run, isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	var data = "subcontractor.id=" + subcontractor.id + "&subcontractor.validDate="
			+ subcontractor.validDate + "&subcontractor.name=" + subcontractor.name
			+ "&subcontractor.subcontractorGradeId=" + subcontractor.subcontractorGradeId
			+ "&subcontractor.contactPerson=" + subcontractor.contactPerson
			+ "&subcontractor.contactPhone=" + subcontractor.contactPhone;
	for (var i = 0; i < subcontractorQualificationIds.length; i++) {
		data = data + "&subcontractorQualifications[" + i + "].id="
				+ subcontractorQualificationIds[i];
	}
	$.ajax({
		url : URL_MODIFY_SUBCONTRACTOR_BY_ID,
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
 * @param subcontractorAttachmentId
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxRemoveSubcontractorAttachmentById(subcontractorAttachmentId, success, fail,
		isExplicit, waiting, run, isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_REMOVE_SUBCONTRACTOR_ATTACHMENT_BY_ID,
		type : TYPE_POST,
		data : {
			"subcontractorAttachment.id" : subcontractorAttachmentId
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
function ajaxListSubcontractorGrade(success, fail, isExplicit, waiting, run,
		isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_LIST_SUBCONTRACTOR_GRADE,
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
function ajaxListSubcontractorQualification(success, fail, isExplicit, waiting, run,
		isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_LIST_SUBCONTRACTOR_QUALIFICATION,
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


