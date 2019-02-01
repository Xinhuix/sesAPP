<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link  type="text/css"  rel="stylesheet"  href="/css/common.css" />
<script type="text/javascript"   src="/js/common.js"></script>
<script type="text/javascript">
	window.onload=function(){
		 if("${result}"==1){
			 alert("操作成功！");
		 }else if("${result}"==2){
			 alert("操作失败");
		 }
	}
   function   pi(){
	   var file=document.getElementsByName("adimg")[0].files;
		var pv=document.getElementById("preview");
		var reader=new FileReader();
		reader.onload=function(event){
			pv.src=event.target.result;
		}
		reader.readAsDataURL(file[0]); 
}
   
</script>
<style type="text/css">
		#addtable td{
		  width:25%;
		}
</style>
</head>
<body>
	<div class="adddiv"  id="adddiv"  style="display:none;">
		<h4  style="text-align:CENTER;table-layout:fixed;">广告添加</h4><hr/>
		<form action="/admin/clientcontro/clientad/add.jhtml"   onsubmit=""    method="post"    enctype="multipart/form-data" >
		<table   style="width:100%;height:100%;text-align:center;table-layout:fixed;"  id="addtable">
			<tr>
				<th colspan="2">添加项</th>
				<th colspan="2">广告预览</th>
			</tr>
			<tr>
				<td>名称：</td>
				<td ><input type="text"  class="inp"  name="adname"/></td>
				<td style="vertical-align:middle;" colspan="2"   rowspan="7">
					<img  src=""     id="preview"     alt="上传图片预览"     style="margin:0 auto;max-height:200px;max-width:100%;"/>
				</td>
			</tr>
			<tr>
				<td>标题</td>
				<td><input type="text" class="inp" name="title"   placeholder="请输入标题"/></td>
			</tr>
			<tr>
				<Td>图片：</Td>
				<td><input  type="file"   class="inp"   onchange="pi()"   accept="image/*"  style="border:none;outline:none;di"  name="adimg"  /></td>
			</tr>
			<Tr>
			<td>是否置顶</td>
				<td><select   name="isup">
					<c:forEach  items="${upper }"  var="up">
						  <option  value="${up.index }">${up.label }</option>
					</c:forEach>
				</select></td>
			</Tr>
			<tr>
				<td>状态</td>
				<Td>
					<select name="status">
						<option  value="0">禁用</option>
						<option  value="1">启用</option>
					</select>
				</Td>
			</tr>
			<tr>
				<Td>活动地址：</Td>
				<td><input  class="inp"  type="text"  name="adpath"/></td>
			</tr>
			<tr>
				<td>类别：</td>
				<td><select class="inp"  name="adtype">
					<c:forEach items="${type.gettype() }"    var="t">
						  <option value="${t.index }">${t.value }</option>
					</c:forEach>
				</select></td>
			</tr>
			<tr>
				<td>详细描述</td>
				<td>
					<textarea  name="description"   class="inp"   cols="10"  rows="5"></textarea>
				</td>
			</tr>
			<tr></tr>
			<tr>
				<td rowspan="5"   colspan="4"  style="border-top:black solid thin;">
					<input type="submit"  class="button"  style="width:15%;;margin-right:30%;"  value="添加" />
				<input type="button" class="button"   onclick="divshow('adddiv')"  value="关闭"   style="width:15%;background-color:red;"/>
				</td>
			</tr>
		</table>
		</form>
	</div>
		<div style="height:40%;">
			<span class="span">客户端广告管理</span>
			<form action="/admin/clientcontro/clientad/search.jhtml"     onsubmit="">
				<table class="table"   style="width:100%;">
					<tr>
						<th><input type="button"  class="button"  value="添加" onclick="divshow('adddiv')"/></th>
						<th colspan="4">客户端广告查询</th>
						<th colspan="4">广告预览</th>
					</tr>
					<tr>
						<td  >类型：</td>
						<td    colspan="2" ><select  class="inp" name="searchtype">
								<option value="-1">请选择</option>
								<c:forEach  items="${type.gettype() }"  var="t">
										<option  value="${t.index }">${t.value }</option>
								</c:forEach>
						</select>
						</td>
						<td  rowspan="5" colspan="2"  style="border-right:solid  thick #95c1d0">
							<input type="submit"  class="button"  value="查询"/>
						</td>
						<td  colspan="4"  rowspan="10">
						</td>
					</tr>
					<tr>
					<td >操作人：</td>
						<td  class="td"  colspan="2">
							<select class="inp"   name="operation_role" >
							<option value="-1">请选择</option>
							<c:if test="${operations!=null }">
								<c:forEach items="${operations }"  var="e">
									<option value="${e.id }">${e.username }</option>
								</c:forEach>
							</c:if>
							</select>
						</td>
				
					</tr>
					<tr>
						<td>名称：</td>
						<td colspan="2">
							<input type="text" class="inp"   name="searchname" placeholder="支持模糊查询"/>
						</td>
					</tr>
					<tr>
						<td>添加时间：</td>
						<td colspan="2">
							<input type="date"  class="inp"   placeholder="日期（****年**月**日）"  name="search_date"/> 
						</td>
					</tr>
				</table>
			</form>
		</div>
		
		<div  style="width:105%;height:60%;">
			<div style="width:100%;height:7%;border:thin solid black;border-left:none;">
				<span class="tabletitle"   style="width:14%;height:100%;">名称</span>
				<span class="tabletitle"    style="width:14%;">图片地址</span>
				<span  class="tabletitle"    style="width:14%;" >类别</span>
				<span  class="tabletitle"    style="width:14%">操作人</span>
				<span  class="tabletitle"   style="width:14%;">活动地址</span>
				<span   class="tabletitle"  style="width:14%">创建日期</span>
				<span class="tabletitle"  style="width:14%;"> 操作</span>
			</div>
			<div style="width:100%;height:90%;overflow:auto;">
					<table class="table  listtable"  style="width:1100px;border:none;">
						<c:if test="${ads!=null }">
								<c:forEach items="${ads }"  var="ad">
								<tr   onclick="showimg()">
									<td>${ad.ad_name }</td>
									<td>${ad.ad_img }</td>
									<td>${type.getvalue(ad.ad_type) }</td>
									<td>${ad.username }</td>
									<td>${ad.ad_path }</td>
									<td>${ad.ad_date }||${ad.id }</td>
									<td><a href="/admin/clientcontro/clientad/del.jhtml?id=${ad.id }"   style="text-align:CENTER;text-decoration:none;">删除</a></td>
									</tr>
								</c:forEach>
						</c:if>
						<c:if test="${ads==null }">
						<tr>
							<th colspan="5">暂无广告</th>
							</tr>
						</c:if>
					</table>
			</div>
				
		</div>
		
</body>
</html>