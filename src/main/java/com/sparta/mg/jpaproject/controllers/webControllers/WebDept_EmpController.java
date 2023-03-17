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

    private DeptEmpRepository deptEmpRepository;
    private EmployeeRepository employeeRepository;
    private Integer empNo;
    private DepartmentRepository departmentRepository;

    @Autowired
    public WebDept_EmpController(DeptEmpRepository deptEmpRepository, EmployeeRepository employeeRepository,
                                 DepartmentRepository departmentRepository) {
        this.deptEmpRepository = deptEmpRepository;
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }

    // Create
    @PreAuthorize("hasRole('ROLE_UPDATE')")
    @GetMapping("/deptEmp/create")
    public String createDeptEmp() {
        return "DepartmentEmployeePages/deptEmp-add-form";
    }

    @PreAuthorize("hasRole('ROLE_UPDATE')")
    @PostMapping("/createDeptEmp")
    public String createDeptEmp(@ModelAttribute("DeptEmpToCreate") DeptEmp addedDeptEmp,
                                @ModelAttribute("DeptEmpIdToCreate") DeptEmpId newDeptEmpId) {
        addedDeptEmp.setId(newDeptEmpId);
        deptEmpRepository.save(addedDeptEmp);
        return "dept_emp-page";
    }

<<<<<<< HEAD
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
=======
    // Read
>>>>>>> 28dd06dd4b6b4cdf23d32a93ab56998de8dfcc54

    // Find All
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/deptEmps")
    public String getAllDeptEmps(Model model) {
        List<DeptEmp> deptEmpList = deptEmpRepository.findAll().subList(0, 10);
        model.addAttribute("deptEmps", deptEmpList);
        return "DepartmentEmployeePages/allDeptEmps";
    }

    @PreAuthorize("hasRole('ROLE_BASIC')")
    @GetMapping("/deptEmp/find")
    public String findDeptEmp() {
        return "DepartmentEmployeePages/deptEmp-find-form";
    }

    @PreAuthorize("hasRole('ROLE_BASIC')")
    @PostMapping("/findDeptEmpById")
    public String findDeptEmp(@ModelAttribute("DeptEmpIdToCreate") DeptEmpId newDeptEmpId,
                              String deptNo, Integer empNo, Model model
    ) {
        DeptEmpId deptEmpId = new DeptEmpId();
        deptEmpId.setEmpNo(empNo);
        deptEmpId.setDeptNo(deptNo);
        model.addAttribute("deptEmp", deptEmpRepository.findById(deptEmpId).orElse(null));
        return "DepartmentEmployeePages/deptEmpById";
    }

    // Update
    @PreAuthorize("hasRole('ROLE_UPDATE')")
    @GetMapping("/deptEmp/edit/{empNo}/{deptNo}")
    public String getDeptEmpToEdit(@PathVariable Integer empNo,
                                   @PathVariable String deptNo,
                                   Model model) {
        DeptEmpId deptEmpId = new DeptEmpId();
        deptEmpId.setEmpNo(empNo);
        deptEmpId.setDeptNo(deptNo);
        DeptEmp deptEmp = deptEmpRepository.findById(deptEmpId).orElse(null);
        model.addAttribute("employeeToEdit", deptEmp);
        return "DepartmentEmployeePages/deptEmp-edits-form";
    }

    @PreAuthorize("hasRole('ROLE_UPDATE')")
    @PostMapping("/updateDeptEmp")
    public String updateEmployee(@ModelAttribute("DeptEmpToEdit") DeptEmp editedDeptEmp) {
        deptEmpRepository.saveAndFlush(editedDeptEmp);
        return "dept_emp-page";
    }

    //delete
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/deptEmp/delete/{empNo}/{deptNo}")
    public String deleteEmployee(@PathVariable Integer empNo, @PathVariable String deptNo) {
        DeptEmpId deptEmpId = new DeptEmpId();
        deptEmpId.setEmpNo(empNo);
        deptEmpId.setDeptNo(deptNo);
        deptEmpRepository.deleteById(deptEmpId);
        return "dept_emp-page";
    }

}
