package dev.codextended.exporttoexcel.utils;

import dev.codextended.exporttoexcel.domain.Employee;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class ExcelUploadUtils {

    public static boolean isValidExcelFile(MultipartFile file) {
        return Objects.equals(file.getContentType(), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }

    public static List<Employee> getEmployeesDataFromExcel(InputStream inputStream) {
        List<Employee> employees = new ArrayList<>();

        XSSFWorkbook workbook = null;

        try {
            workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheet("Payroll");

            int rowIndex = 0;
            for (Row row : sheet){
                if (rowIndex == 0) {
                    rowIndex++;
                    continue;
                }
                Iterator<Cell> cellIterator = row.iterator();
                int cellIndex = 0;
                Employee employee = new Employee();

                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    switch (cellIndex) {
                        case 0 -> employee.setLastname(cell.getStringCellValue());
                        case 1 -> employee.setFirstname(cell.getStringCellValue());
                        case 2 -> employee.setEntryDate(LocalDate.parse(cell.getStringCellValue()));
                        case 3 -> employee.setFunction(cell.getStringCellValue());
                        case 4 -> employee.setCategory(cell.getStringCellValue());
                        case 5 -> employee.setActualSalary(BigDecimal.valueOf(cell.getNumericCellValue()));
                        default -> {
                        }
                    }
                    cellIndex++;
                }
                employees.add(employee);
            }
        } catch (IOException e) {
            e.getStackTrace();
        }

        return employees;
    }
}
