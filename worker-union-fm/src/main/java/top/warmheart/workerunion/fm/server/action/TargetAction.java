package top.warmheart.workerunion.fm.server.action;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.springframework.context.annotation.Scope;

import top.warmheart.server.util.WhDateUtil;
import top.warmheart.server.util.WhStringUtil;
import top.warmheart.server.util.exception.WhNull;
import top.warmheart.workerunion.fm.server.action.ex.ActionJson;
import top.warmheart.workerunion.fm.server.constant.SessionKey;
import top.warmheart.workerunion.fm.server.util.WhOwnerShipUtil;
import top.warmheart.workerunion.server.dto.Staff3Dto;
import top.warmheart.workerunion.server.exception.WhInvalidAttachmentException;
import top.warmheart.workerunion.server.exception.WhInvalidTeamRoleException;
import top.warmheart.workerunion.server.exception.WhProjectTeamExistException;
import top.warmheart.workerunion.server.model.Attachment;
import top.warmheart.workerunion.server.model.Project;
import top.warmheart.workerunion.server.model.ProjectTeam;
import top.warmheart.workerunion.server.model.Staff;
import top.warmheart.workerunion.server.model.Target;
import top.warmheart.workerunion.server.model.TeamRole;
import top.warmheart.workerunion.server.service.AttachmentService;
import top.warmheart.workerunion.server.service.ProjectService;
import top.warmheart.workerunion.server.service.ProjectTeamService;
import top.warmheart.workerunion.server.service.StaffService;
import top.warmheart.workerunion.server.service.TargetService;
import top.warmheart.workerunion.server.service.TeamRoleService;

import com.alibaba.fastjson.JSONObject;

@SuppressWarnings("serial")
@Scope("prototype")
public class TargetAction extends ActionJson {
	private Project project;
	private Staff staff;
	private Attachment attachment;
	private Target target;
	private AttachmentService attachmentService;
	private TargetService targetService;
	private ProjectTeamService projectTeamService;
	private ProjectService projectService;
	private StaffService staffService;
	private TeamRoleService teamRoleService;

	public Target getTarget() {
		return target;
	}

	public void setTarget(Target target) {
		this.target = target;
	}

	public Staff getStaff() {
		return staff;
	}

	public void setStaff(Staff staff) {
		this.staff = staff;
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

	public StaffService getStaffService() {
		return staffService;
	}

	@Resource(name = "staffService")
	public void setStaffService(StaffService staffService) {
		this.staffService = staffService;
	}

	public TeamRoleService getTeamRoleService() {
		return teamRoleService;
	}

	@Resource(name = "teamRoleService")
	public void setTeamRoleService(TeamRoleService teamRoleService) {
		this.teamRoleService = teamRoleService;
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

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		Project projectEx = projectService.getById(project.getId());
		WhOwnerShipUtil.checkStaffCompany(projectEx, staffEx);

		// 查找附件信息
		Attachment attachmentCheck = attachmentService.getLatestByType(projectEx.getId(),
				Attachment.TYPE_AGREEMENT_OF_TARGET);

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
	 * 删除目标协议书
	 * 
	 * @return
	 * @throws Exception
	 */
	public String removeUniqueAttachment() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());
		WhNull.check(attachment);
		WhNull.check(attachment.getId());

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		Project projectEx = projectService.getById(project.getId());
		WhOwnerShipUtil.checkStaffCompany(projectEx, staffEx);
		// 归档校验
		WhOwnerShipUtil.checkGoingProject(projectEx);

		/*
		 * 校验附件信息
		 */
		Attachment attachmentCheck = attachmentService.getById(attachment.getId());
		WhOwnerShipUtil.checkProject(attachmentCheck, projectEx);

		if (!attachmentCheck.getType().equalsIgnoreCase(Attachment.TYPE_AGREEMENT_OF_TARGET)) {
			throw new WhInvalidAttachmentException();
		}

		attachmentService.removeById(attachmentCheck.getId());

		writeStream(getSuccessJsonTemplate());
		return SUCCESS;
	}

	/**
	 * 新增项目经理
	 * 
	 * @return
	 * @throws Exception
	 */
	public String addProjectManager() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());
		WhNull.check(staff);
		WhNull.check(staff.getId());

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		Project projectEx = projectService.getById(project.getId());
		WhOwnerShipUtil.checkStaffCompany(projectEx, staffEx);
		// 归档校验
		WhOwnerShipUtil.checkGoingProject(projectEx);

		/*
		 * 校验人员信息
		 */
		Staff staffCheck = staffService.getById(staff.getId());
		WhOwnerShipUtil.checkStaffCompany(staffCheck, staffEx);

		TeamRole teamRoleProjectManager = teamRoleService.getByCompanyType(staffEx.getCompanyId(),
				TeamRole.TYPE_PROJECT_MANAGER);
		WhOwnerShipUtil.checkTeamRole(teamRoleProjectManager);

		/*
		 * 已存在该角色或该人员已分配角色
		 */
		if (projectTeamService.contains(project.getId(), staff.getId())
				|| projectTeamService.roleContains(project.getId(), teamRoleProjectManager.getId())) {
			throw new WhProjectTeamExistException();
		}

		// 增加新成员
		projectTeamService.add(project.getId(), staff.getId(), teamRoleProjectManager.getId());

		writeStream(getSuccessJsonTemplate());
		return SUCCESS;
	}

	/**
	 * 删除项目经理
	 * 
	 * @return
	 * @throws Exception
	 */
	public String removeProjectManager() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());
		WhNull.check(staff);
		WhNull.check(staff.getId());

		/*
		 * 校验项目
		 */
		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		Project projectEx = projectService.getById(project.getId());
		WhOwnerShipUtil.checkStaffCompany(projectEx, staffEx);
		// 归档校验
		WhOwnerShipUtil.checkGoingProject(projectEx);

		/*
		 * 校验项目角色信息
		 */
		ProjectTeam projectTeamCheck = projectTeamService.getByProjectStaff(project.getId(), staff.getId());
		WhOwnerShipUtil.checkProjectTeam(projectTeamCheck);

		/*
		 * 校验不能删除项目经理
		 */
		TeamRole teamRoleCheck = teamRoleService.getById(projectTeamCheck.getTeamRoleId());
		if (null == teamRoleCheck || !TeamRole.TYPE_PROJECT_MANAGER.equals(teamRoleCheck.getType())) {
			throw new WhInvalidTeamRoleException();
		}

		// 删除该项目组成员
		projectTeamService.deleteByProjectStaff(project.getId(), staff.getId());

		JSONObject json = getSuccessJsonTemplate();
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
		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		Project projectEx = projectService.getById(project.getId());
		WhOwnerShipUtil.checkStaffCompany(projectEx, staffEx);

		// 获取目标信息
		Target targetCheck = targetService.getByProjectId(projectEx.getId());
		// 获取项目组项目经理信息
		Staff3Dto staff3DtoCheck = projectTeamService.getByProjectTeamType(projectEx.getId(),
				TeamRole.TYPE_PROJECT_MANAGER);
		if (null != staff3DtoCheck) {
			staff3DtoCheck.setAge(WhDateUtil.getAge(staff3DtoCheck.getBirthday()));
		}

		JSONObject json = getSuccessJsonTemplate();
		if (null != targetCheck) {
			json.put("hasTarget", true);
			json.put("target", targetCheck);
		} else {
			json.put("hasTarget", false);
		}
		if (null != staff3DtoCheck) {
			json.put("hasProjectManager", true);
			json.put("projectManager", staff3DtoCheck);
		} else {
			json.put("hasProjectManager", false);
		}
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 保存项目目标详情
	 * 
	 * @return
	 * @throws Exception
	 */
	public String save() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());
		WhNull.check(target);
		WhNull.check(target.getDuration());
		WhNull.check(target.getStartDate());
		WhNull.check(target.getEndDate());
		WhNull.checkEmpty(target.getQuality());
		WhNull.checkEmpty(target.getSafety());
		WhNull.check(target.getCost());
		target.setCost(target.getCost().setScale(2, BigDecimal.ROUND_HALF_UP));
		target.setQuality(WhStringUtil.trimAll(target.getQuality()));
		target.setSafety(WhStringUtil.trimAll(target.getSafety()));
		if (target.getCost().compareTo(BigDecimal.valueOf(0)) < 0 || target.getDuration() < 0) {
			throw new IllegalArgumentException();
		}
		if(target.getEndDate().before(target.getStartDate())){
			throw new IllegalArgumentException();
		}

		/*
		 * 校验项目
		 */
		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		Project projectEx = projectService.getById(project.getId());
		WhOwnerShipUtil.checkStaffCompany(projectEx, staffEx);

		target.setProjectId(project.getId());
		target.setCompanyId(staffEx.getCompanyId());
		target.setMemo("");
		// 保存目标信息
		targetService.replace(target);

		JSONObject json = getSuccessJsonTemplate();
		writeStream(json);
		return SUCCESS;
	}
}
