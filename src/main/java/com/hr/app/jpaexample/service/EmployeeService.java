package com.hr.app.jpaexample.service;

import com.hr.app.jpaexample.dao.EmployeeSpecifications;
import com.hr.app.jpaexample.entity.Employee;
import com.hr.app.jpaexample.repository.EmployeeRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ravi
 * @project jpa-example
 * @created on 06 Mar, 2023 10:26 PM
 */

@Service
public class EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;

    public List<Employee> findEmployeesByDepartmentId(Long departmentId) {
        return employeeRepository.findAll(EmployeeSpecifications.findAByDepartmentId(departmentId));
    }
}
