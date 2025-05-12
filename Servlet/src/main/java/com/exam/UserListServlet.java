package com.exam;import java.io.*;

import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet("/userList")
public class UserListServlet extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String JDBC_URL = "jdbc:mysql://localhost:3306/testdb";
    private final String DB_USER = "root";
    private final String DB_PASS = "password";
    private final int PAGE_SIZE = 5; // records per page

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Get current page and search keyword
        int page = 1;
        String keyword = request.getParameter("keyword");
        String pageParam = request.getParameter("page");
        if (pageParam != null) {
            page = Integer.parseInt(pageParam);
        }

        // Offset for SQL LIMIT
        int offset = (page - 1) * PAGE_SIZE;

        out.println("<h2>User List</h2>");
        out.println("<form method='get'>");
        out.println("Search: <input type='text' name='keyword' value='" + (keyword != null ? keyword : "") + "'/>");
        out.println("<input type='submit' value='Filter'/>");
        out.println("</form><br>");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);

            // Query with optional filter
            String sql = "SELECT name, email FROM users WHERE name LIKE ? OR email LIKE ? LIMIT ? OFFSET ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            String search = (keyword == null || keyword.isEmpty()) ? "%" : "%" + keyword + "%";
            stmt.setString(1, search);
            stmt.setString(2, search);
            stmt.setInt(3, PAGE_SIZE);
            stmt.setInt(4, offset);

            ResultSet rs = stmt.executeQuery();

            out.println("<table border='1'><tr><th>Name</th><th>Email</th></tr>");
            while (rs.next()) {
                out.println("<tr><td>" + rs.getString("name") + "</td><td>" + rs.getString("email") + "</td></tr>");
            }
            out.println("</table>");

            rs.close();
            stmt.close();

            // Count total rows for pagination
            String countSql = "SELECT COUNT(*) FROM users WHERE name LIKE ? OR email LIKE ?";
            PreparedStatement countStmt = conn.prepareStatement(countSql);
            countStmt.setString(1, search);
            countStmt.setString(2, search);
            ResultSet countRs = countStmt.executeQuery();
            int totalRecords = 0;
            if (countRs.next()) {
                totalRecords = countRs.getInt(1);
            }

            int totalPages = (int) Math.ceil(totalRecords / (double) PAGE_SIZE);

            // Page links
            out.println("<br>Page: ");
            for (int i = 1; i <= totalPages; i++) {
                out.print("<a href='userList?page=" + i);
                if (keyword != null && !keyword.isEmpty()) {
                    out.print("&keyword=" + keyword);
                }
                out.println("'>" + i + "</a> ");
            }

            out.println("<br><br><a href='form.html'>Back to Form</a>");

            countRs.close();
            countStmt.close();
            conn.close();

        } catch (Exception e) {
            out.println("<p>Error: " + e.getMessage() + "</p>");
        }
    }
}
