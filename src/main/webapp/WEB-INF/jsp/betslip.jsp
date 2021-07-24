<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Betslip management</title>
</head>
<body>
Available betslip operations:
<br>
<a href="${pageContext.request.contextPath}/controller?command=betslip_list_page">View all betslips</a>
<br>
<a href="${pageContext.request.contextPath}/controller?command=betslip_adding_page">Add betslip</a>
<br>
<a href="${pageContext.request.contextPath}/controller?command=betslip_deleting_page">Delete betslip</a>
<br>
<a href="${pageContext.request.contextPath}/controller?command=main_page">Back to main</a>
<br>
</body>
</html>