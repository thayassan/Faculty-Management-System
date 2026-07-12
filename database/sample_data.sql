-- =========================================================
-- Faculty Management System - Sample Data
-- Run schema.sql first, then this file.
-- =========================================================
USE faculty_management_system;

-- ---------------------------------------------------------
-- Departments
-- ---------------------------------------------------------
INSERT INTO departments (name, hod_name, degree_name, staff_count) VALUES
('Applied Computing', 'Kumar Sanga', 'Engineering Technology', 15),
('Software Engineering', 'Kumar Sanga', 'Information Technology', 17),
('Computer Systems Engineering', 'Kumar Sanga', 'Computer Science', 12);

-- ---------------------------------------------------------
-- Degrees
-- ---------------------------------------------------------
INSERT INTO degrees (degree_name, dept_id, student_count) VALUES
('Engineering Technology', 1, 375),
('Information Technology', 2, 375),
('Computer Science', 3, 325),
('Bio Systems Technology', 1, 75);

-- ---------------------------------------------------------
-- Students
-- ---------------------------------------------------------
INSERT INTO students (student_number, full_name, degree_id, email, mobile) VALUES
('ET/2022/011', 'Kumar Sangakkara', 1, 'kumars-et22011@stu.kln.ac.lk', '0123456789'),
('ET/2022/007', 'Nimal Perera', 1, 'nimalp-et22007@stu.kln.ac.lk', '0123456780'),
('ET/2022/009', 'Saman Kumara', 1, 'samank-et22009@stu.kln.ac.lk', '0123456781'),
('ET/2022/012', 'Mithali Raj', 4, 'mithalir-et22012@stu.kln.ac.lk', '0676543210');

-- ---------------------------------------------------------
-- Lecturers
-- ---------------------------------------------------------
INSERT INTO lecturers (full_name, dept_id, email, mobile) VALUES
('Kumar Sangakkara', 2, 'kumars@kln.ac.lk', '0123456789'),
('Mithali Raj', 1, 'mithalir@kln.ac.lk', '0676543210');

-- ---------------------------------------------------------
-- Courses
-- ---------------------------------------------------------
INSERT INTO courses (course_code, course_name, credits, lecturer_id) VALUES
('ETEC21062', 'OOP', 2, 1),
('CSCI21052', 'OOP', 2, 1),
('CSCI21042', 'OOP', 2, 1),
('ETEC21022', 'OOP', 2, 2),
('ETEC21032', 'OOP', 2, 1),
('ETEC21012', 'OOP', 2, 1);

-- ---------------------------------------------------------
-- Enrollments
-- ---------------------------------------------------------
INSERT INTO enrollments (student_id, course_id, grade) VALUES
(1, 1, 'A+'),
(1, 2, 'B'),
(1, 3, 'A'),
(1, 5, 'D'),
(1, 4, 'C'),
(1, 6, 'B');

-- ---------------------------------------------------------
-- Timetable
-- ---------------------------------------------------------
INSERT INTO timetable (course_id, day_of_week, time_slot) VALUES
(1, 'Monday', '08.00'),
(1, 'Tuesday', '08.00'),
(1, 'Wednesday', '08.00'),
(1, 'Thursday', '08.00'),
(1, 'Friday', '08.00'),
(1, 'Monday', '10.00'),
(1, 'Tuesday', '10.00'),
(1, 'Wednesday', '10.00'),
(1, 'Thursday', '10.00'),
(1, 'Friday', '10.00'),
(5, 'Monday', '01.00'),
(5, 'Wednesday', '01.00'),
(5, 'Thursday', '01.00'),
(5, 'Friday', '01.00'),
(5, 'Monday', '03.00'),
(5, 'Wednesday', '03.00'),
(5, 'Thursday', '03.00'),
(5, 'Friday', '03.00'),
(2, 'Tuesday', '01.00'),
(2, 'Tuesday', '03.00');

-- ---------------------------------------------------------
-- Users (login accounts)
-- Passwords are stored as plain text here for simplicity - hash them
-- in a production system.
-- ---------------------------------------------------------
INSERT INTO users (username, password, role, linked_id) VALUES
('admin', 'admin123', 'ADMIN', NULL),
('kumar.student', 'student123', 'STUDENT', 1),
('kumar.lecturer', 'lecturer123', 'LECTURER', 1);
