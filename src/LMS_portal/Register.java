package LMS_portal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Register extends JFrame implements ActionListener {
	JPanel Panel;
    JTextField usernameField;
    JPasswordField passwordField, confirmPasswordField;
    JButton register, clear, back;

    Register() {

        setTitle("LMS - Register");
        setLayout(null);
        
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("images/books.jpg"));
        Image i2 = i1.getImage().getScaledInstance(1170, 700, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel lable = new JLabel(i3);
        lable.setBounds(0, 0, 1170, 700);
        add(lable);
        
        Panel = new JPanel();
        Panel.setBounds(350, 150, 500, 350);
        Panel.setBackground(new Color(255, 255, 255, 150)); 
        Panel.setLayout(null);
        lable.add(Panel);

        JLabel heading = new JLabel("REGISTER");
        heading.setFont(new Font("Raleway", Font.BOLD, 30));
        heading.setBounds(150, 10, 200, 70);
        Panel.add(heading);

        JLabel username = new JLabel("Username:");
        username.setFont(new Font("Raleway", Font.BOLD, 20));
        username.setBounds(40, 80, 150, 30);
        Panel.add(username);

        usernameField = new JTextField();
        usernameField.setFont(new Font("Raleway", Font.BOLD, 18));
        usernameField.setBounds(240, 80, 180, 30);
        Panel.add(usernameField);

        JLabel password = new JLabel("Password:");
        password.setFont(new Font("Raleway", Font.BOLD, 20));
        password.setBounds(40, 140, 150, 30);
        Panel.add(password);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Raleway", Font.BOLD, 18));
        passwordField.setBounds(240, 140, 180, 30);
        Panel.add(passwordField);

        JLabel confirmPassword = new JLabel("Confirm Password:");
        confirmPassword.setFont(new Font("Raleway", Font.BOLD, 20));
        confirmPassword.setBounds(40, 200, 220, 30);
        Panel.add(confirmPassword);

        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setFont(new Font("Raleway", Font.BOLD, 18));
        confirmPasswordField.setBounds(240, 200, 180, 30);
        Panel.add(confirmPasswordField);

        register = new JButton("Register");
        register.setFont(new Font("Raleway", Font.BOLD, 20));
        register.setBackground(Color. BLACK);
        register.setForeground(Color.WHITE);
        register.setBounds(40, 260, 130, 30);
        register.addActionListener(this);
        Panel.add(register);

        clear = new JButton("Clear");
        clear.setFont(new Font("Raleway", Font.BOLD, 20));
        clear.setBackground(Color. BLACK);
        clear.setForeground(Color.WHITE);
        clear.setBounds(200, 260, 100, 30);
        clear.addActionListener(this);
        Panel.add(clear);

        back = new JButton("Back");
        back.setFont(new Font("Raleway", Font.BOLD, 20));
        back.setBackground(Color. BLACK);
        back.setForeground(Color.WHITE);
        back.setBounds(320, 260, 100, 30);
        back.addActionListener(this);
        Panel.add(back);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setSize(1170, 700);
        setLocation(50, 30);
    }

    public void actionPerformed(ActionEvent ae) {

        if (ae.getSource() == register) {

            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());

            if (username.equals("") || password.equals("")) {
                JOptionPane.showMessageDialog(null, "All Fields are Required!");
            }

            else if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(null, "Passwords Do Not Match!");
            }

            else {
                try {
                    Conn c = new Conn();

                    String query = "INSERT INTO users(username, password) VALUES(?, ?)";

                    PreparedStatement pst = c.c.prepareStatement(query);
                    pst.setString(1, username);
                    pst.setString(2, password);

                    pst.executeUpdate();

                    JOptionPane.showMessageDialog(null, "Registration Successful ✅");

                    setVisible(false);
                    new LoginLMS();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        else if (ae.getSource() == clear) {
            usernameField.setText("");
            passwordField.setText("");
            confirmPasswordField.setText("");
        }

        else if (ae.getSource() == back) {
            setVisible(false);
            new LoginLMS();
        }
    }
    public static void main(String[] args) {
        new Register();
    }
   
}