
<#--  <#noparse>  </#noparse> 是freeMaker 中的转义字符-->
<#noparse>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="zh_CN">
<head>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <%def r=request.contextPath%>
    </#noparse>
<#--    标题的生成部分-->
    <title>${domainNameChines}</title>
<#--    JS CSS 引入的部分-->
    <#noparse>

    <asset:javascript src="jquery-3.5.1.min.js"/>
    <asset:stylesheet src="plugins/layui/css/layui.css" />
    <asset:javascript src="plugins/layui/layui.js"/>

</head>
</#noparse>

<body>
<table id="dataTable" lay-filter="dataTable"></table>

<!-- 表格操作列 -->
<script type="text/html" id="tableBar">
    <button class="layui-btn layui-btn-primary layui-btn-xs" lay-event="check" type="button">查看</button>
    <button class="layui-btn layui-btn-primary layui-btn-xs" lay-event="edit" type="button">编辑</button>
    <button class="layui-btn layui-btn-danger layui-btn-xs" lay-event="delete" type="button">删除</button>
</script>


<script>



</script>


</body>
</html>