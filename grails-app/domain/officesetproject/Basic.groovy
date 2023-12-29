package officesetproject
/**
 * 最基本的结构。其他结构也是这个为前提。有 1、创建时间，2.创建人，3、更新时间，4、更新用户
 */

/*
// 提供复制的地方
----------------------------------------------------------------

    Date dateCreated  //自动生成创建时间
    Date lastUpdated //自动生成更新时间
    String createUser // 创建人
    String lastUpdatedBy //最后更新的人

    static mapping = {
        version(false)
        sort dateCreated : "desc" //降序 descending order  desc 升序 asc ascending
        autoTimestamp true
    }

----------------------------------------------------------------
 */

class Basic {

    Date dateCreated  //自动生成创建时间
    Date lastUpdated //自动生成更新时间
    String createUser // 创建人
    String lastUpdatedBy //最后更新的人

// 这里用于写验证参数。 Validation and Constraints
// 基本的验证参数。
    /*
blank    login(blank:false)  验证String 是否是为空
inList   name(inList: ["Joe"]) 在List 中验证是否包含
matches  login(matches: "[a-zA-Z]+") regex匹配
max      age(max: new Date()) price(max: 999F) 最大
maxSize  children(maxSize: 25) 最大
min      age(min: new Date()) price(min: 0F) 最小
minSize  children(minSize: 25) 最小
notEqual login(notEqual: "Bob") 不等
nullable defaults to false. age(nullable: true) 是否可以为null，默认为false
range    range age(range: 18..65) 范围设置
scale    salary(scale: 2) 小数点，存入数据库中的小数点设置
size     children(size: 5..15) number string 的length设定
unique   login(unique: true) 唯一性
url      homePage(url: true)  验证URL
     */
    static constraints = {

    }

//    查询的时候，关闭版本号，默认的排序方式是 createTime ,自动时间增加
//    Automatic timestamping
//    如果您定义了 dateCreated 属性，那么当您创建新实例时，它将被设置为当前日期
//    如果你定义了lastUpdated属性，它会自动更新
//    设置为 false 就不会自动生成了
//    sort 是默认排序的意思。 sort 可以在表达式中使用  def airports = Airport.list(sort:'name') 也可以直接在这里写 一般为创建时间
    static mapping = {
        version(false)
        sort dateCreated : "desc" //降序 descending order  desc 升序 asc ascending
        autoTimestamp true
    }
}
