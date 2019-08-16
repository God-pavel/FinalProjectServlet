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
    <h3><fmt:message key="page.report"/></h3>
    <c:if test="${requestScope.message!=null}">
        <div class="alert alert-danger" role="alert">${requestScope.message}</div>
    </c:if>

    <div class="container">
        <div class="row">
            <div class="col-sm">
                <form action="/app/createXReport" method="post">
                    <button class="btn btn-primary" type="submit">Create X-Report</button>
                </form>
            </div>
            <div class="col-sm">
                <form action="/app/createZReport" method="post">
                    <button class="btn btn-primary" type="submit">Create Z-Report</button>
                </form>
            </div>
        </div>
    </div>

    <div class="card-columns">
        <c:forEach var="report" items="${requestScope.reports}">
            <div class="card my-3" style="width: 18rem;">
                <b>Report: ${report.id}</b>
                <div class="m-2">
                    <p>Type: ${report.reportType}</p>
                    <p>Date: ${report.date}</p>
                    <p>Creator: ${report.user.username}</p>
                </div>
                <div class="card-footer text-muted">
                    <b>Total: ${report.total}</b>
                </div>
            </div>
        </c:forEach>
    </div>
</div>
</body>
<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"
        integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo"
        crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"
        integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1"
        crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"
        integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM"
        crossorigin="anonymous"></script>


</html>