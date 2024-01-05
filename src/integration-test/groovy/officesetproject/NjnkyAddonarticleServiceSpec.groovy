package officesetproject

import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import spock.lang.Specification
import org.hibernate.SessionFactory

@Integration
@Rollback
class NjnkyAddonarticleServiceSpec extends Specification {

    NjnkyAddonarticleService njnkyAddonarticleService
    SessionFactory sessionFactory

    private Long setupData() {
        // TODO: Populate valid domain instances and return a valid ID
        //new NjnkyAddonarticle(...).save(flush: true, failOnError: true)
        //new NjnkyAddonarticle(...).save(flush: true, failOnError: true)
        //NjnkyAddonarticle njnkyAddonarticle = new NjnkyAddonarticle(...).save(flush: true, failOnError: true)
        //new NjnkyAddonarticle(...).save(flush: true, failOnError: true)
        //new NjnkyAddonarticle(...).save(flush: true, failOnError: true)
        assert false, "TODO: Provide a setupData() implementation for this generated test suite"
        //njnkyAddonarticle.id
    }

    void "test get"() {
        setupData()

        expect:
        njnkyAddonarticleService.get(1) != null
    }

    void "test list"() {
        setupData()

        when:
        List<NjnkyAddonarticle> njnkyAddonarticleList = njnkyAddonarticleService.list(max: 2, offset: 2)

        then:
        njnkyAddonarticleList.size() == 2
        assert false, "TODO: Verify the correct instances are returned"
    }

    void "test count"() {
        setupData()

        expect:
        njnkyAddonarticleService.count() == 5
    }

    void "test delete"() {
        Long njnkyAddonarticleId = setupData()

        expect:
        njnkyAddonarticleService.count() == 5

        when:
        njnkyAddonarticleService.delete(njnkyAddonarticleId)
        sessionFactory.currentSession.flush()

        then:
        njnkyAddonarticleService.count() == 4
    }

    void "test save"() {
        when:
        assert false, "TODO: Provide a valid instance to save"
        NjnkyAddonarticle njnkyAddonarticle = new NjnkyAddonarticle()
        njnkyAddonarticleService.save(njnkyAddonarticle)

        then:
        njnkyAddonarticle.id != null
    }
}
