<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <title>All Authors</title>
</head>
<body>
    <h1>Authors</h1>
    <c:url value="/author" var="createURL">
                        <c:param name="id" value="" />
                    </c:url>
    <a href='${createURL}'>Create new</a>
    <table border="1">
        <thead>
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Country</th>
                <th>Update</th>
                <th>Delete</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="authorItem" items="${authors}">
                <c:url value="/author" var="updateURL">
                    <c:param name="id" value="${authorItem.getId()}" />
                </c:url>
                <c:url value="/author/${authorItem.id}" var="deleteURL" />
                <tr>
                    <td>${authorItem.id}</td>
                    <td>${authorItem.name}</td>
                    <td>${authorItem.country}</td>
                    <td><a href="${updateURL}">Update</a></td>
                    <td><a href="${deleteURL}">Delete</a></td>
                </tr>
            </c:forEach>
            <tr>
                <td colspan="5">Total Authors: <c:out value="${authors.size()}"/></td>
            </tr>
        </tbody>
    </table>
</body>
</html>
