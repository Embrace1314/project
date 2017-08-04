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
import top.warmheart.workerunion.server.dao.SafetyCheckItemAttachmentMapper;
import top.warmheart.workerunion.server.dao.SafetyCheckItemMapper;
import top.warmheart.workerunion.server.dto.SafetyCheckItemDto;
import top.warmheart.workerunion.server.model.SafetyCheckItem;
import top.warmheart.workerunion.server.model.SafetyCheckItemAttachment;
import top.warmheart.workerunion.server.service.SafetyCheckItemService;

@Service("safetyCheckItemServiceImpl")
@Scope("singleton")
public class SafetyCheckItemServiceImpl implements SafetyCheckItemService {
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
	public Page<SafetyCheckItem> pageByProjectId(BigInteger projectId, Page<?> page) {
		WhNull.check(projectId);
		WhNull.checkPage(page);

		SafetyCheckItemMapper safetyCheckItemMapper = sqlSessionFactory.openSession().getMapper(
				SafetyCheckItemMapper.class);

		// 获取总记录数
		Page<SafetyCheckItem> pageCheck = new Page<SafetyCheckItem>();
		pageCheck.setTotalItem(safetyCheckItemMapper.countByProjectId(projectId));
		pageCheck.setSize(page.getSize());
		pageCheck.setPagination(Math.min(pageCheck.getTotalPage(), page.getPagination()));

		if (pageCheck.getTotalItem() > 0) {
			// 获取分页项目列表信息
			pageCheck.setList(safetyCheckItemMapper.pageByProjectId(projectId, pageCheck));
		} else {
			pageCheck.setList(new ArrayList<SafetyCheckItem>());
		}
		return pageCheck;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public SafetyCheckItem getItemById(BigInteger id) {
		return sqlSessionFactory.openSession().getMapper(SafetyCheckItemMapper.class).selectByPrimaryKey(id);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public SafetyCheckItemAttachment getAttachmentById(BigInteger attachmentId) {
		return sqlSessionFactory.openSession().getMapper(SafetyCheckItemAttachmentMapper.class)
				.selectByPrimaryKey(attachmentId);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<SafetyCheckItemAttachment> listAttachmentByItemId(BigInteger id) {
		return sqlSessionFactory.openSession().getMapper(SafetyCheckItemAttachmentMapper.class).selectListByItemId(id);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int modifyItemById(SafetyCheckItem safetyCheckItem) {
		return sqlSessionFactory.openSession().getMapper(SafetyCheckItemMapper.class)
				.updateByPrimaryKey(safetyCheckItem);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int removeAttachmentById(BigInteger attachmentId) {
		return sqlSessionFactory.openSession().getMapper(SafetyCheckItemAttachmentMapper.class)
				.deleteById(attachmentId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int addAttachment(SafetyCheckItemAttachment safetyCheckItemAttachment) {
		return sqlSessionFactory.openSession().getMapper(SafetyCheckItemAttachmentMapper.class)
				.insert(safetyCheckItemAttachment);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public BigInteger addItem(SafetyCheckItem safetyCheckItem) {
		sqlSessionFactory.openSession().getMapper(SafetyCheckItemMapper.class).insert(safetyCheckItem);
		return safetyCheckItem.getId();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int removeItemById(BigInteger itemId) {
		return sqlSessionFactory.openSession().getMapper(SafetyCheckItemMapper.class).deleteById(itemId);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<SafetyCheckItemDto> listByCompanyAndStatus(BigInteger companyId, String rectifyStatus) {
		return sqlSessionFactory.openSession().getMapper(SafetyCheckItemMapper.class).selectListByCompanyAndStatus(companyId, rectifyStatus);
	}

}
