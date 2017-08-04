package top.warmheart.workerunion.server.service.impl;

import java.math.BigInteger;
import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import top.warmheart.workerunion.server.dao.RoleMapper;
import top.warmheart.workerunion.server.dao.StaffHasRoleMapper;
import top.warmheart.workerunion.server.model.Role;
import top.warmheart.workerunion.server.model.StaffHasRole;
import top.warmheart.workerunion.server.service.RoleService;

@Service("roleServiceImpl")
@Scope("singleton")
public class RoleServiceImpl implements RoleService{
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
	public List<Role> listByCompanyId(BigInteger companyId) {
		return sqlSessionFactory.openSession().getMapper(RoleMapper.class).selectListByCompanyId(companyId);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Role> listByStaffId(BigInteger staffId, BigInteger companyId) {
		return sqlSessionFactory.openSession().getMapper(RoleMapper.class).selectListByStaff(staffId, companyId);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Role getById(BigInteger id) {
		return sqlSessionFactory.openSession().getMapper(RoleMapper.class).selectByPrimaryKey(id);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void replace(BigInteger staffId, List<Role> roles) {
		sqlSessionFactory.openSession().getMapper(StaffHasRoleMapper.class).deleteByStaffId(staffId);
		for(Role role:roles){
			sqlSessionFactory.openSession().getMapper(StaffHasRoleMapper.class).insert(new StaffHasRole(staffId, role.getId()));
		}
		
	}

}
