package com.jfinalshop.controller.admin.ses;

import com.jfinal.ext.route.ControllerBind;
import com.jfinalshop.Pageable;
import com.jfinalshop.controller.admin.BaseController;
import com.jfinalshop.model.Admin;
import com.jfinalshop.model.SCardmonth;
import com.jfinalshop.service.AdminService;
import com.jfinalshop.service.ses.CardService;
import com.jfinalshop.service.ses.CardmonthService;

/**
 * Controller -月卡设置
 * 
 * 
 */
@ControllerBind(controllerKey = "/admin/cardmonth")
public class SCardmonthController extends BaseController {

	private CardmonthService monthcardService = enhance(CardmonthService.class);
	private CardService cardService = enhance(CardService.class);
	private AdminService adminService = enhance(AdminService.class);

	/**
	 * 列表
	 */
	public void list() {
		Pageable pageable = getBean(Pageable.class);
		setAttr("pageable", pageable);
		setAttr("page", monthcardService.findPage(pageable));
		render("/admin/cardmonth/list.ftl");
	}
	
	
	/**
	 * 添加
	 */
	public void add() {
		setAttr("scardAll",cardService.findAll());
		render("/admin/cardmonth/add.ftl");
	}

	/**
	 * 保存
	 */
	public void save() {
		SCardmonth sCard = getModel(SCardmonth.class);
		Admin admin = adminService.getCurrent();
		sCard.setAdminId(admin.getId());//操作员
		monthcardService.save(sCard);
		addFlashMessage(SUCCESS_MESSAGE);
		redirect("list.jhtml");
	}
	
	/**
	 * 编辑
	 */
	public void edit() {
		Long id = getParaToLong("id");
		setAttr("monthcard", monthcardService.find(id));
		setAttr("scardAll",cardService.findAll());
		render("/admin/cardmonth/edit.ftl");
	}
	
	
	/**
	 * 更新
	 */
	public void update() {
		SCardmonth sCard = getModel(SCardmonth.class);
		monthcardService.update(sCard);
		addFlashMessage(SUCCESS_MESSAGE);
		redirect("/admin/cardmonth/list.jhtml");
	}
	
	/**
	 * 删除
	 */
	public void delete() {
		Long[] ids = getParaValuesToLong("ids");
		monthcardService.delete(ids);
		renderJson(SUCCESS_MESSAGE);
	}
	

}