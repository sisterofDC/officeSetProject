package officesetproject

class Member {
    /** 用户名*/
    String username
    /** 真实姓名*/
    String realName
    /** 性别*/
    String sex;
    /** 出生年月*/
    Date birthday;
    /** 身份证号*/
    String IDNumber;
    /** 民族*/
    String nation;
    /** 学历*/
    String education;
    /** 政治面貌*/
    String politicalStatus;
    /** 省 */
    String province
    /** 市 */
    String city
    /** 县 */
    String county
    /** 工作单位*/
    String workingAddress;
    /** 电话*/
    String phone;
    /** 会员登录密码 */
    String password

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
        username(blank: false, maxSize: 25, unique: true) //不允许为空，最大长度50
        sex(inList: ["男", "女", "其他"]) //性别只能是 男和女
        IDNumber(maxSize: 18)
        realName(blank: false, maxSize: 25)
        nation(blank: false, maxSize: 20); //民族
        password(nullable: true)
        province(maxSize: 20)
        city(maxSize: 20)
        county(maxSize: 50)
        education(blank: true, maxSize: 20); //学历
        politicalStatus(blank: true, maxSize: 20); //政治面貌
        workingAddress(blank: true, maxSize: 100) //工作单位的限制
    }
}
