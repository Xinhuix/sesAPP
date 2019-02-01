[#include "/wap/include/header.ftl" /]
<script type="text/javascript" src="${base}/statics/js/jfinalshop.validate.js?v=2.6.2.161028"></script>
	<div class="mui-content">
		<form action="" method="post" target="_blank" name="recharge">
			<div class="rech_notice"><img src="${base}/statics/images/ico_notice.png" />你已登录SES共享商城，请购买SES卡</div>
			
			<div class="rech_check" id="rech_check">
				<dt>请选择您的SES卡级别：</dt>	
				<div style="clear:both"></div>	
				<dl>
					
[#if scardAll??]
[#list scardAll as c]
<dd><label cardid="a${c.id}" for="cartype" [#if c_index=0] class="checked" [/#if]>
<input type="radio" name="cartype" />${c.name}</dd>
[/#list]
[/#if]
				</dl>
				<div style="clear:both"></div>
			</div>

			<div class="rech_check_month rech_check" id="rech_check_month">
				<dt>请选择您的会员时长：</dt>	
				<div style="clear:both"></div>	
				[#if scardAll??]
				[#list scardAll as a]
<dl id="a${a.id}" [#if a_index>0] style="display: none;" [/#if]>
				[#if monthcardAll??]
				[#list monthcardAll as b]
				[#if b.cardId ==a.id]
<dd >
				<label for="monthcardId" amount="${b.amount}" amountVolume="${b.amountVolume}">
<input type="radio" name="monthcardId" value="${b.id}" />
				${b.number}个月
				</label>
</dd>
				[/#if]
				[/#list][/#if]
</dl>
				[/#list][/#if]
				<div style="clear:both"></div>
			</div>

			<div class="rech_check total_money">
		     <div class="rech_numb">
				<div class="number mui-clearfix " style="display:inline-block;margin-bottom: -10px;">
					<button type="button" class="num-btn num-decrease" data-event="number">-</button>
	<input class="num-input" name="zs" id="zs" type="number" data-max="99" value="1" data-id="buy-num">
					<button type="button" class="num-btn num-increase" data-event="number">+</button>
				</div> 张
				　交纳费用：<money_v id="money_v"></money_v> 元/张
			</div>
		    <input type="hidden" value="" placeholder="请输入充值金额" name="money" id="money">
			<div style="clear:both"></div>
			</div>
			
			<div style="clear:both"></div>
			
			<div class="rech_check" style="padding:.33rem .14rem; text-align:right">
				<div class="total_price" style="float:none">
				返回消费券面值
				<p>¥<em id="money_z">0</em>元/张</p>
				</div>
				<div style="clear:both"></div>
			</div>
		

		
		<div class="agree"><input type="checkbox" checked />阅读并同意《ESE农产品平台注册协议》</div>
		
			<div class="padding bg-white border-bottom">
				
			</div>
			<ul class="pay-lists list-col-10 mui-clearfix">
				<!-- <li class="pay-list" data-paycode="ws_wap" data-paybank="">
					<label class="mui-block">
						<div class="hd-radio">
		    				<input name="radio" value="0" type="radio">
		    			</div>
						<div class="pay-icon"><img src="${base}/statics/images/ws_wap.png"></div>
						<span class="hd-h5">支付宝手机支付</span>
					</label>
				</li> -->
				[#if paymentPlugins??]
					[#list paymentPlugins as paymentPlugin]
						<li class="pay-list" data-paycode="ws_wap" data-paybank="">
							<label class="mui-block">
								<div class="hd-radio">
				    				<input name="radio" name="paymentPluginId"  value="${paymentPlugin.id}" type="radio" data-id="pay_method">
				    			</div>
								<div class="pay-icon"><img src="${paymentPlugin.logo}"></div>
									<span class="hd-h5" style="line-height:1rem">${paymentPlugin.paymentName}</span>
							</label>
						</li>
					[/#list]
				[#else]
					<li class="pay-list"> 后台暂未开启支付方式 </li>
				[/#if]
			</ul>
			<div class="margin padding-small">
				<input type="hidden" name="pay_code" value="ws_wap">
				<input type="hidden" name="pay_bank" value="">
				<!-- <input type="submit" value="确认支付" class="mui-btn mui-btn-blue full hd-h4 recharge"> -->
				<a data-id="subbtn" href="javascript:wxpay();" class="mui-btn mui-btn-blue full hd-h4 line78"style="color:#fff">提交订单并支付</a>
			</div>
		</form>
	</div>
	<footer class="footer posi">
	</footer>
	<div id="cli_dialog_div"></div>
	<div style="clear:both;height:.68rem;"></div>
	[#include "/wap/include/footer.ftl" /]
	</body>
</html>

<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.1.0.js"></script>
<script>
	$(function() {
		$("ul.pay-lists > li").eq(0).find('input').trigger("click"); // 默认选中第一个支付方式
		
	}) 
</script>
<script type="text/javascript">
	/* 微信支付 */
	function wxpay(){
		var zs = $("#zs").val();
		var monthcardId =$("input[name='monthcardId']:checked").val();
		
		var money = $("input[name=money]").val();
		if(money == '' || money.match(/^[0-9]{1}\d*(\.\d{1,2})?$/) == null) {
			$.tips({
				content: '充值金额错误'
			});
			return;
		}
		
		var paymentPluginId = $("[data-id='pay_method']:checked").val();
		if(paymentPluginId == '') {
			$.tips({
				content: '支付方式选择错误'
			});
			return;
		}
		$.post("${base}/wap/payment/submit.jhtml", {amount: money, type: "recharge", paymentPluginId: paymentPluginId,orderzs:zs,monthcardId:monthcardId}, function(res) { 
	    	if (res.code == 0) {
	    		var data = $.parseJSON(res.data);
	    		if (typeof WeixinJSBridge == "undefined") {
	    			if ( document.addEventListener ) {
	    				document.addEventListener('WeixinJSBridgeReady', onBridgeReady(data), false);
	    			} else if (document.attachEvent){
	    				document.attachEvent('WeixinJSBridgeReady', onBridgeReady(data)); 
	    				document.attachEvent('onWeixinJSBridgeReady', onBridgeReady(data));
	    			}
	    		} else {
	    			onBridgeReady(data);
	    		}
	    	} else {
	    		if (res.code == 2) {
	    			$.tips({content: res.message});
	    		} else {
	    			$.tips({content: "参数错误："+res.message});
	    		}
	    	}
	    }); 
	}
	
	function onBridgeReady(json){
		WeixinJSBridge.invoke('getBrandWCPayRequest', json, function(res){
				// 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回 ok，但并不保证它绝对可靠。
				var success = false;
				//alert(JSON.stringify(res));
				if(res.err_msg == "get_brand_wcpay_request:ok" ) {
					success = true;
					$.tips({content: '支付成功!'});
				} else {
					$.tips({content: '支付失败!'});
				}
				window.location.href = '${base}/wap/payment/result.jhtml?success=' + success + '&err_desc=' + res.err_desc;
			}
		); 
	}
	$(function(){
		$("#rech_check dd").on("click",function(){
			$(this).parent().find("label").removeClass("checked");
			$(this).find("label").addClass("checked");	
			var a=$(this).find("label").attr("cardid");
			$(".rech_check_month dl").css('display','none');
			$("#"+a).css('display','block'); 
			$("#money_v").html('');
			$("#money_z").html('');
			$("#money").val('');
		})	


		$("#rech_check_month dd").on("click",function(){
			$(this).parent().find("label").removeClass("checked");
			$(this).find("label").addClass("checked");	
			var amount=$(this).find("label").attr("amount");
			var amountVolume=$(this).find("label").attr("amountVolume");

			$("#money_v").html(amount);
			$("#money_z").html(amountVolume);
			$("#money").val(amount);

			
		})
		
	})
	$(function(){

	/* 改变数量 加减按钮 */
	$("[data-event=number]").on('tap',function(){
		// 当前购买数
		now_nums = parseInt($(this).parent(".number").find("[data-id='buy-num']").val());
		// 最大购买数
		max_nums = parseInt($(this).parent(".number").find("[data-id='buy-num']").data("max"));
		now_nums = (now_nums < 1) ? 1 : now_nums;
		now_nums = (now_nums > max_nums) ? max_nums : now_nums;
		// 更改数据库或 cookie  原来购物车的ajax提交  可以参考
		/*
		$.getJSON('/wap/cart/setNums.jhtml', {productId:$(this).parents('dd').data("skuid"), quantity:now_nums},function(ret){
			
		});
		*/
	});

	/* 改变数量 input文本 */
	$("input[data-id='buy-num']").on('keyup',function(){
		// 当前购买数
		now_nums = parseInt($(this).val());
		// 最大购买数
		max_nums = parseInt($(this).data("max"));
		now_nums = (now_nums < 1) ? 1 : now_nums;
		now_nums = (now_nums > max_nums) ? max_nums : now_nums;
		// 更改数据库或 cookie  原来购物车的ajax提交  可以参考
		/*
		$.getJSON('/wap/cart/setNums.jhtml', {productId:$(this).parents('dd').data("skuid"), quantity:now_nums},function(ret){
			
		});
		*/
	});

	

	
	
});
	$.numbers();
</script>