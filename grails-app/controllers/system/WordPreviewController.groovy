package system

import cn.hutool.core.util.StrUtil
import grails.converters.JSON
import officesetproject.FileInfo
import org.springframework.web.multipart.MultipartFile

class WordPreviewController {
    WordPreviewService wordPreviewService
    FileSystemService fileSystemService
    LibreOfficeInterfaceService libreOfficeInterfaceService
    def pdfToWord(){
        if (request.method=="POST"){

        }else {
          render(view: "pdfToWord")
        }
    }

    def index() { }

    /**
     * 在线预览
     */
    def onlinePreview(){
        if (request.method=="POST"){

        }else {
            render(view: "onlinePreview")
        }
    }

    def convert(){
        //        这里先对接文件上传的接口
        MultipartFile file = params["file"]
        if (file.empty){
            def result = [code: 500, text: "失败"]
            render(result as JSON)
        }else {
            String fileId = fileSystemService.uploadFile(file,"onlinePreview")
            if (fileId!="-1"){
//                拿到fileInfo
                FileInfo fileInfo = FileInfo.findByFileId(fileId)
//                不加参数的
//                libreOfficeInterfaceService.wordToPDF(fileInfo.filePath,fileSystemService.getDirectoryByFileBucket(fileInfo.fileBucket))
                Map<String,String> parametersMap = new HashMap<String,String>()
//                更多的参数需要阅读官方文档
                parametersMap.put("Quality","{\"type\":\"long\",\"value\":\"100\"}")
                parametersMap.put("IsSkipEmptyPages","{\"type\":\"boolean\",\"value\":\"true\"}")
                libreOfficeInterfaceService.wordToPDFWithParameter(fileInfo.filePath,fileSystemService.getDirectoryByFileBucket(fileInfo.fileBucket),parametersMap)
                def result = [code: 200, text: "文件转换成功"]
                render(result as JSON)
            }else {
                def result = [code: 500, text: "文件上传失败"]
                render(result as JSON)
            }
        }
    }
}
