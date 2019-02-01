package com.jfinalshop.api.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;
import com.jfinalshop.Setting;
import com.jfinalshop.api.common.bean.BaseResponse;
import com.jfinalshop.api.common.bean.LoginResponse;
import com.jfinalshop.api.common.bean.Require;
import com.jfinalshop.api.common.token.TokenManager;
import com.jfinalshop.model.Member;
import com.jfinalshop.model.PointLog;
import com.jfinalshop.model.Sms;
import com.jfinalshop.service.MemberRankService;
import com.jfinalshop.service.MemberService;
import com.jfinalshop.service.SmsService;
import com.jfinalshop.util.SMSUtils;
import com.jfinalshop.util.SystemUtils;


@ControllerBind(controllerKey = "/api/account")
public class AccountAPIController extends BaseAPIController {
	
	private MemberService memberService = enhance(MemberService.class);
	private MemberRankService memberRankService = enhance(MemberRankService.class);
	private SmsService smsService = enhance(SmsService.class);
	
	
	/**
	 * 检查用户名是否被禁用或已存在
	 */
	public void checkUsername() {
		String username = getPara("username");
		if (StringUtils.isEmpty(username)) {
			renderJson(false);
			return;
		}
		renderJson(!memberService.usernameDisabled(username) && !memberService.usernameExists(username));
	}
	
	/**
	 * 发送会员注册短信
	 * 
	 */
    public void sendRegisterSms() {
		String username = getPara("username");
		String smsTypeName = getPara("smsType");
		Setting.SmsType smsType = StrKit.notBlank(smsTypeName) ? Setting.SmsType.valueOf(smsTypeName) : null;
		
        if (StringUtils.isEmpty(username)) {
            renderArgumentError("登录名不能为空！");
            return;
        }
        
        if (smsType == null || !Setting.SmsType.memberRegister.equals(smsType)) {
            renderArgumentError("短信类型不能为空,或不是注册类型！");
            return;
        }
        
        //检查手机号码有效性
        if (!SMSUtils.isMobileNo(username)) {
            renderArgumentError("请检查手机号是否正确！");
            return;
        }
        
        String smsCode = SMSUtils.randomSMSCode(4);
        smsService.send(username, smsCode);
        
        Sms sms = new Sms();
        sms.setMobile(username);
        sms.setSmsCode(smsCode);
        sms.setSmsType(smsType.ordinal());
        smsService.save(sms);
		renderJson(new BaseResponse("短信发送成功！【"+smsCode+"】"));
		
    }
    
    /**
	 * 注册提交
	 */
	public void register(){
		String username = getPara("username");
        String smsCode = getPara("smsCode");
        String password = getPara("password");
        
        //校验必填项参数
		if(!notNull(Require.me().put(username, "登录名不能为空！").put(smsCode, "验证码不能为空！"))){
			return;
		}
		
		//检查手机号码有效性
        if (!SMSUtils.isMobileNo(username)) {
            renderArgumentError("请检查手机号是否正确！");
            return;
        }
        
		if (!smsService.smsExists(username, smsCode, Setting.SmsType.memberRegister)) {
			renderArgumentError("验证码输入错误");
			return;
		}
		
		if (memberService.usernameDisabled(username) || memberService.usernameExists(username)) {
			renderArgumentError("用户名被禁用或已被注册");
			return;
		}
		
		Setting setting = SystemUtils.getSetting();
		if (!setting.getIsRegisterEnabled()) {
			renderJson("用户注册功能已关闭");
			return;
		}
		
		if (username.length() < setting.getUsernameMinLength() || username.length() > setting.getUsernameMaxLength()) {
			renderArgumentError("用户名长度在" + setting.getUsernameMinLength() + "-" + setting.getUsernameMaxLength());
			return;
		}
		
		if (password.length() < setting.getPasswordMinLength() || password.length() > setting.getPasswordMaxLength()) {
			renderArgumentError("密码长度在" + setting.getPasswordMinLength() + "-" + setting.getPasswordMaxLength());
			return;
		}
		
		Member member = new Member();
		member.setUsername(StringUtils.lowerCase(username));
		member.setPassword(DigestUtils.md5Hex(password));
		member.setNickname(null);
		member.setPoint(0d);
		member.setBalance(BigDecimal.ZERO);
		member.setAmount(BigDecimal.ZERO);
		member.setIsEnabled(true);
		member.setIsLocked(false);
		member.setLoginFailureCount(0);
		member.setLockedDate(null);
		member.setRegisterIp(getRequest().getRemoteAddr());
		member.setLoginIp(getRequest().getRemoteAddr());
		member.setLoginDate(new Date());
		member.setLoginPluginId(null);
		member.setOpenId(null);
		member.setLockKey(DigestUtils.md5Hex(UUID.randomUUID() + RandomStringUtils.randomAlphabetic(30)));
		member.setSafeKey(null);
		member.setMemberRankId(memberRankService.findDefault().getId());
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
		memberService.save(member);

		if (setting.getRegisterPoint() > 0) {
			memberService.addPoint(member, setting.getRegisterPoint(), PointLog.Type.reward, null, null);
		}
		smsService.delete(username, smsCode);
		LoginResponse response = new LoginResponse();
		response.setToken(TokenManager.getMe().generateToken(member));
		setSessionAttr(Member.PRINCIPAL_ATTRIBUTE_NAME, TokenManager.getMe().validate(response.getToken()));
		renderJson(new BaseResponse("success"));
	}
	
	
	/**
	 * 登录提交
	 * 
	 */
    public void login() {
    	String username = getPara("username");
        String password = getPara("password");
        
        if (StrKit.notBlank(password) && !notNull(Require.me().put(username, "登录名不能为空！").put(password, "密码不能为空！"))) {
            return;
        }
        Member member = memberService.findByUsername(username);
        if (member == null) {
        	renderArgumentError("此账号不存在");
			return;
		}
		if (!member.getIsEnabled()) {
			renderArgumentError("此账号已被禁用");
			return;
		}
		Setting setting = SystemUtils.getSetting();
		if (member.getIsLocked()) {
			if (ArrayUtils.contains(setting.getAccountLockTypes(), Setting.AccountLockType.member)) {
				int loginFailureLockTime = setting.getAccountLockTime();
				if (loginFailureLockTime == 0) {
					renderArgumentError("此账号已被锁定");
					return;
				}
				Date lockedDate = member.getLockedDate();
				Date unlockDate = DateUtils.addMinutes(lockedDate, loginFailureLockTime);
				if (new Date().after(unlockDate)) {
					member.setLoginFailureCount(0);
					member.setIsLocked(false);
					member.setLockedDate(null);
					memberService.update(member);
				} else {
					renderArgumentError("此账号已被锁定");
					return;
				}
			} else {
				member.setLoginFailureCount(0);
				member.setIsLocked(false);
				member.setLockedDate(null);
				memberService.update(member);
			}
		}
		
		if (!DigestUtils.md5Hex(password).equals(member.getPassword())) {
			int loginFailureCount = member.getLoginFailureCount() + 1;
			if (loginFailureCount >= setting.getAccountLockCount()) {
				member.setIsLocked(true);
				member.setLockedDate(new Date());
			}
			member.setLoginFailureCount(loginFailureCount);
			memberService.update(member);
			if (ArrayUtils.contains(setting.getAccountLockTypes(), Setting.AccountLockType.member)) {
				renderArgumentError("密码错误，若连续" + setting.getAccountLockCount() + "次密码错误账号将被锁定");
				return;
			} else {
				renderArgumentError("用户名或密码错误");
				return;
			}
		}
		member.setLoginIp(getRequest().getRemoteAddr());
		member.setLoginDate(new Date());
		member.setLoginFailureCount(0);
		memberService.update(member);
		
        LoginResponse response = new LoginResponse();
        member.remove("password");
 		response.setInfo(member);
 		response.setMessage("login success");
 		response.setToken(TokenManager.getMe().generateToken(member));
        if(StrKit.notBlank(response.getToken())) {
        	setSessionAttr(Member.PRINCIPAL_ATTRIBUTE_NAME, TokenManager.getMe().validate(response.getToken()));
        }
		renderJson(response);
    }
    
}

