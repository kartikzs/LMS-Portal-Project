package LMS_portal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AddCourse extends JFrame implements ActionListener {

    JTextField coursename, duration;
    JButton add, back;
    JFrame parent;

    AddCourse(JFrame parent) {

        this.parent = parent;

        setTitle("Add Course");
        setLayout(new BorderLayout());

        // 🔥 BACKGROUND (AUTO SCALE)
        JPanel bg = new JPanel() {
            Image img = new ImageIcon(ClassLoader.getSystemResource("images/books.jpg")).getImage();

            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
            }
        };

        bg.setLayout(new GridBagLayout());
        setContentPane(bg);

        // 🔥 MAIN PANEL (CENTERED)
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(500, 350));
        panel.setBackground(new Color(255, 255, 255, 180));
        panel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 20, 15, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 🔥 HEADING
        JLabel heading = new JLabel("Add New Course", JLabel.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 28));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(heading, gbc);

        // 🔥 COURSE NAME
        JLabel cnameLabel = new JLabel("Course Name:");
        cnameLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(cnameLabel, gbc);

        coursename = new JTextField();
        gbc.gridx = 1;
        panel.add(coursename, gbc);

        // 🔥 DURATION
        JLabel durLabel = new JLabel("Duration:");
        durLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(durLabel, gbc);

        duration = new JTextField();
        gbc.gridx = 1;
        panel.add(duration, gbc);

        // 🔥 BUTTON PANEL
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnPanel.setOpaque(false);

        add = new JButton("Add Course");
        add.setBackground(Color.BLACK);
        add.setForeground(Color.WHITE);
        add.setPreferredSize(new Dimension(150, 40));

        back = new JButton("Back");
        back.setBackground(Color.BLACK);
        back.setForeground(Color.WHITE);
        back.setPreferredSize(new Dimension(150, 40));

        btnPanel.add(add);
        btnPanel.add(back);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(btnPanel, gbc);

        // 🔥 ACTIONS
        add.addActionListener(this);
        back.addActionListener(this);

        // 🔥 ADD PANEL CENTER
        bg.add(panel);

        // 🔥 WINDOW SETTINGS
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(800, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {

        if (ae.getSource() == add) {

            String cname = coursename.getText().trim();
            String dur = duration.getText().trim();

            if (cname.isEmpty() || dur.isEmpty()) {
                JOptionPane.showMessageDialog(null, "All fields are required!");
                return;
            }

            if (!dur.matches("\\d+\\s*Year")) {
                JOptionPane.showMessageDialog(null, "Enter duration like: 4 Year");
                return;
            }

            try {
                Conn c = new Conn();

                // 🔍 Check duplicate
                String checkQuery = "SELECT * FROM courses WHERE course_name=?";
                PreparedStatement pst = c.c.prepareStatement(checkQuery);
                pst.setString(1, cname);
                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    JOptionPane.showMessageDialog(null, "Course already exists!");
                    return;
                }

                // ✅ Insert Course
                String insertCourse = "INSERT INTO courses(course_name, duration) VALUES(?, ?)";
                pst = c.c.prepareStatement(insertCourse);
                pst.setString(1, cname);
                pst.setString(2, dur);
                pst.executeUpdate();

                // 🔥 Auto semesters
                int years = Integer.parseInt(dur.split(" ")[0]);
                int totalSem = years * 2;

                String insertSem = "INSERT INTO semesters(course_name, semester_no) VALUES(?, ?)";

                for (int i = 1; i <= totalSem; i++) {
                    pst = c.c.prepareStatement(insertSem);
                    pst.setString(1, cname);
                    pst.setInt(2, i);
                    pst.executeUpdate();
                }

                JOptionPane.showMessageDialog(null,
                        "Course Added Successfully\nTotal Semesters: " + totalSem);

                coursename.setText("");
                duration.setText("");

                dispose();
                if (parent != null) parent.setVisible(true);

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            }

        } else if (ae.getSource() == back) {

            dispose();
            if (parent != null) parent.setVisible(true);
        }
    }
    public static void main(String[] args) {
        new LoginLMS();
    }
}