package com.faculty.controller;

import com.faculty.dao.CourseDAO;
import com.faculty.dao.DegreeDAO;
import com.faculty.dao.DepartmentDAO;
import com.faculty.dao.LecturerDAO;
import com.faculty.dao.StudentDAO;
import com.faculty.model.Course;
import com.faculty.model.Degree;
import com.faculty.model.Department;
import com.faculty.model.Lecturer;
import com.faculty.model.Student;

import java.sql.SQLException;
import java.util.List;

/**
 * Central controller used by the Admin dashboard. Wraps every DAO the
 * admin panels need and performs basic validation before delegating
 * to the database layer.
 */
public class AdminController {

    private final StudentDAO studentDAO = new StudentDAO();
    private final LecturerDAO lecturerDAO = new LecturerDAO();
    private final CourseDAO courseDAO = new CourseDAO();
    private final DepartmentDAO departmentDAO = new DepartmentDAO();
    private final DegreeDAO degreeDAO = new DegreeDAO();

    // ---------- Students ----------

    public List<Student> getAllStudents() throws SQLException {
        return studentDAO.getAll();
    }

    public boolean addStudent(Student s) throws SQLException {
        validateNotEmpty(s.getFullName(), "Full name");
        validateNotEmpty(s.getStudentNumber(), "Student ID");
        return studentDAO.insert(s);
    }

    public boolean updateStudent(Student s) throws SQLException {
        validateNotEmpty(s.getFullName(), "Full name");
        validateNotEmpty(s.getStudentNumber(), "Student ID");
        return studentDAO.update(s);
    }

    public boolean deleteStudent(int studentId) throws SQLException {
        return studentDAO.delete(studentId);
    }

    // ---------- Lecturers ----------

    public List<Lecturer> getAllLecturers() throws SQLException {
        return lecturerDAO.getAll();
    }

    public boolean addLecturer(Lecturer l) throws SQLException {
        validateNotEmpty(l.getFullName(), "Full name");
        return lecturerDAO.insert(l);
    }

    public boolean updateLecturer(Lecturer l) throws SQLException {
        validateNotEmpty(l.getFullName(), "Full name");
        return lecturerDAO.update(l);
    }

    public boolean deleteLecturer(int lecturerId) throws SQLException {
        return lecturerDAO.delete(lecturerId);
    }

    // ---------- Courses ----------

    public List<Course> getAllCourses() throws SQLException {
        return courseDAO.getAll();
    }

    public boolean addCourse(Course c) throws SQLException {
        validateNotEmpty(c.getCourseCode(), "Course code");
        validateNotEmpty(c.getCourseName(), "Course name");
        return courseDAO.insert(c);
    }

    public boolean updateCourse(Course c) throws SQLException {
        validateNotEmpty(c.getCourseCode(), "Course code");
        validateNotEmpty(c.getCourseName(), "Course name");
        return courseDAO.update(c);
    }

    public boolean deleteCourse(int courseId) throws SQLException {
        return courseDAO.delete(courseId);
    }

    // ---------- Departments ----------

    public List<Department> getAllDepartments() throws SQLException {
        return departmentDAO.getAll();
    }

    public boolean addDepartment(Department d) throws SQLException {
        validateNotEmpty(d.getName(), "Department name");
        return departmentDAO.insert(d);
    }

    public boolean updateDepartment(Department d) throws SQLException {
        validateNotEmpty(d.getName(), "Department name");
        return departmentDAO.update(d);
    }

    public boolean deleteDepartment(int deptId) throws SQLException {
        return departmentDAO.delete(deptId);
    }

    // ---------- Degrees ----------

    public List<Degree> getAllDegrees() throws SQLException {
        return degreeDAO.getAll();
    }

    public boolean addDegree(Degree d) throws SQLException {
        validateNotEmpty(d.getDegreeName(), "Degree name");
        return degreeDAO.insert(d);
    }

    public boolean updateDegree(Degree d) throws SQLException {
        validateNotEmpty(d.getDegreeName(), "Degree name");
        return degreeDAO.update(d);
    }

    public boolean deleteDegree(int degreeId) throws SQLException {
        return degreeDAO.delete(degreeId);
    }

    // ---------- Helpers ----------

    private void validateNotEmpty(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be empty.");
        }
    }
}
