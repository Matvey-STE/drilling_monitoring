<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<html>
<head>
    <title>Wells Information</title>
</head>
<body>
<%--<!--<%@include file="../../../webapp/WEB-INF/jsp/header.jsp"%>-->--%>
<div>
    <h2>Wells Data Information</h2>
    <th:forEach var="well" items="${requestScope.welldata}">
        <h4>
                ${well.id()}) Company name: ${well.companyName()}
            Field name: ${well.fieldName()}
            Well cluster: ${well.wellCluster()} Well: ${well.well()}
        </h4>
        <a href='/well?surfaceDataId=${well.id()}'>Surface Data</a>
        <a href='/well?downholeDataId=${well.id()}'>Downhole Data</a>
    </th:forEach>
</div>
</body>
</html>
