# Faculty Management System

A desktop Java Swing application for managing students, lecturers, courses,
departments and degrees within the Faculty of Computing & Technology.
Built for CTEC 22043 following the MVC (Model-View-Controller) pattern with
JDBC + MySQL for persistence.

## Tech Stack

- Java (Swing) for the desktop GUI
- JDBC + MySQL for data persistence
- MVC + DAO architecture

## Project Structure

```
faculty-management-system/
├── src/com/faculty/
│   ├── model/        Entity classes (POJOs)
│   ├── view/          Swing GUI screens
│   ├── controller/     Business logic between View and DAO
│   ├── dao/           Database CRUD operations
│   ├── util/           DB connection helper
│   └── main/           Application entry point
├── database/
│   ├── schema.sql        Table definitions
│   └── sample_data.sql   Sample rows for local testing
├── docs/
│   └── FMS-GroupO.pdf   
├── demo/
│   └── demo_video.mp4   
├── .gitignore
└── README.md
```

## Setup Instructions

### 1. Prerequisites
- JDK 17+ installed
- MySQL Server 8.x running locally
- MySQL Connector/J (e.g. `mysql-connector-j-8.x.x.jar`) added to your classpath

### 2. Create the database
```bash
mysql -u root -p < database/schema.sql
mysql -u root -p < database/sample_data.sql
```

### 3. Configure the connection
Edit `src/com/faculty/util/DBConnection.java` and update:
```java
DB_URL      // jdbc:mysql://localhost:3306/faculty_management_system
DB_USER     // your MySQL username
DB_PASSWORD // your MySQL password
```

### 4. Compile and run
```bash
# From the project root, with mysql-connector-j on the classpath:
javac -d out -cp "lib/mysql-connector-j-8.x.x.jar" $(find src -name "*.java")
java -cp "out:lib/mysql-connector-j-8.x.x.jar" com.faculty.main.Main
```
(On Windows, use `;` instead of `:` in the classpath.)

### 5. Sample logins (from sample_data.sql)
| Role     | Username        | Password      |
|----------|-----------------|---------------|
| Admin    | admin           | admin123      |
| Student  | kumar.student   | student123    |
| Lecturer | kumar.lecturer  | lecturer123   |

## Features

- **Authentication** — sign in / sign up with role-based access (Admin, Student, Lecturer)
- **Student module** — view/edit profile, view timetable, view enrolled courses with grades
- **Lecturer module** — view/edit profile, view timetable, view teaching courses
- **Admin module** — full CRUD for Students, Lecturers, Courses, Departments, Degrees

## Notes for Your Team

- This scaffold implements the required MVC + DAO structure and every
  feature listed in the assignment brief with a working UI, so you can run
  it, demo it, and extend it further (e.g. add password hashing, richer
  validation, or nicer dialogs) as your team sees fit.
- Passwords are stored in plain text in this starter for simplicity —
  consider hashing them (e.g. with BCrypt) before submission if your rubric
  expects it.
- Update `docs/FMS-GroupXX.pdf` with each member's work and student numbers,
  and add your demo video under `demo/` (or a link) before submission.
- Rename `FMS-GroupXX` references throughout to your actual group number.
