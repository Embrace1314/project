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
import top.warmheart.workerunion.server.dao.ResourceImplementItemMapper;
import top.warmheart.workerunion.server.dao.SettlementItemMapper;
import top.warmheart.workerunion.server.dto.SettlementItem2Dto;
import top.warmheart.workerunion.server.dto.SettlementItemDto;
import top.warmheart.workerunion.server.model.ResourceImplementItem;
import top.warmheart.workerunion.server.model.SettlementItem;
import top.warmheart.workerunion.server.service.SettlementItemService;

@Service("settlementItemServiceImpl")
@Scope("singleton")
public class SettlementItemServiceImpl implements SettlementItemService {
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
	public Page<SettlementItemDto> pageByProjectId(BigInteger projectId, Page<?> page) {
		WhNull.check(projectId);
		WhNull.checkPage(page);

		SettlementItemMapper settlementItemMapper = sqlSessionFactory.openSession().getMapper(
				SettlementItemMapper.class);

		// 获取总记录数
		Page<SettlementItemDto> pageCheck = new Page<SettlementItemDto>();
		pageCheck.setTotalItem(settlementItemMapper.countByProjectId(projectId));
		pageCheck.setSize(page.getSize());
		pageCheck.setPagination(Math.min(pageCheck.getTotalPage(), page.getPagination()));

		if (pageCheck.getTotalItem() > 0) {
			// 获取分页项目列表信息
			pageCheck.setList(settlementItemMapper.pageByProjectId(projectId, pageCheck));
		} else {
			pageCheck.setList(new ArrayList<SettlementItemDto>());
		}
		return pageCheck;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public SettlementItem getById(BigInteger id) {
		WhNull.check(id);
		return sqlSessionFactory.openSession().getMapper(SettlementItemMapper.class).selectByPrimaryKey(id);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int add(SettlementItem settlementItem) {
		WhNull.check(settlementItem);
		// 校验关联的资源落实项
		ResourceImplementItem resourceImplementItemCheck = sqlSessionFactory.openSession()
				.getMapper(ResourceImplementItemMapper.class)
				.selectByPrimaryKey4Update(settlementItem.getResourceImplementItemId());
		if (null == resourceImplementItemCheck) {
			throw new RuntimeException();
		}

		return sqlSessionFactory.openSession().getMapper(SettlementItemMapper.class).insert(settlementItem);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int removeById(BigInteger id) {
		WhNull.check(id);
		return sqlSessionFactory.openSession().getMapper(SettlementItemMapper.class).deleteByPrimaryKey(id);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int modifyById(SettlementItem settlementItem) {
		WhNull.check(settlementItem);
		// 校验关联的资源落实项
		ResourceImplementItem resourceImplementItemCheck = sqlSessionFactory.openSession()
				.getMapper(ResourceImplementItemMapper.class)
				.selectByPrimaryKey4Update(settlementItem.getResourceImplementItemId());
		if (null == resourceImplementItemCheck) {
			throw new RuntimeException();
		}
		return sqlSessionFactory.openSession().getMapper(SettlementItemMapper.class).updateByPrimaryKey(settlementItem);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<SettlementItem> listByResourceImplementItemId(BigInteger resourceImplementItemId) {
		return sqlSessionFactory.openSession().getMapper(SettlementItemMapper.class)
				.selectListByResourceImplementItemId(resourceImplementItemId);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public SettlementItem getByResourceImplementYearMonth(BigInteger resourceImplementItemId, Integer year,
			Integer month) {
		return sqlSessionFactory.openSession().getMapper(SettlementItemMapper.class)
				.selectByResourceImplementYearMonth(resourceImplementItemId, year, month);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public SettlementItem2Dto getDetailById(BigInteger id) {
		WhNull.check(id);
		return sqlSessionFactory.openSession().getMapper(SettlementItemMapper.class).selectDetailById(id);
	
	}

}
