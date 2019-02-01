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
<script type="text/javascript" src="${base}/statics/js/member.order.js"></script>
<script type="text/javascript" src="${base}/statics/js/ShareSDK.js"></script>
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

 <script>
        function init()
        {
            //1、配置平台信息，有开放平台账号系统的平台需要自行去申请账号（平台字段参考：http://wiki.mob.com/社交平台配置项说明/）
            var platformConfig = {};
            
            //微信
            var weixinConf = {};
            weixinConf["app_id"] = "wx8f17d9be150fb3ef";
            weixinConf["app_secret"] = "5ca6799900062e8dbea3ffa18240294e";
            platformConfig[$sharesdk.PlatformID.WechatPlatform] = weixinConf;

            //QQ
            var qqConf = {};
            qqConf["app_id"] = "1106231389";
            qqConf["app_key"] = "j4VEZK0K8DBRD2MC";
            platformConfig[$sharesdk.PlatformID.QQPlatform] = qqConf;
            $sharesdk.initSDKAndSetPlatfromConfig("", platformConfig);
        }

       
        function showShareMenuClickHandler()
        {
            var params = {
               "text" : "分享农村农业，坐拥健康绿色。SES全国名优特产纯正共享，一次拥有，一生永久。",
                "imageUrl" : "http://ses.esgod.com/statics/seslog.png",
                "title" : "[SES共享商城]",
                "titleUrl" : 'http://ses.esgod.com/wap/register/register.jhtml?tjcode=${member.createDate?string("HHmmss")}${member.id}',
                "description" : "分享农村农业，坐拥健康绿色。SES全国名优特产纯正共享，一次拥有，一生永久。",
                "site" : "[SES共享商城]",
                "siteUrl" : 'http://ses.esgod.com/wap/register/register.jhtml?tjcode=${member.createDate?string("HHmmss")}${member.id}',
                "type" : $sharesdk.ContentType.Auto,
                "client_share" : true, //iOS 启用客户端分享接口
                "advanced_share" :true //iOS 启用微博高级分享接口 3.6.3以后版本支持
            };

            $sharesdk.showShareMenu(null, params, 100, 100, function (reqID, platform, state, shareInfo, error) {
              //alert("showShareMenuClickHandler state = " + state);
               // alert("shareInfo");
               // showLog(shareInfo);
                //alert("error");
               showLog(error);
            });
        }

     
        function showLog(obj)
        {
            for (var prop in obj)
            {
                alert("[" + prop + "]=" + obj[prop]);
                if(typeof obj[prop] == "object")
                {
                    for (var temp in obj[prop])
                    {
                        alert("["+prop+"][" + temp + "]=" + obj[prop][temp]);
                    }
                }
            }
        }

    </script>

	</head>

	<body class="mui-ios mui-ios-9 mui-ios-9-1">
		<header class="mui-bar mui-bar-nav header">
			<a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left"></a>
			<h1 class="mui-title">${title}</h1>
			<span class="mui-pull-right classify-btn">
				<a class="mui-block" href="${base}/wap/product_category.jhtml"><img src="${base}/statics/img/menu.png"></a>
			</span>
			
		</header>

		
<div class="mui-content">
		<div class="newsmain">
		<div class="padding" id="shareid">
		</div>
				<div class="content">
				<p>${tuijian!}<br/><br/></p>
		
	</div>
		</div>
	</div>
	

<script type="text/javascript">
var u = navigator.userAgent;
var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Adr') > -1; //android终端
var isiOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
$(function(){
if(isAndroid){
//alert('是否是Android：'+isAndroid);
$('#shareid').html('<a href="#" class="mui-btn full mui-btn-primary hd-h4 line78" id="share" style="color:#fff">分享给好友</a>');

				$("#share").on("tap",function(){
					window.location.href="mysharesdk:${member.createDate?string("HHmmss")}${member.id}";
				});
}
if(isiOS){
	//alert('是否是iOS：'+isiOS);
	window.onload = function(e) {  
        init();  
    }  
$('#shareid').html('<a href="javascript:void(0)"  onclick="showShareMenuClickHandler()" class="mui-btn full mui-btn-primary hd-h4 line78" id="share" style="color:#fff">分享给好友</a>');
		$("#share").on("tap",function(){
					showShareMenuClickHandler();
				});

}
})
</script>

	[#include "/wap/include/footer.ftl" /]
	</body>
</html>