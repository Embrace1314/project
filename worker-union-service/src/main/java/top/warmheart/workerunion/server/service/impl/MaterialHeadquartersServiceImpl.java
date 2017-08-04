package top.warmheart.workerunion.server.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import top.warmheart.server.util.dto.Page;
import top.warmheart.server.util.exception.WhNull;
import top.warmheart.workerunion.server.dao.MaterialHeadquartersMapper;
import top.warmheart.workerunion.server.model.MaterialHeadquarters;
import top.warmheart.workerunion.server.service.MaterialHeadquartersService;

@Service("materialHeadquartersServiceImpl")
@Scope("singleton")
public class MaterialHeadquartersServiceImpl implements MaterialHeadquartersService {
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
	public Page<MaterialHeadquarters> pageByFuzzy(BigInteger companyId, String num, String name, Page<?> page) {
		WhNull.check(companyId);
		WhNull.checkPage(page);

		MaterialHeadquartersMapper materialHeadquartersMapper = sqlSessionFactory.openSession().getMapper(MaterialHeadquartersMapper.class);

		// 获取总记录数
		Page<MaterialHeadquarters> pageCheck = new Page<MaterialHeadquarters>();
		pageCheck.setTotalItem(materialHeadquartersMapper.countByFuzzy(companyId, num, name));
		pageCheck.setSize(page.getSize());
		pageCheck.setPagination(Math.min(pageCheck.getTotalPage(), page.getPagination()));

		if (pageCheck.getTotalItem() > 0) {
			// 获取分页项目列表信息
			pageCheck.setList(materialHeadquartersMapper.pageByFuzzy(companyId, num, name, pageCheck));
		} else {
			pageCheck.setList(new ArrayList<MaterialHeadquarters>());
		}
		return pageCheck;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public MaterialHeadquarters getByNum(BigInteger companyId, String num) {
		return sqlSessionFactory.openSession().getMapper(MaterialHeadquartersMapper.class).selectByNum(companyId, num);
	}

}

