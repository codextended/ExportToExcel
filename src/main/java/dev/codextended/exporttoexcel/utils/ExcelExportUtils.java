package dev.codextended.exporttoexcel.utils;

import dev.codextended.exporttoexcel.response.EmployeeExport;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

public class ExcelExportUtils {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<EmployeeExport> employeeExportList;

    public ExcelExportUtils(List<EmployeeExport> employeeExportList) {
        this.employeeExportList = employeeExportList;
        workbook = new XSSFWorkbook();
    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);

        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        }else if (value instanceof Long) {
            cell.setCellValue((Long) value);
        }else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        }else if (value instanceof LocalDate) {
            cell.setCellValue((LocalDate) value);
        }else  {
            cell.setCellValue((String) value);
        }

        cell.setCellStyle(style);
    }

    private void createHeaderRow() {
        sheet = workbook.createSheet("Ajustement du Payroll");
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();

        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(20);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        createCell(row, 0, "Ajustement du Payroll", style);
        sheet.addMergedRegion(new CellRangeAddress(0,0,0,9));
        font.setFontHeightInPoints((short) 10);

        row = sheet.createRow(1);
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        createCell(row, 0, "Prenom", style);
        createCell(row, 1, "Nom", style);
        createCell(row, 2, "Date d'Entree", style);
        createCell(row, 3, "Fonction", style);
        createCell(row, 4, "Category", style);
        createCell(row, 5, "Salaire Actuel", style);
        createCell(row, 6, "Mois de Service", style);
        createCell(row, 7, "Pourcentage", style);
        createCell(row, 8, "Nouveau Salaire", style);
//        createCell(row, 9, "Ancienne Masse Salariale", style);
//        createCell(row, 10, "Nouvelle Masse Salariale", style);
    }

    private void writeCustomerData() {
        int rowCount = 2;
        BigDecimal ancienneMasseSalariale = BigDecimal.valueOf(0);
        BigDecimal totalAjustement = BigDecimal.valueOf(0);
        BigDecimal nouvelleMasseSalariale = BigDecimal.valueOf(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);

        for (EmployeeExport employeeExport : employeeExportList) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++, employeeExport.getFirstname(), style);
            createCell(row, columnCount++, employeeExport.getLastname(), style);
            createCell(row, columnCount++, employeeExport.getEntryDate(), style);
            createCell(row, columnCount++, employeeExport.getFunction(), style);
            createCell(row, columnCount++, employeeExport.getCategory(), style);
            createCell(row, columnCount++, employeeExport.getActualSalary(), style);
            createCell(row, columnCount++, employeeExport.getMonthOfService(), style);
            createCell(row, columnCount++, employeeExport.getPercentage(), style);
            createCell(row, columnCount++, employeeExport.getNewSalary(), style);
            ancienneMasseSalariale = ancienneMasseSalariale.add(BigDecimal.valueOf(employeeExport.getActualSalary()));
            nouvelleMasseSalariale = nouvelleMasseSalariale.add(BigDecimal.valueOf(employeeExport.getNewSalary()));
        }
        Row row = sheet.createRow(rowCount);
        createCell(row, 5, ancienneMasseSalariale.doubleValue(), style);
        createCell(row, 8, nouvelleMasseSalariale.doubleValue(), style);
        createCell(row, 7, calculatePercentage(ancienneMasseSalariale, nouvelleMasseSalariale), style);
    }

    private double calculatePercentage(BigDecimal ancienneMasseSalariale, BigDecimal nouvelleMasseSalariale) {
        BigDecimal percentage = nouvelleMasseSalariale
                .subtract(ancienneMasseSalariale)
                .multiply(BigDecimal.valueOf(100))
                .divide(ancienneMasseSalariale, 2, RoundingMode.HALF_UP);

        return percentage.doubleValue();


    }

    public void exportDataToExcel(HttpServletResponse response) throws IOException {
        createHeaderRow();
        writeCustomerData();
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
}
