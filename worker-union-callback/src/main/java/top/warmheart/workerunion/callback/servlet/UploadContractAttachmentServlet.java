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

import top.warmheart.workerunion.server.model.ContractAttachment;
import top.warmheart.workerunion.server.service.ContractService;

/**
 * 上传附件
 * 
 * @author seulad
 *
 */
public class UploadContractAttachmentServlet extends CallbackServlet {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -7118090492092527803L;

	private static final Logger LOG = LoggerFactory.getLogger(UploadContractAttachmentServlet.class);

	private ContractService contractService;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UploadContractAttachmentServlet() {
		super();
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(config
				.getServletContext());
		contractService = (ContractService) webApplicationContext.getBean("contractService");
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
			ContractAttachment contractAttachment = new ContractAttachment();
			contractAttachment.setCompanyId(new BigInteger(params.get("companyId")));
			contractAttachment.setContractId(new BigInteger(params.get("contractId")));
			contractAttachment.setName(parseAttachmentName(params.get("object")));
			contractAttachment.setContentType(params.get("mimeType"));
			contractAttachment.setType(params.get("type"));
			contractAttachment.setStaffId(new BigInteger(params.get("staffId")));
			contractAttachment.setStaffName(params.get("staffName"));
			contractAttachment.setSize(new BigInteger((params.get("size"))));
			contractAttachment.setPath(params.get("object"));
			try {
				contractService.addAttachment(contractAttachment);
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


