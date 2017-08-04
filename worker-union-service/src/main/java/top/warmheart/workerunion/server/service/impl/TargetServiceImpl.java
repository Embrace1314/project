package top.warmheart.workerunion.server.service.impl;

import java.math.BigInteger;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import top.warmheart.workerunion.server.dao.TargetMapper;
import top.warmheart.workerunion.server.model.Target;
import top.warmheart.workerunion.server.service.TargetService;

@Service("targetServiceImpl")
@Scope("singleton")
public class TargetServiceImpl implements TargetService {
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
	public Target getByProjectId(BigInteger projectId) {
		return sqlSessionFactory.openSession().getMapper(TargetMapper.class).selectByPrimaryKey(projectId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int replace(Target target) {
		return sqlSessionFactory.openSession().getMapper(TargetMapper.class).replace(target);
	}
}
