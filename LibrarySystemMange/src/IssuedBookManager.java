import java.sql.*;
import javax.swing.JOptionPane;

public class IssuedBookManager 
{
    private Connection conn;
    public IssuedBookManager(Connection conn) 
    {
        this.conn = conn;
    }
    public void issueBook(int issueId, int bookId, int studentId, String issueDate, String returnDate) 
            throws SQLException 
    {
        if (!isValidDate(issueDate) || !isValidDate(returnDate)) 
        {
            throw new SQLException("Invalid date format. Use YYYY-MM-DD");
        }
        if (!isBookAvailable(bookId)) 
        {
            throw new SQLException("Book is not available for issuing");
        }

        if (!studentExists(studentId)) 
        {
            throw new SQLException("Student does not exist");
        }
        String sql = "INSERT INTO issued_books (issue_id, book_id, student_id, issue_date, return_date) " +
                    "VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, issueId);
            stmt.setInt(2, bookId);
            stmt.setInt(3, studentId);
            stmt.setString(4, issueDate);
            stmt.setString(5, returnDate);
            stmt.executeUpdate();
        }
    }
    
    private boolean isValidDate(String dateStr) 
    {
        return dateStr.matches("\\d{4}-\\d{2}-\\d{2}");
    }
    public boolean isBookAvailable(int bookId) throws SQLException 
    {
        String sql = "SELECT COUNT(*) FROM issued_books WHERE book_id = ? AND " +
                    "(return_date IS NULL OR return_date > CURDATE())";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bookId);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) == 0;
        }

    }
    
    private boolean studentExists(int studentId) throws SQLException {
        String sql = "SELECT 1 FROM students WHERE student_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            return stmt.executeQuery().next();
        }
    }
    private void updateBookAvailability(int bookId, boolean available) throws SQLException {
        String sql = "UPDATE books SET available = ? WHERE book_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, available);
            stmt.setInt(2, bookId);
            stmt.executeUpdate();
        }
    }
}