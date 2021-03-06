[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>${message("admin.order.view")} - Powered By JFinalShop</title>
<meta name="author" content="JFinalShop Team" />
<meta name="copyright" content="JFinalShop" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.tools.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.lSelect.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/input.js"></script>
<style type="text/css">
	.shipping, .returns {
		height: 380px;
		overflow-x: hidden;
		overflow-y: auto;
	}
	
	.shipping .item, .returns .item {
		margin: 6px 0px;
	}
	
	.transitSteps {
		height: 240px;
		line-height: 28px;
		padding: 0px 6px;
		overflow-x: hidden;
		overflow-y: auto;
	}
	
	.transitSteps th {
		width: 150px;
		color: #888888;
		font-weight: normal;
		text-align: left;
	}
	
	
</style>
 <style>
	 
	 .Hotspot{
            width: 100%;
            height: 40px;
            border-top-left-radius: 10px;
            border-top-right-radius: 10px;
            margin: 0px auto;
            margin-top: 10px;
            background-color: #E4E4E4;
            box-shadow: 1px 1px 5px 3px #D3D3D3;
        }
        .Hotspot_1{
            padding-top: 8px;
            padding-left: 20px;
            font-size: 15px;
            color: red;
        }
	 </style>
<script type="text/javascript">
$().ready(function() {

	var $reviewForm = $("#reviewForm");
	var $passed = $("#passed");
	var $receiveForm = $("#receiveForm");
	var $completeForm = $("#completeForm");
	var $failForm = $("#failForm");
	var $reviewButton = $("#reviewButton");
	var $paymentButton = $("#paymentButton");
	var $refundsButton = $("#refundsButton");
	var $shippingButton = $("#shippingButton");
	var $returnsButton = $("#returnsButton");
	var $receiveButton = $("#receiveButton");
	var $completeButton = $("#completeButton");
	var $failButton = $("#failButton");
	var $transitStep = $("a.transitStep");
	var isLocked = false;
	
	[@flash_message /]
	
	// 检查锁定
	function checkLock() {
		if (!isLocked) {
			$.ajax({
				url: "checkLock.jhtml",
				type: "POST",
				data: {id: ${order.id}},
				dataType: "json",
				cache: false,
				success: function(message) {
					if (message.type != "success") {
						$.message(message);
						$reviewButton.add($paymentButton).add($refundsButton).add($shippingButton).add($returnsButton).add($receiveButton).add($completeButton).add($failButton).prop("disabled", true);
						isLocked = true;
					}
				}
			});
		}
	}
	
	// 检查锁定
	checkLock();
	setInterval(function() {
		checkLock();
	}, 50000);
	
	[#if !order.hasExpired() && order.statusName == "pendingReview"]
		// 审核
		$reviewButton.click(function() {
			var $this = $(this);
			$.dialog({
				type: "warn",
				content: "${message("admin.order.reviewConfirm")}",
				ok: "${message("admin.common.true")}",
				cancel: "${message("admin.common.false")}",
				onOk: function() {
					$passed.val("true");
					$reviewForm.submit();
					return false;
				},
				onCancel: function() {
					$passed.val("false");
					$reviewForm.submit();
					return false;
				}
			});
		});
	[/#if]
	
	
	
	
	// 收款
	$paymentButton.click(function() {
		$.dialog({
			title: "${message("admin.order.payment")}",
			content: 
				[@compress single_line = true]
					'<form id="paymentForm" action="payment.jhtml" method="post">
						<input type="hidden" name="token" value="${token}" \/>
						<input type="hidden" name="orderId" value="${order.id}" \/>
						<table class="input">
							<tr>
								<th>
									${message("Order.sn")}:
								<\/th>
								<td width="300">
									${order.sn}
								<\/td>
								<th>
									${message("admin.common.createDate")}:
								<\/th>
								<td>
									${order.createDate?string("yyyy-MM-dd HH:mm:ss")}
								<\/td>
							<\/tr>
							<tr>
								<th>
									${message("Order.amount")}:
								<\/th>
								<td>
									${currency(order.amount, true)}
								<\/td>
								<th>
									${message("Order.amountPayable")}:
								<\/th>
								<td>
									${currency(order.amountPayable, true)}
								<\/td>
							<\/tr>
							<tr>
								<th>
									${message("Payment.bank")}:
								<\/th>
								<td>
									<input type="text" name="payment.bank" class="text" maxlength="200" \/>
								<\/td>
								<th>
									${message("Payment.account")}:
								<\/th>
								<td>
									<input type="text" name="payment.account" class="text" maxlength="200" \/>
								<\/td>
							<\/tr>
							<tr>
								<th>
									${message("Payment.amount")}:
								<\/th>
								<td>
									<input type="text" name="payment.amount" class="text"[#if order.amountPayable > 0] value="${order.amountPayable}"[/#if] maxlength="16" \/>
								<\/td>
								<th>
									${message("Payment.payer")}:
								<\/th>
								<td>
									<input type="text" name="payment.payer" class="text" maxlength="200" \/>
								<\/td>
							<\/tr>
							<tr>
								<th>
									${message("Payment.method")}:
								<\/th>
								<td>
									<select name="method">
										[#list methods as method]
											<option value="${method}">${message("Payment.Method." + method)}<\/option>
										[/#list]
									<\/select>
								<\/td>
								<th>
									${message("Payment.paymentMethod")}:
								<\/th>
								<td>
									<select name="paymentMethodId">
										<option value="">${message("admin.common.choose")}<\/option>
										[#list paymentMethods as paymentMethod]
											[#noescape]
												<option value="${paymentMethod.id}">${paymentMethod.name?html?js_string}<\/option>
											[/#noescape]
										[/#list]
									<\/select>
								<\/td>
							<\/tr>
							<tr>
								<th>
									${message("Payment.memo")}:
								<\/th>
								<td colspan="3">
									<input type="text" name="payment.memo" class="text" maxlength="200" \/>
								<\/td>
							<\/tr>
							<tr>
								<td colspan="4" style="border-bottom: none;">
									&nbsp;
								<\/td>
							<\/tr>
						<\/table>
					<\/form>'
				[/@compress]
			,
			width: 900,
			modal: true,
			ok: "${message("admin.dialog.ok")}",
			cancel: "${message("admin.dialog.cancel")}",
			onShow: function() {
				var $amount = $("#paymentForm input[name='payment.amount']");
				var $method = $("#paymentForm select[name='method']");
				$.validator.addMethod("balance", 
					function(value, element, param) {
						return this.optional(element) || $method.val() != "deposit" || parseFloat(value) <= parseFloat(param);
					},
					"${message("admin.order.insufficientBalance")}"
				);
				$("#paymentForm").validate({
					rules: {
						"payment.amount": {
							required: true,
							positive: true,
							decimal: {
								integer: 12,
								fraction: ${setting.priceScale}
							},
							balance: ${order.member.balance}
						}
					},
					submitHandler: function(form) {
						if (parseFloat($amount.val()) <= ${order.amountPayable} || confirm("${message("admin.order.paymentConfirm")}")) {
							form.submit();
						}
					}
				});
			},
			onOk: function() {
				$("#paymentForm").submit();
				return false;
			}
		});
	});
	
	[#if order.refundableAmount > 0]
		// 退款
		$refundsButton.click(function() {
			$.dialog({
				title: "${message("admin.order.refunds")}",
				content: 
					[@compress single_line = true]
						'<form id="refundsForm" action="refunds.jhtml" method="post">
							<input type="hidden" name="token" value="${token}" \/>
							<input type="hidden" name="orderId" value="${order.id}" \/>
							<table class="input">
								<tr>
									<th>
										${message("Order.sn")}:
									<\/th>
									<td width="300">
										${order.sn}
									<\/td>
									<th>
										${message("admin.common.createDate")}:
									<\/th>
									<td>
										${order.createDate?string("yyyy-MM-dd HH:mm:ss")}
									<\/td>
								<\/tr>
								<tr>
									<th>
										${message("Order.amount")}:
									<\/th>
									<td>
										${currency(order.amount, true)}
									<\/td>
									<th>
										${message("Order.refundableAmount")}:
									<\/th>
									<td>
										${currency(order.refundableAmount, true)}
									<\/td>
								<\/tr>
								<tr>
									<th>
										${message("Refunds.bank")}:
									<\/th>
									<td>
										<input type="text" name="refunds.bank" class="text" maxlength="200" \/>
									<\/td>
									<th>
										${message("Refunds.account")}:
									<\/th>
									<td>
										<input type="text" name="refunds.account" class="text" maxlength="200" \/>
									<\/td>
								<\/tr>
								<tr>
									<th>
										${message("Refunds.amount")}:
									<\/th>
									<td>
										<input type="text" name="refunds.amount" class="text" value="${order.refundableAmount}" maxlength="16" \/>
									<\/td>
									<th>
										${message("Refunds.payee")}:
									<\/th>
									<td>
										<input type="text" name="refunds.payee" class="text" maxlength="200" \/>
									<\/td>
								<\/tr>
								<tr>
									<th>
										${message("Refunds.method")}:
									<\/th>
									<td>
										<select name="method">
											[#list refundsMethods as refundsMethod]
												<option value="${refundsMethod}">${message("Refunds.Method." + refundsMethod)}<\/option>
											[/#list]
										<\/select>
									<\/td>
									<th>
										${message("Refunds.paymentMethod")}:
									<\/th>
									<td>
										<select name="paymentMethodId">
											<option value="">${message("admin.common.choose")}<\/option>
											[#list paymentMethods as paymentMethod]
												[#noescape]
													<option value="${paymentMethod.id}">${paymentMethod.name?html?js_string}<\/option>
												[/#noescape]
											[/#list]
										<\/select>
									<\/td>
								<\/tr>
								<tr>
									<th>
										${message("Refunds.memo")}:
									<\/th>
									<td colspan="3">
										<input type="text" name="refunds.memo" class="text" maxlength="200" \/>
									<\/td>
								<\/tr>
								<tr>
									<td colspan="4" style="border-bottom: none;">
										&nbsp;
									<\/td>
								<\/tr>
							<\/table>
						<\/form>'
					[/@compress]
				,
				width: 900,
				modal: true,
				ok: "${message("admin.dialog.ok")}",
				cancel: "${message("admin.dialog.cancel")}",
				onShow: function() {
					$("#refundsForm").validate({
						rules: {
							amount: {
								required: true,
								positive: true,
								max: ${order.refundableAmount},
								decimal: {
									integer: 12,
									fraction: ${setting.priceScale}
								}
							}
						}
					});
				},
				onOk: function() {
					$("#refundsForm").submit();
					return false;
				}
			});
		});
	[/#if]
	
	[#if order.shippableQuantity > 0]
		// 发货
		$shippingButton.click(function() {
			$.dialog({
				title: "${message("admin.order.shipping")}",
				content: 
					[@compress single_line = true]
						'<form id="shippingForm" action="shipping.jhtml" method="post">
							<input type="hidden" name="token" value="${token}" \/>
							<input type="hidden" name="orderId" value="${order.id}" \/>
							<div class="shipping">
								<table id="shippingLogistics" class="input">
									<tr>
										<th>
											${message("Order.sn")}:
										<\/th>
										<td width="300">
											${order.sn}
										<\/td>
										<th>
											${message("admin.common.createDate")}:
										<\/th>
										<td>
											${order.createDate?string("yyyy-MM-dd HH:mm:ss")}
										<\/td>
									<\/tr>
									<tr>
										<th>
											${message("Shipping.shippingMethod")}:
										<\/th>
										<td>
											<select name="shippingMethodId">
												<option value="">${message("admin.common.choose")}<\/option>
												[#list shippingMethods as shippingMethod]
													[#noescape]
														<option value="${shippingMethod.id}"[#if shippingMethod == order.shippingMethod] selected="selected"[/#if]>${shippingMethod.name?html?js_string}<\/option>
													[/#noescape]
												[/#list]
											<\/select>
										<\/td>
										<th>
											${message("Shipping.deliveryCorp")}:
										<\/th>
										<td>
											<select name="deliveryCorpId">
												<option value="">${message("admin.common.choose")}<\/option>
												[#list deliveryCorps as deliveryCorp]
													[#noescape]
														<option value="${deliveryCorp.id}"[#if order.shippingMethod?? && deliveryCorp == order.shippingMethod.defaultDeliveryCorp] selected="selected"[/#if]>${deliveryCorp.name?html?js_string}<\/option>
													[/#noescape]
												[/#list]
											<\/select>
										<\/td>
									<\/tr>
									<tr>
										<th>
											${message("Shipping.trackingNo")}:
										<\/th>
										<td>
											<input type="text" name="shipping.tracking_no" class="text" maxlength="200" \/>
										<\/td>
										<th>
											${message("Shipping.freight")}:
										<\/th>
										<td>
											<input type="text" name="shipping.freight" class="text" maxlength="16" \/>
										<\/td>
									<\/tr>
									<tr>
										<th>
											${message("Shipping.consignee")}:
										<\/th>
										<td>
											<input type="text" name="shipping.consignee" class="text" value="[#noescape]${order.consignee?html?js_string}[/#noescape]" maxlength="200" \/>
										<\/td>
										<th>
											${message("Shipping.zipCode")}:
										<\/th>
										<td>
											<input type="text" name="shipping.zip_code" class="text" value="[#noescape]${order.zipCode?html?js_string}[/#noescape]" maxlength="200" \/>
										<\/td>
									<\/tr>
									<tr>
										<th>
											${message("Shipping.area")}:
										<\/th>
										<td>
											<span class="fieldSet">
												<input type="hidden" name="areaId" value="${(order.area.id)!}" treePath="${(order.area.treePath)!}" \/>
											<\/span>
										<\/td>
										<th>
											${message("Shipping.address")}:
										<\/th>
										<td>
											<input type="text" name="shipping.address" class="text" value="[#noescape]${order.address?html?js_string}[/#noescape]" maxlength="200" \/>
										<\/td>
									<\/tr>
									<tr>
										<th>
											${message("Shipping.phone")}:
										<\/th>
										<td>
											<input type="text" name="shipping.phone" class="text" value="[#noescape]${order.phone?html?js_string}[/#noescape]" maxlength="200" \/>
										<\/td>
										<th>
											${message("Shipping.memo")}:
										<\/th>
										<td>
											<input type="text" name="shipping.memo" class="text" maxlength="200" \/>
										<\/td>
									<\/tr>
								<\/table>
								<table class="item">
									<tr>
										<th>
											${message("ShippingItem.sn")}
										<\/th>
										<th>
											${message("ShippingItem.name")}
										<\/th>
										<th>
										${message("OrderItem.thumbnail")}
										<\/th>
										<th>
											${message("ShippingItem.isDelivery")}
										<\/th>
										<th>
											${message("admin.order.productStock")}
										<\/th>
										<th>
											${message("admin.order.productQuantity")}
										<\/th>
										<th>
											${message("admin.order.shippedQuantity")}
										<\/th>
										<th>
											${message("admin.order.shippingQuantity")}
										<\/th>
									<\/tr>
									[#list order.orderItems as orderItem]
										<tr>
											<td>
												<input type="hidden" name="shippingItems[${orderItem_index}].sn" value="${orderItem.sn}" \/>
												${orderItem.sn}
											<\/td>
											[#noescape]
												<td width="300">
													<span title="${orderItem.name?html?js_string}">${abbreviate(orderItem.name, 50, "...")?html?js_string}<\/span>
													[#if orderItem.specificationsConverter?has_content]
														<span class="silver">[${orderItem.specificationsConverter?join(", ")?html?js_string}]<\/span>
													[/#if]
													[#if orderItem.typeName != "general"]
														<span class="red">[${message("Goods.Type." + orderItem.typeName)}]<\/span>
													[/#if]
												<\/td>
											[/#noescape]
												<td>
											<img src="${orderItem.thumbnail}" height="100" width="100" ></img>
											<\/td>
											
											<td>
												${message(orderItem.isDelivery?string('admin.common.true', 'admin.common.false'))}
											<\/td>
											
											<td>
												${(orderItem.product.stock)!"-"}
											<\/td>
											<td>
												${orderItem.quantity}
											<\/td>
											<td>
												${orderItem.shippedQuantity}
											<\/td>
											<td>
												[#if orderItem.product?? && orderItem.product.stock < orderItem.shippableQuantity]
													[#assign shippingQuantity = orderItem.product.stock /]
												[#else]
													[#assign shippingQuantity = orderItem.shippableQuantity /]
												[/#if]
												<input type="text" name="shippingItems[${orderItem_index}].quantity" class="text shippingItemsQuantity" value="${shippingQuantity}" style="width: 30px;"[#if shippingQuantity <= 0] disabled="disabled"[/#if] max="${shippingQuantity}" data-is-delivery="${orderItem.isDelivery?string('true', 'false')}" \/>
											<\/td>
										<\/tr>
									[/#list]
								<\/table>
							<\/div>
						<\/form>'
					[/@compress]
				,
				width: 900,
				modal: true,
				ok: "${message("admin.dialog.ok")}",
				cancel: "${message("admin.dialog.cancel")}",
				onShow: function() {
					var $shippingForm = $("#shippingForm");
					var $shippingLogistics = $("#shippingLogistics");
					var $shippingItemsQuantity = $("#shippingForm input.shippingItemsQuantity");
					
					$("#shippingForm input[name='areaId']").lSelect({
						url: "${base}/common/area.jhtml"
					});
					
					function checkDelivery() {
						var isDelivery = false;
						$shippingItemsQuantity.each(function() {
							var $this = $(this);
							if ($this.data("isDelivery") && $this.val() > 0) {
								isDelivery = true;
								return false;
							}
						});
						if (isDelivery) {
							$shippingLogistics.find(":input:not([name='memo'])").prop("disabled", false);
						} else {
							$shippingLogistics.find(":input:not([name='memo'])").prop("disabled", true);
						}
					}
					
					checkDelivery();
					
					$shippingItemsQuantity.on("input propertychange change", function(event) {
						if (event.type != "propertychange" || event.originalEvent.propertyName == "value") {
							checkDelivery()
						}
					});
					
					$.validator.addClassRules({
						shippingItemsQuantity: {
							required: true,
							digits: true
						}
					});
					
					$shippingForm.validate({
						rules: {
							deliveryCorpId: "required",
							freight: {
								min: 0,
								decimal: {
									integer: 12,
									fraction: ${setting.priceScale}
								}
							},
							consignee: "required",
							zipCode: {
								required: true,
								pattern: /^\d{6}$/
							},
							areaId: "required",
							address: "required",
							phone: {
								required: true,
								pattern: /^\d{3,4}-?\d{7,9}$/
							}
						}
					});
				},
				onOk: function() {
					var total = 0;
					$("#shippingForm input.shippingItemsQuantity").each(function() {
						var quantity = $(this).val();
						if ($.isNumeric(quantity)) {
							total += parseInt(quantity);
						}
					});
					
					if (total <= 0) {
						$.message("warn", "${message("admin.order.shippingQuantityPositive")}");
					} else {
						$("#shippingForm").submit();
					}
					return false;
				}
			});
		});
	[/#if]
	
	[#if order.returnableQuantity > 0]
		// 退货
		$returnsButton.click(function() {
			$.dialog({
				title: "${message("admin.order.returns")}",
				content: 
					[@compress single_line = true]
						'<form id="returnsForm" action="returns.jhtml" method="post">
							<input type="hidden" name="token" value="${token}" \/>
							<input type="hidden" name="orderId" value="${order.id}" \/>
							<div class="returns">
								<table class="input">
									<tr>
										<th>
											${message("Order.sn")}:
										<\/th>
										<td width="300">
											${order.sn}
										<\/td>
										<th>
											${message("admin.common.createDate")}:
										<\/th>
										<td>
											${order.createDate?string("yyyy-MM-dd HH:mm:ss")}
										<\/td>
									<\/tr>
									<tr>
										<th>
											${message("Returns.shippingMethod")}:
										<\/th>
										<td>
											<select name="shippingMethodId">
												<option value="">${message("admin.common.choose")}<\/option>
												[#list shippingMethods as shippingMethod]
													[#noescape]
														<option value="${shippingMethod.id}">${shippingMethod.name?html?js_string}<\/option>
													[/#noescape]
												[/#list]
											<\/select>
										<\/td>
										<th>
											${message("Returns.deliveryCorp")}:
										<\/th>
										<td>
											<select name="deliveryCorpId">
												<option value="">${message("admin.common.choose")}<\/option>
												[#list deliveryCorps as deliveryCorp]
													[#noescape]
														<option value="${deliveryCorp.id}">${deliveryCorp.name?html?js_string}<\/option>
													[/#noescape]
												[/#list]
											<\/select>
										<\/td>
									<\/tr>
									<tr>
										<th>
											${message("Returns.trackingNo")}:
										<\/th>
										<td>
											<input type="text" name="returns.tracking_no" class="text" maxlength="200" \/>
										<\/td>
										<th>
											${message("Returns.freight")}:
										<\/th>
										<td>
											<input type="text" name="returns.freight" class="text" maxlength="16" \/>
										<\/td>
									<\/tr>
									<tr>
										<th>
											${message("Returns.shipper")}:
										<\/th>
										<td>
											<input type="text" name="returns.shipper" class="text" value="[#noescape]${order.consignee?html?js_string}[/#noescape]" maxlength="200" \/>
										<\/td>
										<th>
											${message("Returns.zipCode")}:
										<\/th>
										<td>
											<input type="text" name="returns.zip_code" class="text" value="[#noescape]${order.zipCode?html?js_string}[/#noescape]" maxlength="200" \/>
										<\/td>
									<\/tr>
									<tr>
										<th>
											${message("Returns.area")}:
										<\/th>
										<td>
											<span class="fieldSet">
												<input type="hidden" id="areaId" name="areaId" value="${(order.area.id)!}" treePath="${(order.area.treePath)!}" \/>
											<\/span>
										<\/td>
										<th>
											${message("Returns.address")}:
										<\/th>
										<td>
											<input type="text" name="returns.address" class="text" value="[#noescape]${order.address?html?js_string}[/#noescape]" maxlength="200" \/>
										<\/td>
									<\/tr>
									<tr>
										<th>
											${message("Returns.phone")}:
										<\/th>
										<td>
											<input type="text" name="returns.phone" class="text" value="[#noescape]${order.phone?html?js_string}[/#noescape]" maxlength="200" \/>
										<\/td>
										<th>
											${message("Returns.memo")}:
										<\/th>
										<td>
											<input type="text" name="returns.memo" class="text" maxlength="200" \/>
										<\/td>
									<\/tr>
								<\/table>
								<table class="item">
									<tr>
										<th>
											${message("ReturnsItem.sn")}
										<\/th>
										<th>
											${message("ReturnsItem.name")}
										<\/th>
										<th>
										${message("OrderItem.thumbnail")}
								        <\/th>
										<th>
											${message("admin.order.productStock")}
										<\/th>
										<th>
											${message("admin.order.shippedQuantity")}
										<\/th>
										<th>
											${message("admin.order.returnedQuantity")}
										<\/th>
										<th>
											${message("admin.order.returnsQuantity")}
										<\/th>
									<\/tr>
									[#list order.orderItems as orderItem]
										<tr>
											<td>
												<input type="hidden" name="returnsItems[${orderItem_index}].sn" value="${orderItem.sn}" \/>
												${orderItem.sn}
											<\/td>
											[#noescape]
												<td width="300">
													<span title="${orderItem.name?html?js_string}">${abbreviate(orderItem.name, 50, "...")?html?js_string}<\/span>
													[#if orderItem.specificationsConverter?has_content]
														<span class="silver">[${orderItem.specificationsConverter?join(", ")?html?js_string}]<\/span>
													[/#if]
													[#if orderItem.typeName != "general"]
														<span class="red">[${message("Goods.Type." + orderItem.typeName)}]<\/span>
													[/#if]
												<\/td>
											[/#noescape]
											<td>
											<img src="${orderItem.thumbnail}" height="100" width="100" ></img>
											<\/td>
											<td>
												${(orderItem.product.stock)!"-"}
											<\/td>
											<td>
												${orderItem.shippedQuantity}  
											<\/td>
											<td>
												${orderItem.returnedQuantity} 
											<\/td>
											<td>
												<input type="text" name="returnsItems[${orderItem_index}].quantity" class="text returnsItemsQuantity" value="${orderItem.returnableQuantity}" maxlength="9" style="width: 30px;"[#if orderItem.returnableQuantity <= 0] disabled="disabled"[/#if] max="${orderItem.returnableQuantity}" \/>
											<\/td>
										<\/tr>
									[/#list]
								<\/table>
							<\/div>
						<\/form>'
					[/@compress]
				,
				width: 900,
				modal: true,
				ok: "${message("admin.dialog.ok")}",
				cancel: "${message("admin.dialog.cancel")}",
				onShow: function() {
					$("#returnsForm input[name='areaId']").lSelect({
						url: "${base}/common/area.jhtml"
					});
					$.validator.addClassRules({
						returnsItemsQuantity: {
							required: true,
							digits: true
						}
					});
					$("#returnsForm").validate({
						rules: {
							"returns.freight": {
								min: 0,
								decimal: {
									integer: 12,
									fraction: ${setting.priceScale}
								}
							},
							"returns.zip_code": {
								pattern: /^\d{6}$/
							},
							"returns.phone": {
								pattern: /^\d{3,4}-?\d{7,9}$/
							}
						}
					});
				},
				onOk: function() {
					var total = 0;
					$("#returnsForm input.returnsItemsQuantity").each(function() {
						var quantity = $(this).val();
						if ($.isNumeric(quantity)) {
							total += parseInt(quantity);
						}
					});
					if (total <= 0) {
						$.message("warn", "${message("admin.order.returnsQuantityPositive")}");
					} else {
						$("#returnsForm").submit();
					}
					return false;
				}
			});
		});
	[/#if]
	
	[#if !order.hasExpired() && order.statusName == "shipped"]
		// 收货
		$receiveButton.click(function() {
			var $this = $(this);
			$.dialog({
				type: "warn",
				content: "${message("admin.order.receiveConfirm")}",
				onOk: function() {
					$receiveForm.submit();
				}
			});
		});
	[/#if]
	
	[#if !order.hasExpired() && order.statusName == "received"]
		// 完成
		$completeButton.click(function() {
			var $this = $(this);
			$.dialog({
				type: "warn",
				content: "${message("admin.order.completeConfirm")}",
				onOk: function() {
					$completeForm.submit();
				}
			});
		});
	[/#if]
	
	[#if !order.hasExpired() && (order.statusName == "pendingShipment" || order.statusName == "shipped" || order.statusName == "received")]
		// 失败
		$failButton.click(function() {
			var $this = $(this);
			$.dialog({
				type: "warn",
				content: "${message("admin.order.failConfirm")}",
				onOk: function() {
					$failForm.submit();
				}
			});
		});
	[/#if]
	
	// 物流动态
	$transitStep.click(function() {
		var $this = $(this);
		$.ajax({
			url: "transitStep.jhtml?shippingId=" + $this.attr("shippingId"),
			type: "GET",
			dataType: "json",
			cache: true,
			beforeSend: function() {
				$this.hide().after('<span class="loadingIcon">&nbsp;<\/span>');
			},
			success: function(data) {
				if (data.message.type == "success") {
					if (data.transitSteps.length <= 0) {
						$.message("warn", "${message("admin.order.noResult")}");
						return false;
					}
					var transitStepsHtml = "";
					$.each(data.transitSteps, function(i, transitStep) {
						transitStepsHtml += 
							[@compress single_line = true]
								'<tr>
									<th>' + escapeHtml(transitStep.time) + '<\/th>
									<td>' + escapeHtml(transitStep.context) + '<\/td>
								<\/tr>'
							[/@compress]
						;
					});
					$.dialog({
						title: "${message("admin.order.transitStep")}",
						content: 
							[@compress single_line = true]
								'<div class="transitSteps">
									<table>' + transitStepsHtml + '<\/table>
								<\/div>'
							[/@compress]
						,
						width: 600,
						modal: true,
						ok: null,
						cancel: null
					});
				} else {
					$.message(data.message);
				}
			},
			complete: function() {
				$this.show().next("span.loadingIcon").remove();
			}
		});
		return false;
	});

});


//删除
function delButton(id){
	 if (confirm("你确定要删除吗?")) {  
		 $.ajax({
				url: " ../../KuaiDi100/delete.jhtml",
				type: "POST",
				data: 'id='+id,
				dataType: "json",
				cache: false,
				success: function(message) {
					$.message(message);
					if (message == "success") {
						setTimeout(function() {
							location.reload(true);
						}, 1000);
					}
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
	<form id="reviewForm" action="review.jhtml" method="post">
		<input type="hidden" name="id" value="${order.id}" />
		<input type="hidden" id="passed" name="passed" />
	</form>
	<form id="receiveForm" action="receive.jhtml" method="post">
		<input type="hidden" name="id" value="${order.id}" />
	</form>
	<form id="completeForm" action="complete.jhtml" method="post">
		<input type="hidden" name="id" value="${order.id}" />
	</form>
	<form id="failForm" action="fail.jhtml" method="post">
		<input type="hidden" name="id" value="${order.id}" />
	</form>
	<div class="breadcrumb">
		<a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; ${message("admin.order.view")}
	</div>
	<ul id="tab" class="tab">
		<li>
			<input type="button" value="${message("admin.order.orderInfo")}" />
		</li>
		<li>
			<input type="button" value="${message("admin.order.productInfo")}" />
		</li>
		<li>
			<input type="button" value="${message("admin.order.paymentInfo")}" />
		</li>
		<li>
			<input type="button" value="${message("admin.order.refundsInfo")}" />
		</li>
		<li>
			<input type="button" value="${message("admin.order.shippingInfo")}" />
		</li>
		<li>
			<input type="button" value="${message("admin.order.returnsInfo")}" />
		</li>
		<li>
			<input type="button" value="${message("admin.order.orderLog")}" />
		</li>
	</ul>
	<table class="input tabContent">
		<tr>
			<td>
				&nbsp;
			</td>
			<td colspan="3">
				<input type="button" id="reviewButton" class="button" value="${message("admin.order.review")}"[#if order.hasExpired() || order.statusName != "pendingReview"] disabled="disabled"[/#if] />
				<!--<input type="button" id="paymentButton" class="button" value="${message("admin.order.payment")}" />-->
				<input type="button" id="refundsButton" class="button" value="${message("admin.order.refunds")}"[#if order.refundableAmount <= 0] disabled="disabled"[/#if] />
<!-- 				<input type="button" id="shippingButton" class="button" value="${message("admin.order.shipping")}"[#if order.shippableQuantity <= 0] disabled="disabled"[/#if] /> -->
				<input type="button" id="shippingButton" class="button" value="${message("admin.order.shipping")}"[#if order.shippableQuantity <= 0] disabled="disabled"[/#if] />
				<input type="button" id="returnsButton" class="button" value="${message("admin.order.returns")}"[#if order.returnableQuantity <= 0] disabled="disabled"[/#if] />
				<input type="button" id="receiveButton" class="button" value="${message("admin.order.receive")}"[#if order.hasExpired() || order.statusName != "shipped"] disabled="disabled"[/#if] />
				<input type="button" id="completeButton" class="button" value="${message("admin.order.complete")}"[#if order.hasExpired() || order.statusName != "received"] disabled="disabled"[/#if] />
				<input type="button" id="failButton" class="button" value="${message("admin.order.fail")}"[#if order.hasExpired() || (order.statusName != "pendingShipment" && order.statusName != "shipped" && order.statusName != "received")] disabled="disabled"[/#if] />
			</td>
		</tr>
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
				${message("Order.offsetAmount")}:
			</th>
			<td>
				${currency(order.offsetAmount, true)}
			</td>
			<!-- 兑换积分 -->
			<!-- <th>
				${message("Order.exchangePoint")}:
			</th>
			<td>
				${order.exchangePoint}
			</td> -->
		</tr>
		<tr>
			<th>
				${message("Order.amount")}: 
			</th>
			<td>
				<span class="red">${currency(order.amount, true)}</span>
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
		[#if order.refundAmount > 0 || order.refundableAmount > 0]
			<tr>
				<th>
					${message("Order.refundAmount")}:
				</th>
				<td>
					${currency(order.refundAmount, true)}
				</td>
				<th>
					${message("Order.refundableAmount")}:
				</th>
				<td>
					${currency(order.refundableAmount, true)}
				</td>
			</tr>
		[/#if]
		<tr>
		<!-- 商品重量 -->
			<!-- <th>  
				${message("Order.weight")}: 
			</th>
			<td>
				${order.weight}
			</td> -->
			<th>
				${message("Order.quantity")}:
			</th>
			<td>
				${order.quantity}
			</td>
			
			<th>
				${message("Order.phone")}: 
			</th>
			<td>
				${order.phone}
			</td>
		</tr>
		<tr>
			<th>
				${message("Order.shippedQuantity")}:
			</th>
			<td>
				${order.shippedQuantity}
			</td>
			<th>
				${message("Order.returnedQuantity")}:
			</th>
			<td> 
				${order.returnedQuantity}
			</td>
			
			
		</tr>
		<!-- <tr>
			<th>
				${message("admin.order.promotion")}:
			</th>
			<td>
				[#if order.promotionNamesConverter?has_content]
					${order.promotionNames?join(", ")}
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
			<td>
				${currency(order.freight, true)}
			</td>
		</tr>
		-->
		
			
			<!-- <th> 赠送积分
				${message("Order.rewardPoint")}:
			</th>
			<td>
				${order.rewardPoint}
			</td> -->
		
		<tr>
			<!-- <th>  支付方式
				${message("Order.paymentMethod")}:
			</th>
			<td>
				${order.paymentMethodName!"-"}
			</td> -->
			<th>
				${message("Order.shippingMethod")}: 
			</th>
			<td>
				${order.shippingMethodName!"-"}
			</td>
			<th>
				${message("Order.zipCode")}:       
			</th>
			<td>
				${order.zipCode}
			</td>
			
		</tr>
		[#if order.invoice??]
			<tr>
				<th>
					${message("Invoice.title")}:
				</th>
				<td>
					${order.invoice.title}
				</td> 
				<th>
					${message("Order.tax")}:
				</th>
				<td>
					${currency(order.tax, true)}
				</td>
			</tr>
		[/#if]
		<tr>
			<th>
				${message("Order.consignee")}: 
			</th> 
			<td>
				${order.consignee}
			</td>
			<th>
				${message("Order.area")}:
			</th>
			<td>
				${order.areaName}
			</td>
		</tr>
		<tr>
			<th>
				${message("Order.address")}: 
			</th>
			<td>
				${order.address}
			</td>
			<th>
				${message("Order.memo")}:
			</th>
			<td>
				${order.memo}
			</td>
			
		</tr>
		
	</table>
	 
	
	 	<!-- <div style="font-size:20px;border-top-left-radius: 4px;border-top-right-radius: 4px;border: 1px solid ;background-color: 
#F7F7F7;"><font color="#111111" style="margin-left: 20px">商品信息</font></div> 
<HR style="FILTER: alpha(opacity=100,finishopacity=0,style=3)" width="100%" color=#987cb9 SIZE=3>
 -->
 
       
 
 <!-- 商品项详情=============================================================================================== -->
	 <div class="Hotspot"><div class="Hotspot_1">商品项详情</div></div>
	 [#if order.orderItems]
	<div style="border: 1px solid ;">
	
	<table>
		<tr style="width: 100px;background-color: #87CEFF;font-size: 13px;height: 30px;" >
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
				${message("OrderItem.thumbnail")}
			</th>
			<th>
				${message("OrderItem.subtotal")}
			</th>
			<th>
				${message("admin.common.action")} <!-- 操作 --> 
			</th>
		</tr>
			
		[#list order.orderItems as orderItem]
			<tr>
				<td>
					${orderItem.sn}
				</td>
				<td width="400">
					[#if orderItem.product??]
						<a href="${orderItem.product.url}" title="${orderItem.name}" target="_blank">
						<a href="/wap/goods/detail.jhtml?id=${orderItem.product.goods.id}" title="${orderItem.name}" target="_blank">
						${abbreviate(orderItem.name, 100, "...")}(${orderItem.product.goods.caption})
						</a>
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
					<!-- [#if orderItem.typeName == "general"]
						${currency(orderItem.price, true)}
					[#else]
						- 
					[/#if]  --> 
					￥${orderItem.price}.00 
				</td>
				<td>
					${orderItem.quantity}
				</td>
				<td>
					<img src="${orderItem.thumbnail}" height="100" width="100" ></img>
				</td>
				<td>
					<!-- [#if orderItem.typeName == "general"]
						${currency(orderItem.subtotal, true)}	
						
					[#else]
						-
					[/#if]  -->
					 ￥${orderItem.price*orderItem.quantity}.00
				</td>
			
				<td>
				<!-- 发货 -->
				
				<!-- 删除订单项id  -->
				 <input type="button" id="delButton" onclick="delButton(${orderItem.id});" class="button" value="${message("admin.common.delete")}" /> 
				</td>
			</tr>
		[/#list]
		
		[#else]
		<div style="border: 0px solid red;padding-left: 50%;font-size: 20px;">商品项为空</div>
		[/#if] 
	</table>
	</div>
	<!-- 收款详情详情=============================================================================================== -->
	 <div class="Hotspot"><div class="Hotspot_1">收款详情</div></div>
	  [#if order.payments]
	<div style="border: 1px solid ;">
	 <table>
		<tr style="width: 100px;background-color: #87CEFF;font-size: 13px;height: 30px;">
			<th>
				${message("Payment.sn")}
			</th>
			<th>
				${message("Payment.method")}
			</th>
			<th>
				${message("Payment.paymentMethod")}
			</th>
			<th>
				${message("Payment.fee")}
			</th>
			<th>
				${message("Payment.amount")}
			</th>
			<th>
				${message("admin.common.createDate")}
			</th>
			<th>
				${message("admin.common.action")}
			</th>
		</tr>
		[#list order.payments as payment]
			<tr style="height: 50px">
				<td>
					<a href="../payment/view.jhtml?id=${payment.id}">${payment.sn}</a>
				</td>
				<td>
					${message("Payment.Method." + payment.methodName)}
				</td>
				<td>
					${payment.paymentMethod!"-"}
				</td>
				<td>
					${currency(payment.fee, true)}
				</td>
				<td>
					${currency(payment.amount, true)}
				</td>
				<td>
					<span title="${payment.createDate?string("yyyy-MM-dd HH:mm:ss")}">${payment.createDate}</span>
				</td>
				<td>
					<a href="../payment/view.jhtml?id=${payment.id}">[${message("admin.common.seedetails")}]</a>
				</td>
			</tr>
		[/#list]
	[#else]
		<div style="border: 0px solid red;padding-left: 50%;font-size: 20px;">暂无收款详情</div>
		[/#if] 
	</table>
	</div>
	
	<!--退款信息=================================================================================  -->
	<div class="Hotspot"><div class="Hotspot_1">退款信息</div></div>
	  [#if order.refunds]
	<div style="border: 1px solid ;">
	<table>
		<tr style="width: 100px;background-color: #87CEFF;font-size: 13px;height: 30px;">
			<th>
				${message("Refunds.sn")}
			</th>
			<th>
				${message("Refunds.method")}
			</th>
			<th>
				${message("Refunds.paymentMethod")}
			</th>
			<th>
				${message("Refunds.amount")}
			</th>
			<th>
				${message("admin.common.createDate")}
			</th>
			<th>
				${message("admin.common.action")}
			</th>
		</tr>
		[#list order.refunds as refunds]
			<tr style="height: 50px">
				<td>
					${refunds.sn}
				</td>
				<td>
					${message("Refunds.Method." + refunds.methodName)}
				</td>
				<td>
					${refunds.paymentMethod!"-"}
				</td>
				<td>
					${currency(refunds.amount, true)}
				</td>
				<td>
					<span title="${refunds.createDate?string("yyyy-MM-dd HH:mm:ss")}">${refunds.createDate}</span>
				</td>
				<td>
					<a href="../refunds/view.jhtml?id=${refunds.id}">[${message("admin.common.seedetails")}]</a>
				</td>
			</tr>
		[/#list]
	[#else]
		<div style="border: 0px solid red;padding-left: 50%;font-size: 20px;">暂无退款信息</div>
		[/#if] 
	</table>
	</div>
	
	<!--发货信息===================================================================================  -->
	<div class="Hotspot"><div class="Hotspot_1">发货信息</div></div>
	  [#if order.shippings]
	<div style="border: 1px solid ;">
	<table>
		<tr style="width: 100px;background-color: #87CEFF;font-size: 13px;height: 30px;">
			<th>
				${message("Shipping.sn")}
			</th>
			<th>
				${message("Shipping.shippingMethod")}
			</th>
			<th>
				${message("Shipping.deliveryCorp")}
			</th>
			<th>
				${message("Shipping.trackingNo")}
			</th>
			<th>
				${message("Shipping.consignee")}
			</th>
			<th>
				${message("Shipping.isDelivery")}
			</th>
			<th>
				${message("admin.common.createDate")}
			</th>
			<th>
				${message("admin.common.action")}
			</th>
		</tr>
		[#list order.shippings as shipping]
			<tr style="height: 50px">
				<td>
					${shipping.sn}
				</td>
				<td>
					${shipping.shippingMethod!"-"}
				</td>
				<td>
					${shipping.deliveryCorp!"-"}
				</td>
				<td width="260px;">
					${shipping.trackingNo!"-"}
					<a href="https://www.kuaidi100.com/chaxun?nu=${shipping.trackingNo!"-"}" target="_blank">
					<font color="ff0066">查看物流信息</font>
					</a>
					[#if isKuaidi100Enabled && shipping.deliveryCorpCode?has_content && shipping.trackingNo?has_content]
						<a href="javascript:;" class="transitStep" shippingId="${shipping.id}">[${message("admin.order.transitStep")}]</a>
					[/#if]
				</td>
				<td>
					${shipping.consignee!"-"}
				</td>
				<td>
					${message(shipping.isDelivery?string('admin.common.true', 'admin.common.false'))}
				</td>
				<td>
					<span title="${shipping.createDate?string("yyyy-MM-dd HH:mm:ss")}">${shipping.createDate?string("yyyy-MM-dd HH:mm:ss")}</span>
				</td>
				<td>
					<a href="../shipping/view.jhtml?id=${shipping.id}">[${message("admin.common.seedetails")}]</a>
				</td>
			</tr>
		[/#list]
	[#else]
		<div style="border: 0px solid red;padding-left: 50%;font-size: 20px;">暂无发货信息</div>
		[/#if] 
	</table>
	</div>
	
	<!-- 退货信息 =====================================================================  -->
	<div class="Hotspot"><div class="Hotspot_1">退货信息</div></div>
	  [#if order.returns]
	<div style="border: 1px solid ;">
	<table>
		<tr style="width: 100px;background-color: #87CEFF;font-size: 13px;height: 30px;">
			<th>
				${message("Returns.sn")}
			</th>
			<th>
				${message("Returns.shippingMethod")}
			</th>
			<th>
				${message("Returns.deliveryCorp")}
			</th>
			<th>
				${message("Returns.trackingNo")}
			</th>
			<th>
				${message("Returns.shipper")}
			</th>
			<th>
				${message("Setting.phone")}
			</th>
			<th>
				${message("admin.common.createDate")}
			</th>
			<th>
				${message("admin.common.action")}
			</th>
		</tr>
		[#list order.returns as returns]
			<tr style="height: 50px;">
				<td>
					${returns.sn}
				</td>
				<td>
					${returns.shippingMethod!"-"}
				</td>
				<td>
					${returns.deliveryCorp!"-"}
				</td>
				<td>
					${returns.trackingNo!"-"}
				</td>
				<td>
					${returns.shipper}
				</td>
				<td>
					${returns.phone}
				</td>
				<td>
					<span title="${returns.createDate?string("yyyy-MM-dd HH:mm:ss")}">${returns.createDate?string("yyyy-MM-dd HH:mm:ss")}</span>
				</td>
				<td>
					<a href="../returns/view.jhtml?id=${returns.id}">[${message("admin.common.seedetails")}]</a>
				</td>
			</tr>
		[/#list]
	[#else]
		<div style="border: 0px solid red;padding-left: 50%;font-size: 20px;">暂无退货信息</div>
		[/#if] 
	</table>
	</div>
	
	<!-- 订单记录=================================================================================== -->
	<div class="Hotspot"><div class="Hotspot_1">订单记录</div></div>
	  [#if order.orderLogs]
	<div style="border: 1px solid ;">
	<table>
		<tr style="width: 100px;background-color: #87CEFF;font-size: 13px;height: 30px;">
			<th>
				${message("OrderLog.type")}
			</th>
			<th>
				${message("OrderLog.operator")}
			</th>
			<th>
				${message("OrderLog.content")}
			</th>
			<th>
				${message("admin.common.createDate")}
			</th>
		</tr>
		[#list order.orderLogs as orderLog]
			<tr style="height: 50px;">
				<td>
					${message("OrderLog.Type." + orderLog.typeName)}
				</td>
				<td>
					${orderLog.operator!"-"}
				</td>
				<td>
					[#if orderLog.content??]
						<span title="${orderLog.content}">${abbreviate(orderLog.content, 50, "...")}</span>
					[#else]
						-
					[/#if]
				</td>
				<td>
					<span title="${orderLog.createDate?string("yyyy-MM-dd HH:mm:ss")}">${orderLog.createDate?string("yyyy-MM-dd HH:mm:ss")}</span>
				</td>
			</tr>
		[/#list]
	</table>
	[#else]
		<div style="border: 0px solid red;padding-left: 50%;font-size: 20px;">暂无订单记录</div>
		[/#if] 
	</table>
	</div>
	<!-- 商品项详情 -->
	<!-- <table class="item tabContent">
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
				${message("OrderItem.thumbnail")}
			</th>
			<th>
				${message("OrderItem.subtotal")}
			</th>
			<th>
				${message("admin.common.action")} 操作 
			</th>
		</tr>
		[#list order.orderItems as orderItem]
			<tr>
				<td>
					${orderItem.sn}
				</td>
				<td width="400">
					[#if orderItem.product??]
						<a href="${orderItem.product.url}" title="${orderItem.name}" target="_blank">
						<a href="/wap/goods/detail.jhtml?id=${orderItem.product.goods.id}" title="${orderItem.name}" target="_blank">
						${abbreviate(orderItem.name, 100, "...")}(${orderItem.product.goods.caption})
						</a>
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
					<img src="${orderItem.thumbnail}" height="100" width="100" ></img>
				</td>
				<td>
					[#if orderItem.typeName == "general"]
						${currency(orderItem.subtotal, true)}
					[#else]
						-
					[/#if]
				</td>
				删除订单项id 
				<td>
				<a href="javascript:;" id="delButton" onclick="delButton(${orderItem.id});"><font color="red">${message("admin.common.delete")}</font></a>
				</td>
				
			</tr>
		
		[/#list]
		
	</table>
	
	 收款详情
	<table class="item tabContent">
		<tr>
			<th>
				${message("Payment.sn")}
			</th>
			<th>
				${message("Payment.method")}
			</th>
			<th>
				${message("Payment.paymentMethod")}
			</th>
			<th>
				${message("Payment.fee")}
			</th>
			<th>
				${message("Payment.amount")}
			</th>
			<th>
				${message("admin.common.createDate")}
			</th>
		</tr>
		[#list order.payments as payment]
			<tr>
				<td>
					${payment.sn}
				</td>
				<td>
					${message("Payment.Method." + payment.methodName)}
				</td>
				<td>
					${payment.paymentMethod!"-"}
				</td>
				<td>
					${currency(payment.fee, true)}
				</td>
				<td>
					${currency(payment.amount, true)}
				</td>
				<td>
					<span title="${payment.createDate?string("yyyy-MM-dd HH:mm:ss")}">${payment.createDate}</span>
				</td>
			</tr>
		[/#list]
	</table>
	
	退款详情
	<table class="item tabContent">
		<tr>
			<th>
				${message("Refunds.sn")}
			</th>
			<th>
				${message("Refunds.method")}
			</th>
			<th>
				${message("Refunds.paymentMethod")}
			</th>
			<th>
				${message("Refunds.amount")}
			</th>
			<th>
				${message("admin.common.createDate")}
			</th>
		</tr>
		[#list order.refunds as refunds]
			<tr>
				<td>
					${refunds.sn}
				</td>
				<td>
					${message("Refunds.Method." + refunds.methodName)}
				</td>
				<td>
					${refunds.paymentMethod!"-"}
				</td>
				<td>
					${currency(refunds.amount, true)}
				</td>
				<td>
					<span title="${refunds.createDate?string("yyyy-MM-dd HH:mm:ss")}">${refunds.createDate}</span>
				</td>
			</tr>
		[/#list]
	</table>
	
	发货详情
	<table class="item tabContent">
		<tr>
			<th>
				${message("Shipping.sn")}
			</th>
			<th>
				${message("Shipping.shippingMethod")}
			</th>
			<th>
				${message("Shipping.deliveryCorp")}
			</th>
			<th>
				${message("Shipping.trackingNo")}
			</th>
			<th>
				${message("Shipping.consignee")}
			</th>
			<th>
				${message("Shipping.isDelivery")}
			</th>
			<th>
				${message("admin.common.createDate")}
			</th>
		</tr>
		[#list order.shippings as shipping]
			<tr>
				<td>
					${shipping.sn}
				</td>
				<td>
					${shipping.shippingMethod!"-"}
				</td>
				<td>
					${shipping.deliveryCorp!"-"}
				</td>
				<td width="260">
					${shipping.trackingNo!"-"}
					<a href="https://www.kuaidi100.com/chaxun?nu=${shipping.trackingNo!"-"}" target="_blank">
					<font color="ff0066">查看物流信息</font>
					</a>
					[#if isKuaidi100Enabled && shipping.deliveryCorpCode?has_content && shipping.trackingNo?has_content]
						<a href="javascript:;" class="transitStep" shippingId="${shipping.id}">[${message("admin.order.transitStep")}]</a>
					[/#if]
				</td>
				<td>
					${shipping.consignee!"-"}
				</td>
				<td>
					${message(shipping.isDelivery?string('admin.common.true', 'admin.common.false'))}
				</td>
				<td>
					<span title="${shipping.createDate?string("yyyy-MM-dd HH:mm:ss")}">${shipping.createDate}</span>
				</td>
			</tr>
		[/#list]
	</table>
	
	退货信息
	<table class="item tabContent">
		<tr>
			<th>
				${message("Returns.sn")}
			</th>
			<th>
				${message("Returns.shippingMethod")}
			</th>
			<th>
				${message("Returns.deliveryCorp")}
			</th>
			<th>
				${message("Returns.trackingNo")}
			</th>
			<th>
				${message("Returns.shipper")}
			</th>
			<th>
				${message("admin.common.createDate")}
			</th>
		</tr>
		[#list order.returns as returns]
			<tr>
				<td>
					${returns.sn}
				</td>
				<td>
					${returns.shippingMethod!"-"}
				</td>
				<td>
					${returns.deliveryCorp!"-"}
				</td>
				<td>
					${returns.trackingNo!"-"}
				</td>
				<td>
					${returns.shipper}
				</td>
				<td>
					<span title="${returns.createDate?string("yyyy-MM-dd HH:mm:ss")}">${returns.createDate}</span>
				</td>
			</tr>
		[/#list]
	</table>
	
	订单记录
	<table class="item tabContent">
		<tr>
			<th>
				${message("OrderLog.type")}
			</th>
			<th>
				${message("OrderLog.operator")}
			</th>
			<th>
				${message("OrderLog.content")}
			</th>
			<th>
				${message("admin.common.createDate")}
			</th>
		</tr>
		[#list order.orderLogs as orderLog]
			<tr>
				<td>
					${message("OrderLog.Type." + orderLog.typeName)}
				</td>
				<td>
					${orderLog.operator!"-"}
				</td>
				<td>
					[#if orderLog.content??]
						<span title="${orderLog.content}">${abbreviate(orderLog.content, 50, "...")}</span>
					[#else]
						-
					[/#if]
				</td>
				<td>
					<span title="${orderLog.createDate?string("yyyy-MM-dd HH:mm:ss")}">${orderLog.createDate}</span>
				</td>
			</tr>
		[/#list]
	</table> -->
	<table class="input">
		<tr>
			<th>
				&nbsp;
			</th>
			<td>
				<input type="button" class="button" value="${message("admin.common.back")}" onclick="history.back(); return false;" />
			</td>
		</tr>
	</table>
</body>
</html>
[/#escape]