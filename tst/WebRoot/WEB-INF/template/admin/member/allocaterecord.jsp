<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>   
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>分红记录</title>
</head>
<script type="text/javascript">
	function selchange(){
		
		var stype=document.getElementsByName("searchtype")[0];
		var svalue=document.getElementsByName("searchvalue")[0];
		if(stype.value=="1"){
			svalue.placeholder="请输入要查询的用户名！";
		}else if(stype.value=="2"){
			svalue.placeholder="格式=‘20180101’";
		}else{
			svalue.placeholder="←请选择查询项,默认全查";
		}
	}
	
	function onsub(){
		var stype=document.getElementsByName("searchtype")[0];
		var svalue=document.getElementsByName("searchvalue")[0];
		var vu=svalue.value;
		if(parseInt(stype.value)>0&&vu==""){
			alert("请输入查询条件");
			return false;
		}
	}
</script>
<style type="text/css">
	body{
		font-family:sans-serif;
		width:95%;
		height:95%;
	}
	div .h1{
		border:black solid 1px;
		border-bottom:none;
		border-radius:2px;
		
	}
	div table{
		border-collapse:collapse;
		margin-left:2%;
	}
	div td{
		background-color:white;
		border-bottom:thin blue solid;
	}
	li{
		width:25%;
		float:left;
		text-align:CENTER;
	}
</style>
<body>
<div  style="width:100%;border-bottom:#fef double thick;"><strong class="h1">分红记录</strong></div>
<div style="width:100%;border:2px cyan solid;border-top:none;border-radius:5px;">
<form action="/admin/member/allocateRecord.jhtml" onsubmit="return onsub()">
	<table style="width:95%;"><tr>
		<th>快捷查询</th>
		<th><select name="fastquery">
			<option value="0">请选择</option>
			<option value="1">昨日</option>
			<option value="2">上周</option>
			<option value="3">上月</option>
		</select></th>
		<th>条件查询</th>
		<th><select name="searchtype" onchange="selchange()">
			<option value="0">所有</option>
			<option value="1" >用户</option>
			<option value="2">日期</option>
		</select></th>
		<th><input  type="datetime" name="searchvalue" placeholder="请输入查询条件，默认全部查询" /></th>
		<th><input type="submit" value="查询" style="border-radius:5px;border:solid thin red;"></th>
	</tr></table>
</form>
</div>
<div style="width:95%;border-left:thin cyan solid;border-right:cyan thin solid;border-radius:5px;height:20px;margin-left:2%;">
	<ul style="list-style-type:none;width:100%;margin-top:-1em;">
		<li>共计领取人次：</li>
		<li>${page.recordCount }</li>
		<li>派放总量：</li>
		<li>${page.params.get("sum") }&nbsp;积分</li>
	</ul>
</div>
<div style="width:100%;background-color:#ccc;padding-bottom:20px;border-radius:20px 20px  5px 5px;">
<table style="width:95%;text-align:center;">
<tr>
<th>用户名</th>
<th>领取数量</th>
<th>领取日期</th>
<th>领取类型</th>
	</tr>
<c:if test="${page.data.size()>0 }">
		<c:forEach items="${page.data }"  var="r"  varStatus="status">
		<tr>
		 	<td>${r.username }</td>
		 	<td>${r.receivecount }</td>
		 	<td>${r.receivedate }</td>
		 	<td>${page.params.get('atcType').get(r.atype)}</td>
		 	</tr>
		</c:forEach>
	</c:if>
	<c:if test="${page.data.size()<=0}">
		<tr><td colspan="4">
			<h3 style="text-align:CENTER;">当前暂无数据！</h3>
		</td></tr>
	</c:if>
</table>
</div>
<%@ include file="../include/page.jsp"  %>
</body>
</html>