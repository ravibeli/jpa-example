package com.hr.app.jpaexample.service;

import com.hr.app.jpaexample.dao.EmployeeSpecifications;
import com.hr.app.jpaexample.entity.Employee;
import com.hr.app.jpaexample.i18n.exceptions.ApplicationException;
import com.hr.app.jpaexample.mappers.EmployeeMapper;
import com.hr.app.jpaexample.repository.DepartmentRepository;
import com.hr.app.jpaexample.repository.EmployeeRepository;
import com.hr.app.jpaexample.repository.JobRepository;
import com.hr.app.jpaexample.responses.EmployeeDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.hr.app.jpaexample.config.CacheConfig.EMPLOYEES_CACHE;
import static com.hr.app.jpaexample.config.CacheConfig.EMPLOYEES_CACHE_MANAGER;
import static com.hr.app.jpaexample.constants.Constants.ENTITY_ALREADY_EXISTS;
import static com.hr.app.jpaexample.constants.Constants.ENTITY_DELETED_SUCCESSFULLY;
import static com.hr.app.jpaexample.constants.Constants.ENTITY_NOT_FOUND;

/**
 * @author ravibeli@gmail.com
 * @project jpa-example
 * @created on 06 Mar, 2023 10:26 PM
 */


@Slf4j
@Service
public class EmployeeService {

    @Autowired
    CacheManager cacheManager;

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


    @Cacheable(value = "employees", key = "#departmentName", cacheManager = EMPLOYEES_CACHE_MANAGER)
    public List<EmployeeDto> findByDepartmentName(String departmentName) {
        Specification<Employee> spec = Specification.where(EmployeeSpecifications
                .findEmployeesBySearch(departmentName,null,null,null,null,null));
        List<Employee> employees = employeeRepository.findAll(spec);
        return EmployeeMapper.INSTANCE.toEmployeeDtoList(employees);
    }

    @CacheEvict(value = "employees", key = "#departmentName", cacheManager = EMPLOYEES_CACHE_MANAGER)
    public void clearEmployeesCacheByDepartmentName(String departmentName) {}

    @CacheEvict(value = "employees", allEntries = true, cacheManager = EMPLOYEES_CACHE_MANAGER)
    public void evictAllCacheValues() {}

    public void evictEmployeesCacheByDepartmentName(String departmentName) {
        log.info("cacheManager.getCache(EMPLOYEES_CACHE).getName() = {}",
                Objects.requireNonNull(cacheManager.getCache(EMPLOYEES_CACHE)).getName());
        log.info(Objects.requireNonNull(Objects
                .requireNonNull(Objects
                        .requireNonNull(cacheManager.getCache(EMPLOYEES_CACHE)).get(departmentName)).get()).toString());
    }

    public void clearAllCacheValues() {
        log.info("cacheManager.getCache(EMPLOYEES_CACHE).getName() = {}",
                cacheManager.getCache(EMPLOYEES_CACHE).getName());
        Objects.requireNonNull(cacheManager.getCache(EMPLOYEES_CACHE)).clear();
    }

    public void clearCacheValuesBySubjectId(String subjectId) {
        log.info("cacheManager.getCache(EMPLOYEES_CACHE).getName() = {}",
                cacheManager.getCache(EMPLOYEES_CACHE).getName());
        log.info("cache get(subjectId) = {}", Objects.requireNonNull(Objects
                .requireNonNull(Objects
                        .requireNonNull(cacheManager.getCache(EMPLOYEES_CACHE)).get(subjectId)).get()).toString());
        Objects.requireNonNull(cacheManager.getCache(EMPLOYEES_CACHE)).evict(subjectId);
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
