package officesetproject

class Generate {
    /** 包名 */
    String packageName
    /** 类名 */
    String domainName
    /** 类名的中文*/
    String domainNameChines
    /** 变量名 */
    String domainVariableName
    /** 属性名 */
    String classProperty
    /** 属性对应的中文 */
    String classPropertyChinese
    /** 属性类型 */
    String propertyType
    /** 查询类型 */
    String query
    /**  状态 这个字段是否被启用*/
    String status
    /**  必填 选项*/
    String whetherRequired


    Date dateCreated  //自动生成创建时间
    Date lastUpdated //自动生成更新时间
    String createUser // 创建人
    String lastUpdatedBy //最后更新的人

    static mapping = {
        version(false)
        sort dateCreated : "desc" //降序 descending order  desc 升序 asc ascending
        autoTimestamp true
    }


    static constraints = {
//        因为需要生成代码，不能为空，空了会抛异常
        packageName(nullable: false)
        domainName(nullable: false)
        domainVariableName(nullable: false)
        classProperty(nullable: false)
        propertyType(nullable: false)
//        这个中文是后面设置的，需要等会来
        classPropertyChinese(nullable: true,blank: true)
//        查询的设置，有默认的，但是可以设置
        query(nullable: true,blank: true)
//        状态设置，这个字段是否生成状态
        status(nullable: true,blank: true)
//        类名的中午名称
        domainNameChines(nullable: true,blank: true)
//        必填选项
        whetherRequired(nullable: true,blank: true)

        createUser(nullable: true,blank: true)
        lastUpdatedBy(nullable: true,blank: true)
    }
}
