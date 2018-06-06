<%@ taglib tagdir="/WEB-INF/tags" prefix="ownTags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!doctype html>

<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Google Drive Files List</title>
    <!-- FontAwesome -->
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.0.13/css/all.css"
          integrity="sha384-DNOHZ68U8hZfKXOrtjWvjxusGo9WQnrNx2sqG0tfsghAvtVlRW3tvkXWZh58N9jp" crossorigin="anonymous">
    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
          integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
    <!-- Optional theme -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css"
          integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous">
    <!-- Custom CSS-->
    <link rel="stylesheet" href="../css/style.css">
    <link rel="stylesheet" href="../css/rotating.css">
</head>
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
            <a class="navbar-brand" href="/PrivateMemo">Google Drive</a>
        </div>

        <!-- Collect the nav links, forms, and other content for toggling -->
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <ul class="nav navbar-nav navbar-right">
                <li><a href="#" data-toggle="modal" data-target="#importModal"><i class="fas fa-upload"></i> Import file</a>
                </li>
                <li><a href="generateKey"><i class="fas fa-lock"></i> Get encryption key</a></li>
                <li><a href="ruleCreation"><i class="fas fa-edit"></i> Define
                    rules</a></li>
                <li><a href="logout">
                    <i class="fas fa-sign-out-alt"></i> Log out </a></li>
            </ul>
        </div><!-- /.navbar-collapse -->
    </div><!-- /.container-fluid -->
</nav>

<c:if test="${success != null}">
    <div class="alert alert-success">
        <strong>Success !</strong> <c:out value="${success}"/>.
    </div>
</c:if>

<c:if test="${error != null}">
    <div class="alert alert-danger">
        <strong>Error !</strong> <c:out value="${error}"/>.
    </div>
</c:if>

<c:if test="${key != null}">
    <div class="alert alert-info">
        <strong>Info !</strong> Your encryption key is : <c:out value="${key}"/>
    </div>
</c:if>

<div class="container">
    <form action="drools" method="post" style="margin: 0px;">
        <input type="submit" value="Classify" class="btn btn-success" data-backdrop="static" data-toggle="modal"
               data-target="#ownModal">
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
                <div class="modal-header"><h4>Import file</h4></div>
                <div class="modal-body">
                    <form>
                        <div>
                            <label for="inputFile">File input</label>
                            <input type="file" id="inputFile">
                        </div>
                        <div id="encryptDiv">
                            <input type="checkbox" id="encrypt">
                            <span>Encrypt file</span>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="submit" class="btn btn-info">Import</button>
                </div>
            </div>
        </div>
    </div>


    <h3 style="margin: 1em 0 1em;">Files</h3>
    <ownTags:directory tree="${ownFile}"/>
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