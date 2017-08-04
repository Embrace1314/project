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

import top.warmheart.workerunion.server.model.FinalSettlementItemAttachment;
import top.warmheart.workerunion.server.service.FinalSettlementItemService;

/**
 * 上传附件
 * 
 * @author seulad
 *
 */
public class UploadFinalSettlementItemAttachmentServlet extends CallbackServlet {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -7118090492092527803L;

	private static final Logger LOG = LoggerFactory.getLogger(UploadFinalSettlementItemAttachmentServlet.class);

	private FinalSettlementItemService finalSettlementItemService;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UploadFinalSettlementItemAttachmentServlet() {
		super();
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(config
				.getServletContext());
		finalSettlementItemService = (FinalSettlementItemService) webApplicationContext.getBean("finalSettlementItemService");
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
			FinalSettlementItemAttachment finalSettlementItemAttachment = new FinalSettlementItemAttachment();
			finalSettlementItemAttachment.setCompanyId(new BigInteger(params.get("companyId")));
			finalSettlementItemAttachment.setProjectId(new BigInteger(params.get("projectId")));
			finalSettlementItemAttachment.setFinalSettlementItemId(new BigInteger(params.get("finalSettlementItemId")));
			finalSettlementItemAttachment.setName(parseAttachmentName(params.get("object")));
			finalSettlementItemAttachment.setContentType(params.get("mimeType"));
			finalSettlementItemAttachment.setType(params.get("type"));
			finalSettlementItemAttachment.setStaffId(new BigInteger(params.get("staffId")));
			finalSettlementItemAttachment.setStaffName(params.get("staffName"));
			finalSettlementItemAttachment.setSize(new BigInteger((params.get("size"))));
			finalSettlementItemAttachment.setPath(params.get("object"));
			try {
				finalSettlementItemService.addAttachment(finalSettlementItemAttachment);
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
