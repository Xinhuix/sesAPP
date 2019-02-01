package com.jfinalshop.controller.client;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.util.TextUtils;

import com.jfinal.core.Controller;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinalshop.model.Enshrine;
import com.jfinalshop.model.Field;
import com.jfinalshop.model.Member;
import com.jfinalshop.model.Enshrine.Type;
import com.jfinalshop.model.OtherPlay;
import com.jfinalshop.model.SesStore;
import com.jfinalshop.model.Share;
import com.jfinalshop.model.client.CommonSTUC;
import com.jfinalshop.model.client.DP_STUC;

@ControllerBind(controllerKey="/client/DP")
public class DirectionPlayController extends Controller {
		static  String ERROR="error";
		static String NOEXIST="noexist";
		static String PARAWRONG="paraWrong";
		static String NOMORE="nomore";
		
	public void init() {
			try {
				String sql="";
				 String cp=this.getPara("cp");
				 String type=getPara("stype");
				 	int c=0;
				 if(!TextUtils.isEmpty(cp)) {
					c=Integer.parseInt(cp);
				 }
				 if(TextUtils.isEmpty(type)) {
					 List<Record> lr=Db.find("select id,sname as title,img,play_url as url from ses_store   limit "+(c*10)+",10");
					 List<Record> gt=Db.find("select  id,title,play_adr as url,icon_adr as img from other_play  limit  "+(c*10)+",10");
					 List<Record> lf=Db.find("select id,title,play_adr as url,img from field  limit "+(c*10)+",10");
						DP_STUC ds=new DP_STUC();
						ds.makeDPType("店铺","s", lr).makeDPType("其他","g", gt).makeDPType("基地","f", lf);
						this.renderJson(ds);
				 }else {
					 switch(type) {
						case "g":
							sql="select  id,title,play_adr as url,icon_adr as  img from other_play   limit "+(c*10)+",10";
							break;
						case "s":
							sql="select id,sname as title,img,play_url as url from ses_store   limit "+(c*10)+",10 ";
							break;
							case "f":
								sql="select  id,title,play_adr as url,img from field    limit "+(c*10)+",10";
								break;
							default:
								this.renderJson(PARAWRONG);
								break;
						}
					 List<Record> lr=Db.find(sql);
					 if(lr==null||lr.size()<=0) {
						  this.renderJson(NOMORE);
					 }else {
						 this.renderJson(lr);
					 }
				 }
				
				 //this.renderJson(ds);
			}catch(Exception e) {
				e.printStackTrace();
				this.renderJson(ERROR);
			}
	}
	
	
	/**直播数据获取
	 * @author zxx
	 * 
	 */
	public void   dpList() {
		Map<String,Object> result=new HashMap<String,Object>();
		try {
			String cpage=this.getPara("cp");
			String loginCode=this.getPara("loginCode");
			int cp=0;
			if(!TextUtils.isEmpty(cpage)) {
				 cp=Integer.parseInt(cpage)*10; 	
			}else {
				cp=0;
			}
				String st=this.getPara("stype");//s 搜索商铺 g 搜索商品
				//String id=this.getPara("type_id");
				if(TextUtils.isEmpty(st)) {
					this.renderJson(PARAWRONG);
				}else {
					String sql="";
					int type=-1;
					switch(st) {
					case "g":
						sql="select o.id as id,o.title as title,o.play_adr as url,o.icon_adr as img,count(e.id)  as enshrien,sharecount as share   from other_play  o "
								+ " left join enshrine e on o.id=e.foreign_id    and  o.status="+OtherPlay.Statu.use.getIndex()+"  "
								+ "and e.type="+Enshrine.Type.other.getIndex()+"  group by o.id   limit "+cp+",10";
						type=Enshrine.Type.other.getIndex();
						break;
					case "s":
						sql="select  s.*,count(e.id) as enshrine  from (select id, sname as title,img,play_url as url,sharecount as share   from  ses_store where play_url!='' and type_id!=2) as  s left  join  enshrine e on s.id=e.foreign_id "
								+ "     group by s.id     limit "+cp+",10  ";
						type=Enshrine.Type.store.getIndex();
						break;
						case "f":
							sql="select tmp.*,count(e.id) as enshrine from   (select  id,sname as title,img,play_url as url, sharecount as share from ses_store where  play_url!='' and type_id=2) tmp left join enshrine e on tmp.id=e.foreign_id "
									+ "  and e.type="+Enshrine.Type.field.getIndex()+"  group by tmp.id limit "+cp+",10 ";
							//sql="select f.id,f.title,f.play_adr as url,f.img,count(e.id) as enshrine,sharecount as share from field   as f  "
									//+ " left join  enshrine e on f.id=e.foreign_id and e.type="+Enshrine.Type.field.getIndex()+"   group by  f.id   limit  "+cp+",10  ";
							type=Enshrine.Type.field.getIndex();
							break;
						default:
							result.put("status", CommonSTUC.Status.Illegal.getIndex());
							break;
					}
					List<Record> lr=Db.find(sql);
					result.put("data",lr );
					if(!TextUtils.isEmpty(loginCode)) {
						List<Member> members=Member.dao.find("select id from member where logincode=? ",loginCode);
						if(members.size()<=0) {
						result.put("status",CommonSTUC.Status.NoExist.getIndex());
						result.put("data",lr );
						return ;
					}
						Member m=members.get(0);
						for(Record r:lr) {
							List<Enshrine> le=Enshrine.dao.find("select  id from enshrine where  type=? and foreign_id=? and member_id=?  ",type,r.getInt("id"),m.getId());
							r.set("isEnshrine", le.size()>0?1:0);
						}
					}
					result.put("status", CommonSTUC.Status.Success);
					result.put("data",lr );
				}
		
		}catch(Exception e) {
			e.printStackTrace();
			result.put("status",CommonSTUC.Status.Error.getIndex());
		}finally {
			this.renderJson(result);
		}
		}
	
	//直播收藏
	public void  enshrine() {
		CommonSTUC cs=new CommonSTUC();
		try {
			String   type=this.getPara("stype");
			   String  fid=this.getPara("fid");
			   String mid=this.getPara("mid");
			   if(TextUtils.isEmpty(type)||TextUtils.isEmpty(fid)||TextUtils.isEmpty(mid)) {
				  cs.setStatus(CommonSTUC.Status.LackPara.getIndex());
			   }else {
				   int  index=-1;
				    Type[] ts=Enshrine.Type.values();
				    for(Type t:ts) {
				    	if(t.getSymbol()==type.charAt(0)) {
				    		   index=t.getIndex();
				    	}
				    }
				    if(index==-1) {
				    	cs.setStatus(CommonSTUC.Status.ParaWrong.getIndex());
				    }else {
				    	List<Enshrine> le=Enshrine.dao.find("select id from enshrine where  member_id=? and  type=? and foreign_id=?",mid,index,fid);
				    	if(le.size()>0) {
				    		cs.setStatus(CommonSTUC.Status.Exist.getIndex());
				    	}else {
				    		Enshrine es=new Enshrine();
				    		es.setMemberId(Long.valueOf(mid));
				    		es.setForeignId(Long.valueOf(fid));
				    		es.setType(index);
				    		es.setCreatedate(new Date());
				    		if(es.save()) {
				    			cs.setStatus(CommonSTUC.Status.Success.getIndex());
				    		}else {
				    			cs.setStatus(CommonSTUC.Status.Fail.getIndex());
				    		}
				    	}
				    }
			   }
			
		}catch(Exception e) {
			e.printStackTrace();
			cs.setStatus(CommonSTUC.Status.Error.getIndex());
		}
		   this.renderJson(cs);
	}
	public void unEnshrine() {
		Map<String,Object> result=new HashMap<>();
			try {
				   String mid=this.getPara("mid");
				   String type=this.getPara("stype");
				   String fid=this.getPara("fid");
				   int  tindex=-1;
				   for(Enshrine.Type t:Enshrine.Type.values()) {
					   if(t.getSymbol()==type.charAt(0))tindex=t.getIndex();
				   }
				   List<Enshrine> enshrines=Enshrine.dao.find(" select   id  from  enshrine  where   foreign_id="+fid+"  and  member_id="+mid+"  and   type="+tindex);
				  Enshrine enshrine=enshrines.get(0);
				  boolean b=enshrine.delete();
				  result.put("status", b?CommonSTUC.Status.Success.getIndex():CommonSTUC.Status.Fail.getIndex());
			}catch(Exception e) {
				 e.printStackTrace();
				  result.put("status", CommonSTUC.Status.Fail.getIndex());
			}finally {
				 this.renderJson(result);
			}
	}
	//直播分享
	public void share() {
		 CommonSTUC cs=new CommonSTUC();
		 try {
			  String  type=this.getPara("stype");
			  String  mid=this.getPara("mid");
			  String fid=this.getPara("fid");
			 if(TextUtils.isEmpty(type)||TextUtils.isEmpty(mid)||TextUtils.isEmpty(fid)) {
				 		cs.setStatus(CommonSTUC.Status.LackPara.getIndex());
			 }else {
				 			int index=-1;
				 			for(Type t:Enshrine.Type.values()) {
				 				  if(t.getSymbol()==type.charAt(0)) {
				 					  index=t.getIndex();
				 					  break;
				 				  }
				 			}
				 			if(index==-1)cs.setStatus(CommonSTUC.Status.ParaWrong.getIndex());
				 			else {
				 				List<Share> shares=Share.dao.find("select   id,member_id,type,foreign_id,platform from share where member_id=? and foreign_id"
				 						+ "=? and  type=? ",mid,fid,type);
				 				if(shares==null||shares.size()<=0) {
				 					 Share s=new Share();
				 					s.setCount(1l);
				 					s.setForeignId(Long.parseLong(fid));
				 					s.setMemberId(Long.parseLong(mid));
				 					s.setType(index);
				 					cs.setStatus(s.save()?CommonSTUC.Status.Success.getIndex():CommonSTUC.Status.Fail.getIndex());
				 				}else {
				 					Share s=shares.get(0);
				 					s.setCount(s.getCount()+1);
				 					boolean  operation=false;
				 				    switch(type) {
				 				    case "f":
				 				    	 		Field f=Field.dao.findById(fid);
				 				    	 		f.setSharecount(f.getSharecount()+1);
				 				    	 		operation=f.update();
				 				    	break;
				 				    case "g":
				 				    	  OtherPlay op=OtherPlay.dao.findById(fid);
				 				    	  op.setSharecount(op.getSharecount()+1);
				 				    	  operation=op.update();
				 				    	break;
				 				    case "s":
				 				    	SesStore ss=SesStore.dao.findById(fid);
				 				    	ss.setSharecount(ss.getSharecount()+1);
				 				    	operation=ss.update();
				 				    	break;
				 				    }
				 				    cs.setStatus((operation&&s.update())?CommonSTUC.Status.Success.getIndex():CommonSTUC.Status.Fail.getIndex());
				 				}
				 			}
				 	
			 }
		 }catch(Exception e) {
			 cs.setStatus(CommonSTUC.Status.Error.getIndex());
		 }finally {
			 this.renderJson(cs);
		 }
	}
		
}
