<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="jwd" uri="custom" %>
<html>
<head>
    <title>Changing</title>
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
                           href="${pageContext.request.contextPath}/controller?command=person_changing_page">
                                ${requestScope.person}
                        </a>
                    </h2>
                </c:if>
                <c:if test="${not empty requestScope.competition}">
                    <h2>
                        <a class="changing"
                           href="${pageContext.request.contextPath}/controller?command=competition_changing_page">
                                ${requestScope.competition}
                        </a>
                    </h2>
                </c:if>
                <c:if test="${not empty requestScope.betslip}">
                    <h2>
                        <a class="changing"
                           href="${pageContext.request.contextPath}/controller?command=betslip_changing_page">
                                ${requestScope.betslip}
                        </a>
                    </h2>
                </c:if>
                <c:if test="${not empty requestScope.bet}">
                    <h2>
                        <a class="changing"
                           href="${pageContext.request.contextPath}/controller?command=bet_changing_page">
                                ${requestScope.bet}
                        </a>
                    </h2>
                </c:if>
                <c:if test="${not empty requestScope.betHistory}">
                    <h2>
                        <a class="changing"
                           href="${pageContext.request.contextPath}/controller?command=competition_results_committing_page">
                                ${requestScope.betHistory}
                        </a>
                    </h2>
                </c:if>
            </c:when>
            <c:when test="${not empty requestScope.person}">
                <h1>${requestScope.person}</h1>
                <form action="${pageContext.request.contextPath}/controller?command=person_change" method="post">
                    <label for="personSelect">Person ID</label>
                    <select id="personSelect" name="id">
                        <option value="0">Select person</option>
                        <c:forEach var="selectPerson" items="${requestScope.selectPerson}">
                            <option value="${selectPerson.id}">${selectPerson}</option>
                        </c:forEach>
                    </select>
                    <label for="loginField">Login</label>
                    <br>
                    <input type="text" id="loginField" name="login">
                    <br>
                    <label for="passwordField">Password</label>
                    <br>
                    <input type="password" id="passwordField" name="password">
                    <br>
                    <label for="balanceField">Balance</label>
                    <br>
                    <input type="number" id="balanceField" name="balance">
                    <br>
                    <input type="submit" value="Change">
                </form>
                <h2>
                    <a class="changing"
                       href="${pageContext.request.contextPath}/controller?command=person_management_page">
                        Back to person management
                    </a>
                </h2>
            </c:when>
            <c:when test="${not empty requestScope.competition}">
                <h1>${requestScope.competition}</h1>
                <form action="${pageContext.request.contextPath}/controller?command=competition_change" method="post">
                    <label for="competitionSelect">Competition</label>
                    <select id="competitionSelect" name="id">
                        <option value="0">Select competition</option>
                        <c:forEach var="selectCompetition" items="${requestScope.selectCompetition}">
                            <option value="${selectCompetition.id}">${selectCompetition}</option>
                        </c:forEach>
                    </select>
                    <label for="homeTeamSelect">Home team</label>
                    <select id="homeTeamSelect" name="homeTeamId">
                        <option value="0">Select home team</option>
                        <c:forEach var="selectTeam" items="${requestScope.selectTeam}">
                            <option value="${selectTeam.id}">${selectTeam.name}</option>
                        </c:forEach>
                    </select>
                    <label for="awayTeamSelect">Away team</label>
                    <select id="awayTeamSelect" name="awayTeamId">
                        <option value="0">Select away team</option>
                        <c:forEach var="selectTeam" items="${requestScope.selectTeam}">
                            <option value="${selectTeam.id}">${selectTeam.name}</option>
                        </c:forEach>
                    </select>
                    <input type="submit" value="Change">
                </form>
                <h2>
                    <a class="changing"
                       href="${pageContext.request.contextPath}/controller?command=competition_management_page">
                        Back to competition management
                    </a>
                </h2>
            </c:when>
            <c:when test="${not empty requestScope.betslip}">
                <h1>${requestScope.betslip}</h1>
                <form action="${pageContext.request.contextPath}/controller?command=betslip_change" method="post">
                    <label for="betslipSelect">Betslip</label>
                    <select id="betslipSelect" name="id">
                        <option value="0">Select betslip</option>
                        <c:forEach var="selectBetslip" items="${requestScope.selectBetslip}">
                            <option value="${selectBetslip.id}">${selectBetslip.betType.name}, ${selectBetslip}</option>
                        </c:forEach>
                    </select>
                    <label for="competitionIdSelect">Competition</label>
                    <select id="competitionIdSelect" name="competitionId">
                        <option value="0">Select competition</option>
                        <c:forEach var="selectCompetition" items="${requestScope.selectCompetition}">
                            <option value="${selectCompetition.id}">${selectCompetition}</option>
                        </c:forEach>
                    </select>
                    <label for="betTypeSelect">Bet type</label>
                    <select id="betTypeSelect" name="betTypeId">
                        <option value="0">Select bet type</option>
                        <c:forEach var="selectBetType" items="${requestScope.selectBetType}">
                            <option value="${selectBetType.id}">${selectBetType.name}</option>
                        </c:forEach>
                    </select>
                    <label for="coefficientField">Coefficient: </label>
                    <br>
                    <input type="number" id="coefficientField" name="coefficient">
                    <br>
                    <input type="submit" value="Change">
                </form>
                <h2>
                    <a class="changing" href="${pageContext.request.contextPath}/controller?command=betslip_management_page">
                        Back to betslip management
                    </a>
                </h2>
            </c:when>
            <c:when test="${not empty requestScope.betHistory}">
                <h1>${requestScope.betHistory}</h1>
                <form action="${pageContext.request.contextPath}/controller?command=commit_competition_results"
                      method="post">
                    <label for="competition-select">Competition</label>
                    <select id="competition-select" name="id">
                        <option value="0">Select competition</option>
                        <c:forEach var="selectCompetition" items="${requestScope.selectCompetition}">
                            <option value="${selectCompetition.id}">${selectCompetition}</option>
                        </c:forEach>
                    </select>
                    <input type="submit" value="Commit">
                </form>
                <h3>
                    <a class="changing"
                       href="${pageContext.request.contextPath}/controller?command=competition_management_page">
                        Back to competition management
                    </a>
                </h3>
            </c:when>
            <c:otherwise>
                Oops! Something went wrong...
            </c:otherwise>
        </c:choose>
        <h5>
            <label id="time"></label>
            <br>
            <jwd:date/>
        </h5>
    </div>
</div>
<h2><a class="changing" href="${pageContext.request.contextPath}/controller?command=main_page">Back to main</a></h2>
</body>
</html>