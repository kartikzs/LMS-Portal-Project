# 📚 LMS Portal (Java Swing)
A desktop-based Learning Management System developed using Java Swing and MySQL.

▶️ How to Run
1. Install Java and MySQL on your system
2. Download the project files

🗄️ Database Setup
1. Open MySQL Workbench
2. Create a database:
   CREATE DATABASE lms;
3. Import the provided file:
   Server → Data Import → Select `lms.sql` → Start Import

⚙️ Configuration
Make sure the database connection is correct in the code:
jdbc:mysql://localhost:3306/lms

▶️ Run Application
Run the JAR file:
java -jar LMS_portal.jar

⚠️ Note

* MySQL server must be running before starting the application
* Ensure username and password in code match your MySQL setup
