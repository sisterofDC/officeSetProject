package officesetproject

import grails.converters.JSON
import org.springframework.validation.BindingResult
import org.springframework.web.multipart.MultipartFile


class FileSystemController {
    FileSystemService fileSystemService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def list(){
        if (request.method == "POST"){
            def page = (params.int('page') ?: 0)
            def limit = (params.int('limit') ?: 20)
            if (page > 0) {
                page = page - 1
            }
            params.offset = page * limit
            def criteria = FileInfo.createCriteria()
            def results = criteria.list(max: params.limit, offset: params.offset) {
                // 查询文件位置
                if(params.fileLocation){
                    eq('fileLocation', params.fileLocation)
                }
                // 查询文件夹名
                if(params.fileBucket){
                    eq('fileBucket', params.fileBucket)
                }
                // 查询文件原始名字
                if(params.fileOriginName){
                    ilike('fileOriginName', '%' + params.fileOriginName + '%')
                }
                // 查询文件KB大小
                if(params.fileSizeKb){
                    eq('fileSizeKb', params.fileSizeKb)
                }
                // 查询文件大小信息
                if(params.fileSizeInfo){
                    eq('fileSizeInfo', params.fileSizeInfo)
                }
                // 查询文件存储名
                if(params.fileObjectName){
                    eq('fileObjectName', params.fileObjectName)
                }
                // 查询文件位置
                if(params.filePath){
                    eq('filePath', params.filePath)
                }
                order('dateCreated', 'desc')
            }
            def result = [
                    code : 0, msg: "",
                    data : results,
                    count: results.totalCount
            ]
            render result as JSON
        }else {
            render(view: "list")
        }
    }

    def edit() {
        if (request.method == "POST") {
            if (params.id && params.id =~ /^[0-9]*$/) {
                def fileInfo = FileInfo.get(params.long("id"))
                if (fileInfo) {
                    def successResponseData = [
                            "code":200,
                            "data":fileInfo,
                    ]
                    render successResponseData as JSON
                } else {
                    render ([code: 404, text: "未找到记录"] as JSON)
                }
            } else {
                render ([code: 400, text: "非法请求"] as JSON)
            }
        } else {
            render(view: "edit")
        }
    }



    def save() {
        def fileInfo
        if (params.id) {
            fileInfo = FileInfo.get(params.long("id"))
            fileInfo.properties = params as BindingResult
        } else {
            fileInfo = new FileInfo(params)
        }

        if (!fileInfo.hasErrors() && fileInfo.validate()) {
            if (fileInfo.save(failOnError: true)) {
                def result = [code: 200, text: params.id ? "更新成功" : "新增成功"]
                render result as JSON
            } else {
                def result = [code: 500, text: "失败"]
                render result as JSON
            }
        } else {
            println fileInfo.errors
            def result = [code: 500, text: fileInfo.errors.toString().replaceAll(/["'\n]/, '')]
            render result as JSON
        }
    }

    def cmd() {
        if (request.method == "POST") {
            def ids = params.ids?.trim()?.tokenize(',')
            if (ids) {
                ids.each { id ->
                    if (id && id =~ /^[0-9]*$/) {
                        def fileInfo = FileInfo.get(id)
                        if (params.cmd == "delete") {
                            fileInfo?.delete(failOnError: true)
                        }
                    }
                }
            }
            render ([code: 200, text: "操作成功"] as JSON)
        } else {
            render ([code: 400, text: "请求方式错误"] as JSON)
        }
    }

    def upload(){
        if (request.method=="POST"){

        }else {
            render(view: "upload")
        }
    }

    def uploadFile(){
/*
文件上传之前需要在application.yml 中进行配置
grails:
    controllers:
        upload:
            maxFileSize: 2000000
            maxRequestSize: 2000000

把默认的文件上传大小改大

        大致思路:
        1、有统一文件夹目录生成命名
        2、上传文件后 生成的文件名 就是ID名
        3、获取文件原始名称

 */
        MultipartFile file = params["file"]
        if (file.empty) {
            def result = [code: 500, text: "失败"]
            render(result as JSON)
        }else {
            render ([code: 200,data: fileSystemService.uploadFile(file)] as JSON)
        }
    }


}
