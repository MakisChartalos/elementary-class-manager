# Elementary Class Manager

> **Work in Progress**  
> This project is under active development. Currently, only the `TeacherRestController` is implemented, while the other controllers (`StudentRestController`, `ClassGroupRestController`, etc.) and some functionalities are still being worked on.

## Overview

The Elementary Class Manager is a Java-based application built using Jakarta EE. It provides a system for managing elementary school data, including teachers, students, and class groups. The application allows administrators to perform CRUD operations and assign teachers and students to specific class groups.

## Features

- **Teacher Management**
    - Add, update, delete, and retrieve teacher details.
    - Retrieve teachers by their last name.

- **Class Group Management**
    - Insert, update, delete, and retrieve class groups (functionality in progress).
    - Assign teachers and students to class groups (endpoint implementations pending).

- **Student Management**
    - Add, update, delete, and retrieve student details (functionality in progress).
    - Archive students when they complete the final grade (functionality in progress).


## Technologies Used

- **Java** with **Jakarta EE** for the backend implementation.
- **Hibernate** for ORM (Object-Relational Mapping).
- **Tomcat** for server deployment.
- **HikariCP** for database connection pooling.
- **Lombok** for reducing boilerplate code.
- **Maven** for project management.

## Running the Application

1. Ensure you have a MySQL database named `elementaryschooldb` and update the connection details in the `persistence.xml`.
2. Deploy the application on Tomcat server.
3. Use an API client like Postman to interact with the endpoints.


## Existing Endpoints

### Teacher Endpoints

- `POST /teachers`: Add a new teacher.
- `PUT /teachers/{id}`: Update a teacher's details (excluding registration number).
- `DELETE /teachers/{id}`: Delete a teacher.
- `GET /teachers/{id}`: Retrieve a teacher by their ID.
- `GET /teachers?lastname={lastname}`: Retrieve teachers by their last name.
- `GET /teachers/test`: Test endpoint.

### Future Endpoints

#### Class Group Endpoints (Planned)

- `POST /classgroups`: Create a new class group.
- `PUT /classgroups/{id}`: Update class group details.
- `DELETE /classgroups/{id}`: Delete a class group.
- `GET /classgroups/{id}`: Retrieve a class group by ID.

#### Assignments

- `PUT /classgroups/{classGroupId}/assign-student/{studentId}`: Assign a student to a class group, ensuring the student's grade matches the class group's grade.
- `PUT /classgroups/{classGroupId}/assign-teacher/{teacherId}`: Assign a teacher to a class group, replacing the existing teacher if one is already assigned.

### Student Endpoints (Planned)

- `POST /students`: Add a new student.
- `PUT /students/{id}`: Update student details.
- `DELETE /students/{id}`: Delete a student.
- `GET /students/{id}`: Retrieve a student by ID.
- `GET /students?lastname={lastname}`: Retrieve students by their last name.

## Work in Progress

The following components and functionalities are still being developed:

- `StudentRestController` for student-related operations.
- `ClassGroupRestController` for managing class groups and assigning students and teachers.
- Endpoints for assigning students to class groups and teachers to class groups.
- Features for incrementing student grades and archiving graduated students.


## Future Enhancements

- Implement missing controllers and functionalities.
- Add comprehensive testing (unit tests and integration tests).
- Improve exception handling and validation.
- Enhance security (authentication and authorization).

