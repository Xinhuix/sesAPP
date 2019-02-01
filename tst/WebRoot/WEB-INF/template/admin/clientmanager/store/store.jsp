<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core"  %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link  href="/css/common.css"   type="text/css"   rel="stylesheet" />
<script type="text/javascript"  src="/js/common.js"></script>
<script type="text/javascript" src="/js/jquery/jquery-3.3.1.min.js"></script>
<style type="text/css">
	  table#ttype  span:hover{
		  border-radius:2px;
		  border:solid thin black;
		  cursor:pointer;
	}
	  .imgbtn{
	  	width:30px;
	  	border:1px solid red;
	  	text-overflow:ellipsis;
	  	border-radius:2px;
	  }
#storeDiv{
		scrollbar-arrow-color: #ccc; /**//*三角箭头的颜色*/ 
		scrollbar-face-color: #333; /**//*立体滚动条的颜色*/ 
		scrollbar-shadow-color: #ccf; /**//*立体滚动条阴影的颜色*/ 
		scrollbar-track-color: #ccc; /**//*立体滚动条背景颜色*/ 
}
#msgBox{
	z-index:15;
	position:absolute;
	text-align:CENTER;
    background-color:#ccf;
    padding:10px 5px 10px 5px;
	margin:50px 0 0 600px;
	display:none;
	border:solid 0.5px red;
	border-radius:5px;
}
  .updateDiv{
  	   position:absolute;
  	   z-index:9;
  	   width:90%;
  	   height:70%;
  	   background-color:white;
  	   margin: 5%  0 0 5%;
  	   border:solid thick #ab3;
  	   border-radius:10px;
  	   display:none;
  	   padding:5px;
  	   overflow:hidden;
  	   overflow-y:auto;
  	   scrollbar-arrow-color:white;
  	scrollbar-face-color:white;
  	scrollbar-trace-color:white;
  	scrollbar-shadow-color:white;
  } 

  .updateDiv  td{
  	  width:15%;
  }
  .updateDiv input{
  	  border:solid thin #ccf;
  	  border-radius:2px;
  }
  
  .img{
  	  max-width:20%;
  	  max-height:200px;
  	  margin:0 5px 0 5px;
  	  transform:rotate(7deg);
  }
  .img:hover{
  	transform:rotate(0deg);
  	transform:scale(2,2);
  	transition-duration:1s;
  	z-index:9;
  }
</style>
<script type="text/javascript">
	var img,images="";
	function  sm(){
		var tn=$("input[name=type_name]");
		 if(!tn.val()){
			 	alert("类型名称不能为空");
		 }else{
			 $.post("/admin/clientcontro/store/addType.jhtml",
					 {
				 tn:tn.val(),
				 tdes:$("textarea[name=type_des]").val()
				 },function(data,status){
				  if(status=="success"){
					  	  var r=data.result;
					  	  if(r=="name_null"){
					  		  alert("名称为空，添加失败");
					  	  }else if(r=="fail"){
					  		  alert("添加失败");
					  	  }else{
					  		 // alert("is vmoe");
					  		  	var tt=document.getElementById("ttype");
					  		  	alert(tt);
					  		  	var row=tt.insertRow();
					  		  var tname=row.insertCell();
					  		  	var tdate=row.insertCell();
					  		  	var toperation=row.insertCell();
					  		  	 tname.innerHTML=r.tname;
					  		  	 tdate.innerHTML=r.cdate;
					  		  	 toperation.innerHTML="<span style='text-decoration:none;color:blue;'>改</span>&nbsp;&nbsp;"+
					  		  			 " <span   style='text-decoration:none;color:red;'>删</span>";
					  	  }
				  }else{
					  alert("添加失败，请稍后重试！");
				  }
				 	
			 });
		 }
		 return false;
	}
	
	function delType(id,t){
			//alert(id);
				var tb=t.parentNode.parentNode.parentNode;
				 	    var tr=t.parentNode.parentNode;
				 	    var trs=tb.getElementsByTagName("tr");
				 	    if(trs.length<=0){
				 	    	alert("当前没有可以删除的列！");
				 	    	return;
				 	    }
			var  result=window.confirm("删除本条类型将会删除类型下所有店铺数据，确定删除吗？");
			if(result){
				$.get("/admin/clientcontro/store/delType.jhtml?id="+id,function(data,status){
				 	var r=data.result;
				 	if(r=="success"){
				 		tb.removeChild(tr);
				 		var tstore=document.getElementById("tstore");
				 		var ts=tstore.getElementsByTagName("tr");
				 		var tp=ts[0];
				 		for( var i=0;i<ts.length;i++){
				 			//alert("the id is="+id);
				 			  var inp=ts[i].getElementsByTagName("td")[0].getElementsByTagName("input")[0];
				 			  //alert(inp.value);
				 			  if(inp.value==id){
				 				 tp.removeChild(ts[i]);
				 			  }else{
				 				  continue;
				 			  }
				 		}
				 		alert(删除成功);
				 	}else if(r=="fail"){
				 		alert("操作失败");
				 	}
			 });
			}	 
	}
	function udpType(index){
	    var   ts=new Array();
	    <c:forEach  items="${ts}" var="it">
	    	 ts.push("${it}");
	    </c:forEach>
	    var tt=ts[index];
	    var tm=eval(tt);
}
	
	function openbrowser(){
			  var ie=navigator.appName.trim=="MicrosoftInternetExplorer"?true:false;
				  document.getElementById("file").click();
	}
	
	function addimg(input,label,type){
		     var files=input.files;
				if(files.length>=1){
					  var container=document.getElementById("imagecontainer");
					 var formdata=new FormData();
					 formdata.append("img",files[0]);
					 $.ajax({
						 url:'/admin/clientcontro/store/upload.jhtml',
						 type:'post',
						 data:formdata,
						 contentType:false,
						 processData:false,
						 success:function(args){
							 var result=new String(args.result);
							 var msg;
							 switch(result+""){
							 case "error":
								alert("上传错误");
								 break;
							 case "fail":
								alert("上传失败");
								 break;
							 default:
							   var container=document.getElementById(label);
							   var filename=result.substring(result.lastIndexOf("\\")+1);
							   if(type=="replace"){
								     container.innerHTML="<input type='button'   class='imgbtn' title='"+filename+"'  onclick=\"(delimg(this,'"+label+"','replace'))\""+
								      "  value='"+filename+"'  data-val1='"+result+"'  />";
								      img=result;
							   }else if(type=="append"){
								     container.innerHTML=container.innerHTML+" <input type='button'  onclick=\"delimg(this,'imagecontainer','append')\"  "+
								     "class='imgbtn'  title='"+filename+"'  value='"+filename+"'   data-val1='"+result+"'  />";
								     images+=","+result;
							   }
								 break;
							 }
						 }
					 });
				}
	}
	
	function delimg(btn,label,type){
		 var  val=btn.dataset.val1;
		 if(val==null){
			 alert("无法获得有效图片数据");
		 }else{
			 $.ajax({
				  url:"/admin/clientcontro/store/delImg.jhtml",
				  data:{"fn":val},
				  type:"post",
				  success:function(args){
					  var m;
					 var  result=args.result+"";
					 switch(result){
					 case "true":
						 var container=document.getElementById(label);
						 var nodes=container.children;
						 for(var i=0;i<nodes.length;i++){
							    if(nodes[i].dataset.val1==val){
							    	container.removeChild(nodes[i]);
							    }
						 }
						 switch(type){
						 case "replace":
							img= img.replace(val,"");
							 break;
						 case "append":
							 images=images.replace(","+val,"");
							 break;
						 }
						 break;
					 case "false":
						 m="操作失败";
						 break;
						 default:
							 m="操作结果获取失败";
							 break;
					 }
					 if(m!=undefined)alert(m);
				  }
			  });
		 }
	}
	//添加店铺
	  function addStore(){
		     var   name=document.getElementsByName("add_sname")[0];//*
		     var detail=document.getElementsByName("add_sdetail")[0];
		     var storeType=document.getElementsByName("add_storeType")[0];//*
		     var score=document.getElementsByName("add_score")[0];
		     var play_url=document.getElementsByName("add_playUrl")[0];
		     var area_id=document.getElementsByName("add_areaId")[0];//*
		     var detail_addr=document.getElementsByName("add_detailAddr")[0];//*
		     var tg=document.getElementsByName("add_tg")[0];
		     var owner=document.getElementsByName("add_owner")[0];//*
		     var  field=document.getElementsByName("add_field")[0];
		     var price=document.getElementsByName("add_price")[0];
		    if((name==undefined||name.value=="")||(storeType==undefined||storeType.value=="0")||(area_id==undefined||area_id=="0")||(detail_addr==undefined||detail_addr=="")||(owner==undefined||owner=="")){
		    	  alert("请检查各必输项是否填写！");
		    }else{
		    	  if(images.trim().length>0){
		    		  images=images.substring(1,images.length);
		    	  }
		    	 $.ajax({
		    			 url:"/admin/clientcontro/store/addStore.jhtml",
		    			 method:"post",
		    			 data:{"sname":name.value,"detail":detail.value,"storeType":storeType.value,"score":score.value,"play_url":play_url.value,"area_id":area_id.value,"detail_addr":detail_addr.value,"tg":tg.value,"owner":owner.value,"img":img,"images":images,"field":field.value,"price":price.value},
		    			 success:function(args){
		    				   alert(args.result);
		    			 }
		    	 });
		    }
	  }
	
	function list(currentIndex){
		var form=document.getElementById("searchForm");
		var child=form.getElementsByTagName("input");
		var param={};
				for(var i=0;i<child.length;i++){
			if(child[i].type=="text"){
				if(child[i].value!=""){
					param[child[i].name]=child[i].value;
				}
			}
		}
				var ses=form.getElementsByTagName("select");
				for(var i=0;i<ses.length;i++){
					   if(ses[i].value>0){
						  param[ses[i].name]=ses[i].value;
					   }
				}
				if(currentIndex!=undefined){
					  param["page.currentPage"]=currentIndex;
				}
		$.ajax({
			url:"store/search.jhtml",
			data:param,
			method:"post",
			success:function(args){
				var table=document.getElementById("tstore");
				var html="";
			for(var i=0;i<args.data.length;i++){
				html+="<tr>"+
   						"<td >"+args.data[i].sname+"</td>"+
   						"<td>"+args.data[i].tname+"</td>"+
   						"<td>"+args.data[i].score+"</td>"+
   						"<td>"+args.data[i].joindate+"</td>"+
   						"<td>"+args.params.status[args.data[i].status]+"</td>"+
   						"<td><span   style='text-decoration:none;color:blue;'   onclick='update("+args.data[i].id+")'>改</span>&nbsp;&nbsp;"+
   							"<span style='text-decoration:none;color:red'  click='delStore("+args.data[i].id+")'>X</span>"+
   						"</td>"+
   					"</tr>";
			}
			table.innerHTML=html;
			html="";
			var pageDiv=document.getElementById("pageDiv");
			for(var i=args.startPage;i<=args.endPage;i++){
					if(i==args.currentPage){
						html+=i+" ";
					}else{
						html+=" <a href="+args.url+"("+i+")  "+">"+i+"</a>  ";
					}
			}
			html+="----共"+args.recordCount+"条记录，共"+args.pageCount+"页";
			pageDiv.innerHTML=html;
			var params=args.params;
			
						for(var key in params){
							var member=document.getElementsByName(key)[0];
							if(member!=undefined){
								  switch(member.type){
								  case "text":
									  member.value=params[key];break;
								  case "select-one":
									  for(var i in member.children){
										      if(member.children[i].value==params[key]){
										    	     member.children[i].selected="selected";
										    	     break;
										      }
									  }
									  break;
								  }
							}
						}
			
			}
		});
	}
	
	

	var or;
	function update(obj){
		var dv=document.getElementById("updateDiv");
		if(obj!=undefined){
			$.ajax({
				url:"store/getSampleForId.jhtml",
				method:"post",
				data:{id:obj},
				success:function(args){
					for(var key in args.record){
						var temp=dv.ownerDocument.getElementsByName(key)[0];
						if(temp!=undefined){
							   if(temp.type=="text"||temp.type=="textarea"){
								   temp.value=args.record[key];
							   }
							   if(temp.type="select-one"){
								     for(var i in temp.children){
								    	  if(temp.children[i].value==args.record[key]){
								    		  temp.children[i].selected="selected";
								    	  }
								     }
							   }
						}
						   if(key=="operation_licence"){
								if(args.record[key]!=""&args.record[key]!=undefined){
									   var arr=args.record[key].split(",");
									   var html="";
											for(var  y in arr){
												if(y==""||y==undefined)continue;
												html+="  <img  class='img' src='"+arr[y]+"'  />";
											}
											document.getElementById("operation").innerHTML=html;
								}
						   }
						   if(key=="store_img"){
							     if(args.record[key]!=""&&args.record[key]!=undefined){
							    	 var array=args.record[key].split(",");
							    	   var html="";
							    	   for(var y in array){
							    		     if(y=="")continue;
							    		      html+="  <img class='img'  src='"+array[y]+"'  />";
							    	   }
							    	   document.getElementById("store").innerHTML=html;
							     }
						   }
					}
					or=args.record;
				}
			});
		}
		$("div[class=updateDiv]")[0].style.display="block";
	}
	
	var interval;
	function msgBox(msg){
		var box=document.getElementById("msgBox");
		   if(box.style.display=="none"||box.style.display==""){
			   box.style.display="block";
			   box.innerHTML=msg;
			   interval=setInterval("msgBox()",1000);
		   }else{
			   box.style.display="none";
			   clearInterval(interval);
		   }
	}
	
	function updateSubmit(){
		var div=document.getElementById("updateDiv");
		var nObj={};
		var i=0;
		div.ownerDocument.getElementsByName("tg")[0].value=or["tg"];
		if(or!=undefined){
			 for(var key in or){
				   var temp=div.ownerDocument.getElementsByName(key)[0];
				   if(temp!=undefined&&temp.value!=or[key]){
					   nObj["sesStore."+key]=temp.value;
					   i++;
				   }
			 }
			 nObj["sesStore.id"]=or["id"];
			 if(i>0){
				 $.ajax({
					 url:"store/update.jhtml",
					 data:nObj,
					 success:function(args){
						 msgBox(args==1?"修改成功":"修改失败");
					 }
				 });
			 }
		}
	}
	
	function delStore(container,id){
		if(id!=""&&id!=undefined){
			  var reg=/^\d/g;
			  if(reg.test(id)){
				 $.ajax({
					 url:"store/delStore.jhtml",
					 data:{"sesStore.id":id },
					 success:function(args){
						if(args==1){
							 var row=container.parentNode.parentNode;
							 row.parentNode.removeChild(row);
						}else{
							msgBox("删除失败");
						}
					 }
				 });
			  }else{
				  msgBox("参数格式错误，请联系开发人员！");
			  }
		}else{
			msgBox("无法删除，请联系开发人员解决！");
		}
	}
	
</script>
<title>商铺管理</title>
</head>
<body>
<span  id="msgBox"></span>
	<div  class="adddiv"   id="typediv"   style="width:50%;height:30%;margin:20% 25% 35% 25%;display:none;">
			<form  action=""     onsubmit="return sm()">
				<table  class="table"   style="width:100%;">
				<tr>
					<th  colspan="2">添加商铺类型</th>
				</tr>
				<tr>
					<td>名称：</td>
				<td>
						<input type="text"   class="inp"  name="type_name"   placeholder="请输入类型名称" />
				</td>
				</tr>
				<tr>
					<td>图标</td>
					<td>
						<input  type="file"  accept="image/*"  name="type_icon"  class="inp"/>
					</td>
				</tr>
				<tr>
					<td>描述：</td>
					<td>
							<textarea rows="5"    cols="30"    class="inp"   name="type_des"></textarea>
					</td>
				</tr>
				<tr>
					<td colspan="2"><hr/></td>
				</tr>
				<tr>
					<td><input  type="submit"  value="添加"   class="button"/></td>
					<td> <input  type="button"   value="关闭"   class="button"  onclick="divshow('typediv')"  style="background-color:red;"/></td>
				</tr>
			</table>
			</form>
	</div>
	<div  class="adddiv"  id="storediv"   style="display:none;">
	<table  style="width:100%;height:100%;text-align:center;layout:fixed;">
				<tr>
					<th  colspan="3">添加店铺</th>
				</tr>
				<tr>
					<td>店铺名称</td>
					<td><input type="text"   name="add_sname"   placeholder="商铺名称"   class="inp">*</td>
					<td rowspan="8">详述：
						<textarea  name="add_sdetail"  style="width:100%;height:80%;"></textarea>
					</td>
				</tr>
				<tr>
					<td>店铺属性</td>
					<td>
						<select  class="inp" name="add_field">
							<c:forEach  items="${tag }"   var="t">
								  <option  value="${t.id }">${t.name }</option>
							</c:forEach>
						</select>
					</td>
				</tr>
				<tr>
				<td>最低消费</td>
				<td><input  type="text"  class="inp"  name="add_price"  value="0"/></td>
				</tr>
				<Tr>
					<td >店铺图片</td>
					<td><input  type="file"   name="add_singleimg"   onchange="addimg(this,'singleContainer','replace')"  accept="image/*"   class="inp"  />
						<label  id="singleContainer"></label>
					</td>
				<tr>
				<tr>
					<td>店铺类型</td>
					<td>
						<select  name="add_storeType"   class="inp">
							<option  value="0">请选择</option>
							<c:forEach  items="${ts }"   var="st">
								 <option  value="${st.id }">${st.tname }</option>
							</c:forEach>
						</select>*
					</td>
				</tr>
				<tr>
					<td>评分</td>
					<Td><input type="text"   name="add_score"   class="inp"/></Td>
				</tr>
				<tr>
					<td>直播地址</td>
					<td><input type="text" name="add_playUrl" class="inp"   placeholder="直播地址"/></td>
				</tr>
				<tr>
					<td>地址</td>
					<td>
						<select  name="add_areaId"    class="inp">
						  <option  value="0">请选择</option>
						  <c:forEach  items="${area }"  var="a"   >
						  	<option value="${a.id }">${a.name }</option>
						  </c:forEach>
						</select>*<br/>
						<input type="text"  class="inp"  name="add_detailAddr"   placeholder="请输入详细地址"  />*
					</td>
				</tr>
				<tr>
					<Td>简述</Td>
					<Td><input type="Text"    name="add_tg"   class="inp"  placeholder="请用10字以内的描述"/></Td>
				</tr>
				<tr>
					<Td>法人代表</Td>
					<td><input type="text"  name="add_owner"    class="inp"  placeholder="法人代表"/>*</td>
				</tr>
				<tr>
					<Td>详情图</Td>
					<Td><input type="button"   value="+"  style="font-weight:bold;" onclick="openbrowser()"/>
					<input  type="file"  id="file"   onchange="addimg(this,'imagecontainer','append')"  style="visibility:hidden;" accept="image/*"/></Td>
					<Td  id="imagecontainer"></Td>
				</tr>
				<tr>
					<Td   colspan="3"   style="text-align:CENTER;">
						<input type="button"  value="添加"  class="btn"    onclick="addStore()" style="border:1px solid cyan;"/>
						<input type="button"    value="关闭"   style="border:1px solid red;"/>
					</Td>
				</tr>
	</table>
	</div>
	
	<div   class="updateDiv"   id="updateDiv">
					<h4  style="display:inline-block;">店铺修改与审核</h4><button  style="float:right;border:none;"  onclick="divshow('updateDiv')">X</button><hr/>
					<table  style="width:100%;">
						  <tr>
						  	<td>店铺名称：</td>
						  	<td><input  type="text"  name="sname"   /><input type="text"  name="id"   readonly="readonly"  disabled="disabled"/></td>
						  	<th  colspan="5"  style="text-align:left;">认证图片</th>
						  </tr>
							<tr>
								<td>状态</td>
						  	<td><select  name="status">
						  		<c:forEach  items="${page.params.get('status')  }"   var="ss"  varStatus="vs">
						  		  <option  value="${vs.index }">${ss.name }</option>
						  		</c:forEach>
						  	</select></td>
						  	<th colspan="5"  rowspan="7"  style="text-align:left;"  id="operation"></th>
							</tr>
							<tr>
								<td>店铺类型</td>
								<td>
									<select  name="typeId">
											<c:forEach items="${ts }"  var="type">
												 <option  value="${type.id }">${type.tname }</option>
											</c:forEach>
									</select>
								</td>
							</tr>
							<tr><td>
								评分
							</td>
								<td>
									<input  type="text"   name="score"/>
								</td>
							</tr>
							<tr>
								<td>最低消费金额</td>
								<td><input type="text"   name="price"/></td>
							</tr>
							<tr>
								<td>加入日期</td>
								<td><input type="text"  readonly="readonly"  name="joindate"/></td>
							</tr>
							<tr>
								<td>直播地址：</td>
								<td><input type="text"   name="play_url"/></td>
							</tr>
							<tr>
								<td>详细地址：</td>
								<td><input type="text"  name="addr" /></td>
							</tr>
							<tr>
								<td>简介：</td>
								<td><input type="text"  name="tg"/></td>
								<th  colspan="5" style="text-align:left;">商品图片</th>
							</tr>
							<tr>
								<td>所有人：</td>
								<td><input type="text"  name="owner" /></td>
								<th colspan="5"  rowspan="6"  style="text-align:left;"  id="store"></th>
							</tr>
							<tr>
								<td>主营产品：</td>
								<td><input type="text"   name="main_operation"/></td>
							</tr>
							<tr>
								<td>联系电话：</td>
							<td><input type="text"   name="contact_mobile"/></td>
							</tr>
							<tr>
								<td>留言：</td>
								<td><textarea  cols="20"  rows="5"  name="markMsg"></textarea></td>
							</tr>
							<tr>
								<td>介绍：</td>
								<td><textarea rows="5" cols="20"  name="detail"></textarea></td>
							</tr>
							<tr>
								<td>平台相关信息：</td>
								<td>
									<textarea rows="5" cols="20"  name="environmentMsg"></textarea>
								</td>
							</tr>
							<tr>
								<Td colspan="7"  style="text-align:center;">
								<input type="button"   onclick="updateSubmit()"  value="确定" />
								</Td>
							</tr>
					</table>
	</div>
   <div style="width:100%;height:100%;background-color:#ffc;">
   	<span  class="span">商铺种类与商铺管理</span>
   	<div style="width:100%;height:11%;background-color:#ccf;">
   		<form action="${page.url }()"   id="searchForm" >
   <table class="table"    style="width:100%;">
   	  <tr>
   	  	<th colspan="5">查询操作</th>
   	  	<th>添加操作</th>
   	  </tr>
   	  <tr>
   	  	<td>查询种类：</td>
   	  	<td ><select name="searchType"  class="inp">
   	  		<option value="0">请选择</option>
   	  		<option value="1">店铺类别</option>
   	  		<option value="2">店铺</option>
   	  	</select></td>
   	  	<td>名称：</td>
   	  	<td><input type="text"   placeholder="支持模糊查询"   class="inp"  name="searchName" /></td>
   	  	<td rowspan="2"><input type="submit"  class="button"  value="提交" /></td>
   	  	<td rowspan="2"   style="border-left:solid 1px #ffc;">
   	  		<input  type="button"  onclick="divshow('storediv')" class="button"  value="添加商铺" />
   	  		<input  type="button"  onclick="divshow('typediv')"  class="button"   value="添加商铺类型"  />
   	  	</td>
   	  </tr>
   	  <tr>
   	  	<td>创建日期：</td>
   	  	<td>
   	  		<input type="text"   class="inp" name="searchStartDate"   placeholder="格式：****年**月**日"/>
   	  	</td>
   	  	<td>结束日期：</td>
   	  	<td><input type="text"  placeholder="格式：****年**月**日"   class="inp"  name="searchEndDate"/></td>
   	  </tr>
   </table>
   </form>
   	</div>
   	<ol  style="list-style:none;width:100%;height:5px;font-weight:bold;text-align:center;">
   		 <li style="float:left;width:10.5%;">名称</li>
   		 <li style="float:left;width:12.5%;">创建日期</li>
   		 <li style="float:left;width:10.8%;">操作</li>
   		 <li style="float:left;width:12%;">名称</li>
   		 <li style="float:left;width:12.5%;">商铺类型</li>
   		 <li style="float:left;width:10%;">商铺评分</li>
   		 <li style="float:left;width:10%;">加入日期</li>
   		 <li style="float:left;width:10%;">状态</li>
   		 <li  style="float:left;width:10%;">操作</li>
   	</ol>
   <div  style="width:100%;height:90%;background:#ccc;">
   	<div style="height:100%;width:37.5%;float:left;border-right:thick solid  #ffccfc;overflow:auto;">
   		<table   id="ttype"  style="width:100%;text-align:center;border-collapse:collapse;" class=" table  listtable">
   			<c:if test="${ts==null }">
   				 <tr>
   				 	<td >抱歉，当前暂无更多数据，请点击右上角添加！</td>
   				 </tr>
   			</c:if>
   			<c:if test="${ts!=null }">
   				<c:forEach items="${ts }"   var="t"    varStatus="st">
   					<tr>
   					<td>${t.tname }</td>
   					<td>${t.cdate }</td>
   					<td><span  style="color:blue;"    onclick="udpType(${st.index})">改</span>&nbsp;&nbsp;
   					<span  style="color:red;"   onclick="delType(${t.id},this)">X</span>
   					</td>
   				</tr>
   				</c:forEach>
   			</c:if>
   		</table>
   	</div>
   	<div  id="storeDiv"  style="height:100%;width:62%;float:left;overflow:auto;">
   		<table  id="tstore"  class="table listtable"  style="width:100%">
   				<c:if test="${page.data==null }">
   					 <tr>
   					 	<td>暂无更多数据，请点击右上角添加商家</td>
   					 </tr>
   				</c:if>
   				<c:if test="${page.data!=null }">
   				<c:forEach  items="${page.data }"   var="s">
   						<tr >
   						<td >${s.sname }<input  type="hidden"   value="${s.type_id }"/></td>
   						<td>${s.tname }</td>
   						<td>${s.score }</td>
   						<td>${s.joindate }</td>
   						<td>${page.params.get("status")[s.status].name }</td>
   						<td><span   style="text-decoration:none;color:blue;"  onclick="update(${s.id})">改</span>&nbsp;&nbsp;
   							<span style="text-decoration:none;color:red"    onclick="delStore(this,${s.id})">X</span>
   						</td>
   					</tr>
   				</c:forEach>
   				</c:if>
   		</table>
   	</div>
   </div>	
     <div id="pageDiv"  style="width:100%;text-align:center;padding:10px 0 10px 0;box-shadow:0 0 10px 1px #ccf inset;border-radius:5px;border-top-left-radius:0;border-top-right-radius:0;text-shadow:0px 0px 11px  0.1px blue ;">
  	 	<c:forEach  begin="${page.startPage }"   end="${page.endPage }"   var="i">
   		  <c:if test="${i==page.currentPage }">${i }</c:if>
   		<c:if test="${i!=page.currentPage }"><a href="${page.url }(${i})">${i}</a></c:if>
   	</c:forEach>----  共${page.recordCount }条记录，共${page.pageCount }页
  </div>
   </div>
</body>
</html>