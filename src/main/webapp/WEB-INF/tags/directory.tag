<%@ attribute name="tree" required="true" type="unice.polytech.si4.pnsinnov.teamm.api.OwnFile" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="ownTags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:if test="${!empty tree}">
    <p><a href="<c:out value="${tree.file.getWebViewLink()}"/>"><c:out value="${tree}" /></a></p>
    <ul>
        <c:forEach items="${tree.getFolders()}" var="folder">
            <li><ownTags:directory tree="${folder}" /></li>
        </c:forEach>
        <c:forEach items="${tree.getFiles()}" var="file">
            <li><ownTags:directory tree="${file}" /></li>
        </c:forEach>
    </ul>
</c:if>
