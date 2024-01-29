package officesetproject

import grails.gorm.transactions.Transactional
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.springframework.beans.factory.annotation.Value


@Transactional
class BootStrap {

    def init = { servletContext ->


    }

    def destroy = {

    }


}
