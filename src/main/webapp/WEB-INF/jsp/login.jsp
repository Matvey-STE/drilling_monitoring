<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Login</title>
</head>
<body>
<h1>User Login</h1>
<form method="post" action="${pageContext.request.contextPath}/login">
    <label for="email">Email:</label>
    <input type="email" id="email" name="email" required><br><br>

    <label for="password">Password:</label>
    <input type="password" id="password" name="password" required><br><br>

    <button type="submit">Login</button>
    <a href="${pageContext.request.contextPath}/registration">
        <button type="button">Register</button>

    </a>
</form>
<c:if test="${requestScope.error != null}">
    <div style="color: red">
        <c:forEach var="error" items="${requestScope.error}">
            <span>Email or password is not correct</span>
        </c:forEach>
    </div>
</c:if>
</body>
</html>