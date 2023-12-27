package officesetproject

import grails.converters.JSON

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
     * 开始生成
     */
    def beginGenerate(){
        if (request.method == "POST"){
            String domainName = params.get("domainName")
            generateService.generateListGSPFile(domainName)
            generateService.generateEditGSPFile(domainName)
            def successResponseData = [
                    "code":200,
                    "data":"成功",
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



}
