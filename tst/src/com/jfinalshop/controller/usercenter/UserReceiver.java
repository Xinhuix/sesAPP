package com.jfinalshop.controller.usercenter;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.util.TextUtils;

import com.jfinal.core.Controller;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinalshop.model.Area;
import com.jfinalshop.model.Receiver;


@ControllerBind(controllerKey="/UserReceiver")
public class UserReceiver extends Controller {
	
	
	
	 Receiver  addresss= null;
	/**
	 * 查看所有用户的地址
	 * 需要用户的id
	 */
	public void Useraddress() {
		String userid = getPara("id");
		if(TextUtils.isEmpty(userid)) {
			this.renderJson("error");
			return;
		}
		Map<String,Object> map = new HashMap<String,Object>();
		try {
		List<Receiver> address = Receiver.dao.find("select mr.id as mrid,\r\n" + 
				"	     rr.id as id,rr.phone as phone,rr.consignee as consignee,rr.is_default as isDefault,rr.area_name as areaName,rr.address as address,rr.area_id as areaId\r\n" + 
				"FROM\r\n" + 
				"receiver rr LEFT JOIN \r\n" + 
				"member mr ON rr.member_id=mr.id WHERE mr.id=?",userid);
		map.put("address", address);
		map.put("status", 1);
		this.renderJson(map);
		} catch (Exception e) {
		map.put("status", 0);
		this.renderJson(map);
		}
	}
	
	/**
	 * 添加用户地址
	 * 
	 */
	public void AddUseraddress() {
		String address = getPara("address");
		String consignee = getPara("consignee");
		String phone = getPara("phone");
		String areaId = getPara("areaId");
		String memberId = getPara("memberId");
		String isdefault = getPara("isdefault");
		if(TextUtils.isEmpty(address)||TextUtils.isEmpty(consignee)||TextUtils.isEmpty(phone)||TextUtils.isEmpty(areaId)||TextUtils.isEmpty(memberId)) {
			this.renderJson("error");
			return;
		}
		addresss  = new Receiver();
		Map<String,Object> map = new HashMap<String,Object>();
		Area area=Area.dao.findById(getPara("areaId"));
		System.out.println(area);
		try {
		Date date = new Date();	
		addresss.setCreateDate(date);
		addresss.setModifyDate(date);
		addresss.setVersion(0L);
		addresss.setAddress(address);
		addresss.setAreaName(area.getFullName());
		addresss.setConsignee(consignee);
		if(TextUtils.isEmpty(isdefault)) {
			addresss.setIsDefault(false);
		}else {
			addresss.setIsDefault(true);
		}
		addresss.setPhone(phone);
		addresss.setAreaId(Long.valueOf(areaId));
		addresss.setMemberId(Long.valueOf(memberId));
		addresss.save();
		map.put("status", 1);
		this.renderJson(map);
		} catch (Exception e) {
			System.out.println(e);
		map.put("status", 0);	
		this.renderJson(map);
		}
}
	
	/**
	 * 删除地址
	 */
	public void DeleteAddress() {
		String addressid =getPara("id");
		if(TextUtils.isEmpty(addressid)) {
			this.renderJson("error");
			return;
		}
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			String sql ="delete from receiver where id in ("+addressid+")";
			Db.update(sql);
			map.put("status",1);
			this.renderJson(map);
		} catch (Exception e) {
			map.put("status",0);
			this.renderJson(map);
		}
		
	}
	
	/**
	 * 修改地址
	 */
	public void UpdateAddress() {
		addresss  = new Receiver();
		String id = getPara("id");
		if(TextUtils.isEmpty(id)) {
			this.renderJson("error");
			return;
		}
		Map<String,Object> map = new HashMap<String,Object>();
		try {
		Receiver cc= addresss.dao.findById(id);
		String consignee = getPara("consignee");
		String phone= getPara("phone");
		String areaName = getPara("areaName");
		String address = getPara("address");
		
		String getConsignee=cc.getConsignee();
		if(consignee!=null) {
			getConsignee=consignee;
		}
		String getPhone=cc.getPhone();
		if(phone!=null) {
			getPhone=phone;
		}
		String getAreaName=cc.getAreaName();
		
		if(areaName!=null) {
		Area  area = Area.dao.findById(areaName);
			getAreaName=area.getFullName();
		}else {
			areaName = String.valueOf(cc.getAreaId());
		}
		
		String getAddress = cc.getAddress();
		if(address!=null) {
			getAddress=address;
		}
		
		Long ve=cc.getVersion()+1;

		addresss.dao.findById(id).set("consignee", getConsignee).set("version", ve+1).set("phone", getPhone).set("area_name", getAreaName).set("address",getAddress).set("area_id", areaName).update();
		map.put("status",1);
		this.renderJson(map);
		} catch (Exception e) {
		map.put("status",0);
		this.renderJson(map);
		}
		}
	/**
	 * 设置默认地址
	 */
	public void DefaultAddress() {
		//获取地址id
		String receiverid = getPara("id");
		String memberid = getPara("userid");
		if(TextUtils.isEmpty(receiverid)||TextUtils.isEmpty(memberid)) {
			this.renderJson("error");
			return;
		}
		Map<String, Object> map = new HashMap<>();
		try {
		List<Receiver> updatereceiver = Receiver.dao.find("select * from receiver where is_default=1");	
		
		if(updatereceiver.size()!=0) {
			for (Receiver receiver : updatereceiver) {
				Db.update("UPDATE receiver SET is_default =0 WHERE id in(?) ",receiver.getId());
			}
		}
			
		Receiver receiver= Receiver.dao.findById(receiverid);	
			
		Receiver.dao.findById(receiverid).set("modify_date", new Date()).set("is_default", 1).set("version", receiver.getVersion()+1).update();
		map.put("status", 1);
		this.renderJson(map);
		} catch (Exception e) {
		map.put("status", 0);
		this.renderJson(map);
		}
	}
	
}
