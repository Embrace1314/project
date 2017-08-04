package top.warmheart.workerunion.fm.server.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;

import top.warmheart.workerunion.fm.server.action.ex.ActionJson;
import top.warmheart.workerunion.server.model.PurchaseItem;
import top.warmheart.workerunion.server.service.ProjectService;
import top.warmheart.workerunion.server.service.PurchaseItemService;

import com.alibaba.fastjson.JSONObject;

@SuppressWarnings("serial")
@Scope("prototype")
public class PurchaseItemAction extends ActionJson {
	private PurchaseItemService purchaseItemService;
	private ProjectService projectService;
	public PurchaseItemService getPurchaseItemService() {
		return purchaseItemService;
	}

	@Resource(name = "purchaseItemService")
	public void setPurchaseItemService(PurchaseItemService purchaseItemService) {
		this.purchaseItemService = purchaseItemService;
	}
	public ProjectService getProjectService() {
		return projectService;
	}

	@Resource(name = "projectService")
	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}
	/**
	 * 获取企业采购条目列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String listItem() throws Exception {
		// 查找附件信息
		List<PurchaseItem> purchaseItems = purchaseItemService.list();

		JSONObject json = getSuccessJsonTemplate();
		json.put("purchaseItems", purchaseItems);
		writeStream(json);
		return SUCCESS;
	}

}
