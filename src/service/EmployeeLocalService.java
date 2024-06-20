package service;

import database.MemoryDatabase;
import model.Employee;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

import static tools.StaticTools.dateFormater;

public class EmployeeLocalService {
    private final MemoryDatabase memoryDatabase = new MemoryDatabase();

    public List<Employee> getAllEmployees() {
        return memoryDatabase.getPersons();
    }

    public void deleteEmployeeByName(String name) {
        memoryDatabase.removePersonByName(name);
        System.out.println("Deleted employee with name " + name);
        System.out.println("\n\n\n");
    }

    public void increaseSalaryForAllEmployees(double percent) {
        memoryDatabase.increaseSalaryForAllEmployees(percent);
        System.out.println("Salary increased for: " + memoryDatabase.getPersons().size() + " employees.");
        System.out.println("\n\n\n");
    }

    public Map<String, List<Employee>> groupEmployeesByRole(List<Employee> employees) {
        return employees.stream()
                .collect(Collectors.groupingBy(Employee::getRole));
    }


    public List<Employee> filterByBirthDateRange(String birthDateStrInit, String birthDateStrEnd) {
        LocalDate birthDateStart = LocalDate.parse(birthDateStrInit, dateFormater);
        LocalDate birthDateEnd = LocalDate.parse(birthDateStrEnd, dateFormater);
        List<Employee> employees = getAllEmployees();
        return employees.stream()
                .filter(e -> {
                    LocalDate eBirthDate = e.getBirthDate();
                    return !eBirthDate.isBefore(birthDateStart) && !eBirthDate.isAfter(birthDateEnd);
                })
                .collect(Collectors.toList());
    }

    public Employee getSeniorEmployee() {
        List<Employee> employees = getAllEmployees();
        return employees.stream()
                .min(Comparator.comparing(Employee::getBirthDate))
                .orElse(null);
    }

    public int calculateSeniorAge() {
        Employee employee = this.getSeniorEmployee();
        if (employee == null) {
            throw new RuntimeException("Senior employee not found");
        }
        Period period = Period.between(employee.getBirthDate(), LocalDate.now());
        return period.getYears();
    }

    public List<Employee> sortEmployeesInAlphabeticOrder() {
        List<Employee> employees = this.getAllEmployees();
        employees.sort(Comparator.comparing(Employee::getName));
        return employees;
    }

    public Map<String, BigDecimal> calculateMinimumSalaries() {
        List<Employee> employees = this.getAllEmployees();
        Map<String, BigDecimal> minimumSalaries = new HashMap<>();

        BigDecimal minimumWage = new BigDecimal("1212.00");

        for (Employee employee : employees) {
            BigDecimal salary = employee.getSalary();
            BigDecimal numberOfMinimumSalaries = salary.divide(minimumWage, RoundingMode.CEILING);
            minimumSalaries.put(employee.getName(), numberOfMinimumSalaries);
        }
        return minimumSalaries;
    }

    public BigDecimal calculateTotalSalary() {
        List<Employee> employees = this.getAllEmployees();
        BigDecimal totalSalary = BigDecimal.ZERO;
        for (Employee employee : employees) {
            totalSalary = totalSalary.add(employee.getSalary());
        }
        return totalSalary;
    }


    
}
