import java.sql.*;

public class DatabaseHandler 
{
    protected Connection conn;
    public DatabaseHandler(Connection conn) 
    {
    this.conn = conn;
    }
    public void closeConnection() 
    {
        try 
        {
            if (conn != null) conn.close();
        } 
        catch (SQLException e) 
        {
            System.out.println("Error closing connection: " + e.getMessage());
        }
    }
}