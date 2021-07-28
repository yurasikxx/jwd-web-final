<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Competition results committing</title>
</head>
<body>
<c:choose>
    <c:when test="${not empty requestScope.error}">
        <p>${requestScope.error}</p>
        <a href="${pageContext.request.contextPath}/controller?command=competition_results_committing_page">Try again</a>
    </c:when>
    <c:when test="${not empty requestScope.betHistory}">
        <form action="${pageContext.request.contextPath}/controller?command=commit_competition_results" method="post">
            <label>${requestScope.betHistory}</label>
            <br>
            <label for="competitionId">${requestScope.competition}</label>
            <br>
            <input type="number" id="competitionId" name="id">
            <br>
            <input type="submit" value="Commit">
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