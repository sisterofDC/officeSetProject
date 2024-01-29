package system

import grails.converters.JSON
import system.ExcelToolsService

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


    /**
     * 转为HTML 然后转成 PDF
     */
    def convertToHTML(){

    }

    /**
     * 导出所有数据
     */
    def ExportAllData(){

    }

    /**
     * 导出为PDF
     */
    def excelToPDF(){

    }
}
