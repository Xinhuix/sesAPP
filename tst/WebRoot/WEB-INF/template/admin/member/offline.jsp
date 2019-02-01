<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link   type="text/css" rel="stylesheet"  href="/css/common.css">
<script type="text/javascript"   src="/js/jquery/jquery-3.3.1.min.js" ></script>
<script  type="text/javascript">
	  var preCard;
	  var preM;
	  var sc;//被选中会员卡类型
	  var sm;//被选中月份
	  var cards=new Array();
	  var months=new Array();
	  window.onload=function(){
		    <c:forEach  items="${cards}"  var="c">
		    var c={
		    	id:"${c.id}",
		    	name:"${c.name}",
		    	islimit:"${c.islimit}",
		    	limitnum:"${c.limitnum}"
		    };
		    cards.push(c);
		    </c:forEach>
	  <c:forEach  items="${months}"  var="m">
	  	  var m={
	  		id:"${m.id}",
	  		number:"${m.number}",
	  		card_id:"${m.card_id}"
	  	  };
	  	  months.push(m);
	  </c:forEach>
	  }
	  
	  function  cardclick(c,index){
		     c.style.backgroundColor="cyan";
			  sc=cards[index];
			  $("td[name=td_ms_container]")[0].innerHTML="";
	    	  for(var i=0;i<months.length;i++){
	    		    if(months[i].card_id==sc.id){
	    		    	  $("td[name=td_ms_container]")[0].innerHTML+="<label class='mlabel'  onclick='monthclick(this,"+i+")'>"+months[i].number+"个月</label>";;
	    		    }
	    	  }
		     if(preCard){
		    	  preCard.style.backgroundColor="#bac";
		     }
		     preCard=c;
	  }
	  
	 function monthclick(m,index){
		    m.style.backgroundColor="cyan";
		    if(preM&&preM!=m){
		    	   preM.style.backgroundColor="#dbb";
		    }
		    preM=m;
		    sm=months[index];
	 }
	 
	 function  scalar(operation){
		   if(!sc){
			   alert("请先选择会员卡类型");
			   return;
		   }
		   var sum=parseInt($("label[name=sum]")[0].innerHTML);
		   sum+=operation=='add'?1:-1;
		   if(sum<1){
			   sum=1;
			   alert("当前已是最低数值");
		   }
		 if(sc.islimit==1){
			 if(sum>sc.limitnum){
				 alert("此卡为限购卡，无法超出最大限购数量");
				 sum=sc.limitnum;
			 }
		 }
		   $("label[name=sum]")[0].innerHTML=sum;
	 }
	 
	 function  subm (){
		 if($("input[name=uname]").val()==""){
			 alert("请输入用户名");
			 $("input[name]")[0].focus();
			 return;
		 }
		 if($("input[name=date]").val()==""){
			 alert("请输入购买日期");
			 $("input[name=date]")[0].focus();
			 return;
		 }
		   if(!sc){
			   alert("请选择卡类型");
			   return;
		   }
		   if(!sm){
			   alert("请选择卡时长套餐");
			   return;
		   }
		   if($("label[name=sum]").val<1){
			   alert("请选择正确数量");
			   return;
		   }
		   $.post("/admin/member/offLineAdd.jhtml",{uname:$("input[name=uname]").val(),
			   date:$("input[name=date]").val(),
			   cardid:sc.id,
			   monthid:sm.id,
			   mnum:sm.number,
			   csum:$("label[name=sum]")[0].innerHTML
		   },function(result,status){
			   if(status=="success"){
				   var r=result.result;
				   if(r=="添加成功"){
					   var r=confirm("添加成功，是否重置？");
					   if(r)reset();
				   }else{
					   alert(result.result);
				   }
				  
			   }else{
				   alert("通讯失败，地址无法访问，请联系技术人员");
			   }
		   });
	 }
	 
	 function reset(){
		   $("input[name=uname]")[0].value="";
		   $("input[name=date]")[0].value="";
		   $("td[name=td_ms_container]")[0].innerHTML="";
		   //var  cms=tm.childNodes;
		  // alert(cms.length);
		  // for(var i=0;i<cms.length;i++){
			  // tm.removeChild(cms[i]);
		  // }
		   $("label[name=sum]")[0].value=1;
		   if(preCard){
			   preCard.style.backgroundColor="bac";
			   sc=null;
			   sm=null;
		   }
	 }
</script>
<style type="text/css">
	  .label{
	  	  padding-left:10px;
	  	  padding-right:10px;
	  	  border:thick  solid #bab;
	  	  background-color:#bac;
	  	  margin-left:10px;
	  }
	  .label:hover{
	  	background-color:#fff;
	  	border-color:#ccc;
	  }
	  
	  .mlabel{
	  	padding-left:10px;
	  	padding-right:10px;
	  	border:thick solid #bad;
	  	background-color:#dbb;
	  	margin-left:10px;
	  }
</style>
<title>线下用户办理--鱼乐</title>
</head>
<body style="overflow:hide;">
		<span  class="span">线下用户办理--会员卡</span>
			<div  style="width:100%;height:100%;background-color:#ccc;">
			<form  action=""   method="post"  style="">
				   	<table  class="table"   style="width:70%;margin-left:15%;line-height:200%;">
						   <tr  style="line-height:300%;">
						   	<th colspan="3"  style="line-height:200%:">线下用户新增会员卡</th>
						   </tr>
						   <tr>
						   		<td  style="text-align:right;">用户名：</td>
						   		<td  colspan="2"  style="text-align:left;"><input type="text"   name="uname"  class="inp"   placeholder="请输入用户名"/></td>
						   </tr>
						   <tr>
						   		<td  style="text-align:right;">
						   			办理日期：
						   		</td>
						   		<td  colspan="2"  style="text-align:left;">
						   			 <input  type="text"   class="inp"   name="date"  placeholder="例子：20180101"/>
						   		</td>
						   </tr>
						   <tr>
						   		<td  style="text-align:right;">
						   			会员卡类型：
						   		</td>
						   		<td colspan="2"  style="text-align:left;">
						   			<c:forEach  items="${cards }"  var="c"   varStatus="vs">
						   				 <label  class="label"   onclick="cardclick(this,${vs.index})">${c.name }</label>
						   			</c:forEach>
						   		</td>
						   </tr>
						   <tr>
						   	<td  style="text-align:right;">请选择时间组合：</td>
						   	<td colspan="2"   name="td_ms_container" style="text-align:left;">
						       
						   	</td>
						   </tr>
						   <tr>
						   	<td  style="text-align:right;">请选择数量：</td>
						   	<td  style="text-align:left;"  colspan="2">
						   		<label   onclick="scalar('add')">+</label><label  name="sum"  style="border:solid  thin black;margin-left:10px;margin-right:10px;">1</label><label  onclick="scalar('subtract')">-</label>
						   	</td>
						   </tr>
						   <tr>
						   	<td colspan="3">
						   		   <hr/>
						   	</td>
						   </tr>
						   <tr style="line-height:500%;">
						   		<td  colspan="3"><input  type="button"   onclick="subm()"  value="添加"   onclick="add"  class="button"/>
						   			<input type="button"  value="重置"    onclick="reset()"  class="button" />
						   		</td>
						   </tr>
				  </table>
			</form>
			</div>
</body>
</html>