package com.jfinalshop.controller.client;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.util.TextUtils;

import com.jfinal.core.Controller;
import com.jfinal.ext.route.ControllerBind;
import com.jfinalshop.model.ChosenType;
import com.jfinalshop.model.ClientAd;
import com.jfinalshop.model.Goods;
import com.jfinalshop.model.ProductCategory;
import com.jfinalshop.model.Review;
import com.jfinalshop.model.ReviewGreat;
import com.jfinalshop.model.ScreenType;
import com.jfinalshop.model.SesPlatform;
import com.jfinalshop.model.SesStore;
import com.jfinalshop.model.StoreType;
import com.jfinalshop.model.Tag;
import com.jfinalshop.model.client.CommonSTUC;
import com.jfinalshop.model.client.StoreSTUC;
import com.jfinalshop.model.client.StoreSTUC.Distance;
@ControllerBind(controllerKey="/store")
public class StoreController extends Controller {
		  private static String ERROR="error";
		  /**StoreTypeDao typeDao=this.enhance(StoreTypeDao.class);
			public void getType() {
				  List<StoreType> ls=typeDao.findAll();
				  Store_STUC ss=new Store_STUC();
				  ss.setLs(ls);
				  this.renderJson(ss);
			}*/
		  
			
			public void index() {
				   	try {
				   	   StoreSTUC ss=new StoreSTUC();
				   	   List<ClientAd> lc=ClientAd.dao.find("select  ad_img as img,ad_path as adr,title from client_ad where status=1 and ad_type="+ClientAd.Type.storeBanner.getIndex());
				   	   List<StoreType> ls=StoreType.dao.find("select id,tname as name,icon_adr as adr from store_type ");
				   	   List<SesPlatform> lsp=SesPlatform.dao.find("select id,title,img from ses_platform ");
				   	   ss.setSps(lsp);
				   	   ss.setStoread(lc);
				   	   ss.setSts(ls);
				   	   this.renderJson(ss);
				   	}catch(Exception e) {
				   		 this.renderJson(ERROR);
				   	}
				   	   
			}
			//线下门店首页初始化
			public void storeInit() {
				StoreSTUC ss=new StoreSTUC();
				try {	
					Float lat=Float.valueOf(getPara("lat"));
				    Float lng=Float.valueOf(getPara("lng"));
					String sql = "select  id,sname,price,score,left((POWER(MOD(ABS(lng - ?),360),2) + POWER(ABS(lat - ?),2))/1000,4) AS distance,img,lat,lng,detail  from ses_store order by distance asc";				
					String  searchCondition=getPara("searchcondition");				
				 	if(!TextUtils.isEmpty(searchCondition)) {
				 		   sql+="     where  sname like  '%"+searchCondition+"%'   ";
				 	}
				 	List<SesStore> sest=SesStore.dao.find(sql,lng,lat);
				 	System.out.println(sql);
				 	if(sest.size()<=0) {
				 		ss.setStatus(StoreSTUC.Status.NoExist.getIndex());
				 	}else {
				 		ss.setStatus(StoreSTUC.Status.Success.getIndex());
				 		List<ClientAd> lc=ClientAd.dao.find("select  ad_img as img,ad_path as adr,title from client_ad where status=1 and ad_type="+ClientAd.Type.marketBanner.getIndex());
				 		ss.setStoread(lc);
				 		List<StoreType> ls=StoreType.dao.find("select id,tname as name,icon_adr as adr from store_type ");
				 		ss.setSts(ls);
				 		List<SesPlatform> lsp=SesPlatform.dao.find("select id,title,img from ses_platform ");
				 		ss.setSps(lsp);
				 		List<ScreenType> sct=ScreenType.dao.find("select id,name from screen_type");
				 		ss.setSt(sct);
				 		List<Distance> distance=new ArrayList<>();				 		
				 		for (int i = 1; i <=6; i++) {
				 			int dl=1;				 			
				 			if(i==1) {
				 				Distance pp=new Distance("附近","<"+dl);
				 				distance.add(pp);				 				
				 			}else if(i==2) {				 				
				 				dl=dl*5;
				 				Distance pp=new Distance(dl+"公里","<"+dl);	
					 			distance.add(pp);
				 			}else if(i==3){
				 				dl=dl*10;
				 				Distance pp=new Distance(dl+"公里","<"+dl);	
					 			distance.add(pp);
				 			}else if(i==4) {
				 				dl=dl*50;
				 				Distance pp=new Distance(dl+"公里","<"+dl);	
					 			distance.add(pp);
				 			}else if(i==5) {
				 				dl=dl*100;
				 				Distance pp=new Distance(dl+"公里","<"+dl);	
					 			distance.add(pp);
				 			}else {
				 				dl=dl*500;
				 				Distance pp=new Distance(dl+"公里","<"+dl);	
					 			distance.add(pp);
				 			}
				 			
						}				 		 
				 		ss.setDistance(distance);
				 		ss.setSest(sest);
				 	}
				}catch (Exception e) {
					this.renderJson(StoreSTUC.Status.Error.getIndex());
				}finally {
					this.renderJson(ss);
				}
			}
			//门店搜索
			public void clauseSearch() {
				Map<String,Object> result=new HashMap<>();		
				try {
					Float lat=Float.valueOf(getPara("lat"));
				    Float lng=Float.valueOf(getPara("lng"));
					String condition=this.getPara("condition");
					String sid=this.getPara("screen_type_id");
					String stype=this.getPara("type_id");
					String d=this.getPara("distance");
					String cp=this.getPara("page");
					String sql ="select store.* from (select id,sname,price,score,tg_id,type_id,left((POWER(MOD(ABS(lng - ?),360),2) + POWER(ABS(lat - ?),2))/1000,4) AS distance,img,lat,lng  from ses_store) store where 1 = 1";
					/**String sql ;
					if(sid=="1") {
						sql="select  id,sname,price,score,left((POWER(MOD(ABS(lng - ?),360),2) + POWER(ABS(lat - ?),2))/1000,4) AS distance,img  from ses_store where tg_id = 2";
					}else if(sid=="2") {
						sql="select  id,sname,price,score,left((POWER(MOD(ABS(lng - ?),360),2) + POWER(ABS(lat - ?),2))/1000,4) AS distance,img  from ses_store where 1=1 order by score asc";
					}else {
						sql="select a.sname,a.id,a.price,a.score,left((POWER(MOD(ABS(a.lng - ?),360),2) + POWER(ABS(a.lat - ?),2))/1000,4) AS distance,a.img,SUM(b.sales) as sale from ses_store a left join goods b "
								+ "on a.id = b.store_id where 1=1 GROUP BY a.sname  ORDER BY sale asc";
					}*/				
					if(!TextUtils.isEmpty(sid)) {
						if(sid=="1") {
							sql+="  and tg_id=2";
						}else if(sid=="2") {
							sql+="  and tg_id=8";
						}else {
							sql+="  and tg_id=3";
						}						
					}
					
					if(!TextUtils.isEmpty(stype)) {
						sql+="  and type_id="+stype;
					}
					
					
					if(!TextUtils.isEmpty(condition)) {
						sql+="   and sname  like '%"+condition+"%'";
					}
					if(!TextUtils.isEmpty(cp)) {
						   if(cp.trim().matches("[0-9]+")) {
							      int p=Integer.parseInt(cp)*10;
							      sql+="     limit "+p+",10";
						   }else {
							    this.renderJson("parawrong");
							    return;
						   }
					}
					if(!TextUtils.isEmpty(d)) {	
						int index;
						if((index=d.indexOf("附近"))!=-1) {
							sql+="  and distance<="+1;
						}
						if((index=d.indexOf("公里"))!=-1) {							
							sql+="     and  distance<="+d.substring(0,index);
						}
					}
					System.out.println(sql);
					List<SesStore> lg=SesStore.dao.find(sql,lng,lat);
					result.put("status", CommonSTUC.Status.Success.getIndex());
					result.put("lg",lg);
				}catch(Exception e) {
					  e.printStackTrace();
					  result.put("status", CommonSTUC.Status.NoExist.getIndex());
				}finally {
					this.renderJson(result);
				}
	       }
			//线下门店详情
			public void detail() {
				StoreSTUC ss=new StoreSTUC();
                try {
                	Float lat=Float.valueOf(getPara("lat"));
				    Float lng=Float.valueOf(getPara("lng"));
                	long id=this.getParaToLong("id");
                	//long mid=this.getParaToLong("member_id");
                	List<SesStore> ses=SesStore.dao.find("select  id,sname,price,score,left((POWER(MOD(ABS(lng - ?),360),2) + POWER(ABS(lat - ?),2))/1000,4) AS distance,img,lat,lng,detail  from ses_store where id=?",lng,lat,id);              	
                	if(ses.size()<=0) {
                		ss.setStatus(StoreSTUC.Status.NoExist.getIndex());
                	}else {
                		ss.setStatus(StoreSTUC.Status.Success.getIndex());
                		ss.setSest(ses);
                		List<Goods> gs=Goods.dao.find("select  a.id,name,a.image  "
				 				+ "from goods a left join ses_store b on a.store_id = b.id where  a.store_id ="+id);
				 		ss.setGs(gs);
				 		/**List<Evalueate> eva=Evalueate.dao.find("select a.id,a.detail,a.createtime,a.escore,e.name,e.avatar "
				 				+ "from evalueate a left join store_eva b on a.id = b.evalueate_id "
				 				+ "left join ses_store c on b.store_id = c.id "
				 				+ "left join member_eva d on d.evalueate_id = a.id "
				 				+ "left join member e on d.member_id = e.id where a.type = 2 and c.id ="+id);
				 		ss.setEva(eva);*/
				 		List<Review> rg=Review.dao.find("select a.id,a.create_date,a.content,a.images,a.score,b.name from review  a left join member b on a.member_id = b.id where a.rtype = 2 and a.store_id ="+id);
				 		ss.setRg(rg);
				 		/**List<UserCollectionShop> list=UserCollectionShop.dao.find("select id from user_collection_shop where ses_store_id=? and member_id=?",id);
				 		if(list!=null&&list.size()>0) {
							  this.renderJson(StoreSTUC.Collect.Success.getIndex()); 
						   }else {
							  this.renderJson(StoreSTUC.Collect.Cancel.getIndex());
						   }*/                	  
                	}
				}catch (Exception e) {
					this.renderJson(StoreSTUC.Status.Error.getIndex());
				}finally {
					this.renderJson(ss);
				}
			}
			//精选商家
			public void chosenStore() {
				//StoreSTUC ss=new StoreSTUC();
				Map<String,Object> result=new HashMap<>();
				try {			
					String type=this.getPara("store_type_id");					
					String cp=this.getPara("page"); 
					//String sql="select a.id,a.sname,a.img,a.detail,a.type_id,b.image,GROUP_CONCAT(DISTINCT(d.id)) as cid,GROUP_CONCAT(DISTINCT(d.name)) as name from ses_store a left join goods b on a.id = b.store_id left join ses_chosen c on a.id = c.store_id left join chosen_type d on c.chosen_id = d.id group by a.id having 1=1";
					String sql="select   id,sname,img,detail,type_id  from ses_store  ";
					if(!TextUtils.isEmpty(type)) {
						sql+="  where  tg_id="+type;
					}					
					if(!TextUtils.isEmpty(cp)) {
						   if(cp.trim().matches("[0-9]+")) {
							      int p=Integer.parseInt(cp)*10;
							      sql+="     limit "+p+",10";
						   }else {
							    this.renderJson("parawrong");
							    return;
						   }
					}
					System.out.println(sql);
				 	List<SesStore> sest=SesStore.dao.find(sql);
				 	if(sest.size()<=0) {
				 		result.put("status",StoreSTUC.Status.NoExist.getIndex());
				 	}else {
				 		result.put("status",StoreSTUC.Status.Success.getIndex());
				 		List<ClientAd> lc=ClientAd.dao.find("select  ad_img as img,ad_path as adr,title from client_ad where status=1 and ad_type="+ClientAd.Type.chosenStoreBanner.getIndex());
				 		List<Tag> ct=Tag.dao.find("select id,name from tag where  type=? ",Tag.Types.store.ordinal());
				 		result.put("sts", ct);
				 		List<SesPlatform> lsp=SesPlatform.dao.find("select id,title,img from ses_platform ");
				 		result.put("lsp", lsp);	 		
				 	result.put("sest", sest);
					result.put("storead", lc);
				 	}
				}catch (Exception e) {
					this.renderJson(ERROR);
				}finally {
					this.renderJson(result);
				}
			}
			//商家详情
			public void storeDetail() {
				StoreSTUC ss=new StoreSTUC();
				try {
					long id=this.getParaToLong("id");
					//long mid=this.getParaToLong("member_id");
					List<SesStore> sest=SesStore.dao.find("select sname from ses_store where id ="+id);
					if(sest.size()<=0) {
				 		ss.setStatus(StoreSTUC.Status.NoExist.getIndex());
				 	}else {
				 		ss.setStatus(StoreSTUC.Status.Success.getIndex());
				 		List<ClientAd> lc=ClientAd.dao.find("select  ad_img as img,ad_path as adr,title from client_ad where status=1 and ad_type="+ClientAd.Type.storeBanner.getIndex());
				 		ss.setStoread(lc);
				 		List<Goods> gs=Goods.dao.find("select  a.sales,a.id,name,a.price,a.market_price,a.origin,a.cycle,a.type,a.image,a.onlineurl,a.is_dz  "
				 				+ "from goods a left join ses_store b on a.store_id = b.id where  a.store_id ="+id);
				 		ss.setGs(gs);
				 		List<ProductCategory> pc=ProductCategory.dao.find("select id,name from product_category where parent_id = 244 ");
				 		ss.setPc(pc);			 		
				 		ss.setSest(sest);
				 		/**List<UserCollectionShop> list=UserCollectionShop.dao.find("select id from user_collection_shop where ses_store_id=? and member_id=?",id,mid);
				 		if(list!=null&&list.size()>0) {
							  this.renderJson(StoreSTUC.Collect.Success.getIndex()); 
						   }else {
							  this.renderJson(StoreSTUC.Collect.Cancel.getIndex());
						   } */
				 	}
				}catch (Exception e) {
					this.renderJson(StoreSTUC.Status.Error.getIndex());
				}finally {
					this.renderJson(ss);
				}
			}
			//商家筛选
			public void storeGoodSearch() {
				Map<String,Object> result=new HashMap<>();
				try {					
					String type=this.getPara("product_category_id");					
					String cp=this.getPara("page");
					long id=this.getParaToLong("id");
					String sql=null;
					if(id!=0) {
						sql="select   a.id,a.name,a.price,a.market_price,a.number,a.sales,a.origin,a.onlineurl,a.cycle,a.type,a.image  from goods a left join ses_store b on a.store_id = b. id where 1=1 and a.store_id = "+id;
					}										
					if(!TextUtils.isEmpty(type)) {
						sql+="  and product_category_id="+type;
					}					
					if(!TextUtils.isEmpty(cp)) {
						   if(cp.trim().matches("[0-9]+")) {
							      int p=Integer.parseInt(cp)*10;
							      sql+="     limit "+p+",10";
						   }else {
							    this.renderJson("parawrong");
							    return;
						   }
					}
					System.out.println(sql);					
					List<Goods> lg=Goods.dao.find(sql);					
					result.put("lg",lg);					
					result.put("status", CommonSTUC.Status.Success.getIndex());															
					//this.renderJson(lg);
					//result.put("status", CommonSTUC.Status.Success.getIndex());
					//result.put("lg",lg);
				}catch(Exception e) {
					  e.printStackTrace();
					  result.put("status", CommonSTUC.Status.Error.getIndex());
				}finally {
					this.renderJson(result);
				}
			}	
			//店铺点赞
			public void storeGreat() {
				try {
					long mid=this.getParaToLong("member_id");
					long rid=this.getParaToLong("review_id");
					//查询是否有该用户对该评论的点赞记录
					List<ReviewGreat> list=ReviewGreat.dao.find("select a.* from review_great a "
							+ "left join member b on a.member_id = b.id "
							+ "left join review c on a.review_id = c.id "
							+ "where a.member_id =? and a.review_id = ?",mid,rid);
					if(list!=null&&list.size()>0) {
						//如果找到了这条记录，则删除该记录，同时评论的点赞数减1
						ReviewGreat reviewGreat=(ReviewGreat)list.get(0);						
						//删除该条记录
						ReviewGreat.dao.deleteById(reviewGreat.getId());
						//评论点赞数减1
						Review re=Review.dao.findById(rid);
						re.setScore(re.getScore()-1);						
						re.update();
						renderNull();
					}else {
						//如果没有找到这条记录，则添加这条记录，同时评论点赞数加1
						ReviewGreat rg=new ReviewGreat();
						rg.setMemberId(mid);
						rg.setReviewId(rid);
						//添加记录
						rg.save();
						//评论点赞数加1
						Review re=Review.dao.findById(rid);
						re.setScore(re.getScore()+1);
						re.update();
						renderNull();
					}					
				}catch(Exception e){
					this.renderJson("error");
				}
			}
						
}
