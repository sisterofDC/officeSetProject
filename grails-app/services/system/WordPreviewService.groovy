package system

import com.jacob.activeX.ActiveXComponent
import com.jacob.com.Dispatch
import grails.gorm.transactions.Transactional


@Transactional
class WordPreviewService {

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

}
