<%--
  Created by IntelliJ IDEA.
  User: galih
  Date: 6/12/18
  Time: 6:55 PM
  To change this template use File | Settings | File Templates.
--%>
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
            <a class="navbar-brand" href="/Pilot">
                <img style="margin-top: -1em;" class="navbar-left" src="<%=request.getContextPath()%>/img/logo.png" alt="Logo" width="50px"
                     height="50px">
                Welcome to Pilot !
            </a>
        </div>

        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <ul class="nav navbar-nav navbar-right">
                <li>
                    <a href="drive-list?drive=<%= request.getParameter("drive")%>"><i class="fas fa-cloud"></i> My Drive</a>
                </li>
                <li>
                    <a href="#" data-toggle="modal" data-target="#importModal"><i class="fas fa-upload"></i> Import file</a>
                </li>
                <li>
                    <a href="generateKey"><i class="fas fa-lock"></i> Get encryption key</a>
                </li>
                <li>
                    <a href="ruleCreation?drive=<%= request.getParameter("drive")%>"><i class="fas fa-edit"></i> Define
                        rules</a>
                </li>
                <li>
                    <a href="displayRules?drive=<%= request.getParameter("drive")%>"><i class="far fa-eye"></i> Display
                        my rules</a>
                </li>
                <li>
                    <a href="${pageContext.request.contextPath}/api/logout"><i class="fas fa-sign-out-alt"></i> Log out</a>
                </li>
            </ul>
        </div><!-- /.navbar-collapse -->
    </div>
</nav>