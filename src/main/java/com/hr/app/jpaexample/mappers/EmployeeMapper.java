package com.hr.app.jpaexample.mappers;

import com.hr.app.jpaexample.entity.Employee;
import com.hr.app.jpaexample.repository.DepartmentRepository;
import com.hr.app.jpaexample.repository.EmployeeRepository;
import com.hr.app.jpaexample.repository.JobRepository;
import com.hr.app.jpaexample.responses.EmployeeDto;
import java.util.List;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE,
        componentModel = "spring", uses = {JobRepository.class, DepartmentRepository.class, EmployeeRepository.class})
public interface EmployeeMapper {
    EmployeeMapper INSTANCE = Mappers.getMapper(EmployeeMapper.class);

    @Mapping(source = "manager.id", target = "managerId")
    @Mapping(source = "department.departmentId", target = "departmentId")
    @Mapping(source = "job.id", target = "jobId")
    EmployeeDto toEmployeeDto(Employee employee);

    // getReferenceById(id) instead findByJobId(id) should be used for better performance
    //Reference:The Spring Data JPA findById Anti-Pattern: https://vladmihalcea.com/spring-data-jpa-findbyid/
    @Mapping(target = "job", expression = "java(jobRepository.getReferenceById(employeeDto.getJobId()))")
    @Mapping(target = "department", expression = "java(departmentRepository.getReferenceById(employeeDto.getDepartmentId()))")
    @Mapping(target = "manager", expression = "java(employeeRepository.getReferenceById(employeeDto.getManagerId()))")
    Employee toEmployee(EmployeeDto employeeDto, @Context EmployeeRepository employeeRepository,
                        @Context DepartmentRepository departmentRepository,
                        @Context JobRepository jobRepository);

    List<EmployeeDto> toEmployeeDtoList(List<Employee> employees);
}
