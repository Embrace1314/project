package top.warmheart.workerunion.server.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import top.warmheart.server.util.dto.Page;
import top.warmheart.server.util.exception.WhNull;
import top.warmheart.workerunion.server.dao.MaterialHeadquartersMapper;
import top.warmheart.workerunion.server.dao.MaterialHeadquartersOrderDetailMapper;
import top.warmheart.workerunion.server.dao.MaterialHeadquartersOrderMapper;
import top.warmheart.workerunion.server.dto.MaterialHeadquartersOrderDetail2Dto;
import top.warmheart.workerunion.server.model.MaterialHeadquarters;
import top.warmheart.workerunion.server.model.MaterialHeadquartersOrder;
import top.warmheart.workerunion.server.model.MaterialHeadquartersOrderDetail;
import top.warmheart.workerunion.server.model.MaterialOrder;
import top.warmheart.workerunion.server.service.MaterialHeadquartersOrderService;
import top.warmheart.workerunion.server.service.MaterialOrderService;

@Service("materialHeadquartersOrderServiceImpl")
@Scope("singleton")
public class MaterialHeadquartersOrderServiceImpl implements MaterialHeadquartersOrderService {
	private SqlSessionFactory sqlSessionFactory;
	private MaterialOrderService materialOrderService;

	public MaterialOrderService getMaterialOrderService() {
		return materialOrderService;
	}

	@Resource(name = "materialOrderServiceImpl")
	public void setMaterialOrderService(MaterialOrderService materialOrderService) {
		this.materialOrderService = materialOrderService;
	}

	public SqlSessionFactory getSqlSessionFactory() {
		return sqlSessionFactory;
	}

	@Resource(name = "sqlSessionFactory")
	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Page<MaterialHeadquartersOrder> pageByFuzzy(BigInteger companyId, String staffName, Date cstCreate,
			String batchNo, Page<?> page) {
		WhNull.check(companyId);
		WhNull.checkPage(page);

		MaterialHeadquartersOrderMapper materialHeadquartersOrderMapper = sqlSessionFactory.openSession().getMapper(
				MaterialHeadquartersOrderMapper.class);

		// 获取总记录数
		Page<MaterialHeadquartersOrder> pageCheck = new Page<MaterialHeadquartersOrder>();
		pageCheck.setTotalItem(materialHeadquartersOrderMapper.countByFuzzy(companyId, staffName, cstCreate, batchNo));
		pageCheck.setSize(page.getSize());
		pageCheck.setPagination(Math.min(pageCheck.getTotalPage(), page.getPagination()));

		if (pageCheck.getTotalItem() > 0) {
			// 获取分页项目列表信息
			pageCheck.setList(materialHeadquartersOrderMapper.pageByFuzzy(companyId, staffName, cstCreate, batchNo,
					pageCheck));
		} else {
			pageCheck.setList(new ArrayList<MaterialHeadquartersOrder>());
		}
		return pageCheck;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public BigDecimal amountOrderDetailByType(BigInteger companyId, String num, String type) {
		return sqlSessionFactory.openSession().getMapper(MaterialHeadquartersOrderDetailMapper.class)
				.amountByType(companyId, num, type);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Page<MaterialHeadquartersOrderDetail2Dto> pageOrderDetailByNum(BigInteger companyId, String num, Page<?> page) {
		WhNull.check(companyId);
		WhNull.checkTrimEmpty(num);
		WhNull.checkPage(page);

		MaterialHeadquartersOrderDetailMapper materialHeadquartersOrderDetailMapper = sqlSessionFactory.openSession()
				.getMapper(MaterialHeadquartersOrderDetailMapper.class);

		// 获取总记录数
		Page<MaterialHeadquartersOrderDetail2Dto> pageCheck = new Page<MaterialHeadquartersOrderDetail2Dto>();
		pageCheck.setTotalItem(materialHeadquartersOrderDetailMapper.countByNum(companyId, num));
		pageCheck.setSize(page.getSize());
		pageCheck.setPagination(Math.min(pageCheck.getTotalPage(), page.getPagination()));

		if (pageCheck.getTotalItem() > 0) {
			// 获取分页项目列表信息
			pageCheck.setList(materialHeadquartersOrderDetailMapper.pageByNum(companyId, num, pageCheck));
		} else {
			pageCheck.setList(new ArrayList<MaterialHeadquartersOrderDetail2Dto>());
		}
		return pageCheck;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void importMaterialHeadquarters(MaterialHeadquartersOrder materialHeadquartersOrder) {
		// 插入批次
		sqlSessionFactory.openSession().getMapper(MaterialHeadquartersOrderMapper.class)
				.insert(materialHeadquartersOrder);

		MaterialHeadquartersOrderDetailMapper materialHeadquartersOrderDetailMapper = sqlSessionFactory.openSession()
				.getMapper(MaterialHeadquartersOrderDetailMapper.class);
		MaterialHeadquartersMapper materialHeadquartersMapper = sqlSessionFactory.openSession().getMapper(
				MaterialHeadquartersMapper.class);

		for (MaterialHeadquartersOrderDetail materialHeadquartersOrderDetail : materialHeadquartersOrder.getMaterialHeadquartersOrderDetails()) {
			materialHeadquartersOrderDetail.setCompanyId(materialHeadquartersOrder.getCompanyId());
			materialHeadquartersOrderDetail.setCompanyId(materialHeadquartersOrder.getCompanyId());
			materialHeadquartersOrderDetail.setMaterialHeadquartersOrderId(materialHeadquartersOrder.getId());

			// 插入批次详情
			materialHeadquartersOrderDetailMapper.insert(materialHeadquartersOrderDetail);
			
			/*
			 * 更新仓库余量
			 */
			MaterialHeadquarters materialHeadquarters = new MaterialHeadquarters();
			materialHeadquarters.setCompanyId(materialHeadquartersOrderDetail.getCompanyId());
			materialHeadquarters.setCompanyId(materialHeadquartersOrderDetail.getCompanyId());
			materialHeadquarters.setName(materialHeadquartersOrderDetail.getName());
			materialHeadquarters.setNum(materialHeadquartersOrderDetail.getNum());
			materialHeadquarters.setModel(materialHeadquartersOrderDetail.getModel());
			materialHeadquarters.setUnit(materialHeadquartersOrderDetail.getUnit());
			materialHeadquarters.setAmount(materialHeadquartersOrderDetail.getAmount());
			materialHeadquarters.setAmountFreeze(BigDecimal.valueOf(0));

			// 更新仓库存量
			materialHeadquartersMapper.insertOrAccumulate(materialHeadquarters);
		}

	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public MaterialHeadquartersOrder getByCompanyBatchNo(BigInteger companyId, String batchNo) {
		return sqlSessionFactory.openSession().getMapper(MaterialHeadquartersOrderMapper.class)
				.selectByCompanyBatchNo(companyId, batchNo);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void exportMaterialHeadquartersToProject(MaterialHeadquartersOrder materialHeadquartersOrder, MaterialOrder materialOrder) {
		// 插入订单信息
		sqlSessionFactory.openSession().getMapper(MaterialHeadquartersOrderMapper.class)
				.insert(materialHeadquartersOrder);

		MaterialHeadquartersOrderDetailMapper materialHeadquartersOrderDetailMapper = sqlSessionFactory.openSession()
				.getMapper(MaterialHeadquartersOrderDetailMapper.class);
		MaterialHeadquartersMapper materialHeadquartersMapper = sqlSessionFactory.openSession().getMapper(
				MaterialHeadquartersMapper.class);

		for (MaterialHeadquartersOrderDetail materialHeadquartersOrderDetail : materialHeadquartersOrder
				.getMaterialHeadquartersOrderDetails()) {
			materialHeadquartersOrderDetail.setCompanyId(materialHeadquartersOrder.getCompanyId());
			materialHeadquartersOrderDetail.setCompanyId(materialHeadquartersOrder.getCompanyId());
			materialHeadquartersOrderDetail.setMaterialHeadquartersOrderId(materialHeadquartersOrder.getId());
			/*
			 * 校验材料库存
			 */
			MaterialHeadquarters materialHeadquartersCheck = materialHeadquartersMapper.selectByNum4Update(
					materialHeadquartersOrderDetail.getCompanyId(), materialHeadquartersOrderDetail.getNum());
			if (null == materialHeadquartersCheck
					|| materialHeadquartersCheck.getAmount().compareTo(materialHeadquartersOrderDetail.getAmount()) < 0) {
				throw new RuntimeException("库存不足");
			}
			materialHeadquartersCheck.setAmount(materialHeadquartersCheck.getAmount().subtract(
					materialHeadquartersOrderDetail.getAmount()));

			// 插入批次详情
			materialHeadquartersOrderDetailMapper.insert(materialHeadquartersOrderDetail);
			// 更新仓库存量
			materialHeadquartersMapper.updateByPrimaryKey(materialHeadquartersCheck);
		}
		
		materialOrderService.importMaterial(materialOrder);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Page<MaterialHeadquartersOrderDetail2Dto> pageDetailByFuzzy(BigInteger companyId, String staffName,
			Date cstCreate, String batchNo, Page<?> page) {
		WhNull.check(companyId);
		WhNull.checkPage(page);

		MaterialHeadquartersOrderDetailMapper materialHeadquartersOrderDetailMapper = sqlSessionFactory.openSession()
				.getMapper(MaterialHeadquartersOrderDetailMapper.class);

		// 获取总记录数
		Page<MaterialHeadquartersOrderDetail2Dto> pageCheck = new Page<MaterialHeadquartersOrderDetail2Dto>();
		pageCheck.setTotalItem(materialHeadquartersOrderDetailMapper.countByFuzzy(companyId, staffName, cstCreate,
				batchNo));
		pageCheck.setSize(page.getSize());
		pageCheck.setPagination(Math.min(pageCheck.getTotalPage(), page.getPagination()));

		if (pageCheck.getTotalItem() > 0) {
			// 获取分页项目列表信息
			pageCheck.setList(materialHeadquartersOrderDetailMapper.pageByFuzzy(companyId, staffName, cstCreate,
					batchNo, pageCheck));
		} else {
			pageCheck.setList(new ArrayList<MaterialHeadquartersOrderDetail2Dto>());
		}
		return pageCheck;
	}

}
