package officesetproject

class FileInfo {

    /** * 文件存储位置（1:阿里云，2:腾讯云，3:minio，4:本地）*/
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


    }
}
