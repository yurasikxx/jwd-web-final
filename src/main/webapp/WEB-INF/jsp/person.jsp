<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Person management</title>
</head>
<body>
Available person operations:
<br>
<a href="${pageContext.request.contextPath}/controller?command=person_list_page">View all persons</a>
<br>
<a href="${pageContext.request.contextPath}/controller?command=person_delete_page">Delete person</a>
<br>
</body>
</html>