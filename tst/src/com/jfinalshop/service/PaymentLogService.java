package com.jfinalshop.service;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import com.jfinal.aop.Enhancer;
import com.jfinalshop.Setting;
import com.jfinalshop.dao.PaymentLogDao;
import com.jfinalshop.dao.SnDao;
import com.jfinalshop.model.DepositLog;
import com.jfinalshop.model.Member;
import com.jfinalshop.model.Order;
import com.jfinalshop.model.Payment;
import com.jfinalshop.model.PaymentLog;
import com.jfinalshop.model.PointLog;
import com.jfinalshop.model.SCardorder;
import com.jfinalshop.model.SRecommend;
import com.jfinalshop.model.Sn;
import com.jfinalshop.service.ses.CardorderService;
import com.jfinalshop.service.ses.RecommendService;
import com.jfinalshop.util.Assert;
import com.jfinalshop.util.SystemUtils;

/**
 * Service - 支付记录
 * 
 * 
 */
public class PaymentLogService extends BaseService<PaymentLog> {

	/**
	 * 构造方法
	 */
	public PaymentLogService() {
		super(PaymentLog.class);
	}

	private PaymentLogDao paymentLogDao = Enhancer.enhance(PaymentLogDao.class);
	private SnDao snDao = Enhancer.enhance(SnDao.class);
	private MemberService memberService = Enhancer.enhance(MemberService.class);
	private OrderService orderService = Enhancer.enhance(OrderService.class);
	
	private CardorderService ordercardService = Enhancer.enhance(CardorderService.class);
	private RecommendService recommendService= Enhancer.enhance(RecommendService.class);
    private Logger log = Logger.getLogger(this.getClass());
	/**
	 * 根据编号查找支付记录
	 * 
	 * @param sn
	 *            编号(忽略大小写)
	 * @return 支付记录，若不存在则返回null
	 */
	public PaymentLog findBySn(String sn) {
		return paymentLogDao.findBySn(sn);
	}

	/**
	 * 支付处理
	 * 
	 * @param paymentLog
	 *            支付记录
	 */
	public void handle(PaymentLog paymentLog) {
		Assert.notNull(paymentLog);
		Assert.notNull(paymentLog.getType());

		if (paymentLog.getStatus()!=PaymentLog.Status.wait.ordinal()) {
			return;
		}

		switch (paymentLog.getTypeName()) {
		case recharge:
			Member member = paymentLog.getMember();
			if (member != null) {
				//更改月卡订单
				if(paymentLog.getCardorderId()!=null){
					SCardorder cardorder =ordercardService.find(paymentLog.getCardorderId());
					//cardorder=ordercardService.find(paymentLog.getCardorderId());
					cardorder.setStatus("1");
					ordercardService.update(cardorder);
					//用户余额，返回消费卷值
					BigDecimal temp_zs = new BigDecimal(cardorder.getKsl());//张数转换
					BigDecimal card_amount =cardorder.getAmountVolume().multiply(temp_zs);
					
				    memberService.addBalance(member,card_amount,DepositLog.Type.recharge, null, null);
				    paymentLog.setStatus(PaymentLog.Status.success.ordinal());
					paymentLog.update();
				    //1.查找邀请人，获得积分:是否已经返回,s_recommend.status:1,推荐积分:s_recommend.count_pionts
				    SRecommend tmepRecommend=recommendService.findByTomemberid(member.getId());
				    //tmepRecommend=recommendService.findByTomemberid(member.getId());
			    	Setting setting = SystemUtils.getSetting(); //获得推荐返回 比例
				    
				    if(tmepRecommend!=null&&tmepRecommend.getMemberId()!=null&&setting.getRecommend()!=null){
				    //2.修邀请人记录状态	
				    	//用户余额，返回"卡面值*订单数量 *设置比例;setting.getRecommend()"
				    	int int_amount=(cardorder.getAmount()).intValue();
	
				    	float bRecommend=(float)setting.getRecommend()/100;
				    	int rec_amount_B =(int) (int_amount*cardorder.getKsl()*bRecommend);//B获得C设置"推荐注册返回比例"
				    	log.info("rec_amount_B--->"+rec_amount_B+"--"+"cardorder.getKsl()--->"+cardorder.getKsl()+"--"+"setting.getRecommend()--->"+setting.getRecommend()+"--"+"(setting.getRecommend()/100)--->"+(setting.getRecommend()/100));
				    	Long long_amount_B=(long)rec_amount_B;//
				    	tmepRecommend.setCountPionts(long_amount_B);//返回推荐积分
				    	tmepRecommend.setStatus("1");//返回积分状态 0:未;1:已
				    	recommendService.update(tmepRecommend);
				        //3.C给邀请人充积分B:
				    	Member tempMember_B = memberService.find(tmepRecommend.getMemberId());
				    	log.info("tempMember_B---------------------->"+tempMember_B);
				    	log.info("tmepRecommend.getMemberId()------------------->"+tmepRecommend.getMemberId());

				    	if (tempMember_B!=null) { //增加积分
				    		//A推荐B，A得5%；B推荐C，B得5%，A得3%
				    		//1.B推荐C，B得5%；
				    		log.info("rec_amount_B-------------------------->"+rec_amount_B);
							memberService.addPoint(tempMember_B, rec_amount_B, PointLog.Type.recommend, null, null);
							//查找B的推荐人
							SRecommend tmepRecommend_A=recommendService.findByTomemberidTwo(tempMember_B.getId());
							//tmepRecommend_A=recommendService.findByTomemberidTwo(tempMember_B.getId());

							if(tmepRecommend_A!=null&&setting.getTworecommend()!=null){
							Member tempMember_A = memberService.find(tmepRecommend_A.getMemberId());
							//2.A推荐B，A得C3%
							float aRecommend=(float)setting.getTworecommend()/100;
							int rec_amount_A =(int) (int_amount*cardorder.getKsl()*aRecommend);//A获得C设置"推荐注册返回比例"
					    	Long long_amount_A=(long)rec_amount_A;//
					    	log.info("long_amount_A-------------------------------->"+long_amount_A);
							memberService.addPoint(tempMember_A, long_amount_A, PointLog.Type.recommend, null, null);
							tmepRecommend_A.setCountPionts(long_amount_A);
							recommendService.update(tmepRecommend_A);
							}
						}
				    }
				}else{
				//充值，会员余额
					log.info("+++++++++++++++++++++++会员充值 the vip full");
				memberService.addBalance(member,
						paymentLog.getEffectiveAmount(),
						DepositLog.Type.recharge, null, null);
				}
			}
			break;
		case payment:
			Order order = paymentLog.getOrder();
			if (order != null) {
				Payment payment = new Payment();
				payment.setMethod(Payment.Method.online.ordinal());
				payment.setPaymentMethod(paymentLog.getPaymentPluginName());
				payment.setFee(paymentLog.getFee());
				payment.setAmount(paymentLog.getAmount());
				payment.setOrderId(order.getId());
				orderService.payment(order, payment, null);
			}
			break;
		}
		//super.update(paymentLog);
	}

	public PaymentLog save(PaymentLog paymentLog) {
		Assert.notNull(paymentLog);
		paymentLog.setSn(snDao.generate(Sn.Type.paymentLog));
		return super.save(paymentLog);
	}

}