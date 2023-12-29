
<%@ page contentType="text/html;charset=UTF-8" %>
<%def r=request.contextPath%>
<html>
<head>
    <title>Sitemap文件表格</title>
    <asset:javascript src="jquery-3.5.1.min.js"/>
    <asset:stylesheet src="plugins/layui/css/layui.css" />
    <asset:javascript src="plugins/layui/layui.js"/>
    <style>
    .layui-form-label{
        width: 100px;
    }
    .layui-input-block {
        margin-left: 150px;
    }
    </style>
</head>


<body>
<div class="layui-bg-gray" style="padding: 16px;">
    <div class="layui-row layui-col-space15">
        <div class="layui-col-md12">
            <div class="layui-card">
                <div class="layui-card-header">Sitemap文件填写</div>
                <div class="layui-card-body">
                    <!--表单填写部分-->
                    <form id="addOrUpdateForm" lay-filter="addOrUpdateForm" class="layui-form model-form">
                        <input name="id" id="id" type="hidden"/>
                        <div class="layui-form-item">
                            <label class="layui-form-label">域名：</label>
                            <div class="layui-input-block"  >
                                <input name="domainUrlName" id="domainUrlName" class="layui-input" placeholder="请输入域名" autocomplete="off"  />
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">内容：</label>
                            <div class="layui-input-block"  >
                                <textarea name="sitemapContent" id="sitemapContent" class="layui-input" placeholder="" style="height: 400px"></textarea>
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <div class="layui-input-block">
                                <button class="layui-btn" lay-filter="formSubmitBtn" lay-submit id="formSubmitBtn">保存</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    // layui 基本组件
    const table = layui.table;
    const form = layui.form;
    const layer = layui.layer
    const laydate = layui.laydate;
    const dropdown = layui.dropdown;
    const util = layui.util;

    //  方法初始化的地方
    $(document).ready(function() {
        initData()
    })

    // 表单提交事件
    form.on('submit(formSubmitBtn)', function (data) {
        console.log("提交数据",JSON.stringify(data.field))
        $.ajax({
            url: '${r}/sitemapFile/save',
            method: "POST",
            data: data.field,
            success: function (response) {
                if (response.code===200){
                    // 成功保存后，提示，并退出
                    layer.msg(response.text, function() {time:2000});
                }else {
                    console.log(response)
                    layer.msg("服务器错误", function() {time:2000});
                }
            },
            error: function (xhr, status, error) {
                layer.alert("数据请求出现问题");
                console.error("Error:",xhr, status, error);
            }
        });
        return false;
    });

    function initData() {
        $.ajax({
            url: '${r}/sitemapFile/index',
            method: "POST",
            success: function (response) {
                if (response.code===200){
                    // 成功保存后，提示，并退出
                    layer.msg("数据请求成功", function() {time:2000});
                    form.val('addOrUpdateForm',response.data)
                }else {
                    console.log("错误信息",response)
                    layer.msg(response.text, function() {time:2000});
                }
            },
            error: function (xhr, status, error) {
                layer.alert("数据请求出现问题");
                console.error("Error:",xhr, status, error);
            }
        });
    }

</script>




</body>
</html>