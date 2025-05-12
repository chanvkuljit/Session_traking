<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="javax.servlet.http.HttpSession" %>
<!DOCTYPE html>
<html>
<head><title>Confirmation</title></head>
<body>
  <%
    HttpSession userSession = request.getSession(false);
    String username = (userSession != null) ? (String) userSession.getAttribute("username") : "Guest";
  %>

  <h2>Thank you, <%= username %>!</h2>
  <p>Your email <%= request.getAttribute("email") %> has been recorded successfully.</p>
  <a href="userList">View all submissions</a> |
  <a href="form.html">Submit another</a>
</body>
</html>