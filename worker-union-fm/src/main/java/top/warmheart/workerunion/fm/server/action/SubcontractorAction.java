package top.warmheart.workerunion.fm.server.action;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.springframework.context.annotation.Scope;

import top.warmheart.server.util.dto.Page;
import top.warmheart.server.util.exception.WhNull;
import top.warmheart.workerunion.fm.server.action.ex.ActionJson;
import top.warmheart.workerunion.fm.server.constant.SessionKey;
import top.warmheart.workerunion.fm.server.util.WhOwnerShipUtil;
import top.warmheart.workerunion.server.dto.SubcontractorDto;
import top.warmheart.workerunion.server.exception.WhSubcontractorExistException;
import top.warmheart.workerunion.server.model.Project;
import top.warmheart.workerunion.server.model.Staff;
import top.warmheart.workerunion.server.model.Subcontractor;
import top.warmheart.workerunion.server.model.SubcontractorAttachment;
import top.warmheart.workerunion.server.model.SubcontractorGrade;
import top.warmheart.workerunion.server.model.SubcontractorQualification;
import top.warmheart.workerunion.server.service.ProjectService;
import top.warmheart.workerunion.server.service.SubcontractorService;

import com.alibaba.fastjson.JSONObject;

@SuppressWarnings("serial")
@Scope("prototype")
public class SubcontractorAction extends ActionJson {
	private Project project;
	private Page<Void> page;
	private Subcontractor subcontractor;
	private SubcontractorDto subcontractorDto;
	private SubcontractorService subcontractorService;
	private ProjectService projectService;
	private List<SubcontractorQualification> subcontractorQualifications;
	private SubcontractorAttachment subcontractorAttachment;
	
	
	public Page<Void> getPage() {
		return page;
	}

	public void setPage(Page<Void> page) {
		this.page = page;
	}

	public SubcontractorDto getSubcontractorDto() {
		return subcontractorDto;
	}

	public void setSubcontractorDto(SubcontractorDto subcontractorDto) {
		this.subcontractorDto = subcontractorDto;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Subcontractor getSubcontractor() {
		return subcontractor;
	}

	public void setSubcontractor(Subcontractor subcontractor) {
		this.subcontractor = subcontractor;
	}

	public List<SubcontractorQualification> getSubcontractorQualifications() {
		return subcontractorQualifications;
	}

	public void setSubcontractorQualifications(List<SubcontractorQualification> subcontractorQualifications) {
		this.subcontractorQualifications = subcontractorQualifications;
	}

	public SubcontractorAttachment getSubcontractorAttachment() {
		return subcontractorAttachment;
	}

	public void setSubcontractorAttachment(SubcontractorAttachment subcontractorAttachment) {
		this.subcontractorAttachment = subcontractorAttachment;
	}

	public SubcontractorService getSubcontractorService() {
		return subcontractorService;
	}

	@Resource(name = "subcontractorService")
	public void setSubcontractorService(SubcontractorService subcontractorService) {
		this.subcontractorService = subcontractorService;
	}
	public ProjectService getProjectService() {
		return projectService;
	}

	@Resource(name = "projectService")
	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}
	
	
	/**
	 * 获取简要分包商列表
	 * @return
	 * @throws Exception
	 */
	public String listSimpleItem() throws Exception {
		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		WhOwnerShipUtil.checkStaff(staffEx);
		List<Subcontractor> subcontractors = subcontractorService.listSimpleItemByCompanyId(staffEx.getCompanyId());
		
		JSONObject json = getSuccessJsonTemplate();
		json.put("subcontractors", subcontractors);
		writeStream(json);
		return SUCCESS;
	}
	
	/**
	 * 模糊查询获取分包商信息列表，分页
	 * 
	 * @return
	 * @throws Exception
	 */
	public String pageByFuzzy() throws Exception {
		WhNull.check(subcontractorDto);
		WhNull.check(subcontractorDto.getName());
		WhNull.check(subcontractorDto.getNum());
		WhNull.checkPage(page);
		
		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		WhOwnerShipUtil.checkStaff(staffEx);
		subcontractorDto.setCompanyId(staffEx.getCompanyId());
		
		Page<SubcontractorDto>pageCheck = subcontractorService.pageByFuzzy(subcontractorDto, page);
		
		JSONObject json = getSuccessJsonTemplate();
		json.put("page", pageCheck);
		writeStream(json);
		return SUCCESS;

	}

	/**
	 * 添加分包商信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String add() throws Exception {
		WhNull.check(subcontractor);
		WhNull.checkTrimEmpty(subcontractor.getName());
		WhNull.checkTrimEmpty(subcontractor.getNum());
		WhNull.check(subcontractor.getSubcontractorGradeId());
		WhNull.check(subcontractor.getValidDate());
		WhNull.check(subcontractor.getContactPerson());
		WhNull.check(subcontractor.getContactPhone());
		if (null == subcontractorQualifications){
			subcontractorQualifications = new ArrayList<SubcontractorQualification>();
		}
		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		WhOwnerShipUtil.checkStaff(staffEx);

		/*
		 * 校验分包商级别
		 */
		SubcontractorGrade subcontractorGradeCheck = subcontractorService.getGradeById(subcontractor.getSubcontractorGradeId());
		WhOwnerShipUtil.checkSubcontractorGrade(subcontractorGradeCheck);

		/*
		 * 校验资质
		 */
		for (SubcontractorQualification subcontractorQualification : subcontractorQualifications) {
			SubcontractorQualification subcontractorQualificationCheck = subcontractorService
					.getQualificationById(subcontractorQualification.getId());
			WhOwnerShipUtil.checkSubcontractorQualification(subcontractorQualificationCheck);
		}

		/*
		 * 校验编号
		 */
		Subcontractor subcontractorExist = subcontractorService.getByCompanyAndNum(staffEx.getCompanyId(), subcontractor.getNum());
		if (null != subcontractorExist) {
			throw new WhSubcontractorExistException();
		}
		subcontractor.setCompanyId(staffEx.getCompanyId());
		subcontractor.setSubcontractorGradeName(subcontractorGradeCheck.getName());

		// 增加分包商信息
		BigInteger id = subcontractorService.add(subcontractor, subcontractorQualifications);

		JSONObject json = getSuccessJsonTemplate();
		json.put("id", id);
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 删除分包商信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String removeById() throws Exception {
		WhNull.check(subcontractor);
		WhNull.check(subcontractor.getId());

		/*
		 * 校验分包商
		 */
		Subcontractor subcontractorCheck = subcontractorService.getById(subcontractor.getId());
		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		WhOwnerShipUtil.checkStaffCompany(subcontractorCheck, staffEx);

		// 删除分包商信息
		subcontractorService.removeById(subcontractor.getId());

		JSONObject json = getSuccessJsonTemplate();
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 修改分包商信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String modifyById() throws Exception {
		WhNull.check(subcontractor);
		WhNull.check(subcontractor.getId());
		WhNull.checkTrimEmpty(subcontractor.getName());
		WhNull.check(subcontractor.getSubcontractorGradeId());
		WhNull.check(subcontractor.getValidDate());
		WhNull.check(subcontractor.getContactPerson());
		WhNull.check(subcontractor.getContactPhone());
		if (null == subcontractorQualifications){
			subcontractorQualifications = new ArrayList<SubcontractorQualification>();
		}
		
		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		WhOwnerShipUtil.checkStaff(staffEx);
		
		/*
		 * 校验分包商
		 */
		Subcontractor subcontractorCheck = subcontractorService.getById(subcontractor.getId());
		WhOwnerShipUtil.checkStaffCompany(subcontractorCheck, staffEx);

		/*
		 * 校验分包商级别
		 */
		SubcontractorGrade subcontractorGradeCheck = subcontractorService.getGradeById(subcontractor.getSubcontractorGradeId());
		WhOwnerShipUtil.checkSubcontractorGrade(subcontractorGradeCheck);

		/*
		 * 校验资质
		 */
		for (SubcontractorQualification subcontractorQualification : subcontractorQualifications) {
			SubcontractorQualification subcontractorQualificationCheck = subcontractorService
					.getQualificationById(subcontractorQualification.getId());
			WhOwnerShipUtil.checkSubcontractorQualification(subcontractorQualificationCheck);
		}

		subcontractorCheck.setName(subcontractor.getName());
		subcontractorCheck.setSubcontractorGradeId(subcontractor.getSubcontractorGradeId());
		subcontractorCheck.setSubcontractorGradeName(subcontractorGradeCheck.getName());
		subcontractorCheck.setValidDate(subcontractor.getValidDate());
		subcontractorCheck.setContactPerson(subcontractor.getContactPerson());
		subcontractorCheck.setContactPhone(subcontractor.getContactPhone());

		// 增加分包商信息
		subcontractorService.modifyById(subcontractorCheck, subcontractorQualifications);

		JSONObject json = getSuccessJsonTemplate();
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 获取分包商信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getDetailById() throws Exception {
		WhNull.check(subcontractor);
		WhNull.check(subcontractor.getId());

		/*
		 * 获取分包商
		 */
		Subcontractor subcontractorCheck = subcontractorService.getById(subcontractor.getId());
		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		WhOwnerShipUtil.checkStaffCompany(subcontractorCheck, staffEx);
		
		/*
		 * 获取分包商资质列表
		 */
		List<SubcontractorQualification> subcontractorQualifications = subcontractorService.listQualificationBySubcontractorId(subcontractor.getId());
		
		/*
		 * 获取分包商信息附件
		 */
		List<SubcontractorAttachment> subcontractorAttachments = subcontractorService.listAttachmentBySubcontractorId(subcontractor.getId());
		
		JSONObject json = getSuccessJsonTemplate();
		json.put("subcontractor", subcontractorCheck);
		json.put("subcontractorQualifications", subcontractorQualifications);
		json.put("subcontractorAttachments", subcontractorAttachments);
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 删除分包商信息附件文件
	 * 
	 * @return
	 * @throws Exception
	 */
	public String removeAttachmentById() throws Exception {
		WhNull.check(subcontractorAttachment);
		WhNull.check(subcontractorAttachment.getId());
		/*
		 * 获取分包商附件信息
		 */
		SubcontractorAttachment subcontractorAttachmentCheck = subcontractorService.getAttachmentById(subcontractorAttachment.getId());
		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		WhOwnerShipUtil.checkStaffCompany(subcontractorAttachmentCheck, staffEx);
		
		subcontractorService.removeAttachmentById(subcontractorAttachment.getId());
		
		JSONObject json = getSuccessJsonTemplate();
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 获取企业下的分包商级别列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String listGrade() throws Exception {
		List<SubcontractorGrade> subcontractorGrades = subcontractorService.listGrade();
		
		JSONObject json = getSuccessJsonTemplate();
		json.put("subcontractorGrades", subcontractorGrades);
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 获取企业下的分包商资质列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String listQualification() throws Exception {
		List<SubcontractorQualification> subcontractorQualifications = subcontractorService.listQualification();
		
		JSONObject json = getSuccessJsonTemplate();
		json.put("subcontractorQualifications", subcontractorQualifications);
		writeStream(json);
		return SUCCESS;
	}

}
