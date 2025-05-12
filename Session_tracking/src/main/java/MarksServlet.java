import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/MarksServlet")
public class MarksServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
  
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		 HttpSession session = request.getSession();//get current session
		 //Retrieves the value of the hidden <input> field named "step" from the HTML
	     String step = request.getParameter("step");
	     if ("1".equals(step)) {
	            session.setAttribute("theory1", Integer.parseInt(request.getParameter("theory1")));
	            session.setAttribute("theory2", Integer.parseInt(request.getParameter("theory2")));
	            session.setAttribute("theory3", Integer.parseInt(request.getParameter("theory3")));

	            // Redirect to practical marks form
	            response.sendRedirect("Practical.html");
	        } 
	        else if ("2".equals(step)) {
	            session.setAttribute("practical1", Integer.parseInt(request.getParameter("practical1")));
	            session.setAttribute("practical2", Integer.parseInt(request.getParameter("practical2")));
	            session.setAttribute("practical3", Integer.parseInt(request.getParameter("practical3")));

	            // Redirect to doGet() for final result display
	            response.sendRedirect("MarksServlet");
	        }
	
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html");
	    PrintWriter out = response.getWriter();

	    // Only check if session exists, no need to check individual attributes
	    HttpSession session = request.getSession(false); 

	 /*   if (session == null) {
	        out.println("<h2>Session Expired! Please enter marks again.</h2>");
	        out.println("<a href='theory.html'>Start Again</a>");
	        return;
	    }
*/
	    // Retrieve marks (No need for null checks, as all fields are required)
	    int theory1 = (int) session.getAttribute("theory1");
	    int theory2 = (int) session.getAttribute("theory2");
	    int theory3 = (int) session.getAttribute("theory3");
	    int practical1 = (int) session.getAttribute("practical1");
	    int practical2 = (int) session.getAttribute("practical2");
	    int practical3 = (int) session.getAttribute("practical3");

	    // Calculate Total and Percentage
	    int total = theory1 + theory2 + theory3 + practical1 + practical2 + practical3;
	    double percentage = total / 6.0;

	    // Display Final Result
	    out.println("<h2>Final Result</h2>");
	    out.println("<p>Total Marks: " + total + " / 600</p>");
	    out.println("<p>Percentage: " + percentage + "%</p>");
	   }



}
