<%@ attribute name="tree" required="true" type="unice.polytech.si4.pnsinnov.teamm.api.OwnFile" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="ownTags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
            <c:if test="${tree.file.getTrashed() == 'true'}">
                <s>
            </c:if>
            <span><a target="_blank" href="<c:out value="${tree.file.getWebViewLink()}"/>"><c:out value="${tree}"/></a></span>
            <c:if test="${tree.file.getTrashed() == 'true'}">
                </s>
            </c:if>
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
                           data-target="#${tree.file.getId()}" aria-expanded="false" aria-controls="${tree.file.getId()}"></i>
                        <ul class="fa-ul collapse" id="${tree.file.getId()}">
                    </c:otherwise>
                </c:choose>
            </c:when>
            <c:otherwise>
                <ul class="fa-ul" id="${tree.file.getId()}">
            </c:otherwise>
        </c:choose>
            <c:forEach items="${tree.getFolders()}" var="folder">
                <li><span class="fa-li"><i class="fas fa-folder-open text-warning"></i></span><ownTags:directory tree="${folder}"/>
                </li>
            </c:forEach>
            <c:forEach items="${tree.getFiles()}" var="file">
                <li>
                <span class="fa-li">
                    <i class="fas fa-file text-success"></i>
                </span>
                    <ownTags:directory tree="${file}"/>
                    <c:if test="${file.file.getTrashed() == 'false'}">
                        <a target="_blank" class="file" href="downloadDrive?fileid=<c:out value="${file.file.getId()}"/>">
                            <i class="fas fa-download"></i>
                        </a>

                        <c:choose>
                            <c:when test="${file.file.getName().endsWith(\"-crypted\")}">
                                <a target="_blank" class="file" href="filedecryption?encryptedFileId=<c:out value="${file.file.getId()}"/>">
                                    <i class="fas fa-lock-open"></i>
                                </a>
                            </c:when>
                            <c:otherwise>
                                <a target="_blank" class="file" href="fileencryption?fileid=<c:out value="${file.file.getId()}"/>">
                                    <i class="fas fa-lock"></i>
                                </a>
                            </c:otherwise>
                        </c:choose>
                    </c:if>
                </li>
            </c:forEach>
            </ul>
    </c:if>
</c:if>
