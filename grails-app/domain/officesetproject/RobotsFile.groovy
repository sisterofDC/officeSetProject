package officesetproject

class RobotsFile {
    /** 域名 */
    String domainUrlName
    /** robots.txt的内容 */
    String robotsContent


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
        robotsContent(nullable: true, blank: true,maxSize:20000)
        createUser(nullable: true,blank: true)
        lastUpdatedBy(nullable: true,blank: true)
    }
}
