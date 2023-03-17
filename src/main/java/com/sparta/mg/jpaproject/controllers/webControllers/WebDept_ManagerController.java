package com.sparta.mg.jpaproject.controllers.webControllers;

import com.sparta.mg.jpaproject.model.entities.Department;
import com.sparta.mg.jpaproject.model.entities.DeptManager;
import com.sparta.mg.jpaproject.model.entities.DeptManagerId;
import com.sparta.mg.jpaproject.model.repositories.DepartmentRepository;
import com.sparta.mg.jpaproject.model.repositories.DeptManagerRepository;
import com.sparta.mg.jpaproject.model.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Optional;

@Controller
@RequestMapping("/dept-manager")
public class WebDept_ManagerController {

    private final DeptManagerRepository deptManagerRepository;

    private final DepartmentRepository departmentRepository;

    private final EmployeeRepository employeeRepository;

    @Autowired
    public WebDept_ManagerController(DeptManagerRepository deptManagerRepository, DepartmentRepository departmentRepository, EmployeeRepository employeeRepository) {
        this.deptManagerRepository = deptManagerRepository;
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
    }
    @PreAuthorize("hasRole('ROLE_BASIC')")
    @GetMapping()
    public String deptManagerSearch(Model model) {
        model.addAttribute("allDepartments", departmentRepository.findAll());
        return "dept_manager_files/department_to_search";
    }

    //Create
    @PreAuthorize("hasRole('ROLE_UPDATE')")
    @GetMapping("/createForm")
    public String createDeptManagerForm(Model model, String deptId) {
        DeptManager deptManager = new DeptManager();
        deptManager.setDeptNo(departmentRepository.findById(deptId).get());
        DeptManagerId deptManagerId = new DeptManagerId();
        deptManagerId.setDeptNo(deptId);
        deptManager.setId(deptManagerId);
        model.addAttribute("deptManagerToCreate", deptManager);
        return "dept_manager_files/department-manager-create-form";
    }

    @PreAuthorize("hasRole('ROLE_UPDATE')")
    @PostMapping("/create")
    public String saveDeptManager(@ModelAttribute("deptManagerToCreate") DeptManager deptManager, Model model,
                                  RedirectAttributes redirectAttributes) {
        deptManager.setEmpNo(employeeRepository.findById(deptManager.getId().getEmpNo()).get());
        System.out.println(deptManager);
        System.out.println(deptManager.getDeptNo());
        System.out.println(deptManager.getEmpNo());
        System.out.println(deptManager.getId());
        deptManager.setDeptNo(departmentRepository.findById(deptManager.getId().getDeptNo()).get());
        deptManagerRepository.saveAndFlush(deptManager);
        redirectAttributes.addFlashAttribute("status", "Dept Manager successfully created");
        return "redirect:/dept-manager/getDeptManagers?deptId=" + deptManager.getId().getDeptNo();
    }

    //Read
    @PreAuthorize("hasRole('ROLE_BASIC')")
    @GetMapping("/getDeptManagers")
    public String getDepartmentManagersByDeptId(Model model,
                                     @RequestParam String deptId) {
        Optional<Department> dept1 = departmentRepository.findById(deptId);
        if (dept1.isPresent()) {
            model.addAttribute("deptManagers", deptManagerRepository.getDeptManagersById_DeptNo(deptId));
            model.addAttribute("deptId", dept1.get().getId());
        }
        return "dept_manager_files/departmentManagers";
    }

    //Update
    @PreAuthorize("hasRole('ROLE_UPDATE')")
    @GetMapping("/editDeptManager")
    public String updateDeptManager(Model model, @RequestParam String deptId, @RequestParam Integer emp_No) {
        DeptManagerId deptManagerId = new DeptManagerId();
        deptManagerId.setEmpNo(emp_No);
        deptManagerId.setDeptNo(deptId);
        DeptManager deptManager = deptManagerRepository.findById(deptManagerId).orElse(null);
        model.addAttribute("deptManagerToEdit", deptManager);
        return "dept_manager_files/department-manager-edit-form";
    }

    @PreAuthorize("hasRole('ROLE_UPDATE')")
    @PostMapping("/update")
    public String updateDeptManager(@ModelAttribute("deptManagerToEdit") DeptManager deptManager, @RequestParam() Integer empId,RedirectAttributes redirectAttributes){
        deptManager.setEmpNo(employeeRepository.findById(empId).get());
        deptManager.setDeptNo(departmentRepository.findById(deptManager.getId().getDeptNo()).get());
        System.out.println(deptManager);
        deptManagerRepository.updateDeptManagerWithSameDepartment(deptManager.getFromDate(), deptManager.getToDate(),
                deptManager.getEmpNo().getId(), deptManager.getDeptNo().getId(), deptManager.getId().getEmpNo());
        redirectAttributes.addFlashAttribute("status", "Salary successfully updated");
        return "redirect:/dept-manager/getDeptManagers?deptId=" + deptManager.getId().getDeptNo();
    }

    //Delete
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/delete")
    public String deleteDeptManager(Model model, @RequestParam String deptId, @RequestParam Integer emp_No,
            RedirectAttributes redirectAttributes) {
        DeptManagerId deptManagerId = new DeptManagerId();
        deptManagerId.setEmpNo(emp_No);
        deptManagerId.setDeptNo(deptId);
        deptManagerRepository.deleteById(deptManagerId);
        redirectAttributes.addFlashAttribute("status", "Salary successfully deleted");
        return "redirect:/dept-manager/getDeptManagers?deptId=" + deptManagerId.getDeptNo();
    }

}
