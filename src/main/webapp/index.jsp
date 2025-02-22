<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head></head>
    <body>
        <h1>Hello world!</h1>
        <a href='<c:url value="/authors" />'><c:url value="/authors" /></a>
        <a href='<c:url value="/author" />'><c:url value="/author" /></a>
    </body>
</html>