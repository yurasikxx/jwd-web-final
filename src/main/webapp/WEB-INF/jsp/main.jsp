<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.epam.jwd.model.Role" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="jwd" uri="custom" %>
<html>
<head>
    <title>Totalizator</title>
</head>
<script>
    function time() {
        let date = new Date();
        let hours = date.getHours();
        let minutes = date.getMinutes();
        let seconds = date.getSeconds();
        document.getElementById("time").innerHTML = hours + ":" + minutes + ":" + seconds;
        setTimeout("time();", 1000);
    }
</script>
<body onload="time()">
<jwd:welcome/>
<br>
<br>
<c:choose>
    <c:when test="${empty sessionScope.personName}">
        <a href="${pageContext.request.contextPath}/controller?command=register_page">Sign up</a>
        <br>
        <a href="${pageContext.request.contextPath}/controller?command=log_in_page">Log in</a>
        <br>
    </c:when>
    <c:otherwise>
        <c:if test="${sessionScope.personRole eq Role.ADMINISTRATOR}">
            <a href="${pageContext.request.contextPath}/controller?command=person_management_page">Person management</a>
            <br>
            <a href="${pageContext.request.contextPath}/controller?command=competition_management_page">Competition
                management</a>
            <br>
            <a href="${pageContext.request.contextPath}/controller?command=betslip_list_page">Betslip list</a>
            <br>
            <a href="${pageContext.request.contextPath}/controller?command=bet_list_page">Bet list</a>
            <br>
            <a href="${pageContext.request.contextPath}/controller?command=log_out">Log out</a>
            <br>
        </c:if>
        <c:if test="${sessionScope.personRole eq Role.BOOKMAKER}">
            <a href="${pageContext.request.contextPath}/controller?command=competition_list_page">Competition list</a>
            <br>
            <a href="${pageContext.request.contextPath}/controller?command=betslip_management_page">Betslip
                management</a>
            <br>
            <a href="${pageContext.request.contextPath}/controller?command=log_out">Log out</a>
            <br>
        </c:if>
        <c:if test="${sessionScope.personRole eq Role.USER}">
            <a href="${pageContext.request.contextPath}/controller?command=competition_list_page">Competition list</a>
            <br>
            <a href="${pageContext.request.contextPath}/controller?command=betslip_list_page">Betslip list</a>
            <br>
            <a href="${pageContext.request.contextPath}/controller?command=person_bets_list_page">My bets</a>
            <br>
            <a href="${pageContext.request.contextPath}/controller?command=bet_adding_page">Place bet</a>
            <br>
            <a href="${pageContext.request.contextPath}/controller?command=log_out">Log out</a>
            <br>
        </c:if>
    </c:otherwise>
</c:choose>
<br>
<label id="time"></label>
<br>
<jwd:date/>
</body>
</html>