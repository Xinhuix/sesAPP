[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>${message("admin.member.view")} - Powered By JFinalShop</title>
<meta name="author" content="JFinalShop Team" />
<meta name="copyright" content="JFinalShop" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.tools.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/input.js"></script>
<script type="text/javascript">
	
	//删除
function delButton(id){
	 if (confirm("你确定要删除吗?")) {  
		 $.ajax({
				url: " ../../UserReceiver/DeleteAddress.jhtml",
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
		<a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; ${message("admin.member.view")}
	</div>
	<ul id="tab" class="tab">
		<li>
			<input type="button" value="${message("admin.member.base")}" />
		</li>
		[#if memberAttributes?has_content]
			<li>
				<input type="button" value="${message("admin.member.profile")}" />
			</li>
		[/#if]
		<!--收货地址  -->
		[#if address]
		 <li>
			<input type="button" value="${message("Area.address")}" />
		</li>	
		[/#if]
		
		<!--推荐记录  -->
		 <li>
			<input type="button" value="${message("admin.common.RecommendedRecord")}" />
		</li>	
		
		<!--卡包详情  -->
		[#if cardorder]
		 <li>
			<input type="button" value="卡包详情" />
		</li>
		[/#if]	
		
		<!--订单详情  -->
		[#if Orders]
		 <li>
			<input type="button" value="订单详情" />
		</li>
		[/#if]	
	</ul>
	<table class="input tabContent">
		<tr>
			<th>
				${message("Member.username")}:
			</th>
			<td>
				${member.username}
				[#if loginPlugin??]
					<span class="silver">[${loginPlugin.name}]</span>
				[/#if]
			</td>
		</tr>
		<tr>
			<th>
				${message("Member.email")}:
			</th>
			<td>
				${member.email}
			</td>
		</tr>
		<tr>
			<th>
				${message("Member.nickname")}:
			</th>
			<td>
				${member.nickname}
			</td>
		</tr>
		<tr>
			<th>
				${message("Member.memberRank")}:
			</th>
			<td>
				${member.memberRank.name}
			</td>
		</tr>
		<tr>
			<th>
				${message("admin.member.status")}:
			</th>
			<td>
				[#if !member.isEnabled]
					<span class="red">${message("admin.member.disabled")}</span>
				[#elseif member.isLocked]
					<span class="red"> ${message("admin.member.locked")} </span>
				[#else]
					<span class="green">${message("admin.member.normal")}</span>
				[/#if]
			</td>
		</tr>
		[#if member.isLocked]
			<tr>
				<th>
					${message("Member.lockedDate")}:
				</th>
				<td>
					${member.lockedDate?string("yyyy-MM-dd HH:mm:ss")}
				</td>
			</tr>
		[/#if]
		<tr>
			<th>
				${message("Member.point")}:
			</th>
			<td>
				${member.point}
				<a href="../point/log.jhtml?memberId=${member.id}">[${message("admin.common.view")}]</a>
			</td>
		</tr>
		<tr>
			<th>
				<font color="red">${message("Member.balance")}:</font>
			</th>
			<td>
				${currency(member.balance, true)}
				<a href="../deposit/log.jhtml?memberId=${member.id}">[${message("admin.common.view")}]</a>
			</td>
		</tr>
		<tr>
			<th>
				${message("Member.amount")}:
			</th>
			<td>
				${currency(member.amount, true)}
			</td>
		</tr>
		<tr>
			<th>
				${message("admin.common.createDate")}:
			</th>
			<td>
				${member.createDate?string("yyyy-MM-dd HH:mm:ss")}
			</td>
		</tr>
		<tr>
			<th>
				${message("Member.loginDate")}:
			</th>
			<td>
				${(member.loginDate?string("yyyy-MM-dd HH:mm:ss"))!"-"}
			</td>
		</tr>
		<tr>
			<th>
				${message("Member.registerIp")}:
			</th>
			<td>
				${member.registerIp}
			</td>
		</tr>
		<tr>
			<th>
				${message("Member.loginIp")}:
			</th>
			<td>
				${member.loginIp!"-"}
			</td>
		</tr>
		<tr>
			<th>
				${message("admin.member.reviewCount")}:
			</th>
			<td>
				${member.reviews?size}
			</td>
		</tr>
		<tr>
			<th>
				${message("admin.member.consultationCount")}:
			</th>
			<td>
				${member.consultations?size}
			</td>
		</tr>
		<tr>
			<th>
				${message("admin.member.favoriteGoodsCount")}:
			</th>
			<td>
				${member.favoriteGoods?size}
			</td>
		</tr>
	</table>
	[#if memberAttributes?has_content]
		<table class="input tabContent">
			[#list memberAttributes as memberAttribute]
				<tr>
					<th>
						${memberAttribute.name}:
					</th>
					<td>
						[#if memberAttribute.typeName == "name"]
							${member.name}
						[#elseif memberAttribute.typeName == "gender"]
							[#if member.gender??]
								${message("Member.Gender." + member.genderName)}
							[/#if]
						[#elseif memberAttribute.typeName == "birth"]
							${member.birth}
						[#elseif memberAttribute.typeName == "area"]
							[#if member.area??]
								${member.area.fullName}
							[#else]
								${member.areaName}
							[/#if]
						[#elseif memberAttribute.typeName == "address"]
							${member.address}
						[#elseif memberAttribute.typeName == "zipCode"]
							${member.zipCode}
						[#elseif memberAttribute.typeName == "phone"]
							${member.phone}
						[#elseif memberAttribute.typeName == "mobile"]
							${member.mobile}
						[#elseif memberAttribute.typeName == "text"]
							${member.getAttributeValue(memberAttribute)}
						[#elseif memberAttribute.typeName == "select"]
							${member.getAttributeValue(memberAttribute)}
						[#elseif memberAttribute.typeName == "checkbox"]
							[#list member.getAttributeValue(memberAttribute) as option]
								${option}
							[/#list]
						[/#if]
					</td>
				</tr>
			[/#list]
		</table>
	[/#if]
	
	[#if address]
		<table class="input tabContent">
        <tr style="background-color: #E4E4E4;">
		<td>${message("Member.Recipient")}</td>
		<td>${message("Member.area")}</td>
		<td>${message("Member.Address")}</td>
		<td>${message("Member.phone")}</td>
		<td>${message("admin.common.createDate")}</td>
		<td>${message("admin.common.action")}</td>
		</tr>
     [#list address as address]
     	<tr style="margin-left: 20px;">
        <td style="font-size: 13px;color: red"> ${address.consignee}</td>
        <td style="font-size: 13px;">${address.areaName}</td>
        <td style="font-size: 13px;">${address.address}</td>
        <td style="font-size: 13px;color: red">${address.phone}</td>
        <td style="font-size: 13px;">${address.createDate?string("yyyy-MM-dd HH:mm:ss")}</td>
        <td>
		<a href="javascript:;" id="delButton" onclick="delButton(${address.id});"><font color="red">${message("admin.common.delete")}</font></a>
		</td>
       </tr> 
    [/#list]
	</table>
	[/#if]
		<table class="input tabContent">
        <tr style="background-color: #E4E4E4;">
		<td>我推荐人的ID</td>
		<td>创建日期</td>
		<td>返还积分日期</td>
		<td>返回积分数量</td>
		<td>返还积分状态</td>
		<td>${message("admin.common.action")}</td>
		</tr>
	 [#if ListSRecommend]	
     [#list ListSRecommend as ecommend]
     	<tr style="margin-left: 20px;">
        <td style="font-size: 13px;color: red"><a  href="../member/view.jhtml?id=${ecommend.to_member_id}">${ecommend.to_member_id}</a></td>
        <td style="font-size: 13px;">${ecommend.createDate?string("yyyy-MM-dd HH:mm:ss")}</td>
        <td style="font-size: 13px;">[#if ecommend.status == '1']${ecommend.modifyDate?string("yyyy-MM-dd HH:mm:ss")}[/#if]</td>
        
         <td style="font-size: 13px;color: red">
        [#if ecommend.countPionts]  <font color="33cc99">${ecommend.countPionts}</font>[/#if]
		</td>
        
        <td style="font-size: 13px;color: red">
        [#if ecommend.status == '0']  <font color="ff00cc">未返</font>[/#if]
		[#if ecommend.status == '1'] <font color="33cc99">已返</font>[/#if]
		</td>
        <td>
		<a href="javascript:;" id="delButton" onclick="delButton(${address.id});"><font color="red">${message("admin.common.delete")}</font></a>
		</td>
       </tr>
    [/#list]
    [#else]
       <td style="font-size: 20px;text-align:center;">暂无推荐人</td> 
    [/#if]
       
    <tr style="background-color: #87CEFF;">
		<td>推荐我的人ID</td>
		<td>创建日期</td>
		<td>返还积分日期</td>
		<td>返回积分数量</td>
		<td>返还积分状态</td>
		<td>${message("admin.common.action")}</td>
		</tr>
		[#if TomemberidTwo]
		<tr style="margin-left: 20px;">
        <td style="font-size: 13px;color: red"><a  href="../member/view.jhtml?id=${TomemberidTwo.member_id}">${TomemberidTwo.member_id}</a></td>
        <td style="font-size: 13px;">${TomemberidTwo.createDate?string("yyyy-MM-dd HH:mm:ss")}</td>
        <td style="font-size: 13px;">[#if TomemberidTwo.status == '1']${TomemberidTwo.modifyDate?string("yyyy-MM-dd HH:mm:ss")}[/#if]</td>
         <td style="font-size: 13px;color: red">
        [#if TomemberidTwo.countPionts]  <font color="33cc99">${TomemberidTwo.countPionts}</font>[/#if]
		</td>
        <td style="font-size: 13px;color: red">
        [#if TomemberidTwo.status == '0']  <font color="ff00cc">未返</font>[/#if]
		[#if TomemberidTwo.status == '1'] <font color="33cc99">已返</font>[/#if]
		</td>
        <td>
		<a href="javascript:;" id="delButton" onclick="delButton(${address.id});"><font color="red">${message("admin.common.delete")}</font></a>
		</td>
       </tr>
       [#else]
       <td style="font-size: 20px;text-align:center;">独立注册,无推荐人</td> 
       [/#if]
	</table>
	
	[#if cardorder]
	<table class="input tabContent">
        <tr style="background-color: #E4E4E4;">
		<td>ID</td>
		<td>卡名称</td>
		<td>时长</td>
		<td>数量</td>
		<td>金额</td>
		<td>返回消费卷金额</td>
		<td>购买日期</td>
		<td>到期日期</td>
		<td>退订日期</td>
		<td>操作记录</td>
		<td>状态</td>
		</tr>
     [#list cardorder as card]
     	<tr style="margin-left: 20px;">
        <td style="font-size: 13px;color: red"><a  href="../member/view.jhtml?id=${ecommend.to_member_id}">${card.id}</a></td>
        <td style="font-size: 13px;">${card.cardName}</td>
        <td style="font-size: 13px;">${card.number}个月</td>
        <td style="font-size: 13px;">${card.ksl}张</td>
        <td style="font-size: 13px;color: red">
        <font color="ff00cc">${card.amount}￥</font>
        </td>
        <td style="font-size: 13px;">
		<font color="red">${card.amountVolume}￥</font>
		</td>
		<td style="font-size: 13px;">${card.createDate?string("yyyy-MM-dd HH:mm:ss")}</td>
		<td style="font-size: 13px;">${card.endDate?string("yyyy-MM-dd HH:mm:ss")}</td>
		<td style="font-size: 13px;">[#if card.returnDate]${card.returnDate?string("yyyy-MM-dd HH:mm:ss")}[#else]暂无退订记录[/#if]</td>
		<td style="font-size: 13px;">[#if card.memo]
		[#list card.memo?split(";") as memo ] 
			<font color="red">${memo}</font><br>
		[/#list] 
		[#else]暂无操作记录
		[/#if]
		</td>
		<td style="font-size: 13px;">
		[#if card.status == '6'] <font color="green">续期了</font>
		[#elseif card.status='3']<font color="red">已逾期</font>
		[#elseif card.status='5']<font color="#7F7F7F">已退订</font>
		[#elseif card.status='4']<font color="e8e8e8"><a href="../cardreturn/edit.jhtml?id=${card.id}">退卡申请(待审核)</a></font>
		[#elseif card.status='1']<font color="#87CEFA">使用中</font>
		[#elseif card.status='0']<font color="red">未支付</font>
		[#elseif card.status='2']<font color="red">已冻结</font>
		[/#if]
		
		</td>
       </tr>
    [/#list]
	</table>
	[/#if]
	
	
	[#if Orders]
	<table class="input tabContent">
	
	 [#list Orders as order]
	 <tr style="height: 50px;">
			<th>
				下单时间:
			</th>
			<td>
				${order.create_date}
			</td>
			<th>
				订单号：
			</th>
			<td>
				<a href="../order/view.jhtml?id=${order.id}">${order.sn}</a>
			</td>
			
			<th>
				收件人:
			</th>
			<td>
				${order.consignee}
			</td>
			<th>
				付款金额:
			</th>
			<td>
				￥${order.amount}
			</td>
			<th>
				状态：
			</th>
			<td>
				<font color="red">${message("Order.Status." + order.statusName)}
						[#if order.status =="2"]
						[#list order.orderItems as orderItem]
						[#if  orderItem.orderItemStatus =="2"||orderItem.orderItemStatus =="3"||orderItem.orderItemStatus =="4"]
							,${message("Order.Status.Partialunpaid")}
							 [#break]
						[/#if] 	
						[#if orderItem.orderItemStatus =="5"]
							,但是部分已经申请退货
							 [#break]
						[/#if]
						[/#list]
						[/#if]
						</font>
				
			</td>
		</tr>
		<tr style="background-color: #87CEFF;">
		<td>商品名字</td>
		<td>数量</td>
		<td>单价</td>
		<td>发货数量</td>
		<td>退货数量</td>
		<td>图片</td>
		<td>状态</td>
		<td>操作</td>
		</tr>
		
		[#list order.orderItems as item]
		<tr style="margin-left: 20px;">
        <td style="font-size: 13px;">
        <a href="/wap/goods/detail.jhtml?id=${item.product.goods.id}" title="${orderItem.name}" target="_blank">
		${abbreviate(item.name, 100, "...")}(${item.product.goods.caption})
		</a>
        </td>
        <td style="font-size: 13px;">${item.quantity}</td>
        <td style="font-size: 13px;">${item.price}</td>
        <td style="font-size: 13px;">${item.shipped_quantity}</td>
        <td style="font-size: 13px;">${item.returned_quantity}</td>
        <td style="font-size: 13px;"><img src="${item.thumbnail}" style="width: 100px;"></td>
        <td style="font-size: 13px;">
        <font color="red">
        [#if item.order_item_status=="1"]
        	 待发货
        [#elseif  item.order_item_status=="2"] 	
        	已发货
        [#elseif  item.order_item_status=="4"] 	
        	已签收
        [#elseif item.order_item_status=="5"] 	
    	未发货,已申请退款
        [#elseif item.order_item_status=="6"] 	
    	已发货,申请退款
        [#elseif item.order_item_status=="7"] 	
  		退款成功
        [/#if]
        </font>
        </td>
        <td><a href="../order/view.jhtml?id=${order.id}">[查看]</a></td>
       </tr>
		  [/#list]
		[/#list]	
	[/#if]
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