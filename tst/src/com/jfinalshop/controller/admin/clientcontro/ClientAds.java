package com.jfinalshop.controller.admin.clientcontro;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.util.TextUtils;

import com.jfinal.core.Controller;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinalshop.model.Admin;
import com.jfinalshop.model.ClientAd;
import com.jfinalshop.service.AdminService;

@ControllerBind(controllerKey="/admin/clientcontro/clientad")
public class ClientAds  extends Controller{
	
			public void index() {
					List<Admin> la=Admin.dao.find("select * from admin");
					List<Record> lr=Db.find("select  ad.*  from client_ad ad,admin "
							+ "a where ad.operation_employee=a.id");
					this.setAttr("operations",la);
					this.setAttr("upper", ClientAd.Upper.values());
					this.setAttr("type", new ClientAd());
					this.setAttr("ads", lr);
					this.renderJsp("/WEB-INF/template/admin/clientmanager/ad/clientad.jsp");
			}
			
			public void search() {
				List<Admin> la=Admin.dao.find("select  *  from admin");
				this.setAttr("operations",la);
				this.setAttr("type", new ClientAd());
					String stype=this.getPara("searchtype");
					String role=this.getPara("operation_role");
					String name=this.getPara("searchname");
					String date=this.getPara("search_date");
					String sql="select   ad.*,a.id,a.username from client_ad ad,admin a where ad.operation_employee=a.id   ";
					if(!TextUtils.isEmpty(stype)&&!stype.equals("-1")) {
						sql+=" and ad.ad_type="+stype+"   ";
					}
					if(!TextUtils.isEmpty(role)&&!role.equals("-1")) {
						sql+="  and  ad.operation_employee="+role+"   ";
					}
					if(!TextUtils.isEmpty(name)) {
						 sql+="  and ad.ad_name like '%"+name+"%'  ";
					}
					if(!TextUtils.isEmpty(date)) {
						 sql+="  and  TO_DAYS("+date+")=TO_DAYS(ad.ad_date)";
					}
					List<Record> lr=Db.find(sql);
					this.setAttr("ads", lr);
					this.renderJsp("/WEB-INF/template/admin/clientmanager/ad/clientad.jsp");
			}
			
			//add the ad
			public void add() {
					File f=this.getFile("adimg").getFile();
					String path=this.getRequest().getServletContext().getRealPath("/");
					String adname=getPara("adname");
					String adpath=getPara("adpath");
					String adtype=getPara("adtype");
					String desc=this.getPara("description");
					int isup=this.getParaToInt("isup");
					String title=getPara("title");
					int status=this.getParaToInt("status");
					String subpath="upload/advertisement/";
					File dir=new File(path+subpath);
					if(!dir.exists()) {
						dir.mkdir();
					}
					File nf=new File(path+subpath+adname+"_"+adtype+f.getName().substring(f.getName().lastIndexOf("\\.")+1));
					f.renameTo(nf);
					f.delete();
					ClientAd ca=new ClientAd();
					ca.setAdPath(adpath);
					ca.setStatus(status);
					ca.setTitle(title==null?"":title);
					ca.setIsup(isup);
					ca.setDetail(desc==null?"":desc);
					ca.setAdName(adname);
					ca.setAdImg(nf.getPath().replace(path, "").replace("\\", "/"));
					ca.setAdType(Integer.parseInt(adtype));
					ca.setOperationEmployee(Integer.parseInt(new AdminService().getCurrent().getId().toString()));
					ca.setAdDate(new Date());
					if(ca.save())this.setAttr("result", 1);
					else this.setAttr("result", 2);
					index();
			}
			//删除
			public void del() {
				int id=this.getParaToInt("id");
				if(ClientAd.dao.deleteById(id)) {
					 this.setAttr("result", 1);
				}else {
					this.setAttr("result", 0);
				}
				index();
			}
}
