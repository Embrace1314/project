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
import top.warmheart.workerunion.server.dao.SupplierAttachmentMapper;
import top.warmheart.workerunion.server.dao.SupplierGradeMapper;
import top.warmheart.workerunion.server.dao.SupplierHasSupplierQualificationMapper;
import top.warmheart.workerunion.server.dao.SupplierMapper;
import top.warmheart.workerunion.server.dao.SupplierQualificationMapper;
import top.warmheart.workerunion.server.dto.SupplierDto;
import top.warmheart.workerunion.server.model.Supplier;
import top.warmheart.workerunion.server.model.SupplierAttachment;
import top.warmheart.workerunion.server.model.SupplierGrade;
import top.warmheart.workerunion.server.model.SupplierHasSupplierQualification;
import top.warmheart.workerunion.server.model.SupplierQualification;
import top.warmheart.workerunion.server.service.SupplierService;

@Service("supplierServiceImpl")
@Scope("singleton")
public class SupplierServiceImpl implements SupplierService {
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
	public SupplierGrade getGradeById(BigInteger supplierGradeId) {
		return sqlSessionFactory.openSession().getMapper(SupplierGradeMapper.class).selectByPrimaryKey(supplierGradeId);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public SupplierQualification getQualificationById(BigInteger supplierQualificationId) {
		return sqlSessionFactory.openSession().getMapper(SupplierQualificationMapper.class).selectByPrimaryKey(supplierQualificationId);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Supplier getByCompanyAndNum(BigInteger companyId, String num) {
		return sqlSessionFactory.openSession().getMapper(SupplierMapper.class).selectByCompanyAndNum(companyId, num);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public BigInteger add(Supplier supplier, List<SupplierQualification> supplierQualifications) {
		SupplierMapper supplierMapper = sqlSessionFactory.openSession().getMapper(SupplierMapper.class);
		SupplierGradeMapper supplierGradeMapper = sqlSessionFactory.openSession().getMapper(SupplierGradeMapper.class);
		SupplierHasSupplierQualificationMapper supplierHasSupplierQualificationMapper = sqlSessionFactory.openSession().getMapper(SupplierHasSupplierQualificationMapper.class);
		/*
		 * 校验供应商级别
		 */
		SupplierGrade supplierGradeCheck = supplierGradeMapper.selectByPrimaryKey(supplier.getSupplierGradeId());
		if (null == supplierGradeCheck){
			throw new RuntimeException("无效的供应商级别");
		}
		// 插入供应商信息
		supplierMapper.insert(supplier);
		
		// 插入供应商资质信息
		supplierHasSupplierQualificationMapper.deleteBySupplierId(supplier.getId());
		for(SupplierQualification supplierQualification : supplierQualifications){
			supplierHasSupplierQualificationMapper.insert(new SupplierHasSupplierQualification(supplier.getId(), supplierQualification.getId()));
		}
		return supplier.getId();
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Supplier getById(BigInteger id) {
		return sqlSessionFactory.openSession().getMapper(SupplierMapper.class).selectByPrimaryKey(id);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int removeById(BigInteger id) {
		return sqlSessionFactory.openSession().getMapper(SupplierMapper.class).deleteById(id);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void modifyById(Supplier supplier, List<SupplierQualification> supplierQualifications) {
		SupplierMapper supplierMapper = sqlSessionFactory.openSession().getMapper(SupplierMapper.class);
		SupplierGradeMapper supplierGradeMapper = sqlSessionFactory.openSession().getMapper(SupplierGradeMapper.class);
		SupplierHasSupplierQualificationMapper supplierHasSupplierQualificationMapper = sqlSessionFactory.openSession().getMapper(SupplierHasSupplierQualificationMapper.class);
		/*
		 * 校验供应商级别
		 */
		SupplierGrade supplierGradeCheck = supplierGradeMapper.selectByPrimaryKey(supplier.getSupplierGradeId());
		if (null == supplierGradeCheck){
			throw new RuntimeException("无效的供应商级别");
		}
		// 修改供应商信息
		supplierMapper.updateByPrimaryKey(supplier);
		
		// 插入供应商资质信息
		supplierHasSupplierQualificationMapper.deleteBySupplierId(supplier.getId());
		for(SupplierQualification supplierQualification : supplierQualifications){
			supplierHasSupplierQualificationMapper.insert(new SupplierHasSupplierQualification(supplier.getId(), supplierQualification.getId()));
		}
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<SupplierQualification> listQualificationBySupplierId(BigInteger supplierId) {
		return sqlSessionFactory.openSession().getMapper(SupplierQualificationMapper.class).selectListBySupplierId(supplierId);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<SupplierAttachment> listAttachmentBySupplierId(BigInteger supplierId) {
		return sqlSessionFactory.openSession().getMapper(SupplierAttachmentMapper.class).selectListBySupplierId(supplierId);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public SupplierAttachment getAttachmentById(BigInteger attachmentId) {
		return sqlSessionFactory.openSession().getMapper(SupplierAttachmentMapper.class).selectByPrimaryKey(attachmentId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int removeAttachmentById(BigInteger attachmentId) {
		return sqlSessionFactory.openSession().getMapper(SupplierAttachmentMapper.class).deleteById(attachmentId);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<SupplierGrade> listGrade() {
		return sqlSessionFactory.openSession().getMapper(SupplierGradeMapper.class).selectAll();
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<SupplierQualification> listQualification() {
		return sqlSessionFactory.openSession().getMapper(SupplierQualificationMapper.class).selectAll();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Page<SupplierDto> pageByFuzzy(SupplierDto supplierDto, Page<?> page) {
		WhNull.check(supplierDto);
		WhNull.checkPage(page);

		SupplierMapper supplierMapper = sqlSessionFactory.openSession().getMapper(SupplierMapper.class);

		// 获取总记录数
		Page<SupplierDto> pageCheck = new Page<SupplierDto>();
		pageCheck.setTotalItem(supplierMapper.countByFuzzy(supplierDto));
		pageCheck.setSize(page.getSize());
		pageCheck.setPagination(Math.min(pageCheck.getTotalPage(), page.getPagination()));

		if (pageCheck.getTotalItem() > 0) {
			// 获取分页项目列表信息
			pageCheck.setList(supplierMapper.pageByFuzzy(supplierDto, pageCheck));
		} else {
			pageCheck.setList(new ArrayList<SupplierDto>());
		}
		return pageCheck;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int addAttachment(SupplierAttachment supplierAttachment) {
		return sqlSessionFactory.openSession().getMapper(SupplierAttachmentMapper.class).insert(supplierAttachment);
	}

}
