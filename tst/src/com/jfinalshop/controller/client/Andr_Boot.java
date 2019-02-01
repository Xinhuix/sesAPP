package com.jfinalshop.controller.client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.util.TextUtils;

import com.jfinal.core.Controller;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinalshop.Order;
import com.jfinalshop.model.AdPosition;
import com.jfinalshop.model.Area;
import com.jfinalshop.model.Article;
import com.jfinalshop.model.ClientAd;
import com.jfinalshop.model.ClientBoot;
import com.jfinalshop.model.Enshrine;
import com.jfinalshop.model.Goods;
import com.jfinalshop.model.SearchRecord;
import com.jfinalshop.model.SearchRecord.Type;
import com.jfinalshop.model.SesStore;
import com.jfinalshop.model.Tag;
import com.jfinalshop.model.client.CommonSTUC;
import com.jfinalshop.model.client.MainObj;
import com.jfinalshop.service.AdPositionService;
import com.jfinalshop.service.GoodsService;
@ControllerBind(controllerKey="/client")
public class Andr_Boot extends Controller {
	  static String NOTMORE="notmore";
	  static String ERROR="ERROR";
		GoodsService gs=this.enhance(GoodsService.class);
		
		private AdPositionService adPositionService = enhance(AdPositionService.class);
		public void boot() {
			List<ClientBoot> lb=ClientBoot.dao.find("select  * from  client_boot");
			//this.renderJson(lb);
			this.renderJson(lb);
		}
		
		public void Main() {
			Map<String,Object> result=new HashMap<String,Object>();
			try {
					List<Order> orders = new ArrayList<Order>();
					List<ClientAd> ads=ClientAd.dao.find("select   id,title,ad_img as img,ad_path as url from  client_ad where status=? and  ad_type="+ClientAd.Type.mainbanner.getIndex(),ClientAd.Status.Use.ordinal());
					List<Article> la=Article.dao.find("select id,title from article order by is_top desc   limit 0,20");
					//AdPosition adPosition = adPositionService.find(1L);
					//现货：
					List<Goods> goodsList = gs.clientFindList(Goods.Type.general, null,
							null, null, null, null, null, null, true, true, null, null,
							null, null, null, 6, null, orders, false,"0");
					//定制：
					List<Goods> goodsList_dz = gs.clientFindList(Goods.Type.general, null,
							null, null, null, null, null, null, true, true, null, null,
							null, null, null,6, null, orders, false,"1");
					//推荐：
					Long tagId = new Long((long)3);
					List<Goods> goodsList_tag = gs.clientFindList(Goods.Type.general, null,
							null, null, tagId, null, null, null, true, true, null, null,
							null, null, null, 6, null, orders, false,null);
					result.put("existGoods", goodsList);
					result.put("rollAdvertisement",ads );
					result.put("msCenter",la );
					result.put("custGoods",goodsList_dz);
					result.put("recommendGoods",goodsList_tag );
					List<Tag> tag=Tag.dao.find("select id from tag where type=?",Tag.Types.store.ordinal());
					StringBuilder sb=new StringBuilder();
					for(Tag t:tag) {
						sb.append(","+t.getId());
					}
					String tagid=sb.substring(1,sb.length());
					List<SesStore> rd=SesStore.dao.find("select   id,sname,img,score,tg as  shortDescription    from   ses_store   where tg_id in("+tagid+")   limit 0,3 ");
					result.put("recommendStore",rd );
					result.put("status",CommonSTUC.Status.Success.getIndex());
			}catch(Exception e) {
				e.printStackTrace();
				result.put("status", CommonSTUC.Status.Error.getIndex());
			}finally {
				this.renderJson(result);
			}
		}
		
		//首页信息
		public void main() {
			
			List<Order> orders = new ArrayList<Order>();
			List<ClientAd> ads=ClientAd.dao.find("select   id,title,ad_img,ad_path from  client_ad where status=1 and  ad_type="+ClientAd.Type.mainbanner.getIndex());
			List<Article> la=Article.dao.find("select id,title from article order by is_top desc");
			AdPosition adPosition = adPositionService.find(1L);
			//现货：
			List<Goods> goodsList = gs.clientFindList(Goods.Type.general, null,
					null, null, null, null, null, null, true, true, null, null,
					null, null, null, 6, null, orders, false,"0");
			//定制：
			List<Goods> goodsList_dz = gs.clientFindList(Goods.Type.general, null,
					null, null, null, null, null, null, true, true, null, null,
					null, null, null,6, null, orders, false,"1");
			//推荐：
			Long tagId = new Long((long)3);
			List<Goods> goodsList_tag = gs.clientFindList(Goods.Type.general, null,
					null, null, tagId, null, null, null, true, true, null, null,
					null, null, null, 6, null, orders, false,null);
			List<Tag> tag=Tag.dao.find("select id from tag where type=?",Tag.Types.store.ordinal());
			List<Record> rd=Db.find("select s.*,t.*  from  ses_store s ,store_type t  where s.type_id=t.id  limit 0,3");
			MainObj mo=new MainObj();
			mo.setDz(goodsList_dz);
			mo.setStores(rd);
			mo.setTj(goodsList_tag);
			mo.setXh(goodsList);
			mo.setAds(ads);
			mo.setLa(la);
			this.renderJson(mo);
		}
		
		public void searchInit() {
			Map<String,Object> result=new HashMap<String,Object>();
			     try {
			    	  List<SearchRecord> le=SearchRecord.dao.find("select  stext,img   from    search_record  where  ssum=-1 ");
				       int len=6-le.size();
			    	  List<SearchRecord> records=SearchRecord.dao.find("select stext,img from search_record   order by ssum desc   limit 0,"+len);
			    	  le.addAll(records);
			    	  for(SearchRecord sr:le) {
			    		 if(TextUtils.isEmpty(sr.getImg())) {
			    			     List<Goods> goods=Goods.dao.find("select image from goods where  name  like  '%"+sr.getStext()+"%'  or   keyword like '%"+sr.getStext()+"%'  group by id   limit 0,1");
			    			     if(goods.size()>0) sr.setImg(goods.get(0).getImage());
			    		 }
			    	  }
			    	  List<Object> objs=new ArrayList<>();
			    	  for(SearchRecord.Type t:SearchRecord.Type.values()) {
			    		  Map<String,Object> so=new HashMap<String,Object>();
			    		  so.put("searchTitle",t.getName() );
			    		  so.put("index", t.getIndex());
			    		  objs.add(so);
			    	}
			    	  result.put("rangeType", objs);
			    	  result.put("hotRange", le);
			    	  result.put("status",le.size()>0?CommonSTUC.Status.Success.getIndex():CommonSTUC.Status.Fail.getIndex() );
			     }catch(Exception e) {
			    	 e.printStackTrace();
			    	 result.put("status",CommonSTUC.Status.Error.getIndex());
			     }finally {
			    	 this.renderJson(result);
			     }
		}
		
		public void globalSearch() {
			   Map<String,Object> result=new HashMap<String,Object>();
					      String index =this.getPara("index");  
					      String  clause=this.getPara("clause");
					      String  type=this.getPara("type");
					      String category=this.getPara("category");
					      String  priceGap=this.getPara("priceGap");
					      String  area=this.getPara("area");
					     try {
					    	 if(TextUtils.isEmpty(index)||TextUtils.isEmpty(clause)) {
					    		 result.put("status",CommonSTUC.Status.LackPara.getIndex() );
					    		 return;
					    	 }
					    	 Goods g;
					    	 String sql=null;
					    	 String scategory=null;
					    	 switch(index) {
					    	 case "1":
					    		 sql="select   g.id,g.name,g.image,g.price,g.onlineurl  from goods g  inner  "
					    		 		+ "  join  product_category c on c.id=g.product_category_id  and  ( g.keyword like  '%"+clause+"%'   or   g.name like '%"+clause+"%')   " ;
					    		 scategory="select   id,name from product_category  where id<>243 and id<>244 ";
					    		 Map<String,String>  m=new HashMap<>();
					    		 GoodsType gt=new GoodsType(0,"现货");
					    		 GoodsType gt1=new GoodsType(1,"定制");
					    		 List<GoodsType> types=new ArrayList<>();
					    		 types.add(gt1);
					    		 types.add(gt);
					    		 result.put("type", types);
					    		  sql+=TextUtils.isEmpty(category)?"    ":"  and c.id="+category;
					    		  sql+=TextUtils.isEmpty(type)?"":"  and  g.is_dz="+type;
					    		  if(!TextUtils.isEmpty("area")) {
					    			      sql+="  and  origin like '"+area+"' ";
					    		  }
					    		  if(!TextUtils.isEmpty(priceGap)) {
					    			     String price []=priceGap.split("-");
					    			     if(price.length==2) {
					    			    	 sql+=price[0].matches("\\d*")?"  and  g.price>="+price[0]:" ";
					    			    	 sql+=price[1].matches("\\d*")?"  and g.price <="+price[1]:" ";
					    			     }
					    		  }
					    		 break;
					    	 case "0":
					    		 sql="select s.sname as store_name,s.price,s.img,s.play_url as url from ses_store  s  inner join store_type t on s.type_id=t.id"
					    		 		+ "    and   sname like '%"+clause+"%'   ";
					    		 	scategory="select id,tname as name from store_type";
					    		 	sql+=TextUtils.isEmpty(category)?"":"   and t.id="+category;
					    		    if(!TextUtils.isEmpty(priceGap)) {
					    		    	  String price[]=priceGap.split("-");
					    		    	  if(price.length==2) {
					    		    		    sql+=price[0].matches("\\d*")?"  s.price>="+price[0]:"";
					    		    		    sql+=price[1].matches("\\d*")?"  s.price<="+price[1]:"";
					    		    	  }
					    		    }
					    		    if(!TextUtils.isEmpty(area)) {
					    		    	List<Area> areas=Area.dao.find("select  id from   area where name like '%"+area+"%'");
					    		    	String ids=null;
					    		    	if(areas.size()>0) {
					    		    		for(Area a:areas) {
					    		    			ids+=","+a.getId();
					    		    		}
					    		    	}
					    		    	sql+=TextUtils.isEmpty(ids)?"":"  and  s.area_id in ("+ids+") ";
					    		    }
					    		 break;
					    	 case "2":
					    		 sql="select id,author,title from article   where  "
					    		 		+ "      seo_keywords like  '%"+clause+"%'   or title  like  '%"+clause+"%'     ";
					    		 scategory="select id,name from article_category";
					    		 sql+=TextUtils.isEmpty(category)?"":"    and      article_category_id="+category;
					    		 break;
					    	 }
					    	 System.out.println(sql);
					    	 List<Record> records=Db.find(sql);
					    	 result.put("data", records);
					    	 result.put("status", CommonSTUC.Status.Success.getIndex());
					    	 result.put("category",Db.find(scategory));
					    	
					     }catch(Exception e) {
					    	 e.printStackTrace();
					    	 result.put("status",CommonSTUC.Status.Fail.getIndex() );
					     }finally {
					    	 this.renderJson(result);
					     }
		}
		
		public void rangeMatch() {
			  Map<String,Object> result=new HashMap<>();
			 	String index=this.getPara("index");
			  	String clause=this.getPara("clause");
			  	int lacklen=-1;
			  	List<Record> rangeSet=null;
			  	Record r=null;
			  try {
				  	if(TextUtils.isEmpty(index)||TextUtils.isEmpty(clause)) {
				  		result.put("status",CommonSTUC.Status.LackPara.getIndex());
				  		return;
				  	}
				  	SearchRecord.Type type=null;
				  	int dex=Integer.parseInt(index);
				  	for(SearchRecord.Type t:SearchRecord.Type.values()) {
				  		if(t.getIndex()==dex) {
				  			  type=t;
				  			  break;
				  		}
				  	}
				    rangeSet=Db.find("select  stext  from search_record  where   search_type=? and  stext   like  '%"+clause+"%' ",type.getIndex());
				    lacklen=10-rangeSet.size();
				    r=rangeSet.size()>0?rangeSet.get(0):null;
				   String sql=null;
				   switch(type.getIndex()) {
				   case 0:
					   sql="select  sname as stext from  ses_store where  sname like '%"+clause+"%'   limit 0,"+lacklen;
					   break;
				   case 1:
					   sql="select  name as stext from  goods  where  name  like  '%"+clause+"%'  or  keyword like '%"+clause+"%'  limit 0,"+lacklen;
					   break;
				   case 2:
					   sql="select  title as stext  from article where title like '%"+clause+"%' or seo_title like '%"+clause+"%'  limit 0,"+lacklen;
					   break;
				   }
				   System.out.println(sql);
				  List<Record> lackRecord=Db.find(sql);
				  rangeSet.addAll(lackRecord);
				  result.put("matchSet",rangeSet );
				  result.put("status", CommonSTUC.Status.Success.getIndex());
			  }catch(Exception e) {
				  	e.printStackTrace();
				  	result.put("status", CommonSTUC.Status.Error.getIndex());
			  }finally {
				  this.renderJson(result);
				  if(lacklen==10) {
					     SearchRecord nRecord=new SearchRecord();
					     nRecord.setSearchType(Integer.parseInt(index));
					     nRecord.setStext(clause);
					     nRecord.setSsum(1l);
					     nRecord.save();
				  }else {
					  	List<SearchRecord> eRecords=SearchRecord.dao.find("select sid, stext ,ssum from search_record  where   search_type=? and  stext like '%"+clause+"%' ",index);
					  	SearchRecord sr=eRecords.get(0);
					     sr.setSsum(sr.getSsum()+1);
					     sr.update();
				  }
			  }
		}
		public void search() {
					String para;
					try {
						 para=this.getPara("clause");
						List<Goods> lg=new ArrayList<>();
						if(!TextUtils.isEmpty(para)) {
							para = URLDecoder.decode(this.getRequest().getParameter("clause"),"UTF-8");
						 lg=Goods.dao.find("select  name from goods where  keyword  like '%"+para+"%'  and name like '%"+para+"%'    limit  0,5");
							 List<SearchRecord> ls=SearchRecord.dao.find("select  *  from  search_record  where  stext like '%"+para+"%'   limit  0,5");
						 		if(ls!=null&&ls.size()>0) {
						 			SearchRecord sr=ls.get(0);
						 			sr.setSsum(sr.getSsum()+1);
						 			sr.update();
						 		}else {
						 			SearchRecord sr=new SearchRecord();
						 			sr.setStext(para);
						 			sr.setSsum(1l);
						 			sr.save();
						 		}
						}else {
//							List<Record> lr=Db.find("select   stext as name from search_record where  ssum=-1 limit 06");
//							if(lr==null||lr.size()==0) {
//								 lr=Db.find("select stext as name,ssum  from search_record    order by ssum desc limit 0,6");
//							}else if(lr!=null&&lr.size()<5) {
//								  int len=lr.size();
//								  lr.addAll(Db.find("select  stext as name,ssum from search_record order by ssum desc limit 0,"+(6-len)));
//							}
							List<Goods> goods=Goods.dao.find("select  id,image,name from goods order by sales desc limit 0,6 ");
							this.renderJson(goods);
							return ;
						}
						this.renderJson(lg);
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		}
		public void searchProduct() {
				Map<String,Object> result=new HashMap<>();
			   try {
				   String condition=this.getPara("clause");
				   System.out.println(condition);
				   if(TextUtils.isEmpty(condition)) {
					   result.put("status", CommonSTUC.Status.LackPara.getIndex());
				   }else {
					   condition=URLDecoder.decode(this.getRequest().getParameter("clause"),"utf-8");
					   List<Goods> goods=Goods.dao.find("select  id,name,caption,image,keyword,price,seo_description as description,sales,onlineurl "
					   		+ "   from goods where  name like  '%"+condition+"%'  "
					    		+ "  or keyword like  '%"+condition+"%'  or name='"+condition+"'   group by id ");
					    result.put("status", CommonSTUC.Status.Success.getIndex());
					    result.put("goods", goods);
				   }
			   }catch(Exception e) {
				   e.printStackTrace();
				    	result.put("status", CommonSTUC.Status.Error.getIndex());
			   }finally {
				   this.renderJson(result);
			   }
		}
		
		public void events() {
			try {
				List<ClientAd> lc=ClientAd.dao.find("select    id,title,ad_img as image,ad_path as path,detail  from client_ad   where  status=1  and  ad_type="+ClientAd.Type.event.getIndex());
				this.renderJson(lc);
//				if(lc==null||lc.size()==0) {
//					this.renderJson(NOTMORE);
//				}else {
//					this.renderJson(lc);
//				}
			}catch(Exception e) {
				e.printStackTrace();
				this.renderJson(ERROR);
			}
		}
		
		public  void event() {
			String id=this.getPara("id");
			if(TextUtils.isEmpty(id)) {
				this.renderError(1);
			}else {
				ClientAd ca=ClientAd.dao.findById(id);
				if(ca!=null&&!TextUtils.isEmpty(ca.getAdName())) {
					System.out.println(ca.getAdPath());
					this.renderJsp(ca.getAdPath());
				}else {
					this.renderError(2);
				}
			}
		}
		
		public class  GoodsType {
			   private int  id;
			   private String name;
			   public  GoodsType(int id,String name) {
				   this.id=id;
				   this.name=name;
			   }
			public int getId() {
				return id;
			}
			public void setId(int id) {
				this.id = id;
			}
			public String getName() {
				return name;
			}
			public void setName(String name) {
				this.name = name;
			}
			   
		}
}
