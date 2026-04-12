package LMS_portal;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ViewSubjectPage extends JFrame {

    JPanel listPanel;
    String course;
    int semester;
    JFrame parent;

    ViewSubjectPage(String course, int semester, JFrame parent){

        this.course = course;
        this.semester = semester;
        this.parent = parent;

        setTitle(course + " - Semester " + semester);
        setLayout(new BorderLayout());

        // 🔥 BACKGROUND (AUTO SCALE)
        JPanel bg = new JPanel(){
            Image img = new ImageIcon(ClassLoader.getSystemResource("images/books.jpg")).getImage();

            protected void paintComponent(Graphics g){
                super.paintComponent(g);
                g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
            }
        };

        bg.setLayout(new GridBagLayout());
        setContentPane(bg);

        // 🔥 MAIN PANEL (CENTERED)
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(650, 500));
        panel.setBackground(new Color(255,255,255,180));
        panel.setLayout(new BorderLayout(10,10));

        // 🔥 HEADING
        JLabel heading = new JLabel(course + " - Semester " + semester, JLabel.CENTER);
        heading.setFont(new Font("Arial",Font.BOLD,24));
        panel.add(heading, BorderLayout.NORTH);

        // 🔽 LIST PANEL
        listPanel = new JPanel();
        listPanel.setLayout(new GridLayout(0,1,10,10));
        listPanel.setBackground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(listPanel);
        scroll.setBorder(null);
        scroll.setBackground(Color.WHITE);
        scroll.getViewport().setBackground(Color.WHITE);

        panel.add(scroll, BorderLayout.CENTER);

        // 🔙 BACK BUTTON
        JButton back = new JButton("Back");
        back.setBackground(Color.BLACK);
        back.setForeground(Color.WHITE);
        back.setPreferredSize(new Dimension(150,40));

        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.add(back);

        panel.add(bottomPanel, BorderLayout.SOUTH);

        // 🔥 LOAD SUBJECTS
        loadSubjects();

        // 🔙 ACTION
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

    // 🔥 LOAD SUBJECTS
    void loadSubjects(){

        listPanel.removeAll();

        try{
            Conn c = new Conn();

            String q = "SELECT subject_name FROM subjects WHERE course_name=? AND semester_no=?";
            PreparedStatement pst = c.c.prepareStatement(q);

            pst.setString(1, course);
            pst.setInt(2, semester);

            ResultSet rs = pst.executeQuery();

            boolean found = false;

            while(rs.next()){

                found = true;
                String sub = rs.getString("subject_name");

                JButton btn = new JButton("• " + sub);
                btn.setFont(new Font("Arial", Font.BOLD, 18));

                // 🔥 CLEAN LABEL STYLE
                btn.setUI(new javax.swing.plaf.basic.BasicButtonUI());
                btn.setBackground(Color.WHITE);
                btn.setForeground(Color.BLACK);
                btn.setOpaque(false);
                btn.setContentAreaFilled(false);
                btn.setBorder(null);
                btn.setFocusPainted(false);
                btn.setHorizontalAlignment(SwingConstants.LEFT);
                btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

                btn.addActionListener(e -> {
                    new NotesPage(sub, "student");
                });

                btn.addMouseListener(new java.awt.event.MouseAdapter() {

                    public void mouseEntered(java.awt.event.MouseEvent e) {
                        btn.setText("<html><u>• " + sub + "</u></html>");
                    }

                    public void mouseExited(java.awt.event.MouseEvent e) {
                        btn.setText("• " + sub);
                    }
                });

                listPanel.add(btn);
            }

            if(!found){
                JLabel empty = new JLabel("No Subjects Available", JLabel.CENTER);
                empty.setFont(new Font("Arial",Font.BOLD,18));
                listPanel.add(empty);
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        listPanel.revalidate();
        listPanel.repaint();
    }
}