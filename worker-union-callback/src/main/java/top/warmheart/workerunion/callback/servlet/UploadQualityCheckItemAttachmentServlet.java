package top.warmheart.workerunion.callback.servlet;

import java.io.IOException;
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

import top.warmheart.workerunion.server.model.QualityCheckItemAttachment;
import top.warmheart.workerunion.server.service.QualityCheckItemService;

/**
 * 上传附件
 * 
 * @author seulad
 *
 */
public class UploadQualityCheckItemAttachmentServlet extends CallbackServlet {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -7118090492092527803L;

	private static final Logger LOG = LoggerFactory.getLogger(UploadQualityCheckItemAttachmentServlet.class);

	private QualityCheckItemService qualityCheckItemService;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UploadQualityCheckItemAttachmentServlet() {
		super();
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(config
				.getServletContext());
		qualityCheckItemService = (QualityCheckItemService) webApplicationContext.getBean("qualityCheckItemService");
	}

	/**
	 * POST请求结果
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		String ossCallbackBody = GetPostBody(request.getInputStream(),
				Integer.parseInt(request.getHeader("content-length")));
		boolean ret = VerifyOSSCallbackRequest(request, ossCallbackBody);

		String ossCallbackBodyEx = URLDecoder.decode(ossCallbackBody, "UTF-8");
		LOG.debug("verify result:" + ret);
		LOG.debug("OSS Callback Body:" + ossCallbackBodyEx);
		Map<String, String> params = parseParams(ossCallbackBodyEx);
		if (ret) {
			QualityCheckItemAttachment qualityCheckItemAttachment = new QualityCheckItemAttachment();
			qualityCheckItemAttachment.setCompanyId(new BigInteger(params.get("companyId")));
			qualityCheckItemAttachment.setProjectId(new BigInteger(params.get("projectId")));
			qualityCheckItemAttachment.setQualityCheckItemId(new BigInteger(params.get("qualityCheckItemId")));
			qualityCheckItemAttachment.setName(parseAttachmentName(params.get("object")));
			qualityCheckItemAttachment.setContentType(params.get("mimeType"));
			qualityCheckItemAttachment.setType(params.get("type"));
			qualityCheckItemAttachment.setStaffId(new BigInteger(params.get("staffId")));
			qualityCheckItemAttachment.setStaffName(params.get("staffName"));
			qualityCheckItemAttachment.setSize(new BigInteger((params.get("size"))));
			qualityCheckItemAttachment.setPath(params.get("object"));
			try {
				qualityCheckItemService.addAttachment(qualityCheckItemAttachment);
				// 整理输入
				response(request, response, "{\"returnCode\":\"SUCCESS\"}", HttpServletResponse.SC_OK);
				return;
			} catch (Exception e) {
				response(request, response,
						"{\"returnCode\":\"FAIL\", \"errorCode\":\"USER_EXCEPTION\", \"errorCodeDesc\":\"系统异常\"}",
						HttpServletResponse.SC_BAD_REQUEST);
				return;
			}
			
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
}
