package com.jfinalshop.controller.usercenter;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils.Null;
import org.apache.http.util.TextUtils;

import com.jfinal.core.Controller;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinalshop.model.Cart;
import com.jfinalshop.model.CartItem;


@ControllerBind(controllerKey="/UserShoppingCart")
public class UserShoppingCart extends Controller {
	/**
	 * 查询购物车商品
	 * 需要用户的id
	 */
	public void ViewCart() {
		//获取用户的id
		String userid="49";
		//String userid=getPara("id");
		//分页的页数
		String NumPage="10";
		
		//根据用户id查询他的购物车
		List<Record> viewcart =Db.find("select ct.id as ctid,ct.member_id as cemember,mr.id as mrid	FROM cart ct LEFT JOIN member mr ON ct.member_id=mr.id WHERE mr.id=?",userid);
		Long ctid=(viewcart.get(0).get("ctid"));
		System.out.println(ctid);
		//根据购物车的id查询多条出购物项;
		List<Record> viewcarts =Db.find("select ctm.id as ctmid,ctm.product_id  as ctmproductId,ctm.quantity as quantity,\r\n" + 
				"				ct.id as ctid,\r\n" + 
				"				gs.id as gsid,gs.name as name,gs.image as image, gs.weight as weight,\r\n" + 
				"				gs.price as price,gs.seo_title as seoTitle, gs.store_id as gsstoreId,gs.goods_url as goodsUrl,\r\n" + 
				"				se.id as seid,se.store_url as storeUrl,se.sname as sname	\r\n" + 
				"				FROM\r\n" + 
				"				cart_item ctm LEFT JOIN\r\n" + 
				"				cart ct ON ctm.cart_id=ct.id  \r\n" + 
				"				LEFT JOIN product pt ON\r\n" + 
				"				ctm.product_id=pt.id\r\n" + 
				"				LEFT JOIN goods gs ON\r\n" + 
				"				pt.goods_id=gs.id\r\n" + 
				"				LEFT JOIN ses_store se ON\r\n" + 
				"				gs.store_id=se.id WHERE ctm.cart_id="+ctid+" ORDER BY ctm.create_date desc LIMIT 0,5"); 	
		System.out.println(viewcarts);
		if(viewcarts.size()==0) {
			this.renderJson("你尚未添加任何商品");
		}
		this.renderJson(viewcarts);
		
	}
	
	/**
	 * 删除购物车
	 */
	public void DeleteCart() {
		String ctmid=getPara("ctmid");
		if(TextUtils.isEmpty(ctmid)) {
			this.renderJson("error");
			return;
		}
		Map<String, Object> map = new HashMap<>();
		try {
		
		
	/*	for(int i = 0 ; i < list.length; i ++) {
			
			if(i == list.length -1) {
				deleteCartId+=list[i];
			}
			else {
				deleteCartId+=list[i]+",";
			}	
		}*/
		System.out.println(ctmid);
		String sql ="delete from cart_item where id in ("+ctmid+")";
		Db.update(sql);
		map.put("status", 1);
		this.renderJson(map);
		} catch (Exception e) {
		map.put("status", 0);
		this.renderJson(map);
		}
	}
	
	/**
	 * 添加购物车
	 * 需要用户的id,以及添加商品的相关内容
	 */
	public void addCart() {
	//String userid =getPara("userid");
	String userid ="18";
	/**
	 * 先判断这个用户有没有购物车
	 */
	List<Record> ifCart=Db.find("select * from cart where member_id="+userid+"");
	CartItem cartItem= new CartItem();
	//创建时间
	Date date= new Date();
	
	if(ifCart.size()==0) {
		
	//设置过期时间
	Calendar c=Calendar.getInstance();
	c.setTime(date);
	c.add(Calendar.DAY_OF_MONTH, 7);
  	Date tom= c.getTime();
  	
  	Cart cart=	new Cart();
	cart.set("create_date", date).set("modify_date",date)
		.set("version",  0).set("expire",  tom).set("cart_key",  "1234564123123")
		.set("member_id",  userid).save();
	//添加购物项
	//cartItem.set("create_date", date).set("modify_date",date).set("version",  0).set("quantity", getPara("quantity")).set("cart_id", getPara("cartId")).set("product_id", getPara("productId"));

	}else {
	/**
	 * 存在购物车,直接添加到购物项中	
	 */
   cartItem.set("create_date", date).set("modify_date",date).set("version",  0).set("quantity", getPara("quantity")).set("cart_id", getPara("cartId")).set("product_id", getPara("productId"));
   
	}
  }
}
