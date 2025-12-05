package projects;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/CallingServlet")
public class DemoCallingServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // ✅ Change this to match your actual DB
    private static final String JDBC_URL = "jdbc:sqlserver://localhost:1433;databaseName=FSWDJS;encrypt=false";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "12345678";

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Sample data
        String[][] data = {
            {"1", "Alice", "30", "New York"},
            {"2", "Bob", "25", "San Francisco"},
            {"3", "Charlie", "28", "Chicago"}
        };

        try {
            // 1️⃣ Load JDBC driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // 2️⃣ Connect to DB
            Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);

            // 3️⃣ Insert data if not already present
            String insertQuery = "IF NOT EXISTS (SELECT 1 FROM callingtable WHERE id = ?) " +
                                 "INSERT INTO callingtable (id, name, age, location) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(insertQuery);

            for (String[] row : data) {
                ps.setInt(1, Integer.parseInt(row[0]));
                ps.setInt(2, Integer.parseInt(row[0]));
                ps.setString(3, row[1]);
                ps.setInt(4, Integer.parseInt(row[2]));
                ps.setString(5, row[3]);
                ps.executeUpdate();
            }

            // 4️⃣ Fetch all records
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM callingtable");

            // 5️⃣ Display table
            out.println("<html><body style='font-family: Arial, sans-serif;'>");
            out.println("<h2>User Table from Database</h2>");
            out.println("<table border='1' cellpadding='8' cellspacing='0'>");
            out.println("<tr style='background-color:#f2f2f2;'><th>ID</th><th>Name</th><th>Age</th><th>Location</th></tr>");

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int age = rs.getInt("age");
                String location = rs.getString("location");

                out.println("<tr>");
                out.println("<td>" + id + "</td>");
                // ✅ Fixed link on the name column
                out.println("<td><a href='CalledServlet?id=" + id + "' style='text-decoration:none;color:#007bff;'>" + name + "</a></td>");
                out.println("<td>" + age + "</td>");
                out.println("<td>" + location + "</td>");
                out.println("</tr>");
            }

            out.println("</table>");
            out.println("</body></html>");

            conn.close();

        } catch (Exception e) {
            e.printStackTrace(out);
        }
    }
}
