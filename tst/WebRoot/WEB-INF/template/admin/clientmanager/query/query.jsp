<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html >
<html   style="height:100%;width:100%;overflow:hidden;">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>客户级别管理</title>
<style type="text/css">
	     .input {
	     	border-radius:5px;
	     	border:solid thin cyan;
	     	background-color:#F5ECCE;
	     }
	     .btn{
	     	border:none;
	     	background-color:#CEE3F6;
	     	line-height:250%;
	     }
     #list tr:hover{
       background-color:#F7F7F7 ;
       font-size:14px;
       border-bottom:solid 1px red;
     }
     #list td{
     	border-bottom:solid 1px #f7f7f7;
     }
     #list{
     	border-collapse:collapse;
     }
     .addDiv{
     	 width:70%;
     	 height:50%;
     	 margin:8% 0 0 15%;
     	 z-index:11;
     	position:absolute;
     	 background-color:#CEE3F6;
     	 box-shadow:0 0 10px 5px #ccf inset;
     	 display:none;
     	 padding:10px;
     }
     #list span:hover{
     	color:red;
     	cursor:pointer;
     }
     .topDiv{
     	  width:300px;
     	  height:50px;
     	  box-shadow:0px 0px 20px 5px #ccf inset;
     	  position:absolute;
     	  z-index:11;
     	  background-color:#CEE3F6;
     	  display:none;
     	  border-radius:2px;
     	  overflow:hidden;
     	  overflow-x:auto;
     	  text-align:center;
     	  padding:5px;
     }
     .topSpan{
     	  height:100%;
     	  width:30px;
     	  text-overflow:ellipsis;
     	  border-raidus:2px;
     	  background-color:#ff5809;
     	  border:solid thin red;
     }
     .topSpan:hover,.topSpan_free:hover{
     	 border-radius:5px;
     	 background-color:#CEE3F6;
     }
     .topSpan_free{
     	 height:100%;
     	 width:30px;
     	 text-overflow:ellipsis;
     	 background-color:#ccf;
     	 border-radius:2px;	
     	 border:solid thin #ccf;
     }
</style>
<link rel="stylesheet"  type="text/css"  href="/ui/themes/default/easyui.css">
	<link rel="stylesheet"  type="text/css"  href="/ui/themes/icon.css">
	<link rel="stylesheet"  type="text/css"  href="/ui/demo/demo.css">
	<script type="text/javascript"  src="/ui/jquery.min.js"></script>
	<script type="text/javascript"  src="/ui/jquery.easyui.min.js"></script>
	<script type="text/javascript"  src="/js/common.js"></script>
	<script type="text/javascript">
		$(function(){
			$("#stype").combo({
				editable:false,
				required:true,
				label:"搜索类型：",
				maxWidth:300
			});
			$("#stype-attach").appendTo($("#stype").combo("panel"));
			$("#stype-attach label").click(function(){
				var v=$(this).attr("value");
				alert(v);
				$("#stype").combo("setText",$(this).text()).combo("hidePanel").combo("setValue",v);
			});
		});
		
		function showImg(){
			var f=document.getElementsByName("searchRecord.img")[0].files;
			var view=document.getElementsByName("preview")[0];
			var reader=new FileReader();
			reader.onload=function(evt){
				  view.src=evt.target.result;
					view.title="宽度："+view.naturalWidth+"\n高度："+view.naturalHeight;
			}
			reader.readAsDataURL(f[0]);
		
		}
		
		function add(){
					var div=document.getElementById("addDiv");
					var xhr=null;
					if(XMLHttpRequest) xhr=new XMLHttpRequest;
					else xhr=new ActiveXObject("microsoft.XMLHttpRequest");
					var member=div.ownerDocument.querySelectorAll("[name^='searchRecord']");
					var formData=new FormData();
					for(var i in member){
						if(member[i].type=="file"){
							formData.append(member[i].name,member[i].files[0]);
						}else{
							formData.append(member[i].name,member[i].value);
						}
					}
					if(xhr==undefined)alert("组建初始化失败，无法上传");
					else{
						xhr.open("post","query/add.jhtml",true);
						xhr.onload=function(){
							alert(xhr.responseText);
						}
						xhr.send(formData);
					}
		}
		
		function del(container,id){
			if(!confirm("您确定删除这条信息吗？")){
				return;
			}
			var xhr;
			if(XMLHttpRequest)xhr=new XMLHttpRequest();
			else xhr=new ActiveXObject("microsoft.XMLHttpRequest");
			xhr.open("post","query/del.jhtml",true);
			xhr.setRequestHeader("Content-type","application/x-www-form-urlencoded");
			xhr.onload=function(){
				  if(xhr.responseText=="success"){
					  var r=container.parentNode.parentNode;
					  r.parentNode.removeChild(r);
				  }else{
					  alert("删除失败");
				  }
			}
			xhr.send("id="+id);
		}
		function update(obj){
			if(!obj){
				alert("数据缺失，无法更改");
				return;
			}
			  var xhr;
			  if(XMLHttpRequest)xhr=new XMLHttpRequest();
			  else xhr=new ActiveXObject("microsoft.XMLHttpRequest");
			  xhr.open("post","query/getSample.jhtml");
			  xhr.setRequestHeader("Content-type","application/x-www-form-urlencoded");
			  xhr.send("id="+obj);
			  xhr.onreadystatechange=function(){
				   if(xhr.readyState==4){
					   var o=xhr.responseText;
					   if(o=="fail"){
						   alert("未知错误，无法修改");
					   }
					   var os=eval("("+o+")");
					   divshow("addDiv");
					   var container=document.getElementById("addDiv");
					   var button=container.ownerDocument.getElementById("submitButton");
						button.onclick=us;
					   for(var key in os){
						     var field=container.querySelector("[name^='searchRecord."+key+"']");
						   	if(field){
						   		   if(field.type=="text"||field.type=="textarea"||field.type=="hidden"){
						   			 
						   			   field.value=os[key];
						   		   }else if(field.type=="select-one"){
						   			
						   			      for(var c in field.children){
						   			    	    if(field.children[c].value==os[key]){
						   			    	    	  field.children[c].selected="selected";
						   			    	    }
						   			      }
						   		   }else if(field.type=="file"){
						   			      var img=document.getElementsByName("preview")[0];
						   			      img.src=os[key];
						   			      img.title="宽度："+img.naturalWidth+"\n高度："+img.naturalHeight;
						   		   }
						   		   
						   	}
					   }
				   }
			  }
		}
		
		function us(){
			 var xhr;
			 if(XMLHttpRequest)xhr=new XMLHttpRequest();
			 else xhr=new ActiveXObject("micrsoft.XMLHttpRequest");
			 var container =document.getElementById("addDiv");
			 var member=container.querySelectorAll("[name^='searchRecord']");
			 var fd=new FormData();
			 for(var i in member){
				 if(member[i].type=="file"){
					  if(member[i].files.length>0){
						  fd.append(member[i].name,member[i].files[0]);
					  }
				 }else{
					 fd.append(member[i].name,member[i].value);
				 }
			 }
			 xhr.open("post","query/update.jhtml",true);
			// xhr.setRequestHeader("Content-type","application/x-www-form-urlencoded");
			 xhr.send(fd);
			xhr.onload=function(){
				if(xhr.responseText=="success"){
					 alert("修改成功");
					 document.getElementById("submitButton").onclick=add;
				}
			}
		}
		
		function topReady(id){
			var e=event||window.event;
			var topContainer=document.getElementById("topDiv");
			divshow("topDiv");
			var lackWidth=topContainer.offsetWidth/2;
			var lackHeight=topContainer.offsetHeight/2;
			var  marginl=e.clientX-lackWidth;
			var margint=e.clientY-lackHeight;
			topContainer.style.marginLeft=marginl+"px";
			topContainer.style.marginTop=margint+"px";
			
			var xhr;
			if(XMLHttpRequest) xhr=new XMLHttpRequest;
			else xhr=new ActiveXObject("microsoft.XMLHttpRequest");
			xhr.open("get","query/getTops.jhtml",true);
			xhr.send();
			xhr.onreadystatechange=function(){
				 if(xhr.readyState==4&&xhr.status==200){
					    var text=xhr.responseText;
					    var obj=eval("("+text+")");
					    var html="";
					    var height=topContainer.offsetHeight/2;
					    for(var i in obj){
					    	   if(obj[i].sid==undefined){
					    		   html+="<button  class='topSpan_free'  title='当前排位未占用'  onclick='top(this,"+id+","+obj[i].orders+")'>"+obj[i].orders+"</button>";
					    	   }else{
					    		   html+="<button  class='topSpan'  title='当前搜索关键字："+obj[i].stext+"'  onclick='top(this,"+id+","+obj[i].orders+")'>"+obj[i].orders+"</button>";
					    	   }
					    }
					    topContainer.innerHTML=html;
				 }
			}
		}
		function top(ts,id,index){
			  var xhr;
			  if(XMLHttpRequest)xhr=new XMLHttpRequest();
			  else xhr=new ActiveXObject("microsoft.XMLHttpRequest");
				 xhr.open("post","query/top.jhtml",true);
				xhr.setRequestHeader("Content-type","application/x-www-form-urlencoded");
				 xhr.send("id="+id+"&index="+index);
			  xhr.onload=function(){
				  if(xhr.readyState==4&&xhr.status==200){
					  alert(xhr.responseText);
				  }
			  }
		}
	</script>
</head>
<body  style="width:100%;height:100%;overflow:hidden;">
<!-- 置顶栏 -->
<div  class="topDiv"  id="topDiv"  onmouseout="divshow('topDiv')">
		
</div>
<!-- 添加栏 -->
<div  class="addDiv"   id="addDiv"  >
		<h3  style="display:inline-block;">搜索操作</h3><button  style="float:right;background-color:rgba(0,0,0,0);border:none;"   onclick="divshow('addDiv')">X</button>
		<table  style="width:100%;height:90%;table-layout:fixed;">
			<tr>
				<td>搜索关键字：</td>
				<td >
					<textarea rows="5" cols="20"    style="overflow:hidden;"   name="searchRecord.stext"  class="input"  maxlength="200" ></textarea>
				</td>
				<td   colspan="3"  rowspan="5"   style="text-align:center;">
					<img src=""   name="preview"   style="max-width:400px;max-height:300px;"    alt="鼠标放在图片上可看见分辨率"/>
					<input type="hidden"   name="searchRecord.sid" />
				</td>
			</tr>
			<tr>
				<td >搜索领域：</td>
				<td>
					<select  name="searchRecord.search_type"  class="input" >
						<c:forEach  items="${page.params.get('type') }"   var="t">
								<option  value="${t.key }">${t.value }</option>
							</c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<td>匹配数：</td>
				<td>
					<input type="text"  name="searchRecord.ssum"  class="input"  placeholder="默认随机生成"/>
				</td>
			</tr>
			<tr>
				<td>置顶图：</td>
				<td><input type="file"  accept="image/*"  name="searchRecord.img"   style="border:solid thin white; "  class="input"  onchange="showImg()"/></td>
			</tr>
			<tr>
				<td>是否置顶：</td>
				<td>
					<select name="searchRecord.orders"   class="input" >
						<c:forEach items="${page.params.get('order') }"  var="order">
							 <option  value="${order.ordinal() }">${order.name }</option>
						</c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<td  colspan="5"  style="text-align:center;">
					<button   id="submitButton"  onclick="add()">确定</button>
				</td>
			</tr>
		</table>
	
</div>
<!-- 隔离栏 -->
		<span  style="width:100%;font:bold 17px sans-serif ;border-bottom:solid thin black;">客户端搜索管理</span>
		<div  style="width:100%;border:solid  thin #cfc;">
			<form  action=""   method="post" >
				<table  style="width:100%;height:100%;font:normal 15px  sans-serif;">
					<tr>
						<td style="text-align:center;">搜索关键字：</td>
						<td><input type="text"   class="input"  placeholder="关键字.."  name="levelname" />
						</td>
						<Td>
							<input  id="stype"   class="easyui-panel"   style="height:25px;" />
									<div id="stype-attach"  style="padding:10px;height:50px;overflow-y:auto;">
										<c:forEach  items="${page.params.get('type') }"  var="t">
										<label  value="${t.key }">${t.value }</label>
										</c:forEach>
									</div>
						</Td>
						<td colspan="2" >
							<input type="button"    class="btn"   value="查询"/>
							<input type="button"    onclick="divshow('addDiv')"     class="btn"   value="添加"/>
						</td>
					</tr>
				</table>
			</form>
		</div>
		<div  style="width:100%;height:60%;font:normal 15px  sans-serif;border:solid thin #f7f7f8;">
					<div  style="width:100%;white-space:nowrap;">
						<button  style="width:40%;border-radius:0;margin:0;"  class="btn">搜索关键字</button>
						<button  style="width:10%;border-radius:0;margin:0 0 0 -4.5px;"  class="btn">置顶次序</button>
						<button  style="width:15%;text-align:center;margin:0 0 0 -4px;" class="btn">匹配量</button>
						<button  style="width:20%;margin:0 0 0 -4px;" class="btn">搜索领域</button>
						<button  style="width:15%;margin:0 0 0 -5px;"  class="btn">操作</button>
					</div>
					<div style="width:100%;max-height:90%;overflow-y:auto;scrollbar-arrow-color:white;scrollbar-face-color:white;scrollbar-shadow-color:white;scrollbar-base-color:#CEE3F6">
						<table  style="width:100%;text-align:center;"  id="list">
							<c:forEach  items="${page.data }"   var="record">
								<tr style="line-height:200%;">
									<td  style="width:40%;text-align:left;">${record.stext }</td>
									<c:if test="${record.orders>0 }">
										<td  style="width:10%;text-align:left;font:12px sans-serif bold;color:#ccf;">${page.params.get("order")[record.orders].name }</td>
									</c:if>
									<c:if test="${record.orders==0 }">
										<td  style="width:10%;text-align:left;font:12px sans-serif  bold;color:blue;">${page.params.get("order")[record.orders].name }</td>
									</c:if>
									<td  style="width:15%">${record.sum }</td>
									<Td  style="width:20%;">${page.params.get("type").get(record.type) }--${record.type }</Td>
									<Td style="font-size:12px;"><span onclick="topReady(${record.id})">置顶</span>-----<span  onclick="update(${record.id})">修改</span>----<span onclick="del(this,${record.id })" >删除</span></Td>
								</tr>
							</c:forEach>
						</table>
					</div>
			</div>
			<%@include file="../../include/page.jsp" %>
			
</body>
</html>