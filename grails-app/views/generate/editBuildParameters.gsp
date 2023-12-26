<%--
  Created by IntelliJ IDEA.
  User: WANGYIXIN
  Date: 2023/12/26
  Time: 17:13
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<%def r=request.contextPath%>
<html>
<head>
    <title>模板代码参数配置界面</title>
    <asset:javascript src="jquery-3.5.1.min.js"/>
    <asset:stylesheet src="plugins/layui/css/layui.css" />
    <asset:javascript src="plugins/layui/layui.js"/>

    <style>
    </style>
</head>


<body>


<table id="dataTableParameters" lay-filter="dataTableParameters"></table>



<script>
    const table = layui.table;
    const form = layui.form;
    const layer = layui.layer
    const laydate = layui.laydate;
    const dropdown = layui.dropdown;
    const util = layui.util;

    let domainName
    // table
    let dataTableParameters

    $(document).ready(function() {

    })

    function child(obj){
        console.log("父界面的传值",obj);//获取父界面的传值
        domainName = obj.domainName
        $.ajax({
            url: "${r}/generate/editBuildParameters",
            data:{
                "domainName":domainName
            },
            type: "POST",
            success: function(response) {
                console.log(response)
                renderTable(response)
            },
            error: function(error) {
                console.error('Error:', error);
            }
        });
    }

    function renderTable(response) {
        dataTableParameters = table.render({
            elem: '#dataTableParameters',
            cellMinWidth: 100,
            cols: [
                [
                    {field: 'id',title: 'id',hide:true},
                    {field: 'packageName', title: '包名'},
                    {field: 'domainName', title: '类名'},
                    {field: 'domainNameChines', title: '类名的中文'},
                    {field: 'domainVariableName', title: '变量名'},
                    {field: 'classProperty', title: '属性名'},
                    {field: 'classPropertyChinese', title: '属性对应的中文'},
                    {field: 'propertyType', title: '属性类型'},
                    {field: 'query', title: '查询类型'},
                    {field: 'status', title: '状态'},
                    {field: 'dateCreated', title: '创建时间',hide:true},
                    {field: 'lastUpdated', title: '更新时间',hide:true},
                    {field: 'createUser', title: '创建人',hide:true},
                    {field: 'lastUpdatedBy', title: '更新人',hide:true},
                ]
            ],
            data:response,
        });

    }


</script>
</body>
</html>