/**
 * Copyright © WarmHeart Intelligence Science&Technology(NanJing) Company, Limited.
 * All Rights Reserved
 */
package top.warmheart.workerunion.pm.server.action.ex;

import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.pam.UnsupportedTokenException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import top.warmheart.workerunion.server.constant.WebErrorEnum;
import top.warmheart.workerunion.server.exception.*;

import com.alibaba.fastjson.JSONObject;
import com.opensymphony.xwork2.ActionContext;

/**
 * 异常处理
 * 
 * @author seulad
 *
 */
@SuppressWarnings("serial")
@Controller
@Scope("prototype")
public class ExceptionAction extends ActionJson {
	/**
	 * 错误代码返回
	 * 
	 * @return
	 */
	public String exception() {
		Exception ex = (Exception) ActionContext.getContext().getValueStack().findValue("exception");
		LOG.error(ex.getMessage(), ex);
		writeStream(parse(ex));
		return SUCCESS;
	}

	/**
	 * 解析异常信息
	 * 
	 * @param ex
	 * @param jresult
	 */
	private JSONObject parse(Exception ex) {
		JSONObject jresult = getErrorJsonTemplate();
		// 无异常，直接返回
		if (null == ex) {
			return jresult;
		}

		// 参数错误
		if (ex instanceof IllegalArgumentException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.ILLEGAL_PARAMETER.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.ILLEGAL_PARAMETER.errorCodeDesc);
			return jresult;
		}
		/*
		 * 登录异常
		 */
		if (ex instanceof UnknownAccountException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.UNKNOWN_ACCOUNT.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.UNKNOWN_ACCOUNT.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof IncorrectCredentialsException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.INCORRECT_PASSWORD.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.INCORRECT_PASSWORD.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof DisabledAccountException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.DISABLED_ACCOUNT.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.DISABLED_ACCOUNT.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof ExpiredCredentialsException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.NOT_LOGIN.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.NOT_LOGIN.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof UnsupportedTokenException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.INVALID_PROJECT.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.INVALID_PROJECT.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhStaffJobNoExistException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.STAFF_JOBNO_EXIST.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.STAFF_JOBNO_EXIST.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhStaffNotLoginException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.NOT_LOGIN.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.NOT_LOGIN.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhIncompatibleProjectException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.INCOMPATIBLE_PROJECT.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.INCOMPATIBLE_PROJECT.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhStaffCannotDeleteException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.STAFF_CANNOT_DELETE.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.STAFF_CANNOT_DELETE.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhMaterialTransferToArchivedProjectException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.MATERIAL_TRANSFER_TO_ARCHIVED_PROJECT.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.MATERIAL_TRANSFER_TO_ARCHIVED_PROJECT.errorCodeDesc);
			return jresult;
		}
		/*
		 * 已存在异常
		 */
		if (ex instanceof WhProjectTeamExistException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.PROJECT_TEAM_EXIST.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.PROJECT_TEAM_EXIST.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhProjectApplicationExistException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.PROJECT_APPLICATION_EXIST.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.PROJECT_APPLICATION_EXIST.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhContractExistException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.CONTRACT_EXIST.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.CONTRACT_EXIST.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhProjectExistException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.DUPLICATE_PROJECT_NUM.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.DUPLICATE_PROJECT_NUM.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhCostAnalysisExistException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.COST_ANALYSIS_EXIST.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.COST_ANALYSIS_EXIST.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhAttachmentExistException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.ATTACHMENT_EXIST.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.ATTACHMENT_EXIST.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhBusinessCapacityItemExistException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.BUSINESS_CAPACITY_ITEM_EXIST.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.BUSINESS_CAPACITY_ITEM_EXIST.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhBusinessCapacityItemCheckedException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.BUSINESS_CAPACITY_ITEM_CHECKED.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.BUSINESS_CAPACITY_ITEM_CHECKED.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhProductCapacityItemExistException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.PRODUCT_CAPACITY_ITEM_EXIST.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.PRODUCT_CAPACITY_ITEM_EXIST.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhProductCapacityItemCheckedException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.PRODUCT_CAPACITY_ITEM_CHECKED.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.PRODUCT_CAPACITY_ITEM_CHECKED.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhSafetyCheckItemModifyDisabledException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.SAFETY_CHECK_ITEM_MODIFY_DISABLED.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.SAFETY_CHECK_ITEM_MODIFY_DISABLED.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhQualityCheckItemModifyDisabledException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.QUALITY_CHECK_ITEM_MODIFY_DISABLED.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.QUALITY_CHECK_ITEM_MODIFY_DISABLED.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhSettlementItemExistException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.SETTLEMENT_ITEM_EXIST.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.SETTLEMENT_ITEM_EXIST.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhMaterialOrderExistException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.MATERIAL_ORDER_EXIST.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.MATERIAL_ORDER_EXIST.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhMaterialInsufficientException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.MATERIAL_INSUFFICIENT.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.MATERIAL_INSUFFICIENT.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhMaterialHeadquartersOrderExistException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.MATERIAL_HEADQUARTERS_ORDER_EXIST.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.MATERIAL_HEADQUARTERS_ORDER_EXIST.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhMaterialHeadquartersInsufficientException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.MATERIAL_HEADQUARTERS_INSUFFICIENT.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.MATERIAL_HEADQUARTERS_INSUFFICIENT.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhAttachmentAuditedException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.ATTACHMENT_AUDITED.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.ATTACHMENT_AUDITED.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhProjectArchivedException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.PROJECT_ARCHIVED.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.PROJECT_ARCHIVED.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhProjectCollapsedException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.PROJECT_COLLAPSED.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.PROJECT_COLLAPSED.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhProjectArchiveReleaseException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.PROJECT_ARCHIVE_RELEASE.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.PROJECT_ARCHIVE_RELEASE.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhSupplierExistException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.SUPPLIER_EXIST.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.SUPPLIER_EXIST.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhSubcontractorExistException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.SUBCONTRACTOR_EXIST.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.SUBCONTRACTOR_EXIST.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhMaterialTypeExistException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.MATERIAL_TYPE_EXIST.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.MATERIAL_TYPE_EXIST.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhFinalSettlementItemAttachmentExistException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.FINAL_SETTLEMENT_ITEM_ATTACHMENT_EXIST.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.FINAL_SETTLEMENT_ITEM_ATTACHMENT_EXIST.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhCostAnalysisItemDeepenLinkedException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.COST_ANALYSIS_ITEM_DEEPEN_LINKED.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.COST_ANALYSIS_ITEM_DEEPEN_LINKED.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhMaterialTransferApplicationDeterminedException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.MATERIAL_TRANSFER_APPLICATION_DETERMINED.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.MATERIAL_TRANSFER_APPLICATION_DETERMINED.errorCodeDesc);
			return jresult;
		}
		if(ex instanceof WhFundPlanItemLinkedException){
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.FUND_PLAN_ITEM_LINKED.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.FUND_PLAN_ITEM_LINKED.errorCodeDesc);
			return jresult;
		}
		if(ex instanceof WhResourceImplementItemLinkedException){
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.RESOURCE_IMPLEMENT_ITEM_LINKED.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.RESOURCE_IMPLEMENT_ITEM_LINKED.errorCodeDesc);
			return jresult;
		}
		/*
		 * 无效性异常
		 */
		if (ex instanceof WhInvalidAttachmentException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.INVALID_ATTACHMENT.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.INVALID_ATTACHMENT.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhInvalidProjectException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.INVALID_PROJECT.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.INVALID_PROJECT.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhInvalidTeamRoleException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.INVALID_TEAM_ROLE.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.INVALID_TEAM_ROLE.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhInvalidCostAnalysisItemException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.INVALID_COST_ANALYSIS_ITEM.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.INVALID_COST_ANALYSIS_ITEM.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhInvalidCostAnalysisItemDeepenException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.INVALID_COST_ANALYSIS_ITEM_DEEPEN.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.INVALID_COST_ANALYSIS_ITEM_DEEPEN.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhInvalidProjectTeamException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.INVALID_PROJECT_TEAM.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.INVALID_PROJECT_TEAM.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhInvalidProjectApplicationException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.INVALID_PROJECT_APPLICATION.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.INVALID_PROJECT_APPLICATION.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhInvalidFundPlanItemException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.INVALID_FUND_PLAN_ITEM.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.INVALID_FUND_PLAN_ITEM.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhInvalidPurchaseItemException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.INVALID_PURCHASE_ITEM.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.INVALID_PURCHASE_ITEM.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhInvalidYearMonthException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.INVALID_YEARMONTH.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.INVALID_YEARMONTH.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhInvalidTargetException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.INVALID_TARGET.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.INVALID_TARGET.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhInvalidBusinessCapacityItemException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.INVALID_BUSINESS_CAPACITY_ITEM.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.INVALID_BUSINESS_CAPACITY_ITEM.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhInvalidProductCapacityItemException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.INVALID_PRODUCT_CAPACITY_ITEM.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.INVALID_PRODUCT_CAPACITY_ITEM.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhInvalidSafetyCheckItemAttachmentException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.INVALID_SAFETY_CHECK_ITEM_ATTACHMENT.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.INVALID_SAFETY_CHECK_ITEM_ATTACHMENT.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhInvalidQualityCheckItemAttachmentException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.INVALID_QUALITY_CHECK_ITEM_ATTACHMENT.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.INVALID_QUALITY_CHECK_ITEM_ATTACHMENT.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhInvalidSafetyCheckItemException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.INVALID_SAFETY_CHECK_ITEM.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.INVALID_SAFETY_CHECK_ITEM.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhInvalidQualityCheckItemException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.INVALID_QUALITY_CHECK_ITEM.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.INVALID_QUALITY_CHECK_ITEM.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhInvalidSubcontractorException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.INVALID_SUBCONTRACTOR.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.INVALID_SUBCONTRACTOR.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhInvalidSubcontractorAttachmentException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.INVALID_SUBCONTRACTOR_ATTACHMENT.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.INVALID_SUBCONTRACTOR_ATTACHMENT.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhInvalidSubcontractorGradeException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.INVALID_SUBCONTRACTOR_GRADE.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.INVALID_SUBCONTRACTOR_GRADE.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhInvalidSubcontractorQualificationException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.INVALID_SUBCONTRACTOR_QUALIFICATION.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.INVALID_SUBCONTRACTOR_QUALIFICATION.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhInvalidSupplierException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.INVALID_SUPPLIER.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.INVALID_SUPPLIER.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhInvalidSupplierAttachmentException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.INVALID_SUPPLIER_ATTACHMENT.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.INVALID_SUPPLIER_ATTACHMENT.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhInvalidSupplierGradeException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.INVALID_SUPPLIER_GRADE.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.INVALID_SUPPLIER_GRADE.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhInvalidSupplierQualificationException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.INVALID_SUPPLIER_QUALIFICATION.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.INVALID_SUPPLIER_QUALIFICATION.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhInvalidContractException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.INVALID_CONTRACT.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.INVALID_CONTRACT.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhInvalidContractAttachmentException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.INVALID_CONTRACT_ATTACHMENT.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.INVALID_CONTRACT_ATTACHMENT.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhInvalidFinalSettlementItemAttachmentException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.INVALID_FINAL_SETTLEMENT_ITEM_ATTACHMENT.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.INVALID_FINAL_SETTLEMENT_ITEM_ATTACHMENT.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhInvalidMaterialException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.INVALID_MATERIAL.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.INVALID_MATERIAL.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhInvalidMaterialTransferApplicationException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.INVALID_MATERIAL_TRANSFER_APPLICATION.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.INVALID_MATERIAL_TRANSFER_APPLICATION.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhInvalidMaterialTypeException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.INVALID_MATERIAL_TYPE.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.INVALID_MATERIAL_TYPE.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhInvalidMaterialTypeAttachmentException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.INVALID_MATERIAL_TYPE_ATTACHMENT.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.INVALID_MATERIAL_TYPE_ATTACHMENT.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhInvalidMaterialBatchNoException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.INVALID_MATERIAL_BATCH_NO.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.INVALID_MATERIAL_BATCH_NO.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhInvalidMaterialHeadquartersException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.INVALID_MATERIAL_HEADQUARTERS.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.INVALID_MATERIAL_HEADQUARTERS.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhInvalidMaterialHeadquartersBatchNoException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.INVALID_MATERIAL_HEADQUARTERS_BATCH_NO.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.INVALID_MATERIAL_HEADQUARTERS_BATCH_NO.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhInvalidCertificateTypeException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.INVALID_CERTIFICATE_TYPE.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.INVALID_CERTIFICATE_TYPE.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhInvalidCertificateException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.INVALID_CERTIFICATE.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.INVALID_CERTIFICATE.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhInvalidFinalSettlementItemException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.INVALID_FINAL_SETTLEMENT_ITEM.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.INVALID_FINAL_SETTLEMENT_ITEM.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhInvalidFinalSettlementItemAttachmentException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.INVALID_FINAL_SETTLEMENT_ITEM_ATTACHMENT.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.INVALID_FINAL_SETTLEMENT_ITEM_ATTACHMENT.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhInvalidStaffException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.INVALID_STAFF.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.INVALID_STAFF.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhInvalidRoleException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.INVALID_ROLE.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.INVALID_ROLE.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhIncorrectPasswordConfirmException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.INCORRECT_PASSWORD_CONFIRM.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.INCORRECT_PASSWORD_CONFIRM.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhInvalidSettlementItemException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.INVALID_SETTLEMENT_ITEM.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.INVALID_SETTLEMENT_ITEM.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhInvalidResourceImplementItemException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.INVALID_RESOURCE_IMPLEMENT_ITEM.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.INVALID_RESOURCE_IMPLEMENT_ITEM.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhInvalidIDCardException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.INVALID_ID_CARD.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.INVALID_ID_CARD.errorCodeDesc);
			return jresult;
		}
		/*
		 * 已删除异常
		 */
		if (ex instanceof WhRoleDeletedException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.ROLE_DELETED.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.ROLE_DELETED.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhProjectDeletedException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.PROJECT_DELETED.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.PROJECT_DELETED.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhStaffDeletedException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.STAFF_DELETED.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.STAFF_DELETED.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhAttachmentDeletedException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.ATTACHMENT_DELETED.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.ATTACHMENT_DELETED.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhPurchaseItemDeletedException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.PURCHASE_ITEM_DELETED.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.PURCHASE_ITEM_DELETED.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhSafetyCheckItemAttachmentDeletedException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.SAFETY_CHECK_ITEM_ATTACHMENT_DELETED.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.SAFETY_CHECK_ITEM_ATTACHMENT_DELETED.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhQualityCheckItemAttachmentDeletedException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.QUALITY_CHECK_ITEM_ATTACHMENT_DELETED.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.QUALITY_CHECK_ITEM_ATTACHMENT_DELETED.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhSafetyCheckItemDeletedException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.SAFETY_CHECK_ITEM_DELETED.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.SAFETY_CHECK_ITEM_DELETED.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhQualityCheckItemDeletedException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.QUALITY_CHECK_ITEM_DELETED.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.QUALITY_CHECK_ITEM_DELETED.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhSubcontractorDeletedException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.SUBCONTRACTOR_DELETED.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.SUBCONTRACTOR_DELETED.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhSupplierDeletedException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.SUPPLIER_DELETED.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.SUPPLIER_DELETED.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhContractDeletedException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.CONTRACT_DELETED.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.CONTRACT_DELETED.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhContractAttachmentDeletedException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.CONTRACT_ATTACHMENT_DELETED.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.CONTRACT_ATTACHMENT_DELETED.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhCertificateTypeDeletedException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.CERTIFICATE_TYPE_DELETED.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.CERTIFICATE_TYPE_DELETED.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhCertificateDeletedException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.CERTIFICATE_DELETED.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.CERTIFICATE_DELETED.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhMaterialTransferApplicationDeletedException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.MATERIAL_TRANSFER_APPLICATION_DELETED.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.MATERIAL_TRANSFER_APPLICATION_DELETED.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhMaterialTypeDeletedException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.MATERIAL_TYPE_DELETED.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.MATERIAL_TYPE_DELETED.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhMaterialTypeAttachmentDeletedException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.MATERIAL_TYPE_ATTACHMENT_DELETED.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.MATERIAL_TYPE_ATTACHMENT_DELETED.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhFinalSettlementItemDeletedException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.FINAL_SETTLEMENT_ITEM_DELETED.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.FINAL_SETTLEMENT_ITEM_DELETED.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhFinalSettlementItemAttachmentDeletedException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.FINAL_SETTLEMENT_ITEM_ATTACHMENT_DELETED.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.FINAL_SETTLEMENT_ITEM_ATTACHMENT_DELETED.errorCodeDesc);
			return jresult;
		}

		if (ex instanceof WhSubcontractorAttachmentDeletedException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.SUBCONTRACTOR_ATTACHMENT_DELETED.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.SUBCONTRACTOR_ATTACHMENT_DELETED.errorCodeDesc);
			return jresult;
		}
		if (ex instanceof WhSupplierAttachmentDeletedException) {
			jresult.put(WebErrorEnum.CODE, WebErrorEnum.SUPPLIER_ATTACHMENT_DELETED.errorCode);
			jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.SUPPLIER_ATTACHMENT_DELETED.errorCodeDesc);
			return jresult;
		}

		jresult.put(WebErrorEnum.CODE, WebErrorEnum.UNKNOWN_ERROR.errorCode);
		jresult.put(WebErrorEnum.CODE_DESC, WebErrorEnum.UNKNOWN_ERROR.errorCodeDesc);
		return jresult;
	}

}
