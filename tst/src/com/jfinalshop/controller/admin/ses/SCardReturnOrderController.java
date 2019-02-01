package com.jfinalshop.controller.admin.ses;

import com.jfinal.ext.route.ControllerBind;
import com.jfinalshop.Pageable;
import com.jfinalshop.controller.admin.BaseController;
import com.jfinalshop.model.SCardorder;
import com.jfinalshop.service.ses.CardorderService;

/**
 * Controller -月卡订单
 * 
 * 
 */
@ControllerBind(controllerKey = "/admin/cardreturn")
public class SCardReturnOrderController extends BaseController {

	//private CardmonthService monthcardService = enhance(CardmonthService.class);
	//private CardService cardService = enhance(CardService.class);
	//private AdminService adminService = enhance(AdminService.class);
	
	private CardorderService ordercardService = enhance(CardorderService.class);

	/**
	 * 列表
	 */
	public void list() {
		Pageable pageable = getBean(Pageable.class);
		setAttr("pageable", pageable);
		setAttr("page", ordercardService.findPage("4", false, false, pageable));
		render("list.ftl");
	}
	
	/**
	 * 编辑
	 */
	public void edit() {
		Long id = getParaToLong("id");
		setAttr("o", ordercardService.find(id));
		render("edit.ftl");
	}
	
	
	/**
	 * 更新
	 */
	public void update() {
		SCardorder sCard = getModel(SCardorder.class);
		
		ordercardService.update(sCard);
		addFlashMessage(SUCCESS_MESSAGE);
		redirect("list.jhtml");
	}
	
}