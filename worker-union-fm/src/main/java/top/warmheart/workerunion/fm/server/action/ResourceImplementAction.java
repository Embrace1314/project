package top.warmheart.workerunion.fm.server.action;

import java.util.List;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.springframework.context.annotation.Scope;

import top.warmheart.server.util.exception.WhNull;
import top.warmheart.workerunion.fm.server.action.ex.ActionJson;
import top.warmheart.workerunion.fm.server.constant.SessionKey;
import top.warmheart.workerunion.fm.server.util.WhOwnerShipUtil;
import top.warmheart.workerunion.server.dto.ResourceImplementItemDto;
import top.warmheart.workerunion.server.model.Project;
import top.warmheart.workerunion.server.model.ResourceImplementItem;
import top.warmheart.workerunion.server.model.Staff;
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

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		Project projectEx = projectService.getById(project.getId());
		WhOwnerShipUtil.checkStaffCompany(projectEx, staffEx);

		/*
		 * 查找并校验
		 */
		ResourceImplementItem resourceImplementItemCheck = resourceImplementItemService.getById(resourceImplementItem
				.getId());
		WhOwnerShipUtil.checkProject(resourceImplementItemCheck, projectEx);

		JSONObject json = getSuccessJsonTemplate();
		json.put("resourceImplementItem", resourceImplementItemCheck);
		if (null != resourceImplementItemCheck.getFundPlanItemId()) {
			json.put("hasFundPlanItem", true);
		} else {
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

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		Project projectEx = projectService.getById(project.getId());
		WhOwnerShipUtil.checkStaffCompany(projectEx, staffEx);

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

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		Project projectEx = projectService.getById(project.getId());
		WhOwnerShipUtil.checkStaffCompany(projectEx, staffEx);

		// 获取资源落实项列表
		List<ResourceImplementItem> resourceImplementItems = resourceImplementItemService
				.listSimpleByProjectId(projectEx.getId());

		JSONObject json = getSuccessJsonTemplate();
		json.put("resourceImplementItems", resourceImplementItems);
		writeStream(json);
		return SUCCESS;
	}

}
