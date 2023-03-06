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

| Entities    | API Title                     | Method   | URL                                             |
|-------------|-------------------------------|----------|-------------------------------------------------|
| Employee    | Get All Employees Details     | GET      | http://localhost:8080/employees                 |
| Employee    | Search Employees By Dept ID   | GET      | http://localhost:8080/employees/{depatmentId}   |
| Department  | Get All Departments Details   | GET      | http://localhost:8080/departments               |
| Department  | Get Department By Dept ID     | GET      | http://localhost:8080/departments/{depatmentId} |

