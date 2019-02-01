package com.jfinalshop.controller.client;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.util.TextUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.json.JSONArray;
import org.json.JSONObject;

import com.jfinal.core.Controller;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.json.FastJson;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinalshop.entity.ProductImage;
import com.jfinalshop.model.Cart;
import com.jfinalshop.model.CartItem;
import com.jfinalshop.model.ClientAd;
import com.jfinalshop.model.DzType;
import com.jfinalshop.model.Goods;
import com.jfinalshop.model.Member;
import com.jfinalshop.model.Product;
import com.jfinalshop.model.ProductCategory;
import com.jfinalshop.model.Review;
import com.jfinalshop.model.SesStore;
import com.jfinalshop.model.UserCollectionGoods;
import com.jfinalshop.model.client.CommonSTUC;
import com.jfinalshop.model.client.CustSTUC;
import com.jfinalshop.model.client.CustSTUC.PriceGap;
import com.jfinalshop.model.client.StockSTUC.PriceGape;
import com.jfinalshop.model.client.StockSTUC;
import com.jfinalshop.service.GoodsService;
import com.jfinalshop.service.MemberService;
@ControllerBind(controllerKey="/client/product")
public class ProductController  extends Controller {
	private static final  String NO_EXIST="noexist";
	private static final String ERROR="error";
			MemberService memberService=this.enhance(MemberService.class);
			GoodsService goodsService=this.enhance(GoodsService.class);
	  /**
	   * @author zxx
	   * @category clientInterface
	   * 
	   */
		public void detail() {
			   CustSTUC cs=new CustSTUC();
			   try {
				   
				   long id=this.getParaToLong("id");
				   //long mid=this.getParaToLong("member_id");
				   List<Goods> gs=Goods.dao.find("select  a.image,a.id,a.caption,a.origin,a.name,a.price,a.product_images,a.market_price,a.unit,a.is_dz,a.specification_items,a.release_date,a.stype,a.product_date,a.introduction,b.sname,count(c.goods_id) as number,avg(c.score) as scores"
				   		+ ",a.keyword   from goods  a left join ses_store b on a.store_id = b.id left join review c on a.id = c.goods_id where a.id="+id);
				   Goods gds=(Goods)gs.get(0);
				   gs.add(0, gds);
				   List<Product> products = Product.dao.find("select id from product where goods_id = ?",gds.getId());
 				   gds.setAttributeValue2(String.valueOf(products.get(0).getId()));
				   String introduce=gds.getIntroduction();				  
				   String[] attr=introduce.split("src=\"");				   
				   String intrduction="";
				   for (int i = 0; i < attr.length; i++) {
					   String[] temp=attr[i].split("\"");					   
					   intrduction+=temp[0]+",";					   
				   }				   
				   intrduction=intrduction.replaceAll("<[^,]*,", "");
				   if(intrduction.length()>0) {
					   String intro=intrduction.substring(0, intrduction.length()-1);
					   gds.setIntroduction(intro);
				   }
				   String str=gds.getProductImages();
				   //将product_images拼接为以,分割的字符串
				   JSONArray jsonArray = null;
				   jsonArray = new JSONArray(str);
				   String images="";
				   for (int i = 0; i < jsonArray.length(); i++) {					   
					   images+=(jsonArray.getJSONObject(i).get("large")+","); 					   
				   }
				   if(images.length()>0) {
					   String image_product=images.substring(0, images.length()-1);
					   gds.setProductImages(image_product);
				   }			   
				   cs.setLg(gs);
				   
				   List<Review> rg=Review.dao.find("select a.id,a.create_date,a.content,a.images,b.name,b.avatar from review a left join member b on a.member_id = b.id where a.rtype = 1 and a.goods_id ="+id);				   				   					   				   
				   cs.setRg(rg);
				   /**List<UserCollectionGoods> list=UserCollectionGoods.dao.find("select id from user_collection_goods where goods_id=? and member_id=?",id,mid);
				   if(list!=null&&list.size()>0) {
					  this.renderJson(CustSTUC.Collect.Success.getIndex()); 
				   }else {
					  this.renderJson(CustSTUC.Collect.Cancel.getIndex());
				   }*/
				   if(gs==null||gs.size()<=0) {
					   		this.renderJson(CustSTUC.Status.NoExist.getIndex());
				   }else {
					   this.renderJson(cs);
				   }
			   }catch(Exception e) {
				     e.printStackTrace();
				     this.renderJson(CustSTUC.Status.Error.getIndex());
			   }
		}			
		
		//定制商品首页初始化
		public  void custInit() {
			CustSTUC cs=new CustSTUC();
			try {
				int type=this.getParaToInt("type_id");
				String sql="select  sales,id,name,price,market_price,origin,cycle,type,image,onlineurl  from goods  where is_dz = 1 ";							
				String  searchCondition=getPara("searchcondition");
			 	if(!TextUtils.isEmpty(searchCondition)) {
			 		sql+=" and  name like  '%"+searchCondition+"%'   ";			 		
			 	}
			 	  List<Goods> lg=Goods.dao.find(sql);
			 			if(lg.size()<=0) {
			 				cs.setStatus(CustSTUC.Status.NoExist.getIndex());
			 			}else {
			 				cs.setStatus(CustSTUC.Status.Success.getIndex());
			 				if(type==0) {
			 					List<ClientAd> lc=ClientAd.dao.find("select  ad_img as img,ad_path as url,title  from client_ad   where status=1 and ad_type="+ClientAd.Type.customBanner.getIndex());
					 			cs.setAd(lc);
			 				}else {
			 					List<ClientAd> lc=ClientAd.dao.find("select  ad_img as img,ad_path as url,title  from client_ad   where status=1 and ad_type="+ClientAd.Type.allGoodBanner.getIndex());
					 			cs.setAd(lc);
			 				}
			 				List<ProductCategory> ld=ProductCategory.dao.find("select id,name from product_category where parent_id = 243");
				 			   cs.setDt(ld);		 			   
				 			   List<Record> lr=Db.find("select avg(price) from  goods  ");
				 			   cs.setLg(lg);
				 			  /**if(lr!=null&&lr.size()>0) {
				 				     //double price=lr.get(0).getBigDecimal("avg(price)").doubleValue();
				 				     //if(price<10) {
				 				    	 PriceGap pp=new   PriceGap("200以下","<=200");
				 				    	 List<PriceGap> lpg=new ArrayList<>();
				 				    	 lpg.add(pp);
				 				    	 PriceGap pp1=new PriceGap("200-500",">200","<=500");
				 				    	 lpg.add(pp1);
				 				    	 PriceGap pp2=new PriceGap("500-1000",">500","<=1000");
				 				    	 lpg.add(pp2);
				 				    	 PriceGap pp3=new PriceGap("1000以上",">1000");
				 				    	 lpg.add(pp3);
				 				     }else {
				 				    	    long previous=0;
				 				    	    double d=price/10;
				 				    	    List<PriceGap> lpg=new ArrayList<>();
				 				    	    for(int i=1;i<=5;i++) {
				 				    		      long dl=Math.round(i*d);
				 				    		     dl+=(10-(dl%10));
				 				    		      if(i==1) {
				 				    		    	  PriceGap pp=new PriceGap(dl+"以下","<"+dl);
				 				    		    	  lpg.add(pp);
				 				    		    	  previous=dl;
				 				    		      }else {
				 				    		    	  PriceGap pp=new PriceGap(previous+"-"+dl,">"+previous,"<="+dl);
				 				    		    	  lpg.add(pp);
				 				    		    	  previous=dl;
				 				    		      }
				 				    	  }
				 				    	 
				 				     //}
				 			   //}*/
				 			     List<PriceGap> lpg=new ArrayList<>();
				 			     PriceGap pp0=new   PriceGap("全部","");		 				    	 
		 				    	 lpg.add(pp0);
				 			     PriceGap pp=new   PriceGap("200以下","<=200");		 				    	 
		 				    	 lpg.add(pp);
		 				    	 PriceGap pp1=new PriceGap("200-500",">200","<=500");
		 				    	 lpg.add(pp1);
		 				    	 PriceGap pp2=new PriceGap("500-1000",">500","<=1000");
		 				    	 lpg.add(pp2);
		 				    	 PriceGap pp3=new PriceGap("1000以上",">1000");
		 				    	 lpg.add(pp3);
		 				    	 cs.setLp(lpg);
				 			     //cs.setAd(lc);
			 			}
			 
			}catch(Exception e) {
				cs.setStatus(CustSTUC.Status.Error.getIndex());
				e.printStackTrace();
			}finally {
				this.renderJson(cs);
			}
			
		  }
		 
		  public void clauseSearch() {
			    //List<String> gap=new ArrayList<>();
			    Map<String,Object> result=new HashMap<String,Object>();
				try {
					String condition=this.getPara("condition");
					String g=this.getPara("pricegap");
					String dztype=this.getPara("dz_type_id");
					String type=this.getPara("product_category_id");
					String cp=this.getPara("page");
				
					String sql="select   id,sales,name,price,market_price,number,origin,onlineurl,cycle,type,image,is_dz,dz_type_id  from goods where is_dz = 1  ";					
					if(!TextUtils.isEmpty(dztype)) {
						sql+="  and dz_type_id="+dztype;						
					}	
					if(!TextUtils.isEmpty(type)) {
						sql+="  and product_category_id="+type;
					}
					if(!TextUtils.isEmpty(g)) {
						int index;
						if(( index=g.indexOf("全部"))!=-1) {
							sql+="  and price>"+0;
						}
						if(( index=g.indexOf("以下"))!=-1) {
							sql+="  and price<"+g.substring(0,index);
						}
						if(g.indexOf("-")!=-1) {
							sql+="     and price>="+g.split("-")[0];
							sql+="     and  price<="+g.split("-")[1];
						}
						if(( index=g.indexOf("以上"))!=-1) {
							sql+="  and price>"+g.substring(0,index);
						}
					}
					if(!TextUtils.isEmpty(condition)) {
						sql+="   and name  like '%"+condition+"%'";
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
					result.put("status", CommonSTUC.Status.Success.getIndex());
					result.put("lg",lg);										
				}catch(Exception e) {
					  e.printStackTrace();
					  result.put("status", CommonSTUC.Status.Error.getIndex());
				}finally {
					this.renderJson(result);
				}
        }
		
		//现货产品页面初始化
		public  void stockInit() {
			StockSTUC cs=new StockSTUC();
			try {				
				String sql="select  sales,id,name,price,market_price,origin,cycle,type,image,onlineurl,is_dz  from goods where is_dz = 0  ";
			
				String  searchCondition=getPara("searchcondition");
			 	if(!TextUtils.isEmpty(searchCondition)) {
			 		   sql+="     and  name like  '%"+searchCondition+"%'   ";
			 	}
			 	  List<Goods> lg=Goods.dao.find(sql);
			 			if(lg.size()<=0) {
			 				cs.setStatus(StockSTUC.Status.NoExist.getIndex());
			 			}else {

//			 				cs.setStatus(CustSTUC.statu.success);
//			 				 List<DzType> ld=DzType.dao.find("select id,name from dz_type");
//				 			   cs.setDt(ld);
//				 			   List<ClientAd> lc=ClientAd.dao.find("select  ad_img as img,ad_path as url,title  from client_ad   where status=1 and ad_type="+ClientAd.Type.custBanner.getIndex());
//				 			   	cs.setAd(lc);


			 				 cs.setStatus(StockSTUC.Status.Success.getIndex());
			 				 List<ProductCategory> ld=ProductCategory.dao.find("select id,name from product_category where parent_id = 244");
				 			   cs.setDt(ld);
				 			   List<ClientAd> lc=ClientAd.dao.find("select  ad_img as img,ad_path as url,title  from client_ad   where status=1 and ad_type="+ClientAd.Type.allGoodBanner.getIndex());
				 			   cs.setAd(lc);

				 			   List<Record> lr=Db.find("select max(price) from  goods  ");
				 			   cs.setLg(lg);
				 			  /** if(lr!=null&&lr.size()>0) {
				 				     double price=lr.get(0).getBigDecimal("max(price)").doubleValue();
				 				     if(price<10) {
				 				    	 PriceGape pp=new   PriceGape("5以下","<5");
				 				    	List<PriceGape> lpg=new ArrayList<>();
				 				    	 lpg.add(pp);
				 				    	 PriceGape pp1=new PriceGape("5-10",">5","<=10");
				 				     }else {
				 				    	     long previous=0;
				 				    	    double d=price/5;
				 				    	   List<PriceGape> lpg=new ArrayList<>();
				 				    	  for(int i=1;i<=5;i++) {
				 				    		      long dl=Math.round(i*d);
				 				    		     dl+=(10-(dl%10));
				 				    		      if(i==1) {
				 				    		    	  PriceGape pp=new PriceGape(dl+"以下","<"+dl);
				 				    		    	  lpg.add(pp);
				 				    		    	  previous=dl;
				 				    		      }else {
				 				    		    	  PriceGape pp=new PriceGape(previous+"-"+dl,">"+previous,"<="+dl);
				 				    		    	  lpg.add(pp);
				 				    		    	  previous=dl;
				 				    		      }
				 				    	  }
				 				    	 cs.setLp(lpg);
				 				     }
				 			   }
				 			   cs.setAd(lc);*/
				 			     List<PriceGape> lpg=new ArrayList<>();
				 			    PriceGape pp0=new   PriceGape("全部","");		 				    	 
		 				    	 lpg.add(pp0);
				 			    PriceGape pp=new   PriceGape("200以下","<=200");		 				    	 
		 				    	 lpg.add(pp);
		 				    	PriceGape pp1=new PriceGape("200-500",">200","<=500");
		 				    	 lpg.add(pp1);
		 				    	PriceGape pp2=new PriceGape("500-1000",">500","<=1000");
		 				    	 lpg.add(pp2);
		 				    	PriceGape pp3=new PriceGape("1000以上",">1000");
		 				    	 lpg.add(pp3);
		 				    	 cs.setLp(lpg);
				 			     cs.setAd(lc);
			 			}
			 
			}catch(Exception e) {
				cs.setStatus(StockSTUC.Status.Error.getIndex());
				e.printStackTrace();
			}finally {
				this.renderJson(cs);
			}
			
		  }		
		
			public void allclauseSearch() {
						//List<String> gap=new ArrayList<>();
				Map<String,Object> result=new HashMap<>();
				try {
					String condition=this.getPara("condition");
					
					String g=this.getPara("pricegap");
					//String g= URLDecoder.decode(getPara("pricegap"),"UTF-8");
					String type=this.getPara("product_category_id");
					String dztype=this.getPara("dz_type_id");
					String dz=this.getPara("is_dz");
					String cp=this.getPara("page");
					
					String sql="select   id,sales,name,price,market_price,number,origin,onlineurl,cycle,type,image,is_dz,dz_type_id  from goods where is_dz = 0 ";
					if(!TextUtils.isEmpty(dz)) {
						sql+="  and is_dz="+dz;
					}
					if(!TextUtils.isEmpty(dztype)) {
						sql+="  and dz_type_id="+dztype;
					}
					if(!TextUtils.isEmpty(type)) {
						sql+="  and product_category_id="+type;
					}
					if(!TextUtils.isEmpty(g)) {
						int index;
						if(( index=g.indexOf("以下"))!=-1) {
							sql+="  and price<"+g.substring(0,index);
						}
						if(g.indexOf("-")!=-1) {
							sql+="   and price>="+g.split("-")[0];
							sql+="    and  price<="+g.split("-")[1];
						}
						if(( index=g.indexOf("以上"))!=-1) {
							sql+="  and price>"+g.substring(0,index);
						}
					}
					if(!TextUtils.isEmpty(condition)) {
						sql+="   and name  like '%"+condition+"%'";
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
					result.put("status", CommonSTUC.Status.Success.getIndex());
					result.put("lg",lg);	
				}catch(Exception e) {
					  e.printStackTrace();
					  result.put("status", CommonSTUC.Status.Error.getIndex());
				}finally {
					this.renderJson(result);
				}
			}
			//精选定制商品首页初始化
			public  void chosenCustInit() {
				CustSTUC cs=new CustSTUC();
				try {
					String sql="select  a.sales,a.id,a.name,a.price,a.market_price,a.origin,a.cycle,a.type,a.image,a.onlineurl  from goods a "
							+ "left join goods_tag b on a.id = b.goods left join tag c on b.tags = c.id where a.is_dz = 1 and c.id = 5";							
					String  searchCondition=getPara("searchcondition");
				 	if(!TextUtils.isEmpty(searchCondition)) {
				 		sql+=" and  name like  '%"+searchCondition+"%'   ";			 		
				 	}
				 	  List<Goods> lg=Goods.dao.find(sql);
				 			if(lg.size()<=0) {
				 				cs.setStatus(CustSTUC.Status.NoExist.getIndex());
				 			}else {
				 				cs.setStatus(CustSTUC.Status.Success.getIndex());
				 				List<ProductCategory> ld=ProductCategory.dao.find("select id,name from product_category where parent_id = 243");
					 			   cs.setDt(ld);
					 			   List<ClientAd> lc=ClientAd.dao.find("select  ad_img as img,ad_path as url,title  from client_ad   where status=1 and ad_type="+ClientAd.Type.chosenGoodBanner.getIndex());
					 			   	cs.setAd(lc);
					 			   List<Record> lr=Db.find("select avg(price) from  goods  ");
					 			   cs.setLg(lg);
					 			   /**if(lr!=null&&lr.size()>0) {
					 				     double price=lr.get(0).getBigDecimal("avg(price)").doubleValue();
					 				     if(price<10) {
					 				    	 PriceGap pp=new   PriceGap("5以下","<5");
					 				    	 List<PriceGap> lpg=new ArrayList<>();
					 				    	 lpg.add(pp);
					 				    	 PriceGap pp1=new PriceGap("5-10",">5","<=10");
					 				     }else {
					 				    	    long previous=0;
					 				    	    double d=price/10;
					 				    	    List<PriceGap> lpg=new ArrayList<>();
					 				    	    for(int i=1;i<=5;i++) {
					 				    		      long dl=Math.round(i*d);
					 				    		     dl+=(10-(dl%10));
					 				    		      if(i==1) {
					 				    		    	  PriceGap pp=new PriceGap(dl+"以下","<"+dl);
					 				    		    	  lpg.add(pp);
					 				    		    	  previous=dl;
					 				    		      }else {
					 				    		    	  PriceGap pp=new PriceGap(previous+"-"+dl,">"+previous,"<="+dl);
					 				    		    	  lpg.add(pp);
					 				    		    	  previous=dl;
					 				    		      }
					 				    	  }
					 				    	 cs.setLp(lpg); 
					 				     }
					 			   }
					 			   cs.setAd(lc);*/
					 			     List<PriceGap> lpg=new ArrayList<>();
					 			     PriceGap pp=new   PriceGap("200以下","<=200");		 				    	 
			 				    	 lpg.add(pp);
			 				    	 PriceGap pp1=new PriceGap("200-500",">200","<=500");
			 				    	 lpg.add(pp1);
			 				    	 PriceGap pp2=new PriceGap("500-1000",">500","<=1000");
			 				    	 lpg.add(pp2);
			 				    	 PriceGap pp3=new PriceGap("1000以上",">1000");
			 				    	 lpg.add(pp3);
			 				    	 cs.setLp(lpg);
					 			     cs.setAd(lc);
				 			}
				 
				}catch(Exception e) {
					cs.setStatus(CustSTUC.Status.Error.getIndex());
					e.printStackTrace();
				}finally {
					this.renderJson(cs);
				}
				
			  }
			
			//精选现货产品页面初始化
			public  void chosenStockInit() {
				StockSTUC cs=new StockSTUC();
				try {				
					String sql="select  a.sales,a.id,a.name,a.price,a.market_price,a.origin,a.cycle,a.type,a.image,a.onlineurl  from goods a "
							+ "left join goods_tag b on a.id = b.goods left join tag c on b.tags = c.id where a.is_dz = 0 and c.id = 5";
				
					//String  searchCondition=URLDecoder.decode(getPara("searchcondition"),"UTF-8");
					String  searchCondition=getPara("searchcondition");
				 	if(!TextUtils.isEmpty(searchCondition)) {
				 		   sql+="     and  a.name like  '%"+searchCondition+"%'   ";
				 	}
				 	  List<Goods> lg=Goods.dao.find(sql);
				 			if(lg.size()<=0) {
				 				cs.setStatus(StockSTUC.Status.NoExist.getIndex());
				 			}else {
				 				cs.setStatus(StockSTUC.Status.Success.getIndex());
				 				 List<ProductCategory> ld=ProductCategory.dao.find("select id,name from product_category where parent_id = 244");
					 			   cs.setDt(ld);
					 			   List<ClientAd> lc=ClientAd.dao.find("select  ad_img as img,ad_path as url,title  from client_ad   where status=1 and ad_type="+ClientAd.Type.chosenGoodBanner.getIndex());
					 			   cs.setAd(lc);

					 			   List<Record> lr=Db.find("select max(price) from  goods  ");
					 			   cs.setLg(lg);
					 			   /**if(lr!=null&&lr.size()>0) {
					 				     double price=lr.get(0).getBigDecimal("max(price)").doubleValue();
					 				     if(price<10) {
					 				    	 PriceGape pp=new   PriceGape("5以下","<5");
					 				    	List<PriceGape> lpg=new ArrayList<>();
					 				    	 lpg.add(pp);
					 				    	 PriceGape pp1=new PriceGape("5-10",">5","<=10");
					 				     }else {
					 				    	     long previous=0;
					 				    	    double d=price/5;
					 				    	   List<PriceGape> lpg=new ArrayList<>();
					 				    	  for(int i=1;i<=5;i++) {
					 				    		      long dl=Math.round(i*d);
					 				    		     dl+=(10-(dl%10));
					 				    		      if(i==1) {
					 				    		    	  PriceGape pp=new PriceGape(dl+"以下","<"+dl);
					 				    		    	  lpg.add(pp);
					 				    		    	  previous=dl;
					 				    		      }else {
					 				    		    	  PriceGape pp=new PriceGape(previous+"-"+dl,">"+previous,"<="+dl);
					 				    		    	  lpg.add(pp);
					 				    		    	  previous=dl;
					 				    		      }
					 				    	  }
					 				    	 cs.setLp(lpg);
					 				     }
					 			   }
					 			   cs.setAd(lc);*/
					 			    List<PriceGape> lpg=new ArrayList<>();
					 			    PriceGape pp=new   PriceGape("200以下","<=200");		 				    	 
			 				    	 lpg.add(pp);
			 				    	PriceGape pp1=new PriceGape("200-500",">200","<=500");
			 				    	 lpg.add(pp1);
			 				    	PriceGape pp2=new PriceGape("500-1000",">500","<=1000");
			 				    	 lpg.add(pp2);
			 				    	PriceGape pp3=new PriceGape("1000以上",">1000");
			 				    	 lpg.add(pp3);
			 				    	 cs.setLp(lpg);
					 			     cs.setAd(lc);
				 			}
				 
				}catch(Exception e) {
					cs.setStatus(StockSTUC.Status.Error.getIndex());
					e.printStackTrace();
				}finally {
					this.renderJson(cs);
				}
				
			  }
			
			public void allChosenSearch() {
				//List<String> gap=new ArrayList<>();
				Map<String,Object> result=new HashMap<>();
				try {
					String condition=this.getPara("condition");
					String g=this.getPara("pricegap");
					//String g= URLDecoder.decode(getPara("pricegap"),"UTF-8");
					String type=this.getPara("product_category_id");
					String dztype=this.getPara("dz_type_id");
					String dz=this.getPara("is_dz");
					String cp=this.getPara("page");
					
					String sql="select   a.id,a.name,a.sales,a.price,a.market_price,a.number,a.origin,a.onlineurl,a.cycle,a.type,a.image,a.is_dz,a.dz_type_id  from goods  a "
							+ "left join goods_tag b on a.id = b.goods left join tag c on b.tags = c.id where a.is_dz in(select is_dz from goods) and c.id = 5";
					if(!TextUtils.isEmpty(dz)) {
						sql+="  and a.is_dz="+dz;
					}
					if(!TextUtils.isEmpty(dztype)) {
						sql+="  and a.dz_type_id="+dztype;
					}
					if(!TextUtils.isEmpty(type)) {
						sql+="  and a.product_category_id="+type;
					}
					if(!TextUtils.isEmpty(g)) {
						int index;
						if(( index=g.indexOf("以下"))!=-1) {
							sql+="  and price<"+g.substring(0,index);
						}
						if(g.indexOf("-")!=-1) {
							sql+="   and price>="+g.split("-")[0];
							sql+="    and  price<="+g.split("-")[1];
						}
						if(( index=g.indexOf("以上"))!=-1) {
							sql+="  and price>"+g.substring(0,index);
						}
					}
					if(!TextUtils.isEmpty(condition)) {
						sql+="   and name  like '%"+condition+"%'";
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
					List<Goods> lg=Goods.dao.find(sql);
					result.put("status", CommonSTUC.Status.Success.getIndex());
					result.put("lg",lg);					
				}catch(Exception e) {
					e.printStackTrace();
					  result.put("status", CommonSTUC.Status.Error.getIndex());				  
				}finally {
					this.renderJson(result);
				}
	        }	
			
			//精选推荐
			public void chosenGroom() {
				StockSTUC cs=new StockSTUC();
				try {
					String sql="select  a.sales,a.id,a.name,a.price,a.market_price,a.origin,a.cycle,a.type,a.image,a.onlineurl  from goods a left join goods_tag b on a.id = b.goods  where  b.tags = 9";					
					List<Goods> lg=Goods.dao.find(sql);					
					if(lg.size()<=0) {
						cs.setStatus(StockSTUC.Status.NoExist.getIndex());
					}else {
						cs.setStatus(StockSTUC.Status.Success.getIndex());
						List<ClientAd> lc=ClientAd.dao.find("select  ad_img as img,ad_path as url,title  from client_ad  where status=1 and ad_type="+ClientAd.Type.chosenGroomBanner.getIndex());
			 			cs.setAd(lc);
			 			cs.setLg(lg);
					}
				}catch (Exception e) {
					cs.setStatus(StockSTUC.Status.Error.getIndex());
					e.printStackTrace();
				}finally {
					renderJson(cs);
				}
			}
			//购物车
			public void  cart() {
				Map<String,Object> so=new HashMap<>();
				 try {
					   String mid=this.getPara("mid");
					    String page=this.getPara("page");
					    int cp=0;
					    if((!TextUtils.isEmpty(page))&&(page.matches("[\\d]*"))) {
					    	   cp=Integer.parseInt(page)*10;
					    }
					    if(TextUtils.isEmpty(mid)||(!mid.matches("^\\d*$"))) {
					    	 so.put("status", CommonSTUC.Status.Illegal.getIndex());
					    }else {
					    	 List<Record> citems=Db.find("select  ci.id as itemid,ci.quantity,p.stock,p.id as pid,g.id as gid,g.price,g.image,g.name,g.unit,g.seo_description as description,caption  "
					    	 		+ " from cart_item ci inner join  product  p on ci.product_id=p.id "
							    		+ "    inner join goods g on p.goods_id=g.id where  ci.cart_id=(select id from cart where  member_id=?)  limit ?,10",mid,cp);
					    	  for(Record r:citems) {
					    		
					    		    if(r.getStr("description")==null) {
					    		    	 r.set("description", "");
					    		    }
					    	  }
							    so.put("cartItem", citems);
							    so.put("status", CommonSTUC.Status.Success.getIndex());
					    }
				 }catch(Exception e) {
					 e.printStackTrace();
					   so.put("status",CommonSTUC.Status.Error.getIndex());
				 }
				 this.renderJson(so);
			}

			//添加到购物车
			public void joinCart() {
				Map<String,Object> result=new HashMap<>();
						 try {
							 String mid=this.getPara("mid");
							 String gid=this.getPara("gid");
							 String quantity=this.getPara("quantity");
							 String reg="^\\d*$";
							 if(TextUtils.isEmpty(mid)||TextUtils.isEmpty(gid)||TextUtils.isEmpty(quantity)||(!mid.matches(reg))||(!gid.matches(reg))||(!quantity.matches(reg))) {
								   result.put("status", CommonSTUC.Status.LackPara.getIndex());
							 }else {
								   Member m=Member.dao.findById(mid);
								   Goods g=Goods.dao.findById(gid);
								   if(m==null||g==null||TextUtils.isEmpty(m.getUsername())||TextUtils.isEmpty(g.getName())) {
									     result.put("stauts",CommonSTUC.Status.ParaWrong.getIndex());
								   }else {
									   System.out.println("l1");
									   List<Product> products=Product.dao.find("select id from product where goods_id=?",g.getId());
									   if(products==null||products.size()<=0) {
										     result.put("status",CommonSTUC.Status.Illegal.getIndex());
									   }else {
										   System.out.println("l2");
										    List<Cart> carts=Cart.dao.find("select id from cart where member_id=?",mid);
										    Cart c=null;
										    if(carts==null||carts.size()<=0) {
										    	 c=new Cart();
										    	c.setCreateDate(new Date());
										    	c.setModifyDate(new Date());
										    	c.setMemberId(Long.parseLong(mid));
										    	c.setVersion(0l);
										    	c.setCartKey(System.currentTimeMillis()+"");
										    	c.setExpire(new Date());
										    	c.save();
										    }else  c=carts.get(0);
										    if(c==null||c.getId()==null) {
										    	result.put("status", CommonSTUC.Status.Fail.getIndex());
										    }else {
										    	List<CartItem> cis=CartItem.dao.find("select  * from cart_item  where cart_id=?  and product_id=?",c.getId(),products.get(0).getId());
										    	CartItem ci=null;
										    	boolean operation=false;
										    	if(cis==null||cis.size()<=0) {
										    		ci=new CartItem();
										    		ci.setCartId(c.getId());
										    		ci.setCreateDate(new Date());
										    		ci.setModifyDate(new Date());
										    		ci.setVersion(0l);
										    		ci.setProductId(products.get(0).getId());
										    		ci.setQuantity(Integer.parseInt(quantity));
										    		operation=ci.save();
										    	}else {
										    		ci=cis.get(0);
										    		ci.setQuantity(ci.getQuantity()+Integer.parseInt(quantity));
										    		operation=ci.update();
										    	}
										    	if(operation) result.put("status", CommonSTUC.Status.Success.getIndex());
										    	else result.put("status",CommonSTUC.Status.Fail.getIndex());
										    }
										    }
								   }
							 }
						 }catch(Exception e) {
							 e.printStackTrace();
							  result.put("status", CommonSTUC.Status.Error.getIndex());
						 }finally {
							 this.renderJson(result);
						 }
			}
			//购物车商品移除
				public void   cartRemove() {
					Map<String,Object> result =new HashMap<>();
						try {
							 String tds=this.getPara("Pd");
							 String mid=this.getPara("mid");
							 String reg="^\\d*$";
							 if(TextUtils.isEmpty(tds)||TextUtils.isEmpty(mid)||(!mid.matches(reg))) {
								   result.put("status", CommonSTUC.Status.LackPara.getIndex());
							 }else {
								 Member m=Member.dao.findById(mid);
								 if(m.getId()==null) {
									 result.put("status", CommonSTUC.Status.NoExist.getIndex());
								 }else {
									 List<Cart> carts=Cart.dao.find("select id from cart where member_id=?",mid);
									 if(carts==null||carts.size()<=0) {
										 result.put("status", CommonSTUC.Status.Fail.getIndex());
									 }else {
										   // StringBuilder sql=new StringBuilder("delete from cart_item where cart_id=? and product_id  in  (?)");
										    String [] ds=tds.split(",");
										    for(String d:ds) {
										    	  boolean operation=CartItem.dao.deleteById(d);
										    }
										    result.put("status", CommonSTUC.Status.Success.getIndex());
									 }
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
