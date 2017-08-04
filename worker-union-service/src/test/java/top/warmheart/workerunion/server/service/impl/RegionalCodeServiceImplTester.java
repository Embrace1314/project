package top.warmheart.workerunion.server.service.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import top.warmheart.workerunion.server.service.RegionalCodeService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/beans.xml")
@Transactional
public class RegionalCodeServiceImplTester {
	private RegionalCodeService regionalCodeService;

	public RegionalCodeService getRegionalCodeService() {
		return regionalCodeService;
	}

	@Autowired
	public void setRegionalCodeService(RegionalCodeService regionalCodeService) {
		this.regionalCodeService = regionalCodeService;
	}

	@Test
	public void testGetNameByCode() {
		assertEquals("江西省赣州市于都县", regionalCodeService.getNameByCode("360731"));
	}

}
