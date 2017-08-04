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
import top.warmheart.workerunion.server.dao.ContractAttachmentMapper;
import top.warmheart.workerunion.server.dao.ContractMapper;
import top.warmheart.workerunion.server.dto.ContractDto;
import top.warmheart.workerunion.server.model.Contract;
import top.warmheart.workerunion.server.model.ContractAttachment;
import top.warmheart.workerunion.server.service.ContractService;

@Service("contractServiceImpl")
@Scope("singleton")
public class ContractServiceImpl implements ContractService {
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
	public Contract getById(BigInteger id) {
		return sqlSessionFactory.openSession().getMapper(ContractMapper.class).selectByPrimaryKey(id);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Contract> listSimpleItemByProjectId(BigInteger projectId) {
		return sqlSessionFactory.openSession().getMapper(ContractMapper.class)
				.selectListSimpleItemByProjectId(projectId);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Contract getByCompanyAndNum(BigInteger companyId, String num) {
		return sqlSessionFactory.openSession().getMapper(ContractMapper.class).selectByCompanyAndNum(companyId, num);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public BigInteger add(Contract contract) {
		sqlSessionFactory.openSession().getMapper(ContractMapper.class).insert(contract);
		return contract.getId();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int removeById(BigInteger id) {
		return sqlSessionFactory.openSession().getMapper(ContractMapper.class).deleteById(id);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public ContractDto getDetailById(BigInteger id) {
		return sqlSessionFactory.openSession().getMapper(ContractMapper.class).selectDetailById(id);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public ContractAttachment getAttachmentById(BigInteger attachmentId) {
		return sqlSessionFactory.openSession().getMapper(ContractAttachmentMapper.class)
				.selectByPrimaryKey(attachmentId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int removeAttachmentById(BigInteger attachmentId) {
		return sqlSessionFactory.openSession().getMapper(ContractAttachmentMapper.class).deleteById(attachmentId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int modifyById(Contract contract) {
		return sqlSessionFactory.openSession().getMapper(ContractMapper.class).updateByPrimaryKey(contract);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Page<ContractDto> pageByFuzzy(ContractDto contractDto, Page<?> page) {
		WhNull.check(contractDto);
		WhNull.checkPage(page);

		ContractMapper contractMapper = sqlSessionFactory.openSession().getMapper(ContractMapper.class);

		// 获取总记录数
		Page<ContractDto> pageCheck = new Page<ContractDto>();
		pageCheck.setTotalItem(contractMapper.countByFuzzy(contractDto));
		pageCheck.setSize(page.getSize());
		pageCheck.setPagination(Math.min(pageCheck.getTotalPage(), page.getPagination()));

		if (pageCheck.getTotalItem() > 0) {
			// 获取分页项目列表信息
			pageCheck.setList(contractMapper.pageByFuzzy(contractDto, pageCheck));
		} else {
			pageCheck.setList(new ArrayList<ContractDto>());
		}
		return pageCheck;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int addAttachment(ContractAttachment contractAttachment) {
		return sqlSessionFactory.openSession().getMapper(ContractAttachmentMapper.class).insert(contractAttachment);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS,readOnly=true)
	public List<ContractAttachment> listAttachmentByContractId(BigInteger contractId) {
		return sqlSessionFactory.openSession().getMapper(ContractAttachmentMapper.class).selectListByContractId(contractId);
	}

}
