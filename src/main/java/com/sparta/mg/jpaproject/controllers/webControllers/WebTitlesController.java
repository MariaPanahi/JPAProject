package com.sparta.mg.jpaproject.controllers.webControllers;

import com.sparta.mg.jpaproject.model.entities.*;
import com.sparta.mg.jpaproject.model.repositories.EmployeeRepository;
import com.sparta.mg.jpaproject.model.repositories.TitleRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Optional;

@Controller
public class WebTitlesController {

    private final TitleRepository titleRepository;
    private final EmployeeRepository employeeRepository;

    public WebTitlesController(TitleRepository titleRepository, EmployeeRepository employeeRepository) {
        this.titleRepository = titleRepository;
        this.employeeRepository = employeeRepository;
    }



    //Create
    @PreAuthorize("hasRole('ROLE_UPDATE')")
    @GetMapping("/createTitle")
    public String createNewTitle(Model model) {

        Title title = new Title();

        model.addAttribute("titleToCreate", title);
        return "TitlesPages/create-job-title";
    }

    @PreAuthorize("hasRole('ROLE_UPDATE')")
    @PostMapping("/title/save")
    public String save(@ModelAttribute("titleToCreate") Title titleToCreate,
                       @RequestParam Integer empNo,
                       Model model,
                       RedirectAttributes redirectAttributes) {

        titleRepository.save(titleToCreate);
        Optional<Employee> employee = employeeRepository.findById(titleToCreate.getEmpNo().getId());
        model.addAttribute("employee", employee);
        redirectAttributes.addFlashAttribute("status", "New Title Created");
//        return "redirect:/employee/titles?empNo=" + employee.get().getId();
        return "titles-page";
    }

    // Read

    @PreAuthorize("hasRole('ROLE_BASIC')")
    @GetMapping("/title/search")
    public String getTitleSearchPage() {
        return "TitlesPages/searchTitlesPage";
    }


    @PreAuthorize("hasRole('ROLE_BASIC')")
    @GetMapping("/viewTitles")
    public String viewTitles(Model model, @RequestParam Integer empNo) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(empNo);
        if(optionalEmployee.isPresent()) {
            model.addAttribute("titlesToView", titleRepository.findTitleByEmpNo(optionalEmployee.get()));
            model.addAttribute("employeeNo", optionalEmployee.get().getId());
        }
        return "TitlesPages/view-job-titles";
    }

    // Update

    @PreAuthorize("hasRole('ROLE_UPDATE')")
    @PostMapping("/title/edit")
    public String getEditTitle(@RequestParam("empNo") Integer empNo,
                               @RequestParam("title") String title,
                               @RequestParam("fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromDate,
                               Model model) {

        TitleId titleId = new TitleId();
        titleId.setEmpNo(empNo);
        titleId.setTitle(title);
        titleId.setFromDate(fromDate);
        Title title1 = titleRepository.findById(titleId).orElse(null);
        model.addAttribute("titleToEdit", title1);
        return "TitlesPages/update-job-title";
    }

    @PreAuthorize("hasRole('ROLE_UPDATE')")
    @PostMapping("/title/update")
    public String updateTitle(Title titleToEdit) {
        titleRepository.saveAndFlush(titleToEdit);
        return "titles-page";
    }


    // Delete
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping ("/title/delete")
    public String deleteTitle(@RequestParam("empNo") Integer empNo,
                              @RequestParam("title") String title,
                              @RequestParam("fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromDate ) {

        TitleId titleId = new TitleId();
        titleId.setEmpNo(Integer.valueOf(empNo));
        titleId.setTitle(title);
        titleId.setFromDate(fromDate);
        titleRepository.deleteById(titleId);
        return "titles-page";
    }

}
