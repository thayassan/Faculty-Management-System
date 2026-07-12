package com.faculty.view;

import com.faculty.controller.LoginController;
import com.faculty.model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.SQLException;

/**
 * Landing screen of the application. Lets a user sign in (or sign up
 * for a new account) as Admin, Student or Lecturer, then routes them
 * to the correct dashboard.
 */
public class LoginView extends JFrame {

    private final LoginController loginController = new LoginController();

    // Sign in fields
    private JTextField signInUsername;
    private JPasswordField signInPassword;
    private ButtonGroup signInRoleGroup;

    // Sign up fields
    private JTextField signUpUsername;
    private JPasswordField signUpPassword;
    private JPasswordField signUpConfirmPassword;
    private ButtonGroup signUpRoleGroup;

    public LoginView() {
        setTitle("Faculty Management System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 620);
        setMinimumSize(new Dimension(1000, 620));
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());

        // Left brand panel matching HTML .brand
        JPanel leftPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(0x7B5AF0));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        leftPanel.setOpaque(false);
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setPreferredSize(new Dimension(430, 620));
        leftPanel.setBorder(new EmptyBorder(60, 40, 60, 40));

        JLabel capLabel = new JLabel(new VectorIcon("GRAD_CAP", 90, Color.WHITE));
        capLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel("<html><center>Faculty Management<br>System</center></html>");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 30));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel facultyLabel = new JLabel("Faculty of Computing & Technology");
        facultyLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        facultyLabel.setForeground(Color.WHITE);
        facultyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel taglineLabel = new JLabel("Manage your academic journey");
        taglineLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        taglineLabel.setForeground(new Color(0xE3DDFB));
        taglineLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        leftPanel.add(Box.createVerticalGlue());
        leftPanel.add(capLabel);
        leftPanel.add(Box.createVerticalStrut(25));
        leftPanel.add(titleLabel);
        leftPanel.add(Box.createVerticalStrut(60));
        leftPanel.add(facultyLabel);
        leftPanel.add(Box.createVerticalStrut(8));
        leftPanel.add(taglineLabel);
        leftPanel.add(Box.createVerticalGlue());

        // Right form panel matching HTML .right
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(UIStyle.WHITE);
        rightPanel.setBorder(new EmptyBorder(35, 60, 35, 60));

        JPanel cardPanel = new JPanel(new CardLayout());
        cardPanel.setOpaque(false);
        cardPanel.add(buildSignInPanel(), "SIGN_IN");
        cardPanel.add(buildSignUpPanel(), "SIGN_UP");

        // Custom tab bar panel matching HTML .tabs and .tab
        JPanel tabBarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 28, 0));
        tabBarPanel.setOpaque(false);
        tabBarPanel.setBorder(new EmptyBorder(0, 0, 30, 0));

        TabButton signInTab = new TabButton("Sign In", true);
        TabButton signUpTab = new TabButton("Sign Up", false);

        tabBarPanel.add(signInTab);
        tabBarPanel.add(signUpTab);

        CardLayout rightLayout = (CardLayout) cardPanel.getLayout();
        signInTab.addActionListener(e -> {
            signInTab.setSelected(true);
            signUpTab.setSelected(false);
            rightLayout.show(cardPanel, "SIGN_IN");
        });
        signUpTab.addActionListener(e -> {
            signInTab.setSelected(false);
            signUpTab.setSelected(true);
            rightLayout.show(cardPanel, "SIGN_UP");
        });

        rightPanel.add(tabBarPanel, BorderLayout.NORTH);
        rightPanel.add(cardPanel, BorderLayout.CENTER);

        root.add(leftPanel, BorderLayout.WEST);
        root.add(rightPanel, BorderLayout.CENTER);

        setContentPane(root);
    }

    private JPanel buildSignInPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        signInUsername = UIStyle.roundedTextField();
        signInPassword = UIStyle.roundedPasswordField();

        JPanel rolePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        rolePanel.setOpaque(false);
        rolePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        rolePanel.setMaximumSize(new Dimension(450, 44));

        signInRoleGroup = new ButtonGroup();
        JToggleButton adminBtn = roleToggle("Admin", true);
        JToggleButton studentBtn = roleToggle("Student", false);
        JToggleButton lecturerBtn = roleToggle("Lecturer", false);
        signInRoleGroup.add(adminBtn);
        signInRoleGroup.add(studentBtn);
        signInRoleGroup.add(lecturerBtn);

        rolePanel.add(adminBtn);
        rolePanel.add(Box.createHorizontalStrut(10));
        rolePanel.add(studentBtn);
        rolePanel.add(Box.createHorizontalStrut(10));
        rolePanel.add(lecturerBtn);

        JButton signInBtn = UIStyle.primaryButton("Sign In");
        signInBtn.setPreferredSize(new Dimension(450, 52));
        signInBtn.setMaximumSize(new Dimension(450, 52));
        signInBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        signInBtn.addActionListener(e -> doSignIn());

        panel.add(fieldBlock("Username", signInUsername));
        panel.add(Box.createVerticalStrut(20));

        panel.add(fieldBlock("Password", signInPassword));
        panel.add(Box.createVerticalStrut(20));

        panel.add(label("Role"));
        panel.add(Box.createVerticalStrut(8));
        panel.add(rolePanel);
        panel.add(Box.createVerticalStrut(28));

        panel.add(signInBtn);

        signInPassword.addActionListener(e -> doSignIn());

        return panel;
    }

    private JPanel buildSignUpPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        signUpUsername = UIStyle.roundedTextField();
        signUpPassword = UIStyle.roundedPasswordField();
        signUpConfirmPassword = UIStyle.roundedPasswordField();

        JPanel rolePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        rolePanel.setOpaque(false);
        rolePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        rolePanel.setMaximumSize(new Dimension(450, 44));

        signUpRoleGroup = new ButtonGroup();
        JToggleButton adminBtn = roleToggle("Admin", true);
        JToggleButton studentBtn = roleToggle("Student", false);
        JToggleButton lecturerBtn = roleToggle("Lecturer", false);
        signUpRoleGroup.add(adminBtn);
        signUpRoleGroup.add(studentBtn);
        signUpRoleGroup.add(lecturerBtn);

        rolePanel.add(adminBtn);
        rolePanel.add(Box.createHorizontalStrut(10));
        rolePanel.add(studentBtn);
        rolePanel.add(Box.createHorizontalStrut(10));
        rolePanel.add(lecturerBtn);

        JButton signUpBtn = UIStyle.primaryButton("Sign Up");
        signUpBtn.setPreferredSize(new Dimension(450, 52));
        signUpBtn.setMaximumSize(new Dimension(450, 52));
        signUpBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        signUpBtn.addActionListener(e -> doSignUp());

        panel.add(fieldBlock("Username", signUpUsername));
        panel.add(Box.createVerticalStrut(20));

        panel.add(fieldBlock("Password", signUpPassword));
        panel.add(Box.createVerticalStrut(20));

        panel.add(fieldBlock("Confirm Password", signUpConfirmPassword));
        panel.add(Box.createVerticalStrut(20));

        panel.add(label("Role"));
        panel.add(Box.createVerticalStrut(8));
        panel.add(rolePanel);
        panel.add(Box.createVerticalStrut(28));

        panel.add(signUpBtn);

        return panel;
    }

    private JToggleButton roleToggle(String text, boolean selected) {
        return new RoleToggleButton(text, selected);
    }

    private JPanel fieldBlock(String labelText, JTextField field) {
        JPanel block = new JPanel();
        block.setOpaque(false);
        block.setLayout(new BoxLayout(block, BoxLayout.Y_AXIS));
        block.setAlignmentX(Component.LEFT_ALIGNMENT);
        block.setMaximumSize(new Dimension(450, 74));
        JLabel lbl = label(labelText);
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        block.add(lbl);
        block.add(Box.createVerticalStrut(6));
        block.add(field);
        return block;
    }

    private JLabel label(String text) {
        JLabel l = new JLabel(text);
        l.setFont(UIStyle.FONT_LABEL);
        l.setForeground(UIStyle.PRIMARY_PURPLE);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private String selectedRole(ButtonGroup group) {
        for (java.util.Enumeration<AbstractButton> e = group.getElements(); e.hasMoreElements(); ) {
            AbstractButton b = e.nextElement();
            if (b.isSelected()) {
                return b.getText().toUpperCase();
            }
        }
        return "ADMIN";
    }

    private void doSignIn() {
        String username = signInUsername.getText().trim();
        String password = new String(signInPassword.getPassword());
        String role = selectedRole(signInRoleGroup);

        try {
            User user = loginController.login(username, password, role);
            if (user == null) {
                JOptionPane.showMessageDialog(this, "Invalid username, password, or role.",
                        "Login Failed", JOptionPane.ERROR_MESSAGE);
                return;
            }
            routeToDashboard(user);
        } catch (SQLException ex) {
            showDbError(ex);
        }
    }

    private void doSignUp() {
        String username = signUpUsername.getText().trim();
        String password = new String(signUpPassword.getPassword());
        String confirm = new String(signUpConfirmPassword.getPassword());
        String role = selectedRole(signUpRoleGroup);

        if (!password.equals(confirm)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.", "Sign Up Failed", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            boolean ok = loginController.signUp(username, password, role, null);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Account created! You can now sign in.",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Could not create account. Username may already be taken.",
                        "Sign Up Failed", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            showDbError(ex);
        }
    }

    private void routeToDashboard(User user) {
        dispose();
        switch (user.getRole()) {
            case "ADMIN":
                new AdminDashboardView(user).setVisible(true);
                break;
            case "STUDENT":
                new StudentDashboardView(user).setVisible(true);
                break;
            case "LECTURER":
                new LecturerDashboardView(user).setVisible(true);
                break;
            default:
                JOptionPane.showMessageDialog(null, "Unknown role: " + user.getRole());
        }
    }

    private void showDbError(SQLException ex) {
        JOptionPane.showMessageDialog(this,
                "Database error: " + ex.getMessage()
                        + "\n\nCheck that MySQL is running and DBConnection settings are correct.",
                "Database Error", JOptionPane.ERROR_MESSAGE);
    }
}

class TabButton extends JButton {
    private boolean selected;

    public TabButton(String text, boolean selected) {
        super(text);
        this.selected = selected;
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setFont(new Font("SansSerif", Font.BOLD, 22));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setBorder(new EmptyBorder(5, 0, 8, 0));
        updateStyle();
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        updateStyle();
        repaint();
    }

    private void updateStyle() {
        if (selected) {
            setForeground(UIStyle.PRIMARY_PURPLE);
        } else {
            setForeground(new Color(0xC7C3D9));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (selected) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(UIStyle.PRIMARY_PURPLE);
            g2.fillRect(0, getHeight() - 3, getWidth(), 3);
            g2.dispose();
        }
    }
}

class RoleToggleButton extends JToggleButton {
    public RoleToggleButton(String text, boolean selected) {
        super(text, selected);
        setPreferredSize(new Dimension(143, 44));
        setMaximumSize(new Dimension(143, 44));
        setMinimumSize(new Dimension(143, 44));
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Draw background
        if (isSelected()) {
            g2.setColor(UIStyle.PRIMARY_PURPLE);
        } else {
            g2.setColor(UIStyle.LIGHT_GRAY);
        }
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);

        // Draw text centered
        g2.setFont(new Font("SansSerif", Font.BOLD, 14));
        if (isSelected()) {
            g2.setColor(Color.WHITE);
        } else {
            g2.setColor(UIStyle.TEXT_GRAY);
        }

        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(getText());
        int textHeight = fm.getAscent();
        int x = (getWidth() - textWidth) / 2;
        int y = (getHeight() + textHeight) / 2 - 2;

        g2.drawString(getText(), x, y);
        g2.dispose();
    }
}
