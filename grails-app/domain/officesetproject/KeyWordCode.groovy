package officesetproject

class KeyWordCode {
    /** 域名*/
    String domainUrlName
    /** 关键词标题 */
    String keyWordTitle
    /** 关键词*/
    String keyWords
    /** 关键词描述*/
    String keyWordDescription
    /** 额外信息*/
    String otherMessage
    /** 完整代码 */
    String fullCode


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
        domainUrlName(nullable: true,blank: true)
        keyWordTitle(nullable: true,blank: true)
        keyWords(nullable: true,blank: true)
        keyWordDescription(nullable: true,blank: true)
        otherMessage(nullable: true,blank: true)
        fullCode(nullable: true, maxSize:20000)

        createUser(nullable: true,blank: true)
        lastUpdatedBy(nullable: true,blank: true)
    }
}
