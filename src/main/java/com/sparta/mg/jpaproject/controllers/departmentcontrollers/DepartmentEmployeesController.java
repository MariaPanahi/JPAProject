package com.sparta.mg.jpaproject.controllers.departmentcontrollers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.mg.jpaproject.model.entities.Department;
import com.sparta.mg.jpaproject.model.entities.Employee;
import com.sparta.mg.jpaproject.model.repositories.DepartmentRepository;
import com.sparta.mg.jpaproject.model.repositories.DeptEmpRepository;
import com.sparta.mg.jpaproject.services.ApiKeyService;
import com.sparta.mg.jpaproject.tools.CRUD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class DepartmentEmployeesController {
    //Samir

    private final ApiKeyService apiKeyService;

    private final DeptEmpRepository deptEmpRepository;

    private final DepartmentRepository departmentRepository;

    private final ObjectMapper mapper;

    @Autowired
    public DepartmentEmployeesController(ApiKeyService apiKeyService, DeptEmpRepository deptEmpRepository, DepartmentRepository departmentRepository, ObjectMapper objectMapper) {
        this.apiKeyService = apiKeyService;
        this.deptEmpRepository = deptEmpRepository;
        this.departmentRepository = departmentRepository;
        this.mapper = objectMapper;
    }

    //    Read GET /department/{id}/employees
//
//    Given I want to know all employees from a department,
//    When I sent the correct http request given the department id,
//    Then I will receive a list of employees and status code 2xx.
    @GetMapping("/department/{deptId}/employees/{pageNumber}/{size}")
    public ResponseEntity<String> getAllEmployeeOfGivenDeptId(@PathVariable String deptId,
                                                              @PathVariable Integer pageNumber,
                                                              @PathVariable Integer size,
                                                              @RequestHeader("x-api-key") String apiKey) {
        if (!apiKeyService.validateUser(apiKey, CRUD.READ)) {
            return apiKeyService.getInvalidApiKeyResponse();
        }

        Optional<Department> department = departmentRepository.findById(deptId);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("content-type", "application/json");

        if (department.isPresent()) {

            Pageable page = PageRequest.of(pageNumber, size);

            try {
                Page<Employee> pages = deptEmpRepository.getEmployeesByDeptNo(deptId, page);
                System.out.println(pages.toList());

                ResponseEntity<String> response = new ResponseEntity<>(
                        mapper.writeValueAsString(pages.toList()),
                        httpHeaders,
                        HttpStatus.OK
                );

                return response;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ResponseEntity<String> noDepartmentsExistResponse = new ResponseEntity<>(
                "{\"message\":\"The department with id " + deptId + " could not be found\"}",
                httpHeaders,
                HttpStatus.NOT_FOUND // ToDo: Need find the correct HttpStatus response
        );
        return noDepartmentsExistResponse;
    }

}
