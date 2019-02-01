package com.jfinalshop.controller.wap.member;

import java.io.File;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import com.jfinalshop.FileType;
import com.jfinalshop.Message;
import com.jfinalshop.Pageable;
import com.jfinalshop.controller.wap.BaseController;
import com.jfinalshop.interceptor.WapMemberInterceptor;
import com.jfinalshop.model.Member;
import com.jfinalshop.model.Order;
import com.jfinalshop.service.ConsultationService;
import com.jfinalshop.service.CouponCodeService;
import com.jfinalshop.service.FileService;
import com.jfinalshop.service.GoodsService;
import com.jfinalshop.service.MemberService;
import com.jfinalshop.service.MessageService;
import com.jfinalshop.service.OrderService;
import com.jfinalshop.service.ProductNotifyService;
import com.jfinalshop.service.ReviewService;
import com.jfinalshop.service.ses.CardorderService;
import com.jfinalshop.util.SystemUtils;

@ControllerBind(controllerKey = "/wap/member")
@Before(WapMemberInterceptor.class)
public class MemberController extends BaseController {

	/** 最新订单数 */
	private static final int NEW_ORDER_COUNT = 6;

	private MemberService memberService = enhance(MemberService.class);
	private OrderService orderService = enhance(OrderService.class);
	private CouponCodeService couponCodeService = enhance(CouponCodeService.class);
	private MessageService messageService = enhance(MessageService.class);
	private GoodsService goodsService = enhance(GoodsService.class);
	private ProductNotifyService productNotifyService = enhance(ProductNotifyService.class);
	private ReviewService reviewService = enhance(ReviewService.class);
	private ConsultationService consultationService = enhance(ConsultationService.class);
	private FileService fileService = enhance(FileService.class);
	
	private CardorderService ordercardService = enhance(CardorderService.class);
	
	/**
	 * 首页
	 */
	public void index() {
		
		
		System.out.println("进入首页");
		double rvcount=0;
		Member member = memberService.getCurrent();
		SAXReader read=new SAXReader();
		File f=new File(this.getRequest().getServletContext().getRealPath("/")+"WEB-INF/classes/allocateSetting.xml");
		
		System.out.println(f);
		try {
			Document doc=read.read(f);
			System.out.println(doc.getRootElement().element("enabled"));
			if(doc.getRootElement().element("enabled")==null||doc.getRootElement().element("enabled").getText().equals("1")) {
			System.out.println(doc);
			System.out.println("1239");
		//doc.getRootElement().element("enabled").getText().equals("1")
			System.out.println("321");
			if(rvcount==0) {
				System.out.println("123");
			if(rvcount==0) {

				this.setAttr("rvcount", 0);
			}else {
				String today=new SimpleDateFormat("yyyyMMdd").format(new Date());
				List<Record> lr=Db.find("select  *  from atcrecord where member_id="+member.getId()+"  and receivedate=\""+today+"\"  and  atype=1");
				if(lr.size()>0) {
					this.setAttr("receive",1);
					rvcount=Double.parseDouble(lr.get(0).getStr("receivecount"));
				}else {
					this.setAttr("receive",0);
					List<Element> es=doc.getRootElement().elements("level");
					double l1=Double.parseDouble(es.get(0).elementText("count"));
					double l2=Double.parseDouble(es.get(1).elementText("count"));
					double l3=Double.parseDouble(es.get(2).elementText("count"));
					double l4=Double.parseDouble(es.get(3).elementText("count"));
					double l5=Double.parseDouble(es.get(4).elementText("count"));
					List<Record> cards=Db.find("select card_id from s_cardorder where (status=1 or status=6) and member_id="+member.getId() );
					if(cards.size()>0) {
						for(Record r:cards) {
							switch(Integer.parseInt(r.getLong("card_id")+"")) {
							case 7:
								rvcount+=l2;
								break;
							case 8:
								rvcount+=l3;
								break;
							case 9:
								rvcount+=l4;
								break;
							case 10:
								rvcount+=l5;
								break;
							case 13:
								rvcount+=l1;
								break;
							}
						}
						//rvcount+=rvcount;
					}
				
				}
				this.setAttr("rvcount",rvcount);
			
			   }
			  }
			 }
			}catch (DocumentException e) {
			// TODO Auto-generated catch block
			this.setAttr("rvcount", "0");
			e.printStackTrace();
		}			
		setAttr("pendingPaymentOrderCount", orderService.count(null, Order.Status.pendingPayment, member, null, null, null, null, null, null, false));
		setAttr("pendingShipmentOrderCount", orderService.count(null, Order.Status.pendingShipment, member, null, null, null, null, null, null, null));
		setAttr("messageCount", messageService.count(member, false));
		setAttr("couponCodeCount", couponCodeService.count(null, member, null, null, false));
		setAttr("favoriteCount", goodsService.count(null, member, null, null, null, null, null));
		setAttr("productNotifyCount", productNotifyService.count(member, null, null, null));
		setAttr("reviewCount", reviewService.count(member, null, null, null));
		setAttr("consultationCount", consultationService.count(member, null, null));
		setAttr("newOrders", orderService.findList(null, null, member, null, null, null, null, null, null, null, NEW_ORDER_COUNT, null, null));
		// wap
		setAttr("memberPendingPaymentOrderCount", orderService.count(null, Order.Status.pendingPayment, member, null, null, null, null, null, null, null));
		setAttr("memberPendingShipmentOrderCount", orderService.count(null, Order.Status.pendingShipment, member, null, null, null, null, null, null, null));
		setAttr("membershippedOrderCount", orderService.count(null, Order.Status.shipped, member, null, null, null, null, null, null, null));
		setAttr("memberReceivedOrderCount", orderService.count(null, Order.Status.received, member, null, null, null, null, null, null, null));
		setAttr("pendingReviewCount", reviewService.count(member, false));
		//统计是否有过期购卡订单
		setAttr("returnCount",ordercardService.count(member, "3"));
		
		if(SystemUtils.getSetting().getHotline()!=null){ //在线客服
			setAttr("hotline", SystemUtils.getSetting().getHotline());
		}else{
			setAttr("hotline", "");
		}
		
		
		setAttr("member", member);
		setAttr("title" , "用户中心");
		
	}
	
	/**
	 * 每日分红
	 */
	public void dailyAllocate() {
		
		Map<String,String> m=new HashMap<>();
		try {
		Date d=new Date();
		Member mb=memberService.getCurrent();
		//System.out.println(mb.getUsername());
		String today=new SimpleDateFormat("yyyyMMdd").format(d);
		List<Record> lr=Db.find("select id from atcrecord where receivedate=\""+today+"\" and member_id="+mb.getId());
		if(lr.size()>0) {
			m.put("state","0");
			m.put("count","0");
		}else {
			File f=new File(this.getRequest().getServletContext().getRealPath("/")+"WEB-INF/classes/allocateSetting.xml");
			SAXReader reader=new  SAXReader();
			Document doc;
			try {
				doc = reader.read(f);
				if(!doc.getRootElement().element("enabled").getText().equals("1")) {
					List<Record> ac=Db.find("select card_id from s_cardorder where (status=1 or status=6) and member_id="+mb.getId());
					List<Element> es=doc.getRootElement().elements("level");
					double l1count=Double.parseDouble(es.get(0).elementText("count"));
					double l2count=Double.parseDouble(es.get(1).elementText("count"));
					double l3count=Double.parseDouble(es.get(2).elementText("count"));
					double l4count=Double.parseDouble(es.get(3).elementText("count"));
					double l5count=Double.parseDouble(es.get(4).elementText("count"));
					double count=0;
					for(Record r:ac) {
						switch(Integer.parseInt(r.getLong("card_id")+"")) {
						case 7:
							count+=l2count;
							break;
						case 8:
							count+=l3count;
							break;
						case 9:
							count+=l4count;
							break;
						case 10:
							count+=l5count;
							break;
						case 13:
							count+=l1count;
							break;
						}
					}
					BigDecimal bd=new BigDecimal(count);
					count=bd.setScale(2,bd.ROUND_HALF_UP).doubleValue();
					Record r=new Record().set("receivedate", today).set("receivecount",count ).set("member_id", mb.getId());
					Db.save("atcrecord", r);
					mb.setPoint(mb.getPoint()+count);
					mb.update();
					m.put("state","1");
					m.put("count", count+"");
				}else {
					m.put("state", "3");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				m.put("state", "2");
				m.put("count", "0");
			}
			
		}
		}catch(Exception e) {
			m.put("state", "0");
			m.put("count", "0");
		}
		renderJson(m);
	}
	
	/**
	 * 上传
	 */
	public void upload() {
		UploadFile file = getFile();
		FileType fileType = FileType.valueOf(getPara("fileType", "image"));
		
		Member member = memberService.getCurrent();
		Map<String, Object> data = new HashMap<String, Object>();
		if (member == null) {
			data.put(MESSAGE, "当前用户不能为空!");
			data.put(STATUS, ERROR);
			renderJson(data);
			return;
		}
		if (fileType == null || file == null || file.getFile().length() <= 0) {
			data.put(MESSAGE, "请选择选图片");
			data.put(STATUS, ERROR);
			renderJson(data);
			return;
		}
		if (!fileService.isValid(fileType, file)) {
			data.put(MESSAGE, Message.warn("admin.upload.invalid"));
			data.put(STATUS, ERROR);
			renderJson(data);
			return;
		}
		String url = fileService.upload(fileType, file, false);
		if (StringUtils.isEmpty(url)) {
			data.put(MESSAGE, Message.warn("admin.upload.error"));
			data.put(STATUS, ERROR);
			renderJson(data);
			return;
		}
		member.setAvatar(url);
		memberService.update(member);
		data.put(MESSAGE, "上传成功!");
		data.put(STATUS, SUCCESS);
		renderJson(data);
	}
}
