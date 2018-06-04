<%@ page contentType="text/html;charset=UTF-8" %>
<!doctype html>

<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Create rules</title>
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
            <a class="navbar-brand" href="#">Create a rule</a>
        </div>
    </div>
</nav>

<div class="container">

    <form action="api/CreateRule" method="post">

        <div class="panel panel-default form-group">
            <div class="btn-group btn-group-toggle btn-group-justified" data-toggle="buttons">
                <label class="btn btn-info active" id="extensionLabel" onclick="displayDiv('extensionLabel')">
                    <input type="radio" name="options" id="extensionButton" autocomplete="off" checked> Extension
                </label>
                <label class="btn btn-info" id="mimeLabel" onclick="displayDiv('mimeLabel')">
                    <input type="radio" name="options" id="mimeButton" autocomplete="off"> MIME type
                </label>
                <label class="btn btn-info" id="patternLabel" onclick="displayDiv('patternLabel')">
                    <input type="radio" name="options" id="patternButton" autocomplete="off"> Pattern
                </label>
            </div>
        </div>

        <div class="panel panel-default form-group" id="extension-panel">
            <div class="input-group">
                <span class="input-group-addon">Extension to filter :</span>
                <input type="text" class="form-control" id="extension" aria-describedby="basic-addon3">
            </div>
        </div>

        <div class="panel panel-default form-group" id="mime-panel" hidden>
            <div class="panel-heading">
                Choose a MIME type :
            </div>
            <div class="panel-body">
                <div class="list-group">
                    <button type="button" class="list-group-item btn btn-light">text/plain</button>
                    <button type="button" class="list-group-item btn btn-light">application/pdf</button>
                    <button type="button" class="list-group-item btn btn-light">image/gif</button>
                </div>
            </div>
        </div>

        <div class="panel panel-default form-group" id="pattern-panel" hidden>
            <div class="input-group">
                <span class="input-group-addon">Regular expression :</span>
                <input type="text" class="form-control" id="regex" aria-describedby="basic-addon3">
            </div>
        </div>

        <div class="panel panel-default form-group" id="directory-panel">
            <div class="input-group">
                <span class="input-group-addon">Directory path :</span>
                <input type="text" class="form-control" id="destination-dir" aria-describedby="basic-addon3">
            </div>
        </div>

        <input type="text" id="bonjj"/>
        <input type="submit" value="Submit" class="btn btn-default"/>

    </form>

</div>

</body>
<!-- jQuery -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<!-- Latest compiled and minified JavaScript -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"
        integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa"
        crossorigin="anonymous"></script>
<script src="js/rules-form.js"></script>
</html>
