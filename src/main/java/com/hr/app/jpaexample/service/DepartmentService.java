package com.hr.app.jpaexample.service;

import com.hr.app.jpaexample.entity.Department;
import com.hr.app.jpaexample.repository.DepartmentRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ravibeli@gmail.com
 * @project jpa-example
 * @created on 06 Mar, 2023 10:19 PM
 */

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public Department getDepartmentById(Long id) {
        return departmentRepository.findById(id).orElse(null);
    }

    public Department saveDepartment(Department department) {
        return departmentRepository.save(department);
    }
    public Department insertRow(Department department) {
        return departmentRepository.save(department);
    }










    public void deleteDepartmentById(Long id) {
        departmentRepository.deleteById(id);
    }

}

