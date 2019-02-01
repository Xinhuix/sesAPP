<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
</head>
<body>
    <form action="UserCenterIndex/addappversion.jhtml"   method="post" enctype="multipart/form-data" >
   				 <input type="file" name="file"  multiple />
    		版本:<input type="text" id="version" name="version" placeholder="20181217"  />
    		是否强制更新:<input type="browsers"  list="mydata" name="type"  />
    		　<input type="radio" name="..." value="..." checked />
            				<datalist id="mydata">
               				 <option value="是">是
               			    <option value="否">否
           					 </datalist>
   				<input type="submit"/>
   </form> 
   
   
   
</body>
</html>