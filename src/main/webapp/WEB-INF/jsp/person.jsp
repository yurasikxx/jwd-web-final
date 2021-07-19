<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Persons</title>
</head>
<body>
<c:if test="${not empty requestScope.person}">
    <h3>Persons</h3>
    <ul>
        <c:forEach var="person" items="${requestScope.person}">
            <li>${person}</li>
        </c:forEach>
    </ul>
</c:if>
</body>
</html>