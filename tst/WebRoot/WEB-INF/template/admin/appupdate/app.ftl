[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>${message("admin.theme.setting")} - Powered By JFinalShop</title>
<meta name="author" content="JFinalShop Team" />
<meta name="copyright" content="JFinalShop" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/input.js"></script>
<style type="text/css">
.theme li {
	width: 120px;
	height: 150px;
	float: left;
	margin: 0px 20px 0px 2px;
	text-align: center;
}

.theme img {
	width: 120px;
	height: 120px;
	filter: alpha(opacity = 60);
	opacity: 0.6;
	padding: 2px;
	-webkit-box-shadow: 2px 2px 2px rgba(0, 0, 0, 0.1);
	-moz-box-shadow: 2px 2px 2px rgba(0, 0, 0, 0.1);
	box-shadow: 2px 2px 2px rgba(0, 0, 0, 0.1);
	border: 1px solid #cccccc;
	background-color: #ffffff;
}

.theme img.current {
	filter: alpha(opacity = 100);
	opacity: 1;
}
</style>
<script type="text/javascript">
$(function(){
    $(":checkbox").click(function(){
//设置当前选中checkbox的状态为checked
    $(this).attr("checked",true);
    $(this).siblings().attr("checked",false); //设置当前选中的checkbox同级(兄弟级)其他checkbox状态为未选中
});
});

	/* function Upload(){
	   	 if (confirm("你确定要删除吗?")) {  
	   		 $.ajax({
	   				url: "${pageContext.request.contextPath}/AppUpdate/addappversion.jhtml",
	   				type: "POST",
	   				dataType: "json",
	   				cache: false,
	   				success: function(message) {
	   					if (message.status == "1") {
	   						setTimeout(function() {
	   							location.reload(true);
	   						}, 1000);
	   					}
	   				},error: function(message){
	   					console.log(message);
	   				}
	   			}); 
	        		}  
	        else {  
	       	 console.log("取消");
	        }  
	   	} */		
/* 	function Upload(){
        var form = new FormData(document.getElementById("tf"));
        $.ajax({
            url:"${pageContext.request.contextPath}/AppUpdate/addappversion.jhtml",
            type:"post",
            data:form,
            processData:false,
            contentType:false,
            success:function(data){
                window.clearInterval(timer);
                console.log("over..");
            },
            error:function(e){
                alert("错误！！");
                window.clearInterval(timer);
            }
        });        
        get();
    } */
function delButton(id){
   	 if (confirm("你确定要删除吗?")) {  
   		 $.ajax({
   				url: "${pageContext.request.contextPath}/AppUpdate/deleteaddversion.jhtml",
   				type: "POST",
   				data: 'id='+id,
   				dataType: "json",
   				cache: false,
   				success: function(message) {
   					if (message.status == "1") {
   						setTimeout(function() {
   							location.reload(true);
   						}, 1000);
   					}
   				},error: function(message){
   					console.log(message);
   				}
   			}); 
        		}  
        else {  
       	 console.log("取消");
        }  
   	}
</script>
</head>
<body>
	<div class="breadcrumb">
		<a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; APP更新
	</div>
	<form id="tf" action="addappversion.jhtml" method="post" enctype="multipart/form-data"><!--  -->
		<table class="input">
			<tr>
				<th>
					文件上传
				</th>
				<td>
					<input type="file" name="file"  multiple />
				</td>
			</tr>
			<tr>
				<th>
					版本
				</th>
				<td>
				<input type="text" name="version" class="text" placeholder="要大于最前一条的版本号" maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					是否强制更新？
				</th>
				<td>
					<input type="checkbox" name="type" value="1" />是
					<input type="checkbox" name="type" value="0" checked="checked"/>否
				</td>
			</tr>
			<tr>
				<th>
					&nbsp;
				</th>
				<td>
					<input type="submit" class="button" value="${message("admin.common.submit")}" onclick="Upload();"/>
					<input type="button" class="button" value="${message("admin.common.back")}"  /> <!-- onclick="history.back(); return false;" -->
				</td>
			</tr>
		</table>
	</form>
	
	<table class="input tabContent">
        <tr style="background-color: #63B8FF;">
		<td>上传时间</td>
		<td>版本号</td>
		<td>修改时间</td>
		<td>是否强制更新</td>
		<td>操作</td>
		</tr>
     [#list version as version]
     	<tr style="margin-left: 20px;">
        <td style="font-size: 13px;color:red">${version.createDate?string("yyyy-MM-dd HH:mm:ss")}</td>
        <td style="font-size: 13px;">${version.versionnumber}</td>
        <td style="font-size: 13px;">${version.updateDate?string("yyyy-MM-dd HH:mm:ss")}</td>
        <td style="font-size: 13px;">
		<font color="red">[#if version.type== 0 ]
		否
		[#else]
		是
		[/#if]
		</font>
		</td>
		<td style="font-size: 13px;"> <input type="button" id="delButton" onclick="delButton(${version.id});" class="button" value="${message("admin.common.delete")}" /></a></td>
		</tr>
    [/#list]
	</table>
</body>
</html>
[/#escape]