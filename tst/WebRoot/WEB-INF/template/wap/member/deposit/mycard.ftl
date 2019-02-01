[#include "/wap/include/header.ftl" /]
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
			
	<div class="cardlist">
			<ul>
					[#if pages?? && pages?has_content]
						[#list pages as cardorder]
				<li>
					<div class="card"  style="background-image:url(/statics/images/card${cardorder.card.type}.png)">
					[#if cardorder.status='4']
							<span  style="border:5px #ff5809 solid;color:#ff9d6f;margin:20px;border-radius:10px;font-weight:bold;font-family:fantasy;padding:5px;">
								退卡中
							</span>
						[/#if]
							[#if cardorder.status='1']
							<span  style="border:5px #fff solid;color:#fff;border-radius:10px;text-align:center;font-family:fantasy;padding:5px;">
								生效中
							</span>
						[/#if]
						[#if cardorder.status='6']
							<span  style="border:5px #fff solid;color:#fff;margin:20px;border-radius:10px;font-family:fantasy;padding:5px;">
								生效中
							</span>
						[/#if]
						<div class="cardno card1">
						${cardorder.create_date?string("HHmmss")}
						</div>
						<div class="limittime">有效时长：
						${cardorder.create_date?string("yyyy/MM/dd")}
						——${cardorder.end_date}(${cardorder.number}个月)
						</div>
						
					</div>
					<div class="cotrol">
<font style="line-height:.8rem; font-size:.25rem; float:left">×${cardorder.ksl}张</font>
[#if cardorder.status='3']  
 <font color="red" style="line-height:.8rem">已到期</font>						
<a href="javascript:;" orderId="${cardorder.id}" status="6"
class="mui-btn mui-btn-primary hd-h4" style="color:#fff;  margin:.2rem; border:solid 1px #259b24;padding:0 .1rem;">
	续期</a>
<a href="javascript:;" orderId="${cardorder.id}" status="4"
class="mui-btn  mui-btn-primary hd-h4" style="color:#fff;padding:0 .1rem; margin:.2rem; color:#259b24; background-color:#fff; border:solid 1px #259b24">
					退卡
</a>	
[/#if]
					</div>
				</li>
				[/#list]
					[#else]
						<li class="user-list-none balance-none-tip mui-text-center">
							<img src="${base}/statics/images/bg_1.png">
							<p class="margin-top text-black hd-h5">您还没有相关的订单</p>
						</li>
					[/#if]
			</ul>
<div class="padding" style="text-align:center;">
<p><a href="${base}/wap/member/deposit/recharge.jhtml" class="mui-btn1 mui-btn-primary hd-h4 line78" style="width:50%; margin:0 auto;color:#fff; display:inline-block;">
			继续办理用户</a></p>
</div>
	</div>

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
		
	<script type="text/javascript">
	$(function(){
	
		
		//退卡和续订
		$("a.mui-btn").on("tap",function(){
			var status=$(this).attr("status");
			var alerStr="";
			if(status==4){
				alerStr="确定要退卡吗？"
				}
			if(status==6){
				alerStr="确定要续期吗？"
				}
			if(!confirm(alerStr)){
				 return false;
			}
			

			var orderId=$(this).attr("orderId");
			var checkurl = "${base}/wap/member/deposit/tkAndxd.jhtml";
			$.post(checkurl,{status:status,param:orderId},function(ret){
				if(ret.status == 1){
						$.tips({content:ret.message});
				}else if(ret.status==2){
						$.tips({content:ret.message});
				}else{
					$.tips({content:ret.message});
				}
			},'json');


		})
	
	
	})
	
	</script>	
		
		<footer class="footer posi">
		</footer>

		<div id="cli_dialog_div"></div>
		<div style="clear:both;height:.68rem;"></div>
	[#include "/wap/include/footer.ftl" /]
	</body>

</html>