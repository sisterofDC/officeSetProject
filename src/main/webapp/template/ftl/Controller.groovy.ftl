package ${packageName}

import grails.converters.JSON
import grails.gorm.transactions.Transactional
import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.annotation.Secured

@Transactional
class ${domainName}Controller {

   static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

// 表格生成
   @Secured(['ROLE_系统管理员'])
   def list(){
      if (request.method == "POST"){
         def page = (params.int('page') ?: 0)
         def limit = (params.int('limit') ?: 20)
         if (page > 0) {
            page = page - 1
         }
         params.offset = page * limit
<#--查询的类名 -->
         def criteria = ${domainName}.createCriteria()
<#--更改查询语句-->
         def results = criteria.list(max: params.limit, offset: params.offset) {
<#--类名下面的参数查询-->
<#list classPropertiesQuery as property>
            // 查询${property.classPropertyChinese}
            if(params.${property.classPropertyName}){
               ${property.query}
            }
</#list>
            order('dateCreated', 'desc')
         }
         def result = [
            code : 0, msg: "",
            data : results,
            count: results.totalCount
         ]
         render result as JSON
      }else {
         render(view: "list")
      }
   }

// 查看功能
   @Secured(['ROLE_系统管理员'])
   def edit() {
      if (request.method == "POST") {
// 验证ID
      <#noparse>if (params.id && params.id =~ /^[0-9]*$/)</#noparse> {
            def ${domainVariableName} = ${domainName}.get(params.long("id"))
            if (${domainVariableName}) {
               def successResponseData = [
                  "code":200,
                  "data":${domainVariableName},
               ]
               render successResponseData as JSON
            } else {
            render ([code: 404, text: "未找到记录"] as JSON)
            }
         } else {
            render ([code: 400, text: "非法请求"] as JSON)
         }
      } else {
         render(view: "edit")
      }
   }

// 保存功能
   @Secured(['ROLE_系统管理员'])
   def save() {
      def ${domainVariableName}
      if (params.id) {
         ${domainVariableName} = ${domainName}.get(params.long("id"))
         ${domainVariableName}.properties = params
      } else {
         ${domainVariableName} = new ${domainName}(params)
      }
// -------------添加更多的参数验证-------------
// 更新和创建新的实例的时候做好参数验证


// -------------添加更多的参数验证-------------

      if (!${domainVariableName}.hasErrors() && ${domainVariableName}.validate()) {
         if (${domainVariableName}.save(failOnError: true)) {
            def result = [code: 200, text: params.id ? "更新成功" : "新增成功"]
            render result as JSON
         } else {
            def result = [code: 500, text: "失败"]
            render result as JSON
         }
      } else {
         println ${domainVariableName}.errors
         def result = [code: 500, text: ${domainVariableName}.errors.toString().replaceAll(/["'\n]/, '')]
         render result as JSON
      }
   }

// 删除为危险操作，请添加权限验证
// 当前操作为批量操作
   @Secured(['ROLE_系统管理员'])
   def cmd() {
      if (request.method == "POST") {
         def ids = params.ids?.trim()?.tokenize(',')
         if (ids) {
            ids.each { id ->
               if (id && id =~ /^[0-9]*$/) {
                  def ${domainVariableName} = ${domainName}.get(id)
                  // 如果需要，添加更多权限验证
                  if (SpringSecurityUtils.ifAnyGranted('ROLE_系统管理员')) {
                     if (params.cmd == "delete") {
                        ${domainVariableName}?.delete(failOnError: true)
                     }
                  }
               }
            }
         }
         render ([code: 200, text: "操作成功"] as JSON)
      } else {
         render ([code: 400, text: "请求方式错误"] as JSON)
      }
   }

   def sessionFactory

   def batchUpload() {
      if (request.method == "POST") {
         //处理JSON请求
         def jsonRequest = request.JSON
         def successSaveArray = []
         def failSaveArray = []
         if (jsonRequest["data"]==null){
            render ([code: 200, text: "无数据，操作失败"] as JSON)
         }else {
            def uploadListData = jsonRequest["data"]
            uploadListData.each { ${domainVariableName}Data ->
               def ${domainVariableName} = new ${domainName}(${domainVariableName}Data)
               // 创建新的实例的时候做好参数验证
               // -------------添加更多的参数验证-------------



               // -------------添加更多的参数验证-------------


               ${domainVariableName}.save()
            }
            // 批量一次性更新
            sessionFactory.currentSession.flush();
            sessionFactory.currentSession.clear();
            def result = [
               status: 200,
               msg: "上传成功",
               successArray: successSaveArray,
               failArray: failSaveArray,
            ]
            render result as JSON
         }
      } else {
         render(view: "batchUpload")
      }
   }
}
