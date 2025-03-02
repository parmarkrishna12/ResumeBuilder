import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish connection to the database
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/resume_builder", "root", "root");

            // Prepare SQL query to check user credentials
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            // Execute the query
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // Successful login, redirect to the index page with templates
                response.sendRedirect("index.html?login_success=true&username=" + username); // Pass username and success flag to the homepage
            } else {
                // Invalid credentials
                response.setContentType("text/html");
                PrintWriter out = response.getWriter();
                out.println("<h1>Invalid username or password!</h1>");
                out.println("<a href='login.html'>Try Again</a>");
            }

            // Close the connection
            conn.close();
        } catch (Exception e) {
            // Handle any exceptions during the database interaction
            e.printStackTrace();
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<h1>Error during login</h1>");
            out.println("<p>" + e.getMessage() + "</p>");
            out.println("<a href='login.html'>Try Again</a>");
        }
    }
}
