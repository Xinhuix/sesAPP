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
import com.jfinalshop.model.UserCollectionGoods;
import com.jfinalshop.model.UserCollectionShop;

@ControllerBind(controllerKey="/UserCollection")
public class UserCollection extends Controller {
	//店铺对象
	 UserCollectionShop addshop = null;
	//产品对象
	 UserCollectionGoods addgoods = null;
	/**
	 * 查询产品收藏
	 * 需要用户的id
	 */
	public void GoodsCollection() {

		String userid=getPara("id");
		String cp =getPara("cp");
		int page =0;
		 if(!TextUtils.isEmpty(cp)) {
			 page=Integer.parseInt(cp);
			 }
		 if(TextUtils.isEmpty(userid)) {
			 this.renderJson("error");
			 return;	
		 }
			Map<String, Object> map=new HashMap<String,Object>();
		 try {
			 UserCollectionGoods Shoppage =UserCollectionGoods.dao.findFirst("select  count(rns.id) FROM \r\n" + 
						"user_collection_goods rns LEFT JOIN\r\n" + 
						"goods gs ON rns.goods_id=gs.id\r\n" + 
						"LEFT JOIN member mr ON rns.member_id=mr.id WHERE mr.id=?",userid);
				Long totalpage=Shoppage.get("count(rns.id)");
				
				List<Record> UserCollectionGoods =Db.find("select mr.id as mrid,rns.id as rnsid,rns.mecreate_date as mecreateDate,\r\n" + 
						"gs.id as gsid,gs.name as name,gs.image as image,gs.price as price,gs.goods_url as goodsUrl\r\n" + 
						"FROM\r\n" + 
						"user_collection_goods rns LEFT JOIN\r\n" + 
						"goods gs ON rns.goods_id=gs.id\r\n" + 
						"LEFT JOIN member mr ON rns.member_id=mr.id WHERE mr.id=? ORDER BY rns.mecreate_date desc limit ?,6",userid,page*6);
				/*if(page*6>totalpage) {
					this.renderJson("没有更多数据啦..");
					return;
				}*/
				if(totalpage==0) {
					this.renderJson("您还没有收藏产品..");
					return;
				}
			
				map.put("goods", UserCollectionGoods);
				map.put("status", 1);
				this.renderJson(map);
		}catch (Exception e) {
				map.put("status", 0);
				this.renderJson(map);
		 }
	}
	
	/**
	 * 收藏产品
	 * 需要用户id和产品id
	 */
	public void AddGoodsCollection() {
		 String goodsId = getPara("goodsId");
		 String memberId= getPara("memberId");
		 if(TextUtils.isEmpty(goodsId)||TextUtils.isEmpty(memberId)) {
			 this.renderJson("error");
			 return;
		 }
		 List<Record> isgoods =Db.find("select * from user_collection_goods where goods_id="+goodsId+"&&member_id="+memberId+"");
		 if(isgoods.size()!=0) {
			 this.renderJson("你已经收藏过此产品了");
			 return;
		 }
		 Map<String, Object> map=new HashMap<String,Object>();
		 addgoods = new UserCollectionGoods();
		 try {
			 addgoods.set("mecreate_date", new Date()).set("goods_id", goodsId).set("member_id",  memberId).save();
			 map.put("status", 1);
			 this.renderJson(map);
		} catch (Exception e) {
			 map.put("status", 0);
			this.renderJson(map);
		}
		
		}
	/**
	 * 删除收藏商品
	 * 需要用户的id
	 */
	public void DeleteCollectionGoods() {
		String list = getPara("id");
		 if(TextUtils.isEmpty(list)) {
			 this.renderJson("error");
			 return;
			 }
		/*for(int i = 0 ; i < list.length; i ++) {
			
			if(i == list.length -1) {
			deleteid+=list[i];
			}
			else {
			deleteid+=list[i]+",";
			}	
		}*/
		Map<String, Object> map= new HashMap<String,Object>();
		try {
			String sql ="delete from user_collection_goods where id in ("+list+")";
			Db.update(sql);	
			map.put("status",1);
			this.renderJson(map);
		} catch (Exception e) {
			map.put("status",0);
			this.renderJson(map);
		}
		
	}
	
	/**
	 * 查询店铺收藏
	 *需要用户的id
	 */
	public void ShopCollection() {
		
		String userid=getPara("id");
		String cp =getPara("cp");
		int page =0;
		 if(!TextUtils.isEmpty(cp)) {
			 page=Integer.parseInt(cp);
			 }
		 if(TextUtils.isEmpty(userid)) {
			 this.renderJson("error");
			 return;
			 }
		 Map<String, Object> map=new HashMap<String,Object>();
		 try {
			 UserCollectionShop Shoppage =UserCollectionShop.dao.findFirst("select  count(rnp.id) FROM \r\n" + 
						"user_collection_shop rnp LEFT JOIN\r\n" + 
						"ses_store se ON rnp.ses_store_id=se.id\r\n" + 
						"LEFT JOIN member mr ON rnp.member_id=mr.id WHERE mr.id=? ",userid);
				Long totalpage=Shoppage.get("count(rnp.id)");
				
				List<Record> UserCollectionShop =Db.find("select rnp.id as rnpid,rnp.mecreate_date as mecreateDate,\r\n" + 
						"										 se.id as seid,se.img as img,se.sname as sname,se.store_discount as sestoreDiscount,se.store_url as storeUrl,\r\n" + 
						"										 mr.id as mrid\r\n" + 
						"FROM\r\n" + 
						"user_collection_shop rnp LEFT JOIN\r\n" + 
						"ses_store se ON rnp.ses_store_id=se.id\r\n" + 
						"LEFT JOIN member mr ON rnp.member_id=mr.id WHERE mr.id=? ORDER BY rnp.mecreate_date desc limit ?,6",userid,page*6);
				
				if(totalpage==0) {
					this.renderJson("您没有收藏店铺..");
					return;
				}
				map.put("shop", UserCollectionShop);
				map.put("status", 1);
				this.renderJson(map);
		} catch (Exception e) {
				map.put("status",0);
				this.renderJson(map);
		}
		
		
		
		
	}
	
	
	/**
	 * 删除收藏店铺
	 * 需要用户的id
	 */
	public void DeleteCollectionShop() {
	
		String list = getPara("id");
		if(TextUtils.isEmpty(list)) {
			this.renderJson("error");
			return;
		}
		Map<String, Object> map= new HashMap<String,Object>();
		try {
			String sql ="delete from user_collection_shop where id in ("+list+")";
			Db.update(sql);
			map.put("status",1);
			this.renderJson(map);
		} catch (Exception e) {
			map.put("status",0);
			this.renderJson(map);
		}
	}
	
	/**
	 * 收藏店铺
	 * 需要用户memberId和产品sesStoreId
	 */
	public void AddShopCollection() {
		
		String sesStoreId=getPara("sesStoreId");
		String memberId =getPara("memberId");
		 if(TextUtils.isEmpty(sesStoreId)||TextUtils.isEmpty(memberId)) {
			 this.renderJson("error");
			 return;
		 }
		 List<Record> isshop =Db.find("select * from user_collection_shop where ses_store_id="+sesStoreId+"&&member_id="+memberId+"");
		 if(isshop.size()!=0) {
			 this.renderJson("你已经收藏过此店铺了");
			 return;
		 }
		addshop = new UserCollectionShop();
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			 addshop.set("mecreate_date", new Date()).set("ses_store_id", getPara("sesStoreId")).set("member_id",  getPara("memberId")).save();
			 map.put("status", 1);
			 this.renderJson(map);
		} catch (Exception e) {
			map.put("status", 0);
			this.renderJson(map);
		}
	}
}
