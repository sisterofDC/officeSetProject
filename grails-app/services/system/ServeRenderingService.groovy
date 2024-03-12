package system


import cn.hutool.core.codec.Base64
import cn.hutool.core.util.IdUtil
import grails.gorm.transactions.Transactional
import officesetproject.FileInfo
import org.grails.plugin.cache.GrailsCacheManager
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.print.PageMargin
import org.openqa.selenium.print.PageSize
import org.openqa.selenium.print.PrintOptions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.AsyncResult

import java.util.concurrent.Future

@Transactional
class ServeRenderingService {

//    能配置在service中的
    GrailsCacheManager grailsCacheManager
    FileSystemService fileSystemService
    LibreOfficeInterfaceService libreOfficeInterfaceService

    public ChromeDriver chromeDriverInit(){
//        这个配置 driver 需要和 对应的 chrome 对齐 然后jar包驱动是
        System.setProperty("webdriver.chrome.driver","D:\\chrome\\chromedriver-win64\\chromedriver.exe")
        ChromeOptions chromeOptions = new ChromeOptions()
//        无头设置
//        chromeOptions.addArguments("--headless")
        chromeOptions.addArguments("--allow-running-insecure-content")
        chromeOptions.addArguments("--remote-allow-origins=*")
        chromeOptions.addArguments("--start-maximized")
        chromeOptions.addArguments("--disable-gpu")
        chromeOptions.setBinary("D:\\chrome\\chrome-win64\\chrome.exe")
        ChromeDriver chromeDriver = new ChromeDriver(chromeOptions)

        return chromeDriver
    }


    /**
     * 将LibreOffice的配置中的转换的文件，转成单页的PDF，这里用的print的功能，print就可以直接输出为PDF，也可以做预览。做预览就再转换为PNG。
     * @param serverUrl 已近转化为html文件的地址，对接的是
     * @param fileInfo 转换完为文件系统的映射类
     * @return String 保存的文件ID
     */
    String chromeDriverPrintPDF(String serverUrl,FileInfo fileInfo){
//        打开已经转换后的文件设置路径，（这里提醒，LibreOffice 转出来 再到 系统映射需要时间 所以这个函数启动需要前置时间）
        ChromeDriver chromeDriverConfig =  chromeDriverInit()
//        等待新的加载
        Thread.sleep(2000);
        chromeDriverConfig.get(serverUrl)
//        等待服务器端模拟的加载
        Thread.sleep(3000);
//        这里还有细节调节。
//        这里在看几个文件，取table的宽度，这个是libreOffice转HTML 中html页面中，取出来的大小，是定制化的组件。需要去看到底是取那个部分
//        这里是取页面宽度。整合页面太宽了
        def width =  chromeDriverConfig.executeScript("return document.querySelector('table').scrollWidth")
//        这里取页面高度
        def height= chromeDriverConfig.executeScript("return document.querySelector('table').scrollHeight");
//        将取出来进行调整，变成打印的厘米数
        println("高度："+height* 0.0264583333 +"cm");
        println("宽度："+width* 0.0264583333 +"cm");
//        进行转换
        width = width* 0.0264583333
        height = height * 0.0264583333
//        留个2cm的高
        height= height+2.00
//        设置打印的宽度和区间。
        PageSize pageSize = new PageSize(height,width)
//        开始设置打印的参数
        PrintOptions printOptions = new PrintOptions()
//        把边框调整为0
        printOptions.setPageMargin(new PageMargin(0,0,0,0))
//        把纸张大小带进去
        printOptions.setPageSize(pageSize)
//        设置为竖向打印
        printOptions.setOrientation(PrintOptions.Orientation.PORTRAIT)
//        开始打印，这个这个是模拟器的类
        def pdf =  chromeDriverConfig.print(printOptions)
//        看封装的pdf 类， 是一个base64EncodedPdf 转为byte[]
        byte[] pdfBytes = Base64.decode(pdf.getContent())
//        将inputStream塞进去MultipartFile 存进去
        InputStream inputStream = new ByteArrayInputStream(pdfBytes);
//        转为multipartFile然后，转为pdf后缀，最后放在pdf文件夹里面。
        String fileId =  fileSystemService.uploadFile(
                fileSystemService.convertToMultipartFileByInputStream(inputStream,updateFileNameToPDF(fileInfo.getFileOriginName())),
                "PDF")
        chromeDriverConfig.close()
        return fileId
    }

    /**
     * 私有方法转换为PDF，把后缀转换为
     * @param fileName
     * @return
     */
    static String updateFileNameToPDF(String fileName) {
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

    static String updateFileNameToHTML(String fileName) {
        def fileNameSplit = fileName.split('\\.')
        if (fileNameSplit.size() > 1) {
            // 如果有后缀, 变为 ".html"
            def newFileName = fileNameSplit[0..-2].join('.')
            return "${newFileName}.html"
        } else {
            // 没有后缀就直接为加
            return "${fileName}.html"
        }
    }


    @Async
    Future asyncMethodWithReturnType(String fileId,Integer times) {
        System.out.println("Execute method asynchronously - " + Thread.currentThread().getName())
        try {
            return new AsyncResult(preview(fileId,times))
        } catch (InterruptedException e) {
            log.println(e)
        }
        return null
    }


    /**
     * 现在把这个全部弄成future的格式，最后返回生成的链接，这里是
     * @param fileId 文件ID
     * @param times 轮训次数 间隔为500L 0.5秒
     * @return
     */
    LinkedHashMap<String,String> preview(String fileId,Integer times){
//        因为存储ID是有的，分为多个步骤。
        FileInfo fileInfo = FileInfo.findByFileId(fileId)
//        如果查询有这个，中间判断的条件还多，如果后缀是xls等能够进行转换的
//        待办事宜（TODO）:（ 王绎新，2024年2月19日，[添加更多的错误判断]）
        if (fileInfo){
//            把路径取出来，用于生成命令中的源文件
            String filePath = fileInfo.getFilePath()
//            去出文件夹目录，用于输出文件目录 --outdir 的目录，也用于监控文件生成
            String outputDirectory = fileSystemService.getDirectoryByFileBucket(fileInfo.fileBucket)
//            创建一个新文件然后来进行监控，如果直接监控这个文件很容易报null错，直接让生成文件去覆盖这个
            String outputFilePath = outputDirectory+File.separator+fileInfo.fileId+".html"
//            新文件
            File htmlFile = new File(outputFilePath)
//            创建新的文件创建成功的回调
            Boolean createResult = htmlFile.createNewFile()
//            如果创建成功，开始进行转换和监控
            if (createResult){
//                把没有成功的错误整合进去
//                待办事宜（TODO）:（ 王绎新，2024.02.19，[把超时，等错误需要整合进去]）
//                间隔时间
                Long intervalSet = 500L
//                把改变的状态放入内存管理，因为是监控状态，没有回调，直接采用的轮询方式
                def myCache = grailsCacheManager.getCache(fileId)
                myCache.put("isFinished",1)
//                两个监控的初始化，将内存管理的和监控实现类代入进去
                FileMonitor fileMonitorDirectory = new FileMonitor(500L)
                fileMonitorDirectory.monitor(outputDirectory,new LibreOfficeInterfaceService.MyFileMonitor(grailsCacheManager,fileId))
                fileMonitorDirectory.start()
                FileMonitor fileMonitorFile = new FileMonitor(500L)
                fileMonitorFile.monitor(outputFilePath,new LibreOfficeInterfaceService.MyFileMonitor(grailsCacheManager,fileId))
                fileMonitorFile.start()
//                用libreOffice开始转换，这里有IO 操作怕出错。因为无论怎么样都需要关闭监控器
                try{
                    libreOfficeInterfaceService.excelToHtml(filePath,outputDirectory)
                    // 开始轮询
                    for (i in 0..<times) {
                        Integer progress = myCache.get("isFinished",Integer)
                        println("现在进行"+progress)
//                    在MyFileMonitor 中状态改变的时候就是生成完成了
                        if (progress==2){
//                        如果等于2那么就开始转换为PDF了
//                        http://localhost/fileSystem/defaultBucket/+ 加上转化的名字 就是浏览器模拟的打开的url
                            String openUrl= " http://localhost/fileSystem/defaultBucket/"+fileId+".html"
//                        开始进行下一步的转换
                            String pdfFileId = chromeDriverPrintPDF(openUrl,fileInfo)
                            htmlFileIntoFileSystem(updateFileNameToHTML(fileInfo.fileOriginName),fileId+".html",fileInfo.fileBucket,outputFilePath,htmlFile.size())
                            return ["status":"1","code":pdfFileId]
                        }
                        Thread.sleep(intervalSet)
                    }
//                如果10次轮询后，都是1，都没有成功，转换。那么就是超时了
                    Integer endResultCheck = myCache.get("isFinished",Integer)
                    if (endResultCheck==1){
                        return ["status":"2","code":"转换失败，查看文件"]
                    }
                }catch (Exception e){
                    println(e)
                }finally {
//                最后监控的都要停止
                    println("关闭监控")
                    myCache.clear()
                    fileMonitorDirectory.stop()
                    fileMonitorFile.stop()
                }
            }else {
                return ["status":"2","code":"转换初始化失败"]
            }
            return ["status":"2","code":"转换错误"]
        }else {
            return ["status":"2","code":"文件不存在"]
        }
    }

    /**
     * 把生成的html塞进文件管理系统
     * @param originalFilename
     * @param saveName
     * @param fileBucket
     * @param savePath
     * @param fileSizeKb
     */
    void htmlFileIntoFileSystem(String originalFilename,String saveName,String fileBucket,String savePath,Long fileSizeKb){
        String fileId= IdUtil.getSnowflakeNextIdStr()
        FileInfo fileInfo = new FileInfo()
        //        填写对应的字段
        fileInfo.fileOriginName = originalFilename
        fileInfo.fileSuffix = ".html"
        fileInfo.fileSizeKb = fileSizeKb
        fileInfo.fileSizeInfo = fileSystemService.convertFileSizeToKB(fileSizeKb)
        fileInfo.fileId = fileId
        fileInfo.fileObjectName = saveName
        fileInfo.fileBucket = fileBucket
        fileInfo.filePath = savePath
        fileInfo.fileLocation= 1
//        如果需要创建这个，需要整合安全系统里面
        fileInfo.createUser = "generateByLibreoffice"
        fileInfo.save(failOnError: true)
    }





}

