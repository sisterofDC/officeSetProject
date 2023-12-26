package officesetproject

import grails.converters.JSON

class GenerateController {

    GenerateService generateService

    def index() {
        if (request.method == "POST"){
            def successResponseData = [
                    "code":200,
                    "data":"成功",
            ]
            render(successResponseData as JSON)
        }else {
            render(view: "index")
        }
    }

    def allDomainList(){
        render generateService.getAllDomainName() as JSON
    }


    def editBuildParameters(){
        if (request.method == "POST"){
            String domainName = params.get("domainName")
            generateService.getDomainAllInformation(domainName)
            render generateService.getAllParameters(domainName) as JSON
        }else {
            render(view: "editBuildParameters")
        }
    }


    /**
     * 初始化参数
     */
    def initParameters(){



    }


    /*
        if (request.method == "POST"){
            generateService.generateControllerFile("Member");
            def successResponseData = [
                    "code":200,
                    "data":"成功",
            ]
            render(successResponseData as JSON)
        }else {
            render(view: "index")
        }
     */


}
