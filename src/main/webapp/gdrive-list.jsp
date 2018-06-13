<%@ taglib tagdir="/WEB-INF/tags" prefix="ownTags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="header.jsp">
    <jsp:param name="title" value="Google Drive Files List"/>
</jsp:include>
<body>

<jsp:include page="navbar.jsp"/>

<c:if test="${it.success != null}">
    <div class="alert alert-success">
        <strong>Success !</strong> <c:out value="${it.success}"/>.
    </div>
</c:if>

<c:if test="${it.error != null}">
    <div class="alert alert-danger">
        <strong>Error !</strong> <c:out value="${it.error}"/>.
    </div>
</c:if>

<c:if test="${it.key != null}">
    <div class="alert alert-info">
        <strong>Info !</strong> Your encryption key is : <c:out value="${it.key}"/>
    </div>
</c:if>

<div class="container">
    <form action="drools" method="post" style="margin: 0px;display:inline;">
        <input type="submit" value="Classify" class="btn btn-success" data-backdrop="static" data-toggle="modal" data-target="#ownModal">
    </form>
    <form action="drools-simulate" method="post" style="margin: 0px;display:inline;">
        <input type="submit" value="Preview" class="btn btn-info" data-backdrop="static" data-toggle="modal" data-target="#ownModal">
    </form>

    <div class="modal fade" id="ownModal" tabindex="-1" role="dialog" aria-labelledby="ownModalLabel">
        <div class="modal-dialog modal-sm" role="document">
            <div class="modal-content">
                <%--<div class="modal-header"></div>--%>
                <div class="modal-body text-center">
                    <img src="../img/12-64.png" class="rotating" id="spinner"/><br>
                    <span>Applying your rules...</span><br>
                    <span>Please wait</span>
                </div>
                <%--<div class="modal-footer"></div>--%>
            </div>
        </div>
    </div>

    <div class="modal fade" id="importModal" tabindex="-1" role="dialog" aria-labelledby="importModalLabel">
        <div class="modal-dialog modal-sm" role="document">
            <div class="modal-content">
                <form action="upload" method="post" enctype="multipart/form-data">
                    <div class="modal-header"><h4>Upload file</h4></div>
                    <div class="modal-body">
                        <div>
                            <label for="inputFile">File input</label>
                            <input type="file" name="inputFile" id="inputFile">
                        </div>
                        <div id="encryptDiv">
                            <input type="checkbox" name="encrypt" id="encrypt">
                            <span>Encrypt file</span>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="submit" class="btn btn-info">Import</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <div>
        <div class="listing panel panel-default" style="display: inline-block;">
            <h3 style="margin: 1em 0 1em;">My Files</h3>
            <ownTags:directory tree="${it.ownFile}" drive="gdrive"/>
        </div>
        <c:if test="${it.simulate}">
            <div class="listing panel panel-default" style="display: inline-block;">
                <h3 style="margin: 1em 0 1em;">Preview</h3>
                <ownTags:directory-simulation tree="${it.treeSimulation}" drive="gdrive"/>
            </div>
        </c:if>
    </div>
</div>
</body>
<!-- jQuery -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<!-- Latest compiled and minified JavaScript -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"
        integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa"
        crossorigin="anonymous"></script>
<script src="../js/deleteModal.js"></script>
</html>