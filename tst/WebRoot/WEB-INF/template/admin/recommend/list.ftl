[#escape x as x?html]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>卡</title>
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
		<a href="${base}/admin/common/index.jhtml">${message("admin.breadcrumb.home")}</a> &raquo; 
		会员推荐记录 <span>(${message("admin.page.total", page.totalRow)})</span>
	</div>
	<form id="listForm" action="list.jhtml" method="post">
		<div class="bar">
			
			<div class="buttonGroup">
				
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
					<input type="text" id="mobile" name="mobile" value="${mobile}" maxlength="200" placeholder="推荐人手机号"/>
					<button type="submit">&nbsp;</button>
				</div>
				<!--
				<ul>
					<li[#if pageable.searchProperty == "member_id"] class="current"[/#if] val="member_id">推荐人</li>
				</ul>-->
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
					<a href="javascript:;" class="sort" name="to_member_id"><font color="red">[当前被推荐者]</font></a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="member_id">第一推荐人</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="member_id">第二推荐人</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="create_date">创建日期</a>
				</th>				
				<th>
					<a href="javascript:;" class="sort" name="modify_date">返积分日期</a>
				</th>	
				<th>
					<a href="javascript:;" class="sort" name="count_pionts">返回积分数量</a>
				</th>					
				<th>
					<a href="javascript:;" class="sort" name="status">返回积分状态</a>
				</th>
				
			</tr>
			[#list page.list as card]
			
		
				<tr>
					<td>
						<input type="checkbox" name="ids" value="${card.id}" />
					</td>
					<td>${card.id}</td>
					
					<td><a  href="../member/view.jhtml?id=${card.tomember.id}">[#if card.tomember.nickname]<font color="red">${card.tomember.nickname}</font>:[/#if]${card.tomember.username}</a></td>
					<td><a  href="../member/view.jhtml?id=${card.member.id}">[#if card.member.nickname]<font color="red">${card.member.nickname}</font>:[/#if]${card.member.username}</a></td>
					<td><a  href="../member/view.jhtml?id=${card.totomember.id}">[#if card.totomember.nickname]<font color="red">${card.totomember.nickname}</font>:[/#if]${card.totomember.username}</a></td>
					<td>${card.createDate?string("yyyy-MM-dd HH:mm:ss")}</td>
					<td>[#if card.status == '1']${card.modifyDate?string("yyyy-MM-dd HH:mm:ss")}[/#if]</td>					
					<td>
						[#if card.countPionts]  <font color="33cc99">${card.countPionts}</font>[/#if]
					</td>
					<td>
						[#if card.status == '0']  <font color="ff00cc">未返</font>[/#if]
						[#if card.status == '1'] <font color="33cc99">已返</font>[/#if]
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