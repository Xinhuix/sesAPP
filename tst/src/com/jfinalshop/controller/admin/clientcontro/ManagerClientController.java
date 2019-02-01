package com.jfinalshop.controller.admin.clientcontro;

import java.io.File;
import java.sql.Date;
import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinalshop.interceptor.ClientManagerInterceptor;
import com.jfinalshop.model.ClientBoot;
import com.jfinalshop.service.AdminService;

@ControllerBind(controllerKey="/admin/clientmanager")
@Before(ClientManagerInterceptor.class)
public class ManagerClientController extends Controller{
	public void index() {
		
		renderJsp("/WEB-INF/template/admin/clientmanager/main.jsp");
	}
	
	//引导管理
	public void boot() {
		String sql="select  c.*,a.username from client_boot c,admin a where a.id=c.operation_employee ";
		String st=this.getPara("st");
		String sv=this.getPara("sv");
		this.setAttr("sv", sv);
		if(st!=null&&sv!=null) {
					switch(st) {
					case "1":
						sql=sql+" and TO_DAYS(cdate)-TO_DAYS(\""+sv+"\") <=0 ";
						break;
					case "2":
						sql+=" and c.name like \"%"+sv+"%\"";
						break;
					}
		}
		List<Record> lr=Db.find(sql);
		this.setAttr("rs", lr);
		this.renderJsp("/WEB-INF/template/admin/clientmanager/boot.jsp");
	}
	//i添加引导
	public void add() {
		File f=this.getFile("path").getFile();
		String name=this.getPara("boot_name");
		String path=this.getPara("addr");
		ClientBoot cb=new ClientBoot();
		cb.setName(name);
		cb.setPath(path==null?"":path);
		cb.setImg(f.getName());
		cb.setCdate(new Date(System.currentTimeMillis()));
		cb.setState(1);
		cb.setOperationEmployee(Integer.parseInt(new AdminService().getCurrent().getId()+""));
		boolean r=cb.save();
		this.setAttr("result", r==true?"操作成功":"操作失败");
		boot();
	}
		public void del() {
				ClientBoot cb=ClientBoot.dao.findById(this.getPara("id"));
				if(cb!=null) {
					boolean r=cb.delete();
					this.setAttr("result",r==true?"操作成功":"操作失败");
				}
				boot();
		}
}
