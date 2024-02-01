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
            wordPreviewService.convertToWord()
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
            String fileObjectName = fileSystemService.uploadFile(file,"onlinePreview")
            if (fileObjectName!="-1"){
//                拿到fileInfo
                FileInfo fileInfo = FileInfo.findByFileObjectName(fileObjectName)
                String changeFileNamePath = fileInfo.filePath
                changeFileNamePath = StrUtil.removeSuffix(changeFileNamePath,"."+fileInfo.fileSuffix)
                changeFileNamePath = changeFileNamePath+"."+"pdf"
                libreOfficeInterfaceService.wordToPDF(fileInfo.filePath,fileSystemService.getDirectoryByFileBucket(fileInfo.fileBucket),changeFileNamePath)
                def result = [code: 200, text: "文件转换成功"]
                render(result as JSON)
            }else {
                def result = [code: 500, text: "文件上传失败"]
                render(result as JSON)
            }
        }
    }
}
