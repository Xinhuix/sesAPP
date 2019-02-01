[#escape x as x?html]
[#include "/wap/include/header.ftl" /]
<style>
			body {
				background-color: #eeeeee;
			}
		</style>
	<div class="mui-content">

			<div class="mui-slider">
				<div class="mui-slider-group" style="transform: translate3d(0px, 0px, 0px) translateZ(0px);">
							<a class="mui-slider-item" href="/promotion/content/2.jhtml"><img src="/upload/image/201706/0bfa56cb-36ed-4294-83cf-b7cb9271c7d8.jpg"></a>
							<a class="mui-slider-item" href="#"><img src="/upload/image/201706/09ca1620-ecfc-425a-b454-c748a7b6859f.jpg"></a>
							<a class="mui-slider-item" href="#"><img src="/upload/image/201706/73ef86ab-867e-430e-b7d9-6fff656b6cd5.jpg"></a>
				</div>
				<div class="mui-slider-indicator">
					<div class="mui-indicator mui-active"></div>
					<div class="mui-indicator"></div>
					<div class="mui-indicator"></div>
				</div>
			</div>

		[@article_category_root_list count = 10]
			<div class="hotArticleCategory">
				<dl>					
					[#list articleCategories as articleCategory]
						<dd>
							<a href="${base}/wap${articleCategory.path}">${articleCategory.name}</a>
						</dd>
					[/#list]
				</dl>
			</div>
		[/@article_category_root_list]
		
		
		<form id="articleForm" action="${base}${articleCategory.path}" method="get">
		<input type="hidden" id="pageNumber" name="pageNumber" value="${page.pageNumber}" />
		
			<div id="newslist" class="result">
						
					
		[#if page.list?has_content]
							<ul>
								[#list page.list as article]
									<li>
	<a href="${base}/wap/article/detail.jhtml?id=${article.id}" title="${article.title}">${abbreviate(article.title, 80, "...")}</a>
										
										<p class="time" title="${article.createDate?string("yyyy-MM-dd HH:mm:ss")}">${article.createDate}</p>
										<p>${abbreviate(article.text,90, "...")}</p>
									</li>
								[/#list]
							</ul>
						[#else]
							[#noescape]
							<div class="padding">	${message("shop.article.noListResult")}</div>
							[/#noescape]
						[/#if]
			
			</div>
			</form>
	
		
	
		
	</div>





	<div style="clear:both;height:.68rem;"></div>
	[#include "/wap/include/footer.ftl" /]
</body>
</html>
[/#escape]