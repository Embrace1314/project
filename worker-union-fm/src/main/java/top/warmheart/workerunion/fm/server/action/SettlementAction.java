package top.warmheart.workerunion.fm.server.action;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.springframework.context.annotation.Scope;

import top.warmheart.server.util.dto.Page;
import top.warmheart.server.util.exception.WhNull;
import top.warmheart.workerunion.fm.server.action.ex.ActionJson;
import top.warmheart.workerunion.fm.server.constant.SessionKey;
import top.warmheart.workerunion.fm.server.util.WhOwnerShipUtil;
import top.warmheart.workerunion.server.dto.AttachmentDto;
import top.warmheart.workerunion.server.dto.SettlementItem2Dto;
import top.warmheart.workerunion.server.dto.SettlementItemDto;
import top.warmheart.workerunion.server.exception.WhAttachmentAuditedException;
import top.warmheart.workerunion.server.exception.WhInvalidAttachmentException;
import top.warmheart.workerunion.server.model.Attachment;
import top.warmheart.workerunion.server.model.AttachmentAudit;
import top.warmheart.workerunion.server.model.Project;
import top.warmheart.workerunion.server.model.SettlementItem;
import top.warmheart.workerunion.server.model.Staff;
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
	private AttachmentAudit attachmentAudit;
	private Page<Void> page;
	private AttachmentService attachmentService;
	private SettlementItem settlementItem;
	private SettlementItemService settlementItemService;
	private TargetService targetService;
	private SubcontractorService subcontractorService;
	private PurchaseItemService purchaseItemService;
	private ResourceImplementItemService resourceImplementItemService;
	private ProjectService projectService;

	public AttachmentAudit getAttachmentAudit() {
		return attachmentAudit;
	}

	public void setAttachmentAudit(AttachmentAudit attachmentAudit) {
		this.attachmentAudit = attachmentAudit;
	}

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
	 * 获取最新的结算文件信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getLatestAttachment() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		Project projectEx = projectService.getById(project.getId());
		WhOwnerShipUtil.checkStaffCompany(projectEx, staffEx);

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

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		Project projectEx = projectService.getById(project.getId());
		WhOwnerShipUtil.checkStaffCompany(projectEx, staffEx);

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

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		Project projectEx = projectService.getById(project.getId());
		WhOwnerShipUtil.checkStaffCompany(projectEx, staffEx);

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

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		Project projectEx = projectService.getById(project.getId());
		WhOwnerShipUtil.checkStaffCompany(projectEx, staffEx);

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

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		Project projectEx = projectService.getById(project.getId());
		WhOwnerShipUtil.checkStaffCompany(projectEx, staffEx);

		// 获取结算项列表
		Page<SettlementItemDto> pageCheck = settlementItemService.pageByProjectId(projectEx.getId(), page);

		JSONObject json = getSuccessJsonTemplate();
		json.put("page", pageCheck);
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 通过附件的审核
	 * 
	 * @return
	 * @throws Exception
	 */
	public String passAttachment() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());
		WhNull.check(attachmentAudit);
		WhNull.check(attachmentAudit.getAttachmentId());
		WhNull.check(attachmentAudit.getOpinion());

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		Project projectEx = projectService.getById(project.getId());
		WhOwnerShipUtil.checkStaffCompany(projectEx, staffEx);
		// 归档校验
		WhOwnerShipUtil.checkGoingProject(projectEx);

		/*
		 * 校验附件
		 */
		AttachmentDto attachmentDtoCheck = attachmentService.getAuditById(attachmentAudit.getAttachmentId());
		WhOwnerShipUtil.checkProject(attachmentDtoCheck, projectEx);
		if (!Attachment.TYPE_SETTLEMENT_FILE.equalsIgnoreCase(attachmentDtoCheck.getType())) {
			throw new WhInvalidAttachmentException();
		}
		if (!AttachmentAudit.STATUS_UNDETERMINED.equalsIgnoreCase(attachmentDtoCheck.getAuditStatus())) {
			throw new WhAttachmentAuditedException();
		}

		attachmentAudit.setStatus(AttachmentAudit.STATUS_PASS);
		attachmentAudit.setCompanyId(staffEx.getCompanyId());
		attachmentAudit.setProjectId(project.getId());

		attachmentService.replaceAttachmentAudit(attachmentAudit);

		JSONObject json = getSuccessJsonTemplate();
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 驳回附件的审核
	 * 
	 * @return
	 * @throws Exception
	 */
	public String failAttachment() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());
		WhNull.check(attachmentAudit);
		WhNull.check(attachmentAudit.getAttachmentId());
		WhNull.check(attachmentAudit.getOpinion());

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		Project projectEx = projectService.getById(project.getId());
		WhOwnerShipUtil.checkStaffCompany(projectEx, staffEx);
		// 归档校验
		WhOwnerShipUtil.checkGoingProject(projectEx);

		/*
		 * 校验附件
		 */
		AttachmentDto attachmentDtoCheck = attachmentService.getAuditById(attachmentAudit.getAttachmentId());
		WhOwnerShipUtil.checkProject(attachmentDtoCheck, projectEx);
		if (!Attachment.TYPE_SETTLEMENT_FILE.equalsIgnoreCase(attachmentDtoCheck.getType())) {
			throw new WhInvalidAttachmentException();
		}
		if (!AttachmentAudit.STATUS_UNDETERMINED.equalsIgnoreCase(attachmentDtoCheck.getAuditStatus())) {
			throw new WhAttachmentAuditedException();
		}

		attachmentAudit.setStatus(AttachmentAudit.STATUS_FAIL);
		attachmentAudit.setCompanyId(staffEx.getCompanyId());
		attachmentAudit.setProjectId(project.getId());

		// 设置审核信息
		attachmentService.replaceAttachmentAudit(attachmentAudit);

		JSONObject json = getSuccessJsonTemplate();
		writeStream(json);
		return SUCCESS;
	}

}
