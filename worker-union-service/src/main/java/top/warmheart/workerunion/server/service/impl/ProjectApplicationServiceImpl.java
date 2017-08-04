package top.warmheart.workerunion.server.service.impl;

import java.math.BigInteger;
import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import top.warmheart.workerunion.server.dao.ProjectApplicationMapper;
import top.warmheart.workerunion.server.dto.Staff2Dto;
import top.warmheart.workerunion.server.model.Project;
import top.warmheart.workerunion.server.model.ProjectApplication;
import top.warmheart.workerunion.server.service.ProjectApplicationService;

@Service("projectApplicationServiceImpl")
@Scope("singleton")
public class ProjectApplicationServiceImpl implements ProjectApplicationService {
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
	public List<Staff2Dto> listStaffByProjectId(BigInteger projectId) {
		return sqlSessionFactory.openSession().getMapper(ProjectApplicationMapper.class).selectListStaffByProjectId(projectId);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public ProjectApplication getByProjectStaff(BigInteger projectId, BigInteger staffId) {
		return sqlSessionFactory.openSession().getMapper(ProjectApplicationMapper.class).selectByPrimaryKey(projectId, staffId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int deleteByProjectStaff(BigInteger projectId, BigInteger staffId) {
		return sqlSessionFactory.openSession().getMapper(ProjectApplicationMapper.class).deleteByPrimaryKey(projectId, staffId);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<ProjectApplication> getValidByStaff(BigInteger staffId) {
		return sqlSessionFactory.openSession().getMapper(ProjectApplicationMapper.class).selectValidByStaff(staffId, Project.COLLAPSE_STATUS_GOING);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public ProjectApplication getByProjectRole(BigInteger projectId, String type) {
		return sqlSessionFactory.openSession().getMapper(ProjectApplicationMapper.class).selectByProjectRole(projectId,type);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int add(BigInteger projectId, BigInteger staffId, String type) {
		return sqlSessionFactory.openSession().getMapper(ProjectApplicationMapper.class).insertUnique(projectId, staffId, type, Project.COLLAPSE_STATUS_GOING);
	}

}
