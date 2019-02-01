package com.jfinalshop.controller;

import java.util.Map;

import com.bocom.bocompay.BocomClient;
import com.bocom.bocompay.OpList;
import com.jfinal.core.Controller;
import com.jfinal.ext.route.ControllerBind;
import com.jfinalshop.model.PaymentLog;
import com.jfinalshop.service.PaymentLogService;
@ControllerBind(controllerKey="/receive")
public class ReceiveController extends Controller {
	private PaymentLogService paymentLogService = enhance(PaymentLogService.class);
		public void index(){
			String notifyMsg=this.getPara("notifyMsg");
			if(notifyMsg==null){
				 this.setAttr("result", "暂无付款结果，请您稍后查看！");
			}else{
				String path=this.getRequest().getServletContext().getRealPath("/");
				path+="WEB-INF/classes/bocommjava/ini/BocompayMerchant.xml";
				BocomClient client=new BocomClient();
				int ret=client.initialize(path);
				if(ret!=0){
					System.out.println("初始化失败：+\n"+client.getLastErr());
				}
				String rst=client.executeReply("301140880629503", notifyMsg);
				if(rst==null){
					this.setAttr("result", "error");
				}else{
					String tradeState=client.changeNull(client.getData("TradeState"));
					System.out.println(tradeState);
					if(tradeState.contentEquals("Paied")){
						this.setAttr("result", "success");
					}else{
						this.setAttr("result","failed");
					}
					Map optList=client.getOpListMap();
					OpList ol=(OpList)optList.get("MerOrderList");
					int inum=ol.getOpInfoNum();
					System.out.println("the inum is: "+inum);
					for(int i=0;i<inum;i++){
						String merOrderno=ol.getInfoValueByName(i,"MerOrderNo");
						if(merOrderno!=null){
							this.setAttr("orderno", merOrderno);
							System.out.println("the merorderno is    :"+merOrderno);
							this.setAttr("money", ol.getInfoValueByName(i,"PaiedSum"));
							PaymentLog pl=paymentLogService.findBySn(merOrderno);
							if(pl.getStatus().equals("0")){
								System.out.println("handle is move!");
								paymentLogService.handle(pl);
							}
							break;
						}
					}
				}
				
			}
			this.renderJsp("/wappay/trafficPay.jsp");
		}
}
