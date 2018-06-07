<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="f" uri="http://privatememo.bounouas.com/functions" %>

<jsp:include page="header.jsp">
    <jsp:param name="title" value="Log in"/>
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
            <a class="navbar-brand" href="#">Log In</a>
        </div>
    </div>
</nav>
<div class="container">
    <form action="api/login" method="post">
        <div class="form-group">
            <label for="username">Username :</label>
            <input type="text" id="username" name="username">
        </div>
        <div class="form-group">
            <label for="password">Password :</label>
            <input type="password" id="password" name="password">
        </div>
        <button type="submit" class="btn btn-primary">Log In</button>
    </form>
</div>
</body>
<!-- jQuery -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<!-- Latest compiled and minified JavaScript -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"
        integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa"
        crossorigin="anonymous"></script>
</html>