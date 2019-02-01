<!DOCTYPE html>
<html>
<head>
<script type="text/javascript" src="jquery.js"></script>
<meta http-equiv="Content-Type" content="text/html; UTF-8">
<title>Insert title here</title>
</head>
<style type="text/css">
	.div{
		width:80%;
		height:100%;
		text-align:center;
		border:2px dotted green;
		border-radius:10px;
		margin-left:50px;
		padding:20px 50px 20px 50px;
		background-color:#ccc;
	}
	strong{
		margin-left:50px;
	}
	body{
		width:98%;
		margin-left:1em;
		
	}
	table#t1{
	margin-left:8%;
	margin-right:8%;
	background-color:#fff;
	width:90%;
	border-radius:5px;
	}
	h5{
		 width:100%;
		 border-bottom:solid thin white;
		 text-align:left;
	}
	input {
		 border-radius:3px;
		 padding:5px;
		 border:solid thin #C0C0C0;
		 width:200px;
	}
	 .btn{
		  	  border-bottom-left-radius:10px;
		  	  border-top-right-radius:10px;
		  	  border:solid thin red;
		  	  width:50%;
		  	  height:100%;
		  }
		  .btn:hover{
		   border-bottom-right-radius:10px;
		   border-top-left-radius:10px;
		   border-bottom-left-radius:0;
		   border-top-right-radius:0;
		   box-shadow:0 0 5px 5px #ccc inset;
		  }
</style>
<script type="text/javascript">
var reg=/([1-9]$)|0*/;
var l1,l2,l3,l4,l5,abc,tips1,ls;
var spas=new Array(),sums;

window.onload=function(){
	  l1=document.getElementById("lsca1");
	 l2=document.getElementById("lsca2");
	 l3=document.getElementById("lsca3");
	 l4=document.getElementById("lsca4");
	 l5=document.getElementById("lsca5");
	 ls=[l1,l2,l3,l4,l5];
	 sums=["${l1}","${l2}","${l3}","${l4}","${l5}"];
	 var tips2=document.getElementsByName("tips2");
	 abc=document.getElementById("abc");
	 tips1=document.getElementsByName("tips1")[0];
	for(var i=0;i<=4;i++){
		var nm="tips"+(i+2);
		spas[i]=document.getElementsByName(nm)[0];
	}
	recInit();
}

function recInit(){
	var xhr;
	if(XMLHttpRequest){
		xhr=new XMLHttpRequest();
	}else xhr=new ActiveXObject("microsoft.XMLHttpRequest");
	xhr.open("get","introduceInit.jhtml",true);
	xhr.setRequestHeader("Content-type","application/x-www-form-urlencoded");
	xhr.send();
	xhr.onreadystatechange=function(){
		 if(xhr.readyState==4&&xhr.status==200){
		 alert(xhr.responseText);
		 }
	}
	
}
	
	  function sub(){
		   var t2=/([1-9][\.]*[0-9])|0/;
		   if(!(t2.test(spas[0].value))||spas[0].value==""){
		     alert("请输入各项参数！");
		   		return false;
		   }
	  }
	  
	  function input(){
		  var j=0;
		  var cnt=0;
		if(reg.test(abc.value)){
			tips1.innerHTML="正确";
		 	for(var i=0;i<ls.length;i++){
		 		if(reg.test(ls[i].value)){
		 			spas[i].value="";
		 			cnt=cnt+(new Number(ls[i].value)*parseInt(sums[i]));
		 		}else{
		 			j+=1;
		 			spas[i].value="输入错误";	
		 		}
		 	}
		 	if(j+1<=1){
		 		var divide=abc.value/cnt;
		 		for(var i=0;i<ls.length;i++){
		 			spas[i].value=Math.floor((divide*ls[i].value)*100)/100;
		 		}
		 	}
		}else{
			tips1.innerHTML="输入错误，已去除！";
			var r=/\D/g;
			var s=abc.value.replace(r,"");
			abc.value=s;
		}
	  }
</script>
<body style="font-family:sans-serif">
<p   style="width:100%;padding:20px;position:absolute;z-index:10;">fdsafds</p>
<strong>客户相关设置设置${base }</strong>
<div  style="height:30px;padding:0 125px 0 50px;">
	<button  class="btn">分红设置</button><button  class="btn"  onclick="javascript:document.getElementById('introduce').scrollIntoView(true);return false;">推广设置</button>
</div>
		<div  class="div">
		<h5>每日分红设置</h5>
			<form action="/admin/member/allocateSetting.jhtml" method="post" id="f1" onsubmit="return sub()">
				<table id="t1" style="text-align:CENTER;">
					<thead>
						<tr>
							<th>操作项</th>
							<th>操作值</th>
							<th>预览值</th>
						</tr>
					</thead>
					<tr><td colspan="3"><hr/></td></tr>
					<tr>
						<td>是否分红：</td>
						<td><select name="isenabled">
							<option value="0">启用分红</option>
							<option value="1">停止分红</option>
						</select></td>
					</tr>
					<tr>
						<td>分红类型：</td>
						<td><select name="type">
							<option value="1">积分分红</option>
						</select></td>
					</tr>
					<tr>
						<td>小地主：</td>
						<td><input type="number" id="lsca1" value="${l1scale }" name="l1" placeholder="请输入比例"></td>
						<Td>
							<input type="text" disabled="disabled"   name="tips2" value="${l1count}"/>
						</Td>
					</tr>
					<tr>
						<td>大地主：</td>
						<td><input type="number" id="lsca2" name="l2" value="${l2scale}" placeholder="请输入比例"/></td>
						<td><input type="text"  readonly="readonly" name="tips3" value="${l2count}"  disabled="disabled" /></td>
					</tr>
					<tr>
						<Td>山寨主：</Td>                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         
						<td><input type="number" id="lsca3" name="l3" value="${l3scale}" placeholder="请输入比例"/></td>
						<Td><input type="text" readonly="readonly"  name="tips4" value="${l3count}" disabled="disabled" /></Td>
					</tr>
					<tr>
						<td>农场主：</td>
						<td><input type="number" id="lsca4" name="l4" placeholder="请输入比例" value="${l4scale}"/></td>
						<td><input type="text"  readonly="readonly" name="tips5" value="${l4count}" disabled="disabled" /></td>
					</tr>
					<tr><td>庄园主：</td>
						<td><input type="number" id="lsca5" name="l5" placeholder="请输入比例" value="${l5scale}"/></td>
						<td><input type="text"  readonly="readonly" name="tips6" value="${l5count}"  disabled="disabled" /></td>
					</tr>
					<tr>
						<td>分红总额：</td>
						<td>
							<input type="number" id="abc" name="amount" value="${amt}" placeholder="请输入分红总额" oninput="input()"/>
						</td>
						<Td>
							<span name="tips1"></span>
						</Td>
					</tr>
					<tr>
						<td colspan="1">
							<input type="submit" style="width:50%;margin:5em;border-radius:10px;border:2px solid #cff;"  class="btn" value="提交"/>
						</td>
						<Td></td>
						<Td><input type="button"  class="btn" style="width:50%;margin:5em;border-radius:10px;border:2px solid red;" value="预览" onclick="input()"/></td>
					</tr>
				</table>
			</form>
		</div>
		<div  class="div" id="rec">
		     <h5  id="introduce">推广参数设定</h5><br/>
		     <input type="text"    name="title"  placeholder="请输入标题"  title="标题"/><br/>
		     <input type="file"  accept="image/*"   name="icon"  title="图标"  /><br/>
		     <textarea  cols="23"  rows="10" placeholder="请输入介绍"  name="introduce" ></textarea><br/>
		     <input type="text" name="url"   value=""  disabled="disabled"/><br/>
		     <button  class="btn"  style="width:50px;padding:5px 10px 5px 10px;"> 确定 </button>
		</div>
		<div  class="div">
			<h5>客户端首页参数设置</h5><br/>
			<input type="text"  placeholder="请输入商品名称"   onchange="match()"/><br/>
			 <input type="file" name="bottomAd" /><br/>
			 <button  class="btn"   style="width:50px;padding:5px 10px 5px 10px;">确定</button>
		</div>
</body>
</html>