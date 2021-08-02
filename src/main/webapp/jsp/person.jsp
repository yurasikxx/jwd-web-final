<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="jwd" uri="custom" %>
<html>
<head>
    <title>Person management</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/time.js"></script>
</head>
<body onload="time()">
<div id="default-margin-div">
    <div id="default-border-div">
        <ul id="menu">
            <li class="menu">
                <a class="menu"
                   href="${pageContext.request.contextPath}/controller?command=main_page"><b>Main</b>
                </a>
            </li>
            <li class="menu">
                <a class="menu"
                   href="${pageContext.request.contextPath}/controller?command=betslip_list_page">
                    <b>Betslip list</b>
                </a>
            </li>
            <li class="menu">
                <a class="menu" href="${pageContext.request.contextPath}/controller?command=log_out">
                    <b>Log out</b>
                </a>
            </li>
            <li class="menu">
                <a class="menu"
                   href="${pageContext.request.contextPath}/controller?command=person_management_page">
                    <b>Person management</b>
                </a>
            </li>
            <li class="menu">
                <a class="menu"
                   href="${pageContext.request.contextPath}/controller?command=competition_management_page">
                    <b>Competition management</b>
                </a>
            </li>
            <li class="menu">
                <a class="menu"
                   href="${pageContext.request.contextPath}/controller?command=bet_management_page">
                    <b>Bet management</b>
                </a>
            </li>
        </ul>
        <h1>Person management</h1>
        <ul id="default-operation">
            <li class="default-operation">
                <a class="default-operation"
                   href="${pageContext.request.contextPath}/controller?command=person_list_page">
                    <b>View all persons</b>
                </a>
            </li>
            <li class="default-operation">
                <a class="default-operation"
                   href="${pageContext.request.contextPath}/controller?command=person_adding_page">
                    <b>Add person</b>
                </a>
            </li>
            <li class="default-operation">
                <a class="default-operation"
                   href="${pageContext.request.contextPath}/controller?command=person_changing_page">
                    <b>Change person</b>
                </a>
            </li>
            <li class="default-operation">
                <a class="default-operation"
                   href="${pageContext.request.contextPath}/controller?command=person_deleting_page">
                    <b>Delete person</b>
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