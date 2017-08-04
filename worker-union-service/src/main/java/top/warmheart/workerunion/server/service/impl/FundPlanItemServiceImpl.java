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
import top.warmheart.workerunion.server.dao.CostAnalysisItemDeepenMapper;
import top.warmheart.workerunion.server.dao.FundPlanItemMapper;
import top.warmheart.workerunion.server.dto.FundPlanItemDto;
import top.warmheart.workerunion.server.model.CostAnalysisItemDeepen;
import top.warmheart.workerunion.server.model.FundPlanItem;
import top.warmheart.workerunion.server.service.FundPlanItemService;

@Service("fundPlanItemServiceImpl")
@Scope("singleton")
public class FundPlanItemServiceImpl implements FundPlanItemService {
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
	public List<FundPlanItemDto> listByProjectId(BigInteger projectId) {
		WhNull.check(projectId);
		return sqlSessionFactory.openSession().getMapper(FundPlanItemMapper.class).selectListByProjectId(projectId);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public FundPlanItem getById(BigInteger id) {
		WhNull.check(id);
		return sqlSessionFactory.openSession().getMapper(FundPlanItemMapper.class).selectByPrimaryKey(id);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int add(FundPlanItem fundPlanItem) {
		WhNull.check(fundPlanItem);
		// 校验关联的成本深化项
		CostAnalysisItemDeepen costAnalysisItemDeepenCheck = sqlSessionFactory.openSession()
				.getMapper(CostAnalysisItemDeepenMapper.class)
				.selectByPrimaryKey4Update(fundPlanItem.getCostAnalysisItemDeepenId());
		if (null == costAnalysisItemDeepenCheck) {
			throw new RuntimeException();
		}
		return sqlSessionFactory.openSession().getMapper(FundPlanItemMapper.class).insert(fundPlanItem);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int removeById(BigInteger id) {
		WhNull.check(id);
		return sqlSessionFactory.openSession().getMapper(FundPlanItemMapper.class).deleteByPrimaryKey(id);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int modifyById(FundPlanItem fundPlanItem) {
		WhNull.check(fundPlanItem);
		// 校验关联的成本深化项
		CostAnalysisItemDeepen costAnalysisItemDeepenCheck = sqlSessionFactory.openSession()
				.getMapper(CostAnalysisItemDeepenMapper.class)
				.selectByPrimaryKey4Update(fundPlanItem.getCostAnalysisItemDeepenId());
		if (null == costAnalysisItemDeepenCheck) {
			throw new RuntimeException();
		}
		return sqlSessionFactory.openSession().getMapper(FundPlanItemMapper.class).updateByPrimaryKey(fundPlanItem);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<FundPlanItem> listByCostAnalysisItemId(BigInteger costAnalysisItemId) {
		return sqlSessionFactory.openSession().getMapper(FundPlanItemMapper.class).selectListByCostAnalysisItemId(costAnalysisItemId);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<FundPlanItem> listSimpleByProjectId(BigInteger projectId) {
		return sqlSessionFactory.openSession().getMapper(FundPlanItemMapper.class).selectListSimpleByProjectId(projectId);
	}

}
