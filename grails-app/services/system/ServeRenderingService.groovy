package system

import grails.gorm.transactions.Transactional
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions

@Transactional
class ServeRenderingService {

    def chromeDriverSet(){
        System.setProperty("webdriver.chrome.driver","D:\\chrome\\chromedriver-win64\\chromedriver.exe")
        ChromeOptions chromeOptions = new ChromeOptions()
        chromeOptions.setBinary("D:\\chrome\\chrome-win64\\chrome.exe")
        WebDriver chromeDriver = new ChromeDriver(chromeOptions)
//        这里搞一下
        chromeDriver.get("http://localhost/fileSystem/defaultBucket/1750823434284003328.html")
    }
}
