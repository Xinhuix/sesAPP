package com.jfinalshop.controller.usercenter;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.ocpsoft.prettytime.units.Day;

import com.jfinal.core.Controller;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import com.jfinalshop.FileType;
import com.jfinalshop.Message;
import com.jfinalshop.model.Area;
import com.jfinalshop.model.Goods;
import com.jfinalshop.model.Member;
@ControllerBind(controllerKey="/UserData")
public class UserData extends Controller {
	/**
	 * 个人资料
	 * 需要用户的id
	 */
	public void ViewUserData() {
		String userid=getPara("id");
		if(userid==null) {
			this.renderJson("用户的id不能为空");
			return;
		}
		Member member = Member.dao.findFirst("select mr.id as mrid,mr.`name`,mr.mobile,mr.gender,mr.email,mr.portrait,\r\n" + 
				"aa.id as aaid,aa.full_name as fullName\r\n" + 
				"FROM\r\n" + 
				"member mr LEFT JOIN\r\n" + 
				"area aa ON mr.area_id=aa.id WHERE mr.id="+userid+" ");
		this.renderJson(member);
	}
	
	/**
	 * 完善个人资料
	 */
	 public void UpdateData() {
		Member member= new Member();
		member.dao.findById(getPara("id")).set("modify_date", new Date()).set("name", getPara("name","UTF-8")).set("mobile", getPara("mobile")).set("gender", getPara("gender")).set("area_id",getPara("areaId")).update();
	 }
	 
	 /**
	  *  修改头像
	  */
	 public void upload() {
		UploadFile uploadFile= this.getFile(); //获取前台上传文件对象
		
		System.out.println();
		
		
		String fileName= uploadFile.getOriginalFileName();//获取文件名
		System.out.println(fileName);
	
		File file=uploadFile.getFile();//获取文件对象
	    
		FilesService fs= new FilesService();
		File t = new File("G:\\upla\\"+UUID.randomUUID().toString()+file.getName().substring(file.getName().lastIndexOf(".")));
		System.out.println("这是t:"+t);
		try {
			t.createNewFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
		fs.fileChannelCopy(file, t);
		file.delete();
	
		this.renderHtml("success,<a href=\"./\">back</a>");
	 }
}
