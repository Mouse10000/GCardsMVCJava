<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <title>Author Form</title>
</head>
<body>
    <form:form
        method="POST"
        action="/lr5/author" modelAttribute="author">
        <c:choose>
            <c:when test="${method == 'create'}">
                <td><h1>Create author</h1></td>
            </c:when>
            <c:otherwise>
                <td>
                    <h1>Update author</h1>
                    <form:input path="id" type="hidden" value="${author.getId()}"/>
                </td>
            </c:otherwise>
        </c:choose>
        <table>
            <tr>
                <td><form:label path="name">Name</form:label></td>
                <td><form:input path="name" required="true" minlength="2" maxlength="1500"/></td>
            </tr>
            <tr>
                <td><form:label path="country">Country</form:label></td>
                <td><form:input path="country" required="true" /></td>
            </tr>
            <tr>
                <td><input type="submit" value="Submit"/></td>
            </tr>
        </table>
    </form:form>
</body>
</html>