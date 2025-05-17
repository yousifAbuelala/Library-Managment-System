import java.sql.*;

public class BookManager extends DatabaseHandler 
{
    public BookManager(Connection conn) 
    {
        super(conn);
    }
    public void addBook(int id, String title, String author, String department) throws SQLException {
        String sql = "INSERT INTO books (book_id, title, author, department) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.setString(2, title);
            stmt.setString(3, author);
            stmt.setString(4, department);
            stmt.executeUpdate();
        }
    }

    public void listBooks() throws SQLException {
        String sql = "SELECT * FROM bookmanager";  
        try (Statement stmt = conn.createStatement(); 
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("Book_ID") +  
                                 ", Title: " + rs.getString("Book_Title") +
                                 ", Author: " + rs.getString("Book_Author") +
                                 ", Dept: " + rs.getString("Department"));
            }
        }
    }
    public int deleteBook(int bookId) throws SQLException 
    {
        if (!bookExists(bookId)) 
        {
            return 0; 
        }
        String sql = "DELETE FROM books WHERE book_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bookId);
            return stmt.executeUpdate(); 
        }
    }
public Book viewBook(int bookId) throws SQLException 
{
        String sql = "SELECT * FROM books WHERE book_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) 
        {
            stmt.setInt(1, bookId);
            try (ResultSet rs = stmt.executeQuery()) 
            {
                if (rs.next()) 
                {
                    return new Book(
                        rs.getInt("book_id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("department")
                    );
                }
                else 
                {
                    return null; 
                }
            }
        }
    }
 public int editBook(int bookId, String newTitle, String newAuthor, String newDepartment) 
            throws SQLException 
 {
        if (!bookExists(bookId)) 
        {
            return 0;
        }
        String sql = "UPDATE books SET title = ?, author = ?, department = ? WHERE book_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newTitle);
            stmt.setString(2, newAuthor);
            stmt.setString(3, newDepartment);
            stmt.setInt(4, bookId);
            
            return stmt.executeUpdate();
        }
    }

    private boolean bookExists(int bookId) throws SQLException {
        String sql = "SELECT 1 FROM books WHERE book_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bookId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }
}
class Book {
    private int id;
    private String title;
    private String author;
    private String department;
    public Book(int id, String title, String author, String department) 
    {
        this.id = id;
        this.title = title;
        this.author = author;
        this.department = department;
    }
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getDepartment() { return department; }
    @Override
    public String toString() {
        return String.format(
            "Book ID: %d\nTitle: %s\nAuthor: %s\nDepartment: %s",
            id, title, author, department
        );
    }
}