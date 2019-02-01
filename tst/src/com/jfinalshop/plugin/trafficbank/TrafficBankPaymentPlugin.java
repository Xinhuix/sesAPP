package com.jfinalshop.plugin.trafficbank;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.jfinalshop.plugin.PaymentPlugin;

public class TrafficBankPaymentPlugin extends PaymentPlugin {

	@Override
	public String getName() {
		return "交通银行在线支付";
	}

	@Override
	public String getVersion() {
		// TODO Auto-generated method stub
		return "1.0";
	}

	@Override
	public String getAuthor() {
		// TODO Auto-generated method stub
		return "JFinalShop";
	}

	@Override
	public String getSiteUrl() {
		// TODO Auto-generated method stub
		return "http://ses.esgod.com/wap.jhtml";
	}

	@Override
	public String getInstallUrl() {
		// TODO Auto-generated method stub
		return "trafficpay/install.jhtml";
	}

	@Override
	public String getUninstallUrl() {
		// TODO Auto-generated method stub
		return "trafficpay/uninstall.jhtml";
	}

	@Override
	public String getSettingUrl() {
		// TODO Auto-generated method stub
		return "trafficpay/setting.jhtml";
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
		// TODO Auto-generated method stub
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
