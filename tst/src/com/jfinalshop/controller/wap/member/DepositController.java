package com.jfinalshop.controller.wap.member;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.jfinal.aop.Before;
import com.jfinal.aop.Enhancer;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinalshop.Pageable;
import com.jfinalshop.Setting;
import com.jfinalshop.controller.wap.BaseController;
import com.jfinalshop.model.DepositLog;
import com.jfinalshop.model.Member;
import com.jfinalshop.model.Order;
import com.jfinalshop.model.Payment;
import com.jfinalshop.model.PointLog;
import com.jfinalshop.model.SCard;
import com.jfinalshop.model.SCardorder;
import com.jfinalshop.model.SRecommend;
import com.jfinalshop.plugin.PaymentPlugin;
import com.jfinalshop.service.DepositLogService;
import com.jfinalshop.service.MemberService;
import com.jfinalshop.service.PluginService;
import com.jfinalshop.service.PointLogService;
import com.jfinalshop.service.ses.CardService;
import com.jfinalshop.service.ses.CardmonthService;
import com.jfinalshop.service.ses.CardorderService;
import com.jfinalshop.service.ses.RecommendService;
import com.jfinalshop.util.SystemUtils;

/**
 * Controller - 用户中心 - 预存款
 * 
 * 
 */
@ControllerBind(controllerKey = "/wap/member/deposit")
@Before(com.jfinalshop.interceptor.WapMemberInterceptor.class)
public class DepositController extends BaseController {

	/** 每页记录数 */
	private static final int PAGE_SIZE = 10;

	private MemberService memberService = enhance(MemberService.class);
	private DepositLogService depositLogService = enhance(DepositLogService.class);
	private PluginService pluginService = enhance(PluginService.class);
	
	private CardService cardService = enhance(CardService.class);
	private CardmonthService monthcardService = enhance(CardmonthService.class);
	
	private CardorderService ordercardService = enhance(CardorderService.class);
	
	private RecommendService recommendService= Enhancer.enhance(RecommendService.class);
	private PointLogService pointLogService = enhance(PointLogService.class);
	
	/**
	 * 充值
	 */
	public void recharge() {
		List<PaymentPlugin> paymentPlugins = pluginService.getPaymentPlugins(true);
		for(int i=0;i<paymentPlugins.size();i++){
			if(paymentPlugins.get(i).getId().contentEquals("trafficPaymentPlugin")){
				  	paymentPlugins.remove(i);
			}
		}
		if (!paymentPlugins.isEmpty()) {
			setAttr("defaultPaymentPlugin", paymentPlugins.get(0));
			setAttr("paymentPlugins", paymentPlugins);
		}
		List<SCard> cs=cardService.findCard();
		System.out.println(cs.size());
		setAttr("scardAll",cs);
		setAttr("monthcardAll",monthcardService.findAll());
		
		setAttr("title" , "办理用户 - 用户中心");
		render("/wap/member/deposit/recharge.jsp");
	}
	
	
	/**
	 * 推荐
	 */
	public void tuijian() {
		Member member = memberService.getCurrent();
		setAttr("member", member);
		
		if(SystemUtils.getSetting().getTuijian()!=null){ //
			setAttr("tuijian", SystemUtils.getSetting().getTuijian());
		}else{
			setAttr("tuijian", "");
		}
		
		setAttr("title", "推荐好友 - 用户中心");
		render("/wap/member/deposit/tuijian.ftl");
	}
	
	
	/**
	 * 关于ses
	 */
	public void introduction() {
		Member member = memberService.getCurrent();
		setAttr("member", member);
		
		if(SystemUtils.getSetting().getIntroduction()!=null){ //
			setAttr("introduction", SystemUtils.getSetting().getIntroduction());
		}else{
			setAttr("introduction", "");
		}
		setAttr("title", "关于ses- 用户中心");
		render("/wap/member/deposit/introduction.ftl");
	}
	
	
	/**
	 * ESE农产品平台注册协议
	 */
	public void registerAgreement() {
		Member member = memberService.getCurrent();
		setAttr("member", member);
		
		if(SystemUtils.getSetting().getRegisterAgreement()!=null){ //
			setAttr("registerAgreement", SystemUtils.getSetting().getRegisterAgreement());
		}else{
			setAttr("registerAgreement", "");
		}
		setAttr("title", "平台注册协议- 用户中心");
		render("/wap/member/deposit/registerAgreement.ftl");
	}
	
	
	
	/**
	 * 记录
	 */
	public void log() {
		Integer pageNumber = getParaToInt("pageNumber");
		Member member = memberService.getCurrent();
		BigDecimal d=Db.queryBigDecimal("select sum(amount) from s_cardorder where member_id=?  and  status in(1,6,3)",member.getId());
		this.setAttr("mmoney", d==null?0:d);
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		setAttr("pages", depositLogService.findPage(member, pageable));
		setAttr("title" , "用户中心 - 我的钱包");
		render("/wap/member/deposit/log.ftl");
	}
	
	
	/**
	 * 积分变更记录
	 */
	public void logpoint() {
		Integer pageNumber = getParaToInt("pageNumber");
		Member member = memberService.getCurrent();
		Pageable pageable = new Pageable(pageNumber, 100);
		setAttr("pages", pointLogService.findPage(member, pageable));
		
		setAttr("title" , "用户中心 - 我的返还记录");
		render("/wap/member/deposit/logpoint.ftl");
	}
	
	
	/**
	 * 积分兑换：提现/到余额
	 */
	public void pointdh() {
		Setting setting = SystemUtils.getSetting(); //获得推荐返回 比例
		setAttr("cash" , setting.getCash());
		setAttr("title" , "用户中心 - 积分兑换");
		render("/wap/member/deposit/pointdh.ftl");
	}
	
	
	/**
	 * 推荐记录
	 */
	public void tuijianrecord() {
		Integer current = getParaToInt("current");
		
		if(current==null){
			current=1;
		}
		setAttr("current", current);
	
		Member member = memberService.getCurrent();
		if(current==2){		//二级推荐人
		List<SRecommend> srecommendList = new ArrayList<SRecommend>();
		for(SRecommend a:recommendService.findListSRecommend(member.getId())){
			srecommendList.addAll(recommendService.findListSRecommend(a.getToMemberId()));
		}
		setAttr("pages", srecommendList);
		}else{
			setAttr("pages", recommendService.findListSRecommend(member.getId()));
		}
		
		setAttr("title" , "用户中心 - 推荐记录");
		render("/wap/member/deposit/tuijianrecord.ftl");
	}
	
	
	
	
	
	
	
	/**
	 *兑换：提现/到余额
	 */
	public void cashing() {
		String status =getPara("status");
		int count=this.getParaToInt("count");
		Member member = memberService.getCurrent();
		Map<String, String> map = new HashMap<String, String>();
		Setting setting = SystemUtils.getSetting(); //获得推荐返回 比例
		
		System.out.println(status);
		if(member==null){
			map.put(STATUS, ERROR);
			map.put(MESSAGE, "请重新登录!");
			renderJson(map);
			return;
		}
		System.out.println("这是setting:"+setting);
		System.out.println("这是什么:"+setting.getCash());
		if(setting.getCash()==null||(Math.round(member.getPoint())<count*50)){
			//setAttr("message" , "积分不足!");
			//redirect("/wap/member/deposit/pointdh.jhtml");
			
			map.put(STATUS, SUCCESS);
			map.put(MESSAGE, "积分不足!");
			renderJson(map);
			
			return;	
		}
		
		if(status!=null&&!status.equals("")&&setting.getCash()!=null){
			BigDecimal temp_zs = new BigDecimal(50*count); //500对100元
			if(status.equals("1")){
				if (setting.getCash() > 0) { //减少积分
					memberService.addPointCash(member, -count*50, PointLog.Type.cash, temp_zs, null);
				}
				
				//setAttr("message" , "提现申请提交成功!");
				//redirect("/wap/member/deposit/pointdh.jhtml");
				
				map.put(STATUS, SUCCESS);
				map.put(MESSAGE, "提现申请提交成功!");
				renderJson(map);
				return;	
			}
			if(status.equals("2")){
				if(member.getPoint()<100d) {
					map.put(STATUS, ERROR);
					map.put(MESSAGE, "积分不足");
					this.renderJson(map);
					return;
				}
				temp_zs=new BigDecimal(100);
				if (setting.getCash() > 0) { //减少积分
					memberService.addPointCash(member, -setting.getCash(), PointLog.Type.amount, temp_zs, null);
					memberService.addBalance(member, temp_zs, DepositLog.Type.pionts, null, null);
				}
				//setAttr("message" , "兑换消费券成功!");
				//redirect("/wap/member/deposit/pointdh.jhtml");
				map.put(STATUS, SUCCESS);
				map.put(MESSAGE, "兑换消费券成功!");
				renderJson(map);
				return;
			}
		}else{
			//setAttr("message" ,"您的操作有误!");
			//redirect("/wap/member/deposit/pointdh.jhtml");
			map.put(STATUS, ERROR);
			map.put(MESSAGE, "您的操作有误!");
			return;
		}
	}
	
	/**
	 * 我的卡
	 */
	public void mycard() {
		//Integer pageNumber = getParaToInt("pageNumber");
		Member member = memberService.getCurrent();
		//Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		//Page<SCardorder> sco=ordercardService.findPageList(member, pageable);
		List<SCardorder> cardorders=SCardorder.dao.find("select * from s_cardorder where status<>'0'  and  status<>'5'  and member_id="+member.getId());
		Date today=new Date();
		for(SCardorder co:cardorders){
			if(co.getStatus().equals("3")) {
				 continue;
			}
			if(co.getEndDate().before(today)&&!(co.getStatus().contentEquals("4"))){
				co.setStatus("3");
				co.update();
			}
		}
		setAttr("pages", cardorders);
		setAttr("title" , "用户中心 - 我的卡包");
		render("/wap/member/deposit/mycard.ftl");
	}
	
	
	/**
	 *退卡和续订
	 */
	public void tkAndxd() {

		Long orderId = getParaToLong("param");
		String status =getPara("status");
		Member member = memberService.getCurrent();
		Map<String, String> map = new HashMap<String, String>();
		
		if(member==null){
			map.put(STATUS, ERROR);
			map.put(MESSAGE, "请重新登录!");
			renderJson(map);
			return;
		}

		//status=4 退卡操作，status=6 续期操作
		if(orderId!=null&&status!=null&&member!=null){
			SCardorder temp_order=ordercardService.find(orderId);
			 SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd");
			 
			 if(status.contentEquals("4")&&temp_order.getStatus().contentEquals("6")){
				 map.put(STATUS, "2");
				 map.put(MESSAGE, "您已续期，别忘了查看粮票余额哦！");
				 this.renderJson(map);
				 return;
			}else if(temp_order.getStatus().contentEquals("4")&&status.contentEquals("6")){
				map.put(STATUS, "2");
				map.put(MESSAGE, "正在退款中，如有需要可联系客服！");
				this.renderJson(map);
				return;
			}
			 
			if(status.equals("4")&&temp_order.getStatus().equals("3")){//status=4 退卡操作
				//status=4 退卡操作,查看是否"支付且到期"
				
				temp_order.setStatus("4");
				temp_order.setReturnDate(new Date()); //申请时间
				
				if(temp_order.getMemo()!=null){
					temp_order.setMemo(temp_order.getMemo()+"code:4"+dateFormater.format(new Date())+"退卡操作;");
				}else{
					temp_order.setMemo("code:4:"+dateFormater.format(new Date())+"退卡操作;");
				}
				
				ordercardService.update(temp_order);
				map.put(STATUS, SUCCESS);
				map.put(MESSAGE, "退卡申请提交成功!");
				renderJson(map);
				return;
			}else if(status.equals("6")&&temp_order.getStatus().equals("3")){//status=6 续期操作
				//查看是否"支付且到期//status=6 续期操作:修改购买产品日期，用户余额充值
			    //修改备注
				temp_order.setStatus("6");
				if(temp_order.getMemo()!=null){
					temp_order.setMemo(temp_order.getMemo()+"code:6"+dateFormater.format(new Date())+"-续期操作;");
				}else{
					temp_order.setMemo("code:6:"+dateFormater.format(new Date())+"续期操作;");
				}
				
				/***到期日期 ****/
				 Date today = new Date();
				 Calendar cd = Calendar.getInstance();   
                 cd.setTime(today);   
                 cd.add(Calendar.MONTH, temp_order.getNumber());//增加一个月   
                 temp_order.setEndDate(cd.getTime()); //到期日期
				 /***到期日期 ****/
				temp_order.setCreateDate(new Date());; //修改购买日期
				SCardorder new_order=ordercardService.update(temp_order);
			    
				if(new_order!=null){
				/***用户余额充值，返回消费卷值 **/
				BigDecimal temp_zs = new BigDecimal(temp_order.getKsl());//张数转换
				BigDecimal card_amount =temp_order.getAmountVolume().multiply(temp_zs);
			    memberService.addBalance(member,card_amount,DepositLog.Type.recharge, null, null);
			    /***用户余额充值，返回消费卷值 **/
				}
			    
				map.put(STATUS, SUCCESS);
				map.put(MESSAGE, "续期操作成功,请查看帐户余额变化!");
				renderJson(map);
				return;
			}else{
				
				map.put(STATUS, ERROR);
				if(temp_order.getStatus().equals("4")){
					map.put(MESSAGE, "您申请已经提交成功,请耐心等待!");
				}else if(temp_order.getStatus().equals("6")){
					map.put(MESSAGE, "续期已成功,请查看帐户余额变化!");
				}else{
					map.put(MESSAGE, "该卡未到期!");
				}

				renderJson(map);
				return;
			}
		}else{
			map.put(STATUS, ERROR);
			map.put(MESSAGE, "您的操作有误!");
			renderJson(map);
			return;
		}

	}
	
	
}
