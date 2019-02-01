[#include "/wap/include/header.ftl" /]

	<div class="mui-content">
	

	
	
	    <div class="all-classify">
	    [#if rootProductCategories?? && rootProductCategories?has_content]
			[#list rootProductCategories as rootProductCategory]
		    	<h3><span>${rootProductCategory.name}</span></h3>
		    	<ul class="mui-row bg-white">
			    	<li class="mui-col-xs-3">
			    		<a href="${rootProductCategory.wapPath}?productCategoryId=${rootProductCategory.id}">全部</a>
			    	</li>
			    	[#if rootProductCategory.children?? && rootProductCategory.children?has_content]
						[#list rootProductCategory.children as productCategory]
				    		<li class="mui-col-xs-3">
				    			<a href="${productCategory.wapPath}?productCategoryId=${productCategory.id}">
				    				<span>${productCategory.name}</span>
				    			</a>
				    		</li>
			    		[/#list]
			    		[#assign size = rootProductCategory.children?size + 1]
			    		[#assign count = size % 4]
			    		[#if count > 0]
			    			[#list 1..count as t]
			    			<!--  
								<li class="mui-col-xs-3">
				    				<a href="jvascript:;">&nbsp;</a>
				    			</li>
				    		-->
							[/#list]
			    		[/#if]
			    	[/#if]
		    	</ul>
	    	  [/#list]
	     [/#if]
	    </div>
	</div>
	<footer class="footer">
			<!--<div class="text-gray mui-text-center copy-text"></div>-->
		</footer>
		<script>
			$("[name=form_search]").submit(function() {
				if($("[type=search]").val() == "") {
					return false;
				}
			});
		</script>
		[#include "/wap/include/footer.ftl" /]
		<script type="text/javascript">
			mui(".nav-menu").on("tap", ".nav-item", function() {
				if($(this).hasClass("current")) {
					$(this).removeClass("current");
				} else {
					var tw = $(this).outerWidth(true);
					var ch = $(this).children(".submenu");
					var cw = ch.outerWidth(true);
					ch.css({
						left: (tw - cw) / 2 + "px"
					});
					$(".nav-menu .nav-item").removeClass("current");
					$(this).addClass("current");
				}
			});
		</script>
		<div class="nav-menu-mask"></div>
		<script>
			if($(".mui-content.mui-scroll-wrapper").length > 0) {
				$(".mui-content.mui-scroll-wrapper").css({
					bottom: "1.05rem"
				})
			}
		</script>
		<div id="cli_dialog_div"></div>
</body>
</html>
