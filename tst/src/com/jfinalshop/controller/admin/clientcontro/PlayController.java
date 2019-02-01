package com.jfinalshop.controller.admin.clientcontro;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.util.TextUtils;

import com.alibaba.druid.util.StringUtils;
import com.jfinal.core.Controller;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinalshop.model.Admin;
import com.jfinalshop.model.OtherPlay;
import com.jfinalshop.model.Page;
import com.jfinalshop.service.AdminService;

@ControllerBind(controllerKey="/admin/clientmanager/play")
public class PlayController  extends Controller {
			
	AdminService adminService=this.enhance(AdminService.class);
	
	
		public void index() throws Exception {
			Page<Record> p=new Page<>();
			String title=this.getPara("title");
			String  status=this.getPara("statu");
			String date=this.getPara("cdate");
			String id=this.getPara("operation_id");
			String sql="select o.*,a.username from other_play o,admin a where a.id=o.operation_id   ";
			String countSql="select count(o.id) from other_play o,admin a where a.id=o.operation_id   ";
			if(!StringUtils.isEmpty(title)) {
				sql +="   and title like '%"+title+"%'  ";
				countSql+="  and title like '%"+title+"%' ";
			}
			if(!StringUtils.isEmpty(status)) {
				sql+="  and status="+status;
				countSql+="  and status="+status;
			}
			if(!StringUtils.isEmpty(date)) {
				sql+="    and cdate='"+date+"' ";
				countSql+="  and cdate='"+date+"' ";
			}
			if(!StringUtils.isEmpty(id)&&!id.equals("请选择")) {
				sql+="  and  a.id="+id;
				countSql+="  and a.id="+id;
			}
			System.out.println(sql);
			List<Record> rs=Db.find(countSql);
			p.init(rs.size()>0?rs.get(0).getLong("count(o.id)").intValue():0, "play.jhtml",this.getRequest());
			List<Admin> la=Admin.dao.find("select id,username  from admin");
			sql+="  limit ?,?";
			List<Record> lo=Db.find(sql,p.getStartIndex(),p.getPageSize());
			p.setData(lo);
			p.getParams().put("employees", la);
			p.getParams().put("status",OtherPlay.Statu.values());
//			setAttr("ops",lo);
//			setAttr("employees",la);
			 setAttr("status",OtherPlay.Statu.values());
			 this.setAttr("page", p);
			 this.renderJsp("/WEB-INF/template/admin/clientmanager/otherPlay/otherPlay.jsp");
		}
		
		public void add() {
			 File f=this.getFile("add_icon_adr").getFile();
			  String title=getPara("addtitle");
			  String play_adr=this.getPara("add_play_adr");
			  String status=this.getPara("add_status");
			  String desc=this.getPara("add_description");
			  OtherPlay op=new OtherPlay();
			  op.setTitle(title);
			  op.setPlayAdr(play_adr);
			  op.setStatus(Integer.parseInt(status));
			  op.setDescription(desc);
			  if(f!=null) {
				  String path=this.getRequest().getServletContext().getRealPath("");
				  String subpath="/upload/otherplay/";
				  File dir=new File(path+subpath);
				  if (!dir.exists()) {
					dir.mkdir();
				}
				  String suffix=f.getName().substring(f.getName().lastIndexOf("."));
				  String fn=System.currentTimeMillis()+suffix;
				  File nf=new File(dir+"/"+fn);
				  FileOutputStream fos=null;
				  try {
					  if(!nf.exists()) {
						  nf.createNewFile();
					  }
					  fos=new FileOutputStream(nf);
					  byte b[]=new byte[1024];
					  int len=0;
					  FileInputStream fis=new FileInputStream(f);
					  while((len=fis.read(b))!=-1) {
						  fos.write(b);
					  }
					  fis.close();
				  f.delete();
				  op.setIconAdr(subpath+nf.getName());
				  op.setCdate(new Date());
				  op.setOperationId(adminService.getCurrent().getId().intValue());
				  this.getResponse().setCharacterEncoding("utf-8");
				  if(op.save()) {
					   this.getResponse().getWriter().write(""
					   		+ "<script>window.onload=function(){  alert('添加成功')}</script>;"
					   		+ "");
				  }else {
					  this.getResponse().getWriter().write(""
					  		+ "<script>"
					  		+ " window.onload=function(){	alert('添加失败')}"
					  		+ "</script>");
				  }
				} catch ( IOException e) {
					e.printStackTrace();
				}finally {
					if(fos!=null) {
						try {
							fos.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			  }
		}
		
		public void del() {
			Map<String,String> m=new HashMap<>();
			String id=this.getPara("id");
			if(TextUtils.isEmpty(id)) {
				  m.put("code", 400+"");
				  m.put("msg", "操作错误！");
			}else {
				if(OtherPlay.dao.deleteById(id)) {
					m.put("code", 200+"");
					m.put("msg", "删除成功");
				}else {
					m.put("code", 300+"");
					m.put("msg", "操作失败");
				}
			}
			  this.renderJson("result",m);
		}
}
