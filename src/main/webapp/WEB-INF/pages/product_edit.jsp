<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

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
    <h3><fmt:message key="page.product"/> ${requestScope.product.name}</h3>

    <c:if test="${requestScope.message!=null}">
        <div class="alert alert-danger" role="alert">${requestScope.message}</div>
    </c:if>

    <form action="/app/productEdit/${requestScope.product.id}" method="post">
        <p>
            <fmt:message key="merchandise.price"/> <input type="number" step="any" name="price">
        </p>
        <p>
            <fmt:message key="merchandise.amount"/> <input type="number" name="amount">
        </p>

        <button class="btn btn-primary" type="submit"><fmt:message key="merchandise.save"/></button>
    </form>

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