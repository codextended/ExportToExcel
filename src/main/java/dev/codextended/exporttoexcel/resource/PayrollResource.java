package dev.codextended.exporttoexcel.resource;

import dev.codextended.exporttoexcel.domain.Employee;
import dev.codextended.exporttoexcel.service.PayrollService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/payroll")
@RequiredArgsConstructor
public class PayrollResource {

    private final PayrollService payrollService;

    @GetMapping(value = "/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("Hello from Payroll Backend");
    }

    @PostMapping(value = "")
    public ResponseEntity<Employee> addEmployee(@RequestBody Employee employee) {
        return ResponseEntity.ok(payrollService.addEmployee(employee));
    }

    @GetMapping(value = "")
    public ResponseEntity<List<Employee>> getEmployees() {
        return ResponseEntity.ok(payrollService.getEmployees());
    }

    @GetMapping(value = "/csv")
    public void getPayrollFile(HttpServletResponse response) throws Exception {
//
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=Ajustement Payroll.xlsx";
        response.setHeader(headerKey, headerValue);

        payrollService.getPayrollFile(response);

    }

    @PostMapping("/upload-employees")
    public ResponseEntity<?> uploadCustomersData(@RequestParam("file") MultipartFile file) {
        payrollService.saveEmployeesToDatabase(file);
        return ResponseEntity.ok(Map.of("Message", "Employees uploaded and saved to database"));
    }
}
