import java.sql.*;

public class StudentManager extends DatabaseHandler 
{
    public StudentManager(Connection conn) 
    {
        super(conn);
    }
    
    public void addStudent(int student, String name, String email, String phone) throws SQLException {
        String sql = "INSERT INTO students (student_id, name, email, phone) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, student);
            stmt.setString(2, name);
            stmt.setString(3, email);
            stmt.setString(4, phone);
            stmt.executeUpdate();
        }
    }

    public void listStudents() throws SQLException {
        String sql = "SELECT * FROM students";
        try (Statement stmt = conn.createStatement(); 
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("student_id") +
                                   ", Name: " + rs.getString("name") +
                                   ", Email: " + rs.getString("email") +
                                   ", Phone: " + rs.getString("phone"));
            }
        }
    }
}