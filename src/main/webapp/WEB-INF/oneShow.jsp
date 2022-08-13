<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

  <!-- c:out ; c:forEach ; c:if -->
  <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <!-- Formatting (like dates) -->
    <%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
      <!-- form:form -->
      <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
        <!-- for rendering errors on PUT routes -->
        <%@ page isErrorPage="true" %>
          <!DOCTYPE html>
          <html>

          <head>
            <meta charset="UTF-8">
            <title>Title Here</title>
            <!-- Bootstrap -->
            <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet"
              integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC"
              crossorigin="anonymous">

          </head>

          <body>
            <div class="container">
              <!-- Beginning of Container -->

              <a class="btn btn-primary" href="/dashboard">Back to Dashboard</a>


              <h1> ${ tvShow.name} </h1>
              <h2> Created By:
                <c:out value="${tvShow.user.userName}" />
              </h2>
              <p>${ tvShow.network} </p>
              <p> ${ tvShow.description}</p>
              <br>

              <c:choose>
                <c:when test="${userID == tvShow.user.id}">

                  <a class="btn btn-secondary" href="/updateShow/${tvShow.id}">Edit</a>
                  <a class="btn btn-danger" href="/delete/${tvShow.id}">Delete</a>

                </c:when>
              </c:choose>

            </div> <!-- End of Container -->
          </body>

          </html>