package top.warmheart.workerunion.pm.server.action;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.context.annotation.Scope;

import top.warmheart.server.util.dto.Page;
import top.warmheart.server.util.exception.WhNull;
import top.warmheart.workerunion.pm.server.action.ex.ActionJson;
import top.warmheart.workerunion.pm.server.constant.SessionKey;
import top.warmheart.workerunion.pm.server.security.BasicRealm;
import top.warmheart.workerunion.pm.server.util.WhOwnerShipUtil;
import top.warmheart.workerunion.server.model.Attachment;
import top.warmheart.workerunion.server.model.Project;
import top.warmheart.workerunion.server.model.Staff;
import top.warmheart.workerunion.server.service.AttachmentService;
import top.warmheart.workerunion.server.service.ProjectService;

import com.alibaba.fastjson.JSONObject;

@SuppressWarnings("serial")
@Scope("prototype")
public class ProjectAction extends ActionJson {
	/** 分页参数 */
	private Page<Void> page;
	/** 项目状态 */
	private String status;
	/** 项目立项年份 */
	private Date year;
	/** 项目信息 */
	private Project project;
	private ProjectService projectService;
	private AttachmentService attachmentService;

	public Page<Void> getPage() {
		return page;
	}

	public void setPage(Page<Void> page) {
		this.page = page;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getYear() {
		return year;
	}

	public void setYear(Date year) {
		this.year = year;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
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
	 * 获取分页项目列表信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String pageAll() throws Exception {
		// 校验参数
		WhNull.checkPage(page);

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		WhOwnerShipUtil.checkStaff(staffEx);

		// 进行查找
		Page<Project> pageCheck = projectService.pageAllEx(staffEx.getId(), staffEx.getCompanyId(), page);

		JSONObject json = getSuccessJsonTemplate();
		json.put("page", pageCheck);
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 获取未归档的项目列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String listUnarchivedProject() throws Exception {
		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		WhOwnerShipUtil.checkStaff(staffEx);

		// 进行查找
		List<Project> projects = projectService.listSimpleByFileStatus(staffEx.getCompanyId(),
				Project.FILE_STATUS_GOING);
		JSONObject json = getSuccessJsonTemplate();
		json.put("projects", projects);
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 
	 * 根据项目状态获取分页项目列表信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String pageByStatus() throws Exception {
		// 校验参数
		WhNull.checkPage(page);
		WhNull.checkTrimEmpty(status);
		if (!Project.STATUS_LIST.contains(status)) {
			throw new IllegalArgumentException("status参数非法");
		}

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		WhOwnerShipUtil.checkStaff(staffEx);

		// 进行查找
		Page<Project> pageCheck = projectService.pageByStatusEx(staffEx.getId(), staffEx.getCompanyId(), status, page);

		JSONObject json = getSuccessJsonTemplate();
		json.put("page", pageCheck);
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 
	 * 根据项目立项年份获取分页项目列表信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String pageByYear() throws Exception {
		// 校验参数
		WhNull.checkPage(page);
		WhNull.check(year);

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		WhOwnerShipUtil.checkStaff(staffEx);

		// 进行查找
		Page<Project> pageCheck = projectService.pageByYearEx(staffEx.getId(), staffEx.getCompanyId(), year, page);

		JSONObject json = getSuccessJsonTemplate();
		json.put("page", pageCheck);
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 
	 * 根据项目状态及立项年份获取分页项目列表信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String pageByStatusAndYear() throws Exception {
		// 校验参数
		WhNull.checkPage(page);
		WhNull.check(year);
		WhNull.checkTrimEmpty(status);
		if (!Project.STATUS_LIST.contains(status)) {
			throw new IllegalArgumentException("status参数非法");
		}

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		WhOwnerShipUtil.checkStaff(staffEx);

		// 进行查找
		Page<Project> pageCheck = projectService.pageByStatusAndYearEx(staffEx.getId(), staffEx.getCompanyId(), status,
				year, page);

		JSONObject json = getSuccessJsonTemplate();
		json.put("page", pageCheck);
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 登录
	 * 
	 * @return
	 * @throws Exception
	 */
	public String login() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());

		Subject currentProject = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(BasicRealm.LOGIN_PROJECT + ":" + project.getId(), "");

		// 项目登录
		currentProject.login(token);

		Project projectEx = (Project) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_PROJECT);
		WhOwnerShipUtil.checkProject(projectEx);

		// 校验成功
		JSONObject json = getSuccessJsonTemplate();
		json.put("project", projectEx);
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 
	 * 根据项目ID获取项目信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getDetailById() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());

		Project projectEx = (Project) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_PROJECT);
		WhOwnerShipUtil.checkProject(projectEx);
		// 校验
		WhOwnerShipUtil.checkProject(project, projectEx);

		/*
		 * 查找中标通知书附件信息
		 */
		Attachment attachmentCheck = attachmentService.getLatestByType(projectEx.getId(),
				Attachment.TYPE_LETTER_OF_ACCEPTANCE);

		JSONObject json = getSuccessJsonTemplate();
		json.put("project", projectEx);
		json.put("attachment", attachmentCheck);
		writeStream(json);
		return SUCCESS;
	}
}
