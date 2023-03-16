package com.sparta.mg.jpaproject.model.repositories;

import com.sparta.mg.jpaproject.model.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    @Query(value = "SELECT * from employees where last_name = :lastName", nativeQuery = true)
    List<Employee> findEmployeeGivenLastName (String lastName);

    @Query( nativeQuery = true,
            value = "Select DISTINCT employees.* from employees JOIN salaries ON employees.emp_no = salaries.emp_no  WHERE salaries.salary > :salary")
    List<Employee> getEmployeesWithSalaryAbove(double salary);

    @Query(value = "SELECT Count(*) FROM employees.employees WHERE gender = 'M'", nativeQuery = true)
    int findAmountOfMales();

    @Query(value = "SELECT Count(*) FROM employees.employees WHERE gender = 'F'", nativeQuery = true)
    int findAmountOfFemales ();

    Optional<Employee> findEmployeeById(Integer id);
}