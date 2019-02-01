package com.jfinalshop.controller.admin.clientcontro;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.jfinal.core.Controller;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import com.jfinalshop.model.Level;
import com.jfinalshop.model.Page;
import com.jfinalshop.model.SearchRecord;

@ControllerBind(controllerKey="/admin/clientmanager/query")
public class QueryController  extends Controller {
	
		public void index() throws Exception {
			Page <SearchRecord> p=new Page<>();
			String sql="select sid as id,stext,ssum as sum,search_type as type,img,orders   from search_record";
			String countSql="select count(sid) from search_record";
			List<Record> rs=Db.find(countSql);
			p.init(rs.get(0).getLong("count(sid)").intValue(), "query.jhtml", this.getRequest());
			sql+="  limit ?,?";
			SearchRecord.Type [] typs=SearchRecord.Type.values();
			List<SearchRecord> records=SearchRecord.dao.find(sql,p.getStartIndex(),p.getPageSize());
			p.setData(records);
			Map<Integer,String>  type=new HashMap<>();
			for(SearchRecord.Type t:typs) {
				 type.put(t.getIndex(), t.getName());
			}
			p.getParams().put("type", type);
			p.getParams().put("order", SearchRecord.TopOrder.values());
			//this.setAttr("sr",records);
			 //this.setAttr("searchType", typs);
			 //this.setAttr("type",type );
			 this.setAttr("page", p);
			 this.renderJsp("/WEB-INF/template/admin/clientmanager/query/query.jsp");
		}
		public void add() {
			File f=this.getFile("searchRecord.img").getFile();
			String base=this.getRequest().getServletContext().getRealPath("");
			System.out.println(base);
			String subPath="/upload/ads/";
			File dir=new File(base+subPath);
			if(!dir.exists()) {
				dir.mkdirs();
			}
			String lastFix=f.getName().substring(f.getName().lastIndexOf("."));
			File nf=new File(base+subPath+UUID.randomUUID()+lastFix);
			boolean b=f.renameTo(nf);
			SearchRecord sr=this.getModel(SearchRecord.class);
			sr.setImg(subPath+nf.getName());
			System.err.println(sr.getImg());
			 boolean b2=sr.save();
			 if(b&&b2) {
				 this.renderJson("success");
			 }else {
				 sr.delete();
				 nf.delete();
				 this.renderJson("fail");
			 }
		}
	
		public void del() {
			  Long id=this.getParaToLong("id");
			  boolean b=SearchRecord.dao.deleteById(id);
			  this.renderJson(b?"success":"fail");
		}
		
		public void getSample() {
			String id=this.getPara("id");
			SearchRecord sr=SearchRecord.dao.findById(id);
			this.renderJson(sr!=null&&sr.getStext()!=null?sr:"fail");
		}
		
		public void update() {
			
			List<UploadFile>  files=this.getFiles(); 
			SearchRecord sr=this.getModel(SearchRecord.class);
			if(files!=null&&files.size()>0) {
				String base=this.getRequest().getServletContext().getRealPath("");
						String subPath="/upload/ads/";	
						File f=files.get(0).getFile();
						String  suffix=f.getName().substring(f.getName().indexOf("."));
						File nf=new File(base+subPath+UUID.randomUUID()+suffix);
						boolean c=f.renameTo(nf);
						if(c)sr.setImg(subPath+nf.getName());
				}
			boolean b=sr.update();
			this.renderJson(b?"success":"fail");
		}
		
		public void getTops() {
			SearchRecord.TopOrder [] orders=SearchRecord.TopOrder.values();
			String condition="";
			for(SearchRecord.TopOrder o:orders) {
				  condition+=","+o.ordinal();
			}
		 List<Record> rs=Db.find("select  sid,stext,orders from search_record  where orders>0 and  orders in ("+condition.substring(1)+")");
		 for(SearchRecord.TopOrder o:orders) {
			 boolean b=false;
			  for(Record r:rs) {
				    if(r.getInt("orders")==o.ordinal()) {
				    	b=true;
				    	break;
				    }
			  }
			    if(!b&&o.ordinal()>0) {
			    	Record r=new Record();
			    	r.set("orders",o.ordinal());
			    	rs.add(r);
			    }
		 }
		 this.renderJson(rs);
		}
		
		public void top() {
			String index=this.getPara("index");
			int line=Db.update("update search_record  set orders=0  where orders=?",index);
			String id=this.getPara("id");
			SearchRecord sr=new SearchRecord();
			sr.setSid(Long.valueOf(id));
			sr.setOrders(Integer.parseInt(index));
			boolean b=sr.update();
			  this.renderJson(b?"success":"fail");
		}
}
