package com.sparta.mg.jpaproject.controllers.employeecontrollers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.mg.jpaproject.model.entities.Employee;
import com.sparta.mg.jpaproject.model.repositories.EmployeeRepository;
import com.sparta.mg.jpaproject.services.ApiKeyService;
import com.sparta.mg.jpaproject.tools.CRUD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class EmployeeController {

    private final EmployeeRepository employeeRepository;
    private final ObjectMapper objectMapper;
    private final ApiKeyService apiKeyService;

    @Autowired
    public EmployeeController(EmployeeRepository employeeRepository, ObjectMapper objectMapper, ApiKeyService apiKeyService) {
        this.employeeRepository = employeeRepository;
        this.objectMapper = objectMapper;
        this.apiKeyService = apiKeyService;
    }

    @PostMapping(value = "/employee")
    public ResponseEntity<String> setEmployee(@RequestBody Employee employee,
                                              @RequestHeader("x-api-key") String apiKey) {

        if (!apiKeyService.validateUser(apiKey, CRUD.CREATE)) {
            return apiKeyService.getInvalidApiKeyResponse();
        }

        if (employeeRepository.findEmployeeById(employee.getId()).isEmpty()) {
            employeeRepository.save(employee);
            return new ResponseEntity<>(
                    "Employee " + employee + " added.",
                    new HttpHeaders(),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(
                    "Employee with ID " + employee.getId() + " already exists.",
                    new HttpHeaders(),
                    HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @GetMapping(value = "/employee/{id}")
    public ResponseEntity<String> getEmployeeById(@PathVariable Integer id,
                                                  @RequestHeader("x-api-key") String apiKey) {

        if (!apiKeyService.validateUser(apiKey, CRUD.READ)) {
            return apiKeyService.getInvalidApiKeyResponse();
        }

        if (employeeRepository.findEmployeeById(id).isEmpty()) {
            return new ResponseEntity<>(
                   "Employee with ID " + id + " doesn't exist.",
                   new HttpHeaders(),
                   HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(
                    "Employee found: " + employeeRepository.findEmployeeById(id),
                    new HttpHeaders(),
                    HttpStatus.OK);
        }
    }

    @PatchMapping(value = "/employee")
    public ResponseEntity<String> updateEmployee(@RequestBody Employee employee,
                                                 @RequestHeader("x-api-key") String apiKey) {

        if (!apiKeyService.validateUser(apiKey, CRUD.UPDATE)) {
            return apiKeyService.getInvalidApiKeyResponse();
        }

        if (employeeRepository.findEmployeeById(employee.getId()).isEmpty()) {
            return new ResponseEntity<>(
                    "Employee with ID " + employee.getId() + " doesn't exist.",
                    new HttpHeaders(),
                    HttpStatus.NOT_FOUND);
        } else {
            employeeRepository.save(employee);
            return new ResponseEntity<>(
                    "Employee with ID " + employee.getId() + " has been updated.",
                    new HttpHeaders(),
                    HttpStatus.OK);
        }
    }

    @DeleteMapping(value = "employee/{id}")
    public ResponseEntity<String> deleteEmployeeById(@PathVariable Integer id,
                                                     @RequestHeader("x-api-key") String apiKey) {

        if (!apiKeyService.validateUser(apiKey, CRUD.DELETE)) {
            return apiKeyService.getInvalidApiKeyResponse();
        }

        if (employeeRepository.findEmployeeById(id).isPresent()) {
            employeeRepository.delete(employeeRepository.findEmployeeById(id).get());
            return new ResponseEntity<>(
                    "Employee with ID " + id + " has been deleted.",
                    new HttpHeaders(),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(
                    "Employee with ID " + id + " doesn't exist.",
                    new HttpHeaders(),
                    HttpStatus.NOT_FOUND);
        }
    }
}
