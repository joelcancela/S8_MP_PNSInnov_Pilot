<%@ page import="unice.polytech.si4.pnsinnov.teamm.drive.FileClassifier" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true" %>

<jsp:include page="header.jsp">
    <jsp:param name="title" value="Create rule"/>
</jsp:include>
<body>

<jsp:include page="navbar-drive.jsp"/>

<div class="alert alert-success" id="successCreateRule" role="alert" hidden>
    <strong>Success !</strong> Your rule has been successfully created.
</div>
<div class="alert alert-danger" id="failCreateRule" role="alert" hidden>
    <strong>Error !</strong> One or more field are not completed to create the rule.
</div>


<div class="container">

    <form action="CreateRule" method="post" id="formRuleCreation">

        <div class="panel panel-default form-group">
            <div class="btn-group btn-group-toggle btn-group-justified" data-toggle="buttons">
                <label class="btn btn-info active" id="extensionLabel" onclick="displayDiv('extensionLabel')">
                    <input type="radio" name="options" id="extensionButton" value="extensionButton" autocomplete="off"
                           checked> Extension
                </label>
                <label class="btn btn-info" id="mimeLabel" onclick="displayDiv('mimeLabel')">
                    <input type="radio" name="options" id="mimeButton" value="mimeButton" autocomplete="off"> MIME type
                </label>
                <label class="btn btn-info" id="patternLabel" onclick="displayDiv('patternLabel')">
                    <input type="radio" name="options" id="patternButton" value="patternButton" autocomplete="off">
                    Pattern
                </label>
            </div>
        </div>

        <div class="panel panel-default form-group" id="extension-panel">
            <div class="input-group">
                <span class="input-group-addon">Extension to filter :</span>
                <input type="text" class="form-control" name="extension" id="extension" autocomplete="off"
                       aria-describedby="basic-addon3">
            </div>
        </div>

        <div class="panel panel-default form-group" id="mime-panel" hidden>
            <div class="panel-heading">
                Choose a MIME type :
            </div>
            <div class="panel-body">
                <c:forEach items="<%=new FileClassifier().getMimeTypes()%>" var="mimeType">
                    <div class="radio" id="mimeTypes">
                        <label><input type="radio" name="mimeTypeResult" value="${mimeType}">${mimeType}</label>
                    </div>
                </c:forEach>
            </div>
        </div>

        <div class="panel panel-default form-group" id="pattern-panel" hidden>
            <div class="panel-heading">
                Choose a regular expression :
            </div>
            <div class="panel-body">
                <div class="radio">
                    <label><input type="radio" name="regexMode" value="startsWith" checked>Starts with</label>
                </div>
                <div class="radio">
                    <label><input type="radio" name="regexMode" value="endsWith">Ends with</label>
                </div>
                <div class="radio">
                    <label><input type="radio" name="regexMode" value="contains">Contains</label>
                </div>
                <div class="input-group">
                    <span class="input-group-addon">Expression to find :</span>
                    <input type="text" name="regex" class="form-control" aria-describedby="basic-addon3">
                </div>
            </div>
        </div>

        <div class="panel panel-default form-group" id="directory-panel">
            <div class="input-group">
                <span class="input-group-addon">Directory path :</span>
                <input required list="directories" name="destination-dir" id="destination-dir" class="form-control"
                       autocomplete="off" aria-describedby="basic-addon3"/>
                <datalist id="directories">
                    <c:forEach items="${it.listFolders}" var="folder">
                    <option value="${folder}">
                        </c:forEach>
                </datalist>
            </div>
        </div>

        <input type="submit" class="btn" id="submitButton">
    </form>

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
    $(document).ready(function () {
        $("#submitButton").click(function () {
            var $emptyFields = $('#formRuleCreation * :input:visible').filter(function () {
                return $.trim(this.value) === "";
            });

            if (!$emptyFields.length) {
                var $div2 = $("#successCreateRule");
                if ($div2.is(":visible")) {
                    return;
                }
                $div2.show();
                setTimeout(function () {
                    $div2.hide();
                }, 3000);
            } else {
                var $div3 = $("#failCreateRule");
                if ($div3.is(":visible")) {
                    return;
                }
                $div3.show();
                setTimeout(function () {
                    $div3.hide();
                }, 3000);
            }
        });
        var label = $("#mimeTypes").children().first();
        label.children().first().attr('checked', true);
    });
</script>
</html>
