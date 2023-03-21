package dev.codextended.exporttoexcel.repository;

import dev.codextended.exporttoexcel.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
}
