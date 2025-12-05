package projects;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

@WebServlet("/api/data")
public class MyAjaxProject_1 extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String typeParam = Optional.ofNullable(req.getParameter("type"))
        .orElse("Android Mobiles");

        String type = typeParam.replaceAll("\\s+", "").toLowerCase();

        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");

        try (PrintWriter out = resp.getWriter()) {

            // ANDROID MOBILES
            if ("androidmobiles".equals(type)) {
                out.print("{\"items\": ["
                        + "{\"id\":201, \"brand\":\"Samsung\", \"model\":\"Galaxy S24\", \"price\":69999},"
                        + "{\"id\":202, \"brand\":\"OnePlus\", \"model\":\"OnePlus 12\", \"price\":58999},"
                        + "{\"id\":203, \"brand\":\"Vivo\", \"model\":\"Vivo X100\", \"price\":49999}"
                        + "]}");
            }
            // LAPTOPS
            else if ("laptop".equals(type)) {
                out.print("{\"items\": ["
                        + "{\"id\":1, \"name\":\"Dell Inspiron\", \"price\":45000},"
                        + "{\"id\":2, \"name\":\"HP Pavilion\", \"price\":52000},"
                        + "{\"id\":3, \"name\":\"Lenovo ThinkPad\", \"price\":60000}"
                        + "]}");
            }
            // APPLE PRODUCTS
            else if ("appleproducts".equals(type)) {
                out.print("{\"items\": ["
                        + "{\"id\":301, \"name\":\"iPhone 15 Pro\", \"price\":134900},"
                        + "{\"id\":302, \"name\":\"MacBook Pro 16\", \"price\":249900},"
                        + "{\"id\":303, \"name\":\"iPad Pro 12.9\", \"price\":99900}"
                        + "]}");
            }
            // DEFAULT
            else {
                out.print("{\"items\": ["
                        + "{\"id\":1, \"name\":\"Dell Inspiron\", \"price\":45000},"
                        + "{\"id\":2, \"name\":\"HP Pavilion\", \"price\":52000},"
                        + "{\"id\":3, \"name\":\"Lenovo ThinkPad\", \"price\":60000}"
                        + "]}");
            }

            out.flush();
        }
    }
}
