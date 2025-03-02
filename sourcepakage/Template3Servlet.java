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

@WebServlet("/Template3Servlet")
public class Template3Servlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        // Retrieve form data
        String name = request.getParameter("name");
        String jobTitle = request.getParameter("job_title");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String profileSummary = request.getParameter("profile_summary");
        String education = request.getParameter("education");
        String skills = request.getParameter("skills");
        String workExperience = request.getParameter("work_experience");
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            // Load JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Establish connection (update database credentials accordingly)
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/resume_builder", "root", "root");
            
            // Insert data into database
            String sql = "INSERT INTO template3 (name, job_title, email, phone, address, profile_summary, education, skills, work_experience) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setString(2, jobTitle);
            pstmt.setString(3, email);
            pstmt.setString(4, phone);
            pstmt.setString(5, address);
            pstmt.setString(6, profileSummary);
            pstmt.setString(7, education);
            pstmt.setString(8, skills);
            pstmt.setString(9, workExperience);
            
            int rowsInserted = pstmt.executeUpdate();
            
            if (rowsInserted > 0) {
                response.sendRedirect("template3.html"); // Redirect to preview page after saving data
            } else {
                out.println("<h3>Error saving data. Please try again.</h3>");
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<h3>Database connection error: " + e.getMessage() + "</h3>");
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
