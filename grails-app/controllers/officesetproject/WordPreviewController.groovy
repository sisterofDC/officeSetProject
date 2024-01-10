package officesetproject

class WordPreviewController {
    WordPreviewService wordPreviewService

    def pdfToWord(){
        if (request.method=="POST"){
            wordPreviewService.convertToWord()
        }else {
          render(view: "pdfToWord")
        }
    }

    def index() { }
}
