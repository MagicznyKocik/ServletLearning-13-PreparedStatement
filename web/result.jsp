<%--
  Created by IntelliJ IDEA.
  User: azlup
  Date: 22.02.2019
  Time: 13:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Welcome</title>
</head>
<body>
    <h1>Welcome <%= session.getAttribute("username") %></h1>
    <h2>You have following privileges: <%= session.getAttribute("privileges") %> </h2>
</body>
</html>
