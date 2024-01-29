package system

import grails.gorm.transactions.Transactional
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.CreationHelper
import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.VerticalAlignment
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.util.WorkbookUtil
import org.apache.poi.xssf.usermodel.XSSFWorkbook


/**
 * 本代码由POI 5 生成，注意依赖
 */

@Transactional
class ExcelToolsService {

//    文件生成的地方，到时候进行修改
    public String xlsFileName = "D:\\generateTest\\"+ "simpleWrite" + System.currentTimeMillis() + ".xls";
    public String xlsxFileName  = "D:\\generateTest\\"+ "simpleWrite" + System.currentTimeMillis() + ".xlsx";

    def createNewWorkbook(){
//        生成xls
        Workbook wbxls = new HSSFWorkbook();
        try (OutputStream fileOut = new FileOutputStream(xlsFileName)) {
            wbxls.write(fileOut);
        }
//        生成xlsx
        Workbook wbxlsx = new XSSFWorkbook();
        try (OutputStream fileOut = new FileOutputStream(xlsxFileName)) {
            wbxlsx.write(fileOut);
        }
    }

    def createSheet(){
        Workbook xlsWb = new HSSFWorkbook();
//         Workbook xlsxWb = new XSSFWorkbook();
//        下面的表格选项栏创建 ,有一定的限制，不能超过31个字符，不能包括下面字符
//         0x0000  0x0003 colon (:) backslash (\) asterisk (*) question mark (?) forward slash (/) opening square bracket ([) closing square bracket (])
        String safeSheet = WorkbookUtil.createSafeSheetName("表格一")
//        这里进行设置下面的sheet 名设置
        Sheet createSheet = xlsWb.createSheet(safeSheet)
        try (OutputStream fileOut = new FileOutputStream(xlsFileName)) {
            xlsWb.write(fileOut);
        }
    }

//    开始往表格里面写数据
    def createCells(){
        Workbook xlsWb = new HSSFWorkbook();
//         Workbook xlsxWb = new XSSFWorkbook();
        String safeSheet = WorkbookUtil.createSafeSheetName("表格一")
        Sheet sheetOne = xlsWb.createSheet(safeSheet)


        CreationHelper creationHelper = xlsWb.getCreationHelper()
//        拿到单元格的格式说明。
        CellStyle cellStyleWithDate = xlsWb.createCellStyle()
        CellStyle cellStyleWithSetAlignment = xlsWb.createCellStyle()


//        写在第一行 由0开始的
        Row row = sheetOne.createRow(0);
//        在第一个中
        Cell cell = row.createCell(0)
        cell.setCellValue("测试")

        row.createCell(1).setCellValue("测试1")
        row.createCell(2).setCellValue("测试2")
        row.createCell(3).setCellValue("测试3")
        row.createCell(4).setCellValue(
                creationHelper.createRichTextString("这个一个富文本的string")
        )
//        设置单元格的格式，时间设置
        cellStyleWithDate.setDataFormat(creationHelper.createDataFormat().getFormat("m/d/yy"))
        Cell cellInColumnFive = row.createCell(5)
//        设置时间
        cellInColumnFive.setCellValue(new Date())
//        设置格式
        cellInColumnFive.setCellStyle(cellStyleWithDate)
        Row rowInTwo = sheetOne.createRow(1)

        rowInTwo.setHeightInPoints(30)
//        设置分布
        cellStyleWithSetAlignment.setAlignment(HorizontalAlignment.CENTER)
        cellStyleWithSetAlignment.setVerticalAlignment(VerticalAlignment.CENTER)

        rowInTwo.createCell(0,)



        try (OutputStream fileOut = new FileOutputStream(xlsFileName)) {
            xlsWb.write(fileOut);
        }
    }



    def serviceMethod() {

    }
}
