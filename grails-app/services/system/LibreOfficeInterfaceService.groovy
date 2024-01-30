package system

import cn.hutool.core.util.RuntimeUtil
import grails.gorm.transactions.Transactional

@Transactional
class LibreOfficeInterfaceService {

    String libreofficeConfigPath

//    对接libreOffice 的一些接口，便于后面的几个功能
    /*
    1、word转PNG ，JPG，PDF，如果文章过于复杂，还是不好转HTML
    2、excel 转html
    3、PDF 转 PNG，JPG
     */

    public void wordToPNG(){

//        libreoffice --convert-to pdf Test.odt && convert
//        -density 288x288 -units pixelsperinch Test.pdf -background white -alpha background -alpha off  -quality 100 -resize 25% Test.png

//        .\soffice.exe --headless --convert-to png --outdir "D:\saveFile" "D:\testConvertFile\test.doc"


        String str = RuntimeUtil.execForStr("");
        println(str)
    }




}
