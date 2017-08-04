package top.warmheart.workerunion.server.service.impl;

import java.math.BigInteger;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import top.warmheart.workerunion.server.dao.InternalSettlementMapper;
import top.warmheart.workerunion.server.model.InternalSettlement;
import top.warmheart.workerunion.server.service.InternalSettlementService;

@Service("internalSettlementServiceImpl")
@Scope("singleton")
public class InternalSettlementServiceImpl implements InternalSettlementService {
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
	public InternalSettlement getByProjectId(BigInteger projectId) {
		return sqlSessionFactory.openSession().getMapper(InternalSettlementMapper.class).selectByPrimaryKey(projectId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int replace(InternalSettlement internalSettlement) {
		return sqlSessionFactory.openSession().getMapper(InternalSettlementMapper.class).replace(internalSettlement);
	}

}
