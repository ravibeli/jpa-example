package com.hr.app.jpaexample.dao;

import static java.util.Objects.nonNull;

import com.hr.app.jpaexample.entity.Department;
import com.hr.app.jpaexample.entity.Department_;
import com.hr.app.jpaexample.entity.Employee;
import com.hr.app.jpaexample.entity.Employee_;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


/**
 * @author ravibeli@gmail.com
 * @project jpa-example
 * @created on 06 Mar, 2023 10:30 PM
 */

@Service
public class EmployeeSpecifications {
    public static Specification<Employee> findAByDepartmentId(Long departmentId) {
        return new Specification<Employee>() {
            @Override
            public Predicate toPredicate(Root<Employee> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                Join<Employee, Department> departmentJoin = root.join(Employee_.DEPARTMENT, JoinType.INNER);
                return criteriaBuilder.equal(departmentJoin.get(Department_.departmentId), departmentId);
            }
        };
    }

    public static Specification<Employee> findEmployeesBySearch(String departmentName, BigDecimal minSalary, BigDecimal maxSalary,
                                                                String nameStartsWith,
                                                                LocalDate hireDateFrom, LocalDate hireDateTo) {
        return new Specification<Employee>() {
            @Override
            public Predicate toPredicate(Root<Employee> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                Join<Employee, Department> departmentJoin = root.join(Employee_.department);

                List<Predicate> predicates = new ArrayList<>();
                if (nonNull(departmentName)) {
                    predicates.add(builder.and(builder.equal(departmentJoin
                            .get(Department_.DEPARTMENT_NAME), departmentName)));
                }
                if (nonNull(minSalary) & nonNull(maxSalary)) {
                    predicates.add(builder.and(builder.between(root.get(Employee_.salary), minSalary, maxSalary)));
                }
                if (nameStartsWith != null) {
                    predicates.add(builder.like(root.get(Employee_.firstName), nameStartsWith + "%"));
                }
                if (hireDateFrom != null & hireDateTo != null) {
                    predicates.add(builder.and(builder.between(root.get(Employee_.hireDate), hireDateFrom, hireDateTo)));
                }
                return builder.and(predicates.toArray(new Predicate[0]));
            }
        };
    }

}
