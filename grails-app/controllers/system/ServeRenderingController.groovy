package system

import grails.converters.JSON
import officesetproject.FileInfo

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Future

class ServeRenderingController {
    ServeRenderingService serveRenderingService
    WordPreviewService wordPreviewService
    FileSystemService fileSystemService
    private final Map<String, Future<LinkedHashMap<String,String>>> futureMap = new ConcurrentHashMap<>()

    def index() {

    }


    def zipDownload(){
        if (request.method == "POST") {
            def convertIds = params.convertIds?.trim()?.tokenize(',')
            if (convertIds) {
                List<FileInfo> fileInfoList = new ArrayList<>()
                convertIds.each { fileId ->
                    if (fileId && fileId =~ /^[0-9]*$/) {
                        fileInfoList.add(FileInfo.findByFileId(fileId))
                    }
                }
                fileSystemService.batchZipDownload(fileInfoList,response)
            }
        } else {
            render ([code: 400, text: "请求方式错误"] as JSON)
        }
    }


    def beginConvert(){
        if (request.method == "POST") {
            def fileId = params.get("fileId").toString()
            def monitorTimeOutTimes = 10
            if (fileId){
                Future future = serveRenderingService.asyncMethodWithReturnType(fileId,monitorTimeOutTimes)
                String taskId = UUID.randomUUID().toString()
                futureMap.put(taskId, future)
                def successResponseData = [
                        "code":200,
                        "data":taskId,
                ]
                render(successResponseData as JSON)
            }else {
                render ([code: 400, text: "请求参数错误"] as JSON)
            }
        } else {
            render ([code: 400, text: "请求方式错误"] as JSON)
        }
    }

    def taskIsOver(){
        if (request.method == "POST") {
            String taskId = params.get("taskId")
//        拿到任务ID
            Future<LinkedHashMap<String,String>> future = futureMap.get(taskId);
            if (future!=null){
                if (future.isDone()){
                    LinkedHashMap<String,String> pdfFile = future.get()
                    futureMap.remove("taskId")
                    if (pdfFile.get("status")=="1"){
                        String fileId = pdfFile.get("code")
                        def successResponseData = [
                                "code":200,
                                "file":fileId,
                                "data":"finished",
                                "text":"文件生成成功",
                        ]
                        render(successResponseData as JSON)
                    }else {
                        String errorMessage = pdfFile.get("code")
                        def errorResponseData = [
                                "code":500,
                                "text":errorMessage,
                        ]
                        render(errorResponseData as JSON)
                    }
//                查看最后的生成ID
                }else {
                    def successResponseData = [
                            "code":200,
                            "data":"unfinished",
                            "text":"任务没有完成",
                    ]
                    render(successResponseData as JSON)
                }
            }else {
                def errorResponseData = [
                        "code":500,
                        "text":"任务请求错误",
                ]
                render(errorResponseData as JSON)
            }
        }else {
            render ([code: 400, text: "请求方式错误"] as JSON)
        }
    }


    def jacobRender(){
        def fileId = params.get("fileId").toString()
        FileInfo fileInfo = FileInfo.findByFileId(fileId)
        String source = fileInfo.getFilePath()
        String target =  serveRenderingService.updateFileNameToPDF(fileInfo.getFilePath())
        println(source)
        println(target)
        wordPreviewService.getPDFFile(source,target)
        def successResponseData = [
                "code":200,
                "data":"开始转换",
        ]
        render(successResponseData as JSON)
    }

}
