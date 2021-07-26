<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="jwd" uri="custom" %>
<%@ page import="com.epam.jwd.model.Role" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Totalizator</title>
</head>
<body>
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
            See below to next moves:
            <br>
            <a href="${pageContext.request.contextPath}/controller?command=person_management_page">Person management</a>
            <br>
            <a href="${pageContext.request.contextPath}/controller?command=competition_management_page">Competition
                management</a>
            <br>
            <a href="${pageContext.request.contextPath}/controller?command=betslip_management_page">Betslip
                management</a>
            <br>
            <a href="${pageContext.request.contextPath}/controller?command=bet_management_page">Bet management</a>
            <br>
            <a href="${pageContext.request.contextPath}/controller?command=log_out">Log out</a>
            <br>
            <br>
        </c:if>
        <c:if test="${sessionScope.personRole eq Role.USER}">
            See below to next moves:
            <br>
            <a href="${pageContext.request.contextPath}/controller?command=competition_list_page">Competition list</a>
            <br>
            <a href="${pageContext.request.contextPath}/controller?command=betslip_list_page">Betslip list</a>
            <br>
            <br>
            <a href="${pageContext.request.contextPath}/controller?command=log_out">Log out</a>
        </c:if>
    </c:otherwise>
</c:choose>
<br>
<jwd:time/>
</body>
</html>