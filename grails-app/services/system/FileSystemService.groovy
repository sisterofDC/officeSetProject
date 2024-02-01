package system

import cn.hutool.core.util.IdUtil
import grails.core.GrailsApplication
import grails.gorm.transactions.Transactional
import officesetproject.FileInfo
import org.apache.commons.fileupload.FileItem
import org.apache.commons.fileupload.disk.DiskFileItemFactory
import org.apache.commons.io.IOUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.multipart.MultipartFile
import org.apache.tools.zip.ZipEntry
import org.apache.tools.zip.ZipOutputStream
import org.springframework.web.multipart.commons.CommonsMultipartFile

import javax.servlet.http.HttpServletResponse
import java.nio.file.Files
import java.nio.file.Paths
import java.text.DecimalFormat

@Transactional
class FileSystemService {

    @Autowired
    GrailsApplication grailsApplication

    @Autowired
    String systemSavePathConfig

//    默认文件夹
    private String defaultBucketName = "defaultBucket"

    String uploadFile(MultipartFile file, String fileBucket){
//        原始名字
        String originalFilename = file.getOriginalFilename()
//        文件名  直接生成一个雪花ID就行了，因为grails用的id 自增
        String saveName= IdUtil.getSnowflakeNextIdStr()
//        获取文件后缀
        String fileSuffix = ""
//        得到后缀名
        if (originalFilename.isEmpty()){
            fileSuffix = ""
        }else {
//            分割后是 string 数组 不是list
            String[] fileNameSplit = originalFilename.split("\\.")
            if (fileNameSplit.length!=0){
                fileSuffix=fileNameSplit[fileNameSplit.length-1]
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
        if (fileBucket.isEmpty()||fileBucket==""){
//            当没有填，或者是空的时候就是默认
            fileInfo.fileBucket = defaultBucketName
//           默认的保存文件夹
            savePath = systemSavePathConfig+File.separator+defaultBucketName
        }else {
//            创建新的目录文件夹
            savePath=createDirectory(fileBucket)
//            不同文件夹保存目录
            fileInfo.fileBucket = fileBucket
        }

//        填写对应的字段
        fileInfo.fileOriginName = originalFilename
        fileInfo.fileSuffix = fileSuffix
        fileInfo.fileSizeKb = size
        fileInfo.fileSizeInfo = fileSizeInfo
        fileInfo.fileObjectName = saveName
        fileInfo.filePath = savePath+File.separator+saveName
//        如果需要创建这个，需要整合安全系统里面
        fileInfo.createUser = ""
//        正式保存文件
        file.transferTo(new File(fileInfo.filePath))
//        将数据保存后，保存到数据库中，并返回保存的saveName
        if(fileInfo.save(failOnError: true)){
            return fileInfo.fileObjectName
        }else {
            return "-1"
        }
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
                throw new RuntimeException("创建文件夹失败")
            }
        }else {
            return directory.getPath()
        }
    }

    /**
     * 下载之前，操作之前，
     * @param fileInfoList
     * @return
     */
    Boolean checkAllFile(List<FileInfo> fileInfoList){
        fileInfoList.each {it->
            if (checkFile(it)==false){
                return false
            }
        }
        return true
    }


    /**
     *
     * @param fileInfo
     * @return Boolean 文件是否存在 true 是存在
     */
    Boolean checkFile(FileInfo fileInfo){
        return Files.exists(Paths.get(fileInfo.filePath))
    }


    /**
     * zip下载
     * @param fileInfoList
     * @param response
     */
    void batchZipDownload(List<FileInfo> fileInfoList, HttpServletResponse response){
        if (checkAllFile(fileInfoList)){
            ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream())
            try {
                response.setContentType("application/zip")
                response.setHeader("Content-Disposition", "attachment; filename=\"batchFiles.zip\"")
                fileInfoList.each {fileInfo->
                    def filePath = fileInfo.filePath
                    def fileOriginName = fileInfo.fileOriginName
                    FileInputStream fis = new FileInputStream(filePath)
                    ZipEntry zipEntry = new ZipEntry(fileOriginName)
                    zipOut.putNextEntry(zipEntry)
                    byte[] bytes = new byte[1024]
                    int length
                    while ((length = fis.read(bytes)) >= 0) {
                        zipOut.write(bytes, 0, length)
                    }
                    fis.close()
                    zipOut.closeEntry()
                }
            }catch (IOException e){
                println(e)
            }finally {
                zipOut.flush()
                zipOut.close()
                response.getOutputStream().close()
            }
        }
    }


    /**
     * 删除文件
     */
    void deleteFile(FileInfo fileInfo){
        try {
            Files.delete(Paths.get(fileInfo.filePath))
        }
        catch (IOException e) {
            e.printStackTrace()
        }
    }

    void downloadSingleFile(FileInfo, HttpServletResponse response){

    }

    /**
     * 通用的
     * @param inputFile
     * @return
     */
    MultipartFile convertToMultipartFileByFile(File inputFile){
        if (inputFile.exists()){
            FileItem item = new DiskFileItemFactory().createItem("file"
//                如果这里有问题就 MediaType.TEXT_PLAIN_VALUE 等会看一下这个是什么
                    , MediaType.MULTIPART_FORM_DATA_VALUE
                    , true
                    , inputFile.getName())
//            将文件流进行转入
            try (InputStream input = new FileInputStream(inputFile)
                 OutputStream os = item.getOutputStream()) {
                // 流转移
                IOUtils.copy(input, os)
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid file: " + e, e)
            }
            MultipartFile multipartFile = new CommonsMultipartFile(item)
            return multipartFile
        }else {
            println("文件不存在")
            return null
        }
    }

    MultipartFile convertToMultipartFileByInputStream(InputStream inputStream,String FileName){
        FileItem item = new DiskFileItemFactory().createItem("file"
//                如果这里有问题就 MediaType.TEXT_PLAIN_VALUE 等会看一下这个是什么
                , MediaType.MULTIPART_FORM_DATA_VALUE
                , true
                , FileName)
        try{
            OutputStream os = item.getOutputStream()
            IOUtils.copy(inputStream, os)
            MultipartFile multipartFile = new CommonsMultipartFile(item)
            return multipartFile
        }catch (Exception e){
            throw new IllegalArgumentException("inputStream设置失败"+e)
        }
    }

    String getDirectoryByFileBucket(String fileBucket){
        File directory = new File(systemSavePathConfig+File.separator+fileBucket)
        return directory.getPath()
    }
}
