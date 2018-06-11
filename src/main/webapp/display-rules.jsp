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
        <c:choose>
            <c:when test="${it.customRules.size() == 0}">
                <div class="alert alert-info" role="alert">No custom rule found.</div>
            </c:when>
            <c:otherwise>
                <c:forEach items="${it.customRules}" var="rule">
                    <div class="list-group-item">
                        <p>
                            <a onclick="reloadPage()" href="deleteRule?ruleSalience=${rule[0]}" style="float: right;"><i class="fas fa-times"></i></a>
                            <span class="badge badge-primary badge-pill" style="float: left;">${rule[0]}</span>
                        </p>
                        <h3 class="list-group-item-heading" style="color:dimgrey;">${rule[1]}</h3>
                        <div class="list-group-item-text">${rule[2]}</div>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
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

<script>
    function reloadPage() {
        setTimeout(function(){window.location.reload();}, 1500);
    }
</script>
</html>