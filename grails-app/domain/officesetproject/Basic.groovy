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
blank Validates that a String value is not blank login(blank:false)  验证String 是否是为空
inList Validates that a value is within a range or collection of constrained values. name(inList: ["Joe"]) 在List 中验证是否包含
matches Validates that a String value matches a given regular expression. login(matches: "[a-zA-Z]+") regex匹配
max Validates that a value does not exceed the given maximum value. age(max: new Date()) price(max: 999F) 最大
maxSize Validates that a value’s size does not exceed the given maximum value. children(maxSize: 25) 最大
min Validates that a value does not fall below the given minimum value. age(min: new Date()) price(min: 0F) 最小
minSize Validates that a value’s size does not fall below the given minimum value. children(minSize: 25) 最小
notEqual Validates that that a property is not equal to the specified value login(notEqual: "Bob") 不等
nullable Allows a property to be set to null - defaults to false. age(nullable: true) 为空
range Uses a Groovy range to ensure that a property’s value occurs within a specified range age(range: 18..65) 范围
scale Set to the desired scale for floating point numbers (i.e. the number of digits to the right of the decimal point). salary(scale: 2) 小数点
size Uses a Groovy range to restrict the size of a collection or number or the length of a String. children(size: 5..15) 范围
unique Constrains a property as unique at the database level login(unique: true) 唯一性
url Validates that a String value is a valid URL. homePage(url: true)  验证URL
     */
    static constraints = {

    }

//    查询的时候，关闭版本号，默认的排序方式是 createTime ,自动时间增加
//    Automatic timestamping
//    If you define a dateCreated property it will be set to the current date for you when you create new instances
//    if you define a lastUpdated property it will be automatically
//    设置为 false 就不会自动生成了
//    sort 是默认排序的意思。 sort 可以在表达式中使用  def airports = Airport.list(sort:'name') 也可以直接在这里写 一般为创建时间
    static mapping = {
        version(false)
        sort dateCreated : "desc" //降序 descending order  desc 升序 asc ascending
        autoTimestamp true
    }
}
