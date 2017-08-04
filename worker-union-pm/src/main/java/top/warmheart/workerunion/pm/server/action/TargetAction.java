package top.warmheart.workerunion.pm.server.action;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.springframework.context.annotation.Scope;

import top.warmheart.server.util.WhDateUtil;
import top.warmheart.server.util.exception.WhNull;
import top.warmheart.workerunion.pm.server.action.ex.ActionJson;
import top.warmheart.workerunion.pm.server.constant.SessionKey;
import top.warmheart.workerunion.pm.server.util.WhOwnerShipUtil;
import top.warmheart.workerunion.server.dto.Staff3Dto;
import top.warmheart.workerunion.server.model.Attachment;
import top.warmheart.workerunion.server.model.Project;
import top.warmheart.workerunion.server.model.Target;
import top.warmheart.workerunion.server.model.TeamRole;
import top.warmheart.workerunion.server.service.AttachmentService;
import top.warmheart.workerunion.server.service.ProjectService;
import top.warmheart.workerunion.server.service.ProjectTeamService;
import top.warmheart.workerunion.server.service.TargetService;

import com.alibaba.fastjson.JSONObject;

@SuppressWarnings("serial")
@Scope("prototype")
public class TargetAction extends ActionJson {
	private Project project;
	private Attachment attachment;
	private AttachmentService attachmentService;
	private TargetService targetService;
	private ProjectTeamService projectTeamService;
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
	
	public TargetService getTargetService() {
		return targetService;
	}

	@Resource(name = "targetService")
	public void setTargetService(TargetService targetService) {
		this.targetService = targetService;
	}

	public ProjectTeamService getProjectTeamService() {
		return projectTeamService;
	}

	@Resource(name = "projectTeamService")
	public void setProjectTeamService(ProjectTeamService projectTeamService) {
		this.projectTeamService = projectTeamService;
	}
	public ProjectService getProjectService() {
		return projectService;
	}

	@Resource(name = "projectService")
	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}
	/**
	 * 获取唯一的目标协议书
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getUniqueAttachment() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());
		
		Project projectEx = (Project) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_PROJECT);
		WhOwnerShipUtil.checkProject(project, projectEx);
		
		// 查找附件信息
		Attachment attachmentCheck = attachmentService.getLatestByType(projectEx.getId(), Attachment.TYPE_AGREEMENT_OF_TARGET);

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
	 * 获取项目目标详情
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getDetail() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());

		/*
		 * 校验项目
		 */
		Project projectEx = (Project) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_PROJECT);
		WhOwnerShipUtil.checkProject(project, projectEx);
		
		// 获取目标信息
		Target targetCheck = targetService.getByProjectId(projectEx.getId());
		// 获取项目组项目经理信息
		Staff3Dto staff3DtoCheck = projectTeamService.getByProjectTeamType(projectEx.getId(), TeamRole.TYPE_PROJECT_MANAGER);
		if (null != staff3DtoCheck){
			staff3DtoCheck.setAge(WhDateUtil.getAge(staff3DtoCheck.getBirthday()));
		}
		
		JSONObject json = getSuccessJsonTemplate();
		if (null != targetCheck){
			json.put("hasTarget", true);
			json.put("target", targetCheck);
		}else{
			json.put("hasTarget", false);
		}
		if (null != staff3DtoCheck){
			json.put("hasProjectManager", true);
			json.put("projectManager", staff3DtoCheck);
		}else{
			json.put("hasProjectManager", false);
		}
		writeStream(json);
		return SUCCESS;
	}
}
