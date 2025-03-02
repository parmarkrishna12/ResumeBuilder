import java.io.IOException;
import java.net.URLEncoder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/ContactServlet")
public class ContactServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String subject = request.getParameter("subject");
        String message = request.getParameter("message");
        
        // Constructing the WhatsApp message
        String whatsappMessage = "Hello, my name is " + name + ". "
                + "My email is " + email + ". "
                + "Subject: " + subject + "\n" + message;
        
        // Encoding the message to be URL-friendly
        String encodedMessage = URLEncoder.encode(whatsappMessage, "UTF-8");
        
        // WhatsApp API URL (replace with an actual phone number)
        String whatsappUrl = "https://api.whatsapp.com/send?phone=9321642759&text=" + encodedMessage;
        
        // Redirecting to WhatsApp
        response.sendRedirect(whatsappUrl);
    }
}
