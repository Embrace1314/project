package top.warmheart.workerunion.fm.server.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.springframework.context.annotation.Scope;

import top.warmheart.server.util.WhStringUtil;
import top.warmheart.server.util.dto.Page;
import top.warmheart.server.util.exception.WhNull;
import top.warmheart.workerunion.fm.server.action.ex.ActionJson;
import top.warmheart.workerunion.fm.server.constant.Config;
import top.warmheart.workerunion.fm.server.constant.SessionKey;
import top.warmheart.workerunion.fm.server.util.WhBatchNoUtil;
import top.warmheart.workerunion.fm.server.util.WhOwnerShipUtil;
import top.warmheart.workerunion.server.dto.MaterialHeadquartersOrderDetail2Dto;
import top.warmheart.workerunion.server.dto.MaterialTransferApplicationDto;
import top.warmheart.workerunion.server.exception.WhInvalidMaterialHeadquartersBatchNoException;
import top.warmheart.workerunion.server.exception.WhInvalidMaterialHeadquartersException;
import top.warmheart.workerunion.server.exception.WhInvalidMaterialTypeException;
import top.warmheart.workerunion.server.exception.WhMaterialHeadquartersInsufficientException;
import top.warmheart.workerunion.server.exception.WhMaterialHeadquartersOrderExistException;
import top.warmheart.workerunion.server.exception.WhMaterialOrderExistException;
import top.warmheart.workerunion.server.exception.WhMaterialTransferApplicationDeterminedException;
import top.warmheart.workerunion.server.exception.WhMaterialTransferToArchivedProjectException;
import top.warmheart.workerunion.server.exception.WhProjectArchivedException;
import top.warmheart.workerunion.server.model.MaterialHeadquarters;
import top.warmheart.workerunion.server.model.MaterialHeadquartersOrder;
import top.warmheart.workerunion.server.model.MaterialHeadquartersOrderDetail;
import top.warmheart.workerunion.server.model.MaterialOrder;
import top.warmheart.workerunion.server.model.MaterialOrderDetail;
import top.warmheart.workerunion.server.model.MaterialTransferApplication;
import top.warmheart.workerunion.server.model.MaterialTransferApplicationDetail;
import top.warmheart.workerunion.server.model.MaterialType;
import top.warmheart.workerunion.server.model.Project;
import top.warmheart.workerunion.server.model.Staff;
import top.warmheart.workerunion.server.service.MaterialHeadquartersOrderService;
import top.warmheart.workerunion.server.service.MaterialHeadquartersService;
import top.warmheart.workerunion.server.service.MaterialOrderService;
import top.warmheart.workerunion.server.service.MaterialService;
import top.warmheart.workerunion.server.service.MaterialTransferService;
import top.warmheart.workerunion.server.service.MaterialTypeService;
import top.warmheart.workerunion.server.service.ProjectService;

import com.alibaba.fastjson.JSONObject;

@SuppressWarnings("serial")
@Scope("prototype")
public class MaterialHeadquartersAction extends ActionJson {
	private MaterialHeadquarters materialHeadquarters;
	private MaterialHeadquartersOrder materialHeadquartersOrder;
	private MaterialService materialService;
	private MaterialTransferApplication materialTransferApplication;
	private MaterialTransferApplicationDto materialTransferApplicationDto;
	private Page<Void> page;
	private MaterialHeadquartersService materialHeadquartersService;
	private MaterialHeadquartersOrderService materialHeadquartersOrderService;
	private MaterialOrderService materialOrderService;
	private List<MaterialHeadquartersOrderDetail> materialHeadquartersOrderDetails;
	private MaterialTypeService materialTypeService;
	private MaterialTransferService materialTransferService;
	private ProjectService projectService;

	public MaterialTransferApplicationDto getMaterialTransferApplicationDto() {
		return materialTransferApplicationDto;
	}

	public void setMaterialTransferApplicationDto(MaterialTransferApplicationDto materialTransferApplicationDto) {
		this.materialTransferApplicationDto = materialTransferApplicationDto;
	}

	public Page<Void> getPage() {
		return page;
	}

	public void setPage(Page<Void> page) {
		this.page = page;
	}

	public MaterialHeadquarters getMaterialHeadquarters() {
		return materialHeadquarters;
	}

	public MaterialHeadquartersOrder getMaterialHeadquartersOrder() {
		return materialHeadquartersOrder;
	}

	public void setMaterialHeadquartersOrder(MaterialHeadquartersOrder materialHeadquartersOrder) {
		this.materialHeadquartersOrder = materialHeadquartersOrder;
	}

	public void setMaterialHeadquarters(MaterialHeadquarters materialHeadquarters) {
		this.materialHeadquarters = materialHeadquarters;
	}

	public List<MaterialHeadquartersOrderDetail> getMaterialHeadquartersOrderDetails() {
		return materialHeadquartersOrderDetails;
	}

	public void setMaterialHeadquartersOrderDetails(
			List<MaterialHeadquartersOrderDetail> materialHeadquartersOrderDetails) {
		this.materialHeadquartersOrderDetails = materialHeadquartersOrderDetails;
	}

	public MaterialHeadquartersService getMaterialHeadquartersService() {
		return materialHeadquartersService;
	}

	@Resource(name = "materialHeadquartersService")
	public void setMaterialHeadquartersService(MaterialHeadquartersService materialHeadquartersService) {
		this.materialHeadquartersService = materialHeadquartersService;
	}

	public MaterialHeadquartersOrderService getMaterialHeadquartersOrderService() {
		return materialHeadquartersOrderService;
	}

	@Resource(name = "materialHeadquartersOrderService")
	public void setMaterialHeadquartersOrderService(MaterialHeadquartersOrderService materialHeadquartersOrderService) {
		this.materialHeadquartersOrderService = materialHeadquartersOrderService;
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

	public MaterialTransferService getMaterialTransferService() {
		return materialTransferService;
	}

	@Resource(name = "materialTransferService")
	public void setMaterialTransferService(MaterialTransferService materialTransferService) {
		this.materialTransferService = materialTransferService;
	}

	public MaterialService getMaterialService() {
		return materialService;
	}

	@Resource(name = "materialService")
	public void setMaterialService(MaterialService materialService) {
		this.materialService = materialService;
	}

	public ProjectService getProjectService() {
		return projectService;
	}

	@Resource(name = "projectService")
	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}

	public MaterialOrderService getMaterialOrderService() {
		return materialOrderService;
	}

	@Resource(name = "materialOrderService")
	public void setMaterialOrderService(MaterialOrderService materialOrderService) {
		this.materialOrderService = materialOrderService;
	}

	/**
	 * 分页获取仓库材料列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String pageByFuzzy() throws Exception {
		// 校验参数
		WhNull.check(materialHeadquarters);
		WhNull.checkPage(page);

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		WhOwnerShipUtil.checkStaff(staffEx);

		// 获取模糊查询分页结果
		Page<MaterialHeadquarters> pageCheck = materialHeadquartersService.pageByFuzzy(staffEx.getCompanyId(),
				materialHeadquarters.getNum(), materialHeadquarters.getName(), page);

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
		WhNull.check(materialHeadquartersOrder);
		WhNull.checkPage(page);

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		WhOwnerShipUtil.checkStaff(staffEx);

		Page<MaterialHeadquartersOrderDetail2Dto> pageCheck = materialHeadquartersOrderService.pageDetailByFuzzy(
				staffEx.getCompanyId(), materialHeadquartersOrder.getStaffName(),
				materialHeadquartersOrder.getCstCreate(), materialHeadquartersOrder.getBatchNo(), page);

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
		WhNull.check(materialHeadquarters);
		WhNull.checkTrimEmpty(materialHeadquarters.getNum());
		WhNull.checkPage(page);

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		WhOwnerShipUtil.checkStaff(staffEx);

		/*
		 * 校验材料信息
		 */
		MaterialHeadquarters materialHeadquartersCheck = materialHeadquartersService.getByNum(staffEx.getCompanyId(),
				materialHeadquarters.getNum());
		WhOwnerShipUtil.checkMaterialHeadquarters(materialHeadquartersCheck);

		/*
		 * 获取材料出入库记录
		 */
		BigDecimal amountImport = materialHeadquartersOrderService.amountOrderDetailByType(staffEx.getCompanyId(),
				materialHeadquarters.getNum(), MaterialHeadquartersOrder.TYPE_IMPORT);
		BigDecimal amountExport = materialHeadquartersOrderService.amountOrderDetailByType(staffEx.getCompanyId(),
				materialHeadquarters.getNum(), MaterialHeadquartersOrder.TYPE_EXPORT);
		Page<MaterialHeadquartersOrderDetail2Dto> pageCheck = materialHeadquartersOrderService.pageOrderDetailByNum(
				staffEx.getCompanyId(), materialHeadquarters.getNum(), page);

		JSONObject json = getSuccessJsonTemplate();
		json.put("materialHeadquarters", materialHeadquartersCheck);
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
	public String importMaterialHeadquarters() throws Exception {
		// 校验参数
		WhNull.check(materialHeadquartersOrder);
		WhNull.checkEmpty(materialHeadquartersOrderDetails);
		WhNull.checkTrimEmpty(materialHeadquartersOrder.getBatchNo());
		materialHeadquartersOrder.setBatchNo(WhStringUtil.trimAll(materialHeadquartersOrder.getBatchNo()));
		// 校验批次号前缀
		if (materialHeadquartersOrder.getBatchNo().toUpperCase()
				.startsWith(Config.MATERIAL_ORDER_AUTO_BATCH_NO_PREFIX.toUpperCase())
				|| materialHeadquartersOrder.getBatchNo().toUpperCase()
						.startsWith(Config.MATERIAL_HEADQUARTERS_ORDER_AUTO_BATCH_NO_PREFIX.toUpperCase())) {
			throw new WhInvalidMaterialHeadquartersBatchNoException();
		}

		/*
		 * 登录校验
		 */
		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		WhOwnerShipUtil.checkStaff(staffEx);

		/*
		 * 校验批次号是否已使用
		 */
		MaterialHeadquartersOrder materialHeadquartersOrderExist = materialHeadquartersOrderService
				.getByCompanyBatchNo(staffEx.getCompanyId(), materialHeadquartersOrder.getBatchNo());
		if (null != materialHeadquartersOrderExist) {
			throw new WhMaterialHeadquartersOrderExistException();
		}

		materialHeadquartersOrder.setCompanyId(staffEx.getCompanyId());
		materialHeadquartersOrder.setStaffId(staffEx.getId());
		materialHeadquartersOrder.setStaffName(staffEx.getName());
		materialHeadquartersOrder.setMoney(BigDecimal.valueOf(0));
		materialHeadquartersOrder.setType(MaterialHeadquartersOrder.TYPE_IMPORT);

		// 校验入库材料
		for (MaterialHeadquartersOrderDetail materialHeadquartersOrderDetail : materialHeadquartersOrderDetails) {
			WhNull.checkTrimEmpty(materialHeadquartersOrderDetail.getNum());
			WhNull.check(materialHeadquartersOrderDetail.getPrice());
			WhNull.check(materialHeadquartersOrderDetail.getAmount());
			materialHeadquartersOrderDetail.setPrice(materialHeadquartersOrderDetail.getPrice().setScale(2,
					BigDecimal.ROUND_HALF_UP));
			materialHeadquartersOrderDetail.setAmount(materialHeadquartersOrderDetail.getAmount().setScale(5,
					BigDecimal.ROUND_HALF_UP));
			if (materialHeadquartersOrderDetail.getPrice().compareTo(BigDecimal.valueOf(0)) < 0
					|| materialHeadquartersOrderDetail.getAmount().compareTo(BigDecimal.valueOf(0)) <= 0) {
				throw new IllegalArgumentException();
			}
			// 校验材料类型
			MaterialType materialTypeCheck = materialTypeService.getByNum(staffEx.getCompanyId(),
					materialHeadquartersOrderDetail.getNum());
			WhOwnerShipUtil.checkMaterialType(materialTypeCheck);
			materialHeadquartersOrderDetail.setCompanyId(materialHeadquartersOrder.getCompanyId());
			materialHeadquartersOrderDetail.setName(materialTypeCheck.getName());
			materialHeadquartersOrderDetail.setModel(materialTypeCheck.getModel());
			materialHeadquartersOrderDetail.setUnit(materialTypeCheck.getUnit());
			materialHeadquartersOrderDetail.setMoney(materialHeadquartersOrderDetail.getAmount()
					.multiply(materialHeadquartersOrderDetail.getPrice()).setScale(2, BigDecimal.ROUND_HALF_UP));
			materialHeadquartersOrder.setMoney(materialHeadquartersOrder.getMoney().add(
					materialHeadquartersOrderDetail.getMoney()));
		}
		materialHeadquartersOrder.setMaterialHeadquartersOrderDetails(materialHeadquartersOrderDetails);
		// 将批次材料进行入库
		materialHeadquartersOrderService.importMaterialHeadquarters(materialHeadquartersOrder);

		JSONObject json = getSuccessJsonTemplate();
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 材料出库
	 * 
	 * @return
	 * @throws Exception
	 */
	public String exportMaterialHeadquarters() throws Exception {
		// 校验参数
		WhNull.check(materialHeadquartersOrder);
		WhNull.checkEmpty(materialHeadquartersOrderDetails);
		WhNull.checkTrimEmpty(materialHeadquartersOrder.getBatchNo());
		WhNull.check(materialHeadquartersOrder.getTargetProjectId());
		materialHeadquartersOrder.setBatchNo(WhStringUtil.trimAll(materialHeadquartersOrder.getBatchNo()));
		// 校验批次号前缀
		if (materialHeadquartersOrder.getBatchNo().toUpperCase()
				.startsWith(Config.MATERIAL_ORDER_AUTO_BATCH_NO_PREFIX.toUpperCase())
				|| materialHeadquartersOrder.getBatchNo().toUpperCase()
						.startsWith(Config.MATERIAL_HEADQUARTERS_ORDER_AUTO_BATCH_NO_PREFIX.toUpperCase())) {
			throw new WhInvalidMaterialHeadquartersBatchNoException();
		}

		/*
		 * 登录校验
		 */
		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		WhOwnerShipUtil.checkStaff(staffEx);

		/*
		 * 校验目标项目组
		 */
		Project targetProjectCheck = projectService.getById(materialHeadquartersOrder.getTargetProjectId());
		WhOwnerShipUtil.checkStaffCompany(targetProjectCheck, staffEx);
		if (!Project.FILE_STATUS_GOING.equalsIgnoreCase(targetProjectCheck.getFileStatus())) {
			throw new WhProjectArchivedException();
		}

		/*
		 * 校验批次号是否已使用
		 */
		MaterialHeadquartersOrder materialHeadquartersOrderExist = materialHeadquartersOrderService
				.getByCompanyBatchNo(staffEx.getCompanyId(), materialHeadquartersOrder.getBatchNo());
		if (null != materialHeadquartersOrderExist) {
			throw new WhMaterialHeadquartersOrderExistException();
		}
		MaterialOrder materialOrderExist = materialOrderService.getByProjectBatchNo(
				materialHeadquartersOrder.getTargetProjectId(), materialHeadquartersOrder.getBatchNo());
		if (null != materialOrderExist) {
			throw new WhMaterialOrderExistException();
		}

		// 整理总部订单信息
		materialHeadquartersOrder.setCompanyId(staffEx.getCompanyId());
		materialHeadquartersOrder.setStaffId(staffEx.getId());
		materialHeadquartersOrder.setStaffName(staffEx.getName());
		materialHeadquartersOrder.setMoney(BigDecimal.valueOf(0));
		materialHeadquartersOrder.setType(MaterialHeadquartersOrder.TYPE_EXPORT);

		// 整理订单详情
		for (MaterialHeadquartersOrderDetail materialHeadquartersOrderDetail : materialHeadquartersOrderDetails) {
			WhNull.checkTrimEmpty(materialHeadquartersOrderDetail.getNum());
			WhNull.check(materialHeadquartersOrderDetail.getAmount());
			if (materialHeadquartersOrderDetail.getAmount().compareTo(BigDecimal.valueOf(0)) <= 0) {
				throw new IllegalArgumentException();
			}
			// 校验材料库存
			MaterialHeadquarters materialHeadquartersCheck = materialHeadquartersService.getByNum(
					staffEx.getCompanyId(), materialHeadquartersOrderDetail.getNum());
			if (null == materialHeadquartersCheck) {
				throw new WhInvalidMaterialHeadquartersException();
			}
			if (materialHeadquartersOrderDetail.getAmount().compareTo(materialHeadquartersCheck.getAmount()) > 0) {
				throw new WhMaterialHeadquartersInsufficientException();
			}

			materialHeadquartersOrderDetail.setCompanyId(materialHeadquartersOrder.getCompanyId());
			materialHeadquartersOrderDetail.setName(materialHeadquartersCheck.getName());
			materialHeadquartersOrderDetail.setModel(materialHeadquartersCheck.getModel());
			materialHeadquartersOrderDetail.setUnit(materialHeadquartersCheck.getUnit());
			materialHeadquartersOrderDetail.setMoney(materialHeadquartersOrderDetail.getPrice().multiply(materialHeadquartersOrderDetail.getAmount()).setScale(2,
					BigDecimal.ROUND_HALF_UP));
			materialHeadquartersOrder.setMoney(materialHeadquartersOrder.getMoney().add(
					materialHeadquartersOrderDetail.getMoney()));
		}
		materialHeadquartersOrder.setMaterialHeadquartersOrderDetails(materialHeadquartersOrderDetails);

		// 整理项目组订单信息
		MaterialOrder materialOrderNew = new MaterialOrder();
		materialOrderNew.setCompanyId(materialHeadquartersOrder.getCompanyId());
		materialOrderNew.setProjectId(materialHeadquartersOrder.getTargetProjectId());
		materialOrderNew.setStaffId(materialHeadquartersOrder.getStaffId());
		materialOrderNew.setStaffName(materialHeadquartersOrder.getStaffName());
		materialOrderNew.setBatchNo(materialHeadquartersOrder.getBatchNo());
		materialOrderNew.setMoney(materialHeadquartersOrder.getMoney());
		materialOrderNew.setType(MaterialOrder.TYPE_IMPORT);
		
		List<MaterialOrderDetail> materialOrderDetailsNew = new ArrayList<MaterialOrderDetail>();
		for (MaterialHeadquartersOrderDetail materialHeadquartersOrderDetail : materialHeadquartersOrder.getMaterialHeadquartersOrderDetails()) {
			MaterialOrderDetail materialOrderDetailNew = new MaterialOrderDetail();
			materialOrderDetailNew.setCompanyId(materialHeadquartersOrderDetail.getCompanyId());
			materialOrderDetailNew.setProjectId(materialOrderNew.getProjectId());
			materialOrderDetailNew.setName(materialHeadquartersOrderDetail.getName());
			materialOrderDetailNew.setNum(materialHeadquartersOrderDetail.getNum());
			materialOrderDetailNew.setModel(materialHeadquartersOrderDetail.getModel());
			materialOrderDetailNew.setUnit(materialHeadquartersOrderDetail.getUnit());
			materialOrderDetailNew.setAmount(materialHeadquartersOrderDetail.getAmount());
			materialOrderDetailNew.setPrice(materialHeadquartersOrderDetail.getPrice());
			materialOrderDetailNew.setMoney(materialHeadquartersOrderDetail.getMoney());
			materialOrderDetailsNew.add(materialOrderDetailNew);
		}
		materialOrderNew.setMaterialOrderDetails(materialOrderDetailsNew);
		
		// 将批次材料进行出库
		materialHeadquartersOrderService.exportMaterialHeadquartersToProject(materialHeadquartersOrder, materialOrderNew);

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
		WhNull.check(materialHeadquarters);
		WhNull.checkTrimEmpty(materialHeadquarters.getNum());

		/*
		 * 校验登录
		 */
		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		WhOwnerShipUtil.checkStaff(staffEx);

		// 获取模糊查询分页结果
		MaterialHeadquarters materialHeadquartersCheck = materialHeadquartersService.getByNum(staffEx.getCompanyId(),
				materialHeadquarters.getNum());
		if (null == materialHeadquartersCheck
				|| materialHeadquartersCheck.getAmount().compareTo(BigDecimal.valueOf(0)) <= 0) {
			throw new WhInvalidMaterialTypeException();
		}

		JSONObject json = getSuccessJsonTemplate();
		json.put("materialHeadquarters", materialHeadquartersCheck);
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 获取未审核转库申请列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String listUndeterminedTransferApplication() throws Exception {
		/*
		 * 校验登录
		 */
		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		WhOwnerShipUtil.checkStaff(staffEx);

		// 获取未审核转库申请列表
		List<MaterialTransferApplicationDto> materialTransferApplicationDtos = materialTransferService.listByCompanyId(
				staffEx.getCompanyId(), MaterialTransferApplication.STATUS_UNDETERMINED);

		JSONObject json = getSuccessJsonTemplate();
		json.put("materialTransferApplications", materialTransferApplicationDtos);
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 模糊查询转库申请历史记录，分页
	 * 
	 * @return
	 * @throws Exception
	 */
	public String pageTransferApplicationByFuzzy() throws Exception {
		// 校验参数
		WhNull.check(materialTransferApplicationDto);
		WhNull.check(materialTransferApplicationDto.getImportProjectName());
		WhNull.check(materialTransferApplicationDto.getExportProjectName());
		WhNull.check(materialTransferApplicationDto.getStatus());
		WhNull.check(materialTransferApplicationDto.getStaffName());
		WhNull.checkPage(page);
		if (materialTransferApplicationDto.getImportProjectName().contains("总部")) {
			materialTransferApplicationDto.setImportProjectName(null);
			materialTransferApplicationDto.setType(MaterialTransferApplication.TYPE_TO_HEADQUARTERS);
		}
		/*
		 * 校验登录
		 */
		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		WhOwnerShipUtil.checkStaff(staffEx);

		// 获取模糊查询分页结果
		materialTransferApplicationDto.setCompanyId(staffEx.getCompanyId());
		Page<MaterialTransferApplicationDto> pageCheck = materialTransferService.pageByFuzzy(
				materialTransferApplicationDto, page);

		JSONObject json = getSuccessJsonTemplate();
		json.put("page", pageCheck);
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 获取转库申请详情
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getTransferApplicationDetailById() throws Exception {
		// 校验参数
		WhNull.check(materialTransferApplication);
		WhNull.check(materialTransferApplication.getId());

		/*
		 * 校验登录
		 */
		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		WhOwnerShipUtil.checkStaff(staffEx);

		// 校验转库申请
		MaterialTransferApplicationDto materialTransferApplicationDtoCheck = materialTransferService
				.getDetailById(materialTransferApplication.getId());
		WhOwnerShipUtil.checkStaffCompany(materialTransferApplicationDtoCheck, staffEx);

		// 获取详情
		List<MaterialTransferApplicationDetail> materialTransferApplicationDetails = materialTransferService
				.listApplicationDetailByApplicationId(materialTransferApplication.getId());

		JSONObject json = getSuccessJsonTemplate();
		json.put("materialTransferApplication", materialTransferApplicationDtoCheck);
		json.put("materialTransferApplicationDetails", materialTransferApplicationDetails);
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 通过转库审核
	 * 
	 * @return
	 * @throws Exception
	 */
	public String passTransferApplicationById() throws Exception {
		// 校验参数
		WhNull.check(materialTransferApplication);
		WhNull.check(materialTransferApplication.getId());

		/*
		 * 校验登录
		 */
		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		WhOwnerShipUtil.checkStaff(staffEx);

		// 校验转库申请
		MaterialTransferApplicationDto materialTransferApplicationDtoCheck = materialTransferService
				.getDetailById(materialTransferApplication.getId());
		WhOwnerShipUtil.checkStaffCompany(materialTransferApplicationDtoCheck, staffEx);

		if (!MaterialTransferApplication.STATUS_UNDETERMINED.equalsIgnoreCase(materialTransferApplicationDtoCheck
				.getStatus())) {
			throw new WhMaterialTransferApplicationDeterminedException();
		}

		/*
		 * 校验目标项目组
		 */
		if (MaterialTransferApplication.TYPE_TO_PROJECT.equalsIgnoreCase(materialTransferApplicationDtoCheck.getType())) {
			Project projectCheck = projectService.getById(materialTransferApplicationDtoCheck.getImportProjectId());
			WhOwnerShipUtil.checkStaffCompany(projectCheck, staffEx);
			// 校验目标项目组是否归档
			if (Project.FILE_STATUS_ARCHIVED.equalsIgnoreCase(projectCheck.getFileStatus())) {
				throw new WhMaterialTransferToArchivedProjectException();
			}
		}

		// 进行转库到总部
		if (MaterialTransferApplication.TYPE_TO_HEADQUARTERS.equalsIgnoreCase(materialTransferApplicationDtoCheck
				.getType())) {
			materialTransferService.passById(materialTransferApplication.getId(), WhBatchNoUtil.projectGen(),
					staffEx);
		} else {
			// 进行转库到项目组
			materialTransferService.passById(materialTransferApplication.getId(), WhBatchNoUtil.projectGen(), staffEx);
		}

		JSONObject json = getSuccessJsonTemplate();
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 驳回转库审核
	 * 
	 * @return
	 * @throws Exception
	 */
	public String failTransferApplicationById() throws Exception {
		// 校验参数
		WhNull.check(materialTransferApplication);
		WhNull.check(materialTransferApplication.getId());

		/*
		 * 校验登录
		 */
		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		WhOwnerShipUtil.checkStaff(staffEx);

		// 校验转库申请
		MaterialTransferApplicationDto materialTransferApplicationDtoCheck = materialTransferService
				.getDetailById(materialTransferApplication.getId());
		WhOwnerShipUtil.checkStaffCompany(materialTransferApplicationDtoCheck, staffEx);

		if (!MaterialTransferApplication.STATUS_UNDETERMINED.equalsIgnoreCase(materialTransferApplicationDtoCheck
				.getStatus())) {
			throw new WhMaterialTransferApplicationDeterminedException();
		}
		/*
		 * 校验出库项目组
		 */
		Project projectCheck = projectService.getById(materialTransferApplicationDtoCheck.getExportProjectId());
		WhOwnerShipUtil.checkStaffCompany(projectCheck, staffEx);

		materialTransferService.failById(materialTransferApplication.getId());

		JSONObject json = getSuccessJsonTemplate();
		writeStream(json);
		return SUCCESS;
	}
}
