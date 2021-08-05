<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.epam.jwd.model.Role" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="jwd" uri="custom" %>
<c:if test="${not empty sessionScope.locale}">
    <fmt:setLocale scope="application" value="${sessionScope.locale}"/>
</c:if>
<fmt:setBundle basename="application" scope="application"/>
<fmt:requestEncoding value="UTF-8"/>
<html>
<head>
    <title><fmt:message key="main.title"/></title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/time.js"></script>
</head>
<body onload="time()">
<div id="default-margin-div">
    <div id="default-border-div">
        <c:choose>
            <c:when test="${not empty requestScope.error}">
                <p>${requestScope.error}</p>
                <h2>
                    <a class="changing"
                       href="${pageContext.request.contextPath}/controller?command=main_page">
                        <fmt:message key="back.main"/>
                    </a>
                </h2>
            </c:when>
            <c:when test="${empty sessionScope.personRole}">
                <ul id="menu">
                    <li class="menu">
                        <a class="menu" href=""><b><fmt:message key="main"/></b></a>
                    </li>
                    <li class="menu">
                        <a class="menu" href="${pageContext.request.contextPath}/controller?command=log_in_page">
                            <b><fmt:message key="log.in"/></b>
                        </a>
                    </li>
                    <li class="menu">
                        <a class="menu" href="${pageContext.request.contextPath}/controller?command=register_page">
                            <b><fmt:message key="sign.up"/></b>
                        </a>
                    </li>
                </ul>
                <h1><fmt:message key="main.page.title"/></h1>
                <p><jwd:welcome/></p>
                <h1><fmt:message key="main.competitions"/></h1>
                <table>
                    <tr>
                        <th><h2><fmt:message key="main.table.competition.basketball"/></h2></th>
                        <th><h2><fmt:message key="main.table.competition.football"/></h2></th>
                        <th><h2><fmt:message key="main.table.competition.hockey"/></h2></th>
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
                <div id="language-margin-div">
                    <div id="language-border-div">
                        <form action="${pageContext.request.contextPath}/controller?command=select_language"
                              method="post">
                            <label for="select-language"><fmt:message key="language.title"/></label>
                            <select id="select-language" name="locale">
                                <option value=""><fmt:message key="language.select"/></option>
                                <option value="1"><fmt:message key="language.english"/></option>
                                <option value="2"><fmt:message key="language.russian"/></option>
                                <option value="3"><fmt:message key="language.french"/></option>
                            </select>
                            <input type="submit" value="<fmt:message key="language.select"/>">
                        </form>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <c:if test="${sessionScope.personRole eq Role.ADMINISTRATOR}">
                    <ul id="menu">
                        <li class="menu">
                            <a class="menu" href=""><b><fmt:message key="main"/></b></a>
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
                    <h1><fmt:message key="main.admin.page.title"/></h1>
                    <p><jwd:welcome/></p>
                </c:if>
                <c:if test="${sessionScope.personRole eq Role.BOOKMAKER}">
                    <ul id="menu">
                        <li class="menu">
                            <a class="menu" href=""><b><fmt:message key="main"/></b></a>
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
                    <h1><fmt:message key="main.bookmaker.page.title"/></h1>
                    <p><jwd:welcome/></p>
                </c:if>
                <c:if test="${sessionScope.personRole eq Role.USER}">
                    <ul id="menu">
                        <li class="menu">
                            <a class="menu" href=""><b><fmt:message key="main"/></b></a>
                        </li>
                        <li class="menu">
                            <a class="menu"
                               href="${pageContext.request.contextPath}/controller?command=bet_adding_page">
                                <b><fmt:message key="main.user.menu.place.bet"/></b>
                            </a>
                        </li>
                        <li class="menu">
                            <a class="menu"
                               href="${pageContext.request.contextPath}/controller?command=betslip_list_page">
                                <b><fmt:message key="betslip.list"/></b>
                            </a>
                        </li>
                        <li class="menu">
                            <a class="menu"
                               href="${pageContext.request.contextPath}/controller?command=person_bets_list_page">
                                <b><fmt:message key="main.user.menu.bets"/></b>
                            </a>
                        </li>
                        <li class="menu">
                            <a class="menu"
                               href="${pageContext.request.contextPath}/controller?command=person_bet_history_page">
                                <b><fmt:message key="main.user.menu.history.bet"/></b>
                            </a>
                        </li>
                        <li class="menu">
                            <a class="menu" href="${pageContext.request.contextPath}/controller?command=log_out">
                                <b><fmt:message key="log.out"/></b>
                            </a>
                        </li>
                    </ul>
                    <h1><fmt:message key="main.user.page.title"/></h1>
                    <p><jwd:welcome/></p>
                    <p><label><fmt:message key="main.user.balance"/>: ${sessionScope.personBalance}</label></p>
                    <h1><fmt:message key="main.user.betslips"/></h1>
                    <table>
                        <tr>
                            <th><h2><fmt:message key="main.user.table.betslip.home.win"/></h2></th>
                            <th><h2><fmt:message key="main.user.table.betslip.home.w'ont.lose"/></h2></th>
                            <th><h2><fmt:message key="main.user.table.betslip.draw"/></h2></th>
                            <th><h2><fmt:message key="main.user.table.betslip.no.draw"/></h2></th>
                            <th><h2><fmt:message key="main.user.table.betslip.away.w'ont.lose"/></h2></th>
                            <th><h2><fmt:message key="main.user.table.betslip.away.win"/></h2></th>
                        </tr>
                        <tr>
                            <td>
                                <c:forEach var="homeWin" items="${requestScope.homeWin}">
                                    <h3>${homeWin}</h3>
                                </c:forEach>
                            </td>
                            <td>
                                <c:forEach var="homeWillNotLose" items="${requestScope.homeWillNotLose}">
                                    <h3>${homeWillNotLose}</h3>
                                </c:forEach>
                            </td>
                            <td>
                                <c:forEach var="draw" items="${requestScope.draw}">
                                    <h3>${draw}</h3>
                                </c:forEach>
                            </td>
                            <td>
                                <c:forEach var="noDraw" items="${requestScope.noDraw}">
                                    <h3>${noDraw}</h3>
                                </c:forEach>
                            </td>
                            <td>
                                <c:forEach var="awayWillNotLose" items="${requestScope.awayWillNotLose}">
                                    <h3>${awayWillNotLose}</h3>
                                </c:forEach>
                            </td>
                            <td>
                                <c:forEach var="awayWin" items="${requestScope.awayWin}">
                                    <h3>${awayWin}</h3>
                                </c:forEach>
                            </td>
                        </tr>
                    </table>
                </c:if>
                <h1><fmt:message key="main.competitions"/></h1>
                <table>
                    <tr>
                        <th><h2><fmt:message key="main.table.competition.basketball"/></h2></th>
                        <th><h2><fmt:message key="main.table.competition.football"/></h2></th>
                        <th><h2><fmt:message key="main.table.competition.hockey"/></h2></th>
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
                <div id="language-margin-div">
                    <div id="language-border-div">
                        <form action="${pageContext.request.contextPath}/controller?command=select_language"
                              method="post">
                            <label for="select-auth-language"><fmt:message key="language.title"/></label>
                            <select id="select-auth-language" name="locale">
                                <option value=""><fmt:message key="language.select"/></option>
                                <option value="1"><fmt:message key="language.english"/></option>
                                <option value="2"><fmt:message key="language.russian"/></option>
                                <option value="3"><fmt:message key="language.french"/></option>
                            </select>
                            <input type="submit" value="<fmt:message key="language.select"/>">
                        </form>
                    </div>
                </div>
            </c:otherwise>
        </c:choose>
        <h5>
            <label id="time"></label>
            <br>
            <jwd:date/>
        </h5>
    </div>
</div>
</body>
</html>