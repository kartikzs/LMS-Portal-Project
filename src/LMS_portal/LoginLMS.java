package LMS_portal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginLMS extends JFrame implements ActionListener {

    JTextField usertext;
    JPasswordField passwordfield;
    JButton loginBtn, clearBtn, registerBtn;

    LoginLMS() {

        setTitle("Learning Management System");
        setLayout(new BorderLayout());

        // 🔥 BACKGROUND PANEL (AUTO SCALE)
        JPanel background = new JPanel() {
            Image img = new ImageIcon(ClassLoader.getSystemResource("images/books.jpg")).getImage();

            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
            }
        };

        background.setLayout(new GridBagLayout());
        setContentPane(background);

        // 🔥 MAIN PANEL
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(600, 300));
        panel.setBackground(new Color(255, 255, 255, 150));
        panel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 20, 15, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 🔥 USERNAME
        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(new Font("Raleway", Font.BOLD, 25));
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(userLabel, gbc);

        usertext = new JTextField();
        usertext.setFont(new Font("Raleway", Font.BOLD, 20));
        gbc.gridx = 1;
        panel.add(usertext, gbc);

        // 🔥 PASSWORD
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("Raleway", Font.BOLD, 25));
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(passLabel, gbc);

        passwordfield = new JPasswordField();
        passwordfield.setFont(new Font("Raleway", Font.BOLD, 18));
        gbc.gridx = 1;
        panel.add(passwordfield, gbc);

        // 🔥 BUTTON PANEL
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 10));
        btnPanel.setOpaque(false);

        clearBtn = createButton("Clear");
        registerBtn = createButton("Register");
        loginBtn = createButton("Login");

        btnPanel.add(clearBtn);
        btnPanel.add(registerBtn);
        btnPanel.add(loginBtn);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(btnPanel, gbc);

        // CENTER PANEL
        background.add(panel);

        // 🔥 WINDOW SETTINGS
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(800, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    // 🔥 FIXED BUTTON METHOD (BLACK BACKGROUND)
    private JButton createButton(String text) {
        JButton btn = new JButton(text);

        btn.setFont(new Font("Raleway", Font.BOLD, 20));
        btn.setForeground(Color.WHITE);

        // ✅ FIX
        btn.setBackground(Color.BLACK);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);

        // SAME SIZE (UNCHANGED)
        btn.setPreferredSize(new Dimension(150, 50));
        btn.setMinimumSize(new Dimension(150, 50));
        btn.setMaximumSize(new Dimension(150, 50));

        btn.addActionListener(this);
        return btn;
    }

    public void actionPerformed(ActionEvent ae) {

        if (ae.getSource() == loginBtn) {

            try {
                String username = usertext.getText();
                String password = new String(passwordfield.getPassword());

                Conn c = new Conn();

                String query = "SELECT * FROM users WHERE username = ? AND password = ?";
                PreparedStatement pst = c.c.prepareStatement(query);

                pst.setString(1, username);
                pst.setString(2, password);

                ResultSet rs = pst.executeQuery();

                if (rs.next()) {

                    JOptionPane.showMessageDialog(null, "Login Successful");
                    setVisible(false);

                    if (username.equals("ADMIN IGU") && password.equals("DEPT321")) {
                        new Dashboard(true);
                    } else {
                        new Dashboard(false);
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "Invalid Username or Password");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (ae.getSource() == clearBtn) {
            usertext.setText("");
            passwordfield.setText("");

        } else if (ae.getSource() == registerBtn) {
            setVisible(false);
            new Register();
        }
    }

    public static void main(String[] args) {
        try {
        	UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        new LoginLMS();
    }
}