package officesetproject

class Generate {




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
    }
}
