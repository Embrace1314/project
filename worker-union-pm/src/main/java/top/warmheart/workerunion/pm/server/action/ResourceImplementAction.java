package top.warmheart.workerunion.pm.server.action;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.springframework.context.annotation.Scope;

import top.warmheart.server.util.WhStringUtil;
import top.warmheart.server.util.exception.WhNull;
import top.warmheart.workerunion.pm.server.action.ex.ActionJson;
import top.warmheart.workerunion.pm.server.constant.SessionKey;
import top.warmheart.workerunion.pm.server.util.WhOwnerShipUtil;
import top.warmheart.workerunion.server.dto.ResourceImplementItemDto;
import top.warmheart.workerunion.server.exception.WhResourceImplementItemLinkedException;
import top.warmheart.workerunion.server.model.Contract;
import top.warmheart.workerunion.server.model.FundPlanItem;
import top.warmheart.workerunion.server.model.Project;
import top.warmheart.workerunion.server.model.PurchaseItem;
import top.warmheart.workerunion.server.model.ResourceImplementItem;
import top.warmheart.workerunion.server.model.Staff;
import top.warmheart.workerunion.server.model.Subcontractor;
import top.warmheart.workerunion.server.service.ContractService;
import top.warmheart.workerunion.server.service.FundPlanItemService;
import top.warmheart.workerunion.server.service.ProjectService;
import top.warmheart.workerunion.server.service.PurchaseItemService;
import top.warmheart.workerunion.server.service.ResourceImplementItemService;
import top.warmheart.workerunion.server.service.SettlementItemService;
import top.warmheart.workerunion.server.service.SubcontractorService;

import com.alibaba.fastjson.JSONObject;

@SuppressWarnings("serial")
@Scope("prototype")
public class ResourceImplementAction extends ActionJson {
	private Project project;
	private ResourceImplementItem resourceImplementItem;
	private ResourceImplementItemService resourceImplementItemService;
	private PurchaseItemService purchaseItemService;
	private FundPlanItemService fundPlanItemService;
	private SubcontractorService subcontractorService;
	private ContractService contractService;
	private SettlementItemService settlementItemService;
	private ProjectService projectService;
	
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public ResourceImplementItem getResourceImplementItem() {
		return resourceImplementItem;
	}

	public void setResourceImplementItem(ResourceImplementItem resourceImplementItem) {
		this.resourceImplementItem = resourceImplementItem;
	}

	public ResourceImplementItemService getResourceImplementItemService() {
		return resourceImplementItemService;
	}

	@Resource(name = "resourceImplementItemService")
	public void setResourceImplementItemService(ResourceImplementItemService resourceImplementItemService) {
		this.resourceImplementItemService = resourceImplementItemService;
	}

	public PurchaseItemService getPurchaseItemService() {
		return purchaseItemService;
	}

	@Resource(name = "purchaseItemService")
	public void setPurchaseItemService(PurchaseItemService purchaseItemService) {
		this.purchaseItemService = purchaseItemService;
	}

	public FundPlanItemService getFundPlanItemService() {
		return fundPlanItemService;
	}

	@Resource(name = "fundPlanItemService")
	public void setFundPlanItemService(FundPlanItemService fundPlanItemService) {
		this.fundPlanItemService = fundPlanItemService;
	}

	public SubcontractorService getSubcontractorService() {
		return subcontractorService;
	}

	@Resource(name = "subcontractorService")
	public void setSubcontractorService(SubcontractorService subcontractorService) {
		this.subcontractorService = subcontractorService;
	}

	public ContractService getContractService() {
		return contractService;
	}

	@Resource(name = "contractService")
	public void setContractService(ContractService contractService) {
		this.contractService = contractService;
	}

	public SettlementItemService getSettlementItemService() {
		return settlementItemService;
	}

	@Resource(name = "settlementItemService")
	public void setSettlementItemService(SettlementItemService settlementItemService) {
		this.settlementItemService = settlementItemService;
	}
	public ProjectService getProjectService() {
		return projectService;
	}

	@Resource(name = "projectService")
	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}
	/**
	 * 获取资源落实项
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getItemById() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());
		WhNull.check(resourceImplementItem);
		WhNull.check(resourceImplementItem.getId());

		Project projectEx = (Project) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_PROJECT);
		WhOwnerShipUtil.checkProject(project, projectEx);

		/*
		 * 查找并校验
		 */
		ResourceImplementItem resourceImplementItemCheck = resourceImplementItemService.getById(resourceImplementItem
				.getId());
		WhOwnerShipUtil.checkProject(resourceImplementItemCheck, projectEx);

		JSONObject json = getSuccessJsonTemplate();
		json.put("resourceImplementItem", resourceImplementItemCheck);
		if (null!=resourceImplementItemCheck.getFundPlanItemId()){
			json.put("hasFundPlanItem", true);
		}else{
			json.put("hasFundPlanItem", false);
		}
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 获取资源落实项列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String listItem() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());

		Project projectEx = (Project) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_PROJECT);
		WhOwnerShipUtil.checkProject(project, projectEx);

		// 获取资源落实项列表
		List<ResourceImplementItemDto> resourceImplementItems = resourceImplementItemService.listByProjectId(projectEx
				.getId());
		System.out.println(resourceImplementItems);
		JSONObject json = getSuccessJsonTemplate();
		json.put("resourceImplementItems", resourceImplementItems);
		writeStream(json);
		return SUCCESS;
	}
	
	/**
	 * 获取简要资源落实项列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String listSimpleItem() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());

		Project projectEx = (Project) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_PROJECT);
		WhOwnerShipUtil.checkProject(project, projectEx);

		// 获取资源落实项列表
		List<ResourceImplementItem> resourceImplementItems = resourceImplementItemService.listSimpleByProjectId(projectEx
				.getId());

		JSONObject json = getSuccessJsonTemplate();
		json.put("resourceImplementItems", resourceImplementItems);
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 增加新的资源落实项
	 * 
	 * @return
	 * @throws Exception
	 */
	public String addItem() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());
		WhNull.check(resourceImplementItem);
		WhNull.check(resourceImplementItem.getSubcontractorId());
		WhNull.check(resourceImplementItem.getContractId());
		WhNull.check(resourceImplementItem.getPurchaseItemId());

		WhNull.checkTrimEmpty(resourceImplementItem.getName());
		WhNull.checkTrimEmpty(resourceImplementItem.getUnit());
		resourceImplementItem.setName(WhStringUtil.trimAll(resourceImplementItem.getName()));
		resourceImplementItem.setUnit(WhStringUtil.trimAll(resourceImplementItem.getUnit()));
		WhNull.check(resourceImplementItem.getPrice());
		WhNull.check(resourceImplementItem.getAmount());
		resourceImplementItem.setPrice(resourceImplementItem.getPrice().setScale(2, BigDecimal.ROUND_HALF_UP));
		resourceImplementItem.setAmount(resourceImplementItem.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP));
		if (resourceImplementItem.getPrice().compareTo(BigDecimal.valueOf(0)) < 0
				|| resourceImplementItem.getAmount().compareTo(BigDecimal.valueOf(0)) < 0) {
			throw new IllegalArgumentException();
		}

		Project projectEx = (Project) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_PROJECT);
		WhOwnerShipUtil.checkProject(project, projectEx);
		// 归档校验
		WhOwnerShipUtil.checkGoingProject(projectService.getById(projectEx.getId()));

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		WhOwnerShipUtil.checkStaff(staffEx);

		/*
		 * 校验采购条目
		 */
		PurchaseItem purchaseItemCheck = purchaseItemService.getById(resourceImplementItem.getPurchaseItemId());
		WhOwnerShipUtil.checkPurchaseItem(purchaseItemCheck);

		/*
		 * 校验资金计划项
		 */
		if (null != resourceImplementItem.getFundPlanItemId()) {
			FundPlanItem fundPlanItemCheck = fundPlanItemService.getById(resourceImplementItem.getFundPlanItemId());
			WhOwnerShipUtil.checkProject(fundPlanItemCheck, projectEx);
		}
		/*
		 * 校验分包商
		 */
		Subcontractor subcontractorCheck = subcontractorService.getById(resourceImplementItem.getSubcontractorId());
		WhOwnerShipUtil.checkStaffCompany(subcontractorCheck, staffEx);

		/*
		 * 校验合同
		 */
		Contract contractCheck = contractService.getById(resourceImplementItem.getContractId());
		WhOwnerShipUtil.checkProject(contractCheck, projectEx);

		resourceImplementItem.setCompanyId(projectEx.getCompanyId());
		resourceImplementItem.setProjectId(projectEx.getId());
		resourceImplementItem.setSubcontractorName(subcontractorCheck.getName());
		resourceImplementItem.setPurchaseItemName(purchaseItemCheck.getName());
		resourceImplementItem.setContractName(contractCheck.getName());
		resourceImplementItem.setContractNum(contractCheck.getNum());
		resourceImplementItem.setMoney(resourceImplementItem.getPrice().multiply(resourceImplementItem.getAmount()).setScale(2, BigDecimal.ROUND_HALF_UP));
		resourceImplementItemService.add(resourceImplementItem);

		writeStream(getSuccessJsonTemplate());
		return SUCCESS;
	}

	/**
	 * 修改资源落实项
	 * 
	 * @return
	 * @throws Exception
	 */
	public String modifyItemById() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());
		WhNull.check(resourceImplementItem);
		WhNull.check(resourceImplementItem.getId());
		WhNull.checkTrimEmpty(resourceImplementItem.getName());
		WhNull.checkTrimEmpty(resourceImplementItem.getUnit());
		resourceImplementItem.setName(WhStringUtil.trimAll(resourceImplementItem.getName()));
		resourceImplementItem.setUnit(WhStringUtil.trimAll(resourceImplementItem.getUnit()));
		WhNull.check(resourceImplementItem.getPrice());
		WhNull.check(resourceImplementItem.getAmount());
		if (resourceImplementItem.getPrice().compareTo(BigDecimal.valueOf(0)) < 0
				|| resourceImplementItem.getAmount().compareTo(BigDecimal.valueOf(0)) < 0) {
			throw new IllegalArgumentException();
		}

		Project projectEx = (Project) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_PROJECT);
		WhOwnerShipUtil.checkProject(project, projectEx);
		// 归档校验
		WhOwnerShipUtil.checkGoingProject(projectService.getById(projectEx.getId()));

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		WhOwnerShipUtil.checkStaff(staffEx);

		/*
		 * 校验资源落实项
		 */
		ResourceImplementItem resourceImplementItemCheck = resourceImplementItemService.getById(resourceImplementItem
				.getId());
		WhOwnerShipUtil.checkProject(resourceImplementItemCheck, projectEx);

		/*
		 * 校验资金计划项
		 */
		if (null != resourceImplementItem.getFundPlanItemId()) {
			FundPlanItem fundPlanItemCheck = fundPlanItemService.getById(resourceImplementItem.getFundPlanItemId());
			WhOwnerShipUtil.checkProject(fundPlanItemCheck, projectEx);
		}
		
		resourceImplementItemCheck.setName(resourceImplementItem.getName());
		resourceImplementItemCheck.setUnit(resourceImplementItem.getUnit());
		resourceImplementItemCheck.setPrice(resourceImplementItem.getPrice());
		resourceImplementItemCheck.setAmount(resourceImplementItem.getAmount());
		resourceImplementItemCheck.setMoney(resourceImplementItemCheck.getPrice().multiply(resourceImplementItemCheck.getAmount()));
		resourceImplementItemCheck.setFundPlanItemId(resourceImplementItem.getFundPlanItemId());
		
		resourceImplementItemService.modifyById(resourceImplementItemCheck);

		writeStream(getSuccessJsonTemplate());
		return SUCCESS;
	}

	/**
	 * 删除资源落实项
	 * 
	 * @return
	 * @throws Exception
	 */
	public String removeItemById() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());
		WhNull.check(resourceImplementItem);
		WhNull.check(resourceImplementItem.getId());

		Project projectEx = (Project) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_PROJECT);
		WhOwnerShipUtil.checkProject(project, projectEx);
		// 归档校验
		WhOwnerShipUtil.checkGoingProject(projectService.getById(projectEx.getId()));

		/*
		 * 校验资源落实项
		 */
		ResourceImplementItem resourceImplementItemCheck = resourceImplementItemService.getById(resourceImplementItem
				.getId());
		WhOwnerShipUtil.checkProject(resourceImplementItemCheck, projectEx);

		/*
		 * 检查关联的结算管理项
		 */
		if (!settlementItemService.listByResourceImplementItemId(resourceImplementItem.getId()).isEmpty()) {
			throw new WhResourceImplementItemLinkedException();
		}
		// 删除
		resourceImplementItemService.removeById(resourceImplementItem.getId());

		writeStream(getSuccessJsonTemplate());
		return SUCCESS;
	}
}
