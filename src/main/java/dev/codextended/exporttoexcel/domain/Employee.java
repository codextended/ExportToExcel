package dev.codextended.exporttoexcel.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    @Id
    @GeneratedValue
    private UUID id;
    private String firstname;
    private String lastname;
    private LocalDate entryDate;
    private String function;
    private String  category;
    private BigDecimal actualSalary;
}
