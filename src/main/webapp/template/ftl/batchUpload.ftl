
<#--
编写JSP的生成模板真的非常难受。你要记住里面JSP的模板也是用的${}来进行数据解析的
-->

<#--  <#noparse>  </#noparse> 是freeMaker 中的转义字符-->
<#noparse>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>批量上传</title>
<#--  -->
    <%def r=request.contextPath%>
    <asset:javascript src="jquery-3.5.1.min.js"/>
    <asset:stylesheet src="plugins/layui/css/layui.css" />
    <asset:javascript src="plugins/layui/layui.js"/>
    <asset:javascript src="xlsx.core.min.js"/>
</head>

</#noparse>

<body>





${name}



</body>
</html>
