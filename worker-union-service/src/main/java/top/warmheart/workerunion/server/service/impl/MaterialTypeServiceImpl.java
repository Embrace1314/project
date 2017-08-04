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
import top.warmheart.workerunion.server.dao.MaterialTypeAttachmentMapper;
import top.warmheart.workerunion.server.dao.MaterialTypeMapper;
import top.warmheart.workerunion.server.model.MaterialType;
import top.warmheart.workerunion.server.model.MaterialTypeAttachment;
import top.warmheart.workerunion.server.service.MaterialTypeService;

@Service("materialTypeServiceImpl")
@Scope("singleton")
public class MaterialTypeServiceImpl implements MaterialTypeService {
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
	public MaterialType getByNum(BigInteger companyId, String num) {
		return sqlSessionFactory.openSession().getMapper(MaterialTypeMapper.class)
				.selectByCompanyAndNum(companyId, num);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public MaterialType getById(BigInteger id) {
		return sqlSessionFactory.openSession().getMapper(MaterialTypeMapper.class).selectByPrimaryKey(id);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<MaterialTypeAttachment> listByTypeId(BigInteger materialTypeId) {
		return sqlSessionFactory.openSession().getMapper(MaterialTypeAttachmentMapper.class)
				.selectListByTypeId(materialTypeId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public BigInteger add(MaterialType materialType) {
		sqlSessionFactory.openSession().getMapper(MaterialTypeMapper.class).insert(materialType);
		return materialType.getId();
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public MaterialTypeAttachment getAttachmentById(BigInteger attachmentId) {
		return sqlSessionFactory.openSession().getMapper(MaterialTypeAttachmentMapper.class)
				.selectByPrimaryKey(attachmentId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int removeAttachmentById(BigInteger attachmentId) {
		return sqlSessionFactory.openSession().getMapper(MaterialTypeAttachmentMapper.class).deleteById(attachmentId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int removeById(BigInteger id) {
		return sqlSessionFactory.openSession().getMapper(MaterialTypeMapper.class).deleteById(id);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int addAttachment(MaterialTypeAttachment materialTypeAttachment) {
		return sqlSessionFactory.openSession().getMapper(MaterialTypeAttachmentMapper.class).insert(materialTypeAttachment);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly=true)
	public Page<MaterialType> pageByFuzzy(MaterialType materialType, Page<Void> page) {
		WhNull.check(materialType);
		WhNull.checkPage(page);

		MaterialTypeMapper materialTypeMapper = sqlSessionFactory.openSession().getMapper(MaterialTypeMapper.class);

		// 获取总记录数
		Page<MaterialType> pageCheck = new Page<MaterialType>();
		pageCheck.setTotalItem(materialTypeMapper.countByFuzzy(materialType));
		pageCheck.setSize(page.getSize());
		pageCheck.setPagination(Math.min(pageCheck.getTotalPage(), page.getPagination()));

		if (pageCheck.getTotalItem() > 0) {
			// 获取分页项目列表信息
			pageCheck.setList(materialTypeMapper.pageByFuzzy(materialType, pageCheck));
		} else {
			pageCheck.setList(new ArrayList<MaterialType>());
		}
		return pageCheck;

	}

}
