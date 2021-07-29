<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Changing</title>
</head>
<body>
<c:choose>
    <c:when test="${not empty requestScope.error}">
        ${requestScope.error}
        <br>
        <br>
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
        <c:if test="${not empty requestScope.betHistory}">
            <a href="${pageContext.request.contextPath}/controller?command=competition_results_committing_page">${requestScope.betHistory}</a>
        </c:if>
    </c:when>
    <c:when test="${not empty requestScope.person}">
        <form action="${pageContext.request.contextPath}/controller?command=person_change" method="post">
            <label>${requestScope.person}</label>
            <br>
            <label for="personIdField">Person ID: </label>
            <br>
            <input type="number" id="personIdField" name="id">
            <br>
            <label for="loginField">Login: </label>
            <br>
            <input type="text" id="loginField" name="login">
            <br>
            <label for="passwordField">Password: </label>
            <br>
            <input type="password" id="passwordField" name="password">
            <br>
            <label for="balanceField">Balance: </label>
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
            <label for="competitionIdField">Competition ID: </label>
            <br>
            <input type="number" id="competitionIdField" name="id">
            <br>
            <label for="homeTeamIdField">Home team ID: </label>
            <br>
            <input type="number" id="homeTeamIdField" name="homeTeamId">
            <br>
            <label for="awayTeamIdField">Away team ID: </label>
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
            <label for="betslipIdField">Betslip ID: </label>
            <br>
            <input type="number" id="betslipIdField" name="id">
            <br>
            <label for="betslipCompetitionIdField">Competition ID: </label>
            <br>
            <input type="number" id="betslipCompetitionIdField" name="competitionId">
            <br>
            <label for="betTypeIdField">Bet type ID: </label>
            <br>
            <input type="number" id="betTypeIdField" name="betTypeId">
            <br>
            <label for="coefficientField">Coefficient: </label>
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
            <label for="idField">Bet ID: </label>
            <br>
            <input type="number" id="idField" name="id">
            <br>
            <label for="betBetslipIdField">Betslip ID: </label>
            <br>
            <input type="number" id="betBetslipIdField" name="betslipId">
            <br>
            <label for="betTotalField">Bet total: </label>
            <br>
            <input type="number" id="betTotalField" name="betTotal">
            <br>
            <label for="betPersonIdField">Person ID: </label>
            <br>
            <input type="text" id="betPersonIdField" name="personId">
            <br>
            <input type="submit" value="Change">
        </form>
        <br>
        <a href="${pageContext.request.contextPath}/controller?command=bet_management_page">Back to bet management</a>
    </c:when>
    <c:when test="${not empty requestScope.betHistory}">
        <form action="${pageContext.request.contextPath}/controller?command=commit_competition_results" method="post">
            <label>${requestScope.betHistory}</label>
            <br>
            <label for="competitionId">Competition ID: </label>
            <br>
            <input type="number" id="competitionId" name="id">
            <br>
            <input type="submit" value="Commit">
        </form>
    </c:when>
    <c:otherwise>
        Oops! Something went wrong...
    </c:otherwise>
</c:choose>
<br>
<a href="${pageContext.request.contextPath}/controller?command=main_page">Back to main page</a>
</body>
</html>