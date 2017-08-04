package top.warmheart.workerunion.server.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import top.warmheart.server.util.WhDateUtil;
import top.warmheart.server.util.dto.Page;
import top.warmheart.server.util.exception.WhNull;
import top.warmheart.workerunion.server.dao.AttachmentMapper;
import top.warmheart.workerunion.server.dao.ProjectMapper;
import top.warmheart.workerunion.server.model.Attachment;
import top.warmheart.workerunion.server.model.Project;
import top.warmheart.workerunion.server.service.ProjectService;

@Service("projectServiceImpl")
@Scope("singleton")
public class ProjectServiceImpl implements ProjectService {
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
	public void add(Project project, Attachment attachment) {
		WhNull.check(project);
		WhNull.check(attachment);

		sqlSessionFactory.openSession().getMapper(ProjectMapper.class).insert(project);
		attachment.setCompanyId(project.getCompanyId());
		attachment.setType(Attachment.TYPE_LETTER_OF_ACCEPTANCE);
		attachment.setProjectId(project.getId());
		sqlSessionFactory.openSession().getMapper(AttachmentMapper.class).insert(attachment);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Project getById(BigInteger id) {
		WhNull.check(id);
		return sqlSessionFactory.openSession().getMapper(ProjectMapper.class).selectByPrimaryKey(id);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Project getByNum(BigInteger companyId, String num) {
		WhNull.check(companyId);
		WhNull.checkTrimEmpty(num);
		return sqlSessionFactory.openSession().getMapper(ProjectMapper.class).selectByNum(companyId, num);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Page<Project> pageAll(BigInteger companyId, Page<?> page) {
		WhNull.check(companyId);
		WhNull.checkPage(page);

		ProjectMapper projectMapper = sqlSessionFactory.openSession().getMapper(ProjectMapper.class);

		// 获取总记录数
		Page<Project> pageCheck = new Page<Project>();
		pageCheck.setTotalItem(projectMapper.countAll(companyId));
		pageCheck.setSize(page.getSize());
		pageCheck.setPagination(Math.min(pageCheck.getTotalPage(), page.getPagination()));

		if (pageCheck.getTotalItem() > 0) {
			// 获取分页项目列表信息
			pageCheck.setList(projectMapper.pageAll(companyId, pageCheck));
		} else {
			pageCheck.setList(new ArrayList<Project>());
		}
		return pageCheck;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Page<Project> pageByStatusAndYear(BigInteger companyId, String fileStatus, Date year, Page<?> page) {
		WhNull.check(companyId);
		WhNull.checkTrimEmpty(fileStatus);
		WhNull.check(year);
		WhNull.checkPage(page);

		ProjectMapper projectMapper = sqlSessionFactory.openSession().getMapper(ProjectMapper.class);

		// 获取总记录数
		Page<Project> pageCheck = new Page<Project>();
		pageCheck.setTotalItem(projectMapper.countByStatusAndYear(companyId, fileStatus, WhDateUtil.getMinInYear(year),
				WhDateUtil.getMaxInYear(year)));
		pageCheck.setSize(page.getSize());
		pageCheck.setPagination(Math.min(pageCheck.getTotalPage(), page.getPagination()));

		// 存在项目数量，则获取
		if (pageCheck.getTotalItem() > 0) {
			// 获取分页项目列表信息
			pageCheck.setList(projectMapper.pageByStatusAndYear(companyId, fileStatus, WhDateUtil.getMinInYear(year),
					WhDateUtil.getMaxInYear(year), pageCheck));
		} else {
			pageCheck.setList(new ArrayList<Project>());
		}
		return pageCheck;

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Page<Project> pageByStatus(BigInteger companyId, String fileStatus, Page<?> page) {
		WhNull.check(companyId);
		WhNull.checkTrimEmpty(fileStatus);
		WhNull.checkPage(page);

		ProjectMapper projectMapper = sqlSessionFactory.openSession().getMapper(ProjectMapper.class);

		// 获取总记录数
		Page<Project> pageCheck = new Page<Project>();
		pageCheck.setTotalItem(projectMapper.countByStatus(companyId, fileStatus));
		pageCheck.setSize(page.getSize());
		pageCheck.setPagination(Math.min(pageCheck.getTotalPage(), page.getPagination()));

		// 存在项目数量，则获取
		if (pageCheck.getTotalItem() > 0) {
			// 获取分页项目列表信息
			pageCheck.setList(projectMapper.pageByStatus(companyId, fileStatus, pageCheck));
		} else {
			pageCheck.setList(new ArrayList<Project>());
		}
		return pageCheck;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Page<Project> pageByYear(BigInteger companyId, Date year, Page<?> page) {
		WhNull.check(companyId);
		WhNull.check(year);
		WhNull.checkPage(page);

		ProjectMapper projectMapper = sqlSessionFactory.openSession().getMapper(ProjectMapper.class);

		// 获取总记录数
		Page<Project> pageCheck = new Page<Project>();
		pageCheck.setTotalItem(projectMapper.countByYear(companyId, WhDateUtil.getMinInYear(year),
				WhDateUtil.getMaxInYear(year)));
		pageCheck.setSize(page.getSize());
		pageCheck.setPagination(Math.min(pageCheck.getTotalPage(), page.getPagination()));

		// 存在项目数量，则获取
		if (pageCheck.getTotalItem() > 0) {
			// 获取分页项目列表信息
			pageCheck.setList(projectMapper.pageByYear(companyId, WhDateUtil.getMinInYear(year),
					WhDateUtil.getMaxInYear(year), pageCheck));
		} else {
			pageCheck.setList(new ArrayList<Project>());
		}
		return pageCheck;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Page<Project> pageAllEx(BigInteger staffId, BigInteger companyId, Page<?> page) {
		WhNull.check(staffId);
		WhNull.check(companyId);
		WhNull.checkPage(page);

		ProjectMapper projectMapper = sqlSessionFactory.openSession().getMapper(ProjectMapper.class);

		// 获取总记录数
		Page<Project> pageCheck = new Page<Project>();
		pageCheck.setTotalItem(projectMapper.countAllEx(staffId, companyId));
		pageCheck.setSize(page.getSize());
		pageCheck.setPagination(Math.min(pageCheck.getTotalPage(), page.getPagination()));

		if (pageCheck.getTotalItem() > 0) {
			// 获取分页项目列表信息
			pageCheck.setList(projectMapper.pageAllEx(staffId, companyId, pageCheck));
		} else {
			pageCheck.setList(new ArrayList<Project>());
		}
		return pageCheck;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Page<Project> pageByStatusAndYearEx(BigInteger staffId, BigInteger companyId, String fileStatus, Date year,
			Page<?> page) {
		WhNull.check(staffId);
		WhNull.check(companyId);
		WhNull.checkTrimEmpty(fileStatus);
		WhNull.check(year);
		WhNull.checkPage(page);

		ProjectMapper projectMapper = sqlSessionFactory.openSession().getMapper(ProjectMapper.class);

		// 获取总记录数
		Page<Project> pageCheck = new Page<Project>();
		pageCheck.setTotalItem(projectMapper.countByStatusAndYearEx(staffId, companyId, fileStatus,
				WhDateUtil.getMinInYear(year), WhDateUtil.getMaxInYear(year)));
		pageCheck.setSize(page.getSize());
		pageCheck.setPagination(Math.min(pageCheck.getTotalPage(), page.getPagination()));

		// 存在项目数量，则获取
		if (pageCheck.getTotalItem() > 0) {
			// 获取分页项目列表信息
			pageCheck.setList(projectMapper.pageByStatusAndYearEx(staffId, companyId, fileStatus,
					WhDateUtil.getMinInYear(year), WhDateUtil.getMaxInYear(year), pageCheck));
		} else {
			pageCheck.setList(new ArrayList<Project>());
		}
		return pageCheck;

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Page<Project> pageByStatusEx(BigInteger staffId, BigInteger companyId, String fileStatus, Page<?> page) {
		WhNull.check(staffId);
		WhNull.check(companyId);
		WhNull.checkTrimEmpty(fileStatus);
		WhNull.checkPage(page);

		ProjectMapper projectMapper = sqlSessionFactory.openSession().getMapper(ProjectMapper.class);

		// 获取总记录数
		Page<Project> pageCheck = new Page<Project>();
		pageCheck.setTotalItem(projectMapper.countByStatusEx(staffId, companyId, fileStatus));
		pageCheck.setSize(page.getSize());
		pageCheck.setPagination(Math.min(pageCheck.getTotalPage(), page.getPagination()));

		// 存在项目数量，则获取
		if (pageCheck.getTotalItem() > 0) {
			// 获取分页项目列表信息
			pageCheck.setList(projectMapper.pageByStatusEx(staffId, companyId, fileStatus, pageCheck));
		} else {
			pageCheck.setList(new ArrayList<Project>());
		}
		return pageCheck;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Page<Project> pageByYearEx(BigInteger staffId, BigInteger companyId, Date year, Page<?> page) {
		WhNull.check(staffId);
		WhNull.check(companyId);
		WhNull.check(year);
		WhNull.checkPage(page);

		ProjectMapper projectMapper = sqlSessionFactory.openSession().getMapper(ProjectMapper.class);

		// 获取总记录数
		Page<Project> pageCheck = new Page<Project>();
		pageCheck.setTotalItem(projectMapper.countByYearEx(staffId, companyId, WhDateUtil.getMinInYear(year),
				WhDateUtil.getMaxInYear(year)));
		pageCheck.setSize(page.getSize());
		pageCheck.setPagination(Math.min(pageCheck.getTotalPage(), page.getPagination()));

		// 存在项目数量，则获取
		if (pageCheck.getTotalItem() > 0) {
			// 获取分页项目列表信息
			pageCheck.setList(projectMapper.pageByYearEx(staffId, companyId, WhDateUtil.getMinInYear(year),
					WhDateUtil.getMaxInYear(year), pageCheck));
		} else {
			pageCheck.setList(new ArrayList<Project>());
		}
		return pageCheck;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Project> listSimpleByCompanyId(BigInteger companyId) {
		return sqlSessionFactory.openSession().getMapper(ProjectMapper.class).selectListSimpleByCompanyId(companyId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int modifyById(Project project) {
		return sqlSessionFactory.openSession().getMapper(ProjectMapper.class).updateByPrimaryKey(project);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Project> listSimpleByFileStatus(BigInteger companyId, String fileStatus) {
		return sqlSessionFactory.openSession().getMapper(ProjectMapper.class).selectListSimpleByFileStatus(companyId, fileStatus);
	}

}
