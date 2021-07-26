<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.epam.jwd.model.Role" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Viewing</title>
</head>
<body>
<c:if test="${not empty requestScope.person}">
    Persons
    <br>
    <c:forEach var="person" items="${requestScope.person}">
        ${person}
        <br>
    </c:forEach>
    <br>
    <a href="${pageContext.request.contextPath}/controller?command=person_management_page">Back to person management</a>
</c:if>
<c:if test="${not empty requestScope.competition}">
    Competitions
    <br>
    <c:forEach var="competition" items="${requestScope.competition}">
        ${competition}
        <br>
    </c:forEach>
    <c:if test="${sessionScope.personRole eq Role.ADMINISTRATOR}">
        <br>
        <a href="${pageContext.request.contextPath}/controller?command=competition_management_page">Back to competition
            management</a>
    </c:if>
</c:if>
<c:if test="${not empty requestScope.betslip}">
    Betslips:
    <br>
    <c:forEach var="betslip" items="${requestScope.betslip}">
        ${betslip}
        <br>
    </c:forEach>
    <c:if test="${sessionScope.personRole eq Role.BOOKMAKER}">
        <br>
        <a href="${pageContext.request.contextPath}/controller?command=betslip_management_page">Back to betslip
            management</a>
    </c:if>
</c:if>
<c:if test="${not empty requestScope.bet}">
    Bets:
    <br>
    <c:forEach var="bet" items="${requestScope.bet}">
        ${bet}
        <br>
    </c:forEach>
    <c:if test="${sessionScope.personRole eq Role.ADMINISTRATOR}">
        <br>
        <a href="${pageContext.request.contextPath}/controller?command=bet_management_page">Back to bet management</a>
    </c:if>
</c:if>
<c:if test="${not empty requestScope.personBet}">
    Person bets:
    <br>
    <c:forEach var="personBet" items="${requestScope.personBet}">
        ${personBet}
        <br>
    </c:forEach>
</c:if>
<br>
<a href="${pageContext.request.contextPath}/controller?command=main_page">Back to main</a>
</body>
</html>