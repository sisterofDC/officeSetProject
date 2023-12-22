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
    <title>点击生成</title>
    <asset:javascript src="jquery-3.5.1.min.js"/>
</head>

<body>

<button id="test">
点击操作
</button>

<script>
    $(document).ready(function() {
        test()
    })

    function test() {
        let testButton = $("#test")
        testButton.click(function () {
            $.ajax({
                url: "${r}/generate/index",
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