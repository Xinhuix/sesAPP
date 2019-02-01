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
<script type="text/javascript">
$().ready(function() {

	var $inputForm = $("#inputForm");
	[@flash_message /]
	
	$.validator.addMethod("compare", 
		function(value, element, param) {
			var parameterValue = $(param).val();
			if ($.trim(parameterValue) == "" || $.trim(value) == "") {
				return true;
			}
			try {
				return parseFloat(parameterValue) <= parseFloat(value);
			} catch(e) {
				return false;
			}
		},
		"${message("admin.coupon.compare")}"
	);
	
	// 表单验证
	$inputForm.validate({
		rules: {
			"o.status": "required",
		}
	});
});
</script>
</head>
<body>
	<div class="breadcrumb">
		<a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; ${message("admin.coupon.add")}
	</div>
	<form id="inputForm" action="update.jhtml" method="post">
	<input type="hidden" name="sCardorder.id" value="${o.id}" />
	<ul id="tab" class="tab">
			<li>
				<input type="button" value="${message("admin.coupon.base")}" />
			</li>
		
		</ul>
		<div class="tabContent">
			<table class="input">
				<tr>
					<th>
						<span class="requiredField">*</span>会员姓名:
					</th>
					<td>${o.member.name}</td>
				</tr>
				
				<tr>
					<th>
						<span class="requiredField">*</span>会员卡:
					</th>
					<td>${o.cardName}</td>
				</tr>
				
				<tr>
					<th>
						<span class="requiredField">*</span>时长:
					</th>
					<td>${o.number}个月</td>
				</tr>
				<tr>
					<th>
						<span class="requiredField">*</span>金额:
					</th>
					<td>${o.amount}元</td>
				</tr>				
				
				<tr>
					<th>
						<span class="requiredField">*</span>返消费卷金额:
					</th>
					<td>${o.amountVolume}元</td>
				</tr>					
				
				<tr>
					<th>
						<span class="requiredField">*</span>购买日期:
					</th>
					<td>${o.createDate}</td>
				</tr>					
				
				<tr>
					<th>
						状态:
					</th>
					<td>
						<label>
	                [#if o.status='5']
					<font color="red">已退款</font>
					[#elseif o.status='3']
					<font color="yellow">已逾期</font>
					[#else]
<input type="radio" name="sCardorder.status" value="0" [#if o.status =='0'] checked="checked"[/#if] />未支付
<input type="radio" name="sCardorder.status" value="1" [#if o.status =='1'] checked="checked"[/#if]/>已支付
<input type="radio" name="sCardorder.status" value="2" [#if o.status =='2'] checked="checked"[/#if] />已冻结
					[/#if]							
						</label>
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
					<input type="button" class="button" value="${message("admin.common.back")}" onclick="history.back(); return false;" />
				</td>
			</tr>
		</table>
	</form>
</body>
</html>
[/#escape]