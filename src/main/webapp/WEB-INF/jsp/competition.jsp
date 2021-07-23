<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Competition management</title>
</head>
<body>
Available competition operations:
<br>
<a href="${pageContext.request.contextPath}/controller?command=competition_list_page">View all competitions</a>
<br>
<a href="${pageContext.request.contextPath}/controller?command=competition_adding_page">Add competition</a>
<br>
<a href="${pageContext.request.contextPath}/controller?command=competition_deleting_page">Delete competition</a>
<br>
<a href="${pageContext.request.contextPath}/controller?command=main_page">Back to main</a>
<br>
</body>
</html>