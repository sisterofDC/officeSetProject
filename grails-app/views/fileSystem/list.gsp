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
                                    <label class="layui-form-label">原始名字：</label>
                                    <div class="layui-input-inline">
                                        <input name="fileOriginName" id="fileOriginName" class="layui-input" placeholder="请输入文件原始名字" autocomplete="off" />
                                    </div>
                                </div>
                                <div class="layui-inline">
                                    <label class="layui-form-label">后缀：</label>
                                    <div class="layui-input-inline">
                                        <input name="fileSuffix" id="fileSuffix" class="layui-input" placeholder="请输入文件后缀" autocomplete="off" />
                                    </div>
                                </div>
                                <div class="layui-inline">
                                    <label class="layui-form-label">文件大小：</label>
                                    <div class="layui-input-inline">
                                        <input name="fileSizeKb" id="fileSizeKb" class="layui-input" placeholder="请输入文件KB大小" autocomplete="off" />
                                    </div>
                                </div>
                                <div class="layui-inline">
                                    <label class="layui-form-label">大小信息：</label>
                                    <div class="layui-input-inline">
                                        <input name="fileSizeInfo" id="fileSizeInfo" class="layui-input" placeholder="请输入文件大小信息" autocomplete="off" />
                                    </div>
                                </div>
                                <div class="layui-inline">
                                    <label class="layui-form-label">存储名：</label>
                                    <div class="layui-input-inline">
                                        <input name="fileObjectName" id="fileObjectName" class="layui-input" placeholder="请输入文件存储名" autocomplete="off" />
                                    </div>
                                </div>
                                <div class="layui-inline">
                                    <label class="layui-form-label">位置：</label>
                                    <div class="layui-input-inline">
                                        <input name="filePath" id="filePath" class="layui-input" placeholder="请输入文件位置" autocomplete="off" />
                                    </div>
                                </div>
                            <div class="layui-inline">
                                <label class="layui-form-label">创建日期：</label>
                                <div class="layui-input-inline">
                                    <input name="dateCreated" id="dateCreated" class="layui-input" placeholder="请输入创建日期" autocomplete="off" />
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
        <button class="layui-btn layui-btn-sm"  type="button" lay-event="upload"><i class="layui-icon layui-icon-upload"></i>上传</button>
        <button class="layui-btn layui-btn-sm"  type="button" lay-event="add"><i class="layui-icon layui-icon-add-1"></i>新增</button>
        <button class="layui-btn layui-btn-sm"  type="button" lay-event="zipDownload"><i class="layui-icon">&#xe601;</i>打包下载</button>
        <button class="layui-btn layui-btn-sm layui-btn-danger icon-btn" lay-event="backUpSql"><i class="layui-icon">&#xe716;</i>备份数据库文件</button>
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
    let taskId /**任务 ID */
    let intervalId /** 循环执行任务的线程ID  */
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
        laydate.render({
            elem: "#dateCreated",
            type: 'date',
            range: true,
            rangeLinked: true, //是否开启日期范围选择时的区间联动标注模式，该模式必须开启 range 属性才能生效
        });
    }

    // 表格的渲染方法
    function renderTable() {
        insTb = table.render({
            elem: '#dataTable',
            url: '${r}/fileSystem/list',
            toolbar: '#myToolbar', //头部工具栏部分
            method: 'post', //请求方式为POST
            limits: [10,20,30,40,50,60,70,80,90,100,200,400,600,800,1000], //分页选择的地方
            limit: 20, //默认显示的数量
            page:{curr:1}, //初始页
            cols:[[
                {type: 'checkbox'}, //多选框
                {title: "id",field: "id",hide:true},
                {title: '文件存储位置',field: "fileLocation",templet: function(d){return  showFileLocationSet(d.fileLocation)},},
                {title: '文件夹名',field: "fileBucket", },
                {title: '文件原始名字',field: "fileOriginName", },
                {title: '后缀名',field: "fileSuffix",},
                {title: '文件KB大小',field: "fileSizeKb", },
                {title: '文件大小信息',field: "fileSizeInfo", },
                {title: '文件存储名',field: "fileObjectName", },
                {title: '文件位置',field: "filePath", },
                {title: '创建日期',field: "dateCreated", templet:"<div>{{layui.util.toDateString(d.dateCreated, 'yyyy-MM-dd')}}</div>",},
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

            if (obj.event === "upload"){ //上传操作
                uploadFromFileSystem()
            }

            if (obj.event === "zipDownload"){ //批量下载
                let checkRows = table.checkStatus('dataTable');
                if (checkRows.data.length === 0) {
                    layer.msg('请选择要操作的数据', {icon: 2});
                    return;
                }
                let objID=checkRows.data.map(function(item) {
                    return item.id;
                }).join(",");
                let index =layer.confirm('当批量下载文件过多时候，可以会卡死服务器', {
                    skin: 'layui-layer-admin',
                    shade: .1
                }, function () {
                    zipDownloadSet(objID)
                    layer.close(index)
                });
            }

            if (obj.event === "backUpSql"){ //备份数据库文件
                let index = layer.confirm('确定要开始备份数据吗？可能会花费很久的事件', {
                    skin: 'layui-layer-admin',
                    shade: .1
                }, function () {
                    sqlFileBackUp()
                    layer.close(index)
                });
            }
        });

        // 右边操作栏
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
            content: '${r}/fileSystem/edit', // 接口地址
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
            url: '${r}/fileSystem/cmd',
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
                    insTb.reload({page: {curr: 1}});
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

    function uploadFromFileSystem() {
        layer.open({
            title: "文件上传", //设置标题
            type: 2, // 格式为打开网页的iframe的子页面
            area: ['99%','99%'], //
            content: '${r}/fileSystem/upload', // 接口地址
            fixed: false, // 不固定
            maxmin: true,
            closeBtn:1,
            shadeClose: true,
            btnAlign: 'c',
            end: function () {
                // 子页面关闭的时候刷新表格
                insTb.reload();
            },
        });
    }

    function showFileLocationSet(fileLocation) {
        switch (fileLocation) {
            case 1:
                return "服务器本地";
            case 2:
                return "阿里云";
            case 3:
                return "腾讯云";
            case 4:
                return "minio";
            case 5:
                return "其他云盘";
            default:
                return "无效数字";
        }
    }

    /**
     * 打包下载
     */
    function zipDownloadSet(data) {
        $.ajax({
            url: '${r}/fileSystem/zipDownload',
            method: "POST",
            data: {
                "ids":data,
            },
            xhrFields: {
                responseType: 'blob' // Set the responseType to 'blob' here
            },
            success: function(data, status, xhr) {
                let blob = new Blob([data], { type: 'application/octet-stream;charset=UTF-8' });
                let url = window.URL.createObjectURL(blob);
                let a = document.createElement('a');
                a.href = url;
                a.download = "打包文件.zip"
                a.click();
                window.URL.revokeObjectURL(url);
            },
            error: function (xhr, status, error) {
                layer.alert("服务器出现问题");
                console.error("Error:",xhr, status, error);
            }
        });
    }

    /**
     * 备份数据库
     */
    function sqlFileBackUp() {
        $.ajax({
            type: 'GET',
            url: ' ${r}/SaveSqlFile/getCreateNewSqlFile',
            success: function(response) {
                // 设置任务ID
                console.log("参数",response)
                if(response.code===200){
                    taskId=response.data
                    checkTaskCondition()
                }else {
                    layer.msg("服务器错误", function() {time:2000});
                }
            },
            error: function(xhr, status, error) {
                console.error(xhr.responseText);
            }
        });
    }

    /**
     * 设置的一秒钟的轮询
     */
    function checkTaskCondition(){
        function sendAjaxRequest() {
            $.ajax({
                type: "GET",
                url: "${r}/SaveSqlFile/taskIsOver",
                data:{
                    "taskId":taskId
                },
                success: function(response) {
                    console.log("返回结果",response)
                    if (response.code===200){
                        if (response.data==="done"){
                            // 任务加载完毕
                            layer.msg("生成成功，即将刷新页面", {
                                time: 1000,
                            });
                            clearInterval(intervalId);
                            insTb.reload({page: {curr: 1}});
                        }else if (response.data==="unfinished"){
                            layer.msg("正在加载中", {
                                time: 1000,
                            });
                        }
                    }else if (response.code ===500) {
                        console.log("Error")
                    }
                },
                error: function(xhr, status, error) {
                    console.error("Error:", error);
                }
            });
        }
        // 将interval任务设置为全局方便调用
        intervalId = setInterval(function () {
            sendAjaxRequest();
        }, 2000)
    }


    /**
     * 下载一个文件通用的接口方法
     * @param fileObjectName 服务器存储名，
     * @param fileId 文件ID
     */
    //这里是需要下载为原来的文件名的一个方面
    function downLoadSingleFile(fileObjectName,fileId) {

    }

</script>


</body>
</html>