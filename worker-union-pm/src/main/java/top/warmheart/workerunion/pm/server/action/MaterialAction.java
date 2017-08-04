package top.warmheart.workerunion.pm.server.action;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.springframework.context.annotation.Scope;

import top.warmheart.server.util.WhStringUtil;
import top.warmheart.server.util.dto.Page;
import top.warmheart.server.util.exception.WhNull;
import top.warmheart.workerunion.pm.server.action.ex.ActionJson;
import top.warmheart.workerunion.pm.server.constant.Config;
import top.warmheart.workerunion.pm.server.constant.SessionKey;
import top.warmheart.workerunion.pm.server.util.WhBatchNoUtil;
import top.warmheart.workerunion.pm.server.util.WhOwnerShipUtil;
import top.warmheart.workerunion.server.dto.MaterialOrderDetail2Dto;
import top.warmheart.workerunion.server.exception.WhInvalidMaterialBatchNoException;
import top.warmheart.workerunion.server.exception.WhInvalidMaterialException;
import top.warmheart.workerunion.server.exception.WhInvalidMaterialTypeException;
import top.warmheart.workerunion.server.exception.WhMaterialInsufficientException;
import top.warmheart.workerunion.server.exception.WhMaterialOrderExistException;
import top.warmheart.workerunion.server.exception.WhMaterialTransferToArchivedProjectException;
import top.warmheart.workerunion.server.model.Material;
import top.warmheart.workerunion.server.model.MaterialOrder;
import top.warmheart.workerunion.server.model.MaterialOrderDetail;
import top.warmheart.workerunion.server.model.MaterialTransferApplication;
import top.warmheart.workerunion.server.model.MaterialTransferApplicationDetail;
import top.warmheart.workerunion.server.model.MaterialType;
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

		Project projectEx = (Project) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_PROJECT);
		WhOwnerShipUtil.checkProject(project, projectEx);

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

		Project projectEx = (Project) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_PROJECT);
		WhOwnerShipUtil.checkProject(project, projectEx);

		Page<MaterialOrderDetail2Dto> pageCheck = materialOrderService.pageDetailByFuzzy(project.getId(), materialOrder.getStaffName(),
				materialOrder.getCstCreate(), materialOrder.getBatchNo(), page);
		
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

		Project projectEx = (Project) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_PROJECT);
		WhOwnerShipUtil.checkProject(project, projectEx);

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

	/**
	 * 材料入库
	 * 
	 * @return
	 * @throws Exception
	 */
	public String importMaterial() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());
		WhNull.check(materialOrder);
		WhNull.checkEmpty(materialOrderDetails);
		WhNull.checkTrimEmpty(materialOrder.getBatchNo());
		materialOrder.setBatchNo(WhStringUtil.trimAll(materialOrder.getBatchNo()));
		// 校验批次号前缀
		if (materialOrder.getBatchNo().toUpperCase()
				.startsWith(Config.MATERIAL_ORDER_AUTO_BATCH_NO_PREFIX.toUpperCase())
				|| materialOrder.getBatchNo().toUpperCase()
						.startsWith(Config.MATERIAL_HEADQUARTERS_ORDER_AUTO_BATCH_NO_PREFIX.toUpperCase())) {
			throw new WhInvalidMaterialBatchNoException();
		}

		/*
		 * 登录校验
		 */
		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		WhOwnerShipUtil.checkStaff(staffEx);
		Project projectEx = (Project) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_PROJECT);
		WhOwnerShipUtil.checkProject(project, projectEx);
		// 归档校验
		WhOwnerShipUtil.checkGoingProject(projectService.getById(projectEx.getId()));

		/*
		 * 校验批次号是否已使用
		 */
		MaterialOrder materialOrderExist = materialOrderService.getByProjectBatchNo(projectEx.getId(),
				materialOrder.getBatchNo());
		if (null != materialOrderExist) {
			throw new WhMaterialOrderExistException();
		}
		materialOrder.setCompanyId(projectEx.getCompanyId());
		materialOrder.setProjectId(projectEx.getId());
		materialOrder.setStaffId(staffEx.getId());
		materialOrder.setStaffName(staffEx.getName());
		materialOrder.setMoney(BigDecimal.valueOf(0));
		materialOrder.setType(MaterialOrder.TYPE_IMPORT);

		// 校验入库材料
		for (MaterialOrderDetail materialOrderDetail : materialOrderDetails) {
			WhNull.checkTrimEmpty(materialOrderDetail.getNum());
			WhNull.check(materialOrderDetail.getPrice());
			WhNull.check(materialOrderDetail.getAmount());
			materialOrderDetail.setPrice(materialOrderDetail.getPrice().setScale(2, BigDecimal.ROUND_HALF_UP));
			materialOrderDetail.setAmount(materialOrderDetail.getAmount().setScale(5, BigDecimal.ROUND_HALF_UP));
			if (materialOrderDetail.getPrice().compareTo(BigDecimal.valueOf(0)) < 0
					|| materialOrderDetail.getAmount().compareTo(BigDecimal.valueOf(0)) <= 0) {
				throw new IllegalArgumentException();
			}
			// 校验材料类型
			MaterialType materialTypeCheck = materialTypeService.getByNum(projectEx.getCompanyId(),
					materialOrderDetail.getNum());
			WhOwnerShipUtil.checkMaterialType(materialTypeCheck);
			materialOrderDetail.setCompanyId(materialOrder.getCompanyId());
			materialOrderDetail.setProjectId(materialOrder.getProjectId());
			materialOrderDetail.setName(materialTypeCheck.getName());
			materialOrderDetail.setModel(materialTypeCheck.getModel());
			materialOrderDetail.setUnit(materialTypeCheck.getUnit());
			materialOrderDetail.setMoney(materialOrderDetail.getAmount().multiply(materialOrderDetail.getPrice()).setScale(2, BigDecimal.ROUND_HALF_UP));
			materialOrder.setMoney(materialOrder.getMoney().add(materialOrderDetail.getMoney()));
		}
		materialOrder.setMaterialOrderDetails(materialOrderDetails);
		// 将批次材料进行入库
		materialOrderService.importMaterial(materialOrder);

		JSONObject json = getSuccessJsonTemplate();
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 材料领用
	 * 
	 * @return
	 * @throws Exception
	 */
	public String exportMaterial() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());
		WhNull.checkEmpty(materialOrderDetails);

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		WhOwnerShipUtil.checkStaff(staffEx);
		Project projectEx = (Project) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_PROJECT);
		WhOwnerShipUtil.checkProject(project, projectEx);
		// 归档校验
		WhOwnerShipUtil.checkGoingProject(projectService.getById(projectEx.getId()));

		MaterialOrder materialOrderNew = new MaterialOrder();
		materialOrderNew.setCompanyId(projectEx.getCompanyId());
		materialOrderNew.setProjectId(projectEx.getId());
		materialOrderNew.setStaffId(staffEx.getId());
		materialOrderNew.setStaffName(staffEx.getName());
		// 设置批次号
		materialOrderNew
				.setBatchNo(WhBatchNoUtil.projectGen());
		materialOrderNew.setMoney(BigDecimal.valueOf(0));
		materialOrderNew.setType(MaterialOrder.TYPE_EXPORT);

		// 校验领用材料
		for (MaterialOrderDetail materialOrderDetail : materialOrderDetails) {
			WhNull.checkTrimEmpty(materialOrderDetail.getNum());
			WhNull.check(materialOrderDetail.getAmount());
			if (materialOrderDetail.getAmount().compareTo(BigDecimal.valueOf(0)) <= 0) {
				throw new IllegalArgumentException();
			}
			// 校验材料库存
			Material materialCheck = materialService.getByNum(projectEx.getId(), materialOrderDetail.getNum());
			if (null == materialCheck) {
				throw new WhInvalidMaterialException();
			}
			if (materialOrderDetail.getAmount().compareTo(materialCheck.getAmount()) > 0) {
				throw new WhMaterialInsufficientException();
			}

			materialOrderDetail.setCompanyId(materialOrderNew.getCompanyId());
			materialOrderDetail.setProjectId(materialOrderNew.getProjectId());
			materialOrderDetail.setName(materialCheck.getName());
			materialOrderDetail.setModel(materialCheck.getModel());
			materialOrderDetail.setUnit(materialCheck.getUnit());
			materialOrderDetail.setPrice(BigDecimal.valueOf(0));
			materialOrderDetail.setMoney(BigDecimal.valueOf(0));
			materialOrderNew.setMoney(materialOrderNew.getMoney().add(materialOrderDetail.getMoney()));
		}
		materialOrderNew.setMaterialOrderDetails(materialOrderDetails);
		// 将批次材料进行出库
		materialOrderService.exportMaterial(materialOrderNew);

		JSONObject json = getSuccessJsonTemplate();
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 转库申请
	 * 
	 * @return
	 * @throws Exception
	 */
	public String applyTransferMaterial() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());
		WhNull.check(materialTransferApplication);
		WhNull.check(materialTransferApplication.getType());
		WhNull.checkEmpty(materialTransferApplicationDetails);
		if (!MaterialTransferApplication.TYPE_LIST.contains(materialTransferApplication.getType())) {
			throw new IllegalArgumentException();
		}
		/*
		 * 校验入库项目组ID
		 */
		if (MaterialTransferApplication.TYPE_TO_PROJECT.equalsIgnoreCase(materialTransferApplication.getType())) {
			WhNull.check(materialTransferApplication.getImportProjectId());
			if (materialTransferApplication.getImportProjectId().equals(project.getId())) {
				throw new IllegalArgumentException();
			}
		}
		/*
		 * 校验登录
		 */
		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		WhOwnerShipUtil.checkStaff(staffEx);
		Project projectEx = (Project) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_PROJECT);
		WhOwnerShipUtil.checkProject(project, projectEx);
		// 归档校验
		WhOwnerShipUtil.checkGoingProject(projectService.getById(projectEx.getId()));

		/*
		 * 校验入库项目组
		 */
		if (null != materialTransferApplication.getImportProjectId()) {
			Project projectImport = projectService.getById(materialTransferApplication.getImportProjectId());
			WhOwnerShipUtil.checkStaffCompany(projectImport, staffEx);
			if (Project.FILE_STATUS_ARCHIVED.equalsIgnoreCase(projectImport.getFileStatus())){
				throw new WhMaterialTransferToArchivedProjectException();
			}
		}

		materialTransferApplication.setCompanyId(projectEx.getCompanyId());
		materialTransferApplication.setStaffId(staffEx.getId());
		materialTransferApplication.setStaffName(staffEx.getName());
		materialTransferApplication.setExportProjectId(projectEx.getId());
		materialTransferApplication.setMoney(BigDecimal.valueOf(0));
		materialTransferApplication.setStatus(MaterialTransferApplication.STATUS_UNDETERMINED);

		// 校验出库材料
		for (MaterialTransferApplicationDetail materialTransferApplicationDetail : materialTransferApplicationDetails) {
			WhNull.checkTrimEmpty(materialTransferApplicationDetail.getNum());
			WhNull.check(materialTransferApplicationDetail.getAmount());
			WhNull.check(materialTransferApplicationDetail.getPrice());
			materialTransferApplicationDetail.setPrice(materialTransferApplicationDetail.getPrice().setScale(2, BigDecimal.ROUND_HALF_UP));
			materialTransferApplicationDetail.setAmount(materialTransferApplicationDetail.getAmount().setScale(5, BigDecimal.ROUND_HALF_UP));
			
			if (materialTransferApplicationDetail.getAmount().compareTo(BigDecimal.valueOf(0)) <= 0
					|| materialTransferApplicationDetail.getPrice().compareTo(BigDecimal.valueOf(0)) < 0) {
				throw new IllegalArgumentException();
			}
			// 校验材料库存
			Material materialCheck = materialService.getByNum(projectEx.getId(),
					materialTransferApplicationDetail.getNum());
			if (null == materialCheck) {
				throw new WhInvalidMaterialException();
			}
			if (materialTransferApplicationDetail.getAmount().compareTo(materialCheck.getAmount()) > 0) {
				throw new WhMaterialInsufficientException();
			}

			materialTransferApplicationDetail.setCompanyId(materialTransferApplication.getCompanyId());
			materialTransferApplicationDetail.setName(materialCheck.getName());
			materialTransferApplicationDetail.setModel(materialCheck.getModel());
			materialTransferApplicationDetail.setUnit(materialCheck.getUnit());
			materialTransferApplicationDetail.setMoney(materialTransferApplicationDetail.getAmount().multiply(
					materialTransferApplicationDetail.getPrice()).setScale(2, BigDecimal.ROUND_HALF_UP));
			materialTransferApplication.setMoney(materialTransferApplication.getMoney().add(
					materialTransferApplicationDetail.getMoney()));
		}

		// 申请材料转库
		materialTransferService.applyTransferMaterial(materialTransferApplication, materialTransferApplicationDetails);

		JSONObject json = getSuccessJsonTemplate();
		writeStream(json);
		return SUCCESS;
	}
	

	/**
	 * 根据企业材料编号获取材料类型信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getExportByNum() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());
		WhNull.check(material);
		WhNull.checkTrimEmpty(material.getNum());
		
		/*
		 * 校验登录
		 */
		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		WhOwnerShipUtil.checkStaff(staffEx);
		Project projectEx = (Project) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_PROJECT);
		WhOwnerShipUtil.checkProject(project, projectEx);

		// 获取模糊查询分页结果
		Material materialCheck = materialService.getByNum(projectEx.getId(), material.getNum());
		if (null == materialCheck || materialCheck.getAmount().compareTo(BigDecimal.valueOf(0)) <= 0) {
			throw new WhInvalidMaterialTypeException();
		}

		JSONObject json = getSuccessJsonTemplate();
		json.put("material", materialCheck);
		writeStream(json);
		return SUCCESS;
	}
}
