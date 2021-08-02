<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="jwd" uri="custom" %>
<html>
<head>
    <title>Error</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/time.js"></script>
</head>
<body onload="time()">
<div id="auth-margin-div">
    <div id="auth-border-div">
        <h1>Something went wrong...</h1>
        <h2>
            <a class="changing" href="${pageContext.request.contextPath}/controller?command=main_page">Main page</a>
        </h2>
        <h5>
            <label id="time"></label>
            <br>
            <jwd:date/>
        </h5>
    </div>
</div>
</body>
</html>