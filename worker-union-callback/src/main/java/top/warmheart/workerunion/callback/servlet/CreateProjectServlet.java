package top.warmheart.workerunion.callback.servlet;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import top.warmheart.server.util.WhStringUtil;
import top.warmheart.server.util.exception.WhNull;
import top.warmheart.workerunion.callback.exception.WhIllegalParamException;
import top.warmheart.workerunion.callback.exception.WhInvalidStaffException;
import top.warmheart.workerunion.callback.exception.WhProjectExistException;
import top.warmheart.workerunion.server.model.Attachment;
import top.warmheart.workerunion.server.model.Project;
import top.warmheart.workerunion.server.service.ProjectService;

/**
 * Servlet implementation class CreateProjectServlet
 */
public class CreateProjectServlet extends CallbackServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(CreateProjectServlet.class);

	private ProjectService projectService;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CreateProjectServlet() {
		super();
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(config
				.getServletContext());
		projectService = (ProjectService) webApplicationContext.getBean("projectService");
	}

	/**
	 * POST请求结果
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		String ossCallbackBody = GetPostBody(request.getInputStream(),
				Integer.parseInt(request.getHeader("content-length")));
		//boolean ret = VerifyOSSCallbackRequest(request, ossCallbackBody);
		boolean ret = true;
		String ossCallbackBodyEx = URLDecoder.decode(ossCallbackBody, "UTF-8");
		LOG.debug("verify result:" + ret);
		LOG.debug("OSS Callback Body:" + ossCallbackBodyEx);
		Map<String, String> params = parseParams(ossCallbackBodyEx);
		if (ret) {
			/*
			 * 整理数据
			 */
			Project project = new Project();
			project.setCompanyId(new BigInteger(params.get("companyId")));
			project.setNum(params.get("num"));
			project.setName(params.get("name"));
			project.setConstructerName(params.get("constructerName"));
			project.setDesignerName(params.get("designerName"));
			project.setSuperviserName(params.get("superviserName"));
			project.setAddress(params.get("address"));
			project.setType(params.get("type"));
			project.setScale(params.get("scale"));
			project.setBidPrice(new BigDecimal(params.get("bidPrice")));
			project.setBidDuration(Integer.parseInt(params.get("bidDuration")));
			project.setFileStatus(Project.FILE_STATUS_GOING);
			project.setCollapseStatus(Project.COLLAPSE_STATUS_GOING);
			project.setMemo("");
			
			Attachment attachment = new Attachment();
			attachment.setCompanyId(project.getCompanyId());
			attachment.setProjectId(project.getId());
			attachment.setName(parseAttachmentName(params.get("object")));
			attachment.setContentType(params.get("mimeType"));
			attachment.setType(Attachment.TYPE_LETTER_OF_ACCEPTANCE);
			attachment.setStaffId(new BigInteger(params.get("staffId")));
			attachment.setStaffName(params.get("staffName"));
			attachment.setSize(new BigInteger(params.get("size")));
			attachment.setPath(params.get("object"));
			try {
				add(project, attachment);
			} catch (WhIllegalParamException | WhProjectExistException | WhInvalidStaffException e) {
				response(
						request,
						response,
						"{\"returnCode\":\"FAIL\", \"errorCode\":\"USER_EXCEPTION\", \"errorCodeDesc\":\""
								+ e.getMessage() + "\"}", HttpServletResponse.SC_BAD_REQUEST);
				return;
			} catch (Exception e) {
				response(request, response,
						"{\"returnCode\":\"FAIL\", \"errorCode\":\"USER_EXCEPTION\", \"errorCodeDesc\":\"系统异常\"}",
						HttpServletResponse.SC_BAD_REQUEST);
				return;
			}
			// 整理输入
			response(request, response, "{\"returnCode\":\"SUCCESS\"}", HttpServletResponse.SC_OK);
		} else {
			response(request, response,
					"{\"returnCode\":\"FAIL\", \"errorCode\":\"USER_EXCEPTION\", \"errorCodeDesc\":\"检测到客户端安全异常\"}",
					HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
	}

	/**
	 * 获取附件名称
	 * 
	 * @param object
	 * @return
	 */
	private String parseAttachmentName(String object) {
		String fileName = object.split("/")[object.split("/").length - 1];
		return fileName.substring(fileName.indexOf("_") + 1);
	}

	/**
	 * 创建新项目
	 * 
	 * @return
	 * @throws WhIllegalParamException
	 * @throws WhProjectExistException
	 * @throws WhInvalidStaffException
	 * @throws Exception
	 */
	public void add(Project project, Attachment attachment) throws WhIllegalParamException, WhProjectExistException,
			WhInvalidStaffException {
		// 校验参数
		WhNull.check(project);
		WhNull.checkTrimEmpty(project.getNum());
		WhNull.checkTrimEmpty(project.getName());
		WhNull.checkTrimEmpty(project.getConstructerName());
		WhNull.checkTrimEmpty(project.getDesignerName());
		WhNull.checkTrimEmpty(project.getSuperviserName());
		WhNull.checkTrimEmpty(project.getAddress());
		WhNull.checkTrimEmpty(project.getType());
		WhNull.checkTrimEmpty(project.getScale());
		WhNull.check(project.getBidPrice());
		if (project.getBidPrice().compareTo(BigDecimal.valueOf(0)) < 0) {
			throw new WhIllegalParamException("中标价不能小于0！");
		}
		WhNull.check(project.getBidDuration());
		if (project.getBidDuration() <= 0) {
			throw new WhIllegalParamException("中标工期必须大于0！");
		}

		/*
		 * 校验是否存在重复的项目编号
		 */
		Project projectEx = projectService.getByNum(project.getCompanyId(), WhStringUtil.trimAll(project.getNum()));
		if (null != projectEx) {
			throw new WhProjectExistException("重复的项目编号！");
		}

		// 整理数据
		project.setNum(WhStringUtil.trimAll(project.getNum()));
		project.setName(WhStringUtil.trimAll(project.getName()));
		project.setConstructerName(WhStringUtil.trimAll(project.getConstructerName()));
		project.setDesignerName(WhStringUtil.trimAll(project.getDesignerName()));
		project.setSuperviserName(WhStringUtil.trimAll(project.getSuperviserName()));
		project.setAddress(WhStringUtil.trimAll(project.getAddress()));
		project.setType(WhStringUtil.trimAll(project.getType()));

		// 整理附件中标通知书信息
		projectService.add(project, attachment);

	}

}
