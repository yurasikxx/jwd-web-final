<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Bet</title>
</head>
<body>
Available bet operations:
<br>
<a href="${pageContext.request.contextPath}/controller?command=bet_list_page">View all bets</a>
<br>
<a href="${pageContext.request.contextPath}/controller?command=bet_deleting_page">Delete bet</a>
<br>
<a href="${pageContext.request.contextPath}/controller?command=main_page">Back to main</a>
<br>
</body>
</html>