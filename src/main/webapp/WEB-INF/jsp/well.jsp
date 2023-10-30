<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Well</title>
</head>
<body>
<%@include file="header.jsp"%>

<c:if test="${not empty requestScope.surface}">
    <h2>Surface Information:</h2>
    <c:forEach var="surface" items="${requestScope.surface}">
        <h4>
            Date: ${surface.measuredDate()}
            Depth: ${surface.measuredDepth()}
            Hole depth: ${surface.holeDepth()}
            TVD: ${surface.tvDepth()}
        </h4>
    </c:forEach>
</c:if>
<c:if test="${not empty requestScope.downhole}">
    <h2>Downhole Information:</h2>
    <c:forEach var="downhole" items="${requestScope.downhole}">
        <h4>
            Company name: ${downhole.wellDataReadDto().companyName()}
            Field name: ${downhole.wellDataReadDto().fieldName()}
            Well cluster: ${downhole.wellDataReadDto().wellCluster()}
            Well: ${downhole.wellDataReadDto().well()}
        </h4>
        <h5><a href='/well?directionalId=${downhole.id()}'>Directional Info</a></h5>
        <h5><a href='/well?gammaId=${downhole.id()}'>Gamma Info</a></h5>
    </c:forEach>
</c:if>
<c:if test="${not empty requestScope.gamma}">
    <h2>Gamma Information:</h2>
    <c:forEach var="gamma" items="${requestScope.gamma}">
        <h4>
            Date: ${gamma.measureDate()}
            Downhole depth: ${gamma.measuredDepth()}
            Gamma value: ${gamma.grcx()}
        </h4>
    </c:forEach>
</c:if>
<c:if test="${not empty requestScope.directional}">
    <h2>Directional Information:</h2>
    <c:forEach var="directional" items="${requestScope.directional}">
        <h4>
            Date: ${directional.measureDate()}
            Measured depth: ${directional.measuredDepth()}
            Inclination: ${directional.inc()}
            Azimuth corrected: ${directional.azCorr()}
            Azimuth true: ${directional.azTrue()}
        </h4>
    </c:forEach>
</c:if>


</body>
</html>
