<%@ attribute name="tree" required="true" type="unice.polytech.si4.pnsinnov.teamm.api.OwnFile" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="ownTags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script>
    function modifyIcon(element) {
        console.log("Entering Method "+element);
        if (element.classList.contains("fa-plus")) {
            console.log("fa-plus " + event);
            element.classList.remove("fa-plus");
            element.classList.add("fa-minus");
        } else if (element.classList.contains("fa-minus")) {
            console.log("fa-minus " + element);
            element.classList.add("fa-plus");
            element.classList.remove("fa-minus");
        }
    }
</script>
<c:if test="${!empty tree}">
    <c:choose>
        <c:when test="${tree.getFile().getName() == \"Drive Root\"}">
            <span class="fileName"><i class="fas fa-server"></i><a
                    href="<c:out value="${tree.file.getWebViewLink()}"/>"> <c:out
                    value="${tree}"/></a></span>
        </c:when>
        <c:otherwise>
            <span><a href="<c:out value="${tree.file.getWebViewLink()}"/>"><c:out value="${tree}"/></a></span>
        </c:otherwise>
    </c:choose>
    <c:if test="${!tree.getFolders().isEmpty() || !tree.getFiles().isEmpty()}">
        <i class="icon-collapse fas fa-plus" onclick="modifyIcon(this);" data-toggle="collapse"
           data-target="#${tree.file.getId()}" aria-expanded="false" aria-controls="${tree.file.getId()}"></i>
        <ul class="fa-ul collapse" id="${tree.file.getId()}">
            <c:forEach items="${tree.getFolders()}" var="folder">
                <li><span class="fa-li"><i class="fas fa-folder-open"></i></span><ownTags:directory tree="${folder}"/>
                </li>
            </c:forEach>
            <c:forEach items="${tree.getFiles()}" var="file">
                <li>
                <span class="fa-li">
                    <i class="fas fa-file"></i>
                </span>
                    <ownTags:directory tree="${file}"/>
                    <a class="file" href="downloadDrive?fileid=<c:out value="${file.file.getId()}"/>">
                        <i class="fas fa-download"></i>
                    </a>
                </li>
            </c:forEach>
        </ul>
    </c:if>
</c:if>
