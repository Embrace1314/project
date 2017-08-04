package top.warmheart.workerunion.pm.server.action;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.springframework.context.annotation.Scope;

import top.warmheart.server.util.dto.Page;
import top.warmheart.server.util.exception.WhNull;
import top.warmheart.workerunion.pm.server.action.ex.ActionJson;
import top.warmheart.workerunion.pm.server.constant.SessionKey;
import top.warmheart.workerunion.pm.server.util.WhOwnerShipUtil;
import top.warmheart.workerunion.pm.server.util.WhYearMonthUtil;
import top.warmheart.workerunion.server.dto.AttachmentDto;
import top.warmheart.workerunion.server.dto.SettlementItem2Dto;
import top.warmheart.workerunion.server.dto.SettlementItemDto;
import top.warmheart.workerunion.server.exception.WhAttachmentAuditedException;
import top.warmheart.workerunion.server.exception.WhInvalidAttachmentException;
import top.warmheart.workerunion.server.exception.WhSettlementItemExistException;
import top.warmheart.workerunion.server.model.Attachment;
import top.warmheart.workerunion.server.model.AttachmentAudit;
import top.warmheart.workerunion.server.model.Project;
import top.warmheart.workerunion.server.model.PurchaseItem;
import top.warmheart.workerunion.server.model.ResourceImplementItem;
import top.warmheart.workerunion.server.model.SettlementItem;
import top.warmheart.workerunion.server.model.Staff;
import top.warmheart.workerunion.server.model.Subcontractor;
import top.warmheart.workerunion.server.model.Target;
import top.warmheart.workerunion.server.service.AttachmentService;
import top.warmheart.workerunion.server.service.ProjectService;
import top.warmheart.workerunion.server.service.PurchaseItemService;
import top.warmheart.workerunion.server.service.ResourceImplementItemService;
import top.warmheart.workerunion.server.service.SettlementItemService;
import top.warmheart.workerunion.server.service.SubcontractorService;
import top.warmheart.workerunion.server.service.TargetService;

import com.alibaba.fastjson.JSONObject;

@SuppressWarnings("serial")
@Scope("prototype")
public class SettlementAction extends ActionJson {
	private Project project;
	private Attachment attachment;
	private Page<Void> page;
	private AttachmentService attachmentService;
	private SettlementItem settlementItem;
	private SettlementItemService settlementItemService;
	private TargetService targetService;
	private SubcontractorService subcontractorService;
	private PurchaseItemService purchaseItemService;
	private ResourceImplementItemService resourceImplementItemService;
	private ProjectService projectService;
	
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Page<Void> getPage() {
		return page;
	}

	public void setPage(Page<Void> page) {
		this.page = page;
	}

	public Attachment getAttachment() {
		return attachment;
	}

	public void setAttachment(Attachment attachment) {
		this.attachment = attachment;
	}

	public SettlementItem getSettlementItem() {
		return settlementItem;
	}

	public void setSettlementItem(SettlementItem settlementItem) {
		this.settlementItem = settlementItem;
	}

	public AttachmentService getAttachmentService() {
		return attachmentService;
	}

	@Resource(name = "attachmentService")
	public void setAttachmentService(AttachmentService attachmentService) {
		this.attachmentService = attachmentService;
	}

	public SettlementItemService getSettlementItemService() {
		return settlementItemService;
	}

	@Resource(name = "settlementItemService")
	public void setSettlementItemService(SettlementItemService settlementItemService) {
		this.settlementItemService = settlementItemService;
	}

	public TargetService getTargetService() {
		return targetService;
	}

	@Resource(name = "targetService")
	public void setTargetService(TargetService targetService) {
		this.targetService = targetService;
	}

	public SubcontractorService getSubcontractorService() {
		return subcontractorService;
	}

	@Resource(name = "subcontractorService")
	public void setSubcontractorService(SubcontractorService subcontractorService) {
		this.subcontractorService = subcontractorService;
	}

	public PurchaseItemService getPurchaseItemService() {
		return purchaseItemService;
	}

	@Resource(name = "purchaseItemService")
	public void setPurchaseItemService(PurchaseItemService purchaseItemService) {
		this.purchaseItemService = purchaseItemService;
	}

	public ResourceImplementItemService getResourceImplementItemService() {
		return resourceImplementItemService;
	}

	@Resource(name = "resourceImplementItemService")
	public void setResourceImplementItemService(ResourceImplementItemService resourceImplementItemService) {
		this.resourceImplementItemService = resourceImplementItemService;
	}
	public ProjectService getProjectService() {
		return projectService;
	}

	@Resource(name = "projectService")
	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}
	/**
	 * 删除结算文件
	 * 
	 * @return
	 * @throws Exception
	 */
	public String removeAttachmentById() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());
		WhNull.check(attachment);
		WhNull.check(attachment.getId());

		/*
		 * 校验项目
		 */
		Project projectEx = (Project) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_PROJECT);
		WhOwnerShipUtil.checkProject(project, projectEx);
		// 归档校验
		WhOwnerShipUtil.checkGoingProject(projectService.getById(projectEx.getId()));

		/*
		 * 校验附件
		 */
		AttachmentDto attachmentDtoCheck = attachmentService.getAuditById(attachment.getId());
		WhOwnerShipUtil.checkProject(attachmentDtoCheck, projectEx);
		if (!Attachment.TYPE_SETTLEMENT_FILE.equalsIgnoreCase(attachmentDtoCheck.getType())) {
			throw new WhInvalidAttachmentException();
		}
		if (AttachmentAudit.STATUS_PASS.equalsIgnoreCase(attachmentDtoCheck.getAuditStatus())){
			throw new WhAttachmentAuditedException();
		}
		
		// 删除附件
		attachmentService.removeById(attachment.getId());

		JSONObject json = getSuccessJsonTemplate();
		writeStream(json);
		return SUCCESS;
	}
	/**
	 * 获取最新的结算文件信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getLatestAttachment() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());
		Project projectEx = (Project) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_PROJECT);
		WhOwnerShipUtil.checkProject(project, projectEx);

		// 获取最新的附件信息
		Attachment attachmentCheck = attachmentService.getLatestByType(projectEx.getId(),
				Attachment.TYPE_SETTLEMENT_FILE);

		JSONObject json = getSuccessJsonTemplate();
		if (null != attachmentCheck) {
			json.put("hasAttachment", true);
			json.put("attachment", attachmentCheck);
		} else {
			json.put("hasAttachment", false);
		}
		writeStream(json);
		return SUCCESS;
	}
	/**
	 * 分页获取结算文件信息列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String pageAttachment() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());
		WhNull.checkPage(page);
		
		Project projectEx = (Project) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_PROJECT);
		WhOwnerShipUtil.checkProject(project, projectEx);

		// 查找附件信息
		Page<AttachmentDto> pageCheck = attachmentService.pageAuditByProjectType(project.getId(),
				Attachment.TYPE_SETTLEMENT_FILE, page);

		JSONObject json = getSuccessJsonTemplate();
		json.put("page", pageCheck);
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 获取结算项
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getItemById() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());
		WhNull.check(settlementItem);
		WhNull.check(settlementItem.getId());

		Project projectEx = (Project) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_PROJECT);
		WhOwnerShipUtil.checkProject(project, projectEx);

		/*
		 * 查找并校验
		 */
		SettlementItem settlementItemCheck = settlementItemService.getById(settlementItem.getId());
		WhOwnerShipUtil.checkProject(settlementItemCheck, projectEx);

		JSONObject json = getSuccessJsonTemplate();
		json.put("settlementItem", settlementItemCheck);
		writeStream(json);
		return SUCCESS;
	}
	
	/**
	 * 获取结算项
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getItemDetailById() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());
		WhNull.check(settlementItem);
		WhNull.check(settlementItem.getId());

		Project projectEx = (Project) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_PROJECT);
		WhOwnerShipUtil.checkProject(project, projectEx);

		/*
		 * 查找并校验
		 */
		SettlementItem2Dto settlementItemCheck = settlementItemService.getDetailById(settlementItem.getId());
		WhOwnerShipUtil.checkProject(settlementItemCheck, projectEx);

		JSONObject json = getSuccessJsonTemplate();
		json.put("settlementItem", settlementItemCheck);
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 获取结算项列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String pageItem() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());
		WhNull.checkPage(page);
		
		Project projectEx = (Project) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_PROJECT);
		WhOwnerShipUtil.checkProject(project, projectEx);

		// 获取结算项列表
		Page<SettlementItemDto> pageCheck = settlementItemService.pageByProjectId(projectEx.getId(), page);

		JSONObject json = getSuccessJsonTemplate();
		json.put("page", pageCheck);
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 增加新的结算项
	 * 
	 * @return
	 * @throws Exception
	 */
	public String addItem() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());
		WhNull.check(settlementItem);
		WhNull.check(settlementItem.getSubcontractorId());
		WhNull.check(settlementItem.getResourceImplementItemId());
		WhNull.check(settlementItem.getPurchaseItemId());
		WhNull.check(settlementItem.getYear());
		WhNull.check(settlementItem.getMonth());
		WhNull.check(settlementItem.getMoney());
		settlementItem.setMoney(settlementItem.getMoney().setScale(2, BigDecimal.ROUND_HALF_UP));
		if (settlementItem.getMoney().compareTo(BigDecimal.valueOf(0)) < 0) {
			throw new IllegalArgumentException();
		}

		Project projectEx = (Project) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_PROJECT);
		WhOwnerShipUtil.checkProject(project, projectEx);
		// 归档校验
		WhOwnerShipUtil.checkGoingProject(projectService.getById(projectEx.getId()));

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		WhOwnerShipUtil.checkStaff(staffEx);

		Target targetCheck = targetService.getByProjectId(projectEx.getId());
		WhOwnerShipUtil.checkTarget(targetCheck);

		// 校验月份
		WhYearMonthUtil.check(settlementItem.getYear(), settlementItem.getMonth(), targetCheck.getStartDate());

		/*
		 * 校验采购条目
		 */
		PurchaseItem purchaseItemCheck = purchaseItemService.getById(settlementItem.getPurchaseItemId());
		WhOwnerShipUtil.checkPurchaseItem(purchaseItemCheck);

		/*
		 * 校验资源落实项
		 */
		ResourceImplementItem resourceImplementItemCheck = resourceImplementItemService.getById(settlementItem
				.getResourceImplementItemId());
		WhOwnerShipUtil.checkProject(resourceImplementItemCheck, projectEx);

		/*
		 * 校验分包商
		 */
		Subcontractor subcontractorCheck = subcontractorService.getById(settlementItem.getSubcontractorId());
		WhOwnerShipUtil.checkStaffCompany(subcontractorCheck, staffEx);

		/*
		 * 校验该月份的资源落实项是否已经结算
		 */
		SettlementItem settlementItemExist = settlementItemService.getByResourceImplementYearMonth(
				settlementItem.getResourceImplementItemId(), settlementItem.getYear(), settlementItem.getMonth());
		if (null != settlementItemExist) {
			throw new WhSettlementItemExistException();
		}

		// 整理数据
		settlementItem.setCompanyId(projectEx.getCompanyId());
		settlementItem.setProjectId(projectEx.getId());
		settlementItem.setSubcontractorName(subcontractorCheck.getName());
		settlementItem.setPurchaseItemName(purchaseItemCheck.getName());

		// 添加条目
		settlementItemService.add(settlementItem);

		writeStream(getSuccessJsonTemplate());
		return SUCCESS;
	}

	/**
	 * 修改结算项
	 * 
	 * @return
	 * @throws Exception
	 */
	public String modifyItemById() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());
		WhNull.check(settlementItem);
		WhNull.check(settlementItem.getId());
		WhNull.check(settlementItem.getResourceImplementItemId());
		WhNull.check(settlementItem.getMoney());
		settlementItem.setMoney(settlementItem.getMoney().setScale(2, BigDecimal.ROUND_HALF_UP));
		if (settlementItem.getMoney().compareTo(BigDecimal.valueOf(0)) < 0) {
			throw new IllegalArgumentException();
		}
		
		Project projectEx = (Project) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_PROJECT);
		WhOwnerShipUtil.checkProject(project, projectEx);
		// 归档校验
		WhOwnerShipUtil.checkGoingProject(projectService.getById(projectEx.getId()));
		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		WhOwnerShipUtil.checkStaff(staffEx);
		
		Target targetCheck = targetService.getByProjectId(projectEx.getId());
		WhOwnerShipUtil.checkTarget(targetCheck);
		
		/*
		 * 检查当前结算项
		 */
		SettlementItem settlementItemCheck = settlementItemService.getById(settlementItem.getId());
		WhOwnerShipUtil.checkProject(settlementItemCheck, projectEx);
		
		/*
		 * 校验资源落实项
		 */
		ResourceImplementItem resourceImplementItemCheck = resourceImplementItemService.getById(settlementItem
				.getResourceImplementItemId());
		WhOwnerShipUtil.checkProject(resourceImplementItemCheck, projectEx);

		// 整理数据
		settlementItemCheck.setResourceImplementItemId(settlementItem.getResourceImplementItemId());
		settlementItemCheck.setMoney(settlementItem.getMoney());

		settlementItemService.modifyById(settlementItemCheck);

		writeStream(getSuccessJsonTemplate());
		return SUCCESS;
	}

	/**
	 * 删除结算项
	 * 
	 * @return
	 * @throws Exception
	 */
	public String removeItemById() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());
		WhNull.check(settlementItem);
		WhNull.check(settlementItem.getId());

		Project projectEx = (Project) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_PROJECT);
		WhOwnerShipUtil.checkProject(project, projectEx);
		// 归档校验
		WhOwnerShipUtil.checkGoingProject(projectService.getById(projectEx.getId()));

		/*
		 * 校验结算项
		 */
		SettlementItem settlementItemCheck = settlementItemService.getById(settlementItem.getId());
		WhOwnerShipUtil.checkProject(settlementItemCheck, projectEx);

		// 删除
		settlementItemService.removeById(settlementItem.getId());

		writeStream(getSuccessJsonTemplate());
		return SUCCESS;
	}

}
