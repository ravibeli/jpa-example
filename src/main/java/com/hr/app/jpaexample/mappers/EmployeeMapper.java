package com.hr.app.jpaexample.mappers;

import com.hr.app.jpaexample.entity.Employee;
import com.hr.app.jpaexample.responses.EmployeeDto;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface EmployeeMapper {
    EmployeeMapper INSTANCE = Mappers.getMapper(EmployeeMapper.class);

    @Mapping(source = "job.id", target = "jobId")
    EmployeeDto toEmployeeDto(Employee employee);
    List<EmployeeDto> toEmployeeDtoList(List<Employee> employees);

}
