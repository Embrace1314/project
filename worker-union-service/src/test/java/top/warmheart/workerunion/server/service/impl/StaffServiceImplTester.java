package top.warmheart.workerunion.server.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import top.warmheart.server.util.WhDateUtil;
import top.warmheart.server.util.dto.Page;
import top.warmheart.workerunion.server.dto.StaffDto;
import top.warmheart.workerunion.server.model.Staff;
import top.warmheart.workerunion.server.service.StaffService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/beans.xml")
@Transactional
public class StaffServiceImplTester {
	private StaffService staffService;

	public StaffService getStaffService() {
		return staffService;
	}

	@Autowired
	public void setStaffService(StaffService staffService) {
		this.staffService = staffService;
	}

	@Test
	public void testAdd() {
		Staff staff = new Staff();
		staff.setCompanyId(BigInteger.valueOf(1L));
		staff.setName("Test");
		staff.setSex("MALE");
		staff.setJobNo("000000001");
		staff.setNativePlace("");
		staff.setPhone("");
		staff.setIdCardNo("");
		staff.setAddress("");
		staff.setEntryDate(new Date());
		staff.setCareer("");
		staff.setBirthday(new Date());
		staff.setSalt("");
		staff.setPassword("");
		staff.setCstCreate(new Date());
		staff.setCstModified(staff.getCstCreate());
		staff.setMemo("");
		staff.setDel(false);
		
		assertEquals(1, staffService.add(staff));
	}

	@Test
	public void testModifyById() {
		Staff staff = new Staff();
		staff.setId(BigInteger.valueOf(1L));
		staff.setCompanyId(BigInteger.valueOf(1L));
		staff.setName("Test");
		staff.setSex("MALE");
		staff.setJobNo("000000001");
		staff.setNativePlace("");
		staff.setPhone("");
		staff.setIdCardNo("");
		staff.setAddress("");
		staff.setEntryDate(new Date());
		staff.setCareer("");
		staff.setBirthday(new Date());
		staff.setSalt("");
		staff.setPassword("");
		staff.setCstCreate(new Date());
		staff.setCstModified(staff.getCstCreate());
		staff.setMemo("");
		staff.setDel(false);
		
		assertEquals(1, staffService.modifyById(staff));
	}

	@Test
	public void testGetByJobNo() {
		assertNotNull(staffService.getByJobNo(BigInteger.valueOf(1L), "000001"));
	}

	@Test
	public void testGetById() {
		assertNotNull(staffService.getById(BigInteger.valueOf(1L)));
	}

	@Test
	public void testRemoveById() {
		assertEquals(1, staffService.removeById(BigInteger.valueOf(1L)));
	}

	@Test
	public void testGetDetailById() {
		assertNotNull(staffService.getDetailById(BigInteger.valueOf(1L)));
	}

	@Test
	public void testGetAuthenticationStaff() {
		assertNotNull(staffService.getAuthenticationStaff(BigInteger.valueOf(1L)));
	}

	@Test
	public void testPageByFuzzy() throws ParseException {
		StaffDto staffDto = new StaffDto();
		staffDto.setCompanyId(BigInteger.valueOf(1L));
		staffDto.setName("Staff");
		staffDto.setJobNo("00");
		staffDto.setIdCardNo("1990");
		staffDto.setRoleName("MIN");
		staffDto.setProjectName("WH");
		staffDto.setStartDate(WhDateUtil.parseDateTime("2017-03-01 00:00:00"));
		staffDto.setEndDate(WhDateUtil.parseDateTime("2017-03-29 00:00:00"));
		assertEquals(1, staffService.pageByFuzzy(staffDto, new Page<Void>(12, 2)).getList().size());
	}

}
