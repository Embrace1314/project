package top.warmheart.workerunion.server.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import top.warmheart.server.util.dto.Page;
import top.warmheart.server.util.exception.WhNull;
import top.warmheart.workerunion.server.dao.MaterialMapper;
import top.warmheart.workerunion.server.dao.MaterialTransferApplicationDetailMapper;
import top.warmheart.workerunion.server.dao.MaterialTransferApplicationMapper;
import top.warmheart.workerunion.server.dao.ProjectMapper;
import top.warmheart.workerunion.server.dto.MaterialTransferApplicationDto;
import top.warmheart.workerunion.server.model.Material;
import top.warmheart.workerunion.server.model.MaterialHeadquartersOrder;
import top.warmheart.workerunion.server.model.MaterialHeadquartersOrderDetail;
import top.warmheart.workerunion.server.model.MaterialOrder;
import top.warmheart.workerunion.server.model.MaterialOrderDetail;
import top.warmheart.workerunion.server.model.MaterialTransferApplication;
import top.warmheart.workerunion.server.model.MaterialTransferApplicationDetail;
import top.warmheart.workerunion.server.model.Project;
import top.warmheart.workerunion.server.model.Staff;
import top.warmheart.workerunion.server.service.MaterialHeadquartersOrderService;
import top.warmheart.workerunion.server.service.MaterialOrderService;
import top.warmheart.workerunion.server.service.MaterialTransferService;

@Service("materialTransferServiceImpl")
@Scope("singleton")
public class MaterialTransferServiceImpl implements MaterialTransferService {
	private MaterialOrderService materialOrderService;
	private MaterialHeadquartersOrderService materialHeadquartersOrderService;
	private SqlSessionFactory sqlSessionFactory;

	public SqlSessionFactory getSqlSessionFactory() {
		return sqlSessionFactory;
	}

	@Resource(name = "sqlSessionFactory")
	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}

	public MaterialOrderService getMaterialOrderService() {
		return materialOrderService;
	}

	@Resource(name = "materialOrderServiceImpl")
	public void setMaterialOrderService(MaterialOrderService materialOrderService) {
		this.materialOrderService = materialOrderService;
	}

	public MaterialHeadquartersOrderService getMaterialHeadquartersOrderService() {
		return materialHeadquartersOrderService;
	}

	@Resource(name = "materialHeadquartersOrderServiceImpl")
	public void setMaterialHeadquartersOrderService(MaterialHeadquartersOrderService materialHeadquartersOrderService) {
		this.materialHeadquartersOrderService = materialHeadquartersOrderService;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void applyTransferMaterial(MaterialTransferApplication materialTransferApplication,
			List<MaterialTransferApplicationDetail> materialTransferApplicationDetails) {
		MaterialTransferApplicationDetailMapper materialTransferApplicationDetailMapper = sqlSessionFactory
				.openSession().getMapper(MaterialTransferApplicationDetailMapper.class);
		MaterialMapper materialMapper = sqlSessionFactory.openSession().getMapper(MaterialMapper.class);
		MaterialTransferApplicationMapper materialTransferApplicationMapper = sqlSessionFactory.openSession()
				.getMapper(MaterialTransferApplicationMapper.class);
		ProjectMapper projectMapper = sqlSessionFactory.openSession().getMapper(ProjectMapper.class);
		/*
		 * 校验项目信息
		 */
		Project projectExport = projectMapper.selectByPrimaryKey(materialTransferApplication.getExportProjectId());
		if (null == projectExport || projectExport.getDel()) {
			throw new RuntimeException("无效的出库项目组");
		}
		if (null != materialTransferApplication.getImportProjectId()) {
			Project projectImport = projectMapper.selectByPrimaryKey(materialTransferApplication.getExportProjectId());
			if (null == projectImport || projectImport.getDel()) {
				throw new RuntimeException("无效的入库项目组");
			}
		}
		// 插入申请信息
		materialTransferApplicationMapper.insert(materialTransferApplication);

		// 将仓库材料冻结
		for (MaterialTransferApplicationDetail materialTransferApplicationDetail : materialTransferApplicationDetails) {
			materialTransferApplicationDetail.setCompanyId(materialTransferApplication.getCompanyId());
			materialTransferApplicationDetail.setMaterialTransferApplicationId(materialTransferApplication.getId());
			/*
			 * 校验材料库存
			 */
			Material materialCheck = materialMapper.selectByNum4Update(
					materialTransferApplication.getExportProjectId(), materialTransferApplicationDetail.getNum());
			if (null == materialCheck
					|| materialCheck.getAmount().compareTo(materialTransferApplicationDetail.getAmount()) < 0) {
				throw new RuntimeException("库存不足");
			}
			materialCheck.setAmount(materialCheck.getAmount().subtract(materialTransferApplicationDetail.getAmount()));
			materialCheck.setAmountFreeze(materialCheck.getAmountFreeze().add(
					materialTransferApplicationDetail.getAmount()));
			// 插入转库申请详情
			materialTransferApplicationDetailMapper.insert(materialTransferApplicationDetail);
			// 更新仓库存量
			materialMapper.updateByPrimaryKey(materialCheck);
		}
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<MaterialTransferApplicationDto> listByCompanyId(BigInteger companyId, String status) {
		return sqlSessionFactory.openSession().getMapper(MaterialTransferApplicationMapper.class)
				.selectListByCompanyId(companyId, status);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public MaterialTransferApplicationDto getDetailById(BigInteger id) {
		return sqlSessionFactory.openSession().getMapper(MaterialTransferApplicationMapper.class).selectDetailById(id);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<MaterialTransferApplicationDetail> listApplicationDetailByApplicationId(BigInteger transferApplicationId) {
		return sqlSessionFactory.openSession().getMapper(MaterialTransferApplicationDetailMapper.class)
				.selectListByApplicationId(transferApplicationId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void failById(BigInteger id) {
		MaterialTransferApplicationMapper materialTransferApplicationMapper = sqlSessionFactory.openSession()
				.getMapper(MaterialTransferApplicationMapper.class);
		MaterialTransferApplicationDetailMapper materialTransferApplicationDetailMapper = sqlSessionFactory
				.openSession().getMapper(MaterialTransferApplicationDetailMapper.class);
		ProjectMapper projectMapper = sqlSessionFactory.openSession().getMapper(ProjectMapper.class);
		MaterialMapper materialMapper = sqlSessionFactory.openSession().getMapper(MaterialMapper.class);
		/*
		 * 获取转库申请信息
		 */
		MaterialTransferApplication materialTransferApplication = materialTransferApplicationMapper
				.selectByPrimaryKey4Update(id);
		if (null == materialTransferApplication) {
			throw new RuntimeException("无效的转库申请");
		}
		List<MaterialTransferApplicationDetail> materialTransferApplicationDetails = materialTransferApplicationDetailMapper
				.selectListByApplicationId4Update(id);

		/*
		 * 校验项目信息
		 */
		Project projectExport = projectMapper.selectByPrimaryKey(materialTransferApplication.getExportProjectId());
		if (null == projectExport || projectExport.getDel()) {
			throw new RuntimeException("无效的出库项目组");
		}

		// 更新申请信息
		materialTransferApplication.setStatus(MaterialTransferApplication.STATUS_FAIL);
		materialTransferApplicationMapper.updateByPrimaryKey(materialTransferApplication);

		// 将仓库材料解除冻结
		for (MaterialTransferApplicationDetail materialTransferApplicationDetail : materialTransferApplicationDetails) {
			materialTransferApplicationDetail.setCompanyId(materialTransferApplication.getCompanyId());
			materialTransferApplicationDetail.setMaterialTransferApplicationId(materialTransferApplication.getId());
			/*
			 * 校验出库项目组材料
			 */
			Material materialCheck = materialMapper.selectByNum4Update(
					materialTransferApplication.getExportProjectId(), materialTransferApplicationDetail.getNum());
			if (null == materialCheck
					|| materialCheck.getAmountFreeze().compareTo(materialTransferApplicationDetail.getAmount()) < 0) {
				throw new RuntimeException("库存不足");
			}
			materialCheck.setAmountFreeze(materialCheck.getAmountFreeze().subtract(
					materialTransferApplicationDetail.getAmount()));
			materialCheck.setAmount(materialCheck.getAmount().add(materialTransferApplicationDetail.getAmount()));
			// 更新仓库存量
			materialMapper.updateByPrimaryKey(materialCheck);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void passById(BigInteger id, String batchNo, Staff staff) {
		MaterialTransferApplicationMapper materialTransferApplicationMapper = sqlSessionFactory.openSession()
				.getMapper(MaterialTransferApplicationMapper.class);
		MaterialTransferApplicationDetailMapper materialTransferApplicationDetailMapper = sqlSessionFactory
				.openSession().getMapper(MaterialTransferApplicationDetailMapper.class);
		ProjectMapper projectMapper = sqlSessionFactory.openSession().getMapper(ProjectMapper.class);
		/*
		 * 获取转库申请信息
		 */
		MaterialTransferApplication materialTransferApplication = materialTransferApplicationMapper
				.selectByPrimaryKey4Update(id);
		if (null == materialTransferApplication) {
			throw new RuntimeException("无效的转库申请");
		}
		List<MaterialTransferApplicationDetail> materialTransferApplicationDetails = materialTransferApplicationDetailMapper
				.selectListByApplicationId4Update(id);
		/*
		 * 校验出库项目信息
		 */
		Project projectExport = projectMapper.selectByPrimaryKey(materialTransferApplication.getExportProjectId());
		if (null == projectExport || projectExport.getDel()) {
			throw new RuntimeException("无效的出库项目组");
		}
		// 更新申请信息
		materialTransferApplication.setStatus(MaterialTransferApplication.STATUS_PASS);
		materialTransferApplicationMapper.updateByPrimaryKey(materialTransferApplication);

		// 将出库项目组冻结材料出库
		exportProjectFreezeMaterial(materialTransferApplication, materialTransferApplicationDetails, batchNo, staff);
		
		// 转到总部仓库
		if (MaterialTransferApplication.TYPE_TO_HEADQUARTERS.equalsIgnoreCase(materialTransferApplication.getType())) {
			importHeadquarterMaterial(materialTransferApplication, materialTransferApplicationDetails, batchNo, staff);
		} else {
			/*
			 * 校验目标项目信息
			 */
			Project projectImport = projectMapper.selectByPrimaryKey(materialTransferApplication.getImportProjectId());
			if (null == projectImport || projectImport.getDel()) {
				throw new RuntimeException("无效的入库项目组");
			}
			importProjectMaterial(materialTransferApplication, materialTransferApplicationDetails, batchNo, staff);
		}
	}
	
	
	/**
	 * 将冻结材料从指定项目组出库
	 * @param materialTransferApplication
	 * @param materialTransferApplicationDetails
	 * @param batchNo
	 * @param staff
	 */
	private void exportProjectFreezeMaterial(MaterialTransferApplication materialTransferApplication,
			List<MaterialTransferApplicationDetail> materialTransferApplicationDetails, String batchNo, Staff staff) {
		/*
		 * 整理订单信息
		 */
		MaterialOrder materialOrder = new MaterialOrder();
		materialOrder.setCompanyId(materialTransferApplication.getCompanyId());
		materialOrder.setProjectId(materialTransferApplication.getExportProjectId());
		materialOrder.setStaffId(staff.getId());
		materialOrder.setStaffName(staff.getName());
		materialOrder.setBatchNo(batchNo);
		materialOrder.setMoney(materialTransferApplication.getMoney());
		materialOrder.setType(MaterialOrder.TYPE_EXPORT);
		
		/*
		 * 整理订单详情
		 */
		List<MaterialOrderDetail> materialOrderDetails = new ArrayList<MaterialOrderDetail>();
		for (MaterialTransferApplicationDetail materialTransferApplicationDetail : materialTransferApplicationDetails) {
			MaterialOrderDetail materialOrderDetail = new MaterialOrderDetail();
			materialOrderDetail.setCompanyId(materialTransferApplicationDetail.getCompanyId());
			materialOrderDetail.setProjectId(materialOrder.getProjectId());
			materialOrderDetail.setName(materialTransferApplicationDetail.getName());
			materialOrderDetail.setNum(materialTransferApplicationDetail.getNum());
			materialOrderDetail.setModel(materialTransferApplicationDetail.getModel());
			materialOrderDetail.setUnit(materialTransferApplicationDetail.getUnit());
			materialOrderDetail.setAmount(materialTransferApplicationDetail.getAmount());
			materialOrderDetail.setPrice(materialTransferApplicationDetail.getPrice());
			materialOrderDetail.setMoney(materialTransferApplicationDetail.getMoney());
			materialOrderDetails.add(materialOrderDetail);
		}
		materialOrder.setMaterialOrderDetails(materialOrderDetails);
		materialOrderService.exportFreezeMaterial(materialOrder);
	}

	/**
	 * 将材料入库到项目组
	 * 
	 * @param materialTransferApplication
	 * @param materialTransferApplicationDetails
	 * @param staff
	 */
	private void importProjectMaterial(MaterialTransferApplication materialTransferApplication,
			List<MaterialTransferApplicationDetail> materialTransferApplicationDetails, String batchNo, Staff staff) {
		/*
		 * 整理订单信息
		 */
		MaterialOrder materialOrder = new MaterialOrder();
		materialOrder.setCompanyId(materialTransferApplication.getCompanyId());
		materialOrder.setProjectId(materialTransferApplication.getImportProjectId());
		materialOrder.setStaffId(staff.getId());
		materialOrder.setStaffName(staff.getName());
		materialOrder.setBatchNo(batchNo);
		materialOrder.setMoney(materialTransferApplication.getMoney());
		materialOrder.setType(MaterialOrder.TYPE_IMPORT);
		
		/*
		 * 整理订单详情
		 */
		List<MaterialOrderDetail> materialOrderDetails = new ArrayList<MaterialOrderDetail>();
		for (MaterialTransferApplicationDetail materialTransferApplicationDetail : materialTransferApplicationDetails) {
			MaterialOrderDetail materialOrderDetail = new MaterialOrderDetail();
			materialOrderDetail.setCompanyId(materialTransferApplicationDetail.getCompanyId());
			materialOrderDetail.setProjectId(materialOrder.getProjectId());
			materialOrderDetail.setName(materialTransferApplicationDetail.getName());
			materialOrderDetail.setNum(materialTransferApplicationDetail.getNum());
			materialOrderDetail.setModel(materialTransferApplicationDetail.getModel());
			materialOrderDetail.setUnit(materialTransferApplicationDetail.getUnit());
			materialOrderDetail.setAmount(materialTransferApplicationDetail.getAmount());
			materialOrderDetail.setPrice(materialTransferApplicationDetail.getPrice());
			materialOrderDetail.setMoney(materialTransferApplicationDetail.getMoney());
			materialOrderDetails.add(materialOrderDetail);
		}
		materialOrder.setMaterialOrderDetails(materialOrderDetails);
		materialOrderService.importMaterial(materialOrder);

	}

	/**
	 * 将材料入库到总部
	 * 
	 * @param materialTransferApplication
	 * @param materialTransferApplicationDetails
	 * @param staff
	 */
	private void importHeadquarterMaterial(MaterialTransferApplication materialTransferApplication,
			List<MaterialTransferApplicationDetail> materialTransferApplicationDetails, String batchNo, Staff staff) {
		/*
		 * 整理订单信息
		 */
		MaterialHeadquartersOrder materialHeadquartersOrder = new MaterialHeadquartersOrder();
		materialHeadquartersOrder.setCompanyId(materialTransferApplication.getCompanyId());
		materialHeadquartersOrder.setStaffId(staff.getId());
		materialHeadquartersOrder.setStaffName(staff.getName());
		materialHeadquartersOrder.setBatchNo(batchNo);
		materialHeadquartersOrder.setMoney(materialTransferApplication.getMoney());
		materialHeadquartersOrder.setType(MaterialHeadquartersOrder.TYPE_IMPORT);
		
		/*
		 * 整理订单详情
		 */
		List<MaterialHeadquartersOrderDetail> materialHeadquartersOrderDetails = new ArrayList<MaterialHeadquartersOrderDetail>();
		for (MaterialTransferApplicationDetail materialTransferApplicationDetail : materialTransferApplicationDetails) {
			MaterialHeadquartersOrderDetail materialHeadquartersOrderDetail = new MaterialHeadquartersOrderDetail();
			materialHeadquartersOrderDetail.setCompanyId(materialTransferApplicationDetail.getCompanyId());
			materialHeadquartersOrderDetail.setName(materialTransferApplicationDetail.getName());
			materialHeadquartersOrderDetail.setNum(materialTransferApplicationDetail.getNum());
			materialHeadquartersOrderDetail.setModel(materialTransferApplicationDetail.getModel());
			materialHeadquartersOrderDetail.setUnit(materialTransferApplicationDetail.getUnit());
			materialHeadquartersOrderDetail.setAmount(materialTransferApplicationDetail.getAmount());
			materialHeadquartersOrderDetail.setPrice(materialTransferApplicationDetail.getPrice());
			materialHeadquartersOrderDetail.setMoney(materialTransferApplicationDetail.getMoney());
			materialHeadquartersOrderDetails.add(materialHeadquartersOrderDetail);
		}
		materialHeadquartersOrder.setMaterialHeadquartersOrderDetails(materialHeadquartersOrderDetails);
		materialHeadquartersOrderService.importMaterialHeadquarters(materialHeadquartersOrder);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Page<MaterialTransferApplicationDto> pageByFuzzy(
			MaterialTransferApplicationDto materialTransferApplicationDto, Page<?> page) {
		WhNull.check(materialTransferApplicationDto);
		WhNull.checkPage(page);

		MaterialTransferApplicationMapper materialTransferApplicationMapper = sqlSessionFactory.openSession()
				.getMapper(MaterialTransferApplicationMapper.class);

		// 获取总记录数
		Page<MaterialTransferApplicationDto> pageCheck = new Page<MaterialTransferApplicationDto>();
		pageCheck.setTotalItem(materialTransferApplicationMapper.countByFuzzy(materialTransferApplicationDto));
		pageCheck.setSize(page.getSize());
		pageCheck.setPagination(Math.min(pageCheck.getTotalPage(), page.getPagination()));

		if (pageCheck.getTotalItem() > 0) {
			// 获取分页项目列表信息
			pageCheck.setList(materialTransferApplicationMapper.pageByFuzzy(materialTransferApplicationDto, pageCheck));
		} else {
			pageCheck.setList(new ArrayList<MaterialTransferApplicationDto>());
		}
		return pageCheck;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public MaterialTransferApplication getById(BigInteger id) {
		return sqlSessionFactory.openSession().getMapper(MaterialTransferApplicationMapper.class)
				.selectByPrimaryKey(id);
	}

}
