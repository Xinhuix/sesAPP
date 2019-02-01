<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<script>
window.onload=function(){
	alert("转发中");
	var doc=document.getElementById("document");
	doc.action="allocaterecord.jsp";
	alert("0");
	doc.method="post";
	alert("1");
	doc.submit();
}
	
</script>
</body>
</html>