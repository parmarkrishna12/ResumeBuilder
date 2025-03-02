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

@WebServlet("/template2servlet")
public class template2servlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        // Retrieve form fields
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String website = request.getParameter("website");
        String summary = request.getParameter("summary");
        String workTitle = request.getParameter("work_experience_job_title");
        String workCompany = request.getParameter("work_experience_company");
        String workStart = request.getParameter("work_experience_start_date");
        String workDesc = request.getParameter("work_experience_description");
        String educationDegree = request.getParameter("education_degree");
        String educationInstitution = request.getParameter("education_institution");
        String educationStart = request.getParameter("education_start_date");
        String skills = request.getParameter("skills");
        String certifications = request.getParameter("certifications");
        String profileImage = request.getParameter("profile_image"); // Profile image URL

        if (name == null || name.trim().isEmpty()) {
            out.println("Error: Name field cannot be empty");
            return;
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/resume_builder", "root", "root");

            // Insert resume data into the database
            String sql = "INSERT INTO template2 (name, email, phone, address, website, summary, work_title, work_company, work_start, work_desc, education_degree, education_institution, education_start, skills, certifications, profile_image) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setString(4, address);
            ps.setString(5, website);
            ps.setString(6, summary);
            ps.setString(7, workTitle);
            ps.setString(8, workCompany);
            ps.setString(9, workStart);
            ps.setString(10, workDesc);
            ps.setString(11, educationDegree);
            ps.setString(12, educationInstitution);
            ps.setString(13, educationStart);
            ps.setString(14, skills);
            ps.setString(15, certifications);
            ps.setString(16, profileImage);
            ps.executeUpdate();
            con.close();

            // Show resume preview in the same servlet
            displayPreview(out, name, email, phone, address, website, summary, workTitle, workCompany, workStart, workDesc, educationDegree, educationInstitution, educationStart, skills, certifications, profileImage);

        } catch (Exception e) {
            out.println("Error: " + e.getMessage());
        }
    }

    private void displayPreview(PrintWriter out, String name, String email, String phone, String address, String website, String summary,
                                String workTitle, String workCompany, String workStart, String workDesc, String educationDegree,
                                String educationInstitution, String educationStart, String skills, String certifications, String profileImage) {
        out.println("<html lang='en'><head>");
        out.println("<meta charset='utf-8'/>");
        out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'/>");
        out.println("<title>Resume Preview</title>");
        out.println("<script src='https://cdn.tailwindcss.com'></script>");
        out.println("<link href='https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css' rel='stylesheet'/>");
        out.println("<link href='https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap' rel='stylesheet'/>");
        out.println("<style>");
        out.println("body { font-family: 'Roboto', sans-serif; background-color: #f4f4f4; padding: 20px; }");
        out.println(".container { max-width: 800px; background: white; padding: 20px; box-shadow: 0px 0px 10px rgba(0,0,0,0.1); border-radius: 8px; }");
        out.println(".profile-img { width: 100px; height: 100px; border-radius: 50%; border: 3px solid #3498db; }");
        out.println("</style>");
        out.println("</head><body>");
        out.println("<div class='container mx-auto bg-white shadow-lg rounded-lg overflow-hidden'>");

        // Profile section
        out.println("<div class='bg-blue-600 p-6 text-white flex items-center'>");
        if (profileImage != null && !profileImage.isEmpty()) {
            out.println("<img src='" + profileImage + "' class='profile-img' alt='Profile Image'/>");
        }
        out.println("<div class='ml-4'>");
        out.println("<h1 class='text-3xl font-bold'>" + name + "</h1>");
        out.println("<p>" + email + "</p>");
        out.println("</div>");
        out.println("</div>");

        // Contact Info
        out.println("<div class='p-6'>");
        out.println("<h2 class='text-xl font-bold text-gray-800'>Contact Information</h2>");
        out.println("<p><strong>Phone:</strong> " + phone + "</p>");
        out.println("<p><strong>Address:</strong> " + address + "</p>");
        out.println("<p><strong>Website:</strong> <a href='" + website + "' target='_blank' class='text-blue-500'>" + website + "</a></p>");

        // Summary
        out.println("<h2 class='text-xl font-bold text-gray-800 mt-6'>Professional Summary</h2>");
        out.println("<p class='text-gray-600'>" + summary + "</p>");

        // Work Experience
        out.println("<h2 class='text-xl font-bold text-gray-800 mt-6'>Work Experience</h2>");
        out.println("<p><strong>" + workTitle + "</strong> at " + workCompany + "</p>");
        out.println("<p>" + workStart + "</p>");
        out.println("<p class='text-gray-600'>" + workDesc + "</p>");

        // Education
        out.println("<h2 class='text-xl font-bold text-gray-800 mt-6'>Education</h2>");
        out.println("<p><strong>" + educationDegree + "</strong> from " + educationInstitution + "</p>");
        out.println("<p>Graduated: " + educationStart + "</p>");

        // Skills
        out.println("<h2 class='text-xl font-bold text-gray-800 mt-6'>Skills</h2>");
        out.println("<p>" + skills + "</p>");

        // Certifications
        out.println("<h2 class='text-xl font-bold text-gray-800 mt-6'>Certifications</h2>");
        out.println("<p>" + certifications + "</p>");

        out.println("</div></div></body></html>");
    }
}
