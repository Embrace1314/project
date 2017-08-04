package top.warmheart.workerunion.pm.server.action;

import java.util.List;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.springframework.context.annotation.Scope;

import com.alibaba.fastjson.JSONObject;

import top.warmheart.server.util.exception.WhNull;
import top.warmheart.workerunion.pm.server.action.ex.ActionJson;
import top.warmheart.workerunion.pm.server.constant.SessionKey;
import top.warmheart.workerunion.pm.server.util.WhOwnerShipUtil;
import top.warmheart.workerunion.server.dto.AttachmentDto;
import top.warmheart.workerunion.server.exception.WhAttachmentAuditedException;
import top.warmheart.workerunion.server.exception.WhInvalidAttachmentException;
import top.warmheart.workerunion.server.model.Attachment;
import top.warmheart.workerunion.server.model.AttachmentAudit;
import top.warmheart.workerunion.server.model.Project;
import top.warmheart.workerunion.server.service.AttachmentService;
import top.warmheart.workerunion.server.service.ProjectService;

@SuppressWarnings("serial")
@Scope("prototype")
public class FinalReportAction extends ActionJson {
	private Project project;
	private Attachment attachment;
	private AttachmentService attachmentService;
	private ProjectService projectService;
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
	 * 删除总结报告表
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
		if (!Attachment.TYPE_FINAL_REPORT.equalsIgnoreCase(attachmentDtoCheck.getType())) {
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
	 * 获取总结报告审核信息列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String listAttachment() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());

		Project projectEx = (Project) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_PROJECT);
		WhOwnerShipUtil.checkProject(project, projectEx);

		// 查找附件信息
		List<AttachmentDto> attachmentCheck = attachmentService.listAuditAttachment(project.getId(), Attachment.TYPE_FINAL_REPORT);

		JSONObject json = getSuccessJsonTemplate();
		json.put("attachments", attachmentCheck);
		writeStream(json);
		return SUCCESS;
	}
}
