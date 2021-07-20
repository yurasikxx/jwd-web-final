<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Betslip</title>
</head>
<body>
<c:if test="${not empty requestScope.betslip}">
    Betslips:
    <br>
    <c:forEach var="betslip" items="${requestScope.betslip}">
        ${betslip}
        <br>
    </c:forEach>
</c:if>
</body>
</html>