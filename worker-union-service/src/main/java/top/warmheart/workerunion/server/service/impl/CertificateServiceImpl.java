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
import top.warmheart.workerunion.server.dao.CertificateAttachmentMapper;
import top.warmheart.workerunion.server.dao.CertificateMapper;
import top.warmheart.workerunion.server.dao.CertificateSerieMapper;
import top.warmheart.workerunion.server.dao.CertificateTypeMapper;
import top.warmheart.workerunion.server.dto.CertificateDto;
import top.warmheart.workerunion.server.model.Certificate;
import top.warmheart.workerunion.server.model.CertificateAttachment;
import top.warmheart.workerunion.server.model.CertificateSerie;
import top.warmheart.workerunion.server.model.CertificateType;
import top.warmheart.workerunion.server.service.CertificateService;

@Service("certificateServiceImpl")
@Scope("singleton")
public class CertificateServiceImpl implements CertificateService {
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
	public CertificateType getTypeById(BigInteger certificateTypeId) {
		return sqlSessionFactory.openSession().getMapper(CertificateTypeMapper.class).selectByPrimaryKey(certificateTypeId);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<CertificateSerie> listSerie() {
		return sqlSessionFactory.openSession().getMapper(CertificateSerieMapper.class).selectAll();
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<CertificateType> listTypeBySerieId(BigInteger certificateSerieId) {
		return sqlSessionFactory.openSession().getMapper(CertificateTypeMapper.class).selectListBySerieId(certificateSerieId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public BigInteger add(Certificate certificate) {
		sqlSessionFactory.openSession().getMapper(CertificateMapper.class).insert(certificate);
		return certificate.getId();
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Certificate getById(BigInteger id) {
		return sqlSessionFactory.openSession().getMapper(CertificateMapper.class).selectByPrimaryKey(id);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int modifyById(Certificate certificate) {
		return sqlSessionFactory.openSession().getMapper(CertificateMapper.class).updateByPrimaryKey(certificate);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int removeById(BigInteger id) {
		return sqlSessionFactory.openSession().getMapper(CertificateMapper.class).deleteById(id);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public CertificateDto getDetailById(BigInteger id) {
		return sqlSessionFactory.openSession().getMapper(CertificateMapper.class).selectDetailById(id);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Page<CertificateDto> pageByFuzzy(CertificateDto certificateDto, Page<?> page) {
		WhNull.check(certificateDto);
		WhNull.checkPage(page);

		CertificateMapper certificateMapper = sqlSessionFactory.openSession().getMapper(CertificateMapper.class);

		// 获取总记录数
		Page<CertificateDto> pageCheck = new Page<CertificateDto>();
		pageCheck.setTotalItem(certificateMapper.countByFuzzy(certificateDto));
		pageCheck.setSize(page.getSize());
		pageCheck.setPagination(Math.min(pageCheck.getTotalPage(), page.getPagination()));

		if (pageCheck.getTotalItem() > 0) {
			// 获取分页项目列表信息
			pageCheck.setList(certificateMapper.pageByFuzzy(certificateDto, pageCheck));
		} else {
			pageCheck.setList(new ArrayList<CertificateDto>());
		}
		return pageCheck;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Certificate> listByStaffId(BigInteger staffId) {
		return sqlSessionFactory.openSession().getMapper(CertificateMapper.class).selectListByStaffId(staffId);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public CertificateAttachment getAttachmentById(BigInteger attachmentId) {
		return sqlSessionFactory.openSession().getMapper(CertificateAttachmentMapper.class)
				.selectByPrimaryKey(attachmentId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int removeAttachmentById(BigInteger attachmentId) {
		return sqlSessionFactory.openSession().getMapper(CertificateAttachmentMapper.class).deleteById(attachmentId);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS,readOnly=true)
	public List<CertificateAttachment> listAttachmentByCertificateId(BigInteger certificateId) {
		return sqlSessionFactory.openSession().getMapper(CertificateAttachmentMapper.class).selectListByCertificateId(certificateId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int addAttachment(CertificateAttachment certificateAttachment) {
		return sqlSessionFactory.openSession().getMapper(CertificateAttachmentMapper.class).insert(certificateAttachment);
	}
}
