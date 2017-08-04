package top.warmheart.workerunion.fm.server.action;

import java.util.List;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.springframework.context.annotation.Scope;

import top.warmheart.server.util.exception.WhNull;
import top.warmheart.workerunion.fm.server.action.ex.ActionJson;
import top.warmheart.workerunion.fm.server.constant.SessionKey;
import top.warmheart.workerunion.fm.server.util.WhOwnerShipUtil;
import top.warmheart.workerunion.server.dto.AttachmentDto;
import top.warmheart.workerunion.server.exception.WhAttachmentAuditedException;
import top.warmheart.workerunion.server.exception.WhInvalidAttachmentException;
import top.warmheart.workerunion.server.model.Attachment;
import top.warmheart.workerunion.server.model.AttachmentAudit;
import top.warmheart.workerunion.server.model.Project;
import top.warmheart.workerunion.server.model.Staff;
import top.warmheart.workerunion.server.service.AttachmentService;
import top.warmheart.workerunion.server.service.ProjectService;

import com.alibaba.fastjson.JSONObject;

@SuppressWarnings("serial")
@Scope("prototype")
public class TechnicalProposalAction extends ActionJson {
	private Project project;
	private Attachment attachment;
	private AttachmentAudit attachmentAudit;
	private AttachmentService attachmentService;
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

	public Attachment getAttachment() {
		return attachment;
	}

	public void setAttachment(Attachment attachment) {
		this.attachment = attachment;
	}

	public AttachmentService getAttachmentService() {
		return attachmentService;
	}

	@Resource(name = "attachmentService")
	public void setAttachmentService(AttachmentService attachmentService) {
		this.attachmentService = attachmentService;
	}

	public ProjectService getProjectService() {
		return projectService;
	}

	@Resource(name = "projectService")
	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
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
		if (!Attachment.TYPE_TECHNICAL_PROPOSAL.equalsIgnoreCase(attachmentDtoCheck.getType())) {
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
		if (!Attachment.TYPE_TECHNICAL_PROPOSAL.equalsIgnoreCase(attachmentDtoCheck.getType())) {
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

	/**
	 * 获取技术方案审核信息列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String listAttachment() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		Project projectEx = projectService.getById(project.getId());
		WhOwnerShipUtil.checkStaffCompany(projectEx, staffEx);

		// 查找附件信息
		List<AttachmentDto> attachmentCheck = attachmentService.listAuditAttachment(project.getId(),
				Attachment.TYPE_TECHNICAL_PROPOSAL);

		JSONObject json = getSuccessJsonTemplate();
		json.put("attachments", attachmentCheck);
		writeStream(json);
		return SUCCESS;
	}
}
