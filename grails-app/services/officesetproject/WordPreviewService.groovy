package officesetproject

import com.itextpdf.text.pdf.PdfReader
import com.itextpdf.text.pdf.parser.PdfTextExtractor
import grails.gorm.transactions.Transactional
import org.apache.poi.xwpf.usermodel.XWPFDocument
import org.apache.poi.xwpf.usermodel.XWPFParagraph

@Transactional
class WordPreviewService {

    def convertToWord(){
        try {
            // 读取PDF文件
            PdfReader pdfReader = new PdfReader("D:\\PDFToWord\\input.pdf");

            String pdfText = "";
            for (int page = 1; page <= pdfReader.getNumberOfPages(); page++) {
                pdfText += PdfTextExtractor.getTextFromPage(pdfReader, page);
            }
            pdfReader.close();
            // 创建Word文档
            XWPFDocument doc = new XWPFDocument();
            XWPFParagraph p = doc.createParagraph();
            p.createRun().setText(pdfText);
            // 写入Word文件
            FileOutputStream out = new FileOutputStream(new File("D:\\PDFToWord\\output.docx"));
            doc.write(out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    def serviceMethod() {

    }
}
