package officesetproject

import freemarker.template.Configuration
import freemarker.template.Template
import freemarker.template.TemplateException
import grails.core.GrailsApplication
import grails.gorm.transactions.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service


@Transactional
@Service
class GenerateService {
    //    Application 中的 @Bean注册的单列
    @Autowired
    Configuration freeMarkerConfig
    
    @Autowired
    GrailsApplication grailsApplication

    @Value('${grails.codegen.defaultPackage}')
    public String defaultPackage


    def generateTest(){
        // Create the root hash
        Map<String, Object> root = new HashMap<>();
        root.put("name", "sisterofdc");
        generateFunction("batchUpload.ftl",root)
    }


    def generateControllerFile(String domainName){
        def packageName = defaultPackage



        /*
        Map<String, Object> ftlInputData = new HashMap<>();
        def fileName = domainName+"Controller.groovy"
        def filePath = "D:\\generateTest\\"+fileName
        generateFunctionToFile("Controller.groovy.ftl",ftlInputData,filePath)
         */

    }

    public void generateFunctionToFile(String templateFile, Map<String, Object> obj,String filePath) {
        try {
            Template temp = freeMarkerConfig.getTemplate(templateFile);
            File file = new File(filePath);
            Writer fileWriter = new FileWriter(file);
            temp.process(obj, fileWriter);
            fileWriter.flush();
            fileWriter.close();
        } catch (TemplateException | IOException e) {
            throw new RuntimeException(e);
        }
    }



    public void generateFunction(String templateFile, Map<String, Object> obj) {
        StringWriter sw = new StringWriter();
        try {
            Template temp = freeMarkerConfig.getTemplate(templateFile);
            Writer out = new OutputStreamWriter(System.out);
            temp.process(obj, out);
        } catch (TemplateException | IOException e) {
            throw new RuntimeException(e);
        }
    }


    def getAllDomainName(){
        def classList = grailsApplication.getArtefacts("Domain")
        def domainInformation = []
        classList.each{final def singleClass ->
            def domainClassName = singleClass.getName()
            def singleInfo = ["domainName": domainClassName]
            domainInformation.push(singleInfo)
        }
        def result = [
                code : 0, msg: "",
                data : domainInformation,
                count: domainInformation.size(),
        ]
        return result
    }


    def getAllParameters(String domainName){
        def generateList = Generate.findAllByDomainName(domainName)
        return generateList
    }

    /**
     * 获取当前Domain的所有信息
     */
    def getDomainAllInformation(String domainName){
        def packageName = defaultPackage
//        用系统的方法获取domain层的所有类的数组
        def classList = grailsApplication.getArtefacts("Domain")
//        each 循环
        classList.each { final def singleClass ->
//            用java 反射来获取所有的字段名称
            def domainClassName = singleClass.getName()
//            判断是否是要判断的类名
            if (domainClassName == domainName) {
                def domainClazz = singleClass.clazz
                domainClazz.declaredFields.each { field ->
                    if (!field.synthetic && !field.name.startsWith('$')) {
//                        println( field.name)
//                        println( field.type.name)
//                        过滤一些参数和字段
                        if (isFieldNameInList(field.name)==false){
//                            插入之前先查询一下
                            Generate beforeInsert = Generate.findByDomainNameAndClassProperty(domainName,field.name)
                            if (!beforeInsert){
                                Generate generate = new Generate()
                                generate.domainName = domainName
                                generate.packageName = packageName
                                generate.domainVariableName = lowercaseFirstClassName(domainName)
                                generate.classProperty = field.name
                                generate.propertyType = field.type.name
//                            默认为启用模式
                                generate.status = "启用"
                                generate.whetherRequired = "需要必填项"
//                            保存到数据库，用于生成
                                generate.save(failOnError: true)
                            }
                        }
                    }
                }

            }
        }

    }

    /**
     * 将首字母小写
     * @param className
     * @return string lowercased
     */
    def lowercaseFirstClassName(String className) {
        if (className && Character.isUpperCase(className[0] as char)) {
            def lowerCased = className.substring(0, 1).toLowerCase() + className.substring(1)
            return lowerCased
        } else {
            return className
        }
    }

    /**
     * 判断生成的是否有这些不需要的字段
     * @param fieldName
     * @return
     */
    def isFieldNameInList(String fieldName) {
        def fieldNameList = [
                'mapping',
                'constraints',
                'instanceControllersDomainBindingApi',
                'log',
                'id',
                'version',
                'transients',
                'instanceConvertersApi',
                'org_grails_datastore_mapping_dirty_checking_DirtyCheckable__$changedProperties',
                'org_grails_datastore_gorm_GormValidateable__errors',
                'org_grails_datastore_gorm_GormValidateable__skipValidate'
        ]
        return fieldNameList.contains(fieldName)
    }

}
