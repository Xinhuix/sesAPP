	package com.jfinalshop.controller.usercenter;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.swing.plaf.LabelUI;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.TextUtils;

import com.google.gson.Gson;
import com.jfinal.aop.Enhancer;
import com.jfinal.core.Controller;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import com.jfinalshop.controller.usercenter.entity.OrderUtil;
import com.jfinalshop.controller.usercenter.entity.ProductUtil;
import com.jfinalshop.dao.CartDao;
import com.jfinalshop.dao.CartItemDao;
import com.jfinalshop.dao.OrderItemDao;
import com.jfinalshop.dao.OrderLogDao;
import com.jfinalshop.model.Admin;
import com.jfinalshop.model.Cart;
import com.jfinalshop.model.Goods;
import com.jfinalshop.model.Member;
import com.jfinalshop.model.Order;
import com.jfinalshop.model.OrderItem;
import com.jfinalshop.model.OrderLog;
import com.jfinalshop.model.Payment;
import com.jfinalshop.model.Product;
import com.jfinalshop.model.Receiver;
import com.jfinalshop.model.Returns;
import com.jfinalshop.model.ReturnsItem;
import com.jfinalshop.model.Shipping;
import com.jfinalshop.service.MailService;
import com.jfinalshop.service.MemberService;
import com.jfinalshop.service.OrderItemService;
import com.jfinalshop.service.OrderService;
import com.jfinalshop.service.PermissionService;
import com.jfinalshop.service.SmsService;
@ControllerBind(controllerKey="/UserOrder")
public class UserOrder extends Controller{
	private PermissionService permissionService = enhance(PermissionService.class);
	private OrderLogDao orderLogDao = Enhancer.enhance(OrderLogDao.class);
	private MailService mailService = new MailService();
	private SmsService smsService = new SmsService();
	private OrderService orderService = enhance(OrderService.class);
	private MemberService memberService = enhance(MemberService.class);
	
	
	public void  testcart() throws Exception {
		System.out.println("123");
		
		 List<Integer> list= new ArrayList<>();
		 list.add(1);
		 list.add(2);
		 list.add(3);
		 list.add(4);
		 list.add(5);
		 
		int[] b = new int[4];
		for(int i = 0; i<list.size();i++) {
			System.out.println(b[i]);
		}
	
		 /* 
		  
		  
		 HideDataUtil cc = new HideDataUtil();
		 
		 System.out.println(cc.hidePhoneNo("231312215151515"));
		System.out.println(oracle);*/
		 /* List<Order> list = new ArrayList<>();
		  list.add(oracle);
		  setAttr("score", "score");
		  setAttr("cc", "dd");
		  setAttr("alist", list);
		  setAttr("modules", permissionService.getModules());
			setAttr("permissions", permissionService.findAll());
			System.out.println(permissionService.findAll()+"123131");
			System.out.println("这是moules"+permissionService.getModules());*/
		
		render("/admin/xxh_test/errorxxh.ftl");
		
	}
	
	public  void deleteorder() {
		//Db.deleteById("cart_item", "cart_id", cartId);
		String id = getPara("id");
		try {
		
		List<Order> order = Order.dao.find("select * from `order` where member_id=?",id);
		//订单集合
		List<Long> orderid = new ArrayList<>();
		//快递集合
		List<Long> shippingsid = new ArrayList<>();
		//退货集合
		List<Long> returnss = new ArrayList<>();
		for(int i = 0; i< order.size(); i++) {
			orderid.add(order.get(i).getId());
		}
		List<Shipping> shippings = Shipping.dao.find("select * from shipping where order_id in ("+StringUtils.strip(orderid.toString(),"[]")+")");
		for(int s = 0 ; s<shippings.size(); s++) {
			shippingsid.add(shippings.get(s).getId());
		}
		List<Returns> returns = Returns.dao.find("select * from returns where order_id in ("+StringUtils.strip(orderid.toString(),"[]")+")");
		for(int r = 0 ; r<returns.size(); r++) {
			returnss.add(returns.get(r).getId());
		}
		System.out.println("这是orderid:" + orderid   +"这是shippingsid:   " + shippingsid + "这是退货returnss:"    + returnss);
		//删除订单项
		Db.update("delete from order_item where order_id in ("+StringUtils.strip(orderid.toString(),"[]")+")");
		//删除log记录
		Db.update("delete from order_log where  order_id in ("+StringUtils.strip(orderid.toString(),"[]")+")");
		//删除快递项
		Db.update("delete from shipping_item where shipping_id in ("+StringUtils.strip(shippingsid.toString(),"[]")+")");
		//删除快递
		Db.update("delete from shipping where id in ("+StringUtils.strip(shippingsid.toString(),"[]")+")");
		//删除退货项
		Db.update("delete from returns_item where return_id in ("+StringUtils.strip(returnss.toString(),"[]")+")");
		//删除快递
		Db.update("delete from returns where id in ("+StringUtils.strip(returnss.toString(),"[]")+")");
		//删除支付方式
		Db.update("delete from payment where order_id in ("+StringUtils.strip(orderid.toString(),"[]")+")");
		//删除订单
		Db.update("delete from `order` where id in ("+StringUtils.strip(orderid.toString(),"[]")+")");
		renderJson("删除完成");
		} catch (Exception e) {
			System.out.println(e);
		renderJson("出现异常");
		}
	}
	/**
	 * 添加订单
	 */
	public void addOrder() {
		String derid = getPara("orderid");
		String card = getPara("card");
		Date date= new Date();
		
		
		Gson gson = new Gson();
		OrderUtil user = gson.fromJson(derid,OrderUtil.class);
		
		
       //判断数据是否为空
       if(user.getMemberId()==null||user.getReceiverId()==null||user.getProductId()==null||user.getPaymentamount()==null) {
			this.renderJson("error");
			return;
		}
       
		
		Map<String,Object> map = new HashMap<>();
		//创建通用对象临时存储
		Ret ret = Ret.create();
		
		try {
		//根据id查询用户的id地址
			 Receiver receiver= new Receiver();
	     	 receiver = receiver.dao.findById(user.getReceiverId());
		
		List<Receiver> receivers = receiver.dao.find("select * from receiver where member_id=?",user.getMemberId());
		//判断地址是否相同
		int isTheSame = 0;
		for(int i = 0; i<receivers.size();i++) {
			if(receivers.get(i).getId().equals(receiver.getId())) {
				isTheSame++;
				System.out.println("第"+i+"个地址id相同");
			}else {
				System.out.println("第"+i+"个地址id不同");
			}
		}
		if(receiver==null||isTheSame==0) {
			map.put("status", 0);
			map.put("message", "找不到用户地址");
			renderJson(map);
			return;
		}
		//调用添加订单方法	
		addOrderUtil addOrderUtil = new addOrderUtil();
		//获取订单项
		List<ProductUtil> Products = user.getProductId();
		
		//总价钱
		int totalPrice = 0;
		//总数量
		int totalQuantity = 0;
		//总重量
		int totalWeight = 0;
		
		for(int i = 0; i< Products.size(); i++) {
			//通过商品项id查询对应商品的信息
			Product product = Product.dao.findById(Products.get(i).getId());
			if(product==null) {
				map.put("status", 0);
				map.put("message", "找不到对应商品项");
				renderJson(map);
				return;
			}
			//获取对应商品的id
			Long goodsid= product.getGoodsId();
			if(goodsid==null) {
				map.put("status", 0);
				map.put("message", "找不到对应商品");
				renderJson(map);
				return;
			}
			//在通过商品id查询商品的单价和重量
			Goods goods = Goods.dao.findById(goodsid);
			if(goods==null) {
				map.put("status", 0);
				map.put("message", "找不到对应商品信息");
				renderJson(map);
				return;
			}
			if(goods.getWeight()==null) {
				goods.setWeight(0);
			}
			//总价钱
			totalPrice+=Integer.parseInt(Products.get(i).getNum())*goods.getPrice().intValue();
			//总数量
			totalQuantity+=Integer.parseInt(Products.get(i).getNum());
			//总重量
			totalWeight+=Integer.parseInt(Products.get(i).getNum())*goods.getWeight();
		}
		
		Member member =Member.dao.findById(user.getMemberId());
		 BigDecimal big2 = new BigDecimal(user.getPaymentamount().toString());
		BigDecimal balance = null;
		balance = member.getBalance();
		if (balance != null && balance.compareTo(BigDecimal.ZERO) < 0) {
			map.put("status", 0);
			map.put("message", "余额小于等于0");
			renderJson(map);
			return;
		}
		if (balance != null && balance.compareTo(big2) < 0) {
			map.put("status", 0);
			map.put("message", "余额不足");
			renderJson(map);
			return;
		}
		ret.set("receiver",receiver);
		ret.set("totalPrice",totalPrice);
		ret.set("totalQuantity",totalQuantity);
		ret.set("totalWeight",totalWeight);
		
		
		if(user.getPaymentamount().equals(totalPrice)) {
			
			Db.tx(() -> {
			//调用添加订单方法
			//Long orderId= addOrderUtil.addOrder(date,receiver, user.getMemberId(), user.getMemo(), totalPrice,totalQuantity,totalWeight,user.getPaymentamount());
			Order orderId= addOrderUtil.addOrder(date,(Receiver) ret.get("receiver"), user.getMemberId(), user.getMemo(), (Integer)ret.get("totalPrice"),(Integer)ret.get("totalQuantity"),(Integer)ret.get("totalWeight"),user.getPaymentamount());
				
			for(int i = 0; i< Products.size(); i++) {
				
				//通过商品项id查询对应商品的信息
				Product product = Product.dao.findById(Products.get(i).getId());
				
				//获取对应商品的id
				Long goodsid= product.getGoodsId();
				
				//在通过商品id查询商品的单价和重量
				Goods goods = Goods.dao.findById(goodsid);
				
				if(goods.getIsDz().equals(String.valueOf(Products.get(i).getType()))) {
					System.out.println("类型相同");
				}else {
					System.out.println("类型不同");
				}
				//调用添加订单项方法
				addOrderUtil.addOrderItem(date,goods.getName(), goods.getPrice(), goods.getImage(), Products.get(i).getId(),Products.get(i).getNum(),goods.getWeight(),goods.getIsDz(),product.getSn(),orderId);
			}
				//付款方式
				OrderLog orderLog = new OrderLog();
				orderLog.setType(OrderLog.Type.create.ordinal());
				orderLog.setOrderId(orderId.getId());
				orderLogDao.save(orderLog);
				Payment payment = new Payment();
				payment.setMethod(Payment.Method.deposit.ordinal());
				payment.setFee(BigDecimal.ZERO);
				payment.setAmount(orderId.getAmount());
				payment.setOrderId(orderId.getId());
				OrderService orderService = new OrderService();
				orderService.payment(orderId, payment, null);
				mailService.sendCreateOrderMail(orderId);
				smsService.sendCreateOrderSms(orderId);
			
				if (card!=null) {
					//删除购物车项
					Db.update("delete from cart_item where id in ("+card+")");
				}
				
			map.put("status", 1);
			map.put("message", "购买成功");
			this.renderJson(map);
			 return true;
			});	
		}else {
			 map.put("status", 0);
			 map.put("message", "订单有误");
			 this.renderJson(map);
			 return;
		}
	} catch (Exception e) {
		System.out.println("sdasdasdsdas:  "+e);
		map.put("status", 0);
		map.put("message", "订单异常");
		this.renderJson(map);
	}
		 
} 
		 
	
	
	
	/**
	 * 查看用户所有订单
	 * 需要用户的id
	 */
	public void ViewOrder() {
		String userid=getPara("id");
		String cp =getPara("cp");
		int page =0;
		if(TextUtils.isEmpty(userid)) {
			this.renderJson("error");
			return;
		}
		
		 if(!TextUtils.isEmpty(cp)) {
			 page=Integer.parseInt(cp);
			 }
			Map<String,Object> map = new HashMap<String, Object>();
		 try {
		/*OrderItem orderItem =OrderItem.dao.findFirst("select  count(orm.id) from  order_item orm LEFT JOIN  `order` oer ON orm.order_id=oer.id where oer.member_id=?",userid);
		Long Quantity=orderItem.get("count(orm.id)");*/
		List<Record> Orders =Db.find("select orm.id as ormid,orm.name as name,orm.price as price,orm.quantity as quantity,orm.thumbnail as thumbnail,orm.order_item_status as orderItemStatus,orm.order_id as ormorderId,orm.product_id as ormproductId,\r\n" + 
											"oer.id as oerid,oer.freight,oer.is_use_coupon_code as isUseCouponCode,\r\n" + 
											"mr.id as mrid,\r\n" + 
											"pt.id as ptid,\r\n" + 
											"gs.id as gsid,gs.store_id as gsstoreId,gs.caption,gs.seo_title as seoTitle,\r\n" + 
											"se.id as seid,se.sname as sname,se.store_url as storeUrl\r\n" + 
											"FROM\r\n" +                              				//orm 订单项
											"order_item orm LEFT JOIN\r\n" +          				//oer 订单
											"`order` oer ON orm.order_id=oer.id\r\n" +  			//mr  会员
											"LEFT JOIN member mr ON oer.member_id=mr.id\r\n" + 		//pt  订单项
											"LEFT JOIN product pt ON orm.product_id=pt.id\r\n" +    //gs  商品
											"LEFT JOIN goods gs ON pt.goods_id=gs.id\r\n" +         //se  店铺  
											"LEFT JOIN ses_store se ON gs.store_id=se.id WHERE mr.id=?&&orm.is_delete=0  ORDER BY orm.create_date desc limit ?,5",userid,page*5);
		if(Orders.size()==0) {
			map.put("status", 1);
			map.put("message", "您还没有订单哦！");
		}else {
			map.put("Orders", Orders);
			map.put("status", 1);
			map.put("message", "success");
		}
			this.renderJson(map);
		 } catch (Exception e) {
		map.put("status", 0);
		map.put("message", "查看订单出现异常");
    	this.renderJson(map);	 
		 }
	}
	
	/**
	 * 查看订单详情
	 * 需要订单项的id 
	 * 用户id
	 */
	public void ViewOredrDetails() {
		String orderid=getPara("ormorderId");
		String userid =getPara("id");
		if(TextUtils.isEmpty(orderid)) {
			this.renderJson("error");
			return;
		}
		Map<String, Object> map = new HashMap<String,Object>();
		try {
		List<Record> Orders =Db.find("select orm.id as ormid,orm.create_date as createDate,orm.name as name,orm.sn as sn,orm.order_item_status as orderItemStatus,orm.price as price,orm.quantity as quantity,orm.thumbnail as thumbnail,orm.is_review as isReview,\r\n" + 
				"			 				 oer.id as oerid,oer.freight as freight,oer.consignee as consignee,oer.area_name as areaName,oer.address as address,oer.is_use_coupon_code as isUseCouponCode,\r\n" + 
				"		   					 mr.id as mrid,\r\n" + 
				"		   	 				 pt.id as ptid,\r\n" + 
				"			 				 gs.id as gsid,gs.caption,\r\n" + 
				"	     					 se.id as seid,se.sname as sname,se.store_url as storeUrl,\r\n" + 
				"			 				 sg.id as sgid,sg.create_date as sgcreateDate,sg.tracking_no as trackingNo,sg.delivery_corp as deliveryCorp,sg.delivery_corp_code as deliveryCorpCode\r\n" + 
				"FROM\r\n" + 
				"order_item orm LEFT JOIN\r\n" +  						//订单
				"`order` oer ON orm.order_id=oer.id\r\n" + 				//订单项
				"LEFT JOIN member mr ON oer.member_id=mr.id\r\n" + 		//会员
				"LEFT JOIN product pt ON orm.product_id=pt.id\r\n" + 	//商品项
				"LEFT JOIN goods gs ON pt.goods_id=gs.id\r\n" + 	 	//商品		
				"LEFT JOIN ses_store se ON gs.store_id=se.id \r\n" +    //店铺   //快递
				"LEFT JOIN shipping  sg  ON  sg.order_id=oer.id WHERE orm.id=? && mr.id=?",orderid,userid);
		if(Orders.size()==0) {
			map.put("status", 0);
			map.put("message", "查看有误");
		}else {
			map.put("Orders", Orders);
			map.put("status", 1);
		}
			this.renderJson(map);
		} catch (Exception e) {
			map.put("status", 0);
			map.put("message", "查看出现异常");
			this.renderJson(map);
		}
	}
	
	/**
	 * 删除订单
	 */
	public void DeleteOrder() {
		String oredrItemid =getPara("ormid");
		if(TextUtils.isEmpty(oredrItemid)) {
			this.renderJson("error");
			return;
		}
		Map<String,Object> map = new HashMap<String,Object>();
		try {
		OrderItem order= OrderItem.dao.findById(oredrItemid);
		Integer status= order.getOrderItemStatus();
		if(status==7||status==4||status==3)	{
			order.setModifyDate(new Date());
			order.setVersion(order.getVersion()+1);
			order.setIsDelete(1);
			order.update();
			map.put("status", 1);
			map.put("message", "success");
		}else {
			map.put("status", 0);
			map.put("message", "商品状态未完成");
		}
		this.renderJson(map);
		} catch (Exception e) {
			map.put("status", 0);
			map.put("message", "删除出现异常");
			this.renderJson(map);
		}
			
	}
	
	/**
	 * 待发货
	 * 需要用户的id
	 */
	public void  WaitShip() {
		
		String userid=getPara("id");
		if(TextUtils.isEmpty(userid)) {
			this.renderJson("error");
			return;
		}
		Map<String, Object> map = new HashMap<>();
		try {
		List<Record> Orders =Db.find("select orm.id as ormid,orm.name as name,orm.price as price,orm.quantity as quantity,orm.thumbnail as thumbnail,orm.weight as weight,orm.order_item_status as orderItemStatus,\r\n" + 
				"oer.id as oerid,oer.create_date as createDate,oer.freight as freight,oer.is_use_coupon_code as isUseCouponCode,oer.member_id as oermemberId,\r\n" + 
				"mr.id as mrid,\r\n" + 
				"pt.id as ptid,\r\n" + 
				"gs.id as gsid,gs.seo_title as seoTitle,gs.caption,\r\n" + 
				"se.id as seid,se.sname as sname,se.store_url as storeUrl\r\n" + 
				"FROM\r\n" + 
				"order_item orm LEFT JOIN\r\n" + 
				"`order` oer ON orm.order_id=oer.id\r\n" + 
				"LEFT JOIN member mr ON oer.member_id=mr.id\r\n" + 
				"LEFT JOIN product pt ON orm.product_id=pt.id\r\n" +   
				"LEFT JOIN goods gs ON pt.goods_id=gs.id\r\n" + 
				"LEFT JOIN ses_store se ON gs.store_id=se.id WHERE mr.id=? &&orm.order_item_status=1  ORDER BY orm.create_date desc",userid);
		System.out.println(Orders);
		if(Orders.size()==0) {
			map.put("status", 1);
			map.put("message", "暂未有待发货商品");
		}else {
			map.put("WaitShip", Orders);
			map.put("status", 1);
		}
		this.renderJson(map);
		} catch (Exception e) {
		map.put("status", 0);
		map.put("message", "查询出现异常");
		this.renderJson(map);
		}
	}
	
	/**
	 * 待收货
	 * 需要用户的id
	 */
	public void WaitReceipt() {
		String userid=getPara("id");
		if(TextUtils.isEmpty(userid)) {
			this.renderJson("error");
			return;
		}
		Map<String, Object> map = new HashMap<>();
		try {
		List<Record> Orders =Db.find("select orm.id as ormid,orm.name as name,orm.price as price,orm.quantity as quantity,orm.thumbnail as thumbnail,orm.weight as weight,orm.order_item_status as orderItemStatus,\r\n" + 
				"oer.id as oerid,oer.create_date as createDate,oer.freight as freight,oer.is_use_coupon_code as isUseCouponCode,oer.member_id as oermemberId,\r\n" + 
				"mr.id as mrid,\r\n" + 
				"pt.id as ptid,\r\n" + 
				"gs.id as gsid,gs.seo_title as seoTitle,gs.caption,\r\n" + 
				"se.id as seid,se.sname as sname,se.store_url as storeUrl\r\n" + 
				"FROM\r\n" + 
				"order_item orm LEFT JOIN\r\n" + 
				"`order` oer ON orm.order_id=oer.id\r\n" + 
				"LEFT JOIN member mr ON oer.member_id=mr.id\r\n" + 
				"LEFT JOIN product pt ON orm.product_id=pt.id\r\n" +   
				"LEFT JOIN goods gs ON pt.goods_id=gs.id\r\n" + 
				"LEFT JOIN ses_store se ON gs.store_id=se.id WHERE mr.id=? &&orm.order_item_status=2  ORDER BY orm.create_date desc",userid);
		map.put("WaitShip", Orders);
		map.put("status", 1);
		this.renderJson(map);
		} catch (Exception e) {
		map.put("status", 0);
		this.renderJson(map);
		}
	}
	
	
	/**
	 * 确认收货
	 * 需要订单项的id:ormid 用户id:userid  订单id:orderid
	 */
	public void confirmReceupt() {
		String ormid = getPara("ormid");
		String id = getPara("userid");
		String orderid = getPara("orderid");
		if(TextUtils.isBlank(ormid)||TextUtils.isBlank(id)||TextUtils.isBlank(orderid)) {
			this.renderJson("error");
			return;
		}
		Map<String, Object> map = new HashMap<>();
		try {
			List<Order> order = Order.dao.find("select * from `order` where id=? && member_id=?",orderid,id);
			
			Order order1 = orderService.findBySn(order.get(0).getSn());
			System.out.println("这是order1:" + order1);
			if (order1 == null) {
				map.put("status", 0);
				map.put("message", "订单未找到!");
				renderJson(map);
				return;
			}
			Member member = Member.dao.findById(order1.getMemberId());
			if (!member.equals(order1.getMember())) {
				map.put("status", 0);
				map.put("message", "订单创建人与当前用户不同!");
				renderJson(map);
				return;
			}
			if (order1.hasExpired()
					|| !Order.Status.shipped.equals(order1.getStatusName())) {
				map.put("status", 0);
				map.put("message", "订单过期或状态非已发货!");
				renderJson(map);
				return;
			}
			/*if (orderService.isLocked(order1, member, true)) {
				map.put("status", "error");
				map.put("message", "订单已经锁定");
				renderJson(map);
			}*/
		OrderItem	orderItem= OrderItem.dao.findById(ormid);
		if(order.size()==0||!orderItem.getOrderId().equals(Long.valueOf(orderid))) {
			map.put("status", 0);
			map.put("message", "订单有误");
			this.renderJson(map);
			return;
		}	
		if(orderItem.getOrderItemStatus()==2) {
			 Date date= new Date();
			 orderItem.setModifyDate(date);
			 orderItem.setVersion(orderItem.getVersion()+1);
			 orderItem.setOrderItemStatus(4);
			 orderItem.update();
			 map.put("status",1);
		}else {
				map.put("status", 0);
				map.put("message", "订单项不是已发货,无法确认收货");
				this.renderJson(map);
				return;
		}
		List<OrderItem> orderItems = OrderItem.dao.find("select order_item_status from order_item where order_id=?",order1.getId());
		
		if(OrderItemService.AllisReceipt(orderItems, 2)) {
			orderService.receive(order1, null);
		}
		 this.renderJson(map);
		} catch (Exception e) {
		 map.put("status",0);
		 this.renderJson(map);
		}
	}
		
	/**
	 * 退款申请
	 */
	public void Returns() {
		//订单项id
		String orderiemid = getPara("ormid");
		//会员id
		String MemberId = getPara("mrid");
		//产品项id
		String productId = getPara("ptid");
		//获取订单的id
		String oerid = getPara("oerid");	
		//申请说明
		String desc = getPara("desc");
		//退货类型
		int type =getParaToInt("type");
		//退款备注
		String mo = getPara("mo");
		//退款金额
		String ReturnsMoney = getPara("money");
		//退货数量
		String ReturnQuantity = getPara("ReturnQuantity");
		
		if(TextUtils.isEmpty(orderiemid)||TextUtils.isEmpty(MemberId)||TextUtils.isEmpty(productId)||TextUtils.isEmpty(oerid)||TextUtils.isEmpty(ReturnQuantity)) {
			this.renderJson("error");
			return;
		}
		Map<String, Object> map = new HashMap<>();
		if(ReturnQuantity.equals("0")) {
			map.put("status", 0);
			map.put("message", "退货数量不能等于0");
			this.renderJson(map);
			return;
		}
		try {
		Order order = Order.dao.findById(oerid);
		//查询对应的订单项
		List<OrderItem> orderItem = OrderItem.dao.find("select * from order_item where order_id=?",order.getId());
		
		if(orderItem.size()==0) {
			map.put("status", 0);
			map.put("message", "没有订单项");
			this.renderJson(map);
			return;
		}
		Date date = new Date();
		//遍历订单项
		for(int i =0; i< orderItem.size(); i++) {
			
			//判断订单项是否相同
			if(Long.valueOf(orderiemid).equals(orderItem.get(i).getId())) {
				//判断订单项的产品项是否相同
				if(Long.valueOf(productId).equals(orderItem.get(i).getProductId())) {
					Integer num = Integer.valueOf(ReturnQuantity);
					System.out.println("这是num:" + num);
					//未发货退货
					  if(orderItem.get(i).getOrderItemStatus()==1) {
						if(orderItem.get(i).getQuantity()-orderItem.get(i).getReturnedQuantity()==num||orderItem.get(i).getQuantity()-orderItem.get(i).getReturnedQuantity()>num) {
							
							orderItem.get(i).setVersion(orderItem.get(i).getVersion()+1);
							orderItem.get(i).setCreateDate(date);
							orderItem.get(i).setReturnedQuantity(orderItem.get(i).getReturnedQuantity()+num);
							orderItem.get(i).update();
							order.setVersion(order.getVersion()+1);
							order.setCompleteDate(date);
							order.setReturnedQuantity(order.getReturnedQuantity()+num);
							order.update();
						}else {
							map.put("stauts", 0);
							map.put("message", "未发货退款出错");
							this.renderJson(map);
							return;
						}
					}else {
					//已发货退货
					System.out.println(order.getReturnableQuantity());
					if(order.getReturnableQuantity()==num||order.getReturnableQuantity()>num) {
					
					if(orderItem.get(i).getShippedQuantity()==num||orderItem.get(i).getShippedQuantity()>num) {
						
						orderItem.get(i).setVersion(orderItem.get(i).getVersion()+1);
						orderItem.get(i).setCreateDate(date);
						orderItem.get(i).setReturnedQuantity(orderItem.get(i).getReturnedQuantity()+num);
						orderItem.get(i).update();
						order.setVersion(order.getVersion()+1);
						order.setCompleteDate(date);
						order.setReturnedQuantity(order.getReturnedQuantity()+num);
						order.update();
					} else {
						map.put("stauts", 0);
						map.put("message", "退货数量不能大于购买数量");
						this.renderJson(map);
						return;
					}
					}else {
						map.put("stauts", 0);
						map.put("message", "退货数量不能超过购买数量哦");
						this.renderJson(map);
						return;
					}
					}
					Ret ret = Ret.create();
					ret.set("OrderItemStauts",orderItem.get(i).getOrderItemStatus());
					ret.set("version",orderItem.get(i).getVersion());
					Db.tx(() -> {
						
						
					//创建退款
					addOrderUtil OrderUtil = new addOrderUtil();
					//添加退单
					Returns returnsId= OrderUtil.addreturns(date,oerid,mo);
					//添加退单项
					String	images= getSessionAttr("images");
					
					System.out.println("这是session:"+images);
					
					OrderUtil.addreturnsitem(date,images,orderiemid, MemberId, productId, returnsId,desc,type,ReturnsMoney);
					
					try {
						//修改订单状态
						OrderUtil.updateOrderStatus(Long.valueOf((Integer) ret.get("OrderItemStauts")), Long.valueOf(orderiemid),(Long)ret.get("version")+1);
					} catch (Exception e) {
						System.out.println(e);
						e.printStackTrace();
					}
					//清除session
					removeSessionAttr("images");
					  return true;
					});
					
					OrderLog orderLog = new OrderLog();
					orderLog.setType(OrderLog.Type.returns.ordinal());
					orderLog.setOperator(new Admin());
					orderLog.setOrderId(order.getId());
					orderLogDao.save(orderLog);

					mailService.sendReturnsOrderMail(order);
					smsService.sendReturnsOrderSms(order);
					
					map.put("status", 1);
					map.put("message", "申请退款成功");
					this.renderJson(map);
					return;
					
				}
				map.put("status", "0");
				map.put("message", "订单项的与产品项不同");
			}
			map.put("status", "0");
			map.put("message", "没有对应的订单项");
		}
		this.renderJson(map);
		return;
		} catch (Exception e) {
		map.put("status", 0);
		map.put("message", "申请退款异常");
		this.renderJson(map);
		
		}
		
	}
	
	/**
	 * 上传凭证
	 */
	public void getuploat() {
		Map<String, Object> map = new HashMap<>();
		try {
		List<UploadFile> uploadFile = getFiles();
		
		//定义图片大小
		Long imagesize = 4000000L;
		
		FilesService fs= new FilesService();
		String Returnsimgurl = "";
		//判断照片是否合法
		for(int i =0;i<uploadFile.size();i++) {
			//获取文件对象
			File file = uploadFile.get(i).getFile();
			//判断文件大小
			if(file.length()>imagesize) {
				map.put("stuats", 0);
				System.out.println("图片太大");
				map.put("message", "第"+(i+1)+"张凭证超过4M");
				this.renderJson(map);
				return;
			}
			//判断是否是图片
			if(addOrderUtil.isImage(file)) {
				System.out.println("是图片");
			}else {
				System.out.println("不是图片");
				map.put("status", 0);
				map.put("message", "请上传正确凭证照片");
				this.renderJson(map);
				return;
			}
			
		}
		System.out.println("进入将照片保存在本地..");
		//讲照片保存在本地
		for(int i =0;i<uploadFile.size();i++) {
			//获取文件对象
			File file = uploadFile.get(i).getFile();
			
				String imgaeName= UUID.randomUUID().toString()+file.getName().substring(file.getName().lastIndexOf("."));
				//相对路径加文件名
				String imagespath="/upload/returns/"+imgaeName;
				//获取绝对路径
				String images_path=this.getRequest().getServletContext().getRealPath("/");
				//路径相合
				File t = new File(images_path+imagespath);
				if(i == uploadFile.size() -1) {
					Returnsimgurl+=imagespath;
				}
				else {
					Returnsimgurl+=imagespath+",";
				}
			
				try {
					t.createNewFile();
				} catch (Exception e) {
					e.printStackTrace();
				}
				fs.fileChannelCopy(file, t);
				file.delete();
		}
		//把照片路径存到session里
		setSessionAttr("images", Returnsimgurl);
		if(map.isEmpty()) {
			map.put("img", Returnsimgurl);
			map.put("status", 1);
			map.put("message", "上传成功");
			this.renderJson(map);
		}else {
			this.renderJson(map);
		}
		} catch (Exception e) {
			map.put("status", 0);
			map.put("message", "请重新上传合格的图片凭证");
			renderJson(map);
		}
		
	
	}
	
	
	
	/**
	 * 退款详情
	 * 需要用户id跟订单项的id
	 */
	public void ReturnsDetails() {
		String userid = getPara("id");
		String ormid = getPara("ormid");
		if(TextUtils.isEmpty(userid)||TextUtils.isEmpty(ormid)) {
			this.renderJson("error");
			return;
		}
		Map<String, Object> map = new HashMap<>();
		try {
		List<ReturnsItem> returnsItem = ReturnsItem.dao.find("select rsm.id,rsm.create_date as createDate,rsm.`desc`,rsm.cause,rsm.amount,rsm.`status`,rsm.`name`,rsm.sn,rsm.type,orm.order_item_status as orderItemStatus FROM\r\n" + 
				"returns_item rsm LEFT JOIN\r\n" + 
				"order_item orm ON rsm.order_item_id=orm.id\r\n" + 
				"WHERE  rsm.member_id=?&&rsm.order_item_id=?",userid,ormid);
		map.put("returnsItem", returnsItem);
		map.put("status", 1);
		this.renderJson(map);
		} catch (Exception e) {
		map.put("status", 0);
		this.renderJson(map);	
		}
	}
	
	/**
	 * 退款/售后
	 */
	public void RefundAfterSale() {
		String userid = getPara("id");
		if(TextUtils.isEmpty(userid)) {
			this.renderJson("error");
			return;
		}
		Map<String, Object> map =new HashMap<>();
		try {
		List<Record> refund = Db.find("select rsm.id as rsmid,rsm.`name`,rsm.create_date as createDate,rsm.quantity,rsm.amount,rsm.`status`,rsm.order_item_id as orderItemId, \r\n" + 
				"gs.price as gsPrice,gs.image,\r\n" + 
				"sse.id as sseid,sse.sname\r\n" + 
				"FROM \r\n" + 
				"returns_item rsm LEFT JOIN\r\n" + 
				"product pt ON rsm.product_id=pt.id\r\n" + 
				"LEFT JOIN goods gs ON pt.goods_id=gs.id\r\n" + 
				"LEFT JOIN ses_store sse ON gs.store_id=sse.id\r\n" + 
				"WHERE rsm.member_id=?",userid);
		if(refund.size()==0) {
		  map.put("status", 1);
		  map.put("message", "你还没有退款项");
		}else {
		  map.put("refund", refund);
		  map.put("message", "success");
		  map.put("status", 1);
		}	
		this.renderJson(map);
		} catch (Exception e) {
		map.put("status", 0);
		map.put("message", "查询异常");
		this.renderJson(map);	
		}
	}
	/**
	 * 催发货
	 * 需要订单项的id
	 */
	public void Urgentdelivery() {
		String orderItemId = getPara("orderItemId");
		Map<String, Object> map = new HashMap<>();
		if(TextUtils.isEmpty(orderItemId)) {
			map.put("status", 0);
			map.put("message", "订单项id不能为空");
			return;
		}
		try {
			OrderItem orderItem = OrderItem.dao.findById(orderItemId);
			if(orderItem.getIsUrgentDelivery()==1) {
				map.put("status", 1);
				map.put("message", "已催发货了哦~");
			}else {
				orderItem.setModifyDate(new Date());
				orderItem.setVersion(orderItem.getVersion()+1);
				orderItem.setIsUrgentDelivery(1);
				orderItem.update();
				map.put("status", 1);
				map.put("message", "催发货成功~");
			}
			this.renderJson(map);
		} catch (Exception e) {
			map.put("status", 0);
			map.put("message", "催发货异常~");
			this.renderJson(map);
		}
	}
	
	/**
	 * 取消退款
	 * 需要用户id 需要订单项id 需要产品项id
	 */
	public void CancelRefund() {
		String userid = getPara("Id");
		String orderitemid = getPara("orderIdetId");
		String ptid = getPara("ptId");
		
		System.out.println("userid:"+userid+"orderitemid:" +orderitemid+"ptid："+ptid);
		if(TextUtils.isEmpty(userid)||TextUtils.isEmpty(orderitemid)||TextUtils.isEmpty(ptid)) {
			this.renderJson("error");
			return;
		}
		Map<String, Object> map = new HashMap<>();
		try {
			OrderItem orderItem = OrderItem.dao.findById(orderitemid);
			
			List<ReturnsItem> returnsItem = ReturnsItem.dao.find("select * from returns_item where member_id=? && product_id=? && order_item_id=?",userid,ptid,orderitemid);
			Integer stauts = orderItem.getOrderItemStatus();
			if(stauts==5||stauts==6&returnsItem.size()!=0) {
				
				List<ReturnsItem> returnsItem2 = ReturnsItem.dao.find("select * from returns_item where return_id="+returnsItem.get(0).getReturnId()+"");
				
				addOrderUtil util = new addOrderUtil();
				if(returnsItem2.size()==1) {
					//只有一项退款,删除退款项跟退款单
					returnsItem.get(0).delete();
					Returns.dao.findById(returnsItem2.get(0).getReturnId()).delete();
					if(stauts==5) {
						//修改订单状态
						 util.updateOrderandOrderItem(stauts, orderItem);
					}else if (stauts==6) {
						 util.updateOrderandOrderItem(stauts, orderItem);
					}else {
						System.out.println("不等于5也不能等6,那你是咋进来的？？？");
					}
					map.put("status",1);
					map.put("message","取消退款成功");
				}else {
					//不止一项退款的话,删除退款项
					returnsItem.get(0).delete();
					if(stauts==5) {
						//修改订单状态
						util.updateOrderandOrderItem(stauts, orderItem);
					}else if (stauts==6) {
						util.updateOrderandOrderItem(stauts, orderItem);
					}else {
						System.out.println("不等于5也不能等6,那你是咋进来的？？？");
					}
					map.put("status",1);
					map.put("message","取消退款成功");
				}
			}else {
				map.put("status",0);
				map.put("message","您还没有退款项呢");
			}
			this.renderJson(map);
		} catch (Exception e) {
			map.put("status",0);
			map.put("message","取消退款异常");
			this.renderJson(map);
		}
	}
	
	
}
