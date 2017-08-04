package top.warmheart.workerunion.fm.server.action;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.springframework.context.annotation.Scope;

import top.warmheart.server.util.WhStringUtil;
import top.warmheart.server.util.dto.Page;
import top.warmheart.server.util.exception.WhNull;
import top.warmheart.workerunion.fm.server.action.ex.ActionJson;
import top.warmheart.workerunion.fm.server.constant.SessionKey;
import top.warmheart.workerunion.fm.server.util.WhOwnerShipUtil;
import top.warmheart.workerunion.server.dto.ContractDto;
import top.warmheart.workerunion.server.exception.WhContractExistException;
import top.warmheart.workerunion.server.model.Contract;
import top.warmheart.workerunion.server.model.ContractAttachment;
import top.warmheart.workerunion.server.model.Project;
import top.warmheart.workerunion.server.model.Staff;
import top.warmheart.workerunion.server.service.ContractService;
import top.warmheart.workerunion.server.service.ProjectService;

import com.alibaba.fastjson.JSONObject;

@SuppressWarnings("serial")
@Scope("prototype")
public class ContractAction extends ActionJson {
	private Project project;
	private Contract contract;
	private ContractDto contractDto;
	private Page<Void> page;
	private ContractAttachment contractAttachment;
	private ContractService contractService;
	private ProjectService projectService;

	
	public Page<Void> getPage() {
		return page;
	}

	public void setPage(Page<Void> page) {
		this.page = page;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Contract getContract() {
		return contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}

	public ContractDto getContractDto() {
		return contractDto;
	}

	public void setContractDto(ContractDto contractDto) {
		this.contractDto = contractDto;
	}

	public ContractAttachment getContractAttachment() {
		return contractAttachment;
	}

	public void setContractAttachment(ContractAttachment contractAttachment) {
		this.contractAttachment = contractAttachment;
	}

	public ContractService getContractService() {
		return contractService;
	}

	@Resource(name = "contractService")
	public void setContractService(ContractService contractService) {
		this.contractService = contractService;
	}

	public ProjectService getProjectService() {
		return projectService;
	}

	@Resource(name = "projectService")
	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}

	/**
	 * 获取指定项目简要合同列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String listSimpleItemByProjectId() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		Project projectEx = projectService.getById(project.getId());
		WhOwnerShipUtil.checkStaffCompany(projectEx, staffEx);

		// 获取指定项目简要合同列表
		List<Contract> contracts = contractService.listSimpleItemByProjectId(project.getId());

		JSONObject json = getSuccessJsonTemplate();
		json.put("contracts", contracts);
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 添加合同
	 * 
	 * @return
	 * @throws Exception
	 */
	public String add() throws Exception {
		/*
		 * 校验参数
		 */
		WhNull.check(contract);
		WhNull.checkTrimEmpty(contract.getName());
		WhNull.checkTrimEmpty(contract.getNum());
		WhNull.checkTrimEmpty(contract.getType());
		WhNull.checkTrimEmpty(contract.getPartA());
		WhNull.checkTrimEmpty(contract.getPartB());
		WhNull.check(contract.getSignDate());
		WhNull.check(contract.getMoney());
		WhNull.check(contract.getDutyDepartment());
		WhNull.check(contract.getContentAbstract());
		WhNull.check(contract.getProjectId());
		contract.setMoney(contract.getMoney().setScale(2, BigDecimal.ROUND_HALF_UP));
		contract.setName(WhStringUtil.trimAll(contract.getName()));
		contract.setNum(WhStringUtil.trimAll(contract.getNum()));
		contract.setType(WhStringUtil.trimAll(contract.getType()));
		contract.setPartA(WhStringUtil.trimAll(contract.getPartA()));
		contract.setPartB(WhStringUtil.trimAll(contract.getPartB()));
		contract.setDutyDepartment(WhStringUtil.trimAll(contract.getDutyDepartment()));

		/*
		 * 校验所属项目
		 */
		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		Project projectCheck = projectService.getById(contract.getProjectId());
		WhOwnerShipUtil.checkStaffCompany(projectCheck, staffEx);

		/*
		 * 校验合同编号
		 */
		Contract contractExist = contractService.getByCompanyAndNum(staffEx.getCompanyId(), contract.getNum());
		if (null != contractExist) {
			throw new WhContractExistException();
		}

		contract.setCompanyId(staffEx.getCompanyId());
		BigInteger id = contractService.add(contract);

		JSONObject json = getSuccessJsonTemplate();
		json.put("id", id);
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 删除合同
	 * 
	 * @return
	 * @throws Exception
	 */
	public String removeById() throws Exception {
		WhNull.check(contract);
		WhNull.check(contract.getId());
		
		/*
		 * 校验合同信息
		 */
		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		Contract contractCheck = contractService.getById(contract.getId());
		WhOwnerShipUtil.checkStaffCompany(contractCheck, staffEx);
		
		// 删除合同信息
		contractService.removeById(contract.getId());
		
		JSONObject json = getSuccessJsonTemplate();
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 获取合同详情
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getDetailById() throws Exception {
		WhNull.check(contract);
		WhNull.check(contract.getId());
		
		/*
		 * 校验合同信息
		 */
		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		ContractDto contractCheck = contractService.getDetailById(contract.getId());
		WhOwnerShipUtil.checkStaffCompany(contractCheck, staffEx);
		
		List<ContractAttachment> contractAttachments = contractService.listAttachmentByContractId(contract.getId());
		
		JSONObject json = getSuccessJsonTemplate();
		json.put("contract", contractCheck);
		json.put("contractAttachments", contractAttachments);
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 删除合同附件
	 * 
	 * @return
	 * @throws Exception
	 */
	public String removeAttachmentById() throws Exception {
		WhNull.check(contractAttachment);
		WhNull.check(contractAttachment.getId());
		
		/*
		 * 校验合同附件信息
		 */
		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		ContractAttachment contractAttachmentCheck = contractService.getAttachmentById(contractAttachment.getId());
		WhOwnerShipUtil.checkStaffCompany(contractAttachmentCheck, staffEx);
		
		// 删除合同附件
		contractService.removeAttachmentById(contractAttachment.getId());
		
		JSONObject json = getSuccessJsonTemplate();
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 修改合同
	 * 
	 * @return
	 * @throws Exception
	 */
	public String modifyById() throws Exception {
		/*
		 * 校验参数
		 */
		WhNull.check(contract);
		WhNull.check(contract.getId());
		WhNull.checkTrimEmpty(contract.getName());
		WhNull.checkTrimEmpty(contract.getType());
		WhNull.checkTrimEmpty(contract.getPartA());
		WhNull.checkTrimEmpty(contract.getPartB());
		WhNull.check(contract.getSignDate());
		WhNull.check(contract.getMoney());
		WhNull.check(contract.getDutyDepartment());
		WhNull.check(contract.getContentAbstract());
		contract.setMoney(contract.getMoney().setScale(2, BigDecimal.ROUND_HALF_UP));
		contract.setName(WhStringUtil.trimAll(contract.getName()));
		contract.setType(WhStringUtil.trimAll(contract.getType()));
		contract.setPartA(WhStringUtil.trimAll(contract.getPartA()));
		contract.setPartB(WhStringUtil.trimAll(contract.getPartB()));
		contract.setDutyDepartment(WhStringUtil.trimAll(contract.getDutyDepartment()));

		/*
		 * 校验合同信息
		 */
		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		Contract contractCheck = contractService.getById(contract.getId());
		WhOwnerShipUtil.checkStaffCompany(contractCheck, staffEx);
		
		contractCheck.setName(contract.getName());
		contractCheck.setType(contract.getType());
		contractCheck.setPartA(contract.getPartA());
		contractCheck.setPartB(contract.getPartB());
		contractCheck.setSignDate(contract.getSignDate());
		contractCheck.setMoney(contract.getMoney());
		contractCheck.setDutyDepartment(contract.getDutyDepartment());
		contractCheck.setContentAbstract(contract.getContentAbstract());
		
		// 修改
		contractService.modifyById(contractCheck);
		
		JSONObject json = getSuccessJsonTemplate();
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 模糊查询合同列表信息，分页
	 * 
	 * @return
	 * @throws Exception
	 */
	public String pageByFuzzy() throws Exception {
		WhNull.checkPage(page);
		WhNull.check(contractDto);
		WhNull.check(contractDto.getNum());
		WhNull.check(contractDto.getName());
		WhNull.check(contractDto.getProjectName());
		WhNull.check(contractDto.getProjectNum());
		WhNull.check(contractDto.getType());
		contractDto.setNum(WhStringUtil.trimAll(contractDto.getNum()));
		contractDto.setName(WhStringUtil.trimAll(contractDto.getName()));
		contractDto.setProjectName(WhStringUtil.trimAll(contractDto.getProjectName()));
		contractDto.setProjectNum(WhStringUtil.trimAll(contractDto.getProjectNum()));
		contractDto.setType(WhStringUtil.trimAll(contractDto.getType()));
		
		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		WhOwnerShipUtil.checkStaff(staffEx);
		
		// 模糊查询
		contractDto.setCompanyId(staffEx.getCompanyId());
		Page<ContractDto> pageCheck = contractService.pageByFuzzy(contractDto, page);
		
		JSONObject json = getSuccessJsonTemplate();
		json.put("page", pageCheck);
		writeStream(json);
		return SUCCESS;
	}
}
