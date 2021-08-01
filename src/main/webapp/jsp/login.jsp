<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="jwd" uri="custom" %>
<html>
<head>
    <title>Log in</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/authorization.css">
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/time.js"></script>
</head>
<body onload="time()">
<div id="margin-div">
    <div id="border-div">
        <c:choose>
            <c:when test="${not empty requestScope.error}">
                <p>${requestScope.error}</p>
                <a href="${pageContext.request.contextPath}/controller?command=log_in_page">Try again</a>
                <br>
                <a href="${pageContext.request.contextPath}/controller?command=main_page">Back to main</a>
            </c:when>
            <c:otherwise>
                <form action="${pageContext.request.contextPath}/controller?command=log_in" method="post">
                    <label for="loginField">Login</label>
                    <input type="text" id="loginField" name="login">
                    <br>
                    <label for="passwordField">Password</label>
                    <input type="password" id="passwordField" name="password">
                    <br>
                    <input type="submit" value="Log in">
                </form>
            </c:otherwise>
        </c:choose>
    </div>
</div>
<h5>
    <label id="time"></label>
    <br>
    <jwd:date/>
</h5>
</body>
</html>