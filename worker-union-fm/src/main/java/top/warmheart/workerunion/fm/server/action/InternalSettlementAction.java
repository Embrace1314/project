package top.warmheart.workerunion.fm.server.action;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.springframework.context.annotation.Scope;

import top.warmheart.server.util.dto.Page;
import top.warmheart.server.util.exception.WhNull;
import top.warmheart.workerunion.fm.server.action.ex.ActionJson;
import top.warmheart.workerunion.fm.server.constant.SessionKey;
import top.warmheart.workerunion.fm.server.util.WhOwnerShipUtil;
import top.warmheart.workerunion.server.dto.AttachmentDto;
import top.warmheart.workerunion.server.exception.WhInvalidAttachmentException;
import top.warmheart.workerunion.server.model.Attachment;
import top.warmheart.workerunion.server.model.InternalSettlement;
import top.warmheart.workerunion.server.model.Project;
import top.warmheart.workerunion.server.model.Staff;
import top.warmheart.workerunion.server.model.Target;
import top.warmheart.workerunion.server.service.AttachmentService;
import top.warmheart.workerunion.server.service.InternalSettlementService;
import top.warmheart.workerunion.server.service.ProjectService;
import top.warmheart.workerunion.server.service.TargetService;

import com.alibaba.fastjson.JSONObject;

@SuppressWarnings("serial")
@Scope("prototype")
public class InternalSettlementAction extends ActionJson {
	private Project project;
	private Attachment attachment;
	private InternalSettlement internalSettlement;
	private Page<Void> page;
	private AttachmentService attachmentService;
	private TargetService targetService;
	private InternalSettlementService internalSettlementService;
	private ProjectService projectService;

	public InternalSettlement getInternalSettlement() {
		return internalSettlement;
	}

	public void setInternalSettlement(InternalSettlement internalSettlement) {
		this.internalSettlement = internalSettlement;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Attachment getAttachment() {
		return attachment;
	}

	public void setAttachment(Attachment attachment) {
		this.attachment = attachment;
	}

	public Page<Void> getPage() {
		return page;
	}

	public void setPage(Page<Void> page) {
		this.page = page;
	}

	public AttachmentService getAttachmentService() {
		return attachmentService;
	}

	@Resource(name = "attachmentService")
	public void setAttachmentService(AttachmentService attachmentService) {
		this.attachmentService = attachmentService;
	}

	public TargetService getTargetService() {
		return targetService;
	}

	@Resource(name = "targetService")
	public void setTargetService(TargetService targetService) {
		this.targetService = targetService;
	}

	public InternalSettlementService getInternalSettlementService() {
		return internalSettlementService;
	}

	@Resource(name = "internalSettlementService")
	public void setInternalSettlementService(InternalSettlementService internalSettlementService) {
		this.internalSettlementService = internalSettlementService;
	}

	public ProjectService getProjectService() {
		return projectService;
	}

	@Resource(name = "projectService")
	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}

	/**
	 * 获取最新的内部结算文件信息
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
				Attachment.TYPE_INTERNAL_SETTLEMENT_FILE);

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
	 * 分页获取内部结算文件列表信息
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

		Page<Attachment> pageCheck = attachmentService.pageByProjectType(projectEx.getId(),
				Attachment.TYPE_INTERNAL_SETTLEMENT_FILE, page);

		JSONObject json = getSuccessJsonTemplate();
		json.put("page", pageCheck);
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 获取项目完成详情
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getDetail() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		Project projectEx = projectService.getById(project.getId());
		WhOwnerShipUtil.checkStaffCompany(projectEx, staffEx);

		// 获取项目目标及完成情况
		Target targetCheck = targetService.getByProjectId(project.getId());
		InternalSettlement internalSettlementCheck = internalSettlementService.getByProjectId(project.getId());

		JSONObject json = getSuccessJsonTemplate();
		if (null != targetCheck) {
			json.put("hasTarget", true);
			json.put("target", targetCheck);
		} else {
			json.put("hasTarget", false);
		}
		if (null != internalSettlementCheck) {
			json.put("hasInternalSettlement", true);
			json.put("internalSettlement", internalSettlementCheck);
		} else {
			json.put("hasInternalSettlement", false);
		}
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 删除内部结算文件
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
		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		Project projectEx = projectService.getById(project.getId());
		WhOwnerShipUtil.checkStaffCompany(projectEx, staffEx);
		// 归档校验
		WhOwnerShipUtil.checkGoingProject(projectEx);

		/*
		 * 校验附件
		 */
		AttachmentDto attachmentDtoCheck = attachmentService.getAuditById(attachment.getId());
		WhOwnerShipUtil.checkProject(attachmentDtoCheck, projectEx);
		if (!Attachment.TYPE_INTERNAL_SETTLEMENT_FILE.equalsIgnoreCase(attachmentDtoCheck.getType())) {
			throw new WhInvalidAttachmentException();
		}

		// 删除附件
		attachmentService.removeById(attachment.getId());

		JSONObject json = getSuccessJsonTemplate();
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 保存内部结算评估结果
	 * 
	 * @return
	 * @throws Exception
	 */
	public String saveAssessment() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());
		WhNull.checkTrimEmpty(internalSettlement.getQuality());
		WhNull.checkTrimEmpty(internalSettlement.getSafety());
		WhNull.check(internalSettlement.getCost());
		WhNull.check(internalSettlement.getDuration());
		WhNull.check(internalSettlement.getInternalCost());
		WhNull.check(internalSettlement.getExtraAward());
		internalSettlement.setCost(internalSettlement.getCost().setScale(2, BigDecimal.ROUND_HALF_UP));
		internalSettlement.setInternalCost(internalSettlement.getInternalCost().setScale(2, BigDecimal.ROUND_HALF_UP));
		internalSettlement.setExtraAward(internalSettlement.getExtraAward().setScale(2, BigDecimal.ROUND_HALF_UP));
		/*
		 * 校验项目
		 */
		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		Project projectEx = projectService.getById(project.getId());
		WhOwnerShipUtil.checkStaffCompany(projectEx, staffEx);
		// 归档校验
		WhOwnerShipUtil.checkGoingProject(projectEx);

		// 保存
		internalSettlement.setCompanyId(staffEx.getCompanyId());
		internalSettlement.setProjectId(project.getId());
		internalSettlementService.replace(internalSettlement);

		JSONObject json = getSuccessJsonTemplate();
		writeStream(json);
		return SUCCESS;
	}
}
