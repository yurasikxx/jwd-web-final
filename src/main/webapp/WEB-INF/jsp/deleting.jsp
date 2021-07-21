<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Deleting</title>
</head>
<body>
<c:if test="${not empty requestScope.person}">
    <form action="${pageContext.request.contextPath}/controller?command=person_delete" method="post">
        <label for="personDeleteField">Enter person ID that need to delete: </label>
        <br>
        <input type="number" id="personDeleteField" name="id">
        <br>
        <input type="submit" value="Delete">
    </form>
    <br>
    <a href="${pageContext.request.contextPath}/controller?command=person_management_page">Back to person management</a>
</c:if>
<c:if test="${not empty requestScope.competition}">
    <form action="${pageContext.request.contextPath}/controller?command=competition_delete" method="post">
        <label for="competitionDeleteField">Enter competition ID that need to delete: </label>
        <br>
        <input type="number" id="competitionDeleteField" name="id">
        <br>
        <input type="submit" value="Delete">
    </form>
    <br>
    <a href="${pageContext.request.contextPath}/controller?command=competition_management_page">Back to competition management</a>
</c:if>
<c:if test="${not empty requestScope.betslip}">
    <form action="${pageContext.request.contextPath}/controller?command=betslip_delete" method="post">
        <label for="betslipDeleteField">Enter betslip ID that need to delete: </label>
        <br>
        <input type="number" id="betslipDeleteField" name="id">
        <br>
        <input type="submit" value="Delete">
    </form>
    <br>
    <a href="${pageContext.request.contextPath}/controller?command=betslip_management_page">Back to betslip management</a>
</c:if>
<c:if test="${not empty requestScope.bet}">
    <form action="${pageContext.request.contextPath}/controller?command=bet_delete" method="post">
        <label for="betDeleteField">Enter bet ID that need to delete: </label>
        <br>
        <input type="number" id="betDeleteField" name="id">
        <br>
        <input type="submit" value="Delete">
    </form>
    <br>
    <a href="${pageContext.request.contextPath}/controller?command=bet_management_page">Back to bet management</a>
</c:if>
<br>
<a href="${pageContext.request.contextPath}/controller?command=main_page">Back to main</a>
</body>
</html>