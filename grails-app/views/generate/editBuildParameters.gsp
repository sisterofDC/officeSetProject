<%--
  Created by IntelliJ IDEA.
  User: WANGYIXIN
  Date: 2023/12/26
  Time: 17:13
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<%def r=request.contextPath%>
<html>
<head>
    <title>模板代码参数配置界面</title>
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


<form id="addOrUpdateForm" lay-filter="addOrUpdateForm" class="layui-form model-form">
    <div class="layui-form-item">
        <label class="layui-form-label">包名：</label>
        <div class="layui-input-block"  >
            <input name="packageName" id="packageName" class="layui-input" placeholder="" />
        </div>
    </div>

    <div class="layui-form-item">
        <label class="layui-form-label">类名：</label>
        <div class="layui-input-block"  >
            <input name="domainName" id="domainName" class="layui-input" placeholder="" readonly/>
        </div>
    </div>

    <div class="layui-form-item">
        <label class="layui-form-label">变量名：</label>
        <div class="layui-input-block"  >
            <input name="domainVariableName" id="domainVariableName" class="layui-input" placeholder="" readonly/>
        </div>
    </div>


    <div class="layui-form-item">
        <label class="layui-form-label">类名的中文：</label>
        <div class="layui-input-block"  >
            <input name="domainNameChinese" id="domainNameChinese" class="layui-input" placeholder="请输入类名的中文" autocomplete="off" required/>
        </div>
    </div>

    <div class="layui-form-item">
        <div class="layui-input-block">
            <button class="layui-btn" id="submitButton" lay-filter="formSubmitBtn" lay-submit type="button">保存</button>
            <button class="layui-btn layui-btn-primary" type="button" id="closePage">取消</button>
            <button class="layui-btn layui-btn-primary" type="button" id="beginGenerate">开始生成</button>
            <button class="layui-btn layui-btn-primary" type="button" id="downloadZipFile">打包下载为ZIP</button>
        </div>
    </div>

</form>


<table id="dataTableParameters" lay-filter="dataTableParameters"></table>

<script type="text/html" id="statusSwitch">
<input type="checkbox" name="status"   data-id={{d.id}}  value="{{d.id}}"  title="启用|禁用" lay-skin="switch" lay-filter="statusFilter" {{= d.status == '启用' ? "checked" : "" }}>
</script>

<script type="text/html" id="whetherRequiredSwitch">
<input type="checkbox" name="whetherRequired"  data-id={{d.id}}  value="{{d.id}}" title="需要必填项|不需要" lay-skin="switch" lay-filter="whetherRequiredFilter" {{= d.whetherRequired == '需要必填项' ? "checked" : "" }}>
</script>

<script type="text/html" id="inquiryDropDown">
<button class="layui-btn layui-btn-primary inquiryModeDropDown">
    <span>{{= d.inquiryMode || '相等查询' }}</span>
    <i class="layui-icon layui-icon-down layui-font-12"></i>
</button>
</script>

<script type="text/html" id="queryDropDown">
<button class="layui-btn layui-btn-primary queryDropDown">
    <span>{{= d.query || 'input' }}</span>
    <i class="layui-icon layui-icon-down layui-font-12"></i>
</button>
</script>



<script>
    const table = layui.table;
    const form = layui.form;
    const layer = layui.layer
    const laydate = layui.laydate;
    const dropdown = layui.dropdown;
    const util = layui.util;
    let configDataList = [];

    let domainName
    // table
    let dataTableParameters

    $(document).ready(function() {
        buttonRender()
    })

    function child(obj){
        console.log("父界面的传值",obj);//获取父界面的传值
        domainName = obj.domainName
        $.ajax({
            url: "${r}/generate/editBuildParameters",
            data:{
                "domainName":domainName
            },
            type: "POST",
            success: function(response) {
                console.log(response)
                configDataList=response
                renderTable(response)
            },
            error: function(error) {
                console.error('Error:', error);
            }
        });
    }

    function renderTable(response) {
        let packageName = $("#packageName");
        let domainName = $("#domainName");
        let domainVariableName = $("#domainVariableName")
        let domainNameChinese = $("#domainNameChinese")
        let parameterSet = response[0];

        packageName.val(parameterSet.packageName);
        domainName.val(parameterSet.domainName);
        domainVariableName.val(parameterSet.domainVariableName)
        if (parameterSet.domainNameChinese!==null){
            domainNameChinese.val(parameterSet.domainNameChinese)
        }


        dataTableParameters = table.render({
            elem: '#dataTableParameters',
            cellMinWidth: 100,
            id : 'dataTableParametersId',
            lineStyle: 'height: 50px;',
            cols: [
                [
                    {field: 'id',title: 'id',hide:true},
                    {field: 'packageName', title: '包名',hide:true},
                    {field: 'domainName', title: '类名',hide:true},
                    {field: 'domainNameChinese', title: '类名的中文',hide:true},
                    {field: 'domainVariableName', title: '变量名',hide:true},
                    {field: 'classProperty', title: '属性名'},
                    {field: 'classPropertyChinese', title: '属性对应的中文',edit: 'text'},
                    {field: 'propertyType', title: '属性类型'},
                    {field: 'query', title: '查询类型',templet: '#queryDropDown',},
                    {field: 'inquiryMode', title: '查询方式',templet: '#inquiryDropDown',},
                    {field: 'status', title: '状态', templet: '#statusSwitch'},
                    {field: 'whetherRequired',title: "是否必填", templet: '#whetherRequiredSwitch'},
                    {field: 'dateCreated', title: '创建时间',hide:true},
                    {field: 'lastUpdated', title: '更新时间',hide:true},
                    {field: 'createUser', title: '创建人',hide:true},
                    {field: 'lastUpdatedBy', title: '更新人',hide:true},
                ]
            ],
            data:response,
            done: function(res, curr, count){
                var options = this;

                // 获取当前行数据
                table.getRowData = function(tableId, elem){
                    var index = $(elem).closest('tr').data('index');
                    return table.cache[tableId][index] || {};
                };

                dropdown.render({
                    elem: '.inquiryModeDropDown',
                    data: [{
                        title: '相等查询',
                        id: 100
                    },{
                        title: '模糊查询',
                        id: 101
                    },

                    ],
                    click: function(obj){
                        let data = table.getRowData(options.id, this.elem); // 获取当前行数据(如 id 等字段，以作为数据修改的索引)
                        this.elem.find('span').html(obj.title);
                        // 更新数据中对应的字段
                        data.inquiryMode = obj.title;
                        // 显示 - 仅用于演示
                        // layer.msg('选中值: '+ obj.title +'<br>当前行数据：'+ JSON.stringify(data));
                    }
                });

                dropdown.render({
                    elem: '.queryDropDown',
                    data: [{
                        title: 'input',
                        id: 100
                    },{
                        title: 'dateInput',
                        id: 101
                    },{
                        title: 'singleChoice',
                        id: 102
                    }
                    ],
                    click: function(obj){
                        let data = table.getRowData(options.id, this.elem); // 获取当前行数据(如 id 等字段，以作为数据修改的索引)
                        this.elem.find('span').html(obj.title);
                        // 更新数据中对应的字段
                        data.query = obj.title;
                        // layer.msg('选中值: '+ obj.title +'<br>当前行数据：'+ JSON.stringify(data));
                    }
                });


            }
        });

        form.on('switch(statusFilter)', function(obj){
            let id = this.value;
            let name = this.name;
            let value = obj.elem.checked ? '启用' : '禁用';
            editConfigDataList(name,id,value)
        });


        form.on('switch(whetherRequiredFilter)', function(obj){
            let id = this.value;
            let name = this.name;
            let value = obj.elem.checked ? '需要必填项' : '不需要';
            editConfigDataList(name,id,value)
        });


    }


    function buttonRender() {
        let parentIndex = parent.layer.getFrameIndex(window.name); // 获取窗口索引
        let closePage = $("#closePage")
        let submitButton = $("#submitButton")
        let beginGenerate = $("#beginGenerate")
        let downloadZipFile = $("#downloadZipFile")

        closePage.click(function () {
            parent.layer.close(parentIndex);
        })


        submitButton.click(function () {
            let domainNameChineseVal = $("#domainNameChinese").val();
            let packageNameVal = $("#packageName").val();

            if (domainNameChineseVal===null||domainNameChineseVal===''){
                layer.msg("请填写类名的中文", function() {time:2000});
            }else {
                let thisCache = table.cache['dataTableParametersId'] || {};
                $(thisCache).each(function(index, element) {
                    // 设定后开始保存
                    element.domainNameChinese=domainNameChineseVal
                    console.log("序列",index)
                    console.log("数据",element)
                    $.ajax({
                        url: "${r}/generate/save",
                        data: element,
                        type: "POST",
                        success: function(response) {
                            console.log(response)
                        },
                        error: function(error) {
                            console.error('Error:', error);
                        }
                    });
                });
                layer.msg("更新成功", function() {time:2000});
            }
        })

        beginGenerate.click(function () {
            layer.confirm('生成之前请核实填写完对应的中文没有？', {
                skin: 'layui-layer-admin',
                shade: .1
            }, function () {
                $.ajax({
                    url: "${r}/generate/beginGenerate",
                    data:{
                        "domainName":domainName
                    },
                    type: "POST",
                    success: function(response) {
                        console.log(response)
                        if (response.code===200){
                            layer.msg(response.text, function() {time:2000});
                        }
                    },
                    error: function(error) {
                        console.error('Error:', error);
                    }
                });
            });
        })


        downloadZipFile.click(function () {
            let index = layer.confirm('生成之前请核实填写完对应的中文没有？', {
                skin: 'layui-layer-admin',
                shade: .1
            }, function () {
                $.ajax({
                    type: 'POST',
                    url: '${r}/generate/zipFile',
                    data:{
                        "domainName":domainName
                    },
                    xhrFields: {
                        responseType: 'blob' // Set the responseType to 'blob' here
                    },
                    success: function(data, status, xhr) {
                        let blob = new Blob([data], { type: 'application/octet-stream;charset=UTF-8' });
                        let url = window.URL.createObjectURL(blob);
                        let a = document.createElement('a');
                        a.href = url;
                        a.download = domainName+"生成文件.zip"
                        a.click();
                        window.URL.revokeObjectURL(url);
                    },
                    error: function(xhr, status, error) {
                        console.error(xhr.responseText);
                    }
                });
                layer.close(index)
            });
        })
    }

    function editConfigDataList(name, rowId, value) {
        for (var i=0; i<configDataList.length; i++) {
            if (configDataList[i].id == rowId) {
                configDataList[i][name] = value;
                break;
            }
        }
        table.reload('dataTableParametersId', { data: configDataList });
    };


</script>
</body>
</html>