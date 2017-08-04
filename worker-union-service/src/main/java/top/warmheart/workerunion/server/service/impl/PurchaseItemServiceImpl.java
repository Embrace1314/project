package top.warmheart.workerunion.server.service.impl;

import java.math.BigInteger;
import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import top.warmheart.workerunion.server.dao.PurchaseItemMapper;
import top.warmheart.workerunion.server.model.PurchaseItem;
import top.warmheart.workerunion.server.service.PurchaseItemService;

@Service("purchaseItemServiceImpl")
@Scope("singleton")
public class PurchaseItemServiceImpl implements PurchaseItemService {
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
	public List<PurchaseItem> list() {
		return sqlSessionFactory.openSession().getMapper(PurchaseItemMapper.class).selectList();
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public PurchaseItem getById(BigInteger id) {
		return sqlSessionFactory.openSession().getMapper(PurchaseItemMapper.class).selectByPrimaryKey(id);
	}

}
