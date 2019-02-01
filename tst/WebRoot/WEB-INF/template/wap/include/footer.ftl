<div class="nav-menu-1 nav-menu">

	<div class="nav-items-wrap">
		<div class="nav-items item-3">
			<div class="nav-item">
				<a class="mainmenu" href="/wap.jhtml">
				<i [#if footer??&&footer=="wap"]class="ac"[/#if]></i>
				<span class="txt">
				
				首页</span>
				</a>
			</div>
			<div class="nav-item">
				<a class="mainmenu" href="${base}/wap/goods/list.jhtml">
				<i [#if footer??&&footer=="category"]class="ac"[/#if]></i>
				<span class="txt">				
				所有商品</span>
				</a>
			</div>
			<div class="nav-item">
				<a class="mainmenu" href="${base}/wap/cart/list.jhtml"><i></i><span class="txt">购物车</span></a>
			</div>
			<div class="nav-item">
				<a class="mainmenu" href="${base}/wap/member.jhtml"><i [#if footer??&&footer=="member"]class="ac"[/#if]></i><span class="txt">
				我的</span></a>
			</div>
		</div>
	</div>
</div>