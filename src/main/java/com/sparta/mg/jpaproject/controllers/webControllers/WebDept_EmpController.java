package com.sparta.mg.jpaproject.controllers.webControllers;

import com.sparta.mg.jpaproject.model.entities.*;
import com.sparta.mg.jpaproject.model.repositories.DepartmentRepository;
import com.sparta.mg.jpaproject.model.repositories.DeptEmpRepository;
import com.sparta.mg.jpaproject.model.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Controller
public class WebDept_EmpController {
    // Faris

    private DeptEmpRepository deptEmpRepository;
    private EmployeeRepository employeeRepository;
    private Integer empNo;
    private DepartmentRepository departmentRepository;

    @Autowired
    public WebDept_EmpController(DeptEmpRepository deptEmpRepository, EmployeeRepository employeeRepository,
                                 DepartmentRepository departmentRepository){
        this.deptEmpRepository =deptEmpRepository;
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }


    @PostMapping("deptemp/{emp}")
    public String setEmployeeDept( Model model,
                                   @PathVariable Integer emp,
                                   @RequestParam("deptNo") String deptNo,
                                   @RequestParam("fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromDate,
                                   @RequestParam("toDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate toDate) {

//            Optional<Employee> emp1 = employeeRepository.findById(empId);
//            if (emp1.isPresent() && departmentRepository.findById(deptNo).isPresent()) {
//
//                Department dept = departmentRepository.findById(deptNo).get();
//                DeptEmp deptEmp = new DeptEmp();
//                deptEmp.setDeptNo(dept);
//                deptEmp.setToDate(toDate);
//                deptEmp.setFromDate(fromDate);
//
//                var deptEmpId = new DeptEmpId();
//                deptEmpId.setDeptNo(dept.getDeptName());
//                deptEmpRepository.save(deptEmp);
//
//                return ResponseEntity.ok("Department updated for Employee " + empId);
//            } else {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found");
//            }
        //deptEmpRepository.saveAndFlush(2);
        model.addAttribute(emp);
        model.addAttribute(deptNo);
        model.addAttribute(fromDate);
        model.addAttribute(toDate);
        return "dept_emp-page";
    }

    @PreAuthorize("hasRole('ROLE_BASIC')")
    @GetMapping("/deptEmp/{deptNo}")
    public String getAllEmployeesOfDept(Model model, @PathVariable String deptNo) {
        Department dept = departmentRepository.findById(deptNo.toString()).orElse(null);
        model.addAttribute("deptEmps", deptEmpRepository.getEmployeesByDeptNo(deptNo));
        return "DepartmentEmployeePages/emp_by_dept-page";
    }

    @PreAuthorize("hasRole('ROLE_BASIC')")
    @GetMapping("/deptEmp/{empNo}")
    public String getDepartmentsByEmpNo(Model model, @PathVariable Integer empNo) {
//        List<Department> deptEmp = deptEmpRepository.allDepartmentsOfEmployee(empNo);
//        Optional<DeptEmp> emp1 = deptEmpRepository.findById(deptEmpId);
//        Department dept = departmentRepository.findById(String.valueOf(empNo)).orElse(null);
        model.addAttribute("departments", deptEmpRepository.allDepartmentsOfEmployee(empNo));
        return "DepartmentEmployeePages/depts_of_emp-page";
    }

    @PreAuthorize("hasRole('ROLE_UPDATE')")
    @PatchMapping("deptemp/{empId}")
    public String updateEmployeeDept( Model model,
                                      @PathVariable Integer empId,
                                      @RequestParam("deptNo") String deptNo,
                                      @RequestParam("fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromDate,
                                      @RequestParam("toDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate toDate) {
        DeptEmp deptEmp= new DeptEmp();
        deptEmp.setDeptNo(departmentRepository.findById(deptNo).get());
        deptEmp.setEmpNo(employeeRepository.findEmployeeById(empId).get());
        deptEmp.setFromDate(fromDate);
        deptEmp.setToDate(toDate);

        model.addAttribute("updatedDeptEmp", deptEmp);
        return "DepartmentEmployeePages/deptEmp-edit-form";
    }

    @PreAuthorize("hasRole('ROLE_UPDATE')")
    @PostMapping("update/deptEmp")
    public String saveDeptEmp(DeptEmp updatedDeptEmp, RedirectAttributes redirectAttributes) {
        deptEmpRepository.saveAndFlush(updatedDeptEmp);
        redirectAttributes.addFlashAttribute("status", "Employee's department successfully updated");
        return "redirect:/deptEmp/{empNo}?empNo=" + updatedDeptEmp.getId().getEmpNo();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/deptEmp/delete/{empNo}")
    public String deleteEmployeeDepartment(
            @PathVariable("empNo") Integer empNo,
            @RequestParam("deptNo") String deptNo,
            @RequestParam("fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromDate,
            @RequestParam("toDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate toDate,
            RedirectAttributes redirectAttributes) {
        DeptEmp deptEmp= new DeptEmp();
        deptEmp.setDeptNo(departmentRepository.findById(deptNo).get());
        deptEmp.setEmpNo(employeeRepository.findEmployeeById(empNo).get());
        deptEmp.setFromDate(fromDate);
        deptEmp.setToDate(toDate);
        deptEmpRepository.deleteById(deptEmp.getId());
        redirectAttributes.addFlashAttribute("status", "Employees Department successfully deleted");
        return "redirect:/employee/dept_Emp?emp_No=" + empNo;
    }
}
