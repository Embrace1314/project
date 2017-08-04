package top.warmheart.workerunion.pm.server.action;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.springframework.context.annotation.Scope;

import top.warmheart.server.util.exception.WhNull;
import top.warmheart.workerunion.pm.server.action.ex.ActionJson;
import top.warmheart.workerunion.pm.server.constant.SessionKey;
import top.warmheart.workerunion.pm.server.util.WhOwnerShipUtil;
import top.warmheart.workerunion.server.model.Attachment;
import top.warmheart.workerunion.server.model.CostAnalysisItem;
import top.warmheart.workerunion.server.model.Project;
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
	private CostAnalysisItemService costAnalysisItemService;
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

	public CostAnalysisItemService getCostAnalysisItemService() {
		return costAnalysisItemService;
	}

	@Resource(name = "costAnalysisItemService")
	public void setCostAnalysisItemService(CostAnalysisItemService costAnalysisItemService) {
		this.costAnalysisItemService = costAnalysisItemService;
	}
	public ProjectService getProjectService() {
		return projectService;
	}

	@Resource(name = "projectService")
	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
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
		
		Project projectEx = (Project) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_PROJECT);
		WhOwnerShipUtil.checkProject(project, projectEx);
		
		// 查找附件信息
		Attachment attachmentCheck = attachmentService.getLatestByType(projectEx.getId(), Attachment.TYPE_COST_ANALYSIS);

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

		/*
		 * 校验项目
		 */
		Project projectEx = (Project) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_PROJECT);
		WhOwnerShipUtil.checkProject(project, projectEx);
		
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

}
