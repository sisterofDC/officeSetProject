package system

import cn.hutool.core.*
import cn.hutool.core.codec.Base64
import cn.hutool.core.io.FileUtil
import grails.gorm.transactions.Transactional
import org.apache.commons.io.FileUtils
import org.openqa.selenium.Dimension
import org.openqa.selenium.OutputType
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.print.PageMargin
import org.openqa.selenium.print.PageSize
import org.openqa.selenium.print.PrintOptions
import org.springframework.beans.factory.annotation.Autowired

@Transactional
class ServeRenderingService {

//    配置为单列，就不一直启动了
    @Autowired
    ChromeDriver chromeDriverConfig

    def chromeDriverSet(){
//        这里开始打开chrome
        chromeDriverConfig.get("http://localhost/fileSystem/defaultBucket/1750823434284003328.html")
//        等待加载
        Thread.sleep(2000);
//        这里等待的是
        Long width = (Long) chromeDriverConfig.executeScript("return document.querySelector('table').clientWidth")
        Long height =(Long) chromeDriverConfig.executeScript("return document.querySelector('html').clientHeight");
        println("高度："+height);
        println("宽度："+width);
        /**
         *
         */
        chromeDriverConfig.manage().window().setSize(new Dimension(width.intValue(), height.intValue()));
        File srcFile = chromeDriverConfig.getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(srcFile,new File("D:\\GenerateFile\\002.png"))
    }

    def chromeDriverPrint(){
        chromeDriverConfig.get("http://localhost/fileSystem/defaultBucket/1750823434284003328.html")
        Thread.sleep(2000);
        def width =  chromeDriverConfig.executeScript("return document.querySelector('table').clientWidth")
        def height= chromeDriverConfig.executeScript("return document.querySelector('html').clientHeight");
        println("高度："+height* 0.0264583333 );
        println("宽度："+width* 0.0264583333 );
        width = width* 0.0264583333
        height = height * 0.0264583333
        PageSize pageSize = new PageSize(height,width)
        PrintOptions printOptions = new PrintOptions()
        printOptions.setPageMargin(new PageMargin(0,0,0,0))
        printOptions.setPageSize(pageSize)
        printOptions.setOrientation(PrintOptions.Orientation.PORTRAIT)
        def pdf =  chromeDriverConfig.print(printOptions)
        byte[] pdfBytes = Base64.decode(pdf.getContent())
        FileUtil.writeBytes(pdfBytes,"D:\\GenerateFile\\002.pdf");
    }


}

