package poi;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import java.io.FileOutputStream;

// use apache-poi
public class CreateExcelTest {
    public static void main(String[] args) throws Exception {
        // map to a excel file
        HSSFWorkbook wb = new HSSFWorkbook();
        // map to a sheet in the wb file
        HSSFSheet sheet = wb.createSheet("StudentList");
        // map to a row
        HSSFRow row = sheet.createRow(0);// row index
        // map to a col
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("Name");
        cell = row.createCell(1);
        cell.setCellValue("Age");

        HSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);

        for(int i=1; i<=10; i++) {
            row = sheet.createRow(i);
            cell = row.createCell(0);
            cell.setCellValue("student" + i);
            cell = row.createCell(1);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(15 + i);
        }

        FileOutputStream os = new FileOutputStream("D:\\try-poi\\studentList.xls");
        wb.write(os);

        os.close();
    }
}
