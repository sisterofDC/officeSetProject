package officesetproject

import freemarker.template.Configuration
import freemarker.template.TemplateExceptionHandler
import grails.boot.GrailsApp
import grails.boot.config.GrailsAutoConfiguration
import grails.core.GrailsApplication
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean

@CompileStatic
@org.springframework.context.annotation.Configuration
class Application extends GrailsAutoConfiguration {

    @Autowired
    GrailsApplication grailsApplication

    /**
     * 这个是FreeMaker的初始化，会返回一个FreeMaker配置好的 configuration 的单列
     * 该创建一个 freemarker.template.Configuration 实例， 然后调整它的设置。
     * Configuration 实例是存储 FreeMarker 应用级设置的核心部分
     * 同时，它也处理创建和 缓存 预解析模板(比如 Template 对象)的工作。
     * 生命周期的开始执行一次 应该使用 单例配置。这个是不会修改的 无状态Bean
     * @return Configuration freeMarkerCfg
     */
    /*
    多例Bean每次都会新创建新实例，也就是说线程之间不存在Bean共享的问题。因此，多例Bean是不存在线程安全问题的。
    但是单例Bean又分为无状态Bean和有状态Bean。
    在多线程操作中只会对Bean的成员变量进行查询操作，不会修改成员变量的值，这样的Bean称之为无状态Bean。所以无状态的单例Bean是不存在线程安全问题的。
    在多线程操作中如果需要对Bean中的成员变量进行数据更新操作，这样的Bean称之为有状态Bean，有状态的单例Bean就可能存在线程安全问题。
     */

    @Bean
    public Configuration freeMarkerConfig() {
        System.out.println("FreeMaker初始化");
        System.out.println("FreeMarker模板文件夹目录"+grailsApplication.mainContext.getResource("template/ftl").getFile().absolutePath)
        Configuration freeMarkerCfg = new Configuration(Configuration.VERSION_2_3_22);
        freeMarkerCfg.setDefaultEncoding("UTF-8");
        freeMarkerCfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        try {
            freeMarkerCfg.setDirectoryForTemplateLoading(grailsApplication.mainContext.getResource("template/ftl").getFile());
        } catch (IOException e) {
            throw new RuntimeException("没有找到模板文件夹目录", e);
        }
        return freeMarkerCfg;
    }


    /**
     * 文件保存路径的设置
     * 这里就吧整个文件系统的东西初始化了
     * @return
     */
    @Bean
    public String systemSavePathConfig(){
        String fileSystemFolderName = "fileSystem"
        System.out.println("文件系统初始化")
//        默认文件夹
        String defaultBucketName = "defaultBucket"
//        获取目录
        File directory = grailsApplication.mainContext.getResource(fileSystemFolderName).getFile()
        if (!directory.exists()){
            System.out.println("文件系统文件夹创建中")
            Boolean resultMkdir =directory.mkdir()
            if (resultMkdir){
                System.out.println("文件系统文件夹创建成功")
            }else {
                throw new RuntimeException("创建文件夹失败");
            }
        }else {
            System.out.println("文件夹存在初始化完成")
            System.out.println("文件系统保存目录:"+directory.getPath())
            File defaultBucket = new File(directory.getPath()+File.separator+defaultBucketName)
            if (!defaultBucket.exists()){
                defaultBucket.mkdir()
            }
        }
        return directory.getPath()
    }


    static void main(String[] args) {
        GrailsApp.run(Application, args)
    }
}