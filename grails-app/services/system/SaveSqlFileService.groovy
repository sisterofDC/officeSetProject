package system

import grails.gorm.transactions.Transactional
import groovy.sql.GroovyRowResult
import groovy.sql.Sql
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.AsyncResult
import org.springframework.stereotype.Component

import javax.sql.DataSource
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.Future
import java.util.regex.Matcher
import java.util.regex.Pattern


@CompileStatic
@Component
@Transactional
class SaveSqlFileService {
    /** 连接的数据库 */
    @Autowired
    DataSource dataSource
    /** 自动注册grailsApplication 的bean 用于获取当前运行的程序的信息 */
    @Autowired
    String systemSavePathConfig
    /** sql文件命名 */
    private final String sqlFileName = "backup"
    FileSystemService fileSystemService

    /**
     * 保存一个sql文件，已经优化成buffered缓存写入，集成到文件系统里面
     * @return 文件路径
     */
    void saveFile(){
//        创建临时文件
        File tempFile = File.createTempFile(sqlFileNamed(),".sql",new File(systemSavePathConfig))
//        sql驱动
        Sql sql = new Sql(dataSource)
//        缓冲写入文件
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))
//        开始编写sql文件
        generateSqlFile(sql,writer)
//        缓存用完后关闭
        writer.close()
//        sql查询后用完后关闭
        sql.close()
//        变成然后变成MultipartFile
//        然后对接文件系统的接口
        fileSystemService.uploadFile(fileSystemService.convertToMultipartFileByFile(tempFile),"BackUpMysqlFileDirectory")
//        删除临时文件
        tempFile.delete()
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
//            这里需要拿到所有的属性
            List<GroovyRowResult> resultSetList= sql.rows("show full columns from " +dataBaseName+"."+ table+ ";")
//            用于生成插入语句的基本两个属性，名字和状态
            List<String> listColumnName = new ArrayList<String>()
            List<String> listColumnType = new ArrayList<String>()
            resultSetList.forEach { result ->
//                用于生成insert语句只要列名
                listColumnName.add(result.get("Field").toString())
                listColumnType.add(result.get("type").toString())
            }
//            生成create 语句
            String createSqlStatement= generateCreateSql(table,resultSetList)
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

//    创标语句还要修改
    private static String generateCreateSql(String tableName, List<GroovyRowResult> sqlShowColumnsResult){
//        {Field=id, Type=bigint(20), Null=NO, Key=PRI, Default=null, Extra=auto_increment} 解析字符串
        StringBuilder sqlStatement = new StringBuilder()
        sqlStatement.append("DROP TABLE IF EXISTS `").append(tableName).append("`;\n")
        sqlStatement.append("CREATE TABLE IF NOT EXISTS `").append(tableName).append("` (\n")

        sqlShowColumnsResult.each {resultSet->
            String field = resultSet.get("field").toString()
            String type = resultSet.get("type").toString()
            String isNullable = resultSet.get("null").toString()
            String key = resultSet.get("key").toString()
            String defaultName = resultSet.get("Default").toString()
            String extra = resultSet.get("extra").toString()
            String comment = resultSet.get("comment").toString()
            String createSQL = generateColumn(field, type, isNullable, key, defaultName, extra, comment)
            sqlStatement.append(createSQL)
        }
        if (sqlStatement.length() > 2) {
            sqlStatement.setLength(sqlStatement.length() - 2)
        }

        sqlStatement.append(") ENGINE = InnoDB CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;\n\n")
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
//               这里还要改
                if (type=="bit(1)"){
                    if (value.equalsIgnoreCase("true")){
                        valuesSet.append("").append(1).append(", ")
                    }else if (value.equalsIgnoreCase("false")){
                        valuesSet.append("").append(0).append(", ")
                    }
                }else if ("TINYINT(1)".equalsIgnoreCase(type)) {
                    if ("true".equalsIgnoreCase(value)) {
                        valuesSet.append(1).append(", ")
                    } else if ("false".equalsIgnoreCase(value)) {
                        valuesSet.append(0).append(", ")
                    }
                } else {
//                    实际测试中发现需要转换 ' 单冒号
                    value = value.replaceAll("'", "\\\\'")
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
    Future asyncMethodWithReturnType() {
        System.out.println("Execute method asynchronously - " + Thread.currentThread().getName())
        try {
            saveFile()
            return new AsyncResult(null)
        } catch (InterruptedException e) {
            log.println(e)
        }
        return null
    }

    /**
     * 获取文件命名
     * @return
     */
    String sqlFileNamed(){
//        获取当前的时间
        LocalDate date = LocalDate.now()
        LocalTime time = LocalTime.now()
        LocalDateTime dateTime = LocalDateTime.of(date, time)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss")
        String formattedDate = dateTime.format(formatter)
        return formattedDate+"BackUpMySQLFile"
    }


    /**
     * 分别对应不同的创表语句
     * @param field
     * @param type
     * @param isNullable
     * @param key
     * @param defaultValue
     * @param extra
     * @param comment
     * @return
     */
    private static String generateColumn(String field, String type, String isNullable, String key, String defaultValue, String extra, String comment){
        StringBuilder sqlStatement = new StringBuilder()
        sqlStatement.append("`").append(field).append("` ").append(type)
//                设置主键
        if ("PRI".equalsIgnoreCase(key)) {
            sqlStatement.append(" PRIMARY KEY")
        }
//                设置为空     默认 不为空 和 空字符就加入
        if ("NO".equalsIgnoreCase(isNullable)) {
            sqlStatement.append(" NOT NULL")
            if (defaultValue!=null){
//                不为空加进去
                if (!defaultValue.isEmpty()){
//                    如果他默认为deault 这个字段就是特殊处理
                    if (!defaultValue.equalsIgnoreCase("DEFAULT")){
                        sqlStatement.append(" DEFAULT ").append(defaultValue)
                    }else {
                        sqlStatement.append(" DEFAULT ").append("'DEFAULT'")
                    }
                }
            }
        } else if ("YES".equalsIgnoreCase(isNullable)) {
            sqlStatement.append(" NULL")
            sqlStatement.append(" DEFAULT ").append("NULL")
        }

//      自增
        if ("auto_increment".equalsIgnoreCase(extra)) {
            sqlStatement.append(" AUTO_INCREMENT")
        }
//        添加注释
        if (!comment.isEmpty()){
            sqlStatement.append(" COMMENT ").append("'").append(comment).append("' ")
        }
        sqlStatement.append(",\n")
        return  sqlStatement.toString()
    }
}


