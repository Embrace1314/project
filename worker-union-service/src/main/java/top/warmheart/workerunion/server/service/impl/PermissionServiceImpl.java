package top.warmheart.workerunion.server.service.impl;

import java.math.BigInteger;
import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import top.warmheart.workerunion.server.dao.PermissionMapper;
import top.warmheart.workerunion.server.model.Permission;
import top.warmheart.workerunion.server.service.PermissionService;

@Service("permissionServiceImpl")
@Scope("singleton")
public class PermissionServiceImpl implements PermissionService {
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
	public List<Permission> listByTeamRole(BigInteger teamRoleId) {
		return sqlSessionFactory.openSession().getMapper(PermissionMapper.class).listByTeamRole(teamRoleId);
	}

}
