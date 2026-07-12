-- =========================================================
-- Faculty Management System - Database Schema
-- =========================================================
DROP DATABASE IF EXISTS faculty_management_system;
CREATE DATABASE faculty_management_system;
USE faculty_management_system;

-- ---------------------------------------------------------
-- Departments
-- ---------------------------------------------------------
CREATE TABLE departments (
    dept_id       INT AUTO_INCREMENT PRIMARY KEY,
    name          VARCHAR(100) NOT NULL,
    hod_name      VARCHAR(100),
    degree_name   VARCHAR(100),
    staff_count   INT DEFAULT 0
);

-- ---------------------------------------------------------
-- Degrees
-- ---------------------------------------------------------
CREATE TABLE degrees (
    degree_id      INT AUTO_INCREMENT PRIMARY KEY,
    degree_name    VARCHAR(100) NOT NULL,
    dept_id        INT,
    student_count  INT DEFAULT 0,
    FOREIGN KEY (dept_id) REFERENCES departments(dept_id) ON DELETE SET NULL
);

-- ---------------------------------------------------------
-- Students
-- ---------------------------------------------------------
CREATE TABLE students (
    student_id      INT AUTO_INCREMENT PRIMARY KEY,
    student_number  VARCHAR(30) NOT NULL UNIQUE,
    full_name       VARCHAR(100) NOT NULL,
    degree_id       INT,
    email           VARCHAR(100),
    mobile          VARCHAR(20),
    FOREIGN KEY (degree_id) REFERENCES degrees(degree_id) ON DELETE SET NULL
);

-- ---------------------------------------------------------
-- Lecturers
-- ---------------------------------------------------------
CREATE TABLE lecturers (
    lecturer_id   INT AUTO_INCREMENT PRIMARY KEY,
    full_name     VARCHAR(100) NOT NULL,
    dept_id       INT,
    email         VARCHAR(100),
    mobile        VARCHAR(20),
    FOREIGN KEY (dept_id) REFERENCES departments(dept_id) ON DELETE SET NULL
);

-- ---------------------------------------------------------
-- Courses
-- ---------------------------------------------------------
CREATE TABLE courses (
    course_id     INT AUTO_INCREMENT PRIMARY KEY,
    course_code   VARCHAR(20) NOT NULL UNIQUE,
    course_name   VARCHAR(100) NOT NULL,
    credits       INT DEFAULT 0,
    lecturer_id   INT,
    FOREIGN KEY (lecturer_id) REFERENCES lecturers(lecturer_id) ON DELETE SET NULL
);

-- ---------------------------------------------------------
-- Enrollments (student <-> course, with grade)
-- ---------------------------------------------------------
CREATE TABLE enrollments (
    enrollment_id  INT AUTO_INCREMENT PRIMARY KEY,
    student_id     INT NOT NULL,
    course_id      INT NOT NULL,
    grade          VARCHAR(5),
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE CASCADE,
    UNIQUE KEY uniq_enrollment (student_id, course_id)
);

-- ---------------------------------------------------------
-- Timetable (course schedule slots)
-- ---------------------------------------------------------
CREATE TABLE timetable (
    timetable_id  INT AUTO_INCREMENT PRIMARY KEY,
    course_id     INT NOT NULL,
    day_of_week   VARCHAR(10) NOT NULL,
    time_slot     VARCHAR(10) NOT NULL,
    FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE CASCADE
);

-- ---------------------------------------------------------
-- Users (login accounts, role-based access)
-- role: ADMIN, STUDENT, LECTURER
-- linked_id: references students.student_id or lecturers.lecturer_id
--            (NULL for ADMIN)
-- ---------------------------------------------------------
CREATE TABLE users (
    user_id     INT AUTO_INCREMENT PRIMARY KEY,
    username    VARCHAR(50) NOT NULL UNIQUE,
    password    VARCHAR(100) NOT NULL,
    role        VARCHAR(20) NOT NULL,
    linked_id   INT
);
