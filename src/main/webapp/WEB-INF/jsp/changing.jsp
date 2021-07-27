<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Changing</title>
</head>
<body>
<c:choose>
    <c:when test="${not empty requestScope.error}">
        <p>${requestScope.error}</p>
        <c:if test="${not empty requestScope.person}">
            <a href="${pageContext.request.contextPath}/controller?command=person_changing_page">${requestScope.person}</a>
        </c:if>
        <c:if test="${not empty requestScope.competition}">
            <a href="${pageContext.request.contextPath}/controller?command=competition_changing_page">${requestScope.competition}</a>
        </c:if>
        <c:if test="${not empty requestScope.betslip}">
            <a href="${pageContext.request.contextPath}/controller?command=betslip_changing_page">${requestScope.betslip}</a>
        </c:if>
        <c:if test="${not empty requestScope.bet}">
            <a href="${pageContext.request.contextPath}/controller?command=bet_changing_page">${requestScope.bet}</a>
        </c:if>
    </c:when>
    <c:when test="${not empty requestScope.person}">
        <form action="${pageContext.request.contextPath}/controller?command=person_change" method="post">
            <label>${requestScope.person}</label>
            <br>
            <label for="personIdField">${requestScope.id}</label>
            <br>
            <input type="number" id="personIdField" name="id">
            <br>
            <label for="loginField">${requestScope.login}</label>
            <br>
            <input type="text" id="loginField" name="login">
            <br>
            <label for="passwordField">${requestScope.password}</label>
            <br>
            <input type="password" id="passwordField" name="password">
            <br>
            <label for="balanceField">${requestScope.balance}</label>
            <br>
            <input type="number" id="balanceField" name="balance">
            <br>
            <input type="submit" value="Change">
        </form>
        <br>
        <a href="${pageContext.request.contextPath}/controller?command=person_management_page">Back to person
            management</a>
    </c:when>
    <c:when test="${not empty requestScope.competition}">
        <form action="${pageContext.request.contextPath}/controller?command=competition_change" method="post">
            <label>${requestScope.competition}</label>
            <br>
            <label for="competitionIdField">${requestScope.id}</label>
            <br>
            <input type="number" id="competitionIdField" name="id">
            <br>
            <label for="homeTeamIdField">${requestScope.homeTeam}</label>
            <br>
            <input type="number" id="homeTeamIdField" name="homeTeamId">
            <br>
            <label for="awayTeamIdField">${requestScope.awayTeam}</label>
            <br>
            <input type="number" id="awayTeamIdField" name="awayTeamId">
            <br>
            <input type="submit" value="Change">
        </form>
        <br>
        <a href="${pageContext.request.contextPath}/controller?command=competition_management_page">Back to competition
            management</a>
    </c:when>
    <c:when test="${not empty requestScope.betslip}">
        <form action="${pageContext.request.contextPath}/controller?command=betslip_change" method="post">
            <label>${requestScope.betslip}</label>
            <br>
            <label for="betslipIdField">${requestScope.id}</label>
            <br>
            <input type="number" id="betslipIdField" name="id">
            <br>
            <label for="betslipCompetitionIdField">${requestScope.betslipCompetition}</label>
            <br>
            <input type="number" id="betslipCompetitionIdField" name="competitionId">
            <br>
            <label for="betTypeIdField">${requestScope.betType}</label>
            <br>
            <input type="number" id="betTypeIdField" name="betTypeId">
            <br>
            <label for="coefficientField">${requestScope.coefficient}</label>
            <br>
            <input type="text" id="coefficientField" name="coefficient">
            <br>
            <input type="submit" value="Change">
        </form>
        <br>
        <a href="${pageContext.request.contextPath}/controller?command=betslip_management_page">Back to betslip
            management</a>
    </c:when>
    <c:when test="${not empty requestScope.bet}">
        <form action="${pageContext.request.contextPath}/controller?command=bet_change" method="post">
            <label>${requestScope.bet}</label>
            <br>
            <label for="idField">${requestScope.id}</label>
            <br>
            <input type="number" id="idField" name="id">
            <br>
            <label for="betBetslipIdField">${requestScope.betBetslip}</label>
            <br>
            <input type="number" id="betBetslipIdField" name="betslipId">
            <br>
            <label for="betTotalField">${requestScope.betTotal}</label>
            <br>
            <input type="number" id="betTotalField" name="betTotal">
            <br>
            <label for="betPersonIdField">${requestScope.betPerson}</label>
            <br>
            <input type="text" id="betPersonIdField" name="personId">
            <br>
            <input type="submit" value="Change">
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