<%@ page import="unice.polytech.si4.pnsinnov.teamm.api.Login" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="f" uri="http://privatememo.bounouas.com/functions" %>

<jsp:include page="header.jsp">
    <jsp:param name="title" value="Private Memo"/>
</jsp:include>

<body>
<nav class="navbar navbar-default">
    <div class="container-fluid">
        <!-- Brand and toggle get grouped for better mobile display -->
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse"
                    data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="#">Welcome to PrivateMemo !</a>
        </div>

        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <ul class="nav navbar-nav navbar-right">
                <li>
                    <a href="${pageContext.request.contextPath}/login.jsp">
                        <i class="fas fa-sign-in-alt"></i> Log In
                    </a>
                </li>
            </ul>
        </div><!-- /.navbar-collapse -->
    </div>
</nav>
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