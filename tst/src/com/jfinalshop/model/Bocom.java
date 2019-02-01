package com.jfinalshop.model;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.http.util.TextUtils;

import com.bocom.bocompay.BocomClient;
import com.bocom.bocompay.BocomDataMap;

public class Bocom {
	
		public static final String id="bankUnion";
		public static final String name="银联支付";
		public static Pay getPay(String base) {
			String path="static/un.png";
			Pay p=new Pay();
			p.setId(id);
			p.setImg(path);
			p.setName(name);
			  return p;
		}
		
		public static String mkOrder(PaymentLog log,String path,SCardorder cardOrder) {
			
			path+="WEB-INF/classes/bocommjava/ini/BocompayMerchant.xml";
			BocomClient client=new BocomClient();
			int ret=client.initialize(path);
			if(ret!=0) {
				return null;
			}
			
			String mercertid="301110007639500";//商户证书id
			String merptcid="301110007639500";//一级商户协议号
			String datetime=DateFormatUtils.format(new Date(), "yyyyMMdd HHmmss");
			String date=datetime.split(" ")[0];
			String time=datetime.split(" ")[1];
			String tranCode="BPAYPY4101";
			//设置报头
			client.setHead("TranCode", tranCode);
			client.setHead("MerPtcId", merptcid);
			client.setHead("TranDate", date);
			client.setHead("TranTime", time);
			
			String merTranserialNo=log.getSn();   //商家流水号
			client.setData("MerTranSerialNo", merTranserialNo);
			BocomDataMap busInfos=new BocomDataMap();
			String merOrderNo=log.getSn();//平台商户订单号
			busInfos.setData("MerOrderNo", merOrderNo);
			client.setData("BusiInfo", busInfos.toString());
			
			BocomDataMap userInfo =new BocomDataMap();
			userInfo.setData("BuyerId",log.getMemberId());
			client.setData("UserInfo", userInfo.toString());
			
			BocomDataMap tranInfo=new BocomDataMap();
			tranInfo.setData("TranModeId", "D");
			BigDecimal bd=log.getAmount();
			System.out.println(bd.setScale(2));
			tranInfo.setData("TranAmt", bd.setScale(2)+"");
			tranInfo.setData("TranCry", "CNY");
			client.setData("TranInfo", tranInfo.toString());
			
			BocomDataMap goodsInfo=new BocomDataMap();
			if(cardOrder==null||TextUtils.isEmpty(cardOrder.getCardName())) {
				goodsInfo.setData("GoodsName","用户充值"+log.getAmount()+"元粮票");
			}else {
				goodsInfo.setData("GoodsName",cardOrder.getCardName()+cardOrder.getKsl()+"张");
			}
			client.setData("GoodsInfo", goodsInfo.toString());
			BocomDataMap memoInfo=new BocomDataMap();
			client.setData("MemoInfo", memoInfo.toString());
			
			BocomDataMap channelInfo=new BocomDataMap();
			channelInfo.setData("ChannelApi", "0000003070");  //通道接口编号
			channelInfo.setData("ChannelInst","");//通道所属机构
			client.setData("ChannelInfo", channelInfo.toString());
			
			System.out.println(client.toString());
			String signData=client.toSignString(mercertid);
			return signData;
		}
		
		public static void setNotify(String base) {
			  String path=base+"WEB-INF/classes/bocommjava/ini/BocompayMerchant.xml";
			  BocomClient client=new BocomClient();
			  int ret=client.initialize(path);
			  if(ret!=0) {
				  System.out.println(client.getLastErr());
			  }else {
				  String mercertid="301140880629503";//商户证书id
					String merptcid="301310063009501";//一级商户协议号
					String datetime=DateFormatUtils.format(new Date(), "yyyyMMdd HHmmss");
					String date=datetime.split(" ")[0];
					String time=datetime.split(" ")[1];
					String tranCode="BPAYPY4151";
					client.initialize(path);
					client.setHead("TranCode", "BPAYPY4020");
					client.setHead("TranTime",time);
					client.setHead("TranDate", date);
					client.setHead("MerPtcId",merptcid);
				  client.setData("ReturnURL", "http://2v217017k7.iask.in/receive.jhtml");
				  client.setData("NotifyURL", "http://2v217017k7.iask.in/receive.jhtml");
				  System.out.println(client.toString());
				  //String rst=null;
				  String rst=client.execute(mercertid, tranCode);
				  if(rst==null){
					  System.out.println("设置失败\n"+client.getLastErr());
				  }else{
					  System.out.println(client.changeNull(client.getHead("RspType")));
					 // System.out.println(client.getHead("RspMsg"));
				  }
			  }
		}
}
