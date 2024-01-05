
<#--
编写JSP的生成模板真的非常难受。你要记住里面JSP的模板也是用的${}来进行数据解析的
-->

<#--  <#noparse>  </#noparse> 是freeMaker 中的转义字符-->
<#noparse>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="zh_CN">
<head>
    <meta charset="UTF-8">
    </#noparse>
    <title>${domainNameChinese}批量上传</title>
<#--  -->
    <#noparse>
    <%def r=request.contextPath%>
    <asset:javascript src="jquery-3.5.1.min.js"/>
    <asset:stylesheet src="plugins/layui/css/layui.css" />
    <asset:javascript src="plugins/layui/layui.js"/>
    <asset:javascript src="xlsx.core.min.js"/>
</head>
</#noparse>
<body>

<div class="layui-bg-gray" style="padding: 16px;">
    <fieldset class="layui-elem-field layui-field-title">
        <legend>${domainNameChinese}批量上传</legend>
    </fieldset>
    <div class="layui-row layui-col-space15">
        <div class="layui-col-md6">
            <div class="layui-card">
                <div class="layui-card-header">上传提交</div>
                <div class="layui-card-body" style="display: flex">
                    <input type="file" id="exportData" class="upload-input" accept=".xlsx, .xls,">
                    <button type="button" class="layui-btn"  id="submitButton">立即提交</button>
                </div>
            </div>
        </div>
        <div class="layui-col-md6">
            <div class="layui-card">
                <div class="layui-card-header">模板下载</div>
                <div class="layui-card-body" style="display: flex">
                    <button type="button" class="layui-btn layui-btn-primary" lay-on="downloadTemplate" id="downloadTemplate"><i class="layui-icon layui-icon-template-1"></i>模板下载</button>
                    <button type="button" class="layui-btn layui-btn-primary" lay-on="uploadTableTip"><i class="layui-icon layui-icon-tips"></i>提醒</button>
                </div>
            </div>
        </div>
    </div>

    <div class="layui-row layui-col-space15">
        <div class="layui-col-md12">
            <div class="layui-card">
                <div class="layui-card-body">
<#--                    下方解析的table-->
                    <table id="dataTable" lay-filter="dataTable"></table>
                </div>
            </div>
        </div>
    </div>
</div>


<#--选项框-->



<script>
    // layui基本组件
    const table = layui.table;
    const form = layui.form;
    const layer = layui.layer;
    const util = layui.util;
    const laydate = layui.laydate;
    const dropdown = layui.dropdown;

    /**
     * 用于展示的data 从xlsx 中解析
     * @type {*[]}
     */
    let showTableData = []

    /**
     * table
     */
    let insTb

    $(document).ready(function() {
        exportDateFromFile()
        renderButton()
    })


    // 上传解析的类
    class ${domainName} {
        /** 序号 */
        indexSet
        /** 上传结果 */
        uploadResult
        <#list classProperties as property>
        /** ${property.classPropertyChinese}*/
        ${property.classPropertyName}
        </#list>
        //需要要的构造函数
        constructor(
            indexSet,
            uploadResult,
            <#list classPropertiesConstructor as property>
            ${property.classPropertyName},
            </#list>
        ) {
            this.indexSet = indexSet;
            this.uploadResult = uploadResult;
            <#list classPropertiesConstructor as property>
            this.${property.classPropertyName} = ${property.classPropertyName}
            </#list>
        }
    }

    // 需求xlsx 或者xlx文件
    function exportDateFromFile() {
        let exportData = $("#exportData")
        exportData.change(function (e) {
            let files = e.target.files;
            // 判断上传
            if (e.target.files[0].name.slice(-4)==="xlsx"||e.target.files[0].name.slice(-3)==="xls"){
                let fileReader = new FileReader();
                fileReader.onload = function (ev) {
                    try {
                        let xlsxData = [];
                        let data = ev.target.result
                        // 以二进制流方式读取得到整份excel表格对象
                        let workbook = XLSX.read(data, {type: 'binary'})

                        // 表格的表格范围，可用于判断表头是否数量是否正确
                        let fromTo = '';
                        // 遍历每张表读取 (// 这里只取第一张表，如果需要读取所有的，就把break注释)
                        for (let sheet in workbook.Sheets) {
                            if (workbook.Sheets.hasOwnProperty(sheet)) {
                                fromTo = workbook.Sheets[sheet]['!ref'];
                                console.log(fromTo);
                                xlsxData = xlsxData.concat(XLSX.utils.sheet_to_json(workbook.Sheets[sheet]));
                                break;
                            }
                        }
                        //在控制台打印出来表格中的数据
                        console.log(xlsxData)
                        // 初始化数据列表
                        showTableData = []
                        // 用jquery each的方法开始解析
                        $.each(xlsxData, function( index, value ) {
                            // 新的变量，将变量放入showTableData中
                            let ${domainVariableName} = new ${domainName}
                            <#list classPropertiesParse as property>
                            if (value["${property.classPropertyChinese}"]!==undefined){
                                ${property.domainVariableName}.${property.classPropertyName} = value["${property.classPropertyChinese}"];
                            }else {
                            //  设置默认值

                            }
                            </#list>
                            // 设置索引
                            ${domainVariableName}.indexSet = index+1
                            // 设置为上传
                            ${domainVariableName}.uploadResult = "未上传"
                            showTableData.push(${domainVariableName})
                        });
                    }catch (e){
                        layer.msg("读取文件错误", function() {time:2000});
                        console.log("错误信息",e)
                        return;
                    }
                    // 解析完后渲染table
                    renderTable()
                    //在控制台打印出来表格中的数据
                    console.log("解析到的数据",showTableData)
                }
                // 以二进制方式打开文件
                fileReader.readAsBinaryString(files[0]);
            }
        })
    }

    /**
     * 渲染layuiTable
     */
    function renderTable() {
        let insTb = table.render({
            elem: '#dataTable',
            id : "dataTable",
            cols: [[     //标题栏
                {field: 'indexSet', title: '序号', width:80, align: 'center', fixed: 'left',},
                // 根据选项的更改为不同的上传模板
                <#list classPropertiesTable as property>
                {title: '${property.classPropertyChinese}',field: "${property.classPropertyName}" , edit: 'textarea',},
                </#list>

                { field: 'uploadResult', title: '上传状态',},
            ]],
            data:showTableData,
            // 如果需要编写更复杂的表格在线编辑需要的
            done: function(res, curr, count){
                var options = this;

                // 获取当前行数据
                table.getRowData = function(tableId, elem){
                    var index = $(elem).closest('tr').data('index');
                    return table.cache[tableId][index] || {};
                };
                /*
                主要存在三种，时间，选项框，下拉框
                 */
            },
        })

        table.on('edit(dataTable)', function (obj) {
            console.log(obj.value); // 得到编辑修改后的值
            console.log(obj.field); // 得到编辑的字段名
            console.log(obj.data); // 得到修改后该行的数据
        })
    }

    function renderButton() {
        let submitButton = $("#submitButton")
        submitButton.click(function () {

        })
    }

</script>
</body>
</html>
