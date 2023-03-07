# Simple Spring Boot 3.0.4 JPA Example

### Instruction

1. [Install JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
2. [Install Gradle 7.6.1](https://gradle.org/next-steps/?version=7.6.1&format=bin)
3. [Install git command line tool](https://git-scm.com/downloads)
4. Set up environment variable: `JAVA_HOME = C:\Program Files\Java\jdk-17`
5. Set up environment variable: `GRADLE_HOME = C:\gradle-7.6.1`
6. Set up environment variable `PATH = %JAVA_HOME%\bin;%GRADLE_HOME%\bin` 
7. [Install MySQL Installer 8.0.32](https://dev.mysql.com/downloads/installer)
8. [Install MySQL Workbench 8.0.32](https://dev.mysql.com/downloads/workbench)
9. [Download and install HR schema](https://github.com/nomemory/hr-schema-mysql/blob/master/hr-schema-mysql.sql)
10. Git Checkout command: `git clone https://github.com/ravibeli/jpa-example.git`
11. Build the project using command `gradle clean build`
12. [Install postman](https://www.postman.com/downloads)

### API Documentation:

| Entities   | API Title                                                                   | Method | URL                                                                             |
|------------|-----------------------------------------------------------------------------|--------|---------------------------------------------------------------------------------|
| Employee   | Get All Employees Details                                                   | GET    | http://localhost:8080/employees                                                 |
| Employee   | Get Employees By Dept ID                                                    | GET    | http://localhost:8080/employees/20                                              |
| Employee   | Get Employees salary between 4800 and 6000                                  | GET    | http://localhost:8080/employees?minSalary=4800&maxSalary=6000                   |
| Employee   | Get Employees whose salary in range 4800 to 6000 from particular department | GET    | http://localhost:8080/employees?departmentName=IT&minSalary=4800&maxSalary=6000 |
| Employee   | Get Employees whose salary in range 4800 to 6000 from all departments       | GET    | http://localhost:8080/employees?minSalary=4800&maxSalary=6000                   |
| Department | Get All Departments Details                                                 | GET    | http://localhost:8080/departments                                               |
| Department | Get Department By Dept ID                                                   | GET    | http://localhost:8080/departments/20                                            |

#### JPA Specification logic to build multiple AND conditions dynamically based on the input given:

```
    public static Specification<Employee> findByDepartmentNameAndSalaryRange(String departmentName, BigDecimal minSalary, BigDecimal maxSalary) {
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
                return builder.and(predicates.toArray(new Predicate[0]));
            }
        };
    }
```

#### Above code is JPA Specification logic builds dynamic query that satisfied all the below requirements:

1. Search all the employees from departmentName = IT
    > GET http://localhost:8080/employees?departmentName=IT
2. Search all the employees whose salary range between '4800' and '6000' regardless of all the departments
   > GET http://localhost:8080/employees?minSalary=4800&maxSalary=6000
3. Search all the employees whose salary range between '4800' and '6000' in 'IT' department
   > GET http://localhost:8080/employees?departmentName=IT&minSalary=4800&maxSalary=6000
   
