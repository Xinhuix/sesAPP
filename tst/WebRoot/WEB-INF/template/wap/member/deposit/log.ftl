[#include "/wap/include/header.ftl" /]
<style>
			body {
				background-color: #f5f5f5;
			}
		</style>


		<div id="refreshContainer" class="mui-content">
			<div class="mui-pull-top-pocket">
				<div class="mui-pull">
					<div class="mui-pull-loading mui-icon mui-icon-pulldown"></div>
					<div class="mui-pull-caption">下拉可以刷新</div>
				</div>
			</div>
			<div class="mui-content" style="transform: translate3d(0px, 0px, 0px) translateZ(0px);">
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
				<li>积分：<em class="">${member.point}</em></li>
				<!-- <li>经验值：<em class="text-org">0</em></li> -->
			</ul>
		</div>
			
		<div class="member_box">
			<ul>
				<li><a href="#" ><span>${currency(member.balance, true, true)}</span><p>粮票余额<p></a></li>
				<li>
				<a href="#" ><span>￥${mmoney}.00元</span><p>托管费<p></a>
				</li>
				<li>
				<a href="${base}/wap/member/deposit/pointdh.jhtml">
				<img src="${base}/statics/images/ico_jifen.png" />
				<p>我的积分</p></a>
				</li>
				<li><a href="${base}/wap/member/deposit/recharge.jhtml">
				<img src="${base}/statics/images/ico_buy.png" /><p>办理用户</p></a>
				</li>
				<li><a href="${base}/wap/member/deposit/tuijianrecord.jhtml">
				<img src="${base}/statics/images/ico_buy.png" /><p>推荐记录</p></a>
				</li>
				<li>
					
					<a href="${base}/wap/member/deposit/mycard.jhtml">
				   <img src="${base}/statics/images/ico_card.png" /><p>我的SES卡包</p>
				   </a>
				</li>
			</ul>
		
			<div style="clear:both"></div>
		</div>	
		
		
		<div class="member_inbox" >
			<a href="${base}/wap/member/order/list.jhtml" class="mui-btn hd-btn-blue">
			我的订单记录</a>  
			<a href="${base}/wap/member/deposit/logpoint.jhtml" class="mui-btn hd-btn-gray">我的返还记录</a>
		</div>
		
				
				<!--
				<ul class="border-top balance-lists list-col-10 mui-row mui-clearfix log-lists" style="display: none;">
					<li class="padding bg-white border-bottom clearfix"><span>收支明细</span></li>
				</ul>
				<ul class="order-lists margin-top border-top mui-clearfix">
					<li class="padding bg-white border-bottom"><span>收支明细</span></li>
					[#if pages?? && pages.list?has_content]
						[#list pages.list as depositLog]
						<li class="balance-list margin-left padding-tb mui-clearfix">
							<span class="mui-pull-left mui-col-xs-8">
								<span>${depositLog.typeNameValue}</span>
								<span class="text-gray">${depositLog.create_date?string("yyyy-MM-dd HH:mm:ss")}</span>
							</span>
							<span class="mui-pull-right mui-col-xs-4 padding-right text-pink hd-h2 text-ellipsis mui-text-right">${currency(depositLog.credit, true, true)}</span>
						</li>
						[/#list]
					[#else]
						<li class="user-list-none balance-none-tip mui-text-center">
							<img src="${base}/statics/images/bg_1.png">
							<p class="margin-top text-black hd-h5">您还没有相关的订单</p>
						</li>
					[/#if]
				</ul>
				-->
				<div class="mui-pull-bottom-pocket">
					<div class="mui-pull">
						<div class="mui-pull-loading mui-icon mui-spinner"></div>
						<div class="mui-pull-caption">上拉显示更多</div>
					</div>
				</div>
			</div>
			<div class="mui-scrollbar mui-scrollbar-vertical">
				<div class="mui-scrollbar-indicator" style="transition-duration: 0ms; display: block; height: 394px; transform: translate3d(0px, 296px, 0px) translateZ(0px);"></div>
			</div>
		</div>
		<footer class="footer posi">
		</footer>

		<div id="cli_dialog_div"></div>
		<div style="clear:both;height:.68rem;"></div>
	[#include "/wap/include/footer.ftl" /]
	</body>

</html>