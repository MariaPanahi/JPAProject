package com.sparta.mg.jpaproject.controllers;

import com.sparta.mg.jpaproject.model.entities.Employee;
import com.sparta.mg.jpaproject.model.entities.Salary;
import com.sparta.mg.jpaproject.model.entities.SalaryId;
import com.sparta.mg.jpaproject.model.repositories.EmployeeRepository;
import com.sparta.mg.jpaproject.model.repositories.SalaryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class SalaryController {
    // Ali

    private SalaryRepository salaryRepository;
    private EmployeeRepository employeeRepository;

    private static final Logger logger = LoggerFactory.getLogger(SalaryController.class);

    @Autowired
    public SalaryController(SalaryRepository salaryRepository, EmployeeRepository employeeRepository) {
        this.salaryRepository = salaryRepository;
        this.employeeRepository = employeeRepository;
    }

    //Create Method
    @PostMapping("/employee/salary/{empNo}")
    public ResponseEntity<String> createSalary(@PathVariable Integer empNo, @RequestParam("salary") int salary, @RequestParam("fromDate") @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate fromDate, @RequestParam("toDate") @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate toDate) {
        Optional<Employee> emp1 = employeeRepository.findById(empNo);
        if (emp1.isPresent()) {
            Salary newSalary = new Salary();
            newSalary.setSalary(salary);
            newSalary.setToDate(toDate);
            SalaryId salaryId = new SalaryId();
            salaryId.setEmpNo(empNo);
            salaryId.setFromDate(fromDate);
            newSalary.setId(salaryId);
            newSalary.setEmpNo(emp1.get());
            salaryRepository.save(newSalary);
            return ResponseEntity.ok("Salary created for employee " + empNo);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found");
        }
    }

    //Read Method
    @GetMapping("/employee/salary/{empNo}")
    public ResponseEntity<String> getSalaryByEmpNo(@PathVariable Integer empNo) {
        Optional<Employee> emp1 = employeeRepository.findById(empNo);
        if (emp1.isPresent()) {
            List<Salary> salaries = salaryRepository.findSalariesByEmpNo(emp1.get());
            List<Integer> salaryValues = new ArrayList<>();
            for (Salary salary : salaries) {
                salaryValues.add(salary.getSalary());
            }
            return ResponseEntity.ok(salaryValues.toString());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found");
        }
    }

        //Update Method
    @PatchMapping("/employee/salary/{empNo}/{fromDate}")
    public ResponseEntity<String> updateSalary(@PathVariable ("empNo") int empNo,@PathVariable ("fromDate") @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate fromDate, @RequestParam int salary, @RequestParam("toDate") @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate toDate) {
        Optional<Salary> existingSalary = salaryRepository.findSalariesByEmpNoAndFromDate(empNo,fromDate);
        if (existingSalary.isPresent()) {
            Salary updatedSalary = existingSalary.get();
            updatedSalary.setSalary(salary);
            updatedSalary.setToDate(toDate);
            salaryRepository.save(updatedSalary);
            return new ResponseEntity<>("The salary for emp_no " + empNo + " has been updated", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("The salary for emp_no " + empNo + " does not exist", HttpStatus.NOT_FOUND);
        }
    }


    //Delete Method
    @DeleteMapping("/employee/salaries/{empNo}")
    public ResponseEntity<String> deleteSalary(@PathVariable("empNo") int empNo) {
        Optional<Employee> emp1 = employeeRepository.findById(empNo);
        List<Salary> salaries = salaryRepository.findSalariesByEmpNo(emp1.get());
        if (emp1.isPresent()) {
            for (Salary salary : salaries) {
                salaryRepository.delete(salary);
            }
            String message = "All salaries for employee " + empNo + " has been removed";
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("Employee not found", HttpStatus.NOT_FOUND);
        }
    }
}
