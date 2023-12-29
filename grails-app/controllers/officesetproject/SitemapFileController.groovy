package officesetproject

import grails.converters.JSON
import grails.gorm.transactions.Transactional
import org.springframework.beans.factory.annotation.Value


@Transactional
class SitemapFileController {


    def index() {
        if (request.method == "POST") {
            def sitemapFile = SitemapFile.findAll();
            if (sitemapFile.size()>0) {
                def successResponseData = [
                        "code":200,
                        "data":sitemapFile[0],
                ]
                render successResponseData as JSON
            } else {
                render ([code: 404, text: "未找到记录"] as JSON)
            }
        } else {
            render(view: "index")
        }
    }

    def save() {
        def sitemapFile
        def sitemapFileList = SitemapFile.findAll()
        if (sitemapFileList.size()>0) {
            sitemapFile = sitemapFileList[0];
        } else {
            sitemapFile = new SitemapFile();
        }
        sitemapFile.properties = params
        if (!sitemapFile.hasErrors() && sitemapFile.validate()) {
            if (sitemapFile.save(failOnError: true)) {
                def result = [code: 200, text: "保存成功"]
                render result as JSON
            } else {
                def result = [code: 500, text: "失败"]
                render result as JSON
            }
        } else {
            println sitemapFile.errors
            def result = [code: 500, text: sitemapFile.errors.toString().replaceAll(/["'\n]/, '')]
            render result as JSON
        }
    }
}
