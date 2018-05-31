<%@ attribute name="tree" required="true" type="unice.polytech.si4.pnsinnov.teamm.api.OwnFile" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="ownTags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:if test="${!empty tree}">
    <c:choose>
        <c:when test="${tree.getFile().getName() == \"Drive Root\"}">
            <p><i class="fas fa-server"></i><a href="<c:out value="${tree.file.getWebViewLink()}"/>"> <c:out value="${tree}" /></a></p>
        </c:when>
        <c:otherwise>
            <p><a href="<c:out value="${tree.file.getWebViewLink()}"/>"><c:out value="${tree}" /></a></p>
        </c:otherwise>
    </c:choose>
    <ul class="fa-ul">
        <c:forEach items="${tree.getFolders()}" var="folder">
            <li><span class="fa-li"><i class="fas fa-folder-open"></i></span><ownTags:directory tree="${folder}" /></li>
        </c:forEach>
        <c:forEach items="${tree.getFiles()}" var="file">
            <li><span class="fa-li"><i class="fas fa-file"></i></span><ownTags:directory tree="${file}" /></li>
        </c:forEach>
    </ul>
</c:if>
