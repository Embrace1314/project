package top.warmheart.workerunion.server.service.impl;

import java.math.BigInteger;
import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import top.warmheart.server.util.exception.WhNull;
import top.warmheart.workerunion.server.dao.FundPlanItemMapper;
import top.warmheart.workerunion.server.dao.ResourceImplementItemMapper;
import top.warmheart.workerunion.server.dto.ResourceImplementItemDto;
import top.warmheart.workerunion.server.model.FundPlanItem;
import top.warmheart.workerunion.server.model.ResourceImplementItem;
import top.warmheart.workerunion.server.service.ResourceImplementItemService;

@Service("resourceImplementItemServiceImpl")
@Scope("singleton")
public class ResourceImplementItemServiceImpl implements ResourceImplementItemService {
	private SqlSessionFactory sqlSessionFactory;

	public SqlSessionFactory getSqlSessionFactory() {
		return sqlSessionFactory;
	}

	@Resource(name = "sqlSessionFactory")
	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<ResourceImplementItemDto> listByProjectId(BigInteger projectId) {
		WhNull.check(projectId);
		return sqlSessionFactory.openSession().getMapper(ResourceImplementItemMapper.class)
				.selectListByProjectId(projectId);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public ResourceImplementItem getById(BigInteger id) {
		WhNull.check(id);
		return sqlSessionFactory.openSession().getMapper(ResourceImplementItemMapper.class).selectByPrimaryKey(id);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int add(ResourceImplementItem resourceImplementItem) {
		WhNull.check(resourceImplementItem);
		// 校验关联的资金计划项
		if (null != resourceImplementItem.getFundPlanItemId()) {
			FundPlanItem fundPlanItemCheck = sqlSessionFactory.openSession().getMapper(FundPlanItemMapper.class)
					.selectByPrimaryKey4Update(resourceImplementItem.getFundPlanItemId());
			if (null == fundPlanItemCheck) {
				throw new RuntimeException();
			}
		}

		return sqlSessionFactory.openSession().getMapper(ResourceImplementItemMapper.class)
				.insert(resourceImplementItem);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int removeById(BigInteger id) {
		WhNull.check(id);
		return sqlSessionFactory.openSession().getMapper(ResourceImplementItemMapper.class).deleteByPrimaryKey(id);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int modifyById(ResourceImplementItem resourceImplementItem) {
		WhNull.check(resourceImplementItem);
		// 校验关联的资金计划项
		if (null != resourceImplementItem.getFundPlanItemId()) {
			FundPlanItem fundPlanItemCheck = sqlSessionFactory.openSession().getMapper(FundPlanItemMapper.class)
					.selectByPrimaryKey4Update(resourceImplementItem.getFundPlanItemId());
			if (null == fundPlanItemCheck) {
				throw new RuntimeException();
			}
		}
		return sqlSessionFactory.openSession().getMapper(ResourceImplementItemMapper.class)
				.updateByPrimaryKey(resourceImplementItem);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<ResourceImplementItem> listByFundPlanItemId(BigInteger fundPlanItemId) {
		return sqlSessionFactory.openSession().getMapper(ResourceImplementItemMapper.class)
				.selectListByFundPlanItemId(fundPlanItemId);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<ResourceImplementItem> listSimpleByProjectId(BigInteger projectId) {
		return sqlSessionFactory.openSession().getMapper(ResourceImplementItemMapper.class)
				.selectListSimpleByProjectId(projectId);
	}

}
