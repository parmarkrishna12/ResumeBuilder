import java.io.IOException;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/Template5Servlet")
public class Template5Servlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String name = request.getParameter("name");
        String jobTitle = request.getParameter("job_title");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        String address = request.getParameter("address");
        String website = request.getParameter("website");
        String profileSummary = request.getParameter("profile_summary");
        String education = request.getParameter("education");
        String skills = request.getParameter("skills");
        String experience = request.getParameter("experience");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/resume_builder", "root", "root");

            String sql = "INSERT INTO template5 (name, job_title, phone, email, address, website, profile_summary, education, skills, experience) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, jobTitle);
            stmt.setString(3, phone);
            stmt.setString(4, email);
            stmt.setString(5, address);
            stmt.setString(6, website);
            stmt.setString(7, profileSummary);
            stmt.setString(8, education);
            stmt.setString(9, skills);
            stmt.setString(10, experience);
            
            stmt.executeUpdate();
            
            request.setAttribute("name", name);
            request.setAttribute("job_title", jobTitle);
            request.setAttribute("phone", phone);
            request.setAttribute("email", email);
            request.setAttribute("address", address);
            request.setAttribute("website", website);
            request.setAttribute("profile_summary", profileSummary);
            request.setAttribute("education", education);
            request.setAttribute("skills", skills);
            request.setAttribute("experience", experience);

            request.getRequestDispatcher("template5.html").forward(request, response);
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

