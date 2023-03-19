package com.hr.app.jpaexample.service;

import com.hr.app.jpaexample.dao.EmployeeSpecifications;
import com.hr.app.jpaexample.entity.Employee;
import com.hr.app.jpaexample.i18n.exceptions.ApplicationException;
import com.hr.app.jpaexample.mappers.EmployeeMapper;
import com.hr.app.jpaexample.repository.DepartmentRepository;
import com.hr.app.jpaexample.repository.EmployeeRepository;
import com.hr.app.jpaexample.repository.JobRepository;
import com.hr.app.jpaexample.responses.EmployeeDto;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import static com.hr.app.jpaexample.constants.Constants.*;

/**
 * @author ravibeli@gmail.com
 * @project jpa-example
 * @created on 06 Mar, 2023 10:26 PM
 */

@Service
public class EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    JobRepository jobRepository;

    public List<EmployeeDto> findEmployeesByDepartmentId(Long departmentId) {
        List<Employee> employees = employeeRepository.findAll(EmployeeSpecifications.findAByDepartmentId(departmentId));
        return EmployeeMapper.INSTANCE.toEmployeeDtoList(employees);
    }

    public List<EmployeeDto> findAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return EmployeeMapper.INSTANCE.toEmployeeDtoList(employees);
    }

    public List<EmployeeDto> findEmployeesBySearch(String departmentName, BigDecimal minSalary,
                                                   BigDecimal maxSalary, String nameStartsWith,
                                                   LocalDate hireDateFrom, LocalDate hireDateTo) {
        Specification<Employee> spec = Specification.where(EmployeeSpecifications
                .findEmployeesBySearch(departmentName, minSalary, maxSalary, nameStartsWith,hireDateFrom, hireDateTo));
        List<Employee> employees = employeeRepository.findAll(spec);
        return EmployeeMapper.INSTANCE.toEmployeeDtoList(employees);
    }
    public EmployeeDto createEmployee(EmployeeDto employeeDto) {

        //Throw user defined ApplicationException with proper error code
        Optional<Employee> employeeOptional = employeeRepository.findById(employeeDto.getId());

        if (employeeOptional.isPresent()) {
            throw new ApplicationException(HttpStatus.CONFLICT, ENTITY_ALREADY_EXISTS, employeeDto.getId());
        }

        Employee employee = EmployeeMapper.INSTANCE.toEmployee(employeeDto, employeeRepository,
                departmentRepository, jobRepository);
        Employee employeeCreated = employeeRepository.save(employee);
        return EmployeeMapper.INSTANCE.toEmployeeDto(employeeCreated);
    }

    public EmployeeDto updateEmployee(EmployeeDto employeeDto) {

        //Throw user defined ApplicationException with proper error code
        Optional<Employee> existingEmployeeOptional  = employeeRepository.findById(employeeDto.getId());

        if (!existingEmployeeOptional.isPresent()) {
            throw new ApplicationException(HttpStatus.NOT_FOUND, ENTITY_NOT_FOUND, employeeDto.getId());
        }

        Employee updatedEmployee = EmployeeMapper.INSTANCE.toEmployee(employeeDto, employeeRepository,
                departmentRepository, jobRepository);
        updatedEmployee.setId(existingEmployeeOptional.get().getId());
        Employee employeeUpdated = employeeRepository.save(updatedEmployee);
        return EmployeeMapper.INSTANCE.toEmployeeDto(employeeUpdated);
    }

    public void deleteEmployee(Long id) {

        // check if employee with the given id exists
        Optional<Employee> existingEmployeeOptional = employeeRepository.findById(id);

        if (!existingEmployeeOptional.isPresent()) {
            throw new ApplicationException(HttpStatus.NOT_FOUND, ENTITY_NOT_FOUND, id);
        }

        employeeRepository.deleteById(id);
        throw new ApplicationException(HttpStatus.OK, ENTITY_DELETED_SUCCESSFULLY, id);
    }
}
