<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="jwd" uri="custom" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="application"/>
<html>
<head>
    <title><fmt:message key="adding"/></title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/time.js"></script>
</head>
<body onload="time()">
<div id="changing-margin-div">
    <div id="changing-border-div">
        <c:choose>
            <c:when test="${not empty requestScope.error}">
                <p>${requestScope.error}</p>
                <c:if test="${not empty requestScope.person}">
                    <h2>
                        <a class="changing"
                           href="${pageContext.request.contextPath}/controller?command=person_adding_page">
                                ${requestScope.person}
                        </a>
                    </h2>
                </c:if>
                <c:if test="${not empty requestScope.competition}">
                    <h2>
                        <a class="changing"
                           href="${pageContext.request.contextPath}/controller?command=competition_adding_page">
                                ${requestScope.competition}
                        </a>
                    </h2>
                </c:if>
                <c:if test="${not empty requestScope.betslip}">
                    <h2>
                        <a class="changing"
                           href="${pageContext.request.contextPath}/controller?command=betslip_adding_page">
                                ${requestScope.betslip}
                        </a>
                    </h2>
                </c:if>
                <c:if test="${not empty requestScope.bet}">
                    <h2>
                        <a class="changing"
                           href="${pageContext.request.contextPath}/controller?command=bet_adding_page">
                                ${requestScope.bet}
                        </a>
                    </h2>
                </c:if>
            </c:when>
            <c:when test="${not empty requestScope.person}">
                <h1>${requestScope.person}</h1>
                <form action="${pageContext.request.contextPath}/controller?command=person_add" method="post">
                    <label for="loginField"><fmt:message key="auth.login"/></label>
                    <br>
                    <input type="text" id="loginField" name="login">
                    <br>
                    <label for="passwordField"><fmt:message key="auth.password"/></label>
                    <br>
                    <input type="password" id="passwordField" name="password">
                    <br>
                    <input type="submit" value="<fmt:message key="add"/>">
                </form>
                <br>
                <h2>
                    <a class="changing"
                       href="${pageContext.request.contextPath}/controller?command=person_management_page">
                        <fmt:message key="back.person.management"/>
                    </a>
                </h2>
            </c:when>
            <c:when test="${not empty requestScope.competition}">
                <h1>${requestScope.competition}</h1>
                <form action="${pageContext.request.contextPath}/controller?command=competition_add" method="post">
                    <label for="homeTeamSelect"><fmt:message key="competition.team.home"/></label>
                    <select id="homeTeamSelect" name="homeTeamId">
                        <option value="0"><fmt:message key="competition.team.home.select"/></option>
                        <c:forEach var="selectTeam" items="${requestScope.selectTeam}">
                            <option value="${selectTeam.id}">${selectTeam.name}</option>
                        </c:forEach>
                    </select>
                    <label for="awayTeamSelect"><fmt:message key="competition.team.away"/></label>
                    <select id="awayTeamSelect" name="awayTeamId">
                        <option value="0"><fmt:message key="competition.team.away.select"/></option>
                        <c:forEach var="selectTeam" items="${requestScope.selectTeam}">
                            <option value="${selectTeam.id}">${selectTeam.name}</option>
                        </c:forEach>
                    </select>
                    <input type="submit" value="Add">
                </form>
                <br>
                <h2>
                    <a class="changing"
                       href="${pageContext.request.contextPath}/controller?command=competition_management_page">
                        <fmt:message key="back.competition.management"/>
                    </a>
                </h2>
            </c:when>
            <c:when test="${not empty requestScope.betslip}">
                <h1>${requestScope.betslip}</h1>
                <form action="${pageContext.request.contextPath}/controller?command=betslip_add" method="post">
                    <label for="competitionSelect"><fmt:message key="competition"/></label>
                    <select id="competitionSelect" name="competitionId">
                        <option value="0"><fmt:message key="competition.select"/></option>
                        <c:forEach var="selectCompetition" items="${requestScope.selectCompetition}">
                            <option value="${selectCompetition.id}">${selectCompetition}</option>
                        </c:forEach>
                    </select>
                    <label for="betTypeSelect"><fmt:message key="bet.type"/></label>
                    <select id="betTypeSelect" name="betTypeId">
                        <option value="0"><fmt:message key="bet.type.select"/></option>
                        <c:forEach var="selectBetType" items="${requestScope.selectBetType}">
                            <option value="${selectBetType.id}">${selectBetType.name}</option>
                        </c:forEach>
                    </select>
                    <label for="coefficientField"><fmt:message key="coefficient"/></label>
                    <br>
                    <input type="number" id="coefficientField" name="coefficient" placeholder="0 (zero) - random cf">
                    <br>
                    <input type="submit" value="<fmt:message key="add"/>">
                </form>
                <br>
                <h2>
                    <a class="changing"
                       href="${pageContext.request.contextPath}/controller?command=betslip_management_page">
                        <fmt:message key="back.betslip.management"/>
                    </a>
                </h2>
            </c:when>
            <c:when test="${not empty requestScope.bet}">
                <h1>${requestScope.bet}</h1>
                <h2><fmt:message key="main.user.balance"/>: ${sessionScope.personBalance}</h2>
                <form action="${pageContext.request.contextPath}/controller?command=bet_add" method="post">
                    <label for="betslipSelect"><fmt:message key="betslip"/></label>
                    <select id="betslipSelect" name="betslipId">
                        <option value="0"><fmt:message key="betslip.select"/></option>
                        <c:forEach var="selectBetslip" items="${requestScope.selectBetslip}">
                            <option value="${selectBetslip.id}">${selectBetslip}, ${selectBetslip.betType.name}</option>
                        </c:forEach>
                    </select>
                    <label for="betTotalField"><fmt:message key="bet.total"/></label>
                    <br>
                    <input type="number" id="betTotalField" name="betTotal">
                    <br>
                    <input type="submit" value="<fmt:message key="add"/>">
                </form>
            </c:when>
            <c:otherwise>
                <p><fmt:message key="error.msg"/></p>
            </c:otherwise>
        </c:choose>
        <h5>
            <label id="time"></label>
            <br>
            <jwd:date/>
        </h5>
    </div>
</div>
<br>
<h2>
    <a class="changing" href="${pageContext.request.contextPath}/controller?command=main_page">
        <fmt:message key="back.main"/>
    </a>
</h2>
</body>
</html>