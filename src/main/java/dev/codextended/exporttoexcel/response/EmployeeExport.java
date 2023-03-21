package dev.codextended.exporttoexcel.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
@Builder
@Data
public class EmployeeExport {
    private String firstname;
    private String lastname;
    private String entryDate;
    private String function;
    private Double actualSalary;
    private Double newSalary;
    private String category;
    private int monthOfService;
    private int percentage;
}
