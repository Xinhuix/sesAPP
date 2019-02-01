package com.jfinalshop.controller.client;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.util.TextUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.bocom.bocompay.BocompaySetting;
import com.jfinal.core.Controller;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinalshop.model.Atcrecord;
import com.jfinalshop.model.Bocom;
import com.jfinalshop.model.Member;
import com.jfinalshop.model.Pay;
import com.jfinalshop.model.RechargeCombo;
import com.jfinalshop.model.SCard;
import com.jfinalshop.model.SCardmonth;
import com.jfinalshop.model.SCardorder;
import com.jfinalshop.model.client.CommonSTUC;
import com.jfinalshop.model.client.ReceiveCoupon_STUC;
import com.jfinalshop.plugin.PaymentPlugin;
import com.jfinalshop.service.PluginService;
import com.jfinalshop.service.ses.CardService;
import com.jfinalshop.service.ses.CardmonthService;
import com.jfinalshop.service.ses.CardorderService;

@ControllerBind(controllerKey="/client/coupon")
public class CouponController extends Controller {

	private CardService cardService = enhance(CardService.class);
	private CardmonthService monthcardService = enhance(CardmonthService.class);
	private PluginService pluginService = enhance(PluginService.class);
	private static  Map<String,Object> ms=new HashMap<>();
	private  CardorderService cardOrderService=enhance(CardorderService.class);
	     public void coupon() {
	    	 ReceiveCoupon_STUC stuc=new ReceiveCoupon_STUC();
	    	 Map<String,Object> result=new HashMap<String,Object>();
	    	 try {
	    		
	 	    	List<SCard> cs=SCard.dao.find("select  id,name,type,islimit,limitnum from s_card where status=1 ");
	 	 		List<SCardmonth> scm=SCardmonth.dao.find("select id,card_id,number,amount,amount_volume,timetype  from s_cardmonth where status=1");
	 	 		result.put("payment", getPays());
	 	 		result.put("cards", cs);
	 	 		result.put("ms",scm );
	 	 		//stuc.setCards(cs);
	 	 		//stuc.setMs(scm);
	 	 		//stuc.setPp(paymentPlugins);
	 	 		result.put("status", CommonSTUC.Status.Success.getIndex());
	    	 }catch(Exception e) {
	    		 e.printStackTrace();
	    		 result.put("status", CommonSTUC.Status.Error.getIndex());
	    	 }
	 		this.renderJson(result);
	     }
	     
	     public   void recharge() {
	    	 Map<String,Object> result=new HashMap<>();
	    	 try {
	    		 File f=new File(this.getRequest().getServletContext().getRealPath("/")+"WEB-INF/classes/allocateSetting.xml");
	    		 Document doc=null;
	    		 if(!f.exists()) {
	    			 f.createNewFile();
	    			 doc=DocumentHelper.createDocument();
	    			 Element root=doc.addElement("root");
	    		 }else {
	    			 SAXReader reader=new SAXReader();
	    			doc=reader.read(f);
	    		 }
	    		 Element rate=doc.getRootElement().element("rate");
	    		 String scale="1.5";
	    		 FileOutputStream fos=new FileOutputStream(f);
    			 XMLWriter writer=new XMLWriter(fos);
	    		 if(rate==null) {
	    			 rate=doc.getRootElement().addElement("rate");
	    			 rate.setText(scale);
	    			// FileOutputStream fos=new FileOutputStream(f);
	    			// XMLWriter writer=new XMLWriter(fos);
	    		 }else {
	    			  scale=rate.getText();
	    		 }
	    		 Element tip=doc.getRootElement().element("tip");
	    		 String  stip="粮票仅用于ses共享农业商城运营的产品与服务，不能进行转账和现金兑换，不能兑换SES共享商城外的其他产品及服务";
	    		 if(tip==null) {
	    			 tip=doc.getRootElement().addElement("tip");
	    			 tip.setText(stip);
	    		 }else {
	    			 stip=tip.getText().trim();
	    		 }
	    		 writer.write(doc);
    			 writer.flush();
    			 writer.close();
    			 result.put("tips", stip);
	    		 List<RechargeCombo> combos=RechargeCombo.dao.find("select id,combo,price,coupon from recharge_combo");
	    		 result.put("rate", scale);
	    		 result.put("combo", combos);
	    		 result.put("payment", getPays());
	    		 result.put("status", CommonSTUC.Status.Success.getIndex());
	    	 }catch(Exception e) {
	    		 e.printStackTrace();
	    		 result.put("status", CommonSTUC.Status.Error.getIndex());
	    	 }finally {
	    		 this.renderJson(result);
	    	 }
	     }
	     
	     public List getPays() {
	    	 List<PaymentPlugin> paymentPlugins = pluginService.getPaymentPlugins(true);
	    	 List<Pay> ps=new ArrayList<>();
    		 for(PaymentPlugin pp:paymentPlugins) {
    			   Pay p=new Pay();
    			   p.setId(pp.getId());
    			   p.setName(pp.getName());
    			   p.setImg(pp.getLogo());
    			   ps.add(p);
    		 }
    		 ps.add(Bocom.getPay(this.getRequest().getServletContext().getRealPath("/")));
    		 return ps;
	     }
	     
	     /**
	      *  每日收益初始化
	      */
	     public void signInit() {
	    	 Map<String,Object>  result=new HashMap<>();
	    	 try {
	    		     String loginCode=this.getPara("loginCode");
	    		     String date=this.getPara("date");
	    		     if(TextUtils.isEmpty(loginCode)||TextUtils.isEmpty(date)) {
	    		    	 result.put("status", CommonSTUC.Status.LackPara.getIndex());
	    		    	 return;
	    		     }
	    		     List<Member> ms=Member.dao.find("select  id from member where logincode=?",loginCode);
	    		     if(ms==null||ms.size()<=0) {
	    		    	 result.put("status",CommonSTUC.Status.NoExist.getIndex());
	    		    	 return;
	    		     }
	    		    Member m=ms.get(0);
	    		     Date d=new SimpleDateFormat("yyyy-MM-dd").parse(date);
	    		     Calendar calendar=Calendar.getInstance();
	    		     calendar.setTime(d);
	    		     String nd=calendar.get(calendar.YEAR)+"-"+(calendar.get(calendar.MONTH)+1)+"-";
	    		     String fd=nd+"01";
	    		     String ld=nd+"31";
	    		     List<Atcrecord> records=Atcrecord.dao.find("select  receivedate,receivecount as sum from atcrecord  where  date(receivedate) between ? and ?  and member_id=?  and atype=? ",fd,ld,m.getId(),Atcrecord.Type.Dividend.getIndex());
	    		     List<Atcrecord> receivedRecord=Atcrecord.dao.find("select id from atcrecord where date(receivedate)=date(?) and member_id=?  and atype=?",new Date(),m.getId(),Atcrecord.Type.Dividend.getIndex());
	    		     if(receivedRecord.size()>0) {
	    		    	 result.put("received",Atcrecord.Status.Received.ordinal());
	    		     }else {
	    		    	 result.put("received", Atcrecord.Status.UnReceive.ordinal());
	    		     }
	    		     List<Record> rs=Db.find("select sum(receivecount) from atcrecord where member_id=?  and atype=?",ms.get(0).getId(),Atcrecord.Type.Dividend.getIndex());
	    		     System.out.println(rs.get(0).get("sum(receivecount")==null);
	    		     double count=rs.get(0).get("sum(receivecount")!=null?rs.get(0).getBigDecimal("sum(receivecount)").doubleValue():0;
	    		     //List<Atcrecord> rs=Atcrecord.dao.find("select sum(receivecount) as re from atcrecord where member_id=?  and atype=?",ms.get(0).getId(),Atcrecord.Type.Dividend.getIndex());
	    		     //double count=rs.get(0).getBigDecimal("re").doubleValue();
	    		     

	    		     result.put("receiveCount", count);
	    		     result.put("receiveRecord", records);
	    		     result.put("todaySum",cardOrderService.signCount(this.getRequest().getServletContext().getRealPath(""), loginCode) );
	    		     result.put("status",CommonSTUC.Status.Success.getIndex());
	    	 }catch(Exception e) {
	    		 e.printStackTrace();
	    		 result.put("status", CommonSTUC.Status.Error.getIndex());
	    	 }finally {
	    		 this.renderJson(result);
	    	 }
	     }
	     /**
	      * 每日收益 签到
	      */
	     public void sign() {
	    	 Map<String,Object> result=new HashMap<>();
	    	 try {
	    		 String loginCode=this.getPara("loginCode");
	    	if(TextUtils.isEmpty(loginCode)) {
	    		result.put("status", CommonSTUC.Status.LackPara.getIndex());
	    	}else {
	    		result.putAll(cardOrderService.sign(this.getRequest().getServletContext().getRealPath("/"), loginCode));
	    	}
	    	 }catch(Exception e) {
	    		 e.printStackTrace();
	    		 result.put("status", CommonSTUC.Status.Error.getIndex());
	    	 }finally {
	    		 this.renderJson(result);
	    	 }
	     }
}
