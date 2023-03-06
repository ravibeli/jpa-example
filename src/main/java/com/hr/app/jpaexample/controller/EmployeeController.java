package com.hr.app.jpaexample.controller;

import com.hr.app.jpaexample.entity.Department;
import com.hr.app.jpaexample.entity.Employee;
import com.hr.app.jpaexample.responses.EmployeeDto;
import com.hr.app.jpaexample.service.DepartmentService;
import com.hr.app.jpaexample.service.EmployeeService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ravi@gmailcom
 * @project jpa-example
 * @created on 06 Mar, 2023 10:20 PM
 */

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;


    // GET /employees/{id}   GET /employees/<dept_id>  dept_id = 10
    @GetMapping("/{id}")
    public List<EmployeeDto>  getDepartmentById(@PathVariable Long id) {
        return employeeService.findEmployeesByDepartmentId(id);
    }

    @GetMapping("")
    public List<EmployeeDto> getAllEmployees() {
        return employeeService.findAllEmployees();
    }

}

