[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title> </title>
<meta name="author" content="JFinalShop Team" />
<meta name="copyright" content="JFinalShop" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.tools.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/admin/ueditor/ueditor.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/input.js"></script>
<script type="text/javascript" src="${base}/resources/admin/datePicker/WdatePicker.js"></script>
<script type="text/javascript" src="/js/messages_zh.min.js"></script>
<script type="text/javascript">
$("inputForm").validate({
	submitHandler:function(form){
		form.submit();
	}
});
$().ready(function(){
	  $("#inputForm").validate({
	  	 rules:{
	  	 	"sCard.name":"required",
	  	 	"sCard.type":"required",
	  	 	"sCard.limitnum":{
	  	 		required:"#lim:checked",
	  	 		digits:true
	  	 	}
	  	 },messages:{
	  	 	  "sCard.name":"请输入卡名称",
	  	 	  "sCard.type":"请输入级别",
	  	 	  "sCard.limitnum":{
	  	 	  	required:"请输入要限购的数量",
	  	 	  	digits:"请输入数字"
	  	 	  }
	  	 }
	  });
});

</script>
</head>
<body>
	<div class="breadcrumb">
		<a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; 
		会员卡
	</div>
	<form id="inputForm" action="save.jhtml" method="post">
		<ul id="tab" class="tab">
			<li>
				<input type="button" value="${message("admin.coupon.base")}" />
			</li>
		
		</ul>
		<div class="tabContent">
			<table class="input">
				<tr>
					<th>
						<span class="requiredField">*</span>卡名称:
					</th>
					<td>
						<input type="text" name="sCard.name" class="text" maxlength="200" />
					</td>
				</tr>
				<tr>
					<th>
						<span class="requiredField">*</span>级别:
					</th>
					<td>
						<input type="text" name="sCard.type" class="text" maxlength="200" placeholder="数值大级别高"/>
					</td>
				</tr>
				<tr>
					<th>
						状态:
					</th>
					<td>
						<label>
						<input type="radio" name="sCard.status" value="0" checked="checked" />禁用
						<input type="radio" name="sCard.status" value="1"  />使用
						</label>
					</td>
				</tr>
				<tr>
				<th>是否限购</th>
					<td>
							<input type="radio" name="sCard.islimit" value="0" checked="checked"/>不限购
							<input type="radio" name="sCard.islimit"  id="lim" value="1" />限购
					</td>
				</tr>
				<tr>
					<th>限购数量</th>
					<td>
						  <input type="text"  class="text"  name="sCard.limitnum"  placeholder="请输入限购数量" />
					</td>
				</tr>
			</table>
		</div>
		
		<table class="input">
			<tr>
				<th>
					&nbsp;
				</th>
				<td>
					<input type="submit" class="button" value="${message("admin.common.submit")}" />
					<input type="button"   class="button"    value="${message("admin.common.back")}"   onclick="history.back(); return false;" />
				</td>
			</tr>
		</table>
	</form>
</body>
</html>
[/#escape]