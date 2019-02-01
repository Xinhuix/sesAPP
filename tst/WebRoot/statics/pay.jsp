<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.alipay.config.AlipayConfig"%>
<%@page import="com.alipay.api.AlipayClient"%>
<%@page import="com.alipay.api.DefaultAlipayClient"%>
<%@page import="com.alipay.api.AlipayApiException"%>
<%@page import="com.alipay.api.response.AlipayTradeWapPayResponse"%>
<%@page import="com.alipay.api.request.AlipayTradeWapPayRequest"%>
<%@page import="com.alipay.api.domain.AlipayTradeWapPayModel"%>
<%@page import="com.alipay.api.domain.AlipayTradeCreateModel"%>

<%
	if (request.getAttribute("WIDout_trade_no") != null) {

		// 商户订单号，商户网站订单系统中唯一订单号，必填
		String out_trade_no = new String(request
				.getAttribute("WIDout_trade_no").toString()
				.getBytes("ISO-8859-1"), "UTF-8");
		// 订单名称，必填
		String subject = request.getAttribute("WIDsubject").toString();
		
		// 付款金额，必填
		String total_amount = new String(request
				.getAttribute("WIDtotal_amount").toString()
				.getBytes("ISO-8859-1"), "UTF-8");
		// 商品描述，可空
		String body ="";
		if(request.getAttribute("WIDbody")!=null){
		body = new String(request.getAttribute("WIDbody")
				.toString().getBytes("ISO-8859-1"), "UTF-8");
		}

		// 超时时间 可空
		String timeout_express = "2m";
		// 销售产品码 必填
		String product_code = "QUICK_WAP_PAY";
		// SDK 公共请求类，包含公共请求参数，以及封装了签名与验签，开发者无需关注签名与验签     
		//调用RSA签名方式
		AlipayClient client = new DefaultAlipayClient(AlipayConfig.URL,
				AlipayConfig.APPID, AlipayConfig.RSA_PRIVATE_KEY,
				AlipayConfig.FORMAT, AlipayConfig.CHARSET,
				AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.SIGNTYPE);
		AlipayTradeWapPayRequest alipay_request = new AlipayTradeWapPayRequest();

		// 封装请求支付信息
		AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
		model.setOutTradeNo(out_trade_no);
		model.setSubject(subject);
		model.setTotalAmount(total_amount);
		model.setBody(body);
		model.setTimeoutExpress(timeout_express);
		model.setProductCode(product_code);
		alipay_request.setBizModel(model);
		// 设置异步通知地址
		alipay_request.setNotifyUrl(AlipayConfig.notify_url);
		// 设置同步地址
		alipay_request.setReturnUrl(AlipayConfig.return_url);

		// form表单生产
		String form = "";
		try {
			// 调用SDK生成表单
			form = client.pageExecute(alipay_request).getBody();

			response.setContentType("text/html;charset="
					+ AlipayConfig.CHARSET);
			//response.getWriter().write(form);//直接将完整的表单html输出到页面 
			//response.getWriter().flush();
			//response.getWriter().close();
			System.out.println(form);
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
	} else {
		System.out.println("支付宝请求参数有误");
	}
%>
<form  action="<=form"    method="post">
		  <input  type="submit"   value="支付"/>
</form>
