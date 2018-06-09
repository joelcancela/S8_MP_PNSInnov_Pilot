<%@ attribute name="tree" required="true" type="unice.polytech.si4.pnsinnov.teamm.drive.FileRepresentation" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="ownTags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fnP" uri="http://privatememo.bounouas.com/functions" %>
<%--<%@ tag import="java.util.regex.Pattern" %>--%>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script>
    function modifyIcon(element) {
        if (element.classList.contains("fa-plus")) {
            element.classList.remove("fa-plus");
            element.classList.add("fa-minus");
        } else if (element.classList.contains("fa-minus")) {
            element.classList.add("fa-plus");
            element.classList.remove("fa-minus");
        }
    }
</script>
<c:if test="${!empty tree}">
    <c:choose>
        <c:when test="${tree.getFile().getName().equals(\"Drive Root\")}">
            <span class="fileName"><i class="fas fa-server"></i><a target="_blank"
                                                                   href="<c:out value="https://drive.google.com/drive/my-drive"/>">
                <c:out value="${tree}"/></a></span>
        </c:when>
        <c:otherwise>
        </c:otherwise>
    </c:choose>
    <c:if test="${!tree.getFolders().isEmpty() || !tree.getFiles().isEmpty()}">
        <c:choose>
            <c:when test="${!tree.getFile().getName().equals(\"Drive Root\")}">
                <c:choose>
                    <c:when test="${tree.file.getTrashed() == 'true'}">
                        <ul class="fa-ul collapse" id="${tree.file.getId()}">
                    </c:when>
                    <c:otherwise>
                        <i class="icon-collapse fas fa-plus" onclick="modifyIcon(this);" data-toggle="collapse"
                           data-target="#${tree.file.getId()}" aria-expanded="false"
                           aria-controls="${tree.file.getId()}"></i>
                        <ul class="fa-ul collapse" id="${tree.file.getId()}">
                    </c:otherwise>
                </c:choose>
            </c:when>
            <c:otherwise>
                <ul class="fa-ul" id="${tree.file.getId()}">
            </c:otherwise>
        </c:choose>
        <c:forEach items="${tree.getFolders()}" var="folder">
            <c:if test="${folder.file.getTrashed() == 'false'}">
                <li><span class="fa-li"><i class="fas fa-folder-open
                text-warning"></i></span><a target="_blank" href="<c:out value="${folder.file.getWebViewLink()}"/>">
                    <c:out
                            value="${folder}"/></a><ownTags:directory
                        tree="${folder}"/>
                </li>
            </c:if>
        </c:forEach>
        <c:forEach items="${tree.getFiles()}" var="file">
            <ownTags:directory tree="${file}"/>
            <c:if test="${file.file.getTrashed() == 'false'}">
                <li>
                <span class="fa-li">
                    <i class="fas fa-file text-success"></i>
                </span>
                    <span><a target="_blank" href="<c:out value="${file.file.getWebViewLink()}"/>"><c:out
                            value="${file}"/></a></span>
                    <a target="_blank" class="file" href="downloadDrive?fileid=<c:out value="${file.file.getId()}"/>">
                        <i class="fas fa-download"></i>
                    </a>

                    <c:choose>
                        <c:when test="${fnP:isFileCrypted(file)}">
                            <a target="_blank" class="file"
                               href="filedecryption?encryptedFileId=<c:out value="${file.file.getId()}"/>">
                                <i class="fas fa-lock-open"></i>
                            </a>
                        </c:when>
                        <c:otherwise>
                            <a target="_blank" class="file"
                               href="fileencryption?fileid=<c:out value="${file.file.getId()}"/>">
                                <i class="fas fa-lock"></i>
                            </a>
                        </c:otherwise>
                    </c:choose>
                    <a class="file"
                       onclick="deleteConfirm('<c:out value="${file.file.getId()}"/>','<c:out
                               value="${file.file.getName()}"/>')"> <i
                            class="fas fa-times"></i>
                    </a>
                </li>
            </c:if>
        </c:forEach>
        </ul>
    </c:if>
</c:if>
