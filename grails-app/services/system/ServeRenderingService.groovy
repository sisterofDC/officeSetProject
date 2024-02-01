package system


import cn.hutool.core.codec.Base64
import cn.hutool.core.io.FileUtil

import grails.gorm.transactions.Transactional
import officesetproject.FileInfo
import org.apache.commons.io.FileUtils
import org.openqa.selenium.Dimension
import org.openqa.selenium.OutputType
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.print.PageMargin
import org.openqa.selenium.print.PageSize
import org.openqa.selenium.print.PrintOptions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mock.web.MockMultipartFile
import org.springframework.web.multipart.MultipartFile


@Transactional
class ServeRenderingService {

//    配置为单列，就不一直启动了
    @Autowired
    ChromeDriver chromeDriverConfig

    FileSystemService fileSystemService

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


    def getMessageFromInternet(){
        chromeDriverConfig.get("https://mp.weixin.qq.com/s/iplqKrIyJHAI-A779mO2qQ")
        Thread.sleep(2000);



    }

    /**
     * 将LibreOffice的配置中的转换的文件，转成单页的PDF，这里用的print的功能
     * @param serverUrl 已近转化为html文件的地址，对接的是
     * @param fileInfo 转换完为文件系统的映射类
     * @return
     */
    def chromeDriverPrintPDF(String serverUrl,FileInfo fileInfo){
//        打开已经转换后的文件设置路径，（这里提醒，LibreOffice 转出来 再到 系统映射需要时间 所以这个函数启动需要前置时间）
        chromeDriverConfig.get(serverUrl)
//        等待服务器端模拟的加载
        Thread.sleep(2000);
//        这里还有细节调节。
//        这里在看几个文件，取table的宽度
        def width =  chromeDriverConfig.executeScript("return document.querySelector('table').offsetWidth")
//        这里取高度
        def height= chromeDriverConfig.executeScript("return document.querySelector('html').offsetHeight");
//        将取出来进行调整
        println("高度："+height* 0.0264583333 +"cm");
        println("宽度："+width* 0.0264583333 +"cm");
        width = width* 0.0264583333
        height = height * 0.0264583333
//        设置打印的宽度和区间。
        PageSize pageSize = new PageSize(height,width)
//        开始设置打印的参数
        PrintOptions printOptions = new PrintOptions()
//        把边框调整为0
        printOptions.setPageMargin(new PageMargin(0,0,0,0))
        printOptions.setPageSize(pageSize)
//        设置为竖向打印
        printOptions.setOrientation(PrintOptions.Orientation.PORTRAIT)
        def pdf =  chromeDriverConfig.print(printOptions)
//        看封装的pdf 类， 是一个base64EncodedPdf 转为byte[]

        byte[] pdfBytes = Base64.decode(pdf.getContent())
        InputStream inputStream = new ByteArrayInputStream(pdfBytes);
//        转为multipartFile然后，转为pdf后缀，最后放在pdf文件夹里面
        fileSystemService.uploadFile(
                fileSystemService.convertToMultipartFileByInputStream(inputStream,updateFileName(fileInfo.getFileOriginName())),
                "PDF")
    }

    /**
     * 私有方法转换为PDF
     * @param fileName
     * @return
     */
    private static String updateFileName(String fileName) {
        def fileNameSplit = fileName.split('\\.')
        if (fileNameSplit.size() > 1) {
            // 如果有后缀, 变为 ".pdf"
            def newFileName = fileNameSplit[0..-2].join('.')
            return "${newFileName}.pdf"
        } else {
            // 没有后缀就直接为加
            return "${fileName}.pdf"
        }
    }





}

