<!DOCTYPE html>
<html>

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

		<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no">
		<title>SES共享商城</title>
		<meta name="description" content="">
		<meta name="keywords" content="">
		<link rel="stylesheet" type="text/css" href="${base}/statics/css/mui.min.css">
		<link rel="stylesheet" type="text/css" href="${base}/statics/css/jfinalshop.css">
		<link rel="stylesheet" type="text/css" href="${base}/statics/css/jfinalshop.mobile.css">
		<script type="text/javascript" src="${base}/statics/js/mui.min.js"></script>
		<style type="text/css" adt="123"></style>
		<script type="text/javascript" src="${base}/statics/js/jquery-2.1.1.min.js"></script>
		<script type="text/javascript" src="${base}/statics/js/jfinalshop.js"></script>
		<script type="text/javascript" src="${base}/statics/js/jfinalshop.slider.js"></script>
		<script type="text/javascript" src="${base}/statics/js/jquery.lazyload.js"></script>
		<script type="text/javascript" charset="utf-8">
			mui.init();
		</script>
		
	</head>
	<body class="mui-ios mui-ios-9 mui-ios-9-1">
	<script src="https://qiyukf.com/script/3b6726684c2681a4a2692679a4607fb3.js">
	</script>
	 <script type="text/javascript">
	window.onload=function(){
		if("${cc}"){
		var c="${cc }";
			pa.fmc(c);
		}		
	}
	 
	 function ttt(){
	 	ysf.config({
	 	name:'未登录用户',
	 	success:function(){
	 		ysf.open();
	 	},
	 	error:function(){
	 		alert("连接失败，请稍后重试！");
	 	}
	 		});
	 }
	 
	 function allocate(){
			$.post("/wap/member/dailyAllocate.jhtml",{state:1},function(result){
				var s=document.getElementById("s1");
				var poi=document.getElementById("poi");
				var ms="";
				switch(parseInt(result.state)){
				case 0:
				 ms="您已领取！";
				break;
				case 1:
				ms="您本次领取"+result.count+"积分";
				break;
				case 2:
				ms="领取失败，请您稍后再试";
				break;
				case 3:
				ms="分红已关闭";
				break;
				case 4:
				ms="登陆之后才能领取！";
				break;
				}
				document.getElementById("s2").innerHTML=ms;
				document.getElementById("dialog").style.display="block";
				s.style="font-color:black;";
				
				if(result.state!="3"){
				s.innerHTML=result.count;
				poi.innerHTML=parseInt(result.count)+("${member.point}");
				}
			},'json');
		}
		
		function closebox(){
			document.getElementById("dialog").style.display="none";
		}
	 </script>
	 
		<style>
		body #YSF-BTN-HOLDER {
			right: 1em;
			bottom: 11em;
			max-width: 0em;
			max-height: 0em;
		}
		#YSF-BTN-HOLDER #YSF-CUSTOM-ENTRY-0 img{
			width:0px;
			height:0px;
			max-width: none;
			max-height: none;
		}
		
		
			body {
				background-color: #eeeeee;
			}
		</style>
		<header class="mui-bar mui-bar-nav header">
			<div class="logo mui-pull-left">
				<a href="javascript:;" id="mn-child"><img src="${base}/statics/img/menu.png"></a>
			</div>
			<h1 class="mui-title">SES共享商城</h1>
			<a href="${base}/wap/goods/search.jhtml?keyword=" class="hd-menu"><img src="${base}/statics/img/search.png"></a>
			<div class="menu_box">
				<ul>
					<li><a href="${base}/wap/member/message/list.jhtml"><i class="msg"></i>我的消息<!-- <span></span> --></a></li>
					<li><a href="${base}/wap/member/deposit/recharge.jhtml"><i class="join"></i>用户办理</a></li>
					<li><a href="${base}/wap/member/deposit/log.jhtml"><i class="amount"></i>余额查询</a></li>
					<li><a href="${base}/wap/member/deposit/tuijian.jhtml"><i class="tuij"></i>推荐有奖</a></li>
					<li><a href="javascript:void(0)" ontouchend="ttt()"><i  class="serv" ></i>在线客服</a></li>
					<!--<li><a href="${base}/Sample.html"><i class="serv"></i>test</a></li>-->
				</ul>
			</div>
		</header>
		<div id="dialog" style="z-index:100;position:absolute;height:30%;width:75%;border-radius:10px;margin-left:11.5%;border-width:2px;margin-top:35%;background-color:#fff;display:none;">
			<p id="s2" style="position:relative;text-align:CENTER;margin-top:20%;font-size:2em;">fjkaljfl;dsjlak</p>
			<input type="button" value="确定" style="height:2em;width:3em;margin-top:20%;width:50%;margin-left:22%;border-radius:5px;border:color:#ffc" onclick="closebox()"/>
			
		</div>
		<div class="mui-content">
			<div class="mui-slider">
				<div class="mui-slider-group" style="transform: translate3d(0px, 0px, 0px) translateZ(0px);">
				[@ad_position id = 7 /]
				</div>
				<div class="mui-slider-indicator">
					<div class="mui-indicator mui-active"></div>
					<div class="mui-indicator"></div>
					<div class="mui-indicator"></div>
				</div>
			</div>
			<!--
			<a class="custom-notice custom-notice-jd" href="/index.php">
				<div class="notice-img"><img src="${base}/statics/img/57ede501f32fd.gif"></div>
				<div class="notice-text mui-ellipsis" style="color: #0c0303;"></div>
			</a> 
			
			<div class="hd-search">
				<form name="form_search" action="/wap/goods/search.jhtml" method="post">
					<input type="search" placeholder="搜索商品名称" name="keyword">
				</form>
			</div>-->
			<nav class="quick-entry-nav hd-grid">
				<a href="${base}/wap/member.jhtml" class="hd-col-xs-e4 quick-entry-link"><span class="nav-img"><img src="${base}/statics/img/member.png"></span><span class="title">用户中心</span></a>
				<a href="${base}/wap/product_category.jhtml" class="hd-col-xs-e4 quick-entry-link"><span class="nav-img"><img src="${base}/statics/img/category.png"></span><span class="title">产品中心</span></a>
				<a  ontouchend="allocate()" class="hd-col-xs-e4 quick-entry-link"><span class="nav-img"><img src="${base}/statics/img/allocate.png"></span><span class="title">每日分红</span></a>
				<a href="${base}/wap/goods/list.jhtml?tagId=4" class="hd-col-xs-e4 quick-entry-link"><span class="nav-img"><img src="${base}/statics/img/new.png"></span><span class="title">最新上市</span></a>
			</nav>
			<div class="news" style="position:relative;">
				<a href="/wap/article/list/1.jhtml" style="position:absolute; left:0px; top:0px; height:100%; width:30%"></a>
				<SCRIPT>  
function srcMarquee(){
this.ID = document.getElementById(arguments[0]);
if(!this.ID){this.ID = -1;return;}
this.Direction = this.Width = this.Height = this.DelayTime = this.WaitTime = this.Correct = this.CTL = this.StartID = this.Stop = this.MouseOver = 0;
this.Step = 1;
this.Timer = 30;
this.DirectionArray = {"top":0 , "bottom":1 , "left":2 , "right":3};
if(typeof arguments[1] == "number")this.Direction = arguments[1];
if(typeof arguments[2] == "number")this.Step = arguments[2];
if(typeof arguments[3] == "number")this.Width = arguments[3];
if(typeof arguments[4] == "number")this.Height = arguments[4];
if(typeof arguments[5] == "number")this.Timer = arguments[5];
if(typeof arguments[6] == "number")this.DelayTime = arguments[6];
if(typeof arguments[7] == "number")this.WaitTime = arguments[7];
if(typeof arguments[8] == "number")this.ScrollStep = arguments[8]
this.ID.style.overflow = this.ID.style.overflowX = this.ID.style.overflowY = "hidden";
this.ID.noWrap = true;
this.IsNotOpera = (navigator.userAgent.toLowerCase().indexOf("opera") == -1);
if(arguments.length >= 7)this.Start();
}
srcMarquee.prototype.Start = function(){
if(this.ID == -1)return;
if(this.WaitTime < 800)this.WaitTime = 800;
if(this.Timer < 20)this.Timer = 20;
if(this.Width == 0)this.Width = parseInt(this.ID.style.width);
if(this.Height == 0)this.Height = parseInt(this.ID.style.height);
if(typeof this.Direction == "string")this.Direction = this.DirectionArray[this.Direction.toString().toLowerCase()];
this.HalfWidth = Math.round(this.Width / 2);
this.BakStep = this.Step;
this.ID.style.width = this.Width;
this.ID.style.height = this.Height;
if(typeof this.ScrollStep != "number")this.ScrollStep = this.Direction > 1 ? this.Width : this.Height;
var msobj = this;
var timer = this.Timer;
var delaytime = this.DelayTime;
var waittime = this.WaitTime;
msobj.StartID = function(){msobj.Scroll()}
msobj.Continue = function(){
if(msobj.MouseOver == 1){
setTimeout(msobj.Continue,delaytime);
     }
     else{ clearInterval(msobj.TimerID);
msobj.CTL = msobj.Stop = 0;
msobj.TimerID = setInterval(msobj.StartID,timer);
     }
    }
msobj.Pause = function(){
msobj.Stop = 1;
clearInterval(msobj.TimerID);
setTimeout(msobj.Continue,delaytime);
    }
msobj.Begin = function(){
   msobj.ClientScroll = msobj.Direction > 1 ? msobj.ID.scrollWidth : msobj.ID.scrollHeight;
   if((msobj.Direction <= 1 && msobj.ClientScroll <msobj.Height) || (msobj.Direction > 1 && msobj.ClientScroll <msobj.Width))return;
   msobj.ID.innerHTML += msobj.ID.innerHTML;
   msobj.TimerID = setInterval(msobj.StartID,timer);
   if(msobj.ScrollStep < 0)return;
   msobj.ID.onmousemove = function(event){
       if(msobj.ScrollStep == 0 && msobj.Direction > 1){
var event = event || window.event;
if(window.event){
if(msobj.IsNotOpera){msobj.EventLeft = event.srcElement.id == msobj.ID.id ? event.offsetX - msobj.ID.scrollLeft : event.srcElement.offsetLeft - msobj.ID.scrollLeft + event.offsetX;}
else{msobj.ScrollStep = null;return;}
}
else{msobj.EventLeft = event.layerX - msobj.ID.scrollLeft;}
msobj.Direction = msobj.EventLeft > msobj.HalfWidth ? 3 : 2;
msobj.AbsCenter = Math.abs(msobj.HalfWidth - msobj.EventLeft);
msobj.Step = Math.round(msobj.AbsCenter * (msobj.BakStep*2) / msobj.HalfWidth);
}
}
msobj.ID.onmouseover = function(){
if(msobj.ScrollStep == 0)return;
msobj.MouseOver = 1;
clearInterval(msobj.TimerID);
}
msobj.ID.onmouseout = function(){
if(msobj.ScrollStep == 0){
if(msobj.Step == 0)msobj.Step = 1;
return;
}
msobj.MouseOver = 0;
if(msobj.Stop == 0){
clearInterval(msobj.TimerID);
msobj.TimerID = setInterval(msobj.StartID,timer);
}}}
setTimeout(msobj.Begin,waittime);
}
srcMarquee.prototype.Scroll = function(){
switch(this.Direction){
case 0:
this.CTL += this.Step;
if(this.CTL >= this.ScrollStep && this.DelayTime > 0){
this.ID.scrollTop += this.ScrollStep + this.Step - this.CTL;
this.Pause();
return;
}
else{
if(this.ID.scrollTop >= this.ClientScroll){this.ID.scrollTop -= this.ClientScroll;}
this.ID.scrollTop += this.Step;
}
break;
case 1:
this.CTL += this.Step;
if(this.CTL >= this.ScrollStep && this.DelayTime > 0){
this.ID.scrollTop -= this.ScrollStep + this.Step - this.CTL;
this.Pause();
return;
}
else{
if(this.ID.scrollTop <= 0){this.ID.scrollTop += this.ClientScroll;}
this.ID.scrollTop -= this.Step;
}
break;
case 2:
this.CTL += this.Step;
if(this.CTL >= this.ScrollStep && this.DelayTime > 0){
this.ID.scrollLeft += this.ScrollStep + this.Step - this.CTL;
this.Pause();
return;
}
else{
if(this.ID.scrollLeft >= this.ClientScroll){this.ID.scrollLeft -= this.ClientScroll;}
this.ID.scrollLeft += this.Step;
}
break;
case 3:
this.CTL += this.Step;
if(this.CTL >= this.ScrollStep && this.DelayTime > 0){
this.ID.scrollLeft -= this.ScrollStep + this.Step - this.CTL;
this.Pause();
return;
}
else{
if(this.ID.scrollLeft <= 0){this.ID.scrollLeft += this.ClientScroll;}
this.ID.scrollLeft -= this.Step;
}
break;
}
} 
</SCRIPT>
				
				<DIV id=Scroll>
<DIV id=ScrollMe style="OVERFLOW: hidden; HEIGHT: 48px">
<ol style="height:240px;overflow:hidden;">
				
				[@article_category_root_list count = 2]
					[#if articleCategories?has_content]
						
							[#assign rows = 0 /]
							[#list articleCategories as articleCategory]
								[@article_list article_category_id = articleCategory.id count = 6]
									
										[#list articles as article]
											
											<li  style="display:block; height:30px;">
												<a style="display:block" href="/wap/article/detail.jhtml?id=${article.id}">${abbreviate(article.title, 40)}</a>
											</li>
											[#assign rows = rows + 1 /]	
											
										[/#list]
							
								[/@article_list]
							[/#list]
					[/#if]
				[/@article_category_root_list]
				</ol>

</DIV></DIV>
<SCRIPT>new srcMarquee("ScrollMe",0,1,808,24,30,3000,3000,24)</SCRIPT>
</DIV>
				
				
				
			</div>
			<div class="index_goods_block mui-clearfix">
			<!--热门推荐-->
			<div class="hot"><strong style="margin-left:.3rem;text-align:center">热门推荐</strong><a href="${base}/wap/goods/list/244.jhtml?productCategoryId=244&tagId=3">更多 <i>></i></a></div>
			<ul class="custom-goods-items mui-clearfix">
				[#list goodsList_tag as goods]
				<li class="goods-item-list">
					<a class="list-item" href="${base}/wap/goods/detail.jhtml?id=${goods.id}">
						<div class="list-item-pic">
							<div class="square-item">
							<img class="lazy" src="${base}/upload/image/blank.gif" data-original="${goods.image!setting.defaultThumbnailProductImage}" style="display: block;">
							</div>
						</div>
						<div class="list-item-bottom">
							<div class="list-item-title">
								<span>
									[#if goods.caption?has_content]

<span title="${goods.name}">
[#if "${goods.is_dz}" == "1"]
<i class="dz"></i>
[#else]
<i class="xh"></i>
[/#if]
${abbreviate(goods.name, 28)}
</span>
<em title="${goods.caption}">
${abbreviate(goods.caption, 18)}
</em>
									[#else]
										${abbreviate(goods.name, 18)}
									[/#if]
								</span>
							</div>
							<div class="list-item-text">
							<span class="price-org"></p>
							${currency(goods.price, true)}/${goods.unit!}
[#list goods.products as product]
[#if product_index<1]
	  <em style="color: #9b9b9b;font-size: .18rem; display:block">
	  已售:
	   ${goods.sales}
	  剩余:${product.stock} </em>
[/#if]
[/#list]
							</span>
							</div>
						</div>
					</a>
				</li>
				[/#list]
			</ul>
			<!--热门推荐end-->
			<div class="inbanner">
			[@ad_position id = 8 /]
			
			
			<!--现货商品-->

			<div class="xianhuo">现货商品 <a href="${base}/wap/goods/list/244.jhtml?productCategoryId=244" >更多 <i>></i></a></div>
			<ul class="custom-goods-items mui-clearfix">
				[#list goodsList as goods]
				<li class="goods-item-list">
					<a class="list-item" href="${base}/wap/goods/detail.jhtml?id=${goods.id}">
						<div class="list-item-pic">
							<div class="square-item"><img class="lazy" src="${base}/upload/image/blank.gif" data-original="${goods.image!setting.defaultThumbnailProductImage}" style="display: block;"></div>
						</div>
						<div class="list-item-bottom">
							<div class="list-item-title">
								<span>
									[#if goods.caption?has_content]
										<span title="${goods.name}"><i class="xh"></i>${abbreviate(goods.name, 28)}</span>
										<em title="${goods.caption}">${abbreviate(goods.caption, 18)}</em>
									[#else]
										${abbreviate(goods.name, 18)}
									[/#if]
								</span>
							</div>
							<div class="list-item-text"><p style="align:center"><span class="price-org">${currency(goods.price, true)}/${goods.unit!}
[#list goods.products as product]
[#if product_index<1]
	 <em style="color: #9b9b9b;font-size: .18rem; display:block">已售:${goods.sales}&nbsp;剩余:${product.stock} </em>
[/#if]
[/#list]
							</span></div>
						</div>
					</a>
				</li>
				[/#list]
			</ul>

			<!--现货商品end-->
			<div class="inbanner">[@ad_position id = 9 /]</div>
			
			<!--定制商品-->
			<div class="dingzhi">订制商品<a href="${base}/wap/goods/list/243.jhtml?productCategoryId=243" >更多 <i>></i></a></div>
			
			<ul class="custom-goods-items mui-clearfix">
				[#list goodsList_dz as goods]
				<li class="goods-item-list">
					<a class="list-item" href="${base}/wap/goods/detail.jhtml?id=${goods.id}">
						<div class="list-item-pic">
							<div class="square-item">
							<img class="lazy" src="${base}/upload/image/blank.gif" data-original="${goods.image!setting.defaultThumbnailProductImage}" style="display: block;">
							</div>
						</div>
						<div class="list-item-bottom">
							<div class="list-item-title">
								<span>
									[#if goods.caption?has_content]
										<span title="${goods.name}"><i class="dz"></i>${abbreviate(goods.name, 28)}</span>
										<em title="${goods.caption}">${abbreviate(goods.caption, 18)}</em>
									[#else]
										${abbreviate(goods.name, 18)}
									[/#if]
								</span>
							</div>
							<div class="list-item-text"><span class="price-org">${currency(goods.price, true)}/${goods.unit!}
[#list goods.products as product]
[#if product_index<1]
	  <em style="color: #9b9b9b;font-size: .18rem; display:block">已售：${goods.sales}&nbsp;剩余 ${product.stock} </em>
[/#if]
[/#list]							
							</span></div>
						</div>
					</a>
				</li>
				[/#list]
			</ul>
			<!--定制商品end-->
			
			<div class="mui-slider">
				<div class="mui-slider-group" style="transform: translate3d(0px, 0px, 0px) translateZ(0px);">
				[#list adPosition.ads as ad]
					[#if ad.typeName == "image" && ad.hasBegun() && !ad.hasEnded()]
						[#if ad.url??]
							<a class="mui-slider-item" href="#"><img src="${ad.path}"></a>
						[/#if]
					[/#if]
				[/#list]
				</div>
				<div class="mui-slider-indicator">
					<div class="mui-indicator mui-active"></div>
					<div class="mui-indicator"></div>
					<div class="mui-indicator"></div>
				</div>
			</div>
			
			</div>
		</div>
		<footer class="footer">
			<!--<div class="text-gray mui-text-center copy-text"></div>-->
		</footer>
		<script>
			$("[name=form_search]").submit(function() {
				if($("[type=search]").val() == "") {
					return false;
				}
			});
		</script>
		[#include "/wap/include/footer.ftl" /]
		<script type="text/javascript">
			mui(".nav-menu").on("tap", ".nav-item", function() {
				if($(this).hasClass("current")) {
					$(this).removeClass("current");
				} else {
					var tw = $(this).outerWidth(true);
					var ch = $(this).children(".submenu");
					var cw = ch.outerWidth(true);
					ch.css({
						left: (tw - cw) / 2 + "px"
					});
					$(".nav-menu .nav-item").removeClass("current");
					$(this).addClass("current");
				}
			});
		</script>
		<div class="nav-menu-mask"></div>
		<script>
			if($(".mui-content.mui-scroll-wrapper").length > 0) {
				$(".mui-content.mui-scroll-wrapper").css({
					bottom: "1.05rem"
				})
			}
			
			
			$(function(){
				$("#mn-child").on("tap",function(){
				 
					if($(this).hasClass("open")){
						$(".menu_box ").animate({"left":"-50%"},300);
						$(this).removeClass("open");
					}else{					
						$(".menu_box ").animate({"left":"0rem"},300);
						$(this).addClass("open");
						//角度旋转90度
					} 
				 
				});
			
				var gallery = mui('.mui-slider');
				gallery.slider({
					  interval:4000//自动轮播周期，若为0则不自动播放，默认为0；
				});
			
			})
		</script>
		<div id="cli_dialog_div"></div>
		
	</body>

</html>