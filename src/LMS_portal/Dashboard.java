package LMS_portal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Dashboard extends JFrame implements ActionListener {

    JButton addCourse, viewCourse, logout;
    JPanel panel;
    boolean isAdmin;

    Dashboard(boolean isAdmin){

        this.isAdmin = isAdmin;

        setTitle("Dashboard");
        setLayout(new BorderLayout());

        // 🔥 BACKGROUND PANEL (AUTO RESIZE IMAGE)
        JPanel background = new JPanel(){
            Image img = new ImageIcon(ClassLoader.getSystemResource("images/books.jpg")).getImage();

            protected void paintComponent(Graphics g){
                super.paintComponent(g);
                g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
            }
        };

        background.setLayout(new GridBagLayout()); // ✅ CENTER EVERYTHING
        setContentPane(background);

        // 🔥 CENTER PANEL
        panel = new JPanel();
        panel.setPreferredSize(new Dimension(500, 320));
        panel.setBackground(new Color(255, 255, 255, 170)); // glass effect
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // 🔥 HEADING
        JLabel heading = new JLabel("Dashboard");
        heading.setFont(new Font("Arial", Font.BOLD, 30));
        heading.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(Box.createVerticalStrut(15));
        panel.add(heading);
        panel.add(Box.createVerticalStrut(25));

        // 🔥 BUTTON STYLE FUNCTION
        Dimension btnSize = new Dimension(200, 50);

        addCourse = createButton("Add Course", btnSize);
        viewCourse = createButton("View Course", btnSize);
        logout = createButton("Logout", btnSize);

        panel.add(addCourse);
        panel.add(Box.createVerticalStrut(15));
        panel.add(viewCourse);
        panel.add(Box.createVerticalStrut(15));
        panel.add(logout);

        // 🔥 HIDE FOR STUDENT
        if(!isAdmin){
            addCourse.setVisible(false);
        }

        // CENTER PANEL ON SCREEN
        background.add(panel);

        // 🔥 WINDOW SETTINGS
        setExtendedState(JFrame.MAXIMIZED_BOTH); // full screen
        setMinimumSize(new Dimension(800, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    // 🔥 BUTTON CREATOR METHOD
    private JButton createButton(String text, Dimension size){
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 18));
        btn.setBackground(Color.BLACK);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setMaximumSize(size);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.addActionListener(this);
        return btn;
    }

    public void actionPerformed(ActionEvent ae){

        if(ae.getSource() == addCourse){
            new AddCourse(this);
            setVisible(false);

        }else if(ae.getSource() == viewCourse){
            new ViewCourse(isAdmin);
            setVisible(false);

        }else if(ae.getSource() == logout){
            setVisible(false);
            new LoginLMS();
        }
    }

    public static void main(String[] args) {
        new LoginLMS();
    }
}