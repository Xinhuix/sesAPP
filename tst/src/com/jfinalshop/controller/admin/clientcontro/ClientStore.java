package com.jfinalshop.controller.admin.clientcontro;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.util.TextUtils;

import com.jfinal.core.Controller;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinalshop.dao.StoreTypeDao;
import com.jfinalshop.model.Area;
import com.jfinalshop.model.Page;
import com.jfinalshop.model.SesStore;
import com.jfinalshop.model.StoreType;
import com.jfinalshop.model.Tag;
import com.jfinalshop.model.client.CommonSTUC;

@ControllerBind(controllerKey="/admin/clientcontro/store")
public class ClientStore extends Controller {
				private static   final String NAME_NULL="name_null";
				private static final String FAIL="fail";
				private static final String SUCCESS="success";
				private StoreTypeDao typedao=this.enhance(StoreTypeDao.class);
				
	public void index() throws Exception {
				Page<Record> rs=new Page<>();
			   rs.fullMember(this.getRequest());
				List<StoreType> ls=StoreType.dao.find("select   *  from store_type");
				String countSql="select count(s.id) from ses_store s,store_type t where s.type_id=t.id  ";
				List<Record> count=Db.find(countSql);
				List<Record> lr=Db.find("select  s.*,t.tname from  ses_store s,store_type t where s.type_id=t.id    limit ?,?",rs.getStartIndex(),rs.getPageSize());
				List<Area> area=Area.dao.find("select id,name from area where parent_id is null ");
				List<Tag> tags=Tag.dao.find("select id,name from tag where  type=? ",Tag.Types.store.ordinal());
				rs.init(count.get(0).getLong("count(s.id)").intValue(), "index.jhtml", this.getRequest());
				rs.setData(lr);
				SesStore.Status [] ss=SesStore.Status.values();
				rs.getParams().put("status", ss);
				rs.setUrl("javascript:list");
				this.setAttr("ts", ls.size()>0?ls:null);
				this.setAttr("page",rs);
				this.setAttr("area",area);
				this.setAttr("tag", tags);
				this.renderJsp("/WEB-INF/template/admin/clientmanager/store/store.jsp");
	}
	 //查找
	public void search() throws Exception {
			Page<Record> rs=new Page<>();
			//rs.fullMember(this.getRequest());
			String sql="select s.*,t.tname from ses_store s,store_type t where s.type_id=t.id  ";
			String countSql="select count(s.id) from ses_store s,store_type t where s.type_id=t.id";
			String type=this.getPara("searchType");
			String subCondition="";
			if(!TextUtils.isEmpty(type)&&type.equals("2")) {
				String name=this.getPara("searchName");
				subCondition+=TextUtils.isEmpty(name)?"":" and  sname like \"%"+name+"%\" ";
				String start=this.getPara("searchStartDate");
				subCondition+=TextUtils.isEmpty(start)?"":"  and   to_days(s.joindate)-to_days(\""+start+"\")>=0  ";
				String end=this.getPara("searchEndDate");
				subCondition+=TextUtils.isEmpty(end)?"":" and  to_days(s.joindate)-to_days(\""+end+"\")<=0";
			}
			sql+=subCondition;
			countSql+=subCondition;
			sql+="  limit ?,?";
			List<Record> count=Db.find(countSql);
			rs.init(count.get(0).getLong("count(s.id)").intValue(),"javascript:list", this.getRequest());
			rs.setUrl("javascript:list");
			SesStore.Status [] ss=SesStore.Status.values();
			List<String> names=new ArrayList<>();
			for(SesStore.Status s:ss) {
				names.add(s.getName());
			}
			rs.getParams().put("status", names);
			List<Record> stores=Db.find(sql,rs.getStartIndex(),rs.getPageSize());
			rs.setData(stores);
			this.renderJson(rs);
	}
	public void getSampleForId() {
		Map<String,Object> result=new HashMap<>();
		String id=this.getPara("id");
		if(TextUtils.isEmpty(id)) {
			result.put("status", CommonSTUC.Status.LackPara.getIndex());
		}else {
			List<Record>  rs=Db.find("select  s.*,t.tname from ses_store s,store_type t where s.type_id=t.id  and  s.id=? ",id);
			if(rs!=null&&rs.size()>0) {
				  result.put("status", CommonSTUC.Status.Success.getIndex());
				  result.put("record", rs.get(0));
			}else {
				result.put("status",CommonSTUC.Status.NoExist.getIndex());
			}
		}
		
		this.renderJson(result);
	}
	public void update() {
	SesStore ss=this.getModel(SesStore.class);
//		for(Entry<String, String[]> e:this.getRequest().getParameterMap().entrySet()) {
//					ss.set(e.getKey(), e.getValue());
//		}
		System.out.println(ss.getId());
		this.renderJson(ss.update()?1:2);
	}
	public void delStore() {
		 SesStore ss=this.getModel(SesStore.class);
		this.renderJson(ss.delete()?1:2);
		
	}
	public void addType() {
		  Map<String,Object> mp=new HashMap<>();
			 String tname=this.getPara("tn");
			 String des=this.getPara("tdes");
			 if(TextUtils.isEmpty(tname)) {
				 mp.put("result", NAME_NULL);
			 }else {
				    StoreType st=typedao.add(tname, des);
				   
				    if(st!=null) {
				    	mp.put("result", st);
				    }else {
				    	mp.put("result", FAIL);
				    }
			 }
			
			this.renderJson(mp);
	}
	public void delType() {
		String id=this.getPara("id");
		System.out.println(id);
		boolean b=typedao.delById(id);
		Map<String,String> m=new HashMap();
		if(b) 
			m.put("result", SUCCESS);
		else
		m.put("result",FAIL );
		this.renderJson(m);
	}
	
	//添加店铺--后台
	public void addStore() {
		String msg="添加失败";
		try {
			SesStore store=getModel(SesStore.class);
			String sname=this.getPara("sname");
			String detail=this.getPara("detail");
			String type=this.getPara("storeType");
			String score=this.getPara("score");
			String play_url=this.getPara("play_url");
			String area_id=this.getPara("area_id");
			String detail_addr=this.getPara("detail_addr");
			String tg=this.getPara("tg");
			String  owner=this.getPara("owner");
			String img=this.getPara("img");
			String images=this.getPara("images");
			store.setSname(sname);
			store.setDetail(detail);
			store.setJoindate(new Date());
			store.setAddr(detail_addr);
			store.setTypeId(Integer.parseInt(type));
			if(score.matches("^\\d\\[\\.]*\\d$")) {
				   store.setScore(new BigDecimal(score));
			}else {
				   store.setScore(BigDecimal.valueOf(5));
			}
		store.setPrice(this.getPara("price").matches("^\\d[\\.]*\\d$")?Double.valueOf(this.getPara("price")):0);
			store.setPlayUrl(play_url);
			store.setAreaId(Long.parseLong(area_id));
			store.setTg(tg);
			store.setOwner(owner);
			store.setImg(img);
			store.setImages(images);
			store.setTgId(Long.valueOf(this.getPara("field")));
		    boolean b=store.save();
		    if(b) {
		    	msg="添加成功";
		    }
		}catch(Exception e) {
			e.printStackTrace();
			msg="未知错误";
		}finally {
			this.renderJson("result",msg);
		}
	}
	
	public void upload() {
		Map<String,Object> result=new HashMap<String,Object>();
		FileInputStream  is;
		FileOutputStream os;
		try {
			  File f=this.getFile("img").getFile();
			  if(f==null) {
				 this.renderJson("result","fail");
			  }else {
				  is=new FileInputStream(f);
				  byte []b=new byte[1024];
				  int len=-1;
				  String base=this.getRequest().getServletContext().getRealPath("");
				  String subpath="/upload/store/merchant";
				  File dir=new File(base+subpath);
				  String suffix=f.getName().substring(f.getName().indexOf("."));
				  if(!dir.exists())dir.mkdir();
				  File nf=new File(dir.getPath()+"/store_"+System.currentTimeMillis()+suffix);
				  os=new FileOutputStream(nf);
				  while((len=is.read(b))!=-1) {
					   os.write(b);
				  }
				  is.close();
				  os.close();
				  f.delete();
				  f.length();
				  subpath=nf.getPath().replace(base, "");
				  this.renderJson("result",subpath);
			  }
		}catch(Exception e) {
			e.printStackTrace();
			this.renderJson("result","error");
		}
	}
	
	public void delImg() {
		  String filename=this.getPara("fn");
		  boolean isNull=TextUtils.isEmpty(filename);
		  File f=new File(this.getRequest().getServletContext().getRealPath("/")+filename);
		  boolean  del=f.delete();
		  this.renderJson("result",del);
	}
}
