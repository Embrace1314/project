package top.warmheart.workerunion.pm.server.util;

import top.warmheart.workerunion.server.dto.AttachmentDto;
import top.warmheart.workerunion.server.dto.SettlementItem2Dto;
import top.warmheart.workerunion.server.dto.StaffDto;
import top.warmheart.workerunion.server.exception.WhAttachmentDeletedException;
import top.warmheart.workerunion.server.exception.WhContractDeletedException;
import top.warmheart.workerunion.server.exception.WhIncompatibleProjectException;
import top.warmheart.workerunion.server.exception.WhInvalidAttachmentException;
import top.warmheart.workerunion.server.exception.WhInvalidBusinessCapacityItemException;
import top.warmheart.workerunion.server.exception.WhInvalidContractException;
import top.warmheart.workerunion.server.exception.WhInvalidCostAnalysisItemDeepenException;
import top.warmheart.workerunion.server.exception.WhInvalidCostAnalysisItemException;
import top.warmheart.workerunion.server.exception.WhInvalidFinalSettlementItemAttachmentException;
import top.warmheart.workerunion.server.exception.WhInvalidFundPlanItemException;
import top.warmheart.workerunion.server.exception.WhInvalidMaterialException;
import top.warmheart.workerunion.server.exception.WhInvalidMaterialTypeException;
import top.warmheart.workerunion.server.exception.WhInvalidProductCapacityItemException;
import top.warmheart.workerunion.server.exception.WhInvalidProjectException;
import top.warmheart.workerunion.server.exception.WhInvalidProjectTeamException;
import top.warmheart.workerunion.server.exception.WhInvalidPurchaseItemException;
import top.warmheart.workerunion.server.exception.WhInvalidQualityCheckItemAttachmentException;
import top.warmheart.workerunion.server.exception.WhInvalidQualityCheckItemException;
import top.warmheart.workerunion.server.exception.WhInvalidResourceImplementItemException;
import top.warmheart.workerunion.server.exception.WhInvalidSafetyCheckItemAttachmentException;
import top.warmheart.workerunion.server.exception.WhInvalidSettlementItemException;
import top.warmheart.workerunion.server.exception.WhInvalidStaffException;
import top.warmheart.workerunion.server.exception.WhInvalidSubcontractorException;
import top.warmheart.workerunion.server.exception.WhInvalidTargetException;
import top.warmheart.workerunion.server.exception.WhInvalidTeamRoleException;
import top.warmheart.workerunion.server.exception.WhMaterialTypeDeletedException;
import top.warmheart.workerunion.server.exception.WhProjectArchivedException;
import top.warmheart.workerunion.server.exception.WhProjectDeletedException;
import top.warmheart.workerunion.server.exception.WhPurchaseItemDeletedException;
import top.warmheart.workerunion.server.exception.WhQualityCheckItemAttachmentDeletedException;
import top.warmheart.workerunion.server.exception.WhQualityCheckItemDeletedException;
import top.warmheart.workerunion.server.exception.WhSafetyCheckItemAttachmentDeletedException;
import top.warmheart.workerunion.server.exception.WhStaffDeletedException;
import top.warmheart.workerunion.server.exception.WhStaffNotLoginException;
import top.warmheart.workerunion.server.exception.WhSubcontractorDeletedException;
import top.warmheart.workerunion.server.model.Attachment;
import top.warmheart.workerunion.server.model.BusinessCapacityItem;
import top.warmheart.workerunion.server.model.Contract;
import top.warmheart.workerunion.server.model.CostAnalysisItem;
import top.warmheart.workerunion.server.model.CostAnalysisItemDeepen;
import top.warmheart.workerunion.server.model.FinalSettlementItemAttachment;
import top.warmheart.workerunion.server.model.FundPlanItem;
import top.warmheart.workerunion.server.model.Material;
import top.warmheart.workerunion.server.model.MaterialType;
import top.warmheart.workerunion.server.model.ProductCapacityItem;
import top.warmheart.workerunion.server.model.Project;
import top.warmheart.workerunion.server.model.ProjectTeam;
import top.warmheart.workerunion.server.model.PurchaseItem;
import top.warmheart.workerunion.server.model.QualityCheckItem;
import top.warmheart.workerunion.server.model.QualityCheckItemAttachment;
import top.warmheart.workerunion.server.model.ResourceImplementItem;
import top.warmheart.workerunion.server.model.SafetyCheckItem;
import top.warmheart.workerunion.server.model.SafetyCheckItemAttachment;
import top.warmheart.workerunion.server.model.SettlementItem;
import top.warmheart.workerunion.server.model.Staff;
import top.warmheart.workerunion.server.model.Subcontractor;
import top.warmheart.workerunion.server.model.Target;
import top.warmheart.workerunion.server.model.TeamRole;

/**
 * 权属校验工具 主要校验是否同属一个企业
 * 
 * @author seulad
 *
 */
public class WhOwnerShipUtil {

	/**
	 * 校验
	 * 
	 * @param project
	 *            企业
	 * @param staffEx
	 *            当前用户
	 * @throws Exception
	 */
	public static void checkStaffCompany(Project project, Staff staffEx) throws Exception {
		checkStaff(staffEx);

		// 无效的信息
		if (null == project || !staffEx.getCompanyId().equals(project.getCompanyId())) {
			throw new WhInvalidProjectException();
		}

		// 信息已删除
		if (project.getDel()) {
			throw new WhProjectDeletedException();
		}
	}

	public static void checkPurchaseItem(PurchaseItem purchaseItem) throws Exception {
		// 无效的信息
		if (null == purchaseItem) {
			throw new WhInvalidPurchaseItemException();
		}

		// 信息已删除
		if (purchaseItem.getDel()) {
			throw new WhPurchaseItemDeletedException();
		}
	}

	public static void checkStaffCompany(Subcontractor subcontractor, Staff staffEx) throws Exception {
		checkStaff(staffEx);

		// 无效的信息
		if (null == subcontractor || !staffEx.getCompanyId().equals(subcontractor.getCompanyId())) {
			throw new WhInvalidSubcontractorException();
		}

		// 信息已删除
		if (subcontractor.getDel()) {
			throw new WhSubcontractorDeletedException();
		}
	}

	/**
	 * 校验
	 * 
	 * @param staff
	 *            员工信息
	 * @param staffEx
	 *            当前用户
	 * @throws Exception
	 */
	public static void checkStaffCompany(Staff staff, Staff staffEx) throws Exception {
		checkStaff(staffEx);

		// 无效的信息
		if (null == staff || !staffEx.getCompanyId().equals(staff.getCompanyId())) {
			throw new WhInvalidStaffException();
		}

		// 信息已删除
		if (staff.getDel()) {
			throw new WhStaffDeletedException();
		}
	}

	/**
	 * 校验
	 * 
	 * @param staffDto
	 *            员工信息
	 * @param staffEx
	 *            当前用户
	 * @throws Exception
	 */
	public static void checkStaffCompany(StaffDto staffDto, Staff staffEx) throws Exception {
		checkStaff(staffEx);

		// 无效的信息
		if (null == staffDto || !staffEx.getCompanyId().equals(staffDto.getCompanyId())) {
			throw new WhInvalidStaffException();
		}

		// 信息已删除
		if (staffDto.getDel()) {
			throw new WhStaffDeletedException();
		}
	}

	/**
	 * 校验
	 * 
	 * @param attachment
	 *            附件信息
	 * @param staffEx
	 *            当前用户
	 * @throws Exception
	 */
	public static void checkStaffCompany(Attachment attachment, Staff staffEx) throws Exception {
		checkStaff(staffEx);

		// 无效信息
		if (null == attachment || !staffEx.getCompanyId().equals(attachment.getCompanyId())) {
			throw new WhInvalidAttachmentException();
		}

		// 信息已删除
		if (attachment.getDel()) {
			throw new WhAttachmentDeletedException();
		}
	}

	/**
	 * 校验
	 * 
	 * @param attachment
	 *            附件信息
	 * @param staffEx
	 *            当前用户
	 * @throws Exception
	 */
	public static void checkStaffCompany(CostAnalysisItem costAnalysisItem, Staff staffEx) throws Exception {
		checkStaff(staffEx);

		// 无效信息
		if (null == costAnalysisItem || !staffEx.getCompanyId().equals(costAnalysisItem.getCompanyId())) {
			throw new WhInvalidCostAnalysisItemException();
		}
	}

	/**
	 * 校验
	 * 
	 * @param project
	 *            请求的项目
	 * @param projectEx
	 *            登录的项目
	 */
	public static void checkProject(Project project, Project projectEx) throws Exception {
		checkProject(projectEx);

		// 无效信息
		if (null == project || !project.getId().equals(projectEx.getId())) {
			throw new WhIncompatibleProjectException();
		}
	}

	/**
	 * 校验
	 * 
	 * @param attachment
	 *            请求的附件
	 * @param projectEx
	 *            登录的项目
	 */
	public static void checkProject(Attachment attachment, Project projectEx) throws Exception {
		checkProject(projectEx);

		// 无效信息
		if (null == attachment || !attachment.getProjectId().equals(projectEx.getId())) {
			throw new WhInvalidAttachmentException();
		}

		// 信息已删除
		if (attachment.getDel()) {
			throw new WhAttachmentDeletedException();
		}
	}

	/**
	 * 校验
	 * 
	 * @param attachment
	 *            请求的附件
	 * @param projectEx
	 *            登录的项目
	 */
	public static void checkProject(AttachmentDto attachment, Project projectEx) throws Exception {
		checkProject(projectEx);

		// 无效信息
		if (null == attachment || !attachment.getProjectId().equals(projectEx.getId())) {
			throw new WhInvalidAttachmentException();
		}

		// 信息已删除
		if (attachment.getDel()) {
			throw new WhAttachmentDeletedException();
		}
	}

	/**
	 * 校验用户
	 * 
	 * @param staffEx
	 *            登录的用户
	 */
	public static void checkStaff(Staff staffEx) throws Exception {
		// 用户未登录
		if (null == staffEx) {
			throw new WhStaffNotLoginException();
		}
	}

	/**
	 * 校验项目
	 * 
	 * @param projectEx
	 *            登录的项目
	 */
	public static void checkProject(Project projectEx) throws Exception {
		// 项目未登录
		if (null == projectEx) {
			throw new WhIncompatibleProjectException();
		}
	}

	/**
	 * 校验项目是否处于正常运行状态，即未归档
	 * 
	 * @param project
	 * @throws Exception
	 */
	public static void checkGoingProject(Project project) throws Exception {
		if (null == project) {
			throw new WhInvalidProjectException();
		}
		if (project.getDel()) {
			throw new WhProjectDeletedException();
		}
		if (!project.getFileStatus().equalsIgnoreCase(Project.FILE_STATUS_GOING)) {
			throw new WhProjectArchivedException();
		}
	}

	/**
	 * 校验公司归属
	 * 
	 * @param staff
	 * @param projectEx
	 */
	public static void checkProjectCompany(Staff staff, Project projectEx) throws Exception {
		checkProject(projectEx);
		if (null == staff || !staff.getCompanyId().equals(projectEx.getCompanyId())) {
			throw new WhInvalidStaffException();
		}

		if (staff.getDel()) {
			throw new WhStaffDeletedException();
		}
	}

	/**
	 * 校验角色信息
	 * 
	 * @param teamRole
	 * @throws Exception
	 */
	public static void checkTeamRole(TeamRole teamRole) throws Exception {
		if (null == teamRole) {
			throw new WhInvalidTeamRoleException();
		}
	}

	public static void checkFundPlanItem(FundPlanItem fundPlanItem) throws Exception {
		if (null == fundPlanItem) {
			throw new WhInvalidFundPlanItemException();
		}
	}

	public static void checkProjectTeam(ProjectTeam projectTeam) throws Exception {
		if (null == projectTeam) {
			throw new WhInvalidProjectTeamException();
		}
	}

	public static void checkProject(CostAnalysisItemDeepen costAnalysisItemDeepen, Project projectEx) throws Exception {
		checkProject(projectEx);
		if (null == costAnalysisItemDeepen || !costAnalysisItemDeepen.getProjectId().equals(projectEx.getId())) {
			throw new WhInvalidCostAnalysisItemDeepenException();
		}
	}

	public static void checkProject(FundPlanItem fundPlanItem, Project projectEx) throws Exception {
		checkProject(projectEx);
		if (null == fundPlanItem || !fundPlanItem.getProjectId().equals(projectEx.getId())) {
			throw new WhInvalidFundPlanItemException();
		}
	}

	public static void checkTarget(Target target) throws Exception {
		if (null == target) {
			throw new WhInvalidTargetException();
		}
	}

	public static void checkBusinessCapacityItemProject(BusinessCapacityItem businessCapacityItem, Project projectEx)
			throws Exception {
		checkProject(projectEx);
		if (null == businessCapacityItem || !businessCapacityItem.getProjectId().equals(projectEx.getId())) {
			throw new WhInvalidBusinessCapacityItemException();
		}
	}

	public static void checkProductCapacityItemProject(ProductCapacityItem productCapacityItem, Project projectEx)
			throws Exception {
		checkProject(projectEx);
		if (null == productCapacityItem || !productCapacityItem.getProjectId().equals(projectEx.getId())) {
			throw new WhInvalidProductCapacityItemException();
		}
	}

	public static void checkProject(SafetyCheckItemAttachment safetyCheckItemAttachmentCheck, Project projectEx)
			throws Exception {
		checkProject(projectEx);

		// 无效信息
		if (null == safetyCheckItemAttachmentCheck
				|| !safetyCheckItemAttachmentCheck.getProjectId().equals(projectEx.getId())) {
			throw new WhInvalidSafetyCheckItemAttachmentException();
		}

		// 信息已删除
		if (safetyCheckItemAttachmentCheck.getDel()) {
			throw new WhSafetyCheckItemAttachmentDeletedException();
		}
	}

	public static void checkProject(QualityCheckItemAttachment qualityCheckItemAttachmentCheck, Project projectEx)
			throws Exception {
		checkProject(projectEx);

		// 无效信息
		if (null == qualityCheckItemAttachmentCheck
				|| !qualityCheckItemAttachmentCheck.getProjectId().equals(projectEx.getId())) {
			throw new WhInvalidQualityCheckItemAttachmentException();
		}

		// 信息已删除
		if (qualityCheckItemAttachmentCheck.getDel()) {
			throw new WhQualityCheckItemAttachmentDeletedException();
		}
	}

	public static void checkProject(SafetyCheckItem safetyCheckItem, Project projectEx) throws Exception {
		checkProject(projectEx);

		// 无效信息
		if (null == safetyCheckItem || !safetyCheckItem.getProjectId().equals(projectEx.getId())) {
			throw new WhInvalidQualityCheckItemException();
		}

		// 信息已删除
		if (safetyCheckItem.getDel()) {
			throw new WhQualityCheckItemDeletedException();
		}
	}

	public static void checkProject(QualityCheckItem qualityCheckItemCheck, Project projectEx) throws Exception {
		checkProject(projectEx);

		// 无效信息
		if (null == qualityCheckItemCheck || !qualityCheckItemCheck.getProjectId().equals(projectEx.getId())) {
			throw new WhInvalidQualityCheckItemException();
		}

		// 信息已删除
		if (qualityCheckItemCheck.getDel()) {
			throw new WhQualityCheckItemDeletedException();
		}
	}

	public static void checkProject(ResourceImplementItem resourceImplementItem, Project projectEx) throws Exception {
		checkProject(projectEx);
		if (null == resourceImplementItem || !resourceImplementItem.getProjectId().equals(projectEx.getId())) {
			throw new WhInvalidResourceImplementItemException();
		}
	}

	public static void checkProject(Contract contract, Project projectEx) throws Exception {
		checkProject(projectEx);

		// 无效信息
		if (null == contract || !contract.getProjectId().equals(projectEx.getId())) {
			throw new WhInvalidContractException();
		}

		// 信息已删除
		if (contract.getDel()) {
			throw new WhContractDeletedException();
		}
	}

	public static void checkProject(SettlementItem settlementItem, Project projectEx) throws Exception {
		checkProject(projectEx);
		if (null == settlementItem || !settlementItem.getProjectId().equals(projectEx.getId())) {
			throw new WhInvalidSettlementItemException();
		}
	}
	public static void checkProject(SettlementItem2Dto settlementItem, Project projectEx) throws Exception {
		checkProject(projectEx);
		if (null == settlementItem || !settlementItem.getProjectId().equals(projectEx.getId())) {
			throw new WhInvalidSettlementItemException();
		}
	}

	public static void checkProject(FinalSettlementItemAttachment finalSettlementItemAttachment, Project projectEx)
			throws Exception {
		checkProject(projectEx);
		if (null == finalSettlementItemAttachment
				|| !finalSettlementItemAttachment.getProjectId().equals(projectEx.getId())) {
			throw new WhInvalidFinalSettlementItemAttachmentException();
		}
	}

	public static void checkProject(Material materialCheck, Project projectEx) throws Exception {
		checkProject(projectEx);
		if (null == materialCheck || !materialCheck.getProjectId().equals(projectEx.getId())) {
			throw new WhInvalidMaterialException();
		}
	}

	public static void checkMaterialType(MaterialType materialType) throws Exception {
		if (null == materialType) {
			throw new WhInvalidMaterialTypeException();
		}
		if (materialType.getDel()) {
			throw new WhMaterialTypeDeletedException();
		}
	}

}
