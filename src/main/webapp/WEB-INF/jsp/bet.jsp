<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Bet</title>
</head>
<body>
<c:if test="${not empty requestScope.bet}">
    Bets:
    <br>
    <c:forEach var="bet" items="${requestScope.bet}">
        ${bet}
        <br>
    </c:forEach>
</c:if>
</body>
</html>