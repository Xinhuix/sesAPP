package com.jfinalshop.controller.usercenter;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.util.TextUtils;


import com.jfinal.core.Controller;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinalshop.model.Order;
import com.jfinalshop.model.OrderItem;
import com.jfinalshop.model.Receiver;
@ControllerBind(controllerKey="/UserCustom")
public class UserCustom extends Controller{
	/**
	 * 查看私人订制
	 */
	public void Customizeds() {
		String userid=getPara("id");
		if(TextUtils.isEmpty(userid)) {
			this.renderJson("error");
			return;
		}
		Map<String, Object> map = new HashMap<>();
		try {
		
		List<Record> customs =Db.find("select orm.id as ormid,orm.create_date as createDate,orm.quantity,orm.name,orm.price,orm.thumbnail,orm.weight,orm.order_item_status as orderItemStatus,\r\n" + 
				"			 oer.id as oerid,oer.freight,oer.is_use_coupon_code as isUseCouponCode,\r\n" + 
				"			 pt.id as ptid,gs.id as gsid,gs.seo_title as seoTitle,gs.onlineurl,gs.caption,\r\n" + 
				"			 se.sname,se.id as seid,se.store_url as storeUrl\r\n" + 
				"FROM\r\n" + 
				"order_item orm LEFT JOIN\r\n" + 						 //订单项
				"`order` oer ON orm.order_id=oer.id\r\n" + 				 //订单
				"LEFT JOIN product pt ON orm.product_id=pt.id\r\n" + 	 //商品项	
				"LEFT JOIN goods gs ON pt.goods_id=gs.id \r\n" + 		 //商品
				"LEFT JOIN member mr ON oer.member_id=mr.id\r\n" + 		 //会员	
				"LEFT JOIN ses_store se ON gs.store_id=se.id\r\n" + 	 //店铺	
				"WHERE mr.id="+userid+"&&orm.type=1 ORDER BY orm.create_date desc");
		map.put("customs", customs);
		map.put("status", 1);
		this.renderJson(map);
		} catch (Exception e) {
		map.put("status", 0);
		this.renderJson(map);
		}		
	}
	
	/**
	 * 查看私人订制详情
	 */
	public void ViewApplicationShip() {
		//订单项id
		String ordetiterId =getPara("ormid");
		String userId = getPara("id");
		if(TextUtils.isEmpty(ordetiterId)||TextUtils.isEmpty(userId)) {
			this.renderJson("error");
			return;
		}
		Map<String, Object> map =new HashMap<>();
		try {
		
	List<Record> ship = Db.find("select orm.id as ormid,orm.create_date as createDate,orm.quantity,orm.name,orm.price,orm.thumbnail,orm.weight,\r\n" + 
			"							 oer.id as oerid,oer.freight,oer.is_use_coupon_code as isUseCouponCode,\r\n" + 
			"							 gs.seo_title as seoTitle,gs.onlineurl,gs.caption,\r\n" + 
			"							 se.sname,se.id as seid,se.store_url as storeUrl\r\n" + 
			"FROM \r\n" + 
			"order_item orm LEFT JOIN\r\n" + 
			"`order` oer ON orm.order_id=oer.id\r\n" + 
			"LEFT JOIN product pt ON orm.product_id=pt.id\r\n" + 
			"LEFT JOIN goods gs ON pt.goods_id=gs.id \r\n" + 
			"LEFT JOIN member mr ON oer.member_id=mr.id\r\n" + 
			"LEFT JOIN ses_store se ON gs.store_id=se.id\r\n" + 
			"WHERE orm.id=?&&orm.type=1&&oer.member_id=?",ordetiterId,userId);
		map.put("ship", ship);
		map.put("stauts", 1);
		this.renderJson(map);
		} catch (Exception e) {
		map.put("stauts", 0);
		this.renderJson(map);
		}
	}
	/**
	 * 申请发货
	 */
	public void ApplicationShips() {
		//地址id
		String receiverId = getPara("receiverId");
		//会员id
		String userId = getPara("id");
		//订单项id
		String orderItemId = getPara("ormId");
		if(TextUtils.isEmpty(orderItemId)) {
			this.renderJson("error");
			return;
		}
		
		Map<String, Object> map = new HashMap<>();
		try {
		//判断地址是否对用户
		Receiver receiver = Receiver.dao.findById(receiverId);
		if(!String.valueOf(receiver.getMemberId()).equals(userId)) {
			System.out.println("地址不相同");
			this.renderJson("地址不正确");
			return;
		}
		 
		 //获取订单id
		 OrderItem orderItem = OrderItem.dao.findFirst("select order_id from order_item where id=?",orderItemId);
		 Order orderc = Order.dao.findById(orderItem.getOrderId());
		 System.out.println(orderc);
		 if(orderc==null) {
			 this.renderJson("订单有误");
			 return;
		 }
		 //获取用户的订单
		 List<Order> order = Order.dao.find("select id,version from `order` where member_id=?",userId);
		 //遍历订单
		 for(int i = 0; i<order.size(); i ++ ) {
			 //判断订单是否相同
			 if(order.get(i).getId().equals(orderItem.getOrderId())) {
				 if(receiver!=null) {
				 Order addorder = new Order();
				 addorder.setId(order.get(i).getId());
				 addorder.setVersion(order.get(i).getVersion()+1);
				 addorder.setModifyDate(new Date());
				 addorder.setAddress(receiver.getAddress());
				 addorder.setAreaName(receiver.getAreaName());
				 addorder.setConsignee(receiver.getConsignee());
				 addorder.setPhone(receiver.getPhone());
				 addorder.setAreaId(receiver.getAreaId());
				 addorder.update();
				 //申请发货
				 Db.update("update order_item set is_urgent_delivery=3 where id=?",orderItemId);
				 map.put("status", 1);
				 }else {
				 Db.update("update order_item set is_urgent_delivery=3 where id=?",orderItemId);
				 map.put("status", 1);	 
				 }
			 }else {
				 System.out.println("不相同");
			}
		 }
		 this.renderJson(map);
		} catch (Exception e) {
		map.put("status", 0);
		this.renderJson(map);
		}
	}
}
