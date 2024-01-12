package officesetproject

import grails.core.GrailsApplication
import grails.gorm.transactions.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.multipart.MultipartFile

import java.text.DecimalFormat

@Transactional
class FileSystemService {

    @Autowired
    GrailsApplication grailsApplication


    @Autowired
    String systemSavePathConfig

//    默认文件夹。
    private String defaultBucketName = "defaultBucket"

    public Long uploadFile(MultipartFile file,String fileBucket){
//        原始名字
        String originalFilename = file.getOriginalFilename();
//        文件名
        String saveName= UUID.randomUUID().hashCode().toString()
//        获取文件后缀
        String fileSuffix = "";
//        得到后缀名
        if (originalFilename.isEmpty()){
            fileSuffix = ""
        }else {
            String[] fileNameSplit = originalFilename.split(".")
            if (fileNameSplit.size()!=1){
                fileSuffix=fileNameSplit.last()
//                最终保存的名字
                saveName=saveName+"."+fileSuffix
            }else {
                fileSuffix = ""
            }
        }
//        file的大小
        Long size = file.getSize()
//        String 形式的文件大小
        String fileSizeInfo =convertFileSizeToKB(size)


        FileInfo fileInfo = new FileInfo()
//        这里先默认存在 服务器本地 （1:服务器本地,2:阿里云，3:腾讯云，4:minio，5其他云盘）
        fileInfo.fileLocation = 1
//        如果没有设置 文件夹就是默认的文件夹
        String savePath = ""
        if (fileBucket.isEmpty()){
            fileInfo.fileBucket = defaultBucketName
//           默认的保存文件夹
            savePath = systemSavePathConfig+File.separator+defaultBucketName
        }else {
//            创建新的目录文件夹
            savePath=createDirectory(fileBucket)
//            不同文件夹保存目录
            fileInfo.fileBucket = fileBucket
        }

        fileInfo.fileObjectName = originalFilename
        fileInfo.fileSuffix = fileSuffix
        fileInfo.fileSizeKb = size
        fileInfo.fileSizeInfo = fileSizeInfo
        fileInfo.fileObjectName = saveName
        fileInfo.filePath = savePath+File.separator+saveName

//        如果需要创建这个，需要整合安全系统里面
        fileInfo.createUser = ""
//        正式保存文件
        file.transferTo(new File(fileInfo.filePath))
//        将数据保存后，
        fileInfo.save(failOnError: true)


        return 0L
    }

     String convertFileSizeToKB( long fileSizeInBytes ) {
//        转换为KB
        double fileSizeInKB = fileSizeInBytes / 1024.0
//        保留3位小数
        DecimalFormat decimalFormat = new DecimalFormat("#.####")
//        转成string
        String fileSizeInKBFormatted = decimalFormat.format(fileSizeInKB)
        return "${fileSizeInKBFormatted} KB"
    }

     String createDirectory(String fileBucket){
        File directory = new File(systemSavePathConfig+File.separator+fileBucket)
        if (!directory.exists()){
            Boolean resultMkdir =directory.mkdir()
            if (resultMkdir){
                return directory.getPath()
            }else {
//                IO 操作都是报底层错误
                throw new RuntimeException("创建文件夹失败");
            }
        }else {
            return directory.getPath()
        }
    }





}
