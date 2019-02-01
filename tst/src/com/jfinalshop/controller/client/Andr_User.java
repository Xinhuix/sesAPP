package com.jfinalshop.controller.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.http.util.TextUtils;

import com.jfinal.core.Controller;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinalshop.Principal;
import com.jfinalshop.Setting;
import com.jfinalshop.entity.SafeKey;
import com.jfinalshop.model.Area;
import com.jfinalshop.model.Level;
import com.jfinalshop.model.LevelClause;
import com.jfinalshop.model.Member;
import com.jfinalshop.model.PaymentLog;
import com.jfinalshop.model.Receiver;
import com.jfinalshop.model.SCardorder;
import com.jfinalshop.model.SRecommend;
import com.jfinalshop.model.client.CommonSTUC;
import com.jfinalshop.model.client.Msg;
import com.jfinalshop.model.client.UserObj;
import com.jfinalshop.service.MailService;
import com.jfinalshop.service.MemberRankService;
import com.jfinalshop.service.MemberService;
import com.jfinalshop.service.SmsService;
import com.jfinalshop.util.SMSUtils;
import com.jfinalshop.util.SystemUtils;
import com.jfinalshop.util.WebUtils;

@ControllerBind(controllerKey="/client/user")
public class Andr_User extends Controller {

	private MemberService ms=enhance(MemberService.class);
	private  final String NO_EXIST="noexist";
	private final String PWD_WRONG="pwdwrong";
	private final String EXISTS="exists";
	private final String REG_OK="regok";
	private final String REG_FAIL="regfail";
	private SmsService smsservice=this.enhance(SmsService.class);
	private MemberService memberservice=this.enhance(MemberService.class);
	private   static   Map<String,String> msgCodes=new HashMap<>();
	private MemberRankService memberRankService = enhance(MemberRankService.class);
	char[] logincodes=new char[] {'f','d','a','c','b','z','3','8','0','1','h','j'};
	MailService mailService=enhance(MailService.class);
	
	public void login() {
		String uname=getPara("username");
		UserObj ob=new UserObj();
		String pwd=getPara("pwd").toString().toLowerCase().trim();
		List<Member> lm=Member.dao.find("select  balance, nickname,portrait,id,password  from member where  username=? ",uname);
		if(lm.size()==0) {
			ob.setStatus(UserObj.Status.NoExist.getIndex());
		}else {
				if(lm.get(0).getPassword().equals(pwd)) {
						lm.get(0).setUsername(uname);
						StringBuffer sb=new StringBuffer();
						for(int i=0;i<logincodes.length;i++) {
							int index=(int) Math.round(Math.random()*11);
							 sb.append(logincodes[index]);
						}
						Member member=lm.get(0);
						member.setLogincode(sb.toString());
						member.update();
						member.setPassword("");
						ob.setM(member);
						ob.setStatus(UserObj.Status.Success.getIndex());
						this.renderJson(ob);
					//	lm.get(0).setPassword(pwd);
						//lm.get(0).update();
				}else {
					ob.setStatus(UserObj.Status.Error.getIndex());
				}
		}
		this.renderJson(ob);
	}
	
	public void keeplogin() {
		  String logincode=getPara("logincode");
		  if(logincode==null)return;
		  List<Member> lm=Member.dao.find("select * from  member where logincode="+logincode);
		  if(lm.size()>0) {
			  setSessionAttr(Member.PRINCIPAL_ATTRIBUTE_NAME, new Principal(lm.get(0).getId(), lm.get(0).getUsername()));
				WebUtils.addCookie(this.getRequest(),this.getResponse(), Member.USERNAME_COOKIE_NAME, lm.get(0).getUsername());
				List<Record> lr=Db.find("select  sum(amount)  from s_cardorder where  member_id="+lm.get(0).getId()+"  and  (status=1 or status=6)");
			if(lr.size()>0) {
				BigDecimal db=lr.get(0).getBigDecimal("sum(amount)");
				System.out.println(""+db);
				this.setAttr("cc",""+db);
			}
		  }
			this.render("/wap/index.ftl");
			lm.get(0).setLogincode("");
			lm.get(0).update();
	}

	public void checkNumber() {
				String mobile=this.getPara("mobile");
				List<Member> ms=Member.dao.find(" select id from  member where username='"+mobile+"' ");
				if(ms==null||ms.size()<=0) {
					this.renderJson("noexist");
				}else {
					this.renderJson("exist");
				}
	}
	
	public void sendMsg() {
		String mobile=getPara("mobile");
		Msg m=new Msg();
		if(TextUtils.isEmpty(mobile)) {
			m.setStatus("lackpara");
		}else {
			String reg="^1[3-9][0-9][0-9]{8}$";
			//String reg="^(13|14|15|17|18|19|16)\\\\d{9}$";
			String str=null;
			
			if(mobile.matches(reg)) {
					 str=SMSUtils.randomSMSCode(6);
					smsservice.send(mobile, str);
					m.setStatus("success");
					m.setMsgCode(str);
					msgCodes.put(str, System.currentTimeMillis()+"");
			}else
				m.setStatus("numberInCorrect");
		}
		this.renderJson(m);
	}
	
	public void register() {
		    CommonSTUC cs=new CommonSTUC();
		HttpServletRequest request=getRequest();
				String uname=this.getPara("uname");
				String pwd=this.getPara("pwd").toLowerCase();
				String reccode=this.getPara("introduceCode");
				String area_id=this.getPara("area_id");
				String msgCode=this.getPara("msgCode");
				if(TextUtils.isEmpty(uname)||TextUtils.isEmpty(pwd)) {
					cs.setStatus(CommonSTUC.Status.LackPara.getIndex());
					this.renderJson(cs);
					  return;
				}else if(Member.dao.find("select id from member where username=?",uname).size()>0) {
					cs.setStatus(CommonSTUC.Status.Exist.getIndex());
					this.renderJson(cs);
					return;
				}
				else if(TextUtils.isEmpty(msgCode)||msgCodes.get(msgCode)==null) {
					cs.setStatus(CommonSTUC.Status.CodeError.getIndex());
					this.renderJson(cs);
					return;
				}else  {
					boolean  isout=(System.currentTimeMillis()-Long.parseLong(msgCodes.get(msgCode)))/1000<61;
					if(!isout) {
						cs.setStatus(CommonSTUC.Status.OutTime.getIndex());
						  return;
					}
				}
				Member member=new Member();
				member.setUsername(uname);
				member.setPassword(pwd);
				member.setNickname(null);
				member.setPoint(0d);
				member.setBalance(BigDecimal.ZERO);
				member.setAmount(BigDecimal.ZERO);
				member.setIsEnabled(true);
				member.setIsLocked(false);
				member.setLoginFailureCount(0);
				member.setLockedDate(null);
				member.setRegisterIp(request.getRemoteAddr());
				member.setLoginIp(request.getRemoteAddr());
				member.setLoginDate(new Date());
				member.setLoginPluginId(null);
				member.setOpenId(null);
				member.setLockKey(DigestUtils.md5Hex(UUID.randomUUID() + RandomStringUtils.randomAlphabetic(30)));
				member.setSafeKey(null);
				member.setMemberRankId(memberRankService.findDefault() != null ? memberRankService.findDefault().getId() : null);
				member.setCart(null);
				member.setOrders(null);
				member.setPaymentLogs(null);
				member.setDepositLogs(null);
				member.setCouponCodes(null);
				member.setReceivers(null);
				member.setReviews(null);
				member.setConsultations(null);
				member.setFavoriteGoods(null);
				member.setProductNotifies(null);
				member.setInMessages(null);
				member.setOutMessages(null);
				member.setPointLogs(null);
				if(!TextUtils.isEmpty(area_id)) {
					member.setRegAreaId(Integer.parseInt(area_id));
				}
				member=memberservice.save(member);
						if (!StringUtils.isEmpty(reccode)&&reccode.length()>6) { //邀请码 第七位 开始为 userid
							Long user_id= Long.parseLong(reccode.substring(6, reccode.length())); 
							if(memberservice.find(user_id) != null){
							SRecommend recommend=new SRecommend();
							recommend.setMemberId(memberservice.find(user_id).getId()); //推荐人(会员ID)获得推荐积分人
							recommend.setToMemberId(member.getId());//被推荐人(会员ID)
							recommend.setStatus("0");
							recommend.save();
							}
						}
						if(member!=null&&member.getId()>0)cs.setStatus(CommonSTUC.Status.Success.getIndex());
						else cs.setStatus(CommonSTUC.Status.Fail.getIndex());
						this.renderJson(cs);
	}
	
	//修改密码
	public void updatePwd() {
		CommonSTUC cs=new CommonSTUC();
			try {
				String uname=this.getPara("username");
				String pwd=this.getPara("pwd");
				String msgCode=this.getPara("msgCode");
				if(TextUtils.isEmpty(uname)||TextUtils.isEmpty(pwd)||TextUtils.isEmpty(msgCode)) {
				cs.setStatus(CommonSTUC.Status.LackPara.getIndex());
				}else {
				    if(msgCodes.get(msgCode)==null) {
				    	  cs.setStatus(CommonSTUC.Status.ParaWrong.getIndex());
				    }else {
				    	boolean   ot=(System.currentTimeMillis()-Long.parseLong(msgCodes.get(msgCode)))/1000<61;
				    	if(!ot) {
				    		cs.setStatus(CommonSTUC.Status.OutTime.getIndex());
				    	}else {
				    		Member m=memberservice.findByUsername(uname);
							if(m==null||TextUtils.isEmpty(m.getUsername())) {
								    cs.setStatus(CommonSTUC.Status.NoExist.getIndex());
							}else {
								 m.setPassword(pwd.toLowerCase());
								   if(m.update()) {
									cs.setStatus(CommonSTUC.Status.Success.getIndex());
								   }else {
									   cs.setStatus(CommonSTUC.Status.Fail.getIndex());
								   }
								   msgCodes.remove(msgCode);
							}
				    	}
				    }
				}
			}catch(Exception e) {
				cs.setStatus(CommonSTUC.Status.Error.getIndex());
			}finally {
				this.renderJson(cs);
			}
	}
	
	//预查是否存在邀请码
	public void   checkIntroduceCode() {
			  try {
				    String code=this.getPara("introductCode");
				    if(TextUtils.isEmpty(code)||code.length()>6) {
				    	 this.renderJson("illegal");
				    }else {
				         long uid=Long.parseLong(code.substring(6));
				         Member m=Member.dao.findById(uid);
				         if(m==null||m.getUsername()==null) {
				        	      this.renderJson("noexist");
				         }else {
				        	 this.renderJson("exist");
				         }
				    }
			  }catch(Exception e) {
				    this.renderJson("error");
			  }
	}
	public void up() {
		
	}
	
	public void address() {
		   String pd=this.getPara("grade");
		   List<Area> la=new ArrayList<>();
		   String sql="select id,name from  area  where 1=1  ";
		   if(TextUtils.isEmpty(pd)) {
			   sql+="  and  parent_id is null  ";
		   }else {
			   sql+="  and  parent_id='"+pd+"'  ";
		   }
		  la=Area.dao.find(sql);
		  if(la==null||la.size()<=0) {
			    this.renderJson("nomore");
		  }else {
			  this.renderJson(la);
		  }
	}
	
	public void upPortrait() {
		Map<String,Object> result=new HashMap<>();
		try {
			File of=this.getFile("portrait").getFile();
			String id=this.getPara("id");
			Member m=Member.dao.findById(id);
			if(m==null||m.getUsername()==null) {
				result.put("status", CommonSTUC.Status.Fail);
			}else {
				String base =this.getRequest().getServletContext().getRealPath("/");
				   String subpath="upload/user/portrait";
				   File dir=new File(base+subpath);
				   if(!dir.exists()) {
					    boolean b=dir.mkdirs();
					   if(!b) {
						   result.put("status", CommonSTUC.Status.Fail.getIndex());
						   return;
					   }
				   }
				   String suffix=of.getName().split("\\.")[1];
				   System.out.println(suffix);
				   String npath=subpath+"/portrait_"+m.getUsername()+"."+suffix;
				   File nf=new File(base+npath);
				   if(!nf.exists()) {
					   nf.createNewFile();
				   }
				   if(of.length()<=0) {
					   result.put("status", CommonSTUC.Status.Fail);
				   }else {
					   FileInputStream fis=new FileInputStream(of);
					   byte [] bs=new byte[1024];
					   int len=-1;
					   FileOutputStream fos=new FileOutputStream(nf);
					 while((len=fis.read(bs))!=-1) {
						 fos.write(bs);
					 }
					 fis.close();
					 fos.close();
					of.delete();
					m.setPortrait(npath);
					boolean b=m.update();
					if(b) {
						result.put("status", CommonSTUC.Status.Success.getIndex());
						result.put("portrait", m.getPortrait());
					}else {
						result.put("status", CommonSTUC.Status.Fail.getIndex());
						nf.delete();
					}
				   }
			}
		}catch(Exception e) {
			  e.printStackTrace();
			  result.put("status",CommonSTUC.Status.Error.getIndex());
		}finally {
			 this.renderJson(result);
		} 
	}
	
	public void info() {
		Map<String,Object> result=new HashMap<>();
		   		String id=this.getPara("id");
		   	     try {
		   	    	 if(TextUtils.isEmpty(id)||!id.matches("^\\d*$")) {
		   	    		 result.put("status",CommonSTUC.Status.ParaWrong.getIndex());
		   	    		 this.renderJson(result);
		   	    		 return;
		   	    	 }
		   	    	 List<Record> lm=Db.find("select  m.id, m.nickname,m.username,m.email, m.gender,m.portrait,m.avatar,m.level_id,a.name as area "
		   	    	 		+ "  from member m inner join area a on m.reg_area_id=a.id and m.id="+id);
		   	    	 if(lm==null||lm.size()<=0) {
		   	    		 result.put("status", CommonSTUC.Status.NoExist.getIndex());
		   	    	 }else {
		   	    		 Record r=lm.get(0);
		   	    		  r.set("nickname",TextUtils.isEmpty(r.getStr("nickname"))?randomNickName().toUpperCase():r.get("nickname"));
		   	    		  r.set("email", TextUtils.isEmpty(r.getStr("email"))?"":r.get("email"));
		   	    		  r.set("portrait",TextUtils.isEmpty(r.getStr("avatar"))?"":r.getStr("avatar"));
		   	    		  r.set("gender",TextUtils.isEmpty(r.get("gender")+"")?"-1":r.get("gender"));
		   	    		 result.put("status", CommonSTUC.Status.Success.getIndex());
		   	    		List<Record> cardOrders=Db.find("select  sum(amount) from s_cardorder  where status in(1,3,6) and member_id="+r.getLong("id"));
		   	    		Record rr=cardOrders.get(0);
		   	    		Double b=(rr==null||rr.getBigDecimal("sum(amout")==null)?0:cardOrders.get(0).getBigDecimal("sum(amount)").doubleValue();
		   	    		List<Level> ls=Level.dao.find("select id,levelname  from level");
		   	    		
		   	    		if(ls==null||ls.size()<=0) {
		   	    			result.put("level","未知等级");
		   	    		}else {
		   	    			for(Level l:ls) {
		   	    				List<LevelClause> clauses=l.getClause();
		   	    				boolean bl=true;
		   	    				for(LevelClause lc:clauses) {
		   	    						String symbol=lc.getClause().substring(0,1);
		   	    						int num=Integer.parseInt(lc.getClause().substring(1,lc.getClause().length()));
		   	    						switch(symbol) {
		   	    						case ">":
		   	    							bl=(b-num>=0);
		   	    							break;
		   	    						case "<":
		   	    						bl=(b-num<=0);
		   	    							break;
		   	    						case "=":
		   	    							bl=(b==num);
		   	    							break;
		   	    						}
		   	    						System.out.println(l.getLevelname()+"\n"+bl);
		   	    						if(!bl)break;
		   	    				}
		   	    				if(bl) {
		   	    					result.put("level",l.getLevelname());
		   	    					r.set("level_id", l.getId());
		   	    					break;
		   	    				}
		   	    		}
		   	    		String area=r.getStr("area");
		   	    		r.remove("area");
		   	    	  Db.update("member",r);
		   	    	result.put("area", area);
		   	    	r.remove("level_id");
		   	    	r.remove("id");
		   	    	 result.put("info",r);
		   	    	 if(result.get("level")==null) {
		   	    		 result.put("level", "未知等级");
		   	    	 }
		   	    	 List<Record>  rs=Db.find(" select  id from s_recommend   where  member_id=?",id);
		   	    	 result.put("introducecount", rs.size());
		   	    	 }
		   	    	 }
		   	     }catch(Exception e) {
		   	    	 e.printStackTrace();
		   	    	 result.put("status",CommonSTUC.Status.Error.getIndex());
		   	     }finally {
		   	    	 this.renderJson(result);
		   	     }
	}
	public void checkEmail() {
		Member member=Member.dao.findById("907");
		Setting setting = SystemUtils.getSetting();
		SafeKey safeKey = new SafeKey();
		safeKey.setValue(DigestUtils.md5Hex(UUID.randomUUID() + RandomStringUtils.randomAlphabetic(30)));
		safeKey.setExpire(setting.getSafeKeyExpiryTime() != 0 ? DateUtils.addMinutes(new Date(), setting.getSafeKeyExpiryTime()) : null);
		member.setSafeKeyExpire(safeKey.getExpire());
		member.setSafeKeyValue(safeKey.getValue());
		member.setSafeKey(safeKey);
		member.update();
		mailService.sendFindWapPasswordMail("zxxyule@163.com", member.getUsername(), safeKey);
	}
	
	public void setInfo() {
				Map<String,Object> result=new HashMap<>();
				try {
					String reg="^\\d+$";
					//String reg2="^1[3-9]{1}[0-9]{9}$";
					//String reg3="^[a-zA-Z0-9_]+@[a-zA-Z0-9]+\\.com$";
					String id=this.getPara("id");
					  String nick=this.getPara("nickname");
					  String email=this.getPara("email");
					  if(nick!=null||!TextUtils.isEmpty(nick)) {
						  nick=URLDecoder.decode(this.getRequest().getParameter("nickname"),"UTF-8");
					  }
					  if(email!=null||!TextUtils.isEmpty(email)) {
						  email=URLDecoder.decode(email,"UTF-8");
					  }
					  //String num=this.getPara("mobile");
					  String gender=this.getPara("gender");
					  //String city=this.getPara("city");
					
					 if(!id.matches(reg))
					 {
						   result.put("status", CommonSTUC.Status.ParaWrong.getIndex());
					 }else {
						 	Member m=Member.dao.findById(id);
						 	if(m==null||TextUtils.isEmpty(m.getUsername())) {
						 		   result.put("status", CommonSTUC.Status.NoExist.getIndex());
						 		   return;
						 	}
						 	m.setNickname(nick);
						 	m.setGender(Integer.parseInt(gender));
						 	m.setEmail(email);
						 	boolean b=m.update();
						 	result.put("status",b?1:2);
					 }
				}catch(Exception e) {
					e.printStackTrace();
					result.put("status", CommonSTUC.Status.Error.getIndex());
				}finally {
					this.renderJson(result);
				}
	}
	public String randomNickName() {
		 // char [] c= {'f','B','E','S','E','E','E','E','Y','a','b','c','d','e','f','g','e'};
		  StringBuilder sb=new StringBuilder();
		  for(int i=0;i<10;i++) {
			  double b=Math.random()*1000;
			  int is=Integer.parseInt(Math.round(b)+" ");
			  sb.append(logincodes[is%logincodes.length]);
		  }
		  return sb.toString();
	}
}
