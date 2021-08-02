<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="jwd" uri="custom" %>
<html>
<head>
    <title>Competition management</title>
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
        <h1>Competition management</h1>
        <ul id="competition-operation">
            <li class="competition-operation">
                <a class="competition-operation"
                   href="${pageContext.request.contextPath}/controller?command=competition_list_page">
                    <b>View all competitions</b>
                </a>
            </li>
            <li class="competition-operation">
                <a class="competition-operation"
                   href="${pageContext.request.contextPath}/controller?command=competition_adding_page">
                    <b>Add competition</b>
                </a>
            </li>
            <li class="competition-operation">
                <a class="competition-operation"
                   href="${pageContext.request.contextPath}/controller?command=competition_results_committing_page">
                    <b>Commit competition results</b>
                </a>
            </li>
            <li class="competition-operation">
                <a class="competition-operation"
                   href="${pageContext.request.contextPath}/controller?command=competition_changing_page">
                    <b>Change competition</b>
                </a>
            </li>
            <li class="competition-operation">
                <a class="competition-operation"
                   href="${pageContext.request.contextPath}/controller?command=competition_deleting_page">
                    <b>Delete competition</b>
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