	[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>${message("admin.returns.view")} - Powered By JFinalShop</title>
<meta name="author" content="JFinalShop Team" />
<meta name="copyright" content="JFinalShop" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.tools.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/input.js"></script>
<script type="text/javascript">
$().ready(function() {

	[@flash_message /]

});
</script>
<style type="text/css">
	 
	 img{
           height:100px; 
           width:100px;
        }
    .imgc:hover{
            transform: scale(4.5,3);
            box-shadow: 1px 1px 5px 3px #D3D3D3 ;
            box-sizing: border-box;
            }
</style>
</head>
<body>
	<div class="breadcrumb">
		<a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; ${message("admin.returns.view")}
	</div>
	<ul id="tab" class="tab">
		<li>
			<input type="button" value="${message("admin.returns.returnsItem")}" />
		</li>
		<li>
			<input type="button" value="${message("admin.returns.base")}" />
		</li>
		
	</ul>
	<table class="item tabContent">
		<tr>
			<th>
				${message("Member.username")}
			</th>
			<th>
				${message("ReturnsItem.status")}
			</th>
			<th>
				${message("ReturnsItem.sn")}
			</th>
			<th>
				${message("ReturnsItem.name")}
			</th>
			<th>
				<font color="red">退货凭证</font>
			</th>
			<th>
				${message("ReturnsItem.quantity")}
			</th>
			<th>
				${message("ReturnsItem.amount")}
			</th>
			
			<th>
				<font color="red">${message("ReturnsItem.cause")}</font>
			</th>
			<th>
				${message("admin.common.createDate")}
			</th>
			<th>
				<span>${message("admin.common.action")}</span>
			</th>
		</tr>
		[#list returns.returnsItems as returnsItem]
			<tr>
				<td>
					${returnsItem.member.username}
				</td>
				<td>
					${message("ReturnsItem.Status." + returnsItem.statusName)}
				</td>
				<td>
					${returnsItem.sn}
				</td>
				<td>
					<span title="${returnsItem.name}">${abbreviate(returnsItem.name, 50, "...")}</span>
				</td>
				<td>
				[#if returnsItem.images]
				[#list returnsItem.images?split(",") as imgs ]
					<img class="imgc" src="${imgs}"  style="transition: all 0.5s;"></img>	
				[/#list]
				[/#if]
				</td>
				<td>
					${returnsItem.quantity}
				</td>
				<td>
					${returnsItem.amount}
				</td>
				<td>
					${returnsItem.cause}
				</td>
				<td>
					${returnsItem.createDate?string("yyyy-MM-dd HH:mm:ss")}
				</td>
				<td>
					[#if returnsItem.statusName == 'pendingReview']
						<a href="review.jhtml?id=${returnsItem.id}">[${message("ReturnsItem.review")}]</a>
					[#else]
						<span>[暂无操作]</span>
					[/#if]
				</td>
			</tr>
		[/#list]
	</table>
	
	<table class="input tabContent">
		<tr>
			<th>
				${message("Returns.sn")}:
			</th>
			<td>
				${returns.sn}
			</td>
			<th>
				${message("admin.common.createDate")}:
			</th>
			<td> 
				${returns.createDate?string("yyyy-MM-dd HH:mm:ss")}
			</td>
		</tr>
		<tr>
			<th>
				${message("Returns.shippingMethod")}:
			</th>
			<td>
				${returns.shippingMethod!"-"}
			</td>
			<th>
				${message("Returns.deliveryCorp")}:
			</th>
			<td>
				${returns.deliveryCorp!"-"}
			</td>
		</tr>
		<tr>
			<th>
				${message("Returns.trackingNo")}:
			</th>
			<td>
				${returns.trackingNo!"-"}
			</td>
			<th>
				${message("Returns.freight")}:
			</th>
			<td>
				${currency(returns.freight, true)!"-"}
			</td>
		</tr>
		<tr>
			<th>
				${message("Returns.shipper")}:
			</th>
			<td>
				${returns.shipper!"-"}
			</td>
			<th>
				${message("Returns.phone")}:
			</th>
			<td>
				${returns.phone!"-"}
			</td>
				
		</tr>
		<tr>
			<th>
				${message("Returns.area")}:
			</th>
			<td>
				${returns.area!"-"}
			</td>
			<th>
				${message("Returns.address")}:
			</th>
			<td>
				${returns.address!"-"}
			</td>
		</tr>
		<tr>
			<th>
				${message("Returns.zipCode")}:
			</th>
			<td>
				${returns.zipCode!"-"}
			</td>
			<th>
				${message("Returns.order")}:
			</th>
			<td>
				${returns.order.sn}
			</td>
		</tr>
		<tr>
			
			<th>
				${message("Returns.operator")}:
			</th>
			<td>
				${returns.operator!"-"}
			</td>
			<th>
				${message("Returns.memo")}:
			</th>
			<td>
				${returns.memo!"-"}
			</td>
		</tr>
	</table>
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