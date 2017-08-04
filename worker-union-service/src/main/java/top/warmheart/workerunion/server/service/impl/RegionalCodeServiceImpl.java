package top.warmheart.workerunion.server.service.impl;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import top.warmheart.server.util.WhStringUtil;
import top.warmheart.server.util.exception.WhNull;
import top.warmheart.workerunion.server.dao.RegionalCodeMapper;
import top.warmheart.workerunion.server.model.RegionalCode;
import top.warmheart.workerunion.server.service.RegionalCodeService;

@Service("regionalCodeServiceImpl")
@Scope("singleton")
public class RegionalCodeServiceImpl implements RegionalCodeService {
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
	public String getNameByCode(String code) {
		WhNull.checkTrimEmpty(code);
		RegionalCodeMapper regionalCodeMapper = sqlSessionFactory.openSession().getMapper(RegionalCodeMapper.class);

		RegionalCode regionalCode = regionalCodeMapper.selectByCode(code.substring(0, 6));
		if (regionalCode != null) {
			return WhStringUtil.trimAll(regionalCode.getValue());
		}

		regionalCode = regionalCodeMapper.selectByCode(code.substring(0, 4) + "00");
		if (regionalCode != null) {
			return WhStringUtil.trimAll(regionalCode.getValue());
		}

		regionalCode = regionalCodeMapper.selectByCode(code.substring(0, 2) + "0000");
		if (regionalCode != null) {
			return WhStringUtil.trimAll(regionalCode.getValue());
		}

		return "";
	}
}
