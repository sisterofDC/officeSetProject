<%--
  Created by IntelliJ IDEA.
  User: WANGYIXIN
  Date: 2023/12/26
  Time: 12:04
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<%def r=request.contextPath%>
<html>
<head>
    <title>关键词</title>
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

legend {
  font-size: x-large;
  padding: 3px 6px;
}

</style>

</head>

<body>
<fieldset>
  <legend class="layui-elem-field layui-field-title">
    关键词
  </legend>
  <form id="addOrUpdateForm" lay-filter="addOrUpdateForm" class="layui-form model-form">
    <input name="id" type="hidden"/>
    <div class="layui-form-item">
      <label class="layui-form-label">域名：</label>
      <div class="layui-input-block"  >
        <input name="domainUrlName" id="domainUrlName" class="layui-input" placeholder="" />
      </div>
    </div>

    <div class="layui-form-item">
      <label class="layui-form-label">关键词标题：</label>
      <div class="layui-input-block"  >
        <input name="keyWordTitle" id="keyWordTitle" class="layui-input" placeholder="请输入关键词标题" required />
      </div>
    </div>

    <div class="layui-form-item">
      <label class="layui-form-label">关键词描述：</label>
      <div class="layui-input-block"  >
        <input name="keyWordDescription" id="keyWordDescription" class="layui-input" placeholder="请输入关键词描述" required/>
      </div>
    </div>

    <div class="layui-form-item">
      <label class="layui-form-label">关键词：</label>
      <div class="layui-input-block"  >
        <input name="keyWords" id="keyWords" class="layui-input" placeholder="请输入关键词" required/>
      </div>
    </div>

    <div class="layui-form-item">
      <label class="layui-form-label">额外信息：</label>
      <div class="layui-input-block">
        <input name="otherMessage" id="otherMessage" class="layui-input" placeholder="请输入额外信息" required/>
      </div>
    </div>

    <div class="layui-form-item">
      <label class="layui-form-label">完整代码：</label>
      <div class="layui-input-block"  >
        <textarea name="fullCode" id="fullCode" class="layui-input" placeholder="" style="height: 200px"></textarea>
      </div>
    </div>

    <div class="layui-form-item">
      <div class="layui-input-block">
        <button type="button" class="layui-btn layui-bg-blue" id="generateCode">生成代码</button>
        <button class="layui-btn" lay-filter="formSubmitBtn" lay-submit>保存</button>
        <button class="layui-btn layui-btn-primary" type="button" ew-event="closeDialog">取消</button>
      </div>
    </div>
  </form>
</fieldset>

<script>
  const table = layui.table;
  const form = layui.form;
  const layer = layui.layer
  const laydate = layui.laydate;
  const dropdown = layui.dropdown;
  const util = layui.util;

  $(document).ready(function() {
    generateCodeButton()
  })

  // 表单提交事件
  form.on('submit(formSubmitBtn)', function (data) {
    console.log("提交数据",JSON.stringify(data))

    let fullCode = $("#fullCode");
    if (fullCode.val().trim() === '') {
      layer.msg("请生成代码", { time: 2000 });
      return false;
    }


    $.ajax({
      url: '${r}/keyWordCode/save',
      method: "POST",
      data: data,
      success: function (response) {
        if (response.code===200){
          layer.msg("添加成功", function() {time:2000});
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

  function generateCodeButton() {
    let generateCode = $("#generateCode");
    let keyWordTitle = $("#keyWordTitle");
    let keyWords = $("#keyWords");
    let keyWordDescription = $("#keyWordDescription");
    let fullCode = $("#fullCode");
    let otherMessage = $("#otherMessage")
    generateCode.click(function () {
      //
      //<meta property="og:title" content="腾讯云 - 产业智变 云启未来" />
      let titleSetOne = '<title>' + keyWordTitle.val() + '</title>';
      let titleSetTwo = '<meta property="og:title" content="' + keyWordTitle.val() + '">'
      // 关键词列子
      //<meta name="keywords" content="阿里云,云服务器,云计算,云数据库,注册公司,域名注册备案,行业解决方案,企业网盘">
      let keywordsSet = '<meta name="keywords" content="' + keyWords.val() + '">';
      // 描述的列子
      // <meta name="description" content="阿里云——阿里巴巴集团旗下公司,小时售后支持，助企业无忧上云。">
      let descriptionSetOne = '<meta name="description" content="' + keyWordDescription.val() + '">';
      let descriptionSetTwo = '<meta property="og:description" content="' + keyWordDescription.val() + '">';
      fullCode.empty()

      let generatedCode = titleSetOne + "\n" +
              titleSetTwo + "\n" +
              keywordsSet + "\n" +
              descriptionSetOne + "\n" +
              descriptionSetTwo + "\n" +
              otherMessage.val();

      fullCode.val(generatedCode);
    })
  }
</script>

</body>
</html>