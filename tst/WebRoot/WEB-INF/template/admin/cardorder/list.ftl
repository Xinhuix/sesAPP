[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>月卡订单</title>
<meta name="author" content="JFinalShop Team" />
<meta name="copyright" content="JFinalShop" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/list.js"></script>
<script type="text/javascript">
$().ready(function() {

	[@flash_message /]

});
</script>
</head>
<body>
	<div class="breadcrumb">
		<a href="${base}/admin/common/index.jhtml">
		${message("admin.breadcrumb.home")}</a> &raquo; 月卡订单
          <span>(${message("admin.page.total", page.totalRow)})</span>
	</div>
	<form id="listForm" action="list.jhtml" method="post">
		<div class="bar">
			<!--  <a href="add.jhtml" class="iconButton">
				<span class="addIcon">&nbsp;</span>${message("admin.common.add")}
			</a> -->
			<div class="buttonGroup">
			<!-- 	
				<a href="javascript:;" id="deleteButton" class="iconButton disabled">
					<span class="deleteIcon">&nbsp;</span>${message("admin.common.delete")}
				</a>-->
				<a href="javascript:;" id="refreshButton" class="iconButton">
					<span class="refreshIcon">&nbsp;</span>${message("admin.common.refresh")}
				</a>
				<div id="pageSizeMenu" class="dropdownMenu">
					<a href="javascript:;" class="button">
						${message("admin.page.pageSize")}<span class="arrow">&nbsp;</span>
					</a>
					<ul>
						<li[#if page.pageSize == 10] class="current"[/#if] val="10">10</li>
						<li[#if page.pageSize == 20] class="current"[/#if] val="20">20</li>
						<li[#if page.pageSize == 50] class="current"[/#if] val="50">50</li>
						<li[#if page.pageSize == 100] class="current"[/#if] val="100">100</li>
					</ul>
				</div>
			</div>
			<div id="searchPropertyMenu" class="dropdownMenu">
				<div class="search">
					<span class="arrow">&nbsp;</span>
<input type="text" id="searchValue" name="pageable.searchValue" value="${pageable.searchValue}" maxlength="200" />
					<button type="submit">&nbsp;</button>
				</div>
				<ul>
					<li[#if pageable.searchProperty == "name"] class="current"[/#if] val="card_name">
					卡名称
					</li>
				</ul>
			</div>
		</div>
		<table id="listTable" class="list">
			<tr>
				<th class="check">
					<input type="checkbox" id="selectAll" />
				</th>
				<th>
					<a href="javascript:;" class="sort" name="id">ID</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="">支付订单号</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="name">会员姓名</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="name">卡名称</a>
				</th>
	            <th>
					<a href="javascript:;" class="sort" name="number">时长</a>
				</th>	
	            <th>
					<a href="javascript:;" class="sort" name="number">数量</a>
				</th>							
				<th>
					<a href="javascript:;" class="sort" name="amount">金额</a>
				</th>

				<th>
					<a href="javascript:;" class="sort" name="">返消费卷金额</a>
				</th>
			    <th>
					<a href="javascript:;" class="sort" name="create_date">购买日期</a>
				</th>	
				<th>
					<a href="javascript:;" class="sort" name="end_date">到期日期</a>
				</th>
						
				<th>
					<a href="javascript:;" class="sort" name="status">状态</a>
				</th>
				<th>
					<span>${message("admin.common.action")}</span>
				</th>
			</tr>
			[#list page.list as o]
				<tr>
					<td>
						<input type="checkbox" name="ids" value="${o.id}" />
					</td>
					<td>${o.id}</td>
					<td>
					${o.cardmpaymentLog.sn}${o.cardmpaymentLog.paymentPluginName}
					[#if o.cardmpaymentLog.status == '1']:${o.cardmpaymentLog.modifyDate?string("yyyy-MM-dd HH:mm:ss")}[/#if]
					</td>
					<td>${o.member.name}(${o.member.username})</td>
					<td>${o.cardName}</td>
					<td>${o.number}个月</td>
					<td>${o.ksl}张</td>					
					<td>${o.amount}￥</td>
					<td>${o.amountVolume}￥</td>
					<td>${o.createDate}</td>
					<td>${o.endDate}</td>						
					<td>
						[#if o.status == '0']  <font color="red">未支付</font>[/#if]
						[#if o.status == '1'] <font color="green">已支付</font>[/#if]
						[#if o.status == '2']  <font color="blue">已冻结</font>[/#if]
						[#if o.status == '3'] <font color="yellow">已逾期</font>[/#if]
						[#if o.status == '4']  <font color="black">退款申请</font>[/#if]
						[#if o.status == '5'] <font color="purple">已退款</font>[/#if]
					</td>
					<td>
					[#if o.status='5']
					<font color="e8e8e8">审核</font>
					[#else]
						<a href="edit.jhtml?id=${o.id}">审核</a>
					[/#if]
					</td>
				</tr>
			[/#list]
		</table>
		[@pagination pageNumber = page.pageNumber totalPages = page.totalPage]
			[#include "/admin/include/pagination.ftl"]
		[/@pagination]
	</form>
</body>
</html>
[/#escape]