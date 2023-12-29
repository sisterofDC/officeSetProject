package officesetproject

import grails.converters.JSON
import grails.gorm.transactions.Transactional

@Transactional
class KeyWordCodeController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index() {
        if (request.method == "POST") {
            def keyWordCode = KeyWordCode.findAll()
            if (keyWordCode.size()>0){
                def successResponseData = [
                        "code":200,
                        "data":keyWordCode[0],
                ]
                render successResponseData as JSON
            }else {
                render ([code: 404, text: "未找到记录"] as JSON)
            }
        } else {
            render(view: "index")
        }
    }

// 保存功能
    def save() {
        def keyWordCode
        def keyWordCodeList = KeyWordCode.findAll()
        if (keyWordCodeList.size()>0) {
            keyWordCode = keyWordCodeList[0];
        } else {
            keyWordCode = new KeyWordCode();
        }
        keyWordCode.properties = params
        if (!keyWordCode.hasErrors() && keyWordCode.validate()) {
            if (keyWordCode.save(failOnError: true)) {
                def result = [code: 200, text: "保存成功"]
                render result as JSON
            } else {
                def result = [code: 500, text: "失败"]
                render result as JSON
            }
        } else {
            println keyWordCode.errors
            def result = [code: 500, text: keyWordCode.errors.toString().replaceAll(/["'\n]/, '')]
            render result as JSON
        }
    }

}
