package com.jfinalshop.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.ICallback;
import com.jfinalshop.model.SesStore;
import com.jfinalshop.model.StoreType;

public class StoreTypeDao {

	public  StoreType  add(String tname,String des) {
		  StoreType st=new StoreType();
		  st.setTname(tname);
		  st.setDetail(des);
		  st.setCdate(new Date());
		  boolean b=st.save();
		  return b?st:null;
	}
	public boolean delById(String id) {
		try {
			List<SesStore> lss=SesStore.dao.find("select *  from ses_store where type_id="+id);
			for(SesStore ss:lss) {
				  ss.deleteById(ss.getId());
			}
			 return  StoreType.dao.deleteById(id);
		}catch(Exception e) {
			   e.printStackTrace();
		}
			return false;
	}
	
	public   List<StoreType>  findAll(){
		 List<StoreType> st=null;
			  st=StoreType.dao.find("select  *  from store_type");
	
		return  st!=null&&st.size()>0?st:null;
	}
}
