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
		

		<ul class="loglist">
			[#if pages?? && pages?has_content]
			[#list pages.list as pointLog]
				<li>
					[#if pointLog.typeName=="cash"]
					提现申请
					[#elseif pointLog.typeName=="amount"]
					积分兑换
					[#elseif pointLog.typeName=="recommend"]
					推荐积分
					[#else]
						${message("PointLog.Type." + pointLog.typeName)}
					[/#if]
					&nbsp;&nbsp;获得${pointLog.credit}
					&nbsp;&nbsp;扣除${pointLog.debit}
					&nbsp;&nbsp;当前${pointLog.balance}
					[#if pointLog.member??]&nbsp;&nbsp;${pointLog.member.username}
					[#else]-[/#if]
					
					[#if pointLog.memo??]${abbreviate(pointLog.memo, 50, "...")}[/#if]
${pointLog.createDate?string("yyyy-MM-dd HH:mm:ss")}
					[#if pointLog.typeName=="cash"]
					<br />提现额度：${pointLog.piontCash!}元--
					[#if pointLog.status=="0"] <font color="ff00cc">未付款</font>
					[#else]<font color="33cc66">已付款</font>
					[/#if]
					[/#if]
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
		.loglist li img { float:left; width:20%; border:solid 1px #ccc; border-radius:50%;margin:3%;margin-top:5%;}
		.loglist li p { float:left; padding-top:5%; line-height:.42rem; color:#000; font-size:.24rem;}
		.loglist li p em {color:#ccc;}
		.loglist li span {  height:2rem; line-height:2rem;display:block; float:right; padding-right:5%; color:#ccc; font-size:.24rem;}
		</style>	

		<div style="clear:both;height:1.68rem;"></div>
<div class="nav-menu-1 nav-menu">
	[#include "/wap/include/footer.ftl" /]
	</body>
</html>
