<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="jwd" uri="custom" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="application"/>
<html>
<head>
    <title><fmt:message key="competition.management"/></title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/time.js"></script>
</head>
<body onload="time()">
<div id="default-margin-div">
    <div id="default-border-div">
        <ul id="menu">
            <li class="menu">
                <a class="menu"
                   href="${pageContext.request.contextPath}/controller?command=main_page">
                    <b><fmt:message key="main"/></b>
                </a>
            </li>
            <li class="menu">
                <a class="menu"
                   href="${pageContext.request.contextPath}/controller?command=betslip_list_page">
                    <b><fmt:message key="betslip.list"/></b>
                </a>
            </li>
            <li class="menu">
                <a class="menu" href="${pageContext.request.contextPath}/controller?command=log_out">
                    <b><fmt:message key="log.out"/></b>
                </a>
            </li>
            <li class="menu">
                <a class="menu"
                   href="${pageContext.request.contextPath}/controller?command=person_management_page">
                    <b><fmt:message key="person.management"/></b>
                </a>
            </li>
            <li class="menu">
                <a class="menu"
                   href="${pageContext.request.contextPath}/controller?command=competition_management_page">
                    <b><fmt:message key="competition.management"/></b>
                </a>
            </li>
            <li class="menu">
                <a class="menu"
                   href="${pageContext.request.contextPath}/controller?command=bet_management_page">
                    <b><fmt:message key="bet.management"/></b>
                </a>
            </li>
        </ul>
        <h1><fmt:message key="competition.management"/></h1>
        <ul id="default-operation">
            <li class="default-operation">
                <a class="default-operation"
                   href="${pageContext.request.contextPath}/controller?command=competition_list_page">
                    <b><fmt:message key="competition.view"/></b>
                </a>
            </li>
            <li class="default-operation">
                <a class="default-operation"
                   href="${pageContext.request.contextPath}/controller?command=competition_adding_page">
                    <b><fmt:message key="competition.add"/></b>
                </a>
            </li>
            <li class="default-operation">
                <a class="default-operation"
                   href="${pageContext.request.contextPath}/controller?command=competition_changing_page">
                    <b><fmt:message key="competition.change"/></b>
                </a>
            </li>
            <li class="default-operation">
                <a class="default-operation"
                   href="${pageContext.request.contextPath}/controller?command=competition_deleting_page">
                    <b><fmt:message key="competition.delete"/></b>
                </a>
            </li>
        </ul>
        <br>
        <c:if test="${not empty requestScope.error}">
            <p>${requestScope.error}</p>
        </c:if>
        <br>
        <form action="${pageContext.request.contextPath}/controller?command=commit_competition_results" method="post">
            <input type="submit" name="competition-commit" value="<fmt:message key="competition.commit"/>">
        </form>
        <form action="${pageContext.request.contextPath}/controller?command=random_competitions_add" method="post">
            <input type="submit" name="competition-add-random" value="<fmt:message key="competition.add.random"/>">
        </form>
        <h5>
            <label id="time"></label>
            <br>
            <jwd:date/>
        </h5>
    </div>
</div>
</body>
</html>