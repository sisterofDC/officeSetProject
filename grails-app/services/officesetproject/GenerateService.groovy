package officesetproject

import freemarker.template.Configuration
import freemarker.template.Template
import freemarker.template.TemplateException
import grails.gorm.transactions.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Transactional
@Service
class GenerateService {
    @Autowired
    Configuration freeMarkerConfig

    def generateTest(){
        // Create the root hash
        Map<String, Object> root = new HashMap<>();
        root.put("name", "sisterofdc");
        begin("batchUpload.ftl",root)
    }


    public void begin(String templateFile, Map<String, Object> obj) {
        StringWriter sw = new StringWriter();
        try {
            Template temp = freeMarkerConfig.getTemplate(templateFile);
            Writer out = new OutputStreamWriter(System.out);
            temp.process(obj, out);
        } catch (TemplateException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
