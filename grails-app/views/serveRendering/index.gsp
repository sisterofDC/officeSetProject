<%--
  Created by IntelliJ IDEA.
  User: WANGYIXIN
  Date: 2024/1/26
  Time: 17:38
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<%def r=request.contextPath%>
<html>
<head>
    <title>服务器渲染功能</title><article>
    <asset:javascript src="jquery-3.5.1.min.js"/>
    <asset:stylesheet src="plugins/layui/css/layui.css" />
    <asset:javascript src="plugins/layui/layui.js"/>
</head>
<body>

<div class="layui-row layui-col-space15">
    <div class="layui-col-md12">
        <div class="layui-card">
            <div class="layui-card-header">Excel转单页PDF</div>
            <div class="layui-card-body">
                <div class="buttonContainerFile" id="buttonContainerFileShow">
                    <button type="button" class="layui-btn layui-btn-primary  layui-bg-blue" id="chooseFiles">选择文件</button>
                    <button type="button" class="layui-btn layui-btn-primary" lay-event="beginUpload" id="beginUpload"><i class="layui-icon">&#xe681;</i>开始上传</button>
                    <button type="button" class="layui-btn layui-btn-primary" lay-event="beginConvert" id="beginConvert"><i class="layui-icon">&#xe655;</i>开始转换</button>
                </div>
                <table id="dataFileTable" lay-filter="dataFileTable"></table>
            </div>
        </div>
    </div>
</div>

<!-- 表格操作列 -->
<script type="text/html" id="tableBar">
<div class="layui-clear-space">
    <button class="layui-btn layui-btn-primary layui-btn-xs" lay-event="check" type="button">查看</button>
    <button class="layui-btn layui-btn-primary layui-btn-xs" lay-event="download" type="button">下载</button>
    <button class="layui-btn layui-btn-danger layui-btn-xs" lay-event="delete" type="button">删除</button>
</div>
</script>

<!-- 表头 -->
<script type="text/html" id="myToolbar">
<div class="layui-btn-container">
    <button class="layui-btn layui-btn-sm"  type="button" lay-event="zipDownload"><i class="layui-icon">&#xe601;</i>打包下载</button>
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
    const upload = layui.upload;

    // 上传文件
    let files
    // 渲染table 数据的data
    let renderTableDataMap= new Map();
    // table 组件
    let fileTable

    /**
     * 上传文件的渲染
     */
    class UploadFileSet {
        /** 文件序号 */
        index
        /** 文件名*/
        name
        /** 大小*/
        size
        /** 服务器ID */
        cloudStoreFileID
        /** 上传结果*/
        uploadStatus

        constructor(index, name, size, cloudStoreFileID, uploadStatus) {
            this.index = index;
            this.name = name;
            this.size = size;
            this.cloudStoreFileID = cloudStoreFileID;
            this.uploadStatus = uploadStatus;
        }
    }

    $(document).ready(function() {
        uploadButtonRender()
        renderButton()
    })

    /**
     * 这里需要选择后上传
     */
    function uploadButtonRender() {
        // 如果要用这个控件
        // 需要 SuccessfulUpload 的存储上传的成功的ID
        // 需要 renderData (Map) 解析是files 因为分为云上的和本地的
        let uploadEvent = upload.render({
            elem: '#chooseFiles', //绑定的文件上传对象
            url: '${r}/fileSystem/uploadFile', // 此处配置你自己的上传接口即可
            auto: false, // 是否选完文件后自动上传。若为 false，则需设置 bindAction 属性来指向其它按钮提交上传。这里是自动上传。选择后就会上传。
            bindAction: "#beginUpload", // 设置触发上传的元素选择器或 DOM 对象。
            accept:"file", //指定允许上传时校验的文件类型。可选值有：images 图片类型file 所有文件类型video 视频类型 audio 音频类型
            multiple: true, // 是否允许多文件上传。不支持 ie8/9
            // number:1, //	 同时可上传的文件数量，一般当 multiple: true 时使用。
            drag:true, // 是否可以拖拽
            choose: function(obj){
                console.log(obj)
                // 将每次选择的文件追加到文件队列
                files = obj.pushFile();
                // 预读本地文件，如果是多文件，则会遍历。(不支持ie8/9)
                obj.preview(function(index, file){
                    console.log("文件索引",index); // 得到文件索引
                    console.log("文件对象",file); // 得到文件对象
                });
                // 解析数据
                parseFilesData()
                // 渲染表格
                renderTable()
                // obj.upload(index, file); // 对上传失败的单个文件重新上传，一般在某个事件中使用
                // delete files[index]; //删除列表中对应的文件，一般在某个事件中使用
                // obj.resetFile(index, file, '123.jpg'); // 重命名文件名
            },
            done: function(response, index, upload){
                console.log("服务器回调的参数",response)  // 服务器回调的参数
                console.log("文件的index",index) // index
                // 这里是上传成功后的回调
                if (response.code===200){
                    resetFile(index,response.data)
                    renderTable()
                }else {
                    layer.msg('数据上传失败!',function() {time:2000})
                }
            },
            allDone: function(obj){
                console.log("上传的文件总数:"+obj.total); // 上传的文件总数
                console.log("上传成功的文件数:"+obj.successful); // 上传成功的文件数
                console.log("上传失败的文件数:"+obj.failed); // 上传失败的文件数
            },
            error: function(index, upload){
                console.log(index); // 当前文件的索引
                console.log(upload)
            }
        });
    }

    function parseFilesData() {
        // 开始渲染
        $.each(files,function (index,value) {
            console.log(index); // File index
            console.log(value); // File object
            if (renderTableDataMap.has(index)===false){
                let uploadFileSet = new UploadFileSet(
                    index,value.name, convertToKB(value.size),"","待上传"
                )
                renderTableDataMap.set(index,uploadFileSet)
            }
        })
    }

    function renderTable() {
        // 将选择的渲染进去
        let data = [...renderTableDataMap.values()]
        fileTable = table.render({
            elem: '#dataFileTable',
            toolbar: '#myToolbar', //头部工具栏部分
            cellMinWidth: 100,
            cols: [
                [
                    {type:  'numbers', title: '序号', width: 70, fixed: 'left' }, //序号列
                    {field: 'index', title: '文件号', sort: true},
                    {field: 'name', title: '文件名', sort: true},
                    {field: 'size',title: '大小' ,sort: true},
                    {field: 'cloudStoreFileID',title: "服务器文件ID"},
                    {field: 'uploadStatus',title: '上传状态',templet: function (d) {  return showUploadStatus(d.uploadStatus); }},
                    {title: '操作', toolbar: '#tableBar', fixed: 'right', align: 'center',minWidth: 100}
                ]
            ],
            data:data,
            done: function(res, curr, count, origin){
                // console.log(res); // 得到当前渲染的数据
                // console.log(curr);  // 得到当前页码
                console.log(count); // 得到数据总量
                // console.log(origin); // 回调函数所执行的来源 --- 2.8.7+

            },
        });
    }

    function showUploadStatus(data) {
        if (data==="上传成功"){
            return '<span style="background-color: green;color: white;font-size: larger">'+data+'</span>'
        }else if (data==='待上传') {
            return '<span style="background-color: blue;color: white;font-size: larger">'+data+'</span>'
        }else  if (data==='上传失败')  {
            return '<span style="background-color: red;color: white;font-size: larger">'+data+'</span>'
        }else {
            return '<span style="background-color: red;color: white;font-size: larger">'+'未知状态'+'</span>'
        }
    }

    function convertToKB(size) {
        let kilobytes = Math.floor(size / 1024);
        console.log(kilobytes + " KB");
        return kilobytes + " KB"
    }

    function resetFile(index,serverID) {
        if(renderTableDataMap.has(index)===true){
            let setUploadStatus = renderTableDataMap.get(index)
            setUploadStatus.uploadStatus="上传成功"
            setUploadStatus.cloudStoreFileID=serverID
            renderTableDataMap.set(index,setUploadStatus)
        }
    }

    function renderButton() {
        let beginConvert = $("#beginConvert")
        beginConvert.click(function () {
            convertRequestFunction()
        })
    }


    function convertRequestFunction() {
        let valuesArray = [...renderTableDataMap.values()];
        // 这里先不做成全部的
        // $.each(valuesArray,function (index,value) {
        //
        // })
        let value =valuesArray[0]
        if(value.uploadStatus==="上传成功"){
            let fileId = value.cloudStoreFileID
            let postData = {
                fileId: fileId
            };
            $.ajax({
                url: '${r}/ServeRendering/beginConvert',
                type: 'POST',
                data: postData,
                success: function(response) {
                    console.log(response);
                },
                error: function(xhr, status, error) {
                    console.error('POST request failed');
                }
            });
        }

    }

</script>

</body>
</html>