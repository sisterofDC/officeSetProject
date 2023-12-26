
<#noparse>
<%@ page contentType="text/html;charset=UTF-8" %>
<%def r=request.contextPath%>
<html>
<head>
    </#noparse>
    <#--    标题部分 -->
    <title>${domainNameChines}</title>
    <#--    JS CSS 引入的部分-->
    <#noparse>
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

        legend {
            font-size: x-large;
            padding: 3px 6px;
        }
    </style>
</head>
</#noparse>


<body>
<fieldset>
    <legend class="layui-elem-field layui-field-title">
        关键词
    </legend>
    <form id="addOrUpdateForm" lay-filter="addOrUpdateForm" class="layui-form model-form">
        <input name="id" type="hidden"/>

        <#list classPropertiesInput as property>
            <div class="layui-form-item">
                <label class="layui-form-label">${property.classPropertyChinese}：</label>
                <div class="layui-input-block"  >
                    <input name="${property.classPropertyName}" id="${property.classPropertyName}" class="layui-input" placeholder="请输入${property.classPropertyChinese}" />
                </div>
            </div>
        </#list>





        <div class="layui-form-item">
            <div class="layui-input-block">
                <button class="layui-btn" lay-filter="formSubmitBtn" lay-submit>保存</button>
                <button class="layui-btn layui-btn-primary" type="button" ew-event="closeDialog">取消</button>
            </div>
        </div>
    </form>
</fieldset>

<script>
<#--    -->
// layui 基本组件
    const table = layui.table;
    const form = layui.form;
    const layer = layui.layer
    const laydate = layui.laydate;
    const dropdown = layui.dropdown;
    const util = layui.util;

// 额外方法放置的地方
    $(document).ready(function() {

    })

// 表单提交事件
    form.on('submit(formSubmitBtn)', function (data) {
        console.log("提交数据",JSON.stringify(data))
        $.ajax({
            <#--  本身的用于提交的${r} 需要注释 -->
            url: '<#noparse>${r}</#noparse>/${domainVariableName}/save',
            method: "POST",
            data: data,
            success: function (response) {
                if (response.code===200){
                    layer.msg("添加成功", function() {time:2000});
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

</script>

</body>
</html>