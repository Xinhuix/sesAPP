[#include "/wap/include/header.ftl" /]
	
<style>
		body {
				background-color: #f5f5f5;
			}
		</style>
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
				<li>我的粮票余额：${currency(member.balance, true, true)}</li>
			</ul>
		</div>
		<div class="jifenbox">
			<ul>
				<li class="ico_1">
					<div class="jf_info">
					${cash!}积分 <br/>
					兑换粮票<br/>
					100.00元
					</div>
<a href="#" class="mui-btn  mui-btn-primary hd-h4" status="2"
style="color:#fff; line-height:.54rem; padding:0 .2rem;">
					兑换粮票</a>
				</li>
				<li class="ico_2">
					<div class="jf_info">
					${cash!}积分 <br/>
					兑换现金<br/>100.00元
					</div>
					<span style="float:right;margin-top:-6em;">* 50</span>
	<form method="post" style="float:right;margin-top:-6em;margin-right:3em;">
	<input type="button" value="+" id="add" style="width:1em;" onclick="operation('+')" />
	<input type="number" id="sum" readonly="readonly" value="1" style="width:.7rem;height:.54rem;"/>
	<input type="button" value="-" onclick="operation('-')" id="subtract" style="width:1em;"/>
</form>
<a href="#" class="mui-btn  mui-btn-primary hd-h4" status="1"
style="color:#fff; line-height:.54rem; padding:0 .2rem;margin-top:1em;">
					申请提现</a>
				</li>
			</ul>
		</div>
	</div>
	
		<script type="text/javascript">
		var  sm=document.getElementById("sum");
		var  pi=parseInt("${member.point}")/50;
	function operation(opr){
	var v=parseInt(sm.value);
			if(opr=="+"){
				sm.value=(v+1)>pi?v:v+1;
			}else{
				sm.value=v>0?v-1:0;
			}
			
		
		
	}
	
	$(function(){
		
		//兑换消费券和申请提现
		$("a.mui-btn").on("tap",function(){
			var status=$(this).attr("status");
			var alerStr="";
			
			if(status==1){
			var v=parseInt(sm.value);
			if(v<=0){ 
			alert("请正确输入提现数量");
			sm.select=true;
			return;
			}else{
				alerStr="确定要申请提现吗？";
				}
				}
			if(status==2){
				alerStr="确定要兑换消费券吗？";
				}
			if(!confirm(alerStr)){
				 return false;
			}
			
			var checkurl = "${base}/wap/member/deposit/cashing.jhtml";
			$.post(checkurl,{status:status,count:sm.value},function(ret){
				if(ret.status == 1){
						$.tips({content:ret.message}).slideUp(300).delay(800).fadeIn(400);
						window.location.reload();
				}else{
					$.tips({content:ret.message});
				}
			},'json');
		})
	})
	
	</script>
	
	
	
	[#include "/wap/include/footer.ftl" /]
	</body>

</html>