<%--
  Created by IntelliJ IDEA.
  User: WANGYIXIN
  Date: 2024/1/3
  Time: 17:14
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<%def r=request.contextPath%>
<html>
<head>
    <title>execl文件生成界面</title><article>
    <asset:javascript src="jquery-3.5.1.min.js"/>
    <asset:stylesheet src="plugins/layui/css/layui.css" />
    <asset:javascript src="plugins/layui/layui.js"/>

</head>

<body>

<button class="layui-btn layui-btn-primary" type="button" id="beginGenerate">开始生成</button>
<button class="layui-btn layui-btn-primary" type="button" id="createSheet">createSheet</button>
<button class="layui-btn layui-btn-primary" type="button" id="createCells">createCells</button>


<script>
    $(document).ready(function() {
        buttonRender()
    })

    function buttonRender(){
        let beginGenerate = $("#beginGenerate")
        let createCells = $("#createCells")
        let createSheet = $("#createSheet")
        
        beginGenerate.click(function () {
            $.ajax({
                url: "${r}/excelTools/index",
                type: "POST",
                success: function(response) {
                    console.log(response)
                },
                error: function(error) {
                    console.error('Error:', error);
                }
            });
        })


        createSheet.click(function () {
            $.ajax({
                url: "${r}/excelTools/createSheet",
                type: "POST",
                success: function(response) {
                    console.log(response)
                },
                error: function(error) {
                    console.error('Error:', error);
                }
            });
        })

        createCells.click(function () {
            $.ajax({
                url: "${r}/excelTools/createCells",
                type: "POST",
                success: function(response) {
                    console.log(response)
                },
                error: function(error) {
                    console.error('Error:', error);
                }
            });
        })
    }
</script>
</body>
</html>