<%@ page language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:import url="/header.jsp"/>
<c:import url="form.jsp"/>
<c:choose>
  <c:when test="${fn:length(rants) > 0}">
    <div id="rants-container" class="container scrollspy">
      <c:import url="rants.jsp"/>
    </div>
  </c:when>
  <c:otherwise>
     <div id="rant-container" class="container">
       <c:import url="rant.jsp"/>
     </div>
  </c:otherwise>
</c:choose>

<c:import url="footer.jsp"/>