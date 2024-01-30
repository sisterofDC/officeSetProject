<%--
  Created by IntelliJ IDEA.
  User: WANGYIXIN
  Date: 2024/1/26
  Time: 17:38
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<%def r=request.contextPath%>
<html>
<head>
    <title>服务器渲染功能</title><article>
    <asset:javascript src="jquery-3.5.1.min.js"/>
    <asset:stylesheet src="plugins/layui/css/layui.css" />
    <asset:javascript src="plugins/layui/layui.js"/>

</head>

<body>

<button class="layui-btn layui-btn-primary" type="button" id="beginGenerate">开始生成</button>

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
                url: "${r}/ServeRendering/index",
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