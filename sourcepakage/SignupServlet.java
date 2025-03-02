import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/SignupServlet")
public class SignupServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Establish connection to the database
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/resume_builder", "root", "root");

            // Prepare SQL query to insert new user
            String query = "INSERT INTO users (username, password) VALUES (?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            // Execute the query
            int result = pstmt.executeUpdate();

            if (result > 0) {
                // Successful sign-up, redirect to home page with success indicator
                response.sendRedirect("index.html?signup_success=true"); // Pass success parameter to the homepage
            } else {
                // Handle unsuccessful sign-up
                response.setContentType("text/html");
                PrintWriter out = response.getWriter();
                out.println("<h1>Sign-up failed!</h1>");
                out.println("<a href='signup.html'>Try Again</a>");
            }

            // Close the connection
            conn.close();
        } catch (Exception e) {
            // Handle any exceptions during the database interaction
            e.printStackTrace();
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<h1>Error during sign-up</h1>");
            out.println("<p>" + e.getMessage() + "</p>");
            out.println("<a href='signup.html'>Try Again</a>");
        }
    }
}
