<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="jwd" uri="custom" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="application"/>
<html>
<head>
    <title><fmt:message key="betslip.management"/></title>
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
                   href="${pageContext.request.contextPath}/controller?command=betslip_management_page">
                    <b><fmt:message key="betslip.management"/></b>
                </a>
            </li>
            <li class="menu">
                <a class="menu" href="${pageContext.request.contextPath}/controller?command=log_out">
                    <b><fmt:message key="log.out"/></b>
                </a>
            </li>
        </ul>
        <h1><fmt:message key="betslip.management"/></h1>
        <ul id="default-operation">
            <li class="default-operation">
                <a class="default-operation"
                   href="${pageContext.request.contextPath}/controller?command=betslip_list_page">
                    <b><fmt:message key="betslip.view"/></b>
                </a>
            </li>
            <li class="default-operation">
                <a class="default-operation"
                   href="${pageContext.request.contextPath}/controller?command=betslip_adding_page">
                    <b><fmt:message key="betslip.add"/></b>
                </a>
            </li>
            <li class="default-operation">
                <a class="default-operation"
                   href="${pageContext.request.contextPath}/controller?command=betslip_changing_page">
                    <b><fmt:message key="betslip.change"/></b>
                </a>
            </li>
            <li class="default-operation">
                <a class="default-operation"
                   href="${pageContext.request.contextPath}/controller?command=betslip_deleting_page">
                    <b><fmt:message key="betslip.delete"/></b>
                </a>
            </li>
        </ul>
        <br>
        <c:if test="${not empty requestScope.error}">
            <p>${requestScope.error}</p>
        </c:if>
        <br>
        <form action="${pageContext.request.contextPath}/controller?command=random_betslips_add" method="post">
            <input type="submit" name="competition-commit" value="<fmt:message key="betslip.add.random"/>">
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