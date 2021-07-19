<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Register</title>
</head>
<body>
<c:choose>
    <c:when test="${not empty requestScope.error}">
        <p>${requestScope.error}</p>
        <a href="${pageContext.request.contextPath}/controller?command=register_page">Try again</a>
    </c:when>
    <c:otherwise>
        <form action="${pageContext.request.contextPath}/controller?command=register" method="post">
            <label for="loginField">Login: </label>
            <input type="text" id="loginField" name="login">
            <br>
            <label for="passwordField">Password: </label>
            <input type="password" id="passwordField" name="password">
            <br>
            <input type="submit" value="Sign up">
        </form>
    </c:otherwise>
</c:choose>
</body>
</html>