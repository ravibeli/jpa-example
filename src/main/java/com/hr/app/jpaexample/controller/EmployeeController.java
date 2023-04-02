package com.hr.app.jpaexample.controller;

import com.hr.app.jpaexample.entity.Employee;
import com.hr.app.jpaexample.mappers.EmployeeMapper;
import com.hr.app.jpaexample.repository.DepartmentRepository;
import com.hr.app.jpaexample.repository.EmployeeRepository;
import com.hr.app.jpaexample.repository.JobRepository;
import com.hr.app.jpaexample.requests.EmployeeRequest;
import com.hr.app.jpaexample.responses.EmployeeDto;
import com.hr.app.jpaexample.service.EmployeeService;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ravibeli@gmail.com
 * @project jpa-example
 * @created on 06 Mar, 2023 10:20 PM
 */

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    JobRepository jobRepository;

    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @GetMapping("/departments/{id}")
    public List<EmployeeDto>  getDepartmentById(@PathVariable Long id) {
        return employeeService.findEmployeesByDepartmentId(id);
    }

    @GetMapping("/all")
    public List<EmployeeDto> getAllEmployees() {
        return employeeService.findAllEmployees();
    }

    @GetMapping
    public List<EmployeeDto>  findEmployeesByDepartmentName(@RequestParam(required = false, name = "departmentName") String departmentName,
                                                          @RequestParam(required = false, name = "minSalary") BigDecimal minSalary,
                                                          @RequestParam(required = false, name = "maxSalary") BigDecimal maxSalary) {
        return employeeService.findByDepartmentAndSalaryRange(departmentName, minSalary, maxSalary);
    }

    @GetMapping("/{departmentName}")
    public List<EmployeeDto>  findEmployeesByDepartmentName(@PathVariable(required = false, name = "departmentName") String departmentName) {
        return employeeService.findByDepartmentName(departmentName);
    }

    @GetMapping("/clearCache/{departmentName}")
    public String clearCacheEmployeesByDepartmentName(@PathVariable(required = false, name = "departmentName") String departmentName) {
        employeeService.clearCacheValuesBySubjectId(departmentName);
        return "employees of " + departmentName + " cache cleared";
    }

    @GetMapping("/clearCache")
    public String clearCacheAllEmployees() {
        employeeService.clearAllCacheValues();
        return "All employees cache cleared";
    }

    @PostMapping
    public EmployeeDto createEmployee(@RequestBody EmployeeDto employeeDto) {
        return employeeService.createEmployee(employeeDto);
    }
}

