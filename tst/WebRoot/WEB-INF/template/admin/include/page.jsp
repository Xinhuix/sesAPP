<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
   <%@taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
	<div  id="pageDiv"   style="width:100%;text-align:center;padding:10px 0 10px 0;text-shadow:1px 1px 10px blue;box-shadow:0px 0px 15px 1px #ccf inset;border-radius:5px;border-top-right-radius:0;border-top-left-radius:0;">
		  <c:forEach   begin="${page.startPage }"   var="i"  end="${page.endPage }"  varStatus="vs">
		  			<c:if test="${page.currentPage==i }">${i }</c:if>
		  			<c:if test="${page.currentPage!=i }"><a href="${page.url }?page.currentPage=${i}${page.appendParams() }">${i}</a></c:if>
		  </c:forEach>
		  &nbsp;&nbsp;共<span>[ ${page.recordCount }</span>条记录，合计 <span>${page.pageCount }</span>页]
	</div>
	<script type="text/javascript">
			<c:forEach items="${page.params}" var="p">
			var element=document.getElementsByName("${p.key}")[0];
			if(element!=undefined){
				 switch(element.tagName.toLowerCase()){
				 case "select":
					 var child=element.children;
					 for(var i=0;i<child.length;i++){
						    if(child[i].value=="${p.value}"){
						    	 child[i].selected=true;
						    }
					 }
					 break;
				 case "input":
				 		switch(element.type){
				 		case "text":
				 			element.value="${p.value}";
				 			break;
				 		}
					 break;
				 }
			}
			</c:forEach>
		</script>
