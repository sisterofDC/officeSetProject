package system

import grails.converters.JSON
import officesetproject.Generate

class GenerateController {

    GenerateService generateService

    def index() {
        if (request.method == "POST"){
            render generateService.getAllDomainName() as JSON
        }else {
            render(view: "index")
        }
    }


    def editBuildParameters(){
        if (request.method == "POST"){
            String domainName = params.get("domainName")
//            如果有，直接返回，如果没有先生成
            if(generateService.getAllParameters(domainName).size()>0){
                render generateService.getAllParameters(domainName) as JSON
            }else {
                generateService.getDomainAllInformation(domainName)
                render generateService.getAllParameters(domainName) as JSON
            }
        }else {
            render(view: "editBuildParameters")
        }
    }


    /**
     * 保存参数
     */
    def save(){
        if (request.method == "POST"){
            def generate = Generate.get(params.long("id"))
            generate.properties = params
            generateService.save(generate)
            def successResponseData = [
                    "code":200,
                    "text":"数据更新成功",
            ]
            render(successResponseData as JSON)
        }else {
            def errorResponseData = [
                    "code":400,
                    "text":"请求方式错误",
            ]
            render(errorResponseData as JSON)
        }
    }

    /**
     * 开始生成到项目目录，可以直接改到生成到项目文件里面
     */
    def beginGenerate(){
        if (request.method == "POST"){
            String domainName = params.get("domainName")
//            生成对应的
            def list = generateService.generateListGSPFile(domainName)
            generateService.generateFunctionToFile("list.ftl", list["ftlInputData"],list["fileName"],domainName)
            def edit =generateService.generateEditGSPFile(domainName)
            generateService.generateFunctionToFile("edit.ftl",edit["ftlInputData"],edit["fileName"],domainName)
            def controller =generateService.generateControllerGroovyFile(domainName)
            generateService.generateFunctionToFile("Controller.groovy.ftl",controller["ftlInputData"],controller["fileName"],domainName)
            def batchUpload = generateService.generateBatchUploadFile(domainName)
            generateService.generateFunctionToFile("batchUpload.ftl",batchUpload["ftlInputData"],controller["fileName"],domainName)
            def successResponseData = [
                    "code":200,
                    "text":"代码生成成功",
            ]
            render(successResponseData as JSON)
        }else {
            def errorResponseData = [
                    "code":400,
                    "text":"请求方式错误",
            ]
            render(errorResponseData as JSON)
        }
    }

    /**
     * 保存为ZIP文件
     * @return zip 压缩包
     */
    def zipFile(){
        if (request.method == "POST"){
            String domainName = params.get("domainName")
            def list = generateService.generateListGSPFile(domainName)
            def edit =generateService.generateEditGSPFile(domainName)
            def controller =generateService.generateControllerGroovyFile(domainName)
            def batchUpload = generateService.generateBatchUploadFile(domainName)
            List<String> templateFiles = [list["templateFile"], edit["templateFile"], controller["templateFile"],batchUpload["templateFile"]]
            List<Map<String, Object>> objs = [list["ftlInputData"], edit["ftlInputData"], controller["ftlInputData"],batchUpload["ftlInputData"]]
            List<String> fileNames = [list["fileName"], edit["fileName"], controller["fileName"],batchUpload["fileName"]]
            generateService.generateFunctionsToZipAndSendResponse(templateFiles,objs,fileNames,response)
        }else {
            def errorResponseData = [
                    "code":400,
                    "text":"请求方式错误",
            ]
            render(errorResponseData as JSON)
        }
    }
}
