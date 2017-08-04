package top.warmheart.workerunion.pm.server.action;

import java.util.List;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.springframework.context.annotation.Scope;

import com.alibaba.fastjson.JSONObject;

import top.warmheart.server.util.WhDateUtil;
import top.warmheart.server.util.exception.WhNull;
import top.warmheart.workerunion.pm.server.action.ex.ActionJson;
import top.warmheart.workerunion.pm.server.constant.SessionKey;
import top.warmheart.workerunion.pm.server.util.WhOwnerShipUtil;
import top.warmheart.workerunion.server.dto.Staff2Dto;
import top.warmheart.workerunion.server.model.Attachment;
import top.warmheart.workerunion.server.model.Project;
import top.warmheart.workerunion.server.service.AttachmentService;
import top.warmheart.workerunion.server.service.ProjectApplicationService;
import top.warmheart.workerunion.server.service.ProjectService;

@SuppressWarnings("serial")
@Scope("prototype")
public class ProjectApplicationAction extends ActionJson {
	private Project project;
	private Attachment attachment;
	private AttachmentService attachmentService;
	private ProjectApplicationService projectApplicationService;
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

		Project projectEx = (Project) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_PROJECT);
		WhOwnerShipUtil.checkProject(project, projectEx);

		// 查找合同备案表CONTRACT_RECORD
		Attachment attachmentContractRecordCheck = attachmentService.getLatestByType(projectEx.getId(), Attachment.TYPE_CONTRACT_RECORD);
		// 查找质监通知书QUALITY_INSPECT_NOTICE
		Attachment attachmentQualityInspectNoticeCheck = attachmentService.getLatestByType(projectEx.getId(), Attachment.TYPE_QUALITY_INSPECT_NOTICE);
		// 查找安监登记表SAFETY_SUPERVISION_FORM
		Attachment attachmentSafetySupervisionFormCheck = attachmentService.getLatestByType(projectEx.getId(), Attachment.TYPE_SAFETY_SUPERVISION_FORM);
		// 查找施工许可证CONSTRUCTION_PERMIT
		Attachment attachmentConstructionPermitCheck = attachmentService.getLatestByType(projectEx.getId(), Attachment.TYPE_CONSTRUCTION_PERMIT);

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

		Project projectEx = (Project) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_PROJECT);
		WhOwnerShipUtil.checkProject(project, projectEx);

		List<Staff2Dto> list = projectApplicationService.listStaffByProjectId(project.getId());
		for(Staff2Dto staff2Dto : list){
			staff2Dto.setAge(WhDateUtil.getAge(staff2Dto.getBirthday()));
		}
		JSONObject json = getSuccessJsonTemplate();
		json.put("staff", list);
		writeStream(json);
		return SUCCESS;
	}
}
