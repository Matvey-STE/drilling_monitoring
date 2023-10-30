<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Registration</title>
</head>
<body>
<h1>User Registration</h1>
<form method="post" action="/registration">
    <label for="username">Username:</label>
    <input type="text" id="username" name="username" required><br><br>

    <label for="email">Email:</label>
    <input type="email" id="email" name="email" required><br><br>

    <label for="password">Password:</label>
    <input type="password" id="password" name="password" required><br><br>

    <label for="role">Role:</label>
    <select id="role" name="role">
    <c:forEach var="role" items="${requestScope.role}">
        <option label="${role}">${role}</option><br>
    </c:forEach>
    </select><br><br>
    <label for="first_name">First Name:</label>
    <input type="text" id="first_name" name="first_name"><br><br>

    <label for="last_name">Last Name:</label>
    <input type="text" id="last_name" name="last_name"><br><br>

    <input type="submit" value="Register">
    <a href="${pageContext.request.contextPath}/login">
        <button type="button">Login</button></a>
</form>
<c:if test="${not empty requestScope.errors}">
    <div style="color: red">
        <c:forEach var="error" items="${requestScope.errors}">
            <span>${error.message}</span>
            <br>
        </c:forEach>
    </div>
</c:if>
<c:if test="${param.error != null}">
    <div style="color: red">
        <c:forEach var="error" items="${param.error}">
            <span>Username or email is already exist</span>
        </c:forEach>
    </div>
</c:if>
</body>
</html>
