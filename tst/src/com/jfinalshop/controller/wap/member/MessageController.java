package com.jfinalshop.controller.wap.member;

import java.util.HashMap;
import java.util.Map;

import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinalshop.Pageable;
import com.jfinalshop.controller.wap.BaseController;
import com.jfinalshop.interceptor.WapMemberInterceptor;
import com.jfinalshop.model.Member;
import com.jfinalshop.service.MemberService;
import com.jfinalshop.service.MessageService;

/**
 * Controller - 用户中心 - 消息
 * 
 * 
 */
@ControllerBind(controllerKey = "/wap/member/message")
@Before(WapMemberInterceptor.class)
public class MessageController extends BaseController {

	/** 每页记录数 */
	private static final int PAGE_SIZE = 10;

	private MessageService messageService = enhance(MessageService.class);
	private MemberService memberService = enhance(MemberService.class);
	
	/**
	 * 列表
	 */
	public void list() {
		Integer pageNumber = getParaToInt("pageNumber");
		Boolean read = getParaToBoolean("read", null);
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		Member member = memberService.getCurrent();
		setAttr("pages", messageService.findPage(member, pageable, read));
		setAttr("read", read == null ? "all" : read);
		setAttr("title" , "我的消息 - 用户中心");
		render("/wap/member/message/list.ftl");
	}
	
	/**
	 * 设置消息已读
	 */
	public void read() {
		Long id = getParaToLong("id");
		Member member = memberService.getCurrent();
		messageService.read(id, member);
		Map<String, String> map = new HashMap<String, String>();
		map.put(STATUS, SUCCESS);
		renderJson(map);
	}
	
}
