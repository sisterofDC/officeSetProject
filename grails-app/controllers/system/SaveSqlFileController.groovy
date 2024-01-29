package system

import grails.converters.JSON

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Future

class SaveSqlFileController {
    SaveSqlFileService saveSqlFileService
    private final Map<String, Future<String>> futureMap = new ConcurrentHashMap<>()

//    生成sql文件的任务ID
    def getCreateNewSqlFile(){
        Future future = saveSqlFileService.asyncMethodWithReturnType()
        String taskId = UUID.randomUUID().toString()
        futureMap.put(taskId, future)
        def successResponseData = [
                "code":200,
                "data":taskId,
        ]
        render(successResponseData as JSON)
    }

    //    通过查询任务ID看任务是否完成
    def taskIsOver(){
        String taskId = params.get("taskId")
//        拿到任务ID
        Future<String> future = futureMap.get(taskId);
        if (future!=null){
            if (future.isDone()){
                def successResponseData = [
                        "code":200,
                        "data":"done",
                        "text":"文件生成成功",
                ]
                render(successResponseData as JSON)
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
    }

    def index() { }
}
