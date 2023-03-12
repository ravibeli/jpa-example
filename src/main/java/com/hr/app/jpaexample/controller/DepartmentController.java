package com.hr.app.jpaexample.controller;

import com.hr.app.jpaexample.entity.Department;
import com.hr.app.jpaexample.service.DepartmentService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author ravibeli@gmail.com
 * @project jpa-example
 * @created on 06 Mar, 2023 10:20 PM
 */

@RestController
@RequestMapping("/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @GetMapping("")
    public List<Department> getAllDepartments() {
        return departmentService.getAllDepartments();
    }

    @GetMapping("/{id}")
    public Department getDepartmentById(@PathVariable Long id) {
        return departmentService.getDepartmentById(id);
    }

    @PostMapping(value="/insert", consumes = "application/json", produces = "application/json")
    public Department insertRow(@RequestBody Department department){

        return departmentService.insertRow(department);

    }

}

