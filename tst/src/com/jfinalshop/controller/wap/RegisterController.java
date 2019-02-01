package com.jfinalshop.controller.wap;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.i18n.I18n;
import com.jfinal.i18n.Res;
import com.jfinal.kit.StrKit;
import com.jfinalshop.Principal;
import com.jfinalshop.Setting;
import com.jfinalshop.interceptor.WapInterceptor;
import com.jfinalshop.model.Member;
import com.jfinalshop.model.PointLog;
import com.jfinalshop.model.SRecommend;
import com.jfinalshop.model.Sms;
import com.jfinalshop.service.MemberRankService;
import com.jfinalshop.service.MemberService;
import com.jfinalshop.service.SmsService;
import com.jfinalshop.service.ses.RecommendService;
import com.jfinalshop.util.SMSUtils;
import com.jfinalshop.util.SystemUtils;
import com.jfinalshop.util.WebUtils;

/**
 * Controller - 会员注册
 * 
 * 
 */
@ControllerBind(controllerKey = "/wap/register")
@Before(WapInterceptor.class)
public class RegisterController extends BaseController {
	
	private MemberService memberService = enhance(MemberService.class);
	private MemberRankService memberRankService = enhance(MemberRankService.class);
	private SmsService smsService = enhance(SmsService.class);
	
	private RecommendService recommendService = enhance(RecommendService.class);
	
	/**
	 * 检查用户名是否被禁用或已存在
	 */
	public void checkUsername() {
		String username = getPara("param");
		Map<String, String> map = new HashMap<String, String>();
		if (StringUtils.isEmpty(username)) {
			map.put(STATUS, ERROR);
			map.put(MESSAGE, "手机号不为空!");
			renderJson(map);
			return;
		}
		if (memberService.usernameDisabled(username) || memberService.usernameExists(username)) {
			map.put(STATUS, ERROR);
			map.put(MESSAGE, "手机号已存在或禁用中!");
			renderJson(map);
		} else {
			map.put(STATUS, SUCCESS);
			map.put(MESSAGE, "允许使用!");
			renderJson(map);
		}
	}
	
	/**
	 * 检查邀请码是否存在
	 */
	public void checkreccode() {
		try {
			String reccode = getPara("reccode").trim();//请输入邀请码
			
			Map<String, String> map = new HashMap<String, String>();
			if (!StringUtils.isEmpty(reccode)&&reccode.length()>6) { //邀请码 第七位 开始为 userid
				Long user_id= Long.parseLong(reccode.substring(6, reccode.length())); 
				
			if(memberService.find(user_id) != null){
				map.put(STATUS, SUCCESS);
				map.put(MESSAGE, "邀请码验证通过!");
				renderJson(map);
				return;
			} else {
				map.put(STATUS, ERROR);
				map.put(MESSAGE, "请输入正确邀请码!");
				renderJson(map);
				return;
			}
			}else{
				map.put(STATUS, ERROR);
				map.put(MESSAGE, "请输入正确邀请码!");
				renderJson(map);
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 发送短信
	 */
	public void send() {
		String mobile = getPara("mobile");
		String vcode = SMSUtils.randomSMSCode(4);
		Sms sms = new Sms();
		sms.setMobile(mobile);
		sms.setSmsCode(vcode);
		sms.setSmsType(Setting.SmsType.memberRegister.ordinal());
		smsService.saveOrUpdate(sms);
		smsService.send(mobile, vcode);
		renderJson(vcode);
	}
	
	/**
	 * 注册页面
	 */
	public void index() {
		String reccode = getPara("tjcode");//邀请码
		setAttr("reccode" , reccode);
		setAttr("title" , "用户注册");
		render("/wap/register/index.ftl");
	}

	/**
	 * 注册提交
	 */
	public void submit() {
		String mobile = getPara("mobile");
		String password = getPara("password");
		String pwdconfirm = getPara("pwdconfirm");
		String vcode = getPara("vcode");
		String url_forward = getPara("url_forward");
		
		HttpServletRequest request = getRequest();
		HttpServletResponse response = getResponse();
		
		Map<String, String> map = new HashMap<String, String>();
		
		if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(vcode)) {
			map.put(STATUS, ERROR);
			map.put(MESSAGE, "手机号或验证码不为空!");
			renderJson(map);
			return;
		}
		
		if (!smsService.smsExists(mobile, vcode, Setting.SmsType.memberRegister)) {
			map.put(STATUS, ERROR);
			map.put(MESSAGE, "手机号验证码错误!");
			renderJson(map);
			return;
		}
		
		Res resZh = I18n.use();
		Setting setting = SystemUtils.getSetting();
		if (!setting.getIsRegisterEnabled()) {
			map.put(STATUS, ERROR);
			map.put(MESSAGE, resZh.format("shop.register.disabledExist"));
			renderJson(map);
			return;
		}
		
		if (password == null || pwdconfirm == null || (! password.equals(pwdconfirm))) {
			map.put(STATUS, ERROR);
			map.put(MESSAGE, "两次密码不一致!");
			renderJson(map);
			return;
		} 

		if (mobile.length() < setting.getUsernameMinLength() || mobile.length() > setting.getUsernameMaxLength()) {
			map.put(STATUS, ERROR);
			map.put(MESSAGE, resZh.format("shop.common.invalid"));
			renderJson(map);
			return;
		}
		if (password.length() < setting.getPasswordMinLength() || password.length() > setting.getPasswordMaxLength()) {
			map.put(STATUS, ERROR);
			map.put(MESSAGE, resZh.format("shop.common.invalid"));
			renderJson(map);
			return;
		}
		if (memberService.usernameDisabled(mobile) || memberService.usernameExists(mobile)) {
			map.put(STATUS, ERROR);
			map.put(MESSAGE, resZh.format("shop.register.disabledExist"));
			renderJson(map);
			return;
		}

		Member member = new Member();
		member.removeAttributeValue();
		member.setUsername(StringUtils.lowerCase(mobile));
		member.setPassword(DigestUtils.md5Hex(password));
		member.setNickname(null);
		member.setPoint(0d);
		member.setBalance(BigDecimal.ZERO);
		member.setAmount(BigDecimal.ZERO);
		member.setIsEnabled(true);
		member.setIsLocked(false);
		member.setLoginFailureCount(0);
		member.setLockedDate(null);
		member.setRegisterIp(request.getRemoteAddr());
		member.setLoginIp(request.getRemoteAddr());
		member.setLoginDate(new Date());
		member.setLoginPluginId(null);
		member.setOpenId(null);
		member.setLockKey(DigestUtils.md5Hex(UUID.randomUUID() + RandomStringUtils.randomAlphabetic(30)));
		member.setSafeKey(null);
		member.setMemberRankId(memberRankService.findDefault() != null ? memberRankService.findDefault().getId() : null);
		member.setCart(null);
		member.setOrders(null);
		member.setPaymentLogs(null);
		member.setDepositLogs(null);
		member.setCouponCodes(null);
		member.setReceivers(null);
		member.setReviews(null);
		member.setConsultations(null);
		member.setFavoriteGoods(null);
		member.setProductNotifies(null);
		member.setInMessages(null);
		member.setOutMessages(null);
		member.setPointLogs(null);
		member.setMobile(mobile);
		String openId = (String) request.getSession().getAttribute(Member.OPEN_ID);
		if (StrKit.notBlank(openId)) {
			member.setOpenId(openId);
		}
		Member temp_member= memberService.save(member);
		
		//注册成功 后台，添加推荐记录，当购卡支付后，返回积分
		
		String reccode = getPara("reccode").trim();//请输入邀请码
		
		if (!StringUtils.isEmpty(reccode)&&reccode.length()>6) { //邀请码 第七位 开始为 userid
			Long user_id= Long.parseLong(reccode.substring(6, reccode.length())); 
			if(memberService.find(user_id) != null){
			SRecommend recommend=new SRecommend();
			recommend.setMemberId(memberService.find(user_id).getId()); //推荐人(会员ID)获得推荐积分人
			recommend.setToMemberId(temp_member.getId());//被推荐人(会员ID)
			recommend.setStatus("0");
			recommendService.save(recommend);
			}
		}
		
		if (setting.getRegisterPoint() > 0) {
			memberService.addPoint(member, setting.getRegisterPoint(), PointLog.Type.reward, null, null);
		}

		setSessionAttr(Member.PRINCIPAL_ATTRIBUTE_NAME, new Principal(member.getId(), member.getUsername()));
		WebUtils.addCookie(request, response, Member.USERNAME_COOKIE_NAME, member.getUsername());
		if (StringUtils.isNotEmpty(member.getNickname())) {
			WebUtils.addCookie(request, response, Member.NICKNAME_COOKIE_NAME, member.getNickname());
		}

		map.put(STATUS, SUCCESS);
		map.put(MESSAGE, resZh.format("shop.register.success"));
		map.put("referer", url_forward);
		renderJson(map);
	}
	
}
