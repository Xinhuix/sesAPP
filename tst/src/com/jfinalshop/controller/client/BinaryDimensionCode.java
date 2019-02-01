package com.jfinalshop.controller.client;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.TextUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.jfinal.core.Controller;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinalshop.model.Member;
import com.jfinalshop.model.client.CommonSTUC;

@ControllerBind(controllerKey="client/binaryDimension")
public class BinaryDimensionCode  extends Controller {
	   private ConcurrentHashMap<String,Object> ccm=new ConcurrentHashMap<>();
			//支付初始化
		    public   void payCode() {
		    	Map<String,Object> result=new HashMap<>();
		    	try {
		    		 String loginCode=this.getPara("loginCode");
		    		 if(TextUtils.isEmpty(loginCode)) {
		    			   result.put("status", CommonSTUC.Status.LackPara.getIndex());
		    		 }
		    		 List<Member> ms=Member.dao.find("select id,balance,point,username,nickname,portrait  from member where  logincode=?   ",loginCode);
		    		 if(ms==null||ms.size()<=0) {
		    			 result.put("status", CommonSTUC.Status.NoExist.getIndex());
		    		 }else {
		    			 result.put("status",CommonSTUC.Status.Success.getIndex() );
		    			 Member m=ms.get(0);
		    			m.setNickname(TextUtils.isEmpty(m.getNickname())?m.getUsername():m.getNickname());
		    			m.setPortrait(TextUtils.isEmpty(m.getPortrait())?"":m.getPortrait());
		    			 result.put("data",ms.get(0));
		    			 m.update();
		    		 }
		    	}catch(Exception e) {
		    		e.printStackTrace();
		    		result.put("status", CommonSTUC.Status.Error.getIndex());
		    	}finally {
		    		  this.renderJson(result);
		    	}
		    }
		    
		    //推荐
		    public void  recommendCode() {
		    	   Map<String,Object> result=new HashMap<>();
		    	   try {
		    		   String loginCode=this.getPara("loginCode");
		    		   if(TextUtils.isEmpty(loginCode)) {
		    			   result.put("status", CommonSTUC.Status.LackPara.getIndex());
		    			   return;
		    		   }
		    		   List<Record> ms=Db.find("select id,create_date,balance,point,username,nickname,portrait  from member where  logincode=? ",loginCode);
		    		   if(ms==null||ms.size()<=0) {
		    			     result.put("status", CommonSTUC.Status.NoExist.getIndex());
		    		   }else {
		    			   Record m=ms.get(0);
		    			   System.out.println(m.getDate("create_date"));
		    			   m.set("nickname",TextUtils.isEmpty(m.getStr("nickname"))?m.get("username"):m.get("nickname"));
		    			   m.set("portrait", TextUtils.isEmpty(m.getStr("portrait"))?"":m.get("portrait"));
		    			   result.put("data", m);
		    			   result.put("status", CommonSTUC.Status.Success.getIndex());
		    			   result.put("url", "http://localhost/client/binaryDimension/share.jhtml?id="+m.getLong("id"));
		    		   }
		    	   }catch(Exception e) {
		    		   e.printStackTrace();
		    		     result.put("status", CommonSTUC.Status.Error.getIndex());
		    	   }finally {
		    		    this.renderJson(result);
		    	   }
		    }
		    public void register() {
		    	ccm.put(this.getRequest().getRemoteAddr(),this.getPara("id")==null?"0":this.getPara("id"));
		    	   this.renderJsp("/WEB-INF/template/client/register/register.html");
		    }
		    public void share() throws IOException {
		    	Map<String,Object> result=new HashMap<>();
		    	try {
		    		String lc=this.getPara("loginCode");
		    		if(StringUtils.isEmpty(lc)) {
		    			result.put("status", CommonSTUC.Status.LackPara.getIndex());
		    			return;
		    		}
		    		List<Member> ms=Member.dao.find("select id from member where logincode=?",lc);
		    		if(ms.size()<=0) {
		    			result.put("status",CommonSTUC.Status.NoExist.getIndex());
		    			return;
		    		}
		    		 SAXReader reader=new SAXReader();
		    		  String base =this.getRequest().getServletContext().getRealPath("/");
		    		  File f=new File(base+"WEB-INF/classes/allocateSetting.xml");
		    		if(!f.exists()) {
		    			result.put("status", CommonSTUC.Status.Fail.getIndex());
		    			return;
		    		}else {
		    			Document doc=reader.read(f);
		    			Element rec=doc.getRootElement().element("recommend");
		    			if (rec!=null) {
							 String title=rec.elementText("title");
							 String img=rec.elementText("img");
							 String introduce=rec.elementText("introduce");
							 result.put("title",title);
							 result.put("img", img);
							 result.put("introduce",introduce); 
							 result.put("url","http://localhost/client/binaryDimension/share.jhtml?id="+ms.get(0).getId() );
						}else {
							result.put("status", CommonSTUC.Status.Fail.getIndex());
						}
		    		}
		    	}catch(Exception e) {
		    		e.printStackTrace();
		    		result.put("status", CommonSTUC.Status.Error.getIndex());
		    	}finally {
		    		this.renderJson(result);
		    	}
		    		 
		    }
		    
}
