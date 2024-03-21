
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
    <asset:javascript src="plugins/layui/xm-select.js"/>
    <style>
        /** 如果需要更改左边文字的宽度在这里修改 */
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
                    <!--表单填写部分-->
                    <form id="addOrUpdateForm" lay-filter="addOrUpdateForm" class="layui-form model-form">
                        <input name="id" id="id" type="hidden"/>
                    <#list classPropertiesInput as property>
                        <div class="layui-form-item">
                            <label class="layui-form-label">${property.classPropertyChinese}：</label>
                            <div class="layui-input-block">
                                <input name="${property.classPropertyName}" id="${property.classPropertyName}" class="layui-input" placeholder="请输入${property.classPropertyChinese}" autocomplete="off"  ${property.whetherRequired}/>
                            </div>
                        </div>
                    </#list>
                    <#list classPropertiesSpecial as propertySpecial>
                        <div class="layui-form-item">
                            <label class="layui-form-label">${propertySpecial.classPropertyChinese}：</label>
                            <div class="layui-input-block">
                                ${propertySpecial.SpecialInput}
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
<#--  javascript部分  -->
    // layui 基本组件
    const table = layui.table;
    const form = layui.form;
    const layer = layui.layer
    const laydate = layui.laydate;
    const dropdown = layui.dropdown;
    const util = layui.util;
    //  用于保存id
    let idInputSet
    //  方法初始化的地方
    $(document).ready(function() {
        renderButton()
        renderLaydate()
    })

    // 时间渲染的方法
    function renderLaydate() {
/*
    如果想要不同的选择器 更新 type 标签里面的内容
    year 年选择器，只提供年列表选择
    month 年月选择器，只提供年、月选择
    date 日期选择器（默认），可选择：年、月、日选择
    time 时间选择器，只提供时、分、秒选择
    datetime 日期时间选择器，可选择：年月日、时分秒
 */

    <#list classPropertiesDate as property>
        laydate.render({
            elem: "#${property.classPropertyName}",
            id: "${property.classPropertyName}layuiDate",
            type: '${property.dateType}',
        });
    </#list>
    }

    // 用于解析父页面向子页面传递的参数
    function child(obj) {
        console.log("父界面的传值",obj);//获取父界面的传值
        // 解析id
        let id = obj.id;
        if(id!==null){
            // 解析方式，是查看还是编辑
            let type = obj.type
            $("#id").val(id)
            idInputSet = id
            // 请求edit接口参数
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
                        // 给时间渲染的重新渲染 如果需要更改为指定格式，修改后面 时间格式  yyyy-MM-dd HH:mm:ss
                        <#list classPropertiesDate as property>
                        $("#${property.classPropertyName}").val(layui.util.toDateString(response.data.${property.classPropertyName}, 'yyyy-MM-dd'));
                        </#list>
                        if (type==="check"){
                            // 如果是查看，将操作改为readonly，没有保存按钮，取消绑定的时间选择器
                            $('#addOrUpdateForm input').prop('readonly', true).removeAttr('placeholder');;
                            $('#formSubmitBtn').remove();
                            <#list classPropertiesDate as property>
                                laydate.unbind('${property.classPropertyName}layuiDate');
                            </#list>
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
        console.log("提交数据",JSON.stringify(data.field))
        /*
        这里可以在前端做出必要的验证

         */


        $.ajax({
            <#--  本身的用于提交的${r} 需要注释 -->
            url: '<#noparse>${r}</#noparse>/${domainVariableName}/save',
            method: "POST",
            data: data.field,
            success: function (response) {
                if (response.code===200){
                    // 成功保存后，提示，并退出
                    layer.msg(response.text, function() {time:2000});
                    // 2秒钟后关闭页面
                    setTimeout(function() {
                        let parentIndex = parent.layer.getFrameIndex(window.name);
                        parent.layer.close(parentIndex);
                    }, 2000);

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

    //按钮部分的渲染
    function renderButton() {
        // 获取窗口索引
        let parentIndex = parent.layer.getFrameIndex(window.name);
        $("#closePage").click(function () {
            parent.layer.close(parentIndex);
        })
    }
</script>

</body>
</html>