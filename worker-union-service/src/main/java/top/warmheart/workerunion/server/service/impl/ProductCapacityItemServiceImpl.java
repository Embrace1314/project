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
import top.warmheart.workerunion.server.dao.ProductCapacityItemMapper;
import top.warmheart.workerunion.server.model.ProductCapacityItem;
import top.warmheart.workerunion.server.service.ProductCapacityItemService;

@Service("productCapacityItemServiceImpl")
@Scope("singleton")
public class ProductCapacityItemServiceImpl implements ProductCapacityItemService {
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
	public Page<ProductCapacityItem> pageByProjectId(BigInteger projectId, Page<Void> page) {
		WhNull.check(projectId);
		WhNull.checkPage(page);

		ProductCapacityItemMapper productCapacityItemMapper = sqlSessionFactory.openSession().getMapper(ProductCapacityItemMapper.class);

		// 获取总记录数
		Page<ProductCapacityItem> pageCheck = new Page<ProductCapacityItem>();
		pageCheck.setTotalItem(productCapacityItemMapper.countByProjectId(projectId));
		pageCheck.setSize(page.getSize());
		pageCheck.setPagination(Math.min(pageCheck.getTotalPage(), page.getPagination()));

		if (pageCheck.getTotalItem() > 0) {
			// 获取分页项目列表信息
			pageCheck.setList(productCapacityItemMapper.pageByProjectId(projectId, pageCheck));
		} else {
			pageCheck.setList(new ArrayList<ProductCapacityItem>());
		}
		return pageCheck;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public ProductCapacityItem getByProjectYearMonth(BigInteger projectId, Integer year, Integer month) {
		return sqlSessionFactory.openSession().getMapper(ProductCapacityItemMapper.class).selectByProjectYearMonth(projectId, year, month);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int add(ProductCapacityItem productCapacityItem) {
		return sqlSessionFactory.openSession().getMapper(ProductCapacityItemMapper.class).insert(productCapacityItem);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public ProductCapacityItem getById(BigInteger id) {
		return sqlSessionFactory.openSession().getMapper(ProductCapacityItemMapper.class).selectByPrimaryKey(id);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int modifyById(ProductCapacityItem productCapacityItem) {
		return sqlSessionFactory.openSession().getMapper(ProductCapacityItemMapper.class).updateByPrimaryKey(productCapacityItem);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int removeById(BigInteger id) {
		return sqlSessionFactory.openSession().getMapper(ProductCapacityItemMapper.class).deleteByPrimaryKey(id);
	}

}
