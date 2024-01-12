package officesetproject

import grails.gorm.transactions.Transactional
import groovy.sql.GroovyRowResult
import grails.core.GrailsApplication
import groovy.sql.Sql
import groovy.transform.CompileStatic

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.AsyncConfigurer
import org.springframework.scheduling.annotation.AsyncResult
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.stereotype.Component

import javax.sql.DataSource
import java.text.SimpleDateFormat
import java.util.concurrent.Executor
import java.util.concurrent.Future
import java.util.regex.Matcher
import java.util.regex.Pattern

@Configuration
@EnableAsync   //开启异步任务支持
class SpringTaskExecutor implements AsyncConfigurer {

    @Override
    /**
     * 设置新的线程池，从线程池里面进行调用
     */
    Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor()
        taskExecutor.setCorePoolSize(5)
        taskExecutor.setMaxPoolSize(10)
        taskExecutor.setQueueCapacity(20)
        taskExecutor.initialize()
        return taskExecutor
    }

    @Override
    AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return null
    }
}

@CompileStatic
@Component
@Transactional
class SaveSqlFileService {
    /** 连接的数据库 */
    @Autowired
    DataSource dataSource
    /** 自动注册grailsApplication 的bean 用于获取当前运行的程序的信息 */
    @Autowired
    GrailsApplication grailsApplication
    /** 存放sqlFile的文件目录 */
    private final String sqlFileDictionary = "backup"
    /** sql文件命名 */
    private final String sqlFileName = "backup"

    /**
     * 保存一个sql文件，已经优化成buffered缓存写入，静态文件现在全部采用映射的方法，没有直接的文件目录
     * @return 文件路径
     */
    String saveFile(){
//        文件路径
        String filePath = nameSqlFile()
//        sql驱动
        Sql sql = new Sql(dataSource)
//        打开文件
        File outputFile = grailsApplication.mainContext.getResource(filePath).getFile()
//        缓冲写入文件
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))
//        开始编写sql文件
        generateSqlFile(sql,writer)
//        缓存用完后关闭
        writer.close()
//        sql查询后用完后关闭
        sql.close()
        return filePath
    }

    /**
     * 生成sql文件
     * 1· select database(); 拿到database
     * 2· show tables; 拿到所有的table名
     * 3· show columns from database_name.table_name; 拿到当前table的所有列名和属性
     * 4· select * from table_name; 获取所有的数据
     * @param sql
     * @param writer
     */
    private static void generateSqlFile(Sql sql,BufferedWriter writer){
        List<String> listDataBaseName = new ArrayList<String>()
        List<String> realTableName = new ArrayList<String>()
//        获取数据库名 database 的名字。这里不考虑用了多个数据库的情况
        sql.eachRow('select database();'){row ->
            listDataBaseName.add(row.getString("database()"))
        }
        if (listDataBaseName[0]!=""){
            List<String> listTableName = new ArrayList<String>()
//        获取当前数据库 database 所有的 table 的名字
            sql.eachRow("show tables;"){row->
                listTableName.add(row.toString())
            }
            listTableName.forEach {table ->
                String tableName = getRealTableName(table)
                realTableName.add(tableName)
            }
            String createDataBase = "CREATE DATABASE IF NOT EXISTS ${listDataBaseName[0]} CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;\n"
            writer.write(createDataBase)
            String useDataBase = "USE ${listDataBaseName[0]};\n"
            writer.write(useDataBase)
            showAllColumn(realTableName,listDataBaseName[0],sql,writer)
        }else {
            throw new RuntimeException("无法获取数据库dataBase")
        }
    }

    /**
     * 获取当前表名的中的所有列名，获取后编写create语句和insert语句
     * @param tableNames 所有的表名
     * @param dataBaseName 数据库名
     * @param sql 数据库连接
     * @param writer 缓存写入
     */
    private static void showAllColumn(List<String> tableNames, String dataBaseName, Sql sql, BufferedWriter writer){
//        实现sql 语句 show columns from database_name.table_name;
        tableNames.forEach {table ->
            List<GroovyRowResult> resultSetList= sql.rows("show columns from " +dataBaseName+"."+ table+ ";")
            List<String> sqlShowColumnsResult = new ArrayList<String>()
            List<String> listColumnName = new ArrayList<String>()
            List<String> listColumnType = new ArrayList<String>()
            resultSetList.forEach { result ->
//                用于生成insert语句只要列名
                listColumnName.add(result.get("Field").toString())
                listColumnType.add(result.get("type").toString())
//                用于生成create语句需要所有的属性
                sqlShowColumnsResult.add(result.toString())
            }
//            生成create 语句
            String createSqlStatement= generateCreateSql(table,sqlShowColumnsResult)
            writer.write(createSqlStatement)
//            生成insert 语句
            generateInsertSql(table,listColumnName,sql,writer,listColumnType)
        }
    }


    /**
     * 解析 show columns from database_name.table_name; 中每一条数据，然后字段生成
     * @param tableName
     * @param sqlShowColumnsResult
     * @return 创建数据库table 的sql 语句
     */
    private static String generateCreateSql(String tableName, List<String> sqlShowColumnsResult){
//        {Field=id, Type=bigint(20), Null=NO, Key=PRI, Default=null, Extra=auto_increment} 解析字符串
        StringBuilder sqlStatement = new StringBuilder("CREATE TABLE IF NOT EXISTS ${tableName} (")
        for (String row : sqlShowColumnsResult) {
            String[] parts = row.split(", ")
            if (parts.length == 6) {
                String field = parts[0].substring(parts[0].indexOf('=') + 1)
                String type = parts[1].substring(parts[1].indexOf('=') + 1)
                String isNullable = parts[2].substring(parts[2].indexOf('=') + 1)
                String key = parts[3].substring(parts[3].indexOf('=') + 1)
                String defaultValue = parts[4].substring(parts[4].indexOf('=') + 1)
                String extra = parts[5].substring(parts[5].indexOf('=') + 1)
                sqlStatement.append("`").append(field).append("` ").append(type)
//                设置主键
                if (key.equalsIgnoreCase("PRI")){
                    sqlStatement.append(" PRIMARY KEY")
                }
//                设置为空
                if (isNullable.equalsIgnoreCase("NO")) {
                    sqlStatement.append(" NOT NULL")
                }else if (isNullable.equalsIgnoreCase("NO")){
                    sqlStatement.append(" NULL")
                }
//                默认
                if (!defaultValue.equalsIgnoreCase("null")) {
                    sqlStatement.append(" DEFAULT ").append(defaultValue)
                }
//                自增
                if (extra.equalsIgnoreCase("auto_increment}")) {
                    sqlStatement.append(" AUTO_INCREMENT")
                }
                sqlStatement.append(", ")
            }
        }
//        删最后那个逗号 ,
        if (sqlStatement.length() > 19) {
            sqlStatement.setLength(sqlStatement.length() - 2)
        }
//        指定为中文
        sqlStatement.append(") character set = utf8 ;\n\n")
        return sqlStatement
    }

    /**
     * 生成insert语句
     * @param tableName
     * @param columnName
     * @param sql
     * @param writer
     */
    private static void generateInsertSql(String tableName, List<String> columnName, Sql sql, BufferedWriter writer, List<String> columnType){
        StringBuilder columnSet = new StringBuilder()
        columnName.forEach {column->
            columnSet.append(column).append(", ")
        }
//        删除最后那个,
        columnSet.deleteCharAt(columnSet.length()-2)
        sql.eachRow('SELECT * FROM '+tableName){row->
            StringBuilder valuesSet = new StringBuilder()
//            除了boolean需要额外判断，也就是bit(1)的属性，因为不能加单引号
            for (Integer index in 0..<columnName.size()) {
                String column = columnName.get(index)
                String type = columnType.get(index)
                String value = row[column].toString()
                if (type=="bit(1)"){
                    if (value.equalsIgnoreCase("true")){
                        valuesSet.append("").append(1).append(", ")
                    }else if (value.equalsIgnoreCase("false")){
                        valuesSet.append("").append(0).append(", ")
                    }
                }else {
//                    实际测试中发现需要转换 ' 单冒号
                    value = value.replaceAll("'", "\\\\'");
                    valuesSet.append("'").append(value).append("', ")
                }
            }
            //        删除最后那个,
            valuesSet.deleteCharAt(valuesSet.length()-2)
//            StringBuilder sqlStatement = new StringBuilder("INSERT INTO ${tableName} (${columnSet}) VALUES (${valuesSet});\n")
            StringBuilder sqlStatement = new StringBuilder("INSERT INTO ${tableName}  VALUES (${valuesSet});\n")
            writer.write(sqlStatement.toString())
        }
        writer.write("\n")
    }


    /**
     * 创建目录
     * @return boolean 是否创建成功
     */
    private Boolean createDirectory(){
        File directory = grailsApplication.mainContext.getResource(sqlFileDictionary).getFile()
        if (!directory.exists()){
            Boolean resultMkdir =directory.mkdir()
            return resultMkdir == true
        }else {
            return false
        }
    }

    /**
     * 命名和返回路径
     * @return 返回sql文件的路径
     */
    private String nameSqlFile(){
        createDirectory()
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss")
        String currentDate = sdf.format(Calendar.getInstance().getTime())
        String fileName = sqlFileName+currentDate
        String filePath="${sqlFileDictionary}/"+fileName+".sql"
        return filePath
    }

    /**
     * 解析row 中的 table名
     * @param showTableSqlResult
     * @return string table_name 的值 错误为 ""
     */
    private static String getRealTableName(String showTableSqlResult){
        Pattern pattern = Pattern.compile("\\[TABLE_NAME:(.*?)\\]")
        Matcher matcher = pattern.matcher(showTableSqlResult)
        if (matcher.find()) {
            // Extract the volunteer name from the matched group
            String matchStr= matcher.group(1)
            return matchStr
        } else {
            System.out.println("没有找到table")
            return ""
        }
    }


    /**
     * 异步处理加载的sql文件
     * @return
     */
    @Async
    Future<String> asyncMethodWithReturnType() {
        System.out.println("Execute method asynchronously - "
                + Thread.currentThread().getName())
        try {
            String result = saveFile()
            return new AsyncResult<String>(result)
        } catch (InterruptedException e) {
            log.println(e)
        }
        return null
    }

    /**
     * 初始化一个映射静态文件的一个MAP，用于查找和删除
     * @param fileMap
     */
    void initIndexToSqlFile(Map<Integer,SqlFileSet> fileMap){
        File directory = grailsApplication.mainContext.getResource(sqlFileDictionary).getFile()
        if (directory.isDirectory()){
            File[] files = directory.listFiles()
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    System.out.println("这是一级目录一个文件夹:"+files[i].getAbsolutePath())
                } else {
//                    只扫描一级目录
                    File tempFile = files[i]
                    Long fileSize = tempFile.length()
                    String fileName = tempFile.name
                    String filePath = "${sqlFileDictionary}/"+fileName
                    Date fileCreate = new Date(tempFile.lastModified())
                    SqlFileSet sqlFileSet = new SqlFileSet(i,fileName,fileSize,filePath,fileCreate)
                    fileMap.put(i,sqlFileSet)
                }
            }
        }else {
            System.out.println("这是一个文件："+directory.getPath())
        }
    }

    /**
     * 按照隐射的id进行的删除
     * @param id
     * @return boolean 是否删除
     */
    Boolean deleteSqlFile(Integer id){
        Map<Integer,SqlFileSet> fileMap = new HashMap<>()
        initIndexToSqlFile(fileMap)
        SqlFileSet fileSet = fileMap.get(id)
        if (fileSet!=null){
            File sqlFile = grailsApplication.mainContext.getResource(fileSet.fileUrl).getFile()
            if (sqlFile.exists()){
                Boolean resultDelete= sqlFile.delete()
                return resultDelete == true
            }else {
                return false
            }
        }else {
            return false
        }
    }

    /**
     * 静态文件的搜索实际搜索的是fileName
     * @param keyword
     * @return list 静态文件的映射
     */
    List<SqlFileSet> searchFileList(String keyword){
        Map<Integer,SqlFileSet> fileMap = new HashMap<>()
        initIndexToSqlFile(fileMap)
        List<SqlFileSet> matchedFileSets = new ArrayList<>()
        // 遍历映射并查找匹配的条目
        for (Map.Entry<Integer, SqlFileSet> entry : fileMap.entrySet()) {
            SqlFileSet sqlFileSet = entry.getValue()
            // 假设 SqlFileSet 中有一个方法来获取需要匹配的字符串属性
            String fileName = sqlFileSet.fileName // 请替换为你的实际属性名称
            if (fileName != null && fileName.contains(keyword)) {
                matchedFileSets.add(sqlFileSet)
            }
        }
        return matchedFileSets
    }



    /**
     * 映射静态文件的类
     */
    class SqlFileSet{
        Integer id
        String fileName
        long fileSize
        String fileUrl
        Date gmt_modified
        SqlFileSet(Integer id, String fileName, long fileSize, String fileUrl, Date gmt_modified) {
            this.id = id
            this.fileName = fileName
            this.fileSize = fileSize
            this.fileUrl = fileUrl
            this.gmt_modified = gmt_modified
        }
    }

}


