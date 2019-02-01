package com.jfinalshop.service.ses;

import java.io.File;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.util.TextUtils;
import org.apache.log4j.Level;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.jfinal.aop.Enhancer;
import com.jfinal.plugin.activerecord.Page;
import com.jfinalshop.Pageable;
import com.jfinalshop.dao.SCardorderDao;
import com.jfinalshop.model.Atcrecord;
import com.jfinalshop.model.Member;
import com.jfinalshop.model.SCardorder;
import com.jfinalshop.model.client.CommonSTUC;
import com.jfinalshop.service.BaseService;

/**
 * Service - 月卡记录
 * 
 * 
 */
public class CardorderService extends BaseService<SCardorder> {
	
	/**
	 * 构造方法
	 */
	public CardorderService() {
		super(SCardorder.class);
	}
	
	
	private SCardorderDao cardDao = Enhancer.enhance(SCardorderDao.class);;
	
	public SCardorder save(SCardorder ad) {
		return super.save(ad);
	}

	public SCardorder update(SCardorder ad) {
		return super.update(ad);
	}

	public void delete(Long id) {
		super.delete(id);
	}

	public void delete(Long... ids) {
		super.delete(ids);
	}

	public void delete(SCardorder ad) {
		super.delete(ad);
	}
	/**
	 * 
	 * @param base  基础地址
	 * @param loginCode  用户登陆码
	 * @return
	 * @throws DocumentException 
	 */
	 public double signCount(String base,String loginCode) throws DocumentException {
		 double count=0;
		 if(TextUtils.isEmpty(base)||TextUtils.isEmpty(loginCode)) return 0;
		 
			 File f=new File(base+"/WEB-INF/classes/allocateSetting.xml");
			 if(!f.exists())return 0;
			 List<SCardorder> orders=SCardorder.dao.find("select card_id from s_cardorder where member_id=(select id from member where logincode=?) and status in(?,?,?)  "
					 ,loginCode,SCardorder.Status.pay.getIndex(),SCardorder.Status.renewal.getIndex(),SCardorder.Status.outTime.getIndex());
			 if(orders==null||orders.size()<=0)return 0;
			 SAXReader reader=new SAXReader();
			 Document doc=reader.read(f);
			 String enbled=doc.getRootElement().elementText("enabled");
			 List<Element> elements=doc.getRootElement().elements("level");
			 Map<String,Double> levels=new HashMap<>();
			 for(Element e:elements) {
				 levels.put(e.attributeValue("id"), Double.valueOf(e.elementText("count")));
			 }
			 if(levels.size()<=0) {
				 return 0;
			 }
			 for(SCardorder order:orders) {
				 switch(order.getCardId()+"") {
				 case "7":
					 count+=levels.get("l1");
					 break;
				 case "8":
					 count+=levels.get("l2");
					 break;
				 case "9":
					 count+=levels.get("l3");
					 break;
				 case "10":
					 count+=levels.get("l4");
					 break;
				 case "13":
					 count+=levels.get("l5");
					 break;
				 }
			 }
				 BigDecimal bd=new BigDecimal(count);
		 return bd.setScale(2,BigDecimal.ROUND_HALF_DOWN).doubleValue();
	 }
	 /**
	  * 查询名下有效卡
	  *   mid  用户id
	  */
	 public List<SCardorder> getCard(int id){
		   return SCardorder.dao.find("select  id,member_id,card_name,number,ksl,end_date  from s_cardorder where status<>'0' and member_id=?",id);
	 }
	/**
	 * 	签到 
	 * @author zxx
	 * @param base  基础地址
	 * @param loginCode  登陆码
	 * @return
	 */
	public Map sign(String base,String loginCode) {
		Map<String,Object> result=new HashMap<>();
		try {
			List<Member> ms=Member.dao.find("select id from member where logincode=?",loginCode);
			if(ms==null||ms.size()<=0) {
			result.put("status", CommonSTUC.Status.NoExist.getIndex());
			}else {
				Member m=ms.get(0);
				List<Atcrecord> records=Atcrecord.dao.find("select id from atcrecord where  member_id=? and  date(receivedate)=date(?)  and atype=?  ",
						m.getId(),new Date(),Atcrecord.Type.Dividend.getIndex());
				if(records!=null&&records.size()>0) {
					result.put("status", CommonSTUC.Status.Received.getIndex());
				}else {
					Atcrecord record=new Atcrecord();
					File f=new File(base+"/WEB-INF/classes/allocateSetting.xml");
					List<SCardorder> cardOrders=SCardorder.dao.find("select card_id from s_cardorder where member_id=(select id from member where logincode=?) and status in(?,?,?)  "
							 ,loginCode,SCardorder.Status.pay.getIndex(),SCardorder.Status.renewal.getIndex(),SCardorder.Status.outTime.getIndex());
					if(cardOrders==null||cardOrders.size()<=0||!f.exists()) {
						result.put("count", 0);
						record.setReceivecount(BigDecimal.valueOf(0));
					}else {
							double atcCount=0;
							 Document doc=new SAXReader().read(f);
							 Element root =doc.getRootElement();
							 String enabled=root.elementText("enabled");
							 List<Element> es=doc.getRootElement().elements("level");
							 if(root==null||es==null||es.size()<=0||enabled==null||enabled.equals("0")) {
								 result.put("count", 0);
								 record.setReceivecount(BigDecimal.valueOf(0));
							 }else {
								 Map<String,Double> levels=new HashMap<>();
								 for(Element e:es) {
									 levels.put(e.attributeValue("id"), Double.valueOf(e.elementText("count")));
								 }
								 if(levels.size()<=0) {
									 atcCount=0;
								 }else {
									 for(SCardorder order:cardOrders) {
										 switch(order.getCardId()+"") {
										 case "7":
											 atcCount+=levels.get("l1");
											 break;
										 case "8":
											 atcCount+=levels.get("l2");
											 break;
										 case "9":
											 atcCount+=levels.get("l3");
											 break;
										 case "10":
											 atcCount+=levels.get("l4");
											 break;
										 case "13":
											 atcCount+=levels.get("l5");
											 break;
										 }
									 }
								 }
								 BigDecimal bd=new BigDecimal(atcCount).setScale(2,BigDecimal.ROUND_HALF_DOWN);
								 result.put("count", bd.doubleValue());
								 record.setReceivecount(bd);
							 }
				}
					 result.put("status", CommonSTUC.Status.Success.getIndex());
					record.setReceivedate(new Date());
					record.setMemberId(m.getId()+"");
					record.setAtype(Atcrecord.Type.Dividend.getIndex());
					record.save();
				}
			 }
		}catch(Exception e) {
			e.printStackTrace();
			result.put("status",CommonSTUC.Status.Error.getIndex());
		}finally{
			return result;
		}
		
	}
	/**
	 * 查找月记录分页
	 * 
	 * @param isEnabled
	 *            是否启用
	 * @param isExchange
	 *            是否允许积分兑换
	 * @param hasExpired
	 *            是否已过期
	 * @param pageable
	 *            分页信息
	 * @return 优惠券分页
	 */
	public Page<SCardorder> findPage(String isEnabled, Boolean isExchange, Boolean hasExpired, Pageable pageable) {
		return cardDao.findPage(isEnabled, isExchange, hasExpired, pageable);
	}
	
	
	/**
	 * 查找月卡订单记录分页
	 * 
	 * @param member
	 *            会员
	 * @param pageable
	 *            分页信息
	 * @return 月卡订单记录分页
	 */
	public Page<SCardorder> findPageList(Member member, Pageable pageable) {
		return cardDao.findPageList(member, pageable);
	}
	/**
	 * 计算 status：状态的总数
	 * @param member
	 * @param isReview
	 * @return
	 */
	public Long count(Member member, String status) {
		return cardDao.count(member, status);
	}

}