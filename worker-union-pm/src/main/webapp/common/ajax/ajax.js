/**
 * AJAX API 使用该JS，必须先导入jquery.js/conf.js/common.js
 */

/** HTTP-GET */
var TYPE_GET = "get";
/** HTTP-POST */
var TYPE_POST = "post";
/** 传输格式-JSON */
var DATATYPE_JSON = "json";

/** 正常返回returnCode标识 */
var RETURNCODE_SUCCESS = "SUCCESS";
/** 异常返回returnCode标识 */
var RETURNCODE_FAIL = "FAIL";

/**
 * 处理返回"用户未登录"
 * 
 * @param data
 *            返回JSON数据
 * @param isExplicit
 *            是否显式处理
 * @returns 校验结果
 */
function verifyLogin(data, isExplicit) {
	if ("NOT_LOGIN" == data.errorCode) {
		if (isExplicit) {
			alert("用户未登录，请重新登录！");
			gotoHtml(HTML_LOGIN);
		}
		return false;
	} else {
		return true;
	}
}

/**
 * 处理返回"用户未授权"
 *
 * @param data
 *            返回JSON数据
 * @param isExplicit
 *            是否显式处理
 * @returns 校验结果
 */
function verifyProjectLogin(data, isExplicit) {
	if ("UNAUTHORIZED" == data.errorCode) {
		if (isExplicit) {
			alert("用户未授权，请重新选择项目！");
			gotoHtml(HTML_HOME);
		}
		return false;
	} else {
		return true;
	}
}

/**
 * 处理返回"权限不足"
 * 
 * @param data
 *            返回JSON数据
 * @param isExplicit
 *            是否显式处理
 * @returns 校验结果
 */
function verifyAuthorization(data, isExplicit) {
	if ("UNAUTHORIZED" == data.errorCode) {
		if (isExplicit) {
			alert("权限不足！");
		}
		return false;
	} else {
		return true;
	}
}

/**
 * 网络故障
 * 
 * @param isExplicit
 *            是否显式处理
 */
function networkFault(isExplicit) {
	if (isExplicit) {
		alert("网络故障，请稍后再试！");
	}
}

/**
 * 处理返回业务数据"FAIL"
 * 
 * @param data
 *            JSON数据
 * @param isExplicit
 *            是否显式处理
 */
function failDefault(data, isExplicit) {
	if (verifyLogin(data, isExplicit) && verifyAuthorization(data, isExplicit)&&verifyProjectLogin(data, isExplicit)) {
		if (isExplicit) {
			alert(data.errorCodeDesc);
		}
	}
}

/**
 * AJAX-登录
 * 
 * @param jobNo
 *            员工工号
 * @param password
 *            密码
 * @param waiting
 *            缓冲
 * @param success
 *            处理成功获取业务数据
 * @param run
 *            正常运行
 * @param show
 *            是否显示调用
 * @param isExplicit
 *            是否显式处理
 * @param isLoadingExplicit
 *            缓冲是否显示处理
 */
function ajaxLogin(jobNo, password, success, fail, isExplicit, waiting, run,
		isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_LOGIN,
		type : TYPE_POST,
		data : {
			"staff.jobNo" : jobNo,
			"staff.password" : password
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


// 导入子AJAX文件
document.write("<script type='text/javascript' src='" + getContextRoot()
		+ "/common/ajax/ajax_staff.js'></script>");
document.write("<script type='text/javascript' src='" + getContextRoot()
		+ "/common/ajax/ajax_project.js'></script>");
document.write("<script type='text/javascript' src='" + getContextRoot()
		+ "/common/ajax/ajax_sts.js'></script>");
document.write("<script type='text/javascript' src='" + getContextRoot()
		+ "/common/ajax/ajax_cost_analysis.js'></script>");
document.write("<script type='text/javascript' src='" + getContextRoot()
	+ "/common/ajax/ajax_project_application.js'></script>");
document.write("<script type='text/javascript' src='" + getContextRoot()
	+ "/common/ajax/ajax_target.js'></script>");
document.write("<script type='text/javascript' src='" + getContextRoot()
	+ "/common/ajax/ajax_project_team.js'></script>");
document.write("<script type='text/javascript' src='" + getContextRoot()
	+ "/common/ajax/ajax_scheme_deepen.js'></script>");
document.write("<script type='text/javascript' src='" + getContextRoot()
	+ "/common/ajax/ajax_cost_analysis_deepen.js'></script>");
document.write("<script type='text/javascript' src='" + getContextRoot()
	+ "/common/ajax/ajax_fund_plan.js'></script>");
document.write("<script type='text/javascript' src='" + getContextRoot()
	+ "/common/ajax/ajax_purchase_item.js'></script>");
document.write("<script type='text/javascript' src='" + getContextRoot()
	+ "/common/ajax/ajax_business_capacity.js'></script>");
document.write("<script type='text/javascript' src='" + getContextRoot()
	+ "/common/ajax/ajax_product_capacity.js'></script>");
document.write("<script type='text/javascript' src='" + getContextRoot()
	+ "/common/ajax/ajax_technical_proposal.js'></script>");
document.write("<script type='text/javascript' src='" + getContextRoot()
	+ "/common/ajax/ajax_resource_implement.js'></script>");
document.write("<script type='text/javascript' src='" + getContextRoot()
	+ "/common/ajax/ajax_contract.js'></script>");
document.write("<script type='text/javascript' src='" + getContextRoot()
	+ "/common/ajax/ajax_subcontractor.js'></script>");
document.write("<script type='text/javascript' src='" + getContextRoot()
	+ "/common/ajax/ajax_settlement.js'></script>");
document.write("<script type='text/javascript' src='" + getContextRoot()
	+ "/common/ajax/ajax_material.js'></script>");
document.write("<script type='text/javascript' src='" + getContextRoot()
	+ "/common/ajax/ajax_material_type.js'></script>");
document.write("<script type='text/javascript' src='" + getContextRoot()
	+ "/common/ajax/ajax_completion_data.js'></script>");
document.write("<script type='text/javascript' src='" + getContextRoot()
	+ "/common/ajax/ajax_final_report.js'></script>");
document.write("<script type='text/javascript' src='" + getContextRoot()
	+ "/common/ajax/ajax_internal_settlement.js'></script>");
document.write("<script type='text/javascript' src='" + getContextRoot()
	+ "/common/ajax/ajax_final_settlement.js'></script>");
document.write("<script type='text/javascript' src='" + getContextRoot()
	+ "/common/ajax/ajax_safety_check.js'></script>");
document.write("<script type='text/javascript' src='" + getContextRoot()
	+ "/common/ajax/ajax_quality_check.js'></script>");