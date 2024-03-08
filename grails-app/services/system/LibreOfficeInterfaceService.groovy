package system

import cn.hutool.core.io.watch.SimpleWatcher
import cn.hutool.core.io.watch.WatchMonitor
import cn.hutool.core.io.watch.watchers.DelayWatcher
import cn.hutool.core.util.RuntimeUtil
import cn.hutool.core.util.StrUtil
import grails.gorm.transactions.Transactional
import org.apache.commons.io.monitor.FileAlterationListener
import org.apache.commons.io.monitor.FileAlterationMonitor
import org.apache.commons.io.monitor.FileAlterationObserver
import org.grails.plugin.cache.GrailsCacheManager
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


    /**
     * 这个是word转PDF 源参数列表在这里https://help.libreoffice.org/latest/zh-TW/text/shared/guide/pdf_params.html?&DbPAR=SHARED&System=WIN
     * @param inputFile
     * @param outputDirectory
     * @return
     */
    public String wordToPDF(String inputFile, String outputDirectory){
//        普通的直接写PDF 全部用默认的就行了
        String commandLine = libreofficeConfigPath +  " --headless --convert-to pdf --outdir " + "\"${outputDirectory}\"" +" \"${inputFile}\""
//        .\soffice.exe --headless --convert-to pdf:writer_pdf_Export --outdir "D:\saveFile" "D:\testConvertFile\test.doc"
        println(commandLine)
        String str = RuntimeUtil.execForStr(commandLine)
//        这里看有无有自己关闭
        println(str)
        return str
    }



    /**
     *  带参数的转出，需要配置  https://help.libreoffice.org/latest/zh-TW/text/shared/guide/pdf_params.html?&DbPAR=SHARED&System=WIN
     *  这里先尝试用用一些参数来保证导出来的效果好一点
     * 标准的格式 soffice --convert-to pdf:draw_pdf_Export:{"TiledWatermark":{"type":"string","value":"draft"}} test.odg
     * 还是有问题，文档出现大面积的错位问题，格式是保留了。但是还是有问题
     * @param inputFile
     * @param outputDirectory
     * @param parameters
     * @return
     */
    public String wordToPDFWithParameter(String inputFile, String outputDirectory,Map<String,String> parameters){
        if (parameters != null && parameters.size() > 0) {
            def formattedString = parameters.collect { key, value ->
                "\"$key\":$value"
            }.join(",")
            def result = "pdf:draw_pdf_Export:{$formattedString}"
            String commandLine = libreofficeConfigPath +  " --headless --convert-to "+ result+ " --outdir " + "\"${outputDirectory}\"" +" \"${inputFile}\""
            println(commandLine)
            String str = RuntimeUtil.execForStr(commandLine)
            println(str)
        } else {
            println "Map is null or empty."
        }
    }


//    excel 转 html
    /**
     * 利用libreoffice的命令行进行转换指令，详情需要查看 https://help.libreoffice.org/latest/zh-TW/text/shared/guide/convertfilters.html?&DbPAR=SHARED&System=WIN
     * 这里需要把参数弄对，是用calc 的组件进行转换，字符是utf-8 要不全是乱码
     * @param inputFile
     * @param outputDirectory
     */
    public String excelToHtml(String inputFile,  String outputDirectory){
//  --convert-to "html:XHTML Writer File:UTF8" --outdir "D:\saveFile" "D:\testConvertFile\test.excel"
//        String commandLine = libreofficeConfigPath + " --headless --invisible --convert-to \"html:XHTML Calc File:UTF8\" --outdir " + "\"${outputDirectory}\"" +" \"${inputFile}\""
        String commandLine = libreofficeConfigPath + " --headless --invisible --convert-to html --outdir " + "\"${outputDirectory}\"" +" \"${inputFile}\""
//        打印转化的命令语句
        println(commandLine)
        String echoStr = RuntimeUtil.execForStr(commandLine)
//        回显的命令数据
        println(echoStr)
        return echoStr
    }

    public String excelToHtmlVersionTwo(String inputFile,  String outputDirectory){
//  --convert-to "html:XHTML Writer File:UTF8" --outdir "D:\saveFile" "D:\testConvertFile\test.excel"
        String commandLine = libreofficeConfigPath + " --headless --invisible --convert-to \"html:XHTML Calc File:UTF8\" --outdir " + "\"${outputDirectory}\"" +" \"${inputFile}\""
//        打印转化的命令语句
        println(commandLine)
        String echoStr = RuntimeUtil.execForStr(commandLine)
//        回显的命令数据
        return echoStr
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


//    实现文件监控的控件，需要实现所有的listener。代码有点多，只需要实现需要的组件就行了
    public static class MyFileMonitor implements FileAlterationListener{
//        这里需要把内存管理这个这个塞进去
        GrailsCacheManager grailsCacheManager
        String cacheName

        public MyFileMonitor(GrailsCacheManager grailsCacheManager,String cacheName){
            this.grailsCacheManager=grailsCacheManager
            this.cacheName=cacheName
        }

        @Override
        void onDirectoryChange(File directory) {
//            文件目录
            println("文件目录改变")
        }

        @Override
        void onDirectoryCreate(File directory) {
//            文件目录
            println("文件目录创建")
        }

        @Override
        void onDirectoryDelete(File directory) {
//            文件目录
            println("文件目录删除")
        }

        @Override
        void onFileChange(File file) {
//            文件改变
            println("文件改变")
            println(file.name)
            println("文件大小"+file.size())
            println("文件修改事件"+file.lastModified())
//            这里是状态改成2，就是生成的文件改变大小后，就是转换完成
            if (file.size()>0L){
                def myCache = grailsCacheManager.getCache(cacheName)
                myCache.put("isFinished",2)
            }
        }

        @Override
        void onFileCreate(File file) {
//            文件创建
            println("文件创建")
            println(file.name)
        }

        @Override
        void onFileDelete(File file) {
//            文件删除
//            println("文件删除")
//            println(file.name)
////            这里也可以改成2 ，当临时文件删除的时候，就是转换完成了
//            def myCache = grailsCacheManager.getCache('myCache')
//            myCache.put("isFinished",2)
        }

        @Override
        void onStart(FileAlterationObserver observer) {
//            监控状态
//            println("监控状态启动")
        }

        @Override
        void onStop(FileAlterationObserver observer) {
//            监控状态
//            println("监控状态结束")
        }
    }


}
