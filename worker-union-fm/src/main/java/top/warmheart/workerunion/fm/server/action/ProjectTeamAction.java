package top.warmheart.workerunion.fm.server.action;

import java.util.List;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.springframework.context.annotation.Scope;

import top.warmheart.server.util.WhDateUtil;
import top.warmheart.server.util.exception.WhNull;
import top.warmheart.workerunion.fm.server.action.ex.ActionJson;
import top.warmheart.workerunion.fm.server.constant.SessionKey;
import top.warmheart.workerunion.fm.server.util.WhOwnerShipUtil;
import top.warmheart.workerunion.server.dto.Staff3Dto;
import top.warmheart.workerunion.server.model.Attachment;
import top.warmheart.workerunion.server.model.Project;
import top.warmheart.workerunion.server.model.Staff;
import top.warmheart.workerunion.server.service.AttachmentService;
import top.warmheart.workerunion.server.service.ProjectService;
import top.warmheart.workerunion.server.service.ProjectTeamService;
import top.warmheart.workerunion.server.service.StaffService;
import top.warmheart.workerunion.server.service.TeamRoleService;

import com.alibaba.fastjson.JSONObject;

@SuppressWarnings("serial")
@Scope("prototype")
public class ProjectTeamAction extends ActionJson {
	private Project project;
	private Staff staff;
	/** 项目组成员所属类型 */
	private String type;
	private AttachmentService attachmentService;
	private StaffService staffService;
	private ProjectTeamService projectTeamService;
	private TeamRoleService teamRoleService;
	private ProjectService projectService;

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Staff getStaff() {
		return staff;
	}

	public void setStaff(Staff staff) {
		this.staff = staff;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public StaffService getStaffService() {
		return staffService;
	}

	@Resource(name = "staffService")
	public void setStaffService(StaffService staffService) {
		this.staffService = staffService;
	}

	public ProjectTeamService getProjectTeamService() {
		return projectTeamService;
	}

	@Resource(name = "projectTeamService")
	public void setProjectTeamService(ProjectTeamService projectTeamService) {
		this.projectTeamService = projectTeamService;
	}

	public TeamRoleService getTeamRoleService() {
		return teamRoleService;
	}

	@Resource(name = "teamRoleService")
	public void setTeamRoleService(TeamRoleService teamRoleService) {
		this.teamRoleService = teamRoleService;
	}

	public ProjectService getProjectService() {
		return projectService;
	}

	@Resource(name = "projectService")
	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}

	public AttachmentService getAttachmentService() {
		return attachmentService;
	}

	@Resource(name = "attachmentService")
	public void setAttachmentService(AttachmentService attachmentService) {
		this.attachmentService = attachmentService;
	}

	/**
	 * 获取项目组人员列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String listProjectStaff() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		Project projectEx = projectService.getById(project.getId());
		WhOwnerShipUtil.checkStaffCompany(projectEx, staffEx);

		List<Staff3Dto> list = projectTeamService.listStaffByProjectId(project.getId());
		for (Staff3Dto staff3Dto : list) {
			staff3Dto.setAge(WhDateUtil.getAge(staff3Dto.getBirthday()));
		}
		JSONObject json = getSuccessJsonTemplate();
		json.put("staff", list);
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 
	 * 获取团队搭建附件列表信息
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

		List<Attachment> attachments = attachmentService.listByProjectType(projectEx.getId(),
				Attachment.TYPE_PROJECT_TEAM);

		JSONObject json = getSuccessJsonTemplate();
		json.put("attachments", attachments);
		writeStream(json);
		return SUCCESS;
	}
}
