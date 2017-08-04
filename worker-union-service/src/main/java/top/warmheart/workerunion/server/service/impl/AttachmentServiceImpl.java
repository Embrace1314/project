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
import top.warmheart.workerunion.server.dao.AttachmentAuditMapper;
import top.warmheart.workerunion.server.dao.AttachmentMapper;
import top.warmheart.workerunion.server.dto.Attachment2Dto;
import top.warmheart.workerunion.server.dto.AttachmentDto;
import top.warmheart.workerunion.server.exception.WhAttachmentExistException;
import top.warmheart.workerunion.server.model.Attachment;
import top.warmheart.workerunion.server.model.AttachmentAudit;
import top.warmheart.workerunion.server.service.AttachmentService;

@Service("attachmentServiceImpl")
@Scope("singleton")
public class AttachmentServiceImpl implements AttachmentService {
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
	public Attachment getById(BigInteger id) {
		WhNull.check(id);
		return sqlSessionFactory.openSession().getMapper(AttachmentMapper.class).selectByPrimaryKey(id);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int removeById(BigInteger id) {
		WhNull.check(id);
		return sqlSessionFactory.openSession().getMapper(AttachmentMapper.class)
				.deleteById(id, AttachmentAudit.STATUS_PASS);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int add(Attachment attachment) {
		WhNull.check(attachment);
		return sqlSessionFactory.openSession().getMapper(AttachmentMapper.class).insert(attachment);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Attachment getLatestByType(BigInteger projectId, String type) {
		WhNull.check(projectId);
		WhNull.checkTrimEmpty(type);
		return sqlSessionFactory.openSession().getMapper(AttachmentMapper.class).selectLatestByType(projectId, type);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int addUniqueType(Attachment attachment) throws WhAttachmentExistException {
		WhNull.check(attachment);
		AttachmentMapper attachmentMapper = sqlSessionFactory.openSession().getMapper(AttachmentMapper.class);
		List<Attachment> attachments = attachmentMapper.selectByType4Update(attachment.getProjectId(),
				attachment.getType());
		if (!attachments.isEmpty()) {
			throw new WhAttachmentExistException();
		}
		return sqlSessionFactory.openSession().getMapper(AttachmentMapper.class).insert(attachment);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<AttachmentDto> listAuditAttachment(BigInteger projectId, String attachmentType) {
		return sqlSessionFactory.openSession().getMapper(AttachmentMapper.class)
				.selectListAuditAttachment(projectId, attachmentType, AttachmentAudit.STATUS_UNDETERMINED);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Page<AttachmentDto> pageAuditByProjectType(BigInteger projectId, String attachmentType, Page<?> page) {
		WhNull.check(projectId);
		WhNull.checkEmpty(attachmentType);
		WhNull.checkPage(page);

		AttachmentMapper attachmentMapper = sqlSessionFactory.openSession().getMapper(AttachmentMapper.class);

		// 获取总记录数
		Page<AttachmentDto> pageCheck = new Page<AttachmentDto>();
		pageCheck.setTotalItem(attachmentMapper.countByProjectType(projectId, attachmentType));
		pageCheck.setSize(page.getSize());
		pageCheck.setPagination(Math.min(pageCheck.getTotalPage(), page.getPagination()));

		if (pageCheck.getTotalItem() > 0) {
			// 获取分页项目列表信息
			pageCheck.setList(attachmentMapper.pageAuditByProjectType(projectId, attachmentType,
					AttachmentAudit.STATUS_UNDETERMINED, pageCheck));
		} else {
			pageCheck.setList(new ArrayList<AttachmentDto>());
		}
		return pageCheck;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Page<Attachment> pageByProjectType(BigInteger projectId, String type, Page<?> page) {
		WhNull.check(projectId);
		WhNull.checkEmpty(type);
		WhNull.checkPage(page);

		AttachmentMapper attachmentMapper = sqlSessionFactory.openSession().getMapper(AttachmentMapper.class);

		// 获取总记录数
		Page<Attachment> pageCheck = new Page<Attachment>();
		pageCheck.setTotalItem(attachmentMapper.countByProjectType(projectId, type));
		pageCheck.setSize(page.getSize());
		pageCheck.setPagination(Math.min(pageCheck.getTotalPage(), page.getPagination()));

		if (pageCheck.getTotalItem() > 0) {
			// 获取分页项目列表信息
			pageCheck.setList(attachmentMapper.pageByProjectType(projectId, type, pageCheck));
		} else {
			pageCheck.setList(new ArrayList<Attachment>());
		}
		return pageCheck;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public AttachmentDto getAuditById(BigInteger id) {
		WhNull.check(id);
		return sqlSessionFactory.openSession().getMapper(AttachmentMapper.class)
				.selectAuditByPrimaryKey(id, AttachmentAudit.STATUS_UNDETERMINED);

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int replaceAttachmentAudit(AttachmentAudit attachmentAudit) {
		return sqlSessionFactory.openSession().getMapper(AttachmentAuditMapper.class).replace(attachmentAudit);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Attachment2Dto> listUndeterminedByTypeInCompany(BigInteger companyId, String attachmentType) {
		return sqlSessionFactory.openSession().getMapper(AttachmentMapper.class)
				.selectListUndeterminedByTypeInCompany(companyId, attachmentType, AttachmentAudit.STATUS_UNDETERMINED);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Attachment> listByProjectType(BigInteger projectId, String type) {
		return sqlSessionFactory.openSession().getMapper(AttachmentMapper.class)
				.selectListByProjectType(projectId, type);
	}
}
