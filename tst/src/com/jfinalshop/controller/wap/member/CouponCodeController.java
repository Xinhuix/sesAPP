package com.jfinalshop.controller.wap.member;

import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinalshop.Pageable;
import com.jfinalshop.controller.wap.BaseController;
import com.jfinalshop.model.Member;
import com.jfinalshop.service.CouponCodeService;
import com.jfinalshop.service.MemberService;

/**
 * Controller - 用户中心 - 优惠码
 * 
 * 
 */
@ControllerBind(controllerKey = "/wap/member/coupon_code")
@Before(com.jfinalshop.interceptor.WapMemberInterceptor.class)
public class CouponCodeController extends BaseController {
	
	private static final int PAGE_SIZE = 10;
	
	private MemberService memberService = enhance(MemberService.class);
	private CouponCodeService couponCodeService = enhance(CouponCodeService.class);
	
	/**
	 * 列表
	 */
	public void list() {
		Integer pageNumber = getParaToInt("pageNumber");
		Boolean isUsed = getParaToBoolean("isUsed", null);
		Member member = memberService.getCurrent();
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		setAttr("pages", couponCodeService.findPage(member, pageable, isUsed));
		setAttr("isUsed", isUsed == null ? "all" : isUsed);
		setAttr("title" , "用户中心 - 优惠码");
		render("/wap/member/coupon_code/list.ftl");
	}

}
