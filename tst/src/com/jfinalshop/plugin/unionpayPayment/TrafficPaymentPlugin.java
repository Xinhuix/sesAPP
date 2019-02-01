package com.jfinalshop.plugin.unionpayPayment;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.jfinalshop.Setting;
import com.jfinalshop.model.PaymentLog;
import com.jfinalshop.model.PluginConfig;
import com.jfinalshop.plugin.PaymentPlugin;
import com.jfinalshop.util.DateUtils;
import com.jfinalshop.util.SystemUtils;

public class TrafficPaymentPlugin extends PaymentPlugin {

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "银联支付";
	}

	@Override
	public String getVersion() {
		// TODO Auto-generated method stub
		return "1.0";
	}

	@Override
	public String getAuthor() {
		// TODO Auto-generated method stub
		return "郑执";
	}

	@Override
	public String getSiteUrl() {
		// TODO Auto-generated method stub
		return "zxxyule.com";
	}

	@Override
	public String getInstallUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUninstallUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSettingUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRequestUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RequestMethod getRequestMethod() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRequestCharset() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> getParameterMap(String sn, String description, HttpServletRequest request) {
		Setting setting=SystemUtils.getSetting();
		PluginConfig pc=this.getPluginConfig();
		PaymentLog pl=this.getPaymentLog(sn);
		Map<String,Object> pm=new HashMap<String,Object>();
		pm.put("TranCode", "BPAYPY4101");//交易码
		String datetime=DateUtils.formatDate(new Date(),"yyyyMMdd HHmmss");
		pm.put("TranDate",datetime.split(" ")[0]);//交易日期
		pm.put("TranTime", datetime.split(" ")[1]);//交易时间
		pm.put("MerPtcId", "301310063009501");//一级商户协议号
		pm.put("MerTranSerialNo",sn );//商户流水号
		pm.put("SafeReserved", "");//安全观察域
		
		
		
		 
		
		return null;
	}

	@Override
	public boolean verifyNotify(NotifyMethod notifyMethod, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getSn(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getNotifyMessage(NotifyMethod notifyMethod, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
