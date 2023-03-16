package com.sparta.mg.jpaproject.controllers.webControllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class WebWelcomeController {

    // The welcome page
    @GetMapping("/")
    public String welcome() {
        return "../static/welcome" ;
    }

    // Department Page
    @GetMapping("/departmentPage")
    public String departmentPage() {
        return "departments-page";
    }

    // Department Employee Page
    @GetMapping("/departmentEmployee")
    public String departmentEmployeePage() {
        return "dept_emp-page";
    }

    // Department Manager Page
    @GetMapping("/departmentManager")
    public String departmentManagerPage() {
        return "dept_manager-page";
    }

    // Employees Page
    @GetMapping("/employees")
    public String employeesPage() {
        return "employees-page";
    }

    // Salaries Page
    @GetMapping("/salaries")
    public String salariesPage() {
        return "salaries-page";
    }

    // Titles Page
    @GetMapping("/titles")
    public String titlesPage() {
        return "titles-page";
    }
}
