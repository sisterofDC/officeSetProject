package officesetproject

import grails.converters.JSON
import grails.gorm.transactions.Transactional
import org.springframework.validation.BindingResult

@Transactional
class RobotsFileController {

    def index() {
        if (request.method == "POST") {
            def robotsFile = RobotsFile.findAll()
            if (robotsFile.size()>0) {
                def successResponseData = [
                        "code":200,
                        "data":robotsFile[0],
                ]
                render successResponseData as JSON
            } else {
                render ([code: 404, text: "未找到记录"] as JSON)
            }
        } else {
            render(view: "index")
        }
    }

// 保存功能
    def save() {
        def robotsFile
        def robotsFileList = RobotsFile.findAll()
        if (robotsFileList.size()>0) {
            robotsFile = robotsFileList[0]
        } else {
            robotsFile = new RobotsFile()
        }
        robotsFile.properties = params as BindingResult
        if (!robotsFile.hasErrors() && robotsFile.validate()) {
            if (robotsFile.save(failOnError: true)) {
                def result = [code: 200, text: "保存成功" ]
                render result as JSON
            } else {
                def result = [code: 500, text: "失败"]
                render result as JSON
            }
        } else {
            println robotsFile.errors
            def result = [code: 500, text: robotsFile.errors.toString().replaceAll(/["'\n]/, '')]
            render result as JSON
        }
    }
}
