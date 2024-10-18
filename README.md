# Elementary Class Manager

## Overview

The **Elementary Class Manager** is a Java-based web application built with **Jakarta EE**, providing a complete system for managing school operations. It enables administrators to:

- **Teacher Management**: Add, update, delete, and search for teachers by ID or last name.
- **Student Management**: Add, update, delete, and search for students by ID, last name, or social security number (SSN). Students are automatically archived when they complete the final grade.
- **Class Group Management**: Create, update, delete, and retrieve class groups. Assign teachers and students to class groups, ensuring student grades align with their class group.
- **Grade Management**: Increment the grade of all students in a class group and archive students who have completed the final grade.

This application streamlines the management of teachers, students, and class groups, featuring search capabilities by key attributes and automatic grade handling and archiving.

## Features

- **Teacher Management**
  - Add, update, delete, and retrieve teacher details.
  - Search for teachers by their last name.

- **Class Group Management**
  - Insert, update, delete, and retrieve class groups.
  - Assign teachers and students to class groups.
  - Ensure the student's grade matches the class group when assigning them.

- **Student Management**
  - Add, update, delete, and retrieve student details.
  - Archive students upon completion of the final grade.

## Technologies Used

- **Java** with **Jakarta EE** for backend implementation.
- **Hibernate** for ORM (Object-Relational Mapping).
- **Tomcat** for server deployment.
- **HikariCP** for database connection pooling.
- **Lombok** for reducing boilerplate code.
- **Maven** for project management.

## Running the Application

1. Ensure you have a MySQL database named `elementaryschooldb` and update the connection details in the `persistence.xml`.
2. Deploy the application on the Tomcat server.
3. Use an API client like Postman to interact with the endpoints.

## Endpoints

### Teacher Endpoints

- `POST /teachers`: Add a new teacher.
- `PUT /teachers/{id}`: Update a teacher's details (excluding registration number).
- `DELETE /teachers/{id}`: Delete a teacher.
- `GET /teachers/{id}`: Retrieve a teacher by their ID.
- `GET /teachers?lastname={lastname}`: Retrieve teachers by their last name.
- `GET /teachers/test`: Test endpoint.

### Class Group Endpoints

- `POST /classgroups`: Create a new class group.
- `PUT /classgroups/{id}`: Update class group details.
- `DELETE /classgroups/{id}`: Delete a class group.
- `GET /classgroups/{id}`: Retrieve a class group by ID.
- `GET /classgroups`: Retrieve all class groups.
- `PUT /classgroups/{classGroupId}/increment-grade`: Increment the grade for all students in the specified class group.

- `PUT /classgroups/{classGroupId}/assign-student/{studentId}`: Assign a student to a class group, ensuring the student's grade matches the class group's grade.
- `PUT /classgroups/{classGroupId}/assign-teacher/{teacherId}`: Assign a teacher to a class group, replacing the existing teacher if one is already assigned.

### Student Endpoints

- `POST /students`: Add a new student.
- `PUT /students/{id}`: Update student details.
- `DELETE /students/{id}`: Delete a student.
- `GET /students/{id}`: Retrieve a student by ID.
- `GET /students?lastname={lastname}`: Retrieve students by their last name.

## Future Enhancements

- Add comprehensive testing (unit tests and integration tests).
- Improve exception handling and validation.
- Enhance security (authentication and authorization).
