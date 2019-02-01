<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no">
		<title>${title}</title>
		<meta name="description" content="">
		<meta name="keywords" content="">
		<link rel="stylesheet" type="text/css" href="${base}/statics/css/mui.min.css?v=2.6.0.161014">
		<link rel="stylesheet" type="text/css" href="${base}/statics/css/jfinalshop.css?v=2.6.0.161014">
		<link rel="stylesheet" type="text/css" href="${base}/statics/css/jfinalshop.mobile.css?v=2.6.0.161014">
		<script type="text/javascript" src="${base}/statics/js/mui.min.js?v=2.6.0.161014"></script>
		<script type="text/javascript" src="${base}/statics/js/jfinalshop.slider.js?v=2.6.0.161014"></script>
		<script type="text/javascript" src="${base}/statics/js/jquery-2.1.1.min.js?v=2.6.0.161014"></script>
		<script type="text/javascript" src="${base}/statics/js/jfinalshop.js?v=2.6.0.161014"></script>
		<style type="text/css">
			body {
				background-color: #fff;
			}
			
			.header {
				/*background-color: #0068b7;*/
			}
		</style>
		<style type="text/css">
			#ad_headerbanner,
			#ad_text {
				display: none!important;
				display: none
			}
		</style>
	</head>
	<script type="text/javascript">
	window.onload=function(){
		if("${receive}"=="0"){
			document.getElementById("s1").style.color="red";
		}else{
			document.getElementById("s1").style.color="blue";
		}		
	}
		function conservice(){
			 ysf.config({
	 	name:'${member.username }',
	 	data:JSON.stringify([{"key":"age","value":"${member.age }"}]),
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
				}
				document.getElementById("s2").innerHTML=ms;
				document.getElementById("dialog").style.display="block";
				s.style="font-color:black;";
				
				if(result.state!="3"){
				s.innerHTML=result.count;
				poi.innerHTML=((result.count)*100+("${member.point}")*100)/100;
				s.style.color="blue";
				}
			},'json');
		}
		
		function closebox(){
			document.getElementById("dialog").style.display="none";
		}
	</script>
	<body class="mui-ios mui-ios-9 mui-ios-9-1">
	
	<style>
		body #YSF-BTN-HOLDER {
			right: 0em;
			bottom: 0em;
			max-width: 0em;
			max-height: 0em;
		}</style>
		<header class="mui-bar mui-bar-nav header">
			<h1 class="mui-title">${title}</h1>
			<span class="mui-pull-right classify-btn">
				<a class="mui-block" href="${base}/wap/product_category.jhtml"><img src="${base}/statics/img/menu.png"></a>
			</span>
		</header>

<style>
			body {
				background-color: #f5f5f5;
			}
		</style>
		<div id="dialog" style="z-index:100;position:absolute;height:30%;width:75%;border-radius:10px;margin-left:11.5%;border-width:2px;margin-top:35%;background-color:#fff;display:none;">
			<p id="s2" style="position:relative;text-align:CENTER;margin-top:20%;font-size:2em;">fdasdf</p>
			<input type="button" value="确定" style="height:2em;width:3em;margin-top:20%;width:50%;margin-left:22%;border-radius:5px;border:color:#ffc" onclick="closebox()"/>
			
		</div>
	<div class="mui-content">
		<div class="user-brief mui-clearfix">
			<div class="user-head mui-pull-left">
			[#if member.avatar?? && member.avatar?has_content]
				<a><img class="full" src="${base}${member.avatar}"></a>
			[#else]
				<a><img class="full" src="${base}/statics/img/default_head.png"></a>
			[/#if]
			</div>
			<ul class="user-text mui-pull-left">
				<li>用户昵称：${member.username}</li>
				<li>用户等级：${member.scardorder.card.name}</li>
				<li>积分：<em class="" id="poi">${member.point}</em></li>
				<!-- <li>经验值：<em class="text-org">0</em></li> -->
			</ul>
		</div>
		<div class="mui-row member-quick-nav bg-white">
			<a href="${base}/wap/member/favorite/list.jhtml?period=day" class="mui-col-xs-3 item-collect"><span>${favoriteCount}</span>收藏的商品</a>			
			<a  class="mui-col-xs-3 item-coupon border-left-s" ontouchend="allocate()"><span id="s1">${rvcount }</span>每日分红</a>
			<a href="${base}/wap/member/deposit/log.jhtml" class="mui-col-xs-3 item-balance border-left-s"><span>${currency(member.balance, true, true)}</span>我的钱包</a>
			<a href="${base}/wap/member/deposit/tuijian.jhtml" class="mui-col-xs-3 item-balance border-left-s"><span class="mui-block text-ellipsis" style="text-align:center">
			<img class="full" src="${base}/statics/images/share_ico.png" style="width:.25rem; display:inline">
			</span>推荐好友</a>
		</div>
		<ul class="mui-table-view layout-list-common order_title">
			<li class="mui-table-view-cell">
				<a href="${base}/wap/member/order/list.jhtml" class="mui-navigate-right">
					
					<span class="hd-h4" style="color:#259b24">我的订单</span>
					<p class="mui-pull-right">查看全部订单</p>
				</a>
			</li>
		</ul>
		<div class="mui-row wait-work bg-white border-bottom">
			<a href="${base}/wap/member/order/list.jhtml?status=pendingPayment" class="mui-col-xs-3 icon-20">
				<img src="${base}/statics/images/ico_7.png">
				<span>待付款</span>
				[#if memberPendingPaymentOrderCount > 0]
					<em class="tag">${memberPendingPaymentOrderCount}</em>
				[/#if]
			</a>
			<a href="${base}/wap/member/order/list.jhtml?status=pendingShipment" class="mui-col-xs-3 icon-20">
				<img src="${base}/statics/images/ico_8.png">
				<span>待发货</span>
				[#if memberPendingShipmentOrderCount > 0]
					<em class="tag">${memberPendingShipmentOrderCount}</em>
				[/#if]
			</a>
			
			<a href="${base}/wap/member/order/list.jhtml?status=shipped" class="mui-col-xs-3 icon-20">
				<img src="${base}/statics/images/ico_9.png">
				<span>待签收</span>
				[#if membershippedOrderCount > 0]
					<em class="tag">${membershippedOrderCount}</em>
				[/#if]
			</a>
			
			<a href="${base}/wap/member/order/list.jhtml?status=received" class="mui-col-xs-3 icon-20">
				<img src="${base}/statics/images/ico_9.png">
				<span>已收货</span>
				[#if memberReceivedOrderCount > 0]
					<!--<em class="tag">${memberReceivedOrderCount}</em>-->
				[/#if]
			</a>
			<!--  
			<a href="${base}/wap/member/review/list.jhtml?type=positive" class="mui-col-xs-3 icon-20">
				<img src="${base}/statics/images/ico_10.png">
				<span>待评价</span>
				[#if pendingReviewCount > 0]
					<em class="tag">${pendingReviewCount}</em>
				[/#if]
			</a>
			-->
		</div>
		
		<ul class="mui-table-view layout-list-common">
			<li class="mui-table-view-cell">
				<a href="${base}/wap/member/order/list.jhtml?isdiz=1" class="mui-navigate-right">
					<span class="icon-20"><img src="${base}/statics/images/ico_m_1.png"></span>
					<span class="hd-h6">我的定制</span>
					<p class="mui-pull-right">个人专属</p>
				</a>
			</li>			
			<li class="mui-table-view-cell">
				<a href="${base}/wap/member/profile/edit.jhtml" class="mui-navigate-right">
					<span class="icon-20"><img src="${base}/statics/images/icon_m_2.png"></span>
					<span class="hd-h6">个人资料</span>
					<p class="mui-pull-right">修改信息</p>
				</a>
			</li>
			<li class="mui-table-view-cell">
				<a href="${base}/wap/member/password/edit.jhtml" class="mui-navigate-right">
					<span class="icon-20"><img src="${base}/statics/images/icon_m_3.png"></span>
					<span class="hd-h6">账号安全</span>
					<p class="mui-pull-right">修改密码</p>
				</a>
			</li>
		 
			<li class="mui-table-view-cell">
				<a href="${base}/wap/member/service/list.jhtml?status=-1" class="mui-navigate-right">
					<span class="icon-20"><img src="${base}/statics/images/icon_m_4.png" /></span>
					<span class="hd-h6">售后服务</span>
					<p class="mui-pull-right"></p>
				</a>
			</li>
			
			
			<li class="mui-table-view-cell">
				<a href="${base}/wap/member/deposit/tuijian.jhtml" class="mui-navigate-right">
					<span class="icon-20"><img src="${base}/statics/images/icon_m_5.png" /></span>
					<span class="hd-h6">我的邀请码</span>
					<p class="mui-pull-right">${member.createDate?string("HHmmss")}${member.id}</p>
					<!-- yyyy-MM-dd HH:mm:ss -->
				</a>
			</li>
			<li class="mui-table-view-cell">
				<a  class="mui-navigate-right" ontouchend="conservice()">
					<span class="icon-20"><img src="${base}/statics/images/icon_m_6.png" /></span>
					<span class="hd-h6">在线客服</span>
					<p class="mui-pull-right">${hotline!}</p>
				</a>
			</li>
			
			<li class="mui-table-view-cell">
				<a href="${base}/wap/member/deposit/introduction.jhtml" class="mui-navigate-right">
					<span class="icon-20"><img src="${base}/statics/images/icon_m_4.png" /></span>
					<span class="hd-h6">关于SES</span>
					<p class="mui-pull-right"></p>
				</a>
			</li>
		</ul>
		<div class="margin padding-small">
			<a href="/wap/logout.jhtml" class="mui-btn mui-btn-blue full mui-h5 line78" style="color:#fff;">退出当前账号</a>
		</div>
	</div>
	[#include "/wap/include/footer.ftl" /]
	
	
	

[#if returnCount > 0]
<div class="dialog">
	<div class="msgbox">
		<div class="msg">您有用户卡即将到期,请处理！</div>
		<p>
			<a href="${base}/wap/member/deposit/mycard.jhtml" class="mui-btn  mui-btn-primary hd-h4 " style="color:#fff; width:40%; margin:.1rem; border:solid 1px #259b24">续期</a><a href="${base}/wap/member/deposit/mycard.jhtml" class="mui-btn  mui-btn-primary hd-h4 " style="color:#fff;width:40%; margin:.1rem; color:#259b24; background-color:#fff; border:solid 1px #259b24">退款</a>
		 </p>
		<p>
		
		<a href="javascript:$('.dialog').hide();" class="mui-btn  mui-btn-primary hd-h4 " style="background-color:#dcdcdc;color:#acacac; width:40%; margin:.1rem; border:solid 1px #dcdcdc">下次提醒</a><a href="${base}/wap/member/deposit/mycard.jhtml" class="mui-btn  mui-btn-primary hd-h4 " style="color:#fff;width:40%; margin:.1rem; color:#259b24; background-color:#fff; border:solid 1px #259b24;">查看</a>
		</p>
	</div>
	
</div>
[/#if]

	<script src="https://qiyukf.com/script/3b6726684c2681a4a2692679a4607fb3.js">
	
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
	</script>
	<div id="cli_dialog_div"></div>
	
</body>

</html>