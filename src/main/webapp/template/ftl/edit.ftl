
<#noparse>
<%@ page contentType="text/html;charset=UTF-8" %>
<%def r=request.contextPath%>
<html>
<head>
    </#noparse>
    <#--    标题部分 -->
    <title>${domainNameChinese}表格</title>
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
    </style>
</head>
</#noparse>


<body>


<div class="layui-bg-gray" style="padding: 16px;">
    <div class="layui-row layui-col-space15">
        <div class="layui-col-md12">
            <div class="layui-card">
                <div class="layui-card-header">${domainNameChinese}表格</div>
                <div class="layui-card-body">

                    <form id="addOrUpdateForm" lay-filter="addOrUpdateForm" class="layui-form model-form">
                        <input name="id" id="id" type="hidden"/>

                        <#list classPropertiesInput as property>
                            <div class="layui-form-item">
                                <label class="layui-form-label">${property.classPropertyChinese}：</label>
                                <div class="layui-input-block"  >
                                    <input name="${property.classPropertyName}" id="${property.classPropertyName}" class="layui-input" placeholder="请输入${property.classPropertyChinese}" autocomplete="off"  ${property.whetherRequired}/>
                                </div>
                            </div>
                        </#list>


                        <div class="layui-form-item">
                            <div class="layui-input-block">
                                <button class="layui-btn" lay-filter="formSubmitBtn" lay-submit id="formSubmitBtn">保存</button>
                                <button class="layui-btn layui-btn-primary" type="button" ew-event="closeDialog" id="closePage">取消</button>
                            </div>
                        </div>
                    </form>

                </div>
            </div>
        </div>
    </div>
</div>


<script>
<#--    -->
// layui 基本组件
    const table = layui.table;
    const form = layui.form;
    const layer = layui.layer
    const laydate = layui.laydate;
    const dropdown = layui.dropdown;
    const util = layui.util;

    let idInputSet
// 额外方法放置的地方
    $(document).ready(function() {
        buttonRender()
    })

    function child(obj) {
        let id = obj.id;
        if(id===null){
            let type = obj.type
            $("#id").val(id)
            idInputSet = id
            $.ajax({
                <#--  本身的用于提交的${r} 需要注释 -->
                url: '<#noparse>${r}</#noparse>/${domainVariableName}/edit',
                method: "POST",
                data: {
                    "id":id,
                },
                success: function (response) {
                    if (response.code===200){
                        layer.msg("数据获取成功", function() {time:2000});
                        // 给表单赋值
                        form.val('addOrUpdateForm',response.data)
                        if (type==="check"){
                            $('#addOrUpdateForm input').prop('readonly', true).removeAttr('placeholder');;
                            $('#formSubmitBtn').remove();
                        }
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
        }else {

        }
    }

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

    function buttonRender() {
        let parentIndex = parent.layer.getFrameIndex(window.name); // 获取窗口索引
        $("#closePage").click(function () {
            parent.layer.close(parentIndex);
        })
    }
</script>

</body>
</html>