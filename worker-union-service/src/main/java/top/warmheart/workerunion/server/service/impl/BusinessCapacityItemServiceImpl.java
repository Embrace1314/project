package top.warmheart.workerunion.server.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import top.warmheart.server.util.dto.Page;
import top.warmheart.server.util.exception.WhNull;
import top.warmheart.workerunion.server.dao.BusinessCapacityItemMapper;
import top.warmheart.workerunion.server.dto.BusinessCapacityItemDto;
import top.warmheart.workerunion.server.model.BusinessCapacityItem;
import top.warmheart.workerunion.server.service.BusinessCapacityItemService;

@Service("businessCapacityItemServiceImpl")
@Scope("singleton")
public class BusinessCapacityItemServiceImpl implements BusinessCapacityItemService {
	private SqlSessionFactory sqlSessionFactory;

	public SqlSessionFactory getSqlSessionFactory() {
		return sqlSessionFactory;
	}

	@Resource(name = "sqlSessionFactory")
	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Page<BusinessCapacityItemDto> pageByProjectId(BigInteger projectId, Page<Void> page) {
		WhNull.check(projectId);
		WhNull.checkPage(page);

		BusinessCapacityItemMapper businessCapacityItemMapper = sqlSessionFactory.openSession().getMapper(BusinessCapacityItemMapper.class);

		// 获取总记录数
		Page<BusinessCapacityItemDto> pageCheck = new Page<BusinessCapacityItemDto>();
		pageCheck.setTotalItem(businessCapacityItemMapper.countByProjectId(projectId));
		pageCheck.setSize(page.getSize());
		pageCheck.setPagination(Math.min(pageCheck.getTotalPage(), page.getPagination()));

		if (pageCheck.getTotalItem() > 0) {
			// 获取分页项目列表信息
			pageCheck.setList(businessCapacityItemMapper.pageByProjectId(projectId, pageCheck));
		} else {
			pageCheck.setList(new ArrayList<BusinessCapacityItemDto>());
		}
		return pageCheck;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public BusinessCapacityItem getByProjectYearMonth(BigInteger projectId, Integer year, Integer month) {
		return sqlSessionFactory.openSession().getMapper(BusinessCapacityItemMapper.class).selectByProjectYearMonth(projectId, year, month);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int add(BusinessCapacityItem businessCapacityItem) {
		return sqlSessionFactory.openSession().getMapper(BusinessCapacityItemMapper.class).insert(businessCapacityItem);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public BusinessCapacityItem getById(BigInteger id) {
		return sqlSessionFactory.openSession().getMapper(BusinessCapacityItemMapper.class).selectByPrimaryKey(id);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int modifyById(BusinessCapacityItem businessCapacityItem) {
		return sqlSessionFactory.openSession().getMapper(BusinessCapacityItemMapper.class).updateByPrimaryKey(businessCapacityItem);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int removeById(BigInteger id) {
		return sqlSessionFactory.openSession().getMapper(BusinessCapacityItemMapper.class).deleteByPrimaryKey(id);
	}

}
