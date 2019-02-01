package com.jfinalshop.controller.XhxManagementSystem;



import java.util.List;

import org.apache.http.util.TextUtils;

import com.jfinal.ext.route.ControllerBind;

import com.jfinalshop.controller.admin.BaseController;
import com.jfinalshop.model.Member;
import com.jfinalshop.model.UserFootprint;


@ControllerBind(controllerKey = "Footprint")
public class Footprint extends BaseController {
	
	/**
	 * 排行榜
	 */
	public void view() {
		String date = getPara("date");
		if(TextUtils.isEmpty(date)) {
		render("/admin/xxh_test/barsimple.html");
		return;
		}
		List<UserFootprint> userFootprint = UserFootprint.dao.find("select uft.userid,mr.username,count(*) as count from user_footprint uft LEFT JOIN  member mr on uft.userid=mr.id WHERE DATE_SUB(CURDATE(), INTERVAL "+date+" DAY) <= date(uft.create_date) group by uft.userid ORDER BY count desc ");
		setAttr("member", Member.dao.findById(18));
		renderJson(userFootprint);
	}
}
