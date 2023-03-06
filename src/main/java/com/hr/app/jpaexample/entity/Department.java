package com.hr.app.jpaexample.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Data;

/**
 * @author ravibeli@gmail.com
 * @project jpa-example
 * @created on 06 Mar, 2023 10:13 PM
 */

@Data
@Entity
@Table(name = "departments")
public class Department {

    @Id
    @Column(name = "department_id")
    private Long departmentId;

    @Column(name = "department_name")
    private String departmentName;

    @Column(name = "manager_id")
    private Long managerId;

    @Column(name = "location_id")
    private Long locationId;

    // getters and setters
}

