<%@ page import="unice.polytech.si4.pnsinnov.teamm.drools.ProxyGoogleDrive" %>
<%@ page import="com.google.api.services.drive.model.File" %>
<%@ page import="java.util.List" %>
<html>
<head>
    <title>Drools rules</title>
</head>
<body>
    <%new ProxyGoogleDrive().applyRules(((List<File>) request.getAttribute("list")));%>
</body>
</html>
