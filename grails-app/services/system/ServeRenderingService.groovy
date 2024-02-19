package system


import cn.hutool.core.codec.Base64
import grails.gorm.transactions.Transactional
import officesetproject.FileInfo
import org.apache.commons.io.FileUtils
import org.grails.plugin.cache.GrailsCacheManager
import org.openqa.selenium.Dimension
import org.openqa.selenium.OutputType
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.print.PageMargin
import org.openqa.selenium.print.PageSize
import org.openqa.selenium.print.PrintOptions
import org.springframework.beans.factory.annotation.Autowired

@Transactional
class ServeRenderingService {

//    配置为单列，就不一直启动了。启动浪费事件。后面改成headLess的配置
    @Autowired
    ChromeDriver chromeDriverConfig

//    能配置在service中的
    GrailsCacheManager grailsCacheManager
    FileSystemService fileSystemService
    LibreOfficeInterfaceService libreOfficeInterfaceService

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
//        这里在看几个文件，取table的宽度，这个是转HTML 中取出来的大小，是定制化的组件
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

    /**
     * 现在把这个全部弄成future的格式，最后返回生成的链接
     * @param fileId
     * @param times
     * @return
     */
    public String preview(String fileId,Integer times){
//        因为存储ID是有的
        FileInfo fileInfo = FileInfo.findByFileId(fileId)
        if (fileInfo){
            String filePath = fileInfo.getFilePath()
            String outputDirectory = fileSystemService.getDirectoryByFileBucket(fileInfo.fileBucket)
//            创建一个新文件然后来进行监控
            String outputFilePath = outputDirectory+File.separator+fileInfo.fileId+".html"
            File htmlFile = new File(outputFilePath)
            Boolean createResult = htmlFile.createNewFile()
            if (createResult){
//                把没有成功的错误整合进去
                Long intervalSet = 500L
                FileMonitor fileMonitorDirectory = new FileMonitor(500L)
                fileMonitorDirectory.monitor(outputDirectory,new LibreOfficeInterfaceService.MyFileMonitor(grailsCacheManager))
                fileMonitorDirectory.start()
                def myCache = grailsCacheManager.getCache('myCache')
                myCache.put("isFinished",1)
                FileMonitor fileMonitorFile = new FileMonitor(500L)
                fileMonitorFile.monitor(outputFilePath,new LibreOfficeInterfaceService.MyFileMonitor(grailsCacheManager))
                fileMonitorFile.start()
                libreOfficeInterfaceService.excelToHtml(filePath,outputDirectory)
                for (i in 0..<times) {
                    Integer progress = myCache.get("isFinished",Integer)
                    println("现在进行"+progress)
                    if (progress==2){
//                        如果等于2那么就开始转换为PDF了
//                        http://localhost/fileSystem/defaultBucket/+ 加上转化的名字
                        String openUrl= " http://localhost/fileSystem/defaultBucket/"+fileId+".html"
                        chromeDriverPrintPDF(openUrl,fileInfo)
                        break
                    }
                    Thread.sleep(intervalSet)
                }
                fileMonitorDirectory.stop()
                fileMonitorFile.stop()
//            实在不行，就弄成thread 用来处理 future
            }
            return "正在转换"
        }else {
            return "文件不存在"
        }
    }


}

