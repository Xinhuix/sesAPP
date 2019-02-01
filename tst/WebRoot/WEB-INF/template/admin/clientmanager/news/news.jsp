<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTf-8"%>
    <%@taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>客户端资讯中心</title>
<link type="text/css"  rel="stylesheet"  href="/css/common.css">
</head>
<body>
<div  class="addiv"  id="adddiv"></div>
<div  style="height:20%;">
	<span  class="span">终端资讯管理</span>
	<form action=""   onsubmit="">
		<table  class="table"  style="width:100%;line-height:200%;height:100%;">
			<tr>
				<td>资讯类型：</td>
				<td><select  name="acategory"  class="inp">
						<option  value="all">显示全部</option>
						<c:forEach items="${acategorys }"  var="category">
								<option value="${category.id }">${category.name }</option>
						</c:forEach>
				</select></td>
				<td>查询类型：</td>
				<td><select  class="inp"  name="st">
					<option value="0">请选择</option>
					<option value="1">名称</option>
					<option value="2">日期</option>
					<option value="3">作者</option>
					<option value="4">是否置顶</option>
				</select></td>
				<td>查询条件：</td>
				<td><input type="text"  class="inp"  name="svalue"/></td>
				<td><input   class="button"  type="submit"  value="查询"/>
				<button class="button"  onclick="divshow('adddiv')">添加</button>
				</td>
			</tr>
		</table>
	</form>
</div>
<div style="width:105%;height:80%;">
<div></div>
		<table  class="table listtable"  style="width:100%;">
			<thead>
				<tr>
					<th>名称</th>
					<th></th>
					<th></th>
					<th></th>
				</tr>
			</thead>
		</table>
</div>
</body>
</html>