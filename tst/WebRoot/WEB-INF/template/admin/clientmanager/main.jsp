<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>客户端控制台</title>

<style type="text/css">
	ul{
		list-style-type:none;
	}
	.menu1l{
		border:solid thin white;
		background-color:gray;
		width:90%;
		color:white;
		padding:5px;
		text-align:center;
		font-weight:bold;
	}
	.menu1l:hover{
		border-radius:15px;
	}
	.submenu_div{
		display:none;
		
	}
	.submenu_ul{
	border:solid thin #cff;
		border-radius:-1em 0 0 0;
		width:100%;
	}
	#menu_div ul{
		border-radius:10px;
		background-color:white;
	}
	#menu_div a{
		text-decoration:none;
	}
	.submenu_li{
	    background-color:white;
		border-radius:0 15px 15px 0;
		border-right:solid thin #ccf;
		font-size:15px;
		padding:5px 0 5px 5px;
		margin-left:-1em;
		width:100%;
		text-align:center;
		
	}
	.submenu_li a:link{}
	.submenu_li a:visited{
			color:yelow;
			border:solid thin black;
	}
	.submenu_li a:hover{}
	.submenu_li a:active{color:green;}
	.submenu_li:hover{
	 border-left:solid thin #ccf;
	 border-radius:15px;
	 width:105%;
	}
	.submenu_li:active{
		background-color:cyan;
	}
	.submenu_li a{
		width:100%;
		height:100%;
	}
	
</style>
<script type="text/javascript">
	 var cur;
	 var cursub;
	 function menu(nm){
		var nm=document.getElementById(nm);
		 if(cur==null){
			 nm.style.display="block";
		 }else{
			 if(nm==cur){
				 //alert(cur.style.display=="block"?"none":"block");
				 nm.style.display=nm.style.display=="block"?"none":"block";
			 }else{
				 nm.style.display="block";
				 cur.style.display="none";
			 }
		 }
		 cur=nm;
	 }
	 function submenu(ns){
		 //ns.style.backgroundColor="#f0f0f0";
		 if(cursub!=null){
			 cursub.style.backgroundColor="white";
		 }
		 var ns=document.getElementById(ns);
		if(ns!=cursub){
			 ns.style.backgroundColor="#f0f0f0";
			 if(cursub!=null)cursub.style.backgroundColor="white";
			 cursub=ns;
		}
	 }
</script>
</head>
<body style="width:1400px;text-align:center;margin:0 auto;margin-top:20px;">
	<div style="width:100%;">
		<div style="height:90px;width:95%;margin-left:2%;border:solid #ccf thick;background-color:#ccfccf">
		<h2 style="margin:1em;">SES共享商城客户端控制中心</h2>
	</div>
	<div  style="height:85%;width:95%;margin-left:2%;border:solid #cff thin;font-family:sans-serif;">
		<div id="menu_div" style="width:200px;border:#fcc solid thin;float:left;background-color:white;font-size:1.2em;">
			<ul style="width:95%;padding:5px;">
				<li>
					<div class="menu1l"  onclick="menu('m1')">新增模块</div>
					<div class="submenu_div"   id="m1">
						<ul>
							<li class="submenu_li" id="a1"><a href="/admin/clientmanager/boot.jhtml"   target="iframes" onclick="submenu('a1')">引导页管理</a></li>
							<li class="submenu_li" id="a2"><a href="/admin/clientcontro/clientad.jhtml"   target="iframes" onclick="submenu('a2')">广告/活动管理</a></li>
							<li class="submenu_li" id="a3"><a href="/admin/clientcontro/news.jhtml"   target="iframes"   onclick="submenu('a3')">资讯管理</a></li>
							<li class="submenu_li"  id="a4"><a href="/admin/clientcontro/store.jhtml"    target="iframes"  onclick="submenu('a4')">商铺管理</a></li>
							<li  class="submenu_li"    id="a5"><a href="/admin/otherplay.jhtml"    target="iframes"    onclick="submenu('a5')">其他直播</a></li>
							<li  class="submenu_li"  id="a6"><a href="/admin/"    target="iframes"    onclick="submenu('a6')">基地管理</a></li>
							<li  class="submenu_li"  id="a7"><a href="/admin/clientmanager/level.jhtml"     target="iframes"   onclick="submenu('a7')">会员等级</a></li>
						</ul>
					</div>
				</li>
				<li>
					<div class="menu1l"  onclick="menu('m2')">商铺管理</div>
					<div class="submenu_div" id="m2">
						<ul>
							<li class="submenu_li"><a href="">商铺</a></li>		
						</ul>
					</div>
				</li>
			</ul>
		</div>
		<iframe id="iframes" name="iframes" src="  /index.jsp"   style="width:1100px;height:700px;float:left;border:none" scrolling="no">
		</iframe>
	</div>
	</div>
</body>
</html>