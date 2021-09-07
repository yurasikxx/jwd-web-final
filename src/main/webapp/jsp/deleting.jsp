<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="jwd" uri="custom" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="application"/>
<html>
<head>
    <title><fmt:message key="deleting"/></title>
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
                           href="${pageContext.request.contextPath}/controller?command=person_deleting_page">
                                ${requestScope.person}
                        </a>
                    </h2>
                </c:if>
                <c:if test="${not empty requestScope.competition}">
                    <h2>
                        <a class="changing"
                           href="${pageContext.request.contextPath}/controller?command=competition_deleting_page">
                                ${requestScope.competition}
                        </a>
                    </h2>
                </c:if>
                <c:if test="${not empty requestScope.betslip}">
                    <h2>
                        <a class="changing"
                           href="${pageContext.request.contextPath}/controller?command=betslip_deleting_page">
                                ${requestScope.betslip}
                        </a>
                    </h2>
                </c:if>
                <c:if test="${not empty requestScope.bet}">
                    <h2>
                        <a class="changing"
                           href="${pageContext.request.contextPath}/controller?command=bet_deleting_page">
                                ${requestScope.bet}
                        </a>
                    </h2>
                </c:if>
            </c:when>
            <c:when test="${not empty requestScope.person}">
                <h1>${requestScope.person}</h1>
                <form action="${pageContext.request.contextPath}/controller?command=person_delete" method="post">
                    <label for="personSelect"><fmt:message key="person"/></label>
                    <select id="personSelect" name="id">
                        <option value="0"><fmt:message key="person.select"/></option>
                        <c:forEach var="selectPerson" items="${requestScope.selectPerson}">
                            <option value="${selectPerson.id}">${selectPerson}</option>
                        </c:forEach>
                    </select>
                    <input type="submit" value="<fmt:message key="delete"/>">
                </form>
                <h2>
                    <a class="changing"
                       href="${pageContext.request.contextPath}/controller?command=person_management_page">
                        <fmt:message key="back.person.management"/>
                    </a>
                </h2>
            </c:when>
            <c:when test="${not empty requestScope.competition}">
                <h1>${requestScope.competition}</h1>
                <form action="${pageContext.request.contextPath}/controller?command=competition_delete" method="post">
                    <label for="competitionSelect"><fmt:message key="competition"/></label>
                    <select id="competitionSelect" name="id">
                        <option value="0"><fmt:message key="competition.select"/></option>
                        <c:forEach var="selectCompetition" items="${requestScope.selectCompetition}">
                            <option value="${selectCompetition.id}">${selectCompetition}</option>
                        </c:forEach>
                    </select>
                    <input type="submit" value="<fmt:message key="delete"/>">
                </form>
                <h2>
                    <a class="changing"
                       href="${pageContext.request.contextPath}/controller?command=competition_management_page">
                        <fmt:message key="back.competition.management"/>
                    </a>
                </h2>
            </c:when>
            <c:when test="${not empty requestScope.betslip}">
                <h1>${requestScope.betslip}</h1>
                <form action="${pageContext.request.contextPath}/controller?command=betslip_delete" method="post">
                    <label for="betslipSelect"><fmt:message key="betslip"/></label>
                    <select id="betslipSelect" name="id">
                        <option value="0"><fmt:message key="betslip.select"/></option>
                        <c:forEach var="selectBetslip" items="${requestScope.selectBetslip}">
                            <option value="${selectBetslip.id}">
                                    ${selectBetslip.id}, ${selectBetslip.betslipType.name}, ${selectBetslip}
                            </option>
                        </c:forEach>
                    </select>
                    <input type="submit" value="<fmt:message key="delete"/>">
                </form>
                <h2>
                    <a class="changing"
                       href="${pageContext.request.contextPath}/controller?command=betslip_management_page">
                        <fmt:message key="back.betslip.management"/>
                    </a>
                </h2>
            </c:when>
            <c:otherwise>
                <fmt:message key="error.msg"/>
            </c:otherwise>
        </c:choose>
        <h5>
            <label id="time"></label>
            <br>
            <jwd:date/>
        </h5>
    </div>
</div>
<h2>
    <a class="changing" href="${pageContext.request.contextPath}/controller?command=main_page">
        <fmt:message key="back.main"/>
    </a>
</h2>
</body>
</html>