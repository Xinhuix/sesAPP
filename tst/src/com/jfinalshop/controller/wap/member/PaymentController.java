package com.jfinalshop.controller.wap.member;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import com.bocom.bocompay.BocomClient;
import com.bocom.bocompay.BocomDataMap;
import com.bocom.bocompay.BocompaySetting;
import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.i18n.I18n;
import com.jfinal.i18n.Res;
import com.jfinal.kit.HttpKit;
import com.jfinal.kit.LogKit;
import com.jfinal.kit.StrKit;
import com.jfinal.weixin.sdk.api.PaymentApi;
import com.jfinal.weixin.sdk.api.SnsAccessToken;
import com.jfinal.weixin.sdk.api.SnsAccessTokenApi;
import com.jfinal.weixin.sdk.kit.IpKit;
import com.jfinal.weixin.sdk.kit.PaymentKit;
import com.jfinal.weixin.sdk.utils.JsonUtils;
import com.jfinalshop.Setting;
import com.jfinalshop.controller.admin.plugin.TrafficpayController;
import com.jfinalshop.controller.wap.BaseController;
import com.jfinalshop.entity.AjaxResult;
import com.jfinalshop.interceptor.WapInterceptor;
import com.jfinalshop.model.Member;
import com.jfinalshop.model.Order;
import com.jfinalshop.model.PaymentLog;
import com.jfinalshop.model.PaymentMethod;
import com.jfinalshop.model.PluginConfig;
import com.jfinalshop.model.SCardmonth;
import com.jfinalshop.model.SCardorder;
import com.jfinalshop.plugin.PaymentPlugin;
import com.jfinalshop.service.MemberService;
import com.jfinalshop.service.OrderService;
import com.jfinalshop.service.PaymentLogService;
import com.jfinalshop.service.PluginService;
import com.jfinalshop.service.ses.CardmonthService;
import com.jfinalshop.service.ses.CardorderService;
import com.jfinalshop.util.SystemUtils;

import java.util.Date;
/**
 * Controller - 支付
 * 
 * 
 */
@ControllerBind(controllerKey = "/ssswap/payment")
@Before(WapInterceptor.class)
public class PaymentController extends BaseController {

	private OrderService orderService = enhance(OrderService.class);
	private MemberService memberService = enhance(MemberService.class);
	private PluginService pluginService = enhance(PluginService.class);
	private PaymentLogService paymentLogService = enhance(PaymentLogService.class);
	
	private CardorderService ordercardService = enhance(CardorderService.class);
	private CardmonthService monthcardService = enhance(CardmonthService.class);
	
	private Res resZh = I18n.use();
	private AjaxResult ajax = new AjaxResult();

	public static final String RETURN_CODE = "return_code";
	public static final String RETURN_MSG = "return_msg";
	public static final String RESULT_CODE = "result_code";

	/**
	 * 第一步获取的code参数
	 * 
	 */
	public void getWeixinCode() {
		String sn = getPara("sn");
		PaymentPlugin paymentPlugin = pluginService
				.getPaymentPlugin("weixinPaymentPlugin");
		PluginConfig pluginConfig = paymentPlugin.getPluginConfig();

		Setting setting = SystemUtils.getSetting();
		String notify_url = setting.getSiteUrl()
				+ "/wap/payment/weixinCodeNotify.jhtml";

		System.out.println("notify_url---11----->" + notify_url);
		String url = SnsAccessTokenApi.getAuthorizeURL(
				pluginConfig.getAttribute("appid"), notify_url, sn, true);

		try {
			getResponse().sendRedirect(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		renderNull();
	}

	/**
	 * 支付订单 - 支付页面 同时也接收微信CODE
	 * 
	 */
	public void weixinCodeNotify() {
		String code = getPara("code"); // 微信CODE
		System.out.println("code--->" + code);

		String sn = getPara("state"); // 订单号
		PaymentPlugin paymentPlugin = pluginService
				.getPaymentPlugin("weixinPaymentPlugin");
		PluginConfig pluginConfig = paymentPlugin.getPluginConfig();

		// 通过code获取access_token
		SnsAccessToken snsAccessToken = SnsAccessTokenApi.getSnsAccessToken(
				pluginConfig.getAttribute("appid"),
				pluginConfig.getAttribute("appSecret"), code);

		String openId = snsAccessToken.getOpenid();

		System.out.println("openId--->" + openId);

		Member member = memberService.getCurrent();
		if (member != null && StrKit.notBlank(openId)) {
			member.setOpenId(openId);
			member.update();
		}
		redirect("/wap/order/payment.jhtml?sn=" + sn);
	}
	
	public static void main(String... args){
		
	}
	
	public void trafficPay(Member member,PaymentLog paylog,SCardorder cardorder){
		
			String mercertid="301140880629503";
			String merptcid="301310063009501";
			String datetime=DateFormatUtils.format(new Date(), "yyyyMMdd HHmmss");
			String date=datetime.split(" ")[0];
			String time=datetime.split(" ")[1];
			String trancode="BPAYPY4101";
			
			//开始构建client
			String path=this.getRequest().getServletContext().getRealPath("/");
			path+="WEB-INF/classes/bocommjava/ini/BocompayMerchant.xml";
			BocomClient client=new BocomClient();
			 int ret=client.initialize(path);
			 if(ret!=0){
				 System.out.println("初始化失败，错误"+client.getLastErr());
				 return;
			 }
			client.setHead("TranCode",trancode );
			client.setHead("TranDate",date );
			client.setHead("TranTime", time);
			client.setHead("MerPtcId", merptcid);
			
			BocomDataMap businfo=new BocomDataMap();
			businfo.setData("MerOrderNo",paylog.getSn());
			client.setData("BusiInfo",businfo.toString());
			
			BocomDataMap userinfo=new BocomDataMap();
			userinfo.setData("BuyerId",member.getId());
			client.setData("UserInfo", userinfo.toString());
			
			BocomDataMap goodinfo=new BocomDataMap();
			goodinfo.setData("GoodsName", cardorder.getCardName());
			 client.setData("GoodsInfo", goodinfo.toString());
			 
			 BocomDataMap traninfo=new BocomDataMap();
			 traninfo.setData("TranModeId", "D");
			 System.out.println(cardorder.getAmount());
			 traninfo.setData("TranAmt", new DecimalFormat("#.00").format(cardorder.getAmount()));
			 traninfo.setData("TranCry","CNY");
			 client.setData("TranInfo", traninfo.toString());
			 
			 BocomDataMap channelinfo=new BocomDataMap();
			 channelinfo.setData("ChannelApi", "3010001010");//0000003010  银联支付
			 channelinfo.setData("ChannelInst", "netpay");
			 client.setData("ChannelInfo", channelinfo.toString());
			 
			 client.setData("MemoInfo", "");
			 String sn=client.toSignString(mercertid);
			 this.setAttr("signData", sn);
			 String url=BocompaySetting.BpayOrderPayURL;
			 this.setAttr("urlaction", url);
			 this.setAttr("sn", paylog.getSn());
			 this.setAttr("amount", new DecimalFormat("#.00").format(cardorder.getAmount()));
			 this.setAttr("uname", member.getName());
			 this.setAttr("goodsname",cardorder.getCardName());
			 client=null;
			 BocomClient client2=new BocomClient();
			  client2.initialize(path);
			  client2.setHead("TranCode", "BPAYPY4020");
			  client2.setHead("TranTime",time);
			  client2.setHead("TranDate", date);
			  client2.setHead("MerPtcId",merptcid);
			  client2.setData("ReturnURL", "http://2v217017k7.iask.in/receive.jhtml");
			  client2.setData("NotifyURL", "http://2v217017k7.iask.in//receive.jhtml");
			  System.out.println(client2.toString());
			  //String rst=null;
			  String rst=client2.execute(mercertid, trancode);
			  if(rst==null){
				  System.out.println("设置失败\n"+client2.getLastErr());
			  }else{
				  System.out.println(client2.changeNull(client2.getHead("RspType")));
				 // System.out.println(client.getHead("RspMsg"));
			  }
	}
  
	
	
	/**
	 * 插件提交
	 */
	public void submit() {
		String typeName = getPara("type", "");
		PaymentLog.Type type = StrKit.notBlank(typeName) ? PaymentLog.Type
				.valueOf(typeName) : null;
		String paymentPluginId = getPara("paymentPluginId");
		
		String sn = getPara("sn");
		
		String amountStr = getPara("amount");
		BigDecimal amount = StrKit.notBlank(amountStr) ? new BigDecimal(
				amountStr) : null;

		if (type == null) {
			return;
		}
		Member member = memberService.getCurrent();
		if (member == null) {
			//member=memberService.findByUsername("18511832809");
			return;
		}
		//System.out.println(paymentPluginId);
		PaymentPlugin paymentPlugin = pluginService
				.getPaymentPlugin(paymentPluginId);
		if (paymentPlugin == null ||(!paymentPlugin.getIsEnabled())) {
			return;
		}
		
		PluginConfig pluginConfig = paymentPlugin.getPluginConfig();
		
		Setting setting = SystemUtils.getSetting();
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		switch (type) {
		case recharge: 
			System.out.println(type);
			//购买月卡记录
			Integer zs = getParaToInt("orderzs");//获得购卡张数
			Long monthcardId = getParaToLong("monthcardId");//选择的月卡时长id
			
			SCardorder cardorder = getModel(SCardorder.class);
			cardorder.setMonthcardId(monthcardId);

			//if (cardorder.getMonthcardId() == null || cardorder.getMonthcardId() <= 0) {
				//return;
			//}
			if (zs != null &&zs >0&&cardorder.getMonthcardId() != null) {//购卡
				
			if (cardorder.getMonthcardId() == null || cardorder.getMonthcardId() <= 0) {
				return;
			}
			if(cardorder.getMonthcardId()!=null){
				//选择的月卡
				try {
					SCardmonth temp=monthcardService.find(cardorder.getMonthcardId());
					cardorder.setMemberId(member.getId());//会员ID
					cardorder.setCardId(temp.getCardId());//卡ID
					cardorder.setCardName(temp.getCard().getName());//卡名称,月卡ID
					cardorder.setNumber(temp.getNumber());//时长(卡面月数值)
					cardorder.setAmount(temp.getAmount());//金额
					cardorder.setAmountVolume(temp.getAmountVolume());//返回消费卷值
					cardorder.setStatus("0");//状态 0:未支付
					cardorder.setKsl(zs);//购卡张数
					
					/***到期日期 ****/
					int tt=this.getParaToInt("tt");
					 Date today = new Date();
					 Calendar cd = Calendar.getInstance();   
	                 cd.setTime(today);   
	                 cd.add(tt==0?Calendar.DAY_OF_MONTH:Calendar.MONTH, temp.getNumber());//增加N个月   
					 cardorder.setEndDate(cd.getTime()); //到期日期
					 /***到期日期 ****/
					SCardorder temp_cardorder =ordercardService.save(cardorder);
					
					BigDecimal temp_zs = new BigDecimal(zs);//张数转换
					BigDecimal card_amount =temp.getAmount().multiply(temp_zs);
					
					PaymentLog paymentLog = new PaymentLog();
					paymentLog.setSn(null);
					paymentLog.setType(Integer.valueOf(type.ordinal()));
					paymentLog.setStatus(PaymentLog.Status.wait.ordinal());
					paymentLog.setFee(BigDecimal.valueOf(0));
					paymentLog.setAmount(card_amount);
					paymentLog.setPaymentPluginId(paymentPluginId);
					paymentLog.setPaymentPluginName(paymentPlugin.getName());
					paymentLog.setMemberId(member.getId());
					
					paymentLog.setCardorderId(temp_cardorder.getId());//购卡记录ID
					
					paymentLog.setOrder(null);
					PaymentLog tmep_paymentLog=paymentLogService.save(paymentLog);
					parameterMap = paymentPlugin.getParameterMap(paymentLog.getSn(),
							resZh.format("shop.payment.rechargeDescription"),
							getRequest());
					
					if(paymentPluginId.contentEquals("trafficPaymentPlugin")){
						trafficPay(member,tmep_paymentLog,temp_cardorder);
						renderJsp("/wappay/trafficPay.jsp");
						return;
					}

if(paymentPluginId.equals("alipayDirectPaymentPlugin")){
//支付宝H5支付
setAttr("WIDout_trade_no",tmep_paymentLog.getSn());// 商户订单号
setAttr("WIDsubject" , temp.getCard().getName()+"卡"+temp.getNumber()+"个月"+zs+"张");//订单名称，必填
setAttr("WIDtotal_amount",tmep_paymentLog.getAmount());// 付款金额，必填
renderJsp("/wappay/pay.jsp");
return;	
}
//微信H5支付
if(paymentPluginId.equals("weixinPaymentPlugin")){
	
	String body=temp.getCard().getName()+"卡"+temp.getNumber()+"个月"+zs+"张";
	//System.out.println(body);
	String out_trade_no=tmep_paymentLog.getSn();
	int  price = (int) ((tmep_paymentLog.getAmount().doubleValue()) * 100);

	appPay(body,out_trade_no,price);
	return;	
}
	
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			}else{ //余额充值
			if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0
					|| amount.precision() > 15
					|| amount.scale() > setting.getPriceScale()) {
				return;
			}
			PaymentLog paymentLog = new PaymentLog();
			paymentLog.setSn(null);
			paymentLog.setType(type.ordinal());
			paymentLog.setStatus(PaymentLog.Status.wait.ordinal());
			paymentLog.setFee(paymentPlugin.calculateFee(amount));
			paymentLog.setAmount(paymentPlugin.calculateAmount(amount));
			paymentLog.setPaymentPluginId(paymentPluginId);
			paymentLog.setPaymentPluginName(paymentPlugin.getName());
			paymentLog.setMemberId(member.getId());
			paymentLog.setOrder(null);
			paymentLogService.save(paymentLog);
			parameterMap = paymentPlugin.getParameterMap(paymentLog.getSn(),
					resZh.format("shop.payment.rechargeDescription"),
					getRequest());
			
			
			break;
		}
		case payment: 
			Order order = orderService.findBySn(sn);
			if (order == null || !member.equals(order.getMember())
					|| orderService.isLocked(order, member, true)) {
				return;
			}
			if (order.getPaymentMethod() == null
					|| !PaymentMethod.Method.online.equals(order
							.getPaymentMethod().getMethodName())) {
				return;
			}
			if (order.getAmountPayable().compareTo(BigDecimal.ZERO) <= 0) {
				return;
			}

			PaymentLog paymentLog = new PaymentLog();
			paymentLog.setSn(null);
			paymentLog.setType(type.ordinal());
			paymentLog.setStatus(PaymentLog.Status.wait.ordinal());
			paymentLog.setFee(paymentPlugin.calculateFee(order.getAmount()));
			// paymentLog.setAmount(paymentPlugin.calculateAmount(order.getAmount()));
			paymentLog.setAmount(paymentPlugin.calculateAmount(order
					.getAmountPayable())); // 改成取【应付金额】
			paymentLog.setPaymentPluginId(paymentPluginId);
			paymentLog.setPaymentPluginName(paymentPlugin.getName());
			paymentLog.setOrderId(order.getId());
			paymentLog.setMember(null);
			paymentLogService.save(paymentLog);
			parameterMap = paymentPlugin.getParameterMap(
					paymentLog.getSn(),
					resZh.format("shop.payment.paymentDescription",
							order.getSn()), getRequest());
			break;
		
		}
		//********************************************
		
		//微信公号支付
		Map<String, String> params = convertAttributes(parameterMap);
		params.put("openid", member.getOpenId());
		params.put(
				"sign",
				PaymentKit.createSign(params,
						pluginConfig.getAttribute("paternerKey"))); // 签名

		// 统一下单
		String xmlResult = PaymentApi.pushOrder(params);
		Map<String, String> result = PaymentKit.xmlToMap(xmlResult);
		String returnCode = result.get(RETURN_CODE);
		String returnMsg = result.get(RETURN_MSG);
		
		if (StrKit.isBlank(returnCode)
				|| !StringUtils.equals("SUCCESS", returnCode)) {
			ajax.addError(returnMsg);
			renderJson(ajax);
			return;
		}
		String resultCode = result.get(RESULT_CODE);
		if (StrKit.isBlank(resultCode)
				|| !StringUtils.equals("SUCCESS", returnCode)) {
			ajax.addError(returnMsg);
			renderJson(ajax);
			return;
		}
		
		
		// 以下字段在return_code 和result_code都为SUCCESS的时候有返回
		String prepay_id = result.get("prepay_id");

		Map<String, String> packageParams = new HashMap<String, String>();
		packageParams.put("appId", pluginConfig.getAttribute("appid"));
		packageParams.put("timeStamp", System.currentTimeMillis() / 1000 + "");
		packageParams.put("nonceStr", System.currentTimeMillis() + "");
		packageParams.put("package", "prepay_id=" + prepay_id);
		packageParams.put("signType", "MD5");
		String packageSign = PaymentKit.createSign(packageParams,
				pluginConfig.getAttribute("paternerKey"));
		packageParams.put("paySign", packageSign);

		String jsonStr = JsonUtils.toJson(packageParams);
		ajax.success(jsonStr);
		LogKit.info("返回ajax: " + ajax);
		
		renderJson(ajax);
	}

	
	/**
     * 微信APP支付
	 * @throws UnsupportedEncodingException 
     */ public void appPay(String body,String out_trade_no,int price) throws UnsupportedEncodingException{
    	
    	 //不用设置授权目录域名 
    	 //统一下单地址 https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=9_1# 
    	 String paymentPluginId = "weixinPaymentPlugin";
         PaymentPlugin paymentPlugin = this.pluginService.getPaymentPlugin(paymentPluginId);
         if(paymentPlugin != null && paymentPlugin.getIsEnabled()) {
             PluginConfig pluginConfig = paymentPlugin.getPluginConfig();
             String appid = pluginConfig.getAttribute("appid");
             String mch_id = pluginConfig.getAttribute("mch_id");
             String paternerKey = pluginConfig.getAttribute("paternerKey");
             Map<String, String> params = new HashMap();
             params.put("appid", appid);
             params.put("mch_id", mch_id);
             params.put("nonce_str", String.valueOf(System.currentTimeMillis() / 1000L));
             params.put("body", body);
             params.put("attach", paymentPluginId);
             params.put("out_trade_no", out_trade_no);
             params.put("total_fee", String.valueOf(price));
             String ip = IpKit.getRealIp(this.getRequest());
             if(StrKit.isBlank(ip)) {
                 ip = "127.0.0.1";
             }
             ip="60.253.229.33";
             params.put("spbill_create_ip", ip);
             String notify_url = "http://ses.esgod.com/wap/payment/paymentNotify.jhtml";
             params.put("notify_url", notify_url);
             params.put("trade_type", "MWEB");
             String sign = PaymentKit.createSign(params, paternerKey);
             params.put("sign", sign);
             String xmlResult = PaymentApi.pushOrder(params);
             Map<String, String> result = PaymentKit.xmlToMap(xmlResult);
             String return_code = (String)result.get("return_code");
             String return_msg = (String)result.get("return_msg");
             if(!StrKit.isBlank(return_code) && "SUCCESS".equals(return_code)) {
                 String result_code = (String)result.get("result_code");
                 if(!StrKit.isBlank(result_code) && "SUCCESS".equals(result_code)) {
                     String mweb_url = (String)result.get("mweb_url");
                     String redirecturl = URLEncoder.encode("http://ses.esgod.com/wap/member/deposit/mycard.jhtml", "utf-8");
                     mweb_url = mweb_url + "&redirect_url=" + redirecturl;
                     this.setAttr("title", "微信支付");
                     this.setAttr("mweb_url", mweb_url);
                     this.setAttr("out_trade_no", out_trade_no);
                     this.render("/wap/member/deposit/weixinpay.ftl");
                 } else {
                     this.ajax.addError(return_msg);
                     this.renderJson(this.ajax);
                 }
             } else {
                 this.ajax.addError(return_msg);
                 this.renderJson(this.ajax);
             }
         }
    	 }
 /***
  * H5微信 回调方法
  */
     public void notifyUrl() {
    	 
    	 /*********Start**订单查询*************************/
    	 String out_trade_no = getPara("out_trade_no");
    	 
    	 PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin("weixinPaymentPlugin");
 		if(paymentPlugin == null || !paymentPlugin.getIsEnabled()) {
 			renderJavascript("alert('支付失败');");
 			return;
 		}
    	PluginConfig pluginConfig = paymentPlugin.getPluginConfig();
 		String appid=pluginConfig.getAttribute("appid"),
 				mch_id=pluginConfig.getAttribute("mch_id"),
 				paternerKey=pluginConfig.getAttribute("paternerKey");
    	 
     	  Map<String, String> params_query = new HashMap<String, String>(); 
          params_query=PaymentApi.queryByOutTradeNo(appid, mch_id, paternerKey, out_trade_no);
          
     	  System.out.println("支付查询结果--->"+params_query);
     	 
     	  String return_code = params_query.get("return_code");
     	  String return_msg = params_query.get("return_msg");
     	 if (StrKit.isBlank(return_code) || !"SUCCESS".equals(return_code)) { 
     		 ajax.addError(return_msg);
     		 renderJson(ajax);
     		 return; 
     		 } 
     	 String result_code = params_query.get("result_code");
     	 if (StrKit.isBlank(result_code) || !"SUCCESS".equals(result_code)) {
     		 ajax.addError(return_msg);
     		 renderJson(ajax);
     		 return; 
     	} 
     	 /*********End**订单查询*************************/
     	String trade_state = params_query.get("trade_state");
    	String out_trade_no_return = params_query.get("out_trade_no"); //查询返回订单号
    	String trade_state_desc = params_query.get("trade_state_desc"); //查询返回订单结果描述
    	
    	if(trade_state!=null&&"SUCCESS".equals(trade_state)
    			&&out_trade_no_return!=null&&out_trade_no_return.equals(out_trade_no)) {
    		
				PaymentLog paymentLog = paymentLogService.findBySn(out_trade_no_return);
				if (paymentLog != null) {
					// 执行更新订单
					if(paymentLog.getStatus().equals("0")){
					paymentLogService.handle(paymentLog);
					}
					setAttr("msg","<script type='text/javascript'>alert('支付成功');window.location.href='/wap/member/deposit/mycard.jhtml';</script>");
					return; 
				}else{
					setAttr("msg","<script type='text/javascript'>alert('"+trade_state_desc+"');window.history.back();</script>");
					return; 
				}
    	 }else{
    		    setAttr("msg","<script type='text/javascript'>alert('"+trade_state_desc+"');window.history.back();</script>");
				return; 
    	 }
     }
     
     
     
	/**
	 * 插件通知 支付结果通用通知文档:
	 * https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_7
	 */
	public void paymentNotify() {
		String xmlMsg = HttpKit.readData(getRequest());
		LogKit.info("支付通知=" + xmlMsg);

		Map<String, String> params = PaymentKit.xmlToMap(xmlMsg);
		String total_fee = params.get("total_fee"); // 总金额
		String transaction_id = params.get("transaction_id"); // 微信支付订单号
		String out_trade_no = params.get("out_trade_no"); // 商户订单号

		// 以下是附加参数
		String attach = params.get("attach");

		// 注意重复通知的情况，同一订单号可能收到多次通知，请注意一定先判断订单状态 避免已经成功、关闭、退款的订单被再次更新
		PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(attach);
		PluginConfig pluginConfig = paymentPlugin.getPluginConfig();
		if (paymentPlugin != null
				&& PaymentKit.verifyNotify(params,
						pluginConfig.getAttribute("paternerKey"))) {
			PaymentLog paymentLog = paymentLogService.findBySn(out_trade_no);
			if (paymentLog != null) {
				LogKit.info("微信支付订单号:" + transaction_id + ",总金额:" + total_fee
						+ "分");
				// 执行更新订单
				paymentLogService.handle(paymentLog);

				// 发送通知等
				Map<String, String> xml = new HashMap<String, String>();
				xml.put(RETURN_CODE, "SUCCESS");
				xml.put(RETURN_MSG, "OK");
				renderText(PaymentKit.toXml(xml));
				return;
			}
		}
		renderText("");
	}

	/**
	 * 支付成功
	 * 
	 */
	public void success() {
		String sn = getPara("sn");
		Boolean success = getParaToBoolean("success", false);
		Order order = orderService.findBySn(sn);
		setAttr("order", order);
		setAttr("title", success ? "订单支付成功" : "订单支付失败");
		render("/wap/order/success.ftl");
	}

	/**
	 * 充值支付结果
	 * 
	 */
	public void result() {
		Boolean success = getParaToBoolean("success", false);
		setAttr("title", success ? "充值成功" : "充值失败");
		render("/wap/order/result.ftl");
	}

	/**
	 * Object convert String
	 * 
	 * @param parameterMap
	 *            参数
	 * @return 签名
	 */
	private Map<String, String> convertAttributes(
			Map<String, Object> parameterMap) {
		Map<String, String> parameterNewMap = new HashMap<String, String>();
		for (Map.Entry<String, Object> entry : parameterMap.entrySet()) {
			if (entry.getValue() instanceof String) {
				parameterNewMap.put(entry.getKey(), (String) entry.getValue());
			}
		}
		return parameterNewMap;
	}

}
