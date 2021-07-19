<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="jwd" uri="custom" %>
<%@ page import="com.epam.jwd.model.Role" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>1XPari Totalizator</title>
</head>
<body>
<jwd:welcome/>
<br>
<c:choose>
    <c:when test="${empty sessionScope.personName}">
        <a href="${pageContext.request.contextPath}/controller?command=log_in_page">Log in</a>
    </c:when>
    <c:otherwise>
        <c:if test="${sessionScope.personRole eq Role.ADMINISTRATOR}">
            <h1>Please click below to see all competitions: </h1>
            <br>
            <a href="${pageContext.request.contextPath}/controller?command=competition_page">Competition page</a>
            <br>
        </c:if>
        <a href="${pageContext.request.contextPath}/controller?command=log_out">Log out</a>
    </c:otherwise>
</c:choose>
<jwd:time/>
</body>
</html>