[#include "/wap/include/header.ftl" /]
	<div class="mui-content">
	    <ul class="mui-table-view layout-list-common comment-form margin-none">
	    	<li class="padding border-bottom mui-text-center">
	    		<span class="service-apply-ok"></span>
	    		<h2 class="hd-h3 margin-tb strong">${title}</h2>
	    	</li>
	    	<li class="padding">
	    		<span class="hd-h5">订单号：<em class="text-org">${order.sn}</em></span>
	    	</li>
	    	<li class="padding">
	    		<span class="hd-h5">支付金额：<em class="text-org">${order.amountPaid}</em></span>
	    	</li>
	    </ul>
	    <div class="padding" style="text-align:center;">
	    	<a href="${base}/wap/member/order/list.jhtml" class="mui-btn  mui-btn-primary hd-h4 line78" style="color:#fff; width:40%; margin:.2rem; border:solid 1px #259b24">查看订单</a><a href="${base}/wap.jhtml" class="mui-btn  mui-btn-primary hd-h4 line78" style="color:#fff;width:40%; margin:.2rem; color:#259b24; background-color:#fff; border:solid 1px #259b24">继续购物</a>
	    </div>
	</div>
	<footer class="footer posi">
		<div class="mui-text-center copy-text">
			<span></span>
		</div>
	</footer>
	<div style="clear:both;height:.68rem;"></div>
	[#include "/wap/include/footer.ftl" /]
</body>
</html>
