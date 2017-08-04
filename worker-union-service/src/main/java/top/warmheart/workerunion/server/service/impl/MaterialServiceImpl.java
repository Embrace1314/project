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
import top.warmheart.workerunion.server.dao.MaterialMapper;
import top.warmheart.workerunion.server.model.Material;
import top.warmheart.workerunion.server.service.MaterialService;

@Service("materialServiceImpl")
@Scope("singleton")
public class MaterialServiceImpl implements MaterialService {
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
	public Page<Material> pageByFuzzy(BigInteger projectId, String num, String name, Page<?> page) {
		WhNull.check(projectId);
		WhNull.checkPage(page);

		MaterialMapper materialMapper = sqlSessionFactory.openSession().getMapper(MaterialMapper.class);

		// 获取总记录数
		Page<Material> pageCheck = new Page<Material>();
		pageCheck.setTotalItem(materialMapper.countByFuzzy(projectId, num, name));
		pageCheck.setSize(page.getSize());
		pageCheck.setPagination(Math.min(pageCheck.getTotalPage(), page.getPagination()));

		if (pageCheck.getTotalItem() > 0) {
			// 获取分页项目列表信息
			pageCheck.setList(materialMapper.pageByFuzzy(projectId, num, name, pageCheck));
		} else {
			pageCheck.setList(new ArrayList<Material>());
		}
		return pageCheck;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Material getByNum(BigInteger projectId, String num) {
		return sqlSessionFactory.openSession().getMapper(MaterialMapper.class).selectByNum(projectId, num);
	}

}
