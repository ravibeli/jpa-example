package com.hr.app.jpaexample.dao;

import com.hr.app.jpaexample.entity.Department;
import com.hr.app.jpaexample.entity.Employee;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


/**
 * @author rav  9:30pm to 11pm : 1.5hours
 * @project jpa-example
 * @created on 06 Mar, 2023 10:30 PM
 */

@Service
public class EmployeeSpecifications {
    public static Specification<Employee> findAByDepartmentId(Long departmentId) {
        return new Specification<Employee>() {
            @Override
            public Predicate toPredicate(Root<Employee> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                Join<Employee, Department> departmentJoin = root.join("department", JoinType.INNER);
                return criteriaBuilder.equal(departmentJoin.get("departmentId"), departmentId);
            }
        };
    }

}
