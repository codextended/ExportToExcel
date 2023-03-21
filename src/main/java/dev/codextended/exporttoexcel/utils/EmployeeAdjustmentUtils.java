package dev.codextended.exporttoexcel.utils;

import dev.codextended.exporttoexcel.domain.Employee;
import dev.codextended.exporttoexcel.response.EmployeeExport;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class EmployeeAdjustmentUtils {

    public static List<EmployeeExport> employeesToExport(List<Employee> employees) {
        List<EmployeeExport> employeeExportList = new ArrayList<>();

        for (Employee employee : employees) {
            EmployeeExport employeeExport = EmployeeExport.builder()
                    .firstname(employee.getFirstname())
                    .lastname(employee.getLastname())
                    .entryDate(employee.getEntryDate().toString())
                    .function(employee.getFunction())
                    .category(employee.getCategory())
                    .actualSalary(employee.getActualSalary().doubleValue())
                    .newSalary(calculateNewSalary(employee.getActualSalary(), employee.getEntryDate(), employee.getCategory()).doubleValue())
                    .monthOfService(calculateMonthsOfService(employee.getEntryDate()))
                    .percentage(calculateMosPercentage(employee.getEntryDate(), employee.getCategory()))
                    .build();
            employeeExportList.add(employeeExport);
        }

        return employeeExportList;
    }

    private static BigDecimal calculateNewSalary(BigDecimal actualSalary, LocalDate entryDate, String name) {
        int percentage = calculateMosPercentage(entryDate, name);
        BigDecimal newSalary = actualSalary.multiply(BigDecimal.valueOf((double)percentage/100 + 1));

        return newSalary;
    }

    private static int calculateMosPercentage(LocalDate entryDate, String name) {
        int monthsOfService = calculateMonthsOfService(entryDate);
        int finalPercentage = 0;

        switch (name) {
            case "B" -> {
                if (monthsOfService < 61) {
                    finalPercentage = 5;
                } else if (monthsOfService < 121) {
                    finalPercentage = 6;
                } else {
                    finalPercentage = 7;
                }
            }
            case "C", "D", "E" -> {
                if (monthsOfService < 61) {
                    finalPercentage = 10;
                } else if (monthsOfService < 121) {
                    finalPercentage = 12;
                } else {
                    finalPercentage = 15;
                }
            }
            case "F", "A" -> {
                if (monthsOfService < 61) {
                    finalPercentage = 3;
                } else if (monthsOfService < 121) {
                    finalPercentage = 4;
                } else {
                    finalPercentage = 5;
                }
            }
            case "H" -> {
                finalPercentage = 4;
            }
        }
        return finalPercentage;
    }

    private static int calculateMonthsOfService(LocalDate entryDate) {

        long monthsBetween = ChronoUnit.MONTHS.between(
                entryDate.withDayOfMonth(1),
                LocalDate.now().withDayOfMonth(1)
        );
        return (int) monthsBetween;
    }
}
