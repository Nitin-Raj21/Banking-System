package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL      = "jdbc:mysql://localhost:3306/banking_system";
    private static final String USER     = "root";
    private static final String PASSWORD = "your_password";

    private DBConnection() {}

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

Commit message
Add database connection utility
Description
DBConnection class to manage MySQL connections using JDBC
Click Commit changes ✅

Come back Day 4 and I'll give you the next files! 🟩You said: file name?file name?9:25 PMClaude responded: Type exactly this in the filename box:Type exactly this in the filename box:
src/db/DBConnection.java
GitHub will automatically create the src and db folders for you when you type the / in the name.Claude Fable 5 is currently unavailab
