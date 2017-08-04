package top.warmheart.workerunion.server.service.impl;

import java.math.BigInteger;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import top.warmheart.workerunion.server.dao.TeamRoleMapper;
import top.warmheart.workerunion.server.model.TeamRole;
import top.warmheart.workerunion.server.service.TeamRoleService;

@Service("teamRoleServiceImpl")
@Scope("singleton")
public class TeamRoleServiceImpl implements TeamRoleService {
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
	public TeamRole getByCompanyType(BigInteger companyId, String type) {
		return sqlSessionFactory.openSession().getMapper(TeamRoleMapper.class).selectByCompanyType(companyId, type);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public TeamRole getById(BigInteger id) {
		return sqlSessionFactory.openSession().getMapper(TeamRoleMapper.class).selectByPrimaryKey(id);
	}

}
