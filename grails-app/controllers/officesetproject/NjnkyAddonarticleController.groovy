package officesetproject

import grails.converters.JSON
import grails.gorm.transactions.Transactional
import org.springframework.validation.BindingResult


@Transactional
class NjnkyAddonarticleController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

// 表格生成

    def list(){
        if (request.method == "POST"){
            def page = (params.int('page') ?: 0)
            def limit = (params.int('limit') ?: 20)
            if (page > 0) {
                page = page - 1
            }
            params.offset = page * limit
            def criteria = NjnkyAddonarticle.createCriteria()
            def results = criteria.list(max: params.limit, offset: params.offset) {
                // 查询内容
                if(params.body){
                    ilike('body', '%' + params.body + '%')
                }
                // 查询栏目
                if(params.typeid){
                    eq('typeid', Short.valueOf(params.typeid))
                }
                // 查询自定义模板
                if(params.templet){
                    ilike('templet', '%' + params.templet + '%')
                }
                // 查询跳转URL
                if(params.redirecturl){
                    ilike('redirecturl', '%' + params.redirecturl + '%')
                }
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

    def edit() {
        if (request.method == "POST") {
            if (params.id && params.id =~ /^[0-9]*$/) {
                def njnkyAddonarticle = NjnkyAddonarticle.get(params.long("id"))
                if (njnkyAddonarticle) {
                    def successResponseData = [
                            "code":200,
                            "data":njnkyAddonarticle,
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

    def save() {
        def njnkyAddonarticle
        if (params.id) {
            njnkyAddonarticle = NjnkyAddonarticle.get(params.long("id"))
            njnkyAddonarticle.properties = params as BindingResult
        } else {
            njnkyAddonarticle = new NjnkyAddonarticle(params)
        }

        if (!njnkyAddonarticle.hasErrors() && njnkyAddonarticle.validate()) {
            if (njnkyAddonarticle.save(failOnError: true)) {
                def result = [code: 200, text: params.id ? "更新成功" : "新增成功"]
                render result as JSON
            } else {
                def result = [code: 500, text: "失败"]
                render result as JSON
            }
        } else {
            println njnkyAddonarticle.errors
            def result = [code: 500, text: njnkyAddonarticle.errors.toString().replaceAll(/["'\n]/, '')]
            render result as JSON
        }
    }

// 删除为危险操作，请添加权限验证
// 当前操作为批量操作

    def cmd() {
        if (request.method == "POST") {
            def ids = params.ids?.trim()?.tokenize(',')
            if (ids) {
                ids.each { id ->
                    if (id && id =~ /^[0-9]*$/) {
                        def njnkyAddonarticle = NjnkyAddonarticle.get(id)
                        if (params.cmd == "delete") {
                            njnkyAddonarticle?.delete(failOnError: true)
                        }
                    }
                }
            }
            render ([code: 200, text: "操作成功"] as JSON)
        } else {
            render ([code: 400, text: "请求方式错误"] as JSON)
        }
    }
}
