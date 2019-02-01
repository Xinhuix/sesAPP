<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>欢迎页</title>
<style type="text/css">
.title_style {
	margin-left: 10%;
}

.ip_style {
	border-radius: 5px;
	width: 180px;
	border: none;
	border: thin solid #f0f0f0;
}

.ip_style:hover {
	border-color: #cd
}


#tt  td{
	border-bottom:solid  thin #f9f9f9;
	line-height:150%;
}
#tt th{
	border-bottom:double thin #8f8f8f;
}

#addwin{
position:fixed;
	z-index:2;
	margin:10%  14%  24% 14%;
	background-color:white;
	border:solid thin #f8f8f8;
	width:70%;
	height:50%;
	box-shadow: 0 5px  50px #888;
}
#addwin td{
	line-height:300%;
}
.img{
	width:180px;
	height:260px;
}
#tt  a{
	text-decoration:none;
}
</style>
<link href="/css/common.css"  type="text/css"  rel="stylesheet"/>
</head>
<script type="text/javascript">

		function addshow(pattern){
			var div=document.getElementById("addwin");
			if(pattern===1){
				div.style.display="block";
			}else{
				div.style.display="none";
			}
		}
		
		function showimg(){
			var pt=document.getElementsByName("path")[0].files;
			var img=document.getElementById('schma');
			var reader=new FileReader();
			reader.onload=function(evt){
				img.src=evt.target.result;
			}
			reader.readAsDataURL(pt[0]);
		}
		
		function subs(){
			var bt=document.getElementsByName("boot_name")[0];
			var file=document.getElementsByName("path")[0];
			if(!(bt.value)){
				alert("请输入引导名称");
				return false;
			}else if(!file.files[0]){
				alert("请选择引导图");
				return false;
			}
		}
		
		function ssub(){
			var s=document.getElementsByName("st")[0];
			var v=document.getElementsByName('sv')[0];
			var b=parseInt(s.value);
			if(b){
				  if(!v.value){
					  alert('请输入查询条件！');
					  return false;
				  }
			}else{
				alert("请选择查询类别！");
				return  false;
			}
		}
		
		function pv(vd){
			document.getElementById('img').src="/upload/"+vd;
		}
</script>
<body style="border:none">
<div id="addwin"  style="z-index:10;padding:10px 0;font: bold 15px sans-serif ;display:none">
	<h4 style="text-align:CENTER;">添加引导页</h4>
	<hr/>
	<form action="/admin/clientmanager/add.jhtml"   method="post"   onsubmit="return subs()" enctype="multipart/form-data">
		<table style="margin:0 auto; width:70%;height:80%;">
				<tr>
					<td>名称：</td>
					<td><input type="text"  placeholder="请输入名称"  name="boot_name"  style="width:80%"></td>
					<td rowspan="5">
						<img alt="引导预览"  src=""   id="schma"   class="img" />
					</td>
				</tr>
				<tr>
					<td>图片：</td>
					<td><input type="file"   name="path"   onchange="showimg()"  style="width:80%"/></td>
					<td></td>
				</tr>
				<tr>
					<td>地址：</td>
					<td><input  type="text"  name="addr"   style="width:80%;" placeholder="请输入活动地址"></td>
				</tr>
				<tr>
					<td colspan="2"   style="line-height:100px;padding-left:20%;">
						<input type="submit"  value="提交"   class="bt_style" />
							<input type="button" value="关闭"  class="bt_style"    onclick="addshow(2)"  style="margin-left:10%;"/>
					</td>
				</tr>
		</table>
	</form>
</div>
	<span style="width: 100%; background-color: #f0f0f0; font: bold 17px sans-serif; border: solid thin #f0f0f0;">引导页管理</span>
	<div style="width: 100%; border: solid thin #f0f0f0; height: 30px; padding-top: 10px; font: bold 12px sans-serif;">
		<button style="margin: 0 20px 0 20px; float: left;" class="button"   onclick="addshow(1)">添加</button>
		<form action="/admin/clientmanager/boot.jhtml"   onsubmit="return ssub()" style="border-left: thin #f0f0f0 solid;">
			<span class="title_style" >查询类别：</span> <select name="st"    class="ip_style">
				<option value="0">请选择查询类别</option>
				<option value="1">添加日期</option>
				<option value="2">引导名称</option>
			</select> <span class="title_style">查询条件：</span> <input type="text"  value="${sv }"  name="sv"  class="ip_style" /> 
			<input  type="submit"  value="查询"   class="bt_style"   style="margin-left: 5%;" />
		</form>
	</div>
	<div style="width: 100%; height:100%;border:thin solid #B0E2FF;border-top-color:black; font: 15px sans-serif;text-align:center;">
	<div style="width:75.5%;height:100%;float:left;overflow:hidden;">
		<div style="width:105%;height:100%;overflow:auto" >
			<table id="tt"   class="listtable"  style="width: 100%;border-collapse:collapse;table-layout:fixed;word-wrap:break-word;">
			<tr >
				<th>名称</th>
				<th>活动地址</th>
				<th>状态</th>
				<th>创建日期</th>
				<th>操作人</th>
				<th>操作</th>
			</tr>
			<c:if test="${rs==null }">
				<tr><td colspan="5" >抱歉，当前暂无引导页,请点击左上角添加引导页</td></tr>
			</c:if>
			<c:if test="${rs!=null }">
				   <c:forEach items="${rs }" var="r">
				   		<tr >
				   			<td onclick="pv('${r.img}')">${r.name }</td>
				   			<td>${r.path }</td>
				   			<c:if test="${r.state==0 }">
				   				<td>过期</td>
				   			</c:if>
				   			<c:if test="${r.state==1 }">
				   				<td>正常</td>
				   			</c:if>
				   			<td>${r.cdate }</td>
				   			<td>${r.username }</td>
				   			<td>
				   				<a href="/admin/clientmanager/del.jhtml?id=${r.id }">删除</a>
				   			</td>
				   		</tr>
				   </c:forEach>
			</c:if>
		</table>
		</div>
		</div>
		<div style="width:24%;height:100%;float: right; border-left: 5px double #f0f0f0;margin-left:-20px; ">
		<span>预览页</span>
			<img id="img"  alt="引导预览" style="height: 400px; width: 95%; margin:auto auto;border:none;"/>
		</div>
	</div>
</body>
</html>