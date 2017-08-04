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
import top.warmheart.workerunion.server.dao.CostAnalysisItemMapper;
import top.warmheart.workerunion.server.model.CostAnalysisItem;
import top.warmheart.workerunion.server.service.CostAnalysisItemService;

@Service("costAnalysisItemServiceImpl")
@Scope("singleton")
public class CostAnalysisItemServiceImpl implements CostAnalysisItemService {
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
	public List<CostAnalysisItem> listByProjectId(BigInteger projectId) {
		WhNull.check(projectId);
		return sqlSessionFactory.openSession().getMapper(CostAnalysisItemMapper.class).selectListByProjectId(projectId);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public CostAnalysisItem getById(BigInteger id) {
		WhNull.check(id);
		return sqlSessionFactory.openSession().getMapper(CostAnalysisItemMapper.class).selectByPrimaryKey(id);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int add(CostAnalysisItem costAnalysisItem) {
		WhNull.check(costAnalysisItem);
		return sqlSessionFactory.openSession().getMapper(CostAnalysisItemMapper.class).insert(costAnalysisItem);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int removeById(BigInteger id) {
		WhNull.check(id);
		return sqlSessionFactory.openSession().getMapper(CostAnalysisItemMapper.class).deleteByPrimaryKey(id);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int modifyById(CostAnalysisItem costAnalysisItem) {
		WhNull.check(costAnalysisItem);
		return sqlSessionFactory.openSession().getMapper(CostAnalysisItemMapper.class).updateByPrimaryKey(costAnalysisItem);
	}

}
