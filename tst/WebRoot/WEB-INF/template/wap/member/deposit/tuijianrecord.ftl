[#include "/wap/include/header.ftl" /]

	<body class="mui-ios mui-ios-9 mui-ios-9-1">
		<header class="mui-bar mui-bar-nav header">
			<a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left"></a>
			<h1 class="mui-title">推荐记录</h1>
			<span class="mui-pull-right classify-btn">
				<a class="mui-block" href="#menu"><img src="/statics/img/menu.png"></a>
			</span>
			<div  class="menu bg-white">
				
			</div>
		</header>

	
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
				<li>我的积分：${member.point}</li>
				<li>我的消费券余额：${currency(member.balance, true, true)}</li>
			</ul>
		</div>
		
		<div class="mbtit">
		<a href="${base}/wap/member/deposit/tuijianrecord.jhtml" [#if current??&&current=1] class="active" [/#if]>一级用户</a><a [#if current??&&current==2] class="active" [/#if] href="${base}/wap/member/deposit/tuijianrecord.jhtml?current=2">二级用户</a>
		</div>
		<ul class="loglist">
			[#if pages?? && pages?has_content]
			[#list pages as depositLog]
			<li>
			[#if depositLog.tomember.avatar?? && depositLog.tomember.avatar?has_content]
			<img class="full" src="${base}${depositLog.tomember.avatar}">
			[#else]
				<img src="/statics/img/default_head.png">
			[/#if]
				<p>${depositLog.tomember.nickname}<br/>${depositLog.tomember.username}<br/><em>ID:${depositLog.id}</em></p>
				<span>${depositLog.create_date?string("yyyy-MM-dd")}</span>
			</li>
			[/#list]
			[/#if]
		</ul>
	</div>
		<style>
		.mbtit {height:.8rem; line-height:.8rem; padding:0 5%;} 
		.mbtit a {text-align:center; display:inline-block; width:50%; font-size:.26rem;line-height:.75rem;}
		.mbtit a.active { color:#259b24; border-bottom:solid 3px #259b24;}
		.loglist {border-bottom:solid .1rem #ccc;}
		.loglist li { height:2rem; border-top:solid .1rem #ccc;}
		.loglist li img { float:left; width:20%;height:80%;border:solid 1px #ccc; border-radius:30%;margin:3%;margin-top:5%;}
		.loglist li p { float:left; padding-top:5%; line-height:.42rem; color:#000; font-size:.24rem;}
		.loglist li p em {color:#ccc;}
		.loglist li span {  height:2rem; line-height:2rem;display:block; float:right; padding-right:5%; color:#ccc; font-size:.24rem;}
		</style>	

		<div style="clear:both;height:1.68rem;"></div>
<div class="nav-menu-1 nav-menu">
	[#include "/wap/include/footer.ftl" /]
	</body>
</html>
