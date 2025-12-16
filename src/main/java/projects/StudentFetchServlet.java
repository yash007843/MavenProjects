package projects;

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

@WebServlet("/login")
public class StudentFetchServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final String URL = "jdbc:sqlserver://localhost:1433;" + "databaseName=FSWDJS;" + "encrypt=true;trustServerCertificate=true";

    private static final String USER = "sa";
    private static final String PASSWORD = "12345678";
    private static final String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Ensure driver class is loaded (useful when driver is not on container classpath)
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            throw new ServletException("SQL Server JDBC Driver not found on classpath", e);
        }

        String regNo = request.getParameter("registernumber");

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Student Details</title>");
        out.println("<link rel=\"stylesheet\" " + "href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css\">");
        out.println("</head><body class='bg-light'>");
        out.println("<div class='container mt-5'>");
        out.println("<div class='card'>");
        out.println("<div class='card-header bg-success text-white'>Student Details</div>");
        out.println("<div class='card-body'>");

        out.println("<table class='table table-bordered table-striped'>");
        out.println("<thead><tr>");
        out.println("<th>Student Name</th>");
        out.println("<th>Register Number</th>");
        out.println("<th>Degree</th>");
        out.println("<th>Branch</th>");
        out.println("<th>Marksheet</th>");
        out.println("</tr></thead>");
        out.println("<tbody>");

        String baseSql =
                "SELECT [Student Name], [Register Number], Degree, Branch, Marksheet "
              + "FROM [Student Details]";

        boolean hasReg = regNo != null && !regNo.trim().isEmpty();
        String sql = hasReg ? baseSql + " WHERE [Register Number] = ?" : baseSql;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (hasReg) {
                ps.setString(1, regNo.trim());
            }

            try (ResultSet rs = ps.executeQuery()) {

                boolean anyRows = false;

                while (rs.next()) {
                    anyRows = true;
                    String name     = rs.getString("Student Name");
                    String register = rs.getString("Register Number");
                    String degree   = rs.getString("Degree");
                    String branch   = rs.getString("Branch");
                    // String marksheet = rs.getString("Marksheet");

                    out.println("<tr>");
                    out.println("<td>" + name + "</td>");
                    out.println("<td>" + register + "</td>");
                    out.println("<td>" + degree + "</td>");
                    out.println("<td>" + branch + "</td>");
                    out.println("<td><a href='javascript:void(0)' " + "onclick=\"loadMarksheet('" + register + "')\">View</a></td>");
                    out.println("</tr>");
                }

                if (!anyRows) {
                    out.println("<tr><td colspan='5' class='text-center text-danger'>");
                    out.println("No student found for Register Number: " + regNo + "</td></tr>");
                }
            }

        } catch (SQLException e) {
            out.println("<tr><td colspan='5' class='text-danger'>Error: "
                    + e.getMessage() + "</td></tr>");
        }

        out.println("</tbody></table>");

        // ===== Marksheet Section (AJAX will load data here) =====
        out.println("<hr>");
        out.println("<h4 class='mt-4'>Student Marksheet</h4>");
        out.println("<div id='marksheetStatus' class='mb-2 text-info'></div>");
        out.println("<div id='marksheetContainer'></div>");

        // =====  SCRIPT FUNCTION =====
        out.println("<script>");
        out.println("async function loadMarksheet(regNo) {");
        out.println("  const statusDiv = document.getElementById('marksheetStatus');");
        out.println("  const container = document.getElementById('marksheetContainer');");
        out.println("  statusDiv.innerHTML = 'Loading marksheet...';");
        out.println("  container.innerHTML = '';");
        out.println("");
        out.println("  try {");
        out.println("    const response = await fetch('" + request.getContextPath() + "/marksheet?reg=' + encodeURIComponent(regNo));");
        out.println("    if (!response.ok) throw new Error('Server error');");
        out.println("    const data = await response.json();");
        out.println("");
        out.println("    let html = `<table class='table table-bordered table-striped mt-3'>");
        out.println("      <thead class='table-dark'>");
        out.println("        <tr><th>Subject</th><th>Marks</th></tr>");
        out.println("      </thead><tbody>`;");
        out.println("");
        out.println("    data.MARKSHEET.forEach(item => {");
        out.println("      if (!item.SUBJECT) return;");
        out.println("      html += `<tr><td>${item.SUBJECT}</td><td>${item.MARKS}</td></tr>`;");
        out.println("    });");
        out.println("");
        out.println("    html += '</tbody></table>';");
        out.println("    container.innerHTML = html;");
        out.println("    statusDiv.innerHTML = 'Marksheet loaded for Register No: ' + data.REG_NO;");
        out.println("  } catch (e) {");
        out.println("    statusDiv.innerHTML = '<span class=\"text-danger\">Error loading marksheet</span>';");
        out.println("  }");
        out.println("}");
        out.println("</script>");

        out.println("<a href='" + request.getContextPath() + "/' class='btn btn-primary mt-3'>Back to Login</a>");
        out.println("</div></div></div>");
        out.println("</body></html>");
        out.close();
    }
}
