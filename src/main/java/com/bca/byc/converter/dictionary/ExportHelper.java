package com.bca.byc.converter.dictionary;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;

public class ExportHelper {

    private static CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle headerStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        headerStyle.setFont(font);
        return headerStyle;
    }

    public static void createRow(HSSFSheet sheet, HSSFRow row, int colIndex, String rowName) {
        HSSFCell cell = row.createCell(colIndex);
        cell.setCellValue(rowName);
        cell.setCellStyle(createHeaderStyle(sheet.getWorkbook()));
    }

}
