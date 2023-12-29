package officesetproject

import grails.gorm.transactions.Transactional
import org.springframework.beans.factory.annotation.Value


@Transactional
class BootStrap {
//    加载系统配置文件中的指定的域名
    @Value('${custom.domainUrlName}')
    private String domainUrlName;


    def init = { servletContext ->
    }
    def destroy = {
    }
}
