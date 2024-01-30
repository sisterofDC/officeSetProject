package system

import grails.converters.JSON

class ServeRenderingController {
    ServeRenderingService serveRenderingService
    LibreOfficeInterfaceService libreOfficeInterfaceService
    def index() {
        if (request.method=="POST"){
            libreOfficeInterfaceService.wordToPNG()
            render([code:200] as JSON)
        }else {
            render(view: "index")
        }
    }
}
