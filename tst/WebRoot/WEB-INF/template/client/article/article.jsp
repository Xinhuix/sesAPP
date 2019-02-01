<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <%@taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html >
<html  style="height:100%;width:100%;overflow:hidden;">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0"/>
<title>${article.title }</title>
<script type="text/javascript"  src="/js/jquery/jquery-3.3.1.min.js"></script>
<script type="text/javascript">
   
   var scrheight,scrwidth,titleDiv,comDiv,trcDiv,conDiv,titleheight,trcheight,conheight;
   var type=1;
   var mbox,mInterval,mid,sid,prefix,appenderId;
   
   
		   window.onload=function(){
		       scrheight=document.body.clientHeight;
		       scrwidth=document.body.clientWidth;
		       titleDiv=document.getElementById("titlediv");
		       trcDiv=document.getElementById("contentdiv");
		       comDiv=document.getElementById("commentdiv");
		       trcDiv=document.getElementById("truncatediv");
		       titleheight=scrheight/100*15;
		       trcheight=scrheight/10;
		       conheight=scrheight-titleheight-trcheight;
		       mid="<%=session.getAttribute("mid") %>";
		}
		function scroll(obj){
			 var scrolltop=parseInt(obj.scrollTop);
			 scrolltop=scrolltop>60?60:scrolltop;
			 obj.style.scrolltop=scrolltop;
			 var height=60-scrolltop;
			 var opacity=(60-scrolltop)/60;
			 var truncatediv=document.getElementById("truncatediv");
			 truncatediv.style.height=height+"px";
			 truncatediv.style.opacity=opacity;
		/*  var  titlediv=document.getElementById("titlediv");
			var  th=document.body.clientheight;
			 titlediv.style.height=th+"px"; */
		}
 
	     
	     function cmt(aid){
	    	  var str=document.getElementById("comment_inp").value;
	    	  if(str==""){
	    		  msg("评论为空");
	    		  return;
	    	  }
	    	  if(type==2){
	    		  str=str.replace(prefix,"");
	    		  if(str==""){
	    			  msg("评论为空");
	    			  return;
	    		  }
	    	  }
	    	 
	    	   if(mid=="undefined"||mid=="null"){
	    		   msg("请登陆之后再评论！");
    	    }else{
    	    	  $.ajax({
   	    		   url:"comment.jhtml",
   	    		   method:"post",
   	    		   data:{"mid":mid,"articleId":aid,"type":type,"commentStr":str,"appenderId":mid,"commentId":sid},
   	    		   success:function(args){
   	    			  if(args.status=="1")msg("评论成功！");
   	    			  else msg("评论失败！");
   	    		   }
   	    	   });
   	    	   document.getElementById("comment_inp").value="";
   	    	   type=1;
    	    }
	     }
	     
	     function praise(id,appenderId,aid){
			if(mid==undefined||mid=="undefined"||mid=="null"){
				msg("登陆之后才能点赞哦");
				return;
			}
	    	 if(id==""||id==undefined){
	    		 msg("点赞失败");
	    	 }else{
	    		 type=3;
	    		 $.ajax({
	    			 url:"comment.jhtml",
	    			 method:"post",
	    			 data:{"type":type,"commentId":id,"mid":mid,"appenderId":appenderId,"articleId":aid},
	    			 success:function(args){
	    				   if(args.status=="2"){
	    					   msg("点赞失败，请稍后重试");
	    				   }else if(args.status=="1"){
	    					   msg("点赞成功");
	    				   }else{
	    					   msg("无法重复点赞哦");
	    				   }
	    			 }
	    		 });
	    	 }
	     }
	    
	     function appender(md,sd,nickname){
	    	   type=2;
	    	   appenderId=md;
	    	   sid=sd;
	    	   prefix="回复 '"+nickname+"':";
	    	   document.getElementById("comment_inp").value=prefix;
	     }
	     
	     function inp_monitor(){
	    	if(type==2){
	    		 var vl=document.getElementById("comment_inp").value;
	    		 if(vl.indexOf(prefix)==-1){
	    			 type=1;
	    			 document.getElementById("comment_inp").value="";
	    		 }
	    	}
	     }
	     function msg(msg){
	    	   if(mbox==undefined){
	    		   mbox=document.getElementById("msgbox");
	    	   }
	    	   if(mbox.style.display=="block"){
	    		      mbox.style.display="none";
	    		      clearInterval(mInterval);
	    	   }else{
	    		   mbox.style.display="block";
	    		   mbox.innerHTML=msg;
	    		   mInterval=setInterval("msg('')",1000);
	    	   }
	     }
</script>
<style type="text/css">
	 .msgBox{
	 	 z-index:9;
	 	 position:absolute;
	 	 border:solid thin red;
	 	 width:70%;
	 	 margin-left:13%;
	 	 margin-top:45%;
	 	 padding:15px;
	 	 font-family:sans-serif;
	 	 font-size:14px;
	 	 font-color:#fff;
	 	 display:none;
	 	 background-color:red;
	 }
</style>
</head>
<body  style="height:100%;width:100%;margin:0;padding:0;overflow:hidden;text-align:center;">
<div  id="msgbox" class="msgBox">
    <span></span>
</div>
		<div  style="width:100%;height:50px;text-align:center;"  id="titlediv">
			<!--  <label style="color:green;font-size:20px;font-weight:bold;float:left;line-height:200%;margin:0 0 0 20px;"  onclick="javascript:window.history.back();">  &lt;  </label>-->
			<h3  style="height:100%;margin:0;line-height:200%;">${article.title }</h3><br/>
			<img src=""  />
		</div>
		<div  style="margin:0;padding:0;background-color:#f5ecce;height:20px;overflow:hidden;"    id="truncatediv">
				<label  style="float:left;margin:5px;font-size:11px;">作者：${article.author }</label>
			<label  style="float:right;margin:5px;margin-left:0px;font-size:9px;">日期：${article.createDate}</label>
			<label  style="float:left;margin:5px;font-size:11px;">点击量：${article.hits }</label>                                  
			</div>
			<hr  style="padding:0;margin:0;"/>	
		<div  style="width:100%;height:75%;margin:0;padding:0;overflow-y:auto;"   onscroll="scroll(this)"  id="contentdiv">
			<!-- <h2 style="text-align:center;margin:0;ling-height:200%;">${article.title }</h2>-->
			${article.content }
			<div  style="width:100%;overflow-x:auto;border-bottom:solid thin red;background-color:#FAEBD7;text-align:left;">
				<div style="width:100%;height:40px;background-color:#fff;margin-bottom:0;"><img src="/upload/hotcomment.jpg"   style="height:30px;width:30px;float:left;"/>
				<span  style="float:left;font-weight:bold;font-family:sans-serif;margin-top:5px;background-color:#fff;">热门评论</span><br/>
				</div>
			<hr  style="margin:0 0  20px 0;font-family:sans-serif;font-size:14px;"/>
				   <c:forEach  items="${comment }"  var="c">
				   		<div style="width:100%;margin:5px 0 5px 0;padding:10px 0 10px 0;background-color:#fff;">
				   			 <div  style="width:100%;"  onclick="appender(${c.id},${c.sid },'${c.nickname}')">
				   			 	 <img src="/upload/111.jpg"  style="border-radius:15px;float:left;margin-left:20px;width:30px;height:30px;">
				   			 	<span  style="font-weight:normal;font-family:sans-serif;font-size:14px;">${c.nickname }</span>
				   			 </div>
				   			   <div  style="margin:10px 10px 10px 50px;font-family:sans-serif;font-size:14px;">&nbsp;&nbsp;&nbsp;&nbsp;${c.comment }</div>
									 		<table  style="width:70%;margin-left:25%;font-size:12px;">
				   			   				  <c:forEach  items="${cmt.getSubComment(c.id,c.sid) }"  var="sub">
				   			   				  <tr>
				   			   				  	<td> <img  alt=""  src="/${sub.portrait }"   style="border-radius:15px;width:25px;height:25px;margin-bottom:-10px;"/>&nbsp;${sub.nickname }:<span  style="line-height:200%;">${sub.comment }</span></td>
				   			   				  	<td></td>
				   			   				  </tr>
				   			   				  </c:forEach> </table>
				   			   <div  style="width:100%;"><label  style="margin-left:30px;opacity:0.5;font-size:12px;">${c.cdate }</label>
								   			   <div  style="float:right;">
								   			   <img   src="/upload/praise1@2x.png"  style="width:15px;height:15px;"  onclick="praise(${c.sid},${c.id },${article.id })" />
								   			   <label  style="margin-right:15px;opacity:0.5;font-size:12px;">${c.praise }</label>
								   			   </div>
				   			   </div>
				   		</div>
				   </c:forEach>
			</div>
		</div>
		<div  style="position:fixed;bottom:0px;height:10%;width:100%;background-color:#5ecce;"  id="commentdiv"><hr  style="padding:0;margin:0;"/>
		<input  type="text"   id="comment_inp"    onkeyup="inp_monitor()"   style="height:100%;width:70%;border:none;background-color:#f5ecce;float:left;"  placeholder="评价"/>
		<button style="border:0;float:left;height:103%;width:30%;margin:0;"  onclick="cmt(${article.id})">发送</button>
		</div>
</body>
</html>