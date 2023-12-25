package officesetproject

import grails.converters.JSON

class GenerateController {
//    Application 中的 @Bean注册的单列
    def GenerateService generateService

    def index() {
//        展示所有的数据库
        if (request.method == "POST"){
            generateService.getDomainAllInformation("Member");
            def successResponseData = [
                    "code":200,
                    "data":"成功",
            ]
            render(successResponseData as JSON)
        }else {
            render(view: "index")
        }
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
