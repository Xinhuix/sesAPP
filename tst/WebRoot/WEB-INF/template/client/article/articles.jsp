<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib  prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<html    style="padding:0;margin:0;overflow:hidden;overflow:hidden;">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0"/>
<title>资讯中心</title>
<script type="text/javascript"    src="/js/jquery/jquery-3.3.1.min.js" ></script>
<script type="text/javascript">
						var  imgs=new Array();
						var  interval;
						var index=0;
						var banner;
						var banner_title;
						var cMenu;
						<c:forEach items="${ad}"  var="simple">
						    var simple={
						    		id:"${simple.id}",
						    		ad_img:"${simple.ad_img}",
						    		title:"${simpple.title}"
						    };
							imgs.push(simple);
						</c:forEach>
						window.onload=function(){
							banner=document.getElementById("div_banner");
							banner_title=document.getElementById("banner_title");
							  if(imgs.length<=0){
								   banner.style.display="none";
							  }else{
								  interval=setInterval("rollBanner()",3000);
							  }
							  //clearInterval(interval);
						}
						function rollBanner(){
							if(index>=imgs.length)index=0;
							   banner.style.background="url(/"+imgs[index].ad_img+")";
							   banner.style.backgroundSize="100% 100%";
							   banner_title.innerHTML=imgs[index].title;
								index++;
						}
						
						function menuTouch(menu,id){
						  var formData=new FormData();
						  formData.append("id",id);
						  $.ajax({
							  url:'search.jhtml',
								 data:{"id":id},
								 success:function(args){
								    var ary=args.result;
								    if(ary.length>0){
								    	var container=document.getElementById("article_container");
								    	var ih="";
								    	container.innerHTML="";
								    	for(var i=0;i<ary.length;i++){
								    		 ih+="<div  onclick='redirect("+ary[i].id+")'  style='height:100px;border-bottom:solid thin #e0e0e0;padding:10px 5px 10px 5px;'>"+
								    		 	"<div   style='width:70%;height:100%;float:left;'>"+
								    		 	" <span class='article_title'>"+ary[i].title+
						       					"</span> "+
						       					"<span  class='article_description'>"+(ary[i].seo_description!=null?ary[i].seo_description:"")+
						       					"</span>"+
						       					"<div  style='width:100%;float:left;font-size:12px;'>评论总数："+ary[i].count+"  分享总数："+ary[i].goodcount+"</div>"+
								    		 	"</div>"+
								    		 	"<img src='/upload/031.jpg' style='max-width:30%;max-height:100%;float:left;margin:0 auto;' />"+
								    		 "</div>";
								    	}
								    	container.innerHTML=ih;
								    	 if(cMenu!=undefined){
										    	cMenu.setAttribute("class","");
										    }
								    	cMenu=menu;
									    cMenu.setAttribute("class","li_select");
								    }
							  }
						  });
						}
						
						function search(){
							  var clause=document.getElementById("clause");
							  if(clause.value=="")return;
							  else{
								$.ajax({
									url:"search.jhtml",
									method:"post",
									data:{"clause":clause.value},
									success:function(args){
										var result=args.result;
										var container=document.getElementById("article_container");
										var ihtml="";
										if(result.length<=0){
											 ihtml="<label  style='width:100%;align:center;margin:100px 80px 0 80px;;'>暂无符合条件的资讯</label>";
										}else{
											for(var i=0;i<result.length;i++){
												ihtml+="<div onclick='redirect("+result[i].id+")'  style='height:100px;border-bottom:solid thin #e0e0e0;padding:10px 5px 10px 5px;'>"+
															"<div  style='width:70%;height:80%;float:left;'>"+
												    		"<span  class='article_title'>"+result[i].title+"</span>"+
												    		"<span   class='article_description'>"+(result[i].seo_description!=null?result[i].seo_description:"")+"</span>"+
												    		"<div    style='width:100%;float:left;font-size:12px;'>评论总数："+result[i].count+"-分享总数："+result[i].goodcount+"</div>"+
												    		"</div>"+
												    		"<img    style='max-width:30%;max-height:100%;float:left;border-radius:2px;'  src='/upload/031.jpg' />"+
														"</div>";
											}
										}
										container.innerHTML=ihtml;
									}
								});
							  }
						}
						function redirect(id){
							window.location.href="article.jhtml?id="+id;
						}
</script>
<style type="text/css">
	 #menu li{
	 	  float:left;
	 	  width:33%;
	 	  list-style:none;
	 	  text-align:center;
	 	  margin: 0 auto;
	 }
	 .li_select{
	 		border-bottom:solid  thin #cca;
	 	   color:#caa;
	 	   float:left;
	 }
	 .div_article{
	 	 background-color:#cca;
	 	 height:150px;
	 	 width:88%;
	 	 margin:10px 5px 10px 5px;
	 }
	 .article_title{
	 	 display:block;
		font-size:15px;
		height:20px;
		font-family:sans-serif;
		font-weight:bold;
	 	 width:100%;
	 	 overflow:hidden;
	 	 display:block;
	 	  text-overflow:ellipsis;
	 	  white-space:nowrap;
	 }
	.article_description{
	     width:100%;
	     height:50px;
		 overflow:hidden;
		 display:block;
		 text-overflow:ellipsis;
		 opacity:0.8;
		 font-size:12px;
	}
	.article_description:after{
		 content:"...";
	}
</style>
</head>
<body   style="overflow:hidden;">
		<div  style="width:100%;height:50px;">
			<!--  <span   style="display:block;margin:10px 0px 10px 10px;float:left;margin:10px 5px 10px 5px;"> &lt; </span>-->
			<input  type="text"   id="clause"   style="width:80%;border-radius:15px;border:solid thin #ccf;height:55%;margin-top:10px;float:left;margin-left:20px;background-image:url('/upload/search2x.png');background-repeat:no-repeat;background-position:right;"   placeholder="请输入查询关键字" />
			<label  style="color:blue;font-size:12px;margin:15px 15px 10px 5px;float:left;"   onclick="search()">搜索</label>
		</div>
		<hr/>
		<div  style="width:95%;height:200px;margin:0;margin-left:2%;overflow:hidden;">
			<div  style="height:30px;width:100%;">
			 <ul  style="width:100%;padding:0;margin:0;"  id="menu">
			 	<c:forEach  items="${category }"  var="c"   varStatus="status" >
			 		<li  onclick="menuTouch(this,${c.id})">${c.name }</li>
			 	</c:forEach>
			 </ul>
			</div>
			<div   id="div_banner"  style="background:url('/upload/01.jpg');width:100%;height:170px;background-repeat:repeat;background-size:100% 100%;">
				<label  id="banner_title"  style="width:91%;float:bottom;position:absolute;height:25px;margin:145px 10px 10px 0px;background-color:#000;opacity:0.5;color:white;">
				</label>
			</div>
		</div>
		<div  style="height:500px;width:110%;overflow-y:auto;"  id="article_container">
		   <c:forEach  items="${article }"     var="a">
		       <div   onclick="redirect(${a.id})" style="height:100px;border-bottom:solid thin #e0e0e0;padding:10px 5px 10px 5px;">
		       			<div  style="width:70%;height:80%;float:left;">
		       					<span class="article_title">
		       							${a.title }
		       					</span>
		       					<span class="article_description">
		       							  &nbsp;&nbsp;&nbsp;&nbsp;${a.seo_description }
		       					</span>
		       						<div  style="width:100%;float:left;font-size:12px;">评论总数：${a.count }  -  分享总数：${a.goodcount }</div>
		       			</div>
		       			<img  style="max-width:30%;max-height:100%;float:left;border-radius:2px;"  alt=""   src="/upload/031.jpg"/>
		       </div>
		   </c:forEach>
		</div>
</body>
</html>