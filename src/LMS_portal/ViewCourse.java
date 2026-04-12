package LMS_portal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;

public class ViewCourse extends JFrame implements ActionListener {

    JPanel mainPanel;
    boolean isAdmin;

    ViewCourse(boolean isAdmin){

        this.isAdmin = isAdmin;

        setTitle("Courses");
        setLayout(new BorderLayout());

        // 🔥 Background
        JPanel background = new JPanel() {
            Image img = new ImageIcon(ClassLoader.getSystemResource("images/books.jpg")).getImage();

            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
            }
        };

        background.setLayout(new BorderLayout());
        setContentPane(background);

        // 🔙 TOP PANEL
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setOpaque(false);

        JButton back = new JButton("Back");
        back.setFont(new Font("Arial", Font.BOLD, 16));
        back.setBackground(Color.BLACK);
        back.setForeground(Color.WHITE);

        topPanel.add(back);
        background.add(topPanel, BorderLayout.NORTH);

        back.addActionListener(e -> {
            dispose();
            new Dashboard(isAdmin);
        });

        // 🔥 MAIN PANEL (WRAP LAYOUT)
        mainPanel = new JPanel();
        mainPanel.setLayout(new WrapLayout(FlowLayout.CENTER, 30, 30));
        mainPanel.setOpaque(false);

        JScrollPane scroll = new JScrollPane(mainPanel);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(20);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);

        background.add(scroll, BorderLayout.CENTER);

        // 🔄 LOAD DATA
        try{
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("select * from courses");

            ArrayList<JButton> buttons = new ArrayList<>();

            while(rs.next()){
                String course = rs.getString("course_name");

                JButton b = new JButton(course);
                b.setFont(new Font("Arial", Font.BOLD, 18));
                b.setBackground(Color.WHITE);
                b.setFocusPainted(false);
                b.setCursor(new Cursor(Cursor.HAND_CURSOR));

                // 🔥 FIXED SIZE (NO SHRINK)
                b.setPreferredSize(new Dimension(220, 120));
                b.setMinimumSize(new Dimension(220, 120));
                b.setMaximumSize(new Dimension(220, 120));

                b.addActionListener(this);

                // 🔥 ADMIN DELETE
                if(isAdmin){
                    b.addMouseListener(new MouseAdapter(){
                        public void mousePressed(MouseEvent e){
                            if(SwingUtilities.isRightMouseButton(e)){
                                int confirm = JOptionPane.showConfirmDialog(
                                        null,
                                        "Delete this course?",
                                        "Confirm",
                                        JOptionPane.YES_NO_OPTION);

                                if(confirm == JOptionPane.YES_OPTION){
                                    try{
                                        Conn c = new Conn();
                                        String q = "delete from courses where course_name='"+course+"'";
                                        c.s.executeUpdate(q);

                                        JOptionPane.showMessageDialog(null,"Course Deleted");

                                        dispose();
                                        new ViewCourse(isAdmin);

                                    }catch(Exception ex){
                                        ex.printStackTrace();
                                    }
                                }
                            }
                        }
                    });
                }

                buttons.add(b);
            }

            // 🔥 ADD BUTTONS DIRECTLY (NO GRID)
            for(JButton b : buttons){
                mainPanel.add(b);
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        // 🔥 WINDOW SETTINGS
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(800, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae){
        JButton clicked = (JButton)ae.getSource();
        String course = clicked.getText();

        setVisible(false);
        new CourseDetails(course, this, isAdmin);
    }

    public static void main(String[] args) {
        new LoginLMS();
    }
}