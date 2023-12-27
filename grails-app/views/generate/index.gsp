<%--
  Created by IntelliJ IDEA.
  User: WANGYIXIN
  Date: 2023/12/22
  Time: 11:57
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<%def r=request.contextPath%>
<html>
<head>
    <title>模板代码生成界面</title>
    <asset:javascript src="jquery-3.5.1.min.js"/>
    <asset:stylesheet src="plugins/layui/css/layui.css" />
    <asset:javascript src="plugins/layui/layui.js"/>

    <style>
    .titleSet{
        font-size: x-large;
    }
</style>
</head>

<body>
<div class="layui-bg-gray" style="padding: 16px;">
    <div class="layui-row layui-col-space15">
        <div class="layui-col-md12">
            <div class="layui-card">
                <div class="layui-card-header titleSet">模板代码生成界面</div>
                <div class="layui-card-body">
                    <table id="dataTable" lay-filter="dataTable"></table>
                </div>
            </div>
        </div>
    </div>
</div>




<!-- 表格操作列 -->
<script type="text/html" id="tableBar">
   <button class="layui-btn layui-btn-primary layui-btn-xs" lay-event="edit" type="button">编辑生成参数</button>
</script>



<script>
    const table = layui.table;
    const form = layui.form;
    const layer = layui.layer
    const laydate = layui.laydate;
    const dropdown = layui.dropdown;
    const util = layui.util;

    // table
    let insTb

    $(document).ready(function() {
        renderTable()
    })

    function renderTable() {
        insTb = table.render({
            elem: '#dataTable',
            url: '${r}/generate/index',
            method: 'post',
            cols: [
                [
                    {field: 'domainName', title: 'domain层的类名',minWidth: 160},
                    {title: '操作', toolbar: '#tableBar', fixed: 'right', align: 'center',minWidth: 400}
                ]
            ],
        });
    }


    table.on('tool(dataTable)', function (obj) {
        // 编辑生成参数
        if (obj.event === 'edit') {
            console.log("传递给子页面的参数",obj.data)
            editBuildParameters(obj.data.domainName)
        }
    });

    function editBuildParameters(domainName) {
        layer.open({
            title:"编辑生成参数",
            type: 2,
            area: ['95%','95%'],
            content: '${r}/generate/editBuildParameters',
            fixed: false, // 不固定
            maxmin: true,
            shadeClose: true,
            btnAlign: 'c',
            // 向layer 传递数据
            success:function (layero,index) {
                let iframe = window['layui-layer-iframe' + index];
                let dataToChild = {
                    "domainName":domainName,
                }
                iframe.child(dataToChild)
            },
            // 关闭后渲染选择的数据
            end: function(){
                console.log('弹层已被移除');
            }
        });
    }
</script>

</body>
</html>