<%--
  Created by IntelliJ IDEA.
  User: WANGYIXIN
  Date: 2024/1/9
  Time: 11:21
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title></title>
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

        beginGenerate.click(function () {
            $.ajax({
                url: "${r}/WordPreview/pdfToWord",
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