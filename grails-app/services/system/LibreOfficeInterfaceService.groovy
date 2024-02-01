package system

import cn.hutool.core.io.watch.SimpleWatcher
import cn.hutool.core.io.watch.WatchMonitor
import cn.hutool.core.io.watch.watchers.DelayWatcher
import cn.hutool.core.util.RuntimeUtil
import cn.hutool.core.util.StrUtil
import grails.gorm.transactions.Transactional
import org.springframework.beans.factory.annotation.Autowired

import java.nio.file.Path
import java.nio.file.WatchEvent


/**
 * 这是一个软件工具类，多数方法是调用的其他软件的参数，并不是JAVA的原生，需要大量的配置与设定
 * 安装  LibreOffice  https://www.libreoffice.org/download/download-libreoffice/
 * 安装  imagemagick  https://www.imagemagick.org/script/download.php  imagemagick 功能并不齐全
 * 安装  ghostscript  https://ghostscript.com/releases/gsdnld.html 这个安装后才能 pdf to png
 * 在线预览的大致思路，
 * （1）、对应word文件预览 word to pdf to png to html <img src="">
 * （2）、对于excel文件 两种方式 1、excel to html 然后直接渲染回去 然后在 img  2、excel to single PDF to img （如果内容已经无法显示了，转出来还是有问题）
 * （3）、对应PPT文件 里面东西太多了，我没法展示。
 */
@Transactional
class LibreOfficeInterfaceService {
    @Autowired
    String libreofficeConfigPath

    @Autowired
    String imagemagickConfigPath

//    对接libreOffice 的一些接口，便于后面的几个功能
    /*
    1、word转PNG ，JPG，PDF，如果文章过于复杂，还是不好转HTML
    2、excel 转html
    3、PDF 转 PNG，JPG
     */

    void wordToPDF(String inputFile, String outputDirectory, String changeFileNamePath){
//        println(inputFile)
//        println(outputDirectory)
//        println(changeFileNamePath)
        String commandLine = libreofficeConfigPath +  " --headless --convert-to pdf:draw_pdf_Export --outdir " + "\"${outputDirectory}\"" +" \"${inputFile}\""
//        .\soffice.exe --headless --convert-to pdf:writer_pdf_Export --outdir "D:\saveFile" "D:\testConvertFile\test.doc"
        println(commandLine)
        println(changeFileNamePath)
        //        开启监控后，在进行转换
        SimpleWatcher simpleWatcher = new SimpleWatcher(){
            @Override
            void onCreate(WatchEvent<?> event, Path currentPath) {
                println("文件被创建"+currentPath.toString())
//                当文件创建后，等待
            }

            @Override
            void onModify(WatchEvent<?> event, Path currentPath) {
                println("文件被修改"+currentPath.toString())
//                这里不知道会不会修改的时候不能导入
                Thread.sleep(1000)
                imagemagickPDFToPNG(changeFileNamePath)
            }
        }

        WatchMonitor monitor = WatchMonitor.createAll(new File(changeFileNamePath),simpleWatcher)
        monitor.start()

        String str = RuntimeUtil.execForStr(commandLine)
//        这里看有无有自己关闭
        println(str)

    }


    void excelToHtml(){
//  --convert-to "html:XHTML Writer File:UTF8" --outdir "D:\saveFile" "D:\testConvertFile\test.excel"

    }

    /**
     * 原版的libreoffice 转png效果太差了，这里还是走的一圈，先转的PDF 再转的PNG
     */
    void imagemagickPDFToPNG(String changeFileNamePath){
//        想用这个 组件需要的东西 这个也需要调试
//        .\magick.exe convert 'D:\saveFile\test.pdf' 'D:\saveFile\test.png'
        String pngFilePath = StrUtil.removeSuffix(changeFileNamePath,".pdf")
        String commandLine = imagemagickConfigPath + " convert " + " \"${changeFileNamePath}\" " + " \"${pngFilePath}\" "
        println(commandLine)
        String str = RuntimeUtil.execForStr(commandLine)
        println(str)
    }


    /*
    这里只用于 word 转 pdf 不用这个。
     */

    void pdfToPng(){

    }

}