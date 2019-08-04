<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="locales.messages"/>

<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <title>Cash register</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
          integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T"
          crossorigin="anonymous">
</head>
<body>
<%@include file="/WEB-INF/parts/navbar.jspf" %>
<div class="container mt-4">
    <h3><fmt:message key="page.registration"/></h3>

    <c:if test="${requestScope.error}">
        <div class="alert alert-danger" role="alert"><fmt:message key="message.exist.user"/></div>
    </c:if>

    <form action="${pageContext.request.contextPath}/app/registration" method="post">
        <div class="form-group row">
            <label class="col-sm-2 col-form-label" for="username"> <fmt:message key="registration.username"/></label>
            <div class="col-sm-6">
                <input type="text" class="form-control" id="username" name="username" required/>
            </div>
        </div>

        <div class="form-group row">
            <label class="col-sm-2 col-form-label" for="password"> <fmt:message key="registration.password"/></label>
            <div class="col-sm-6">
                <input type="password" class="form-control" id="password" name="password" required/>
            </div>
        </div>

        <button class="btn btn-primary" type="submit"><fmt:message key="registration.register"/></button>


    </form>


</div>
</div>


<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"
        integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo"
        crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"
        integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1"
        crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"
        integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM"
        crossorigin="anonymous"></script>
</body>
</html>
