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
import top.warmheart.workerunion.server.dao.QualityCheckItemAttachmentMapper;
import top.warmheart.workerunion.server.dao.QualityCheckItemMapper;
import top.warmheart.workerunion.server.dto.QualityCheckItemDto;
import top.warmheart.workerunion.server.model.QualityCheckItem;
import top.warmheart.workerunion.server.model.QualityCheckItemAttachment;
import top.warmheart.workerunion.server.service.QualityCheckItemService;

@Service("qualityCheckItemServiceImpl")
@Scope("singleton")
public class QualityCheckItemServiceImpl implements QualityCheckItemService {
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
	public Page<QualityCheckItem> pageByProjectId(BigInteger projectId, Page<?> page) {
		WhNull.check(projectId);
		WhNull.checkPage(page);

		QualityCheckItemMapper qualityCheckItemMapper = sqlSessionFactory.openSession().getMapper(
				QualityCheckItemMapper.class);

		// 获取总记录数
		Page<QualityCheckItem> pageCheck = new Page<QualityCheckItem>();
		pageCheck.setTotalItem(qualityCheckItemMapper.countByProjectId(projectId));
		pageCheck.setSize(page.getSize());
		pageCheck.setPagination(Math.min(pageCheck.getTotalPage(), page.getPagination()));

		if (pageCheck.getTotalItem() > 0) {
			// 获取分页项目列表信息
			pageCheck.setList(qualityCheckItemMapper.pageByProjectId(projectId, pageCheck));
		} else {
			pageCheck.setList(new ArrayList<QualityCheckItem>());
		}
		return pageCheck;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public QualityCheckItem getItemById(BigInteger id) {
		return sqlSessionFactory.openSession().getMapper(QualityCheckItemMapper.class).selectByPrimaryKey(id);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public QualityCheckItemAttachment getAttachmentById(BigInteger attachmentId) {
		return sqlSessionFactory.openSession().getMapper(QualityCheckItemAttachmentMapper.class)
				.selectByPrimaryKey(attachmentId);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<QualityCheckItemAttachment> listAttachmentByItemId(BigInteger id) {
		return sqlSessionFactory.openSession().getMapper(QualityCheckItemAttachmentMapper.class).selectListByItemId(id);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int modifyItemById(QualityCheckItem qualityCheckItem) {
		return sqlSessionFactory.openSession().getMapper(QualityCheckItemMapper.class)
				.updateByPrimaryKey(qualityCheckItem);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int removeAttachmentById(BigInteger attachmentId) {
		return sqlSessionFactory.openSession().getMapper(QualityCheckItemAttachmentMapper.class)
				.deleteById(attachmentId);

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int addAttachment(QualityCheckItemAttachment qualityCheckItemAttachment) {
		return sqlSessionFactory.openSession().getMapper(QualityCheckItemAttachmentMapper.class)
				.insert(qualityCheckItemAttachment);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int removeItemById(BigInteger id) {
		return sqlSessionFactory.openSession().getMapper(QualityCheckItemMapper.class).deleteById(id);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public BigInteger addItem(QualityCheckItem qualityCheckItem) {
		sqlSessionFactory.openSession().getMapper(QualityCheckItemMapper.class).insert(qualityCheckItem);
		return qualityCheckItem.getId();
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<QualityCheckItemDto> listByCompanyAndStatus(BigInteger companyId, String rectifyStatus) {
		return sqlSessionFactory.openSession().getMapper(QualityCheckItemMapper.class).selectListByCompanyAndStatus(companyId, rectifyStatus);
	}
}
