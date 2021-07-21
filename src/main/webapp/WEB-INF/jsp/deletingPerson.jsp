<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Person delete</title>
</head>
<body>
<form action="${pageContext.request.contextPath}/controller?command=person_delete" method="post">
    <label for="deleteField">Enter person id that need to delete: </label>
    <br>
    <input type="number" id="deleteField" name="id">
    <br>
    <input type="submit" value="Delete">
</form>
<br>
<a href="${pageContext.request.contextPath}/controller?command=person_management_page">Back</a>
<br>
<a href="${pageContext.request.contextPath}/controller?command=main_page">Back to main</a>
</body>
</html>