package com.jfinalshop.controller.admin.ses;

import org.apache.http.util.TextUtils;

import com.jfinal.ext.route.ControllerBind;
import com.jfinalshop.Pageable;
import com.jfinalshop.controller.admin.BaseController;
import com.jfinalshop.model.SCard;
import com.jfinalshop.service.ses.CardService;

/**
 * Controller - 会员卡
 * 
 * 
 */
@ControllerBind(controllerKey = "/admin/card")
public class CardController extends BaseController {

	private CardService cardService = enhance(CardService.class);

	/**
	 * 列表
	 */
	public void list() {
		Pageable pageable = getBean(Pageable.class);
		setAttr("pageable", pageable);
		setAttr("page", cardService.findPage(pageable));
		render("/admin/card/list.ftl");
	}
	
	
	/**
	 * 添加
	 */
	public void add() {
		render("/admin/card/add.ftl");
	}

	/**
	 * 保存
	 */
	public void save() {
		SCard sCard = getModel(SCard.class);
		String limitnum=this.getPara("sCard.limitnum");
		sCard.setLimitnum(TextUtils.isEmpty(limitnum)?0:Integer.parseInt(limitnum));
		cardService.save(sCard);
		addFlashMessage(SUCCESS_MESSAGE);
		redirect("list.jhtml");
		
	}
	
	/**
	 * 编辑
	 */
	public void edit() {
		Long id = getParaToLong("id");
		setAttr("scard", cardService.find(id));
		render("/admin/card/edit.ftl");
	}
	
	
	/**
	 * 更新
	 */
	public void update() {
		SCard sCard = getModel(SCard.class);
		cardService.update(sCard);
		addFlashMessage(SUCCESS_MESSAGE);
		redirect("/admin/card/list.jhtml");
	}
	
	/**
	 * 删除
	 */
	public void delete() {
		Long[] ids = getParaValuesToLong("ids");
		cardService.delete(ids);
		renderJson(SUCCESS_MESSAGE);
	}
	

}