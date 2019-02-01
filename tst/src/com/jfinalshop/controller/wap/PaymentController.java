

package com.jfinalshop.controller.wap;

import com.bocom.bocompay.BocompaySetting;
import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.i18n.I18n;
import com.jfinal.i18n.Res;
import com.jfinal.kit.HttpKit;
import com.jfinal.kit.LogKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.weixin.sdk.api.PaymentApi;
import com.jfinal.weixin.sdk.api.SnsAccessToken;
import com.jfinal.weixin.sdk.api.SnsAccessTokenApi;
import com.jfinal.weixin.sdk.kit.IpKit;
import com.jfinal.weixin.sdk.kit.PaymentKit;
import com.jfinal.weixin.sdk.utils.JsonUtils;
import com.jfinalshop.Setting;
import com.jfinalshop.entity.AjaxResult;
import com.jfinalshop.interceptor.WapInterceptor;
import com.jfinalshop.model.Atcrecord;
import com.jfinalshop.model.Bocom;
import com.jfinalshop.model.DepositLog;
import com.jfinalshop.model.Member;
import com.jfinalshop.model.Order;
import com.jfinalshop.model.PaymentLog;
import com.jfinalshop.model.PluginConfig;
import com.jfinalshop.model.RechargeCombo;
import com.jfinalshop.model.SCard;
import com.jfinalshop.model.SCardmonth;
import com.jfinalshop.model.SCardorder;
import com.jfinalshop.model.client.CommonSTUC;
import com.jfinalshop.model.PaymentLog.Status;
import com.jfinalshop.model.PaymentLog.Type;
import com.jfinalshop.model.PaymentMethod.Method;
import com.jfinalshop.plugin.PaymentPlugin;
import com.jfinalshop.service.MemberService;
import com.jfinalshop.service.OrderService;
import com.jfinalshop.service.PaymentLogService;
import com.jfinalshop.service.PluginService;
import com.jfinalshop.service.ses.CardmonthService;
import com.jfinalshop.service.ses.CardorderService;
import com.jfinalshop.util.SystemUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.TextUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

@ControllerBind(
    controllerKey = "/wap/payment"
)
@Before({WapInterceptor.class})
public class PaymentController extends BaseController {
    private OrderService orderService = (OrderService)this.enhance(OrderService.class);
    private MemberService memberService = (MemberService)this.enhance(MemberService.class);
    private PluginService pluginService = (PluginService)this.enhance(PluginService.class);
    private PaymentLogService paymentLogService = (PaymentLogService)this.enhance(PaymentLogService.class);
    private CardorderService ordercardService = (CardorderService)this.enhance(CardorderService.class);
    private CardmonthService monthcardService = (CardmonthService)this.enhance(CardmonthService.class);
    private Res resZh = I18n.use();
    private AjaxResult ajax = new AjaxResult();
    public static final String RETURN_CODE = "return_code";
    public static final String RETURN_MSG = "return_msg";
    public static final String RESULT_CODE = "result_code";
     
    public static Map<String,SCardorder> orders=new HashMap<String,SCardorder>();
    
    public PaymentController() {
    }

    public void getWeixinCode() {
        String sn = this.getPara("sn");
        PaymentPlugin paymentPlugin = this.pluginService.getPaymentPlugin("weixinPaymentPlugin");
        PluginConfig pluginConfig = paymentPlugin.getPluginConfig();
        Setting setting = SystemUtils.getSetting();
        String notify_url = setting.getSiteUrl() + "/wap/payment/weixinCodeNotify.jhtml";
        String url = SnsAccessTokenApi.getAuthorizeURL(pluginConfig.getAttribute("appid"), notify_url, sn, true);

        try {
            this.getResponse().sendRedirect(url);
        } catch (IOException var8) {
            var8.printStackTrace();
        }

        this.renderNull();
    }

    public void weixinCodeNotify() {
        String code = this.getPara("code");
        System.out.println("code--->" + code);
        String sn = this.getPara("state");
        PaymentPlugin paymentPlugin = this.pluginService.getPaymentPlugin("weixinPaymentPlugin");
        PluginConfig pluginConfig = paymentPlugin.getPluginConfig();
        SnsAccessToken snsAccessToken = SnsAccessTokenApi.getSnsAccessToken(pluginConfig.getAttribute("appid"), pluginConfig.getAttribute("appSecret"), code);
        String openId = snsAccessToken.getOpenid();
        System.out.println("openId--->" + openId);
        Member member = this.memberService.getCurrent();
        if(member != null && StrKit.notBlank(openId)) {
            member.setOpenId(openId);
            member.update();
        }

        this.redirect("/wap/order/payment.jhtml?sn=" + sn);
    }
    public boolean  checkNull(String...params) {
    	  for(String str:params) {
    		  if(TextUtils.isEmpty(str))return true;
    	  else
    		  continue;
    }
    	  return false;
    }
    public void buyCard() {
    	Map<String,Object> result=new HashMap<String,Object>();
    	try {
    		 // String typeName=this.getPara("type");
     	   String paymentPluginId=this.getPara("paymentPluginId");//支付插件id
     	   String amountStr=this.getPara("money");//购卡金额
     	   String cardId=this.getPara("cardId");//卡id
     	   String zs=this.getPara("cardAmount");//卡数量
     	  // String carType=this.getPara("limit");//限购
     	   String monthId=this.getPara("monthCardId");
     	   String timeType=this.getPara("timeType");//时间计算类型
     	   String loginCode=this.getPara("loginCode");//用户登录码
     	    if(checkNull(paymentPluginId,amountStr,cardId,zs,monthId,timeType,loginCode)) {
     	    	    result.put("status", CommonSTUC.Status.LackPara.getIndex());
     	    }else {
     	        BigDecimal amount=new BigDecimal(amountStr);
     	        int  cardSum=Integer.parseInt(zs);
     	    	  PaymentPlugin payment=this.pluginService.getPaymentPlugin(paymentPluginId);
     	    	 List<SCard> cards=SCard.dao.find("select  id,name,islimit,limitnum,status   from  s_card where status=1  and id=?  ",cardId);
     	    	  List<Member> members=Member.dao.find("select id,username from member where logincode=?",loginCode);
     	    	  List<SCardmonth> cardMonths=SCardmonth.dao.find("select  id,card_id,number,amount,amount_volume,timetype  "
     	    	  		+ "   from s_cardmonth where  status=1  and id=?  and timetype=?  and      card_id=?  ",monthId,timeType,cardId );
     	    	  System.out.println(cards.size()+"\n"+payment+"\n"+cardMonths.size()+"\n"+members.size());
     	    	  if(cards.size()<=0||(payment==null&&!paymentPluginId.equals("bankUnion"))||cardMonths.size()<=0||members.size()<=0) {
     	    		  	  result.put("status",CommonSTUC.Status.ParaWrong.getIndex());
     	    	  }else {
     	    		  Member member=members.get(0);
     	    		    SCard card=cards.get(0);
     	    		    int limitNum=card.getLimitnum();
     	    		    List<Record> cardorder=Db.find("select count(id) from s_cardorder where (status='1' or status='3') and  card_id=? ",card.getId());
     	    		    int  buyCount=cardorder.get(0).getLong("count(id)").intValue();
     	    		    boolean islimit=card.getIslimit()==1;
     	    		    if((islimit&&cardSum>=limitNum)||(islimit&&buyCount>=limitNum)) {
     	    		    	result.put("status", CommonSTUC.Status.limitOut.getIndex());
     	    		    }else {
     	    		    	 SCardmonth cardMonth=cardMonths.get(0);
     	    		    	 BigDecimal namount=cardMonth.getAmount().multiply(BigDecimal.valueOf(cardSum));
     	    		    	 if(namount.doubleValue()!=amount.doubleValue()) {
     	    		    		  result.put("status", CommonSTUC.Status.AmountWrong.getIndex());
     	    		    	 }else {
     	    		    		 SCardorder cardOrder=new SCardorder();
     	    		    		 cardOrder.setMonthcardId(cardMonth.getId());
     	    		    		 cardOrder.setAmount(cardMonth.getAmount().multiply(BigDecimal.valueOf(cardSum)));
     	    		    		 cardOrder.setAmountVolume(cardMonth.getAmountVolume().multiply(BigDecimal.valueOf(cardSum)));
     	    		    		 cardOrder.setCardId(cardMonth.getCardId());
     	    		    		 cardOrder.setCardName(cardMonth.getCard().getName());
     	    		    		 cardOrder.setNumber(cardMonth.getNumber());
     	    		    		 cardOrder.setStatus(Status.wait.ordinal()+"");
     	    		    		 cardOrder.setKsl(cardSum);
     	    		    		 cardOrder.setMemberId(member.getId());
     	    		    		 Calendar calendar=Calendar.getInstance();
     	    		    		 calendar.setTime(new Date());
     	    		    		 calendar.add(timeType.equals("0")?Calendar.DAY_OF_MONTH:Calendar.MONTH, cardOrder.getNumber());
     	    		    		 cardOrder.setEndDate(calendar.getTime());
     	    		    		cardOrder=ordercardService.save(cardOrder);
     	    		    		 if(cardOrder.getId()==null) {
     	    		    			 result.put("status", CommonSTUC.Status.MKFail.getIndex());
     	    		    		 }else {
     	    		    			PaymentLog paymentLog=new PaymentLog();
     	    		    			paymentLog.setAmount(cardMonth.getAmount().multiply(BigDecimal.valueOf(cardSum)));
     	    		    			paymentLog.setType(Type.recharge.ordinal());
     	    		    			paymentLog.setFee(BigDecimal.valueOf(0));
     	    		    			 if(paymentPluginId.equals("bankUnion")) {
     	    		    				paymentLog.setPaymentPluginId(paymentPluginId);
     	    		    				paymentLog.setPaymentPluginName("银联支付");
       					    	  }else {
       					    		paymentLog.setPaymentPluginId(payment.getId());
       					    		paymentLog.setPaymentPluginName(payment.getName());
       					    	  }
     	    		    			paymentLog.setSn(null);
     	    		    			paymentLog.setStatus(Status.wait.ordinal());
     	    		    			paymentLog.setMemberId(member.getId());
     	    		    			paymentLog.setCardorderId(cardOrder.getId());
     	    		    			paymentLog=paymentLogService.save(paymentLog);
     	    		    			result.put("status",TextUtils.isEmpty(paymentLog.getSn())?CommonSTUC.Status.Fail.getIndex():CommonSTUC.Status.Success.getIndex());
     	    		    			result.put("sn", paymentLog.getSn());
     	    		    		 }
     	    		    	 }
     	    		    }
     	    	  }
     	    }
    	}catch(Exception e) {
    		  e.printStackTrace();
    		  result.put("status",CommonSTUC.Status.Error.getIndex() );
    	}finally {
    		 this.renderJson(result);
    	}
    }
    /**
     * 交通银行银联支付
     * @param pl
     */
    public void  traffictPay(PaymentLog pl) {
    	SCardorder cardOrder=SCardorder.dao.findById(pl.getCardorderId());
    	String base=this.getRequest().getServletContext().getRealPath("/");
        String result=Bocom.mkOrder(pl, base, cardOrder);
        String action=BocompaySetting.BpayOrderPayURL;
       // Bocom.setNotify(base);
        System.out.println("the addr is:"+action);
        this.setAttr("urlaction", action);
        this.setAttr("signData", result);
        renderJsp("/wappay/trafficPay.jsp");
        //Bocom.setNotify(this.getRequest().getServletContext().getRealPath("/"));
    }
    
    //充值校验
    public void rechargeValidate() {
    		   Map<String,Object>  result=new HashMap<>();
    		   try {
    			   String price =this.getPara("price");
    			   String coupon=this.getPara("coupon");
    			   String id=this.getPara("id");
    			   String loginCode=this.getPara("loginCode");
    			   String paymentId=this.getPara("paymentId");
    			   SAXReader reader =null;
    			   Document doc=null;
    			 
    			   File f=new File(this.getRequest().getServletContext().getRealPath("/")+"WEB-INF/classes/allocateSetting.xml");
    			   if(!f.exists()) {
    				   result.put("status",CommonSTUC.Status.Fail.getIndex());
    			   }else {
    				   if(TextUtils.isEmpty(price)||TextUtils.isEmpty(coupon)||TextUtils.isEmpty(id)||TextUtils.isEmpty(paymentId)||TextUtils.isEmpty(loginCode)) {
    					   result.put("status", CommonSTUC.Status.LackPara.getIndex());
    				   }else {
    					 
    					   List<Member> ms=Member.dao.find("select  id,username,balance from member where logincode=?",loginCode);
    	    			   if(ms==null||ms.size()<=0) {
    	    				   result.put("status", CommonSTUC.Status.NoExist.getIndex());
    	    				   return; 
    	    			   }
    	    			   Member m=ms.get(0);
    					   PaymentPlugin plugin=this.pluginService.getPaymentPlugin(paymentId);
    					   if(plugin==null&&!paymentId.equals("bankUnion")) {
    						   result.put("status", CommonSTUC.Status.ParaWrong.getIndex());
    						   return;
    					   }
    					   
    					     double cps=0d;
    					     double pri=0;
    					      if(id.equals("cust")) {
    					    	  reader=new SAXReader();
    					    	  doc=reader.read(f);
    					    	 Element e=doc.getRootElement();
    					    	 String text=e.elementText("rate");
    					    	  pri=Double.parseDouble(price);
    					    	double rate=Double.parseDouble(text);
    					    	 if((cps=pri*rate)!=(Double.parseDouble(coupon))) {
    					    		 result.put("status", CommonSTUC.Status.ParaWrong.getIndex());
    					    		 return;
    					    	 }
    					      }else {
    					    	  RechargeCombo combo=RechargeCombo.dao.findById(id);
    					    	  System.out.println(combo.getCombo());
    					    	  cps=Double.parseDouble(coupon);
    					    	  pri=Double.parseDouble(price);
    					    	  System.out.println(cps+"\n"+pri+"\n"+combo.getPrice()+"   "+combo.getCoupon());
    					    	  if(combo.getPrice().doubleValue()!=pri||combo.getCoupon().doubleValue()!=cps) {
    					    		  result.put("status",CommonSTUC.Status.ParaWrong.getIndex());
    					    		  return;
    					    	  }
    					      }
    					      PaymentLog log=new PaymentLog();
					    	  log.setCreateDate(new Date());
					    	  log.setType(Type.recharge.ordinal());
					    	  log.setAmount(BigDecimal.valueOf(cps));
					    	  log.setStatus(PaymentLog.Status.wait.ordinal());
					    	  log.setAmount(BigDecimal.valueOf(pri));
					    	  log.set("coupon", cps);
					    	  log.setMemberId(m.getId());
					    	  log.setModifyDate(new Date());
					    	  log.setVersion(1l);
					    	  log.setFee(BigDecimal.valueOf(0));
					    	  if(paymentId.equals("bankUnion")) {
					    		  log.setPaymentPluginId(paymentId);
					    		  log.setPaymentPluginName("银联支付");
					    	  }else {
					    		  log.setPaymentPluginId(plugin.getId());
    					    	  log.setPaymentPluginName(plugin.getName());
					    	  }
					    	  log.setSn(null);
					    	  log=this.paymentLogService.save(log);
					    	  result.put("status",TextUtils.isEmpty(log.getSn())?CommonSTUC.Status.Fail.getIndex():CommonSTUC.Status.Success.getIndex());
					    	  result.put("sn", log.getSn());
    				   }
    			   }
    		   }catch(Exception e) {
    			   e.printStackTrace();
    			     result.put("status", CommonSTUC.Status.Error.getIndex());
    		   }finally {
    			   this.renderJson(result);
    		   }
    }
    public void pay() throws UnsupportedEncodingException {
    	  String sn=this.getPara("sn");
    	  if(TextUtils.isEmpty(sn)) {
    		  return;
    	  }else {
    		  List<PaymentLog> paymentLogs=PaymentLog.dao.find("select  * from  payment_log where sn=?",sn);
    		  if(paymentLogs.size()>0) {
    			  PaymentLog paymentLog=paymentLogs.get(0);
    			  SCardorder cardOrder=SCardorder.dao.findById(paymentLog.getCardorderId());
    			  if(paymentLog.getPaymentPluginId().equals("alipayDirectPaymentPlugin")){
    	            	//支付宝H5支付
    	            	setAttr("WIDout_trade_no",sn);// 商户订单号
    	            	if(cardOrder==null||TextUtils.isEmpty(cardOrder.getCardName())) {
    	            		setAttr("WIDsubject" ,"fdsafdsafdsafdsf");//订单名称，必填
    	            	}else {
    	            		setAttr("WIDsubject","粮票充值"+paymentLog.getAmount());
    	            	}
    	            	BigDecimal bd=paymentLog.getAmount().setScale(2);
    	            	setAttr("WIDtotal_amount",bd.doubleValue());// 付款金额，必填
    	            	this.renderJsp("/wappay/pay.jsp");
    	            	return;
    	            	}
    	            if(paymentLog.getPaymentPluginId().equals("weixinPaymentPlugin")) {
    	                String body =(cardOrder==null ||TextUtils.isEmpty(cardOrder.getCardName()))?"粮票充值-"+paymentLog.getAmount():cardOrder.getCard().getName() + "卡" + cardOrder.getNumber() + "个月" + cardOrder.getKsl() + "张";
    	                String out_trade_no =sn;
    	                int price =paymentLog.getAmount().intValue()*100;
    	                this.appPay(body, out_trade_no, price);
    	            }
    	            if(paymentLog.getPaymentPluginId().equals("bankUnion")) {
    	            	traffictPay(paymentLog);
    	            }
    		  }
    	  }
    	    
    }
    /**
     * 银联支付
     */
    public void bocomPay(String orderNo) {
	       String MerTranSerialNo=orderNo;//流水号
	     
    }
    
    public void submit() {
        String typeName = this.getPara("type", "");
        Type type = StrKit.notBlank(typeName)?Type.valueOf(typeName):null;
        String paymentPluginId = this.getPara("paymentPluginId");
        String sn = this.getPara("sn");
        String amountStr = this.getPara("amount");
        BigDecimal amount = StrKit.notBlank(amountStr)?new BigDecimal(amountStr):null;
        if(type != null) {
        	//String loginCode=this.getPara("loginCode");
            Member member = this.memberService.getCurrent();
           //member= member==null?memberService.findByUsername("18511832809"):member;
            if(member != null) {
                PaymentPlugin paymentPlugin = this.pluginService.getPaymentPlugin(paymentPluginId);
                if(paymentPlugin != null && paymentPlugin.getIsEnabled()) {
                    PluginConfig pluginConfig = paymentPlugin.getPluginConfig();
                    Setting setting = SystemUtils.getSetting();
                    Map<String, Object> parameterMap = new HashMap();
                    switch(type) {
                    case recharge:
                        Integer zs = this.getParaToInt("orderzs");
                        Long monthcardId = this.getParaToLong("monthcardId");
                        SCardorder cardorder = this.getModel(SCardorder.class);
                        cardorder.setMonthcardId(monthcardId);
                        if(cardorder.getMonthcardId() == null || cardorder.getMonthcardId().longValue() <= 0L) {
                            return;
                        }

                        if(zs != null && zs.intValue() > 0 && cardorder.getMonthcardId() != null) {
                            if(cardorder.getMonthcardId() != null && cardorder.getMonthcardId().longValue() > 0L) {
                                    try {
                                        SCardmonth temp = (SCardmonth)this.monthcardService.find(cardorder.getMonthcardId());
                                        cardorder.setMemberId(member.getId());
                                        cardorder.setCardId(temp.getCardId());
                                        cardorder.setCardName(temp.getCard().getName());
                                        cardorder.setNumber(temp.getNumber());
                                        cardorder.setAmount(temp.getAmount());
                                        cardorder.setAmountVolume(temp.getAmountVolume());
                                        cardorder.setStatus("0");
                                        cardorder.setKsl(zs);
                                        int tt=this.getPara("tt")==null?1:Integer.parseInt(this.getPara("tt"));
                                       Date today = new Date();
                                        Calendar cd = Calendar.getInstance();
                                        cd.setTime(today);
                                        cd.add(tt==0?Calendar.DAY_OF_MONTH:Calendar.MONTH, temp.getNumber().intValue());
                                        cardorder.setEndDate(cd.getTime());
                                        SCardorder temp_cardorder = this.ordercardService.save(cardorder);
                                        BigDecimal temp_zs = new BigDecimal(zs.intValue());
                                        BigDecimal card_amount = temp.getAmount().multiply(temp_zs);
                                        PaymentLog paymentLog = new PaymentLog();
                                        paymentLog.setSn((String)null);
                                        paymentLog.setType(Integer.valueOf(type.ordinal()));
                                        paymentLog.setStatus(Integer.valueOf(Status.wait.ordinal()));
                                        paymentLog.setFee(paymentPlugin.calculateFee(card_amount));
                                        paymentLog.setAmount(paymentPlugin.calculateAmount(card_amount));
                                        paymentLog.setPaymentPluginId(paymentPluginId);
                                        paymentLog.setPaymentPluginName(paymentPlugin.getName());
                                        paymentLog.setMemberId(member.getId());
                                        paymentLog.setCardorderId(temp_cardorder.getId());
                                        paymentLog.setOrder((Order)null);
                                        PaymentLog tmep_paymentLog = this.paymentLogService.save(paymentLog);
                                        parameterMap = paymentPlugin.getParameterMap(paymentLog.getSn(), this.resZh.format("shop.payment.rechargeDescription", new Object[0]), this.getRequest());
                                        if(paymentPluginId.equals("alipayDirectPaymentPlugin")){
                                        	//支付宝H5支付
                                        	setAttr("WIDout_trade_no",tmep_paymentLog.getSn());// 商户订单号
                                        	setAttr("WIDsubject" , temp.getCard().getName()+"卡-"+temp.getNumber()+"个月"+zs+"张");//订单名称，必填
                                        	setAttr("WIDtotal_amount",tmep_paymentLog.getAmount());// 付款金额，必填
                                        	System.out.println(tmep_paymentLog.getSn()+"\n"+temp.getCard().getName()+"卡"+temp.getNumber()+"个月"+zs+"张"+"\n"+tmep_paymentLog.getAmount());
                                        	
                                        	this.renderJsp("/wappay/pay.jsp");
                                        	return;	
                                        	}
                                        if(paymentPluginId.equals("weixinPaymentPlugin")) {
                                            String body = temp.getCard().getName() + "卡" + temp.getNumber() + "个月" + zs + "张";
                                            String out_trade_no = tmep_paymentLog.getSn();
                                            int price = (int)(tmep_paymentLog.getAmount().doubleValue() * 100.0D);
                                            this.appPay(body, out_trade_no, price);
                                            return;
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                break;
                            }

                            return;
                        } else {
                            if(amount != null && amount.compareTo(BigDecimal.ZERO) > 0 && amount.precision() <= 15 && amount.scale() <= setting.getPriceScale().intValue()) {
                                PaymentLog paymentLog = new PaymentLog();
                                paymentLog.setSn((String)null);
                                paymentLog.setType(Integer.valueOf(type.ordinal()));
                                paymentLog.setStatus(Integer.valueOf(Status.wait.ordinal()));
                                paymentLog.setFee(paymentPlugin.calculateFee(amount));
                                paymentLog.setAmount(paymentPlugin.calculateAmount(amount));
                                paymentLog.setPaymentPluginId(paymentPluginId);
                                paymentLog.setPaymentPluginName(paymentPlugin.getName());
                                paymentLog.setMemberId(member.getId());
                                paymentLog.setOrder((Order)null);
                                this.paymentLogService.save(paymentLog);
                                parameterMap = paymentPlugin.getParameterMap(paymentLog.getSn(), this.resZh.format("shop.payment.rechargeDescription", new Object[0]), this.getRequest());
                                break;
                            }

                            return;
                        }
                    case payment:
                         {
                            Order order = this.orderService.findBySn(sn);
                            if(order != null && member.equals(order.getMember()) && !this.orderService.isLocked(order, member, true)) {
                                if(order.getPaymentMethod() != null && Method.online.equals(order.getPaymentMethod().getMethodName())) {
                                    if(order.getAmountPayable().compareTo(BigDecimal.ZERO) <= 0) {
                                        return;
                                    }
                                    PaymentLog paymentLog = new PaymentLog();
                                    paymentLog.setSn((String)null);
                                    paymentLog.setType(Integer.valueOf(type.ordinal()));
                                    paymentLog.setStatus(Integer.valueOf(Status.wait.ordinal()));
                                    paymentLog.setFee(paymentPlugin.calculateFee(order.getAmount()));
                                    paymentLog.setAmount(paymentPlugin.calculateAmount(order.getAmountPayable()));
                                    paymentLog.setPaymentPluginId(paymentPluginId);
                                    paymentLog.setPaymentPluginName(paymentPlugin.getName());
                                    paymentLog.setOrderId(order.getId());
                                    paymentLog.setMember((Member)null);
                                    this.paymentLogService.save(paymentLog);
                                    parameterMap = paymentPlugin.getParameterMap(paymentLog.getSn(), this.resZh.format("shop.payment.paymentDescription", new Object[]{order.getSn()}), this.getRequest());
                                    break;
                                }

                                return;
                            }

                            return;
                        }
                    }

                    Map<String, String> params = this.convertAttributes((Map)parameterMap);
                    params.put("openid", member.getOpenId());
                    params.put("sign", PaymentKit.createSign(params, pluginConfig.getAttribute("paternerKey")));
                    String xmlResult = PaymentApi.pushOrder(params);
                    Map<String, String> result = PaymentKit.xmlToMap(xmlResult);
                    String returnCode = (String)result.get("return_code");
                    String returnMsg = (String)result.get("return_msg");
                    if(!StrKit.isBlank(returnCode) && StringUtils.equals("SUCCESS", returnCode)) {
                        String resultCode = (String)result.get("result_code");
                        if(!StrKit.isBlank(resultCode) && StringUtils.equals("SUCCESS", returnCode)) {
                            String prepay_id = (String)result.get("prepay_id");
                            Map<String, String> packageParams = new HashMap();
                            packageParams.put("appId", pluginConfig.getAttribute("appid"));
                            packageParams.put("timeStamp", String.valueOf(System.currentTimeMillis() / 1000L));
                            packageParams.put("nonceStr", String.valueOf(System.currentTimeMillis()));
                            packageParams.put("package", "prepay_id=" + prepay_id);
                            packageParams.put("signType", "MD5");
                            String packageSign = PaymentKit.createSign(packageParams, pluginConfig.getAttribute("paternerKey"));
                            packageParams.put("paySign", packageSign);
                            String jsonStr = JsonUtils.toJson(packageParams);
                            this.ajax.success(jsonStr);
                            LogKit.info("返回ajax: " + this.ajax);
                            this.renderJson(this.ajax);
                        } else {
                            this.ajax.addError(returnMsg);
                            this.renderJson(this.ajax);
                        }
                    } else {
                        this.ajax.addError(returnMsg);
                        this.renderJson(this.ajax);
                    }
                }
            }
        }
    }

    
    
    public void appPay(String body, String out_trade_no, int price) throws UnsupportedEncodingException {
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
            System.out.println(body);
            params.put("body", body);
            params.put("attach", paymentPluginId);
            params.put("out_trade_no", out_trade_no);
            params.put("total_fee",price+"");
            params.put("scene_info","");
            String ip = IpKit.getRealIp(this.getRequest());
              System.out.println(ip);
            if(StrKit.isBlank(ip)) {
                ip = "127.0.0.1";
            }
            	
            params.put("spbill_create_ip", ip);
            String notify_url = "http://gxsc.esgod.com/wap/payment/paymentNotify.jhtml";
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
                    String redirecturl = URLEncoder.encode("http://gxsc.esgod.com/wap/member/deposit/mycard.jhtml", "utf-8");
                    mweb_url = mweb_url+ "&redirect_url=" + redirecturl;
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

    public void notifyUrl() {
        String out_trade_no = this.getPara("out_trade_no");
        PaymentPlugin paymentPlugin = this.pluginService.getPaymentPlugin("weixinPaymentPlugin");
        if(paymentPlugin != null && paymentPlugin.getIsEnabled()) {
            PluginConfig pluginConfig = paymentPlugin.getPluginConfig();
            String appid = pluginConfig.getAttribute("appid");
            String mch_id = pluginConfig.getAttribute("mch_id");
            String paternerKey = pluginConfig.getAttribute("paternerKey");
            new HashMap();
            Map<String, String> params_query = PaymentApi.queryByOutTradeNo(appid, mch_id, paternerKey, out_trade_no);
           // System.out.println("支付查询结果--->" + params_query);
            String return_code = (String)params_query.get("return_code");
            String return_msg = (String)params_query.get("return_msg");
            if(!StrKit.isBlank(return_code) && "SUCCESS".equals(return_code)) {
                String result_code = (String)params_query.get("result_code");
                if(!StrKit.isBlank(result_code) && "SUCCESS".equals(result_code)) {
                    String trade_state = (String)params_query.get("trade_state");
                    String out_trade_no_return = (String)params_query.get("out_trade_no");
                    String trade_state_desc = (String)params_query.get("trade_state_desc");
                    if(trade_state != null && "SUCCESS".equals(trade_state) && out_trade_no_return != null && out_trade_no_return.equals(out_trade_no)) {
                        PaymentLog paymentLog = this.paymentLogService.findBySn(out_trade_no_return);
                        if(paymentLog != null) {
                            if(paymentLog.getStatus().equals("0")) {
                                //this.paymentLogService.handle(paymentLog);
                            }
                            this.setAttr("msg", "<script type='text/javascript'>alert('支付成功');window.location.href='/wap/member/deposit/mycard.jhtml';</script>");
                        } else {
                            this.setAttr("msg", "<script type='text/javascript'>alert('" + trade_state_desc + "');window.history.back();</script>");
                        }
                    } else {
                        this.setAttr("msg", "<script type='text/javascript'>alert('" + trade_state_desc + "');window.history.back();</script>");
                    }
                } else {
                    this.ajax.addError(return_msg);
                    this.renderJson(this.ajax);
                }
            } else {
                this.ajax.addError(return_msg);
                this.renderJson(this.ajax);
            }
        } else {
            this.renderJavascript("alert('支付失败');");
        }
    }

    public void paymentNotify() {
        String xmlMsg = HttpKit.readData(this.getRequest());
        LogKit.info("支付通知=" + xmlMsg);
        Map<String, String> params = PaymentKit.xmlToMap(xmlMsg);
        String total_fee = (String)params.get("total_fee");
        String transaction_id = (String)params.get("transaction_id");
        String out_trade_no = (String)params.get("out_trade_no");
        String attach = (String)params.get("attach");
        PaymentPlugin paymentPlugin = this.pluginService.getPaymentPlugin(attach);
        PluginConfig pluginConfig = paymentPlugin.getPluginConfig();
        if(paymentPlugin != null && PaymentKit.verifyNotify(params, pluginConfig.getAttribute("paternerKey"))) {
            PaymentLog paymentLog = this.paymentLogService.findBySn(out_trade_no);
            if(paymentLog != null) {
                LogKit.info("微信支付订单号:" + transaction_id + ",总金额:" + total_fee + "分");
                this.paymentLogService.handle(paymentLog);
                Map<String, String> xml = new HashMap();
                xml.put("return_code", "SUCCESS");
                xml.put("return_msg", "OK");
                this.renderText(PaymentKit.toXml(xml));
                return;
            }
        }

        this.renderText("");
    }

    public void success() {
        String sn = this.getPara("sn");
        Boolean success = this.getParaToBoolean("success", Boolean.valueOf(false));
        Order order = this.orderService.findBySn(sn);
        this.setAttr("order", order);
        this.setAttr("title", success.booleanValue()?"订单支付成功":"订单支付失败");
        this.render("/wap/order/success.ftl");
    }

    public void result() {
        Boolean success = this.getParaToBoolean("success", Boolean.valueOf(false));
        this.setAttr("title", success.booleanValue()?"充值成功":"充值失败");
        this.render("/wap/order/result.ftl");
    }

    private Map<String, String> convertAttributes(Map<String, Object> parameterMap) {
        Map<String, String> parameterNewMap = new HashMap();
        Iterator var4 = parameterMap.entrySet().iterator();
        while(var4.hasNext()) {
            Entry<String, Object> entry = (Entry)var4.next();
            if(entry.getValue() instanceof String) {
                parameterNewMap.put((String)entry.getKey(), (String)entry.getValue());
            }
        }
        return parameterNewMap;
    }
    public  void reg_coupon() {
    	 Map<String,String> ms=new HashMap<String,String>();
    	   Member m=memberService.getCurrent();
    	   if(m==null||m.getUsername()==null) {
    		   ms.put("result","用户名不存在");
    	   }else {
    		   try {
				Date d=new SimpleDateFormat("yyyyMMdd").parse("20180713");
				boolean b=m.getCreateDate().getTime()-d.getTime()<0;
				if(b) {
					ms.put("result", "抱歉，只有2018年7月13日之后新注册用户才可领取");
				}	else {
					 List<Record>  lr=Db.find("select   a.* from  atcrecord a,member m where 1=1 and a.member_id="+m.getId()+"    and a.atype=2  ");
		    		   if(lr.size()>0) {
		    			   ms.put("result","这是新用户福利，只能领取一次哦");
		    		   }else {
		    			   BigDecimal bd=m.getBalance();
		    			   m.setBalance(m.getBalance().add(new BigDecimal(50)));
		    			  if( m.update()) {
		    				  Atcrecord ar=this.getModel(Atcrecord.class);
		    				 ar.setReceivecount(BigDecimal.valueOf(50));
		    				 ar.setReceivedate(new Date());
		    				ar.setMemberId(m.getId()+"");
		    				ar.setAtype(Atcrecord.Type.Reg.getIndex());
		    				ar.save();
		    				  ms.put("result","领取成功" );
		    				  DepositLog dl=new DepositLog();
		    				  dl.setCreateDate(new Date());
		    				  dl.setModifyDate(new Date());
		    				  dl.setBalance(bd.add(BigDecimal.valueOf(50)));
		    				  dl.setVersion(1l);
		    				  dl.setDebit(BigDecimal.valueOf(0));
		    				 dl.setCredit(BigDecimal.valueOf(50));
		    				 dl.setMemo("新用户注册福利");
		    				 dl.setType(DepositLog.Type.adjustment.ordinal());
		    				  dl.setMemberId(m.getId());
		    				  dl.save();
		    			  }
		    			  else
		    				  ms.put("result", "领取失败");
		    		   }
				}
			} catch (ParseException e) {
				ms.put("result", "抱歉，发生未知错误，请稍后重试");
			}
    		  
    	   }
    	   this.renderJson(ms);
    }
}
