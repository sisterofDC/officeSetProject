<%--
  Created by IntelliJ IDEA.
  User: WANGYIXIN
  Date: 2024/1/11
  Time: 15:50
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<%def r=request.contextPath%>
<html>
<head>
    <title>上传模板</title>
    <asset:javascript src="jquery-3.5.1.min.js"/>
    <asset:stylesheet src="plugins/layui/css/layui.css" />
    <asset:javascript src="plugins/layui/layui.js"/>
</head>

<body>

<div class="layui-upload-drag" style="display: block;" id="chooseFiles" name="file">
    <i class="layui-icon layui-icon-upload"></i>
    <div>点击上传，或将文件拖拽到此处</div>
</div>



<script>
    const table = layui.table;
    const form = layui.form;
    const layer = layui.layer
    const laydate = layui.laydate;
    const dropdown = layui.dropdown
    const upload = layui.upload;

    // 上传文件
    let files
    // 渲染table 数据的data
    let renderTableDataMap= new Map();

    $(document).ready(function() {
        uploadButtonRender()
    })


    function uploadButtonRender() {
        // 如果要用这个控件
        // 需要 SuccessfulUpload 的存储上传的成功的ID
        // 需要 renderData (Map) 解析是files 因为分为云上的和本地的
        let uploadEvent = upload.render({
            elem: '#chooseFiles', //绑定的文件上传对象
            url: '${r}/fileSystem/uploadFile', // 此处配置你自己的上传接口即可
            auto: true, // 是否选完文件后自动上传。若为 false，则需设置 bindAction 属性来指向其它按钮提交上传。这里是自动上传。选择后就会上传。
            // bindAction: '#fileUpload', // 设置触发上传的元素选择器或 DOM 对象。
            accept:"file", //指定允许上传时校验的文件类型。可选值有：images 图片类型file 所有文件类型video 视频类型 audio 音频类型
            multiple: true, // 是否允许多文件上传。不支持 ie8/9
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

                // obj.upload(index, file); // 对上传失败的单个文件重新上传，一般在某个事件中使用
                // delete files[index]; //删除列表中对应的文件，一般在某个事件中使用
                // obj.resetFile(index, file, '123.jpg'); // 重命名文件名
            },
            done: function(response, index, upload){
                console.log("服务器回调的参数",response)  // 服务器回调的参数
                console.log("文件的index",index) // index
                console.log(upload)
                // 这里是上传成功后的回调
                if (response.code===200){

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

    // 解析文件属性
    function parseFilesData() {
        $.each(files, function(index, file) {

        });
    }

</script>


</body>
</html>