<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Log in</title>
</head>
<body>
<form action="${pageContext.request.contextPath}/controller?command=log_in" method="post">
    <label for="loginField">Login: </label>
    <input type="text" id="loginField" name="login">
    <br>
    <label for="passwordField">Password: </label>
    <input type="password" id="passwordField" name="password">
    <br>
    <input type="submit" value="Log in">
</form>
</body>
</html>
