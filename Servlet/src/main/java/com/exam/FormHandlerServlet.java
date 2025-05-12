package com.exam;import java.io.*;

import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet("/formHandler")
public class FormHandlerServlet extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String JDBC_URL = "jdbc:mysql://localhost:3306/testdb";
    private final String DB_USER = "root";
    private final String DB_PASS = "password";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("username");
        String email = request.getParameter("email");

        // Simple validation
        if (name == null || name.isEmpty() || email == null || !email.contains("@")) {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<h3>Error: Invalid input. Please enter a valid name and email.</h3>");
            return;
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);

            String sql = "INSERT INTO users (name, email) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, email);

            int rows = stmt.executeUpdate();

            stmt.close();
            conn.close();

            if (rows > 0) {
                HttpSession session = request.getSession(); // SESSION TRACKING
                session.setAttribute("username", name);

                request.setAttribute("username", name);
                request.setAttribute("email", email);
                RequestDispatcher rd = request.getRequestDispatcher("confirmation.jsp");
                rd.forward(request, response);
            }else {
                response.setContentType("text/html");
                PrintWriter out = response.getWriter();
                out.println("<h3>Failed to insert data.</h3>");
            }

        } catch (Exception e) {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<h3>Error: " + e.getMessage() + "</h3>");
        }
    }
}
