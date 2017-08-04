package top.warmheart.workerunion.server.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
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
import top.warmheart.workerunion.server.model.Attachment;
import top.warmheart.workerunion.server.model.Project;
import top.warmheart.workerunion.server.service.ProjectService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/beans.xml")
@Transactional
public class ProjectServiceImplTester {
	private ProjectService projectService;

	public ProjectService getProjectService() {
		return projectService;
	}

	@Autowired
	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}

	@Test
	public void testAdd() {
		Project project = new Project();
		project.setCompanyId(BigInteger.valueOf(1L));
		project.setName("WH");
		project.setNum("000000");
		project.setConstructerName("WH");
		project.setDesignerName("WH");
		project.setSuperviserName("WH");
		project.setAddress("");
		project.setType("");
		project.setScale("12");
		project.setBidDuration(1200);
		project.setFileStatus(Project.FILE_STATUS_GOING);
		project.setCollapseStatus(Project.COLLAPSE_STATUS_GOING);
		project.setBidPrice(BigDecimal.valueOf(1200L));
		project.setMemo("");
		project.setCstCreate(new Date());
		project.setCstModified(project.getCstCreate());
		project.setDel(false);

		Attachment attachment = new Attachment();
		attachment.setCompanyId(BigInteger.valueOf(2L));
		attachment.setProjectId(BigInteger.valueOf(2L));
		attachment.setName("test.mp4");
		attachment.setContentType("");
		attachment.setSize(BigInteger.valueOf(1200L));
		attachment.setPath("");
		attachment.setType("");
		attachment.setStaffId(BigInteger.valueOf(1L));
		attachment.setStaffName("");
		attachment.setCstCreate(new Date());
		attachment.setCstModified(attachment.getCstCreate());
		attachment.setDel(false);

		projectService.add(project, attachment);
		assertEquals(project.getId(), attachment.getProjectId());
		assertNotNull(attachment.getId());
	}

	@Test
	public void testGetById() {
		assertNotNull(projectService.getById(BigInteger.valueOf(1L)));
	}

	@Test
	public void testModifyById() {
		Project project = projectService.getById(BigInteger.valueOf(1L));
		project.setCollapseStatus(Project.COLLAPSE_STATUS_COLLAPSED);
		project.setFileStatus(Project.FILE_STATUS_ARCHIVED);
		assertEquals(1, projectService.modifyById(project));
	}

	@Test
	public void testGetByNum() {
		assertNotNull(projectService.getByNum(BigInteger.valueOf(1L), "000001"));
	}

	@Test
	public void testPageAll() {
		assertEquals(1, projectService.pageAll(BigInteger.valueOf(1L), new Page<Void>(12, 2)).getList().size());
		assertEquals(1, projectService.pageAllEx(BigInteger.valueOf(1L), BigInteger.valueOf(1L), new Page<Void>(12, 2))
				.getList().size());
	}

	@Test
	public void testPageByStatusAndYear() throws ParseException {
		assertEquals(
				1,
				projectService
						.pageByStatusAndYear(BigInteger.valueOf(1L), Project.FILE_STATUS_GOING,
								WhDateUtil.parseDateTime("2017-03-01 00:00:00"), new Page<Void>(12, 2)).getList()
						.size());
		assertEquals(
				1,
				projectService
						.pageByStatusAndYearEx(BigInteger.valueOf(1L), BigInteger.valueOf(1L), Project.FILE_STATUS_GOING,
								WhDateUtil.parseDateTime("2017-03-01 00:00:00"), new Page<Void>(12, 2)).getList()
						.size());
	}

	@Test
	public void testPageByStatus() {
		assertEquals(1,
				projectService.pageByStatus(BigInteger.valueOf(1L), Project.FILE_STATUS_GOING, new Page<Void>(12, 2))
						.getList().size());
		assertEquals(
				1,
				projectService
						.pageByStatusEx(BigInteger.valueOf(1L), BigInteger.valueOf(1L), Project.FILE_STATUS_GOING,
								new Page<Void>(12, 2)).getList().size());
	}

	@Test
	public void testPageByYear() throws ParseException {
		assertEquals(
				1,
				projectService
						.pageByYear(BigInteger.valueOf(1L), WhDateUtil.parseDateTime("2017-03-01 00:00:00"),
								new Page<Void>(3, 2)).getList().size());
		assertEquals(
				1,
				projectService
						.pageByYearEx(BigInteger.valueOf(1L), BigInteger.valueOf(1L),
								WhDateUtil.parseDateTime("2017-03-01 00:00:00"), new Page<Void>(3, 2)).getList().size());
	}

	@Test
	public void testListSimpleByCompanyId() throws ParseException {
		assertEquals(1, projectService.listSimpleByCompanyId(BigInteger.valueOf(1L)).size());
	}

	@Test
	public void testListSimpleByFileStatus() throws ParseException {
		assertEquals(1, projectService.listSimpleByFileStatus(BigInteger.valueOf(1L), Project.FILE_STATUS_GOING).size());
		assertEquals(0, projectService.listSimpleByFileStatus(BigInteger.valueOf(1L), Project.FILE_STATUS_ARCHIVED)
				.size());

	}
}
