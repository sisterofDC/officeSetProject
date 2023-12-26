package officesetproject

import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import spock.lang.Specification
import org.hibernate.SessionFactory

@Integration
@Rollback
class KeyWordCodeServiceSpec extends Specification {

    KeyWordCodeService keyWordCodeService
    SessionFactory sessionFactory

    private Long setupData() {
        // TODO: Populate valid domain instances and return a valid ID
        //new KeyWordCode(...).save(flush: true, failOnError: true)
        //new KeyWordCode(...).save(flush: true, failOnError: true)
        //KeyWordCode keyWordCode = new KeyWordCode(...).save(flush: true, failOnError: true)
        //new KeyWordCode(...).save(flush: true, failOnError: true)
        //new KeyWordCode(...).save(flush: true, failOnError: true)
        assert false, "TODO: Provide a setupData() implementation for this generated test suite"
        //keyWordCode.id
    }

    void "test get"() {
        setupData()

        expect:
        keyWordCodeService.get(1) != null
    }

    void "test list"() {
        setupData()

        when:
        List<KeyWordCode> keyWordCodeList = keyWordCodeService.list(max: 2, offset: 2)

        then:
        keyWordCodeList.size() == 2
        assert false, "TODO: Verify the correct instances are returned"
    }

    void "test count"() {
        setupData()

        expect:
        keyWordCodeService.count() == 5
    }

    void "test delete"() {
        Long keyWordCodeId = setupData()

        expect:
        keyWordCodeService.count() == 5

        when:
        keyWordCodeService.delete(keyWordCodeId)
        sessionFactory.currentSession.flush()

        then:
        keyWordCodeService.count() == 4
    }

    void "test save"() {
        when:
        assert false, "TODO: Provide a valid instance to save"
        KeyWordCode keyWordCode = new KeyWordCode()
        keyWordCodeService.save(keyWordCode)

        then:
        keyWordCode.id != null
    }
}
