package com.faculty.view;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Shared look-and-feel helpers so every screen has a consistent
 * purple "Faculty Management System" theme, matching the reference
 * mockups in the assignment brief.
 */
public final class UIStyle {

    public static final Color PRIMARY_PURPLE = new Color(0x7B5AF0);
    public static final Color LIGHT_BG = new Color(0xF5F4FB);
    public static final Color LIGHT_GRAY = new Color(0xEDEAFB);
    public static final Color TEXT_GRAY = new Color(0x8A85A6);
    public static final Color WHITE = Color.WHITE;
    public static final Color TEXT_DARK = new Color(0x7B5AF0);

    public static final Font FONT_TITLE = new Font("SansSerif", Font.BOLD, 30);
    public static final Font FONT_HEADING = new Font("SansSerif", Font.BOLD, 22);
    public static final Font FONT_LABEL = new Font("SansSerif", Font.BOLD, 14);
    public static final Font FONT_BUTTON = new Font("SansSerif", Font.BOLD, 16);
    public static final Font FONT_FIELD = new Font("SansSerif", Font.PLAIN, 14);
    public static final Font FONT_DESC = new Font("SansSerif", Font.PLAIN, 12);

    private UIStyle() {
    }

    public static JButton primaryButton(String text) {
        return new RoundedButton(text, PRIMARY_PURPLE, WHITE, 14);
    }

    public static JButton secondaryButton(String text) {
        return new RoundedButton(text, TEXT_GRAY, WHITE, 14);
    }

    public static JTextField roundedTextField() {
        JTextField field = new JTextField();
        styleInputField(field);
        return field;
    }

    public static JPasswordField roundedPasswordField() {
        JPasswordField field = new JPasswordField();
        styleInputField(field);
        return field;
    }

    private static void styleInputField(JTextField field) {
        field.setPreferredSize(new Dimension(450, 48));
        field.setMaximumSize(new Dimension(450, 48));
        field.setFont(FONT_FIELD);
        field.setForeground(TEXT_DARK);
        field.setBackground(WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(PRIMARY_PURPLE, 10, 2),
                new EmptyBorder(0, 14, 0, 14)
        ));
    }

    public static JButton sidebarButton(String text, String iconType) {
        JButton b = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setForeground(TEXT_GRAY);
        b.setBackground(WHITE);
        b.setFont(FONT_BUTTON);
        b.setFocusPainted(false);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setIcon(new VectorIcon(iconType, 18, TEXT_GRAY));
        b.setIconTextGap(10);
        b.setBorder(new EmptyBorder(12, 16, 12, 16));
        b.setAlignmentX(Component.LEFT_ALIGNMENT);
        b.setMaximumSize(new Dimension(200, 44));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    public static void setSidebarButtonSelected(JButton btn, boolean selected, String iconType) {
        if (selected) {
            btn.setForeground(PRIMARY_PURPLE);
            btn.setIcon(new VectorIcon(iconType, 18, PRIMARY_PURPLE));
        } else {
            btn.setForeground(TEXT_GRAY);
            btn.setIcon(new VectorIcon(iconType, 18, TEXT_GRAY));
        }
    }

    public static JButton logoutButton() {
        JButton b = new JButton();
        b.setPreferredSize(new Dimension(46, 46));
        b.setMaximumSize(new Dimension(46, 46));
        b.setMinimumSize(new Dimension(46, 46));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        b.setBorder(null);
        b.setBackground(WHITE);
        b.setForeground(PRIMARY_PURPLE);
        b.setIcon(new VectorIcon("LOGOUT", 22, PRIMARY_PURPLE));
        
        b.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(WHITE);
                g2.fillOval(0, 0, c.getWidth(), c.getHeight());
                g2.dispose();
                super.paint(g, c);
            }
        });
        return b;
    }

    public static JPanel sidebar() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(PRIMARY_PURPLE);
        panel.setBorder(new EmptyBorder(25, 12, 25, 12));
        panel.setPreferredSize(new Dimension(220, 600));
        return panel;
    }

    public static JLabel heading(String text) {
        JLabel l = new JLabel(text);
        l.setFont(FONT_HEADING);
        l.setForeground(PRIMARY_PURPLE);
        return l;
    }

    public static void styleTable(JTable table) {
        table.setGridColor(PRIMARY_PURPLE);
        table.setForeground(Color.DARK_GRAY);
        table.setRowHeight(56);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.setSelectionBackground(new Color(0xECE5FF));
        table.setSelectionForeground(PRIMARY_PURPLE);
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(1, 1));
        
        table.getTableHeader().setFont(FONT_BUTTON);
        table.getTableHeader().setBackground(WHITE);
        table.getTableHeader().setForeground(PRIMARY_PURPLE);
        
        table.addHierarchyListener(new java.awt.event.HierarchyListener() {
            @Override
            public void hierarchyChanged(java.awt.event.HierarchyEvent e) {
                if ((e.getChangeFlags() & java.awt.event.HierarchyEvent.PARENT_CHANGED) != 0) {
                    Container parent = table.getParent();
                    if (parent instanceof JViewport) {
                        Container grandParent = parent.getParent();
                        if (grandParent instanceof JScrollPane) {
                            JScrollPane scrollPane = (JScrollPane) grandParent;
                            scrollPane.setBorder(BorderFactory.createLineBorder(PRIMARY_PURPLE, 1));
                            scrollPane.getViewport().setBackground(WHITE);
                        }
                    }
                }
            }
        });

        javax.swing.table.DefaultTableCellRenderer headerRenderer = new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val, boolean isSel, boolean hasFoc, int row, int col) {
                JLabel c = (JLabel) super.getTableCellRendererComponent(t, val, isSel, hasFoc, row, col);
                c.setHorizontalAlignment(SwingConstants.CENTER);
                c.setBackground(WHITE);
                c.setForeground(PRIMARY_PURPLE);
                c.setFont(FONT_BUTTON);
                c.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 1, PRIMARY_PURPLE));
                String text = val != null ? val.toString() : "";
                text = text.replace("No of Staff", "No of<br>Staff")
                           .replace("Mobile Number", "Mobile<br>Number")
                           .replace("Courses teaching", "Courses<br>teaching")
                           .replace("No of Students", "No of<br>Students");
                c.setText("<html><center>" + text + "</center></html>");
                return c;
            }
        };
        table.getTableHeader().setDefaultRenderer(headerRenderer);
        table.getTableHeader().setPreferredSize(new Dimension(table.getTableHeader().getWidth(), 56));

        javax.swing.table.DefaultTableCellRenderer centerRenderer = new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val, boolean isSel, boolean hasFoc, int row, int col) {
                JLabel c = (JLabel) super.getTableCellRendererComponent(t, val, isSel, hasFoc, row, col);
                c.setHorizontalAlignment(SwingConstants.CENTER);
                c.setBackground(WHITE);
                c.setForeground(Color.DARK_GRAY);
                if (isSel) {
                    c.setBackground(new Color(0xECE5FF));
                    c.setForeground(PRIMARY_PURPLE);
                }
                String text = val != null ? val.toString() : "";
                Font f = new Font("SansSerif", Font.PLAIN, 13);
                if (text.length() > 22) f = f.deriveFont(12f);
                if (text.length() > 32) f = f.deriveFont(11f);
                c.setFont(f);
                int w = Math.max(60, t.getColumnModel().getColumn(col).getWidth() - 10);
                c.setText("<html><div style='text-align:center;width:" + w + "px;'>" + text + "</div></html>");
                return c;
            }
        };
        
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    public static void styleStudentTable(JTable table) {
        table.setGridColor(PRIMARY_PURPLE);
        table.setForeground(PRIMARY_PURPLE);
        table.setRowHeight(56);
        table.setFont(new Font("SansSerif", Font.BOLD, 13));
        table.setSelectionBackground(new Color(0xECE5FF));
        table.setSelectionForeground(PRIMARY_PURPLE);
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(1, 1));
        
        table.getTableHeader().setFont(FONT_BUTTON);
        table.getTableHeader().setBackground(WHITE);
        table.getTableHeader().setForeground(PRIMARY_PURPLE);
        
        table.addHierarchyListener(new java.awt.event.HierarchyListener() {
            @Override
            public void hierarchyChanged(java.awt.event.HierarchyEvent e) {
                if ((e.getChangeFlags() & java.awt.event.HierarchyEvent.PARENT_CHANGED) != 0) {
                    Container parent = table.getParent();
                    if (parent instanceof JViewport) {
                        Container grandParent = parent.getParent();
                        if (grandParent instanceof JScrollPane) {
                            JScrollPane scrollPane = (JScrollPane) grandParent;
                            scrollPane.setBorder(BorderFactory.createLineBorder(PRIMARY_PURPLE, 1));
                            scrollPane.getViewport().setBackground(WHITE);
                        }
                    }
                }
            }
        });

        javax.swing.table.DefaultTableCellRenderer headerRenderer = new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val, boolean isSel, boolean hasFoc, int row, int col) {
                JLabel c = (JLabel) super.getTableCellRendererComponent(t, val, isSel, hasFoc, row, col);
                c.setHorizontalAlignment(SwingConstants.CENTER);
                c.setBackground(WHITE);
                c.setForeground(PRIMARY_PURPLE);
                c.setFont(FONT_BUTTON);
                c.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 1, PRIMARY_PURPLE));
                String text = val != null ? val.toString() : "";
                c.setText("<html><center>" + text + "</center></html>");
                return c;
            }
        };
        table.getTableHeader().setDefaultRenderer(headerRenderer);
        table.getTableHeader().setPreferredSize(new Dimension(table.getTableHeader().getWidth(), 56));

        javax.swing.table.DefaultTableCellRenderer centerRenderer = new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val, boolean isSel, boolean hasFoc, int row, int col) {
                JLabel c = (JLabel) super.getTableCellRendererComponent(t, val, isSel, hasFoc, row, col);
                
                Object firstValObj = t.getValueAt(row, 0);
                String rowHeader = firstValObj != null ? firstValObj.toString() : "";
                String text = val != null ? val.toString() : "";
                
                if (t.getColumnCount() == 6 && "Time".equalsIgnoreCase(t.getColumnName(0)) && "Interval".equalsIgnoreCase(rowHeader)) {
                    c.setBackground(PRIMARY_PURPLE);
                    c.setForeground(WHITE);
                    c.setFont(new Font("SansSerif", Font.BOLD, 14));
                    if (col == 3) {
                        c.setText("Interval");
                    } else {
                        c.setText("");
                    }
                } else {
                    c.setBackground(WHITE);
                    c.setForeground(PRIMARY_PURPLE);
                    if (isSel) {
                        c.setBackground(new Color(0xECE5FF));
                    }
                    Font f = new Font("SansSerif", Font.BOLD, 13);
                    if (text.length() > 22) f = f.deriveFont(12f);
                    if (text.length() > 32) f = f.deriveFont(11f);
                    c.setFont(f);
                    int w = Math.max(60, t.getColumnModel().getColumn(col).getWidth() - 10);
                    c.setText("<html><div style='text-align:center;width:" + w + "px;'>" + text + "</div></html>");
                }
                c.setHorizontalAlignment(SwingConstants.CENTER);
                return c;
            }
        };
        
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    public static void adjustTableHeight(JTable table) {
        Container p = table.getParent();
        if (p instanceof JViewport) {
            Container gp = p.getParent();
            if (gp instanceof JScrollPane) {
                JScrollPane scrollPane = (JScrollPane) gp;
                int visibleRows = Math.min(Math.max(table.getRowCount(), 1), 8);
                int rowH = table.getRowHeight();
                int headerH = table.getTableHeader().getPreferredSize().height;
                int totalH = headerH + visibleRows * rowH + 4;
                scrollPane.setPreferredSize(new Dimension(scrollPane.getPreferredSize().width, totalH));
                scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, totalH));
                scrollPane.revalidate();
                scrollPane.repaint();
            }
        }
    }

    public static void autoFitColumns(JTable table, int minWidth, int maxWidth) {
        FontMetrics fm = table.getFontMetrics(table.getFont());
        FontMetrics headerFm = table.getFontMetrics(FONT_BUTTON);
        for (int col = 0; col < table.getColumnCount(); col++) {
            int widest = headerFm.stringWidth(table.getColumnName(col)) + 30;
            for (int row = 0; row < table.getRowCount(); row++) {
                Object val = table.getValueAt(row, col);
                if (val != null) {
                    String text = val.toString();
                    widest = Math.max(widest, fm.stringWidth(text) + 24);
                }
            }
            int finalWidth = Math.min(Math.max(widest, minWidth), maxWidth);
            table.getColumnModel().getColumn(col).setPreferredWidth(finalWidth);
        }
    }
}

class RoundedButton extends JButton {
    private final int radius;

    public RoundedButton(String text, Color bgColor, Color fgColor, int radius) {
        super(text);
        this.radius = radius;
        setBackground(bgColor);
        setForeground(fgColor);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setFont(UIStyle.FONT_BUTTON);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (getModel().isPressed()) {
            g2.setColor(getBackground().darker());
        } else if (getModel().isRollover()) {
            g2.setColor(getBackground().brighter());
        } else {
            g2.setColor(getBackground());
        }
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        g2.dispose();
        super.paintComponent(g);
    }
}

class RoundedBorder extends AbstractBorder {
    private final Color color;
    private final int radius;
    private final int thickness;

    public RoundedBorder(Color color, int radius, int thickness) {
        this.color = color;
        this.radius = radius;
        this.thickness = thickness;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(color);
        for (int i = 0; i < thickness; i++) {
            g2.drawRoundRect(x + i, y + i, width - 1 - (i * 2), height - 1 - (i * 2), radius, radius);
        }
        g2.dispose();
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(thickness, thickness, thickness, thickness);
    }

    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
        insets.left = thickness;
        insets.right = thickness;
        insets.top = thickness;
        insets.bottom = thickness;
        return insets;
    }
}

class UserProfileIcon extends JComponent {
    public UserProfileIcon() {
        setPreferredSize(new Dimension(80, 80));
        setMaximumSize(new Dimension(80, 80));
        setMinimumSize(new Dimension(80, 80));
        setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        g2.setColor(Color.WHITE);
        g2.fillOval(0, 0, w, h);

        g2.setColor(new Color(0xA6A6A6));
        int headSize = w / 3;
        int headX = (w - headSize) / 2;
        int headY = h / 4;
        g2.fillOval(headX, headY, headSize, headSize);

        int shoulderWidth = (int) (w * 0.7);
        int shoulderHeight = h / 2;
        int shoulderX = (w - shoulderWidth) / 2;
        int shoulderY = headY + headSize + 4;
        g2.fillArc(shoulderX, shoulderY, shoulderWidth, shoulderHeight, 0, 180);

        g2.dispose();
    }
}

class VectorIcon implements Icon {
    private final String type;
    private final int size;
    private final Color color;

    public VectorIcon(String type, int size, Color color) {
        this.type = type;
        this.size = size;
        this.color = color;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(color);
        g2.translate(x, y);

        switch (type) {
            case "USER":
                g2.fillOval(size / 3, size / 8, size / 3, size / 3);
                g2.fillArc(size / 8, size / 2, (int) (size * 0.75), size / 2, 0, 180);
                break;
            case "CALENDAR":
                g2.drawRect(size / 8, size / 8, (int) (size * 0.75), (int) (size * 0.75));
                g2.fillRect(size / 4, 0, size / 8, size / 4);
                g2.fillRect((int) (size * 0.62), 0, size / 8, size / 4);
                g2.drawLine((int) (size * 0.3), (int) (size * 0.5), (int) (size * 0.45), (int) (size * 0.65));
                g2.drawLine((int) (size * 0.45), (int) (size * 0.65), (int) (size * 0.7), (int) (size * 0.35));
                break;
            case "BOOK":
                g2.drawRect(size / 8, size / 6, (int) (size * 0.75), (int) (size * 0.68));
                g2.drawLine(size / 2, size / 6, size / 2, (int) (size * 0.84));
                g2.drawLine(size / 4, size / 3, size / 2 - 2, size / 3);
                g2.drawLine(size / 4, size / 2, size / 2 - 2, size / 2);
                g2.drawLine(size / 2 + 3, size / 3, (int) (size * 0.75), size / 3);
                g2.drawLine(size / 2 + 3, size / 2, (int) (size * 0.75), size / 2);
                break;
            case "BUILDING":
                int[] xPoints = {size / 2, size / 8, (int) (size * 0.87)};
                int[] yPoints = {size / 8, size / 3, size / 3};
                g2.fillPolygon(xPoints, yPoints, 3);
                g2.fillRect(size / 8, (int) (size * 0.75), (int) (size * 0.75), size / 8);
                g2.fillRect((int) (size * 0.22), size / 3 + 2, size / 10, (int) (size * 0.38));
                g2.fillRect((int) (size * 0.45), size / 3 + 2, size / 10, (int) (size * 0.38));
                g2.fillRect((int) (size * 0.68), size / 3 + 2, size / 10, (int) (size * 0.38));
                break;
            case "GRAD_CAP":
                int[] capX = {size / 2, size / 10, size / 2, (int) (size * 0.9)};
                int[] capY = {size / 4, size / 2, (int) (size * 0.75), size / 2};
                g2.fillPolygon(capX, capY, 4);
                g2.drawLine((int) (size * 0.9), size / 2, (int) (size * 0.9), (int) (size * 0.7));
                g2.fillRect((int) (size * 0.85), (int) (size * 0.7), size / 8, size / 8);
                int[] baseX = {(int) (size * 0.3), (int) (size * 0.7), (int) (size * 0.65), (int) (size * 0.35)};
                int[] baseY = {(int) (size * 0.62), (int) (size * 0.62), (int) (size * 0.8), (int) (size * 0.8)};
                g2.fillPolygon(baseX, baseY, 4);
                break;
            case "LOGOUT":
                g2.drawRect(size / 8, size / 8, (int) (size * 0.5), (int) (size * 0.75));
                g2.setColor(c.getBackground());
                g2.fillRect(size / 2 + size / 8 - 1, size / 3, 4, size / 3);
                g2.setColor(color);
                g2.drawLine(size / 3, size / 2, (int) (size * 0.8), size / 2);
                g2.drawLine((int) (size * 0.8), size / 2, (int) (size * 0.6), size / 3);
                g2.drawLine((int) (size * 0.8), size / 2, (int) (size * 0.6), (int) (size * 0.67));
                break;
        }

        g2.dispose();
    }

    @Override
    public int getIconWidth() {
        return size;
    }

    @Override
    public int getIconHeight() {
        return size;
    }
}
