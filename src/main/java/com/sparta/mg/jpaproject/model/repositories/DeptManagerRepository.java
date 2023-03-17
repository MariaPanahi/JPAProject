package com.sparta.mg.jpaproject.model.repositories;

import com.sparta.mg.jpaproject.model.entities.Department;
import com.sparta.mg.jpaproject.model.entities.DeptManager;
import com.sparta.mg.jpaproject.model.entities.DeptManagerId;
import com.sparta.mg.jpaproject.model.entities.Employee;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface DeptManagerRepository extends JpaRepository<DeptManager, DeptManagerId> {

    List<DeptManager> getDeptManagersById_DeptNo(String deptNo);


    @Modifying
    @Transactional
    @Query(value = "UPDATE employees.dept_manager " +
            "SET emp_no = :newEmpNo, from_date=:fromDate, to_date=:toDate " +
            "WHERE dept_no = :deptNo AND emp_no = :oldEmpNo"
    ,nativeQuery = true)
    void updateDeptManagerWithSameDepartment(LocalDate fromDate, LocalDate toDate, Integer newEmpNo, String deptNo, Integer oldEmpNo);
}