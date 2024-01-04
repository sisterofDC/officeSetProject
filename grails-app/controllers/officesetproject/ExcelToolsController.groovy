package officesetproject

import grails.converters.JSON

class ExcelToolsController {
    ExcelToolsService excelToolsService
    def index() {
        if (request.method=="POST"){
            excelToolsService.createNewWorkbook()
        }else {
            render(view: "index")
        }
    }


    def createSheet(){
        if (request.method=="POST"){
            excelToolsService.createSheet()
            def successResponseData = [
                    "code":200,
                    "text":"生成成功",
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

    def createCells(){
        if (request.method=="POST"){
            excelToolsService.createCells()
            def successResponseData = [
                    "code":200,
                    "text":"生成成功",
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
