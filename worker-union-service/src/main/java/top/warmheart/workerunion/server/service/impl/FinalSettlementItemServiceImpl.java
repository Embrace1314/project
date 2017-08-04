package top.warmheart.workerunion.server.service.impl;

import java.math.BigInteger;
import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import top.warmheart.workerunion.server.dao.FinalSettlementItemAttachmentMapper;
import top.warmheart.workerunion.server.dao.FinalSettlementItemMapper;
import top.warmheart.workerunion.server.dto.FinalSettlementItemDto;
import top.warmheart.workerunion.server.model.FinalSettlementItem;
import top.warmheart.workerunion.server.model.FinalSettlementItemAttachment;
import top.warmheart.workerunion.server.service.FinalSettlementItemService;

@Service("finalSettlementItemServiceImpl")
@Scope("singleton")
public class FinalSettlementItemServiceImpl implements FinalSettlementItemService {
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
	public List<FinalSettlementItemDto> listItemByProjectId(BigInteger projectId) {
		return sqlSessionFactory.openSession().getMapper(FinalSettlementItemMapper.class)
				.selectListByProjectId(projectId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int addItem(FinalSettlementItem finalSettlementItem) {
		return sqlSessionFactory.openSession().getMapper(FinalSettlementItemMapper.class).insert(finalSettlementItem);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public FinalSettlementItem getItemById(BigInteger id) {
		return sqlSessionFactory.openSession().getMapper(FinalSettlementItemMapper.class).selectByPrimaryKey(id);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int modifyItemById(FinalSettlementItem finalSettlementItem) {
		return sqlSessionFactory.openSession().getMapper(FinalSettlementItemMapper.class)
				.updateByPrimaryKey(finalSettlementItem);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int removeItemById(BigInteger id) {
		return sqlSessionFactory.openSession().getMapper(FinalSettlementItemMapper.class).deleteById(id);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public FinalSettlementItemAttachment getAttachmentByItemId(BigInteger finalSettlementItemId) {
		return sqlSessionFactory.openSession().getMapper(FinalSettlementItemAttachmentMapper.class)
				.selectByPrimaryKey(finalSettlementItemId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int removeAttachmentByItemId(BigInteger finalSettlementItemId) {
		return sqlSessionFactory.openSession().getMapper(FinalSettlementItemAttachmentMapper.class)
				.deleteByPrimaryKey(finalSettlementItemId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int addAttachment(FinalSettlementItemAttachment finalSettlementItemAttachment) {
		return sqlSessionFactory.openSession().getMapper(FinalSettlementItemAttachmentMapper.class)
				.insert(finalSettlementItemAttachment);
	}
}
