<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Adding</title>
</head>
<body>
<c:choose>
    <c:when test="${not empty requestScope.error}">
        <p>${requestScope.error}</p>
    </c:when>
    <c:when test="${not empty requestScope.competition}">
        <form action="${pageContext.request.contextPath}/controller?command=competition_add" method="post">
            <label>${requestScope.competition}</label>
            <br>
            <label for="sportIdField">${requestScope.sport}</label>
            <br>
            <input type="number" id="sportIdField" name="sport">
            <br>
            <label for="homeTeamIdField">${requestScope.homeTeam}</label>
            <br>
            <input type="number" id="homeTeamIdField" name="homeTeam">
            <br>
            <label for="awayTeamIdField">${requestScope.awayTeam}</label>
            <br>
            <input type="number" id="awayTeamIdField" name="awayTeam">
            <br>
            <input type="submit" value="Add">
        </form>
        <br>
        <a href="${pageContext.request.contextPath}/controller?command=competition_management_page">Back to competition
            management</a>
    </c:when>
    <c:otherwise>
        Oops! Something went wrong...
    </c:otherwise>
</c:choose>
</body>
</html>