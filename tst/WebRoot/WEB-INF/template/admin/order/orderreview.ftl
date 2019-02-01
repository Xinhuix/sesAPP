[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>${message("admin.order.edit")} - Powered By JFinalShop</title>
<meta name="author" content="JFinalShop Team" />
<meta name="copyright" content="JFinalShop" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.tools.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/webuploader.js"></script>
<script type="text/javascript" src="${base}/resources/admin/ueditor/ueditor.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/input.js"></script>
<script type="text/javascript">
$().ready(function() {
	var $introduction = $("#introduction");
	$introduction.editor();
	
	[@flash_message /]

});
</script>
</head>
<body>
	<div class="breadcrumb">
		<a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; ${message("admin.order.edit")}
	</div>
	<ul id="tab" class="tab">
		<li>
			<input type="button" value="${message("admin.order.orderInfo")}" />
		</li>
	</ul>
	<form id="inputForm" action="orderreviewSubmint.jhtml" method="post">
		<input type="hidden" name="id" value="${order.id}" />
		<table class="input tabContent">
			<tr>
				<th>
					${message("Order.sn")}:
				</th>
				<td width="360">
					${order.sn}
				</td>
				<th>
					${message("admin.common.createDate")}:
				</th>
				<td>
					${order.createDate?string("yyyy-MM-dd HH:mm:ss")}
				</td>
			</tr>
			<tr>
				<th>
					${message("Order.type")}:
				</th>
				<td>
					${message("Order.Type." + order.typeName)}
				</td>
				<th>
					${message("Order.status")}:
				</th>
				<td>
					${message("Order.Status." + order.statusName)}
					[#if order.hasExpired()]
						<span class="silver">(${message("admin.order.hasExpired")})</span>
					[#else]
						[#if order.expire??]
							<span class="silver">(${message("Order.expire")}: ${order.expire?string("yyyy-MM-dd HH:mm:ss")})</span>
						[/#if]
					[/#if]
				</td>
			</tr>
			<tr>
				<th>
					${message("Order.member")}:
				</th>
				<td>
					<a href="../member/view.jhtml?id=${order.member.id}">${order.member.username}</a>
				</td>
				<th>
					${message("Member.memberRank")}:
				</th>
				<td>
					${order.member.memberRank.name}
				</td>
			</tr>
			<tr>
				<th>
					${message("Order.price")}:
				</th>
				<td>
					${currency(order.price, true)}
				</td>
				<th>
					${message("Order.exchangePoint")}:
				</th>
				<td>
					${order.exchangePoint}
				</td>
			</tr>
			<tr>
				<th>
					${message("Order.amount")}:
				</th>
				<td>
					<span id="amount" class="red">${currency(order.amount, true)}</span>
				</td>
				<th>
					${message("Order.amountPaid")}:
				</th>
				<td>
					${currency(order.amountPaid, true)}
					[#if order.amountPayable > 0]
						<span class="silver">(${message("Order.amountPayable")}: ${currency(order.amountPayable, true)})</span>
					[/#if]
				</td>
			</tr>
			<tr>
				<th>
					${message("Order.weight")}:
				</th>
				<td>
					${order.weight}
				</td>
				<th>
					${message("Order.quantity")}:
				</th>
				<td>
					${order.quantity}
				</td>
			</tr>
			<tr>
				<th>
					${message("admin.order.promotion")}:
				</th>
				<td>
					[#if order.promotionNamesConverter?has_content]
						${order.promotionNamesConverter?join(", ")}
					[#else]
						-
					[/#if]
				</td>
				<th>
					${message("Order.promotionDiscount")}:
				</th>
				<td>
					${currency(order.promotionDiscount, true)}
				</td>
			</tr>
			<tr>
				<th>
					${message("admin.order.coupon")}:
				</th>
				<td>
					${(order.couponCode.coupon.name)!"-"}
				</td>
				<th>
					${message("Order.couponDiscount")}:
				</th>
				<td>
					${currency(order.couponDiscount, true)}
				</td>
			</tr>
			<tr>
				<th>
					${message("Order.fee")}:
				</th>
				<td>
					${currency(order.fee, true)}
				</td>
				<th>
					${message("Order.freight")}:
				</th>
				
			</tr>
		
		
			<tr>
					<th>订单追溯:</th>
					<td>
<textarea id="introduction" name="review"  class="editor" style="width: 100%;">
					${order.review}</textarea>
					</td>
				</tr>
			
		</table>
		<table class="item tabContent">
			<tr>
				<th>
					${message("OrderItem.sn")}
				</th>
				<th>
					${message("OrderItem.name")}
				</th>
				<th>
					${message("OrderItem.price")}
				</th>
				<th>
					${message("OrderItem.quantity")}
				</th>
				<th>
					${message("OrderItem.subtotal")}
				</th>
			</tr>
			[#list order.orderItems as orderItem]
				<tr>
					<td>
						${orderItem.sn}
					</td>
					<td width="400">
						[#if orderItem.product??]
							<a href="${orderItem.product.url}" title="${orderItem.name}" target="_blank">${abbreviate(orderItem.name, 50, "...")}</a>
						[#else]
							<span title="${orderItem.name}">${abbreviate(orderItem.name, 50, "...")}</span>
						[/#if]
						[#if orderItem.specificationsConverter?has_content]
							<span class="silver">[${orderItem.specificationsConverter?join(", ")}]</span>
						[/#if]
						[#if orderItem.typeName != "general"]
							<span class="red">[${message("Goods.Type." + orderItem.typeName)}]</span>
						[/#if]
					</td>
					<td>
						[#if orderItem.typeName == "general"]
							${currency(orderItem.price, true)}
						[#else]
							-
						[/#if]
					</td>
					<td>
						${orderItem.quantity}
					</td>
					<td>
						[#if orderItem.typeName == "general"]
							${currency(orderItem.subtotal, true)}
						[#else]
							-
						[/#if]
					</td>
				</tr>
			[/#list]
		</table>
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