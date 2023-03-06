package com.hr.app.jpaexample.responses;

import java.time.LocalDate;
import lombok.Data;

/**
 * @author ravibeli@gmail.com
 * @project jpa-example
 * @created on 07 Mar, 2023 12:18 AM
 */

@Data
public class EmployeeDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private LocalDate hireDate;
}
