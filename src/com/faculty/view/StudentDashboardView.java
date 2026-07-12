package com.faculty.view;

import com.faculty.controller.StudentController;
import com.faculty.dao.StudentDAO;
import com.faculty.model.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

/**
 * Dashboard shown after a STUDENT-role login. Sidebar navigation switches
 * between Profile Details, Time table and Courses Enrolled, matching the
 * assignment wireframes.
 *
 * NOTE: Colors are defined locally for now. Once Kathiravel's UIStyle.java
 * (shared theme) is merged, the color/font constants below should be
 * replaced with UIStyle.* references so every view looks consistent.
 */
public class StudentDashboardView extends JFrame {

    private static final Color SIDEBAR_COLOR = new Color(89, 55, 199);
    private static final Color ACCENT_COLOR = new Color(124, 77, 255);

    private final StudentController controller = new StudentController();
    private final int studentId;

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel contentPanel = new JPanel(cardLayout);

    private JTextField fullNameField;
    private JTextField studentNumberField;
    private JTextField degreeField;
    private JTextField emailField;
    private JTextField mobileField;

    private DefaultTableModel timetableModel;
    private DefaultTableModel coursesModel;

    public StudentDashboardView(int studentId) {
        this.studentId = studentId;

        setTitle("Faculty Management System - Student Dashboard");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(buildSidebar(), BorderLayout.WEST);

        contentPanel.add(buildProfilePanel(), "PROFILE");
        contentPanel.add(buildTimetablePanel(), "TIMETABLE");
        contentPanel.add(buildCoursesPanel(), "COURSES");
        add(contentPanel, BorderLayout.CENTER);

        loadProfile();
        loadTimetable();
        loadEnrollments();

        cardLayout.show(contentPanel, "PROFILE");
    }

    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBackground(SIDEBAR_COLOR);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        JLabel welcome = new JLabel("Welcome, Student");
        welcome.setForeground(Color.WHITE);
        welcome.setFont(new Font("SansSerif", Font.BOLD, 18));
        welcome.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(welcome);
        sidebar.add(Box.createVerticalStrut(20));

        sidebar.add(sidebarButton("Profile Details", () -> cardLayout.show(contentPanel, "PROFILE")));
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(sidebarButton("Time table", () -> cardLayout.show(contentPanel, "TIMETABLE")));
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(sidebarButton("Courses Enrolled", () -> cardLayout.show(contentPanel, "COURSES")));

        sidebar.add(Box.createVerticalGlue());

        JButton logoutButton = new JButton("Log out");
        logoutButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        logoutButton.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> {
                try {
                    Class<?> loginViewClass = Class.forName("com.faculty.view.LoginView");
                    JFrame login = (JFrame) loginViewClass.getDeclaredConstructor().newInstance();
                    login.setVisible(true);
                } catch (Exception ignored) {
                    // LoginView should exist (Thayassan's module); ignore if not on classpath yet
                }
            });
        });
        sidebar.add(logoutButton);

        return sidebar;
    }

    private JButton sidebarButton(String label, Runnable onClick) {
        JButton button = new JButton(label);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(190, 36));
        button.setFocusPainted(false);
        button.addActionListener(e -> onClick.run());
        return button;
    }

    private JPanel buildProfilePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Profile Details");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setForeground(ACCENT_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(title, gbc);
        gbc.gridwidth = 1;

        fullNameField = new JTextField(20);
        studentNumberField = new JTextField(20);
        studentNumberField.setEditable(false); // student number is immutable
        degreeField = new JTextField(20);
        emailField = new JTextField(20);
        mobileField = new JTextField(20);

        addFormRow(panel, gbc, 1, "Full Name", fullNameField);
        addFormRow(panel, gbc, 2, "Student ID", studentNumberField);
        addFormRow(panel, gbc, 3, "Degree", degreeField);
        addFormRow(panel, gbc, 4, "Email", emailField);
        addFormRow(panel, gbc, 5, "Mobile Number", mobileField);

        JButton saveButton = new JButton("Save changes");
        saveButton.setBackground(ACCENT_COLOR);
        saveButton.setForeground(Color.WHITE);
        saveButton.addActionListener(e -> saveProfile());
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        panel.add(saveButton, gbc);

        return panel;
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, String label, JTextField field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private JPanel buildTimetablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JLabel title = new JLabel("Time table");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setForeground(ACCENT_COLOR);
        panel.add(title, BorderLayout.NORTH);

        timetableModel = new DefaultTableModel(new Object[]{"Day", "Time", "Course"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(timetableModel);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        return panel;
    }

    private JPanel buildCoursesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JLabel title = new JLabel("Courses Enrolled");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setForeground(ACCENT_COLOR);
        panel.add(title, BorderLayout.NORTH);

        coursesModel = new DefaultTableModel(new Object[]{"Course code", "Course name", "Credits", "Grade"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(coursesModel);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        return panel;
    }

    private void loadProfile() {
        try {
            Student student = controller.getProfile(studentId);
            if (student == null) {
                showError("Could not find your student profile.");
                return;
            }
            fullNameField.setText(student.getFullName());
            studentNumberField.setText(student.getStudentNumber());
            degreeField.setText(student.getDegreeName() != null ? student.getDegreeName() : "");
            emailField.setText(student.getEmail());
            mobileField.setText(student.getMobile());
        } catch (SQLException e) {
            showError("Database error while loading profile: " + e.getMessage());
        }
    }

    private void saveProfile() {
        try {
            Student student = controller.getProfile(studentId);
            if (student == null) {
                showError("Could not find your student profile.");
                return;
            }
            student.setFullName(fullNameField.getText().trim());
            student.setEmail(emailField.getText().trim());
            student.setMobile(mobileField.getText().trim());
            // Degree field is display-only here; changing degree_id would need a
            // dropdown of degrees (out of scope for this view - degrees are
            // managed by the Admin module).

            String error = controller.saveProfile(student);
            if (error != null) {
                showError(error);
            } else {
                JOptionPane.showMessageDialog(this, "Profile updated successfully.");
            }
        } catch (SQLException e) {
            showError("Database error while saving profile: " + e.getMessage());
        }
    }

    private void loadTimetable() {
        try {
            List<StudentDAO.TimetableSlot> slots = controller.getTimetable(studentId);
            timetableModel.setRowCount(0);
            for (StudentDAO.TimetableSlot slot : slots) {
                timetableModel.addRow(new Object[]{slot.dayOfWeek, slot.timeSlot, slot.courseCode});
            }
        } catch (SQLException e) {
            showError("Database error while loading timetable: " + e.getMessage());
        }
    }

    private void loadEnrollments() {
        try {
            List<StudentDAO.CourseGradeRow> rows = controller.getEnrollments(studentId);
            coursesModel.setRowCount(0);
            for (StudentDAO.CourseGradeRow row : rows) {
                coursesModel.addRow(new Object[]{row.courseCode, row.courseName, row.credits, row.grade});
            }
        } catch (SQLException e) {
            showError("Database error while loading enrolled courses: " + e.getMessage());
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
