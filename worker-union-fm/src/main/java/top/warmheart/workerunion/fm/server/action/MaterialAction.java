package top.warmheart.workerunion.fm.server.action;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.springframework.context.annotation.Scope;

import top.warmheart.server.util.dto.Page;
import top.warmheart.server.util.exception.WhNull;
import top.warmheart.workerunion.fm.server.action.ex.ActionJson;
import top.warmheart.workerunion.fm.server.constant.SessionKey;
import top.warmheart.workerunion.fm.server.util.WhOwnerShipUtil;
import top.warmheart.workerunion.server.dto.MaterialOrderDetail2Dto;
import top.warmheart.workerunion.server.model.Material;
import top.warmheart.workerunion.server.model.MaterialOrder;
import top.warmheart.workerunion.server.model.MaterialOrderDetail;
import top.warmheart.workerunion.server.model.MaterialTransferApplication;
import top.warmheart.workerunion.server.model.MaterialTransferApplicationDetail;
import top.warmheart.workerunion.server.model.Project;
import top.warmheart.workerunion.server.model.Staff;
import top.warmheart.workerunion.server.service.MaterialOrderService;
import top.warmheart.workerunion.server.service.MaterialService;
import top.warmheart.workerunion.server.service.MaterialTransferService;
import top.warmheart.workerunion.server.service.MaterialTypeService;
import top.warmheart.workerunion.server.service.ProjectService;

import com.alibaba.fastjson.JSONObject;

@SuppressWarnings("serial")
@Scope("prototype")
public class MaterialAction extends ActionJson {
	private Project project;
	private Material material;
	private MaterialOrder materialOrder;
	private Page<Void> page;
	private MaterialService materialService;
	private MaterialOrderService materialOrderService;
	private List<MaterialOrderDetail> materialOrderDetails;
	private MaterialTypeService materialTypeService;
	private MaterialTransferApplication materialTransferApplication;
	private List<MaterialTransferApplicationDetail> materialTransferApplicationDetails;
	private ProjectService projectService;
	private MaterialTransferService materialTransferService;

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

	public Material getMaterial() {
		return material;
	}

	public MaterialOrder getMaterialOrder() {
		return materialOrder;
	}

	public void setMaterialOrder(MaterialOrder materialOrder) {
		this.materialOrder = materialOrder;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	public List<MaterialOrderDetail> getMaterialOrderDetails() {
		return materialOrderDetails;
	}

	public void setMaterialOrderDetails(List<MaterialOrderDetail> materialOrderDetails) {
		this.materialOrderDetails = materialOrderDetails;
	}

	public List<MaterialTransferApplicationDetail> getMaterialTransferApplicationDetails() {
		return materialTransferApplicationDetails;
	}

	public void setMaterialTransferApplicationDetails(
			List<MaterialTransferApplicationDetail> materialTransferApplicationDetails) {
		this.materialTransferApplicationDetails = materialTransferApplicationDetails;
	}

	public MaterialService getMaterialService() {
		return materialService;
	}

	@Resource(name = "materialService")
	public void setMaterialService(MaterialService materialService) {
		this.materialService = materialService;
	}

	public MaterialOrderService getMaterialOrderService() {
		return materialOrderService;
	}

	@Resource(name = "materialOrderService")
	public void setMaterialOrderService(MaterialOrderService materialOrderService) {
		this.materialOrderService = materialOrderService;
	}

	public MaterialTypeService getMaterialTypeService() {
		return materialTypeService;
	}

	@Resource(name = "materialTypeService")
	public void setMaterialTypeService(MaterialTypeService materialTypeService) {
		this.materialTypeService = materialTypeService;
	}

	public MaterialTransferApplication getMaterialTransferApplication() {
		return materialTransferApplication;
	}

	public void setMaterialTransferApplication(MaterialTransferApplication materialTransferApplication) {
		this.materialTransferApplication = materialTransferApplication;
	}

	public ProjectService getProjectService() {
		return projectService;
	}

	@Resource(name = "projectService")
	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}

	public MaterialTransferService getMaterialTransferService() {
		return materialTransferService;
	}

	@Resource(name = "materialTransferService")
	public void setMaterialTransferService(MaterialTransferService materialTransferService) {
		this.materialTransferService = materialTransferService;
	}

	/**
	 * 分页获取仓库材料列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String pageByFuzzy() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());
		WhNull.check(material);
		WhNull.checkPage(page);

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		Project projectEx = projectService.getById(project.getId());
		WhOwnerShipUtil.checkStaffCompany(projectEx, staffEx);

		// 获取模糊查询分页结果
		Page<Material> pageCheck = materialService.pageByFuzzy(project.getId(), material.getNum(), material.getName(),
				page);

		JSONObject json = getSuccessJsonTemplate();
		json.put("page", pageCheck);
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 分页获取批次查询结果
	 * 
	 * @return
	 * @throws Exception
	 */
	public String pageOrderByFuzzy() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());
		WhNull.check(materialOrder);
		WhNull.checkPage(page);

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		Project projectEx = projectService.getById(project.getId());
		WhOwnerShipUtil.checkStaffCompany(projectEx, staffEx);

		Page<MaterialOrderDetail2Dto> pageCheck = materialOrderService.pageDetailByFuzzy(project.getId(),
				materialOrder.getStaffName(), materialOrder.getCstCreate(), materialOrder.getBatchNo(), page);

		JSONObject json = getSuccessJsonTemplate();
		json.put("page", pageCheck);
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 分页获取指定材料出入库历史记录
	 * 
	 * @return
	 * @throws Exception
	 */
	public String pageOrderByNum() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());
		WhNull.check(material);
		WhNull.checkTrimEmpty(material.getNum());
		WhNull.checkPage(page);

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		Project projectEx = projectService.getById(project.getId());
		WhOwnerShipUtil.checkStaffCompany(projectEx, staffEx);

		/*
		 * 校验材料信息
		 */
		Material materialCheck = materialService.getByNum(project.getId(), material.getNum());
		WhOwnerShipUtil.checkProject(materialCheck, projectEx);

		/*
		 * 获取材料出入库记录
		 */
		BigDecimal amountImport = materialOrderService.amountOrderDetailByType(project.getId(), material.getNum(),
				MaterialOrder.TYPE_IMPORT);
		BigDecimal amountExport = materialOrderService.amountOrderDetailByType(project.getId(), material.getNum(),
				MaterialOrder.TYPE_EXPORT);
		Page<MaterialOrderDetail2Dto> pageCheck = materialOrderService.pageOrderDetailByNum(project.getId(),
				material.getNum(), page);

		JSONObject json = getSuccessJsonTemplate();
		json.put("material", materialCheck);
		json.put("amountImport", amountImport);
		json.put("amountExport", amountExport);
		json.put("page", pageCheck);
		writeStream(json);
		return SUCCESS;
	}

}
