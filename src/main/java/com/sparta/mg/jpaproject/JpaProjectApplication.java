package com.sparta.mg.jpaproject;

import com.sparta.mg.jpaproject.model.entities.User;
import com.sparta.mg.jpaproject.model.repositories.DepartmentRepository;
import com.sparta.mg.jpaproject.model.repositories.SalaryRepository;

import com.sparta.mg.jpaproject.model.repositories.UserRepository;
import com.sparta.mg.jpaproject.services.DepartmentService;
import com.sparta.mg.jpaproject.services.EmployeeService;
import com.sparta.mg.jpaproject.services.SalaryService;
import com.sparta.mg.jpaproject.model.entities.Employee;
import com.sparta.mg.jpaproject.model.repositories.DeptEmpRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;


@SpringBootApplication
public class JpaProjectApplication {
    private Logger logger = LoggerFactory.getLogger(JpaProjectApplication.class);

    public static void main(String[] args) {

        SpringApplication.run(JpaProjectApplication.class, args);
    }

    // ONLY RUN ONCE
//    @Bean
//    CommandLineRunner runner(UserRepository userRepository, PasswordEncoder passwordEncoder) {
//        return args -> {
//            userRepository.save(new User("basic", passwordEncoder.encode("password"), "ROLE_BASIC"));
//            userRepository.save(new User("update", passwordEncoder.encode("password"), "ROLE_BASIC,ROLE_UPDATE"));
//            userRepository.save(new User("admin", passwordEncoder.encode("password"), "ROLE_BASIC,ROLE_UPDATE,ROLE_ADMIN"));
//        };
//    }

}
