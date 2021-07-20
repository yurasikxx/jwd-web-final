<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Persons</title>
</head>
<body>
<c:if test="${not empty requestScope.person}">
    Persons
    <br>
    <c:forEach var="person" items="${requestScope.person}">
        ${person}
        <br>
    </c:forEach>
</c:if>
</body>
</html>