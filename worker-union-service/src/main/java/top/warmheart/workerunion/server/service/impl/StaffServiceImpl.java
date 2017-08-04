/**
 * Copyright © WarmHeart Intelligence Science&Technology(NanJing) Company, Limited.
 * All Rights Reserved
 */
package top.warmheart.workerunion.server.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import top.warmheart.server.util.dto.Page;
import top.warmheart.server.util.exception.WhNull;
import top.warmheart.workerunion.server.dao.PermissionMapper;
import top.warmheart.workerunion.server.dao.RoleMapper;
import top.warmheart.workerunion.server.dao.StaffMapper;
import top.warmheart.workerunion.server.dto.StaffDto;
import top.warmheart.workerunion.server.model.Project;
import top.warmheart.workerunion.server.model.Role;
import top.warmheart.workerunion.server.model.Staff;
import top.warmheart.workerunion.server.service.StaffService;

@Service("staffServiceImpl")
@Scope("singleton")
public class StaffServiceImpl implements StaffService {
	private SqlSessionFactory sqlSessionFactory;

	public SqlSessionFactory getSqlSessionFactory() {
		return sqlSessionFactory;
	}

	@Resource(name = "sqlSessionFactory")
	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int add(Staff staff) {
		WhNull.check(staff);
		return sqlSessionFactory.openSession().getMapper(StaffMapper.class).insert(staff);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int modifyById(Staff staff) {
		WhNull.check(staff);
		return sqlSessionFactory.openSession().getMapper(StaffMapper.class).updateByPrimaryKey(staff);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Staff getByJobNo(BigInteger companyId, String jobNo) {
		WhNull.check(companyId);
		WhNull.checkTrimEmpty(jobNo);
		return sqlSessionFactory.openSession().getMapper(StaffMapper.class).selectByJobNo(companyId, jobNo);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Staff getById(BigInteger id) {
		WhNull.check(id);
		return sqlSessionFactory.openSession().getMapper(StaffMapper.class).selectByPrimaryKey(id);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int removeById(BigInteger id) {
		WhNull.check(id);
		return sqlSessionFactory.openSession().getMapper(StaffMapper.class).deleteById(id);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public StaffDto getDetailById(BigInteger id) {
		WhNull.check(id);
		return sqlSessionFactory.openSession().getMapper(StaffMapper.class).selectDetailById(id, Project.COLLAPSE_STATUS_GOING);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Staff getAuthenticationStaff(BigInteger id) {
		WhNull.check(id);
		Staff staffEx = sqlSessionFactory.openSession().getMapper(StaffMapper.class).selectByPrimaryKey(id);
		if (null == staffEx) {
			return null;
		}

		// 用户存在，获取角色信息
		List<Role> roles = sqlSessionFactory.openSession().getMapper(RoleMapper.class).selectListByStaff(staffEx.getId(), staffEx.getCompanyId());
		staffEx.setRoles(roles);
		if (!roles.isEmpty()) {
			// 角色非空，获取权限信息
			for (Role role : roles) {
				role.setPermissions(sqlSessionFactory.openSession().getMapper(PermissionMapper.class).listByRole(role.getId()));
			}
		}

		return staffEx;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Page<StaffDto> pageByFuzzy(StaffDto staffDto, Page<?> page) {
		WhNull.check(staffDto);
		WhNull.checkPage(page);
		StaffMapper staffMapper = sqlSessionFactory.openSession().getMapper(StaffMapper.class);
		
		Page<StaffDto> pageEx = new Page<StaffDto>();
		pageEx.setTotalItem(staffMapper.countByFuzzy(staffDto, Project.COLLAPSE_STATUS_GOING));
		pageEx.setSize(page.getSize());
		pageEx.setPagination(Math.min(pageEx.getTotalPage(), page.getPagination()));
		// 存在项目数量，则获取
		if (pageEx.getTotalItem() > 0) {
			// 获取分页项目列表信息
			pageEx.setList(staffMapper.pageByFuzzy(staffDto,Project.COLLAPSE_STATUS_GOING, pageEx));
		} else {
			pageEx.setList(new ArrayList<StaffDto>());
		}
		return pageEx;
	}
}
