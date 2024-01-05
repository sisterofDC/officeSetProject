
<%@ page contentType="text/html;charset=UTF-8" %>
<%def r=request.contextPath%>
<html>
<head>
    <title>内江农科院文章表格</title>
    <asset:javascript src="jquery-3.5.1.min.js"/>
    <asset:stylesheet src="plugins/layui/css/layui.css" />
    <asset:javascript src="plugins/layui/layui.js"/>
    %{--    编辑框--}%
    <asset:javascript src="plugins/ckeditor/ckeditor.js"/>
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
                <div class="layui-card-header">内江农科院文章表格</div>
                <div class="layui-card-body">
                    <!--表单填写部分-->
                    <form id="addOrUpdateForm" lay-filter="addOrUpdateForm" class="layui-form model-form">
                        <input name="id" id="id" type="hidden"/>
                            <div class="layui-form-item">
                                <label class="layui-form-label">内容：</label>
                                <div class="layui-input-block">
                                    <textarea name="body" id="body" class="layui-input" placeholder="请输入内容" autocomplete="off" style="height: 200px"></textarea>
                                </div>
                            </div>
                            <div class="layui-form-item">
                                <label class="layui-form-label">栏目：</label>
                                <div class="layui-input-block"  >
                                    <input name="typeid" id="typeid" class="layui-input" placeholder="请输入栏目" autocomplete="off"  />
                                </div>
                            </div>
                            <div class="layui-form-item">
                                <label class="layui-form-label">跳转URL：</label>
                                <div class="layui-input-block"  >
                                    <input name="redirecturl" id="redirecturl" class="layui-input" placeholder="请输入跳转URL" autocomplete="off"  />
                                </div>
                            </div>

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
        <div class="layui-col-md12">
            <div class="layui-card">
                <div class="layui-card-header">文章</div>
                <div class="layui-card-body">
                    <div id="bodyContent">

                    </div>
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
    //  用于保存id
    let idInputSet
    //  方法初始化的地方
    $(document).ready(function() {
        buttonRender()
        renderCkeditor()
    })

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
                url: '${r}/njnkyAddonarticle/edit',
                method: "POST",
                data: {
                    "id":id,
                },
                success: function (response) {
                    if (response.code===200){
                        layer.msg("数据获取成功", function() {time:2000});
                        // 给表单赋值
                        form.val('addOrUpdateForm',response.data)
                        let bodyContent = $("#bodyContent")
                        bodyContent.empty()
                        bodyContent.append(response.data.body)
                        if (type==="check"){
                            // 如果是查看，将操作改为readonly，没有保存按钮，取消绑定的时间选择器
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
        console.log("提交数据",JSON.stringify(data.field))
        $.ajax({
            url: '${r}/njnkyAddonarticle/save',
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

    function renderCkeditor() {
        CKEDITOR.replace('body',{

        });
    }

    //按钮部分的渲染
    function buttonRender() {
        // 获取窗口索引
        let parentIndex = parent.layer.getFrameIndex(window.name);
        $("#closePage").click(function () {
            parent.layer.close(parentIndex);
        })
    }
</script>

</body>
</html>