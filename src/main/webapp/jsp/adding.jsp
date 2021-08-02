<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="jwd" uri="custom" %>
<html>
<head>
    <title>Adding</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/time.js"></script>
</head>
<body onload="time()">
<c:choose>
    <c:when test="${not empty requestScope.error}">
        ${requestScope.error}
        <br>
        <br>
        <c:if test="${not empty requestScope.person}">
            <a href="${pageContext.request.contextPath}/controller?command=person_adding_page">${requestScope.person}</a>
        </c:if>
        <c:if test="${not empty requestScope.competition}">
            <a href="${pageContext.request.contextPath}/controller?command=competition_adding_page">${requestScope.competition}</a>
        </c:if>
        <c:if test="${not empty requestScope.betslip}">
            <a href="${pageContext.request.contextPath}/controller?command=betslip_adding_page">${requestScope.betslip}</a>
        </c:if>
        <c:if test="${not empty requestScope.bet}">
            <a href="${pageContext.request.contextPath}/controller?command=bet_adding_page">${requestScope.bet}</a>
        </c:if>
    </c:when>
    <c:when test="${not empty requestScope.person}">
        <form action="${pageContext.request.contextPath}/controller?command=person_add" method="post">
            <label>${requestScope.person}</label>
            <br>
            <label for="loginField">Login: </label>
            <br>
            <input type="text" id="loginField" name="login">
            <br>
            <label for="passwordField">Password: </label>
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
            <label for="homeTeamIdField">Home team ID: </label>
            <br>
            <input type="number" id="homeTeamIdField" name="homeTeamId">
            <br>
            <label for="awayTeamIdField">Away team ID: </label>
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
            <label for="competitionIdField">Competition ID: </label>
            <br>
            <input type="number" id="competitionIdField" name="competitionId">
            <br>
            <label for="betTypeIdField">Bet type ID: </label>
            <br>
            <input type="number" id="betTypeIdField" name="betTypeId">
            <br>
            <label for="coefficientField">Coefficient: </label>
            <br>
            <input type="number" id="coefficientField" name="coefficient">
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
            <label>Balance: ${sessionScope.personBalance}</label>
            <br>
            <label for="betslipIdField">Betslip ID: </label>
            <br>
            <input type="number" id="betslipIdField" name="betslipId">
            <br>
            <label for="betTotalField">Bet total ID: </label>
            <br>
            <input type="number" id="betTotalField" name="betTotal">
            <br>
            <input type="submit" value="Add">
        </form>
    </c:when>
    <c:otherwise>
        Oops! Something went wrong...
    </c:otherwise>
</c:choose>
<br>
<a href="${pageContext.request.contextPath}/controller?command=main_page">Back to main page</a>
<h5>
    <label id="time"></label>
    <br>
    <jwd:date/>
</h5>
</body>
</html>