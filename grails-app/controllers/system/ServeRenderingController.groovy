package system

import grails.converters.JSON

class ServeRenderingController {
    ServeRenderingService serveRenderingService
    def index() {
        if (request.method=="POST"){
            serveRenderingService.chromeDriverPrint()
            render([code:200] as JSON)
        }else {
            render(view: "index")
        }
    }
}
