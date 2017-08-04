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
import top.warmheart.workerunion.server.model.CostAnalysisItemDeepen;
import top.warmheart.workerunion.server.service.CostAnalysisItemDeepenService;

@Service("costAnalysisItemDeepenServiceImpl")
@Scope("singleton")
public class CostAnalysisItemDeepenServiceImpl implements CostAnalysisItemDeepenService {
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
	public List<CostAnalysisItemDeepen> listByProjectId(BigInteger projectId) {
		WhNull.check(projectId);
		return sqlSessionFactory.openSession().getMapper(CostAnalysisItemDeepenMapper.class).selectListByProjectId(projectId);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public CostAnalysisItemDeepen getById(BigInteger id) {
		WhNull.check(id);
		return sqlSessionFactory.openSession().getMapper(CostAnalysisItemDeepenMapper.class).selectByPrimaryKey(id);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int add(CostAnalysisItemDeepen costAnalysisItemDeepen) {
		WhNull.check(costAnalysisItemDeepen);
		return sqlSessionFactory.openSession().getMapper(CostAnalysisItemDeepenMapper.class).insert(costAnalysisItemDeepen);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int removeById(BigInteger id) {
		WhNull.check(id);
		return sqlSessionFactory.openSession().getMapper(CostAnalysisItemDeepenMapper.class).deleteByPrimaryKey(id);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int modifyById(CostAnalysisItemDeepen costAnalysisItemDeepen) {
		WhNull.check(costAnalysisItemDeepen);
		return sqlSessionFactory.openSession().getMapper(CostAnalysisItemDeepenMapper.class).updateByPrimaryKey(costAnalysisItemDeepen);
	}

}
