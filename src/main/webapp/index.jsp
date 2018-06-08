<%@ page import="unice.polytech.si4.pnsinnov.teamm.api.Login" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="f" uri="http://pilot.bounouas.com/functions" %>

<jsp:include page="header.jsp">
    <jsp:param name="title" value="Private Memo"/>
</jsp:include>

<body>

<jsp:include page="navbar.jsp"/>

<div class="container">
    <div class="row">
        <div class="col-sm-4 text-center">
            <a href="api/login?drive=google" class="btn btn-primary btn-lg active" role="button"><i
                    class="fab fa-google-drive"></i>
                Connect to Google Drive</a>
        </div>
        <div class="col-sm-4 text-center">
            <a href="" class="btn btn-primary btn-lg active" role="button"><i class="fab fa-dropbox"></i>
                Connect to Dropbox</a>
        </div>
        <div class="col-sm-4 text-center">
            <a href="" class="btn btn-primary btn-lg active" role="button"><i class="fas fa-cloud"></i>
                Connect to One Drive</a>
        </div>
    </div>
    <div class="row">
        <div style="margin-top: 30px;" class="panel panel-success">
            <div class="panel-heading">Users available :</div>
            <div class="panel-body">
                <ul class="list-group">
                    <%
                        for (String user : Login.getAvailableUsers()) {
                    %>
                    <li class="list-group-item"><%=user%></li>
                    <%
                        }
                    %>
                </ul>
            </div>
        </div>
    </div>
</div>
</body>
<!-- jQuery -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<!-- Latest compiled and minified JavaScript -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"
        integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa"
        crossorigin="anonymous"></script>
</html>