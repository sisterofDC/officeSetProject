package officesetproject


import freemarker.template.Configuration
import freemarker.template.Template
import freemarker.template.TemplateException
import grails.core.GrailsApplication
import grails.gorm.transactions.Transactional
import org.apache.tools.zip.ZipEntry
import org.apache.tools.zip.ZipOutputStream
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

import javax.servlet.ServletOutputStream
import javax.servlet.http.HttpServletResponse


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


    def generateListGSPFile(String domainName){
        def generateList = Generate.findAllByDomainName(domainName)
        Map<String, Object> ftlInputData = new HashMap<>()
        if (generateList.size()>0){
            Generate generateSingle = generateList.get(0)
            String domainNameChinese = generateSingle.domainNameChinese
            String domainVariableName = generateSingle.domainVariableName
//        判空
            if (domainNameChinese==""){
                return false
            }else {
                ftlInputData.put("domainNameChinese",domainNameChinese)
                ftlInputData.put("domainVariableName",domainVariableName)
                List<LinkedHashMap<String,String>> classPropertiesInput = new ArrayList<>()
                List<LinkedHashMap<String,String>> classPropertiesTable = new ArrayList<>()
                List<LinkedHashMap<String,String>> classPropertiesDate = new ArrayList<>()
                generateList.each {generate ->
                    if (generate.status=="启用"){
                        def propertyInput = [
                                "classPropertyChinese":generate.classPropertyChinese,
                                "classPropertyName":generate.classProperty,
                        ]
                        classPropertiesInput.add(propertyInput)

                        if (generate.query=="dateInput"){
                            def propertyTable = [
                                    "classPropertyChinese":generate.classPropertyChinese,
                                    "classPropertyName":generate.classProperty,
                                    "templet":"templet:\"<div>{{layui.util.toDateString(d." +generate.classProperty + ", 'yyyy-MM-dd')}}</div>\",",
                            ]
                            def propertyDate =[
                                    "classPropertyName":generate.classProperty,
                            ]
                            classPropertiesTable.add(propertyTable)
                            classPropertiesDate.add(propertyDate)
                        }else if (generate.query=="input"){
                            def propertyTable = [
                                    "classPropertyChinese":generate.classPropertyChinese,
                                    "classPropertyName":generate.classProperty,
                                    "templet":"",
                            ]
                            classPropertiesTable.add(propertyTable)
                        }
                    }
                }
                ftlInputData.put("classPropertiesInput",classPropertiesInput)
                ftlInputData.put("classPropertiesTable",classPropertiesTable)
                ftlInputData.put("classPropertiesDate",classPropertiesDate)
                def fileName ="list.gsp"
                return [
                        "templateFile":"list.ftl",
                        "ftlInputData":ftlInputData,
                        "fileName":fileName,
                ]
            }
        }
    }

    def generateEditGSPFile(String domainName){
        def generateList = Generate.findAllByDomainName(domainName)
        Map<String, Object> ftlInputData = new HashMap<>()
        if (generateList.size()>0){
            Generate generateSingle = generateList.get(0)
            String domainNameChinese = generateSingle.domainNameChinese
            String domainVariableName = generateSingle.domainVariableName
            if (domainNameChinese==""){
                return false
            }else {
                ftlInputData.put("domainNameChinese",domainNameChinese)
                ftlInputData.put("domainVariableName",domainVariableName)
                List<LinkedHashMap<String,String>> classPropertiesInput = new ArrayList<>()
                List<LinkedHashMap<String,String>> classPropertiesDate = new ArrayList<>()
                generateList.each {generate ->
                    if (generate.status=="启用"){
                        if (generate.whetherRequired=="需要必填项"){
                            def propertyInput = [
                                    "classPropertyChinese":generate.classPropertyChinese,
                                    "classPropertyName":generate.classProperty,
                                    "whetherRequired":"required",
                            ]
                            classPropertiesInput.add(propertyInput)
                        }else {
                            def propertyInput = [
                                    "classPropertyChinese":generate.classPropertyChinese,
                                    "classPropertyName":generate.classProperty,
                                    "whetherRequired":"",
                            ]
                            classPropertiesInput.add(propertyInput)
                        }

                        if (generate.query=="dateInput"){
                            def propertyDate =[
                                    "classPropertyName":generate.classProperty,
                            ]
                            classPropertiesDate.add(propertyDate)
                        }
                    }
                }
                ftlInputData.put("classPropertiesInput",classPropertiesInput)
                ftlInputData.put("classPropertiesDate",classPropertiesDate)
                def fileName ="edit.gsp"
                return [
                        "templateFile":"edit.ftl",
                        "ftlInputData":ftlInputData,
                        "fileName":fileName,
                ]

            }
        }
    }

    def generateControllerGroovyFile(String domainName){
        def generateList = Generate.findAllByDomainName(domainName)
        Map<String, Object> ftlInputData = new HashMap<>()
        if (generateList.size()>0){
            Generate generateSingle = generateList.get(0)
            String domainNameChinese = generateSingle.domainNameChinese
            String domainVariableName = generateSingle.domainVariableName
            String packageName = generateSingle.packageName
            if (domainNameChinese==""){
                return false
            }else {
                ftlInputData.put("domainName",domainName)
                ftlInputData.put("domainVariableName",domainVariableName)
                ftlInputData.put("packageName",packageName)
                List<LinkedHashMap<String,String>> classPropertiesQuery = new ArrayList<>()
                generateList.each {generate ->
                    if (generate.status=="启用"){
                        if (generate.query=="dateInput"){
                            def propertyQuery = [
                                    "classPropertyChinese":generate.classPropertyChinese,
                                    "classPropertyName":generate.classProperty,
                                    "query":"def dateArray = params.get('" +generate.classProperty+ "').toString().split(' - ')\n" +
                                            "               def startDate = Date.parse('yyyy-MM-dd HH:mm:ss', dateArray[0])\n" +
                                            "               def endDate = Date.parse('yyyy-MM-dd HH:mm:ss',  dateArray[1])\n" +
                                            "               ge('"+generate.classProperty+ "',startDate)\n" +
                                            "               le('"+generate.classProperty+ "',endDate)",
                            ]
                            classPropertiesQuery.add(propertyQuery)
                        }else if (generate.query=="input"){
                            if (generate.inquiryMode=="相等查询"){
                                def propertyQuery = [
                                        "classPropertyChinese":generate.classPropertyChinese,
                                        "classPropertyName":generate.classProperty,
                                        "query":"eq('"+generate.classProperty+"', params." +generate.classProperty+ ")",
                                ]
                                classPropertiesQuery.add(propertyQuery)
                            }else if (generate.inquiryMode=="模糊查询"){
                                def propertyQuery = [
                                        "classPropertyChinese":generate.classPropertyChinese,
                                        "classPropertyName":generate.classProperty,
                                        "query":"ilike('" +generate.classProperty+ "', '%' + params." +generate.classProperty+ " + '%')",
                                ]
                                classPropertiesQuery.add(propertyQuery)
                            }
                        }
                    }
                }
                ftlInputData.put("classPropertiesQuery",classPropertiesQuery)
                def fileName =domainName+"Controller.groovy"
                return [
                        "templateFile":"Controller.groovy.ftl",
                        "ftlInputData":ftlInputData,
                        "fileName":fileName,
                ]
            }
        }
    }


    /**
     * 写入文件
     * @param templateFile
     * @param obj
     * @param filePath
     */
    void generateFunctionToFile(String templateFile, Map<String, Object> obj, String filePath) {
        try {
            Template temp = freeMarkerConfig.getTemplate(templateFile)
            File file = new File(filePath)
            Writer fileWriter = new FileWriter(file)
            temp.process(obj, fileWriter)
            fileWriter.flush()
            fileWriter.close()
        } catch (TemplateException | IOException e) {
            throw new RuntimeException(e)
        }
    }

    void generateFunctionsToZipAndSendResponse(List<String> templateFiles, List<Map<String, Object>> objs, List<String> fileNames, HttpServletResponse response) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ZipOutputStream zipOut = new ZipOutputStream(byteArrayOutputStream);

            for (int i = 0; i < templateFiles.size(); i++) {
                String templateFile = templateFiles.get(i);
                Map<String, Object> obj = objs.get(i);
                String filename = fileNames.get(i);

                Template temp = freeMarkerConfig.getTemplate(templateFile);
                ByteArrayOutputStream templateByteArrayOutputStream = new ByteArrayOutputStream();
                Writer fileWriter = new OutputStreamWriter(templateByteArrayOutputStream);

                temp.process(obj, fileWriter);
                fileWriter.flush();

                ZipEntry zipEntry = new ZipEntry(filename);
                zipOut.putNextEntry(zipEntry);
                zipOut.write(templateByteArrayOutputStream.toByteArray());
                zipOut.closeEntry();
            }

            zipOut.close();
            byteArrayOutputStream.close();

            // Set response headers
            response.setContentType("application/zip");
            response.setHeader("Content-Disposition", "attachment; filename=generated.zip");

            // Write the zip file to the response
            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(byteArrayOutputStream.toByteArray());
            outputStream.flush();
            outputStream.close();
        } catch (TemplateException | IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 用的 System.out.println 输出参数，更多的用于测试
     * @param templateFile
     * @param obj
     */
    void generateFunction(String templateFile, Map<String, Object> obj) {
        try {
            Template temp = freeMarkerConfig.getTemplate(templateFile)
            Writer out = new OutputStreamWriter(System.out)
            temp.process(obj, out)
        } catch (TemplateException | IOException e) {
            throw new RuntimeException(e)
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
//                                这里改成前端选择
//                                先弄几个简单的，查询类型，更多的类型判断
//                                if (field.type.name=="java.lang.String"){
//                                    generate.query = "input"
//                                }else if (field.type.name=="java.util.Date"){
//                                    generate.query = "dateInput"
//                                }else if (field.type.name==""){
//
//                                }
                                generate.inquiryMode="相等查询"
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

    def save(Generate generate){
        generate.save(failOnError: true)
    }
}
