package officesetproject

class FileInfo {

    /** * 文件存储位置（1:服务器本地,2:阿里云，3:腾讯云，4:minio，5其他云盘）*/
     Integer fileLocation
    /** * 文件仓库*/
     String fileBucket
    /** * 文件名称（上传时候的文件名）*/
     String fileOriginName
    /** * 文件后缀 */
     String fileSuffix
    /** * 文件大小kb */
     Long fileSizeKb
    /** * 文件大小信息，计算后的 */
     String fileSizeInfo
    /** * 存储到bucket的名称（文件唯一标识id）*/
     String fileObjectName
    /** * 文件ID*/
     String fileId
    /** * 存储路径 */
     String filePath


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
        fileLocation(nullable: false,inList: [1,2,3,4,5])
        fileBucket(nullable: false,blank: false,maxSize:5000)
        fileOriginName(nullable: true,blank: true,maxSize:5000)
        fileSuffix(nullable: true,blank: true)
        fileSizeKb(nullable: false)
        fileSizeInfo(nullable:false,blank: false)
        fileObjectName(nullable:false,blank: false)
        fileId(nullable: false,blank: false)
        filePath(nullable:false,blank: false)
        createUser(nullable: true,blank: true)
        lastUpdatedBy(nullable: true,blank: true)
    }
}
