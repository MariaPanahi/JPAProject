package com.sparta.mg.jpaproject.controllers.webControllers;

import com.sparta.mg.jpaproject.model.entities.Department;
import com.sparta.mg.jpaproject.model.repositories.DepartmentRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class WebDepartmentsController {
    private final DepartmentRepository departmentRepository;

    public WebDepartmentsController(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    // Create a new Department
    @GetMapping("/createDepartment")
    public String createDepartment(Model model) {
        Department department = new Department();
        model.addAttribute("departmentToCreate", department);
        return "departments/department-create-form";
    }

    // Receives Department object to create from a form.
    @PostMapping("/departmentReturn")
    public String departmentReturn(@ModelAttribute("departmentToCreate") Department department) {
        departmentRepository.save(department);
        return "departments/success";
    }

    // View all Departments
    @GetMapping("/allDepartments")
    public String getAllDepartments(Model model) {
        model.addAttribute("departments", departmentRepository.findAll());
        return "departments/all-departments";
    }

    // Requesting to edit a department
    @GetMapping("/department/edit/{id}")
    public String getDepartmentToEdit(@PathVariable String id, Model model) {
        Department department = departmentRepository.findById(id).orElse(null);
        model.addAttribute("departmentToEdit", department);
        return "departments/department-edit-form";
    }

    // Receives Department Object to edit from a form.
    @PostMapping("/updateDepartment")
    public String updateDepartment(@ModelAttribute("departmentToEdit") Department editedDepartment) {
        departmentRepository.saveAndFlush(editedDepartment);
        return "departments/success";
    }

    // Delete a department
    @PostMapping("/department/delete/{id}")
    public String deleteDepartment(@PathVariable String id) {
        departmentRepository.deleteById(id);
        return "departments/success";
    }


}
