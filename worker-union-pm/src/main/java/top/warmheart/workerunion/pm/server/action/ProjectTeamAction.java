package top.warmheart.workerunion.pm.server.action;

import java.util.List;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.springframework.context.annotation.Scope;

import top.warmheart.server.util.WhDateUtil;
import top.warmheart.server.util.exception.WhNull;
import top.warmheart.workerunion.pm.server.action.ex.ActionJson;
import top.warmheart.workerunion.pm.server.constant.SessionKey;
import top.warmheart.workerunion.pm.server.util.WhOwnerShipUtil;
import top.warmheart.workerunion.server.dto.Staff3Dto;
import top.warmheart.workerunion.server.exception.WhInvalidAttachmentException;
import top.warmheart.workerunion.server.exception.WhInvalidTeamRoleException;
import top.warmheart.workerunion.server.exception.WhProjectTeamExistException;
import top.warmheart.workerunion.server.model.Attachment;
import top.warmheart.workerunion.server.model.Project;
import top.warmheart.workerunion.server.model.ProjectTeam;
import top.warmheart.workerunion.server.model.Staff;
import top.warmheart.workerunion.server.model.TeamRole;
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
	private Attachment attachment;
	private AttachmentService attachmentService;
	private StaffService staffService;
	private ProjectTeamService projectTeamService;
	private TeamRoleService teamRoleService;
	private ProjectService projectService;

	public Attachment getAttachment() {
		return attachment;
	}

	public void setAttachment(Attachment attachment) {
		this.attachment = attachment;
	}

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
	 * 增加项目组成员
	 * 
	 * @return
	 * @throws Exception
	 */
	public String addProjectStaff() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());
		WhNull.check(staff);
		WhNull.check(staff.getId());
		WhNull.checkTrimEmpty(type);
		// 不能为项目经理
		if (!TeamRole.TYPE_LIST.contains(type) || TeamRole.TYPE_PROJECT_MANAGER.equals(type)) {
			throw new IllegalArgumentException();
		}

		/*
		 * 校验项目
		 */
		Project projectEx = (Project) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_PROJECT);
		WhOwnerShipUtil.checkProject(project, projectEx);
		// 归档校验
		WhOwnerShipUtil.checkGoingProject(projectService.getById(projectEx.getId()));

		/*
		 * 校验员工归属企业
		 */
		Staff staffCheck = staffService.getById(staff.getId());
		WhOwnerShipUtil.checkProjectCompany(staffCheck, projectEx);

		/*
		 * 校验项目成员类型
		 */
		TeamRole teamRoleCheck = teamRoleService.getByCompanyType(projectEx.getCompanyId(), type);
		WhOwnerShipUtil.checkTeamRole(teamRoleCheck);

		/*
		 * 校验重复的分配
		 */
		if (projectTeamService.contains(project.getId(), staff.getId())
				|| projectTeamService.roleContains(project.getId(), teamRoleCheck.getId())) {
			throw new WhProjectTeamExistException();
		}

		// 增加数据信息
		projectTeamService.add(project.getId(), staff.getId(), teamRoleCheck.getId());

		JSONObject json = getSuccessJsonTemplate();
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 删除项目组成员
	 * 
	 * @return
	 * @throws Exception
	 */
	public String removeProjectStaff() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());
		WhNull.check(staff);
		WhNull.check(staff.getId());

		/*
		 * 校验项目
		 */
		Project projectEx = (Project) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_PROJECT);
		WhOwnerShipUtil.checkProject(project, projectEx);
		// 归档校验
		WhOwnerShipUtil.checkGoingProject(projectService.getById(projectEx.getId()));

		/*
		 * 校验项目角色信息
		 */
		ProjectTeam projectTeamCheck = projectTeamService.getByProjectStaff(project.getId(), staff.getId());
		WhOwnerShipUtil.checkProjectTeam(projectTeamCheck);

		/*
		 * 校验不能删除项目经理
		 */
		TeamRole teamRoleCheck = teamRoleService.getById(projectTeamCheck.getTeamRoleId());
		if (null == teamRoleCheck || TeamRole.TYPE_PROJECT_MANAGER.equals(teamRoleCheck.getType())) {
			throw new WhInvalidTeamRoleException();
		}

		// 删除该项目组成员
		projectTeamService.deleteByProjectStaff(project.getId(), staff.getId());

		JSONObject json = getSuccessJsonTemplate();
		writeStream(json);
		return SUCCESS;
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

		Project projectEx = (Project) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_PROJECT);
		WhOwnerShipUtil.checkProject(project, projectEx);

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
	 * 删除团队搭建附件
	 * 
	 * @return
	 * @throws Exception
	 */
	public String removeAttachment() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());
		WhNull.check(attachment);
		WhNull.check(attachment.getId());

		Project projectEx = (Project) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_PROJECT);
		WhOwnerShipUtil.checkProject(project, projectEx);
		// 归档校验
		WhOwnerShipUtil.checkGoingProject(projectService.getById(projectEx.getId()));

		Attachment attachmentCheck = attachmentService.getById(attachment.getId());
		WhOwnerShipUtil.checkProject(attachmentCheck, projectEx);
		if (!Attachment.TYPE_PROJECT_TEAM.equalsIgnoreCase(attachmentCheck.getType())) {
			throw new WhInvalidAttachmentException();
		}

		attachmentService.removeById(attachment.getId());

		JSONObject json = getSuccessJsonTemplate();
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
		Project projectEx = (Project) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_PROJECT);
		WhOwnerShipUtil.checkProject(project, projectEx);

		List<Attachment> attachments = attachmentService.listByProjectType(projectEx.getId(),
				Attachment.TYPE_PROJECT_TEAM);

		JSONObject json = getSuccessJsonTemplate();
		json.put("attachments", attachments);
		writeStream(json);
		return SUCCESS;
	}
}
