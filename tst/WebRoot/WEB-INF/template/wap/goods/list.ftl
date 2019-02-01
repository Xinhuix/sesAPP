[#include "/wap/include/header.ftl" /]
	<style>
			body {
				background-color: #eeeeee;
			}
		</style>
	<div class="mui-content">

			<div class="mui-slider">
				<div class="mui-slider-group" style="transform: translate3d(0px, 0px, 0px) translateZ(0px);">
							[@ad_position id = 7 /]
				</div>
				<div class="mui-slider-indicator">
					<div class="mui-indicator mui-active"></div>
					<div class="mui-indicator"></div>
					<div class="mui-indicator"></div>
				</div>
			</div>
	</div>
	
	<div class="cate">
	<a href="/wap/product_category.jhtml" >全部</a>
	[#if productCategory??]<a href="#" class="active">${productCategory.name } </a>[/#if]
	[#if productCategory??]
	[@product_category_children_list product_category_id = productCategory.id recursive = false]
		[#assign filterProductCategories = productCategories /]
	[/@product_category_children_list]
	[@attribute_list product_category_id = productCategory.id]
		[#assign filterAttributes = attributes /]
	[/@attribute_list]				
	[#if filterProductCategories?has_content || filterBrands?has_content || filterAttributes?has_content]
				[#assign rows = 0 /]
				[#if filterProductCategories?has_content]								
					[#list filterProductCategories as filterProductCategory]
							[#if rows < 3 ]
							[#assign rows = rows + 1 /]	
<a href="/wap${filterProductCategory.path}">
${filterProductCategory.name}
</a>
							[/#if]
					[/#list]
				[/#if]
	[/#if]
	[/#if]
	
	
	<div class="morecate"><i></i></div>
	<div class="cateall">
		[#if productCategory??]
	[@product_category_children_list product_category_id = productCategory.id recursive = false]
		[#assign filterProductCategories = productCategories /]
	[/@product_category_children_list]

	[@attribute_list product_category_id = productCategory.id]
		[#assign filterAttributes = attributes /]
	[/@attribute_list]				
	[#if filterProductCategories?has_content || filterBrands?has_content || filterAttributes?has_content]
				[#assign rows = 0 /]
				[#if filterProductCategories?has_content]					
					[#list filterProductCategories as filterProductCategory]
							[#assign rows = rows + 1 /]	
							[#if rows > 3 ]							
<a href="/wap${filterProductCategory.path}">${filterProductCategory.name}</a>
							[/#if]
					[/#list]
				[/#if]
	[/#if]
	[/#if]
	</div>
	</div>
	
	
						
	
	
	
	<!--下拉刷新容器-->
	<div id="refreshContainer" class="mui-content" style="padding-top:0">
	  	<div class="mui-scroll" style="clear:both;padding-bottom:.9rem;">
	  		<div class="has-scorll-top"></div>
			<!--数据列表-->
	  		<ul id="goods-lists" class="margin-top custom-goods-items  mui-clearfix ">
	  		[#if pages?? && pages.list?has_content]
	  			[#list pages.list as goods]
	  				[#assign defaultProduct = goods.defaultProduct /]
		  			<li class="goods-item-list">
						<div class="chandi">${goods.origin}</div>
						
							<div class="list-item-pic">
								<a class="list-item" href="${base}/wap/goods/detail.jhtml?id=${goods.id}">
								<div class="square-item">
<img src="${goods.image!setting.defaultThumbnailProductImage}" class="lazy" data-original="${goods.thumbnail!setting.defaultThumbnailProductImage}" style="display: block;">
								
								</div>
								</a>
								<!--<div class="gmethod"><span>管理方式：网站视频直播</span></div>-->
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
${abbreviate(goods.name, 28)} 
</span>
									</div>
								[#else]
									<div class="list-item-title">${abbreviate(goods.name, 48)}</div>
								[/#if]

<div class="list-item-text"><span class="price-org">${currency(defaultProduct.price, true)}</span>
<!--起定数量：${goods.number}-->
</div>
<div class="list-item-text2">规格类型：<!--  660平米/亩/300公斤-->
</div>
<div class="list-item-text3"> 
[#list goods.products as product]
[#if product_index<1]
	  已售：${goods.sales}&nbsp剩余: ${product.stock}
[/#if]
[/#list]
<a href="${goods.onlineurl}" class="zhibo">
[#if "${goods.is_dz}" == "1"]
产地
[#else]
商城
[/#if]
直播
</a></div>	
							</div>
					</li>
[/#list]
			[/#if]
			</ul>
		</div>
		<div style="clear:both;height:.68rem;"></div>
	</div>
[#include "/wap/include/footer.ftl" /]
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
		
		$(".morecate").on('tap',function(){
		
			if($(this).find("i").hasClass("open")){
				$(".cateall").fadeOut();
				$(this).find("i").removeClass("open");
			}else{					
				$(".cateall").fadeIn();
				$(this).find("i").addClass("open");
			}		
		
		})
		
		
		
	})
	
</script>
</body>
</html>
