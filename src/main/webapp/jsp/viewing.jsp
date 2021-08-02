<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.epam.jwd.model.Role" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="jwd" uri="custom" %>
<html>
<head>
    <title>Viewing</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/time.js"></script>
</head>
<body onload="time()">
<div id=viewing-margin-div>
    <div id="viewing-border-div">
        <c:if test="${not empty requestScope.person}">
            <h1>Persons</h1>
            <table>
                <tr>
                    <th><h2>ID</h2></th>
                    <th><h2>Login</h2></th>
                    <th><h2>Balance</h2></th>
                    <th><h2>Role</h2></th>
                </tr>
                <tr>
                    <td>
                        <c:forEach var="person" items="${requestScope.person}">
                            <h3>${person.id}</h3>
                        </c:forEach>
                    </td>
                    <td>
                        <c:forEach var="person" items="${requestScope.person}">
                            <h3>${person.login}</h3>
                        </c:forEach>
                    </td>
                    <td>
                        <c:forEach var="person" items="${requestScope.person}">
                            <h3>${person.balance}</h3>
                        </c:forEach>
                    </td>
                    <td>
                        <c:forEach var="person" items="${requestScope.person}">
                            <h3>${person.role.name}</h3>
                        </c:forEach>
                    </td>
                </tr>
            </table>
            <h3>
                <a class="changing" href="${pageContext.request.contextPath}/controller?command=person_management_page">
                    Back to person management
                </a>
            </h3>
        </c:if>
        <c:if test="${not empty requestScope.competition}">
            <h1>Competitions</h1>
            <table>
                <tr>
                    <th><h2>ID</h2></th>
                    <th><h2>Sport</h2></th>
                    <th><h2>Home</h2></th>
                    <th><h2>Away</h2></th>
                </tr>
                <tr>
                    <td>
                        <c:forEach var="competition" items="${requestScope.competition}">
                            <h3>${competition.id}</h3>
                        </c:forEach>
                    </td>
                    <td>
                        <c:forEach var="competition" items="${requestScope.competition}">
                            <h3>${competition.home.sport.name}</h3>
                        </c:forEach>
                    </td>
                    <td>
                        <c:forEach var="competition" items="${requestScope.competition}">
                            <h3>${competition.home.name}</h3>
                        </c:forEach>
                    </td>
                    <td>
                        <c:forEach var="competition" items="${requestScope.competition}">
                            <h3>${competition.away.name}</h3>
                        </c:forEach>
                    </td>
                </tr>
            </table>
            <c:if test="${sessionScope.personRole eq Role.ADMINISTRATOR}">
                <h3>
                    <a class="changing"
                       href="${pageContext.request.contextPath}/controller?command=person_management_page">
                        Back to person management
                    </a>
                </h3>
            </c:if>
        </c:if>
        <c:if test="${not empty requestScope.betslip}">
            <h1>Betslips</h1>
            <table>
                <tr>
                    <th><h2>ID</h2></th>
                    <th><h2>Competition ID</h2></th>
                    <th><h2>Sport</h2></th>
                    <th><h2>Home</h2></th>
                    <th><h2>Away</h2></th>
                    <th><h2>Bet type ID</h2></th>
                    <th><h2>Bet type</h2></th>
                    <th><h2>Coefficient</h2></th>
                </tr>
                <tr>
                    <td>
                        <c:forEach var="betslip" items="${requestScope.betslip}">
                            <h3>${betslip.id}</h3>
                        </c:forEach>
                    </td>
                    <td>
                        <c:forEach var="betslip" items="${requestScope.betslip}">
                            <h3>${betslip.competition.id}</h3>
                        </c:forEach>
                    </td>
                    <td>
                        <c:forEach var="betslip" items="${requestScope.betslip}">
                            <h3>${betslip.competition.home.sport.name}</h3>
                        </c:forEach>
                    </td>
                    <td>
                        <c:forEach var="betslip" items="${requestScope.betslip}">
                            <h3>${betslip.competition.home.name}</h3>
                        </c:forEach>
                    </td>
                    <td>
                        <c:forEach var="betslip" items="${requestScope.betslip}">
                            <h3>${betslip.competition.away.name}</h3>
                        </c:forEach>
                    </td>
                    <td>
                        <c:forEach var="betslip" items="${requestScope.betslip}">
                            <h3>${betslip.betType.id}</h3>
                        </c:forEach>
                    </td>
                    <td>
                        <c:forEach var="betslip" items="${requestScope.betslip}">
                            <h3>${betslip.betType.name}</h3>
                        </c:forEach>
                    </td>
                    <td>
                        <c:forEach var="betslip" items="${requestScope.betslip}">
                            <h3>${betslip.coefficient}</h3>
                        </c:forEach>
                    </td>
                </tr>
            </table>
            <c:if test="${sessionScope.personRole eq Role.BOOKMAKER}">
                <h3>
                    <a class="changing" href="${pageContext.request.contextPath}/controller?command=betslip_management_page">
                        Back to betslip management
                    </a>
                </h3>
            </c:if>
        </c:if>
        <c:if test="${not empty requestScope.bet}">
            <h1>Bets</h1>
            <table>
                <tr>
                    <th><h2>ID</h2></th>
                    <th><h2>Betslip ID</h2></th>
                    <th><h2>Competition ID</h2></th>
                    <th><h2>Sport</h2></th>
                    <th><h2>Home</h2></th>
                    <th><h2>Away</h2></th>
                    <th><h2>Bet type ID</h2></th>
                    <th><h2>Bet type</h2></th>
                    <th><h2>Coefficient</h2></th>
                    <th><h2>Person ID</h2></th>
                    <th><h2>Login</h2></th>
                    <th><h2>Role</h2></th>
                    <th><h2>Bet total</h2></th>
                </tr>
                <tr>
                    <td>
                        <c:forEach var="bet" items="${requestScope.bet}">
                            <h3>${bet.id}</h3>
                        </c:forEach>
                    </td>
                    <td>
                        <c:forEach var="bet" items="${requestScope.bet}">
                            <h3>${bet.betslip.id}</h3>
                        </c:forEach>
                    </td>
                    <td>
                        <c:forEach var="bet" items="${requestScope.bet}">
                            <h3>${bet.betslip.competition.id}</h3>
                        </c:forEach>
                    </td>
                    <td>
                        <c:forEach var="bet" items="${requestScope.bet}">
                            <h3>${bet.betslip.competition.home.sport.name}</h3>
                        </c:forEach>
                    </td>
                    <td>
                        <c:forEach var="bet" items="${requestScope.bet}">
                            <h3>${bet.betslip.competition.home.name}</h3>
                        </c:forEach>
                    </td>
                    <td>
                        <c:forEach var="bet" items="${requestScope.bet}">
                            <h3>${bet.betslip.competition.away.name}</h3>
                        </c:forEach>
                    </td>
                    <td>
                        <c:forEach var="bet" items="${requestScope.bet}">
                            <h3>${bet.betslip.betType.id}</h3>
                        </c:forEach>
                    </td>
                    <td>
                        <c:forEach var="bet" items="${requestScope.bet}">
                            <h3>${bet.betslip.betType.name}</h3>
                        </c:forEach>
                    </td>
                    <td>
                        <c:forEach var="bet" items="${requestScope.bet}">
                            <h3>${bet.betslip.coefficient}</h3>
                        </c:forEach>
                    </td>
                    <td>
                        <c:forEach var="bet" items="${requestScope.bet}">
                            <h3>${bet.person.id}</h3>
                        </c:forEach>
                    </td>
                    <td>
                        <c:forEach var="bet" items="${requestScope.bet}">
                            <h3>${bet.person.login}</h3>
                        </c:forEach>
                    </td>
                    <td>
                        <c:forEach var="bet" items="${requestScope.bet}">
                            <h3>${bet.person.role}</h3>
                        </c:forEach>
                    </td>
                    <td>
                        <c:forEach var="bet" items="${requestScope.bet}">
                            <h3>${bet.betTotal}</h3>
                        </c:forEach>
                    </td>
                </tr>
            </table>
            <h3>
                <a href="${pageContext.request.contextPath}/controller?command=bet_management_page">
                    Back to bet management
                </a>
            </h3>
        </c:if>
        <c:if test="${not empty requestScope.betHistory}">
            <h1>Bet history</h1>
            <table>
                <tr>
                    <th><h2>ID</h2></th>
                    <th><h2>Sport</h2></th>
                    <th><h2>Home</h2></th>
                    <th><h2>Away</h2></th>
                    <th><h2>Bet type</h2></th>
                    <th><h2>Coefficient</h2></th>
                    <th><h2>Bet total</h2></th>
                    <th><h2>User</h2></th>
                    <th><h2>Competition result</h2></th>
                    <th><h2>Bet result</h2></th>
                </tr>
                <tr>
                    <td>
                        <c:forEach var="betHistory" items="${requestScope.betHistory}">
                            <h3>${betHistory.id}</h3>
                        </c:forEach>
                    </td>
                    <td>
                        <c:forEach var="betHistory" items="${requestScope.betHistory}">
                            <h3>${betHistory.home.sport}</h3>
                        </c:forEach>
                    </td>
                    <td>
                        <c:forEach var="betHistory" items="${requestScope.betHistory}">
                            <h3>${betHistory.home.name}</h3>
                        </c:forEach>
                    </td>
                    <td>
                        <c:forEach var="betHistory" items="${requestScope.betHistory}">
                            <h3>${betHistory.away.name}</h3>
                        </c:forEach>
                    </td>
                    <td>
                        <c:forEach var="betHistory" items="${requestScope.betHistory}">
                            <h3>${betHistory.betType.name}</h3>
                        </c:forEach>
                    </td>
                    <td>
                        <c:forEach var="betHistory" items="${requestScope.betHistory}">
                            <h3>${betHistory.coefficient}</h3>
                        </c:forEach>
                    </td>
                    <td>
                        <c:forEach var="betHistory" items="${requestScope.betHistory}">
                            <h3>${betHistory.betTotal}</h3>
                        </c:forEach>
                    </td>
                    <td>
                        <c:forEach var="betHistory" items="${requestScope.betHistory}">
                            <h3>${betHistory.personLogin}</h3>
                        </c:forEach>
                    </td>
                    <td>
                        <c:forEach var="betHistory" items="${requestScope.betHistory}">
                            <h3>${betHistory.competitionResult}</h3>
                        </c:forEach>
                    </td>
                    <td>
                        <c:forEach var="betHistory" items="${requestScope.betHistory}">
                            <h3>${betHistory.betResult}</h3>
                        </c:forEach>
                    </td>
                </tr>
            </table>
        </c:if>
        <c:if test="${not empty requestScope.personBet}">
            <h1>My bets</h1>
            <table>
                <tr>
                    <th><h2>Sport</h2></th>
                    <th><h2>Home</h2></th>
                    <th><h2>Away</h2></th>
                    <th><h2>Bet type</h2></th>
                    <th><h2>Coefficient</h2></th>
                    <th><h2>Bet total</h2></th>
                </tr>
                <tr>
                    <td>
                        <c:forEach var="personBet" items="${requestScope.personBet}">
                            <h3>${personBet.betslip.competition.home.sport.name}</h3>
                        </c:forEach>
                    </td>
                    <td>
                        <c:forEach var="personBet" items="${requestScope.personBet}">
                            <h3>${personBet.betslip.competition.home.name}</h3>
                        </c:forEach>
                    </td>
                    <td>
                        <c:forEach var="personBet" items="${requestScope.personBet}">
                            <h3>${personBet.betslip.competition.away.name}</h3>
                        </c:forEach>
                    </td>
                    <td>
                        <c:forEach var="personBet" items="${requestScope.personBet}">
                            <h3>${personBet.betslip.betType.name}</h3>
                        </c:forEach>
                    </td>
                    <td>
                        <c:forEach var="personBet" items="${requestScope.personBet}">
                            <h3>${personBet.betslip.coefficient}</h3>
                        </c:forEach>
                    </td>
                    <td>
                        <c:forEach var="personBet" items="${requestScope.personBet}">
                            <h3>${personBet.betTotal}</h3>
                        </c:forEach>
                    </td>
                </tr>
            </table>
        </c:if>
        <c:if test="${not empty requestScope.personBetHistory}">
            <h1>My bet history</h1>
            <table>
                <tr>
                    <th><h2>Sport</h2></th>
                    <th><h2>Home</h2></th>
                    <th><h2>Away</h2></th>
                    <th><h2>Bet type</h2></th>
                    <th><h2>Coefficient</h2></th>
                    <th><h2>Bet total</h2></th>
                    <th><h2>Competition result</h2></th>
                    <th><h2>Bet result</h2></th>
                </tr>
                <tr>
                    <td>
                        <c:forEach var="personBetHistory" items="${requestScope.personBetHistory}">
                            <h3>${personBetHistory.home.sport}</h3>
                        </c:forEach>
                    </td>
                    <td>
                        <c:forEach var="personBetHistory" items="${requestScope.personBetHistory}">
                            <h3>${personBetHistory.home.name}</h3>
                        </c:forEach>
                    </td>
                    <td>
                        <c:forEach var="personBetHistory" items="${requestScope.personBetHistory}">
                            <h3>${personBetHistory.away.name}</h3>
                        </c:forEach>
                    </td>
                    <td>
                        <c:forEach var="personBetHistory" items="${requestScope.personBetHistory}">
                            <h3>${personBetHistory.betType.name}</h3>
                        </c:forEach>
                    </td>
                    <td>
                        <c:forEach var="personBetHistory" items="${requestScope.personBetHistory}">
                            <h3>${personBetHistory.coefficient}</h3>
                        </c:forEach>
                    </td>
                    <td>
                        <c:forEach var="personBetHistory" items="${requestScope.personBetHistory}">
                            <h3>${personBetHistory.betTotal}</h3>
                        </c:forEach>
                    </td>
                    <td>
                        <c:forEach var="personBetHistory" items="${requestScope.personBetHistory}">
                            <h3>${personBetHistory.competitionResult}</h3>
                        </c:forEach>
                    </td>
                    <td>
                        <c:forEach var="personBetHistory" items="${requestScope.personBetHistory}">
                            <h3>${personBetHistory.betResult}</h3>
                        </c:forEach>
                    </td>
                </tr>
            </table>
        </c:if>
        <br>
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