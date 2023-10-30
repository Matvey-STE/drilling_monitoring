<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Wells Information</title>
</head>
<body>
<%@include file="header.jsp"%>
<div>
    <h2>Wells Data Information</h2>
    <c:forEach var="well" items="${requestScope.welldata}">
        <h4>
                ${well.id()}) Company name: ${well.companyName()}
            Field name: ${well.fieldName()}
            Well cluster: ${well.wellCluster()} Well: ${well.well()}
        </h4>
        <a href='/well?surfaceDataId=${well.id()}'>Surface Data</a>
        <a href='/well?downholeDataId=${well.id()}'>Downhole Data</a>
    </c:forEach>
</div>
</body>
</html>
