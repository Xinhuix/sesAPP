[#include "/wap/include/header.ftl" /]
<script type="text/javascript" src="${base}/statics/js/member.order.js"></script>
		
<div class="mui-content">
		<div class="newsmain">
				<div class="content">
				<p align="center"> 
				微信支付确认
				<br/><br/></p>
				
		<div class="padding">
		<a href="${mweb_url}" class="mui-btn full mui-btn-primary hd-h4 line78" style="color:#fff">
		    立即支付
		</a>
		<br/><br/>
		<a href="/wap/payment/notifyUrl.jhtml?out_trade_no=${out_trade_no}" class="mui-btn full mui-btn-primary hd-h4 line78" style="color:#fff">
		   已完成支付
		</a>
		</div>
	</div>
		</div>
	</div>
<!--[#include "/wap/include/footer.ftl" /]-->
	</body>
</html>