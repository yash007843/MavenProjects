package projects;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/CalledServlet")
public class DemoCalledServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // ✅ Simulated user details (extra data)
    private static final String[][] detailsData = {
        {"1", "Alice", "30", "New York", "alice@example.com", "555-1234"},
        {"2", "Bob", "25", "San Francisco", "bob@example.com", "555-5678"},
        {"3", "Charlie", "28", "Chicago", "charlie@example.com", "555-8765"}
    };

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String id = request.getParameter("id");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<html><head><title>User Details</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; background-color: #f9f9f9; }");
        out.println("table { border-collapse: collapse; width: 50%; margin: 20px auto; background-color: white; box-shadow: 0px 0px 10px rgba(0,0,0,0.1); }");
        out.println("th, td { border: 1px solid #ddd; padding: 10px; text-align: left; }");
        out.println("th { background-color: #007bff; color: white; }");
        out.println("h2 { text-align: center; }");
        out.println("a { display: block; text-align: center; margin-top: 20px; text-decoration: none; color: #007bff; }");
        out.println("</style></head><body>");

        out.println("<h2>Details for ID: " + id + "</h2>");

        String[] found = null;
        for (String[] row : detailsData) {
            if (row[0].equals(id)) {
                found = row;
                break;
            }
        }

        if (found != null) {
            out.println("<table>");
            out.println("<tr><th>ID</th><td>" + found[0] + "</td></tr>");
            out.println("<tr><th>Name</th><td>" + found[1] + "</td></tr>");
            out.println("<tr><th>Age</th><td>" + found[2] + "</td></tr>");
            out.println("<tr><th>Location</th><td>" + found[3] + "</td></tr>");
            out.println("<tr><th>Email</th><td>" + found[4] + "</td></tr>");
            out.println("<tr><th>Phone</th><td>" + found[5] + "</td></tr>");
            out.println("</table>");
        } else {
            out.println("<p style='text-align:center;'>No details found for ID: " + id + "</p>");
        }

        out.println("<a href='CallingServlet'>← Back to Table</a>");
        out.println("</body></html>");
    }
}
