package com.jfinalshop.controller.usercenter;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.TextUtils;

import com.jfinal.aop.Enhancer;
import com.jfinal.core.Controller;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import com.jfinalshop.controller.usercenter.entity.MessageNotificationUtil;
import com.jfinalshop.dao.OrderItemDao;
import com.jfinalshop.model.Appversion;
import com.jfinalshop.model.Cart;
import com.jfinalshop.model.Member;
import com.jfinalshop.model.Message;
import com.jfinalshop.model.Order;
import com.jfinalshop.model.OrderItem;
import com.jfinalshop.model.SCardorder;
import com.jfinalshop.model.UserCollectionGoods;
import com.jfinalshop.model.UserCollectionShop;
import com.jfinalshop.model.UserFootprint;
import com.jfinalshop.model.client.MessageNotification;
import com.jfinalshop.util.SystemUtils;




@ControllerBind(controllerKey="/UserCenterIndex")
public class UserCenterIndex extends Controller{
	private OrderItemDao orderItemDao = Enhancer.enhance(OrderItemDao.class);
	/**
	 * 首页的数据
	 */
	public void index() {
		 String mid=getPara("id");
		 if(TextUtils.isEmpty(mid)) {
			 this.renderJson("error");
			 return;
		 }
		
	  Map<String,Object> map=new HashMap<String,Object>();
	  try {
		  
	  //查询用户信息
	  Member BasicInformation =Member.dao.findById(mid);
	  //消息数量
	  List<Message> news = Message.dao.find("select count(id) from message where receiver_id=?",mid);
	  //固定广告消息
	 List<Message> messnews = Message.dao.find("select count(*) from message where messagefrom=1");
	 Long news1 = news.get(0).get("count(id)");
	 Long messnews1 = messnews.get(0).get("count(*)");
	  
	  //购物车数量
	  List<Cart> ShoppingCart = Cart.dao.find("select count(cm.cart_id) FROM cart_item cm LEFT JOIN cart ct ON cm.cart_id=ct.id LEFT JOIN member mr ON ct.member_id=mr.id WHERE mr.id=?",mid);
	  //收藏产品数量
	  List<UserCollectionGoods> CollectionGoods =UserCollectionGoods.dao.find("select count(rns.id) FROM user_collection_goods rns LEFT JOIN member mr ON rns.member_id=mr.id WHERE mr.id=?",mid);
	  //收藏店铺数量
	  List<UserCollectionShop> CollectionShop =UserCollectionShop.dao.find("select count(rnp.id) FROM user_collection_shop rnp LEFT JOIN member mr ON rnp.member_id=mr.id WHERE mr.id=?",mid);	
	  //待发货未读数量	
	  List<Order> OrderWaitShip = Order.dao.find("select COUNT(orm.id) FROM `order` oer LEFT JOIN order_item orm ON orm.order_id=oer.id  WHERE	oer.member_id=? &&orm.order_item_status=1&&orm.is_read=0",mid);
	  //待收货未读数量
	  List<Order> OrderWaitReceipt=Order.dao.find("select COUNT(orm.id) FROM `order` oer LEFT JOIN order_item orm ON orm.order_id=oer.id  WHERE	oer.member_id=? &&orm.order_item_status=2&&orm.is_read=2",mid);
	  //消息提示
	 // List<Record> notification=Db.find("select oer.id as oerid,orm.id as ormid,orm.name as name,orm.thumbnail as thumbnail FROM `order` oer LEFT JOIN order_item orm ON orm.order_id=oer.id where oer.member_id=? ORDER BY orm.create_date desc LIMIT 0,3",mid);
	 //查订单                                                                                
	 List<OrderItem> orderItems =  OrderItem.dao.find("select orm.id,orm.name,orm.thumbnail,orm.order_item_status FROM order_item orm LEFT JOIN  `order` oer ON orm.order_id=oer.id where oer.member_id=?",mid);
	 //存消息的list
	 List<MessageNotificationUtil> list = new ArrayList<>();
	 //查询订单		
	 for(int i =0; i< orderItems.size(); i++) {
		 Integer stauts = orderItems.get(i).getOrderItemStatus();
		 if(stauts==null) {
			 continue; 
		 }
		 if(stauts==1||stauts==2||stauts==3||stauts==7) {
			 //存消息的对象
			 MessageNotificationUtil messageOrder = new MessageNotificationUtil();
			 messageOrder.setId(orderItems.get(i).getId());
			 messageOrder.setName(orderItems.get(i).getName());
			 messageOrder.setImgs(orderItems.get(i).getThumbnail());
			 messageOrder.setStatus(MessageNotification.Order.getName(orderItems.get(i).getOrderItemStatus(),orderItems.get(i).getName()));
			//把订单添加到集合中
			 list.add(messageOrder);
		 }
	 }
	 //添加公共信息
	 Message message = Message.dao.findById(1);
	 MessageNotificationUtil messageOrder = new MessageNotificationUtil();
	 messageOrder.setId(message.getId());
	 messageOrder.setImgs(message.getMessageimgurl());
	 messageOrder.setName(message.getTitle());
	 messageOrder.setStatus(message.getContent());
	 list.add(messageOrder);
	 
	 
	 
	 //查询ESE 卡包
	 List<SCardorder> sCardorders = SCardorder.dao.find("select id,card_name,status  from  s_cardorder where member_id=?",mid);
	 for(int i =0; i< sCardorders.size();i++) {
		String  status = sCardorders.get(i).getStatus();
		if(status.equals("3")) {
			 MessageNotificationUtil messageCard = new MessageNotificationUtil();
			//卡包id
			messageCard.setId(sCardorders.get(i).getId());
			//卡包名字
			messageCard.setName(sCardorders.get(i).getCardName());
			//卡包状态
			messageCard.setStatus(MessageNotification.sesCardpackage.getNames(Integer.valueOf(sCardorders.get(i).getStatus()), sCardorders.get(i).getCardName()));
			list.add(messageCard);
		}
	 }
	  //足迹
	  List<UserFootprint> userFootprints = UserFootprint.dao.find("select count(id) from user_footprint where userid=?",mid);
	  Long Goods =CollectionGoods.get(0).get("count(rns.id)");
	  Long Shop =CollectionShop.get(0).get("count(rnp.id)");
	  Long Collections=Goods+Shop;	 
	  //用户名字
	  map.put("userName", BasicInformation.getNickname());
	  //积分
	  map.put("userPoint", BasicInformation.getPoint());
	  //头像
	  map.put("Portrait", BasicInformation.getAvatar());
	  //余额
	  map.put("balance", BasicInformation.getBalance());
	  //消息条数
	  map.put("news", news1+messnews1);
	  //购物车
	  map.put("ShoppingCart", ShoppingCart.get(0).get("count(cm.cart_id)"));
	  //收藏
	  map.put("Collections", Collections);
	  //待发货未读数量
	  map.put("WaitShip", OrderWaitShip.get(0).get("COUNT(orm.id)"));
	  //待收货未读数量
	  map.put("WaitReceipt", OrderWaitReceipt.get(0).get("COUNT(orm.id)"));
	  //消息提醒
	  map.put("notification", list);
	  //足迹
	  map.put("userFootprints", userFootprints.get(0).get("count(id)"));
	  map.put("status", 1);
	  this.renderJson(map);
	  } catch (Exception e) {
		  System.out.println(e);
	  map.put("status", 0);
	  this.renderJson(map);
		}
	}
	
	
	/**
	 * 待发货未读
	 * 需要用户的id
	 */
	public void  IndexWaitShip() {
		
		String userid=getPara("id");
		if(TextUtils.isEmpty(userid)) {
			this.renderJson("error");
			return;
		}
		Map<String, Object> map = new HashMap<>();
		try {
			//还回的数据
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
		//需要修改已读的数据
		List<OrderItem> updateIsread = OrderItem.dao.find("select orm.id,orm.modify_date,orm.version  from order_item orm LEFT JOIN `order` oer ON orm.order_id=oer.id LEFT JOIN member mr ON oer.member_id=mr.id where mr.id=? &&orm.order_item_status=1 &&orm.is_read=0  ORDER BY orm.create_date desc",userid);	
		List<Long> ormids = new ArrayList<>();
		for(int i = 0; i<updateIsread.size();i++) {
			orderItemDao.update(updateIsread.get(i));
			Long ormid= updateIsread.get(i).getId();
			ormids.add(ormid);
		}
		if(ormids.size()!=0) {
	    	//将list转为字符串去除括号
			String ormidsupdate= StringUtils.strip(ormids.toString(),"[]");  
			//批量修改已读
	    	String sql="UPDATE order_item SET is_read=1 WHERE id in("+ormidsupdate+")";
	    	Db.update(sql);
		}
		map.put("WaitShip", Orders);
		map.put("status", 1);
		this.renderJson(map);
		} catch (Exception e) {
		map.put("status", 0);
		this.renderJson(map);
		}
	}
	
	/**
	 * 待收货
	 * 需要用户的id
	 */
	public void IndexWaitReceipt() {
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
			//需要修改已读的数据
		List<OrderItem> updateIsread = OrderItem.dao.find("select orm.id,orm.modify_date,orm.version  from order_item orm LEFT JOIN `order` oer ON orm.order_id=oer.id LEFT JOIN member mr ON oer.member_id=mr.id where mr.id=? &&orm.order_item_status=2 &&orm.is_read=2  ORDER BY orm.create_date desc",userid);	
		List<Long> ormids = new ArrayList<>();
		for(int i = 0; i<updateIsread.size();i++) {
				orderItemDao.update(updateIsread.get(i));
				Long ormid= updateIsread.get(i).getId();
				ormids.add(ormid);
			}	
		if(ormids.size()!=0) {
    	//将list转为字符串去除括号
		String ormidsupdate= StringUtils.strip(ormids.toString(),"[]");  
		//批量修改已读
    	String sql="UPDATE order_item SET is_read=3 WHERE id in("+ormidsupdate+")";
    	Db.update(sql);
		}
		map.put("WaitShip", Orders);
		map.put("status", 1);
		this.renderJson(map);
		} catch (Exception e) {
		map.put("status", 0);
		this.renderJson(map);
		}
	}
	/**
	 * 已完成
	 * 需要用户id
	 */
	public void completed() {
		String id = getPara("id");
		Map<String,Object> map = new HashMap<>();
		if(TextUtils.isEmpty(id)) {
			map.put("status", "0");
			map.put("message", "参数不可为空");
			this.renderJson(map);
			return;
		}
		try {
			List<Record> Orders =Db.find("select orm.id as ormid,orm.name as name,orm.price as price,orm.quantity as quantity,orm.thumbnail as thumbnail,orm.order_item_status as orderItemStatus,\r\n" + 
					"oer.id as oerid,oer.create_date as createDate,oer.freight as freight,oer.is_use_coupon_code as isUseCouponCode,\r\n" + 
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
					"LEFT JOIN ses_store se ON gs.store_id=se.id WHERE mr.id=? &&orm.order_item_status=4  ORDER BY orm.create_date desc",id);
			if(Orders.size()==0) {
				map.put("status", "0");
				map.put("message", "暂未有完成订单");
			}else {
				map.put("status", "1");
				map.put("Orders", Orders);
			}
			this.renderJson(map);
		} catch (Exception e) {
			map.put("status", "0");
			map.put("message", "查询出现异常");
			this.renderJson(map);
		}
	}
	
	/**
	 * 关于ses
	 */
	public void introduction() {
		String id = getPara("id");
		Member member = Member.dao.findById(id);
		setAttr("member", member);
		
		if(SystemUtils.getSetting().getIntroduction()!=null){ 
			setAttr("introduction", SystemUtils.getSetting().getIntroduction());
		}else{
			setAttr("introduction", "");
		}
		setAttr("title", "关于ses- 用户中心");
		render("/wap/member/deposit/introduction.ftl");
	}
	

}
