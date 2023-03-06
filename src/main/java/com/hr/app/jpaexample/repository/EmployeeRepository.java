package com.hr.app.jpaexample.repository;

import com.hr.app.jpaexample.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author ravibeli@gmail.com
 * @project jpa-example
 * @created on 06 Mar, 2023 10:25 PM
 */

@Repository
public interface EmployeeRepository extends JpaSpecificationExecutor<Employee>,
        JpaRepository<Employee, Long> {
}

