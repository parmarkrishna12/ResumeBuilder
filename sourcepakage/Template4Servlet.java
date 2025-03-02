import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Template4Servlet")
public class Template4Servlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String name = request.getParameter("name");
        String jobTitle = request.getParameter("job_title");
        String dateOfBirth = request.getParameter("date_of_birth");
        String address = request.getParameter("address");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        String website = request.getParameter("website");
        String twitter = request.getParameter("twitter");
        String profileSummary = request.getParameter("profile_summary");
        String education = request.getParameter("education");
        String skills = request.getParameter("skills");
        String experience = request.getParameter("experience");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/resume_builder", "root", "root");

            String sql = "INSERT INTO template4 (name, job_title, date_of_birth, address, phone, email, website, twitter, profile_summary, education, skills, experience) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, jobTitle);
            stmt.setString(3, dateOfBirth);
            stmt.setString(4, address);
            stmt.setString(5, phone);
            stmt.setString(6, email);
            stmt.setString(7, website);
            stmt.setString(8, twitter);
            stmt.setString(9, profileSummary);
            stmt.setString(10, education);
            stmt.setString(11, skills);
            stmt.setString(12, experience);
            
            stmt.executeUpdate();
            conn.close();

            // Redirect back to the resume page in read-only mode
            response.sendRedirect("template4.html");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
