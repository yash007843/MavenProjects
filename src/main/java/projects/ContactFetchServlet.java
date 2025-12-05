package projects;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/fetch")
public class ContactFetchServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    final String URL =  "jdbc:sqlserver://localhost:1433;databaseName=FSWDJS;encrypt=true;trustServerCertificate=true";
    final String USER = "sa";
    final String PASSWORD = "12345678";
    final String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    Connection conn = null;

    public void init() throws ServletException {
        try {
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String Location = request.getParameter("brand");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<html><body>");
        out.println("<h3>Student Contacts</h3>");
        out.println("<table border=1><tr><td>Name</td><td>Mobile_number</td><td>City</td><td>Pincode</td></tr>");          
        try {
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM Contacts";
            if (Location != null && !Location.isEmpty()) {
                sql += " WHERE Place = '" + Location + "'";
            }
            //  out.println("<tr><td>" + sql + "</td><td>" + " " + "</td><td>" + " " + "</td><td>" + " " + "</td></tr>");
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String name = rs.getString("Person_Name");
                String Mobile_Number = rs.getString("Mobile_Number");
                String Place = rs.getString("place");
                String Pincode = rs.getString("Pin_code");
               out.println("<tr><td>" + name + "</td><td>" + Mobile_Number + "</td><td>" + Place + "</td><td>" + Pincode + "</td></tr>");
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            //e.printStackTrace();
            out.println("<tr><td>" + e.getMessage() + "</td><td>" + " " + "</td><td>" + " " + "</td><td>" + " " + "</td></tr>");
        }
        out.println("</table></body></html>");
        out.close();
    }

    public void destroy() {
        try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
    }
}

