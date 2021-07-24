<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Person management</title>
</head>
<body>
Available person operations:
<br>
<a href="${pageContext.request.contextPath}/controller?command=person_list_page">View all persons</a>
<br>
<a href="${pageContext.request.contextPath}/controller?command=person_adding_page">Add person</a>
<br>
<a href="${pageContext.request.contextPath}/controller?command=person_deleting_page">Delete person</a>
<br>
<a href="${pageContext.request.contextPath}/controller?command=main_page">Back to main</a>
<br>
</body>
</html>