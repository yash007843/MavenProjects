package projects;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/candidateprofile")
public class CandidateProfileServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String nameParam = request.getParameter("name");

        if (nameParam == null || nameParam.trim().isEmpty()) {
            out.println("<h3>No Candidate Name Provided!</h3>");
            return;
        }

        try {
            // ✅ Database connection
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection conn = DriverManager.getConnection(
                    "jdbc:sqlserver://localhost:1433;databaseName=FSWDJS;encrypt=false",
                    "sa",
                    "12345678"
            );

            // ✅ Use your actual column names
            String sql = "SELECT [Candidate Name], [Company Name], [Role], [Contact Number], [Email ID] " +
                         "FROM Profile WHERE Name = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nameParam.trim());

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                out.println("<html><body style='font-family: Arial, sans-serif; margin: 40px;'>");
                out.println("<h2>Candidate Details</h2>");
                out.println("<table border='1' cellpadding='8' cellspacing='0' style='border-collapse: collapse;'>");

                out.println("<tr><th>Candidate Name</th><td>" + rs.getString("Candidate Name") + "</td></tr>");
                out.println("<tr><th>Company Name</th><td>" + rs.getString("Company Name") + "</td></tr>");
                out.println("<tr><th>Role</th><td>" + rs.getString("Role") + "</td></tr>");
                out.println("<tr><th>Contact Number</th><td>" + rs.getString("Contact Number") + "</td></tr>");
                out.println("<tr><th>Email ID</th><td>" + rs.getString("Email ID") + "</td></tr>");

                out.println("</table>");
                out.println("</body></html>");
            } else {
                out.println("<h3>No candidate found for name: " + nameParam + "</h3>");
            }

            conn.close();
        } catch (Exception e) {
            e.printStackTrace(out);
        }
    }
}
