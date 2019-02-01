package com.jfinalshop.controller.usercenter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.MapUtils;
import org.apache.http.util.TextUtils;

import com.alibaba.druid.sql.visitor.functions.Isnull;
import com.jfinal.core.Controller;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinalshop.model.UserFootprint;
import com.jfinalshop.service.ThemeService;
@ControllerBind(controllerKey="/UserFootprint")
public class UserFootprints extends Controller{
	/**
	 * 添加用户足迹
	 * 需要用户id
	 * 商品id
	 * 商品url
	 */
	public void AddFotprint() {
		String id =getPara("id");//用户id
		String goodsid = getPara("goodsid");//商品id
		String imgurl = getPara("imgurl");  //商品路径
		if(TextUtils.isEmpty(id)||TextUtils.isEmpty(goodsid)||TextUtils.isEmpty(imgurl)) {
			this.renderJson("error");
			return;
		}
		Map<String, Object> map =new HashMap<>();
		UserFootprint userFootprint = new UserFootprint();
		List<UserFootprint> isnull = userFootprint.dao.find("select id,create_date,goods_id,imgurl,member_id from user_footprint where member_id=?",id+","+goodsid);
		if(isnull.size()==0) {
			try {
				userFootprint.setCreateDate(new Date());
				userFootprint.setGoodsId(Long.valueOf(goodsid));
				userFootprint.setImgurl(imgurl);
				userFootprint.setMemberId(id+","+goodsid);
				userFootprint.setUserid(Integer.valueOf(id));
				userFootprint.save();
				map.put("status", 1);
				this.renderJson(map);
				} catch (Exception e) {
				map.put("status", 0);
				this.renderJson(map);	
				}
		}else {
			try {
				Integer FootprintId = isnull.get(0).get("id");
				userFootprint.dao.findById(FootprintId).set("create_date", new Date()).update();
				map.put("status", 1);
				this.renderJson(map);
			} catch (Exception e) {
				map.put("status", 0);
				this.renderJson(map);
			}
			
		}
	}
	/**
	 * 查询足迹
	 * 3天
	 */
	public void ThreeDays() {
		String id = getPara("id");
		String date = getPara("date"); 
		if(TextUtils.isEmpty(id)||TextUtils.isEmpty(date)) {
			this.renderJson("error");
			return;
		}
		Map<String,Object> map = new HashMap<>();
		try {
			List<UserFootprint> userFootprint = UserFootprint.dao.find("select  id,create_date as createDate, goods_id as goodsId,imgurl from user_footprint where userid=? && DATE_SUB(CURDATE(), INTERVAL "+date+" DAY) <= date(create_date) ORDER BY create_date desc ",id);
			map.put("userFootprint", userFootprint);
			map.put("status", 1);
			this.renderJson(map);
			System.out.println(userFootprint);
		} catch (Exception e) {
			map.put("status", 0);
			this.renderJson(map);
		}
	}
	
	/**
	 * 默认足迹
	 * 
	 */
	public void DefaultFootpritns() {
		String id = getPara("id");
		if(TextUtils.isEmpty(id)) {
			this.renderJson("error");
			return;
		}
		  Map<String, Object> footpritns = new LinkedHashMap<>();
		  
		  Map<String, Object> map = new HashMap<>();
		try {
			List<UserFootprint> userFootprint = UserFootprint.dao.find("select id,create_date as createDate,goods_id as goodsId,imgurl from user_footprint where userid=? ORDER BY create_date desc ",id);
		 //时间转换	
       	  SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
       	  
       	  for(int i =0; i<userFootprint.size();i++) {
       		  String dateString1 = formatter.format(userFootprint.get(i).get("createDate"));
       		 
       		  //判断map的key是否存在
       		  if(footpritns.containsKey(dateString1)) {
       		   List<UserFootprint> value = (List<UserFootprint>) footpritns.get(dateString1);
       		   value.add(userFootprint.get(i));
       		  }else {
       			List<UserFootprint> list = new ArrayList<>();
       			  list.add(userFootprint.get(i));
       			  footpritns.put(dateString1, list);
			}
       	  }
     	/*for(Map.Entry<String , Object> maps :footpritns.entrySet()){
     		System.out.println(maps.getKey());
			ArrayList rtnList = (ArrayList) maps.getValue();
			for (Object object : rtnList) {
				System.out.println(object);
			}
		}*/
			map.put("footpritns", footpritns);
			map.put("status", 1);
			this.renderJson(map);
		} catch (Exception e) {
			map.put("status", 0);
			this.renderJson(footpritns);
		}
	}
	
	/**
	 * 查询足迹
	 * 一个月	
	 */
/*	public void OneMonth() {
		String id = getPara("id");
		if(TextUtils.isEmpty(id)) {
			this.renderJson("error");
			return;
		}
		Map<String,Object> map = new HashMap<>();
		try {
			List<UserFootprint> userFootprint = UserFootprint.dao.find("select goods_id as goodsId,imgurl from user_footprint where userid=? && DATE_SUB(CURDATE(), INTERVAL 30 DAY) <= date(create_date)",id);
			map.put("userFootprint", userFootprint);
			map.put("status", 1);
			this.renderJson(map);
			System.out.println(userFootprint);
		} catch (Exception e) {
			map.put("status", 0);
			this.renderJson(map);
		}
	}
	
	*//**
	 * 查询足迹
	 * 三个月	
	 *//*
	public void ThreeMonth() {
		String id = getPara("id");
		if(TextUtils.isEmpty(id)) {
			this.renderJson("error");
			return;
		}
		Map<String,Object> map = new HashMap<>();
		try {
			List<UserFootprint> userFootprint = UserFootprint.dao.find("select goods_id as goodsId,imgurl from user_footprint where userid=? && DATE_SUB(CURDATE(), INTERVAL 90 DAY) <= date(create_date)",id);
			map.put("userFootprint", userFootprint);
			map.put("status", 1);
			this.renderJson(map);
			System.out.println(userFootprint);
		} catch (Exception e) {
			map.put("status", 0);
			this.renderJson(map);
		}
	}
	*/
	/**
	 * 删除足迹
	 * 需要足迹表id
	 */
	public void DeleteFootprint() {
		String footprintid = getPara("id");
		if(TextUtils.isEmpty(footprintid)) {
			this.renderJson("error");
			return;
		}
		Map<String, Object> map = new HashMap<>();
		try {
			String sql ="delete from user_footprint where id in ("+footprintid+")";
			Db.update(sql);
			map.put("status", 1);
			this.renderJson(map);
		} catch (Exception e) {
			map.put("status", 0);
			this.renderJson(map);
		}
	}
}
