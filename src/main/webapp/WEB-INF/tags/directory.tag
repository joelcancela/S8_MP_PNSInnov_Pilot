<%@ attribute name="tree" required="true" type="unice.polytech.si4.pnsinnov.teamm.api.OwnFile" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="ownTags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:if test="${!empty tree}">
    <c:choose>
        <c:when test="${tree.getFile().getName() == \"Drive Root\"}">
            <span class="fileName"><i class="fas fa-server"></i><a href="<c:out value="${tree.file.getWebViewLink()}"/>"> <c:out
                    value="${tree}"/></a></span>
        </c:when>
        <c:otherwise>
            <span><a href="<c:out value="${tree.file.getWebViewLink()}"/>"><c:out value="${tree}"/></a></span>
        </c:otherwise>
    </c:choose>
    <c:if test="${!tree.getFolders().isEmpty() || !tree.getFiles().isEmpty()}">
        <ul class="fa-ul">
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
                    <a class="file" href="fileencryption?fileid=<c:out value="${file.file.getId()}"/>">
                        <i class="fas fa-lock"></i>
                    </a>
                </li>
            </c:forEach>
        </ul>
    </c:if>
</c:if>
