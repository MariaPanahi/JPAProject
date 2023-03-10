package com.sparta.mg.jpaproject.controllers;

import com.sparta.mg.jpaproject.model.entities.Employee;
import com.sparta.mg.jpaproject.model.entities.Salary;
import com.sparta.mg.jpaproject.model.entities.SalaryId;
import com.sparta.mg.jpaproject.model.repositories.EmployeeRepository;
import com.sparta.mg.jpaproject.model.repositories.SalaryRepository;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SalaryControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Mock
    private SalaryRepository salaryRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private SalaryController salaryController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(salaryController).build();
    }

    @Test
    public void testCreateSalary() throws Exception {
        // Mocking the employeeRepository to return a mocked Employee object
        Employee employee = mock(Employee.class);
        when(employeeRepository.findById(1)).thenReturn(Optional.of(employee));

        // Performing a POST request to the createSalary endpoint
        mockMvc.perform(MockMvcRequestBuilders.post("/employee/salary/1")
                        .param("salary", "50000")
                        .param("fromDate", "2022-01-01")
                        .param("toDate", "2022-12-31"))
                .andExpect(status().isOk())
                .andExpect(content().string("Salary created for employee 1"));
    }

    @Test
    @DisplayName("Get all salaries for an employee")
    void getSalaryByEmpNoTest() throws Exception {
        Employee employee = new Employee();
        employee.setId(1);
        employee.setFirstName("John");
        employee.setLastName("Doe");

        Salary salary1 = new Salary();
        salary1.setSalary(50000);
        salary1.setToDate(LocalDate.of(2023, 12, 31));
        salary1.setEmpNo(employee);

        Salary salary2 = new Salary();
        salary2.setSalary(55000);
        salary2.setToDate(LocalDate.of(2022, 12, 31));
        salary2.setEmpNo(employee);

        List<Salary> salaries = new ArrayList<>();
        salaries.add(salary1);
        salaries.add(salary2);

        when(employeeRepository.findById(1)).thenReturn(Optional.of(employee));
        when(salaryRepository.findSalariesByEmpNo(employee)).thenReturn(salaries);

        mockMvc.perform(get("/employee/salary/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("[50000, 55000]"));
    }

    @Test
    @DisplayName("Delete all salaries for an employee")
    void deleteSalaryByEmpNoTest() throws Exception {
        Employee employee = new Employee();
        employee.setId(1);
        employee.setFirstName("John");
        employee.setLastName("Doe");

        Salary salary1 = new Salary();
        salary1.setSalary(50000);
        salary1.setToDate(LocalDate.of(2023, 12, 31));
        salary1.setEmpNo(employee);

        Salary salary2 = new Salary();
        salary2.setSalary(55000);
        salary2.setToDate(LocalDate.of(2022, 12, 31));
        salary2.setEmpNo(employee);

        List<Salary> salaries = new ArrayList<>();
        salaries.add(salary1);
        salaries.add(salary2);

        when(employeeRepository.findById(1)).thenReturn(Optional.of(employee));
        when(salaryRepository.findSalariesByEmpNo(employee)).thenReturn(salaries);

        mockMvc.perform(delete("/employee/salaries/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("All salaries for employee 1 has been removed"));
    }

    @Test
    @DisplayName("Update salary")
    void updateSalaryTest() throws Exception {
        Salary salary = new Salary();
        salary.setSalary(50000);
        salary.setToDate(LocalDate.of(2022, 12, 31));
        Employee employee = new Employee();
        employee.setId(1);
        SalaryId salaryId = new SalaryId();
        salaryId.setFromDate(LocalDate.of(2022, 1, 1));
        salaryId.setEmpNo(employee.getId());
        salary.setId(salaryId);
        salary.setEmpNo(employee);

        when(salaryRepository.findSalariesByEmpNoAndFromDate(1, LocalDate.of(2022, 1, 1))).thenReturn(Optional.of(salary));

        mockMvc.perform(MockMvcRequestBuilders.patch("/employee/salary/1/2022-01-01")
                        .param("salary", "55000")
                        .param("toDate", "2023-12-31"))
                .andExpect(status().isOk())
                .andExpect(content().string("The salary for emp_no 1 has been updated"));

    }
}

//verify(salaryRepository, times(1)).findSalariesByEmpNoAndFromDate(1, LocalDate.of(2022, 1, 1));
////        verify(salaryRepository, times(1)).save(salary);
////        assertEquals(55000, salary.getSalary());
////        assertEquals(LocalDate.of(2023, 12, 31), salary.getToDate());
//

