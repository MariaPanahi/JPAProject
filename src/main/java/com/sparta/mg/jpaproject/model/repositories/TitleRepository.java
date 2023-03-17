package com.sparta.mg.jpaproject.model.repositories;

import com.sparta.mg.jpaproject.model.entities.Employee;
import com.sparta.mg.jpaproject.model.entities.Title;
import com.sparta.mg.jpaproject.model.entities.TitleId;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TitleRepository extends JpaRepository<Title, TitleId> {

    List<Title> findTitleByEmpNo(Employee empNo);

    @Query(nativeQuery = true, value = "SELECT * FROM titles where emp_no = :empNo AND from_date = :fromDate AND title = :title")
    Optional<Title> findTitleByEmpFromDateTitle (int empNo, LocalDate fromDate, String title);

    @Query(value = "SELECT DISTINCT t.id.title from Title t")
    List<String> getAllTitles();

    // @Modifying
    // @Query(nativeQuery = true, value = "INSERT INTO titles (emp_no, title, from_date, to_date) VALUES (:empNo, :title, :fromDate, :toDate)")
    // Optional<Title> createTitleByEmpFromDateTitle(@Param("empNo") int empNo, @Param("title") String title, @Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);

    // @Modifying
    // @Query(nativeQuery = true, value = "INSERT INTO titles (emp_no, title, from_date, to_date) VALUES (:empNo, :title, :fromDate, :toDate)")
    // int createTitleByEmpFromDateTitle(@Param("empNo") int empNo, @Param("title") String title, @Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);

    //  @Modifying
    //  @Query(nativeQuery = true, value = "DELETE FROM titles WHERE emp_no = :empNo AND title = :title AND from_date = :fromDate")
    //  void deleteTitleByEmpFromDateTitle(@Param("empNo") int empNo, @Param("title") String title, @Param("fromDate") LocalDate fromDate);

    // @Modifying
    // @Transactional
    // @Query(nativeQuery = true, value = "DELETE FROM titles WHERE emp_no = :empNo AND title = :title AND from_date = :fromDate")
    // List<Title> deleteTitleByEmpFromDateTitle(@Param("empNo") int empNo, @Param("title") String title, @Param("fromDate") LocalDate fromDate);

}