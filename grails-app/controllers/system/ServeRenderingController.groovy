package system

import grails.converters.JSON
import officesetproject.FileInfo

class ServeRenderingController {
    ServeRenderingService serveRenderingService

    def index() {

    }

    def beginConvert(){
        if (request.method == "POST") {
            def fileId = params.get("fileId").toString()
            def monitorTimeOutTimes = 10
            if (fileId){
                render ([code: 200,text:serveRenderingService.preview(fileId,monitorTimeOutTimes)] as JSON)
            }else {
                render ([code: 400, text: "请求参数错误"] as JSON)
            }
        } else {
            render ([code: 400, text: "请求方式错误"] as JSON)
        }
    }
}
