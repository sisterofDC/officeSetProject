package officesetproject

import grails.converters.JSON

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Future

class SaveSqlFileController {
    SaveSqlFileService saveSqlFileService
    private final Map<String, Future<String>> futureMap = new ConcurrentHashMap<>()

//    生成sql文件的任务ID
    def getCreateNewSqlFile(){
        Future<String> future = saveSqlFileService.asyncMethodWithReturnType()
        String taskId = UUID.randomUUID().toString()
        futureMap.put(taskId, future)
        render(taskId)
    }

    //    通过查询任务ID看任务是否完成
    def taskIsOver(){
        String taskId = params.get("taskId")
//        拿到任务ID
        Future<String> future = futureMap.get(taskId);
        if (future!=null){
            if (future.isDone()){
                render("文件生成成功")
            }else {
                render("任务没有完成")
            }
        }else {
            render("错误读取任务")
        }
    }

//    查看所有的sql 文件
    def list(){
//        生成的映射Map再进行返回
        Map<Integer,SaveSqlFileService.SqlFileSet> fileMap = new HashMap<>()
        saveSqlFileService.initIndexToSqlFile(fileMap)
        List<SaveSqlFileService.SqlFileSet> fileList = new ArrayList<>(fileMap.values())
        def result = [
                code : 0,
                msg: "",
                data : fileList,
                count: fileList.size()
        ]
        render result as JSON
    }

//    删除已经优化，传的是隐射的id值
    def delete(){
        def objID =  params.id.trim().tokenize(',')
        for (def i = 0; i < objID.size(); i++) {
            if (objID[i] && objID[i] =~ /^[0-9]*$/) {
                Integer id = objID[i] as Integer
                saveSqlFileService.deleteSqlFile(id)
            }else {
                render(([status: 200, msg: "删除失败"]) as JSON)
            }
        }
        render(([status: 200, msg: "ok"]) as JSON)
    }

//    搜索
    def search(){
        String key = params.get("key")
        if (key!=""&&key!=null){
//            搜索的是fileName不是进数据库
            List<SaveSqlFileService.SqlFileSet> fileList = saveSqlFileService.searchFileList(key)
            def result = [
                    code : 0,
                    msg: "",
                    data : fileList,
                    count: fileList.size()
            ]
            render result as JSON
        }else {
            render(([status: 500, msg: "fail"]) as JSON)
        }
    }

    def index() { }
}
