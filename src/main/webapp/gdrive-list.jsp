<%@ page import="java.util.List" %>
<%@ page import="com.google.api.services.drive.model.File" %>
<%@ page import="unice.polytech.si4.pnsinnov.teamm.drools.ProxyGoogleDrive" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    if (request.getParameter("submit1") != null) {
%>
<jsp:forward page="drools-rules.jsp"/>
<%
        return;
    }
%>
<!doctype html>

<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Google Drive Files List</title>
    <!-- FontAwesome -->
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.0.13/css/all.css"
          integrity="sha384-DNOHZ68U8hZfKXOrtjWvjxusGo9WQnrNx2sqG0tfsghAvtVlRW3tvkXWZh58N9jp" crossorigin="anonymous">
    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
          integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
    <!-- Optional theme -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css"
          integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous">
    <!-- Custom CSS-->
    <link rel="stylesheet" href="style.css">
</head>
<body>
<div class="container">
    <h2>Google Drive</h2>
</div>
<div class="container">
    <form action="drools" method="post">
        <input type="submit" value="Classify" class="btn btn-success">
    </form>

    <h3>Files</h3>
    <table class="table">
        <tbody>
        <c:forEach items="${list}" var="item">
            <tr>
                <td>
                    <a href="<c:out value="${item.getWebViewLink()}"/>"><c:out value="${item.getName()}"/></a>
                    Link : <c:out value="${item.getId()}"/>
                    <a href="downloadDrive?filename=<c:out value="${item.getName()}"/>&filelink=<c:out value="${item.getId()}"/>">Download</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
<!-- Latest compiled and minified JavaScript -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"
        integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa"
        crossorigin="anonymous"></script>
<!-- jQuery -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
</html>