package com.jfinalshop.controller.admin.ses;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.jfinal.ext.route.ControllerBind;
import com.jfinalshop.Pageable;
import com.jfinalshop.controller.admin.BaseController;
import com.jfinalshop.model.SCard;
import com.jfinalshop.model.SRecommend;
import com.jfinalshop.service.ses.CardService;
import com.jfinalshop.service.ses.RecommendService;

/**
 * Controller - 推荐记录列表
 * 
 * 
 */
@ControllerBind(controllerKey = "/admin/recommend")
public class RecommendController extends BaseController {

	private RecommendService recommendService = enhance(RecommendService.class);

	/**
	 * 列表
	 */
	public void list() {
		Pageable pageable = getBean(Pageable.class);
		setAttr("pageable", pageable);
		// setAttr("page", recommendService.findPage(pageable));

		// 搜索属性、搜索值
		String mobile = getPara("mobile");
		setAttr("mobile", mobile);
		setAttr("page", recommendService.findPagememberId(mobile, pageable));
		render("/admin/recommend/list.ftl");
	}

}