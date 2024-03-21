package system

import cn.hutool.core.util.RuntimeUtil
import com.jacob.activeX.ActiveXComponent
import com.jacob.com.ComThread
import com.jacob.com.Dispatch
import com.jacob.com.Variant
import grails.gorm.transactions.Transactional
import org.springframework.beans.factory.annotation.Autowired

import javax.imageio.ImageIO
import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor
import java.awt.image.BufferedImage


@Transactional
class WordPreviewService {

    @Autowired
    String imagemagickConfigPath

    def getPDFFile(String source,String target){
        ActiveXComponent app
        try{
            //调用windows中的office程序
            app = new ActiveXComponent("word.application")
            //调用word时，不调用窗口
            app.setProperty("Visible", false)
            //获取文档
            Dispatch docs = app.getProperty("Documents").toDispatch()
            //打开指定文档
            Dispatch doc = Dispatch.call(docs,"Open",source).toDispatch()
            //调用文件的另存为功能，宏值为17(pdf格式
            Dispatch.call(doc,"SaveAs",target,17)
            //调用关闭
            Dispatch.call(doc,"Close")
        } catch (Exception e) {
            e.printStackTrace()
        }finally {
            app.invoke("Quit")
        }
    }




    def convertToJPG(String source,String output){
        String CommandLine = imagemagickConfigPath+" convert -verbose "+ source +"  -density 220 -quality 80 -background white -alpha copy"+ output
        println(CommandLine)
        String str = RuntimeUtil.execForStr(CommandLine)
        println(str)
    }
}
