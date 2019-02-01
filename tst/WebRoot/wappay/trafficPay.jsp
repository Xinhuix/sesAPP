<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'trafficPay.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<meta name=”viewport” content=”initial-scale=1, maximum-scale=3, minimum-scale=1, user-scalable=no”>
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  	<script type="text/javascript">
  		 window.onload=function(){
  		 var rel="${result }";
  		 alert(rel);
  					if("success"==rel){
  					alert("success is move!");
  						document.getElementById("d1").style.display="none";
  						document.getElementById("d2").style.display="";
  						document.getElementById("dr").innerHTML="支付成功";
  					}else if(rel=="failed"){
  						document.getElementById("d1").style.display="none";
  						document.getElementById("d2").style.display="visible";
  						document.getElementById("dr").innerHTML="支付失败";
  					}else if(rel=="error"){
  						document.getElementById("d1").style.display="none";
  						document.getElementById("d2").style.display="";
  						document.getElementById("dr").innerHTML="支付错误";
  					}
  		 	}
  	</script>
  <body style="text-align:center;font-size:3rem;">	

  <h5>付款信息</h5>
  <div id="d1">
  <table style="margin:0 auto;border-width:0px;margin-top:3rem;">
  	<tr style="line-height:5rem;">
  	<td>订&nbsp;单&nbsp;号：</td>
  	<td style="center">${sn }</td>
  	</tr>
  	<tr style="line-height:5rem">
  		<td>支付金额：</td>
  		<td>${amount }&nbsp;元</td>
  	</tr>
  	<tr style="line-height:5rem">
  		<td>商品名称：</td>
  		<td>${goodsname }</td>
  	</tr>
  	
  </table>
    <form action="${urlaction }" method="post"  style="size:3rem;" id="f1">
    	<input type="hidden" value="${signData }" name="signData"/>
    	<input type="submit" style="font-size:4rem;line-height:5rem;margin-top:10px;width:50%;background-color:#02db3c;"  value="付款"/>
    </form>
    </div>
    <div id="d2" style="display:none;font-size:5rem;">
    	<table>
    		<tr style="line-height:5rem;">
    			<td>订单号：</td>
    			<td>${orderno }</td>
    		</tr>
    		<tr style="line-height:5rem;">
    			<td>金额：</td>
    			<td>${money }&nbsp;元</td>
    		</tr>
    		<tr style="line-height=5rem">
    			<td>支付状态：</td>
    			<td id="dr"></td>
    		</tr>
    	</table>
    </div>
  </body>
</html>
