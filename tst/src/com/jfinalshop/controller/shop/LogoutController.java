package com.jfinalshop.controller.shop;

import com.jfinal.ext.route.ControllerBind;
import com.jfinalshop.model.Member;
import com.jfinalshop.service.MemberService;
import com.jfinalshop.util.WebUtils;

/**
 * Controller - 会员注销
 * 
 * 
 */
@ControllerBind(controllerKey = "/logout")
public class LogoutController extends BaseController {

	private MemberService memberService = enhance(MemberService.class);

	/**
	 * 注销
	 */
	public void index() {
		WebUtils.removeCookie(getRequest(), getResponse(), Member.USERNAME_COOKIE_NAME);
		WebUtils.removeCookie(getRequest(), getResponse(), Member.NICKNAME_COOKIE_NAME);
		if (memberService.isAuthenticated()) {
			getSession().removeAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME);
		}
		redirect("/");
	}

}