package com.faculty.view;

import com.faculty.controller.AdminController;
import com.faculty.model.Course;
import com.faculty.model.Degree;
import com.faculty.model.Department;
import com.faculty.model.Lecturer;
import com.faculty.model.Student;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

/**
 * Admin dashboard: full CRUD (Add / view / edit / delete) for Students,
 * Lecturers, Courses, Departments and Degrees.
 */
public class AdminDashboardView extends JFrame {

    private final AdminController controller = new AdminController();
    private final com.faculty.model.User currentUser;

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel contentPanel = new JPanel();

    // Sidebar button fields for state management
    private JButton studentsBtn;
    private JButton lecturersBtn;
    private JButton coursesBtn;
    private JButton departmentsBtn;
    private JButton degreesBtn;

    // Table models per entity
    private DefaultTableModel studentsModel;
    private DefaultTableModel lecturersModel;
    private DefaultTableModel coursesModel;
    private DefaultTableModel departmentsModel;
    private DefaultTableModel degreesModel;

    private JTable studentsTable;
    private JTable lecturersTable;
    private JTable coursesTable;
    private JTable departmentsTable;
    private JTable degreesTable;

    // Keep the currently loaded lists so table row index -> entity id mapping is easy
    private List<Student> studentsCache;
    private List<Lecturer> lecturersCache;
    private List<Course> coursesCache;
    private List<Department> departmentsCache;
    private List<Degree> degreesCache;

    public AdminDashboardView(com.faculty.model.User user) {
        this.currentUser = user;

        setTitle("Faculty Management System - Admin");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 640);
        setMinimumSize(new Dimension(900, 560));
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.add(buildSidebar(), BorderLayout.WEST);

        contentPanel.setLayout(cardLayout);
        contentPanel.setBackground(UIStyle.LIGHT_BG);
        contentPanel.add(buildStudentsPanel(), "STUDENTS");
        contentPanel.add(buildLecturersPanel(), "LECTURERS");
        contentPanel.add(buildCoursesPanel(), "COURSES");
        contentPanel.add(buildDepartmentsPanel(), "DEPARTMENTS");
        contentPanel.add(buildDegreesPanel(), "DEGREES");

        root.add(contentPanel, BorderLayout.CENTER);
        setContentPane(root);

        loadStudents();
        updateSelectedSidebarButton(studentsBtn);
    }

    private JPanel buildSidebar() {
        JPanel sidebar = UIStyle.sidebar();

        JLabel welcome = new JLabel("Welcome, " + currentUser.getUsername());
        welcome.setForeground(Color.WHITE);
        welcome.setFont(UIStyle.FONT_HEADING);
        welcome.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcome.setBorder(new EmptyBorder(10, 0, 20, 0));

        studentsBtn = UIStyle.sidebarButton("Students", "USER");
        lecturersBtn = UIStyle.sidebarButton("Lecturers", "USER");
        coursesBtn = UIStyle.sidebarButton("Courses", "BOOK");
        departmentsBtn = UIStyle.sidebarButton("Departments", "BUILDING");
        degreesBtn = UIStyle.sidebarButton("Degrees", "GRAD_CAP");
        JButton logoutBtn = UIStyle.logoutButton();

        studentsBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        lecturersBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        coursesBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        departmentsBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        degreesBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        studentsBtn.addActionListener(e -> {
            loadStudents();
            updateSelectedSidebarButton(studentsBtn);
            cardLayout.show(contentPanel, "STUDENTS");
        });
        lecturersBtn.addActionListener(e -> {
            loadLecturers();
            updateSelectedSidebarButton(lecturersBtn);
            cardLayout.show(contentPanel, "LECTURERS");
        });
        coursesBtn.addActionListener(e -> {
            loadCourses();
            updateSelectedSidebarButton(coursesBtn);
            cardLayout.show(contentPanel, "COURSES");
        });
        departmentsBtn.addActionListener(e -> {
            loadDepartments();
            updateSelectedSidebarButton(departmentsBtn);
            cardLayout.show(contentPanel, "DEPARTMENTS");
        });
        degreesBtn.addActionListener(e -> {
            loadDegrees();
            updateSelectedSidebarButton(degreesBtn);
            cardLayout.show(contentPanel, "DEGREES");
        });
        logoutBtn.addActionListener(e -> logout());

        sidebar.add(welcome);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(studentsBtn);
        sidebar.add(Box.createVerticalStrut(6));
        sidebar.add(lecturersBtn);
        sidebar.add(Box.createVerticalStrut(6));
        sidebar.add(coursesBtn);
        sidebar.add(Box.createVerticalStrut(6));
        sidebar.add(departmentsBtn);
        sidebar.add(Box.createVerticalStrut(6));
        sidebar.add(degreesBtn);
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(logoutBtn);

        return sidebar;
    }

    private void updateSelectedSidebarButton(JButton selectedBtn) {
        UIStyle.setSidebarButtonSelected(studentsBtn, studentsBtn == selectedBtn, "USER");
        UIStyle.setSidebarButtonSelected(lecturersBtn, lecturersBtn == selectedBtn, "USER");
        UIStyle.setSidebarButtonSelected(coursesBtn, coursesBtn == selectedBtn, "BOOK");
        UIStyle.setSidebarButtonSelected(departmentsBtn, departmentsBtn == selectedBtn, "BUILDING");
        UIStyle.setSidebarButtonSelected(degreesBtn, degreesBtn == selectedBtn, "GRAD_CAP");
    }

    private JPanel buildEntityPanelShell(String title, JTable table, Runnable onAdd, Runnable onEdit, Runnable onDelete) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UIStyle.LIGHT_BG);
        panel.setBorder(new EmptyBorder(25, 35, 25, 35));

        JLabel heading = UIStyle.heading(title);
        heading.setHorizontalAlignment(SwingConstants.CENTER);

        JButton addBtn = UIStyle.primaryButton("Add new");
        JButton editBtn = UIStyle.secondaryButton("Edit");
        JButton deleteBtn = UIStyle.secondaryButton("Delete");

        addBtn.addActionListener(e -> onAdd.run());
        editBtn.addActionListener(e -> onEdit.run());
        deleteBtn.addActionListener(e -> onDelete.run());

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        toolbar.setOpaque(false);
        toolbar.add(addBtn);
        toolbar.add(editBtn);
        toolbar.add(deleteBtn);

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(heading, BorderLayout.CENTER);
        top.add(toolbar, BorderLayout.SOUTH);
        top.setBorder(new EmptyBorder(0, 0, 15, 0));

        UIStyle.styleTable(table);
        table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        JButton saveChangesBtn = UIStyle.primaryButton("Save changes");
        saveChangesBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Changes saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        });
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottom.setOpaque(false);
        bottom.setBorder(new EmptyBorder(15, 0, 0, 0));
        bottom.add(saveChangesBtn);

        JScrollPane scrollPane = new JScrollPane(table);
        JPanel tableWrap = new JPanel();
        tableWrap.setLayout(new BoxLayout(tableWrap, BoxLayout.Y_AXIS));
        tableWrap.setOpaque(false);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        tableWrap.add(scrollPane);
        tableWrap.add(Box.createVerticalGlue());

        panel.add(top, BorderLayout.NORTH);
        panel.add(tableWrap, BorderLayout.CENTER);
        panel.add(bottom, BorderLayout.SOUTH);
        return panel;
    }

    // =========================================================
    // STUDENTS
    // =========================================================

    private JPanel buildStudentsPanel() {
        String[] cols = {"Full Name", "Student ID", "Degree", "Email", "Mobile Number"};
        studentsModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        studentsTable = new JTable(studentsModel);
        JPanel panel = buildEntityPanelShell("Students", studentsTable,
                this::addStudent, this::editStudent, this::deleteStudent);
        return panel;
    }

    private void loadStudents() {
        try {
            studentsCache = controller.getAllStudents();
            studentsModel.setRowCount(0);
            for (Student s : studentsCache) {
                studentsModel.addRow(new Object[]{
                        s.getFullName(), s.getStudentNumber(), s.getDegreeName(), s.getEmail(), s.getMobile()
                });
            }
            UIStyle.adjustTableHeight(studentsTable);
            UIStyle.autoFitColumns(studentsTable, 90, 220);
        } catch (SQLException ex) {
            showDbError(ex);
        }
    }

    private void addStudent() {
        List<Degree> degrees;
        try {
            degrees = controller.getAllDegrees();
        } catch (SQLException ex) {
            showDbError(ex);
            return;
        }
        if (degrees.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please add a Degree first.", "No Degrees", JOptionPane.WARNING_MESSAGE);
            return;
        }
        JTextField nameField = new JTextField();
        JTextField idField = new JTextField();
        JComboBox<Degree> degreeCombo = new JComboBox<>(degrees.toArray(new Degree[0]));
        JTextField emailField = new JTextField();
        JTextField mobileField = new JTextField();

        JPanel form = buildForm(
                new String[]{"Full Name", "Student ID", "Degree", "Email", "Mobile Number"},
                new JComponent[]{nameField, idField, degreeCombo, emailField, mobileField});

        int result = JOptionPane.showConfirmDialog(this, form, "Add New Student",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) {
            return;
        }
        try {
            Student s = new Student();
            s.setFullName(nameField.getText().trim());
            s.setStudentNumber(idField.getText().trim());
            s.setDegreeId(((Degree) degreeCombo.getSelectedItem()).getDegreeId());
            s.setEmail(emailField.getText().trim());
            s.setMobile(mobileField.getText().trim());
            controller.addStudent(s);
            loadStudents();
        } catch (IllegalArgumentException iae) {
            JOptionPane.showMessageDialog(this, iae.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            showDbError(ex);
        }
    }

    private void editStudent() {
        int row = studentsTable.getSelectedRow();
        if (row < 0) {
            showSelectFirst();
            return;
        }
        Student existing = studentsCache.get(row);
        List<Degree> degrees;
        try {
            degrees = controller.getAllDegrees();
        } catch (SQLException ex) {
            showDbError(ex);
            return;
        }
        JTextField nameField = new JTextField(existing.getFullName());
        JTextField idField = new JTextField(existing.getStudentNumber());
        JComboBox<Degree> degreeCombo = new JComboBox<>(degrees.toArray(new Degree[0]));
        for (int i = 0; i < degreeCombo.getItemCount(); i++) {
            if (degreeCombo.getItemAt(i).getDegreeId() == existing.getDegreeId()) {
                degreeCombo.setSelectedIndex(i);
                break;
            }
        }
        JTextField emailField = new JTextField(existing.getEmail());
        JTextField mobileField = new JTextField(existing.getMobile());

        JPanel form = buildForm(
                new String[]{"Full Name", "Student ID", "Degree", "Email", "Mobile Number"},
                new JComponent[]{nameField, idField, degreeCombo, emailField, mobileField});

        int result = JOptionPane.showConfirmDialog(this, form, "Edit Student",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) {
            return;
        }
        try {
            existing.setFullName(nameField.getText().trim());
            existing.setStudentNumber(idField.getText().trim());
            existing.setDegreeId(((Degree) degreeCombo.getSelectedItem()).getDegreeId());
            existing.setEmail(emailField.getText().trim());
            existing.setMobile(mobileField.getText().trim());
            controller.updateStudent(existing);
            loadStudents();
        } catch (IllegalArgumentException iae) {
            JOptionPane.showMessageDialog(this, iae.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            showDbError(ex);
        }
    }

    private void deleteStudent() {
        int row = studentsTable.getSelectedRow();
        if (row < 0) {
            showSelectFirst();
            return;
        }
        Student existing = studentsCache.get(row);
        if (!confirmDelete(existing.getFullName())) {
            return;
        }
        try {
            controller.deleteStudent(existing.getStudentId());
            loadStudents();
        } catch (SQLException ex) {
            showDbError(ex);
        }
    }

    // =========================================================
    // LECTURERS
    // =========================================================

    private JPanel buildLecturersPanel() {
        String[] cols = {"Full Name", "Department", "Courses teaching", "Email", "Mobile Number"};
        lecturersModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        lecturersTable = new JTable(lecturersModel);
        JPanel panel = buildEntityPanelShell("Lecturers", lecturersTable,
                this::addLecturer, this::editLecturer, this::deleteLecturer);
        return panel;
    }

    private void loadLecturers() {
        try {
            lecturersCache = controller.getAllLecturers();
            lecturersModel.setRowCount(0);
            for (Lecturer l : lecturersCache) {
                lecturersModel.addRow(new Object[]{
                    l.getFullName(), l.getDeptName(), l.getCoursesTeaching(), l.getEmail(), l.getMobile()
                });
            }
            UIStyle.adjustTableHeight(lecturersTable);
            UIStyle.autoFitColumns(lecturersTable, 90, 280);
        } catch (SQLException ex) {
            showDbError(ex);
        }
    }

    private void addLecturer() {
        List<Department> departments;
        try {
            departments = controller.getAllDepartments();
        } catch (SQLException ex) {
            showDbError(ex);
            return;
        }
        if (departments.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please add a Department first.", "No Departments", JOptionPane.WARNING_MESSAGE);
            return;
        }
        JTextField nameField = new JTextField();
        JComboBox<Department> deptCombo = new JComboBox<>(departments.toArray(new Department[0]));
        JTextField emailField = new JTextField();
        JTextField mobileField = new JTextField();

        JPanel form = buildForm(
                new String[]{"Full Name", "Department", "Email", "Mobile Number"},
                new JComponent[]{nameField, deptCombo, emailField, mobileField});

        int result = JOptionPane.showConfirmDialog(this, form, "Add New Lecturer",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) {
            return;
        }
        try {
            Lecturer l = new Lecturer();
            l.setFullName(nameField.getText().trim());
            l.setDeptId(((Department) deptCombo.getSelectedItem()).getDeptId());
            l.setEmail(emailField.getText().trim());
            l.setMobile(mobileField.getText().trim());
            controller.addLecturer(l);
            loadLecturers();
        } catch (IllegalArgumentException iae) {
            JOptionPane.showMessageDialog(this, iae.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            showDbError(ex);
        }
    }

    private void editLecturer() {
        int row = lecturersTable.getSelectedRow();
        if (row < 0) {
            showSelectFirst();
            return;
        }
        Lecturer existing = lecturersCache.get(row);
        List<Department> departments;
        try {
            departments = controller.getAllDepartments();
        } catch (SQLException ex) {
            showDbError(ex);
            return;
        }
        JTextField nameField = new JTextField(existing.getFullName());
        JComboBox<Department> deptCombo = new JComboBox<>(departments.toArray(new Department[0]));
        for (int i = 0; i < deptCombo.getItemCount(); i++) {
            if (deptCombo.getItemAt(i).getDeptId() == existing.getDeptId()) {
                deptCombo.setSelectedIndex(i);
                break;
            }
        }
        JTextField emailField = new JTextField(existing.getEmail());
        JTextField mobileField = new JTextField(existing.getMobile());

        JPanel form = buildForm(
                new String[]{"Full Name", "Department", "Email", "Mobile Number"},
                new JComponent[]{nameField, deptCombo, emailField, mobileField});

        int result = JOptionPane.showConfirmDialog(this, form, "Edit Lecturer",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) {
            return;
        }
        try {
            existing.setFullName(nameField.getText().trim());
            existing.setDeptId(((Department) deptCombo.getSelectedItem()).getDeptId());
            existing.setEmail(emailField.getText().trim());
            existing.setMobile(mobileField.getText().trim());
            controller.updateLecturer(existing);
            loadLecturers();
        } catch (IllegalArgumentException iae) {
            JOptionPane.showMessageDialog(this, iae.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            showDbError(ex);
        }
    }

    private void deleteLecturer() {
        int row = lecturersTable.getSelectedRow();
        if (row < 0) {
            showSelectFirst();
            return;
        }
        Lecturer existing = lecturersCache.get(row);
        if (!confirmDelete(existing.getFullName())) {
            return;
        }
        try {
            controller.deleteLecturer(existing.getLecturerId());
            loadLecturers();
        } catch (SQLException ex) {
            showDbError(ex);
        }
    }

    // =========================================================
    // COURSES
    // =========================================================

    private JPanel buildCoursesPanel() {
        String[] cols = {"Course code", "Course name", "Credits", "Lecturer"};
        coursesModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        coursesTable = new JTable(coursesModel);
        JPanel panel = buildEntityPanelShell("Courses", coursesTable,
                this::addCourse, this::editCourse, this::deleteCourse);
        return panel;
    }

    private void loadCourses() {
        try {
            coursesCache = controller.getAllCourses();
            coursesModel.setRowCount(0);
            for (Course c : coursesCache) {
                coursesModel.addRow(new Object[]{
                        c.getCourseCode(), c.getCourseName(), c.getCredits(),
                        c.getLecturerName() == null ? "Unassigned" : c.getLecturerName()
                });
            }
            UIStyle.adjustTableHeight(coursesTable);
            UIStyle.autoFitColumns(coursesTable, 90, 280);
        } catch (SQLException ex) {
            showDbError(ex);
        }
    }

    private void addCourse() {
        List<Lecturer> lecturers;
        try {
            lecturers = controller.getAllLecturers();
        } catch (SQLException ex) {
            showDbError(ex);
            return;
        }
        JTextField codeField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField creditsField = new JTextField();
        JComboBox<Object> lecturerCombo = new JComboBox<>();
        lecturerCombo.addItem("Unassigned");
        for (Lecturer l : lecturers) {
            lecturerCombo.addItem(l);
        }

        JPanel form = buildForm(
                new String[]{"Course code", "Course name", "Credits", "Lecturer"},
                new JComponent[]{codeField, nameField, creditsField, lecturerCombo});

        int result = JOptionPane.showConfirmDialog(this, form, "Add New Course",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) {
            return;
        }
        try {
            Course c = new Course();
            c.setCourseCode(codeField.getText().trim());
            c.setCourseName(nameField.getText().trim());
            c.setCredits(parseIntOrZero(creditsField.getText()));
            Object selected = lecturerCombo.getSelectedItem();
            c.setLecturerId(selected instanceof Lecturer ? ((Lecturer) selected).getLecturerId() : null);
            controller.addCourse(c);
            loadCourses();
        } catch (IllegalArgumentException iae) {
            JOptionPane.showMessageDialog(this, iae.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            showDbError(ex);
        }
    }

    private void editCourse() {
        int row = coursesTable.getSelectedRow();
        if (row < 0) {
            showSelectFirst();
            return;
        }
        Course existing = coursesCache.get(row);
        List<Lecturer> lecturers;
        try {
            lecturers = controller.getAllLecturers();
        } catch (SQLException ex) {
            showDbError(ex);
            return;
        }
        JTextField codeField = new JTextField(existing.getCourseCode());
        JTextField nameField = new JTextField(existing.getCourseName());
        JTextField creditsField = new JTextField(String.valueOf(existing.getCredits()));
        JComboBox<Object> lecturerCombo = new JComboBox<>();
        lecturerCombo.addItem("Unassigned");
        for (Lecturer l : lecturers) {
            lecturerCombo.addItem(l);
        }
        if (existing.getLecturerId() != null) {
            for (int i = 0; i < lecturerCombo.getItemCount(); i++) {
                Object item = lecturerCombo.getItemAt(i);
                if (item instanceof Lecturer && ((Lecturer) item).getLecturerId() == existing.getLecturerId()) {
                    lecturerCombo.setSelectedIndex(i);
                    break;
                }
            }
        }

        JPanel form = buildForm(
                new String[]{"Course code", "Course name", "Credits", "Lecturer"},
                new JComponent[]{codeField, nameField, creditsField, lecturerCombo});

        int result = JOptionPane.showConfirmDialog(this, form, "Edit Course",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) {
            return;
        }
        try {
            existing.setCourseCode(codeField.getText().trim());
            existing.setCourseName(nameField.getText().trim());
            existing.setCredits(parseIntOrZero(creditsField.getText()));
            Object selected = lecturerCombo.getSelectedItem();
            existing.setLecturerId(selected instanceof Lecturer ? ((Lecturer) selected).getLecturerId() : null);
            controller.updateCourse(existing);
            loadCourses();
        } catch (IllegalArgumentException iae) {
            JOptionPane.showMessageDialog(this, iae.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            showDbError(ex);
        }
    }

    private void deleteCourse() {
        int row = coursesTable.getSelectedRow();
        if (row < 0) {
            showSelectFirst();
            return;
        }
        Course existing = coursesCache.get(row);
        if (!confirmDelete(existing.getCourseCode())) {
            return;
        }
        try {
            controller.deleteCourse(existing.getCourseId());
            loadCourses();
        } catch (SQLException ex) {
            showDbError(ex);
        }
    }

    // =========================================================
    // DEPARTMENTS
    // =========================================================

    private JPanel buildDepartmentsPanel() {
        String[] cols = {"Name", "HOD", "Degree", "No of Staff"};
        departmentsModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        departmentsTable = new JTable(departmentsModel);
        JPanel panel = buildEntityPanelShell("Departments", departmentsTable,
                this::addDepartment, this::editDepartment, this::deleteDepartment);
        return panel;
    }

    private void loadDepartments() {
        try {
            departmentsCache = controller.getAllDepartments();
            departmentsModel.setRowCount(0);
            for (Department d : departmentsCache) {
                departmentsModel.addRow(new Object[]{d.getName(), d.getHodName(), d.getDegreeName(), d.getStaffCount()});
            }
            UIStyle.adjustTableHeight(departmentsTable);
            UIStyle.autoFitColumns(departmentsTable, 90, 220);
        } catch (SQLException ex) {
            showDbError(ex);
        }
    }

    private void addDepartment() {
        JTextField nameField = new JTextField();
        JTextField hodField = new JTextField();
        JTextField degreeField = new JTextField();
        JTextField staffField = new JTextField();

        JPanel form = buildForm(
                new String[]{"Name", "HOD", "Degree", "No of Staff"},
                new JComponent[]{nameField, hodField, degreeField, staffField});

        int result = JOptionPane.showConfirmDialog(this, form, "Add New Department",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) {
            return;
        }
        try {
            Department d = new Department();
            d.setName(nameField.getText().trim());
            d.setHodName(hodField.getText().trim());
            d.setDegreeName(degreeField.getText().trim());
            d.setStaffCount(parseIntOrZero(staffField.getText()));
            controller.addDepartment(d);
            loadDepartments();
        } catch (IllegalArgumentException iae) {
            JOptionPane.showMessageDialog(this, iae.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            showDbError(ex);
        }
    }

    private void editDepartment() {
        int row = departmentsTable.getSelectedRow();
        if (row < 0) {
            showSelectFirst();
            return;
        }
        Department existing = departmentsCache.get(row);
        JTextField nameField = new JTextField(existing.getName());
        JTextField hodField = new JTextField(existing.getHodName());
        JTextField degreeField = new JTextField(existing.getDegreeName());
        JTextField staffField = new JTextField(String.valueOf(existing.getStaffCount()));

        JPanel form = buildForm(
                new String[]{"Name", "HOD", "Degree", "No of Staff"},
                new JComponent[]{nameField, hodField, degreeField, staffField});

        int result = JOptionPane.showConfirmDialog(this, form, "Edit Department",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) {
            return;
        }
        try {
            existing.setName(nameField.getText().trim());
            existing.setHodName(hodField.getText().trim());
            existing.setDegreeName(degreeField.getText().trim());
            existing.setStaffCount(parseIntOrZero(staffField.getText()));
            controller.updateDepartment(existing);
            loadDepartments();
        } catch (IllegalArgumentException iae) {
            JOptionPane.showMessageDialog(this, iae.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            showDbError(ex);
        }
    }

    private void deleteDepartment() {
        int row = departmentsTable.getSelectedRow();
        if (row < 0) {
            showSelectFirst();
            return;
        }
        Department existing = departmentsCache.get(row);
        if (!confirmDelete(existing.getName())) {
            return;
        }
        try {
            controller.deleteDepartment(existing.getDeptId());
            loadDepartments();
        } catch (SQLException ex) {
            showDbError(ex);
        }
    }

    // =========================================================
    // DEGREES
    // =========================================================

    private JPanel buildDegreesPanel() {
        String[] cols = {"Degree", "Department", "No of Students"};
        degreesModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        degreesTable = new JTable(degreesModel);
        JPanel panel = buildEntityPanelShell("Degrees", degreesTable,
                this::addDegree, this::editDegree, this::deleteDegree);
        return panel;
    }

    private void loadDegrees() {
        try {
            degreesCache = controller.getAllDegrees();
            degreesModel.setRowCount(0);
            for (Degree d : degreesCache) {
                degreesModel.addRow(new Object[]{d.getDegreeName(), d.getDeptName(), d.getStudentCount()});
            }
            UIStyle.adjustTableHeight(degreesTable);
            UIStyle.autoFitColumns(degreesTable, 90, 220);
        } catch (SQLException ex) {
            showDbError(ex);
        }
    }

    private void addDegree() {
        List<Department> departments;
        try {
            departments = controller.getAllDepartments();
        } catch (SQLException ex) {
            showDbError(ex);
            return;
        }
        if (departments.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please add a Department first.", "No Departments", JOptionPane.WARNING_MESSAGE);
            return;
        }
        JTextField nameField = new JTextField();
        JComboBox<Department> deptCombo = new JComboBox<>(departments.toArray(new Department[0]));
        JTextField studentCountField = new JTextField();

        JPanel form = buildForm(
                new String[]{"Degree", "Department", "No of Students"},
                new JComponent[]{nameField, deptCombo, studentCountField});

        int result = JOptionPane.showConfirmDialog(this, form, "Add New Degree",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) {
            return;
        }
        try {
            Degree d = new Degree();
            d.setDegreeName(nameField.getText().trim());
            d.setDeptId(((Department) deptCombo.getSelectedItem()).getDeptId());
            d.setStudentCount(parseIntOrZero(studentCountField.getText()));
            controller.addDegree(d);
            loadDegrees();
        } catch (IllegalArgumentException iae) {
            JOptionPane.showMessageDialog(this, iae.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            showDbError(ex);
        }
    }

    private void editDegree() {
        int row = degreesTable.getSelectedRow();
        if (row < 0) {
            showSelectFirst();
            return;
        }
        Degree existing = degreesCache.get(row);
        List<Department> departments;
        try {
            departments = controller.getAllDepartments();
        } catch (SQLException ex) {
            showDbError(ex);
            return;
        }
        JTextField nameField = new JTextField(existing.getDegreeName());
        JComboBox<Department> deptCombo = new JComboBox<>(departments.toArray(new Department[0]));
        for (int i = 0; i < deptCombo.getItemCount(); i++) {
            if (deptCombo.getItemAt(i).getDeptId() == existing.getDeptId()) {
                deptCombo.setSelectedIndex(i);
                break;
            }
        }
        JTextField studentCountField = new JTextField(String.valueOf(existing.getStudentCount()));

        JPanel form = buildForm(
                new String[]{"Degree", "Department", "No of Students"},
                new JComponent[]{nameField, deptCombo, studentCountField});

        int result = JOptionPane.showConfirmDialog(this, form, "Edit Degree",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) {
            return;
        }
        try {
            existing.setDegreeName(nameField.getText().trim());
            existing.setDeptId(((Department) deptCombo.getSelectedItem()).getDeptId());
            existing.setStudentCount(parseIntOrZero(studentCountField.getText()));
            controller.updateDegree(existing);
            loadDegrees();
        } catch (IllegalArgumentException iae) {
            JOptionPane.showMessageDialog(this, iae.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            showDbError(ex);
        }
    }

    private void deleteDegree() {
        int row = degreesTable.getSelectedRow();
        if (row < 0) {
            showSelectFirst();
            return;
        }
        Degree existing = degreesCache.get(row);
        if (!confirmDelete(existing.getDegreeName())) {
            return;
        }
        try {
            controller.deleteDegree(existing.getDegreeId());
            loadDegrees();
        } catch (SQLException ex) {
            showDbError(ex);
        }
    }

    // =========================================================
    // Shared helpers
    // =========================================================

    private JPanel buildForm(String[] labels, JComponent[] fields) {
        JPanel form = new JPanel(new GridLayout(labels.length, 2, 10, 10));
        form.setBorder(new EmptyBorder(10, 10, 10, 10));
        for (int i = 0; i < labels.length; i++) {
            JLabel l = new JLabel(labels[i]);
            l.setFont(UIStyle.FONT_LABEL);
            form.add(l);
            form.add(fields[i]);
        }
        return form;
    }

    private int parseIntOrZero(String text) {
        try {
            return Integer.parseInt(text.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private boolean confirmDelete(String name) {
        int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete \"" + name + "\"? This cannot be undone.",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        return choice == JOptionPane.YES_OPTION;
    }

    private void showSelectFirst() {
        JOptionPane.showMessageDialog(this, "Please select a row first.", "No Selection", JOptionPane.WARNING_MESSAGE);
    }

    private void logout() {
        dispose();
        new LoginView().setVisible(true);
    }

    private void showDbError(SQLException ex) {
        JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(),
                "Database Error", JOptionPane.ERROR_MESSAGE);
    }
}
