package com.jfinalshop.controller.admin.plugin;

import com.jfinal.ext.route.ControllerBind;
import com.jfinalshop.controller.admin.BaseController;
import com.jfinalshop.model.PluginConfig;
import com.jfinalshop.plugin.unionpayPayment.TrafficPaymentPlugin;
import com.jfinalshop.service.PluginConfigService;

@ControllerBind(controllerKey="../admin/payment_plugin/trafficpay")
public class TrafficpayController extends BaseController {
		
	private TrafficPaymentPlugin tpp=new TrafficPaymentPlugin();
	private PluginConfigService configservice=this.enhance(PluginConfigService.class);
	
	//安装插件
	public void install(){
		if(!tpp.getIsInstalled()){
			PluginConfig pc=new PluginConfig();
			pc.setPluginId(tpp.getId());
			pc.setIsEnabled(true);
			configservice.save(pc);
		}
		this.renderJson(SUCCESS_MESSAGE);
		
	}
	
	
	//卸载
	public void uninstall(){
		if(tpp.getIsInstalled()){
			configservice.deleteByPluginId(tpp.getId());
		}
		this.renderJson(SUCCESS_MESSAGE);
	}
	
	//设置
	
	public void  receivePay(){
		System.out.println("is move");
	}
}
