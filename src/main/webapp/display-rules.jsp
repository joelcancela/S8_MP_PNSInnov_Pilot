<%@ page import="unice.polytech.si4.pnsinnov.teamm.drive.FileClassifier" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<jsp:include page="header.jsp">
    <jsp:param name="title" value="Display rules"/>
</jsp:include>
<body>

<jsp:include page="navbar.jsp"/>

<div class="container">

    <div class="list-group">
        <c:forEach items="${it.customRules}" var="rule">
            <div class="list-group-item">
                <span class="badge badge-primary badge-pill">42</span>
                <h4 class="list-group-item-heading" style="color:blue;">${rule[0]}</h4>
                <p class="list-group-item-text">${rule[1]}</p>
            </div>
        </c:forEach>
    </div>

</div>

</body>
<!-- jQuery -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<!-- Latest compiled and minified JavaScript -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"
        integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa"
        crossorigin="anonymous"></script>
<script src="../js/rules-form.js"></script>
</html>