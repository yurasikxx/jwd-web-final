<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="jwd" uri="custom" %>
<html>
<head>
    <title>Deleting</title>
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
            <a href="${pageContext.request.contextPath}/controller?command=person_deleting_page">${requestScope.person}</a>
        </c:if>
        <c:if test="${not empty requestScope.competition}">
            <a href="${pageContext.request.contextPath}/controller?command=competition_deleting_page">${requestScope.competition}</a>
        </c:if>
        <c:if test="${not empty requestScope.betslip}">
            <a href="${pageContext.request.contextPath}/controller?command=betslip_deleting_page">${requestScope.betslip}</a>
        </c:if>
        <c:if test="${not empty requestScope.bet}">
            <a href="${pageContext.request.contextPath}/controller?command=bet_deleting_page">${requestScope.bet}</a>
        </c:if>
    </c:when>
    <c:when test="${not empty requestScope.person}">
        <form action="${pageContext.request.contextPath}/controller?command=person_delete" method="post">
            <label>${requestScope.person}</label>
            <br>
            <label for="personDeleteField">Enter person ID: </label>
            <br>
            <input type="number" id="personDeleteField" name="id">
            <br>
            <input type="submit" value="Delete">
        </form>
        <br>
        <a href="${pageContext.request.contextPath}/controller?command=person_management_page">Back to person
            management</a>
    </c:when>
    <c:when test="${not empty requestScope.competition}">
        <form action="${pageContext.request.contextPath}/controller?command=competition_delete" method="post">
            <label>${requestScope.competition}</label>
            <br>
            <label for="competitionDeleteField">Competition ID: </label>
            <br>
            <input type="number" id="competitionDeleteField" name="id">
            <br>
            <input type="submit" value="Delete">
        </form>
        <br>
        <a href="${pageContext.request.contextPath}/controller?command=competition_management_page">Back to competition
            management</a>
    </c:when>
    <c:when test="${not empty requestScope.betslip}">
        <form action="${pageContext.request.contextPath}/controller?command=betslip_delete" method="post">
            <label>${requestScope.betslip}</label>
            <br>
            <label for="betslipDeleteField">Betslip ID: </label>
            <br>
            <input type="number" id="betslipDeleteField" name="id">
            <br>
            <input type="submit" value="Delete">
        </form>
        <br>
        <a href="${pageContext.request.contextPath}/controller?command=betslip_management_page">Back to betslip
            management</a>
    </c:when>
    <c:when test="${not empty requestScope.bet}">
        <form action="${pageContext.request.contextPath}/controller?command=bet_delete" method="post">
            <label>${requestScope.bet}</label>
            <br>
            <label for="betDeleteField">Bet ID: </label>
            <br>
            <input type="number" id="betDeleteField" name="id">
            <br>
            <input type="submit" value="Delete">
        </form>
        <br>
        <a href="${pageContext.request.contextPath}/controller?command=bet_management_page">Back to bet management</a>
    </c:when>
    <c:otherwise>
        Oops! Something went wrong...
    </c:otherwise>
</c:choose>
<br>
<a href="${pageContext.request.contextPath}/controller?command=main_page">Back to main</a>
<h5>
    <label id="time"></label>
    <br>
    <jwd:date/>
</h5>
</body>
</html>