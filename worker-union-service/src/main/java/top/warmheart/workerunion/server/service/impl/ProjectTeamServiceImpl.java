package top.warmheart.workerunion.server.service.impl;

import java.math.BigInteger;
import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import top.warmheart.workerunion.server.dao.ProjectTeamMapper;
import top.warmheart.workerunion.server.dto.Staff3Dto;
import top.warmheart.workerunion.server.model.ProjectTeam;
import top.warmheart.workerunion.server.service.ProjectTeamService;

@Service("projectTeamServiceImpl")
@Scope("singleton")
public class ProjectTeamServiceImpl implements ProjectTeamService {
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
	public boolean contains(BigInteger projectId, BigInteger staffId) {
		return null != sqlSessionFactory.openSession().getMapper(ProjectTeamMapper.class).selectByPrimaryKey(projectId, staffId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int add(BigInteger projectId, BigInteger staffId, BigInteger teamRoleId) {
		ProjectTeam projectTeam = new ProjectTeam();
		projectTeam.setProjectId(projectId);
		projectTeam.setStaffId(staffId);
		projectTeam.setTeamRoleId(teamRoleId);
		return sqlSessionFactory.openSession().getMapper(ProjectTeamMapper.class).insert(projectTeam);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public boolean roleContains(BigInteger projectId, BigInteger teamRoleId) {
		return null != sqlSessionFactory.openSession().getMapper(ProjectTeamMapper.class).selectByProjectRole(projectId, teamRoleId);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public ProjectTeam getByProjectStaff(BigInteger projectId, BigInteger staffId) {
		return sqlSessionFactory.openSession().getMapper(ProjectTeamMapper.class).selectByPrimaryKey(projectId, staffId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int deleteByProjectStaff(BigInteger projectId, BigInteger staffId) {
		return sqlSessionFactory.openSession().getMapper(ProjectTeamMapper.class).deleteByPrimaryKey(projectId, staffId);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Staff3Dto> listStaffByProjectId(BigInteger projectId) {
		return sqlSessionFactory.openSession().getMapper(ProjectTeamMapper.class).selectListStaffByProjectId(projectId);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Staff3Dto getByProjectTeamType(BigInteger projectId, String type) {
		return sqlSessionFactory.openSession().getMapper(ProjectTeamMapper.class).selectByProjectTeamType(projectId, type);
	}

}
