<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.epam.jwd.model.Role" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="jwd" uri="custom" %>
<html>
<head>
    <title>Totalizator</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/main.css">
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/time.js"></script>
</head>
<body onload="time()">
<div id="margin-div">
    <div id="border-div">
        <c:choose>
            <c:when test="${empty sessionScope.personName}">
                <ul id="menu">
                    <li class="menu">
                        <a class="menu" href=""><b>Main</b></a>
                    </li>
                    <li class="menu">
                        <a class="menu" href="${pageContext.request.contextPath}/controller?command=log_in_page">
                            <b>Log in</b>
                        </a>
                    </li>
                    <li class="menu">
                        <a class="menu" href="${pageContext.request.contextPath}/controller?command=register_page">
                            <b>Sign up</b>
                        </a>
                    </li>
                </ul>
                <h1>Main page</h1>
                <p><jwd:welcome/></p>
            </c:when>
            <c:otherwise>
                <c:if test="${sessionScope.personRole eq Role.ADMINISTRATOR}">
                    <ul id="menu">
                        <li class="menu">
                            <a class="menu" href=""><b>Main</b></a>
                        </li>
                        <li class="menu">
                            <a class="menu"
                               href="${pageContext.request.contextPath}/controller?command=competition_management_page">
                                <b>Competition management</b>
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
                               href="${pageContext.request.contextPath}/controller?command=betslip_list_page">
                                <b>Betslip list</b>
                            </a>
                        </li>
                        <li class="menu">
                            <a class="menu"
                               href="${pageContext.request.contextPath}/controller?command=bet_list_page">
                                <b>Bet list</b>
                            </a>
                        </li>
                        <li class="menu">
                            <a class="menu" href="${pageContext.request.contextPath}/controller?command=log_out">
                                <b>Log out</b>
                            </a>
                        </li>
                    </ul>
                    <h1>Main administrator page</h1>
                    <p><jwd:welcome/></p>
                </c:if>
                <c:if test="${sessionScope.personRole eq Role.BOOKMAKER}">
                    <ul id="menu">
                        <li class="menu">
                            <a class="menu" href=""><b>Main</b></a>
                        </li>
                        <li class="menu">
                            <a class="menu"
                               href="${pageContext.request.contextPath}/controller?command=betslip_management_page">
                                <b>Betslip management</b>
                            </a>
                        </li>
                        <li class="menu">
                            <a class="menu" href="${pageContext.request.contextPath}/controller?command=log_out">
                                <b>Log out</b>
                            </a>
                        </li>
                    </ul>
                    <h1>Main bookmaker page</h1>
                    <p><jwd:welcome/></p>
                </c:if>
                <c:if test="${sessionScope.personRole eq Role.USER}">
                    <ul id="menu">
                        <li class="menu">
                            <a class="menu" href=""><b>Main</b></a>
                        </li>
                        <li class="menu">
                            <a class="menu"
                               href="${pageContext.request.contextPath}/controller?command=bet_adding_page">
                                <b>Place bet</b>
                            </a>
                        </li>
                        <li class="menu">
                            <a class="menu"
                               href="${pageContext.request.contextPath}/controller?command=betslip_list_page">
                                <b>Betslip list</b>
                            </a>
                        </li>
                        <li class="menu">
                            <a class="menu"
                               href="${pageContext.request.contextPath}/controller?command=person_bets_list_page">
                                <b>My bets</b>
                            </a>
                        </li>
                        <li class="menu">
                            <a class="menu"
                               href="${pageContext.request.contextPath}/controller?command=person_bet_history_page">
                                <b>My history bet</b>
                            </a>
                        </li>
                        <li class="menu">
                            <a class="menu" href="${pageContext.request.contextPath}/controller?command=log_out">
                                <b>Log out</b>
                            </a>
                        </li>
                    </ul>
                    <h1>Main user page</h1>
                    <p><jwd:welcome/></p>
                    <p><label>Balance: ${sessionScope.personBalance}</label></p>
                </c:if>
            </c:otherwise>
        </c:choose>
        <h1>Competitions</h1>
        <table>
            <tr>
                <th><h2>Basketball</h2></th>
                <th><h2>Football</h2></th>
                <th><h2>Hockey</h2></th>
            </tr>
            <tr>
                <td>
                    <c:forEach var="basketballCompetition" items="${requestScope.basketballCompetition}">
                        <h3>${basketballCompetition}</h3>
                    </c:forEach>
                </td>
                <td>
                    <c:forEach var="footballCompetition" items="${requestScope.footballCompetition}">
                        <h3>${footballCompetition}</h3>
                    </c:forEach>
                </td>
                <td>
                    <c:forEach var="hockeyCompetition" items="${requestScope.hockeyCompetition}">
                        <h3>${hockeyCompetition}</h3>
                    </c:forEach>
                </td>
            </tr>
        </table>
        <h5>
            <label id="time"></label>
            <br>
            <jwd:date/>
        </h5>
    </div>
</div>
</body>
</html>