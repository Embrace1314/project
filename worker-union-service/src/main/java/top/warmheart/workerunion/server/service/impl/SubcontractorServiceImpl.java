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
import top.warmheart.workerunion.server.dao.SubcontractorAttachmentMapper;
import top.warmheart.workerunion.server.dao.SubcontractorGradeMapper;
import top.warmheart.workerunion.server.dao.SubcontractorHasSubcontractorQualificationMapper;
import top.warmheart.workerunion.server.dao.SubcontractorMapper;
import top.warmheart.workerunion.server.dao.SubcontractorQualificationMapper;
import top.warmheart.workerunion.server.dto.SubcontractorDto;
import top.warmheart.workerunion.server.model.Subcontractor;
import top.warmheart.workerunion.server.model.SubcontractorAttachment;
import top.warmheart.workerunion.server.model.SubcontractorGrade;
import top.warmheart.workerunion.server.model.SubcontractorHasSubcontractorQualification;
import top.warmheart.workerunion.server.model.SubcontractorQualification;
import top.warmheart.workerunion.server.service.SubcontractorService;

@Service("subcontractorServiceImpl")
@Scope("singleton")
public class SubcontractorServiceImpl implements SubcontractorService {
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
	public Subcontractor getById(BigInteger id) {
		return sqlSessionFactory.openSession().getMapper(SubcontractorMapper.class).selectByPrimaryKey(id);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Subcontractor> listSimpleItemByCompanyId(BigInteger companyId) {
		return sqlSessionFactory.openSession().getMapper(SubcontractorMapper.class).selectListSimpleItemByCompanyId(companyId);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public SubcontractorGrade getGradeById(BigInteger subcontractorGradeId) {
		return sqlSessionFactory.openSession().getMapper(SubcontractorGradeMapper.class).selectByPrimaryKey(subcontractorGradeId);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public SubcontractorQualification getQualificationById(BigInteger subcontractorQualificationId) {
		return sqlSessionFactory.openSession().getMapper(SubcontractorQualificationMapper.class).selectByPrimaryKey(subcontractorQualificationId);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Subcontractor getByCompanyAndNum(BigInteger companyId, String num) {
		return sqlSessionFactory.openSession().getMapper(SubcontractorMapper.class).selectByCompanyAndNum(companyId, num);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public BigInteger add(Subcontractor subcontractor, List<SubcontractorQualification> subcontractorQualifications) {
		SubcontractorMapper subcontractorMapper = sqlSessionFactory.openSession().getMapper(SubcontractorMapper.class);
		SubcontractorGradeMapper subcontractorGradeMapper = sqlSessionFactory.openSession().getMapper(SubcontractorGradeMapper.class);
		SubcontractorHasSubcontractorQualificationMapper subcontractorHasSubcontractorQualificationMapper = sqlSessionFactory.openSession().getMapper(SubcontractorHasSubcontractorQualificationMapper.class);
		/*
		 * 校验供应商级别
		 */
		SubcontractorGrade subcontractorGradeCheck = subcontractorGradeMapper.selectByPrimaryKey(subcontractor.getSubcontractorGradeId());
		if (null == subcontractorGradeCheck){
			throw new RuntimeException("无效的供应商级别");
		}
		// 插入分包商信息
		subcontractorMapper.insert(subcontractor);
		
		// 插入分包商资质信息
		subcontractorHasSubcontractorQualificationMapper.deleteBySubcontractorId(subcontractor.getId());
		for(SubcontractorQualification subcontractorQualification : subcontractorQualifications){
			subcontractorHasSubcontractorQualificationMapper.insert(new SubcontractorHasSubcontractorQualification(subcontractor.getId(), subcontractorQualification.getId()));
		}
		return subcontractor.getId();
	}


	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int removeById(BigInteger id) {
		return sqlSessionFactory.openSession().getMapper(SubcontractorMapper.class).deleteById(id);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void modifyById(Subcontractor subcontractor, List<SubcontractorQualification> subcontractorQualifications) {
		SubcontractorMapper subcontractorMapper = sqlSessionFactory.openSession().getMapper(SubcontractorMapper.class);
		SubcontractorGradeMapper subcontractorGradeMapper = sqlSessionFactory.openSession().getMapper(SubcontractorGradeMapper.class);
		SubcontractorHasSubcontractorQualificationMapper subcontractorHasSubcontractorQualificationMapper = sqlSessionFactory.openSession().getMapper(SubcontractorHasSubcontractorQualificationMapper.class);
		/*
		 * 校验供应商级别
		 */
		SubcontractorGrade subcontractorGradeCheck = subcontractorGradeMapper.selectByPrimaryKey(subcontractor.getSubcontractorGradeId());
		if (null == subcontractorGradeCheck){
			throw new RuntimeException("无效的供应商级别");
		}
		// 修改供应商信息
		subcontractorMapper.updateByPrimaryKey(subcontractor);
		
		// 插入供应商资质信息
		subcontractorHasSubcontractorQualificationMapper.deleteBySubcontractorId(subcontractor.getId());
		for(SubcontractorQualification subcontractorQualification : subcontractorQualifications){
			subcontractorHasSubcontractorQualificationMapper.insert(new SubcontractorHasSubcontractorQualification(subcontractor.getId(), subcontractorQualification.getId()));
		}

	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<SubcontractorQualification> listQualificationBySubcontractorId(BigInteger subcontractorId) {
		return sqlSessionFactory.openSession().getMapper(SubcontractorQualificationMapper.class).selectListBySubcontractorId(subcontractorId);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<SubcontractorAttachment> listAttachmentBySubcontractorId(BigInteger subcontractorId) {
		return sqlSessionFactory.openSession().getMapper(SubcontractorAttachmentMapper.class).selectListBySubcontractorId(subcontractorId);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public SubcontractorAttachment getAttachmentById(BigInteger attachmentId) {
		return sqlSessionFactory.openSession().getMapper(SubcontractorAttachmentMapper.class).selectByPrimaryKey(attachmentId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int removeAttachmentById(BigInteger attachmentId) {
		return sqlSessionFactory.openSession().getMapper(SubcontractorAttachmentMapper.class).deleteById(attachmentId);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<SubcontractorGrade> listGrade() {
		return sqlSessionFactory.openSession().getMapper(SubcontractorGradeMapper.class).selectAll();
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<SubcontractorQualification> listQualification() {
		return sqlSessionFactory.openSession().getMapper(SubcontractorQualificationMapper.class).selectAll();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Page<SubcontractorDto> pageByFuzzy(SubcontractorDto subcontractorDto, Page<?> page) {
		WhNull.check(subcontractorDto);
		WhNull.checkPage(page);

		SubcontractorMapper subcontractorMapper = sqlSessionFactory.openSession().getMapper(SubcontractorMapper.class);

		// 获取总记录数
		Page<SubcontractorDto> pageCheck = new Page<SubcontractorDto>();
		pageCheck.setTotalItem(subcontractorMapper.countByFuzzy(subcontractorDto));
		pageCheck.setSize(page.getSize());
		pageCheck.setPagination(Math.min(pageCheck.getTotalPage(), page.getPagination()));

		if (pageCheck.getTotalItem() > 0) {
			// 获取分页项目列表信息
			pageCheck.setList(subcontractorMapper.pageByFuzzy(subcontractorDto, pageCheck));
		} else {
			pageCheck.setList(new ArrayList<SubcontractorDto>());
		}
		return pageCheck;

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int addAttachment(SubcontractorAttachment subcontractorAttachment) {
		return sqlSessionFactory.openSession().getMapper(SubcontractorAttachmentMapper.class).insert(subcontractorAttachment);
		
	}

}
