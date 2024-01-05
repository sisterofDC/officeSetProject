<%--
  Created by IntelliJ IDEA.
  User: WANGYIXIN
  Date: 2024/1/4
  Time: 15:31
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<%def r=request.contextPath%>
<html>
<head>
    <title>Word文件预览界面</title>
    <asset:javascript src="jquery-3.5.1.min.js"/>
    <asset:javascript src="bootstrap.js"/>
    <asset:stylesheet src="bootstrap.css"/>
    <asset:javascript src="jszip.js"/>
    <asset:javascript src="docx-preview.js"/>
    <asset:javascript src="thumbnail.example.js"/>
    <asset:stylesheet src="thumbnail.example.css"/>
</head>

<body class="vh-100 d-flex flex-column">
<div class="hstack p-2 gap-2 bg-light">
    <input id="files" type="file" class="form-control" style="width: 50ch;" accept=".docx" />
    <button id="loadButton" class="btn btn-primary px-4">Reload</button>

</div>


<div class="flex-grow-1 d-flex flex-row" style="height: 0;">
    <details class="docx-thumbnails h-100">
        <summary></summary>
        <div id="thumbnails-container" class="docx-thumbnails-container"></div>
    </details>
    <div id="document-container" class="overflow-auto flex-grow-1 h-100"></div>
</div>

<script>
    let currentDocument = null;

    const docxOptions = Object.assign(docx.defaultOptions, {
        debug: true,
        experimental: true,
    });

    const container = document.querySelector("#document-container");
    const fileInput = document.querySelector("#files");
    const loadButton = document.querySelector("#loadButton");

    function renderDocx(file) {
        currentDocument = file;
        if (!currentDocument)
            return;

        docx.renderAsync(currentDocument, container, null, docxOptions)
            .then((x) => {
                renderThumbnails(container, document.querySelector("#thumbnails-container"));
                console.log(x);
            });
    }


    fileInput.addEventListener("change", ev => {
        renderDocx(fileInput.files[0]);
    });

    loadButton.addEventListener("click", ev => renderDocx(fileInput.files[0]));
    container.addEventListener("dragover", ev => ev.preventDefault());
    container.addEventListener("drop", ev => {
        ev.preventDefault();
        renderDocx(ev.dataTransfer.files[0]);
    });


</script>
</body>

</html>