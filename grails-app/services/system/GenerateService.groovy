package system


import freemarker.template.Configuration
import freemarker.template.Template
import freemarker.template.TemplateException
import grails.core.GrailsApplication
import grails.gorm.transactions.Transactional
import officesetproject.Generate
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

    @Autowired
    String systemSavePathConfig

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
//                默认创建一个display 的一个列子
                ftlInputData.put("classPropertyNameWithBoolean","display")
                ftlInputData.put("trueTypeValue","'1'")
                ftlInputData.put("falseTypeValue","'0'")


                List<LinkedHashMap<String,String>> classPropertiesInput = new ArrayList<>()
                List<LinkedHashMap<String,String>> classPropertiesTable = new ArrayList<>()
                List<LinkedHashMap<String,String>> classPropertiesDate = new ArrayList<>()
                List<LinkedHashMap<String,String>> classPropertiesSpecial = new ArrayList<>()
                generateList.each {generate ->
                    if (generate.status=="启用"){

                        if (generate.propertyType=="java.lang.Integer"){
                            addNumberClassPropertiesInput(classPropertiesInput,generate)
                        }else if (generate.propertyType=="java.lang.String"){
                            addDefaultPropertiesInput(classPropertiesInput,generate)
                        }else if (generate.propertyType=="java.lang.Double"){
                            addNumberClassPropertiesInput(classPropertiesInput,generate)
                        }else if (generate.propertyType=="java.math.BigDecimal"){
                            addNumberClassPropertiesInput(classPropertiesInput,generate)
                        }else {
                            addDefaultPropertiesInput(classPropertiesInput,generate)
                        }

                        if (generate.query=="dateInputWithDay"){
                            def propertyTable = [
                                    "classPropertyChinese":generate.classPropertyChinese,
                                    "classPropertyName":generate.classProperty,
                                    "templet":"templet:\"<div>{{layui.util.toDateString(d." +generate.classProperty + ", 'yyyy-MM-dd')}}</div>\",",
                            ]
                            def propertyDate =[
                                    "classPropertyName":generate.classProperty,
                                    "dateType":"date",
                            ]
                            classPropertiesTable.add(propertyTable)
                            classPropertiesDate.add(propertyDate)
                        }else if(generate.query=="dateInputWithSecond"){
//                            加一点长度
                            def propertyTable = [
                                    "classPropertyChinese":generate.classPropertyChinese,
                                    "classPropertyName":generate.classProperty,
                                    "templet":"templet:\"<div>{{layui.util.toDateString(d." +generate.classProperty + ", 'yyyy-MM-dd HH:mm:ss')}}</div>\", width: 200,",
                            ]
                            def propertyDate =[
                                    "classPropertyName":generate.classProperty,
                                    "dateType":"datetime",
                            ]
                            classPropertiesTable.add(propertyTable)
                            classPropertiesDate.add(propertyDate)
                        } else if (generate.query=="input"){
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
                ftlInputData.put("classPropertiesSpecial",classPropertiesSpecial)
                def fileName ="list.gsp"
                return [
                        "templateFile":"list.ftl",
                        "ftlInputData":ftlInputData,
                        "fileName":fileName,
                ]
            }
        }
    }


    void addNumberClassPropertiesInput(List<LinkedHashMap<String,String>> classPropertiesInput,Generate generate ){
        def propertyInput = [
                "classPropertyChinese":generate.classPropertyChinese,
                "classPropertyName":generate.classProperty,
                "type":"type='number'"
        ]
        classPropertiesInput.add(propertyInput)
    }

    void addDefaultPropertiesInput(List<LinkedHashMap<String,String>> classPropertiesInput,Generate generate ){
        def propertyInput = [
                "classPropertyChinese":generate.classPropertyChinese,
                "classPropertyName":generate.classProperty,
                "type":""
        ]
        classPropertiesInput.add(propertyInput)
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
                List<LinkedHashMap<String,String>> classPropertiesSpecial  = new ArrayList<>()
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

                        if (generate.query=="dateInputWithDay"){
                            def propertyDate =[
                                    "classPropertyName":generate.classProperty,
                                    "dateType":"date"
                            ]
                            classPropertiesDate.add(propertyDate)
                        }else if (generate.query=="dateInputWithSecond"){
                            def propertyDate =[
                                    "classPropertyName":generate.classProperty,
                                    "dateType":"datetime"
                            ]
                            classPropertiesDate.add(propertyDate)
                        }
                    }
                }
                ftlInputData.put("classPropertiesInput",classPropertiesInput)
                ftlInputData.put("classPropertiesDate",classPropertiesDate)
                ftlInputData.put("classPropertiesSpecial",classPropertiesSpecial)
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
//                渲染查询语句的
                List<LinkedHashMap<String,String>> classPropertiesQuery = new ArrayList<>()
//                渲染前端返回用到的
                List<LinkedHashMap<String,String>> classPropertiesReturnBack = new ArrayList<>()
                generateList.each {generate ->
                    def returnBack = [
                            "propertyName":generate.classProperty,
                            "domainVariableName":generate.domainVariableName,
                    ]
                    classPropertiesReturnBack.add(returnBack)
                }

                generateList.each {generate ->
                    if (generate.status=="启用"){
//                        日期类型的选择
//                        秒级
                        if (generate.query=="dateInputWithSecond"){
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
//                            天级
                        } else if(generate.query=="dateInputWithDay"){
                            def propertyQuery = [
                                    "classPropertyChinese":generate.classPropertyChinese,
                                    "classPropertyName":generate.classProperty,
                                    "query":"def dateArray = params.get('" +generate.classProperty+ "').toString().split(' - ')\n" +
                                            "               def startDate = Date.parse('yyyy-MM-dd', dateArray[0])\n" +
                                            "               def endDate = Date.parse('yyyy-MM-dd',  dateArray[1])\n" +
                                            "               ge('"+generate.classProperty+ "',startDate)\n" +
                                            "               le('"+generate.classProperty+ "',endDate)",
                            ]
                            classPropertiesQuery.add(propertyQuery)
                        } else if (generate.query=="input"){
//                            这里先空着，后面来想办法，把所有的映射做完
                            if (generate.propertyType=="boolean"){

                            }else if (generate.propertyType=="java.lang.Integer"){

                            }else if (generate.propertyType=="java.lang.String"){

                            }else if (generate.propertyType=="java.lang.Double"){

                            }else if (generate.propertyType=="java.math.BigDecimal"){

                            }

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
                ftlInputData.put("classPropertiesReturnBack",classPropertiesReturnBack)
                def fileName =domainName+"Controller.groovy"
                return [
                        "templateFile":"Controller.groovy.ftl",
                        "ftlInputData":ftlInputData,
                        "fileName":fileName,
                ]
            }
        }
    }


    def generateBatchUploadFile(String domainName){
        def generateList = Generate.findAllByDomainName(domainName)
        Map<String, Object> ftlInputData = new HashMap<>()
        if (generateList.size()>0) {
            Generate generateSingle = generateList.get(0)
            String domainNameChinese = generateSingle.domainNameChinese
            String domainVariableName = generateSingle.domainVariableName
            String packageName = generateSingle.packageName
//            不为空判断
            if (domainNameChinese != "" && domainNameChinese!=null) {
                ftlInputData.put("domainName",domainName)
                ftlInputData.put("domainVariableName",domainVariableName)
                ftlInputData.put("packageName",packageName)
                ftlInputData.put("domainNameChinese",domainNameChinese)
                List<LinkedHashMap<String,String>> classPropertiesQuery = new ArrayList<>()
                List<LinkedHashMap<String,String>> classPropertiesParseQuery = new ArrayList<>()
                List<LinkedHashMap<String,String>> classPropertiesTableQuery = new ArrayList<>()
                List<LinkedHashMap<String,String>> classPropertiesConstructorQuery = new ArrayList<>()
                generateList.each {generate ->
                    if (generate.status=="启用"){
                        def propertyQuery = [
                                "classPropertyChinese":generate.classPropertyChinese,
                                "classPropertyName":generate.classProperty,
                        ]
                        classPropertiesQuery.add(propertyQuery)
                        def constructorQuery = [
                                "classPropertyName":generate.classProperty,
                        ]
                        classPropertiesConstructorQuery.add(constructorQuery)
                        def parseQuery = [
                                "classPropertyChinese":generate.classPropertyChinese,
                                "classPropertyName":generate.classProperty,
                                "domainVariableName":domainVariableName,
                        ]
                        classPropertiesParseQuery.add(parseQuery)
                        def tableQuery = [
                                "classPropertyChinese":generate.classPropertyChinese,
                                "classPropertyName":generate.classProperty,
                        ]
                        classPropertiesTableQuery.add(tableQuery)
                    }
                }
                ftlInputData.put("classPropertiesParse",classPropertiesParseQuery)
                ftlInputData.put("classPropertiesTable",classPropertiesTableQuery)
                ftlInputData.put("classProperties",classPropertiesQuery)
                ftlInputData.put("classPropertiesConstructor",classPropertiesConstructorQuery)

                def fileName ="batchUpload.gsp"
                return [
                        "templateFile":"batchUpload.ftl",
                        "ftlInputData":ftlInputData,
                        "fileName":fileName,
                ]
            }else {
                return false
            }
        }

    }

    /**
     * 写入文件
     * @param templateFile
     * @param obj
     * @param filePath
     */
    void generateFunctionToFile(String templateFile, Map<String, Object> obj, String fileName,String domainName) {
//        这里进行的fileName 的凭借和目录创建
//        有两种生成方式，
//        第一个：是生成在项目的生成目录下
//        （A）、用grailsApplication getResource getPath然后重新拼接字符串
//        （B）、分别生成不同的完整路径，然后开始输出（本身东西不多）
//        第二个：还是生成在静态目录文件下面，创建对应的目录，然后生成进去，这里不进去文件管理系统。因为是代码生成不是其他的
        try {
//            文件创建目录
            String generateFileDirectoryName = "generateFile"
//            先创建主文件夹
            File fileDirectory = new File(systemSavePathConfig+File.separator+generateFileDirectoryName)
//            文件不存在创建目录
            if (!fileDirectory.exists()){
                fileDirectory.mkdir()
            }
//            然后创建对应类名的文件夹，然后把东西塞进去
            File domainDirectory = new File(systemSavePathConfig+File.separator+generateFileDirectoryName+File.separator+domainName)
            if (!domainDirectory.exists()){
                domainDirectory.mkdir()
            }
//            真正的完整路径
            fileName = systemSavePathConfig+File.separator+generateFileDirectoryName+File.separator+domainName+File.separator+fileName
            Template temp = freeMarkerConfig.getTemplate(templateFile)
            File file = new File(fileName)
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
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()
            ZipOutputStream zipOut = new ZipOutputStream(byteArrayOutputStream)

            for (int i = 0; i < templateFiles.size(); i++) {
                String templateFile = templateFiles.get(i)
                Map<String, Object> obj = objs.get(i)
                String filename = fileNames.get(i)

                Template temp = freeMarkerConfig.getTemplate(templateFile)
                ByteArrayOutputStream templateByteArrayOutputStream = new ByteArrayOutputStream()
                Writer fileWriter = new OutputStreamWriter(templateByteArrayOutputStream)

                temp.process(obj, fileWriter)
                fileWriter.flush()

                ZipEntry zipEntry = new ZipEntry(filename)
                zipOut.putNextEntry(zipEntry)
                zipOut.write(templateByteArrayOutputStream.toByteArray())
                zipOut.closeEntry()
            }
            zipOut.close()
            byteArrayOutputStream.close()
            // Set response headers
            response.setContentType("application/zip")
            response.setHeader("Content-Disposition", "attachment; filename=generated.zip")

            // Write the zip file to the response
            ServletOutputStream outputStream = response.getOutputStream()
            outputStream.write(byteArrayOutputStream.toByteArray())
            outputStream.flush()
            outputStream.close()
        } catch (TemplateException | IOException e) {
            throw new RuntimeException(e)
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
