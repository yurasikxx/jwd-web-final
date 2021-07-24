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
        <c:if test="${not empty requestScope.competition}">
            <a href="${pageContext.request.contextPath}/controller?command=competition_adding_page">${requestScope.competition}</a>
        </c:if>
    </c:when>
    <c:when test="${not empty requestScope.person}">
        <form action="${pageContext.request.contextPath}/controller?command=person_add" method="post">
            <label>${requestScope.person}</label>
            <br>
            <label for="loginField">${requestScope.login}</label>
            <br>
            <input type="text" id="loginField" name="login">
            <br>
            <label for="passwordField">${requestScope.password}</label>
            <br>
            <input type="password" id="passwordField" name="password">
            <br>
            <input type="submit" value="Add">
        </form>
        <br>
        <a href="${pageContext.request.contextPath}/controller?command=person_management_page">Back to person
            management</a>
    </c:when>
    <c:when test="${not empty requestScope.competition}">
        <form action="${pageContext.request.contextPath}/controller?command=competition_add" method="post">
            <label>${requestScope.competition}</label>
            <br>
            <label for="sportIdField">${requestScope.sport}</label>
            <br>
            <input type="number" id="sportIdField" name="sportId">
            <br>
            <label for="homeTeamIdField">${requestScope.homeTeam}</label>
            <br>
            <input type="number" id="homeTeamIdField" name="homeTeamId">
            <br>
            <label for="awayTeamIdField">${requestScope.awayTeam}</label>
            <br>
            <input type="number" id="awayTeamIdField" name="awayTeamId">
            <br>
            <input type="submit" value="Add">
        </form>
        <br>
        <a href="${pageContext.request.contextPath}/controller?command=competition_management_page">Back to competition
            management</a>
    </c:when>
    <c:when test="${not empty requestScope.betslip}">
        <form action="${pageContext.request.contextPath}/controller?command=betslip_add" method="post">
            <label>${requestScope.betslip}</label>
            <br>
            <label for="competitionIdField">${requestScope.betslipCompetition}</label>
            <br>
            <input type="number" id="competitionIdField" name="competitionId">
            <br>
            <label for="betTypeIdField">${requestScope.betType}</label>
            <br>
            <input type="number" id="betTypeIdField" name="betTypeId">
            <br>
            <label for="coefficientField">${requestScope.coefficient}</label>
            <br>
            <input type="text" id="coefficientField" name="coefficient">
            <br>
            <input type="submit" value="Add">
        </form>
        <br>
        <a href="${pageContext.request.contextPath}/controller?command=betslip_management_page">Back to betslip
            management</a>
    </c:when>
    <c:when test="${not empty requestScope.bet}">
        <form action="${pageContext.request.contextPath}/controller?command=bet_add" method="post">
            <label>${requestScope.bet}</label>
            <br>
            <label for="betslipIdField">${requestScope.betBetslip}</label>
            <br>
            <input type="number" id="betslipIdField" name="betslipId">
            <br>
            <label for="betTotalField">${requestScope.betTotal}</label>
            <br>
            <input type="number" id="betTotalField" name="betTotal">
            <br>
            <label for="personIdField">${requestScope.betPerson}</label>
            <br>
            <input type="text" id="personIdField" name="personId">
            <br>
            <input type="submit" value="Add">
        </form>
        <br>
        <a href="${pageContext.request.contextPath}/controller?command=bet_management_page">Back to bet management</a>
    </c:when>
    <c:otherwise>
        Oops! Something went wrong...
    </c:otherwise>
</c:choose>
<br>
<a href="${pageContext.request.contextPath}/controller?command=main_page">Back to main page</a>
</body>
</html>