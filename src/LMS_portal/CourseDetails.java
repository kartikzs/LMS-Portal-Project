package LMS_portal;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class CourseDetails extends JFrame {

    JComboBox<String> semesterBox;
    JFrame parent;
    boolean isAdmin;

    CourseDetails(String cname, JFrame parent, boolean isAdmin){

        this.parent = parent;
        this.isAdmin = isAdmin;

        setTitle("Course Details");
        setLayout(new BorderLayout());

        // 🔥 BACKGROUND (AUTO SCALE)
        JPanel bg = new JPanel(){
            Image img = new ImageIcon(ClassLoader.getSystemResource("images/books.jpg")).getImage();

            protected void paintComponent(Graphics g){
                super.paintComponent(g);
                g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
            }
        };

        bg.setLayout(new GridBagLayout()); // ✅ CENTER EVERYTHING
        setContentPane(bg);

        // 🔥 MAIN PANEL (AUTO CENTER + SCALE)
        JPanel panel = new JPanel();
        panel.setBackground(new Color(255,255,255,180));
        panel.setLayout(new GridBagLayout());
        panel.setPreferredSize(new Dimension(500,350));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 20, 15, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 🔥 HEADING
        JLabel heading = new JLabel(cname, JLabel.CENTER);
        heading.setFont(new Font("Arial",Font.BOLD,28));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(heading, gbc);

        // 🔥 LABEL
        JLabel select = new JLabel("Select Semester:");
        select.setFont(new Font("Arial",Font.BOLD,20));
        gbc.gridy = 1;
        panel.add(select, gbc);

        // 🔥 LOAD SEMESTERS
        java.util.ArrayList<String> semList = new java.util.ArrayList<>();

        try{
            Conn c = new Conn();
            String query = "SELECT semester_no FROM semesters WHERE course_name='"+cname+"' ORDER BY semester_no";
            ResultSet rs = c.s.executeQuery(query);

            semList.add("Select");

            while(rs.next()){
                int sem = rs.getInt("semester_no");
                semList.add("Semester " + sem);
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        semesterBox = new JComboBox<>(semList.toArray(new String[0]));
        semesterBox.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridy = 2;
        panel.add(semesterBox, gbc);

        // 🔥 BUTTON PANEL
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnPanel.setOpaque(false);

        JButton open = new JButton("Open");
        open.setBackground(Color.BLACK);
        open.setForeground(Color.WHITE);
        open.setPreferredSize(new Dimension(140,40));

        JButton back = new JButton("Back");
        back.setPreferredSize(new Dimension(140,40));

        btnPanel.add(open);
        btnPanel.add(back);

        gbc.gridy = 3;
        panel.add(btnPanel, gbc);

        // 🔥 ACTIONS
        open.addActionListener(e -> {

            String selected = (String) semesterBox.getSelectedItem();

            if(selected.equals("Select")){
                JOptionPane.showMessageDialog(null,"Select semester");
            }else{

                int sem = Integer.parseInt(selected.split(" ")[1]);

                setVisible(false);

                if(isAdmin){
                    new SubjectPage(cname, sem, this);
                }else{
                    new ViewSubjectPage(cname, sem, this);
                }
            }
        });

        back.addActionListener(e -> {
            dispose();
            parent.setVisible(true);
        });

        // 🔥 ADD CENTER PANEL
        bg.add(panel);

        // 🔥 WINDOW SETTINGS
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(800,600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}