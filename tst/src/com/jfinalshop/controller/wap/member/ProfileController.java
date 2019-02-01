package com.jfinalshop.controller.wap.member;

import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinalshop.Setting;
import com.jfinalshop.controller.wap.BaseController;
import com.jfinalshop.interceptor.WapMemberInterceptor;
import com.jfinalshop.model.Member;
import com.jfinalshop.model.MemberAttribute;
import com.jfinalshop.service.MemberAttributeService;
import com.jfinalshop.service.MemberService;
import com.jfinalshop.util.SystemUtils;

/**
 * Controller - 用户中心 - 个人资料
 * 
 * 
 */
@ControllerBind(controllerKey = "/wap/member/profile")
@Before(WapMemberInterceptor.class)
public class ProfileController extends BaseController {

	MemberService memberService = enhance(MemberService.class);
	private MemberAttributeService memberAttributeService = enhance(MemberAttributeService.class);
	/**
	 * 编辑
	 */
	public void edit() {
		setAttr("title" , "安全中心 - 用户中心");
		setAttr("member" , memberService.getCurrent());
		render("/wap/member/profile/edit.ftl");
	}
	
	/**
	 * 银行卡信息
	 */
	public void bankinfo() {
		setAttr("title" , "银行卡信息 - 用户中心");
		setAttr("member" , memberService.getCurrent());
		render("/wap/member/profile/bankinfo.ftl");
	}
	
	
	/**
	 * 更新
	 */
	public void update() {
		Member member = memberService.getCurrent();
		
		String email = getPara("email");
		String nickname = getPara("nickname");
		Setting setting = SystemUtils.getSetting();
		if (!setting.getIsDuplicateEmail() && !memberService.emailUnique(member.getEmail(), email)) {
			redirect(ERROR_VIEW);
			return;
		}
		member.setEmail(email);
		member.setNickname(nickname);
		
		member.removeAttributeValue();
		for (MemberAttribute memberAttribute : memberAttributeService.findList(true, true)) {
			String[] values = getRequest().getParameterValues("memberAttribute_" + memberAttribute.getId());
			if (!memberAttributeService.isValid(memberAttribute, values)) {
				redirect(ERROR_VIEW);
				return;
			}
			Object memberAttributeValue = memberAttributeService.toMemberAttributeValue(memberAttribute, values);
			member.setAttributeValue(memberAttribute, memberAttributeValue);
		}
		memberService.update(member);
		setAttr("member" , memberService.getCurrent());
		setAttr("cur", 1);
		render("/wap/member/profile/bankinfo.ftl");
	}

	
	
}
