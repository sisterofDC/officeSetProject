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


    def generateControllerFile(String domainClassName){
        def packageName = defaultPackage
        def domainName = domainClassName
        Map<String, Object> ftlInputData = new HashMap<>();
        ftlInputData.put("packageName",packageName)
        ftlInputData.get("domainName",domainName)
        ftlInputData.get("domainVariableName",lowercaseFirstClassName(domainName))

        ftlInputData.get("classProperties",domainName)


        def fileName = domainName+"Controller.groovy"
        def filePath = "D:\\generateTest\\"+fileName
        generateFunctionToFile("Controller.groovy.ftl",ftlInputData,filePath)
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


    /**
     * 获取当前Domain的所有信息
     */
    def getDomainAllInformation(String className){
        def classList = grailsApplication.getArtefacts("Domain")
        classList.each { final def singleClass ->
            def domainName = singleClass.getName()
            if (domainName == className) {
                println(domainName)

                def domainClazz = singleClass.clazz
                def propertyList = []

                domainClazz.declaredFields.each { field ->
                    if (!field.synthetic && !field.name.startsWith('$')) {
                        println( field.name)
                        println( field.type.name)
//                        过滤一些参数和字段
                        if (field.name==""){

                        }




                        propertyList << ["name": field.name, "type": field.type.name] as Map<String, Object>
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
        if (className && Character.isUpperCase(className[0])) {
            def lowercased = className.substring(0, 1).toLowerCase() + className.substring(1)
            return lowercased
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
