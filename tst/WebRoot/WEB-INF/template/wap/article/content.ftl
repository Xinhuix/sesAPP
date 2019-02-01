[#escape x as x?html]
<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no">
		<title>${title}</title>
		<meta name="description" content="">
		<meta name="keywords" content="">
		<link rel="stylesheet" type="text/css" href="${base}/statics/css/mui.min.css?v=2.6.0.161014">
		<link rel="stylesheet" type="text/css" href="${base}/statics/css/jfinalshop.css?v=2.6.0.161014">
		<link rel="stylesheet" type="text/css" href="${base}/statics/css/jfinalshop.mobile.css?v=2.6.0.161014">
		<script type="text/javascript" src="${base}/statics/js/mui.min.js?v=2.6.0.161014"></script>
		<script type="text/javascript" src="${base}/statics/js/jfinalshop.slider.js?v=2.6.0.161014"></script>
		<script type="text/javascript" src="${base}/statics/js/jquery-2.1.1.min.js?v=2.6.0.161014"></script>
		<script type="text/javascript" src="${base}/statics/js/jfinalshop.js?v=2.6.0.161014"></script>
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
	</head>

	<body class="mui-ios mui-ios-9 mui-ios-9-1">
		<header class="mui-bar mui-bar-nav header">
			<a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left"></a>
			<h1 class="mui-title">${article.title}</h1>
			<span class="mui-pull-right classify-btn">
				<a class="mui-block" href="${base}/wap/product_category.jhtml"><img src="${base}/statics/img/menu.png"></a>
			</span>
			<!--
			<div id="menu" class="menu bg-white">
				<div class="mui-row">
					<a class="mui-col-xs-3" href="${base}/wap.jhtml">
						<img src="${base}/statics/images/ico_11.png">fdafdsafdfadf
					</a>
					<a class="mui-col-xs-3" href="${base}/wap/product_category.jhtml">
						<img src="${base}/statics/images/ico_12.png">ȫ������
					</a>
					<a class="mui-col-xs-3" href="${base}/wap/cart/list.jhtml">
						<img src="${base}/statics/images/ico_13.png">���ﳵ
					</a>
					<a class="mui-col-xs-3" href="${base}/wap/member.jhtml">
						<img src="${base}/statics/images/ico_14.png">�û�����
					</a>
				</div>
			</div>
			-->
		</header>

	<div class="mui-content">
		<div class="newsmain">
			<h1 class="title">${article.title}[#if pageNumber > 1] (${pageNumber})[/#if]</h1>
			<div class="info">
				${message("shop.article.createDate")}: ${article.createDate?string("yyyy-MM-dd HH:mm:ss")}
				${message("shop.article.author")}: ${article.author}
				${message("shop.article.hits")}: <span id="hits">&nbsp;</span>
			</div>
			[#noescape]
				<div class="content">${article.content}</div>
			[/#noescape]
		</div>
	</div>
		<div style="clear:both;height:1.68rem;"></div>
	[#include "/wap/include/footer.ftl" /]
</body>
</html>
[/#escape]