package top.warmheart.workerunion.fm.server.action;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.springframework.context.annotation.Scope;

import top.warmheart.server.util.WhStringUtil;
import top.warmheart.server.util.exception.WhNull;
import top.warmheart.workerunion.fm.server.action.ex.ActionJson;
import top.warmheart.workerunion.fm.server.constant.SessionKey;
import top.warmheart.workerunion.fm.server.util.WhOwnerShipUtil;
import top.warmheart.workerunion.server.exception.WhInvalidAttachmentException;
import top.warmheart.workerunion.server.model.Attachment;
import top.warmheart.workerunion.server.model.CostAnalysisItem;
import top.warmheart.workerunion.server.model.Project;
import top.warmheart.workerunion.server.model.Staff;
import top.warmheart.workerunion.server.service.AttachmentService;
import top.warmheart.workerunion.server.service.CostAnalysisItemService;
import top.warmheart.workerunion.server.service.ProjectService;

import com.alibaba.fastjson.JSONObject;

@SuppressWarnings("serial")
@Scope("prototype")
public class CostAnalysisAction extends ActionJson {
	private Project project;
	private Attachment attachment;
	private CostAnalysisItem costAnalysisItem;
	private AttachmentService attachmentService;
	private ProjectService projectService;
	private CostAnalysisItemService costAnalysisItemService;

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

	public CostAnalysisItem getCostAnalysisItem() {
		return costAnalysisItem;
	}

	public void setCostAnalysisItem(CostAnalysisItem costAnalysisItem) {
		this.costAnalysisItem = costAnalysisItem;
	}

	public AttachmentService getAttachmentService() {
		return attachmentService;
	}

	@Resource(name = "attachmentService")
	public void setAttachmentService(AttachmentService attachmentService) {
		this.attachmentService = attachmentService;
	}

	public ProjectService getProjectService() {
		return projectService;
	}

	@Resource(name = "projectService")
	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}

	public CostAnalysisItemService getCostAnalysisItemService() {
		return costAnalysisItemService;
	}

	@Resource(name = "costAnalysisItemService")
	public void setCostAnalysisItemService(CostAnalysisItemService costAnalysisItemService) {
		this.costAnalysisItemService = costAnalysisItemService;
	}

	/**
	 * 获取成本分析项
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getItemById() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());
		WhNull.check(costAnalysisItem);
		WhNull.check(costAnalysisItem.getId());

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		Project projectEx = projectService.getById(project.getId());
		WhOwnerShipUtil.checkStaffCompany(projectEx, staffEx);

		/*
		 * 查找并校验
		 */
		CostAnalysisItem costAnalysisItemCheck = costAnalysisItemService.getById(costAnalysisItem.getId());
		WhOwnerShipUtil.checkProject(costAnalysisItemCheck, projectEx);

		JSONObject json = getSuccessJsonTemplate();
		json.put("costAnalysisItem", costAnalysisItemCheck);
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 获取唯一的成本分析表
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
		Attachment attachmentCheck = attachmentService.getLatestByType(project.getId(), Attachment.TYPE_COST_ANALYSIS);

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
	 * 获取成本分析详情
	 * 
	 * @return
	 * @throws Exception
	 */
	public String acquireDetail() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		Project projectEx = projectService.getById(project.getId());
		WhOwnerShipUtil.checkStaffCompany(projectEx, staffEx);

		// 获取成本项列表
		List<CostAnalysisItem> costAnalysisItems = costAnalysisItemService.listByProjectId(projectEx.getId());

		/*
		 * 根据具体成本项 计算合计金额
		 */
		// 直接成本总额
		BigDecimal directCostSum = BigDecimal.valueOf(0);
		// 间接成本总额
		BigDecimal indirectCostSum = BigDecimal.valueOf(0);
		// 指定分包成本总额
		BigDecimal subcontractCostSum = BigDecimal.valueOf(0);
		// 税额总计
		BigDecimal taxCostSum = BigDecimal.valueOf(0);
		for (CostAnalysisItem costAnalysisItem : costAnalysisItems) {
			switch (costAnalysisItem.getType()) {
			case CostAnalysisItem.TYPE_DIRECT_COST:
				directCostSum = directCostSum.add(costAnalysisItem.getCost());
				break;
			case CostAnalysisItem.TYPE_INDIRECT_COST:
				indirectCostSum = indirectCostSum.add(costAnalysisItem.getCost());
				break;
			case CostAnalysisItem.TYPE_SUBCONTRACT_COST:
				subcontractCostSum = subcontractCostSum.add(costAnalysisItem.getCost());
				break;
			case CostAnalysisItem.TYPE_TAX_COST:
				taxCostSum = taxCostSum.add(costAnalysisItem.getCost());
				break;
			default:
				break;
			}
		}
		// 税前总额
		BigDecimal pretaxSum = directCostSum.add(indirectCostSum).add(subcontractCostSum);
		// 成本总额
		BigDecimal costSum = pretaxSum.add(taxCostSum);
		// 预计利润
		BigDecimal profitSum = projectEx.getBidPrice().subtract(costSum);

		JSONObject json = getSuccessJsonTemplate();
		json.put("directCostSum", directCostSum);
		json.put("indirectCostSum", indirectCostSum);
		json.put("subcontractCostSum", subcontractCostSum);
		json.put("taxCostSum", taxCostSum);
		json.put("pretaxSum", pretaxSum);
		json.put("costSum", costSum);
		json.put("profitSum", profitSum);

		json.put("costAnalysisItems", costAnalysisItems);
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 删除成本分析表
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

		if (!attachmentCheck.getType().equalsIgnoreCase(Attachment.TYPE_COST_ANALYSIS)) {
			throw new WhInvalidAttachmentException();
		}

		attachmentService.removeById(attachmentCheck.getId());

		writeStream(getSuccessJsonTemplate());
		return SUCCESS;
	}

	/**
	 * 增加新的成本分析项
	 * 
	 * @return
	 * @throws Exception
	 */
	public String addItem() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());
		WhNull.check(costAnalysisItem);
		WhNull.checkTrimEmpty(costAnalysisItem.getType());
		WhNull.checkTrimEmpty(costAnalysisItem.getName());
		WhNull.check(costAnalysisItem.getMemo());
		WhNull.check(costAnalysisItem.getCost());
		costAnalysisItem.setCost(costAnalysisItem.getCost().setScale(2, BigDecimal.ROUND_HALF_UP));
		if (costAnalysisItem.getCost().compareTo(BigDecimal.valueOf(0)) < 0) {
			throw new IllegalArgumentException();
		}

		/*
		 * 校验TYPE合法性
		 */
		if (!CostAnalysisItem.TYPE_LIST.contains(costAnalysisItem.getType())) {
			throw new IllegalArgumentException();
		}

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		Project projectEx = projectService.getById(project.getId());
		WhOwnerShipUtil.checkStaffCompany(projectEx, staffEx);
		// 归档校验
		WhOwnerShipUtil.checkGoingProject(projectEx);

		costAnalysisItem.setProjectId(projectEx.getId());
		costAnalysisItem.setCompanyId(projectEx.getCompanyId());
		costAnalysisItemService.add(costAnalysisItem);

		writeStream(getSuccessJsonTemplate());
		return SUCCESS;
	}

	/**
	 * 修改成本分析项
	 * 
	 * @return
	 * @throws Exception
	 */
	public String modifyItem() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());
		WhNull.check(costAnalysisItem);
		WhNull.check(costAnalysisItem.getId());
		WhNull.checkTrimEmpty(costAnalysisItem.getType());
		WhNull.checkTrimEmpty(costAnalysisItem.getName());
		WhNull.check(costAnalysisItem.getMemo());
		WhNull.check(costAnalysisItem.getCost());
		costAnalysisItem.setCost(costAnalysisItem.getCost().setScale(2, BigDecimal.ROUND_HALF_UP));
		if (costAnalysisItem.getCost().compareTo(BigDecimal.valueOf(0)) < 0) {
			throw new IllegalArgumentException();
		}
		costAnalysisItem.setType(WhStringUtil.trimAll(costAnalysisItem.getType()));
		costAnalysisItem.setName(WhStringUtil.trimAll(costAnalysisItem.getName()));

		/*
		 * 校验TYPE合法性
		 */
		if (!CostAnalysisItem.TYPE_LIST.contains(costAnalysisItem.getType())) {
			throw new IllegalArgumentException();
		}

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		Project projectEx = projectService.getById(project.getId());
		WhOwnerShipUtil.checkStaffCompany(projectEx, staffEx);
		// 归档校验
		WhOwnerShipUtil.checkGoingProject(projectEx);

		/*
		 * 校验成本分析项的合法性
		 */
		CostAnalysisItem costAnalysisItemCheck = costAnalysisItemService.getById(costAnalysisItem.getId());
		WhOwnerShipUtil.checkProject(costAnalysisItemCheck, projectEx);

		costAnalysisItemCheck.setType(costAnalysisItem.getType());
		costAnalysisItemCheck.setName(costAnalysisItem.getName());
		costAnalysisItemCheck.setMemo(costAnalysisItem.getMemo());
		costAnalysisItemCheck.setCost(costAnalysisItem.getCost());
		costAnalysisItemService.modifyById(costAnalysisItemCheck);

		writeStream(getSuccessJsonTemplate());
		return SUCCESS;
	}

	/**
	 * 删除成本分析项
	 * 
	 * @return
	 * @throws Exception
	 */
	public String removeItem() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());
		WhNull.check(costAnalysisItem);
		WhNull.check(costAnalysisItem.getId());

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		Project projectEx = projectService.getById(project.getId());
		WhOwnerShipUtil.checkStaffCompany(projectEx, staffEx);
		// 归档校验
		WhOwnerShipUtil.checkGoingProject(projectEx);

		/*
		 * 校验成本项合法性
		 */
		CostAnalysisItem costAnalysisItemCheck = costAnalysisItemService.getById(costAnalysisItem.getId());
		WhOwnerShipUtil.checkProject(costAnalysisItemCheck, projectEx);

		costAnalysisItemService.removeById(costAnalysisItem.getId());

		writeStream(getSuccessJsonTemplate());
		return SUCCESS;
	}
}
