package officesetproject

/**
 *  农科院文章
 */
class NjnkyAddonarticle {
    /** 内容 */
    String body
    /** 栏目ID */
    Short typeid
    /** 自定义模板 */
    String templet
    /** 跳转URL */
    String redirecturl
    /** 用户IP */
    String userip

    static mapping = {
        body type: 'text'
        version false
    }

    static constraints = {
        body(nullable: true,blank: true)
        typeid(nullable:true,blank:true)
        templet(nullable:true,blank:true)
        redirecturl(nullable:true,blank:true)
        userip(nullable:true,blank:true)
    }
}
