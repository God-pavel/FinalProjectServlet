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

    <h3><fmt:message key= "page.check"/></h3>
    <c:if test="${requestScope.message!=null}">
        <div class="alert alert-danger" role="alert">${requestScope.message}</div>
    </c:if>

    <p><fmt:message key= "check.creator"/> ${requestScope.check.user.getUsername()}</p>
    <p><fmt:message key= "check.time"/> ${requestScope.check.time}</p>

    <c:forEach var="key" items="${requestScope.check.productAmount.keySet()}">
        <p>${key.getName()} <fmt:message key= "check.amount"/> ${requestScope.check.productAmount.get(key)}</p>

    </c:forEach>
    <p><fmt:message key= "check.total"/> ${requestScope.check.total}</p>

    <form action="/app/closeCheck/${requestScope.check.id}" method="post">
        <button class="btn btn-primary m-3" type="submit"><fmt:message key= "check.close"/></button>
    </form>


    <div class="container">
        <div class="row">
            <div class="col-sm">
                <form action="/app/addByName/${requestScope.check.id}" method="post">
                    <div class="form-group row">
                        <label class="col-sm-3 col-form-label"><fmt:message key= "check.productName"/> </label>
                        <div class="col-sm-5">
                            <input type="text" class="form-control" name="name" required/>
                        </div>
                    </div>

                    <div class="form-group row">
                        <label class="col-sm-3 col-form-label"><fmt:message key= "check.amount"/></label>
                        <div class="col-sm-5">
                            <input type="number" step="any" class="form-control" name="amount" required/>
                        </div>
                    </div>

                    <input type="hidden" name="checkId" value="${requestScope.check.id}"/>
                    <button class="btn btn-outline-secondary btn-sm" type="submit"><fmt:message key= "check.addProductByName"/></button>
                </form>
            </div>
            <div class="col-sm">
                <form action="/app/addById/${requestScope.check.id}" method="post">
                    <div class="form-group row">
                        <label class="col-sm-3 col-form-label"><fmt:message key= "check.productId"/> </label>
                        <div class="col-sm-5">
                            <input type="number" class="form-control" name="id" required/>
                        </div>
                    </div>

                    <div class="form-group row">
                        <label class="col-sm-3 col-form-label"><fmt:message key= "check.amount"/></label>
                        <div class="col-sm-5">
                            <input type="number" step="any" class="form-control" name="amount" required/>
                        </div>
                    </div>

                    <input type="hidden" name="checkId" value="${requestScope.check.id}"/>
                    <button class="btn btn-outline-secondary btn-sm" type="submit"><fmt:message key= "check.addProductById"/></button>
                </form>
            </div>

        </div>
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