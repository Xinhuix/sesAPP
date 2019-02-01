<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <%@ taglib prefix="c"  uri= "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>其他直播</title>
<script type="text/javascript"  src="/js/jquery/jquery-3.3.1.min.js"></script>
<script type="text/javascript"  src="/js/jquery/jquery.validate.min.js"></script>
<style type="text/css">
		th{
			font-size:14px;
		}
		.input{
			 border:solid 1px cyan;
			 border-radius:5px;
			 width:150px;
		}
		.input_btn{
			border-radius:2px;
			border:solid thin green;
		}
		.addiv{
			  z-indx:10;
			  position:absolute;
			  width:75%;
			  height:55%;
			  margin-left:12%;
			  background-color:#fff;
			  box-shadow:0px 5px 50px #888;
			  display:none;
		}
		#listTable{
			 border-collapse:collapse;
			 font:14px sans-serif bold;
		}
		#listTable td{
		width:100%;
		white-space:nowrap;
			text-overflow:ellipsis;
			overflow:hidden;
			padding:5px 0 5px 0;
		}
		#listTable tr:hover{
			 background-color:white;
		}
</style>
<script type="text/javascript">

	  function add(){
		  var container=document.getElementsByClassName("addiv")[0];
	     var ts=container.queryAllSelector("name^=OtherPlay.");
	     alert(ts.length);
		//  var img=document.getElementsByName("add_icon")[0];
		  
		   if(title.value==null||title.value==""){
			   alert("请输入标题");
			   title.focus();
			   return  false;
		   }
		   if(play_adr.value==null||play_adr.value==""){
			   alert("请输入播放地址");
			   play_adr.focus();
			   return  false;
		   }
		  if(icon_adr.files.length<=0){
			  alert("请选择缩略图图标");
			  icon_adr.focus();
			  return  false;
		  }
		 var status=document.getElementsByName("add_status")[0];
		  if(!parseInt(status.value)){
			  alert("请选择状态决定是否开始当前直播");
			  status.focus();
			  return  false;
		  }
		 return true;
		
	  }
	  
	  function showimg(path,target){
		      var  f=path.files;
		      var img=document.getElementsByName(target)[0];
		      var url=getObjectURL(f[0]);
		      img.src=url;
	  }
	  function  getObjectURL(file){
		  var url=null;
		  if(window.createObjectURL!=undefined){
			    url=window.createObjectURL(file);
		  }else if(window.URL!=undefined){
			  url=window.URL.createObjectURL(file);
		  }else if(window.webKitURL!=undefined){
			  url=window.webKitURL.createObjectURL(file);
		  }
		  return url;
	  }
	  
	  
	  function addshow(){
		  if($(".addiv")[0].style.display=="block"){
			   $(".addiv")[0].style.display="none";
		  }else{
			  $(".addiv")[0].style.display="block";
		  }
	  }
	  
	  function del(target,id){
		     if(!confirm("确定删除吗？")){
		    	 return;
		     }
		     if(parseInt(id)!=NaN){
		    	  $.post("otherplay/del.jhtml",{id:id},function(data,status){
		    		  alert(data.result.msg);
		    		  if(data.result.code==200){
		    			  	target.parentNode.parentNode.parentNode.removeChild(target.parentNode.parentNode);
		    		  }
		    	  });
		     }
	  }
</script>
</head>
<body  style="height:600px;background-color:#ddf;width:100%;font-family:sans-serif;">
<span style="width:100%;font:bold 17px sans-serif  ;">其他直播管理</span>
<form   action="play.jhtml"     method="post">
	<table   style="width:100%;border:solid 1px #ccf;border-collapse:collapse;table-layout:fixed;">
		<tr>
			<th >标题</th>
			<td><input  class="input"  type="text"    name="title"    placeholder="请输入标题"/></td>
			<th>状态</th>
			<td>
				 <select   name="statu"  class="input">
				 		<option  value="0" >请选择</option>
				 		<c:forEach  items="${page.params.get('status') }"  var="statu">
				 		   <option  value="${statu.index }"> ${statu.name }</option>
				 		</c:forEach>
				 </select>
			</td>
			<td  rowspan="2"  style="border-left:solid thin #ccf;">
			<input type="submit"   class="input_btn"  value="查询" />
				<input  type="button"    class="input_btn"     onclick="addshow()"  value="添加">
			</td>
		</tr>
		<tr>
			<th>日期</th>
			<td><input  type="text"   class="input"  name="cdate"  placeholder="创建日期"/></td>
			<th>操作人</th>
			<td>
				<select  name="operation_id"  class="input">
					<option>请选择</option>
					<c:forEach  items="${page.params.get('employees') }"  var="employee">
						  <option  value="${employee.id }">${employee.username }</option>
					</c:forEach>
				</select>
			</td>
		</tr>
</table>
</form>
<div style="width:99%;height:30px;border:solid thin #ccf;white-space:normal;font-size:0;">
	<input  type="button"  value="标题"     style="height:100%;width:10%;border:0;"/>
	<input  type="button"  value="操作人"    style="height:100%;width:5%;border:0;"/>
	<input  type="button"  value="状态"     style="height:100%;width:6%;border:0;"/>
	<input  type="button"  value="创建日期"     style="height:100%;width:10%;border:0;"/>
	<input  type="button"  value="缩略图"     style="height:100%;margin:0;width:30%;border:0;"/>
	<input  type="button"  value="直播地址"   style="height:100%;width:33%;border:0;"/>
	<input type="button" value="操作" style="height:100%;border:0;width:6%;"/>
</div>
  <div  style="width:100%;height:100%;float:left;border:solid thin #ddf;overflow:auto;overflow-x:hidden;">
  			<table style="width:99%;text-align:center;table-layout:fixed;word-wrap:break-word;"   id="listTable">
  					<c:forEach  items="${page.data }"  var="op">
  						<tr>
  							<td  style="width:10%;border-bottom:solid thin #fff;">${op.title }</td>
  							<td  style="width:5%;border-bottom:solid thin #fff;">${op.username }</td>
  							<td  style="width:6%;border-bottom:solid thin #fff;">
  							<!--<c:forEach  items="${status }"  var="statu">
  									<c:if test="${statu.index==op.status }">${statu.name}</c:if>
  							</c:forEach></td>-->
  							${page.params.get('status')[op.status-1] }
  							<td  style="width:10%;border-bottom:solid thin #fff;">${op.cdate }</td>
  							<td style="width:30%;border-bottom:dashed thin red;">${op.icon_adr }</td>
  							<td  style="width:33%;border-bottom:solid thin #fff;">${op.play_adr }</td>
  							<td  style="width:5%;border-bottom:dashed thin red;">
  									<button  style="margin:0;padding:0;border:0;background-color:transparent;"   onclick="update()">改</button>
  									<button style="margin:0;padding:0;border:0;"   onclick="del(this,'${op.id}')">×</button>
  							</td>
  						</tr>
  					</c:forEach>
  			</table>
  </div>
  <div  class="addiv" >
  					<table  style="width:100%;height:80%;border-collapse:collapse;line-height:200%;table-layout:fixed;border-bottom:solid thin #ccc;">
  						  <tr>
  						  	<th  style="font-size:17px;border-bottom:solid thick #fcc;"  colspan="2" >添加直播</th>
  						  	<th   style="border-bottom:solid thick #fcc;" >图标预览</th>
  						  </tr>
  						  <tr>
  						  	<th>标题</th>
  						  	<td><input  type="text"    placeholder="请输入标题"    name="OtherPlay.title"    class="input"/>*</td>
  						  	<td  rowspan="5"   style="border-left:solid thin #ccf;">
  						  		<img alt=" 图像预览"    src=""    name="add_icon"   style="max-height:50%;display:block;" />
  						  	</td>
  						  </tr>
  						  <tr>
  						  	<th>播放地址</th>
  						  	<td><input type="text"   class="input"  name="OtherPlay.play_adr"   placeholder="请输入播放地址"/></td>
  						  </tr>
  						  <tr>
  						  	<th>图标地址</th>
  						  	<td><input  type="file"   onchange="showimg(this,'add_icon')"  style="border-style:outset;" class="input"   accept="image/*"  name="OtherPlay.icon_adr"  placeholder="点击选择图片"></td>
  						  </tr>
  						  <tr>
  						  	<th>状态</th>
  						  	<td>
  								<select  name="OtherPlay.status"   class="input">
  								<option>请选择...</option>
  								<c:forEach  items="${status }"  var="statu">
  									<option  value="${statu.index }">${statu.name }</option>
  								</c:forEach>
  								</select>					  	
  						  	   </td>
  						  </tr>
						<tr>
							<th>备注	</th>
							<td  >
								<textarea  cols="20"  rows="5"   name="OtherPlay.description"   placeholder="请输入备注"></textarea>
							</td>
						</tr>
  					</table>
  					<div  style="width:100%;text-align:center;padding:50px;">
  						<input     type="submit"  value="添加" class="input"    onclick="add()" >
  						<input type="button"   value="重置" class="input"    style="border-color:blue; "/>
  						<input type="button"   value="关闭" class="input"  style="border-color:red;"   onclick="addshow()">
  					</div>
  </div>
</body>
</html>