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
import top.warmheart.workerunion.server.dao.MaterialMapper;
import top.warmheart.workerunion.server.dao.MaterialOrderDetailMapper;
import top.warmheart.workerunion.server.dao.MaterialOrderMapper;
import top.warmheart.workerunion.server.dto.MaterialOrderDetail2Dto;
import top.warmheart.workerunion.server.model.Material;
import top.warmheart.workerunion.server.model.MaterialOrder;
import top.warmheart.workerunion.server.model.MaterialOrderDetail;
import top.warmheart.workerunion.server.service.MaterialOrderService;

@Service("materialOrderServiceImpl")
@Scope("singleton")
public class MaterialOrderServiceImpl implements MaterialOrderService {
	private SqlSessionFactory sqlSessionFactory;

	public SqlSessionFactory getSqlSessionFactory() {
		return sqlSessionFactory;
	}

	@Resource(name = "sqlSessionFactory")
	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Page<MaterialOrder> pageByFuzzy(BigInteger projectId, String staffName, Date cstCreate, String batchNo,
			Page<?> page) {
		WhNull.check(projectId);
		WhNull.checkPage(page);

		MaterialOrderMapper materialOrderMapper = sqlSessionFactory.openSession().getMapper(MaterialOrderMapper.class);

		// 获取总记录数
		Page<MaterialOrder> pageCheck = new Page<MaterialOrder>();
		pageCheck.setTotalItem(materialOrderMapper.countByFuzzy(projectId, staffName, cstCreate, batchNo));
		pageCheck.setSize(page.getSize());
		pageCheck.setPagination(Math.min(pageCheck.getTotalPage(), page.getPagination()));

		if (pageCheck.getTotalItem() > 0) {
			// 获取分页项目列表信息
			pageCheck.setList(materialOrderMapper.pageByFuzzy(projectId, staffName, cstCreate, batchNo, pageCheck));
		} else {
			pageCheck.setList(new ArrayList<MaterialOrder>());
		}
		return pageCheck;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public BigDecimal amountOrderDetailByType(BigInteger projectId, String num, String type) {
		return sqlSessionFactory.openSession().getMapper(MaterialOrderDetailMapper.class)
				.amountByType(projectId, num, type);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Page<MaterialOrderDetail2Dto> pageOrderDetailByNum(BigInteger projectId, String num, Page<?> page) {
		WhNull.check(projectId);
		WhNull.checkTrimEmpty(num);
		WhNull.checkPage(page);

		MaterialOrderDetailMapper materialOrderDetailMapper = sqlSessionFactory.openSession().getMapper(
				MaterialOrderDetailMapper.class);

		// 获取总记录数
		Page<MaterialOrderDetail2Dto> pageCheck = new Page<MaterialOrderDetail2Dto>();
		pageCheck.setTotalItem(materialOrderDetailMapper.countByNum(projectId, num));
		pageCheck.setSize(page.getSize());
		pageCheck.setPagination(Math.min(pageCheck.getTotalPage(), page.getPagination()));

		if (pageCheck.getTotalItem() > 0) {
			// 获取分页项目列表信息
			pageCheck.setList(materialOrderDetailMapper.pageByNum(projectId, num, pageCheck));
		} else {
			pageCheck.setList(new ArrayList<MaterialOrderDetail2Dto>());
		}
		return pageCheck;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void importMaterial(MaterialOrder materialOrder) {
		materialOrder.setType(MaterialOrder.TYPE_IMPORT);
		// 插入批次
		sqlSessionFactory.openSession().getMapper(MaterialOrderMapper.class).insert(materialOrder);

		MaterialOrderDetailMapper materialOrderDetailMapper = sqlSessionFactory.openSession().getMapper(
				MaterialOrderDetailMapper.class);
		MaterialMapper materialMapper = sqlSessionFactory.openSession().getMapper(MaterialMapper.class);

		for (MaterialOrderDetail materialOrderDetail : materialOrder.getMaterialOrderDetails()) {
			materialOrderDetail.setCompanyId(materialOrder.getCompanyId());
			materialOrderDetail.setProjectId(materialOrder.getProjectId());
			materialOrderDetail.setMaterialOrderId(materialOrder.getId());

			// 插入批次详情
			materialOrderDetailMapper.insert(materialOrderDetail);
			Material material = new Material();
			material.setCompanyId(materialOrderDetail.getCompanyId());
			material.setProjectId(materialOrderDetail.getProjectId());
			material.setName(materialOrderDetail.getName());
			material.setNum(materialOrderDetail.getNum());
			material.setModel(materialOrderDetail.getModel());
			material.setUnit(materialOrderDetail.getUnit());
			material.setAmount(materialOrderDetail.getAmount());
			material.setAmountFreeze(BigDecimal.valueOf(0));

			// 更新仓库存量
			materialMapper.insertOrAccumulate(material);
		}

	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public MaterialOrder getByProjectBatchNo(BigInteger projectId, String batchNo) {
		return sqlSessionFactory.openSession().getMapper(MaterialOrderMapper.class)
				.selectByProjectBatchNo(projectId, batchNo);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void exportMaterial(MaterialOrder materialOrder) {
		materialOrder.setType(MaterialOrder.TYPE_EXPORT);
		// 插入批次
		sqlSessionFactory.openSession().getMapper(MaterialOrderMapper.class).insert(materialOrder);
		MaterialOrderDetailMapper materialOrderDetailMapper = sqlSessionFactory.openSession().getMapper(
				MaterialOrderDetailMapper.class);
		MaterialMapper materialMapper = sqlSessionFactory.openSession().getMapper(MaterialMapper.class);

		for (MaterialOrderDetail materialOrderDetail : materialOrder.getMaterialOrderDetails()) {
			materialOrderDetail.setCompanyId(materialOrder.getCompanyId());
			materialOrderDetail.setProjectId(materialOrder.getProjectId());
			materialOrderDetail.setMaterialOrderId(materialOrder.getId());
			/*
			 * 校验材料库存
			 */
			Material materialCheck = materialMapper.selectByNum4Update(materialOrderDetail.getProjectId(),
					materialOrderDetail.getNum());
			if (null == materialCheck || materialCheck.getAmount().compareTo(materialOrderDetail.getAmount()) < 0) {
				throw new RuntimeException("库存不足");
			}
			materialCheck.setAmount(materialCheck.getAmount().subtract(materialOrderDetail.getAmount()));
			
			// 插入批次详情
			materialOrderDetailMapper.insert(materialOrderDetail);
			// 更新仓库存量
			materialMapper.updateByPrimaryKey(materialCheck);
		}
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void exportFreezeMaterial(MaterialOrder materialOrder) {
		materialOrder.setType(MaterialOrder.TYPE_EXPORT);
		// 插入批次
		sqlSessionFactory.openSession().getMapper(MaterialOrderMapper.class).insert(materialOrder);
		MaterialOrderDetailMapper materialOrderDetailMapper = sqlSessionFactory.openSession().getMapper(
				MaterialOrderDetailMapper.class);
		MaterialMapper materialMapper = sqlSessionFactory.openSession().getMapper(MaterialMapper.class);

		for (MaterialOrderDetail materialOrderDetail : materialOrder.getMaterialOrderDetails()) {
			materialOrderDetail.setCompanyId(materialOrder.getCompanyId());
			materialOrderDetail.setProjectId(materialOrder.getProjectId());
			materialOrderDetail.setMaterialOrderId(materialOrder.getId());
			/*
			 * 校验材料库存
			 */
			Material materialCheck = materialMapper.selectByNum4Update(materialOrderDetail.getProjectId(),
					materialOrderDetail.getNum());
			if (null == materialCheck || materialCheck.getAmountFreeze().compareTo(materialOrderDetail.getAmount()) < 0) {
				throw new RuntimeException("库存不足");
			}
			materialCheck.setAmountFreeze(materialCheck.getAmountFreeze().subtract(materialOrderDetail.getAmount()));
			
			// 插入批次详情
			materialOrderDetailMapper.insert(materialOrderDetail);
			// 更新仓库存量
			materialMapper.updateByPrimaryKey(materialCheck);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly=true)
	public Page<MaterialOrderDetail2Dto> pageDetailByFuzzy(BigInteger projectId, String staffName, Date cstCreate,
			String batchNo, Page<?> page) {
		WhNull.check(projectId);
		WhNull.checkPage(page);

		MaterialOrderDetailMapper materialOrderDetailMapper = sqlSessionFactory.openSession().getMapper(MaterialOrderDetailMapper.class);

		// 获取总记录数
		Page<MaterialOrderDetail2Dto> pageCheck = new Page<MaterialOrderDetail2Dto>();
		pageCheck.setTotalItem(materialOrderDetailMapper.countByFuzzy(projectId, staffName, cstCreate, batchNo));
		pageCheck.setSize(page.getSize());
		pageCheck.setPagination(Math.min(pageCheck.getTotalPage(), page.getPagination()));

		if (pageCheck.getTotalItem() > 0) {
			// 获取分页项目列表信息
			pageCheck.setList(materialOrderDetailMapper.pageByFuzzy(projectId, staffName, cstCreate, batchNo, pageCheck));
		} else {
			pageCheck.setList(new ArrayList<MaterialOrderDetail2Dto>());
		}
		return pageCheck;
	}

}
