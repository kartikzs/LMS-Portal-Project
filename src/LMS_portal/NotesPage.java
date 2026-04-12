package LMS_portal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.*;

public class NotesPage extends JFrame {

    String subject;
    String role;

    JPanel listPanel;

    NotesPage(String subject, String role){

        this.subject = subject;
        this.role = role;

        setTitle(subject + " Notes");
        setSize(500,500);
        setLayout(null);

        JLabel heading = new JLabel(subject + " Notes", JLabel.CENTER);
        heading.setBounds(100,20,300,30);
        heading.setFont(new Font("Arial", Font.BOLD, 20));
        add(heading);

        // 🔽 List Panel
        listPanel = new JPanel();
        listPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        listPanel.setBackground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(listPanel);
        scroll.setBounds(50,70,380,250);
        add(scroll);

        JButton upload = new JButton("Upload PDF");
        upload.setBounds(50,350,150,40);
        add(upload);

        JButton refresh = new JButton("Refresh");
        refresh.setBounds(250,350,150,40);
        add(refresh);

        // 🔒 Student → disable upload
        if(role.equals("student")){
            upload.setEnabled(false);
        }

        loadFiles();

        upload.addActionListener(e -> uploadPDF());
        refresh.addActionListener(e -> loadFiles());

        setVisible(true);
    }

    // 🔥 LOAD ALL FILES
    void loadFiles(){

        listPanel.removeAll();

        try{
            Conn c = new Conn();

            String q = "SELECT * FROM notes WHERE subject_name=?";
            PreparedStatement pst = c.c.prepareStatement(q);
            pst.setString(1, subject);

            ResultSet rs = pst.executeQuery();

            boolean found = false;

            while(rs.next()){

                found = true;

                int id = rs.getInt("id");
                String fileName = rs.getString("file_name");

                JButton btn = new JButton("• " + fileName);
                btn.setHorizontalAlignment(SwingConstants.LEFT);
                btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
                btn.setPreferredSize(new Dimension(400, 40)); // width, height

                // 🔥 VIEW PDF
                btn.addActionListener(e -> viewPDF(id));

                // 🔥 ADMIN DELETE (RIGHT CLICK)
                btn.addMouseListener(new MouseAdapter(){

                    public void mousePressed(MouseEvent e){

                        if(SwingUtilities.isRightMouseButton(e) && role.equals("admin")){

                            int confirm = JOptionPane.showConfirmDialog(null,
                                    "Delete " + fileName + "?",
                                    "Confirm",
                                    JOptionPane.YES_NO_OPTION);

                            if(confirm == JOptionPane.YES_OPTION){
                                deletePDF(id);
                            }
                        }
                    }
                });

                listPanel.add(btn);
            }

            if(!found){
                JLabel empty = new JLabel("❌ No Lesson Found", JLabel.CENTER);
                empty.setFont(new Font("Arial", Font.BOLD, 16));
                listPanel.add(empty);
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        listPanel.revalidate();
        listPanel.repaint();
    }

    // 📤 UPLOAD
    void uploadPDF(){

        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PDF Files", "pdf"));

        int result = chooser.showOpenDialog(this);

        if(result == JFileChooser.APPROVE_OPTION){

            File file = chooser.getSelectedFile();

            try{
                FileInputStream fis = new FileInputStream(file);

                Conn c = new Conn();

                String q = "INSERT INTO notes(subject_name, file_name, pdf_file) VALUES(?,?,?)";
                PreparedStatement pst = c.c.prepareStatement(q);

                pst.setString(1, subject);
                pst.setString(2, file.getName());
                pst.setBinaryStream(3, fis, (int)file.length());

                pst.executeUpdate();

                JOptionPane.showMessageDialog(null,"PDF Uploaded");

                loadFiles();

            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    // 📖 VIEW
    void viewPDF(int id){

        try{
            Conn c = new Conn();

            String q = "SELECT pdf_file FROM notes WHERE id=?";
            PreparedStatement pst = c.c.prepareStatement(q);
            pst.setInt(1, id);

            ResultSet rs = pst.executeQuery();

            if(rs.next()){

                InputStream is = rs.getBinaryStream("pdf_file");

                File file = new File("temp_" + id + ".pdf");
                FileOutputStream fos = new FileOutputStream(file);

                byte[] buffer = new byte[1024];
                int bytesRead;

                while((bytesRead = is.read(buffer)) != -1){
                    fos.write(buffer,0,bytesRead);
                }

                fos.close();

                Desktop.getDesktop().open(file);
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    // ❌ DELETE
    void deletePDF(int id){

        try{
            Conn c = new Conn();

            String q = "DELETE FROM notes WHERE id=?";
            PreparedStatement pst = c.c.prepareStatement(q);
            pst.setInt(1, id);

            pst.executeUpdate();

            JOptionPane.showMessageDialog(null,"Deleted");

            loadFiles();

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}