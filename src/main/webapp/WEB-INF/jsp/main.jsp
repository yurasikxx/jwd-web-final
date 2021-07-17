<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>1XPari Totalizator</title>
</head>
<body>
<c:if test="${not empty sessionScope.person}">
    <h1>Hello, ${sessionScope.person}</h1>
</c:if>
<h1>Please click below to see all competitions: </h1>
<br>
<a href="${pageContext.request.contextPath}/controller?command=competition_page">Competition page</a>
<br>
<c:choose>
    <c:when test="${empty sessionScope.person}">
        <a href="${pageContext.request.contextPath}/controller?command=log_in_page">Log in</a>
    </c:when>
    <c:otherwise>
        <a href="${pageContext.request.contextPath}/controller?command=log_out">Log out</a>
    </c:otherwise>
</c:choose>
</body>
</html>
