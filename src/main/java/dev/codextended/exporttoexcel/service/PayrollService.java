package dev.codextended.exporttoexcel.service;

import dev.codextended.exporttoexcel.domain.Employee;
import dev.codextended.exporttoexcel.repository.EmployeeRepository;
import dev.codextended.exporttoexcel.response.EmployeeExport;
import dev.codextended.exporttoexcel.utils.EmployeeAdjustmentUtils;
import dev.codextended.exporttoexcel.utils.ExcelExportUtils;
import dev.codextended.exporttoexcel.utils.ExcelUploadUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PayrollService {

    private final EmployeeRepository employeeRepository;

    @Transactional
    public Employee addEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Transactional
    public List<Employee> getEmployees() {
        return employeeRepository.findAll();
    }

    @Transactional
    public void getPayrollFile(HttpServletResponse response) throws IOException {
        List<Employee> employees = employeeRepository.findAll();
        List<EmployeeExport> employeeExportList = EmployeeAdjustmentUtils.employeesToExport(employees);

        ExcelExportUtils exportUtils = new ExcelExportUtils(employeeExportList);
        exportUtils.exportDataToExcel(response);

    }

    @Transactional
    public void saveEmployeesToDatabase(MultipartFile file) {
        if (ExcelUploadUtils.isValidExcelFile(file)) {
            try {
                List<Employee> employees = ExcelUploadUtils.getEmployeesDataFromExcel(file.getInputStream());
                employeeRepository.saveAll(employees);
            } catch (IOException e) {
                throw new IllegalArgumentException("File is not a valid excel file");
            }
        }
    }
}
