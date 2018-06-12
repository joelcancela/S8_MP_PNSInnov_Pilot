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

                <div class="card">
                    <div class="card-body">
                        <h2 class="card-title">Your rules</h2>
                        <h3 class="card-subtitle mb-2 text-muted">Modify the order by drag and drop</h3>
                        <ul id="listRules" class="sortable-list">
                            <c:forEach items="${it.customRules}" var="rule">
                                <li draggable="true" class="list-group-item">
                                    <a onclick="reloadPage()" href="deleteRule?ruleSalience=${rule[0]}"
                                       style="float: right;"><i class="fas fa-times"></i></a>
                                    <h3 class="list-group-item-heading ruleName" style="color: #1C94C4;">${rule[1]}</h3>
                                    <div class="list-group-item-text">${rule[2]}</div>
                                </li>
                            </c:forEach>
                        </ul>
                        <button class="btn btn-info" onclick="updateOrderRules()">Save this order</button>
                    </div>
                </div>
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
        setTimeout(function () {
            window.location.reload();
        }, 1500);
    }

    function updateOrderRules() {
        var ruleNodes = document.getElementsByClassName("ruleName");
        var names = [];
        for (var i = 0; i < ruleNodes.length; i++) {
            names.push(ruleNodes[i].innerText.match(/"(.*?)"/)[1]);
        }
        var params = {ruleNames : names};

        method = "post";
        var form = document.createElement("form");
        form.setAttribute("method", method);
        form.setAttribute("action", "UpdateRuleOrder");
        for(var key in params) {
            if(params.hasOwnProperty(key)) {
                var hiddenField = document.createElement("input");
                hiddenField.setAttribute("type", "hidden");
                hiddenField.setAttribute("name", key);
                hiddenField.setAttribute("value", params[key]);
                form.appendChild(hiddenField);
            }
        }
        document.body.appendChild(form);
        form.submit();
    }
</script>
<link href="../css/ruleList.css" type="text/css" rel="stylesheet"/>
<script src="../js/jquery.sortable.js"></script>
<script>
    $('.sortable-list').sortable({
        onDrop: function ($item, container, _super) {
            console.log("Order changed");
            _super($item, container);
        }
    });
</script>
</html>