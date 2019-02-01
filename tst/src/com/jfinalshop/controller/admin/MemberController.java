package com.jfinalshop.controller.admin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.TextUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.render.Render;
import com.jfinalshop.Message;
import com.jfinalshop.Pageable;
import com.jfinalshop.Setting;
import com.jfinalshop.model.Atcrecord;
import com.jfinalshop.model.DepositLog;
import com.jfinalshop.model.Member;
import com.jfinalshop.model.MemberAttribute;
import com.jfinalshop.model.Page;
import com.jfinalshop.model.PointLog;
import com.jfinalshop.model.PointLog.Type;
import com.jfinalshop.model.SCard;
import com.jfinalshop.model.SCardmonth;
import com.jfinalshop.model.SCardorder;
import com.jfinalshop.service.AdminService;
import com.jfinalshop.model.Order;
import com.jfinalshop.model.OrderItem;
import com.jfinalshop.model.Receiver;
import com.jfinalshop.model.SCard;
import com.jfinalshop.model.SCardmonth;
import com.jfinalshop.model.SCardorder;
import com.jfinalshop.model.SRecommend;
import com.jfinalshop.service.MemberAttributeService;
import com.jfinalshop.service.MemberRankService;
import com.jfinalshop.service.MemberService;
import com.jfinalshop.service.PluginService;
import com.jfinalshop.service.ses.CardorderService;
import com.jfinalshop.util.SystemUtils;

/**
 * Controller - 会员
 * 
 * 
 */
@ControllerBind(controllerKey = "/admin/member")
public class MemberController extends BaseController {

	private MemberService memberService = enhance(MemberService.class);
	private MemberRankService memberRankService = enhance(MemberRankService.class);
	private MemberAttributeService memberAttributeService = enhance(MemberAttributeService.class);
	private PluginService pluginService = new PluginService();
	private CardorderService cardOrderService=this.enhance(CardorderService.class);
	private AdminService adminService=this.enhance(AdminService.class);
	/**
	 * 检查用户名是否被禁用或已存在
	 */
	public void checkUsername() {
		String username = getPara("member.username");
		if (StringUtils.isEmpty(username)) {
			renderJson(false);
			return;
		}
		renderJson(!memberService.usernameDisabled(username) && !memberService.usernameExists(username));
	}

	/**
	 * 检查E-mail是否唯一
	 */
	public void checkEmail() {
		String previousEmail = getPara("previousEmail");
		String email = getPara("member.email");
		if (StringUtils.isEmpty(email)) {
			renderJson(false);
			return;
		}
		renderJson(memberService.emailUnique(previousEmail, email));
	}

	/**
	 * 查看
	 */
	public void view() {
		Long id = getParaToLong("id");
		Member member = memberService.find(id);
		setAttr("genders", Member.Gender.values());
		setAttr("memberAttributes", memberAttributeService.findList(true, true));
		setAttr("member", member);
		setAttr("loginPlugin", pluginService.getLoginPlugin(member.getLoginPluginId()));
//<<<<<<< HEAD
		setAttr("cardOrder",this.cardOrderService.getCard(id.intValue()));
		List<Record> atcrecord=Db.find("select   receivedate,receivecount,atype from atcrecord where  member_id=?",id);
		List<Record> pointLog=Db.find("select   create_date,balance,credit,debit,operator,type  from point_log where member_id=?",id);
		List<Record> depositLog=Db.find("select 	create_date,balance,credit,debit,operator,type from deposit_log where member_id=?",id);
		Map<Integer,String> plType=new HashMap<>();
		for(Type pl:PointLog.Type.values()) {
			  plType.put(pl.ordinal(), pl.getName());
		}
		Map<Integer,String> dlType=new HashMap<>();
		for(DepositLog.Type t:DepositLog.Type.values()) {
			  dlType.put(t.ordinal(), t.getName());
		}
		Map<Integer,String> atcType=new HashMap<>();
		for(Atcrecord.Type t:Atcrecord.Type.values()) {
			 atcType.put(t.getIndex(), t.getName());
		}
		this.setAttr("atcType",atcType );
		this.setAttr("depositType", dlType);
		this.setAttr("pointType", plType);
		this.setAttr("atcRecord", atcrecord);
		this.setAttr("pointLog",pointLog );
		this.setAttr("depositLog", depositLog);
//=======
//		//查询地址
//		setAttr("address", Receiver.dao.find("select * from receiver where  member_id=?",id));
//		//查询推荐人数
//			SRecommend	sRecommend = new SRecommend();
//		//我推荐的人	
//		setAttr("ListSRecommend", sRecommend.dao.findListSRecommend(id));
//		//推荐我的人
//		setAttr("TomemberidTwo", sRecommend.dao.findByTomemberidTwo(id));
//		//查看卡包
//		setAttr("cardorder", SCardorder.dao.find("select * from s_cardorder where member_id=?",id));
//		//订单
//		setAttr("Orders", Order.dao.find("select * from `order` where member_id =? ORDER BY 	create_date desc",id));
//				
//>>>>>>> 35d36332ba85e3ce0f2f3adfe6cdd8f090784c82
		render("/admin/member/view.ftl");
	}
	
	/**
	 * 修改积分or粮票
	 */
	public void adjust() {
		Map<String,Object> result=new HashMap<>();
		try {
			String username=this.getPara("username");
			String type=this.getPara("type");
			int num=this.getParaToInt("num");
			Member m=this.memberService.findByUsername(username);
			if(m==null||TextUtils.isEmpty(m.getUsername())) {
				throw new Exception();
			}else {
				  BigDecimal balance=type.equals("1")?BigDecimal.valueOf(m.getPoint()):m.getBalance();
				  if(balance.doubleValue()+num<0) {
					  throw new Exception();
				  }
				 if(type.equals("1")) {
					 this.memberService.addPoint(m, num, PointLog.Type.adjustment,this.adminService.getCurrent() ,"后台积分调整");
				 }else if(type.equals("2")) {
					  this.memberService.addBalance(m, BigDecimal.valueOf(num), DepositLog.Type.adjustment, adminService.getCurrent(),"后台粮票调整");
				 }
				 result.put("msg", "修改成功");
			}
		}catch(Exception e) {
			System.out.println("go");
			  result.put("msg", "修改失败");
		}finally {
			System.out.println("finally go");
			this.renderJson("result",result);
		}
	}
  /**
   * 删除卡
   */
	public void delCard() {
		Map<String,Object> result=new HashMap<>();
		try {
			 long id=this.getParaToLong("id");
			 this.cardOrderService.delete(id);
			 result.put("status", 1);
			 result.put("msg", "删除成功");
		}catch(Exception e){
			e.printStackTrace();
			result.put("status", 2);
			result.put("msg", "删除失败");
		}finally {
			this.renderJson(result);
		}
		    
	}
	/**
	 * 添加
	 */
	public void add() {
		setAttr("genders", Member.Gender.values());
		setAttr("memberRanks", memberRankService.findAll());
		setAttr("memberAttributes", memberAttributeService.findList(true, true));
		render("/admin/member/add.ftl");
	}

	/**
	 * 保存
	 */
	public void save() {
		Member member = getModel(Member.class);
		Long memberRankId = getParaToLong("memberRankId");
		member.setMemberRankId(memberRankService.find(memberRankId).getId());
		
		Boolean isEnabled = getParaToBoolean("isEnabled", false);
		member.setIsEnabled(isEnabled);
		
		Setting setting = SystemUtils.getSetting();
		if (member.getUsername().length() < setting.getUsernameMinLength() || member.getUsername().length() > setting.getUsernameMaxLength()) {
			redirect(ERROR_VIEW);
			return;
		}
		if (member.getPassword().length() < setting.getPasswordMinLength() || member.getPassword().length() > setting.getPasswordMaxLength()) {
			redirect(ERROR_VIEW);
			return;
		}
		if (memberService.usernameDisabled(member.getUsername()) || memberService.usernameExists(member.getUsername())) {
			redirect(ERROR_VIEW);
			return;
		}
		if (!setting.getIsDuplicateEmail() && memberService.emailExists(member.getEmail())) {
			redirect(ERROR_VIEW);
			return;
		}
		member.removeAttributeValue();
		for (MemberAttribute memberAttribute : memberAttributeService.findList(true, true)) {
			String[] values = getRequest().getParameterValues("memberAttribute_" + memberAttribute.getId());
			if (!memberAttributeService.isValid(memberAttribute, values)) {
				redirect(ERROR_VIEW);
				return;
			}
			Object memberAttributeValue = memberAttributeService.toMemberAttributeValue(memberAttribute, values);
			if (StrKit.notNull(memberAttributeValue)) {
				member.setAttributeValue(memberAttribute, memberAttributeValue);
			}
		}
		member.setPassword(DigestUtils.md5Hex(member.getPassword()));
		member.setPoint(0d);
		member.setBalance(BigDecimal.ZERO);
		member.setAmount(BigDecimal.ZERO);
		member.setIsLocked(false);
		member.setLoginFailureCount(0);
		member.setLockedDate(null);
		member.setRegisterIp(getRequest().getRemoteAddr());
		member.setLoginIp(null);
		member.setLoginDate(null);
		member.setLoginPluginId(null);
		member.setOpenId(null);
		member.setLockKey(null);
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
		member.setUsername(StringUtils.lowerCase(member.getUsername()));
		member.setEmail(StringUtils.lowerCase(member.getEmail()));
		member.setLockKey(DigestUtils.md5Hex(UUID.randomUUID() + RandomStringUtils.randomAlphabetic(30)));
		memberService.save(member);
		addFlashMessage(SUCCESS_MESSAGE);
		redirect("list.jhtml");
	}

	/**
	 * 编辑
	 */
	public void edit() {
		Long id = getParaToLong("id");
		Member member = memberService.find(id);
		setAttr("genders", Member.Gender.values());
		System.out.println(Member.Gender.values()[0]);
		setAttr("memberRanks", memberRankService.findAll());
		setAttr("memberAttributes", memberAttributeService.findList(true, true));
		setAttr("member", member);
		setAttr("loginPlugin", pluginService.getLoginPlugin(member.getLoginPluginId()));
		render("/admin/member/edit.ftl");
	}

	/**
	 * 更新
	 */
	public void update() {
		Member member = getModel(Member.class);
		Long memberRankId = getParaToLong("memberRankId");
		//member.setMemberRank(memberRankService.find(memberRankId));
		member.setMemberRankId(memberRankService.find(memberRankId).getId());
		
		Setting setting = SystemUtils.getSetting();
		if (member.getPassword() != null && (member.getPassword().length() < setting.getPasswordMinLength() || member.getPassword().length() > setting.getPasswordMaxLength())) {
			redirect(ERROR_VIEW);
			return;
		}
		Member pMember = memberService.find(member.getId());
		if (pMember == null) {
			redirect(ERROR_VIEW);
			return;
		}
		if (!setting.getIsDuplicateEmail() && !memberService.emailUnique(pMember.getEmail(), member.getEmail())) {
			redirect(ERROR_VIEW);
			return;
		}
		member.removeAttributeValue();
		for (MemberAttribute memberAttribute : memberAttributeService.findList(true, true)) {
			String[] values = getRequest().getParameterValues("memberAttribute_" + memberAttribute.getId());
			if (!memberAttributeService.isValid(memberAttribute, values)) {
				redirect(ERROR_VIEW);
				return;
			}
			Object memberAttributeValue = memberAttributeService.toMemberAttributeValue(memberAttribute, values);
			if (StrKit.notNull(memberAttributeValue)) {
				member.setAttributeValue(memberAttribute, memberAttributeValue);
			}
		}
		if (StringUtils.isEmpty(member.getPassword())) {
			member.setPassword(pMember.getPassword());
		} else {
			member.setPassword(DigestUtils.md5Hex(member.getPassword()));
		}
		
		if (pMember.getIsLocked()) {
			member.setIsLocked(false);
			member.setLoginFailureCount(0);
			member.setLockedDate(null);
		} else {
			member.setIsLocked(pMember.getIsLocked());
			member.setLoginFailureCount(pMember.getLoginFailureCount());
			member.setLockedDate(pMember.getLockedDate());
		}
		member.setEmail(StringUtils.lowerCase(member.getEmail()));
		member.remove("username");
		
		memberService.update(member);
		addFlashMessage(SUCCESS_MESSAGE);
		redirect("list.jhtml");
	}

	/**
	 * 列表
	 */
	public void list() {
		Pageable pageable = getBean(Pageable.class);
		setAttr("memberRanks", memberRankService.findAll());
		setAttr("memberAttributes", memberAttributeService.findAll());
		setAttr("pageable", pageable);
		setAttr("page", memberService.findPage(pageable));
		render("/admin/member/list.ftl");
	}

	/**
	 * 删除
	 */
	public void delete() {
		Long[] ids = getParaValuesToLong("ids");
		if (ids != null) {
			for (Long id : ids) {
				Member member = memberService.find(id);
				if (member != null && member.getBalance().compareTo(BigDecimal.ZERO) > 0) {
					renderJson(Message.error("admin.member.deleteExistDepositNotAllowed", member.getUsername()));
					return;
				}
			}
			memberService.delete(ids);
		}
		renderJson(SUCCESS_MESSAGE);
	}
	/**
	 * 每日分红
	 */
	
	public void allocate() {
		 List<Record> lr=Db.find("select card_id,count(*) from s_cardorder group by card_id");
		 int sum=0;
		 String ln="";
		 for(Record r:lr) {
			 int id=Integer.parseInt(r.getLong("card_id")+"");
			 sum=Integer.parseInt(r.getLong("count(*)")+"");
			 switch(id) {
			 case 7:
				 ln="l2";
				 break;
			 case 8:
				 ln="l3";
				 break;
			 case 9:
				 ln="l4";
				 break;
			 case 10:
				 ln="l5";
				 break;
			 case 13:
				 ln="l1";
				 break;
			 }
			 this.setAttr(ln, sum);
		 }
		String base=this.getRequest().getServletContext().getRealPath("/");
		File f=new File(base+"WEB-INF/classes/allocateSetting.xml");
		SAXReader reader=new SAXReader();
		try {
			if(!f.exists()) {
				f.createNewFile();
				Document dc=DocumentHelper.createDocument();
				Element root=dc.addElement("root");
				FileOutputStream fos=new FileOutputStream(f);
				OutputFormat format=OutputFormat.createPrettyPrint();
				format.setEncoding("UTF-8");
				XMLWriter write=new XMLWriter(fos,format);
				write.write(dc);
				write.flush();
				write.close();
			}else {
			Document doc=reader.read(f);
			Element enabled=doc.getRootElement().element("enabled");
			if(enabled!=null) {
				String ena=enabled.getText();
				String type=doc.getRootElement().element("type").getText();
				List<Element> le=doc.getRootElement().elements("level");
				for(Element e:le) {
					this.setAttr(e.attributeValue("id")+"scale", e.element("scale").getText());
					this.setAttr(e.attributeValue("id")+"count",e.element("count").getText());
				}
				this.setAttr("amt", doc.getRootElement().element("amount").getText());
			}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			this.render("allocate.ftl");
			e.printStackTrace();
		}
		this.render("allocate.ftl");
	}
	/**
	 * 每日分红设置
	 */
	public void allocateSetting() {
		
		String isenabled=getPara("isenabled");
		String type=getPara("type");
		File f=new File(this.getRequest().getServletContext().getRealPath("/"+"WEB-INF/classes/allocateSetting.xml"));
		HttpServletResponse response=this.getResponse();
		try {
			Document doc=new SAXReader().read(f);
			Element root=doc.getRootElement();
			if(root.element("enabled")==null) {
				Element enabled=root.addElement("enabled");
				enabled.setText(isenabled);
				Element types=root.addElement("type");
				types.setText(type);
				int i=0;
				
				while(i<5) {
					Element level=root.addElement("level");
					level.addAttribute("id", "l"+(i+1));
					Element scale=level.addElement("scale");
					scale.setText(getPara("l"+(i+1)));
					Element count=level.addElement("count");
					count.setText(getPara("tips"+(i+2)));
					i++;
				}
				Element amt=root.addElement("amount");
				amt.setText(getPara("amount"));
				FileOutputStream fos=new FileOutputStream(f);
				OutputFormat format=OutputFormat.createPrettyPrint();
				format.setEncoding("UTF-8");
				XMLWriter write=new XMLWriter(fos,format);
				write.write(doc);
				write.flush();
				write.close();
			}else {
				String l1=getPara("l1");
				String tips2=getPara("tips2");
				String l2=getPara("l2");
				String tips3=getPara("tips3");
				String l3=getPara("l3");
				String tips4=getPara("tips4");
				String l4=getPara("l4");
				String tips5=getPara("tips5");
				String tips6=getPara("tips6");
				String l5=getPara("l5");
				Element enabled=root.element("enabled");
				enabled.setText(isenabled);
				Element tp=root.element("type");
				tp.setText(type);
				Element amt=root.element("amount");
				amt.setText(getPara("amount"));
				List<Element> le=root.elements("level");
				for(int i=0;i<le.size();i++){
					Element e=le.get(i);
					e.element("scale").setText(getPara("l"+(i+1)));
					e.element("count").setText(getPara("tips"+(i+2)));
				}
				FileOutputStream fos=new FileOutputStream(f);
				OutputFormat format=OutputFormat.createPrettyPrint();
				format.setEncoding("UTF-8");
				XMLWriter write=new XMLWriter(fos,format);
				write.write(doc);
				write.flush();
				write.close();
			}
			response.setCharacterEncoding("UTF-8");
			response.getWriter().print("<script>alert('成功')</script>");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		allocate();
	}
	public void introduceInit() throws DocumentException {
		HashMap<String,Object> result=new HashMap<>();
		try {
		SAXReader reader=new SAXReader();
		String base =this.getRequest().getServletContext().getRealPath("/");
		File f=new File(base+"WEB-INF/classes/allocateSetting.xml");
		if(!f.exists()) {
			result.put("status", 2);
			return;
		}
		Document doc=reader.read(f);
		Element introduce=doc.getRootElement().element("recommend");
		List<Element> es=introduce.elements();
		Map<String,String> obj=new HashMap<>();
		for(Element e:es) {
			obj.put(e.getName(), e.getData().toString());
		}
		result.put("set",obj);
	result.put("statue",1);
		}finally {
			this.renderJson(result);
		}
	}
	
	public void introduceSetting() {
		File icon=this.getFile("icon").getFile();
			  String title=this.getPara("title");
			  String introduce=this.getPara("introduce");
	}
	
	public void allocateRecord() {
		 Page<Record> p=new Page<>();
		try {
		 String fast=this.getPara("fastquery");
		 String sd=this.getPara("searchvalue");
		 String stype=this.getPara("searchtype");
		 String countSql="select count(a.id),sum(receivecount) from atcrecord a,member m where m.id=a.member_id ";
		 String sql="select a.*,m.username from atcrecord a,member m where   a.member_id=m.id  ";
		 SimpleDateFormat format=new SimpleDateFormat("yyyyMMdd");
		Date today=new Date();
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(today);
		 if(!TextUtils.isEmpty(fast)&&!fast.equals("0")) {
			 switch(fast) {
			 case "1":
				 calendar.add(Calendar.DAY_OF_MONTH, -1);
				 sql+=" and  receivedate=\""+format.format(calendar.getTime())+"\"";
				 countSql+="  and receivedate='"+format.format(calendar.getTime())+"'  ";
				 break;
			 case "2":
				int week=calendar.get(Calendar.DAY_OF_WEEK);
				int subtractdays=week-1==0?-7:-(week-1);
				calendar.add(Calendar.DAY_OF_MONTH, subtractdays);
				sql+=" and TO_DAYS(\""+format.format(calendar.getTime())+"\")-TO_DAYS(receivedate)<=7";
				countSql+="and  to_days('"+format.format(calendar.getTime())+"')-TO_DAYS(a.receivedate)<=7   ";
				break;
			 case "3":
				 //calendar.add(Calendar.DAY_OF_MONTH, -(calendar.get(Calendar.DAY_OF_MONTH)));
				// calendar.add(Calendar.MONTH, -1);
				 int month=calendar.get(Calendar.MONTH);
				 int year=calendar.get(Calendar.YEAR);
				 String basedate=month<=9?year+"0"+month:year+month+"";
				 sql+=" and date(receivedate) between \""+basedate+"01\" and \""+basedate+"31\"";
				 countSql+="  and date(receivedate) between  '"+basedate+"01'  and"+"  '"+basedate+"31' ";
				 break;
			 }
		 }
		 if(!TextUtils.isEmpty(stype)&&!stype.equals("0")) {
			 if(!TextUtils.isEmpty(sd)) {
				 if(stype.equals("1")) {
					 sql+="   and m.username like \"%"+sd+"%\"  ";
					 countSql+="and m.username like '%"+sd+"%'  ";
				 }else {
					 sql+=" and receivedate=\""+sd+"\"";
					 countSql+="and  receivedate='"+sd+"' ";
				 }
			 }
			 //sql+=" and receivedate=\""+sd+"\"";
		 }
		sql+="   limit ?,?";
		List<Record>  CountAndSum=Db.find(countSql);
		double sum=CountAndSum.get(0).get("sum(receivecount")==null?0:CountAndSum.get(0).getBigDecimal("sum(receivecount)").doubleValue();
		Long count=CountAndSum.get(0).get("count(a.id)")==null?0:CountAndSum.get(0).getLong("count(a.id)");
		p.getParams().put("sum", CountAndSum.get(0).get("sum(receivecount)"));
		p.init(count.intValue(), "allocateRecord.jhtml",this.getRequest());
		List<Record> lr=Db.find(sql,p.getStartIndex(),p.getPageSize());
		 p.setData(lr);
		 Map<Integer,String> ms=new HashMap<>();
		 for(Atcrecord.Type t:Atcrecord.Type.values()) {
			ms.put(t.getIndex(), t.getName());
		 }
		 p.getParams().put("atcType", ms);
		 this.setAttr("page", p);
		 this.renderJsp("/WEB-INF/template/admin/member/allocaterecord.jsp");
		}catch(Exception e) {
			e.printStackTrace();
			//this.setAttr("testurl", e.getMessage());
			this.renderJsp("/WEB-INF/template/admin/member/allocaterecord.jsp");
		}
		 
	}
	
	public void offLine() {
		List<SCard> ls=SCard.dao.find("select * from s_card");
		this.setAttr("cards",ls);
		List<SCardmonth> scm=SCardmonth.dao.find("select * from s_cardmonth");
		this.setAttr("months", scm);
		this.renderJsp("/WEB-INF/template/admin/member/offline.jsp");
	}
	
   public void  offLineAdd() {
	   Map m=new HashMap();
	   String date=this.getPara("date");
		 int  cid=this.getParaToInt("cardid");
		 int  mid=this.getParaToInt("monthid");
		 int  mnum=this.getParaToInt("mnum");
		 int sum=this.getParaToInt("csum");
	 String uname=this.getPara("uname");
	 List<Member> lm=Member.dao.find("select   *   from  member where username='"+uname+"'");
	 if(lm==null||lm.size()==0) {
		     m.put("result", "用户不存在");
		     this.renderJson(m);
		     return;
	 }
	 if(TextUtils.isEmpty(date)) {
		 m.put("result", "日期不能为空");
		 this.renderJson(m);
		 return;
	 }
	 SCard c=SCard.dao.findById(""+cid);
	 if(c==null||c.getName()==null) {
		 m.put("result", "卡不存在");
		 return;
	 }
	 SCardmonth scm=SCardmonth.dao.findById(""+mid);
	 if(scm==null||scm.getAmount()==null) {
		 m.put("result", "卡时长组合不存在");
		 return;
	 }
	 if(scm.getNumber()!=mnum) {
		 m.put("result", "卡时长验证失败");
		 return;
	 }
	 if(c.getIslimit()==1&&sum>c.getLimitnum()) {
		   m.put("result", "超过限购卡限购额");
		   this.renderJson(m);
	 }
	 if(sum<=0) {
		 m.put("error", "数量错误");
		 return;
	 }
	 SCardorder sco=new SCardorder();
	 sco.setMemberId(lm.get(0).getId());
	 sco.setCardId(c.getId());
	 sco.setCardName(c.getName());
	 sco.setMonthcardId(scm.getId());
	 sco.setNumber(scm.getNumber());
	 sco.setAmount(scm.getAmount());
	 sco.setAmountVolume(scm.getAmountVolume());
	 try {
		 Calendar cld=Calendar.getInstance();
		Date d=new SimpleDateFormat("yyyyMMdd").parse(date);
		sco.setCreateDate(d);
		 cld.setTime(d);
		 cld.add(Calendar.MONTH, mnum);
		 sco.setEndDate(cld.getTime());
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		m.put("result","日期转换错误，请输入合法的日期格式" );
		this.renderJson(m);
		return;
	}
	sco.setModifyDate(new Date());
	sco.setVersion(1l);
	sco.setStatus("1");
	sco.setKsl(sum);
	boolean b=sco.save();
	if(b) {
		m.put("result", "添加成功");
	}else
	   m.put("result","添加失败");
	   this.renderJson(m);
   }

}