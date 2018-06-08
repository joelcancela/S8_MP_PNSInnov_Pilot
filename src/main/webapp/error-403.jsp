<%--
  Created by IntelliJ IDEA.
  User: galih
  Date: 6/5/18
  Time: 11:52 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:include page="header.jsp">
    <jsp:param name="title" value="Error 403"/>
</jsp:include>

<body>

    <jsp:include page="navbar.jsp"/>

    <div class="container">
        <div class="col-lg-8 col-lg-offset-2 text-center">
            <div class="logo">
                <h1>403</h1>
            </div>
            <p class="lead text-muted">Oops, an error has occurred. Forbidden!</p>
            <div class="clearfix"></div>
            <div class="col-lg-6 col-lg-offset-3">
                <form action="index.html">
                    <div class="input-group">
                        <input type="text" placeholder="search ..." class="form-control">
                        <span class="input-group-btn">
              <button class="btn btn-default" type="button"><i class="fa fa-search"></i></button>
            </span>
                    </div>
                </form>
            </div>
            <div class="clearfix"></div>
            <div class="sr-only">
                &nbsp;

            </div>
            <br>
            <div class="col-lg-6 col-lg-offset-3">
                <div class="btn-group btn-group-justified">
                    <a href="/Pilot" class="btn btn-info">Return Home</a>
                </div>
            </div>
        </div>
        <!-- /.col-lg-8 col-offset-2 -->
    </div>
</body>
<!-- jQuery -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<!-- Latest compiled and minified JavaScript -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"
        integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa"
        crossorigin="anonymous"></script>
</html>
