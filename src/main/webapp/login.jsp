<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="f" uri="http://pilot.bounouas.com/functions" %>

<jsp:include page="header.jsp">
    <jsp:param name="title" value="Log in"/>
</jsp:include>

<body>

<jsp:include page="navbar.jsp"/>

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