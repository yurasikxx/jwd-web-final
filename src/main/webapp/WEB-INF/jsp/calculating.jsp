<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Calculating</title>
</head>
<body>
<c:choose>
    <c:when test="${not empty requestScope.error}">
        <p>${requestScope.error}</p>
        <a href="${pageContext.request.contextPath}/controller?command=bet_calculating_page">Try again</a>
    </c:when>
    <c:when test="${not empty requestScope.betHistory}">
        <form action="${pageContext.request.contextPath}/controller?command=bet_calculate" method="post">
            <label>${requestScope.betHistory}</label>
            <br>
            <label for="betId">${requestScope.bet}</label>
            <br>
            <input type="number" id="betId" name="id">
            <br>
            <input type="submit" value="Calculate">
        </form>
    </c:when>
    <c:otherwise>
        Oops! Something went wrong...
        <br>
        <a href="${pageContext.request.contextPath}/controller?command=main_page">Back to main</a>
    </c:otherwise>
</c:choose>
<br>
<a href="${pageContext.request.contextPath}/controller?command=bet_management_page">Back to bet management</a>
<br>
<a href="${pageContext.request.contextPath}/controller?command=main_page">Back to main</a>
</body>
</html>