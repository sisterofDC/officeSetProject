package officesetproject

import grails.converters.JSON

class GenerateController {
    def GenerateService generateService

    def index() {
        if (request.method == "POST"){
            generateService.generateTest();
            def successResponseData = [
                    "code":200,
                    "data":"成功",
            ]
            render(successResponseData as JSON)
        }else {
            render(view: "index")
        }
    }
}
