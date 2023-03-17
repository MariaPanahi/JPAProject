package com.sparta.mg.jpaproject.controllers.webControllers;

import com.sparta.mg.jpaproject.model.entities.Employee;
import com.sparta.mg.jpaproject.model.entities.Salary;
import com.sparta.mg.jpaproject.model.entities.SalaryId;
import com.sparta.mg.jpaproject.model.repositories.EmployeeRepository;
import com.sparta.mg.jpaproject.model.repositories.SalaryRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Optional;

@Controller
public class WebEmployeesController {
    private final EmployeeRepository employeeRepository;

    public WebEmployeesController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @PreAuthorize("hasRole('ROLE_UPDATE')")
    @GetMapping("/employee/create")
    public String getCreateEmployeeForm(Model model) {
        model.addAttribute("employeeToCreate", new Employee());
        return "EmployeePages/create-employee-form";
    }

    @PreAuthorize("hasRole('ROLE_UPDATE')")
    @PostMapping("/employee/save")
    public String save(@ModelAttribute("employeeToCreate") Employee employeeToCreate,
                       Model model,
                       RedirectAttributes redirectAttributes) {
        employeeRepository.save(employeeToCreate);
        redirectAttributes.addFlashAttribute("status", "Employee successfully created");
        return "redirect:/employee?empNo=" + employeeToCreate.getId();
    }

    @PreAuthorize("hasRole('ROLE_BASIC')")
    @GetMapping("/employee/search")
    public String getEmployeeSearchPage() {
        return "EmployeePages/search-employee-page";
    }

    @PreAuthorize("hasRole('ROLE_BASIC')")
    @GetMapping("/employee")
    public String getEmployee(Model model, @RequestParam Integer empNo) {
        Optional<Employee> emp1 = employeeRepository.findById(empNo);
        if (emp1.isPresent()) {
            model.addAttribute("employee", emp1.get());
        }
        return "EmployeePages/employee";
    }

    @PreAuthorize("hasRole('ROLE_UPDATE')")
    @GetMapping("/employee/edit")
    public String getEditSalary(@RequestParam("emp_No") Integer emp_No, Model model){
        Employee emp = employeeRepository.findById(emp_No).orElse(null);
        model.addAttribute("empToEdit", emp);
        return "EmployeePages/employee-edit-form";
    }

    @PreAuthorize("hasRole('ROLE_UPDATE')")
    @PostMapping("/update/employee")
    public String updateSalary(Employee empToEdit, RedirectAttributes redirectAttributes){
        employeeRepository.saveAndFlush(empToEdit);
        redirectAttributes.addFlashAttribute("status", "Employee successfully updated");
        return "redirect:/employee?empNo=" + empToEdit.getId();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/employee/delete")
    public String deleteSalary(
            @RequestParam("emp_No") Integer emp_No,
            RedirectAttributes redirectAttributes) {
        employeeRepository.deleteById(emp_No);
        redirectAttributes.addFlashAttribute("status", "Employee successfully deleted");
        return "redirect:/employees";
    }

}
