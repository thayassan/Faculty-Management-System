package com.faculty.view;

import com.faculty.controller.LecturerController;
import com.faculty.model.Course;
import com.faculty.model.Lecturer;
import com.faculty.model.TimetableEntry;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class LecturerDashboardView extends JFrame {

    private static final String[] DAYS = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday" };

    private final LecturerController controller = new LecturerController();
    private final com.faculty.model.User currentUser;
    private final int lecturerId;

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel contentPanel = new JPanel();

    // Sidebar buttons for state management
    private JButton profileBtn;
    private JButton timetableBtn;
    private JButton coursesBtn;

    // Profile fields
    private JTextField fullNameField;
    private JTextField deptField;
    private JTextField emailField;
    private JTextField mobileField;

    public LecturerDashboardView(com.faculty.model.User user) {
        this.currentUser = user;
        this.lecturerId = user.getLinkedId() != null ? user.getLinkedId() : -1;

        setTitle("Faculty Management System - Lecturer");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 600);
        setMinimumSize(new Dimension(800, 550));
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.add(buildSidebar(), BorderLayout.WEST);

        contentPanel.setLayout(cardLayout);
        contentPanel.setBackground(UIStyle.LIGHT_BG);
        contentPanel.add(buildProfilePanel(), "PROFILE");
        contentPanel.add(buildTimetablePanel(), "TIMETABLE");
        contentPanel.add(buildCoursesPanel(), "COURSES");

        root.add(contentPanel, BorderLayout.CENTER);
        setContentPane(root);

        if (lecturerId == -1) {
            JOptionPane.showMessageDialog(this,
                    "This account is not linked to a lecturer record. Ask an admin to link your account.",
                    "No Linked Profile", JOptionPane.WARNING_MESSAGE);
        } else {
            loadProfile();
        }
        updateSelectedSidebarButton(profileBtn);
    }

    private JPanel buildSidebar() {
        JPanel sidebar = UIStyle.sidebar();

        UserProfileIcon userIcon = new UserProfileIcon();

        JLabel welcome = new JLabel("Welcome, " + currentUser.getUsername());
        welcome.setForeground(Color.WHITE);
        welcome.setFont(UIStyle.FONT_HEADING);
        welcome.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcome.setBorder(new EmptyBorder(10, 0, 20, 0));

        profileBtn = UIStyle.sidebarButton("Profile Details", "USER");
        timetableBtn = UIStyle.sidebarButton("Time table", "CALENDAR");
        coursesBtn = UIStyle.sidebarButton("Teaching Courses", "BOOK");
        JButton logoutBtn = UIStyle.logoutButton();

        profileBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        timetableBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        coursesBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        profileBtn.addActionListener(e -> {
            updateSelectedSidebarButton(profileBtn);
            cardLayout.show(contentPanel, "PROFILE");
        });
        timetableBtn.addActionListener(e -> {
            loadTimetable();
            updateSelectedSidebarButton(timetableBtn);
            cardLayout.show(contentPanel, "TIMETABLE");
        });
        coursesBtn.addActionListener(e -> {
            loadCourses();
            updateSelectedSidebarButton(coursesBtn);
            cardLayout.show(contentPanel, "COURSES");
        });
        logoutBtn.addActionListener(e -> logout());

        sidebar.add(userIcon);
        sidebar.add(welcome);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(profileBtn);
        sidebar.add(Box.createVerticalStrut(6));
        sidebar.add(timetableBtn);
        sidebar.add(Box.createVerticalStrut(6));
        sidebar.add(coursesBtn);
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(logoutBtn);

        return sidebar;
    }

    private void updateSelectedSidebarButton(JButton selectedBtn) {
        UIStyle.setSidebarButtonSelected(profileBtn, profileBtn == selectedBtn, "USER");
        UIStyle.setSidebarButtonSelected(timetableBtn, timetableBtn == selectedBtn, "CALENDAR");
        UIStyle.setSidebarButtonSelected(coursesBtn, coursesBtn == selectedBtn, "BOOK");
    }

    // ---------- Profile ----------

    private JPanel buildProfilePanel() {
        JPanel panel = new JPanel();
        panel.setBackground(UIStyle.LIGHT_BG);
        panel.setBorder(new EmptyBorder(30, 40, 30, 40));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel heading = UIStyle.heading("Profile Details");
        heading.setAlignmentX(Component.CENTER_ALIGNMENT);

        fullNameField = UIStyle.roundedTextField();
        deptField = UIStyle.roundedTextField();
        deptField.setEditable(false);
        emailField = UIStyle.roundedTextField();
        mobileField = UIStyle.roundedTextField();

        JButton saveBtn = UIStyle.primaryButton("Save changes");
        saveBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        saveBtn.addActionListener(e -> saveProfile());

        panel.add(heading);
        panel.add(Box.createVerticalStrut(20));
        panel.add(formRow("Full Name", fullNameField));
        panel.add(formRow("Department", deptField));
        panel.add(formRow("Email", emailField));
        panel.add(formRow("Mobile Number", mobileField));
        panel.add(Box.createVerticalStrut(15));
        panel.add(saveBtn);

        return panel;
    }

    private JPanel formRow(String labelText, JTextField field) {
        JPanel row = new JPanel(new BorderLayout(15, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(600, 40));
        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(140, 25));
        label.setFont(UIStyle.FONT_LABEL);
        label.setForeground(UIStyle.PRIMARY_PURPLE);
        row.add(label, BorderLayout.WEST);
        row.add(field, BorderLayout.CENTER);
        row.setBorder(new EmptyBorder(6, 0, 6, 0));
        return row;
    }

    private void loadProfile() {
        try {
            Lecturer l = controller.getProfile(lecturerId);
            if (l == null) {
                return;
            }
            fullNameField.setText(l.getFullName());
            deptField.setText(l.getDeptName());
            emailField.setText(l.getEmail());
            mobileField.setText(l.getMobile());
        } catch (SQLException ex) {
            showDbError(ex);
        }
    }

    private void saveProfile() {
        try {
            Lecturer l = controller.getProfile(lecturerId);
            if (l == null) {
                return;
            }
            l.setFullName(fullNameField.getText().trim());
            l.setEmail(emailField.getText().trim());
            l.setMobile(mobileField.getText().trim());
            boolean ok = controller.updateProfile(l);
            JOptionPane.showMessageDialog(this,
                    ok ? "Profile updated successfully." : "Update failed.",
                    ok ? "Success" : "Error",
                    ok ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            showDbError(ex);
        }
    }

    // ---------- Timetable ----------

    private JTable timetableTable;
    private DefaultTableModel timetableModel;

    private JPanel buildTimetablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UIStyle.LIGHT_BG);
        panel.setBorder(new EmptyBorder(30, 40, 30, 40));

        JLabel heading = UIStyle.heading("Time table");
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(heading, BorderLayout.CENTER);
        top.setBorder(new EmptyBorder(0, 0, 15, 0));

        String[] columns = new String[6];
        columns[0] = "Time";
        System.arraycopy(DAYS, 0, columns, 1, DAYS.length);
        timetableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        timetableTable = new JTable(timetableModel);
        UIStyle.styleStudentTable(timetableTable);

        JScrollPane scrollPane = new JScrollPane(timetableTable);
        JPanel tableWrap = new JPanel();
        tableWrap.setLayout(new BoxLayout(tableWrap, BoxLayout.Y_AXIS));
        tableWrap.setOpaque(false);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        tableWrap.add(scrollPane);
        tableWrap.add(Box.createVerticalGlue());

        panel.add(top, BorderLayout.NORTH);
        panel.add(tableWrap, BorderLayout.CENTER);
        return panel;
    }

    private void loadTimetable() {
        try {
            List<TimetableEntry> entries = controller.getTimetable(lecturerId);
            timetableModel.setRowCount(0);

            String[] slots = { "08.00", "10.00", "Interval", "01.00", "03.00" };

            for (String slot : slots) {
                Object[] row = new Object[6];
                row[0] = slot;

                if ("Interval".equals(slot)) {
                    for (int i = 1; i <= 5; i++) {
                        row[i] = "Interval";
                    }
                } else {
                    for (int i = 1; i <= 5; i++) {
                        row[i] = "";
                    }
                    for (TimetableEntry t : entries) {
                        if (t.getTimeSlot().equals(slot)) {
                            for (int i = 0; i < DAYS.length; i++) {
                                if (DAYS[i].equalsIgnoreCase(t.getDayOfWeek())) {
                                    String code = t.getCourseCode();
                                    String displayText = "OOP";
                                    if (code != null && code.contains("21032")) {
                                        displayText = "SE";
                                    }
                                    row[i + 1] = displayText;
                                }
                            }
                        }
                    }
                }
                timetableModel.addRow(row);
            }
            UIStyle.adjustTableHeight(timetableTable);
            UIStyle.autoFitColumns(timetableTable, 70, 150);
        } catch (SQLException ex) {
            showDbError(ex);
        }
    }

    // ---------- Teaching Courses ----------

    private DefaultTableModel coursesModel;
    private JTable coursesTable;

    private JPanel buildCoursesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UIStyle.LIGHT_BG);
        panel.setBorder(new EmptyBorder(30, 40, 30, 40));

        JLabel heading = UIStyle.heading("Teaching Courses");
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(heading, BorderLayout.CENTER);
        top.setBorder(new EmptyBorder(0, 0, 15, 0));

        String[] columns = { "Course code", "Course name", "Credits" };
        coursesModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        coursesTable = new JTable(coursesModel);
        UIStyle.styleStudentTable(coursesTable);

        JScrollPane scrollPane = new JScrollPane(coursesTable);
        JPanel tableWrap = new JPanel();
        tableWrap.setLayout(new BoxLayout(tableWrap, BoxLayout.Y_AXIS));
        tableWrap.setOpaque(false);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        tableWrap.add(scrollPane);
        tableWrap.add(Box.createVerticalGlue());

        panel.add(top, BorderLayout.NORTH);
        panel.add(tableWrap, BorderLayout.CENTER);
        return panel;
    }

    private void loadCourses() {
        try {
            List<Course> list = controller.getTeachingCourses(lecturerId);
            coursesModel.setRowCount(0);
            for (Course c : list) {
                coursesModel.addRow(new Object[] { c.getCourseCode(), c.getCourseName(), c.getCredits() });
            }
            UIStyle.adjustTableHeight(coursesTable);
            UIStyle.autoFitColumns(coursesTable, 90, 220);
        } catch (SQLException ex) {
            showDbError(ex);
        }
    }

    // ---------- Common ----------

    private void logout() {
        dispose();
        new LoginView().setVisible(true);
    }

    private void showDbError(SQLException ex) {
        JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(),
                "Database Error", JOptionPane.ERROR_MESSAGE);
    }
}
