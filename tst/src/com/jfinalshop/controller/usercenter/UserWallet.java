package com.jfinalshop.controller.usercenter;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.util.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinalshop.Setting;
import com.jfinalshop.controller.usercenter.entity.DepositlogUtil;
import com.jfinalshop.controller.usercenter.entity.PointlogUtil;
import com.jfinalshop.controller.usercenter.entity.SrecommendUtil;
import com.jfinalshop.dao.PointLogDao;
import com.jfinalshop.model.DepositLog;
import com.jfinalshop.model.IntegralStream;
import com.jfinalshop.model.Member;
import com.jfinalshop.model.PointLog;
import com.jfinalshop.model.SCardorder;
import com.jfinalshop.model.SRecommend;
import com.jfinalshop.model.client.DepositLogSTUC;
import com.jfinalshop.model.client.PointLogSTUC;
import com.jfinalshop.service.MemberService;
import com.jfinalshop.service.PointLogService;
import com.jfinalshop.util.SystemUtils;

import ch.qos.logback.core.status.Status;

@ControllerBind(controllerKey = "/UserWallet")

public class UserWallet extends Controller {	
	private MemberService memberService = enhance(MemberService.class);

	/**
	 * 我的钱包 需要用户的id
	 */
	public void Mypurse() {
		String userid =getPara("id");
		Map<String,Object> map = new HashMap<>();
		if(!TextUtils.isEmpty(userid)) {
	 		List<Member> index =Member.dao.find("select id,nickname,point,avatar as portrait,1 as quantity,balance FROM member WHERE id=?",userid);
	 		map.put("index", index);
	 		map.put("status", 1);
	 		this.renderJson(map);
	 	}else {
	 		map.put("status", 0);
	 		map.put("message", "查看出现异常");
	 		this.renderJson(map);
		}
		
	}
	
	/**
	 * 我的银行卡
	 * 需要用户id
	 */
	public void MyBankcard() {
		String userid =getPara("id");
		if(TextUtils.isEmpty(userid)) {
			this.renderJson("error");
			return;
		}
		Map<String, Object> map=new HashMap<String,Object>();
		try {
		Member member = Member.dao.findById(userid);	
	    map.put("id", member.getId());
	    map.put("Bankcard", member.getAttributeValue4());
	    map.put("CardNumber", HideDataUtil.hideCardNo(member.getAttributeValue2()));
	    map.put("logo", member.getAttributeValue3());
	    map.put("status", 1);
		this.renderJson(map);
		} catch (Exception e) {
		map.put("status", 0);
		this.renderJson(map);
		}
	}
	
	/**
	 * 我的积分
	 * 需要用户id
	 */
	public void Myintegral() {
		String userid=getPara("id");
		if(TextUtils.isEmpty(userid)) {
			this.renderJson("error");
			return;
		}
		Map<String, Object> map = new HashMap<>();														// limit 0,5
		try {
			List<PointLog> pointLogs = PointLog.dao.find("select * from point_log where member_id=? ORDER BY id desc ",userid);
			for (PointLog pointLog : pointLogs) {
				System.out.println(pointLog);
			}
			
			List<Object> list = new ArrayList<Object>();
			for(int i =0; i<pointLogs.size();i++) {
			PointlogUtil	point = new PointlogUtil();
			point.setId(pointLogs.get(i).getId());
			point.setCreateDate(pointLogs.get(i).getCreateDate());
			point.setBalance(pointLogs.get(i).getBalance());
			point.setCredit(pointLogs.get(i).getCredit());
			point.setDebit(pointLogs.get(i).getDebit());
			point.setType(PointLogSTUC.Trantype.getName(pointLogs.get(i).getType()));
			point.setMemo(pointLogs.get(i).getMemo());
			point.setStatus(pointLogs.get(i).getStatus());
			list.add(point);
			}
			map.put("integrals", list);
			map.put("status", 1);
			this.renderJson(map);	
		} catch (Exception e) {
			map.put("status", 0);
			this.renderJson(map);
		}
		
	}
	
	/**
	 * 粮票余额
	 * 需要用户id
	 */
	
	public void MoneyBalance() {
		String userid=getPara("id");
		if(TextUtils.isEmpty(userid)) {
			this.renderJson("error");
			return;
		}
		
		Map<String, Object> map = new HashMap<>();
		try {
		
			List<DepositLog> MyMoneys = DepositLog.dao.find("select * from deposit_log where member_id=? ORDER BY id desc",userid);
		
			List<Object> list = new ArrayList<Object>();
			
			for(int i =0; i<MyMoneys.size();i++) {
			DepositlogUtil	point = new DepositlogUtil();
			point.setId(MyMoneys.get(i).getId());
			point.setCreateDate(MyMoneys.get(i).getCreateDate());
			point.setBalance(MyMoneys.get(i).getBalance());
			point.setCredit(MyMoneys.get(i).getCredit());
			point.setDebit(MyMoneys.get(i).getDebit());
			point.setType(DepositLogSTUC.Trantype.getName(MyMoneys.get(i).getType()));
			point.setMemo(MyMoneys.get(i).getMemo());
			list.add(point);
			}
		map.put("incomeStreams", list);
		map.put("status", 1);
		this.renderJson(map);
		} catch (Exception e) {
		map.put("status", 0);
		this.renderJson(map);
		}
	}
	
	/**
	 * 积分兑换粮票
	 * 需要用户id
	 */
	//开启事物
	@Before(Tx.class)
	public void Redeem() throws SQLException {
	
		Integer integral =getParaToInt("point");
		String id =getPara("id");
		if(integral==null||id==null) {
			this.renderJson("error");
			return;
		}
		Map<String, Object> map = new HashMap<>();
		try {
		Member rdeeem = Member.dao.findById(id);
		if(rdeeem.getPoint()>=integral) {
			Double cc=rdeeem.getPoint()-integral;
			//减去积分
			Db.update("update member SET point="+cc+" WHERE id="+id+"");
			BigDecimal	balance= rdeeem.getBalance();
			//类型转换
			Integer balances= integral+balance.intValue();
			
		//	Double cc2=rdeeem.getPoint();
			//增加粮票
			Db.update("update member SET balance="+balances+" WHERE id="+id+"");
			
			//添加兑换记录
			IntegralStream add = new IntegralStream();
			add.set("create_data", new Date()).set("event", "兑换粮票-"+integral).set("balance", cc).set("member_id", id).save();
			map.put("status", 1);
			this.renderJson(map);		
		}else {
			map.put("status","余额不足,兑换失败");
			this.renderJson(map);
		}
		} catch (Exception e)  {
			map.put("status", 0);
			this.renderJson(map);
			throw e;
		}
	}
	
	/**
	 * 积分兑换粮票和申请兑换现金
	 * 需要用户id,兑换数量,类型
	 */
	@Before(Tx.class)
	public void RedeemExchangeMoney() {
		String status =getPara("status");
		int count=this.getParaToInt("count");
		String id =getPara("id");
		Member member = Member.dao.findById(id);
		Map<String, Object> map = new HashMap<String, Object>();
		Setting setting = SystemUtils.getSetting(); //获得推荐返回 比例
	   MemberService memberService= new  MemberService();
		System.out.println(status);
		if(TextUtils.isEmpty(status)||TextUtils.isEmpty(id)||member==null){
			this.renderJson("error");
			return;
		}
		
		if(setting.getCash()==null||(Math.round(member.getPoint())<count)){
			map.put("stauts", 0);
			map.put("message", "积分不足!");
			renderJson(map);
			return;	
		}
		
		if(status!=null&&!status.equals("")&&setting.getCash()!=null){
			BigDecimal temp_zs = new BigDecimal(count); //积分类型转换
			if(status.equals("1")){  //100积分兑换现金
				if (setting.getCash() > 0) { //减少积分
					memberService.addPointCash(member, -count, PointLog.Type.cash, temp_zs, "用户申请公分兑换现金");
				}
				map.put("point", member.getPoint());
				map.put("balane", member.getBalance());
				map.put("status", 1);
				map.put("message", "提现申请提交成功!");
				renderJson(map);
				return;	
			}
			if(status.equals("2")){ //兑换粮票
				if(member.getPoint()<count) {
					map.put("status", 0);
					map.put("message", "积分不足！");
					this.renderJson(map);
					return;
				}
				temp_zs=new BigDecimal(count);
				if (setting.getCash() > 0) { //减少积分
					memberService.addPointCash(member, -count, PointLog.Type.amount, temp_zs, "积分转成余额");
					memberService.addBalance(member, temp_zs, DepositLog.Type.pionts, null, "工分兑换粮票");
				}
				map.put("point", member.getPoint());
				map.put("balane", member.getBalance());
				map.put("status", 1);
				map.put("message", "兑换消费券成功!");
				renderJson(map);
				return;
			}
		}else{
			map.put("status", 0);
			map.put("message", "操作有误!");
			return;
		}
	}
	
	
	
	
	/**
	 * 积分兑换现金
	 * 需要用户id
	 */
	public void ExchangeMoney() {
		Integer integral =getParaToInt("point");
		String id =getPara("id");
		
		if(integral==null||id==null) {
			this.renderJson("error");
			return;
		}
		Map<String, Object> map = new HashMap<>();
		try {
			Member rdeeem = Member.dao.findById(id);	
		
			
			if(rdeeem.getPoint()>=integral) {
				Double cc=rdeeem.getPoint()-integral;
				//减去积分
				Db.update("update member SET point="+cc+" WHERE id="+id+"");
				
				//添加兑换记录
				IntegralStream add = new IntegralStream();
				add.set("create_data", new Date()).set("event", "兑换现金-"+integral).set("balance", cc).set("member_id", id).set("status", 0).save();
				map.put("status", 1);
				this.renderJson(map);		
			}else {
				map.put("status","余额不足,兑换失败");
				this.renderJson(map);
			}
			
		} catch (Exception e) {
			map.put("status",0);
			this.renderJson(map);
		}
	}
	
	
	/**
	 * 我的推荐
	 * 需要用户id
	 */
	public void Myrecommend() {
		String id = getPara("id");
		if(TextUtils.isEmpty(id)) {
			this.renderJson("error");
			return;
		}
		Map<String,Object> map=new HashMap<String,Object>();
		Member member = Member.dao.findById(id);
		try {
		//查询用户推荐了谁	
		List<Record> recommends = Db.find("select * from s_recommend WHERE member_id=?",id);
		Integer recommendssize = recommends.size();	
		if(recommends.size()==0) {
			map.put("recommends", recommends);
			map.put("size", 0);
			map.put("avatar", member.getAvatar());
			this.renderJson(map);
			return;
		}
		String tomemberid="";
		for(int i = 0;i<recommends.size();i++) {
			//查询被推荐人推荐了多少人
			Long cc=recommends.get(i).get("to_member_id");
			//被推荐人的id
			if(i == recommends.size() -1) {
				tomemberid+=cc;
			}
			else {
				tomemberid+=cc+",";
			}
		}
		List<SrecommendUtil> onelist = new ArrayList<>();
		List<SrecommendUtil> twolist = new ArrayList<>();
		//一级用户
		List<Record> oneuser = Db.find("select mr.avatar,sd.id as sdid,sd.member_id as memberId,sd.to_member_id as toMemberId,sd.create_date as createDate,mr.username,mr.id as mrid from \r\n" + 
				"s_recommend sd LEFT JOIN\r\n" + 
				"member mr ON sd.to_member_id=mr.id WHERE sd.to_member_id in ("+tomemberid+") ORDER BY sd.create_date desc");
		for(int i = 0; i<oneuser.size(); i ++) {
			SrecommendUtil sre = new SrecommendUtil();
			
			if(oneuser.get(i).get("avatar")==null) {
				sre.setAvatar(null);
			}else {
				sre.setAvatar(oneuser.get(i).get("avatar"));
			}
			sre.setCreateDate(oneuser.get(i).get("createDate"));
			sre.setUsername(oneuser.get(i).get("username"));
			Long mrid = oneuser.get(i).get("mrid");
			List<Record> Secondary =Db.find("select count(*) FROM s_recommend WHERE member_id=?",mrid);
			Long size =Secondary.get(0).get("count(*)");
			System.out.println("这是size:" + size);
			sre.setSize(size);
			onelist.add(sre);
		}
		//二级用户
		List<Record> twouser = Db.find("select sd.id as id,sd.create_date as createDate,sd.member_id as memberId,sd.to_member_id as toMemberId,mr.username,mr.avatar as avatar,mr.id as mrid\r\n" + 
				"FROM \r\n" + 
				"s_recommend sd LEFT JOIN \r\n" + 
				"member mr ON sd.to_member_id=mr.id WHERE sd.member_id  in ("+tomemberid+") ORDER BY sd.create_date desc ");
		for(int i = 0 ;i<twouser.size();i++) {
			SrecommendUtil sre = new SrecommendUtil();
			if(twouser.get(i).get("avatar")==null) {
				sre.setAvatar(null);
			}else {
				sre.setAvatar(twouser.get(i).get("avatar"));
			}
			sre.setCreateDate(twouser.get(i).get("createDate"));
			sre.setUsername(twouser.get(i).get("username"));
			Long mrid = twouser.get(i).get("mrid");
			List<Record> Secondary =Db.find("select count(*) FROM s_recommend WHERE member_id=?",mrid);
			Long size =Secondary.get(0).get("count(*)");
			sre.setSize(size);
			twolist.add(sre);
		}
		
		
		List<Record> Secondary =Db.find("select count(*) FROM s_recommend sd LEFT JOIN member mr ON sd.member_id=mr.id WHERE mr.id IN ("+tomemberid+") ");
		//总推荐数
		Long Secondarycount =Secondary.get(0).get("count(*)");
		Long size=recommendssize+Secondarycount;
		map.put("size", size);   //总条数
		map.put("avatar", member.getAvatar()); //用户头像
		map.put("OneUser", onelist); //一级用户
		map.put("twouser", twolist); //二级用户
		map.put("status", 1);
		this.renderJson(map);
		} catch (Exception e) {
		map.put("status", 0);
		this.renderJson(map);
		}	
	}
		
	
	/**
	 * SES卡包
	 * 需要用户id
	 */
	public void sesCardpackage() {
		String id = getPara("id");
		if(TextUtils.isEmpty(id)) {
			this.renderJson("error");
			return;
		}
		Map<String, Object> map = new HashMap<>();
		List<Object> list = new ArrayList<>();
		try {
		List<SCardorder> CardPackage =SCardorder.dao.find("select sr.id,sr.version,sr.modify_date,sr.card_name as carName,sr.create_date,sr.ksl,sr.end_date,sr.status,\r\n" + 
				"mr.id as mrid FROM\r\n" + 
				"s_cardorder sr LEFT JOIN \r\n" + 
				"member mr ON sr.member_id=mr.id WHERE mr.id=?",id);
		Date date = new Date();
		
		for (int i = 0;i<CardPackage.size(); i ++) {
			String Status = CardPackage.get(i).getStatus();
			if(Status.equals("3")||Status.equals("0")) {
				 continue;
			}
			if(CardPackage.get(i).getEndDate().before(date)&&!(Status.contentEquals("4")&&!(Status.contentEquals("2")))){
				CardPackage.get(i).setStatus("3");
				CardPackage.get(i).setModifyDate(date);
				CardPackage.get(i).setVersion(CardPackage.get(i).getVersion()+1);
				CardPackage.get(i).update();
			}
		}
		
		for(int i = 0;i<CardPackage.size(); i ++) {
			String Status = CardPackage.get(i).getStatus();
			if(Status.equals("1")||Status.equals("3")||Status.equals("6")) {
				Long time = CardPackage.get(i).getCreateDate().getTime();
				CardPackage.get(i).setCardId(time);
				list.add(CardPackage.get(i));
			}
}
		map.put("CardPackage", list);
		map.put("status", 1);
		map.put("message", "查询成功");
		this.renderJson(map);
		} catch (Exception e) {
		map.put("status", 0);
		map.put("message", "查询出现异常");
		this.renderJson(map);
		}
	}
	
	/**
	 * 绑定银行卡
	 * 需要用户的id
	 */
	public void BindingBankCard() {
		String id =getPara("id");//用户id
		String cardholderName=getPara("cardholderName"); //持卡人姓名
		String Bank = getPara("Bank");//开户行
		String BankCard=getPara("BankCard");//银行卡号
		String mobile = getPara("mobile");//持卡人手机号
		if(TextUtils.isEmpty(id)||TextUtils.isEmpty(cardholderName)||TextUtils.isEmpty(Bank)||TextUtils.isEmpty(BankCard)) {
			this.renderJson("error");
			return;
		}
		
		Map<String,Object> map = new HashMap<>();
		try {
		Member member= Member.dao.findById(id);
		//获取银行卡号key
		String  strinBack  = HideDataUtil.getCardDetail(BankCard);
		JSONObject jsStr = JSONObject.parseObject(strinBack); //将字符串{“id”：1}
		//银行logo
		String  logo= jsStr.getString("bank");
		Boolean key=jsStr.getBoolean("validated");
		if(key) {
			System.out.println("银行卡正确");
		}else {
			map.put("status", 0);
			map.put("message", "银行卡号错误");
			this.renderJson(map);
			return;
		}
		if(mobile==null) {
			mobile = member.getMobile();
		}
		if(HideDataUtil.isPhoneLegal(mobile)) {
			System.out.println("手机号正确");
		}else {
			map.put("status", 0);
			map.put("message", "手机号错误");
			this.renderJson(map);
			return;
		}
		member.setModifyDate(new Date());
		member.setVersion(member.getVersion()+1);
		member.setAttributeValue0(cardholderName);
		member.setAttributeValue1(Bank);
		member.setAttributeValue2(BankCard);
		member.setAttributeValue3(logo);
		member.setAttributeValue4(HideDataUtil.getNameOfBank(BankCard));
		member.setMobile(mobile);
		member.update();
		map.put("status", 1);
		map.put("message", "绑定成功");
		this.renderJson(map);
		} catch (Exception e) {
		map.put("status", 0);
		map.put("message", "绑定出现异常");
		this.renderJson(map);
		}
	}
	
	/**
	 * SES卡包退订
	 * 需要 卡号id和用户id
	 */
	 public void SesCardUnsubscribe() {
		 String id = getPara("userId");
		 String cardid = getPara("cardId");
		 if(TextUtils.isEmpty(id)||TextUtils.isEmpty(cardid)) {
			 this.renderJson("error");
			 return;
		 }
		 Map<String, Object> map  = new HashMap<>();
		 try {
		List<SCardorder>	sCardorders = SCardorder.dao.find("select * from s_cardorder where id=? && member_id=?",cardid,id);
		if(sCardorders.size()==0) {
			map.put("status", 0);
			map.put("message", "暂未有此卡");
			this.renderJson(map);
			return;
		}
		String status = sCardorders.get(0).getStatus();
		if(status.equals("4")) {
			map.put("status", 1);
			map.put("message", "已申请过退订,正在审核中...");
			this.renderJson(map);
			return;
		}
		//如果是已支付,或者逾期才可以退订
		if(status.equals("1")||status.equals("3")) {
			Date date = new Date();
			sCardorders.get(0).setVersion(sCardorders.get(0).getVersion()+1);
			sCardorders.get(0).setModifyDate(date);
			sCardorders.get(0).setStatus("4");
			//时间格式转换
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        String createdate = sdf.format(date);
			if(sCardorders.get(0).getMemo()==null) {
				sCardorders.get(0).setMemo("code:4:"+createdate+"退卡操作;");	
			}else {
				sCardorders.get(0).setMemo(sCardorders.get(0).getMemo()+"code:4:"+createdate+"退卡操作;");	
			}
			sCardorders.get(0).setReturnDate(date);
			sCardorders.get(0).update();
			map.put("status", 1);
			map.put("message", "退卡申请提交成功!");
			this.renderJson(map);
		}else {
			map.put("status", 0);
			map.put("message", "退卡有误");
			this.renderJson(map);
		}
		
		} catch (Exception e) {
			map.put("status", 0);
			map.put("message", "退卡异常");
			this.renderJson(map);
		}
	 }
	 
	 /**
	  * SES卡包续期
	  * 需要 卡号id和用户id
	  */
	 public void SesCardRenewal() {

		 String id = getPara("userId");
		 String cardid = getPara("cardId");
		 if(TextUtils.isEmpty(id)||TextUtils.isEmpty(cardid)) {
			 this.renderJson("error");
			 return;
		 }
		 Map<String, Object> map  = new HashMap<>();
		 try {
		List<SCardorder>	sCardorders = SCardorder.dao.find("select * from s_cardorder where id=? && member_id=?",cardid,id);
		if(sCardorders.size()==0) {
			map.put("status", 0);
			map.put("message", "暂未有此卡");
			this.renderJson(map);
			return;
		}
		String status = sCardorders.get(0).getStatus();
		//如果是已支付,或者逾期才可以退订
		if(status.equals("1")||status.equals("3")||status.equals("6")) {
			if(status.equals("6")) {
				map.put("status", 0);
				map.put("message", "此卡已续过期,请待卡包到期之后再来续期");
				this.renderJson(map);
				return;
			}
			Date date = new Date();
			sCardorders.get(0).setVersion(sCardorders.get(0).getVersion()+1);
			sCardorders.get(0).setModifyDate(date);
			sCardorders.get(0).setStatus("6");
			 //时间格式转换	
			 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			 String createdate = sdf.format(date);
			 //备注是否备注过？
			if(sCardorders.get(0).getMemo()==null) {
				sCardorders.get(0).setMemo("code:6:"+createdate+"续期"+sCardorders.get(0).getNumber()+"个月操作;");	
			}else {
				sCardorders.get(0).setMemo(sCardorders.get(0).getMemo()+"code:6:"+createdate+"续期"+sCardorders.get(0).getNumber()+"个月操作;");	
			}
			//当前时间是否大于卡包结束时间
			Calendar calendar = Calendar.getInstance();
			if(date.after(sCardorders.get(0).getEndDate())) {
				//从当前时间续期指定月数
				calendar.setTime(date);
				calendar.add(Calendar.MONTH, sCardorders.get(0).getNumber());//增加月数
				sCardorders.get(0).setEndDate(calendar.getTime());
			}else {
				//从过期时间在续期指定月数
				calendar.setTime(sCardorders.get(0).getEndDate());
				calendar.add(Calendar.MONTH, sCardorders.get(0).getNumber());//增加月数
				sCardorders.get(0).setEndDate(calendar.getTime());
			} 
			sCardorders.get(0).update();
			
			BigDecimal temp_zs = new BigDecimal(sCardorders.get(0).getKsl());//张数转换
			BigDecimal card_amount =sCardorders.get(0).getAmountVolume().multiply(temp_zs);
			Member member = Member.dao.findById(id);
		    memberService.addBalance(member,card_amount,DepositLog.Type.recharge, null, "卡包续期");
		
			map.put("status", 1);
			map.put("message", "续期操作成功,请查看帐户余额变化!");
			this.renderJson(map);
		}else {
			map.put("status", 0);
			map.put("message", "续期有误");
			this.renderJson(map);
		}
		} catch (Exception e) {
			map.put("status", 0);
			map.put("message", "续期异常");
			this.renderJson(map);
		}
	 }
	
}
