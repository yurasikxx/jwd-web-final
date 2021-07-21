<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Person list</title>
</head>
<body>
<c:if test="${not empty requestScope.person}">
  Persons
  <br>
  <c:forEach var="person" items="${requestScope.person}">
    ${person}
    <br>
  </c:forEach>
  <br>
  <br>
  <a href="${pageContext.request.contextPath}/controller?command=person_management_page">Back</a>
  <br>
  <a href="${pageContext.request.contextPath}/controller?command=main_page">Back to main</a>
</c:if>
</body>
</html>