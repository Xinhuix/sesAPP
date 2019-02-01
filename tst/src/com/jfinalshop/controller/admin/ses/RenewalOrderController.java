package com.jfinalshop.controller.admin.ses;

import com.jfinal.ext.route.ControllerBind;
import com.jfinalshop.Pageable;
import com.jfinalshop.controller.admin.BaseController;
import com.jfinalshop.service.ses.CardorderService;

/**
 * Controller -月卡订单
 * 
 * 
 */
@ControllerBind(controllerKey = "/admin/cardrenewal")
public class RenewalOrderController extends BaseController {
	
	private CardorderService ordercardService = enhance(CardorderService.class);

	/**
	 * 列表
	 */
	public void list() {
		Pageable pageable = getBean(Pageable.class);
		setAttr("pageable", pageable);
		setAttr("page", ordercardService.findPage("6", false, false, pageable));
		render("list.ftl");
	}
	
}