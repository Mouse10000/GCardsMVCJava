<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <title>Author Form</title>
</head>
<body>
    <h1>Update Author</h1>
    <form:form action="../authorU" method="post" >
        <form:input type="hidden" name="id" value="${author.id}"/><br/><br/>
        <form:label for="name">Name:</label>
        <form:input type="text" name="name" id="name" value="${author.name}"/><br/><br/>
        <form:label for="country">Country:</label>
        <form:input type="text" name="country" id="country" value="${author.country}"/><br/><br/>
        <form:input type="submit" value="Save"/>
    </form>
</body>
</html>