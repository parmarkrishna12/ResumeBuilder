import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import java.io.*;
import java.sql.*;
import java.nio.file.*;

@WebServlet("/template1Servlet")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2, // 2MB
    maxFileSize = 1024 * 1024 * 10,      // 10MB
    maxRequestSize = 1024 * 1024 * 50    // 50MB
)
public class template1Servlet extends HttpServlet {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/resume_builder?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    // Path for storing images inside Tomcat's webapps folder
    private static final String IMAGE_UPLOAD_DIR = "C:/Tomcat/webapps/resume_images/";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            // Ensure MySQL Driver is Loaded
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Ensure directory exists
            File uploadDir = new File(IMAGE_UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            // Get Form Data
            String name = request.getParameter("name");
            String jobTitle = request.getParameter("jobTitle");
            String phone = request.getParameter("phone");
            String email = request.getParameter("email");
            String address = request.getParameter("address");
            String profile = request.getParameter("profile");
            String skills = request.getParameter("skills");
            String experience = request.getParameter("experience");
            String education = request.getParameter("education");
            String projects = request.getParameter("projects");
            String certifications = request.getParameter("certifications");

            // Handle File Upload (Profile Image)
            Part filePart = request.getPart("profileImage");
            String fileName = null;
            if (filePart != null && filePart.getSize() > 0) {
                fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
                String imagePath = IMAGE_UPLOAD_DIR + fileName;
                filePart.write(imagePath);
            }

            // Store Data in Database
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            String sql = "INSERT INTO template1 (name, jobTitle, phone, email, address, profile, skills, experience, education, projects, certifications, profileImage) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, name);
            statement.setString(2, jobTitle);
            statement.setString(3, phone);
            statement.setString(4, email);
            statement.setString(5, address);
            statement.setString(6, profile);
            statement.setString(7, skills);
            statement.setString(8, experience);
            statement.setString(9, education);
            statement.setString(10, projects);
            statement.setString(11, certifications);
            statement.setString(12, fileName); // Store only file name in DB

            int rowsInserted = statement.executeUpdate();
            connection.close();

            if (rowsInserted > 0) {
                showPreview(response, fileName);
            } else {
                out.println("<h2>Error saving resume data.</h2>");
            }

        } catch (ClassNotFoundException e) {
            out.println("<h2>Error: MySQL JDBC Driver not found.</h2>");
        } catch (SQLException e) {
            out.println("<h2>Error saving resume: " + e.getMessage() + "</h2>");
        }
    }

    private void showPreview(HttpServletResponse response, String fileName) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM template1 ORDER BY id DESC LIMIT 1")) {

            if (resultSet.next()) {
                out.println("<!DOCTYPE html>");
                out.println("<html lang='en'><head><meta charset='UTF-8'><meta name='viewport' content='width=device-width, initial-scale=1.0'>");
                out.println("<title>Resume Preview</title>");
                out.println("<link href='https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css' rel='stylesheet'>");
                out.println("</head><body class='bg-gray-100'>");

                out.println("<div class='max-w-4xl mx-auto bg-white shadow-lg rounded-lg overflow-hidden p-6'>");
                out.println("<div class='flex flex-col md:flex-row'>");

                // Left Side (Profile, Contact, Skills)
                out.println("<div class='w-full md:w-1/3 bg-gray-800 text-white p-6'>");
                out.println("<div class='text-center mb-6'>");

                // Profile Image
                String imagePath = (fileName != null) ? "http://localhost:8080/resume_images/" + resultSet.getString("profileImage") : "https://placehold.co/150x150";
                out.println("<img src='" + imagePath + "' class='w-32 h-32 rounded-full mx-auto' alt='Profile Image'/>");

                out.println("<h2 class='text-2xl font-bold mt-4'>" + resultSet.getString("name") + "</h2>");
                out.println("<p class='text-gray-400'>" + resultSet.getString("jobTitle") + "</p></div>");

                out.println("<h3 class='text-xl font-bold mb-2'>Contact</h3>");
                out.println("<p>📞 " + resultSet.getString("phone") + "</p>");
                out.println("<p>✉ " + resultSet.getString("email") + "</p>");
                out.println("<p>📍 " + resultSet.getString("address") + "</p>");

                // Skills - Fixed Display
               // Skills - Split and Show List
out.println("<h3 class='text-xl font-bold mt-4'>Skills</h3>");
String skills = resultSet.getString("skills");
if (skills != null && !skills.trim().isEmpty()) {
    out.println("<ul class='list-disc list-inside'>");
    for (String skill : skills.split(",")) {
        out.println("<li>" + skill.trim() + "</li>");
    }
    out.println("</ul>");
} else {
    out.println("<p>No skills mentioned.</p>");
}

                out.println("</ul>");

                out.println("</div>");

                // Right Side (Profile, Experience, Education, Projects, Certifications)
                out.println("<div class='w-full md:w-2/3 p-6'>");

                out.println("<h3 class='text-2xl font-bold mb-2'>Profile</h3>");
                out.println("<p>" + resultSet.getString("profile") + "</p>");

                out.println("<h3 class='text-2xl font-bold mt-4'>Experience</h3>");
                out.println("<p>" + resultSet.getString("experience") + "</p>");

                out.println("<h3 class='text-2xl font-bold mt-4'>Education</h3>");
                out.println("<p>" + resultSet.getString("education") + "</p>");

                out.println("<h3 class='text-2xl font-bold mt-4'>Projects</h3>");
                out.println("<p>" + resultSet.getString("projects") + "</p>");

                out.println("<h3 class='text-2xl font-bold mt-4'>Certifications</h3>");
                out.println("<p>" + resultSet.getString("certifications") + "</p>");

                out.println("</div></div></div></body></html>");
            }
        } catch (SQLException e) {
            out.println("<h2>Error loading resume data: " + e.getMessage() + "</h2>");
        }
    }
}
