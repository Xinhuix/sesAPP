package com.jfinalshop.controller.XhxManagementSystem;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.util.TextUtils;

import com.jfinal.core.Controller;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.upload.UploadFile;
import com.jfinalshop.controller.usercenter.FilesService;
import com.jfinalshop.model.Appversion;
@ControllerBind(controllerKey = "/AppUpdate")
public class AppUpdate extends Controller{
	

	/**
	 * 版本更新
	 */
	public void appupdate() {
		Integer version = getParaToInt("version");
		Map<String, Object> map = new HashMap<>();
		if(version==null) {
			map.put("status", 0);
			map.put("message", "版本号不能为空");
			this.renderJson(map);
			return;
		}
		try {
			Appversion appversion = Appversion.dao.findFirst("SELECT * FROM appversion ORDER BY create_date DESC LIMIT 1");
			if(Double.valueOf(version)<appversion.getVersionnumber()) {
				map.put("status", 1);
				map.put("message", "当前版本可以更新");
				map.put("appversion", appversion);
			}else {
				map.put("status", 0);
				map.put("message", "当前版本无需更新");
			}
			this.renderJson(map);
		} catch (Exception e) {
			map.put("status", 0);
			map.put("message", "更新异常");
			this.renderJson(map);
		}
	}
	
	/**
	 * 上传版本
	 */
	public void addappversion() {
		
			
		Map<String, Object> map = new HashMap<>();
		try {
		UploadFile  updatefile = getFile();
		Integer  version = getParaToInt("version");	
		Integer  type  = getParaToInt("type");
		
		String name = updatefile.getFileName();
		String isapk = name.substring(name.length()-3, name.length());
		if(!isapk.equals("apk")) {
			map.put("status", 0);
			map.put("message", "请上传正确的apk文件");
			this.renderJson(map);
			return;
		}
		if(version==null||type==null) {
			map.put("status", 0);
			map.put("message", "版本号或者类型不能为空");
			this.renderJson(map);
			return;
		}
		FilesService fs= new FilesService();
		File file = updatefile.getFile();
		
		//String imgaeName= UUID.randomUUID().toString()+file.getName().substring(file.getName().lastIndexOf("."));
		
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		String time = format.format(new Date());
		String imgaeName= time+"GXNY"+version+file.getName().substring(file.getName().lastIndexOf("."));
		//相对路径加文件名
		String imagespath="/upload/appupdate/"+imgaeName;
		//获取绝对路径
		String images_path=this.getRequest().getServletContext().getRealPath("/");
		//路径相合
		File t = new File(images_path+imagespath);
		fs.fileChannelCopy(file, t);
		file.delete();
		Appversion updateadd = new Appversion();
		Date date = new Date();
		updateadd.setCreateDate(date);
		updateadd.setModifyDate(date);
		updateadd.setVersionnumber(version);
		updateadd.setUpdateDate(date);
		updateadd.setFileurl(imagespath);
		updateadd.setType(type);
		updateadd.save();
		map.put("status", 1);
		map.put("message", "上传成功");
		this.renderJson(map);
		} catch (Exception e) {
			System.out.println(e);
		map.put("status", 0);
		map.put("message", "上传出现异常");
		this.renderJson(map);
		}
	}
	
	public void addapp() {
			setAttr("version", Appversion.dao.find("select * from appversion  ORDER BY create_date desc"));
			render("/admin/appupdate/app.ftl");
	}
	
	public void deleteaddversion() {
		String id =getPara("id");
		Map<String, Object> map = new HashMap<>();
		if(TextUtils.isBlank(id)) {
			map.put("status", 0);
			map.put("message", "参数不能为空");
			renderJson(map);
		}
		try {
			Db.update("delete from appversion where id=?",id);
			map.put("status", 1);
			map.put("message", "删除成功");
			renderJson(map);
		} catch (Exception e) {
			map.put("status", 0);
			map.put("message", "删除失败");
			renderJson(map);
		}
	
	
	}
}
