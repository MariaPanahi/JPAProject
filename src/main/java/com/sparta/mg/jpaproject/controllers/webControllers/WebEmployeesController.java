package com.sparta.mg.jpaproject.controllers.webControllers;

import com.sparta.mg.jpaproject.model.entities.*;
import com.sparta.mg.jpaproject.model.repositories.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@SessionAttributes({"employeeToCreateExtra", "deptId",
        "titleName", "titleInput", "salaryInput", "titleValue"})

public class WebEmployeesController {
    private final EmployeeRepository employeeRepository;

    private final DepartmentRepository departmentRepository;

    private final TitleRepository titleRepository;

    private final SalaryRepository salaryRepository;

    private final DeptEmpRepository deptEmpRepository;

    public WebEmployeesController(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository, TitleRepository titleRepository, SalaryRepository salaryRepository, DeptEmpRepository deptEmpRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.titleRepository = titleRepository;
        this.salaryRepository = salaryRepository;
        this.deptEmpRepository = deptEmpRepository;
    }

    @ModelAttribute("employeeToCreateExtra")
    public Employee employeeToCreateExtra() {
        return new Employee();
    }

    @ModelAttribute("deptId")
    public String deptId() {
        return "deptId value";
    }

    @ModelAttribute("titleName")
    public String titleName() {
        return "titleName value";
    }

    @ModelAttribute("titleInput")
    public String titleInput() {
        return "titleInput value";
    }

    @ModelAttribute("salaryInput")
    public Integer salaryInput() {
        return 0;
    }

    @ModelAttribute("titleValue")
    public String titleValue() {
        return "titleValue value";
    }

    private Title setTitle(Employee employee, String titleName) {
        Title title1 = new Title();
        TitleId titleId = new TitleId();
        titleId.setTitle(titleName);
        titleId.setEmpNo(employee.getId());
        titleId.setFromDate(LocalDate.now());
        title1.setToDate(LocalDate.of(9999, 01, 01));
        title1.setEmpNo(employee);
        title1.setId(titleId);
        return title1;
    }

    private Salary setSalary(Employee employee, Integer salary) {
        Salary salary1 = new Salary();
        SalaryId salaryId = new SalaryId();
        salaryId.setEmpNo(employee.getId());
        salaryId.setFromDate(LocalDate.now());
        salary1.setId(salaryId);
        salary1.setSalary(salary);
        salary1.setToDate(LocalDate.of(9999, 01,01));
        salary1.setEmpNo(employee);
        return salary1;
    }

    private DeptEmp setDeptEmp(Employee employee, String deptId) {
        DeptEmp deptEmp = new DeptEmp();
        DeptEmpId deptEmpId = new DeptEmpId();
        deptEmpId.setDeptNo(deptId);
        deptEmpId.setEmpNo(employee.getId());
        deptEmp.setId(deptEmpId);
        deptEmp.setDeptNo(departmentRepository.findById(deptId).get());
        deptEmp.setFromDate(LocalDate.now());
        deptEmp.setToDate(LocalDate.of(9999, 01, 01));
        deptEmp.setEmpNo(employee);
        return deptEmp;
    }

    @PreAuthorize("hasRole('ROLE_UPDATE')")
    @GetMapping("/employee/create")
    public String getCreateEmployeeForm(Model model) {
        model.addAttribute("employeeToCreate", new Employee());
        return "EmployeePages/create-employee-form";
    }

    @PreAuthorize("hasRole('ROLE_UPDATE')")
    @GetMapping("/employee/createExtra")
    public String getCreateExtraEmployeeForm(Model model) {
        model.addAttribute("employeeToCreate", new Employee());
        model.addAttribute("allDepartments", departmentRepository.findAll());
        List<String> allTitles = titleRepository.getAllTitles();
        allTitles.add("Other");
        model.addAttribute("allTitles",allTitles);
        return "EmployeePages/create-employee-extra-form";
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

    @PreAuthorize("hasRole('ROLE_UPDATE')")
    @GetMapping("/employee/saveExtra")
    public String saveExtra(@ModelAttribute("employeeToCreateExtra") Employee employeeToCreate,
                       String deptId,
                            String titleName,
                            String titleInput,
                            Integer salaryInput,
                            Model model) {
        System.out.println(employeeToCreate);
        model.addAttribute("deptId", deptId);
        model.addAttribute("salaryInput", salaryInput);
        System.out.println(deptId);
        System.out.println("SHOW ME titleName: " + titleName);
        System.out.println("SHOW ME titleInput: " + titleInput);
        System.out.println(salaryInput);
        if (titleName.equals("Other")) {
            model.addAttribute("titleValue", titleInput);
        } else {
            model.addAttribute("titleValue", titleName);
        }

        model.addAttribute("deptName", departmentRepository.findById(deptId).get().getDeptName());
        return "/EmployeePages/create-employee-extra-confirmation";
    }


    @PreAuthorize("hasRole('ROLE_UPDATE')")
    @PostMapping("/employee/confirmExtra")
    public String saveExtras(RedirectAttributes redirectAttributes, Model model) {
        Employee employee = (Employee) model.getAttribute("employeeToCreateExtra");
        String deptId = (String) model.getAttribute("deptId");
        String title = (String) model.getAttribute("titleValue");
        Integer salary = (Integer) model.getAttribute("salaryInput");
        employeeRepository.save(employee);
        System.out.println(deptId);
        System.out.println(title);
        System.out.println(salary);
        System.out.println(employee);
        Salary salary1 = setSalary(employee, salary);
        salaryRepository.save(salary1);
        Title title1 = setTitle(employee, title);
        titleRepository.save(title1);
        DeptEmp deptEmp = setDeptEmp(employee, deptId);
        deptEmpRepository.save(deptEmp);
        redirectAttributes.addFlashAttribute("status", "Employee successfully created");
        return "redirect:/employee?empNo=" + employee.getId();
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
