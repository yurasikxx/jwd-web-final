<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="jwd" uri="custom" %>
<html>
<head>
    <title>Betslip management</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/person.css">
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/time.js"></script>
</head>
<body onload="time()">
<div id="margin-div">
    <div id="border-div">
        <ul id="menu">
            <li class="menu">
                <a class="menu"
                   href="${pageContext.request.contextPath}/controller?command=main_page"><b>Main</b>
                </a>
            </li>
            <li class="menu">
                <a class="menu"
                   href="${pageContext.request.contextPath}/controller?command=betslip_management_page"><b>Betslip
                    management</b>
                </a>
            </li>
            <li class="menu">
                <a class="menu" href="${pageContext.request.contextPath}/controller?command=log_out"><b>Log
                    out</b>
                </a>
            </li>
        </ul>
        <h1>Betslip management</h1>
        <ul id="operation">
            <li class="operation">
                <a class="operation"
                   href="${pageContext.request.contextPath}/controller?command=betslip_list_page">
                    <b>View all betslips</b>
                </a>
            </li>
            <li class="operation">
                <a class="operation"
                   href="${pageContext.request.contextPath}/controller?command=betslip_adding_page">
                    <b>Add betslip</b>
                </a>
            </li>
            <li class="operation">
                <a class="operation"
                   href="${pageContext.request.contextPath}/controller?command=betslip_changing_page">
                    <b>Change betslip</b>
                </a>
            </li>
            <li class="operation">
                <a class="operation"
                   href="${pageContext.request.contextPath}/controller?command=betslip_deleting_page">
                    <b>Delete betslip</b>
                </a>
            </li>
        </ul>
        <h5>
            <label id="time"></label>
            <br>
            <jwd:date/>
        </h5>
    </div>
</div>
</body>
</html>