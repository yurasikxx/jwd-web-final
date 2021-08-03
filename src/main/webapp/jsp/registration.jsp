<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="jwd" uri="custom" %>
<html>
<head>
    <title>Registration</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/time.js"></script>
</head>
<body onload="time()">
<div id="auth-margin-div">
    <div id="auth-border-div">
        <c:choose>
            <c:when test="${not empty requestScope.error}">
                <p>${requestScope.error}</p>
                <h2>
                    <a class="changing" href="${pageContext.request.contextPath}/controller?command=register_page">
                        Try again
                    </a>
                </h2>
            </c:when>
            <c:otherwise>
                <form action="${pageContext.request.contextPath}/controller?command=register" method="post">
                    <label for="loginField">Login: </label>
                    <input type="text" id="loginField" name="login" placeholder="firstname_lastname">
                    <br>
                    <label for="passwordField">Password: </label>
                    <input type="password" id="passwordField" name="password" placeholder="password123">
                    <br>
                    <input type="submit" value="Sign up">
                </form>
            </c:otherwise>
        </c:choose>
    </div>
</div>
<h2><a class="changing" href="${pageContext.request.contextPath}/controller?command=main_page">Back to main</a></h2>
<h5>
    <label id="time"></label>
    <br>
    <jwd:date/>
</h5>
</body>
</html>