<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Competitions</title>
</head>
<body>
<c:if test="${not empty requestScope.competition}">
    Competitions:
    <br>
    <c:forEach var="competition" items="${requestScope.competition}">
        ${competition}
        <br>
    </c:forEach>
</c:if>
</body>
</html>