package projects ;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/fetch")
public class CandidateFetchServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    final String URL = "jdbc:sqlserver://localhost:1433;databaseName=FSWDJS;encrypt=true;trustServerCertificate=true";
    final String USER = "sa";
    final String PASSWORD = "12345678";
    final String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    Connection conn = null;

    // Initialize the database connection when the servlet starts
    public void init() throws ServletException {
        try {
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Handle GET requests
 protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    response.setContentType("text/html");
    PrintWriter out = response.getWriter();

    String id = request.getParameter("ID");
    System.out.println("ID received from request: '" + id + "'");

    boolean summary;

    try {
        String sql;
        PreparedStatement pstmt;

        if (id != null && !id.trim().isEmpty()) {
            // Filter by specific candidate ID
            sql = "SELECT * FROM Profile WHERE ID = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, Integer.parseInt(id.trim()));
            summary = false;
        } else {
            // Show all candidates
            sql = "SELECT * FROM Profile";
            pstmt = conn.prepareStatement(sql);
            summary = true;
        }

        ResultSet rs = pstmt.executeQuery();

        out.println("<html><body>");
        out.println("<h3>Candidate Records</h3>");
        out.println("<table border='1' cellpadding='5' cellspacing='0'>");
        out.println("<tr><th>Name</th><th>ID</th><th>Profile</th></tr>");

        boolean hasData = false;

        while (rs.next()) {
            hasData = true;
            out.println("<tr>");
            out.println("<td><a href='candidateprofile?name=" + rs.getString("Name") + "' target='_blank'>" + rs.getString("Name") + "</a></td>");
            out.println("<td>" + rs.getInt("ID") + "</td>");
            out.println("<td>" + rs.getString("Profile") + "</td>");
            out.println("</tr>");
        }

        if (!hasData) {
            if (summary) {
                out.println("<tr><td colspan='3'>No candidates found.</td></tr>");
            } else {
                out.println("<tr><td colspan='3'>No candidate found with that ID.</td></tr>");
            }
        }

        out.println("</table>");
        out.println("</body></html>");

        rs.close();
        pstmt.close();

    } catch (SQLException e) {
        out.println("<p style='color:red;'>SQL Error: " + e.getMessage() + "</p>");
    } catch (NumberFormatException e) {
        out.println("<p style='color:red;'>Invalid ID format. Please enter a numeric ID.</p>");
    }
}

}