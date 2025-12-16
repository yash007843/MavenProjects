package projects;

import java.io.IOException;
import java.sql.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/marksheet")
public class StudentMarksheetServlet extends HttpServlet {

    private static final String URL =
        "jdbc:sqlserver://localhost:1433;databaseName=FSWDJS;encrypt=true;trustServerCertificate=true";
    private static final String USER = "sa";
    private static final String PASSWORD = "12345678";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String regNo = request.getParameter("reg");

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String studentName = "";
        StringBuilder marks = new StringBuilder();
        marks.append("[");

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
                 PreparedStatement ps = con.prepareStatement(
                     "SELECT STUDENT_NAME, SUBJECT, MARKS_OBTAINED " +
                     "FROM Student_Marksheet WHERE REG_NO = ?")) {

                ps.setString(1, regNo);
                ResultSet rs = ps.executeQuery();

                boolean first = true;

                while (rs.next()) {

                    if (studentName.isEmpty()) {
                        studentName = rs.getString("STUDENT_NAME");
                    }

                    if (!first) marks.append(",");

                    marks.append("{")
                         .append("\"SUBJECT\":\"").append(rs.getString("SUBJECT")).append("\",")
                         .append("\"MARKS\":").append(rs.getInt("MARKS_OBTAINED"))
                         .append("}");

                    first = false;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        marks.append("]");

        String json =
            "{"
          + "\"REG_NO\":\"" + regNo + "\","
          + "\"STUDENT_NAME\":\"" + studentName + "\","
          + "\"MARKSHEET\":" + marks
          + "}";

        response.getWriter().print(json);
    }
}
