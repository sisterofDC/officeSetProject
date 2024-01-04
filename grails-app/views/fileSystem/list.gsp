<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="zh_CN">
<head>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <%def r=request.contextPath%>
    <title>文件系统表格</title>

    <asset:javascript src="jquery-3.5.1.min.js"/>
    <asset:stylesheet src="plugins/layui/css/layui.css" />
    <asset:javascript src="plugins/layui/layui.js"/>

</head>

<body>

<div class="layui-bg-gray" style="padding: 16px;">
    <div class="layui-row layui-col-space15">
        <div class="layui-col-md12">
            <div class="layui-card">
                <div class="layui-card-header">文件系统搜索栏</div>
                <div class="layui-card-body">
                    <form class="layui-form" id="searchForm" lay-filter="searchFormFilter">
                        <div class="layui-form-item">

                                <div class="layui-inline">
                                    <label class="layui-form-label">文件位置：</label>
                                    <div class="layui-input-inline">
                                        <input name="fileLocation" id="fileLocation" class="layui-input" placeholder="请输入文件位置" autocomplete="off" />
                                    </div>
                                </div>
                                <div class="layui-inline">
                                    <label class="layui-form-label">文件夹名：</label>
                                    <div class="layui-input-inline">
                                        <input name="fileBucket" id="fileBucket" class="layui-input" placeholder="请输入文件夹名" autocomplete="off" />
                                    </div>
                                </div>
                                <div class="layui-inline">
                                    <label class="layui-form-label">文件原始名字：</label>
                                    <div class="layui-input-inline">
                                        <input name="fileOriginName" id="fileOriginName" class="layui-input" placeholder="请输入文件原始名字" autocomplete="off" />
                                    </div>
                                </div>
                                <div class="layui-inline">
                                    <label class="layui-form-label">文件后缀：</label>
                                    <div class="layui-input-inline">
                                        <input name="fileSuffix" id="fileSuffix" class="layui-input" placeholder="请输入文件后缀" autocomplete="off" />
                                    </div>
                                </div>
                                <div class="layui-inline">
                                    <label class="layui-form-label">文件KB大小：</label>
                                    <div class="layui-input-inline">
                                        <input name="fileSizeKb" id="fileSizeKb" class="layui-input" placeholder="请输入文件KB大小" autocomplete="off" />
                                    </div>
                                </div>
                                <div class="layui-inline">
                                    <label class="layui-form-label">文件大小信息：</label>
                                    <div class="layui-input-inline">
                                        <input name="fileSizeInfo" id="fileSizeInfo" class="layui-input" placeholder="请输入文件大小信息" autocomplete="off" />
                                    </div>
                                </div>
                                <div class="layui-inline">
                                    <label class="layui-form-label">文件存储名：</label>
                                    <div class="layui-input-inline">
                                        <input name="fileObjectName" id="fileObjectName" class="layui-input" placeholder="请输入文件存储名" autocomplete="off" />
                                    </div>
                                </div>
                                <div class="layui-inline">
                                    <label class="layui-form-label">文件位置：</label>
                                    <div class="layui-input-inline">
                                        <input name="filePath" id="filePath" class="layui-input" placeholder="请输入文件位置" autocomplete="off" />
                                    </div>
                                </div>


                            <div class="layui-inline">&emsp;
                                <button class="layui-btn icon-btn" lay-filter="tableSearch" lay-submit>
                                    <i class="layui-icon">&#xe615;</i>搜索
                                </button>
                                <button type="reset" class="layui-btn layui-btn-primary icon-btn">
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


<!-- 表格操作列 -->
<script type="text/html" id="tableBar">
    <div class="layui-clear-space">
        <button class="layui-btn layui-btn-primary layui-btn-xs" lay-event="check" type="button">查看</button>
        <button class="layui-btn layui-btn-primary layui-btn-xs" lay-event="download" type="button">下载</button>
        <button class="layui-btn layui-btn-primary layui-btn-xs" lay-event="edit" type="button">编辑</button>
        <button class="layui-btn layui-btn-danger layui-btn-xs" lay-event="delete" type="button">删除</button>
    </div>
</script>

<!-- 表头 -->
<script type="text/html" id="myToolbar">
    <div class="layui-btn-container">
        <button class="layui-btn layui-btn-sm"  type="button" lay-event="add"><i class="layui-icon layui-icon-add-1"></i>新增</button>
        <button class="layui-btn layui-btn-sm"  type="button" lay-event="zipDownload"><i class="layui-icon">&#xe601;</i>打包下载</button>
        <button class="layui-btn layui-btn-sm layui-btn-danger icon-btn" lay-event="delete"><i class="layui-icon">&#xe640;</i>批量删除</button>
    </div>
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
        renderTable()
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
    }

    // 表格的渲染方法
    function renderTable() {
        insTb = table.render({
            elem: '#dataTable',
            url: '${r}/fileInfo/list',
            toolbar: '#myToolbar', //头部工具栏部分
            method: 'post', //请求方式为POST
            limits: [10,20,30,40,50,60,70,80,90,100,200,400,600,800,1000], //分页选择的地方
            limit: 20, //默认显示的数量
            page:{curr:1}, //初始页
            cols:[[
                {type: 'checkbox'}, //多选框
                {title: "id",field: "id",hide:true},
                {title: '文件位置',field: "fileLocation", },
                {title: '文件夹名',field: "fileBucket", },
                {title: '文件原始名字',field: "fileOriginName", },
                {title: '文件KB大小',field: "fileSizeKb", },
                {title: '文件大小信息',field: "fileSizeInfo", },
                {title: '文件存储名',field: "fileObjectName", },
                {title: '文件位置',field: "filePath", },
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

        table.on('tool(dataTable)', function (obj) {
            /* 删除 */
            if (obj.event === 'delete') {
                // 删除要提醒
                layer.confirm('确定要操作该数据吗？', {
                    skin: 'layui-layer-admin',
                    shade: .1
                }, function () {
                    let objID = obj.data.id
                    commandSet(objID,"delete")
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

    //
    function showAddOrUpdateModel(data,showType) {
        // 设置标题部分
        let titleSet
        if (data){
            if (showType==="edit"){
                titleSet = '修改'
            }else if (showType === "check"){
                titleSet = '查看'
            }
        }else {
            titleSet = "添加"
        }
        // 打开子页面
        layer.open({
            title: titleSet + '文件系统', //设置标题
            type: 2, // 格式为打开网页的iframe的子页面
            area: ['99%','99%'], //
            content: '${r}/fileInfo/edit', // 接口地址
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
            url: '${r}/fileInfo/cmd',
            method: "POST",
            data: {
                "ids":ids,
                "cmd":commandType,
            },
            success: function (response) {
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


</script>


</body>
</html>