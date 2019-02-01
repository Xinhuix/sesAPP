[#include "/wap/include/header.ftl" /]
   
	<!--下拉刷新容器-->
	<div id="refreshContainer" class="mui-content" style="padding-bottom:.68rem" >
	  	<div class="mui-scroll">
	  		<div class="has-scorll-top"></div>
			<div class="searchbox">
				<form name="" action="${base}/wap/goods/search.jhtml" method="get">
				<div class="scform">
					<input  type="text" name="keyword" placeholder="搜索${list_str!}等" />
				</form>
				<dd>热门搜索：
				[#if list_str?? && list_str?has_content]
				[#list list_str as liststr]
				<a href="${base}/wap/goods/search.jhtml?keyword=${liststr!}">${liststr!}</a> 
				[/#list]
				[/#if]
				</dd>				
				</div>
			</div>
			
			
			
			<!--数据列表-->
	  		<ul id="goods-lists" class="margin-top custom-goods-items  mui-clearfix ">
	  			[#if goodsList?? && goodsList?has_content]
	  				[#list goodsList as goods]
		  				<li class="goods-item-list">
						<div class="chandi">${goods.origin}</div>
						
							<div class="list-item-pic">
								<a class="list-item" href="${base}/wap/goods/detail.jhtml?id=${goods.id}">
								<div class="square-item">
								<img src="${goods.image!setting.defaultThumbnailProductImage}" class="lazy" data-original="${goods.thumbnail!setting.defaultThumbnailProductImage}" style="display: block;">
								</div>
								</a>
								<!--<div class="gmethod"><span> 管理方式：网站视频直播</span></div>-->
							</div>
							<div class="list-item-bottom">
								[#if goods.caption?has_content]
									<div class="list-item-title">
<span title="${goods.name}">
[#if "${goods.is_dz}" == "1"]
<i class="dz"></i>
[#else]
<i class="xh"></i>
[/#if] 
${abbreviate(goods.name, 28)}</span>
									</div>
								[#else]
									<div class="list-item-title">${abbreviate(goods.name, 48)}</div>
								[/#if]

<div class="list-item-text"><span class="price-org">${currency(goods.price, true)}</span>
	<!--起定数量：${goods.number}-->
	</div>
	<div class="list-item-text2"></div>
	<div class="list-item-text3">
<!--
<div class="list-item-text2">规格类型：</div>
	<div class="list-item-text3">
[#list goods.products as product]
已售：${product.allocated_stock}　剩余 ${product.stock}
[/#list]
-->
<a href="${goods.onlineurl!}" class="zhibo">
[#if "${goods.is_dz}" == "1"]
产地
[#else]
商城
[/#if]
直播</a></div>
							</div>
						
					</li>
	  				[/#list]
	  			[#else]
		  			<li class="user-list-none mui-text-center">
			
					<img src="${base}/statics/images/bg_6.png">
						<p class="text-black hd-h4">
							没有搜索到商品
						</p>
					</li>
	  			[/#if]
			</ul>
			<div style="clear:both;height:.68rem;"></div>
		</div>
	</div>
	<script>
		$(function(){
			mui(".filter-items").on('tap','.filter-more',function(){
				if($(".filter-wrap").hasClass("open")){
					$(".filter-wrap").hide(0,function(){
						$(".filter-wrap").removeClass("open");
					});
				}else{
					$(".filter-wrap").show(0,function(){
						$(".filter-wrap").addClass("open");
					});
				}
			});
		})
		
	</script>
	
	[#include "/wap/include/footer.ftl" /]
</body>
</html>
