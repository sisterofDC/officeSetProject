
<#--  <#noparse>  </#noparse> 是freeMaker 中的转义字符-->
<#noparse>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="zh_CN">
<head>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <%def r=request.contextPath%>
    </#noparse>
<#--    标题的生成部分-->
    <title>${domainNameChinese}表格</title>
<#--    JS CSS 引入的部分-->
    <#noparse>

    <asset:javascript src="jquery-3.5.1.min.js"/>
    <asset:stylesheet src="plugins/layui/css/layui.css" />
    <asset:javascript src="plugins/layui/layui.js"/>
    <asset:javascript src="plugins/layui/xm-select.js"/>
</head>
</#noparse>

<body>

<div class="layui-bg-gray" style="padding: 16px;">
    <div class="layui-row layui-col-space15">
        <div class="layui-col-md12">
            <div class="layui-card">
                <div class="layui-card-header">${domainNameChinese}搜索栏</div>
                <div class="layui-card-body">
                    <form class="layui-form" id="searchForm" lay-filter="searchFormFilter">
                        <div class="layui-form-item">
                        <#list classPropertiesInput as property>
                            <div class="layui-inline">
                                <label class="layui-form-label">${property.classPropertyChinese}：</label>
                                <div class="layui-input-inline">
                                    <input name="${property.classPropertyName}" id="${property.classPropertyName}" class="layui-input" placeholder="请输入${property.classPropertyChinese}" ${property.type} autocomplete="off" />
                                </div>
                            </div>
                        </#list>
<#--                            用于xm-select 渲染使用-->
                        <#list classPropertiesSpecial as propertySpecial>
                            <div class="layui-inline">
                                <label class="layui-form-label">${propertySpecial.classPropertyChinese}：</label>
                                <div class="layui-input-inline">
                                    <div name="${propertySpecial.classPropertyName}" id="${propertySpecial.classPropertyName}"><div>
                                </div>
                            </div>
                        </#list>
                            <div class="layui-inline">&emsp;
                                <button class="layui-btn icon-btn" lay-filter="tableSearch" lay-submit>
                                    <i class="layui-icon">&#xe615;</i>搜索
                                </button>
                                <button type="reset" class="layui-btn layui-btn-primary icon-btn" id="resetButton">
                                    <i class="layui-icon">&#xe669;</i>重置
                                </button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <div class="layui-row layui-col-space15">
        <div class="layui-col-md12">
            <div class="layui-card">
                <div class="layui-card-body">
                    <table id="dataTable" lay-filter="dataTable"></table>
                </div>
            </div>
        </div>
    </div>
</div>



<!-- 右边表格操作列 -->
<script type="text/html" id="tableBar">
    <div class="layui-clear-space">
        <button class="layui-btn layui-btn-primary layui-btn-xs" lay-event="check" type="button">查看</button>
        <button class="layui-btn layui-btn-primary layui-btn-xs" lay-event="edit" type="button">编辑</button>
        <button class="layui-btn layui-btn-danger layui-btn-xs" lay-event="delete" type="button">删除</button>
<!--   如果需要更多直接添加功能，     -->
    </div>
</script>

<!-- 表头 头部操作栏部分-->
<script type="text/html" id="myToolbar">
    <div class="layui-btn-container">
        <button class="layui-btn layui-btn-sm"  type="button" lay-event="add"><i class="layui-icon layui-icon-add-1"></i>新增</button>
        <button class="layui-btn layui-btn-sm layui-btn-danger icon-btn" lay-event="delete"><i class="layui-icon">&#xe640;</i>删除</button>
    </div>
</script>


<!-- 处理是否的函数 类型于 Boolean 或者 二选一属性的 -->
<script type="text/html" id="${classPropertyNameWithBoolean}Switch">
    <input type="checkbox" name="${classPropertyNameWithBoolean}"   data-id={{d.id}}  value="{{d.id}}"  title="启用|禁用" lay-skin="switch" lay-filter="${classPropertyNameWithBoolean}Switch" {{= d.enabled == ${trueTypeValue} ? "checked" : "" }}>
</script>


<script>
    // layui 基本组件
    const table = layui.table;
    const form = layui.form;
    const layer = layui.layer
    const laydate = layui.laydate;
    const dropdown = layui.dropdown;
    const util = layui.util;
    // 表格的参数 用于   insTb.reload();
    let insTb
    // 方法初始化的地方
    $(document).ready(function() {
        renderXmSelect()
        renderTable()
        renderLaydate()
        renderButton()
    })

    // 时间渲染的方法
    function renderLaydate() {
        /*
        如果想要不同的选择器，更新 type 标签里面的内容，默认选择的只有年月日。每次修改后，需要更改controller中的时间解析代码
        year 年选择器，只提供年列表选择
        month 年月选择器，只提供年、月选择
        date 日期选择器（默认），可选择：年、月、日选择
        time 时间选择器，只提供时、分、秒选择
        datetime 日期时间选择器，可选择：年月日、时分秒
         */
        <#list classPropertiesDate as property>
        laydate.render({
            elem: "#${property.classPropertyName}",
            type: '${property.dateType}',
            range: true,
            rangeLinked: true, //是否开启日期范围选择时的区间联动标注模式，该模式必须开启 range 属性才能生效
        });
        </#list>
    }

    // 表格的渲染方法
    function renderTable() {
        insTb = table.render({
            elem: '#dataTable',
            url: '<#noparse>${r}</#noparse>/${domainVariableName}/list',
            toolbar: '#myToolbar', //头部工具栏部分
            method: 'post', //请求方式为POST
            limits: [10,20,30,40,50,60,70,80,90,100,200,400,600,800,1000], //分页选择的地方
            limit: 20, //默认显示的数量
            page:{curr:1}, //初始页
            // 如果存在更多模板，需要自己添加
            cols:[[
                {type: 'checkbox'}, //多选框
                {title: "id",field: "id",hide:true},
                <#list classPropertiesTable as property>
                {title: '${property.classPropertyChinese}',field: "${property.classPropertyName}", ${property.templet}},
                </#list>

                //如果右边宽度不够直接修改width
                {title: '操作栏', toolbar: '#tableBar', align: 'center', width: 200, fixed: 'right'}, //最右边操作栏
            ]]
       })

        // 头部操作栏部分
        table.on('toolbar(dataTable)', function (obj) {
            /* 删除 */
            if (obj.event === 'delete') {
                let checkRows = table.checkStatus('dataTable');
                if (checkRows.data.length === 0) {
                    layer.msg('请选择要操作的数据', {icon: 2});
                    return;
                }
                // 删除要提醒
                layer.confirm('确定要操作该数据吗？', {
                    skin: 'layui-layer-admin',
                    shade: .1
                }, function () {
                    let objID=checkRows.data.map(function(item) {
                        return item.id;
                    }).join(",");
                    commandSet(objID,"delete")
                });
            }

            /* 添加 */
            if (obj.event === 'add') { // 添加
                showAddOrUpdateModel();
            }
        });

        // 右边操作栏部分
        table.on('tool(dataTable)', function (obj) {
            /* 删除 */
            if (obj.event === 'delete') {
                // 删除要提醒
                let index = layer.confirm('确定要操作该数据吗？', {
                    skin: 'layui-layer-admin',
                    shade: .1
                }, function () {
                    let objID = obj.data.id
                    commandSet(objID,"delete")
                    layer.close(index)
                });
            }

            /* 编辑 */
            if (obj.event === 'edit') {
                console.log("传递给子页面的参数",obj.data)
                showAddOrUpdateModel(obj.data.id,"edit");
            }

            /* 查看 */
            if (obj.event === 'check') {
                console.log("传递给子页面的参数",obj.data)
                showAddOrUpdateModel(obj.data.id,"check");
            }
        });

    }

    // 搜索栏提交部分
    form.on('submit(tableSearch)', function (data) {
        insTb.reload({where: data.field, page: {curr: 1}});
        return false;
    });


    /**
     * 表格中开关的渲染框
     */
    form.on('switch(${classPropertyNameWithBoolean}Switch)', function(obj){
        let id = this.value;
        let name = this.name;
        let value = obj.elem.checked ? ${trueTypeValue} : ${falseTypeValue};
        console.log(id,name,value)

        $.ajax({
            url: '<#noparse>${r}</#noparse>/${domainVariableName}/save',
            method: "POST",
            data: {
                id:id,
                ${classPropertyNameWithBoolean}:value
            },
            success: function (response) {
                if (response.code===200){
                    // 成功保存后，提示，并退出
                    layer.msg(response.text, function() {time:2000});
                    insTb.reload();
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
    });



    // 集成后的更改页面选择器
    function showAddOrUpdateModel(data,showType) {
        // 设置标题部分
        let titleSet
        // 对标题进行处理
        if (data){
            if (showType==="edit"){
                titleSet = '修改'
            }else if (showType === "check"){
                titleSet = '查看'
            }else if (showType === "add"){
                titleSet = "添加"
            }
        }else {
            titleSet = "添加"
        }
        // 打开子页面
        layer.open({
            title: titleSet + '${domainNameChinese}', //设置标题
            type: 2, // 格式为打开网页的iframe的子页面
            area: ['99%','99%'], //
            content: '<#noparse>${r}</#noparse>/${domainVariableName}/edit', // 接口地址
            fixed: false, // 不固定
            maxmin: true,
            closeBtn:1,
            shadeClose: true,
            btnAlign: 'c',
            success:function (layero,index) {
                // iframe地址
                let iframe = window['layui-layer-iframe' + index];
                // 向子页面传递参数
                if (data){
                    // 这里如果需要传递更多参数就在这个里面进行修改，并在edit里面进行取出
                    let dataToChild = {
                        "id":data,
                        "type":showType,
                    }
                    iframe.child(dataToChild)
                }
            },
            end: function () {
                // 子页面关闭的时候刷新表格
                insTb.reload();
            },
        });
    }

    // 命令操作 ids 操作 批量操作
    function commandSet(ids,commandType) {
        $.ajax({
            <#--  本身的用于提交的${r} 需要注释 -->
            url: '<#noparse>${r}</#noparse>/${domainVariableName}/cmd',
            method: "POST",
            data: {
                "ids":ids,
                "cmd":commandType,
            },
            success: function (response) {
                console.log("服务器应答参数",response)
                if (response.code===200){
                    layer.msg(response.text, function() {time:2000});
                    // 操作完后刷新table
                    insTb.reload();
                }else {
                    console.log(response)
                    layer.msg("服务器错误", function() {time:2000});
                }
            },
            error: function (xhr, status, error) {
                layer.alert("服务器出现问题");
                console.error("Error:",xhr, status, error);
            }
        });
    }

    /**
     * 额外的button渲染，如果特殊的按钮需要处理
     */
    function renderButton() {
        let resetButton = $("#resetButton")
        resetButton.click(function () {

        })
    }

    /**
     * 渲染XmSelect
     * 1、如果本地渲染，需要在resetButton哪里重新配置，渲染的数据。
     * 2、如果网络请求渲染，ajax请求 需要调整为 async: false 需要保证数据回来后再进行后面的渲染。否则后面现实都是undefined
     * 3、根据官方选出需要的样式 https://maplemei.gitee.io/xm-select/#/component/install
     */
    function renderXmSelect() {

    }
</script>


</body>
</html>