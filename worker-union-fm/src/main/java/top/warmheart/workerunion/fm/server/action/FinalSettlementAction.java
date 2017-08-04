package top.warmheart.workerunion.fm.server.action;

import java.math.BigDecimal;
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
import top.warmheart.workerunion.server.dto.FinalSettlementItemDto;
import top.warmheart.workerunion.server.model.FinalSettlementItem;
import top.warmheart.workerunion.server.model.FinalSettlementItemAttachment;
import top.warmheart.workerunion.server.model.Project;
import top.warmheart.workerunion.server.model.Staff;
import top.warmheart.workerunion.server.service.FinalSettlementItemService;
import top.warmheart.workerunion.server.service.ProjectService;

import com.alibaba.fastjson.JSONObject;

@SuppressWarnings("serial")
@Scope("prototype")
public class FinalSettlementAction extends ActionJson {
	private Project project;
	private FinalSettlementItem finalSettlementItem;
	private Page<Void> page;
	private FinalSettlementItemService finalSettlementItemService;
	private ProjectService projectService;

	public FinalSettlementItem getFinalSettlementItem() {
		return finalSettlementItem;
	}

	public void setFinalSettlementItem(FinalSettlementItem finalSettlementItem) {
		this.finalSettlementItem = finalSettlementItem;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Page<Void> getPage() {
		return page;
	}

	public void setPage(Page<Void> page) {
		this.page = page;
	}

	public FinalSettlementItemService getFinalSettlementItemService() {
		return finalSettlementItemService;
	}

	@Resource(name = "finalSettlementItemService")
	public void setFinalSettlementItemService(FinalSettlementItemService finalSettlementItemService) {
		this.finalSettlementItemService = finalSettlementItemService;
	}

	public ProjectService getProjectService() {
		return projectService;
	}

	@Resource(name = "projectService")
	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}

	/**
	 * 获取竣工决算条目列表
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

		// 合计送审金额（单位：元）
		BigDecimal sumSubmitMoney = new BigDecimal(0);
		// 合计核定金额（单位：元）
		BigDecimal sumCheckedMoney = new BigDecimal(0);
		// 合计核减金额（单位：元）
		BigDecimal sumIncreasedMoney = new BigDecimal(0);
		// 合计核增金额（单位：元）
		BigDecimal sumDecreasedMoney = new BigDecimal(0);
		// 获取列表
		List<FinalSettlementItemDto> list = finalSettlementItemService.listItemByProjectId(project.getId());
		for (FinalSettlementItemDto finalSettlementItemDto : list) {
			sumSubmitMoney = sumSubmitMoney.add(finalSettlementItemDto.getSubmitMoney());
			sumCheckedMoney = sumCheckedMoney.add(finalSettlementItemDto.getCheckedMoney());
			sumIncreasedMoney = sumIncreasedMoney.add(finalSettlementItemDto.getIncreasedMoney());
			sumDecreasedMoney = sumDecreasedMoney.add(finalSettlementItemDto.getDecreasedMoney());
		}

		JSONObject json = getSuccessJsonTemplate();
		json.put("finalSettlementItems", list);
		json.put("sumSubmitMoney", sumSubmitMoney);
		json.put("sumCheckedMoney", sumCheckedMoney);
		json.put("sumIncreasedMoney", sumIncreasedMoney);
		json.put("sumDecreasedMoney", sumDecreasedMoney);
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 增加新的竣工决算项
	 * 
	 * @return
	 * @throws Exception
	 */
	public String addItem() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());
		WhNull.check(finalSettlementItem);
		WhNull.checkTrimEmpty(finalSettlementItem.getSubprojectName());
		WhNull.check(finalSettlementItem.getSubmitMoney());
		WhNull.check(finalSettlementItem.getCheckedMoney());
		WhNull.check(finalSettlementItem.getIncreasedMoney());
		WhNull.check(finalSettlementItem.getDecreasedMoney());
		finalSettlementItem.setSubmitMoney(finalSettlementItem.getSubmitMoney().setScale(2, BigDecimal.ROUND_HALF_UP));
		finalSettlementItem.setCheckedMoney(finalSettlementItem.getCheckedMoney().setScale(2, BigDecimal.ROUND_HALF_UP));
		finalSettlementItem.setIncreasedMoney(finalSettlementItem.getIncreasedMoney().setScale(2, BigDecimal.ROUND_HALF_UP));
		finalSettlementItem.setDecreasedMoney(finalSettlementItem.getDecreasedMoney().setScale(2, BigDecimal.ROUND_HALF_UP));
		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		Project projectEx = projectService.getById(project.getId());
		WhOwnerShipUtil.checkStaffCompany(projectEx, staffEx);
		// 归档校验
		WhOwnerShipUtil.checkGoingProject(projectEx);

		finalSettlementItem.setProjectId(projectEx.getId());
		finalSettlementItem.setCompanyId(projectEx.getCompanyId());
		finalSettlementItemService.addItem(finalSettlementItem);

		writeStream(getSuccessJsonTemplate());
		return SUCCESS;
	}

	/**
	 * 修改竣工决算项
	 * 
	 * @return
	 * @throws Exception
	 */
	public String modifyItemById() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());
		WhNull.check(finalSettlementItem);
		WhNull.check(finalSettlementItem.getId());
		WhNull.checkTrimEmpty(finalSettlementItem.getSubprojectName());
		WhNull.check(finalSettlementItem.getSubmitMoney());
		WhNull.check(finalSettlementItem.getCheckedMoney());
		WhNull.check(finalSettlementItem.getIncreasedMoney());
		WhNull.check(finalSettlementItem.getDecreasedMoney());
		finalSettlementItem.setSubmitMoney(finalSettlementItem.getSubmitMoney().setScale(2, BigDecimal.ROUND_HALF_UP));
		finalSettlementItem.setCheckedMoney(finalSettlementItem.getCheckedMoney().setScale(2, BigDecimal.ROUND_HALF_UP));
		finalSettlementItem.setIncreasedMoney(finalSettlementItem.getIncreasedMoney().setScale(2, BigDecimal.ROUND_HALF_UP));
		finalSettlementItem.setDecreasedMoney(finalSettlementItem.getDecreasedMoney().setScale(2, BigDecimal.ROUND_HALF_UP));
		
		finalSettlementItem.setSubprojectName(WhStringUtil.trimAll(finalSettlementItem.getSubprojectName()));
		
		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		Project projectEx = projectService.getById(project.getId());
		WhOwnerShipUtil.checkStaffCompany(projectEx, staffEx);
		// 归档校验
		WhOwnerShipUtil.checkGoingProject(projectEx);

		/*
		 * 校验竣工决算项的合法性
		 */
		FinalSettlementItem finalSettlementItemCheck = finalSettlementItemService.getItemById(finalSettlementItem.getId());
		WhOwnerShipUtil.checkProject(finalSettlementItemCheck, projectEx);

		finalSettlementItemCheck.setSubprojectName(finalSettlementItem.getSubprojectName());
		finalSettlementItemCheck.setSubmitMoney(finalSettlementItem.getSubmitMoney());
		finalSettlementItemCheck.setCheckedMoney(finalSettlementItem.getCheckedMoney());
		finalSettlementItemCheck.setIncreasedMoney(finalSettlementItem.getIncreasedMoney());
		finalSettlementItemCheck.setDecreasedMoney(finalSettlementItem.getDecreasedMoney());
		finalSettlementItemService.modifyItemById(finalSettlementItemCheck);

		writeStream(getSuccessJsonTemplate());
		return SUCCESS;
	}

	/**
	 * 删除竣工决算项
	 * 
	 * @return
	 * @throws Exception
	 */
	public String removeItemById() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());
		WhNull.check(finalSettlementItem);
		WhNull.check(finalSettlementItem.getId());

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		Project projectEx = projectService.getById(project.getId());
		WhOwnerShipUtil.checkStaffCompany(projectEx, staffEx);
		// 归档校验
		WhOwnerShipUtil.checkGoingProject(projectEx);

		/*
		 * 校验竣工决算项合法性
		 */
		FinalSettlementItem finalSettlementItemCheck = finalSettlementItemService.getItemById(finalSettlementItem.getId());
		WhOwnerShipUtil.checkProject(finalSettlementItemCheck, projectEx);

		finalSettlementItemService.removeItemById(finalSettlementItem.getId());

		writeStream(getSuccessJsonTemplate());
		return SUCCESS;
	}
	/**
	 * 删除竣工决算项附件
	 * 
	 * @return
	 * @throws Exception
	 */
	public String removeAttachmentById() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());
		WhNull.check(finalSettlementItem);
		WhNull.check(finalSettlementItem.getId());

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		Project projectEx = projectService.getById(project.getId());
		WhOwnerShipUtil.checkStaffCompany(projectEx, staffEx);
		// 归档校验
		WhOwnerShipUtil.checkGoingProject(projectEx);

		/*
		 * 校验附件
		 */
		FinalSettlementItemAttachment finalSettlementItemAttachmentCheck = finalSettlementItemService.getAttachmentByItemId(finalSettlementItem.getId());
		WhOwnerShipUtil.checkProject(finalSettlementItemAttachmentCheck, projectEx);
		
		finalSettlementItemService.removeAttachmentByItemId(finalSettlementItem.getId());

		writeStream(getSuccessJsonTemplate());
		return SUCCESS;
	}

	/**
	 * 获取竣工决算项
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getItemById() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());
		WhNull.check(finalSettlementItem);
		WhNull.check(finalSettlementItem.getId());

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		Project projectEx = projectService.getById(project.getId());
		WhOwnerShipUtil.checkStaffCompany(projectEx, staffEx);

		/*
		 * 查找并校验
		 */
		FinalSettlementItem finalSettlementItemCheck = finalSettlementItemService.getItemById(finalSettlementItem.getId());
		WhOwnerShipUtil.checkProject(finalSettlementItemCheck, projectEx);

		JSONObject json = getSuccessJsonTemplate();
		json.put("finalSettlementItem", finalSettlementItemCheck);
		writeStream(json);
		return SUCCESS;
	}

}
