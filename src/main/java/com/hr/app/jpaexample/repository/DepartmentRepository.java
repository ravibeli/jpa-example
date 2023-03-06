package com.hr.app.jpaexample.repository;

import com.hr.app.jpaexample.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author ravibeli@gmail.com
 * @project jpa-example
 * @created on 06 Mar, 2023 10:19 PM
 */

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

}

