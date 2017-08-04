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
import top.warmheart.workerunion.server.dto.Staff2Dto;
import top.warmheart.workerunion.server.exception.WhInvalidAttachmentException;
import top.warmheart.workerunion.server.exception.WhProjectApplicationExistException;
import top.warmheart.workerunion.server.model.Attachment;
import top.warmheart.workerunion.server.model.Project;
import top.warmheart.workerunion.server.model.ProjectApplication;
import top.warmheart.workerunion.server.model.Staff;
import top.warmheart.workerunion.server.service.AttachmentService;
import top.warmheart.workerunion.server.service.ProjectApplicationService;
import top.warmheart.workerunion.server.service.ProjectService;
import top.warmheart.workerunion.server.service.StaffService;

import com.alibaba.fastjson.JSONObject;

@SuppressWarnings("serial")
@Scope("prototype")
public class ProjectApplicationAction extends ActionJson {
	private Project project;
	private Staff staff;
	private String type;
	private Attachment attachment;
	private AttachmentService attachmentService;
	private ProjectApplicationService projectApplicationService;
	private ProjectService projectService;
	private StaffService staffService;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public ProjectApplicationService getProjectApplicationService() {
		return projectApplicationService;
	}

	@Resource(name = "projectApplicationService")
	public void setProjectApplicationService(ProjectApplicationService projectApplicationService) {
		this.projectApplicationService = projectApplicationService;
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

	/**
	 * 获取附件信息列表
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

		// 查找合同备案表CONTRACT_RECORD
		Attachment attachmentContractRecordCheck = attachmentService.getLatestByType(projectEx.getId(),
				Attachment.TYPE_CONTRACT_RECORD);
		// 查找质监通知书QUALITY_INSPECT_NOTICE
		Attachment attachmentQualityInspectNoticeCheck = attachmentService.getLatestByType(projectEx.getId(),
				Attachment.TYPE_QUALITY_INSPECT_NOTICE);
		// 查找安监登记表SAFETY_SUPERVISION_FORM
		Attachment attachmentSafetySupervisionFormCheck = attachmentService.getLatestByType(projectEx.getId(),
				Attachment.TYPE_SAFETY_SUPERVISION_FORM);
		// 查找施工许可证CONSTRUCTION_PERMIT
		Attachment attachmentConstructionPermitCheck = attachmentService.getLatestByType(projectEx.getId(),
				Attachment.TYPE_CONSTRUCTION_PERMIT);

		JSONObject json = getSuccessJsonTemplate();
		if (null != attachmentContractRecordCheck) {
			json.put("hasAttachmentContractRecord", true);
			json.put("attachmentContractRecord", attachmentContractRecordCheck);
		} else {
			json.put("hasAttachmentContractRecord", false);
		}
		if (null != attachmentQualityInspectNoticeCheck) {
			json.put("hasAttachmentQualityInspectNotice", true);
			json.put("attachmentQualityInspectNotice", attachmentQualityInspectNoticeCheck);
		} else {
			json.put("hasAttachmentQualityInspectNotice", false);
		}
		if (null != attachmentSafetySupervisionFormCheck) {
			json.put("hasAttachmentSafetySupervisionForm", true);
			json.put("attachmentSafetySupervisionForm", attachmentSafetySupervisionFormCheck);
		} else {
			json.put("hasAttachmentSafetySupervisionForm", false);
		}
		if (null != attachmentConstructionPermitCheck) {
			json.put("hasAttachmentConstructionPermit", true);
			json.put("attachmentConstructionPermit", attachmentConstructionPermitCheck);
		} else {
			json.put("hasAttachmentConstructionPermit", false);
		}
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 获取项目报建人员列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String listStaff() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		Project projectEx = projectService.getById(project.getId());
		WhOwnerShipUtil.checkStaffCompany(projectEx, staffEx);

		List<Staff2Dto> list = projectApplicationService.listStaffByProjectId(project.getId());
		for (Staff2Dto staff2Dto : list) {
			staff2Dto.setAge(WhDateUtil.getAge(staff2Dto.getBirthday()));
		}
		JSONObject json = getSuccessJsonTemplate();
		json.put("staff", list);
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 增加项目报建成员
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
		if (!ProjectApplication.TYPE_LIST.contains(type)) {
			throw new IllegalArgumentException();
		}

		/*
		 * 校验项目
		 */
		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		Project projectEx = projectService.getById(project.getId());
		WhOwnerShipUtil.checkStaffCompany(projectEx, staffEx);
		// 归档校验
		WhOwnerShipUtil.checkGoingProject(projectEx);

		/*
		 * 校验员工归属企业
		 */
		Staff staffCheck = staffService.getById(staff.getId());
		WhOwnerShipUtil.checkProjectCompany(staffCheck, projectEx);

		/*
		 * 校验重复的分配
		 */
		if (!projectApplicationService.getValidByStaff(staff.getId()).isEmpty()
				|| null != projectApplicationService.getByProjectRole(project.getId(), type)) {
			throw new WhProjectApplicationExistException();
		}

		// 增加数据信息
		projectApplicationService.add(project.getId(), staff.getId(), type);

		JSONObject json = getSuccessJsonTemplate();
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 删除项目报建成员
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
		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		Project projectEx = projectService.getById(project.getId());
		WhOwnerShipUtil.checkStaffCompany(projectEx, staffEx);
		// 归档校验
		WhOwnerShipUtil.checkGoingProject(projectEx);

		/*
		 * 校验项目角色信息
		 */
		ProjectApplication projectApplicationCheck = projectApplicationService.getByProjectStaff(project.getId(),
				staff.getId());
		WhOwnerShipUtil.checkProjectApplication(projectApplicationCheck);

		// 删除该项目报建成员
		projectApplicationService.deleteByProjectStaff(project.getId(), staff.getId());

		JSONObject json = getSuccessJsonTemplate();
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 删除合同备案表凭证
	 * 
	 * @return
	 * @throws Exception
	 */
	public String removeContractRecordAttachment() throws Exception {
		return removeUniqueAttachment(Attachment.TYPE_CONTRACT_RECORD);
	}

	/**
	 * 删除质检通知书凭证
	 * 
	 * @return
	 * @throws Exception
	 */
	public String removeQualityInspectNoticeAttachmentn() throws Exception {
		return removeUniqueAttachment(Attachment.TYPE_QUALITY_INSPECT_NOTICE);
	}

	/**
	 * 删除安监登记表凭证
	 * 
	 * @return
	 * @throws Exception
	 */
	public String removeSafetySupervisionFormAttachment() throws Exception {
		return removeUniqueAttachment(Attachment.TYPE_SAFETY_SUPERVISION_FORM);
	}

	/**
	 * 删除施工许可证凭证
	 * 
	 * @return
	 * @throws Exception
	 */
	public String removeConstructionPermitAttachment() throws Exception {
		return removeUniqueAttachment(Attachment.TYPE_CONSTRUCTION_PERMIT);
	}

	/**
	 * 删除附件
	 * 
	 * @return
	 * @throws Exception
	 */
	private String removeUniqueAttachment(String type) throws Exception {
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

		if (!attachmentCheck.getType().equalsIgnoreCase(type)) {
			throw new WhInvalidAttachmentException();
		}

		attachmentService.removeById(attachmentCheck.getId());

		writeStream(getSuccessJsonTemplate());
		return SUCCESS;
	}

}
