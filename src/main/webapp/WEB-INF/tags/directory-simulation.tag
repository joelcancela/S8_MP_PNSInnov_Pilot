<%@ attribute name="tree" required="true" type="unice.polytech.si4.pnsinnov.teamm.drive.FileRepresentation" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="ownTags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fnP" uri="http://pilot.bounouas.com/functions" %>
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
    <c:if test="${tree.getFile().getName().equals(\"Drive Root\")}">
            <span class="fileName"><i class="fas fa-server"></i>
                <c:out value="${tree}"/></span>
    </c:if>
    <c:if test="${!tree.getFolders().isEmpty() || !tree.getFiles().isEmpty()}">
        <c:choose>
            <c:when test="${!tree.getFile().getName().equals(\"Drive Root\")}">
                <i class="icon-collapse fas fa-plus" onclick="modifyIcon(this);" data-toggle="collapse"
                   data-target="#sim-${tree.file.getId()}" aria-expanded="false"
                   aria-controls="sim-${tree.file.getId()}"></i>
                <ul class="fa-ul collapse" id="sim-${tree.file.getId()}">
            </c:when>
            <c:otherwise>
                <ul class="fa-ul" id="sim-${tree.file.getId()}">
            </c:otherwise>
        </c:choose>
        <c:forEach items="${tree.getFolders()}" var="folder">
            <li><span class="fa-li"><i class="fas fa-folder-open
                text-warning"></i></span><c:out value="${folder}"/><ownTags:directory-simulation tree="${folder}"/>
            </li>
        </c:forEach>
        <c:forEach items="${tree.getFiles()}" var="file">
            <ownTags:directory-simulation tree="${file}"/>
            <li>
                <span class="fa-li">
                    <i class="fas fa-file text-success"></i>
                </span>
                <span><c:out value="${file}"/></span>
            </li>
        </c:forEach>
        </ul>
    </c:if>
</c:if>
