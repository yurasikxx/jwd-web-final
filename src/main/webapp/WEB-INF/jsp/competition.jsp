<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <title>Competitions</title>
</head>
<body>
<h2>Competition list</h2>
<c:if test="${not empty requestScope.competition}">
    <h3>Competitions: </h3>
    <ul>
        <c:forEach var="competition" items="${requestScope.competition}">
            <li>${competition}</li>
        </c:forEach>
    </ul>
</c:if>
</body>
</html>
